package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.vehicule.EtatVoiture;

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
 * Implémentation du Repository pour l'entité {@link EtatVoiture}.
 * Gère les opérations CRUD pour les états de voiture en interagissant
 * avec la base de données via JDBC.
 */
public class EtatVoitureRepositoryImpl implements Repository<EtatVoiture, Integer> {

    private static final Logger LOGGER = Logger.getLogger(EtatVoitureRepositoryImpl.class.getName());

    private static final String SQL_FIND_BY_ID = "SELECT id_etat_voiture, lib_etat_voiture FROM ETAT_VOITURE WHERE id_etat_voiture = ?";
    private static final String SQL_FIND_ALL = "SELECT id_etat_voiture, lib_etat_voiture FROM ETAT_VOITURE";
    private static final String SQL_FIND_ALL_PAGED = "SELECT id_etat_voiture, lib_etat_voiture FROM ETAT_VOITURE LIMIT ? OFFSET ?";
    private static final String SQL_SAVE = "INSERT INTO ETAT_VOITURE (lib_etat_voiture) VALUES (?)";
    private static final String SQL_UPDATE = "UPDATE ETAT_VOITURE SET lib_etat_voiture = ? WHERE id_etat_voiture = ?";
    private static final String SQL_DELETE = "DELETE FROM ETAT_VOITURE WHERE id_etat_voiture = ?";
    private static final String SQL_COUNT = "SELECT COUNT(*) FROM ETAT_VOITURE";

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<EtatVoiture> findById(Integer id) {
        if (id == null) {
            return Optional.empty();
        }
        EtatVoiture etatVoiture = null;
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_BY_ID)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    etatVoiture = mapResultSetToEtatVoiture(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de EtatVoiture par ID: " + id, e);
        }
        return Optional.ofNullable(etatVoiture);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EtatVoiture> findAll() {
        List<EtatVoiture> etats = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                etats.add(mapResultSetToEtatVoiture(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les EtatVoiture", e);
        }
        return etats;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EtatVoiture> findAll(int page, int size) {
        if (page < 0 || size <= 0) {
            LOGGER.log(Level.WARNING, "Pagination invalide : page={0}, size={1}", new Object[]{page, size});
            return new ArrayList<>();
        }
        List<EtatVoiture> etats = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ALL_PAGED)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, page * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    etats.add(mapResultSetToEtatVoiture(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération paginée des EtatVoiture", e);
        }
        return etats;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EtatVoiture save(EtatVoiture entity) {
        if (entity == null || entity.getLibEtatVoiture() == null || entity.getLibEtatVoiture().trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "Tentative de sauvegarde d'une entité EtatVoiture nulle ou avec libellé vide.");
            return null;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_SAVE, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, entity.getLibEtatVoiture());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("La création de EtatVoiture a échoué, aucune ligne affectée.");
            }

            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setIdEtatVoiture(generatedKeys.getInt(1));
            } else {
                conn.rollback();
                throw new SQLException("La création de EtatVoiture a échoué, aucun ID généré retourné.");
            }
            conn.commit();
            return entity;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la sauvegarde de EtatVoiture", e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la sauvegarde de EtatVoiture", ex);
                }
            }
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
    public EtatVoiture update(EtatVoiture entity) {
        if (entity == null || entity.getIdEtatVoiture() == null || entity.getLibEtatVoiture() == null || entity.getLibEtatVoiture().trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "Tentative de mise à jour d'une entité EtatVoiture nulle, avec ID nul ou libellé vide.");
            return null;
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_UPDATE);
            pstmt.setString(1, entity.getLibEtatVoiture());
            pstmt.setInt(2, entity.getIdEtatVoiture());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                conn.commit();
                return entity;
            } else {
                conn.rollback();
                LOGGER.log(Level.WARNING, "Aucune ligne mise à jour pour EtatVoiture ID: {0}. L'entité n'existe peut-être pas.", entity.getIdEtatVoiture());
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de EtatVoiture ID: " + entity.getIdEtatVoiture(), e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la mise à jour de EtatVoiture", ex);
                }
            }
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
            LOGGER.log(Level.WARNING, "Tentative de suppression d'un EtatVoiture avec ID nul.");
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
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de EtatVoiture ID: " + id, e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la suppression de EtatVoiture", ex);
                }
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
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des EtatVoiture", e);
        }
        return count;
    }

    /**
     * Mappe un {@link ResultSet} à un objet {@link EtatVoiture}.
     *
     * @param rs Le ResultSet à mapper.
     * @return L'objet EtatVoiture mappé.
     * @throws SQLException Si une erreur SQL se produit lors de l'accès aux colonnes.
     */
    private EtatVoiture mapResultSetToEtatVoiture(ResultSet rs) throws SQLException {
        EtatVoiture etatVoiture = new EtatVoiture();
        etatVoiture.setIdEtatVoiture(rs.getInt("id_etat_voiture"));
        etatVoiture.setLibEtatVoiture(rs.getString("lib_etat_voiture"));
        return etatVoiture;
    }
}