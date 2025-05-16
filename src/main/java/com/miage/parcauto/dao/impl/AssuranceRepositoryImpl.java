package main.java.com.miage.parcauto.dao.impl;

import main.java.com.miage.parcauto.dao.AssuranceRepository;
import main.java.com.miage.parcauto.model.assurance.Assurance;
import main.java.com.miage.parcauto.util.DbUtil;
import main.java.com.miage.parcauto.exception.DataAccessException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AssuranceRepositoryImpl implements AssuranceRepository {

    private Assurance mapResultSetToAssurance(ResultSet rs) throws SQLException {
        Assurance assurance = new Assurance();
        assurance.setNumCarteAssurance(rs.getInt("num_carte_assurance"));
        assurance.setCompagnie(rs.getString("compagnie"));
        Timestamp dateDebutTs = rs.getTimestamp("date_debut");
        if (dateDebutTs != null) {
            assurance.setDateDebut(dateDebutTs.toLocalDateTime());
        }
        Timestamp dateFinTs = rs.getTimestamp("date_fin");
        if (dateFinTs != null) {
            assurance.setDateFin(dateFinTs.toLocalDateTime());
        }
        assurance.setCout(rs.getBigDecimal("cout"));
        return assurance;
    }

    @Override
    public Optional<Assurance> findById(Integer id) throws DataAccessException {
        String sql = "SELECT num_carte_assurance, compagnie, date_debut, date_fin, cout FROM assurances WHERE num_carte_assurance = ?";
        try (Connection conn = DbUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAssurance(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche de l'assurance par ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Assurance> findAll() throws DataAccessException {
        List<Assurance> assurances = new ArrayList<>();
        String sql = "SELECT num_carte_assurance, compagnie, date_debut, date_fin, cout FROM assurances";
        try (Connection conn = DbUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                assurances.add(mapResultSetToAssurance(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération de toutes les assurances", e);
        }
        return assurances;
    }

    @Override
    public List<Assurance> findAll(int page, int size) throws DataAccessException {
        List<Assurance> assurances = new ArrayList<>();
        String sql = "SELECT num_carte_assurance, compagnie, date_debut, date_fin, cout FROM assurances LIMIT ? OFFSET ?";
        try (Connection conn = DbUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, page * size); // Assumant que 'page' est 0-indexed
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    assurances.add(mapResultSetToAssurance(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération paginée des assurances", e);
        }
        return assurances;
    }

    @Override
    public Assurance save(Assurance assurance) throws DataAccessException {
        String sql = "INSERT INTO assurances (num_carte_assurance, compagnie, date_debut, date_fin, cout) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DbUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // num_carte_assurance est l'ID et doit être fourni
            pstmt.setInt(1, assurance.getNumCarteAssurance());
            pstmt.setString(2, assurance.getCompagnie());
            pstmt.setTimestamp(3,
                    assurance.getDateDebut() != null ? Timestamp.valueOf(assurance.getDateDebut()) : null);
            pstmt.setTimestamp(4, assurance.getDateFin() != null ? Timestamp.valueOf(assurance.getDateFin()) : null);
            pstmt.setBigDecimal(5, assurance.getCout());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Erreur lors de la sauvegarde de l'assurance: " + assurance.getNumCarteAssurance(), e);
        }
        return assurance;
    }

    @Override
    public Assurance update(Assurance assurance) throws DataAccessException {
        String sql = "UPDATE assurances SET compagnie = ?, date_debut = ?, date_fin = ?, cout = ? WHERE num_carte_assurance = ?";
        try (Connection conn = DbUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, assurance.getCompagnie());
            pstmt.setTimestamp(2,
                    assurance.getDateDebut() != null ? Timestamp.valueOf(assurance.getDateDebut()) : null);
            pstmt.setTimestamp(3, assurance.getDateFin() != null ? Timestamp.valueOf(assurance.getDateFin()) : null);
            pstmt.setBigDecimal(4, assurance.getCout());
            pstmt.setInt(5, assurance.getNumCarteAssurance());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La mise à jour de l'assurance avec ID "
                        + assurance.getNumCarteAssurance() + " a échoué, aucune ligne affectée.");
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Erreur lors de la mise à jour de l'assurance: " + assurance.getNumCarteAssurance(), e);
        }
        return assurance;
    }

    @Override
    public boolean delete(Integer id) throws DataAccessException {
        String sql = "DELETE FROM assurances WHERE num_carte_assurance = ?";
        try (Connection conn = DbUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la suppression de l'assurance: " + id, e);
        }
    }

    @Override
    public long count() throws DataAccessException {
        String sql = "SELECT COUNT(*) FROM assurances";
        try (Connection conn = DbUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors du comptage des assurances", e);
        }
        return 0;
    }

    @Override
    public List<Assurance> findExpiringSoon(Connection conn, LocalDateTime dateLimite) throws DataAccessException {
        List<Assurance> assurances = new ArrayList<>();
        String sql = "SELECT num_carte_assurance, compagnie, date_debut, date_fin, cout FROM assurances WHERE date_fin <= ?";
        // La connexion est passée en paramètre, nous l'utilisons directement.
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(dateLimite));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    assurances.add(mapResultSetToAssurance(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Erreur lors de la recherche des assurances expirant bientôt avant: " + dateLimite, e);
        }
        return assurances;
    }
}