package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.document.DocumentSocietaire;
import main.java.com.miage.parcauto.model.document.TypeDocument;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour l'entité DocumentSocietaire.
 * Fournit des opérations de persistance spécifiques pour les documents des sociétaires.
 */
public interface DocumentSocietaireRepository extends Repository<DocumentSocietaire, Integer> {

    /**
     * Recherche tous les documents associés à un sociétaire spécifique.
     *
     * @param conn la connexion à la base de données.
     * @param idCompteSocietaire l'identifiant du compte sociétaire.
     * @return une liste des documents pour ce sociétaire.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<DocumentSocietaire> findBySocietaireCompteId(Connection conn, Integer idCompteSocietaire) throws SQLException;

    /**
     * Recherche tous les documents d'un type spécifique pour un sociétaire donné.
     *
     * @param conn la connexion à la base de données.
     * @param idCompteSocietaire l'identifiant du compte sociétaire.
     * @param typeDocument le type de document à rechercher.
     * @return une liste des documents correspondants.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<DocumentSocietaire> findBySocietaireCompteIdAndType(Connection conn, Integer idCompteSocietaire, TypeDocument typeDocument) throws SQLException;
}