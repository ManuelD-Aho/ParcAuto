package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.finance.Mouvement;
import main.java.com.miage.parcauto.model.finance.TypeMouvement;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface DAO pour l'entité Mouvement.
 * Fournit des opérations de persistance spécifiques pour les mouvements financiers des comptes sociétaires.
 */
public interface MouvementRepository extends Repository<Mouvement, Integer> {

    /**
     * Recherche tous les mouvements associés à un compte sociétaire spécifique.
     *
     * @param conn la connexion à la base de données.
     * @param idCompteSocietaire l'identifiant du compte sociétaire.
     * @return une liste des mouvements pour ce compte.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<Mouvement> findBySocietaireCompteId(Connection conn, Integer idCompteSocietaire) throws SQLException;

    /**
     * Recherche tous les mouvements d'un type spécifique pour un compte sociétaire donné.
     *
     * @param conn la connexion à la base de données.
     * @param idCompteSocietaire l'identifiant du compte sociétaire.
     * @param type le type de mouvement à rechercher.
     * @return une liste des mouvements correspondants.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<Mouvement> findBySocietaireCompteIdAndType(Connection conn, Integer idCompteSocietaire, TypeMouvement type) throws SQLException;

    /**
     * Recherche tous les mouvements financiers effectués dans un intervalle de dates donné.
     *
     * @param conn la connexion à la base de données.
     * @param debut la date de début de l'intervalle.
     * @param fin la date de fin de l'intervalle.
     * @return une liste des mouvements dans cet intervalle.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<Mouvement> findByDateRange(Connection conn, LocalDateTime debut, LocalDateTime fin) throws SQLException;
}