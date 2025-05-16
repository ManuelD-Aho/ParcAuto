package main.java.com.miage.parcauto.exception;

public class AuthenticationException extends ParcAutoException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}