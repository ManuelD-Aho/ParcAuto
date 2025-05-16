package main.java.com.miage.parcauto.exception;

public class VehiculeNotFoundException extends EntityNotFoundException {

    public VehiculeNotFoundException(String message) {
        super(message);
    }

    public VehiculeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}