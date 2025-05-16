package main.java.com.miage.parcauto.exception;

public class ValidationException extends ParcAutoException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}