package main.java.com.miage.parcauto.exception;

import main.java.com.miage.parcauto.service.ValidationService.ValidationResult;

/**
 * Exception levée lors d'erreurs de validation.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class ValidationException extends ParcAutoException {

    private final ValidationResult validationResult;

    /**
     * Constructeur par défaut.
     */
    public ValidationException() {
        super("Erreur de validation");
        this.validationResult = null;
    }

    /**
     * Constructeur avec message.
     * 
     * @param message Message d'erreur
     */
    public ValidationException(String message) {
        super(message);
        this.validationResult = null;
    }

    /**
     * Constructeur avec résultat de validation.
     * 
     * @param validationResult Résultat de la validation contenant les erreurs
     */
    public ValidationException(ValidationResult validationResult) {
        super("Erreur de validation: " + String.join(", ", validationResult.getAllErrors()));
        this.validationResult = validationResult;
    }

    /**
     * Constructeur avec message et résultat de validation.
     * 
     * @param message          Message d'erreur
     * @param validationResult Résultat de la validation contenant les erreurs
     */
    public ValidationException(String message, ValidationResult validationResult) {
        super(message);
        this.validationResult = validationResult;
    }

    /**
     * Retourne le résultat de validation.
     * 
     * @return Résultat de la validation ou null si non disponible
     */
    public ValidationResult getValidationResult() {
        return validationResult;
    }
}
