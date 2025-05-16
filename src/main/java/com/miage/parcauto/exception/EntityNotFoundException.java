package main.java.com.miage.parcauto.exception;

public class EntityNotFoundException extends ParcAutoException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}