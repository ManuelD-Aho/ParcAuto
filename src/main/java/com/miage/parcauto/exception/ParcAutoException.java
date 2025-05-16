package main.java.com.miage.parcauto.exception;

public class ParcAutoException extends Exception {

    public ParcAutoException(String message) {
        super(message);
    }

    public ParcAutoException(String message, Throwable cause) {
        super(message, cause);
    }
}