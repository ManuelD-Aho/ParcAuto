package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.assurance.Assurance;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;
import main.java.com.miage.parcauto.dao.Repository; // Ajout de l'import

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implémentation du Repository pour l'entité {@link Assurance}.
 * Gère les opérations CRUD pour les contrats d'assurance des véhicules.
 */
public class AssuranceRepositoryImpl implements Repository<Assurance, Integer> {

    private static final Logger LOGGER = Logger.getLogger(AssuranceRepositoryImpl.class.getName());

    private static final String SQL_SELECT_BASE = "SELECT a.id_assurance, a.id_vehicule, a.nom_assureur, a.num_police, " +
            "a.date_debut_assurance, a.date_fin_assurance, a.cout_annuel_assurance, a.type_couverture " +
            "FROM ASSURANCE a ";

    private static final String SQL_FIND_BY_ID = SQL_SELECT_BASE + "WHERE a.id_assurance = ?";
    private static final String SQL_FIND_ALL = SQL_SELECT_BASE + "ORDER BY a.date_fin_assurance DESC";
    private static final String SQL_FIND_ALL_PAGED = SQL_SELECT_BASE + "ORDER BY a.date_fin_assurance DESC LIMIT ? OFFSET ?";
    private static final String SQL_SAVE = "INSERT INTO ASSURANCE (id_vehicule, nom_assureur, num_police, date_debut_assurance, date_fin_assurance, cout_annuel_assurance, type_couverture) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE ASSURANCE SET id_vehicule = ?, nom_assureur = ?, num_police = ?, date_debut_assurance = ?, " +
            "date_fin_assurance = ?, cout_annuel_assurance = ?, type_couverture = ? WHERE id_assurance = ?";
    private static final String SQL_DELETE = "DELETE FROM ASSURANCE WHERE id_assurance = ?";
    private static final String SQL_COUNT = "SELECT COUNT(*) FROM ASSURANCE";
    private static final String SQL_FIND_BY_VEHICULE_ID = SQL_SELECT_BASE + "WHERE a.id_vehicule = ? ORDER BY a.date_fin_assurance DESC";
    private static final String SQL_FIND_EXPIRED_OR_EXPIRING_SOON = SQL_SELECT_BASE + "WHERE a.date_fin_assurance <= ? ORDER BY a.date_fin_assurance ASC";

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Assurance> findById(Integer id) {
        if (id == null) return Optional.empty();
        Assurance assurance = null;
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_BY_ID)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    assurance = mapResultSetToAssurance(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de Assurance par ID: " + id, e);
        }
        return Optional.ofNullable(assurance);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Assurance> findAll() {
        List<Assurance> assurances = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                assurances.add(mapResultSetToAssurance(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de toutes les Assurances", e);
        }
        return assurances;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Assurance> findAll(int page, int size) {
        if (page < 0 || size <= 0) {
            LOGGER.log(Level.WARNING, "Pagination invalide : page={0}, size={1}", new Object[]{page, size});
            return new ArrayList<>();
        }
        List<Assurance> assurances = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ALL_PAGED)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, page * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    assurances.add(mapResultSetToAssurance(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération paginée des Assurances", e);
        }
        return assurances;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Assurance save(Assurance entity) {
        if (entity == null || entity.getVehicule() == null || entity.getVehicule().getIdVehicule() == null) {
            LOGGER.log(Level.WARNING, "Tentative de sauvegarde d'une Assurance nulle ou sans véhicule associé.");
            return null; // id_vehicule est NOT NULL
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_SAVE, Statement.RETURN_GENERATED_KEYS);
            mapAssuranceToPreparedStatement(entity, pstmt, false);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("La création de Assurance a échoué, aucune ligne affectée.");
            }
            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setIdAssurance(generatedKeys.getInt(1));
            } else {
                conn.rollback();
                throw new SQLException("La création de Assurance a échoué, aucun ID généré retourné.");
            }
            conn.commit();
            return entity;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la sauvegarde de Assurance pour véhicule ID: " + entity.getVehicule().getIdVehicule(), e);
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
    public Assurance update(Assurance entity) {
        if (entity == null || entity.getIdAssurance() == null || entity.getVehicule() == null || entity.getVehicule().getIdVehicule() == null) {
            LOGGER.log(Level.WARNING, "Tentative de mise à jour d'une Assurance nulle, sans ID, ou sans véhicule associé.");
            return null;
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_UPDATE);
            mapAssuranceToPreparedStatement(entity, pstmt, true);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                conn.commit();
                return entity;
            } else {
                conn.rollback();
                LOGGER.log(Level.WARNING, "Aucune ligne mise à jour pour Assurance ID: {0}", entity.getIdAssurance());
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de Assurance ID: " + entity.getIdAssurance(), e);
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
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de Assurance ID: " + id, e);
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
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des Assurances", e);
        }
        return count;
    }

    /**
     * Recherche toutes les assurances associées à un ID de véhicule spécifique.
     * @param idVehicule L'identifiant du véhicule.
     * @return Une liste d'Assurance pour le véhicule donné.
     */
    public List<Assurance> findByVehiculeId(int idVehicule) {
        List<Assurance> assurances = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_BY_VEHICULE_ID)) {
            pstmt.setInt(1, idVehicule);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    assurances.add(mapResultSetToAssurance(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des Assurances pour le Véhicule ID: " + idVehicule, e);
        }
        return assurances;
    }

    /**
     * Recherche les assurances expirées ou expirant avant une date donnée.
     * @param dateLimite La date limite (inclusive).
     * @return Une liste d'assurances.
     */
    public List<Assurance> findExpiredOrExpiringSoon(LocalDate dateLimite) {
        if (dateLimite == null) return new ArrayList<>();
        List<Assurance> assurances = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_EXPIRED_OR_EXPIRING_SOON)) {
            pstmt.setDate(1, Date.valueOf(dateLimite));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    assurances.add(mapResultSetToAssurance(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des assurances expirant avant " + dateLimite, e);
        }
        return assurances;
    }

    private Assurance mapResultSetToAssurance(ResultSet rs) throws SQLException {
        Assurance assurance = new Assurance();
        assurance.setIdAssurance(rs.getInt("id_assurance"));

        int idVehicule = rs.getInt("id_vehicule");
        if (!rs.wasNull()) {
            Vehicule vehicule = new Vehicule(); // Partiellement chargé
            vehicule.setIdVehicule(idVehicule);
            assurance.setVehicule(vehicule);
        }

        assurance.setNomAssureur(rs.getString("nom_assureur"));
        assurance.setNumPolice(rs.getString("num_police"));
        Date dateDebutDb = rs.getDate("date_debut_assurance");
        if (dateDebutDb != null) {
            assurance.setDateDebutAssurance(dateDebutDb.toLocalDate());
        }
        Date dateFinDb = rs.getDate("date_fin_assurance");
        if (dateFinDb != null) {
            assurance.setDateFinAssurance(dateFinDb.toLocalDate());
        }
        assurance.setCoutAnnuelAssurance(rs.getBigDecimal("cout_annuel_assurance"));
        assurance.setTypeCouverture(rs.getString("type_couverture"));
        return assurance;
    }

    private void mapAssuranceToPreparedStatement(Assurance entity, PreparedStatement pstmt, boolean isUpdate) throws SQLException {
        int paramIndex = 1;

        pstmt.setInt(paramIndex++, entity.getVehicule().getIdVehicule()); // id_vehicule NOT NULL
        pstmt.setString(paramIndex++, entity.getNomAssureur()); // nom_assureur NOT NULL
        pstmt.setString(paramIndex++, entity.getNumPolice());   // num_police NOT NULL

        if (entity.getDateDebutAssurance() != null) { // date_debut_assurance NOT NULL
            pstmt.setDate(paramIndex++, Date.valueOf(entity.getDateDebutAssurance()));
        } else {
            pstmt.setNull(paramIndex++, Types.DATE); // Devrait être validé avant
        }
        if (entity.getDateFinAssurance() != null) { // date_fin_assurance NOT NULL
            pstmt.setDate(paramIndex++, Date.valueOf(entity.getDateFinAssurance()));
        } else {
            pstmt.setNull(paramIndex++, Types.DATE); // Devrait être validé avant
        }
        if (entity.getCoutAnnuelAssurance() != null) { // cout_annuel_assurance NOT NULL
            pstmt.setBigDecimal(paramIndex++, entity.getCoutAnnuelAssurance());
        } else {
            pstmt.setNull(paramIndex++, Types.DECIMAL); // Devrait être validé avant
        }
        pstmt.setString(paramIndex++, entity.getTypeCouverture());

        if (isUpdate) {
            pstmt.setInt(paramIndex, entity.getIdAssurance());
        }
    }

    private void dbUtilRollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction Assurance.", ex);
            }
        }
    }
}