package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.finance.SocietaireCompte;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Interface DAO pour l'entité SocietaireCompte.
 * Fournit des opérations de persistance spécifiques pour les comptes des sociétaires.
 */
public interface SocietaireCompteRepository extends Repository<SocietaireCompte, Integer> {

    /**
     * Recherche un compte sociétaire par son numéro de compte unique.
     *
     * @param conn la connexion à la base de données.
     * @param numeroCompte le numéro de compte à rechercher.
     * @return un Optional contenant le compte sociétaire si trouvé, sinon un Optional vide.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    Optional<SocietaireCompte> findByNumeroCompte(Connection conn, String numeroCompte) throws SQLException;

    /**
     * Recherche un compte sociétaire associé à un membre du personnel spécifique.
     * (Utile si un membre du personnel peut aussi être sociétaire et avoir un compte).
     *
     * @param conn la connexion à la base de données.
     * @param idPersonnel l'identifiant du membre du personnel.
     * @return un Optional contenant le compte sociétaire si trouvé, sinon un Optional vide.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    Optional<SocietaireCompte> findByPersonnelId(Connection conn, Integer idPersonnel) throws SQLException;
}