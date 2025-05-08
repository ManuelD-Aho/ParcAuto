package com.miage.parcauto;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.miage.parcauto.util.SecurityManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Classe principale de l'application de gestion de parc automobile.
 * Cette classe sert de point d'entrée à l'application et initialise l'interface utilisateur JavaFX.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class MainApp extends Application {
    
    private static final Logger LOGGER = Logger.getLogger(MainApp.class.getName());
    private static final String APP_TITLE = "Gestion de Parc Automobile";
    private static final String LOGIN_FXML = "/fxml/login.fxml";
    private static final String APP_ICON = "/images/logo.png";
    
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
            // Initialisation du système de sécurité
            initializeSecurityManager();
            
            // Chargement de la vue de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource(LOGIN_FXML));
            Parent root = loader.load();
            
            // Configuration de la scène principale
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/theme.css").toExternalForm());
            
            // Configuration du stage principal
            primaryStage.setTitle(APP_TITLE);
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(APP_ICON)));
            primaryStage.setResizable(true);
            primaryStage.centerOnScreen();
            
            // Affichage de l'application
            primaryStage.show();
            
            LOGGER.info("Application démarrée avec succès");
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement de l'interface utilisateur", e);
            showFatalError(e);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur inattendue lors du démarrage de l'application", e);
            showFatalError(e);
        }
    }
    
    /**
     * Initialise le gestionnaire de sécurité de l'application.
     * Cette méthode est appelée au démarrage pour charger les permissions des utilisateurs.
     */
    private void initializeSecurityManager() {
        try {
            // Récupérer l'instance du SecurityManager pour initialiser les permissions
            SecurityManager.getInstance();
            LOGGER.info("Système de sécurité initialisé avec succès");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'initialisation du système de sécurité", e);
            throw e; // Propager l'exception car c'est une erreur critique
        }
    }
    
    /**
     * Méthode appelée lors de l'arrêt de l'application.
     * Ferme toutes les ressources ouvertes et effectue les opérations de nettoyage nécessaires.
     */
    @Override
    public void stop() {
        LOGGER.info("Fermeture de l'application");
        // Libération des ressources si nécessaire
    }
    
    /**
     * Affiche une boîte de dialogue d'erreur fatale avant de quitter l'application.
     * 
     * @param e Exception à l'origine de l'erreur
     */
    private void showFatalError(Exception e) {
        LOGGER.severe("Erreur fatale: " + e.getMessage());
        // Afficher une boîte de dialogue d'erreur avant de quitter
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur fatale");
        alert.setHeaderText("Une erreur fatale est survenue");
        alert.setContentText("L'application va être fermée : " + e.getMessage());
        alert.showAndWait();
        System.exit(1);
    }
}