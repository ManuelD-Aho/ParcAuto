package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.dto.BilanFinancierDTO;
import main.java.com.miage.parcauto.dto.CoutEntretienDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface DAO pour les opérations financières complexes et les rapports.
 * Ne suit pas le modèle CRUD générique.
 */
public interface FinanceRepository {

    /**
     * Calcule le bilan financier sur une période donnée.
     * Inclut les revenus des missions et les coûts (entretiens, assurances, etc.).
     *
     * @param conn la connexion à la base de données.
     * @param debut la date de début de la période.
     * @param fin la date de fin de la période.
     * @return un objet BilanFinancierDTO contenant les informations agrégées.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    BilanFinancierDTO getBilanPeriode(Connection conn, LocalDate debut, LocalDate fin) throws SQLException;

    /**
     * Calcule les coûts d'entretien par véhicule pour une année donnée.
     *
     * @param conn la connexion à la base de données.
     * @param annee l'année pour laquelle calculer les coûts.
     * @return une liste d'objets CoutEntretienDTO, un par véhicule ayant eu des entretiens.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<CoutEntretienDTO> getCoutEntretienParVehiculePourAnnee(Connection conn, int annee) throws SQLException;

}