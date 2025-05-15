package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.mission.Mission;
import main.java.com.miage.parcauto.model.mission.StatutMission;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface DAO pour l'entité Mission.
 * Fournit des opérations de persistance spécifiques pour les missions des véhicules.
 */
public interface MissionRepository extends Repository<Mission, Integer> {

    /**
     * Recherche les missions actives (non clôturées) pour un véhicule spécifique.
     *
     * @param conn la connexion à la base de données.
     * @param idVehicule l'identifiant du véhicule.
     * @return une liste des missions actives pour ce véhicule.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<Mission> findActiveForVehicule(Connection conn, Integer idVehicule) throws SQLException;

    /**
     * Recherche les missions dont la date de début ou de fin se situe dans un intervalle donné.
     *
     * @param conn la connexion à la base de données.
     * @param debut la date de début de l'intervalle.
     * @param fin la date de fin de l'intervalle.
     * @return une liste des missions dans cet intervalle.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<Mission> findByPeriod(Connection conn, LocalDateTime debut, LocalDateTime fin) throws SQLException;

    /**
     * Recherche les missions pour un véhicule spécifique.
     *
     * @param conn la connexion à la base de données.
     * @param idVehicule l'identifiant du véhicule.
     * @return une liste des missions pour ce véhicule.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<Mission> findByVehiculeId(Connection conn, Integer idVehicule) throws SQLException;

    /**
     * Recherche les missions pour un véhicule spécifique et un statut donné.
     *
     * @param conn la connexion à la base de données.
     * @param idVehicule l'identifiant du véhicule.
     * @param statut le statut de la mission.
     * @return une liste des missions correspondant aux critères.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<Mission> findByVehiculeIdAndStatus(Connection conn, Integer idVehicule, StatutMission statut) throws SQLException;
}