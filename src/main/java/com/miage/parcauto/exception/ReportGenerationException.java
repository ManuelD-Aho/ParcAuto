package main.java.com.miage.parcauto.exception;

public class ReportGenerationException extends ParcAutoException {

    public ReportGenerationException(String message) {
        super(message);
    }

    public ReportGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}