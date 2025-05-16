package main.java.com.miage.parcauto.exception;

public class SocietaireNotFoundException extends EntityNotFoundException {

    public SocietaireNotFoundException(String message) {
        super(message);
    }

    public SocietaireNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}