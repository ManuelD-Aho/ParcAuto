package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.mission.Mission;
import main.java.com.miage.parcauto.model.mission.StatutMission; // Assurez-vous que ce fichier existe et correspond à l'ENUM de la DB
import main.java.com.miage.parcauto.model.rh.Personnel;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implémentation du Repository pour l'entité {@link Mission}.
 * Gère les opérations CRUD et les requêtes spécifiques pour les missions.
 */
public class MissionRepositoryImpl implements Repository<Mission, Integer> {

    private static final Logger LOGGER = Logger.getLogger(MissionRepositoryImpl.class.getName());

    private static final String SQL_SELECT_BASE = "SELECT m.id_mission, m.date_debut_mission, m.date_fin_mission, m.lieu_depart, m.lieu_arrivee, " +
            "m.km_prevu, m.km_reel, m.motif, m.statut_mission, m.id_vehicule, m.id_personnel " +
            "FROM MISSION m ";

    private static final String SQL_FIND_BY_ID = SQL_SELECT_BASE + "WHERE m.id_mission = ?";
    private static final String SQL_FIND_ALL = SQL_SELECT_BASE + "ORDER BY m.date_debut_mission DESC";
    private static final String SQL_FIND_ALL_PAGED = SQL_SELECT_BASE + "ORDER BY m.date_debut_mission DESC LIMIT ? OFFSET ?";
    private static final String SQL_SAVE = "INSERT INTO MISSION (id_vehicule, id_personnel, date_debut_mission, date_fin_mission, lieu_depart, lieu_arrivee, km_prevu, km_reel, motif, statut_mission) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE MISSION SET id_vehicule = ?, id_personnel = ?, date_debut_mission = ?, date_fin_mission = ?, lieu_depart = ?, " +
            "lieu_arrivee = ?, km_prevu = ?, km_reel = ?, motif = ?, statut_mission = ? WHERE id_mission = ?";
    private static final String SQL_DELETE = "DELETE FROM MISSION WHERE id_mission = ?";
    private static final String SQL_COUNT = "SELECT COUNT(*) FROM MISSION";
    private static final String SQL_FIND_ACTIVE_FOR_VEHICULE_ID = SQL_SELECT_BASE + "WHERE m.id_vehicule = ? AND m.statut_mission = 'EnCours' ORDER BY m.date_debut_mission DESC";
    private static final String SQL_FIND_BY_PERIOD = SQL_SELECT_BASE + "WHERE m.date_debut_mission >= ? AND m.date_debut_mission < ? ORDER BY m.date_debut_mission ASC";


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Mission> findById(Integer id) {
        if (id == null) return Optional.empty();
        Mission mission = null;
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_BY_ID)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    mission = mapResultSetToMission(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de Mission par ID: " + id, e);
        }
        return Optional.ofNullable(mission);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Mission> findAll() {
        List<Mission> missions = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                missions.add(mapResultSetToMission(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de toutes les Missions", e);
        }
        return missions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Mission> findAll(int page, int size) {
        if (page < 0 || size <= 0) {
            LOGGER.log(Level.WARNING, "Pagination invalide : page={0}, size={1}", new Object[]{page, size});
            return new ArrayList<>();
        }
        List<Mission> missions = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ALL_PAGED)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, page * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    missions.add(mapResultSetToMission(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération paginée des Missions", e);
        }
        return missions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mission save(Mission entity) {
        if (entity == null || entity.getVehicule() == null || entity.getVehicule().getIdVehicule() == null ||
                entity.getPersonnel() == null || entity.getPersonnel().getIdPersonnel() == null) {
            LOGGER.log(Level.WARNING, "Tentative de sauvegarde d'une Mission nulle ou sans véhicule/personnel associé.");
            return null; // id_vehicule et id_personnel sont NOT NULL
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_SAVE, Statement.RETURN_GENERATED_KEYS);
            mapMissionToPreparedStatement(entity, pstmt, false);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("La création de Mission a échoué, aucune ligne affectée.");
            }
            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setIdMission(generatedKeys.getInt(1));
            } else {
                conn.rollback();
                throw new SQLException("La création de Mission a échoué, aucun ID généré retourné.");
            }
            conn.commit();
            return entity;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la sauvegarde de Mission", e);
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
    public Mission update(Mission entity) {
        if (entity == null || entity.getIdMission() == null ||
                entity.getVehicule() == null || entity.getVehicule().getIdVehicule() == null ||
                entity.getPersonnel() == null || entity.getPersonnel().getIdPersonnel() == null) {
            LOGGER.log(Level.WARNING, "Tentative de mise à jour d'une Mission nulle, sans ID, ou sans véhicule/personnel associé.");
            return null;
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_UPDATE);
            mapMissionToPreparedStatement(entity, pstmt, true);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                conn.commit();
                return entity;
            } else {
                conn.rollback();
                LOGGER.log(Level.WARNING, "Aucune ligne mise à jour pour Mission ID: {0}", entity.getIdMission());
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de Mission ID: " + entity.getIdMission(), e);
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
            // Avant de supprimer une mission, il faudrait gérer les DepenseMission associées
            // Soit par suppression en cascade en DB, soit explicitement ici ou dans un service.
            // Pour l'instant, ce repository ne gère que la table MISSION.
            pstmt = conn.prepareStatement(SQL_DELETE);
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            conn.commit();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de Mission ID: " + id, e);
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
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des Missions", e);
        }
        return count;
    }

    /**
     * Recherche les missions actives pour un véhicule spécifique.
     * @param idVehicule L'ID du véhicule.
     * @return Une liste de missions actives pour le véhicule.
     */
    public List<Mission> findActiveForVehiculeId(int idVehicule) {
        List<Mission> missions = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ACTIVE_FOR_VEHICULE_ID)) {
            pstmt.setInt(1, idVehicule);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    missions.add(mapResultSetToMission(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de Missions actives pour le véhicule ID: " + idVehicule, e);
        }
        return missions;
    }

    /**
     * Recherche les missions dont la date de début est comprise dans une période donnée.
     * @param debut La date de début de la période (inclusive).
     * @param fin La date de fin de la période (exclusive pour le jour).
     * @return Une liste de missions.
     */
    public List<Mission> findByPeriod(LocalDate debut, LocalDate fin) {
        if (debut == null || fin == null) return new ArrayList<>();
        List<Mission> missions = new ArrayList<>();
        LocalDateTime debutDateTime = debut.atStartOfDay();
        LocalDateTime finDateTime = fin.plusDays(1).atStartOfDay();

        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_BY_PERIOD)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(debutDateTime));
            pstmt.setTimestamp(2, Timestamp.valueOf(finDateTime));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    missions.add(mapResultSetToMission(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de Missions par période: " + debut + " à " + fin, e);
        }
        return missions;
    }

    private Mission mapResultSetToMission(ResultSet rs) throws SQLException {
        Mission mission = new Mission();
        mission.setIdMission(rs.getInt("id_mission"));

        int idVehicule = rs.getInt("id_vehicule");
        if (!rs.wasNull()) {
            Vehicule vehicule = new Vehicule();
            vehicule.setIdVehicule(idVehicule);
            mission.setVehicule(vehicule);
        }

        int idPersonnel = rs.getInt("id_personnel");
        if (!rs.wasNull()) {
            Personnel personnel = new Personnel();
            personnel.setIdPersonnel(idPersonnel);
            mission.setPersonnel(personnel);
        }

        Timestamp dateDebutTs = rs.getTimestamp("date_debut_mission");
        if (dateDebutTs != null) {
            mission.setDateDebutMission(dateDebutTs.toLocalDateTime());
        }
        Timestamp dateFinTs = rs.getTimestamp("date_fin_mission");
        if (dateFinTs != null) {
            mission.setDateFinMission(dateFinTs.toLocalDateTime());
        }
        mission.setLieuDepart(rs.getString("lieu_depart"));
        mission.setLieuArrivee(rs.getString("lieu_arrivee"));
        mission.setKmPrevu(rs.getObject("km_prevu", Integer.class));
        mission.setKmReel(rs.getObject("km_reel", Integer.class));
        mission.setMotif(rs.getString("motif"));

        String statutMissionDb = rs.getString("statut_mission");
        if (statutMissionDb != null) {
            try {
                mission.setStatutMission(StatutMission.fromString(statutMissionDb));
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.WARNING, "Statut Mission inconnu '" + statutMissionDb + "' pour Mission ID: " + mission.getIdMission() + ". Valeurs attendues: " + StatutMission.getValidValues(), e);
            }
        }
        // Les dépenses (DepenseMission) ne sont pas chargées ici,
        // elles le seront par DepenseMissionRepository ou par un service.
        return mission;
    }

    private void mapMissionToPreparedStatement(Mission entity, PreparedStatement pstmt, boolean isUpdate) throws SQLException {
        int paramIndex = 1;

        // id_vehicule et id_personnel sont NOT NULL
        pstmt.setInt(paramIndex++, entity.getVehicule().getIdVehicule());
        pstmt.setInt(paramIndex++, entity.getPersonnel().getIdPersonnel());

        if (entity.getDateDebutMission() != null) {
            pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(entity.getDateDebutMission()));
        } else {
            pstmt.setNull(paramIndex++, Types.TIMESTAMP); // date_debut_mission est NOT NULL
        }
        if (entity.getDateFinMission() != null) {
            pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(entity.getDateFinMission()));
        } else {
            pstmt.setNull(paramIndex++, Types.TIMESTAMP);
        }
        pstmt.setString(paramIndex++, entity.getLieuDepart());
        pstmt.setString(paramIndex++, entity.getLieuArrivee());
        pstmt.setObject(paramIndex++, entity.getKmPrevu(), Types.INTEGER);
        pstmt.setObject(paramIndex++, entity.getKmReel(), Types.INTEGER);
        pstmt.setString(paramIndex++, entity.getMotif());

        if (entity.getStatutMission() != null) {
            pstmt.setString(paramIndex++, entity.getStatutMission().getValeurDb());
        } else {
            pstmt.setNull(paramIndex++, Types.VARCHAR); // statut_mission est NOT NULL
        }

        if (isUpdate) {
            pstmt.setInt(paramIndex, entity.getIdMission());
        }
    }

    private void dbUtilRollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction Mission.", ex);
            }
        }
    }
}