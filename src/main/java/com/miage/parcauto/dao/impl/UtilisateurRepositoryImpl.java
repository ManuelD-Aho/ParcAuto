package main.java.com.miage.parcauto.dao.impl;

import main.java.com.miage.parcauto.dao.UtilisateurRepository;
import main.java.com.miage.parcauto.model.utilisateur.Utilisateur;
import main.java.com.miage.parcauto.model.utilisateur.Role;
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

public class UtilisateurRepositoryImpl implements UtilisateurRepository {

    private Utilisateur mapResultSetToUtilisateur(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setIdUtilisateur(rs.getInt("id_utilisateur"));
        int idPersonnel = rs.getInt("id_personnel");
        utilisateur.setIdPersonnel(rs.wasNull() ? null : idPersonnel);
        utilisateur.setLogin(rs.getString("login"));
        utilisateur.setMotDePasse(rs.getString("mot_de_passe"));
        utilisateur.setSalt(rs.getString("salt"));
        String roleStr = rs.getString("role");
        utilisateur.setRole(roleStr != null ? Role.valueOf(roleStr) : null);
        utilisateur.setMfaSecret(rs.getString("mfa_secret"));
        utilisateur.setActif(rs.getBoolean("actif"));
        utilisateur.setDateCreation(
                rs.getTimestamp("date_creation") != null ? rs.getTimestamp("date_creation").toLocalDateTime() : null);
        utilisateur.setDateDerniereConnexion(rs.getTimestamp("date_derniere_connexion") != null
                ? rs.getTimestamp("date_derniere_connexion").toLocalDateTime()
                : null);
        return utilisateur;
    }

    @Override
    public Optional<Utilisateur> findById(Connection conn, Integer id) throws SQLException {
        String sql = "SELECT * FROM UTILISATEUR WHERE id_utilisateur = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUtilisateur(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche de l'utilisateur par ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Utilisateur> findAll(Connection conn) throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM UTILISATEUR";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                utilisateurs.add(mapResultSetToUtilisateur(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération de tous les utilisateurs", e);
        }
        return utilisateurs;
    }

    @Override
    public List<Utilisateur> findAll(Connection conn, int page, int size) throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM UTILISATEUR LIMIT ? OFFSET ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, (page - 1) * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    utilisateurs.add(mapResultSetToUtilisateur(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la récupération paginée des utilisateurs", e);
        }
        return utilisateurs;
    }

    @Override
    public Utilisateur save(Connection conn, Utilisateur utilisateur) throws SQLException {
        String sql = "INSERT INTO UTILISATEUR (id_personnel, login, mot_de_passe, salt, role, date_creation, date_derniere_connexion, actif) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (utilisateur.getIdPersonnel() != null)
                pstmt.setInt(1, utilisateur.getIdPersonnel());
            else
                pstmt.setNull(1, Types.INTEGER);
            pstmt.setString(2, utilisateur.getLogin());
            pstmt.setString(3, utilisateur.getMotDePasse());
            pstmt.setString(4, utilisateur.getSalt());
            pstmt.setString(5, utilisateur.getRole() != null ? utilisateur.getRole().getValeur() : null);
            pstmt.setTimestamp(6,
                    utilisateur.getDateCreation() != null ? Timestamp.valueOf(utilisateur.getDateCreation())
                            : Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setTimestamp(7,
                    utilisateur.getDateDerniereConnexion() != null
                            ? Timestamp.valueOf(utilisateur.getDateDerniereConnexion())
                            : null);
            pstmt.setBoolean(8, utilisateur.isActif());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La création de l'utilisateur a échoué, aucune ligne affectée.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    utilisateur.setIdUtilisateur(generatedKeys.getInt(1));
                } else {
                    throw new DataAccessException("La création de l'utilisateur a échoué, aucun ID obtenu.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la sauvegarde de l'utilisateur: " + utilisateur.getLogin(),
                    e);
        }
        return utilisateur;
    }

    @Override
    public Utilisateur update(Connection conn, Utilisateur utilisateur) throws SQLException {
        String sql = "UPDATE UTILISATEUR SET id_personnel = ?, login = ?, mot_de_passe = ?, salt = ?, role = ?, date_derniere_connexion = ?, actif = ? WHERE id_utilisateur = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (utilisateur.getIdPersonnel() != null)
                pstmt.setInt(1, utilisateur.getIdPersonnel());
            else
                pstmt.setNull(1, Types.INTEGER);
            pstmt.setString(2, utilisateur.getLogin());
            pstmt.setString(3, utilisateur.getMotDePasse());
            pstmt.setString(4, utilisateur.getSalt());
            pstmt.setString(5, utilisateur.getRole() != null ? utilisateur.getRole().getValeur() : null);
            pstmt.setTimestamp(6,
                    utilisateur.getDateDerniereConnexion() != null
                            ? Timestamp.valueOf(utilisateur.getDateDerniereConnexion())
                            : null);
            pstmt.setBoolean(7, utilisateur.isActif());
            pstmt.setInt(8, utilisateur.getIdUtilisateur());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("La mise à jour de l'utilisateur avec ID "
                        + utilisateur.getIdUtilisateur() + " a échoué, aucune ligne affectée.");
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Erreur lors de la mise à jour de l'utilisateur: " + utilisateur.getIdUtilisateur(), e);
        }
        return utilisateur;
    }

    @Override
    public boolean delete(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM UTILISATEUR WHERE id_utilisateur = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la suppression de l'utilisateur: " + id, e);
        }
    }

    @Override
    public long count(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM UTILISATEUR";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors du comptage des utilisateurs", e);
        }
        return 0;
    }

    @Override
    public Optional<Utilisateur> findByLogin(Connection conn, String login) throws SQLException {
        String sql = "SELECT * FROM UTILISATEUR WHERE login = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUtilisateur(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erreur lors de la recherche de l'utilisateur par login: " + login, e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Utilisateur> findByIdPersonnel(Connection conn, Integer idPersonnel) throws SQLException {
        String sql = "SELECT * FROM UTILISATEUR WHERE id_personnel = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (idPersonnel != null) {
                pstmt.setInt(1, idPersonnel);
            } else {
                pstmt.setNull(1, Types.INTEGER); // Permet de chercher id_personnel IS NULL si c'est le cas
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUtilisateur(rs));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(
                    "Erreur lors de la recherche de l'utilisateur par ID Personnel: " + idPersonnel, e);
        }
        return Optional.empty();
    }
}