package main.java.com.miage.parcauto.dao.impl;

import main.java.com.miage.parcauto.dao.FonctionRepository;
import main.java.com.miage.parcauto.model.rh.Fonction;
import main.java.com.miage.parcauto.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FonctionRepositoryImpl implements FonctionRepository {

    private Fonction mapResultSetToFonction(ResultSet rs) throws SQLException {
        Fonction fonction = new Fonction();
        fonction.setIdFonction(rs.getInt("id_fonction"));
        fonction.setLibelle(rs.getString("libelle"));
        return fonction;
    }

    @Override
    public Optional<Fonction> findById(Connection conn, Integer id) throws SQLException {
        String sql = "SELECT id_fonction, libelle FROM FONCTION WHERE id_fonction = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToFonction(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche de la fonction par ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Fonction> findAll(Connection conn) throws SQLException {
        List<Fonction> fonctions = new ArrayList<>();
        String sql = "SELECT id_fonction, libelle FROM FONCTION";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                fonctions.add(mapResultSetToFonction(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération de toutes les fonctions", e);
        }
        return fonctions;
    }

    @Override
    public List<Fonction> findAll(Connection conn, int page, int size) throws SQLException {
        List<Fonction> fonctions = new ArrayList<>();
        String sql = "SELECT id_fonction, libelle FROM FONCTION LIMIT ? OFFSET ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, (page - 1) * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    fonctions.add(mapResultSetToFonction(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération paginée des fonctions", e);
        }
        return fonctions;
    }

    @Override
    public Fonction save(Connection conn, Fonction fonction) throws SQLException {
        String sql = "INSERT INTO FONCTION (libelle) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, fonction.getLibelle());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La création de la fonction a échoué, aucune ligne affectée.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    fonction.setIdFonction(generatedKeys.getInt(1));
                } else {
                    throw new DataAccessException("La création de la fonction a échoué, aucun ID obtenu.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la sauvegarde de la fonction: " + fonction.getLibelle(), e);
        }
        return fonction;
    }

    @Override
    public Fonction update(Connection conn, Fonction fonction) throws SQLException {
        String sql = "UPDATE FONCTION SET libelle = ? WHERE id_fonction = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fonction.getLibelle());
            pstmt.setInt(2, fonction.getIdFonction());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La mise à jour de la fonction avec ID " + fonction.getIdFonction() + " a échoué, aucune ligne affectée.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la mise à jour de la fonction: " + fonction.getIdFonction(), e);
        }
        return fonction;
    }

    @Override
    public boolean delete(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM FONCTION WHERE id_fonction = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la suppression de la fonction: " + id, e);
        }
    }

    @Override
    public long count(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM FONCTION";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors du comptage des fonctions", e);
        }
        return 0;
    }

    @Override
    public Optional<Fonction> findByLibelle(Connection conn, String libelle) throws SQLException {
        String sql = "SELECT id_fonction, libelle FROM FONCTION WHERE libelle = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, libelle);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToFonction(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche de la fonction par libellé: " + libelle, e);
        }
        return Optional.empty();
    }
}