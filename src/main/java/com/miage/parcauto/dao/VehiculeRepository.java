package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.vehicule.Vehicule;
import main.java.com.miage.parcauto.model.vehicule.Energie;
// L'import de EtatVoiture n'est pas nécessaire ici si on utilise que l'ID dans findByEtatVoitureId
// import main.java.com.miage.parcauto.model.vehicule.EtatVoiture;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Interface DAO pour l'entité Vehicule.
 * Fournit des opérations de persistance spécifiques pour les véhicules du parc.
 */
public interface VehiculeRepository extends Repository<Vehicule, Integer> {

    /**
     * Recherche les véhicules par leur état.
     * La méthode originale demandait un objet EtatVoiture, mais il est plus direct
     * pour un DAO de prendre un ID. Le service peut faire la conversion si nécessaire.
     *
     * @param conn la connexion à la base de données.
     * @param idEtatVoiture l'identifiant de l'état du véhicule.
     * @return une liste des véhicules correspondant à cet état.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<Vehicule> findByEtatVoitureId(Connection conn, Integer idEtatVoiture) throws SQLException;

    /**
     * Recherche les véhicules nécessitant une maintenance.
     * La logique exacte (par exemple, km_actuels >= km_prochain_entretien - kmSeuil)
     * dépendra de la structure de la table et des informations disponibles.
     *
     * @param conn la connexion à la base de données.
     * @param kmSeuilProchainEntretien le seuil de kilométrage avant le prochain entretien.
     *        La signification exacte de ce paramètre dépend de la requête SQL sous-jacente.
     * @return une liste des véhicules nécessitant une maintenance.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<Vehicule> findRequiringMaintenance(Connection conn, int kmSeuilProchainEntretien) throws SQLException;

    /**
     * Recherche un véhicule par son numéro d'immatriculation.
     *
     * @param conn la connexion à la base de données.
     * @param immatriculation le numéro d'immatriculation à rechercher.
     * @return un Optional contenant le véhicule si trouvé, sinon un Optional vide.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    Optional<Vehicule> findByImmatriculation(Connection conn, String immatriculation) throws SQLException;

    /**
     * Recherche un véhicule par son numéro de châssis.
     *
     * @param conn la connexion à la base de données.
     * @param numeroChassi le numéro de châssis à rechercher.
     * @return un Optional contenant le véhicule si trouvé, sinon un Optional vide.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    Optional<Vehicule> findByNumeroChassi(Connection conn, String numeroChassi) throws SQLException;

    /**
     * Recherche les véhicules par leur type d'énergie.
     *
     * @param conn la connexion à la base de données.
     * @param energie le type d'énergie.
     * @return une liste des véhicules utilisant ce type d'énergie.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<Vehicule> findByEnergie(Connection conn, Energie energie) throws SQLException;
}