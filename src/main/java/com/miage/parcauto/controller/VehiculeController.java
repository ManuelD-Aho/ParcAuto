package main.java.com.miage.parcauto.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.com.miage.parcauto.model.vehicule.EtatVoiture;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;
import main.java.com.miage.parcauto.service.VehiculeService;
import main.java.com.miage.parcauto.util.Permission;
import main.java.com.miage.parcauto.util.Validator;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

/**
 * Contrôleur pour la gestion des véhicules.
 * Gère l'affichage, l'ajout, la modification et la suppression des véhicules.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class VehiculeController extends BaseController implements Initializable {
    
    private static final Logger LOGGER = Logger.getLogger(VehiculeController.class.getName());
    
    private final VehiculeService vehiculeService;
    
    @FXML
    private TableView<Vehicule> tableVehicules;
    
    @FXML
    private TableColumn<Vehicule, String> colImmatriculation;
    
    @FXML
    private TableColumn<Vehicule, String> colMarque;
    
    @FXML
    private TableColumn<Vehicule, String> colModele;
    
    @FXML
    private TableColumn<Vehicule, Integer> colKilometrage;
    
    @FXML
    private TableColumn<Vehicule, String> colEtat;
    
    @FXML
    private TableColumn<Vehicule, String> colDateMiseEnCirculation;
    
    @FXML
    private VBox detailPane;
    
    @FXML
    private TextField txtImmatriculation;
    
    @FXML
    private TextField txtMarque;
    
    @FXML
    private TextField txtModele;
    
    @FXML
    private TextField txtKilometrage;
    
    @FXML
    private DatePicker dateMiseEnCirculation;
    
    @FXML
    private ComboBox<EtatVoiture> comboEtat;
    
    @FXML
    private Button btnAjouter;
    
    @FXML
    private Button btnModifier;
    
    @FXML
    private Button btnSupprimer;
    
    @FXML
    private Button btnDeclarer;
    
    @FXML
    private Button btnRetour;
    
    private ObservableList<Vehicule> vehicules;
    private Vehicule selectedVehicule;
    
    /**
     * Constructeur par défaut.
     */
    public VehiculeController() {
        this.vehiculeService = new VehiculeService();
    }
    
    /**
     * Constructeur avec injection de dépendance pour les tests.
     * 
     * @param vehiculeService Service de gestion des véhicules
     */
    public VehiculeController(VehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }
    
    /**
     * Initialise le contrôleur.
     * Configure les colonnes de la table et charge les données.
     * 
     * @param url URL de localisation
     * @param rb Bundle de ressources
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Vérification des permissions
        if (!checkPermissions()) {
            onPermissionDenied(btnRetour);
            return;
        }
        
        // Configuration des colonnes de la table
        colImmatriculation.setCellValueFactory(new PropertyValueFactory<>("immatriculation"));
        colMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colModele.setCellValueFactory(new PropertyValueFactory<>("modele"));
        colKilometrage.setCellValueFactory(new PropertyValueFactory<>("kmActuels"));
        colEtat.setCellValueFactory(cellData -> {
            if (cellData.getValue().getEtatVoiture() != null) {
                return new SimpleStringProperty(cellData.getValue().getEtatVoiture().getLibEtatVoiture());
            }
            return new SimpleStringProperty("Non défini");
        });
        colDateMiseEnCirculation.setCellValueFactory(cellData -> {
            if (cellData.getValue().getDateMiseEnService() != null) {
                return new SimpleStringProperty(cellData.getValue().getDateMiseEnService().toString());
            }
            return new SimpleStringProperty("Non définie");
        });
        
        // Chargement des états de véhicule dans le combo
        loadEtatsVoiture();
        
        // Chargement des véhicules
        loadVehicules();
        
        // Sélection d'une ligne de la table
        tableVehicules.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedVehicule = newSelection;
                showVehiculeDetails(selectedVehicule);
            } else {
                clearVehiculeDetails();
            }
        });
        
        // Configuration des permissions pour les boutons
        configureButtonsVisibility();
    }
    
    /**
     * Vérifie les permissions de l'utilisateur pour accéder à la gestion des véhicules.
     * 
     * @return true si l'utilisateur a les permissions nécessaires, false sinon
     */
    @Override
    protected boolean checkPermissions() {
        return hasAnyPermission(
                Permission.CONSULTER_VEHICULES_TOUS,
                Permission.CONSULTER_VEHICULES_PERSONNEL,
                Permission.CONSULTER_VEHICULES_LECTURE);
    }
    
    /**
     * Configure la visibilité des boutons en fonction des permissions de l'utilisateur.
     */
    private void configureButtonsVisibility() {
        btnAjouter.setVisible(hasPermission(Permission.AJOUTER_VEHICULE));
        btnModifier.setVisible(hasPermission(Permission.MODIFIER_VEHICULE));
        btnSupprimer.setVisible(hasPermission(Permission.SUPPRIMER_VEHICULE));
        btnDeclarer.setVisible(hasAnyPermission(
                Permission.DECLARER_PANNE_TOUS,
                Permission.DECLARER_PANNE_PERSONNEL));
    }
    
    /**
     * Charge la liste des véhicules depuis le service.
     */
    private void loadVehicules() {
        try {
            List<Vehicule> vehiculesList;
            
            // Chargement des véhicules en fonction des permissions
            if (hasPermission(Permission.CONSULTER_VEHICULES_TOUS)) {
                vehiculesList = vehiculeService.getAllVehicules();
            } else if (hasPermission(Permission.CONSULTER_VEHICULES_PERSONNEL)) {
                vehiculesList = vehiculeService.getVehiculesByUtilisateur(getCurrentUser().getIdUtilisateur());
            } else {
                vehiculesList = vehiculeService.getAllVehicules(); // Lecture seule
            }
            
            vehicules = FXCollections.observableArrayList(vehiculesList);
            tableVehicules.setItems(vehicules);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement des véhicules", e);
            showErrorAlert("Erreur", "Impossible de charger la liste des véhicules");
        }
    }
    
    /**
     * Charge les états de véhicule dans le combo.
     */
    private void loadEtatsVoiture() {
        try {
            List<EtatVoiture> etats = vehiculeService.getAllEtats();
            comboEtat.setItems(FXCollections.observableArrayList(etats));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement des états de véhicule", e);
        }
    }
    
    /**
     * Affiche les détails d'un véhicule sélectionné.
     * 
     * @param vehicule Le véhicule sélectionné
     */
    private void showVehiculeDetails(Vehicule vehicule) {
        if (vehicule != null) {
            txtImmatriculation.setText(vehicule.getImmatriculation());
            txtMarque.setText(vehicule.getMarque());
            txtModele.setText(vehicule.getModele());
            txtKilometrage.setText(String.valueOf(vehicule.getKmActuels()));
            
            if (vehicule.getDateMiseEnService() != null) {
                dateMiseEnCirculation.setValue(vehicule.getDateMiseEnService().toLocalDate());
            }
            
            if (vehicule.getEtatVoiture() != null) {
                comboEtat.setValue(vehicule.getEtatVoiture());
            }
            
            detailPane.setVisible(true);
        }
    }
    
    /**
     * Efface les champs de détail du véhicule.
     */
    private void clearVehiculeDetails() {
        txtImmatriculation.clear();
        txtMarque.clear();
        txtModele.clear();
        txtKilometrage.clear();
        dateMiseEnCirculation.setValue(null);
        comboEtat.setValue(null);
        selectedVehicule = null;
    }
    
    /**
     * Gère le clic sur le bouton Ajouter.
     * 
     * @param event L'événement de clic
     */
    @FXML
    private void handleAjouter(ActionEvent event) {
        if (!hasPermission(Permission.AJOUTER_VEHICULE)) {
            showErrorAlert("Accès refusé", "Vous n'avez pas la permission d'ajouter un véhicule");
            return;
        }
        
        if (!validateInputs()) {
            return;
        }
        
        try {
            Vehicule vehicule = new Vehicule();
            vehicule.setImmatriculation(txtImmatriculation.getText());
            vehicule.setMarque(txtMarque.getText());
            vehicule.setModele(txtModele.getText());
            vehicule.setKmActuels(Integer.parseInt(txtKilometrage.getText()));
            vehicule.setDateMiseEnService(dateMiseEnCirculation.getValue().atStartOfDay());
            vehicule.setEtatVoiture(comboEtat.getValue());
            
            Vehicule added = vehiculeService.createVehicule(vehicule);
            
            if (added != null) {
                vehicules.add(added);
                clearVehiculeDetails();
                showInfoAlert("Succès", "Véhicule ajouté avec succès");
            } else {
                showErrorAlert("Erreur", "Impossible d'ajouter le véhicule");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'ajout du véhicule", e);
            showErrorAlert("Erreur", "Une erreur est survenue lors de l'ajout du véhicule");
        }
    }
    
    /**
     * Gère le clic sur le bouton Modifier.
     * 
     * @param event L'événement de clic
     */
    @FXML
    private void handleModifier(ActionEvent event) {
        if (!hasPermission(Permission.MODIFIER_VEHICULE)) {
            showErrorAlert("Accès refusé", "Vous n'avez pas la permission de modifier un véhicule");
            return;
        }
        
        if (selectedVehicule == null) {
            showErrorAlert("Erreur", "Veuillez sélectionner un véhicule à modifier");
            return;
        }
        
        if (!validateInputs()) {
            return;
        }
        
        try {
            selectedVehicule.setImmatriculation(txtImmatriculation.getText());
            selectedVehicule.setMarque(txtMarque.getText());
            selectedVehicule.setModele(txtModele.getText());
            selectedVehicule.setKmActuels(Integer.parseInt(txtKilometrage.getText()));
            selectedVehicule.setDateMiseEnService(dateMiseEnCirculation.getValue().atStartOfDay());
            selectedVehicule.setEtatVoiture(comboEtat.getValue());
            
            boolean updated = vehiculeService.updateVehicule(selectedVehicule);
            
            if (updated) {
                refreshTable();
                clearVehiculeDetails();
                showInfoAlert("Succès", "Véhicule modifié avec succès");
            } else {
                showErrorAlert("Erreur", "Impossible de modifier le véhicule");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la modification du véhicule", e);
            showErrorAlert("Erreur", "Une erreur est survenue lors de la modification du véhicule");
        }
    }
    
    /**
     * Gère le clic sur le bouton Supprimer.
     * 
     * @param event L'événement de clic
     */
    @FXML
    private void handleSupprimer(ActionEvent event) {
        if (!hasPermission(Permission.SUPPRIMER_VEHICULE)) {
            showErrorAlert("Accès refusé", "Vous n'avez pas la permission de supprimer un véhicule");
            return;
        }
        
        if (selectedVehicule == null) {
            showErrorAlert("Erreur", "Veuillez sélectionner un véhicule à supprimer");
            return;
        }
        
        // Demande de confirmation
        if (showConfirmationAlert("Confirmation", "Êtes-vous sûr de vouloir supprimer ce véhicule ?")) {
            try {
                boolean deleted = vehiculeService.deleteVehicule(selectedVehicule.getIdVehicule());
                
                if (deleted) {
                    vehicules.remove(selectedVehicule);
                    clearVehiculeDetails();
                    showInfoAlert("Succès", "Véhicule supprimé avec succès");
                } else {
                    showErrorAlert("Erreur", "Impossible de supprimer le véhicule");
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erreur lors de la suppression du véhicule", e);
                showErrorAlert("Erreur", "Une erreur est survenue lors de la suppression du véhicule");
            }
        }
    }
    
    /**
     * Gère le clic sur le bouton pour déclarer une panne.
     * 
     * @param event L'événement de clic
     */
    @FXML
    private void handleDeclarerPanne(ActionEvent event) {
        if (!hasAnyPermission(Permission.DECLARER_PANNE_TOUS, Permission.DECLARER_PANNE_PERSONNEL)) {
            showErrorAlert("Accès refusé", "Vous n'avez pas la permission de déclarer une panne");
            return;
        }
        
        if (selectedVehicule == null) {
            showErrorAlert("Erreur", "Veuillez sélectionner un véhicule pour déclarer une panne");
            return;
        }
        
        // Si l'utilisateur a seulement la permission DECLARER_PANNE_PERSONNEL,
        // vérifier que le véhicule lui est assigné
        if (!hasPermission(Permission.DECLARER_PANNE_TOUS) && 
            hasPermission(Permission.DECLARER_PANNE_PERSONNEL)) {
            
            if (!vehiculeService.isVehiculeAssignedToUser(selectedVehicule.getIdVehicule(), 
                                                         getCurrentUser().getIdUtilisateur())) {
                showErrorAlert("Accès refusé", "Vous pouvez déclarer une panne uniquement pour vos véhicules");
                return;
            }
        }
        
        // Rediriger vers la vue de déclaration de panne ou afficher un dialogue
        navigateTo(btnDeclarer, "/fxml/panne_declaration.fxml", "Déclaration de panne");
    }
    
    /**
     * Gère le clic sur le bouton Retour.
     * 
     * @param event L'événement de clic
     */
    @FXML
    private void handleRetour(ActionEvent event) {
        navigateTo(btnRetour, "/fxml/dashboard.fxml", "Gestion de Parc Automobile - Tableau de bord");
    }
    
    /**
     * Rafraîchit la table des véhicules.
     */
    private void refreshTable() {
        loadVehicules();
    }
    
    /**
     * Valide les entrées du formulaire.
     * 
     * @return true si toutes les entrées sont valides, false sinon
     */
    private boolean validateInputs() {
        StringBuilder errors = new StringBuilder();
        
        // Validation de l'immatriculation (format français: AB-123-CD)
        String immatriculation = txtImmatriculation.getText();
        if (immatriculation.isEmpty()) {
            errors.append("L'immatriculation est obligatoire\n");
        } else if (!Validator.isValidImmatriculation(immatriculation)) {
            errors.append("Format d'immatriculation invalide (ex: AB-123-CD)\n");
        }
        
        // Validation de la marque
        if (txtMarque.getText().isEmpty()) {
            errors.append("La marque est obligatoire\n");
        }
        
        // Validation du modèle
        if (txtModele.getText().isEmpty()) {
            errors.append("Le modèle est obligatoire\n");
        }
        
        // Validation du kilométrage
        try {
            int km = Integer.parseInt(txtKilometrage.getText());
            if (km < 0) {
                errors.append("Le kilométrage ne peut pas être négatif\n");
            }
        } catch (NumberFormatException e) {
            errors.append("Le kilométrage doit être un nombre entier\n");
        }
        
        // Validation de la date de mise en circulation
        if (dateMiseEnCirculation.getValue() == null) {
            errors.append("La date de mise en circulation est obligatoire\n");
        }
        
        // Validation de l'état
        if (comboEtat.getValue() == null) {
            errors.append("L'état du véhicule est obligatoire\n");
        }
        
        // Affichage des erreurs s'il y en a
        if (errors.length() > 0) {
            showErrorAlert("Erreurs de validation", errors.toString());
            return false;
        }
        
        return true;
    }
}