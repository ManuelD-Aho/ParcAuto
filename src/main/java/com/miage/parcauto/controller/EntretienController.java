package main.java.com.miage.parcauto.controller;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.com.miage.parcauto.model.entretien.Entretien;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;
import main.java.com.miage.parcauto.service.EntretienService;
import main.java.com.miage.parcauto.service.VehiculeService;
import main.java.com.miage.parcauto.util.Permission;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Contrôleur pour la gestion des entretiens.
 * Permet la consultation, la création, la modification, la suppression et la clôture des entretiens.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class EntretienController extends BaseController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(EntretienController.class.getName());

    private final EntretienService entretienService;
    private final VehiculeService vehiculeService;

    @FXML
    private TableView<Entretien> tableEntretiens;
    @FXML
    private TableColumn<Entretien, String> colVehicule;
    @FXML
    private TableColumn<Entretien, String> colType;
    @FXML
    private TableColumn<Entretien, String> colStatut;
    @FXML
    private TableColumn<Entretien, String> colDateEntree;
    @FXML
    private TableColumn<Entretien, String> colDateSortie;
    @FXML
    private TableColumn<Entretien, String> colCout;

    @FXML
    private ComboBox<Vehicule> comboVehicule;
    @FXML
    private ComboBox<Entretien.TypeEntretien> comboType;
    @FXML
    private ComboBox<Entretien.StatutOT> comboStatut;
    @FXML
    private DatePicker dateEntree;
    @FXML
    private DatePicker dateSortie;
    @FXML
    private TextField txtMotif;
    @FXML
    private TextField txtObservation;
    @FXML
    private TextField txtCout;
    @FXML
    private Button btnAjouter;
    @FXML
    private Button btnModifier;
    @FXML
    private Button btnSupprimer;
    @FXML
    private Button btnCloturer;
    @FXML
    private Button btnRetour;
    @FXML
    private VBox detailPane;

    private ObservableList<Entretien> entretiens;
    private Entretien selectedEntretien;

    /**
     * Constructeur par défaut.
     */
    public EntretienController() {
        this.entretienService = new EntretienService();
        this.vehiculeService = new VehiculeService();
    }

    /**
     * Constructeur pour l'injection de dépendances (tests).
     */
    public EntretienController(EntretienService entretienService, VehiculeService vehiculeService) {
        this.entretienService = entretienService;
        this.vehiculeService = vehiculeService;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (!checkPermissions()) {
            onPermissionDenied(btnRetour);
            return;
        }
        // Configuration des colonnes
        colVehicule.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getInfoVehicule() != null ? cellData.getValue().getInfoVehicule() : ""));
        colType.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getType() != null ? cellData.getValue().getType().name() : ""));
        colStatut.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getStatutOt() != null ? cellData.getValue().getStatutOt().name() : ""));
        colDateEntree.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDateEntreeEntr() != null ? cellData.getValue().getDateEntreeEntr().toString() : ""));
        colDateSortie.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDateSortieEntr() != null ? cellData.getValue().getDateSortieEntr().toString() : ""));
        colCout.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getCoutEntr() != null ? cellData.getValue().getCoutEntr().toPlainString() : ""));

        // Chargement des entretiens et des véhicules
        loadEntretiens();
        loadVehicules();

        // ComboBox types/statuts
        comboType.setItems(FXCollections.observableArrayList(Entretien.TypeEntretien.values()));
        comboStatut.setItems(FXCollections.observableArrayList(Entretien.StatutOT.values()));

        // Sélection d'une ligne
        tableEntretiens.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                selectedEntretien = newSel;
                showEntretienDetails(selectedEntretien);
            } else {
                clearEntretienDetails();
            }
        });

        configureButtonsVisibility();
    }

    @Override
    protected boolean checkPermissions() {
        return hasAnyPermission(
            Permission.SUIVI_ENTRETIEN_TOUS,
            Permission.SUIVI_ENTRETIEN_PERSONNEL,
            Permission.PLANIFIER_ENTRETIEN_PREVENTIF
        );
    }

    private void configureButtonsVisibility() {
        btnAjouter.setVisible(hasPermission(Permission.PLANIFIER_ENTRETIEN_PREVENTIF));
        btnModifier.setVisible(hasPermission(Permission.SAISIR_INTERVENTION));
        btnSupprimer.setVisible(hasPermission(Permission.CLOTURER_INTERVENTION));
        btnCloturer.setVisible(hasPermission(Permission.CLOTURER_INTERVENTION));
    }

    private void loadEntretiens() {
        try {
            List<Entretien> list = entretienService.getAllEntretiens();
            entretiens = FXCollections.observableArrayList(list);
            tableEntretiens.setItems(entretiens);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement des entretiens", e);
            showErrorAlert("Erreur", "Impossible de charger la liste des entretiens");
        }
    }

    private void loadVehicules() {
        try {
            List<Vehicule> vehicules = vehiculeService.getAllVehicules();
            comboVehicule.setItems(FXCollections.observableArrayList(vehicules));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement des véhicules", e);
        }
    }

    private void showEntretienDetails(Entretien entretien) {
        if (entretien != null) {
            // Remplir les champs
            txtMotif.setText(entretien.getMotifEntr());
            txtObservation.setText(entretien.getObservation());
            txtCout.setText(entretien.getCoutEntr() != null ? entretien.getCoutEntr().toPlainString() : "");
            comboType.setValue(entretien.getType());
            comboStatut.setValue(entretien.getStatutOt());
            dateEntree.setValue(entretien.getDateEntreeEntr() != null ? entretien.getDateEntreeEntr().toLocalDate() : null);
            dateSortie.setValue(entretien.getDateSortieEntr() != null ? entretien.getDateSortieEntr().toLocalDate() : null);
            // Sélectionner le véhicule dans la combo
            if (entretien.getIdVehicule() != null) {
                Optional<Vehicule> veh = comboVehicule.getItems().stream()
                        .filter(v -> v.getIdVehicule() == entretien.getIdVehicule())
                        .findFirst();
                veh.ifPresent(comboVehicule::setValue);
            }
            detailPane.setVisible(true);
        }
    }

    private void clearEntretienDetails() {
        txtMotif.clear();
        txtObservation.clear();
        txtCout.clear();
        comboType.setValue(null);
        comboStatut.setValue(null);
        dateEntree.setValue(null);
        dateSortie.setValue(null);
        comboVehicule.setValue(null);
        selectedEntretien = null;
    }

    @FXML
    private void handleAjouter(ActionEvent event) {
        if (!hasPermission(Permission.PLANIFIER_ENTRETIEN_PREVENTIF)) {
            showErrorAlert("Accès refusé", "Vous n'avez pas la permission d'ajouter un entretien");
            return;
        }
        if (!validateInputs()) return;
        try {
            Entretien entretien = new Entretien();
            entretien.setIdVehicule(comboVehicule.getValue().getIdVehicule());
            entretien.setType(comboType.getValue());
            entretien.setStatutOt(comboStatut.getValue());
            entretien.setDateEntreeEntr(dateEntree.getValue().atStartOfDay());
            entretien.setDateSortieEntr(dateSortie.getValue() != null ? dateSortie.getValue().atStartOfDay() : null);
            entretien.setMotifEntr(txtMotif.getText());
            entretien.setObservation(txtObservation.getText());
            entretien.setCoutEntr(txtCout.getText().isEmpty() ? null : new java.math.BigDecimal(txtCout.getText()));
            Entretien added = entretienService.createEntretien(entretien, true);
            if (added != null) {
                entretiens.add(added);
                clearEntretienDetails();
                showInfoAlert("Succès", "Entretien ajouté avec succès");
            } else {
                showErrorAlert("Erreur", "Impossible d'ajouter l'entretien");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'ajout de l'entretien", e);
            showErrorAlert("Erreur", "Une erreur est survenue lors de l'ajout de l'entretien");
        }
    }

    @FXML
    private void handleModifier(ActionEvent event) {
        if (!hasPermission(Permission.SAISIR_INTERVENTION)) {
            showErrorAlert("Accès refusé", "Vous n'avez pas la permission de modifier un entretien");
            return;
        }
        if (selectedEntretien == null) {
            showErrorAlert("Erreur", "Veuillez sélectionner un entretien à modifier");
            return;
        }
        if (!validateInputs()) return;
        try {
            selectedEntretien.setIdVehicule(comboVehicule.getValue().getIdVehicule());
            selectedEntretien.setType(comboType.getValue());
            selectedEntretien.setStatutOt(comboStatut.getValue());
            selectedEntretien.setDateEntreeEntr(dateEntree.getValue().atStartOfDay());
            selectedEntretien.setDateSortieEntr(dateSortie.getValue() != null ? dateSortie.getValue().atStartOfDay() : null);
            selectedEntretien.setMotifEntr(txtMotif.getText());
            selectedEntretien.setObservation(txtObservation.getText());
            selectedEntretien.setCoutEntr(txtCout.getText().isEmpty() ? null : new java.math.BigDecimal(txtCout.getText()));
            boolean updated = entretienService.updateEntretien(selectedEntretien);
            if (updated) {
                refreshTable();
                clearEntretienDetails();
                showInfoAlert("Succès", "Entretien modifié avec succès");
            } else {
                showErrorAlert("Erreur", "Impossible de modifier l'entretien");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la modification de l'entretien", e);
            showErrorAlert("Erreur", "Une erreur est survenue lors de la modification de l'entretien");
        }
    }

    @FXML
    private void handleSupprimer(ActionEvent event) {
        if (!hasPermission(Permission.CLOTURER_INTERVENTION)) {
            showErrorAlert("Accès refusé", "Vous n'avez pas la permission de supprimer un entretien");
            return;
        }
        if (selectedEntretien == null) {
            showErrorAlert("Erreur", "Veuillez sélectionner un entretien à supprimer");
            return;
        }
        if (showConfirmationAlert("Confirmation", "Êtes-vous sûr de vouloir supprimer cet entretien ?")) {
            try {
                boolean deleted = entretienService.deleteEntretien(selectedEntretien.getIdEntretien());
                if (deleted) {
                    entretiens.remove(selectedEntretien);
                    clearEntretienDetails();
                    showInfoAlert("Succès", "Entretien supprimé avec succès");
                } else {
                    showErrorAlert("Erreur", "Impossible de supprimer l'entretien");
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de l'entretien", e);
                showErrorAlert("Erreur", "Une erreur est survenue lors de la suppression de l'entretien");
            }
        }
    }

    @FXML
    private void handleCloturer(ActionEvent event) {
        if (!hasPermission(Permission.CLOTURER_INTERVENTION)) {
            showErrorAlert("Accès refusé", "Vous n'avez pas la permission de clôturer un entretien");
            return;
        }
        if (selectedEntretien == null) {
            showErrorAlert("Erreur", "Veuillez sélectionner un entretien à clôturer");
            return;
        }
        if (showConfirmationAlert("Confirmation", "Clôturer cet entretien ?")) {
            try {
                boolean closed = entretienService.cloturerEntretien(selectedEntretien.getIdEntretien());
                if (closed) {
                    refreshTable();
                    clearEntretienDetails();
                    showInfoAlert("Succès", "Entretien clôturé avec succès");
                } else {
                    showErrorAlert("Erreur", "Impossible de clôturer l'entretien");
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erreur lors de la clôture de l'entretien", e);
                showErrorAlert("Erreur", "Une erreur est survenue lors de la clôture de l'entretien");
            }
        }
    }

    @FXML
    private void handleRetour(ActionEvent event) {
        navigateTo(btnRetour, "/fxml/dashboard.fxml", "Gestion de Parc Automobile - Tableau de bord");
    }

    private void refreshTable() {
        loadEntretiens();
    }

    private boolean validateInputs() {
        StringBuilder errors = new StringBuilder();
        if (comboVehicule.getValue() == null) errors.append("Le véhicule est obligatoire\n");
        if (comboType.getValue() == null) errors.append("Le type d'entretien est obligatoire\n");
        if (comboStatut.getValue() == null) errors.append("Le statut est obligatoire\n");
        if (dateEntree.getValue() == null) errors.append("La date d'entrée est obligatoire\n");
        if (!txtCout.getText().isEmpty()) {
            try {
                new java.math.BigDecimal(txtCout.getText());
            } catch (NumberFormatException e) {
                errors.append("Le coût doit être un nombre valide\n");
            }
        }
        if (errors.length() > 0) {
            showErrorAlert("Erreurs de validation", errors.toString());
            return false;
        }
        return true;
    }
}