package main.java.com.miage.parcauto.dao.impl;

import main.java.com.miage.parcauto.dao.SocietaireCompteRepository;
import main.java.com.miage.parcauto.model.finance.SocietaireCompte;
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

public class SocietaireCompteRepositoryImpl implements SocietaireCompteRepository {

    private SocietaireCompte mapResultSetToSocietaireCompte(ResultSet rs) throws SQLException {
        SocietaireCompte compte = new SocietaireCompte();
        compte.setIdCompteSocietaire(rs.getInt("id_compte_societaire"));

        int idPersonnel = rs.getInt("id_personnel"); // Clé étrangère vers PERSONNEL
        compte.setIdPersonnel(rs.wasNull() ? null : idPersonnel);

        compte.setNumeroCompte(rs.getString("numero_compte"));
        compte.setSolde(rs.getBigDecimal("solde"));

        Timestamp dateCreationTs = rs.getTimestamp("date_creation");
        compte.setDateCreation(dateCreationTs != null ? dateCreationTs.toLocalDateTime() : null);

        compte.setActif(rs.getBoolean("actif"));
        return compte;
    }

    @Override
    public Optional<SocietaireCompte> findById(Connection conn, Integer id) throws SQLException {
        String sql = "SELECT * FROM SOCIETAIRE_COMPTE WHERE id_compte_societaire = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToSocietaireCompte(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche du compte sociétaire par ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<SocietaireCompte> findAll(Connection conn) throws SQLException {
        List<SocietaireCompte> comptes = new ArrayList<>();
        String sql = "SELECT * FROM SOCIETAIRE_COMPTE";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                comptes.add(mapResultSetToSocietaireCompte(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération de tous les comptes sociétaires", e);
        }
        return comptes;
    }

    @Override
    public List<SocietaireCompte> findAll(Connection conn, int page, int size) throws SQLException {
        List<SocietaireCompte> comptes = new ArrayList<>();
        String sql = "SELECT * FROM SOCIETAIRE_COMPTE LIMIT ? OFFSET ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, (page - 1) * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    comptes.add(mapResultSetToSocietaireCompte(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération paginée des comptes sociétaires", e);
        }
        return comptes;
    }

    @Override
    public SocietaireCompte save(Connection conn, SocietaireCompte compte) throws SQLException {
        String sql = "INSERT INTO SOCIETAIRE_COMPTE (id_personnel, numero_compte, solde, date_creation, actif) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (compte.getIdPersonnel() != null) pstmt.setInt(1, compte.getIdPersonnel()); else pstmt.setNull(1, Types.INTEGER);
            pstmt.setString(2, compte.getNumeroCompte());
            pstmt.setBigDecimal(3, compte.getSolde());
            pstmt.setTimestamp(4, compte.getDateCreation() != null ? Timestamp.valueOf(compte.getDateCreation()) : Timestamp.valueOf(java.time.LocalDateTime.now()));
            pstmt.setBoolean(5, compte.isActif());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La création du compte sociétaire a échoué, aucune ligne affectée.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    compte.setIdCompteSocietaire(generatedKeys.getInt(1));
                } else {
                    throw new DataAccessException("La création du compte sociétaire a échoué, aucun ID obtenu.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la sauvegarde du compte sociétaire: " + compte.getNumeroCompte(), e);
        }
        return compte;
    }

    @Override
    public SocietaireCompte update(Connection conn, SocietaireCompte compte) throws SQLException {
        String sql = "UPDATE SOCIETAIRE_COMPTE SET id_personnel = ?, numero_compte = ?, solde = ?, date_creation = ?, actif = ? WHERE id_compte_societaire = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (compte.getIdPersonnel() != null) pstmt.setInt(1, compte.getIdPersonnel()); else pstmt.setNull(1, Types.INTEGER);
            pstmt.setString(2, compte.getNumeroCompte());
            pstmt.setBigDecimal(3, compte.getSolde());
            pstmt.setTimestamp(4, compte.getDateCreation() != null ? Timestamp.valueOf(compte.getDateCreation()) : null); // Ne pas changer date_creation à la légère
            pstmt.setBoolean(5, compte.isActif());
            pstmt.setInt(6, compte.getIdCompteSocietaire());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La mise à jour du compte sociétaire avec ID " + compte.getIdCompteSocietaire() + " a échoué, aucune ligne affectée.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la mise à jour du compte sociétaire: " + compte.getIdCompteSocietaire(), e);
        }
        return compte;
    }

    @Override
    public boolean delete(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM SOCIETAIRE_COMPTE WHERE id_compte_societaire = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            // Gérer dépendances (MOUVEMENT, DOCUMENT_SOCIETAIRE, MISSION) avant suppression
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la suppression du compte sociétaire: " + id, e);
        }
    }

    @Override
    public long count(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM SOCIETAIRE_COMPTE";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors du comptage des comptes sociétaires", e);
        }
        return 0;
    }

    @Override
    public Optional<SocietaireCompte> findByNumeroCompte(Connection conn, String numeroCompte) throws SQLException {
        String sql = "SELECT * FROM SOCIETAIRE_COMPTE WHERE numero_compte = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, numeroCompte);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToSocietaireCompte(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche du compte sociétaire par numéro: " + numeroCompte, e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<SocietaireCompte> findByPersonnelId(Connection conn, Integer idPersonnel) throws SQLException {
        String sql = "SELECT * FROM SOCIETAIRE_COMPTE WHERE id_personnel = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idPersonnel);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToSocietaireCompte(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche du compte sociétaire par ID Personnel: " + idPersonnel, e);
        }
        return Optional.empty();
    }
}