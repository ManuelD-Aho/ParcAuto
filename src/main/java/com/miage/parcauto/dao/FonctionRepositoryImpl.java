package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.rh.Fonction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implémentation du Repository pour l'entité {@link Fonction}.
 * Gère les opérations CRUD pour les fonctions du personnel.
 */
public class FonctionRepositoryImpl implements Repository<Fonction, Integer> {

    private static final Logger LOGGER = Logger.getLogger(FonctionRepositoryImpl.class.getName());

    private static final String SQL_FIND_BY_ID = "SELECT id_fonction, lib_fonction FROM FONCTION WHERE id_fonction = ?";
    private static final String SQL_FIND_ALL = "SELECT id_fonction, lib_fonction FROM FONCTION ORDER BY lib_fonction ASC";
    private static final String SQL_FIND_ALL_PAGED = "SELECT id_fonction, lib_fonction FROM FONCTION ORDER BY lib_fonction ASC LIMIT ? OFFSET ?";
    private static final String SQL_SAVE = "INSERT INTO FONCTION (lib_fonction) VALUES (?)";
    private static final String SQL_UPDATE = "UPDATE FONCTION SET lib_fonction = ? WHERE id_fonction = ?";
    private static final String SQL_DELETE = "DELETE FROM FONCTION WHERE id_fonction = ?";
    private static final String SQL_COUNT = "SELECT COUNT(*) FROM FONCTION";

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Fonction> findById(Integer id) {
        if (id == null) return Optional.empty();
        Fonction fonction = null;
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_BY_ID)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    fonction = mapResultSetToFonction(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de Fonction par ID: " + id, e);
        }
        return Optional.ofNullable(fonction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Fonction> findAll() {
        List<Fonction> fonctions = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                fonctions.add(mapResultSetToFonction(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de toutes les Fonctions", e);
        }
        return fonctions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Fonction> findAll(int page, int size) {
        if (page < 0 || size <= 0) {
            LOGGER.log(Level.WARNING, "Pagination invalide : page={0}, size={1}", new Object[]{page, size});
            return new ArrayList<>();
        }
        List<Fonction> fonctions = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ALL_PAGED)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, page * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    fonctions.add(mapResultSetToFonction(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération paginée des Fonctions", e);
        }
        return fonctions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Fonction save(Fonction entity) {
        if (entity == null || entity.getLibFonction() == null || entity.getLibFonction().trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "Tentative de sauvegarde d'une entité Fonction nulle ou avec libellé vide.");
            return null;
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_SAVE, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, entity.getLibFonction());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("La création de Fonction a échoué, aucune ligne affectée.");
            }
            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setIdFonction(generatedKeys.getInt(1));
            } else {
                conn.rollback();
                throw new SQLException("La création de Fonction a échoué, aucun ID généré retourné.");
            }
            conn.commit();
            return entity;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la sauvegarde de Fonction: " + entity.getLibFonction(), e);
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
    public Fonction update(Fonction entity) {
        if (entity == null || entity.getIdFonction() == null || entity.getLibFonction() == null || entity.getLibFonction().trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "Tentative de mise à jour d'une entité Fonction nulle, avec ID nul ou libellé vide.");
            return null;
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_UPDATE);
            pstmt.setString(1, entity.getLibFonction());
            pstmt.setInt(2, entity.getIdFonction());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                conn.commit();
                return entity;
            } else {
                conn.rollback();
                LOGGER.log(Level.WARNING, "Aucune ligne mise à jour pour Fonction ID: {0}", entity.getIdFonction());
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de Fonction ID: " + entity.getIdFonction(), e);
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
        if (id == null) {
            LOGGER.log(Level.WARNING, "Tentative de suppression d'une Fonction avec ID nul.");
            return false;
        }
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
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de Fonction ID: " + id, e);
            dbUtilRollback(conn);
            // Vérifier si l'erreur est due à une contrainte de clé étrangère
            if (e.getSQLState().startsWith("23")) { // Code d'erreur SQL pour violation de contrainte d'intégrité
                LOGGER.log(Level.WARNING, "Impossible de supprimer Fonction ID: {0}. Elle est probablement utilisée par un Personnel.", id);
            }
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
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des Fonctions", e);
        }
        return count;
    }

    private Fonction mapResultSetToFonction(ResultSet rs) throws SQLException {
        Fonction fonction = new Fonction();
        fonction.setIdFonction(rs.getInt("id_fonction"));
        fonction.setLibFonction(rs.getString("lib_fonction"));
        return fonction;
    }

    private void dbUtilRollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction Fonction.", ex);
            }
        }
    }
}