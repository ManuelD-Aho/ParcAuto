package main.java.com.miage.parcauto.exception;

/**
 * Exception levée lors d'erreurs de base de données.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class DatabaseException extends ParcAutoException {

    /**
     * Constructeur par défaut.
     */
    public DatabaseException() {
        super("Erreur de base de données");
    }

    /**
     * Constructeur avec message.
     * 
     * @param message Message d'erreur
     */
    public DatabaseException(String message) {
        super(message);
    }

    /**
     * Constructeur avec message et cause.
     * 
     * @param message Message d'erreur
     * @param cause   Cause de l'exception
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructeur avec cause.
     * 
     * @param cause Cause de l'exception
     */
    public DatabaseException(Throwable cause) {
        super("Erreur de base de données", cause);
    }
}
