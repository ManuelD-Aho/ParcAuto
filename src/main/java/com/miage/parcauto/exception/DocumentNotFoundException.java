package main.java.com.miage.parcauto.exception;

public class DocumentNotFoundException extends EntityNotFoundException {

    public DocumentNotFoundException(String message) {
        super(message);
    }

    public DocumentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}