package main.java.com.miage.parcauto.dao.impl;

import main.java.com.miage.parcauto.dao.AssuranceRepository;
import main.java.com.miage.parcauto.model.assurance.Assurance;
import main.java.com.miage.parcauto.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AssuranceRepositoryImpl implements AssuranceRepository {

    private Assurance mapResultSetToAssurance(ResultSet rs) throws SQLException {
        Assurance assurance = new Assurance();
        assurance.setNumCarteAssurance(rs.getInt("num_carte_assurance"));
        assurance.setCompagnie(rs.getString("compagnie"));
        assurance.setAdresse(rs.getString("adresse"));
        assurance.setTelephone(rs.getString("telephone"));

        Timestamp dateDebutTs = rs.getTimestamp("date_debut");
        if (dateDebutTs != null) {
            assurance.setDateDebut(dateDebutTs.toLocalDateTime());
        }
        Timestamp dateFinTs = rs.getTimestamp("date_fin");
        if (dateFinTs != null) {
            assurance.setDateFin(dateFinTs.toLocalDateTime());
        }
        assurance.setPrix(rs.getBigDecimal("prix"));
        return assurance;
    }

    @Override
    public Optional<Assurance> findById(Connection conn, Integer id) throws SQLException {
        String sql = "SELECT num_carte_assurance, compagnie, adresse, telephone, date_debut, date_fin, prix FROM ASSURANCE WHERE num_carte_assurance = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
    public List<Assurance> findAll(Connection conn) throws SQLException {
        List<Assurance> assurances = new ArrayList<>();
        String sql = "SELECT num_carte_assurance, compagnie, adresse, telephone, date_debut, date_fin, prix FROM ASSURANCE";
        try (Statement stmt = conn.createStatement();
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
    public List<Assurance> findAll(Connection conn, int page, int size) throws SQLException {
        List<Assurance> assurances = new ArrayList<>();
        String sql = "SELECT num_carte_assurance, compagnie, adresse, telephone, date_debut, date_fin, prix FROM ASSURANCE LIMIT ? OFFSET ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, (page - 1) * size);
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
    public Assurance save(Connection conn, Assurance assurance) throws SQLException {
        String sql = "INSERT INTO ASSURANCE (compagnie, adresse, telephone, date_debut, date_fin, prix) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, assurance.getCompagnie());
            pstmt.setString(2, assurance.getAdresse());
            pstmt.setString(3, assurance.getTelephone());
            pstmt.setTimestamp(4, assurance.getDateDebut() != null ? Timestamp.valueOf(assurance.getDateDebut()) : null);
            pstmt.setTimestamp(5, assurance.getDateFin() != null ? Timestamp.valueOf(assurance.getDateFin()) : null);
            pstmt.setBigDecimal(6, assurance.getPrix());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La création de l'assurance a échoué, aucune ligne affectée.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    assurance.setNumCarteAssurance(generatedKeys.getInt(1));
                } else {
                    throw new DataAccessException("La création de l'assurance a échoué, aucun ID (num_carte_assurance) obtenu. Vérifiez si la colonne est auto-incrémentée.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la sauvegarde de l'assurance pour la compagnie: " + assurance.getCompagnie(), e);
        }
        return assurance;
    }

    @Override
    public Assurance update(Connection conn, Assurance assurance) throws SQLException {
        String sql = "UPDATE ASSURANCE SET compagnie = ?, adresse = ?, telephone = ?, date_debut = ?, date_fin = ?, prix = ? WHERE num_carte_assurance = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, assurance.getCompagnie());
            pstmt.setString(2, assurance.getAdresse());
            pstmt.setString(3, assurance.getTelephone());
            pstmt.setTimestamp(4, assurance.getDateDebut() != null ? Timestamp.valueOf(assurance.getDateDebut()) : null);
            pstmt.setTimestamp(5, assurance.getDateFin() != null ? Timestamp.valueOf(assurance.getDateFin()) : null);
            pstmt.setBigDecimal(6, assurance.getPrix());
            pstmt.setInt(7, assurance.getNumCarteAssurance());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La mise à jour de l'assurance avec ID " + assurance.getNumCarteAssurance() + " a échoué, aucune ligne affectée.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la mise à jour de l'assurance: " + assurance.getNumCarteAssurance(), e);
        }
        return assurance;
    }

    @Override
    public boolean delete(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM ASSURANCE WHERE num_carte_assurance = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la suppression de l'assurance: " + id, e);
        }
    }

    @Override
    public long count(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ASSURANCE";
        try (Statement stmt = conn.createStatement();
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
    public List<Assurance> findExpiringSoon(Connection conn, LocalDateTime dateLimite) throws SQLException {
        List<Assurance> assurances = new ArrayList<>();
        String sql = "SELECT num_carte_assurance, compagnie, adresse, telephone, date_debut, date_fin, prix FROM ASSURANCE WHERE date_fin <= ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(dateLimite));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    assurances.add(mapResultSetToAssurance(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche des assurances expirant avant " + dateLimite, e);
        }
        return assurances;
    }
}