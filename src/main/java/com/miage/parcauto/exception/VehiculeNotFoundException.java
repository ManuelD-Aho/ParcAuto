package main.java.com.miage.parcauto.exception;

/**
 * Exception levée lorsqu'un véhicule n'est pas trouvé.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class VehiculeNotFoundException extends ParcAutoException {

    /**
     * Constructeur par défaut.
     */
    public VehiculeNotFoundException() {
        super("Véhicule non trouvé");
    }

    /**
     * Constructeur avec identifiant du véhicule.
     * 
     * @param idVehicule Identifiant du véhicule non trouvé
     */
    public VehiculeNotFoundException(Integer idVehicule) {
        super("Véhicule avec ID " + idVehicule + " non trouvé");
    }
}
