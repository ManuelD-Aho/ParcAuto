package main.java.com.miage.parcauto.dao.impl;

import main.java.com.miage.parcauto.dao.PersonnelRepository;
import main.java.com.miage.parcauto.model.rh.Personnel;
import main.java.com.miage.parcauto.model.rh.Sexe;
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

public class PersonnelRepositoryImpl implements PersonnelRepository {

    private Personnel mapResultSetToPersonnel(ResultSet rs) throws SQLException {
        Personnel personnel = new Personnel();
        personnel.setIdPersonnel(rs.getInt("id_personnel"));
        personnel.setIdService(rs.getInt("id_service"));
        personnel.setIdFonction(rs.getInt("id_fonction"));
        personnel.setMatricule(rs.getString("matricule"));
        personnel.setNom(rs.getString("nom"));
        personnel.setPrenom(rs.getString("prenom"));
        personnel.setEmail(rs.getString("email"));
        String sexeStr = rs.getString("sexe");
        personnel.setSexe(sexeStr != null ? Sexe.valueOf(sexeStr) : null);
        personnel.setDateEmbauche(
                rs.getTimestamp("date_embauche") != null ? rs.getTimestamp("date_embauche").toLocalDateTime() : null);
        personnel.setObservation(rs.getString("observation"));
        return personnel;
    }

    @Override
    public Optional<Personnel> findById(Connection conn, Integer id) throws SQLException {
        String sql = "SELECT * FROM PERSONNEL WHERE id_personnel = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPersonnel(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche du personnel par ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Personnel> findAll(Connection conn) throws SQLException {
        List<Personnel> personnels = new ArrayList<>();
        String sql = "SELECT * FROM PERSONNEL";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                personnels.add(mapResultSetToPersonnel(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération de tous les membres du personnel", e);
        }
        return personnels;
    }

    @Override
    public List<Personnel> findAll(Connection conn, int page, int size) throws SQLException {
        List<Personnel> personnels = new ArrayList<>();
        String sql = "SELECT * FROM PERSONNEL LIMIT ? OFFSET ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, (page - 1) * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    personnels.add(mapResultSetToPersonnel(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération paginée des membres du personnel", e);
        }
        return personnels;
    }

    @Override
    public Personnel save(Connection conn, Personnel personnel) throws SQLException {
        String sql = "INSERT INTO PERSONNEL (id_service, id_fonction, matricule, nom, prenom, date_naissance, sexe, adresse, telephone, email, date_embauche, observation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, personnel.getIdService());
            pstmt.setInt(2, personnel.getIdFonction());
            pstmt.setString(3, personnel.getMatricule());
            pstmt.setString(4, personnel.getNom());
            pstmt.setString(5, personnel.getPrenom());
            pstmt.setTimestamp(6,
                    personnel.getDateNaissance() != null
                            ? Timestamp.valueOf(personnel.getDateNaissance().atStartOfDay())
                            : null);
            pstmt.setString(7, personnel.getSexe() != null ? personnel.getSexe().getValeur() : null);
            pstmt.setString(8, personnel.getAdresse());
            pstmt.setString(9, personnel.getTelephone());
            pstmt.setString(10, personnel.getEmail());
            pstmt.setTimestamp(11,
                    personnel.getDateEmbauche() != null ? Timestamp.valueOf(personnel.getDateEmbauche().atStartOfDay())
                            : null);
            pstmt.setString(12, personnel.getObservation());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La création du membre du personnel a échoué, aucune ligne affectée.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    personnel.setIdPersonnel(generatedKeys.getInt(1));
                } else {
                    throw new DataAccessException("La création du membre du personnel a échoué, aucun ID obtenu.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la sauvegarde du membre du personnel: " + personnel.getNom()
                    + " " + personnel.getPrenom(), e);
        }
        return personnel;
    }

    @Override
    public Personnel update(Connection conn, Personnel personnel) throws SQLException {
        String sql = "UPDATE PERSONNEL SET id_service = ?, id_fonction = ?, matricule = ?, nom = ?, prenom = ?, date_naissance = ?, sexe = ?, adresse = ?, telephone = ?, email = ?, date_embauche = ?, observation = ? WHERE id_personnel = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, personnel.getIdService());
            pstmt.setInt(2, personnel.getIdFonction());
            pstmt.setString(3, personnel.getMatricule());
            pstmt.setString(4, personnel.getNom());
            pstmt.setString(5, personnel.getPrenom());
            pstmt.setTimestamp(6,
                    personnel.getDateNaissance() != null
                            ? Timestamp.valueOf(personnel.getDateNaissance().atStartOfDay())
                            : null);
            pstmt.setString(7, personnel.getSexe() != null ? personnel.getSexe().getValeur() : null);
            pstmt.setString(8, personnel.getAdresse());
            pstmt.setString(9, personnel.getTelephone());
            pstmt.setString(10, personnel.getEmail());
            pstmt.setTimestamp(11,
                    personnel.getDateEmbauche() != null ? Timestamp.valueOf(personnel.getDateEmbauche().atStartOfDay())
                            : null);
            pstmt.setString(12, personnel.getObservation());
            pstmt.setInt(13, personnel.getIdPersonnel());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La mise à jour du membre du personnel avec ID "
                        + personnel.getIdPersonnel() + " a échoué, aucune ligne affectée.");
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Erreur lors de la mise à jour du membre du personnel: " + personnel.getIdPersonnel(), e);
        }
        return personnel;
    }

    @Override
    public boolean delete(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM PERSONNEL WHERE id_personnel = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            // Gérer les dépendances (Utilisateur, Affectation, Mission, SocietaireCompte)
            // avant suppression, soit par configuration DB (ON DELETE CASCADE/SET NULL)
            // soit explicitement dans la couche service.
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la suppression du membre du personnel: " + id, e);
        }
    }

    @Override
    public long count(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM PERSONNEL";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors du comptage des membres du personnel", e);
        }
        return 0;
    }

    @Override
    public Optional<Personnel> findByMatricule(Connection conn, String matricule) throws SQLException {
        String sql = "SELECT * FROM PERSONNEL WHERE matricule = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, matricule);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPersonnel(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche du personnel par matricule: " + matricule, e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Personnel> findByEmail(Connection conn, String email) throws SQLException {
        String sql = "SELECT * FROM PERSONNEL WHERE email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPersonnel(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche du personnel par email: " + email, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Personnel> findByServiceId(Connection conn, Integer idService) throws SQLException {
        List<Personnel> personnels = new ArrayList<>();
        String sql = "SELECT * FROM PERSONNEL WHERE id_service = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idService);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    personnels.add(mapResultSetToPersonnel(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Erreur lors de la recherche des membres du personnel par ID service: " + idService, e);
        }
        return personnels;
    }

    @Override
    public List<Personnel> findByFonctionId(Connection conn, Integer idFonction) throws SQLException {
        List<Personnel> personnels = new ArrayList<>();
        String sql = "SELECT * FROM PERSONNEL WHERE id_fonction = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idFonction);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    personnels.add(mapResultSetToPersonnel(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Erreur lors de la recherche des membres du personnel par ID fonction: " + idFonction, e);
        }
        return personnels;
    }
}