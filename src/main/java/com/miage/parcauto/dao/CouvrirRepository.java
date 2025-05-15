package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.assurance.Assurance;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour la gestion de la table de jointure COUVRIR
 * entre les Véhicules et les Assurances.
 */
public interface CouvrirRepository {

    /**
     * Lie un véhicule à une assurance.
     *
     * @param conn la connexion à la base de données.
     * @param idVehicule l'identifiant du véhicule.
     * @param numCarteAssurance le numéro de la carte d'assurance.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    void linkVehiculeToAssurance(Connection conn, Integer idVehicule, Integer numCarteAssurance) throws SQLException;

    /**
     * Délier un véhicule d'une assurance spécifique.
     *
     * @param conn la connexion à la base de données.
     * @param idVehicule l'identifiant du véhicule.
     * @param numCarteAssurance le numéro de la carte d'assurance.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    void unlinkVehiculeFromAssurance(Connection conn, Integer idVehicule, Integer numCarteAssurance) throws SQLException;

    /**
     * Délier tous les véhicules d'une assurance spécifique (par exemple, lors de la suppression d'une assurance).
     *
     * @param conn la connexion à la base de données.
     * @param numCarteAssurance le numéro de la carte d'assurance.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    void unlinkAllVehiculesFromAssurance(Connection conn, Integer numCarteAssurance) throws SQLException;

    /**
     * Délier un véhicule de toutes ses assurances (par exemple, lors de la suppression d'un véhicule).
     *
     * @param conn la connexion à la base de données.
     * @param idVehicule l'identifiant du véhicule.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    void unlinkAllAssurancesFromVehicule(Connection conn, Integer idVehicule) throws SQLException;

    /**
     * Trouve tous les véhicules couverts par une assurance spécifique.
     *
     * @param conn la connexion à la base de données.
     * @param numCarteAssurance le numéro de la carte d'assurance.
     * @return une liste d'objets Vehicule.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<Vehicule> findVehiculesByAssuranceId(Connection conn, Integer numCarteAssurance) throws SQLException;

    /**
     * Trouve toutes les assurances couvrant un véhicule spécifique.
     *
     * @param conn la connexion à la base de données.
     * @param idVehicule l'identifiant du véhicule.
     * @return une liste d'objets Assurance.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<Assurance> findAssurancesByVehiculeId(Connection conn, Integer idVehicule) throws SQLException;

    /**
     * Vérifie si un lien spécifique existe entre un véhicule et une assurance.
     *
     * @param conn la connexion à la base de données.
     * @param idVehicule l'identifiant du véhicule.
     * @param numCarteAssurance le numéro de la carte d'assurance.
     * @return true si le lien existe, false sinon.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    boolean isLinked(Connection conn, Integer idVehicule, Integer numCarteAssurance) throws SQLException;
}