package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.rh.Personnel;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Interface DAO pour l'entité Personnel.
 * Fournit des opérations de persistance spécifiques pour les membres du personnel.
 */
public interface PersonnelRepository extends Repository<Personnel, Integer> {

    /**
     * Recherche un membre du personnel par son matricule.
     *
     * @param conn la connexion à la base de données.
     * @param matricule le matricule à rechercher.
     * @return un Optional contenant le membre du personnel si trouvé, sinon un Optional vide.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    Optional<Personnel> findByMatricule(Connection conn, String matricule) throws SQLException;

    /**
     * Recherche un membre du personnel par son adresse email.
     *
     * @param conn la connexion à la base de données.
     * @param email l'adresse email à rechercher.
     * @return un Optional contenant le membre du personnel si trouvé, sinon un Optional vide.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    Optional<Personnel> findByEmail(Connection conn, String email) throws SQLException;

    /**
     * Recherche tous les membres du personnel appartenant à un service spécifique.
     *
     * @param conn la connexion à la base de données.
     * @param idService l'identifiant du service.
     * @return une liste des membres du personnel de ce service.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<Personnel> findByServiceId(Connection conn, Integer idService) throws SQLException;

    /**
     * Recherche tous les membres du personnel ayant une fonction spécifique.
     *
     * @param conn la connexion à la base de données.
     * @param idFonction l'identifiant de la fonction.
     * @return une liste des membres du personnel ayant cette fonction.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<Personnel> findByFonctionId(Connection conn, Integer idFonction) throws SQLException;
}