package main.java.com.miage.parcauto.exception;

/**
 * Exception levée lorsqu'une mission n'est pas trouvée.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class MissionNotFoundException extends ParcAutoException {

    /**
     * Constructeur par défaut.
     */
    public MissionNotFoundException() {
        super("Mission non trouvée");
    }

    /**
     * Constructeur avec identifiant de la mission.
     * 
     * @param idMission Identifiant de la mission non trouvée
     */
    public MissionNotFoundException(Integer idMission) {
        super("Mission avec ID " + idMission + " non trouvée");
    }
}
