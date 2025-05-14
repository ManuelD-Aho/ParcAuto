package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.entretien.Entretien;
import main.java.com.miage.parcauto.model.entretien.EntretienStats;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository spécifique pour la gestion des entretiens.
 * Fournit des méthodes de recherche avancées sur les entretiens.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public interface EntretienRepository extends Repository<Entretien, Integer> {
    /**
     * Recherche les entretiens d'un véhicule donné.
     * 
     * @param idVehicule Identifiant du véhicule
     * @return Liste des entretiens du véhicule
     */
    List<Entretien> findByVehicule(int idVehicule);

    /**
     * Recherche les entretiens planifiés entre deux dates.
     * 
     * @param debut Date de début
     * @param fin   Date de fin
     * @return Liste des entretiens planifiés
     */
    List<Entretien> findScheduledBetween(LocalDate debut, LocalDate fin);

    /**
     * Calcule les statistiques d'entretien pour une année donnée (0 = toutes
     * années).
     *
     * @param year Année concernée (0 pour toutes)
     * @return Statistiques d'entretien
     */
    EntretienStats calculateStats(int year);
}
