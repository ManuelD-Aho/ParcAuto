package main.java.com.miage.parcauto.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.com.miage.parcauto.dao.DocumentDao.Document;
import main.java.com.miage.parcauto.dao.DocumentDao.TypeDoc;
import main.java.com.miage.parcauto.model.Societaire;
import main.java.com.miage.parcauto.service.DocumentService;
import main.java.com.miage.parcauto.service.SocietaireService;
import main.java.com.miage.parcauto.util.Permission;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * Contrôleur pour la gestion des documents.
 * Permet l'upload, la consultation, la validation et la suppression des documents.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class DocumentController extends BaseController implements Initializable {
    
    private static final Logger LOGGER = Logger.getLogger(DocumentController.class.getName());
    
    private final DocumentService documentService;
    private final SocietaireService societaireService;
    
    @FXML
    private TableView<Document> tableDocuments;
    
    @FXML
    private TableColumn<Document, String> colNomFichier;
    
    @FXML
    private TableColumn<Document, String> colSocietaire;
    
    @FXML
    private TableColumn<Document, String> colType;
    
    @FXML
    private TableColumn<Document, String> colDateUpload;
    
    @FXML
    private VBox detailPane;
    
    @FXML
    private ComboBox<Societaire> comboSocietaire;
    
    @FXML
    private ComboBox<TypeDoc> comboTypeDoc;
    
    @FXML
    private TextField txtNomFichier;
    
    @FXML
    private TextField txtDateUpload;
    
    @FXML
    private Button btnUpload;
    
    @FXML
    private Button btnConsulter;
    
    @FXML
    private Button btnValider;
    
    @FXML
    private Button btnSupprimer;
    
    @FXML
    private Button btnRetour;
    
    private ObservableList<Document> documents;
    private Document selectedDocument;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    /**
     * Constructeur par défaut.
     */
    public DocumentController() {
        this.documentService = new DocumentService();
        this.societaireService = new SocietaireService();
    }
    
    /**
     * Constructeur avec injection de dépendance pour les tests.
     * 
     * @param documentService Service de gestion des documents
     * @param societaireService Service de gestion des sociétaires
     */
    public DocumentController(DocumentService documentService, SocietaireService societaireService) {
        this.documentService = documentService;
        this.societaireService = societaireService;
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
        colNomFichier.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getNomFichier()));
            
        colSocietaire.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getNomSocietaire()));
            
        colType.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getTypeDoc().name()));
            
        colDateUpload.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDateUpload() != null ? 
                dateTimeFormatter.format(cellData.getValue().getDateUpload()) : ""));
        
        // Chargement des documents
        loadDocuments();
        
        // Chargement des sociétaires dans la combo
        loadSocietaires();
        
        // Chargement des types de documents dans la combo
        comboTypeDoc.setItems(FXCollections.observableArrayList(TypeDoc.values()));
        
        // Sélection d'une ligne de la table
        tableDocuments.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedDocument = newSelection;
                showDocumentDetails(selectedDocument);
            } else {
                clearDocumentDetails();
            }
        });
        
        // Configuration des permissions pour les boutons
        configureButtonsVisibility();
    }
    
    /**
     * Vérifie les permissions de l'utilisateur pour accéder à la gestion des documents.
     * 
     * @return true si l'utilisateur a les permissions nécessaires, false sinon
     */
    @Override
    protected boolean checkPermissions() {
        return hasAnyPermission(
                Permission.CONSULTER_DOCUMENTS_TOUS,
                Permission.CONSULTER_DOCUMENTS_TECHNIQUE,
                Permission.CONSULTER_DOCUMENTS_PERSONNEL,
                Permission.UPLOAD_DOCUMENTS_TOUS,
                Permission.UPLOAD_DOCUMENTS_TECHNIQUE,
                Permission.UPLOAD_DOCUMENTS_PERSONNEL);
    }
    
    /**
     * Configure la visibilité des boutons en fonction des permissions de l'utilisateur.
     */
    private void configureButtonsVisibility() {
        btnUpload.setVisible(hasAnyPermission(
                Permission.UPLOAD_DOCUMENTS_TOUS,
                Permission.UPLOAD_DOCUMENTS_TECHNIQUE,
                Permission.UPLOAD_DOCUMENTS_PERSONNEL));
        
        btnConsulter.setVisible(hasAnyPermission(
                Permission.CONSULTER_DOCUMENTS_TOUS,
                Permission.CONSULTER_DOCUMENTS_TECHNIQUE,
                Permission.CONSULTER_DOCUMENTS_PERSONNEL));
        
        btnValider.setVisible(hasPermission(Permission.VALIDER_DOCUMENTS));
        btnSupprimer.setVisible(hasPermission(Permission.SUPPRIMER_DOCUMENTS));
    }
    
    /**
     * Charge la liste des documents depuis le service.
     */
    private void loadDocuments() {
        try {
            List<Document> documentsList;
            
            // Chargement des documents en fonction des permissions
            if (hasPermission(Permission.CONSULTER_DOCUMENTS_TOUS)) {
                documentsList = documentService.getAllDocuments();
            } else if (hasPermission(Permission.CONSULTER_DOCUMENTS_TECHNIQUE)) {
                // Logique spécifique pour les documents techniques
                documentsList = documentService.getDocumentsByTypes(TypeDoc.CarteGrise, TypeDoc.Assurance);
            } else if (hasPermission(Permission.CONSULTER_DOCUMENTS_PERSONNEL)) {
                // Logique pour les documents du sociétaire connecté
                Societaire societaire = societaireService.getSocietaireByIdUtilisateur(getCurrentUser().getIdUtilisateur());
                if (societaire != null) {
                    documentsList = documentService.getDocumentsBySocietaire(societaire.getIdSocietaire());
                } else {
                    documentsList = documentService.getAllDocuments(); // Fallback
                    showWarningAlert("Attention", "Impossible de trouver votre profil sociétaire. Affichage de tous les documents.");
                }
            } else {
                documentsList = documentService.getAllDocuments(); // Lecture seule
            }
            
            documents = FXCollections.observableArrayList(documentsList);
            tableDocuments.setItems(documents);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement des documents", e);
            showErrorAlert("Erreur", "Impossible de charger la liste des documents");
        }
    }
    
    /**
     * Charge la liste des sociétaires dans la combobox.
     */
    private void loadSocietaires() {
        try {
            // Conversion explicite SocieteCompte -> Societaire si besoin
            List<Societaire> societairesList = societaireService.getAllSocietaires().stream()
                .map(sc -> {
                    Societaire s = new Societaire();
                    s.setIdSocietaire(sc.getIdSocietaire());
                    s.setNom(sc.getNom());
                    if (sc.getPersonnel() != null) s.setPrenom(sc.getPersonnel().getPrenomPersonnel());
                    s.setEmail(sc.getEmail());
                    s.setTelephone(sc.getTelephone());
                    return s;
                })
                .toList();
            comboSocietaire.setItems(FXCollections.observableArrayList(societairesList));
            
            // Configuration de l'affichage des sociétaires dans la combobox
            comboSocietaire.setCellFactory(param -> new ListCell<Societaire>() {
                @Override
                protected void updateItem(Societaire item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getNom() + " " + item.getPrenom());
                    }
                }
            });
            
            comboSocietaire.setButtonCell(new ListCell<Societaire>() {
                @Override
                protected void updateItem(Societaire item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getNom() + " " + item.getPrenom());
                    }
                }
            });
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement des sociétaires", e);
        }
    }
    
    /**
     * Affiche les détails d'un document sélectionné.
     * 
     * @param document Le document sélectionné
     */
    private void showDocumentDetails(Document document) {
        if (document != null) {
            txtNomFichier.setText(document.getNomOriginal() != null ? 
                document.getNomOriginal() : document.getNomFichier());
                
            txtDateUpload.setText(document.getDateUpload() != null ? 
                dateTimeFormatter.format(document.getDateUpload()) : "");
                
            comboTypeDoc.setValue(document.getTypeDoc());
            
            // Sélectionner le sociétaire dans la combo
            if (document.getIdSocietaire() != null) {
                Optional<Societaire> soc = comboSocietaire.getItems().stream()
                        .filter(s -> document.getIdSocietaire() != null && document.getIdSocietaire().equals(s.getIdSocietaire()))
                        .findFirst();
                soc.ifPresent(comboSocietaire::setValue);
            }
            
            detailPane.setVisible(true);
        }
    }
    
    /**
     * Efface les champs de détail du document.
     */
    private void clearDocumentDetails() {
        txtNomFichier.clear();
        txtDateUpload.clear();
        comboSocietaire.setValue(null);
        comboTypeDoc.setValue(null);
        selectedDocument = null;
    }
    
    /**
     * Gère le clic sur le bouton Upload.
     * 
     * @param event L'événement de clic
     */
    @FXML
    private void handleUpload(ActionEvent event) {
        if (!hasAnyPermission(
                Permission.UPLOAD_DOCUMENTS_TOUS,
                Permission.UPLOAD_DOCUMENTS_TECHNIQUE,
                Permission.UPLOAD_DOCUMENTS_PERSONNEL)) {
            showErrorAlert("Accès refusé", "Vous n'avez pas la permission d'uploader un document");
            return;
        }
        
        // Vérifier si un sociétaire est sélectionné
        if (comboSocietaire.getValue() == null) {
            showErrorAlert("Erreur", "Veuillez sélectionner un sociétaire");
            return;
        }
        
        // Vérifier si un type de document est sélectionné
        if (comboTypeDoc.getValue() == null) {
            showErrorAlert("Erreur", "Veuillez sélectionner un type de document");
            return;
        }
        
        // Permissions spécifiques selon le rôle
        if (hasPermission(Permission.UPLOAD_DOCUMENTS_PERSONNEL) && 
            !hasPermission(Permission.UPLOAD_DOCUMENTS_TOUS) && 
            !hasPermission(Permission.UPLOAD_DOCUMENTS_TECHNIQUE)) {
            
            // On récupère le sociétaire lié à l'utilisateur
            Societaire userSocietaire = societaireService.getSocietaireByIdUtilisateur(getCurrentUser().getIdUtilisateur());
            
            // Vérifier que l'utilisateur ne peut uploader que ses propres documents
            if (userSocietaire == null || 
                !comboSocietaire.getValue().getIdSocietaire().equals(userSocietaire.getIdSocietaire())) {
                showErrorAlert("Accès refusé", "Vous ne pouvez uploader que vos propres documents");
                return;
            }
        }
        
        // Configuration du FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un document");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Documents", "*.pdf", "*.docx", "*.doc"),
                new ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png"),
                new ExtensionFilter("Tous les fichiers", "*.*"));
        
        // Ouverture du dialogue de sélection de fichier
        File selectedFile = fileChooser.showOpenDialog(btnUpload.getScene().getWindow());
        
        if (selectedFile != null) {
            try {
                // Vérifier si un document du même type existe déjà
                if (documentService.documentExists(
                        comboSocietaire.getValue().getIdSocietaire(),
                        comboTypeDoc.getValue())) {
                    
                    if (!showConfirmationAlert("Document existant",
                            "Un document de ce type existe déjà pour ce sociétaire. Voulez-vous le remplacer?")) {
                        return;
                    }
                    
                    // Logique de remplacement à implémenter si nécessaire
                }
                
                // Upload du document
                Document uploaded = documentService.uploadDocument(
                        selectedFile.toPath(),
                        comboSocietaire.getValue().getIdSocietaire(),
                        comboTypeDoc.getValue(),
                        selectedFile.getName());
                
                if (uploaded != null) {
                    // Rafraîchir la liste
                    refreshTable();
                    clearDocumentDetails();
                    showInfoAlert("Succès", "Document uploadé avec succès");
                } else {
                    showErrorAlert("Erreur", "Impossible d'uploader le document");
                }
                
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erreur lors de l'upload du document", e);
                showErrorAlert("Erreur", "Une erreur est survenue lors de l'upload du document: " + e.getMessage());
            }
        }
    }
    
    /**
     * Gère le clic sur le bouton Consulter.
     * Ouvre le document dans l'application associée.
     * 
     * @param event L'événement de clic
     */
    @FXML
    private void handleConsulter(ActionEvent event) {
        if (!hasAnyPermission(
                Permission.CONSULTER_DOCUMENTS_TOUS,
                Permission.CONSULTER_DOCUMENTS_TECHNIQUE,
                Permission.CONSULTER_DOCUMENTS_PERSONNEL)) {
            showErrorAlert("Accès refusé", "Vous n'avez pas la permission de consulter ce document");
            return;
        }
        
        if (selectedDocument == null) {
            showErrorAlert("Erreur", "Veuillez sélectionner un document à consulter");
            return;
        }
        
        // Permissions spécifiques selon le rôle
        if (hasPermission(Permission.CONSULTER_DOCUMENTS_PERSONNEL) && 
            !hasPermission(Permission.CONSULTER_DOCUMENTS_TOUS) && 
            !hasPermission(Permission.CONSULTER_DOCUMENTS_TECHNIQUE)) {
            
            // On récupère le sociétaire lié à l'utilisateur
            Societaire userSocietaire = societaireService.getSocietaireByIdUtilisateur(getCurrentUser().getIdUtilisateur());
            
            // Vérifier que l'utilisateur ne peut consulter que ses propres documents
            if (userSocietaire == null || !selectedDocument.getIdSocietaire().equals(userSocietaire.getIdSocietaire())) {
                showErrorAlert("Accès refusé", "Vous ne pouvez consulter que vos propres documents");
                return;
            }
        }
        
        try {
            Optional<Path> documentPath = documentService.getDocumentPath(selectedDocument.getIdDoc());
            
            if (!documentPath.isPresent()) {
                showErrorAlert("Erreur", "Le fichier n'a pas été trouvé sur le serveur");
                return;
            }
            
            // Ouvrir le fichier avec l'application associée
            openFileWithDefaultApp(documentPath.get().toFile());
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la consultation du document", e);
            showErrorAlert("Erreur", "Impossible d'ouvrir le document");
        }
    }
    
    /**
     * Ouvre un fichier avec l'application par défaut du système.
     * 
     * @param file Le fichier à ouvrir
     */
    private void openFileWithDefaultApp(File file) {
        try {
            if (!file.exists()) {
                showErrorAlert("Erreur", "Le fichier n'existe pas: " + file.getAbsolutePath());
                return;
            }
            
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                // Pour Windows
                new ProcessBuilder("cmd", "/c", file.getAbsolutePath()).start();
            } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                // Pour macOS
                new ProcessBuilder("open", file.getAbsolutePath()).start();
            } else {
                // Pour Linux/Unix
                new ProcessBuilder("xdg-open", file.getAbsolutePath()).start();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'ouverture du fichier", e);
            showErrorAlert("Erreur", "Impossible d'ouvrir le fichier avec l'application par défaut");
        }
    }
    
    @FXML
    private void handleValider(ActionEvent event) {
        if (!hasPermission(Permission.VALIDER_DOCUMENTS)) {
            showErrorAlert("Accès refusé", "Vous n'avez pas la permission de valider un document");
            return;
        }
        if (selectedDocument == null) {
            showErrorAlert("Erreur", "Veuillez sélectionner un document à valider");
            return;
        }
        // Logique de validation à implémenter selon les besoins
        showInfoAlert("Information", "La validation de documents sera disponible dans une prochaine version");
    }
    
    /**
     * Gère le clic sur le bouton Supprimer.
     * 
     * @param event L'événement de clic
     */
    @FXML
    private void handleSupprimer(ActionEvent event) {
        if (!hasPermission(Permission.SUPPRIMER_DOCUMENTS)) {
            showErrorAlert("Accès refusé", "Vous n'avez pas la permission de supprimer un document");
            return;
        }
        
        if (selectedDocument == null) {
            showErrorAlert("Erreur", "Veuillez sélectionner un document à supprimer");
            return;
        }
        
        // Demande de confirmation
        if (showConfirmationAlert("Confirmation", "Êtes-vous sûr de vouloir supprimer ce document ?")) {
            try {
                boolean deleted = documentService.deleteDocument(selectedDocument.getIdDoc());
                
                if (deleted) {
                    documents.remove(selectedDocument);
                    clearDocumentDetails();
                    showInfoAlert("Succès", "Document supprimé avec succès");
                } else {
                    showErrorAlert("Erreur", "Impossible de supprimer le document");
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erreur lors de la suppression du document", e);
                showErrorAlert("Erreur", "Une erreur est survenue lors de la suppression du document");
            }
        }
    }
    
    @FXML
    private void handleRetour(ActionEvent event) {
        navigateTo(btnRetour, "/fxml/dashboard.fxml", "Gestion de Parc Automobile - Tableau de bord");
    }
    
    /**
     * Rafraîchit la table des documents.
     */
    private void refreshTable() {
        loadDocuments();
    }
}