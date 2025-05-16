package main.java.com.miage.parcauto.dao.impl;

import main.java.com.miage.parcauto.dao.DepenseMissionRepository;
import main.java.com.miage.parcauto.model.mission.DepenseMission;
import main.java.com.miage.parcauto.model.mission.NatureDepenseMission;
import main.java.com.miage.parcauto.exception.DataAccessException;
import main.java.com.miage.parcauto.util.DbUtil; // Assurez-vous que DbUtil gère correctement les connexions

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DepenseMissionRepositoryImpl implements DepenseMissionRepository {

    private DepenseMission mapResultSetToDepenseMission(ResultSet rs) throws SQLException {
        DepenseMission depense = new DepenseMission();
        depense.setIdDepense(rs.getInt("id_depense"));
        depense.setIdMission(rs.getInt("id_mission"));
        String natureStr = rs.getString("nature");
        if (natureStr != null) {
            try {
                depense.setNature(NatureDepenseMission.valueOf(natureStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                // Gérer le cas où la valeur de la DB ne correspond à aucune enum
                // Par exemple, logger une erreur ou affecter une valeur par défaut
                System.err.println("Valeur NatureDepenseMission non valide depuis la DB: " + natureStr);
                depense.setNature(null); // Ou une valeur par défaut appropriée
            }
        } else {
            depense.setNature(null);
        }
        depense.setMontant(rs.getBigDecimal("montant"));
        Timestamp dateDepenseTs = rs.getTimestamp("date_depense");
        if (dateDepenseTs != null) {
            depense.setDateDepense(dateDepenseTs.toLocalDateTime());
        }
        depense.setJustificatif(rs.getString("justificatif"));
        depense.setObservation(rs.getString("observation"));
        return depense;
    }

    @Override
    public Optional<DepenseMission> findById(Integer id) {
        String sql = "SELECT * FROM DEPENSE_MISSION WHERE id_depense = ?";
        try (Connection conn = DbUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToDepenseMission(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche de la dépense de mission par ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<DepenseMission> findAll() {
        List<DepenseMission> depenses = new ArrayList<>();
        String sql = "SELECT * FROM DEPENSE_MISSION";
        try (Connection conn = DbUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                depenses.add(mapResultSetToDepenseMission(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération de toutes les dépenses de mission", e);
        }
        return depenses;
    }

    @Override
    public List<DepenseMission> findAll(int page, int size) {
        List<DepenseMission> depenses = new ArrayList<>();
        String sql = "SELECT * FROM DEPENSE_MISSION LIMIT ? OFFSET ?";
        try (Connection conn = DbUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, page * size); // Correct pour offset 0-based si page est 0-based
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    depenses.add(mapResultSetToDepenseMission(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération paginée des dépenses de mission", e);
        }
        return depenses;
    }

    @Override
    public DepenseMission save(DepenseMission depense) {
        String sql = "INSERT INTO DEPENSE_MISSION (id_mission, nature, montant, date_depense, justificatif, observation) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, depense.getIdMission());
            pstmt.setString(2, depense.getNature() != null ? depense.getNature().name() : null); // Utiliser .name()
                                                                                                 // pour Enum
            pstmt.setBigDecimal(3, depense.getMontant());
            pstmt.setTimestamp(4,
                    depense.getDateDepense() != null ? Timestamp.valueOf(depense.getDateDepense()) : null);
            pstmt.setString(5, depense.getJustificatif());
            pstmt.setString(6, depense.getObservation());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La création de la dépense de mission a échoué, aucune ligne affectée.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    depense.setIdDepense(generatedKeys.getInt(1));
                } else {
                    throw new DataAccessException("La création de la dépense de mission a échoué, aucun ID obtenu.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la sauvegarde de la dépense de mission pour la mission ID: "
                    + (depense != null ? depense.getIdMission() : "null"), e);
        }
        return depense;
    }

    @Override
    public DepenseMission update(DepenseMission depense) {
        String sql = "UPDATE DEPENSE_MISSION SET id_mission = ?, nature = ?, montant = ?, date_depense = ?, justificatif = ?, observation = ? WHERE id_depense = ?";
        try (Connection conn = DbUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, depense.getIdMission());
            pstmt.setString(2, depense.getNature() != null ? depense.getNature().name() : null); // Utiliser .name()
                                                                                                 // pour Enum
            pstmt.setBigDecimal(3, depense.getMontant());
            pstmt.setTimestamp(4,
                    depense.getDateDepense() != null ? Timestamp.valueOf(depense.getDateDepense()) : null);
            pstmt.setString(5, depense.getJustificatif());
            pstmt.setString(6, depense.getObservation());
            pstmt.setInt(7, depense.getIdDepense());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La mise à jour de la dépense de mission avec ID "
                        + depense.getIdDepense() + " a échoué, aucune ligne affectée.");
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Erreur lors de la mise à jour de la dépense de mission: "
                            + (depense != null ? depense.getIdDepense() : "null"),
                    e);
        }
        return depense;
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM DEPENSE_MISSION WHERE id_depense = ?";
        try (Connection conn = DbUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la suppression de la dépense de mission: " + id, e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM DEPENSE_MISSION";
        try (Connection conn = DbUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors du comptage des dépenses de mission", e);
        }
        return 0;
    }

    @Override
    public List<DepenseMission> findByMissionId(Integer idMission) {
        List<DepenseMission> depenses = new ArrayList<>();
        String sql = "SELECT * FROM DEPENSE_MISSION WHERE id_mission = ?";
        try (Connection conn = DbUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMission);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    depenses.add(mapResultSetToDepenseMission(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche des dépenses pour la mission ID: " + idMission,
                    e);
        }
        return depenses;
    }
}