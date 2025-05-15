package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.rh.Service; // Attention, c'est bien model.rh.Service

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Interface DAO pour l'entité Service (RH).
 * Fournit des opérations de persistance spécifiques pour les services/départements de l'entreprise.
 */
public interface ServiceRHRepository extends Repository<Service, Integer> {

    /**
     * Recherche un service par son libellé.
     *
     * @param conn la connexion à la base de données.
     * @param libelle le libellé du service à rechercher.
     * @return un Optional contenant le service si trouvé, sinon un Optional vide.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    Optional<Service> findByLibelle(Connection conn, String libelle) throws SQLException;
}