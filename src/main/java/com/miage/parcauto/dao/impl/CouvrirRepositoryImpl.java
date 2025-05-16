package main.java.com.miage.parcauto.dao.impl;

import main.java.com.miage.parcauto.dao.CouvrirRepository;
import main.java.com.miage.parcauto.model.assurance.Assurance;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;
import main.java.com.miage.parcauto.model.vehicule.Energie;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CouvrirRepositoryImpl implements CouvrirRepository {

    private Vehicule mapResultSetToVehicule(ResultSet rs) throws SQLException {
        Vehicule vehicule = new Vehicule();
        vehicule.setIdVehicule(rs.getInt("v_id_vehicule"));
        vehicule.setIdEtatVoiture(rs.getInt("v_id_etat_voiture"));
        String energieStr = rs.getString("v_energie");
        vehicule.setEnergie(energieStr != null ? Energie.valueOf(energieStr) : null);
        vehicule.setImmatriculation(rs.getString("v_immatriculation"));
        vehicule.setNumeroChassis(rs.getString("v_numero_chassis"));
        vehicule.setMarque(rs.getString("v_marque"));
        vehicule.setModele(rs.getString("v_modele"));
        return vehicule;
    }

    private Assurance mapResultSetToAssurance(ResultSet rs) throws SQLException {
        Assurance assurance = new Assurance();
        assurance.setNumCarteAssurance(rs.getInt("a_num_carte_assurance"));
        assurance.setCompagnie(rs.getString("a_compagnie"));
        assurance.setAdresse(rs.getString("a_adresse"));
        assurance.setTelephone(rs.getString("a_telephone"));
        assurance.setDateDebut(
                rs.getTimestamp("a_date_debut") != null ? rs.getTimestamp("a_date_debut").toLocalDateTime() : null);
        assurance.setDateFin(
                rs.getTimestamp("a_date_fin") != null ? rs.getTimestamp("a_date_fin").toLocalDateTime() : null);
        assurance.setPrix(rs.getBigDecimal("a_prix"));
        return assurance;
    }

    @Override
    public void linkVehiculeToAssurance(Connection conn, Integer idVehicule, Integer numCarteAssurance)
            throws SQLException {
        String sql = "INSERT INTO COUVRIR (id_vehicule, num_carte_assurance) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVehicule);
            pstmt.setInt(2, numCarteAssurance);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void unlinkVehiculeFromAssurance(Connection conn, Integer idVehicule, Integer numCarteAssurance)
            throws SQLException {
        String sql = "DELETE FROM COUVRIR WHERE id_vehicule = ? AND num_carte_assurance = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVehicule);
            pstmt.setInt(2, numCarteAssurance);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void unlinkAllVehiculesFromAssurance(Connection conn, Integer numCarteAssurance) throws SQLException {
        String sql = "DELETE FROM COUVRIR WHERE num_carte_assurance = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, numCarteAssurance);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void unlinkAllAssurancesFromVehicule(Connection conn, Integer idVehicule) throws SQLException {
        String sql = "DELETE FROM COUVRIR WHERE id_vehicule = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVehicule);
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Vehicule> findVehiculesByAssuranceId(Connection conn, Integer numCarteAssurance) throws SQLException {
        List<Vehicule> list = new ArrayList<>();
        String sql = "SELECT v.* FROM VEHICULE v JOIN COUVRIR c ON v.id_vehicule = c.id_vehicule WHERE c.num_carte_assurance = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, numCarteAssurance);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToVehicule(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Assurance> findAssurancesByVehiculeId(Connection conn, Integer idVehicule) throws SQLException {
        List<Assurance> list = new ArrayList<>();
        String sql = "SELECT a.* FROM ASSURANCE a JOIN COUVRIR c ON a.num_carte_assurance = c.num_carte_assurance WHERE c.id_vehicule = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVehicule);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToAssurance(rs));
                }
            }
        }
        return list;
    }

    @Override
    public boolean isLinked(Connection conn, Integer idVehicule, Integer numCarteAssurance) throws SQLException {
        String sql = "SELECT 1 FROM COUVRIR WHERE id_vehicule = ? AND num_carte_assurance = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVehicule);
            pstmt.setInt(2, numCarteAssurance);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}