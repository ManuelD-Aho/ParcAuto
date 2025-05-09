package main.java.com.miage.parcauto.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.com.miage.parcauto.dao.UtilisateurDao;
import main.java.com.miage.parcauto.dao.UtilisateurDao.Utilisateur;
import main.java.com.miage.parcauto.util.SessionManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Contrôleur pour la vue de login.
 * Gère l'authentification des utilisateurs et la redirection vers le tableau de bord.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class LoginController {

    private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());
    private final UtilisateurDao utilisateurDao;

    @FXML
    private TextField txtLogin;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnConnexion;

    @FXML
    private Label lblErreur;

    @FXML
    private VBox mfaPanel;

    @FXML
    private TextField txtCodeMfa;

    /**
     * Constructeur.
     * Initialise le DAO utilisateur.
     */
    public LoginController() {
        this.utilisateurDao = new UtilisateurDao();
    }

    /**
     * Méthode d'initialisation appelée après le chargement du FXML.
     */
    @FXML
    public void initialize() {
        // Cacher le panneau MFA par défaut
        if (mfaPanel != null) {
            mfaPanel.setVisible(false);
        }
        
        // Cacher le message d'erreur par défaut
        if (lblErreur != null) {
            lblErreur.setVisible(false);
        }
    }

    /**
     * Gère le clic sur le bouton de connexion.
     * 
     * @param event L'événement de clic
     */
    @FXML
    private void handleConnexion(ActionEvent event) {
        String login = txtLogin.getText();
        String password = txtPassword.getText();
        
        if (login.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }
        
        try {
            // Tentative d'authentification
            Optional<Utilisateur> userOpt = utilisateurDao.authentifier(login, password);
            
            if (userOpt.isPresent()) {
                Utilisateur user = userOpt.get();
                
                // Si l'utilisateur a le MFA activé, afficher le panneau MFA
                if (user.hasMfaEnabled()) {
                    showMfaPanel();
                    return;
                }
                
                // Sinon, connexion réussie
                loginSuccessful(user);
                
            } else {
                showError("Login ou mot de passe incorrect");
            }
            
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'authentification", ex);
            showError("Une erreur est survenue lors de la connexion");
        }
    }
    
    /**
     * Gère la validation du code MFA.
     * 
     * @param event L'événement de clic
     */
    @FXML
    private void handleValidateMfa(ActionEvent event) {
        String login = txtLogin.getText();
        String password = txtPassword.getText();
        String mfaCode = txtCodeMfa.getText();
        
        if (mfaCode.isEmpty()) {
            showError("Veuillez entrer le code MFA");
            return;
        }
        
        try {
            // Tentative d'authentification avec MFA
            Optional<Utilisateur> userOpt = utilisateurDao.authentifierAvecMfa(login, password, mfaCode);
            
            if (userOpt.isPresent()) {
                loginSuccessful(userOpt.get());
            } else {
                showError("Code MFA incorrect");
            }
            
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'authentification avec MFA", ex);
            showError("Une erreur est survenue lors de la vérification du code MFA");
        }
    }
    
    /**
     * Traitement après une connexion réussie.
     * 
     * @param user L'utilisateur authentifié
     */
    private void loginSuccessful(Utilisateur user) {
        // Enregistre l'utilisateur dans la session
        SessionManager.getInstance().setCurrentUser(user);
        
        try {
            // Charger et afficher le tableau de bord
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
            Parent root = loader.load();
            
            // Configuration de la scène
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/theme.css").toExternalForm());
            
            // Récupérer le stage actuel
            Stage stage = (Stage) btnConnexion.getScene().getWindow();
            stage.setTitle("Gestion de Parc Automobile - Tableau de bord");
            stage.setScene(scene);
            
            LOGGER.info("Connexion réussie pour l'utilisateur: " + user.getLogin());
            
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement du tableau de bord", ex);
            showError("Impossible de charger le tableau de bord");
        }
    }
    
    /**
     * Affiche un message d'erreur.
     * 
     * @param message Le message d'erreur à afficher
     */
    private void showError(String message) {
        lblErreur.setText(message);
        lblErreur.setVisible(true);
        LOGGER.warning("Erreur de connexion: " + message);
    }
    
    /**
     * Affiche le panneau de saisie du code MFA.
     */
    private void showMfaPanel() {
        mfaPanel.setVisible(true);
        lblErreur.setVisible(false);
    }
    
    /**
     * Affiche une boîte de dialogue d'erreur.
     * 
     * @param message Le message d'erreur à afficher
     */
    private void showErrorDialog(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}