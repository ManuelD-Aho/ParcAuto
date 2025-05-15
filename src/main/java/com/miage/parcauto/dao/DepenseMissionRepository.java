package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.mission.DepenseMission;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour l'entité DepenseMission.
 * Fournit des opérations de persistance spécifiques pour les dépenses associées aux missions.
 */
public interface DepenseMissionRepository extends Repository<DepenseMission, Integer> {

    /**
     * Recherche toutes les dépenses associées à une mission spécifique.
     *
     * @param conn la connexion à la base de données.
     * @param idMission l'identifiant de la mission.
     * @return une liste des dépenses pour cette mission.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<DepenseMission> findByMissionId(Connection conn, Integer idMission) throws SQLException;
}