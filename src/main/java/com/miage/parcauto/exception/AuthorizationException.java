package main.java.com.miage.parcauto.exception;

public class AuthorizationException extends ParcAutoException {

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}