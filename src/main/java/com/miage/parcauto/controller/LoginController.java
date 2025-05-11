package main.java.com.miage.parcauto.controller;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import main.java.com.miage.parcauto.dao.UtilisateurDao;
import main.java.com.miage.parcauto.dao.UtilisateurDao.Utilisateur;
import main.java.com.miage.parcauto.util.SessionManager;

/**
 * Contrôleur amélioré pour la vue de login.
 * Gère l'authentification des utilisateurs avec animations et transitions.
 *
 * @author MIAGE Holding
 * @version 1.1
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

    @FXML
    private ImageView logoImage;

    /**
     * Constructeur.
     * Initialise le DAO utilisateur.
     */
    public LoginController() {
        this.utilisateurDao = new UtilisateurDao();
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
            mfaPanel.setOpacity(0);
        }

        if (lblErreur != null) {
            lblErreur.setVisible(false);
            lblErreur.setOpacity(0);
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

        // Focus sur le champ de login
        txtLogin.requestFocus();
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
        lblErreur.setVisible(false);
        if (login == null || login.isBlank() || password == null || password.isBlank()) {
            showErrorDialog("Veuillez saisir votre identifiant et votre mot de passe.");
            return;
        }
        try {
            Optional<Utilisateur> userOpt = utilisateurDao.authentifier(login, password);
            if (userOpt.isPresent()) {
                SessionManager.getInstance().setCurrentUser(userOpt.get());
                loginSuccessful();
            } else {
                showErrorDialog("Identifiant ou mot de passe incorrect.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'authentification", e);
            showErrorDialog("Erreur lors de l'authentification : " + e.getMessage());
        }
    }

    /**
     * Appelée en cas de succès de connexion. Charge le dashboard.
     */
    private void loginSuccessful() {
        try {
            URL fxmlUrl = getClass().getResource("/fxml/dashboard.fxml");
            if (fxmlUrl == null) {
                LOGGER.severe("dashboard.fxml introuvable dans le classpath !");
                showErrorDialog("dashboard.fxml introuvable dans le classpath !");
                return;
            }
            Parent dashboardRoot = FXMLLoader.load(fxmlUrl);
            Stage stage = (Stage) txtLogin.getScene().getWindow();
            Scene scene = new Scene(dashboardRoot);
            // Ajout des CSS si elles existent
            String css1 = "/css/theme.css";
            String css2 = "/css/dashboard.css";
            URL cssUrl1 = getClass().getResource(css1);
            if (cssUrl1 != null)
                scene.getStylesheets().add(cssUrl1.toExternalForm());
            URL cssUrl2 = getClass().getResource(css2);
            if (cssUrl2 != null)
                scene.getStylesheets().add(cssUrl2.toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Gestion de Parc Automobile - Tableau de bord");
            stage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement du dashboard", e);
            showErrorDialog("Erreur lors du chargement du dashboard : " + e.getMessage());
        }
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
                loginSuccessful(userOpt.get());
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
     * Traitement après une connexion réussie avec animation de transition.
     *
     * @param user L'utilisateur authentifié
     */
    private void loginSuccessful(Utilisateur user) {
        // Enregistre l'utilisateur dans la session
        SessionManager.getInstance().setCurrentUser(user);

        try {
            // Charger le tableau de bord (classpath ou fallback absolu)
            URL dashboardFxml = getClass().getResource("/fxml/dashboard.fxml");
            if (dashboardFxml == null) {
                // Fallback absolu
                java.nio.file.Path absPath = java.nio.file.Paths.get(System.getProperty("user.dir"), "src", "main",
                        "resources", "fxml", "dashboard.fxml");
                if (absPath.toFile().exists()) {
                    dashboardFxml = absPath.toUri().toURL();
                } else {
                    LOGGER.severe("dashboard.fxml introuvable dans le classpath ni en chemin absolu !");
                    showErrorDialog(
                            "Erreur critique : le fichier dashboard.fxml est introuvable. Contactez l'administrateur.");
                    return;
                }
            }
            FXMLLoader loader = new FXMLLoader(dashboardFxml);
            Parent root = loader.load();

            // Configuration de la scène
            Scene scene = new Scene(root);
            // Appliquer le style CSS si disponible
            URL cssUrl = getClass().getResource("/css/theme.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                LOGGER.warning("Feuille de style theme.css introuvable");
            }

            // Récupérer le stage actuel
            Stage stage = (Stage) btnConnexion.getScene().getWindow();

            // Animation de transition
            FadeTransition fadeOut = new FadeTransition(Duration.millis(600), btnConnexion.getScene().getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                stage.setTitle("Gestion de Parc Automobile - Tableau de bord");
                stage.setScene(scene);

                // Animation d'entrée pour le tableau de bord
                root.setOpacity(0);
                FadeTransition fadeIn = new FadeTransition(Duration.millis(800), root);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fadeOut.play();

            LOGGER.info("Connexion réussie pour l'utilisateur: " + user.getLogin());

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement du tableau de bord", ex);
            showError("Impossible de charger le tableau de bord");
        }
    }

    /**
     * Affiche un message d'erreur avec animation.
     *
     * @param message Le message d'erreur à afficher
     */
    private void showError(String message) {
        lblErreur.setText(message);
        lblErreur.setVisible(true);

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
        mfaPanel.setVisible(true);
        lblErreur.setVisible(false);

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
        // Ajout d'une CSS custom si elle existe
        String cssPath = "/css/theme.css";
        URL cssUrl = getClass().getResource(cssPath);
        if (cssUrl != null) {
            alert.getDialogPane().getStylesheets().add(cssUrl.toExternalForm());
        }
        alert.showAndWait();
    }
}