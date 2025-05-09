package main.java.com.miage.parcauto.controller;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.com.miage.parcauto.model.mission.Mission;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;
import main.java.com.miage.parcauto.service.MissionService;
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
 * Contrôleur pour la gestion des missions.
 * Permet la consultation, la création, la modification, la suppression et la clôture des missions.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class MissionController extends BaseController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(MissionController.class.getName());

    private final MissionService missionService;
    private final VehiculeService vehiculeService;

    @FXML
    private TableView<Mission> tableMissions;
    @FXML
    private TableColumn<Mission, String> colVehicule;
    @FXML
    private TableColumn<Mission, String> colLibelle;
    @FXML
    private TableColumn<Mission, String> colSite;
    @FXML
    private TableColumn<Mission, String> colDateDebut;
    @FXML
    private TableColumn<Mission, String> colDateFin;
    @FXML
    private TableColumn<Mission, String> colStatut;
    @FXML
    private TableColumn<Mission, String> colKmPrevu;
    @FXML
    private TableColumn<Mission, String> colKmReel;

    @FXML
    private ComboBox<Vehicule> comboVehicule;
    @FXML
    private TextField txtLibelle;
    @FXML
    private TextField txtSite;
    @FXML
    private DatePicker dateDebut;
    @FXML
    private DatePicker dateFin;
    @FXML
    private TextField txtKmPrevu;
    @FXML
    private TextField txtKmReel;
    @FXML
    private ComboBox<Mission.StatusMission> comboStatut;
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

    private ObservableList<Mission> missions;
    private Mission selectedMission;

    /**
     * Constructeur par défaut.
     */
    public MissionController() {
        this.missionService = new MissionService();
        this.vehiculeService = new VehiculeService();
    }

    /**
     * Constructeur pour l'injection de dépendances (tests).
     */
    public MissionController(MissionService missionService, VehiculeService vehiculeService) {
        this.missionService = missionService;
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
                cellData.getValue().getVehicule() != null ? cellData.getValue().getVehicule().toString() : ""));
        colLibelle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLibMission()));
        colSite.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSite()));
        colDateDebut.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDateDebutMission() != null ? cellData.getValue().getDateDebutMission().toLocalDate().toString() : ""));
        colDateFin.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDateFinMission() != null ? cellData.getValue().getDateFinMission().toLocalDate().toString() : ""));
        colStatut.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getStatus() != null ? cellData.getValue().getStatus().getLibelle() : ""));
        colKmPrevu.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getKmPrevu() != null ? cellData.getValue().getKmPrevu().toString() : ""));
        colKmReel.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getKmReel() != null ? cellData.getValue().getKmReel().toString() : ""));

        // Chargement des missions et des véhicules
        loadMissions();
        loadVehicules();
        comboStatut.setItems(FXCollections.observableArrayList(Mission.StatusMission.values()));

        // Sélection d'une ligne
        tableMissions.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                selectedMission = newSel;
                showMissionDetails(selectedMission);
            } else {
                clearMissionDetails();
            }
        });
        configureButtonsVisibility();
    }

    @Override
    protected boolean checkPermissions() {
        return hasAnyPermission(
            Permission.CREER_MISSION,
            Permission.PLANIFIER_MISSION,
            Permission.DEMARRER_MISSION,
            Permission.CLOTURER_MISSION,
            Permission.ANNULER_MISSION,
            Permission.SAISIR_DEPENSES_MISSION
        );
    }

    private void configureButtonsVisibility() {
        btnAjouter.setVisible(hasPermission(Permission.CREER_MISSION));
        btnModifier.setVisible(hasPermission(Permission.PLANIFIER_MISSION));
        btnSupprimer.setVisible(hasPermission(Permission.ANNULER_MISSION));
        btnCloturer.setVisible(hasPermission(Permission.CLOTURER_MISSION));
    }

    private void loadMissions() {
        try {
            List<Mission> list = missionService.getAllMissions();
            missions = FXCollections.observableArrayList(list);
            tableMissions.setItems(missions);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement des missions", e);
            showErrorAlert("Erreur", "Impossible de charger la liste des missions");
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

    private void showMissionDetails(Mission mission) {
        if (mission != null) {
            txtLibelle.setText(mission.getLibMission());
            txtSite.setText(mission.getSite());
            txtKmPrevu.setText(mission.getKmPrevu() != null ? mission.getKmPrevu().toString() : "");
            txtKmReel.setText(mission.getKmReel() != null ? mission.getKmReel().toString() : "");
            comboStatut.setValue(mission.getStatus());
            dateDebut.setValue(mission.getDateDebutMission() != null ? mission.getDateDebutMission().toLocalDate() : null);
            dateFin.setValue(mission.getDateFinMission() != null ? mission.getDateFinMission().toLocalDate() : null);
            if (mission.getVehicule() != null) {
                Optional<Vehicule> veh = comboVehicule.getItems().stream()
                        .filter(v -> v.getIdVehicule() == mission.getVehicule().getIdVehicule())
                        .findFirst();
                veh.ifPresent(comboVehicule::setValue);
            }
            detailPane.setVisible(true);
        }
    }

    private void clearMissionDetails() {
        txtLibelle.clear();
        txtSite.clear();
        txtKmPrevu.clear();
        txtKmReel.clear();
        comboStatut.setValue(null);
        dateDebut.setValue(null);
        dateFin.setValue(null);
        comboVehicule.setValue(null);
        selectedMission = null;
    }

    @FXML
    private void handleAjouter(ActionEvent event) {
        if (!hasPermission(Permission.CREER_MISSION)) {
            showErrorAlert("Accès refusé", "Vous n'avez pas la permission d'ajouter une mission");
            return;
        }
        if (!validateInputs()) return;
        try {
            Mission mission = new Mission();
            mission.setLibMission(txtLibelle.getText());
            mission.setSite(txtSite.getText());
            mission.setIdVehicule(comboVehicule.getValue().getIdVehicule());
            mission.setDateDebutMission(dateDebut.getValue().atStartOfDay());
            mission.setDateFinMission(dateFin.getValue().atStartOfDay());
            mission.setKmPrevu(Integer.parseInt(txtKmPrevu.getText()));
            mission.setStatus(Mission.StatusMission.Planifiee);
            Mission added = missionService.createMission(mission, false);
            if (added != null) {
                missions.add(added);
                clearMissionDetails();
                showInfoAlert("Succès", "Mission ajoutée avec succès");
            } else {
                showErrorAlert("Erreur", "Impossible d'ajouter la mission");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'ajout de la mission", e);
            showErrorAlert("Erreur", "Une erreur est survenue lors de l'ajout de la mission");
        }
    }

    @FXML
    private void handleModifier(ActionEvent event) {
        if (!hasPermission(Permission.PLANIFIER_MISSION)) {
            showErrorAlert("Accès refusé", "Vous n'avez pas la permission de modifier une mission");
            return;
        }
        if (selectedMission == null) {
            showErrorAlert("Erreur", "Veuillez sélectionner une mission à modifier");
            return;
        }
        if (!validateInputs()) return;
        try {
            selectedMission.setLibMission(txtLibelle.getText());
            selectedMission.setSite(txtSite.getText());
            selectedMission.setIdVehicule(comboVehicule.getValue().getIdVehicule());
            selectedMission.setDateDebutMission(dateDebut.getValue().atStartOfDay());
            selectedMission.setDateFinMission(dateFin.getValue().atStartOfDay());
            selectedMission.setKmPrevu(Integer.parseInt(txtKmPrevu.getText()));
            boolean updated = missionService.updateMission(selectedMission, false);
            if (updated) {
                refreshTable();
                clearMissionDetails();
                showInfoAlert("Succès", "Mission modifiée avec succès");
            } else {
                showErrorAlert("Erreur", "Impossible de modifier la mission");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la modification de la mission", e);
            showErrorAlert("Erreur", "Une erreur est survenue lors de la modification de la mission");
        }
    }

    @FXML
    private void handleSupprimer(ActionEvent event) {
        if (!hasPermission(Permission.ANNULER_MISSION)) {
            showErrorAlert("Accès refusé", "Vous n'avez pas la permission de supprimer une mission");
            return;
        }
        if (selectedMission == null) {
            showErrorAlert("Erreur", "Veuillez sélectionner une mission à supprimer");
            return;
        }
        if (showConfirmationAlert("Confirmation", "Êtes-vous sûr de vouloir supprimer cette mission ?")) {
            try {
                boolean deleted = missionService.deleteMission(selectedMission.getIdMission(), false);
                if (deleted) {
                    missions.remove(selectedMission);
                    clearMissionDetails();
                    showInfoAlert("Succès", "Mission supprimée avec succès");
                } else {
                    showErrorAlert("Erreur", "Impossible de supprimer la mission");
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de la mission", e);
                showErrorAlert("Erreur", "Une erreur est survenue lors de la suppression de la mission");
            }
        }
    }

    @FXML
    private void handleCloturer(ActionEvent event) {
        if (!hasPermission(Permission.CLOTURER_MISSION)) {
            showErrorAlert("Accès refusé", "Vous n'avez pas la permission de clôturer une mission");
            return;
        }
        if (selectedMission == null) {
            showErrorAlert("Erreur", "Veuillez sélectionner une mission à clôturer");
            return;
        }
        if (showConfirmationAlert("Confirmation", "Clôturer cette mission ?")) {
            try {
                int kmReel = Integer.parseInt(txtKmReel.getText());
                boolean closed = missionService.cloturerMission(selectedMission.getIdMission(), kmReel);
                if (closed) {
                    refreshTable();
                    clearMissionDetails();
                    showInfoAlert("Succès", "Mission clôturée avec succès");
                } else {
                    showErrorAlert("Erreur", "Impossible de clôturer la mission");
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erreur lors de la clôture de la mission", e);
                showErrorAlert("Erreur", "Une erreur est survenue lors de la clôture de la mission");
            }
        }
    }

    @FXML
    private void handleRetour(ActionEvent event) {
        navigateTo(btnRetour, "/fxml/dashboard.fxml", "Gestion de Parc Automobile - Tableau de bord");
    }

    private void refreshTable() {
        loadMissions();
    }

    private boolean validateInputs() {
        StringBuilder errors = new StringBuilder();
        if (comboVehicule.getValue() == null) errors.append("Le véhicule est obligatoire\n");
        if (txtLibelle.getText().isEmpty()) errors.append("Le libellé est obligatoire\n");
        if (txtSite.getText().isEmpty()) errors.append("Le site est obligatoire\n");
        if (dateDebut.getValue() == null) errors.append("La date de début est obligatoire\n");
        if (dateFin.getValue() == null) errors.append("La date de fin est obligatoire\n");
        if (!txtKmPrevu.getText().isEmpty()) {
            try {
                int km = Integer.parseInt(txtKmPrevu.getText());
                if (km < 0) errors.append("Le kilométrage prévu ne peut pas être négatif\n");
            } catch (NumberFormatException e) {
                errors.append("Le kilométrage prévu doit être un nombre entier\n");
            }
        }
        if (!txtKmReel.getText().isEmpty()) {
            try {
                int km = Integer.parseInt(txtKmReel.getText());
                if (km < 0) errors.append("Le kilométrage réel ne peut pas être négatif\n");
            } catch (NumberFormatException e) {
                errors.append("Le kilométrage réel doit être un nombre entier\n");
            }
        }
        if (errors.length() > 0) {
            showErrorAlert("Erreurs de validation", errors.toString());
            return false;
        }
        return true;
    }
}