package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.rh.Service;

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
 * Implémentation du Repository pour l'entité {@link Service}.
 * Gère les opérations CRUD pour les services de l'entreprise.
 */
public class ServiceRepositoryImpl implements Repository<Service, Integer> {

    private static final Logger LOGGER = Logger.getLogger(ServiceRepositoryImpl.class.getName());

    private static final String SQL_FIND_BY_ID = "SELECT id_service, lib_service FROM SERVICE WHERE id_service = ?";
    private static final String SQL_FIND_ALL = "SELECT id_service, lib_service FROM SERVICE ORDER BY lib_service ASC";
    private static final String SQL_FIND_ALL_PAGED = "SELECT id_service, lib_service FROM SERVICE ORDER BY lib_service ASC LIMIT ? OFFSET ?";
    private static final String SQL_SAVE = "INSERT INTO SERVICE (lib_service) VALUES (?)";
    private static final String SQL_UPDATE = "UPDATE SERVICE SET lib_service = ? WHERE id_service = ?";
    private static final String SQL_DELETE = "DELETE FROM SERVICE WHERE id_service = ?";
    private static final String SQL_COUNT = "SELECT COUNT(*) FROM SERVICE";

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Service> findById(Integer id) {
        if (id == null) return Optional.empty();
        Service service = null;
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_BY_ID)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    service = mapResultSetToService(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de Service par ID: " + id, e);
        }
        return Optional.ofNullable(service);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Service> findAll() {
        List<Service> services = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                services.add(mapResultSetToService(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les Services", e);
        }
        return services;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Service> findAll(int page, int size) {
        if (page < 0 || size <= 0) {
            LOGGER.log(Level.WARNING, "Pagination invalide : page={0}, size={1}", new Object[]{page, size});
            return new ArrayList<>();
        }
        List<Service> services = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ALL_PAGED)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, page * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    services.add(mapResultSetToService(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération paginée des Services", e);
        }
        return services;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Service save(Service entity) {
        if (entity == null || entity.getLibService() == null || entity.getLibService().trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "Tentative de sauvegarde d'une entité Service nulle ou avec libellé vide.");
            return null;
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_SAVE, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, entity.getLibService());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("La création de Service a échoué, aucune ligne affectée.");
            }
            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setIdService(generatedKeys.getInt(1));
            } else {
                conn.rollback();
                throw new SQLException("La création de Service a échoué, aucun ID généré retourné.");
            }
            conn.commit();
            return entity;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la sauvegarde de Service: " + entity.getLibService(), e);
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
    public Service update(Service entity) {
        if (entity == null || entity.getIdService() == null || entity.getLibService() == null || entity.getLibService().trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "Tentative de mise à jour d'une entité Service nulle, avec ID nul ou libellé vide.");
            return null;
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_UPDATE);
            pstmt.setString(1, entity.getLibService());
            pstmt.setInt(2, entity.getIdService());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                conn.commit();
                return entity;
            } else {
                conn.rollback();
                LOGGER.log(Level.WARNING, "Aucune ligne mise à jour pour Service ID: {0}", entity.getIdService());
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de Service ID: " + entity.getIdService(), e);
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
            LOGGER.log(Level.WARNING, "Tentative de suppression d'un Service avec ID nul.");
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
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de Service ID: " + id, e);
            dbUtilRollback(conn);
            // Vérifier si l'erreur est due à une contrainte de clé étrangère
            if (e.getSQLState().startsWith("23")) { // Code d'erreur SQL pour violation de contrainte d'intégrité
                LOGGER.log(Level.WARNING, "Impossible de supprimer Service ID: {0}. Il est probablement utilisé par un Personnel.", id);
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
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des Services", e);
        }
        return count;
    }

    private Service mapResultSetToService(ResultSet rs) throws SQLException {
        Service service = new Service();
        service.setIdService(rs.getInt("id_service"));
        service.setLibService(rs.getString("lib_service"));
        return service;
    }

    private void dbUtilRollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction Service.", ex);
            }
        }
    }
}