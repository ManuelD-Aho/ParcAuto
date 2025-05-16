package main.java.com.miage.parcauto.exception;

public class UtilisateurNotFoundException extends EntityNotFoundException {

    public UtilisateurNotFoundException(String message) {
        super(message);
    }

    public UtilisateurNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}