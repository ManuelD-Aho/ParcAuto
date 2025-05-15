package main.java.com.miage.parcauto.dao.impl;

import main.java.com.miage.parcauto.dao.AffectationRepository;
import main.java.com.miage.parcauto.model.affectation.Affectation;
import main.java.com.miage.parcauto.model.affectation.TypeAffectation;
import main.java.com.miage.parcauto.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AffectationRepositoryImpl implements AffectationRepository {

    private Affectation mapResultSetToAffectation(ResultSet rs) throws SQLException {
        Affectation affectation = new Affectation();
        affectation.setIdAffectation(rs.getInt("id_affectation"));
        affectation.setIdVehicule(rs.getInt("id_vehicule"));

        int idPersonnel = rs.getInt("id_personnel");
        if (rs.wasNull()) {
            affectation.setIdPersonnel(null);
        } else {
            affectation.setIdPersonnel(idPersonnel);
        }

        String typeStr = rs.getString("type");
        if (typeStr != null) {
            affectation.setType(TypeAffectation.fromString(typeStr));
        }

        Timestamp dateDebutTs = rs.getTimestamp("date_debut");
        if (dateDebutTs != null) {
            affectation.setDateDebut(dateDebutTs.toLocalDateTime());
        }

        Timestamp dateFinTs = rs.getTimestamp("date_fin");
        if (dateFinTs != null) {
            affectation.setDateFin(dateFinTs.toLocalDateTime());
        } else {
            affectation.setDateFin(null);
        }
        return affectation;
    }

    @Override
    public Optional<Affectation> findById(Connection conn, Integer id) throws SQLException {
        String sql = "SELECT id_affectation, id_vehicule, id_personnel, type, date_debut, date_fin FROM AFFECTATION WHERE id_affectation = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAffectation(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche de l'affectation par ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Affectation> findAll(Connection conn) throws SQLException {
        List<Affectation> affectations = new ArrayList<>();
        String sql = "SELECT id_affectation, id_vehicule, id_personnel, type, date_debut, date_fin FROM AFFECTATION";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                affectations.add(mapResultSetToAffectation(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération de toutes les affectations", e);
        }
        return affectations;
    }

    @Override
    public List<Affectation> findAll(Connection conn, int page, int size) throws SQLException {
        List<Affectation> affectations = new ArrayList<>();
        String sql = "SELECT id_affectation, id_vehicule, id_personnel, type, date_debut, date_fin FROM AFFECTATION LIMIT ? OFFSET ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, (page - 1) * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    affectations.add(mapResultSetToAffectation(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération paginée des affectations", e);
        }
        return affectations;
    }

    @Override
    public Affectation save(Connection conn, Affectation affectation) throws SQLException {
        String sql = "INSERT INTO AFFECTATION (id_vehicule, id_personnel, type, date_debut, date_fin) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, affectation.getIdVehicule());
            if (affectation.getIdPersonnel() != null) {
                pstmt.setInt(2, affectation.getIdPersonnel());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            pstmt.setString(3, affectation.getType() != null ? affectation.getType().getValeur() : null);
            pstmt.setTimestamp(4, affectation.getDateDebut() != null ? Timestamp.valueOf(affectation.getDateDebut()) : null);
            if (affectation.getDateFin() != null) {
                pstmt.setTimestamp(5, Timestamp.valueOf(affectation.getDateFin()));
            } else {
                pstmt.setNull(5, Types.TIMESTAMP);
            }

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La création de l'affectation a échoué, aucune ligne affectée.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    affectation.setIdAffectation(generatedKeys.getInt(1));
                } else {
                    throw new DataAccessException("La création de l'affectation a échoué, aucun ID obtenu.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la sauvegarde de l'affectation", e);
        }
        return affectation;
    }

    @Override
    public Affectation update(Connection conn, Affectation affectation) throws SQLException {
        String sql = "UPDATE AFFECTATION SET id_vehicule = ?, id_personnel = ?, type = ?, date_debut = ?, date_fin = ? WHERE id_affectation = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, affectation.getIdVehicule());
            if (affectation.getIdPersonnel() != null) {
                pstmt.setInt(2, affectation.getIdPersonnel());
            } else {
                pstmt.setNull(2, Types.INTEGER);
            }
            pstmt.setString(3, affectation.getType() != null ? affectation.getType().getValeur() : null);
            pstmt.setTimestamp(4, affectation.getDateDebut() != null ? Timestamp.valueOf(affectation.getDateDebut()) : null);
            if (affectation.getDateFin() != null) {
                pstmt.setTimestamp(5, Timestamp.valueOf(affectation.getDateFin()));
            } else {
                pstmt.setNull(5, Types.TIMESTAMP);
            }
            pstmt.setInt(6, affectation.getIdAffectation());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La mise à jour de l'affectation avec ID " + affectation.getIdAffectation() + " a échoué, aucune ligne affectée.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la mise à jour de l'affectation: " + affectation.getIdAffectation(), e);
        }
        return affectation;
    }

    @Override
    public boolean delete(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM AFFECTATION WHERE id_affectation = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la suppression de l'affectation: " + id, e);
        }
    }

    @Override
    public long count(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM AFFECTATION";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors du comptage des affectations", e);
        }
        return 0;
    }

    @Override
    public List<Affectation> findByVehiculeId(Connection conn, Integer idVehicule) throws SQLException {
        List<Affectation> affectations = new ArrayList<>();
        String sql = "SELECT id_affectation, id_vehicule, id_personnel, type, date_debut, date_fin FROM AFFECTATION WHERE id_vehicule = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVehicule);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    affectations.add(mapResultSetToAffectation(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche des affectations par ID véhicule: " + idVehicule, e);
        }
        return affectations;
    }

    @Override
    public List<Affectation> findByPersonnelId(Connection conn, Integer idPersonnel) throws SQLException {
        List<Affectation> affectations = new ArrayList<>();
        String sql = "SELECT id_affectation, id_vehicule, id_personnel, type, date_debut, date_fin FROM AFFECTATION WHERE id_personnel = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPersonnel);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    affectations.add(mapResultSetToAffectation(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche des affectations par ID personnel: " + idPersonnel, e);
        }
        return affectations;
    }

    @Override
    public List<Affectation> findBySocietaireId(Connection conn, Integer idSocietaire) throws SQLException {
        List<Affectation> affectations = new ArrayList<>();
        // Cette requête suppose qu'un sociétaire est identifié via la table PERSONNEL
        // et que l'idSocietaire correspond à un id_personnel qui est aussi un sociétaire.
        // Ou via une mission liée à un sociétaire, puis à un véhicule, puis à une affectation.
        // La requête directe sur AFFECTATION.id_personnel est la plus simple si idSocietaire = idPersonnel.
        // Si la logique est plus complexe (ex: via Mission), cette requête doit être adaptée
        // ou cette méthode gérée au niveau Service avec plusieurs appels DAO.
        // Pour l'instant, on considère que idSocietaire peut correspondre à id_personnel pour un sociétaire.
        String sql = "SELECT a.id_affectation, a.id_vehicule, a.id_personnel, a.type, a.date_debut, a.date_fin " +
                "FROM AFFECTATION a " +
                "JOIN PERSONNEL p ON a.id_personnel = p.id_personnel " +
                "JOIN SOCIETAIRE_COMPTE sc ON p.id_personnel = sc.id_personnel " +
                "WHERE sc.id_compte_societaire = ?"; // Ou sc.id_personnel si idSocietaire est l'id personnel
        // Ou directement p.id_personnel = ? si idSocietaire est l'id_personnel

        // Alternative plus simple si l'idSocietaire EST l'id_personnel du sociétaire
        // String sql = "SELECT id_affectation, id_vehicule, id_personnel, type, date_debut, date_fin FROM AFFECTATION WHERE id_personnel = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idSocietaire);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    affectations.add(mapResultSetToAffectation(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche des affectations par ID sociétaire: " + idSocietaire, e);
        }
        return affectations;
    }
}