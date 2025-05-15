package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.entretien.Entretien;
import main.java.com.miage.parcauto.model.entretien.StatutOT;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface DAO pour l'entité Entretien.
 * Fournit des opérations de persistance spécifiques pour les entretiens des véhicules.
 */
public interface EntretienRepository extends Repository<Entretien, Integer> {

    /**
     * Recherche les entretiens associés à un véhicule spécifique.
     *
     * @param conn la connexion à la base de données.
     * @param idVehicule l'identifiant du véhicule.
     * @return une liste des entretiens pour ce véhicule.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<Entretien> findByVehiculeId(Connection conn, Integer idVehicule) throws SQLException;

    /**
     * Recherche les entretiens planifiés ou réalisés dans un intervalle de dates donné.
     * La recherche se base sur la date de planification (date_prevue).
     *
     * @param conn la connexion à la base de données.
     * @param debut la date de début de l'intervalle.
     * @param fin la date de fin de l'intervalle.
     * @return une liste des entretiens dans cet intervalle.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<Entretien> findScheduledBetween(Connection conn, LocalDateTime debut, LocalDateTime fin) throws SQLException;

    /**
     * Recherche les entretiens pour un véhicule spécifique et un statut donné.
     *
     * @param conn la connexion à la base de données.
     * @param idVehicule l'identifiant du véhicule.
     * @param statut le statut de l'ordre de travail (OT).
     * @return une liste des entretiens correspondant aux critères.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<Entretien> findByVehiculeIdAndStatut(Connection conn, Integer idVehicule, StatutOT statut) throws SQLException;
}