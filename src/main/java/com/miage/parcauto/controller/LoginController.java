package main.java.com.miage.parcauto.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import main.java.com.miage.parcauto.dao.UtilisateurDao;
import main.java.com.miage.parcauto.dao.UtilisateurDao.Utilisateur;
import main.java.com.miage.parcauto.util.ResourceManager;
import main.java.com.miage.parcauto.util.SessionManager;
import main.java.com.miage.parcauto.util.ThemeManager;

/**
 * Contrôleur amélioré pour la vue de login.
 * Gère l'authentification des utilisateurs avec animations et transitions.
 *
 * @author MIAGE Holding
 * @version 1.3
 */
public class LoginController {

    private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());
    private final UtilisateurDao utilisateurDao;
    private final ThemeManager themeManager;

    // Constantes pour les ressources
    private static final String DASHBOARD_FXML = "/fxml/dashboard.fxml";
    private static final String[] DASHBOARD_CSS = {
            "/css/views/dashboard.css"
    };

    @FXML
    private TextField txtLogin;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnConnexion;

    @FXML
    private Label lblErreur; // Assurez-vous que fx:id="lblErreur" existe dans login.fxml

    @FXML
    private VBox mfaPanel;

    @FXML
    private TextField txtCodeMfa;

    @FXML
    private ImageView logoImage;

    @FXML
    private Button themeDefaultBtn;

    @FXML
    private Button themeDarkBtn;

    @FXML
    private Button themeContrastBtn;

    @FXML
    private Button themeCorporateBtn;

    @FXML
    private Button themeModernBtn;

    /**
     * Constructeur.
     * Initialise le DAO utilisateur et le gestionnaire de thèmes.
     */
    public LoginController() {
        this.utilisateurDao = new UtilisateurDao();
        this.themeManager = ThemeManager.getInstance();
    }

    /**
     * Méthode d'initialisation appelée après le chargement du FXML.
     * Ajoute des animations et initialise l'interface.
     */
    @FXML
    public void initialize() {
        // Cacher le panneau MFA et message d'erreur par défaut
        if (mfaPanel != null) {
            mfaPanel.setVisible(false);
            mfaPanel.setManaged(false); // Ne prend pas de place si invisible
        }

        // Correction : gestion de l'affichage du message d'erreur
        if (lblErreur != null) {
            lblErreur.setVisible(false);
            lblErreur.setManaged(false); // Ne prend pas de place si invisible
        } else {
            LOGGER.warning("FXML injection manquée : lblErreur est null dans initialize(). Vérifiez le fichier FXML.");
        }

        // Animation du logo au chargement
        if (logoImage != null) {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(1500), logoImage);
            scaleTransition.setFromX(0.8);
            scaleTransition.setFromY(0.8);
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.setAutoReverse(true);
            scaleTransition.setCycleCount(2);
            scaleTransition.play();
        }

        // Configurer les boutons de thème s'ils existent
        configureThemeButtons();

        // Focus sur le champ de login
        txtLogin.requestFocus();
    }

    /**
     * Configure les boutons de changement de thème
     */
    private void configureThemeButtons() {
        // Vérifier si les boutons de thème existent
        if (themeDefaultBtn != null) {
            themeDefaultBtn.setOnAction(e -> changeTheme("default", e));
        }

        if (themeDarkBtn != null) {
            themeDarkBtn.setOnAction(e -> changeTheme("dark", e));
        }

        if (themeContrastBtn != null) {
            themeContrastBtn.setOnAction(e -> changeTheme("contrast", e));
        }

        if (themeCorporateBtn != null) {
            themeCorporateBtn.setOnAction(e -> changeTheme("corporate", e));
        }

        if (themeModernBtn != null) {
            themeModernBtn.setOnAction(e -> changeTheme("modern", e));
        }
    }

    /**
     * Change le thème de l'application
     *
     * @param themeName Nom du thème à appliquer
     * @param event     L'événement de clic
     */
    private void changeTheme(String themeName, ActionEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();
        themeManager.setTheme(themeName, scene, "/css/views/login.css");

        // Animation du bouton sélectionné
        Button selectedBtn = (Button) event.getSource();
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), selectedBtn);
        scaleTransition.setToX(1.2);
        scaleTransition.setToY(1.2);
        scaleTransition.setCycleCount(2);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();
    }

    /**
     * Gère le clic sur le bouton de connexion avec animation.
     *
     * @param event L'événement de clic
     */
    @FXML
    private void handleConnexion(ActionEvent event) {
        String login = txtLogin.getText();
        String password = txtPassword.getText();

        // Vérification de nullité pour lblErreur avant utilisation
        if (lblErreur == null) {
            LOGGER.severe(
                    "lblErreur est null dans handleConnexion. L'injection FXML a échoué. Impossible d'afficher les erreurs UI.");
            // Optionnel: Afficher une alerte système si lblErreur n'est pas disponible
            Alert alert = new Alert(AlertType.ERROR, "Erreur critique : Composant UI manquant.");
            alert.showAndWait();
            return; // Sortir pour éviter d'autres NullPointerExceptions
        }

        lblErreur.setVisible(false);
        lblErreur.setManaged(false);

        if (login == null || login.isBlank() || password == null || password.isBlank()) {
            showErrorDialog("Veuillez saisir votre identifiant et votre mot de passe.");
            return;
        }
        try {
            Optional<Utilisateur> userOpt = utilisateurDao.authentifier(login, password);
            if (userOpt.isPresent()) {
                SessionManager.getInstance().setCurrentUser(userOpt.get());
                loginSuccessful(event);
            } else {
                showErrorDialog("Identifiant ou mot de passe incorrect.");
                shakeFields();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'authentification", e);
            showErrorDialog("Erreur lors de l'authentification : " + e.getMessage());
        }
    }

    /**
     * Appelée en cas de succès de connexion. Charge le dashboard.
     *
     * @param event L'événement de clic
     */
    private void loginSuccessful(ActionEvent event) {
        try {
            // Chargement du dashboard via le ResourceManager
            FXMLLoader loader = ResourceManager.getFXMLLoader(DASHBOARD_FXML);
            Parent dashboardRoot = loader.load();

            // Configuration de la scène
            Scene scene = new Scene(dashboardRoot);

            // Application du thème et des styles CSS via le ThemeManager
            themeManager.applyTheme(scene, DASHBOARD_CSS);

            // Affichage dans le stage actuel
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Gestion de Parc Automobile - Tableau de bord");

            // Animation de transition
            animateTransition(dashboardRoot, scene, stage);

            LOGGER.info("Navigation vers le tableau de bord réussie");

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement du dashboard", e);
            showErrorDialog("Erreur lors du chargement du dashboard : " + e.getMessage());
        }
    }

    /**
     * Anime la transition entre l'écran de login et le dashboard.
     *
     * @param dashboardRoot Noeud racine du dashboard
     * @param newScene      Nouvelle scène à afficher
     * @param stage         Stage principal
     */
    private void animateTransition(Parent dashboardRoot, Scene newScene, Stage stage) {
        // Opacité initiale à 0 pour l'animation d'entrée
        dashboardRoot.setOpacity(0);

        // Appliquer la nouvelle scène
        stage.setScene(newScene);

        // Animation d'entrée pour le tableau de bord
        FadeTransition fadeIn = new FadeTransition(Duration.millis(800), dashboardRoot);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    /**
     * Gère la validation du code MFA avec animation.
     *
     * @param event L'événement de clic
     */
    @FXML
    private void handleValidateMfa(ActionEvent event) {
        // Animation du bouton
        animateButtonClick();

        String login = txtLogin.getText();
        String password = txtPassword.getText();
        String mfaCode = txtCodeMfa.getText();

        if (mfaCode.isEmpty()) {
            showError("Veuillez entrer le code MFA");
            shakeField(txtCodeMfa);
            return;
        }

        try {
            // Tentative d'authentification avec MFA
            Optional<Utilisateur> userOpt = utilisateurDao.authentifierAvecMfa(login, password, mfaCode);

            if (userOpt.isPresent()) {
                loginSuccessful(userOpt.get(), event);
            } else {
                showError("Code MFA incorrect");
                shakeField(txtCodeMfa);
            }

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'authentification avec MFA", ex);
            showError("Une erreur est survenue lors de la vérification du code MFA");
        }
    }

    /**
     * Traitement après une connexion réussie avec MFA.
     *
     * @param user  L'utilisateur authentifié
     * @param event L'événement de clic
     */
    private void loginSuccessful(Utilisateur user, ActionEvent event) {
        // Enregistre l'utilisateur dans la session
        SessionManager.getInstance().setCurrentUser(user);
        loginSuccessful(event);
    }

    /**
     * Affiche un message d'erreur avec animation.
     *
     * @param message Le message d'erreur à afficher
     */
    private void showError(String message) {
        // Vérification de nullité pour lblErreur avant utilisation
        if (lblErreur == null) {
            LOGGER.severe("lblErreur est null dans showError. L'injection FXML a échoué.");
            // Optionnel: Afficher une alerte système
            Alert alert = new Alert(AlertType.ERROR, "Erreur d'affichage : " + message);
            alert.showAndWait();
            return;
        }
        lblErreur.setText(message);
        lblErreur.setVisible(true);
        lblErreur.setManaged(true); // Rendre managé pour qu'il prenne de la place

        // Animation du message d'erreur
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), lblErreur);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        LOGGER.warning("Erreur de connexion: " + message);
    }

    /**
     * Affiche le panneau de saisie du code MFA avec animation.
     */
    private void showMfaPanel() {
        // Vérification de nullité pour mfaPanel et lblErreur avant utilisation
        if (mfaPanel == null || lblErreur == null) {
            LOGGER.severe("mfaPanel ou lblErreur est null dans showMfaPanel. L'injection FXML a échoué.");
            return;
        }
        mfaPanel.setVisible(true);
        mfaPanel.setManaged(true); // Rendre managé
        lblErreur.setVisible(false);
        lblErreur.setManaged(false);

        // Animation d'apparition du panneau MFA
        FadeTransition fadeIn = new FadeTransition(Duration.millis(400), mfaPanel);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        TranslateTransition slideIn = new TranslateTransition(Duration.millis(400), mfaPanel);
        slideIn.setFromY(20);
        slideIn.setToY(0);

        ParallelTransition parallelTransition = new ParallelTransition(fadeIn, slideIn);
        parallelTransition.play();

        // Focus sur le champ de code MFA
        txtCodeMfa.requestFocus();
    }

    /**
     * Animation de secousse pour les champs de saisie en cas d'erreur.
     */
    private void shakeFields() {
        shakeField(txtLogin);
        shakeField(txtPassword);
    }

    /**
     * Animation de secousse pour un champ spécifique.
     *
     * @param field Le champ à animer
     */
    private void shakeField(TextField field) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(field.translateXProperty(), 0)),
                new KeyFrame(Duration.millis(50), new KeyValue(field.translateXProperty(), -5)),
                new KeyFrame(Duration.millis(100), new KeyValue(field.translateXProperty(), 5)),
                new KeyFrame(Duration.millis(150), new KeyValue(field.translateXProperty(), -5)),
                new KeyFrame(Duration.millis(200), new KeyValue(field.translateXProperty(), 5)),
                new KeyFrame(Duration.millis(250), new KeyValue(field.translateXProperty(), -5)),
                new KeyFrame(Duration.millis(300), new KeyValue(field.translateXProperty(), 0)));
        timeline.play();
    }

    /**
     * Animation du bouton lors du clic.
     */
    private void animateButtonClick() {
        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(100), btnConnexion);
        scaleDown.setToX(0.95);
        scaleDown.setToY(0.95);

        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(100), btnConnexion);
        scaleUp.setToX(1.0);
        scaleUp.setToY(1.0);

        scaleDown.setOnFinished(event -> scaleUp.play());
        scaleDown.play();
    }

    /**
     * Affiche une boîte de dialogue d'erreur stylée.
     *
     * @param message Le message à afficher
     */
    private void showErrorDialog(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Ajout d'une CSS custom via le ResourceManager
        String css = ResourceManager.getStylesheetPath("/css/theme-" + themeManager.getCurrentTheme() + ".css");
        if (css != null) {
            alert.getDialogPane().getStylesheets().add(css);
        }

        alert.showAndWait();
    }
}