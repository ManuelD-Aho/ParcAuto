package main.java.com.miage.parcauto.exception;

/**
 * Exception levée lorsqu'un problème d'authentification survient.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class AuthenticationException extends ParcAutoException {

    /**
     * Constructeur par défaut.
     */
    public AuthenticationException() {
        super("Erreur d'authentification");
    }

    /**
     * Constructeur avec message.
     * 
     * @param message Message d'erreur
     */
    public AuthenticationException(String message) {
        super(message);
    }

    /**
     * Constructeur avec message et cause.
     * 
     * @param message Message d'erreur
     * @param cause   Cause de l'exception
     */
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
