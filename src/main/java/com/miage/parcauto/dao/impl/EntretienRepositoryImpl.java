package main.java.com.miage.parcauto.dao.impl;

import main.java.com.miage.parcauto.dao.EntretienRepository;
import main.java.com.miage.parcauto.model.entretien.Entretien;
import main.java.com.miage.parcauto.model.entretien.StatutOT;
import main.java.com.miage.parcauto.model.entretien.TypeEntretien;
import main.java.com.miage.parcauto.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntretienRepositoryImpl implements EntretienRepository {

    private Entretien mapResultSetToEntretien(ResultSet rs) throws SQLException {
        Entretien entretien = new Entretien();
        entretien.setIdEntretien(rs.getInt("id_entretien"));
        entretien.setIdVehicule(rs.getInt("id_vehicule"));
        String typeStr = rs.getString("type");
        entretien.setType(typeStr != null ? TypeEntretien.valueOf(typeStr) : null);
        String statutStr = rs.getString("statut");
        entretien.setStatut(statutStr != null ? StatutOT.valueOf(statutStr) : null);
        entretien.setDateEntree(
                rs.getTimestamp("date_entree") != null ? rs.getTimestamp("date_entree").toLocalDateTime() : null);
        entretien.setDateSortie(
                rs.getTimestamp("date_sortie") != null ? rs.getTimestamp("date_sortie").toLocalDateTime() : null);
        entretien.setMotif(rs.getString("motif"));
        entretien.setObservations(rs.getString("observations"));
        entretien.setCoutEstime(rs.getBigDecimal("cout_estime"));
        entretien.setCoutReel(rs.getBigDecimal("cout_reel"));
        entretien.setLieu(rs.getString("lieu"));
        return entretien;
    }

    @Override
    public Optional<Entretien> findById(Connection conn, Integer id) throws SQLException {
        String sql = "SELECT * FROM ENTRETIEN WHERE id_entretien = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntretien(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche de l'entretien par ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Entretien> findAll(Connection conn) throws SQLException {
        List<Entretien> entretiens = new ArrayList<>();
        String sql = "SELECT * FROM ENTRETIEN";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                entretiens.add(mapResultSetToEntretien(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération de tous les entretiens", e);
        }
        return entretiens;
    }

    @Override
    public List<Entretien> findAll(Connection conn, int page, int size) throws SQLException {
        List<Entretien> entretiens = new ArrayList<>();
        String sql = "SELECT * FROM ENTRETIEN LIMIT ? OFFSET ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, (page - 1) * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    entretiens.add(mapResultSetToEntretien(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération paginée des entretiens", e);
        }
        return entretiens;
    }

    @Override
    public Entretien save(Connection conn, Entretien entretien) throws SQLException {
        String sql = "INSERT INTO ENTRETIEN (id_vehicule, type, statut_ot, date_prevue, date_realisation, libelle, details, prestataire, pieces_detachees, cout_estime, cout_reel, km_vehicule, km_prochain_entretien, observation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, entretien.getIdVehicule());
            pstmt.setString(2, entretien.getType() != null ? entretien.getType().getValeur() : null);
            pstmt.setString(3, entretien.getStatutOt() != null ? entretien.getStatutOt().getValeur()
                    : StatutOT.OUVERT.getValeur()); // Défaut si non fourni
            pstmt.setTimestamp(4,
                    entretien.getDatePrevue() != null ? Timestamp.valueOf(entretien.getDatePrevue()) : null);
            pstmt.setTimestamp(5,
                    entretien.getDateRealisation() != null ? Timestamp.valueOf(entretien.getDateRealisation()) : null);
            pstmt.setString(6, entretien.getLibelle());
            pstmt.setString(7, entretien.getDetails());
            pstmt.setString(8, entretien.getPrestataire());
            pstmt.setString(9, entretien.getPiecesDetachees());
            pstmt.setBigDecimal(10, entretien.getCoutEstime());
            pstmt.setBigDecimal(11, entretien.getCoutReel());
            if (entretien.getKmVehicule() != null)
                pstmt.setInt(12, entretien.getKmVehicule());
            else
                pstmt.setNull(12, Types.INTEGER);
            if (entretien.getKmProchainEntretien() != null)
                pstmt.setInt(13, entretien.getKmProchainEntretien());
            else
                pstmt.setNull(13, Types.INTEGER);
            pstmt.setString(14, entretien.getObservation());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La création de l'entretien a échoué, aucune ligne affectée.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entretien.setIdEntretien(generatedKeys.getInt(1));
                } else {
                    throw new DataAccessException("La création de l'entretien a échoué, aucun ID obtenu.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la sauvegarde de l'entretien: " + entretien.getLibelle(), e);
        }
        return entretien;
    }

    @Override
    public Entretien update(Connection conn, Entretien entretien) throws SQLException {
        String sql = "UPDATE ENTRETIEN SET id_vehicule = ?, type = ?, statut_ot = ?, date_prevue = ?, date_realisation = ?, libelle = ?, details = ?, prestataire = ?, pieces_detachees = ?, cout_estime = ?, cout_reel = ?, km_vehicule = ?, km_prochain_entretien = ?, observation = ? WHERE id_entretien = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, entretien.getIdVehicule());
            pstmt.setString(2, entretien.getType() != null ? entretien.getType().getValeur() : null);
            pstmt.setString(3, entretien.getStatutOt() != null ? entretien.getStatutOt().getValeur() : null);
            pstmt.setTimestamp(4,
                    entretien.getDatePrevue() != null ? Timestamp.valueOf(entretien.getDatePrevue()) : null);
            pstmt.setTimestamp(5,
                    entretien.getDateRealisation() != null ? Timestamp.valueOf(entretien.getDateRealisation()) : null);
            pstmt.setString(6, entretien.getLibelle());
            pstmt.setString(7, entretien.getDetails());
            pstmt.setString(8, entretien.getPrestataire());
            pstmt.setString(9, entretien.getPiecesDetachees());
            pstmt.setBigDecimal(10, entretien.getCoutEstime());
            pstmt.setBigDecimal(11, entretien.getCoutReel());
            if (entretien.getKmVehicule() != null)
                pstmt.setInt(12, entretien.getKmVehicule());
            else
                pstmt.setNull(12, Types.INTEGER);
            if (entretien.getKmProchainEntretien() != null)
                pstmt.setInt(13, entretien.getKmProchainEntretien());
            else
                pstmt.setNull(13, Types.INTEGER);
            pstmt.setString(14, entretien.getObservation());
            pstmt.setInt(15, entretien.getIdEntretien());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La mise à jour de l'entretien avec ID " + entretien.getIdEntretien()
                        + " a échoué, aucune ligne affectée.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la mise à jour de l'entretien: " + entretien.getIdEntretien(),
                    e);
        }
        return entretien;
    }

    @Override
    public boolean delete(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM ENTRETIEN WHERE id_entretien = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la suppression de l'entretien: " + id, e);
        }
    }

    @Override
    public long count(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ENTRETIEN";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors du comptage des entretiens", e);
        }
        return 0;
    }

    @Override
    public List<Entretien> findByVehiculeId(Connection conn, Integer idVehicule) throws SQLException {
        List<Entretien> entretiens = new ArrayList<>();
        String sql = "SELECT * FROM ENTRETIEN WHERE id_vehicule = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVehicule);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    entretiens.add(mapResultSetToEntretien(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche des entretiens par ID véhicule: " + idVehicule,
                    e);
        }
        return entretiens;
    }

    @Override
    public List<Entretien> findScheduledBetween(Connection conn, LocalDateTime debut, LocalDateTime fin)
            throws SQLException {
        List<Entretien> entretiens = new ArrayList<>();
        String sql = "SELECT * FROM ENTRETIEN WHERE date_prevue >= ? AND date_prevue <= ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(debut));
            pstmt.setTimestamp(2, Timestamp.valueOf(fin));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    entretiens.add(mapResultSetToEntretien(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Erreur lors de la recherche des entretiens planifiés entre " + debut + " et " + fin, e);
        }
        return entretiens;
    }

    @Override
    public List<Entretien> findByVehiculeIdAndStatut(Connection conn, Integer idVehicule, StatutOT statut)
            throws SQLException {
        List<Entretien> entretiens = new ArrayList<>();
        String sql = "SELECT * FROM ENTRETIEN WHERE id_vehicule = ? AND statut_ot = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idVehicule);
            pstmt.setString(2, statut.getValeur());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    entretiens.add(mapResultSetToEntretien(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche des entretiens pour le véhicule " + idVehicule
                    + " avec statut " + statut, e);
        }
        return entretiens;
    }
}