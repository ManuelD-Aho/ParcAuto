package main.java.com.miage.parcauto.exception;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import main.java.com.miage.parcauto.service.ValidationService.ValidationResult;

/**
 * Gestionnaire central des erreurs pour l'application ParcAuto.
 * Cette classe fournit des méthodes pour gérer les exceptions de manière
 * uniforme.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class ErrorHandler {

    private static final Logger LOGGER = Logger.getLogger(ErrorHandler.class.getName());

    /**
     * Constructeur privé pour empêcher l'instanciation de cette classe utilitaire.
     */
    private ErrorHandler() {
        throw new IllegalStateException("Classe utilitaire non instanciable");
    }

    /**
     * Gère une exception et affiche une boîte de dialogue appropriée.
     * 
     * @param ex    Exception à traiter
     * @param title Titre de la boîte de dialogue
     */
    public static void handleException(Throwable ex, String title) {
        LOGGER.log(Level.SEVERE, "Erreur: " + ex.getMessage(), ex);

        if (ex instanceof ValidationException) {
            handleValidationException((ValidationException) ex, title);
        } else if (ex instanceof VehiculeNotFoundException ||
                ex instanceof EntretienNotFoundException ||
                ex instanceof MissionNotFoundException) {
            displayError(title, ex.getMessage());
        } else if (ex instanceof DatabaseException) {
            displayError("Erreur de base de données",
                    "Une erreur est survenue lors de l'accès à la base de données.\n" +
                            "Détails: " + ex.getMessage(),
                    ex);
        } else if (ex instanceof AuthenticationException) {
            displayError("Erreur d'authentification", ex.getMessage());
        } else {
            displayError(title,
                    "Une erreur inattendue est survenue.\n" +
                            "Détails: " + ex.getMessage(),
                    ex);
        }
    }

    /**
     * Gère spécifiquement les exceptions de validation.
     * 
     * @param ex    Exception de validation
     * @param title Titre de la boîte de dialogue
     */
    private static void handleValidationException(ValidationException ex, String title) {
        Optional<ValidationResult> validationResult = Optional.ofNullable(ex.getValidationResult());

        if (validationResult.isPresent()) {
            StringBuilder errorMessage = new StringBuilder("Les erreurs suivantes ont été détectées:\n");
            validationResult.get().getAllErrors().forEach(error -> {
                errorMessage.append("• ").append(error).append("\n");
            });
            displayError(title, errorMessage.toString());
        } else {
            displayError(title, ex.getMessage());
        }
    }

    /**
     * Affiche une boîte de dialogue d'erreur simple.
     * 
     * @param title   Titre de la boîte de dialogue
     * @param message Message d'erreur
     */
    public static void displayError(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText("Erreur");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    /**
     * Affiche une boîte de dialogue d'erreur avec détails techniques.
     * 
     * @param title   Titre de la boîte de dialogue
     * @param message Message d'erreur
     * @param ex      Exception à afficher
     */
    public static void displayError(String title, String message, Throwable ex) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText("Erreur");
            alert.setContentText(message);

            if (ex != null) {
                // Création d'un TextArea pour afficher la pile d'appels
                String exceptionText = getStackTraceAsString(ex);

                Label label = new Label("Détails techniques:");

                TextArea textArea = new TextArea(exceptionText);
                textArea.setEditable(false);
                textArea.setWrapText(true);

                textArea.setMaxWidth(Double.MAX_VALUE);
                textArea.setMaxHeight(Double.MAX_VALUE);
                GridPane.setVgrow(textArea, Priority.ALWAYS);
                GridPane.setHgrow(textArea, Priority.ALWAYS);

                GridPane expandableContent = new GridPane();
                expandableContent.setMaxWidth(Double.MAX_VALUE);
                expandableContent.add(label, 0, 0);
                expandableContent.add(textArea, 0, 1);

                alert.getDialogPane().setExpandableContent(expandableContent);
            }

            alert.showAndWait();
        });
    }

    /**
     * Convertit une pile d'appels en chaîne de caractères.
     * 
     * @param ex Exception
     * @return Chaîne représentant la pile d'appels
     */
    private static String getStackTraceAsString(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        sb.append(ex.toString()).append("\n");

        for (StackTraceElement element : ex.getStackTrace()) {
            sb.append("\tat ").append(element.toString()).append("\n");
        }

        return sb.toString();
    }
}
