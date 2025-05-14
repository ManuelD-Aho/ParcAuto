package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.mission.Mission;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository spécifique pour la gestion des missions.
 * Fournit des méthodes de recherche avancées sur les missions.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public interface MissionRepository extends Repository<Mission, Integer> {
    /**
     * Recherche les missions actives pour un véhicule donné.
     * 
     * @param idVehicule Identifiant du véhicule
     * @return Liste des missions actives
     */
    List<Mission> findActiveForVehicule(int idVehicule);

    /**
     * Recherche les missions sur une période donnée.
     * 
     * @param debut Date de début
     * @param fin   Date de fin
     * @return Liste des missions sur la période
     */
    List<Mission> findByPeriod(LocalDate debut, LocalDate fin);
}
