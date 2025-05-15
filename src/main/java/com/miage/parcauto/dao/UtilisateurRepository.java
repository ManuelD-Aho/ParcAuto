package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.utilisateur.Utilisateur;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Interface DAO pour l'entité Utilisateur.
 * Fournit des opérations de persistance spécifiques pour les utilisateurs du système.
 */
public interface UtilisateurRepository extends Repository<Utilisateur, Integer> {

    /**
     * Recherche un utilisateur par son login.
     *
     * @param conn la connexion à la base de données.
     * @param login le login de l'utilisateur à rechercher.
     * @return un Optional contenant l'utilisateur si trouvé, sinon un Optional vide.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    Optional<Utilisateur> findByLogin(Connection conn, String login) throws SQLException;

    /**
     * Recherche un utilisateur par l'identifiant du personnel auquel il est associé.
     *
     * @param conn la connexion à la base de données.
     * @param idPersonnel l'identifiant du personnel.
     * @return un Optional contenant l'utilisateur si trouvé, sinon un Optional vide.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    Optional<Utilisateur> findByIdPersonnel(Connection conn, Integer idPersonnel) throws SQLException;
}