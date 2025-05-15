package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.rh.Personnel;
import main.java.com.miage.parcauto.model.utilisateur.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implémentation du Repository pour l'entité {@link Utilisateur}.
 * Gère les opérations CRUD et les requêtes spécifiques pour les utilisateurs
 * en interagissant avec la base de données via JDBC.
 */
public class UtilisateurRepositoryImpl implements Repository<Utilisateur, Integer> {

    private static final Logger LOGGER = Logger.getLogger(UtilisateurRepositoryImpl.class.getName());

    // La jointure avec PERSONNEL est incluse pour récupérer l'ID du personnel associé.
    // Un service se chargerait de charger l'objet Personnel complet si nécessaire.
    private static final String SQL_SELECT_BASE = "SELECT u.id, u.login, u.hash, u.role, u.mfa_secret, u.id_personnel " +
            "FROM UTILISATEUR u ";
    // "LEFT JOIN PERSONNEL p ON u.id_personnel = p.id_personnel "; // Optionnel ici, dépend si on charge Personnel

    private static final String SQL_FIND_BY_ID = SQL_SELECT_BASE + "WHERE u.id = ?";
    private static final String SQL_FIND_BY_LOGIN = SQL_SELECT_BASE + "WHERE u.login = ?";
    private static final String SQL_FIND_ALL = SQL_SELECT_BASE;
    private static final String SQL_FIND_ALL_PAGED = SQL_SELECT_BASE + "ORDER BY u.id ASC LIMIT ? OFFSET ?";
    private static final String SQL_SAVE = "INSERT INTO UTILISATEUR (login, hash, role, id_personnel, mfa_secret) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE UTILISATEUR SET login = ?, hash = ?, role = ?, id_personnel = ?, mfa_secret = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM UTILISATEUR WHERE id = ?";
    private static final String SQL_COUNT = "SELECT COUNT(*) FROM UTILISATEUR";

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Utilisateur> findById(Integer id) {
        if (id == null) return Optional.empty();
        Utilisateur utilisateur = null;
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_BY_ID)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    utilisateur = mapResultSetToUtilisateur(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de Utilisateur par ID: " + id, e);
        }
        return Optional.ofNullable(utilisateur);
    }

    /**
     * Recherche un utilisateur par son login.
     * @param login Le login de l'utilisateur à rechercher.
     * @return Un Optional contenant l'utilisateur s'il est trouvé, sinon Optional.empty().
     */
    public Optional<Utilisateur> findByLogin(String login) {
        if (login == null || login.trim().isEmpty()) return Optional.empty();
        Utilisateur utilisateur = null;
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_BY_LOGIN)) {
            pstmt.setString(1, login);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    utilisateur = mapResultSetToUtilisateur(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de Utilisateur par login: " + login, e);
        }
        return Optional.ofNullable(utilisateur);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Utilisateur> findAll() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                utilisateurs.add(mapResultSetToUtilisateur(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les Utilisateurs", e);
        }
        return utilisateurs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Utilisateur> findAll(int page, int size) {
        if (page < 0 || size <= 0) {
            LOGGER.log(Level.WARNING, "Pagination invalide : page={0}, size={1}", new Object[]{page, size});
            return new ArrayList<>();
        }
        List<Utilisateur> utilisateurs = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ALL_PAGED)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, page * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    utilisateurs.add(mapResultSetToUtilisateur(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération paginée des Utilisateurs", e);
        }
        return utilisateurs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Utilisateur save(Utilisateur entity) {
        if (entity == null) return null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_SAVE, Statement.RETURN_GENERATED_KEYS);
            mapUtilisateurToPreparedStatement(entity, pstmt, false);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("La création de Utilisateur a échoué, aucune ligne affectée.");
            }
            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getInt(1));
            } else {
                conn.rollback();
                throw new SQLException("La création de Utilisateur a échoué, aucun ID généré retourné.");
            }
            conn.commit();
            return entity;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la sauvegarde de Utilisateur: " + entity.getLogin(), e);
            dbUtilRollback(conn);
            return null;
        } finally {
            DbUtil.closeQuietly(null, pstmt, generatedKeys);
            DbUtil.closeQuietly(conn, null, null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Utilisateur update(Utilisateur entity) {
        if (entity == null || entity.getId() == null) return null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_UPDATE);
            mapUtilisateurToPreparedStatement(entity, pstmt, true);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                conn.commit();
                return entity;
            } else {
                conn.rollback();
                LOGGER.log(Level.WARNING, "Aucune ligne mise à jour pour Utilisateur ID: {0}", entity.getId());
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de Utilisateur ID: " + entity.getId(), e);
            dbUtilRollback(conn);
            return null;
        } finally {
            DbUtil.closeQuietly(conn, pstmt);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Integer id) {
        if (id == null) return false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_DELETE);
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            conn.commit();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de Utilisateur ID: " + id, e);
            dbUtilRollback(conn);
            return false;
        } finally {
            DbUtil.closeQuietly(conn, pstmt);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long count() {
        long count = 0;
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_COUNT);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                count = rs.getLong(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des Utilisateurs", e);
        }
        return count;
    }

    private Utilisateur mapResultSetToUtilisateur(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(rs.getInt("id"));
        utilisateur.setLogin(rs.getString("login"));
        utilisateur.setHashMotDePasse(rs.getString("hash")); // Nom du champ dans l'entité

        String roleDb = rs.getString("role");
        if (roleDb != null) {
            try {
                utilisateur.setRole(RoleUtilisateur.fromString(roleDb));
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.WARNING, "Rôle utilisateur inconnu '" + roleDb + "' trouvé en base pour l'utilisateur ID: " + utilisateur.getId(), e);
                // Gérer le cas d'un rôle inconnu, par exemple en mettant null ou un rôle par défaut.
                // Pour l'instant, on laisse le rôle à null si non trouvé.
            }
        }

        utilisateur.setMfaSecret(rs.getString("mfa_secret"));

        int idPersonnel = rs.getInt("id_personnel");
        if (!rs.wasNull()) {
            // Créer un objet Personnel "partiel" avec juste l'ID.
            // La couche service se chargera de le charger complètement si besoin.
            Personnel personnel = new Personnel();
            personnel.setIdPersonnel(idPersonnel);
            utilisateur.setPersonnel(personnel);
        }
        return utilisateur;
    }

    private void mapUtilisateurToPreparedStatement(Utilisateur entity, PreparedStatement pstmt, boolean isUpdate) throws SQLException {
        int paramIndex = 1;
        pstmt.setString(paramIndex++, entity.getLogin());
        pstmt.setString(paramIndex++, entity.getHashMotDePasse());
        if (entity.getRole() != null) {
            pstmt.setString(paramIndex++, entity.getRole().getCodeDb());
        } else {
            pstmt.setNull(paramIndex++, Types.VARCHAR); // Ou le type ENUM si la DB le gère spécifiquement
        }
        if (entity.getPersonnel() != null && entity.getPersonnel().getIdPersonnel() != null) {
            pstmt.setInt(paramIndex++, entity.getPersonnel().getIdPersonnel());
        } else {
            pstmt.setNull(paramIndex++, Types.INTEGER);
        }
        pstmt.setString(paramIndex++, entity.getMfaSecret());

        if (isUpdate) {
            pstmt.setInt(paramIndex, entity.getId());
        }
    }

    private void dbUtilRollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction Utilisateur.", ex);
            }
        }
    }
}