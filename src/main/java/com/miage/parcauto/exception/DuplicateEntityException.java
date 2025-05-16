package main.java.com.miage.parcauto.exception;

public class DuplicateEntityException extends ParcAutoException {

    public DuplicateEntityException(String message) {
        super(message);
    }

    public DuplicateEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}