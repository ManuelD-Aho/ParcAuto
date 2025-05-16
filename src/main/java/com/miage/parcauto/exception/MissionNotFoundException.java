package main.java.com.miage.parcauto.exception;

public class MissionNotFoundException extends EntityNotFoundException {

    public MissionNotFoundException(String message) {
        super(message);
    }

    public MissionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}