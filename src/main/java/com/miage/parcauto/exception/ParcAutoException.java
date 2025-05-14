package main.java.com.miage.parcauto.exception;

/**
 * Classe racine pour toutes les exceptions spécifiques de l'application
 * ParcAuto.
 * Permet de capturer toutes les exceptions métier dans un même type.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class ParcAutoException extends RuntimeException {

    /**
     * Constructeur par défaut.
     */
    public ParcAutoException() {
        super();
    }

    /**
     * Constructeur avec message d'erreur.
     * 
     * @param message Le message d'erreur
     */
    public ParcAutoException(String message) {
        super(message);
    }

    /**
     * Constructeur avec message d'erreur et cause.
     * 
     * @param message Le message d'erreur
     * @param cause   La cause de l'exception
     */
    public ParcAutoException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructeur avec cause.
     * 
     * @param cause La cause de l'exception
     */
    public ParcAutoException(Throwable cause) {
        super(cause);
    }
}
