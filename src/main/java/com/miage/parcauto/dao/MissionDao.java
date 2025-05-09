package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.mission.Mission;
import main.java.com.miage.parcauto.model.mission.DepenseMission;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;
import main.java.com.miage.parcauto.model.vehicule.EtatVoiture;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe d'accès aux données pour les missions et leurs dépenses associées.
 * Gère les opérations CRUD et les requêtes spécifiques liées aux missions.
 *
 * @author MIAGE Holding
 * @version 1.1
 */
public class MissionDao {

    private static final Logger LOGGER = Logger.getLogger(MissionDao.class.getName());

    // Instance de DbUtil pour la gestion des connexions
    private final DbUtil dbUtil;

    // Instance de VehiculeDao pour les opérations liées aux véhicules
    private final VehiculeDao vehiculeDao;

    /**
     * Constructeur par défaut. Initialise les instances de DbUtil et VehiculeDao.
     */
    public MissionDao() {
        this.dbUtil = DbUtil.getInstance();
        this.vehiculeDao = new VehiculeDao();
    }

    /**
     * Constructeur avec injection de dépendance pour les tests.
     *
     * @param dbUtil Instance de DbUtil à utiliser
     * @param vehiculeDao Instance de VehiculeDao à utiliser
     */
    public MissionDao(DbUtil dbUtil, VehiculeDao vehiculeDao) {
        this.dbUtil = dbUtil;
        this.vehiculeDao = vehiculeDao;
    }

    /**
     * Récupère toutes les missions de la base de données.
     *
     * @return Liste des missions
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Mission> findAll() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Mission> missions = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT m.*, v.immatriculation, v.marque, v.modele " +
                    "FROM MISSION m " +
                    "JOIN VEHICULES v ON m.id_vehicule = v.id_vehicule " +
                    "ORDER BY m.date_debut_mission DESC";

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Mission mission = extractMissionFromResultSet(rs);
                missions.add(mission);
            }

            return missions;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de toutes les missions", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère une mission par son ID.
     *
     * @param id ID de la mission à récupérer
     * @return Optional contenant la mission si elle existe, vide sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Optional<Mission> findById(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT m.*, v.immatriculation, v.marque, v.modele " +
                    "FROM MISSION m " +
                    "JOIN VEHICULES v ON m.id_vehicule = v.id_vehicule " +
                    "WHERE m.id_mission = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Mission mission = extractMissionFromResultSet(rs);
                // Récupération des dépenses associées à cette mission
                mission.setDepenses(findDepensesByMissionId(id, conn));
                return Optional.of(mission);
            }

            return Optional.empty();

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de la mission par ID: " + id, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère les missions pour un véhicule spécifique.
     *
     * @param idVehicule ID du véhicule
     * @return Liste des missions associées au véhicule
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Mission> findByVehicule(int idVehicule) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Mission> missions = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT m.*, v.immatriculation, v.marque, v.modele " +
                    "FROM MISSION m " +
                    "JOIN VEHICULES v ON m.id_vehicule = v.id_vehicule " +
                    "WHERE m.id_vehicule = ? " +
                    "ORDER BY m.date_debut_mission DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idVehicule);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                missions.add(extractMissionFromResultSet(rs));
            }

            return missions;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des missions pour le véhicule ID: " + idVehicule, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère les missions selon leur statut.
     *
     * @param status Statut des missions à récupérer
     * @return Liste des missions ayant ce statut
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Mission> findByStatus(Mission.StatusMission status) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Mission> missions = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT m.*, v.immatriculation, v.marque, v.modele " +
                    "FROM MISSION m " +
                    "JOIN VEHICULES v ON m.id_vehicule = v.id_vehicule " +
                    "WHERE m.status = ? " +
                    "ORDER BY m.date_debut_mission DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status.name());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                missions.add(extractMissionFromResultSet(rs));
            }

            return missions;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des missions par statut: " + status, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère les missions par période.
     *
     * @param debut Date de début de la période
     * @param fin Date de fin de la période
     * @return Liste des missions pendant cette période
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Mission> findByPeriode(LocalDateTime debut, LocalDateTime fin) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Mission> missions = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT m.*, v.immatriculation, v.marque, v.modele " +
                    "FROM MISSION m " +
                    "JOIN VEHICULES v ON m.id_vehicule = v.id_vehicule " +
                    "WHERE (m.date_debut_mission BETWEEN ? AND ?) OR " +
                    "(m.date_fin_mission BETWEEN ? AND ?) OR " +
                    "(m.date_debut_mission <= ? AND (m.date_fin_mission >= ? OR m.date_fin_mission IS NULL)) " +
                    "ORDER BY m.date_debut_mission DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, Timestamp.valueOf(debut));
            pstmt.setTimestamp(2, Timestamp.valueOf(fin));
            pstmt.setTimestamp(3, Timestamp.valueOf(debut));
            pstmt.setTimestamp(4, Timestamp.valueOf(fin));
            pstmt.setTimestamp(5, Timestamp.valueOf(debut));
            pstmt.setTimestamp(6, Timestamp.valueOf(fin));

            rs = pstmt.executeQuery();

            while (rs.next()) {
                missions.add(extractMissionFromResultSet(rs));
            }

            return missions;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des missions par période", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Recherche des missions par site.
     *
     * @param site Site de la mission
     * @return Liste des missions pour ce site
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Mission> findBySite(String site) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Mission> missions = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT m.*, v.immatriculation, v.marque, v.modele " +
                    "FROM MISSION m " +
                    "JOIN VEHICULES v ON m.id_vehicule = v.id_vehicule " +
                    "WHERE m.site LIKE ? " +
                    "ORDER BY m.date_debut_mission DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + site + "%");

            rs = pstmt.executeQuery();

            while (rs.next()) {
                missions.add(extractMissionFromResultSet(rs));
            }

            return missions;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des missions par site: " + site, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Recherche de missions par critères textuels.
     *
     * @param searchTerm Terme de recherche
     * @return Liste des missions correspondant aux critères
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Mission> search(String searchTerm) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Mission> missions = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            // Préparer le terme de recherche pour LIKE
            String searchPattern = "%" + searchTerm + "%";

            String sql = "SELECT m.*, v.immatriculation, v.marque, v.modele " +
                    "FROM MISSION m " +
                    "JOIN VEHICULES v ON m.id_vehicule = v.id_vehicule " +
                    "WHERE m.lib_mission LIKE ? " +
                    "OR m.site LIKE ? " +
                    "OR m.circuit_mission LIKE ? " +
                    "OR m.observation_mission LIKE ? " +
                    "OR v.immatriculation LIKE ? " +
                    "ORDER BY m.date_debut_mission DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            pstmt.setString(5, searchPattern);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                missions.add(extractMissionFromResultSet(rs));
            }

            return missions;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des missions avec le terme: " + searchTerm, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère les missions en cours actuellement.
     *
     * @return Liste des missions en cours
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Mission> findMissionsEnCours() throws SQLException {
        return findByStatus(Mission.StatusMission.EnCours);
    }

    /**
     * Récupère les missions planifiées.
     *
     * @return Liste des missions planifiées
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Mission> findMissionsPlanifiees() throws SQLException {
        return findByStatus(Mission.StatusMission.Planifiee);
    }

    /**
     * Récupère les missions clôturées.
     *
     * @return Liste des missions clôturées
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Mission> findMissionsCloturees() throws SQLException {
        return findByStatus(Mission.StatusMission.Cloturee);
    }

    /**
     * Crée une nouvelle mission dans la base de données.
     * Met à jour l'état du véhicule si la mission est immédiatement en cours.
     *
     * @param mission La mission à créer
     * @param updateVehiculeEtat Si true, met à jour l'état du véhicule
     * @return La mission créée avec son ID généré
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Mission create(Mission mission, boolean updateVehiculeEtat) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();
            conn.setAutoCommit(false);  // Début transaction

            String sql = "INSERT INTO MISSION (id_vehicule, lib_mission, site, date_debut_mission, " +
                    "date_fin_mission, km_prevu, km_reel, status, cout_total, circuit_mission, observation_mission) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Paramètres obligatoires
            pstmt.setInt(1, mission.getIdVehicule());
            pstmt.setString(2, mission.getLibMission());
            pstmt.setString(3, mission.getSite());

            if (mission.getDateDebutMission() != null) {
                pstmt.setTimestamp(4, Timestamp.valueOf(mission.getDateDebutMission()));
            } else {
                pstmt.setNull(4, Types.TIMESTAMP);
            }

            // Paramètres optionnels
            if (mission.getDateFinMission() != null) {
                pstmt.setTimestamp(5, Timestamp.valueOf(mission.getDateFinMission()));
            } else {
                pstmt.setNull(5, Types.TIMESTAMP);
            }

            if (mission.getKmPrevu() != null) {
                pstmt.setInt(6, mission.getKmPrevu());
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }

            if (mission.getKmReel() != null) {
                pstmt.setInt(7, mission.getKmReel());
            } else {
                pstmt.setNull(7, Types.INTEGER);
            }

            if (mission.getStatus() != null) {
                pstmt.setString(8, mission.getStatus().name());
            } else {
                pstmt.setString(8, Mission.StatusMission.Planifiee.name());
            }

            if (mission.getCoutTotal() != null) {
                pstmt.setBigDecimal(9, mission.getCoutTotal());
            } else {
                pstmt.setBigDecimal(9, BigDecimal.ZERO);
            }

            pstmt.setString(10, mission.getCircuitMission());
            pstmt.setString(11, mission.getObservationMission());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La création de la mission a échoué, aucune ligne affectée.");
            }

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                mission.setIdMission(rs.getInt(1));
            } else {
                throw new SQLException("La création de la mission a échoué, aucun ID généré.");
            }

            // Si la mission est en cours et que la mise à jour de l'état du véhicule est demandée
            if (updateVehiculeEtat && mission.getStatus() == Mission.StatusMission.EnCours) {
                vehiculeDao.updateEtat(mission.getIdVehicule(), EtatVoiture.EN_MISSION);
            }

            // Si des dépenses sont associées à la mission, les enregistrer
            if (mission.getDepenses() != null && !mission.getDepenses().isEmpty()) {
                for (DepenseMission depense : mission.getDepenses()) {
                    depense.setIdMission(mission.getIdMission());
                    createDepense(depense, conn);
                }
            }

            conn.commit();  // Valider transaction
            return mission;

        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback();  // Annuler transaction en cas d'erreur
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction", e);
                }
            }
            LOGGER.log(Level.SEVERE, "Erreur lors de la création de la mission", ex);
            throw ex;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);  // Rétablir autocommit
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Erreur lors du rétablissement de l'autocommit", e);
                }
            }
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Met à jour une mission dans la base de données.
     * Gère également la mise à jour de l'état du véhicule si le statut change.
     *
     * @param mission La mission à mettre à jour
     * @param updateVehiculeEtat Si true, met à jour l'état du véhicule si nécessaire
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean update(Mission mission, boolean updateVehiculeEtat) throws SQLException {
        if (mission.getIdMission() == null) {
            throw new IllegalArgumentException("L'ID de la mission ne peut pas être null pour une mise à jour");
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();
            conn.setAutoCommit(false);  // Début transaction

            // Récupérer l'état actuel de la mission pour détecter les changements de statut
            String sqlSelect = "SELECT status FROM MISSION WHERE id_mission = ?";
            pstmt = conn.prepareStatement(sqlSelect);
            pstmt.setInt(1, mission.getIdMission());

            rs = pstmt.executeQuery();

            Mission.StatusMission oldStatus = null;
            if (rs.next()) {
                oldStatus = Mission.StatusMission.valueOf(rs.getString("status"));
            } else {
                throw new SQLException("Mission non trouvée avec l'ID: " + mission.getIdMission());
            }

            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);

            // Mise à jour de la mission
            String sql = "UPDATE MISSION SET id_vehicule = ?, lib_mission = ?, site = ?, " +
                    "date_debut_mission = ?, date_fin_mission = ?, km_prevu = ?, km_reel = ?, " +
                    "status = ?, cout_total = ?, circuit_mission = ?, observation_mission = ? " +
                    "WHERE id_mission = ?";

            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, mission.getIdVehicule());
            pstmt.setString(2, mission.getLibMission());
            pstmt.setString(3, mission.getSite());

            if (mission.getDateDebutMission() != null) {
                pstmt.setTimestamp(4, Timestamp.valueOf(mission.getDateDebutMission()));
            } else {
                pstmt.setNull(4, Types.TIMESTAMP);
            }

            if (mission.getDateFinMission() != null) {
                pstmt.setTimestamp(5, Timestamp.valueOf(mission.getDateFinMission()));
            } else {
                pstmt.setNull(5, Types.TIMESTAMP);
            }

            if (mission.getKmPrevu() != null) {
                pstmt.setInt(6, mission.getKmPrevu());
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }

            if (mission.getKmReel() != null) {
                pstmt.setInt(7, mission.getKmReel());
            } else {
                pstmt.setNull(7, Types.INTEGER);
            }

            pstmt.setString(8, mission.getStatus().name());

            if (mission.getCoutTotal() != null) {
                pstmt.setBigDecimal(9, mission.getCoutTotal());
            } else {
                pstmt.setBigDecimal(9, BigDecimal.ZERO);
            }

            pstmt.setString(10, mission.getCircuitMission());
            pstmt.setString(11, mission.getObservationMission());

            pstmt.setInt(12, mission.getIdMission());

            int affectedRows = pstmt.executeUpdate();

            // Si la mise à jour de l'état du véhicule est demandée et que le statut a changé
            if (updateVehiculeEtat && !oldStatus.equals(mission.getStatus())) {
                if (mission.getStatus() == Mission.StatusMission.EnCours) {
                    vehiculeDao.updateEtat(mission.getIdVehicule(), EtatVoiture.EN_MISSION);
                } else if (mission.getStatus() == Mission.StatusMission.Cloturee) {
                    vehiculeDao.updateEtat(mission.getIdVehicule(), EtatVoiture.DISPONIBLE);
                }
            }

            // Si des dépenses sont modifiées, les mettre à jour
            if (mission.getDepenses() != null) {
                // Supprimer toutes les dépenses existantes
                deleteAllDepensesByMissionId(mission.getIdMission(), conn);

                // Recréer les dépenses
                for (DepenseMission depense : mission.getDepenses()) {
                    depense.setIdMission(mission.getIdMission());
                    createDepense(depense, conn);
                }
            }

            conn.commit();  // Valider transaction
            return affectedRows > 0;

        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback();  // Annuler transaction en cas d'erreur
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction", e);
                }
            }
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de la mission ID: " + mission.getIdMission(), ex);
            throw ex;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);  // Rétablir autocommit
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Erreur lors du rétablissement de l'autocommit", e);
                }
            }
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Met à jour le statut d'une mission.
     * Met également à jour l'état du véhicule si nécessaire.
     *
     * @param idMission ID de la mission
     * @param status Nouveau statut
     * @param kmReel Kilométrage réel (obligatoire pour les missions clôturées)
     * @param updateVehiculeEtat Si true, met à jour l'état du véhicule selon le statut
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     * @throws IllegalArgumentException Si le kilométrage est manquant pour une mission clôturée
     */
    public boolean updateStatus(int idMission, Mission.StatusMission status, Integer kmReel, boolean updateVehiculeEtat)
            throws SQLException, IllegalArgumentException {

        if (status == Mission.StatusMission.Cloturee && kmReel == null) {
            throw new IllegalArgumentException("Le kilométrage réel est obligatoire pour clôturer une mission");
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();
            conn.setAutoCommit(false);  // Début transaction

            // Récupérer les informations actuelles de la mission
            String sqlSelect = "SELECT id_vehicule, status FROM MISSION WHERE id_mission = ?";
            pstmt = conn.prepareStatement(sqlSelect);
            pstmt.setInt(1, idMission);

            rs = pstmt.executeQuery();

            if (!rs.next()) {
                return false;  // Mission non trouvée
            }

            int idVehicule = rs.getInt("id_vehicule");
            Mission.StatusMission oldStatus = Mission.StatusMission.valueOf(rs.getString("status"));

            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);

            // Mise à jour du statut de la mission
            StringBuilder sqlUpdate = new StringBuilder("UPDATE MISSION SET status = ?");

            // Si on clôture la mission, ajouter la date de fin et le kilométrage réel
            if (status == Mission.StatusMission.Cloturee) {
                sqlUpdate.append(", date_fin_mission = ?, km_reel = ?");
            }

            sqlUpdate.append(" WHERE id_mission = ?");

            pstmt = conn.prepareStatement(sqlUpdate.toString());
            pstmt.setString(1, status.name());

            if (status == Mission.StatusMission.Cloturee) {
                pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                pstmt.setInt(3, kmReel);
                pstmt.setInt(4, idMission);
            } else {
                pstmt.setInt(2, idMission);
            }

            int affectedRows = pstmt.executeUpdate();

            // Si la mise à jour a réussi et que l'état du véhicule doit être mis à jour
            if (updateVehiculeEtat && affectedRows > 0 && !oldStatus.equals(status)) {
                if (status == Mission.StatusMission.EnCours) {
                    vehiculeDao.updateEtat(idVehicule, EtatVoiture.EN_MISSION);
                } else if (status == Mission.StatusMission.Cloturee) {
                    vehiculeDao.updateEtat(idVehicule, EtatVoiture.DISPONIBLE);

                    // Mise à jour du coût total en incluant les dépenses
                    updateCoutTotalFromDepenses(idMission, conn);
                }
            }

            conn.commit();  // Valider transaction
            return affectedRows > 0;

        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback();  // Annuler transaction en cas d'erreur
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction", e);
                }
            }
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du statut de la mission ID: " + idMission, ex);
            throw ex;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);  // Rétablir autocommit
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Erreur lors du rétablissement de l'autocommit", e);
                }
            }
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Démarre une mission (passage au statut "en cours").
     *
     * @param idMission ID de la mission
     * @return true si le changement d'état a été effectué
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean demarrerMission(int idMission) throws SQLException {
        return updateStatus(idMission, Mission.StatusMission.EnCours, null, true);
    }

    /**
     * Clôture une mission (passage au statut "clôturée").
     *
     * @param idMission ID de la mission
     * @param kmReel Kilométrage réel (obligatoire)
     * @return true si le changement d'état a été effectué
     * @throws SQLException En cas d'erreur d'accès à la base de données
     * @throws IllegalArgumentException Si le kilométrage est manquant
     */
    public boolean cloturerMission(int idMission, int kmReel) throws SQLException, IllegalArgumentException {
        return updateStatus(idMission, Mission.StatusMission.Cloturee, kmReel, true);
    }

    /**
     * Supprime une mission de la base de données.
     * Vérifie que la mission n'est pas en cours avant de la supprimer.
     *
     * @param id ID de la mission à supprimer
     * @param forceDelete Si true, supprime la mission même si elle est en cours
     * @return true si la suppression a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     * @throws IllegalStateException Si la mission est en cours et que forceDelete est false
     */
    public boolean delete(int id, boolean forceDelete) throws SQLException, IllegalStateException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();
            conn.setAutoCommit(false);  // Début transaction

            // Vérifier si la mission est en cours
            if (!forceDelete) {
                String sqlCheck = "SELECT status FROM MISSION WHERE id_mission = ?";
                pstmt = conn.prepareStatement(sqlCheck);
                pstmt.setInt(1, id);

                rs = pstmt.executeQuery();

                if (rs.next() && Mission.StatusMission.valueOf(rs.getString("status")) == Mission.StatusMission.EnCours) {
                    throw new IllegalStateException("Impossible de supprimer une mission en cours. Utilisez forceDelete=true pour forcer la suppression.");
                }

                dbUtil.closeResultSet(rs);
                dbUtil.closePreparedStatement(pstmt);
            }

            // Supprimer d'abord les dépenses associées à la mission (la contrainte devrait le faire automatiquement avec ON DELETE CASCADE)
            String sqlDeleteDepenses = "DELETE FROM DEPENSE_MISSION WHERE id_mission = ?";
            pstmt = conn.prepareStatement(sqlDeleteDepenses);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            dbUtil.closePreparedStatement(pstmt);

            // Supprimer ensuite la mission
            String sqlDeleteMission = "DELETE FROM MISSION WHERE id_mission = ?";
            pstmt = conn.prepareStatement(sqlDeleteMission);
            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();

            conn.commit();  // Valider transaction
            return affectedRows > 0;

        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback();  // Annuler transaction en cas d'erreur
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction", e);
                }
            }
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de la mission ID: " + id, ex);
            throw ex;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);  // Rétablir autocommit
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Erreur lors du rétablissement de l'autocommit", e);
                }
            }
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Crée une dépense de mission dans la base de données.
     *
     * @param depense La dépense à créer
     * @param conn Une connexion à la base de données existante (transaction en cours)
     * @return La dépense créée avec son ID généré
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    private DepenseMission createDepense(DepenseMission depense, Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql = "INSERT INTO DEPENSE_MISSION (id_mission, nature, montant, justificatif) " +
                    "VALUES (?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, depense.getIdMission());
            pstmt.setString(2, depense.getNature().name());
            pstmt.setBigDecimal(3, depense.getMontant());
            pstmt.setString(4, depense.getJustificatif());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La création de la dépense a échoué, aucune ligne affectée.");
            }

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                depense.setId(rs.getInt(1));
            } else {
                throw new SQLException("La création de la dépense a échoué, aucun ID généré.");
            }

            return depense;

        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Ajoute une dépense à une mission.
     * Met à jour également le coût total de la mission.
     *
     * @param depense La dépense à ajouter
     * @return La dépense créée avec son ID généré
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public DepenseMission addDepense(DepenseMission depense) throws SQLException {
        Connection conn = null;

        try {
            conn = dbUtil.getConnection();
            conn.setAutoCommit(false);  // Début transaction

            // Créer la dépense
            DepenseMission createdDepense = createDepense(depense, conn);

            // Mettre à jour le coût total de la mission
            updateCoutTotalFromDepenses(depense.getIdMission(), conn);

            conn.commit();  // Valider transaction
            return createdDepense;

        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback();  // Annuler transaction en cas d'erreur
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction", e);
                }
            }
            LOGGER.log(Level.SEVERE, "Erreur lors de l'ajout d'une dépense à la mission ID: " + depense.getIdMission(), ex);
            throw ex;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);  // Rétablir autocommit
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Erreur lors du rétablissement de l'autocommit", e);
                }
            }
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère les dépenses d'une mission.
     *
     * @param idMission ID de la mission
     * @return Liste des dépenses associées à la mission
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<DepenseMission> getDepenses(int idMission) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();
            return findDepensesByMissionId(idMission, conn);
        } finally {
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère les dépenses d'une mission à partir d'une connexion existante.
     *
     * @param idMission ID de la mission
     * @param conn Connexion à la base de données
     * @return Liste des dépenses associées à la mission
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    private List<DepenseMission> findDepensesByMissionId(int idMission, Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<DepenseMission> depenses = new ArrayList<>();

        try {
            String sql = "SELECT * FROM DEPENSE_MISSION WHERE id_mission = ? ORDER BY id";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idMission);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                depenses.add(extractDepenseMissionFromResultSet(rs));
            }

            return depenses;

        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Met à jour le coût total d'une mission en additionnant toutes ses dépenses.
     *
     * @param idMission ID de la mission
     * @param conn Connexion à la base de données
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    private void updateCoutTotalFromDepenses(int idMission, Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Calculer la somme des dépenses
            String sqlSum = "SELECT SUM(montant) AS total_depenses FROM DEPENSE_MISSION WHERE id_mission = ?";

            pstmt = conn.prepareStatement(sqlSum);
            pstmt.setInt(1, idMission);

            rs = pstmt.executeQuery();

            BigDecimal coutTotal = BigDecimal.ZERO;
            if (rs.next()) {
                BigDecimal totalDepenses = rs.getBigDecimal("total_depenses");
                if (totalDepenses != null) {
                    coutTotal = totalDepenses;
                }
            }

            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);

            // Mettre à jour le coût total de la mission
            String sqlUpdate = "UPDATE MISSION SET cout_total = ? WHERE id_mission = ?";

            pstmt = conn.prepareStatement(sqlUpdate);
            pstmt.setBigDecimal(1, coutTotal);
            pstmt.setInt(2, idMission);

            pstmt.executeUpdate();

        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Supprime toutes les dépenses associées à une mission.
     *
     * @param idMission ID de la mission
     * @param conn Connexion à la base de données
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    private void deleteAllDepensesByMissionId(int idMission, Connection conn) throws SQLException {
        PreparedStatement pstmt = null;

        try {
            String sql = "DELETE FROM DEPENSE_MISSION WHERE id_mission = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idMission);

            pstmt.executeUpdate();

        } finally {
            dbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Supprime une dépense de mission.
     * Met à jour également le coût total de la mission.
     *
     * @param idDepense ID de la dépense à supprimer
     * @return true si la suppression a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean deleteDepense(int idDepense) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();
            conn.setAutoCommit(false);  // Début transaction

            // Récupérer l'ID de la mission associée à la dépense
            String sqlSelect = "SELECT id_mission FROM DEPENSE_MISSION WHERE id = ?";
            pstmt = conn.prepareStatement(sqlSelect);
            pstmt.setInt(1, idDepense);

            rs = pstmt.executeQuery();

            Integer idMission = null;
            if (rs.next()) {
                idMission = rs.getInt("id_mission");
            } else {
                return false;  // Dépense non trouvée
            }

            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);

            // Supprimer la dépense
            String sqlDelete = "DELETE FROM DEPENSE_MISSION WHERE id = ?";
            pstmt = conn.prepareStatement(sqlDelete);
            pstmt.setInt(1, idDepense);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0 && idMission != null) {
                // Mettre à jour le coût total de la mission
                updateCoutTotalFromDepenses(idMission, conn);
            }

            conn.commit();  // Valider transaction
            return affectedRows > 0;

        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback();  // Annuler transaction en cas d'erreur
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction", e);
                }
            }
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de la dépense ID: " + idDepense, ex);
            throw ex;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);  // Rétablir autocommit
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Erreur lors du rétablissement de l'autocommit", e);
                }
            }
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Calcule les statistiques des missions.
     *
     * @param annee Année pour les statistiques (0 pour toutes les années)
     * @return Les statistiques des missions
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public MissionStats calculateStats(int annee) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ");
            sql.append("COUNT(*) AS total_missions, ");
            sql.append("SUM(CASE WHEN status = 'Planifiee' THEN 1 ELSE 0 END) AS planifiees, ");
            sql.append("SUM(CASE WHEN status = 'EnCours' THEN 1 ELSE 0 END) AS en_cours, ");
            sql.append("SUM(CASE WHEN status = 'Cloturee' THEN 1 ELSE 0 END) AS cloturees, ");
            sql.append("SUM(km_reel) AS km_total, ");
            sql.append("SUM(cout_total) AS cout_total ");
            sql.append("FROM MISSION ");

            if (annee > 0) {
                sql.append("WHERE YEAR(date_debut_mission) = ?");
            }

            pstmt = conn.prepareStatement(sql.toString());

            if (annee > 0) {
                pstmt.setInt(1, annee);
            }

            rs = pstmt.executeQuery();

            if (rs.next()) {
                int totalMissions = rs.getInt("total_missions");
                int planifiees = rs.getInt("planifiees");
                int enCours = rs.getInt("en_cours");
                int cloturees = rs.getInt("cloturees");
                long kmTotal = rs.getLong("km_total");
                BigDecimal coutTotal = rs.getBigDecimal("cout_total");

                coutTotal = coutTotal != null ? coutTotal : BigDecimal.ZERO;

                // Calculer les statistiques supplémentaires
                BigDecimal coutMoyen = BigDecimal.ZERO;
                if (cloturees > 0) {
                    coutMoyen = coutTotal.divide(BigDecimal.valueOf(cloturees), 2, RoundingMode.HALF_UP);
                }

                double kmMoyen = 0;
                if (cloturees > 0) {
                    kmMoyen = (double) kmTotal / cloturees;
                }

                Map<String, Integer> missionsBySite = getMissionCountBySite(conn, annee);

                MissionStats stats = new MissionStats(
                        totalMissions, planifiees, enCours, cloturees,
                        kmTotal, kmMoyen, coutTotal, coutMoyen, missionsBySite
                );

                return stats;
            }

            // Par défaut, retourner des stats vides
            return new MissionStats(
                    0, 0, 0, 0, 0, 0, BigDecimal.ZERO, BigDecimal.ZERO, new HashMap<>()
            );

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul des statistiques des missions", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Compte les missions par site.
     *
     * @param conn Connexion à la base de données
     * @param annee Année pour les statistiques (0 pour toutes les années)
     * @return Map contenant le nombre de missions par site
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    private Map<String, Integer> getMissionCountBySite(Connection conn, int annee) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Map<String, Integer> counts = new HashMap<>();

        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT site, COUNT(*) as count FROM MISSION ");

            if (annee > 0) {
                sql.append("WHERE YEAR(date_debut_mission) = ? ");
            }

            sql.append("GROUP BY site ORDER BY count DESC");

            pstmt = conn.prepareStatement(sql.toString());

            if (annee > 0) {
                pstmt.setInt(1, annee);
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                String site = rs.getString("site");
                int count = rs.getInt("count");
                counts.put(site, count);
            }

            return counts;

        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Vérifie la disponibilité d'un véhicule pour une période de mission.
     *
     * @param idVehicule ID du véhicule
     * @param debut Date de début de la période
     * @param fin Date de fin de la période
     * @param exclureMissionId ID d'une mission à exclure (utile pour les mises à jour)
     * @return true si le véhicule est disponible, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean isVehiculeDisponible(int idVehicule, LocalDateTime debut, LocalDateTime fin, Integer exclureMissionId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            // Vérifier si le véhicule est en entretien pendant la période
            String sqlEntretien = "SELECT 1 FROM ENTRETIEN " +
                    "WHERE id_vehicule = ? AND " +
                    "((date_entree_entr <= ? AND (date_sortie_entr >= ? OR date_sortie_entr IS NULL)) OR " +
                    "(date_entree_entr BETWEEN ? AND ?) OR " +
                    "(date_sortie_entr BETWEEN ? AND ?)) " +
                    "LIMIT 1";

            pstmt = conn.prepareStatement(sqlEntretien);
            pstmt.setInt(1, idVehicule);
            pstmt.setTimestamp(2, Timestamp.valueOf(fin));
            pstmt.setTimestamp(3, Timestamp.valueOf(debut));
            pstmt.setTimestamp(4, Timestamp.valueOf(debut));
            pstmt.setTimestamp(5, Timestamp.valueOf(fin));
            pstmt.setTimestamp(6, Timestamp.valueOf(debut));
            pstmt.setTimestamp(7, Timestamp.valueOf(fin));

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return false;  // Véhicule en entretien pendant la période
            }

            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);

            // Vérifier si le véhicule est en mission pendant la période
            StringBuilder sqlMission = new StringBuilder("SELECT 1 FROM MISSION " +
                    "WHERE id_vehicule = ? AND status != 'Cloturee' AND " +
                    "((date_debut_mission <= ? AND (date_fin_mission >= ? OR date_fin_mission IS NULL)) OR " +
                    "(date_debut_mission BETWEEN ? AND ?) OR " +
                    "(date_fin_mission BETWEEN ? AND ?))");

            if (exclureMissionId != null) {
                sqlMission.append(" AND id_mission != ?");
            }

            sqlMission.append(" LIMIT 1");

            pstmt = conn.prepareStatement(sqlMission.toString());
            pstmt.setInt(1, idVehicule);
            pstmt.setTimestamp(2, Timestamp.valueOf(fin));
            pstmt.setTimestamp(3, Timestamp.valueOf(debut));
            pstmt.setTimestamp(4, Timestamp.valueOf(debut));
            pstmt.setTimestamp(5, Timestamp.valueOf(fin));
            pstmt.setTimestamp(6, Timestamp.valueOf(debut));
            pstmt.setTimestamp(7, Timestamp.valueOf(fin));

            if (exclureMissionId != null) {
                pstmt.setInt(8, exclureMissionId);
            }

            rs = pstmt.executeQuery();

            return !rs.next();  // Véhicule disponible si aucune mission trouvée

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la vérification de la disponibilité du véhicule ID: " + idVehicule, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Extrait un objet Mission à partir d'un ResultSet.
     *
     * @param rs ResultSet contenant les données de la mission
     * @return Objet Mission créé à partir des données
     * @throws SQLException En cas d'erreur d'accès aux données du ResultSet
     */
    private Mission extractMissionFromResultSet(ResultSet rs) throws SQLException {
        Mission mission = new Mission();

        mission.setIdMission(rs.getInt("id_mission"));
        mission.setIdVehicule(rs.getInt("id_vehicule"));
        mission.setLibMission(rs.getString("lib_mission"));
        mission.setSite(rs.getString("site"));

        Timestamp dateDebut = rs.getTimestamp("date_debut_mission");
        if (dateDebut != null) {
            mission.setDateDebutMission(dateDebut.toLocalDateTime());
        }

        Timestamp dateFin = rs.getTimestamp("date_fin_mission");
        if (dateFin != null) {
            mission.setDateFinMission(dateFin.toLocalDateTime());
        }

        int kmPrevu = rs.getInt("km_prevu");
        if (!rs.wasNull()) {
            mission.setKmPrevu(kmPrevu);
        }

        int kmReel = rs.getInt("km_reel");
        if (!rs.wasNull()) {
            mission.setKmReel(kmReel);
        }

        String statusStr = rs.getString("status");
        mission.setStatus(Mission.StatusMission.valueOf(statusStr));

        mission.setCoutTotal(rs.getBigDecimal("cout_total"));
        mission.setCircuitMission(rs.getString("circuit_mission"));
        mission.setObservationMission(rs.getString("observation_mission"));

        // Informations véhicule (si disponibles)
        try {
            String immatriculation = rs.getString("immatriculation");
            String marque = rs.getString("marque");
            String modele = rs.getString("modele");

            if (immatriculation != null && marque != null && modele != null) {
                Vehicule vehicule = new Vehicule();
                vehicule.setIdVehicule(mission.getIdVehicule());
                vehicule.setImmatriculation(immatriculation);
                vehicule.setMarque(marque);
                vehicule.setModele(modele);

                mission.setVehicule(vehicule);
            }
        } catch (SQLException ex) {
            // Ignorer les colonnes non disponibles
        }

        return mission;
    }

    /**
     * Extrait un objet DepenseMission à partir d'un ResultSet.
     *
     * @param rs ResultSet contenant les données de la dépense
     * @return Objet DepenseMission créé à partir des données
     * @throws SQLException En cas d'erreur d'accès aux données du ResultSet
     */
    private DepenseMission extractDepenseMissionFromResultSet(ResultSet rs) throws SQLException {
        DepenseMission depense = new DepenseMission();

        depense.setId(rs.getInt("id"));
        depense.setIdMission(rs.getInt("id_mission"));

        String natureStr = rs.getString("nature");
        depense.setNature(DepenseMission.NatureDepense.valueOf(natureStr));

        depense.setMontant(rs.getBigDecimal("montant"));
        depense.setJustificatif(rs.getString("justificatif"));

        return depense;
    }

    /**
     * Classe interne représentant les statistiques des missions.
     */
    public static class MissionStats {
        private final int totalMissions;
        private final int planifiees;
        private final int enCours;
        private final int cloturees;
        private final long kmTotal;
        private final double kmMoyen;
        private final BigDecimal coutTotal;
        private final BigDecimal coutMoyen;
        private final Map<String, Integer> missionsBySite;

        /**
         * Constructeur pour les statistiques des missions.
         *
         * @param totalMissions Nombre total de missions
         * @param planifiees Nombre de missions planifiées
         * @param enCours Nombre de missions en cours
         * @param cloturees Nombre de missions clôturées
         * @param kmTotal Nombre total de kilomètres effectués
         * @param kmMoyen Nombre moyen de kilomètres par mission
         * @param coutTotal Coût total des missions
         * @param coutMoyen Coût moyen par mission
         * @param missionsBySite Répartition des missions par site
         */
        public MissionStats(int totalMissions, int planifiees, int enCours, int cloturees,
                            long kmTotal, double kmMoyen, BigDecimal coutTotal, BigDecimal coutMoyen,
                            Map<String, Integer> missionsBySite) {
            this.totalMissions = totalMissions;
            this.planifiees = planifiees;
            this.enCours = enCours;
            this.cloturees = cloturees;
            this.kmTotal = kmTotal;
            this.kmMoyen = kmMoyen;
            this.coutTotal = coutTotal;
            this.coutMoyen = coutMoyen;
            this.missionsBySite = missionsBySite;
        }

        // Getters

        /**
         * @return Nombre total de missions
         */
        public int getTotalMissions() {
            return totalMissions;
        }

        /**
         * @return Nombre de missions planifiées
         */
        public int getPlanifiees() {
            return planifiees;
        }

        /**
         * @return Nombre de missions en cours
         */
        public int getEnCours() {
            return enCours;
        }

        /**
         * @return Nombre de missions clôturées
         */
        public int getCloturees() {
            return cloturees;
        }

        /**
         * @return Nombre total de kilomètres effectués
         */
        public long getKmTotal() {
            return kmTotal;
        }

        /**
         * @return Nombre moyen de kilomètres par mission
         */
        public double getKmMoyen() {
            return kmMoyen;
        }

        /**
         * @return Coût total des missions
         */
        public BigDecimal getCoutTotal() {
            return coutTotal;
        }

        /**
         * @return Coût moyen par mission
         */
        public BigDecimal getCoutMoyen() {
            return coutMoyen;
        }

        /**
         * @return Répartition des missions par site
         */
        public Map<String, Integer> getMissionsBySite() {
            return missionsBySite;
        }

        /**
         * @return Taux d'achèvement des missions (pourcentage de missions clôturées)
         */
        public double getTauxAchevement() {
            if (totalMissions == 0) {
                return 0;
            }
            return ((double) cloturees / totalMissions) * 100;
        }

        /**
         * @return Coût moyen par kilomètre
         */
        public BigDecimal getCoutMoyenParKm() {
            if (kmTotal == 0) {
                return BigDecimal.ZERO;
            }
            return coutTotal.divide(BigDecimal.valueOf(kmTotal), 2, RoundingMode.HALF_UP);
        }
    }
}