package main.java.com.miage.parcauto.dao.impl;

import main.java.com.miage.parcauto.dao.FinanceRepository;
import main.java.com.miage.parcauto.dto.BilanFinancierDTO;
import main.java.com.miage.parcauto.dto.CoutEntretienDTO;
import main.java.com.miage.parcauto.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class FinanceRepositoryImpl implements FinanceRepository {

    @Override
    public BilanFinancierDTO getBilanPeriode(Connection conn, LocalDate debut, LocalDate fin) throws SQLException {
        BilanFinancierDTO bilan = new BilanFinancierDTO();
        Timestamp tsDebut = Timestamp.valueOf(debut.atStartOfDay());
        Timestamp tsFin = Timestamp.valueOf(fin.plusDays(1).atStartOfDay()); // Exclusif pour la fin

        // Note: Le calcul des revenus de mission est complexe s'il n'y a pas de champ "prix" dans Mission.
        // On va supposer pour l'instant que ce calcul est soit simplifié, soit non inclus directement ici.
        // Pour cet exemple, nous allons nous concentrer sur les coûts.
        // Vous devrez adapter cette requête en fonction de comment les revenus sont modélisés.

        // Coûts des entretiens
        String sqlCoutsEntretiens = "SELECT SUM(cout_reel) FROM ENTRETIEN WHERE date_realisation >= ? AND date_realisation < ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlCoutsEntretiens)) {
            pstmt.setTimestamp(1, tsDebut);
            pstmt.setTimestamp(2, tsFin);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal cout = rs.getBigDecimal(1);
                    bilan.setTotalCoutsEntretiens(cout != null ? cout : BigDecimal.ZERO);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors du calcul des coûts d'entretiens pour le bilan.", e);
        }

        // Coûts des assurances (prorata complexe, ici simplifié: assurances actives pendant la période)
        String sqlCoutsAssurances = "SELECT SUM(prix) FROM ASSURANCE WHERE date_debut < ? AND date_fin > ?"; // Simplifié
        try (PreparedStatement pstmt = conn.prepareStatement(sqlCoutsAssurances)) {
            pstmt.setTimestamp(1, tsFin);
            pstmt.setTimestamp(2, tsDebut);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal cout = rs.getBigDecimal(1);
                    bilan.setTotalCoutsAssurances(cout != null ? cout : BigDecimal.ZERO);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors du calcul des coûts d'assurances pour le bilan.", e);
        }

        // Coûts des dépenses de mission (carburant, frais annexes)
        String sqlDepensesMissions = "SELECT SUM(montant) FROM DEPENSE_MISSION WHERE date_depense >= ? AND date_depense < ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sqlDepensesMissions)) {
            pstmt.setTimestamp(1, tsDebut);
            pstmt.setTimestamp(2, tsFin);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal cout = rs.getBigDecimal(1);
                    bilan.setTotalDepensesMissions(cout != null ? cout : BigDecimal.ZERO);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors du calcul des dépenses de missions pour le bilan.", e);
        }


        // Calcul du résultat net (simplifié ici, car revenus non calculés)
        // resultatNet = Revenus - (Entretiens + Assurances + DepensesMissions)
        bilan.setResultatNet(
                bilan.getTotalRevenusMissions()
                        .subtract(bilan.getTotalCoutsEntretiens())
                        .subtract(bilan.getTotalCoutsAssurances())
                        .subtract(bilan.getTotalDepensesMissions())
        );

        return bilan;
    }

    @Override
    public List<CoutEntretienDTO> getCoutEntretienParVehiculePourAnnee(Connection conn, int annee) throws SQLException {
        List<CoutEntretienDTO> couts = new ArrayList<>();
        String sql = "SELECT v.id_vehicule, v.immatriculation, v.marque, v.modele, SUM(e.cout_reel) as cout_total, COUNT(e.id_entretien) as nb_entretiens " +
                "FROM VEHICULES v " +
                "JOIN ENTRETIEN e ON v.id_vehicule = e.id_vehicule " +
                "WHERE YEAR(e.date_realisation) = ? AND e.cout_reel IS NOT NULL " +
                "GROUP BY v.id_vehicule, v.immatriculation, v.marque, v.modele " +
                "ORDER BY cout_total DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, annee);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    CoutEntretienDTO dto = new CoutEntretienDTO();
                    dto.setIdVehicule(rs.getInt("id_vehicule"));
                    dto.setImmatriculationVehicule(rs.getString("immatriculation"));
                    dto.setMarqueVehicule(rs.getString("marque"));
                    dto.setModeleVehicule(rs.getString("modele"));
                    dto.setCoutTotalEntretien(rs.getBigDecimal("cout_total"));
                    dto.setNombreEntretiens(rs.getInt("nb_entretiens"));
                    couts.add(dto);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors du calcul des coûts d'entretien par véhicule pour l'année " + annee, e);
        }
        return couts;
    }
}