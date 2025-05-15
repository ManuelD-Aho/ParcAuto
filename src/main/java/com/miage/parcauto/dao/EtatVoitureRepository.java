package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.vehicule.EtatVoiture;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Interface DAO pour l'entité EtatVoiture.
 * Fournit des opérations de persistance spécifiques pour les états des véhicules.
 */
public interface EtatVoitureRepository extends Repository<EtatVoiture, Integer> {

    /**
     * Recherche un état de voiture par son libellé.
     *
     * @param conn la connexion à la base de données.
     * @param libelle le libellé de l'état à rechercher.
     * @return un Optional contenant l'état de voiture si trouvé, sinon un Optional vide.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    Optional<EtatVoiture> findByLibelle(Connection conn, String libelle) throws SQLException;
}