package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.rh.Fonction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Interface DAO pour l'entité Fonction.
 * Fournit des opérations de persistance spécifiques pour les fonctions du personnel.
 */
public interface FonctionRepository extends Repository<Fonction, Integer> {

    /**
     * Recherche une fonction par son libellé.
     *
     * @param conn la connexion à la base de données.
     * @param libelle le libellé de la fonction à rechercher.
     * @return un Optional contenant la fonction si trouvée, sinon un Optional vide.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    Optional<Fonction> findByLibelle(Connection conn, String libelle) throws SQLException;
}