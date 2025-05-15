package main.java.com.miage.parcauto.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.com.miage.parcauto.util.Permission;
import main.java.com.miage.parcauto.util.ResourceManager;
import main.java.com.miage.parcauto.util.SecurityManager;
import main.java.com.miage.parcauto.util.SessionManager;
import main.java.com.miage.parcauto.util.ThemeManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Contrôleur de base pour tous les contrôleurs de l'application.
 * Fournit des méthodes utilitaires pour la gestion des permissions, la navigation et l'affichage d'alertes.
 *
 * @author MIAGE Holding
 * @version 1.2
 */
public abstract class BaseController {

    private static final Logger LOGGER = Logger.getLogger(BaseController.class.getName());
    protected final SecurityManager securityManager;
    protected final SessionManager sessionManager;
    protected final ThemeManager themeManager;

    // Constantes pour les ressources CSS
    protected static final String[] DEFAULT_CSS = {"/css/theme-default.css"};

    // Composants qui peuvent être présents dans les vues
    @FXML
    protected Label lblCurrentDateTime;

    @FXML
    protected Label lblUserName;

    @FXML
    protected Label lblUserRole;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Constructeur.
     * Initialise les gestionnaires de sécurité, de session et de thème.
     */
    public BaseController() {
        this.securityManager = SecurityManager.getInstance();
        this.sessionManager = SessionManager.getInstance();
        this.themeManager = ThemeManager.getInstance();
    }

    /**
     * Initialise les éléments communs à tous les contrôleurs.
     * Cette méthode devrait être appelée par les sous-classes dans leur méthode initialize().
     */
    protected void initializeBaseComponents() {
        // Mettre à jour les informations utilisateur
        updateUserInfo();

        // Mettre à jour la date et l'heure
        updateDateTime();
    }

    /**
     * Vérifie si l'utilisateur actuel possède la permission spécifiée.
     *
     * @param permission La permission à vérifier
     * @return true si l'utilisateur a la permission, false sinon
     */
    protected boolean hasPermission(Permission permission) {
        return securityManager.hasPermission(permission);
    }

    /**
     * Vérifie si l'utilisateur actuel possède toutes les permissions spécifiées.
     *
     * @param permissions Les permissions à vérifier
     * @return true si l'utilisateur a toutes les permissions, false sinon
     */
    protected boolean hasAllPermissions(Permission... permissions) {
        return securityManager.hasAllPermissions(permissions);
    }

    /**
     * Vérifie si l'utilisateur actuel possède au moins une des permissions spécifiées.
     *
     * @param permissions Les permissions à vérifier
     * @return true si l'utilisateur a au moins une des permissions, false sinon
     */
    protected boolean hasAnyPermission(Permission... permissions) {
        return securityManager.hasAnyPermission(permissions);
    }

    /**
     * Obtient l'utilisateur actuellement connecté.
     *
     * @return L'utilisateur actuel ou null si aucun utilisateur n'est connecté
     */
    protected Utilisateur getCurrentUser() {
        return sessionManager.getCurrentUser();
    }

    /**
     * Vérifie si un utilisateur est connecté.
     *
     * @return true si un utilisateur est connecté, false sinon
     */
    protected boolean isAuthenticated() {
        return sessionManager.isAuthenticated();
    }

    /**
     * Déconnecte l'utilisateur actuel et redirige vers la page de login.
     *
     * @param control Un contrôle JavaFX utilisé pour obtenir la fenêtre actuelle
     */
    protected void logout(Control control) {
        try {
            // Déconnexion de l'utilisateur
            sessionManager.logout();

            // Charger et afficher la page de login via le ResourceManager
            FXMLLoader loader = ResourceManager.getFXMLLoader("/fxml/login.fxml");
            Parent root = loader.load();

            // Configuration de la scène
            Scene scene = new Scene(root);
            themeManager.applyTheme(scene, "/css/views/login.css");

            // Récupérer le stage actuel
            Stage stage = (Stage) control.getScene().getWindow();
            stage.setTitle("Gestion de Parc Automobile - Authentification");
            stage.setScene(scene);

            LOGGER.info("Déconnexion effectuée avec succès");

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la déconnexion", ex);
            showErrorAlert("Erreur lors de la déconnexion", ex.getMessage());
        }
    }

    /**
     * Navigue vers une autre vue.
     *
     * @param control Un contrôle JavaFX utilisé pour obtenir la fenêtre actuelle
     * @param fxmlPath Le chemin vers le fichier FXML de la vue
     * @param title Le titre à afficher dans la fenêtre
     * @param cssPath Chemin vers la feuille de style spécifique à la vue
     */
    protected void navigateTo(Control control, String fxmlPath, String title, String cssPath) {
        try {
            // Charger et afficher la nouvelle vue via le ResourceManager
            FXMLLoader loader = ResourceManager.getFXMLLoader(fxmlPath);
            Parent root = loader.load();

            // Configuration de la scène
            Scene scene = new Scene(root);
            themeManager.applyTheme(scene, cssPath);

            // Récupérer le stage actuel
            Stage stage = (Stage) control.getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(scene);

            LOGGER.info("Navigation vers " + fxmlPath);

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la navigation vers " + fxmlPath, ex);
            showErrorAlert("Erreur de navigation", "Impossible de charger la vue : " + fxmlPath);
        }
    }

    /**
     * Surcharge de navigateTo sans CSS spécifique
     */
    protected void navigateTo(Control control, String fxmlPath, String title) {
        navigateTo(control, fxmlPath, title, null);
    }

    /**
     * Change le thème de l'application
     *
     * @param themeName Nom du thème
     * @param control Un contrôle JavaFX pour obtenir la scène
     * @param cssPath Chemin CSS spécifique à la vue
     */
    protected void changeTheme(String themeName, Control control, String cssPath) {
        Scene scene = control.getScene();
        if (scene != null) {
            themeManager.setTheme(themeName, scene, cssPath);
        }
    }

    /**
     * Affiche une alerte d'erreur.
     *
     * @param title Le titre de l'alerte
     * @param message Le message d'erreur
     */
    protected void showErrorAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Appliquer le style CSS via le ResourceManager
        String css = ResourceManager.getStylesheetPath("/css/theme-" + themeManager.getCurrentTheme() + ".css");
        if (css != null) {
            alert.getDialogPane().getStylesheets().add(css);
        }

        alert.showAndWait();
    }

    /**
     * Affiche une alerte d'information.
     *
     * @param title Le titre de l'alerte
     * @param message Le message d'information
     */
    protected void showInfoAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Appliquer le style CSS via le ResourceManager
        String css = ResourceManager.getStylesheetPath("/css/theme-" + themeManager.getCurrentTheme() + ".css");
        if (css != null) {
            alert.getDialogPane().getStylesheets().add(css);
        }

        alert.showAndWait();
    }

    /**
     * Affiche une alerte de confirmation.
     *
     * @param title Le titre de l'alerte
     * @param message Le message de confirmation
     * @return true si l'utilisateur a confirmé, false sinon
     */
    protected boolean showConfirmationAlert(String title, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Appliquer le style CSS via le ResourceManager
        String css = ResourceManager.getStylesheetPath("/css/theme-" + themeManager.getCurrentTheme() + ".css");
        if (css != null) {
            alert.getDialogPane().getStylesheets().add(css);
        }

        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    /**
     * Affiche une alerte d'avertissement.
     *
     * @param title Le titre de l'alerte
     * @param message Le message d'avertissement
     */
    protected void showWarningAlert(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Appliquer le style CSS via le ResourceManager
        String css = ResourceManager.getStylesheetPath("/css/theme-" + themeManager.getCurrentTheme() + ".css");
        if (css != null) {
            alert.getDialogPane().getStylesheets().add(css);
        }

        alert.showAndWait();
    }

    /**
     * Met à jour les informations utilisateur dans l'interface
     */
    protected void updateUserInfo() {
        Utilisateur currentUser = sessionManager.getCurrentUser();

        if (currentUser != null) {
            if (lblUserName != null) {
                lblUserName.setText(currentUser.getLogin());
            }

            if (lblUserRole != null) {
                lblUserRole.setText(currentUser.getRole() != null ? currentUser.getRole().toString() : "Non défini");
            }
        }
    }

    /**
     * Met à jour la date et l'heure dans l'interface
     */
    protected void updateDateTime() {
        if (lblCurrentDateTime != null) {
            String currentDateTime = LocalDateTime.now().format(dateTimeFormatter);
            String username = sessionManager.getCurrentUser() != null ? sessionManager.getCurrentUser().getLogin() : "Anonyme";
            lblCurrentDateTime.setText(currentDateTime + " | " + username);
        }
    }

    /**
     * Vérifie les permissions nécessaires pour accéder à la vue actuelle.
     * Cette méthode doit être implémentée par les sous-classes pour définir les permissions requises.
     * Elle est généralement appelée dans la méthode initialize() du contrôleur.
     *
     * @return true si l'utilisateur a les permissions nécessaires, false sinon
     */
    protected abstract boolean checkPermissions();

    /**
     * Méthode appelée lorsque l'utilisateur n'a pas les permissions nécessaires.
     * Le comportement par défaut est d'afficher une alerte et de revenir au tableau de bord.
     *
     * @param control Un contrôle JavaFX utilisé pour obtenir la fenêtre actuelle
     */
    protected void onPermissionDenied(Control control) {
        showErrorAlert("Accès refusé", "Vous n'avez pas les permissions nécessaires pour accéder à cette fonctionnalité.");
        navigateTo(control, "/fxml/dashboard.fxml", "Gestion de Parc Automobile - Tableau de bord", "/css/views/dashboard.css");
    }
}