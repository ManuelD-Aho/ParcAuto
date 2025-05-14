package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.mission.Mission;
import main.java.com.miage.parcauto.model.mission.Mission.StatutMission;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implémentation concrète du repository pour la gestion des missions.
 * Cette classe encapsule la logique JDBC pour l'accès aux données des missions.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class MissionRepositoryImpl implements MissionRepository {

    private static final Logger LOGGER = Logger.getLogger(MissionRepositoryImpl.class.getName());

    private final DbUtil dbUtil;
    private final VehiculeRepository vehiculeRepository;

    /**
     * Constructeur par défaut.
     */
    public MissionRepositoryImpl() {
        this.dbUtil = DbUtil.getInstance();
        this.vehiculeRepository = new VehiculeRepositoryImpl();
    }

    /**
     * Constructeur avec injection de dépendance (pour tests).
     *
     * @param dbUtil             Instance de DbUtil à utiliser
     * @param vehiculeRepository Instance de VehiculeRepository à utiliser
     */
    public MissionRepositoryImpl(DbUtil dbUtil, VehiculeRepository vehiculeRepository) {
        this.dbUtil = dbUtil;
        this.vehiculeRepository = vehiculeRepository;
    }

    @Override
    public Optional<Mission> findById(Integer id) {
        String sql = "SELECT m.*, v.MARQUE, v.MODELE, v.IMMATRICULATION, " +
                "s.NOM as NOM_SOCIETAIRE, s.PRENOM as PRENOM_SOCIETAIRE " +
                "FROM MISSION m " +
                "LEFT JOIN VEHICULE v ON m.ID_VEHICULE = v.ID_VEHICULE " +
                "LEFT JOIN SOCIETAIRE s ON m.ID_SOCIETAIRE = s.ID_SOCIETAIRE " +
                "WHERE m.ID_MISSION = ?";

        try (Connection conn = dbUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(buildMissionFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de la mission par ID", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Mission> findAll() {
        String sql = "SELECT m.*, v.MARQUE, v.MODELE, v.IMMATRICULATION, " +
                "s.NOM as NOM_SOCIETAIRE, s.PRENOM as PRENOM_SOCIETAIRE " +
                "FROM MISSION m " +
                "LEFT JOIN VEHICULE v ON m.ID_VEHICULE = v.ID_VEHICULE " +
                "LEFT JOIN SOCIETAIRE s ON m.ID_SOCIETAIRE = s.ID_SOCIETAIRE " +
                "ORDER BY m.DATE_DEPART DESC";

        List<Mission> missions = new ArrayList<>();

        try (Connection conn = dbUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                missions.add(buildMissionFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de toutes les missions", e);
        }

        return missions;
    }

    @Override
    public List<Mission> findAll(int page, int size) {
        String sql = "SELECT m.*, v.MARQUE, v.MODELE, v.IMMATRICULATION, " +
                "s.NOM as NOM_SOCIETAIRE, s.PRENOM as PRENOM_SOCIETAIRE " +
                "FROM MISSION m " +
                "LEFT JOIN VEHICULE v ON m.ID_VEHICULE = v.ID_VEHICULE " +
                "LEFT JOIN SOCIETAIRE s ON m.ID_SOCIETAIRE = s.ID_SOCIETAIRE " +
                "ORDER BY m.DATE_DEPART DESC " +
                "LIMIT ? OFFSET ?";

        List<Mission> missions = new ArrayList<>();

        try (Connection conn = dbUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, size);
            stmt.setInt(2, page * size);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    missions.add(buildMissionFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération paginée des missions", e);
        }

        return missions;
    }

    @Override
    public Mission save(Mission mission) {
        String sql = "INSERT INTO MISSION (ID_VEHICULE, ID_SOCIETAIRE, DESTINATION, MOTIF, " +
                "DATE_DEPART, DATE_RETOUR_PREVUE, STATUT, KM_DEPART, KM_RETOUR, DATE_RETOUR_REELLE) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setMissionParameters(stmt, mission);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("La création de la mission a échoué, aucune ligne affectée.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    mission.setIdMission(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("La création de la mission a échoué, aucun ID obtenu.");
                }
            }

            // Mise à jour de l'état du véhicule si nécessaire
            if (mission.getVehicule() != null && mission.getVehicule().getIdVehicule() != null) {
                updateVehiculeStatut(mission.getVehicule().getIdVehicule(), true);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'enregistrement de la mission", e);
        }

        return mission;
    }

    @Override
    public Mission update(Mission mission) {
        String sql = "UPDATE MISSION SET ID_VEHICULE = ?, ID_SOCIETAIRE = ?, DESTINATION = ?, " +
                "MOTIF = ?, DATE_DEPART = ?, DATE_RETOUR_PREVUE = ?, STATUT = ?, " +
                "KM_DEPART = ?, KM_RETOUR = ?, DATE_RETOUR_REELLE = ? " +
                "WHERE ID_MISSION = ?";

        try (Connection conn = dbUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            setMissionParameters(stmt, mission);
            stmt.setInt(11, mission.getIdMission());

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de la mission", e);
        }

        return mission;
    }

    @Override
    public boolean delete(Integer id) {
        // Vérifier si la mission existe et n'est pas déjà terminée
        Optional<Mission> mission = findById(id);
        if (mission.isPresent() && mission.get().getStatut() != StatutMission.Terminee) {
            // Si la mission est en cours, libérer le véhicule
            if (mission.get().getVehicule() != null && mission.get().getVehicule().getIdVehicule() != null) {
                updateVehiculeStatut(mission.get().getVehicule().getIdVehicule(), false);
            }
        }

        String sql = "DELETE FROM MISSION WHERE ID_MISSION = ?";

        try (Connection conn = dbUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de la mission", e);
            return false;
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM MISSION";

        try (Connection conn = dbUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des missions", e);
        }

        return 0;
    }

    @Override
    public List<Mission> findActiveForVehicule(int idVehicule) {
        String sql = "SELECT m.*, v.MARQUE, v.MODELE, v.IMMATRICULATION, " +
                "s.NOM as NOM_SOCIETAIRE, s.PRENOM as PRENOM_SOCIETAIRE " +
                "FROM MISSION m " +
                "LEFT JOIN VEHICULE v ON m.ID_VEHICULE = v.ID_VEHICULE " +
                "LEFT JOIN SOCIETAIRE s ON m.ID_SOCIETAIRE = s.ID_SOCIETAIRE " +
                "WHERE m.ID_VEHICULE = ? AND m.STATUT = 'EnCours'";

        List<Mission> missions = new ArrayList<>();

        try (Connection conn = dbUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVehicule);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    missions.add(buildMissionFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des missions actives pour le véhicule", e);
        }

        return missions;
    }

    @Override
    public List<Mission> findByPeriod(LocalDate debut, LocalDate fin) {
        String sql = "SELECT m.*, v.MARQUE, v.MODELE, v.IMMATRICULATION, " +
                "s.NOM as NOM_SOCIETAIRE, s.PRENOM as PRENOM_SOCIETAIRE " +
                "FROM MISSION m " +
                "LEFT JOIN VEHICULE v ON m.ID_VEHICULE = v.ID_VEHICULE " +
                "LEFT JOIN SOCIETAIRE s ON m.ID_SOCIETAIRE = s.ID_SOCIETAIRE " +
                "WHERE (DATE(m.DATE_DEPART) BETWEEN ? AND ?) " +
                "   OR (DATE(m.DATE_RETOUR_PREVUE) BETWEEN ? AND ?) " +
                "   OR (DATE(m.DATE_DEPART) <= ? AND (DATE(m.DATE_RETOUR_PREVUE) >= ? OR DATE(m.DATE_RETOUR_REELLE) >= ?))";

        List<Mission> missions = new ArrayList<>();

        try (Connection conn = dbUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(debut));
            stmt.setDate(2, java.sql.Date.valueOf(fin));
            stmt.setDate(3, java.sql.Date.valueOf(debut));
            stmt.setDate(4, java.sql.Date.valueOf(fin));
            stmt.setDate(5, java.sql.Date.valueOf(debut));
            stmt.setDate(6, java.sql.Date.valueOf(fin));
            stmt.setDate(7, java.sql.Date.valueOf(fin));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    missions.add(buildMissionFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des missions par période", e);
        }

        return missions;
    }

    /**
     * Construit un objet Mission à partir d'un ResultSet.
     * 
     * @param rs ResultSet contenant les données de la mission
     * @return Objet Mission construit
     * @throws SQLException En cas d'erreur de lecture des données
     */
    private Mission buildMissionFromResultSet(ResultSet rs) throws SQLException {
        Mission mission = new Mission();
        mission.setIdMission(rs.getInt("ID_MISSION"));

        // Construire l'objet Vehicule avec les informations minimales
        Vehicule vehicule = new Vehicule();
        vehicule.setIdVehicule(rs.getInt("ID_VEHICULE"));
        vehicule.setMarque(rs.getString("MARQUE"));
        vehicule.setModele(rs.getString("MODELE"));
        vehicule.setImmatriculation(rs.getString("IMMATRICULATION"));
        mission.setVehicule(vehicule);

        mission.setIdSocietaire(rs.getInt("ID_SOCIETAIRE"));
        mission.setNomPrenomSocietaire(rs.getString("NOM_SOCIETAIRE") + " " + rs.getString("PRENOM_SOCIETAIRE"));
        mission.setDestination(rs.getString("DESTINATION"));
        mission.setMotif(rs.getString("MOTIF"));

        Timestamp dateDepart = rs.getTimestamp("DATE_DEPART");
        if (dateDepart != null) {
            mission.setDateDepart(dateDepart.toLocalDateTime());
        }

        Timestamp dateRetourPrevue = rs.getTimestamp("DATE_RETOUR_PREVUE");
        if (dateRetourPrevue != null) {
            mission.setDateRetourPrevue(dateRetourPrevue.toLocalDateTime());
        }

        String statutStr = rs.getString("STATUT");
        if (statutStr != null) {
            mission.setStatut(StatutMission.valueOf(statutStr));
        }

        mission.setKmDepart(rs.getInt("KM_DEPART"));
        mission.setKmRetour(rs.getInt("KM_RETOUR"));

        Timestamp dateRetourReelle = rs.getTimestamp("DATE_RETOUR_REELLE");
        if (dateRetourReelle != null) {
            mission.setDateRetourReelle(dateRetourReelle.toLocalDateTime());
        }

        return mission;
    }

    /**
     * Configure les paramètres d'une requête préparée à partir d'un objet Mission.
     * 
     * @param stmt    PreparedStatement à configurer
     * @param mission Objet Mission source des données
     * @throws SQLException En cas d'erreur de configuration
     */
    private void setMissionParameters(PreparedStatement stmt, Mission mission) throws SQLException {
        if (mission.getVehicule() != null) {
            stmt.setInt(1, mission.getVehicule().getIdVehicule());
        } else {
            stmt.setNull(1, Types.INTEGER);
        }

        stmt.setInt(2, mission.getIdSocietaire());
        stmt.setString(3, mission.getDestination());
        stmt.setString(4, mission.getMotif());

        if (mission.getDateDepart() != null) {
            stmt.setTimestamp(5, Timestamp.valueOf(mission.getDateDepart()));
        } else {
            stmt.setNull(5, Types.TIMESTAMP);
        }

        if (mission.getDateRetourPrevue() != null) {
            stmt.setTimestamp(6, Timestamp.valueOf(mission.getDateRetourPrevue()));
        } else {
            stmt.setNull(6, Types.TIMESTAMP);
        }

        if (mission.getStatut() != null) {
            stmt.setString(7, mission.getStatut().name());
        } else {
            stmt.setString(7, StatutMission.Planifiee.name());
        }

        stmt.setInt(8, mission.getKmDepart());

        if (mission.getKmRetour() > 0) {
            stmt.setInt(9, mission.getKmRetour());
        } else {
            stmt.setNull(9, Types.INTEGER);
        }

        if (mission.getDateRetourReelle() != null) {
            stmt.setTimestamp(10, Timestamp.valueOf(mission.getDateRetourReelle()));
        } else {
            stmt.setNull(10, Types.TIMESTAMP);
        }
    }

    /**
     * Met à jour l'état d'un véhicule en mission ou disponible.
     * 
     * @param idVehicule ID du véhicule
     * @param enMission  true si en mission, false si disponible
     */
    private void updateVehiculeStatut(int idVehicule, boolean enMission) {
        vehiculeRepository.findById(idVehicule).ifPresent(vehicule -> {
            // Si on met le véhicule en mission et qu'il n'est pas déjà en mission
            if (enMission && !isVehiculeEnMission(idVehicule)) {
                // Rien à faire, le statut est géré via la table MISSION
            }
            // Si on libère le véhicule et qu'il n'est plus en mission
            else if (!enMission && !isVehiculeEnMission(idVehicule)) {
                // Rien à faire, le statut est géré via la table MISSION
            }
        });
    }

    @Override
    public boolean isVehiculeEnMission(int idVehicule) {
        String sql = "SELECT COUNT(*) FROM MISSION WHERE ID_VEHICULE = ? AND STATUT = 'EnCours'";

        try (Connection conn = dbUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idVehicule);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la vérification si le véhicule est en mission", e);
        }

        return false;
    }

    @Override
    public boolean terminerMission(int idMission, int kmRetour) {
        String sql = "UPDATE MISSION SET STATUT = ?, KM_RETOUR = ?, DATE_RETOUR_REELLE = ? WHERE ID_MISSION = ?";

        try (Connection conn = dbUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, StatutMission.Terminee.name());
            stmt.setInt(2, kmRetour);
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(4, idMission);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                // Mettre à jour le kilométrage du véhicule
                Optional<Mission> missionOpt = findById(idMission);
                if (missionOpt.isPresent() && missionOpt.get().getVehicule() != null) {
                    int idVehicule = missionOpt.get().getVehicule().getIdVehicule();

                    // Mettre à jour le kilométrage du véhicule
                    vehiculeRepository.findById(idVehicule).ifPresent(vehicule -> {
                        vehicule.setKmActuels(kmRetour);
                        vehiculeRepository.update(vehicule);
                    });
                }

                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la terminaison de la mission", e);
        }

        return false;
    }
}
