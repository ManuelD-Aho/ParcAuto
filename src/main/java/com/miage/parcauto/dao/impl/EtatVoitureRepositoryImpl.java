package main.java.com.miage.parcauto.dao.impl;

import main.java.com.miage.parcauto.dao.EtatVoitureRepository;
import main.java.com.miage.parcauto.model.vehicule.EtatVoiture;
import main.java.com.miage.parcauto.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EtatVoitureRepositoryImpl implements EtatVoitureRepository {

    private EtatVoiture mapResultSetToEtatVoiture(ResultSet rs) throws SQLException {
        EtatVoiture etatVoiture = new EtatVoiture();
        etatVoiture.setIdEtatVoiture(rs.getInt("id_etat_voiture"));
        etatVoiture.setLibelle(rs.getString("lib_etat_voiture"));
        return etatVoiture;
    }

    @Override
    public Optional<EtatVoiture> findById(Connection conn, Integer id) throws SQLException {
        String sql = "SELECT id_etat_voiture, lib_etat_voiture FROM ETAT_VOITURE WHERE id_etat_voiture = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEtatVoiture(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche de l'état de voiture par ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<EtatVoiture> findAll(Connection conn) throws SQLException {
        List<EtatVoiture> etats = new ArrayList<>();
        String sql = "SELECT id_etat_voiture, lib_etat_voiture FROM ETAT_VOITURE";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                etats.add(mapResultSetToEtatVoiture(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération de tous les états de voiture", e);
        }
        return etats;
    }

    @Override
    public List<EtatVoiture> findAll(Connection conn, int page, int size) throws SQLException {
        List<EtatVoiture> etats = new ArrayList<>();
        String sql = "SELECT id_etat_voiture, lib_etat_voiture FROM ETAT_VOITURE LIMIT ? OFFSET ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, (page - 1) * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    etats.add(mapResultSetToEtatVoiture(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération paginée des états de voiture", e);
        }
        return etats;
    }

    @Override
    public EtatVoiture save(Connection conn, EtatVoiture etatVoiture) throws SQLException {
        String sql = "INSERT INTO ETAT_VOITURE (lib_etat_voiture) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, etatVoiture.getLibelle());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La création de l'état de voiture a échoué, aucune ligne affectée.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    etatVoiture.setIdEtatVoiture(generatedKeys.getInt(1));
                } else {
                    throw new DataAccessException("La création de l'état de voiture a échoué, aucun ID obtenu.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la sauvegarde de l'état de voiture: " + etatVoiture.getLibelle(), e);
        }
        return etatVoiture;
    }

    @Override
    public EtatVoiture update(Connection conn, EtatVoiture etatVoiture) throws SQLException {
        String sql = "UPDATE ETAT_VOITURE SET lib_etat_voiture = ? WHERE id_etat_voiture = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, etatVoiture.getLibelle());
            pstmt.setInt(2, etatVoiture.getIdEtatVoiture());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La mise à jour de l'état de voiture avec ID " + etatVoiture.getIdEtatVoiture() + " a échoué, aucune ligne affectée.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la mise à jour de l'état de voiture: " + etatVoiture.getIdEtatVoiture(), e);
        }
        return etatVoiture;
    }

    @Override
    public boolean delete(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM ETAT_VOITURE WHERE id_etat_voiture = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la suppression de l'état de voiture: " + id, e);
        }
    }

    @Override
    public long count(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ETAT_VOITURE";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors du comptage des états de voiture", e);
        }
        return 0;
    }

    @Override
    public Optional<EtatVoiture> findByLibelle(Connection conn, String libelle) throws SQLException {
        String sql = "SELECT id_etat_voiture, lib_etat_voiture FROM ETAT_VOITURE WHERE lib_etat_voiture = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, libelle);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEtatVoiture(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche de l'état de voiture par libellé: " + libelle, e);
        }
        return Optional.empty();
    }
}