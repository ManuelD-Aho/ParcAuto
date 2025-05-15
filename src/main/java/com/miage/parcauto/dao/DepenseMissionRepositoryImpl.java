package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.mission.DepenseMission;
import main.java.com.miage.parcauto.model.mission.Mission;
import main.java.com.miage.parcauto.model.mission.NatureDepenseMission; // Assurez-vous que ce fichier existe et correspond à l'ENUM de la DB

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implémentation du Repository pour l'entité {@link DepenseMission}.
 * Gère les opérations CRUD et les requêtes spécifiques pour les dépenses de missions.
 */
public class DepenseMissionRepositoryImpl implements Repository<DepenseMission, Integer> {

    private static final Logger LOGGER = Logger.getLogger(DepenseMissionRepositoryImpl.class.getName());

    private static final String SQL_SELECT_BASE = "SELECT d.id_depense, d.id_mission, d.date_depense, d.montant, " +
            "d.description_depense, d.nature_depense " +
            "FROM DEPENSE_MISSION d ";

    private static final String SQL_FIND_BY_ID = SQL_SELECT_BASE + "WHERE d.id_depense = ?";
    private static final String SQL_FIND_ALL = SQL_SELECT_BASE + "ORDER BY d.date_depense DESC";
    private static final String SQL_FIND_ALL_PAGED = SQL_SELECT_BASE + "ORDER BY d.date_depense DESC LIMIT ? OFFSET ?";
    private static final String SQL_SAVE = "INSERT INTO DEPENSE_MISSION (id_mission, date_depense, montant, description_depense, nature_depense) " +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE DEPENSE_MISSION SET id_mission = ?, date_depense = ?, montant = ?, " +
            "description_depense = ?, nature_depense = ? WHERE id_depense = ?";
    private static final String SQL_DELETE = "DELETE FROM DEPENSE_MISSION WHERE id_depense = ?";
    private static final String SQL_COUNT = "SELECT COUNT(*) FROM DEPENSE_MISSION";
    private static final String SQL_FIND_BY_MISSION_ID = SQL_SELECT_BASE + "WHERE d.id_mission = ? ORDER BY d.date_depense ASC";

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<DepenseMission> findById(Integer id) {
        if (id == null) return Optional.empty();
        DepenseMission depenseMission = null;
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_BY_ID)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    depenseMission = mapResultSetToDepenseMission(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de DepenseMission par ID: " + id, e);
        }
        return Optional.ofNullable(depenseMission);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DepenseMission> findAll() {
        List<DepenseMission> depenses = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                depenses.add(mapResultSetToDepenseMission(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de toutes les DepenseMissions", e);
        }
        return depenses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DepenseMission> findAll(int page, int size) {
        if (page < 0 || size <= 0) {
            LOGGER.log(Level.WARNING, "Pagination invalide : page={0}, size={1}", new Object[]{page, size});
            return new ArrayList<>();
        }
        List<DepenseMission> depenses = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ALL_PAGED)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, page * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    depenses.add(mapResultSetToDepenseMission(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération paginée des DepenseMissions", e);
        }
        return depenses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DepenseMission save(DepenseMission entity) {
        if (entity == null || entity.getMission() == null || entity.getMission().getIdMission() == null) {
            LOGGER.log(Level.WARNING, "Tentative de sauvegarde d'une DepenseMission nulle ou sans mission associée.");
            return null; // id_mission est NOT NULL
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_SAVE, Statement.RETURN_GENERATED_KEYS);
            mapDepenseMissionToPreparedStatement(entity, pstmt, false);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("La création de DepenseMission a échoué, aucune ligne affectée.");
            }
            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setIdDepense(generatedKeys.getInt(1));
            } else {
                conn.rollback();
                throw new SQLException("La création de DepenseMission a échoué, aucun ID généré retourné.");
            }
            conn.commit();
            return entity;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la sauvegarde de DepenseMission pour Mission ID: " + entity.getMission().getIdMission(), e);
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
    public DepenseMission update(DepenseMission entity) {
        if (entity == null || entity.getIdDepense() == null || entity.getMission() == null || entity.getMission().getIdMission() == null) {
            LOGGER.log(Level.WARNING, "Tentative de mise à jour d'une DepenseMission nulle, sans ID, ou sans mission associée.");
            return null;
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_UPDATE);
            mapDepenseMissionToPreparedStatement(entity, pstmt, true);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                conn.commit();
                return entity;
            } else {
                conn.rollback();
                LOGGER.log(Level.WARNING, "Aucune ligne mise à jour pour DepenseMission ID: {0}", entity.getIdDepense());
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de DepenseMission ID: " + entity.getIdDepense(), e);
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
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de DepenseMission ID: " + id, e);
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
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des DepenseMissions", e);
        }
        return count;
    }

    /**
     * Recherche toutes les dépenses associées à un ID de mission spécifique.
     * @param idMission L'identifiant de la mission.
     * @return Une liste de DepenseMission pour la mission donnée.
     */
    public List<DepenseMission> findByMissionId(int idMission) {
        List<DepenseMission> depenses = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_BY_MISSION_ID)) {
            pstmt.setInt(1, idMission);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    depenses.add(mapResultSetToDepenseMission(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des DepenseMissions pour la Mission ID: " + idMission, e);
        }
        return depenses;
    }

    private DepenseMission mapResultSetToDepenseMission(ResultSet rs) throws SQLException {
        DepenseMission depense = new DepenseMission();
        depense.setIdDepense(rs.getInt("id_depense"));

        int idMission = rs.getInt("id_mission");
        if (!rs.wasNull()) {
            Mission mission = new Mission(); // Partiellement chargé
            mission.setIdMission(idMission);
            depense.setMission(mission);
        }

        Timestamp dateDepenseTs = rs.getTimestamp("date_depense");
        if (dateDepenseTs != null) {
            depense.setDateDepense(dateDepenseTs.toLocalDateTime());
        }
        depense.setMontant(rs.getBigDecimal("montant"));
        depense.setDescription(rs.getString("description_depense")); // Correspond à 'description' dans l'entité

        String natureDepenseDb = rs.getString("nature_depense");
        if (natureDepenseDb != null) {
            try {
                depense.setNatureDepense(NatureDepenseMission.fromString(natureDepenseDb));
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.WARNING, "NatureDepenseMission inconnue '" + natureDepenseDb + "' pour Depense ID: " + depense.getIdDepense() + ". Valeurs attendues: " + NatureDepenseMission.getValidValues(), e);
            }
        }
        return depense;
    }

    private void mapDepenseMissionToPreparedStatement(DepenseMission entity, PreparedStatement pstmt, boolean isUpdate) throws SQLException {
        int paramIndex = 1;

        // id_mission est NOT NULL
        pstmt.setInt(paramIndex++, entity.getMission().getIdMission());

        if (entity.getDateDepense() != null) { // date_depense est NOT NULL
            pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(entity.getDateDepense()));
        } else {
            pstmt.setNull(paramIndex++, Types.TIMESTAMP); // Devrait être géré par la validation métier avant d'arriver ici
        }
        if (entity.getMontant() != null) { // montant est NOT NULL
            pstmt.setBigDecimal(paramIndex++, entity.getMontant());
        } else {
            pstmt.setNull(paramIndex++, Types.DECIMAL); // Devrait être géré
        }
        pstmt.setString(paramIndex++, entity.getDescription());

        if (entity.getNatureDepense() != null) { // nature_depense est NOT NULL
            pstmt.setString(paramIndex++, entity.getNatureDepense().getValeurDb());
        } else {
            pstmt.setNull(paramIndex++, Types.VARCHAR); // Devrait être géré
        }

        if (isUpdate) {
            pstmt.setInt(paramIndex, entity.getIdDepense());
        }
    }

    private void dbUtilRollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction DepenseMission.", ex);
            }
        }
    }
}