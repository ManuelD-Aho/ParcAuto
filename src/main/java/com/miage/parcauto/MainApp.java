package main.java.com.miage.parcauto;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import main.java.com.miage.parcauto.dao.DbUtil;
import main.java.com.miage.parcauto.util.ResourceManager;
import main.java.com.miage.parcauto.util.SecurityManager;

/**
 * Classe principale de l'application de gestion de parc automobile.
 * Cette classe sert de point d'entrée à l'application et initialise l'interface utilisateur JavaFX.
 *
 * @author MIAGE Holding
 * @version 1.3
 * @date 2025-05-11
 */
public class MainApp extends Application {

    private static final Logger LOGGER = Logger.getLogger(MainApp.class.getName());

    // Configuration de l'application
    private static final String APP_TITLE = "Gestion de Parc Automobile";
    private static final String APP_VERSION = "1.2.0";
    private static final String LOGIN_FXML = "/fxml/login.fxml";
    private static final String APP_ICON = "/images/logo.png";

    // Fichiers de style
    private static final String[] CSS_FILES = {
            "/css/theme.css",         // Style global
            "/css/login-theme.css"    // Style spécifique à l'écran de login
    };

    // Liste des ressources critiques à vérifier au démarrage
    private static final String[] CRITICAL_RESOURCES = {
            LOGIN_FXML,
            APP_ICON,
            "/css/theme.css",
            "/images/logo-parcauto.png"  // L'image qui cause le problème
    };

    private Stage primaryStage;
    private Scene mainScene;

    /**
     * Point d'entrée principal de l'application.
     *
     * @param args Arguments de ligne de commande
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initialise l'interface utilisateur de l'application.
     *
     * @param primaryStage Stage principal de l'application
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            this.primaryStage = primaryStage;

            // S'assurer que les répertoires de ressources existent
            ResourceManager.ensureResourceDirectories();

            // Vérifier les ressources critiques
            if (!ResourceManager.validateResources(CRITICAL_RESOURCES)) {
                LOGGER.warning("Certaines ressources critiques sont manquantes. L'application pourrait ne pas fonctionner correctement.");
            }

            // Initialiser les composants système
            initializeSystemComponents();

            // Configurer l'apparence de la fenêtre
            configureStage(primaryStage);

            // Charger l'interface de login
            loadLoginInterface(primaryStage);

            LOGGER.info("Application démarrée avec succès");

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement de l'interface: {0}", e.getMessage());
            showFatalError(e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur inattendue: {0}", e.getMessage());
            showFatalError(e);
        }
    }

    /**
     * Initialise les composants système de l'application.
     */
    private void initializeSystemComponents() {
        LOGGER.info("Initialisation des composants système...");

        // Initialiser le système de sécurité
        initializeSecurityManager();

        // Vérifier la connexion à la base de données
        testDatabaseConnection();
    }

    /**
     * Initialise le gestionnaire de sécurité de l'application.
     */
    private void initializeSecurityManager() {
        try {
            SecurityManager.getInstance();
            LOGGER.info("Système de sécurité initialisé avec succès");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'initialisation du système de sécurité", e);
            throw e; // Erreur critique, on propage l'exception
        }
    }

    /**
     * Teste la connexion à la base de données.
     */
    private void testDatabaseConnection() {
        try {
            boolean isConnected = DbUtil.getInstance().testConnection();
            if (isConnected) {
                LOGGER.info("Connexion à la base de données établie avec succès");
            } else {
                LOGGER.warning("Impossible de se connecter à la base de données");
            }
        } catch (Exception e) {
            // On log l'erreur mais on ne bloque pas le lancement de l'application
            LOGGER.log(Level.WARNING, "Erreur lors du test de connexion à la base de données", e);
        }
    }

    /**
     * Configure l'apparence de la fenêtre principale.
     *
     * @param stage Le stage à configurer
     */
    private void configureStage(Stage stage) {
        stage.setTitle(APP_TITLE + " v" + APP_VERSION);
        stage.setResizable(true);
        stage.setMaximized(true);

        // Charger l'icône de l'application
        loadAppIcon(stage);
    }

    /**
     * Charge l'icône de l'application.
     *
     * @param stage Le stage auquel appliquer l'icône
     */
    private void loadAppIcon(Stage stage) {
        Image appIcon = ResourceManager.getImage(APP_ICON);
        if (appIcon != null) {
            stage.getIcons().add(appIcon);
            LOGGER.fine("Icône chargée avec succès");
        } else {
            LOGGER.warning("Impossible de charger l'icône de l'application");
        }
    }

    /**
     * Charge l'interface de login et l'affiche.
     *
     * @param stage Le stage dans lequel afficher l'interface
     * @throws IOException Si une erreur survient lors du chargement de l'interface
     */
    private void loadLoginInterface(Stage stage) throws IOException {
        // Charger le fichier FXML via le ResourceManager
        FXMLLoader loader = ResourceManager.getFXMLLoader(LOGIN_FXML);
        Parent root = loader.load();

        // Créer la scène
        mainScene = new Scene(root);

        // Appliquer les styles CSS
        ResourceManager.applyStylesheets(mainScene, CSS_FILES);

        // Afficher la scène
        stage.setScene(mainScene);
        stage.show();
    }

    /**
     * Méthode appelée lors de l'arrêt de l'application.
     * Ferme toutes les ressources ouvertes et effectue les opérations de nettoyage nécessaires.
     */
    @Override
    public void stop() {
        LOGGER.info("Fermeture de l'application...");

        // Fermer la connexion à la base de données
        try {
            DbUtil.getInstance().shutdown();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Erreur lors de la fermeture de la connexion à la base de données", e);
        }

        LOGGER.info("Application fermée avec succès");
    }

    /**
     * Affiche une boîte de dialogue d'erreur fatale avant de quitter l'application.
     * Exécution sur le thread JavaFX pour éviter les problèmes de thread.
     *
     * @param e Exception à l'origine de l'erreur
     */
    private void showFatalError(Exception e) {
        LOGGER.log(Level.SEVERE, "Erreur fatale: {0}", e.getMessage());

        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur fatale");
            alert.setHeaderText("Une erreur fatale est survenue");
            alert.setContentText("L'application va être fermée : " + e.getMessage());

            // Appliquer un style personnalisé à l'alerte
            String css = ResourceManager.getStylesheetPath("/css/theme.css");
            if (css != null) {
                alert.getDialogPane().getStylesheets().add(css);
            }

            alert.showAndWait();
            Platform.exit();
            System.exit(1);
        });
    }
}