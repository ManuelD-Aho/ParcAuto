package main.java.com.miage.parcauto.exception;

/**
 * Exception levée lorsqu'une ressource financière n'est pas trouvée.
 * Par exemple, lorsqu'on tente d'accéder à un bilan financier qui n'existe pas.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class FinanceNotFoundException extends ParcAutoException {

    /**
     * Constructeur avec un message d'erreur.
     *
     * @param message message d'erreur
     */
    public FinanceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructeur avec un message d'erreur et une cause.
     *
     * @param message message d'erreur
     * @param cause   cause de l'exception
     */
    public FinanceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
