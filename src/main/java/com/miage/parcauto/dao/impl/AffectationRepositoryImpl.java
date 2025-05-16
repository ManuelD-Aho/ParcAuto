package main.java.com.miage.parcauto.dao.impl;

import main.java.com.miage.parcauto.dao.AffectationRepository;
import main.java.com.miage.parcauto.model.affectation.Affectation;
import main.java.com.miage.parcauto.model.affectation.TypeAffectation;

import java.sql.*;
import java.util.*;

public class AffectationRepositoryImpl implements AffectationRepository {

    private Affectation mapResultSetToAffectation(ResultSet rs) throws SQLException {
        Affectation affectation = new Affectation();
        affectation.setId(rs.getInt("id_affectation"));
        affectation.setIdVehicule(rs.getInt("id_vehicule"));
        int idPersonnel = rs.getInt("id_personnel");
        affectation.setIdPersonnel(rs.wasNull() ? null : idPersonnel);
        int idSocietaire = rs.getInt("id_societaire");
        affectation.setIdSocietaire(rs.wasNull() ? null : idSocietaire);
        String typeStr = rs.getString("type");
        affectation.setTypeAffectation(typeStr != null ? TypeAffectation.valueOf(typeStr) : null);
        affectation.setDateDebut(
                rs.getTimestamp("date_debut") != null ? rs.getTimestamp("date_debut").toLocalDateTime() : null);
        affectation
                .setDateFin(rs.getTimestamp("date_fin") != null ? rs.getTimestamp("date_fin").toLocalDateTime() : null);
        return affectation;
    }

    @Override
    public Optional<Affectation> findById(Connection conn, Integer id) throws SQLException {
        String sql = "SELECT * FROM AFFECTATION WHERE id_affectation = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAffectation(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Affectation> findAll(Connection conn) throws SQLException {
        List<Affectation> list = new ArrayList<>();
        String sql = "SELECT * FROM AFFECTATION";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToAffectation(rs));
            }
        }
        return list;
    }

    @Override
    public List<Affectation> findAll(Connection conn, int page, int size) throws SQLException {
        List<Affectation> list = new ArrayList<>();
        String sql = "SELECT * FROM AFFECTATION LIMIT ? OFFSET ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, page * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToAffectation(rs));
                }
            }
        }
        return list;
    }

    @Override
    public Affectation save(Connection conn, Affectation affectation) throws SQLException {
        String sql = "INSERT INTO AFFECTATION (id_vehicule, id_personnel, id_societaire, type, date_debut, date_fin) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, affectation.getIdVehicule());
            if (affectation.getIdPersonnel() != null) {
                pstmt.setInt(2, affectation.getIdPersonnel());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            if (affectation.getIdSocietaire() != null) {
                pstmt.setInt(3, affectation.getIdSocietaire());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setString(4,
                    affectation.getTypeAffectation() != null ? affectation.getTypeAffectation().name() : null);
            pstmt.setTimestamp(5,
                    affectation.getDateDebut() != null ? Timestamp.valueOf(affectation.getDateDebut()) : null);
            pstmt.setTimestamp(6,
                    affectation.getDateFin() != null ? Timestamp.valueOf(affectation.getDateFin()) : null);
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    affectation.setId(rs.getInt(1));
                }
            }
        }
        return affectation;
    }

    @Override
    public Affectation update(Connection conn, Affectation affectation) throws SQLException {
        String sql = "UPDATE AFFECTATION SET id_vehicule = ?, id_personnel = ?, id_societaire = ?, type = ?, date_debut = ?, date_fin = ? WHERE id_affectation = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, affectation.getIdVehicule());
            if (affectation.getIdPersonnel() != null) {
                pstmt.setInt(2, affectation.getIdPersonnel());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            if (affectation.getIdSocietaire() != null) {
                pstmt.setInt(3, affectation.getIdSocietaire());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setString(4,
                    affectation.getTypeAffectation() != null ? affectation.getTypeAffectation().name() : null);
            pstmt.setTimestamp(5,
                    affectation.getDateDebut() != null ? Timestamp.valueOf(affectation.getDateDebut()) : null);
            pstmt.setTimestamp(6,
                    affectation.getDateFin() != null ? Timestamp.valueOf(affectation.getDateFin()) : null);
            pstmt.setInt(7, affectation.getId());
            pstmt.executeUpdate();
        }
        return affectation;
    }

    @Override
    public boolean delete(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM AFFECTATION WHERE id_affectation = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public long count(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM AFFECTATION";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0;
    }

    @Override
    public List<Affectation> findByVehiculeId(Connection conn, Integer idVehicule) throws SQLException {
        List<Affectation> list = new ArrayList<>();
        String sql = "SELECT * FROM AFFECTATION WHERE id_vehicule = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVehicule);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToAffectation(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Affectation> findByPersonnelId(Connection conn, Integer idPersonnel) throws SQLException {
        List<Affectation> list = new ArrayList<>();
        String sql = "SELECT * FROM AFFECTATION WHERE id_personnel = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPersonnel);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToAffectation(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Affectation> findBySocietaireId(Connection conn, Integer idSocietaire) throws SQLException {
        List<Affectation> list = new ArrayList<>();
        String sql = "SELECT * FROM AFFECTATION WHERE id_societaire = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idSocietaire);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToAffectation(rs));
                }
            }
        }
        return list;
    }
}