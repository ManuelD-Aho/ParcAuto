package com.miage.parcauto.util;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Utilitaire statique pour charger des vues FXML avec gestion des erreurs et stylage.
 * Permet de centraliser le chargement des interfaces JavaFX dans l'application ParcAuto.
 *
 * Usage :
 *   FxmlLoader.loadAndShow(stage, "/fxml/ma_vue.fxml", "Titre", resourceBundle);
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public final class FxmlLoader {
    private static final Logger LOGGER = Logger.getLogger(FxmlLoader.class.getName());

    private FxmlLoader() {}

    /**
     * Charge et affiche une vue FXML dans la fenêtre spécifiée.
     *
     * @param stage La fenêtre cible
     * @param fxmlPath Le chemin du fichier FXML
     * @param title Le titre de la fenêtre
     * @param bundle Le ResourceBundle pour l'internationalisation (peut être null)
     * @throws IOException si le chargement échoue
     */
    public static void loadAndShow(Stage stage, String fxmlPath, String title, ResourceBundle bundle) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL fxmlUrl = FxmlLoader.class.getResource(fxmlPath);
        if (fxmlUrl == null) {
            throw new IOException("Fichier FXML introuvable : " + fxmlPath);
        }
        loader.setLocation(fxmlUrl);
        if (bundle != null) loader.setResources(bundle);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        // Ajout du thème CSS principal
        scene.getStylesheets().add(FxmlLoader.class.getResource("/css/theme.css").toExternalForm());
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Charge une vue FXML et retourne le Parent (sans l'afficher).
     *
     * @param fxmlPath Chemin du fichier FXML
     * @param bundle ResourceBundle (peut être null)
     * @return Parent racine de la vue
     * @throws IOException si le chargement échoue
     */
    public static Parent load(String fxmlPath, ResourceBundle bundle) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL fxmlUrl = FxmlLoader.class.getResource(fxmlPath);
        if (fxmlUrl == null) {
            throw new IOException("Fichier FXML introuvable : " + fxmlPath);
        }
        loader.setLocation(fxmlUrl);
        if (bundle != null) loader.setResources(bundle);
        return loader.load();
    }

    /**
     * Charge une vue FXML et retourne le contrôleur associé.
     *
     * @param fxmlPath Chemin du fichier FXML
     * @param bundle ResourceBundle (peut être null)
     * @param <T> Type du contrôleur
     * @return Contrôleur associé à la vue
     * @throws IOException si le chargement échoue
     */
    public static <T> T loadController(String fxmlPath, ResourceBundle bundle) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL fxmlUrl = FxmlLoader.class.getResource(fxmlPath);
        if (fxmlUrl == null) {
            throw new IOException("Fichier FXML introuvable : " + fxmlPath);
        }
        loader.setLocation(fxmlUrl);
        if (bundle != null) loader.setResources(bundle);
        loader.load();
        return loader.getController();
    }

    /**
     * Charge et affiche une vue FXML dans la fenêtre spécifiée, avec gestion d'erreur.
     *
     * @param stage La fenêtre cible
     * @param fxmlPath Le chemin du fichier FXML
     * @param title Le titre de la fenêtre
     * @param bundle Le ResourceBundle pour l'internationalisation (peut être null)
     */
    public static void safeLoadAndShow(Stage stage, String fxmlPath, String title, ResourceBundle bundle) {
        try {
            loadAndShow(stage, fxmlPath, title, bundle);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement de la vue FXML : " + fxmlPath, ex);
            // Affichage d'une alerte JavaFX si possible
            javafx.application.Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de chargement");
                alert.setHeaderText(null);
                alert.setContentText("Impossible de charger la vue : " + fxmlPath);
                alert.showAndWait();
            });
        }
    }
}