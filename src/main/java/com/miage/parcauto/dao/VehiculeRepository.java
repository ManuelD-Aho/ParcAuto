package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.vehicule.EtatVoiture;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;
import java.util.List;
import java.util.Optional;

/**
 * Repository spécifique pour la gestion des véhicules.
 * Fournit des méthodes de recherche avancées sur les véhicules.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public interface VehiculeRepository extends Repository<Vehicule, Integer> {
    /**
     * Recherche les véhicules par état.
     * 
     * @param etat État du véhicule
     * @return Liste des véhicules correspondant à l'état
     */
    List<Vehicule> findByEtat(EtatVoiture etat);

    /**
     * Recherche les véhicules nécessitant une maintenance.
     * 
     * @param kmThreshold Seuil kilométrique
     * @return Liste des véhicules à entretenir
     */
    List<Vehicule> findRequiringMaintenance(int kmThreshold);
}
