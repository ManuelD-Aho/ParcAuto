package main.java.com.miage.parcauto.exception;

public class EntretienNotFoundException extends EntityNotFoundException {

    public EntretienNotFoundException(String message) {
        super(message);
    }

    public EntretienNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}