package main.java.com.miage.parcauto.exception;

/**
 * Exception levée lorsqu'un entretien n'est pas trouvé.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class EntretienNotFoundException extends ParcAutoException {

    /**
     * Constructeur par défaut.
     */
    public EntretienNotFoundException() {
        super("Entretien non trouvé");
    }

    /**
     * Constructeur avec identifiant de l'entretien.
     * 
     * @param idEntretien Identifiant de l'entretien non trouvé
     */
    public EntretienNotFoundException(Integer idEntretien) {
        super("Entretien avec ID " + idEntretien + " non trouvé");
    }
}
