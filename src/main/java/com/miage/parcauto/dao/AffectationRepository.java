package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.affectation.Affectation;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour l'entité Affectation.
 * Fournit des opérations de persistance spécifiques pour les affectations de véhicules.
 */
public interface AffectationRepository extends Repository<Affectation, Integer> {

    /**
     * Recherche les affectations associées à un véhicule spécifique.
     *
     * @param conn la connexion à la base de données.
     * @param idVehicule l'identifiant du véhicule.
     * @return une liste des affectations pour ce véhicule.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<Affectation> findByVehiculeId(Connection conn, Integer idVehicule) throws SQLException;

    /**
     * Recherche les affectations associées à un membre du personnel spécifique.
     *
     * @param conn la connexion à la base de données.
     * @param idPersonnel l'identifiant du membre du personnel.
     * @return une liste des affectations pour ce membre du personnel.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<Affectation> findByPersonnelId(Connection conn, Integer idPersonnel) throws SQLException;

    /**
     * Recherche les affectations associées à un sociétaire spécifique.
     * (Note: La table AFFECTATION n'a pas de lien direct avec SOCIETAIRE_COMPTE,
     * cette méthode pourrait être redondante ou nécessiter une logique métier plus complexe
     * si un sociétaire est lié via un personnel ou une mission).
     * Pour l'instant, nous la laissons pour la complétude si une telle logique existe.
     *
     * @param conn la connexion à la base de données.
     * @param idSocietaire l'identifiant du sociétaire (ou du compte sociétaire).
     * @return une liste des affectations pour ce sociétaire.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<Affectation> findBySocietaireId(Connection conn, Integer idSocietaire) throws SQLException;

}