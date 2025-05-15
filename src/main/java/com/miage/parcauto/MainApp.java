package main.java.com.miage.parcauto;

import java.io.IOException;
import java.io.PrintStream;
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
import main.java.com.miage.parcauto.util.ThemeManager;

/**
 * Classe principale de l'application de gestion de parc automobile.
 * Cette classe sert de point d'entrée à l'application et initialise l'interface
 * utilisateur JavaFX.
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
            "/css/views/login.css" // Style spécifique à l'écran de login
    };

    // Liste des ressources critiques à vérifier au démarrage
    private static final String[] CRITICAL_RESOURCES = {
            LOGIN_FXML,
            APP_ICON,
            "/css/theme-base.css",
            "/css/theme-default.css",
            "/css/theme-dark.css",
            "/css/theme-contrast.css",
            "/css/theme-corporate.css",
            "/css/theme-modern.css",
            "/images/logo-parcauto.png"
    };

    private Stage primaryStage;
    private Scene mainScene;

    /**
     * Point d'entrée principal de l'application.
     *
     * @param args Arguments de ligne de commande
     */
    public static void main(String[] args) {
        System.out.println("MainApp.main() appelé."); // Log de démarrage
        // Rediriger System.err vers un fichier pour capturer les erreurs natives ou de
        // bas niveau
        try {
            PrintStream errStream = new PrintStream("system_err.log");
            System.setErr(errStream);
        } catch (IOException e) {
            System.out.println("Impossible de rediriger System.err: " + e.getMessage());
        }
        launch(args);
    }

    /**
     * Initialise l'interface utilisateur de l'application.
     *
     * @param primaryStage Stage principal de l'application
     */
    @Override
    public void start(Stage primaryStage) {
        System.out.println("MainApp.start() appelé."); // Log de démarrage
        try {
            this.primaryStage = primaryStage;
            System.out.println("ResourceManager.ensureResourceDirectories() va être appelé.");
            // S'assurer que les répertoires de ressources existent
            ResourceManager.ensureResourceDirectories();
            System.out.println("ResourceManager.ensureResourceDirectories() appelé avec succès.");

            System.out.println("ResourceManager.validateResources() va être appelé.");
            // Vérifier les ressources critiques
            if (!ResourceManager.validateResources(CRITICAL_RESOURCES)) {
                LOGGER.warning(
                        "Certaines ressources critiques sont manquantes. L'application pourrait ne pas fonctionner correctement.");
                System.out.println("AVERTISSEMENT: Certaines ressources critiques sont manquantes.");
            }
            System.out.println("ResourceManager.validateResources() appelé avec succès.");

            System.out.println("initializeSystemComponents() va être appelé.");
            // Initialiser les composants système
            initializeSystemComponents();
            System.out.println("initializeSystemComponents() appelé avec succès.");

            System.out.println("configureStage() va être appelé.");
            // Configurer l'apparence de la fenêtre
            configureStage(primaryStage);
            System.out.println("configureStage() appelé avec succès.");

            System.out.println("loadLoginInterface() va être appelé.");
            // Charger l'interface de login
            loadLoginInterface(primaryStage);
            System.out.println("loadLoginInterface() appelé avec succès.");

            LOGGER.info("Application démarrée avec succès");
            System.out.println("Application démarrée avec succès (log système).");

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement de l'interface: {0}", e.getMessage());
            System.err.println("MainApp.start() - IOException: " + e.getMessage());
            e.printStackTrace(System.err);
            showFatalError(e);
        } catch (Throwable e) { // Changé en Throwable pour attraper plus d'erreurs (ex: LinkageError)
            LOGGER.log(Level.SEVERE, "Erreur inattendue dans start(): {0}", e.getMessage());
            System.err.println("MainApp.start() - Erreur inattendue (Throwable): " + e.getMessage());
            e.printStackTrace(System.err);
            // Tenter d'afficher une erreur fatale, mais cela pourrait échouer si JavaFX
            // n'est pas initialisé
            try {
                showFatalError(new Exception("Erreur de démarrage: " + e.getMessage(), e));
            } catch (Throwable t) {
                System.err.println("Impossible d'afficher l'erreur fatale via JavaFX: " + t.getMessage());
                Platform.exit();
                System.exit(1);
            }
        }
    }

    /**
     * Initialise les composants système de l'application.
     */
    private void initializeSystemComponents() {
        System.out.println("initializeSystemComponents() - Début");
        LOGGER.info("Initialisation des composants système...");

        System.out.println("initializeSecurityManager() va être appelé.");
        // Initialiser le système de sécurité
        initializeSecurityManager();
        System.out.println("initializeSecurityManager() appelé.");

        System.out.println("testDatabaseConnection() va être appelé.");
        // Vérifier la connexion à la base de données
        testDatabaseConnection();
        System.out.println("testDatabaseConnection() appelé.");

        System.out.println("ThemeManager.getInstance() va être appelé.");
        // Initialiser le gestionnaire de thèmes
        ThemeManager.getInstance();
        System.out.println("ThemeManager.getInstance() appelé.");
        System.out.println("initializeSystemComponents() - Fin");
    }

    /**
     * Initialise le gestionnaire de sécurité de l'application.
     */
    private void initializeSecurityManager() {
        System.out.println("initializeSecurityManager() - Début");
        try {
            SecurityManager.getInstance();
            LOGGER.info("Système de sécurité initialisé avec succès");
            System.out.println("Système de sécurité initialisé avec succès.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'initialisation du système de sécurité", e);
            System.err.println("Erreur lors de l'initialisation du système de sécurité: " + e.getMessage());
            e.printStackTrace(System.err);
            throw e; // Erreur critique, on propage l'exception
        }
        System.out.println("initializeSecurityManager() - Fin");
    }

    /**
     * Teste la connexion à la base de données.
     */
    private void testDatabaseConnection() {
        System.out.println("testDatabaseConnection() - Début");
        try {
            boolean isConnected = DbUtil.getInstance().testConnection();
            if (isConnected) {
                LOGGER.info("Connexion à la base de données établie avec succès");
                System.out.println("Connexion à la base de données établie avec succès.");
            } else {
                LOGGER.warning("Impossible de se connecter à la base de données");
                System.out.println("AVERTISSEMENT: Impossible de se connecter à la base de données.");
            }
        } catch (Exception e) {
            // On log l'erreur mais on ne bloque pas le lancement de l'application
            LOGGER.log(Level.WARNING, "Erreur lors du test de connexion à la base de données", e);
            System.err.println("Erreur lors du test de connexion à la base de données: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        System.out.println("testDatabaseConnection() - Fin");
    }

    /**
     * Configure l'apparence de la fenêtre principale.
     *
     * @param stage Le stage à configurer
     */
    private void configureStage(Stage stage) {
        System.out.println("configureStage() - Début");
        stage.setTitle(APP_TITLE + " v" + APP_VERSION);
        stage.setResizable(true);
        stage.setMaximized(false);
        stage.setMinWidth(1280);
        stage.setMinHeight(800);
        stage.setWidth(1280);
        stage.setHeight(800);
        System.out.println("Dimensions et titre du stage configurés.");

        System.out.println("loadAppIcon() va être appelé.");
        // Charger l'icône de l'application
        loadAppIcon(stage);
        System.out.println("loadAppIcon() appelé.");

        System.out.println("ThemeManager.getInstance().applyThemeToApplication() va être appelé.");
        // Appliquer le thème à l'application
        ThemeManager.getInstance().applyThemeToApplication(stage);
        System.out.println("ThemeManager.getInstance().applyThemeToApplication() appelé.");
        System.out.println("configureStage() - Fin");
    }

    /**
     * Charge l'icône de l'application.
     *
     * @param stage Le stage auquel appliquer l'icône
     */
    private void loadAppIcon(Stage stage) {
        System.out.println("loadAppIcon() - Début");
        Image appIcon = ResourceManager.getImage(APP_ICON);
        if (appIcon != null) {
            stage.getIcons().add(appIcon);
            LOGGER.fine("Icône chargée avec succès");
            System.out.println("Icône chargée avec succès: " + APP_ICON);
        } else {
            LOGGER.warning("Impossible de charger l'icône de l'application: " + APP_ICON);
            System.out.println("AVERTISSEMENT: Impossible de charger l'icône de l'application: " + APP_ICON);
        }
        System.out.println("loadAppIcon() - Fin");
    }

    /**
     * Charge l'interface de login et l'affiche.
     *
     * @param stage Le stage dans lequel afficher l'interface
     * @throws IOException Si une erreur survient lors du chargement de l'interface
     */
    private void loadLoginInterface(Stage stage) throws IOException {
        System.out.println("loadLoginInterface() - Début. FXML: " + LOGIN_FXML);
        // Charger le fichier FXML via le ResourceManager
        FXMLLoader loader = ResourceManager.getFXMLLoader(LOGIN_FXML);
        System.out.println("FXMLLoader obtenu pour " + LOGIN_FXML);
        Parent root = loader.load();
        System.out.println("FXML chargé: " + LOGIN_FXML);

        // Créer la scène
        mainScene = new Scene(root);
        System.out.println("Scène créée.");

        // Appliquer le thème et les styles CSS spécifiques
        System.out
                .println("Application du thème à la scène de login. CSS spécifiques: " + String.join(", ", CSS_FILES));
        ThemeManager.getInstance().applyTheme(mainScene, CSS_FILES);
        System.out.println("Thème appliqué à la scène de login.");

        // Afficher la scène
        stage.setScene(mainScene);
        System.out.println("Scène principale définie sur le stage.");
        stage.show();
        System.out.println("Stage affiché (stage.show() appelé).");
        System.out.println("loadLoginInterface() - Fin");
    }

    /**
     * Méthode appelée lors de l'arrêt de l'application.
     * Ferme toutes les ressources ouvertes et effectue les opérations de nettoyage
     * nécessaires.
     */
    @Override
    public void stop() {
        System.out.println("MainApp.stop() appelé.");
        LOGGER.info("Fermeture de l'application...");

        // Fermer la connexion à la base de données
        try {
            DbUtil.getInstance().shutdown();
            System.out.println("Connexion à la base de données fermée.");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Erreur lors de la fermeture de la connexion à la base de données", e);
            System.err.println("Erreur lors de la fermeture de la connexion à la base de données: " + e.getMessage());
            e.printStackTrace(System.err);
        }

        LOGGER.info("Application fermée avec succès");
        System.out.println("Application fermée avec succès (log système).");
    }

    /**
     * Affiche une boîte de dialogue d'erreur fatale avant de quitter l'application.
     * Exécution sur le thread JavaFX pour éviter les problèmes de thread.
     *
     * @param e Exception à l'origine de l'erreur
     */
    private void showFatalError(Exception e) {
        System.err.println("showFatalError() appelé avec l'exception: " + e.getMessage());
        e.printStackTrace(System.err); // Imprimer la stack trace immédiatement
        LOGGER.log(Level.SEVERE, "Erreur fatale: {0}", e.getMessage());

        // Vérifier si la plateforme JavaFX est prête
        if (!Platform.isFxApplicationThread()) {
            System.err.println("showFatalError() - Pas sur le thread FX. Utilisation de Platform.runLater().");
            Platform.runLater(() -> displayAlert(e));
        } else {
            System.err.println("showFatalError() - Déjà sur le thread FX. Appel direct de displayAlert().");
            displayAlert(e);
        }
    }

    private void displayAlert(Exception e) {
        System.err.println("displayAlert() appelé avec l'exception: " + e.getMessage());
        try {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur fatale");
            alert.setHeaderText("Une erreur fatale est survenue et l'application doit fermer.");
            alert.setContentText("Détails de l'erreur : " + e.getMessage());

            System.out.println("Tentative de chargement du CSS pour l'alerte d'erreur.");
            // Appliquer un style personnalisé à l'alerte
            String css = ResourceManager.getStylesheetPath("/css/theme-default.css");
            if (css != null) {
                alert.getDialogPane().getStylesheets().add(css);
                System.out.println("CSS pour l'alerte d'erreur appliqué: " + css);
            } else {
                System.out.println("CSS pour l'alerte d'erreur non trouvé (/css/theme-default.css).");
            }

            alert.showAndWait();
        } catch (Throwable t) {
            // Si l'affichage de l'alerte échoue (par exemple, si JavaFX n'est pas
            // initialisé)
            System.err.println("Impossible d'afficher l'alerte JavaFX: " + t.getMessage());
            t.printStackTrace(System.err);
        } finally {
            // Quitter l'application quoi qu'il arrive
            System.err.println("displayAlert() - Fermeture de l'application (Platform.exit() et System.exit(1)).");
            Platform.exit();
            System.exit(1);
        }
    }
}