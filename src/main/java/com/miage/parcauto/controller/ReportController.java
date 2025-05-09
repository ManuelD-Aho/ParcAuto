package main.java.com.miage.parcauto.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.com.miage.parcauto.dao.FinanceDao.BilanFinancier;
import main.java.com.miage.parcauto.dao.FinanceDao.BilanMensuel;
import main.java.com.miage.parcauto.dao.FinanceDao.RentabiliteVehicule;
import main.java.com.miage.parcauto.service.FinanceService;
import main.java.com.miage.parcauto.util.Permission;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

/**
 * Contrôleur pour l'affichage des rapports et statistiques avancées.
 * Permet de consulter le bilan financier, l'évolution mensuelle, le TCO et la rentabilité des véhicules.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class ReportController extends BaseController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(ReportController.class.getName());
    private final FinanceService financeService;

    @FXML
    private DatePicker dateDebut;
    @FXML
    private DatePicker dateFin;
    @FXML
    private Button btnAfficherBilan;
    @FXML
    private Label lblRecettes;
    @FXML
    private Label lblDepenses;
    @FXML
    private Label lblSolde;
    @FXML
    private Label lblMarge;
    @FXML
    private TableView<BilanMensuelRow> tableEvolution;
    @FXML
    private TableColumn<BilanMensuelRow, String> colMois;
    @FXML
    private TableColumn<BilanMensuelRow, String> colRecettes;
    @FXML
    private TableColumn<BilanMensuelRow, String> colDepenses;
    @FXML
    private TableColumn<BilanMensuelRow, String> colSolde;
    @FXML
    private TableView<RentabiliteVehicule> tableRentabilite;
    @FXML
    private VBox tcoPane;
    @FXML
    private Label lblTcoVehicule;
    @FXML
    private Label lblTcoCoutTotal;
    @FXML
    private Label lblTcoCoutParKm;
    @FXML
    private Spinner<Integer> spinnerAnnee;

    private ObservableList<BilanMensuelRow> evolutionData;
    private ObservableList<RentabiliteVehicule> rentabiliteData;

    /**
     * Constructeur par défaut.
     */
    public ReportController() {
        this.financeService = new FinanceService();
    }

    /**
     * Constructeur pour l'injection de dépendances (tests).
     */
    public ReportController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (!checkPermissions()) {
            onPermissionDenied(btnAfficherBilan);
            return;
        }
        int currentYear = LocalDate.now().getYear();
        spinnerAnnee.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2020, currentYear + 1, currentYear));
        colMois.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().mois));
        colRecettes.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().recettes));
        colDepenses.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().depenses));
        colSolde.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().solde));
        loadBilan();
        loadEvolutionMensuelle();
        loadRentabilite();
    }

    @Override
    protected boolean checkPermissions() {
        return hasAnyPermission(
                Permission.ETATS_FINANCIERS,
                Permission.ETATS_FINANCIERS_PERSONNEL,
                Permission.ANALYSE_COUTS,
                Permission.CONSULTER_COMPTES_TOUS,
                Permission.CONSULTER_COMPTES_PERSONNEL
        );
    }

    @FXML
    private void handleAfficherBilan(ActionEvent event) {
        loadBilan();
    }

    private void loadBilan() {
        try {
            LocalDate debut = dateDebut.getValue() != null ? dateDebut.getValue() : LocalDate.now().withDayOfYear(1);
            LocalDate fin = dateFin.getValue() != null ? dateFin.getValue() : LocalDate.now();
            BilanFinancier bilan = financeService.getBilanFinancier(debut.atStartOfDay(), fin.atTime(23,59,59));
            lblRecettes.setText(bilan.getTotalRecettes().toPlainString() + " €");
            lblDepenses.setText(bilan.getTotalDepenses().toPlainString() + " €");
            lblSolde.setText(bilan.getSolde().toPlainString() + " €");
            lblMarge.setText(bilan.getMargePct().setScale(2, RoundingMode.HALF_UP) + " %");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement du bilan financier", e);
            showErrorAlert("Erreur", "Impossible de charger le bilan financier");
        }
    }

    private void loadEvolutionMensuelle() {
        try {
            int annee = spinnerAnnee.getValue();
            Map<Month, BilanMensuel> evolution = financeService.getEvolutionMensuelle(annee);
            evolutionData = FXCollections.observableArrayList();
            for (Month m : Month.values()) {
                BilanMensuel b = evolution.get(m);
                evolutionData.add(new BilanMensuelRow(
                        m.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.FRENCH),
                        b.getRecettes().setScale(2, RoundingMode.HALF_UP) + " €",
                        b.getDepenses().setScale(2, RoundingMode.HALF_UP) + " €",
                        b.getSolde().setScale(2, RoundingMode.HALF_UP) + " €"
                ));
            }
            tableEvolution.setItems(evolutionData);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement de l'évolution mensuelle", e);
            showErrorAlert("Erreur", "Impossible de charger l'évolution mensuelle");
        }
    }

    private void loadRentabilite() {
        try {
            int annee = spinnerAnnee.getValue();
            List<RentabiliteVehicule> rapport = financeService.getRapportRentabilite(annee);
            rentabiliteData = FXCollections.observableArrayList(rapport);
            tableRentabilite.setItems(rentabiliteData);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement du rapport de rentabilité", e);
            showErrorAlert("Erreur", "Impossible de charger le rapport de rentabilité");
        }
    }

    /**
     * Ligne d'évolution mensuelle pour TableView.
     */
    public static class BilanMensuelRow {
        public final String mois;
        public final String recettes;
        public final String depenses;
        public final String solde;
        public BilanMensuelRow(String mois, String recettes, String depenses, String solde) {
            this.mois = mois;
            this.recettes = recettes;
            this.depenses = depenses;
            this.solde = solde;
        }
    }
}