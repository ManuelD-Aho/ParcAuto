package main.java.com.miage.parcauto.dao.impl;

import main.java.com.miage.parcauto.dao.MouvementRepository;
import main.java.com.miage.parcauto.model.finance.Mouvement;
import main.java.com.miage.parcauto.model.finance.TypeMouvement;
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

public class MouvementRepositoryImpl implements MouvementRepository {

    private Mouvement mapResultSetToMouvement(ResultSet rs) throws SQLException {
        Mouvement mouvement = new Mouvement();
        mouvement.setIdMouvement(rs.getInt("id_mouvement"));
        mouvement.setIdCompteSocietaire(rs.getInt("id_compte_societaire"));

        String typeStr = rs.getString("type");
        if (typeStr != null) {
            mouvement.setType(TypeMouvement.fromString(typeStr));
        }
        mouvement.setMontant(rs.getBigDecimal("montant"));

        Timestamp dateMouvementTs = rs.getTimestamp("date_mouvement");
        mouvement.setDateMouvement(dateMouvementTs != null ? dateMouvementTs.toLocalDateTime() : null);

        mouvement.setLibelle(rs.getString("libelle"));
        mouvement.setReferenceTransaction(rs.getString("reference_transaction"));
        return mouvement;
    }

    @Override
    public Optional<Mouvement> findById(Connection conn, Integer id) throws SQLException {
        String sql = "SELECT * FROM MOUVEMENT WHERE id_mouvement = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToMouvement(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche du mouvement par ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Mouvement> findAll(Connection conn) throws SQLException {
        List<Mouvement> mouvements = new ArrayList<>();
        String sql = "SELECT * FROM MOUVEMENT";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                mouvements.add(mapResultSetToMouvement(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération de tous les mouvements", e);
        }
        return mouvements;
    }

    @Override
    public List<Mouvement> findAll(Connection conn, int page, int size) throws SQLException {
        List<Mouvement> mouvements = new ArrayList<>();
        String sql = "SELECT * FROM MOUVEMENT LIMIT ? OFFSET ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, (page - 1) * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    mouvements.add(mapResultSetToMouvement(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération paginée des mouvements", e);
        }
        return mouvements;
    }

    @Override
    public Mouvement save(Connection conn, Mouvement mouvement) throws SQLException {
        String sql = "INSERT INTO MOUVEMENT (id_compte_societaire, type, montant, date_mouvement, libelle, reference_transaction) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, mouvement.getIdCompteSocietaire());
            pstmt.setString(2, mouvement.getType() != null ? mouvement.getType().getValeur() : null);
            pstmt.setBigDecimal(3, mouvement.getMontant());
            pstmt.setTimestamp(4, mouvement.getDateMouvement() != null ? Timestamp.valueOf(mouvement.getDateMouvement()) : Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(5, mouvement.getLibelle());
            pstmt.setString(6, mouvement.getReferenceTransaction());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La création du mouvement a échoué, aucune ligne affectée.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    mouvement.setIdMouvement(generatedKeys.getInt(1));
                } else {
                    throw new DataAccessException("La création du mouvement a échoué, aucun ID obtenu.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la sauvegarde du mouvement: " + mouvement.getLibelle(), e);
        }
        return mouvement;
    }

    @Override
    public Mouvement update(Connection conn, Mouvement mouvement) throws SQLException {
        String sql = "UPDATE MOUVEMENT SET id_compte_societaire = ?, type = ?, montant = ?, date_mouvement = ?, libelle = ?, reference_transaction = ? WHERE id_mouvement = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, mouvement.getIdCompteSocietaire());
            pstmt.setString(2, mouvement.getType() != null ? mouvement.getType().getValeur() : null);
            pstmt.setBigDecimal(3, mouvement.getMontant());
            pstmt.setTimestamp(4, mouvement.getDateMouvement() != null ? Timestamp.valueOf(mouvement.getDateMouvement()) : null);
            pstmt.setString(5, mouvement.getLibelle());
            pstmt.setString(6, mouvement.getReferenceTransaction());
            pstmt.setInt(7, mouvement.getIdMouvement());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La mise à jour du mouvement avec ID " + mouvement.getIdMouvement() + " a échoué, aucune ligne affectée.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la mise à jour du mouvement: " + mouvement.getIdMouvement(), e);
        }
        return mouvement;
    }

    @Override
    public boolean delete(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM MOUVEMENT WHERE id_mouvement = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la suppression du mouvement: " + id, e);
        }
    }

    @Override
    public long count(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM MOUVEMENT";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors du comptage des mouvements", e);
        }
        return 0;
    }

    @Override
    public List<Mouvement> findBySocietaireCompteId(Connection conn, Integer idCompteSocietaire) throws SQLException {
        List<Mouvement> mouvements = new ArrayList<>();
        String sql = "SELECT * FROM MOUVEMENT WHERE id_compte_societaire = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCompteSocietaire);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    mouvements.add(mapResultSetToMouvement(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche des mouvements pour le compte sociétaire ID: " + idCompteSocietaire, e);
        }
        return mouvements;
    }

    @Override
    public List<Mouvement> findBySocietaireCompteIdAndType(Connection conn, Integer idCompteSocietaire, TypeMouvement type) throws SQLException {
        List<Mouvement> mouvements = new ArrayList<>();
        String sql = "SELECT * FROM MOUVEMENT WHERE id_compte_societaire = ? AND type = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCompteSocietaire);
            pstmt.setString(2, type.getValeur());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    mouvements.add(mapResultSetToMouvement(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche des mouvements pour le compte sociétaire ID: " + idCompteSocietaire + " et type: " + type, e);
        }
        return mouvements;
    }

    @Override
    public List<Mouvement> findByDateRange(Connection conn, LocalDateTime debut, LocalDateTime fin) throws SQLException {
        List<Mouvement> mouvements = new ArrayList<>();
        String sql = "SELECT * FROM MOUVEMENT WHERE date_mouvement >= ? AND date_mouvement <= ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(debut));
            pstmt.setTimestamp(2, Timestamp.valueOf(fin));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    mouvements.add(mapResultSetToMouvement(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche des mouvements entre " + debut + " et " + fin, e);
        }
        return mouvements;
    }
}