package main.java.com.miage.parcauto.exception;

public class DataAccessException extends ParcAutoException {

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}