package main.java.com.miage.parcauto.exception;

public class OperationFailedException extends ParcAutoException {

    public OperationFailedException(String message) {
        super(message);
    }

    public OperationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}