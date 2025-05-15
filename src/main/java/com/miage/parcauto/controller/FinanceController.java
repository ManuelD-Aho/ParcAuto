package main.java.com.miage.parcauto.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

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
 * Contrôleur JavaFX pour la gestion des finances du parc automobile.
 * Permet d'afficher le bilan, l'évolution mensuelle, les alertes, la répartition budgétaire,
 * le TCO et la rentabilité des véhicules.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class FinanceController extends BaseController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(FinanceController.class.getName());
    private final FinanceService financeService;

    @FXML
    DatePicker dateDebut;
    @FXML
    DatePicker dateFin;
    @FXML
    Button btnAfficherBilan;
    @FXML
    Label lblRecettes;
    @FXML
    Label lblDepenses;
    @FXML
    Label lblSolde;
    @FXML
    Label lblMarge;
    @FXML
    TableView<BilanMensuelRow> tableEvolution;
    @FXML
    TableColumn<BilanMensuelRow, String> colMois;
    @FXML
    TableColumn<BilanMensuelRow, String> colRecettes;
    @FXML
    TableColumn<BilanMensuelRow, String> colDepenses;
    @FXML
    TableColumn<BilanMensuelRow, String> colSolde;
    @FXML
    TableView<AlerteAssurance> tableAlertesAssurance;
    @FXML
    TableView<AlerteEntretien> tableAlertesEntretien;
    @FXML
    TableView<RepartitionRow> tableRepartition;
    @FXML
    TableView<RentabiliteVehicule> tableRentabilite;
    @FXML
    VBox tcoPane;
    @FXML
    Label lblTcoVehicule;
    @FXML
    Label lblTcoCoutTotal;
    @FXML
    Label lblTcoCoutParKm;
    @FXML
    Spinner<Integer> spinnerAnnee;
    @FXML
    Spinner<Integer> spinnerJoursAlerte;
    @FXML
    Spinner<Integer> spinnerKmEntretien;

    private ObservableList<BilanMensuelRow> evolutionData;
    private ObservableList<AlerteAssurance> alertesAssuranceData;
    private ObservableList<AlerteEntretien> alertesEntretienData;
    private ObservableList<RepartitionRow> repartitionData;
    private ObservableList<RentabiliteVehicule> rentabiliteData;

    /**
     * Constructeur par défaut.
     */
    public FinanceController() {
        this.financeService = new FinanceService();
    }

    /**
     * Constructeur pour l'injection de dépendances (tests).
     */
    public FinanceController(FinanceService financeService) {
        this.financeService = financeService;
    }

    /**
     * Initialise le contrôleur et configure les composants JavaFX.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (!checkPermissions()) {
            onPermissionDenied(btnAfficherBilan);
            return;
        }
        // Initialisation des spinners
        int currentYear = LocalDate.now().getYear();
        spinnerAnnee.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2020, currentYear + 1, currentYear));
        spinnerJoursAlerte.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 90, 30));
        spinnerKmEntretien.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1000, 50000, 20000, 1000));

        // Configuration des colonnes d'évolution mensuelle
        colMois.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().mois));
        colRecettes.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().recettes));
        colDepenses.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().depenses));
        colSolde.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().solde));

        // Chargement initial
        loadBilan();
        loadEvolutionMensuelle();
        loadAlertes();
        loadRepartition();
        loadRentabilite();
    }

    /**
     * Vérifie les permissions pour accéder à la gestion financière.
     */
    @Override
    public boolean checkPermissions() {
        return hasAnyPermission(
                Permission.ETATS_FINANCIERS,
                Permission.ETATS_FINANCIERS_PERSONNEL,
                Permission.ANALYSE_COUTS,
                Permission.CONSULTER_COMPTES_TOUS,
                Permission.CONSULTER_COMPTES_PERSONNEL
        );
    }

    /**
     * Gère le clic sur le bouton "Afficher Bilan".
     */
    @FXML
    private void handleAfficherBilan(ActionEvent event) {
        loadBilan();
    }

    /**
     * Charge et affiche le bilan financier sur la période sélectionnée.
     */
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

    /**
     * Charge et affiche l'évolution mensuelle pour l'année sélectionnée.
     */
    private void loadEvolutionMensuelle() {
        try {
            int annee = spinnerAnnee.getValue();
            Map<Month, BilanMensuel> evolution = financeService.getEvolutionMensuelle(annee);
            evolutionData = FXCollections.observableArrayList();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMMM");
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

    /**
     * Charge et affiche les alertes d'assurance et d'entretien.
     */
    private void loadAlertes() {
        try {
            int jours = spinnerJoursAlerte.getValue();
            int km = spinnerKmEntretien.getValue();
            List<AlerteAssurance> alertesAssurance = financeService.getAlertesAssurances(jours);
            List<AlerteEntretien> alertesEntretien = financeService.getAlertesEntretiens(km);
            alertesAssuranceData = FXCollections.observableArrayList(alertesAssurance);
            alertesEntretienData = FXCollections.observableArrayList(alertesEntretien);
            tableAlertesAssurance.setItems(alertesAssuranceData);
            tableAlertesEntretien.setItems(alertesEntretienData);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement des alertes", e);
            showErrorAlert("Erreur", "Impossible de charger les alertes");
        }
    }

    /**
     * Charge et affiche la répartition budgétaire pour l'année sélectionnée.
     */
    private void loadRepartition() {
        try {
            int annee = spinnerAnnee.getValue();
            Map<String, BigDecimal> repartition = financeService.getRepartitionBudgetaire(annee);
            repartitionData = FXCollections.observableArrayList();
            for (Map.Entry<String, BigDecimal> entry : repartition.entrySet()) {
                repartitionData.add(new RepartitionRow(entry.getKey(), entry.getValue().setScale(2, RoundingMode.HALF_UP) + " €"));
            }
            tableRepartition.setItems(repartitionData);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement de la répartition budgétaire", e);
            showErrorAlert("Erreur", "Impossible de charger la répartition budgétaire");
        }
    }

    /**
     * Charge et affiche la rentabilité annuelle des véhicules.
     */
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

    // --- Classes utilitaires pour l'affichage dans les TableView ---

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

    /**
     * Ligne de répartition budgétaire pour TableView.
     */
    public static class RepartitionRow {
        public final String type;
        public final String montant;
        public RepartitionRow(String type, String montant) {
            this.type = type;
            this.montant = montant;
        }
    }
}