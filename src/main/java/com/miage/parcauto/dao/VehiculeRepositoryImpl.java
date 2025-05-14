package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.vehicule.EtatVoiture;
import main.java.com.miage.parcauto.model.vehicule.TypeEnergie;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;

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
 * Implémentation du Repository pour l'entité {@link Vehicule}.
 * Gère les opérations CRUD et les requêtes spécifiques pour les véhicules
 * en interagissant avec la base de données via JDBC.
 */
public class VehiculeRepositoryImpl implements Repository<Vehicule, Integer> {

    private static final Logger LOGGER = Logger.getLogger(VehiculeRepositoryImpl.class.getName());

    private static final String SQL_SELECT_BASE = "SELECT v.id_vehicule, v.immatriculation, v.marque, v.modele, v.couleur, v.km_actuels, " +
            "v.date_acquisition, v.numero_chassi, v.nb_places, v.date_ammortissement, v.date_mise_en_service, " +
            "v.puissance, v.prix_vehicule, v.date_etat, " +
            "ev.id_etat_voiture as ev_id, ev.lib_etat_voiture as ev_lib, " +
            "te.id_type_energie as te_id, te.lib_type_energie as te_lib " +
            "FROM VEHICULES v " +
            "LEFT JOIN ETAT_VOITURE ev ON v.id_etat_voiture = ev.id_etat_voiture " +
            "LEFT JOIN TYPE_ENERGIE te ON v.id_type_energie = te.id_type_energie ";

    private static final String SQL_FIND_BY_ID = SQL_SELECT_BASE + "WHERE v.id_vehicule = ?";
    private static final String SQL_FIND_ALL = SQL_SELECT_BASE;
    private static final String SQL_FIND_ALL_PAGED = SQL_SELECT_BASE + "ORDER BY v.id_vehicule ASC LIMIT ? OFFSET ?";
    private static final String SQL_SAVE = "INSERT INTO VEHICULES (immatriculation, marque, modele, couleur, km_actuels, date_acquisition, " +
            "numero_chassi, id_etat_voiture, id_type_energie, nb_places, date_ammortissement, " +
            "date_mise_en_service, puissance, prix_vehicule, date_etat) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE VEHICULES SET immatriculation = ?, marque = ?, modele = ?, couleur = ?, km_actuels = ?, " +
            "date_acquisition = ?, numero_chassi = ?, id_etat_voiture = ?, id_type_energie = ?, " +
            "nb_places = ?, date_ammortissement = ?, date_mise_en_service = ?, puissance = ?, " +
            "prix_vehicule = ?, date_etat = ? WHERE id_vehicule = ?";
    private static final String SQL_DELETE = "DELETE FROM VEHICULES WHERE id_vehicule = ?";
    private static final String SQL_COUNT = "SELECT COUNT(*) FROM VEHICULES";
    private static final String SQL_FIND_BY_ETAT = SQL_SELECT_BASE + "WHERE v.id_etat_voiture = ?";
    private static final String SQL_FIND_REQUIRING_MAINTENANCE = SQL_SELECT_BASE + "WHERE v.km_actuels >= ?";


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Vehicule> findById(Integer id) {
        if (id == null) return Optional.empty();
        Vehicule vehicule = null;
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_BY_ID)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    vehicule = mapResultSetToVehicule(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de Vehicule par ID: " + id, e);
        }
        return Optional.ofNullable(vehicule);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Vehicule> findAll() {
        List<Vehicule> vehicules = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                vehicules.add(mapResultSetToVehicule(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les Vehicules", e);
        }
        return vehicules;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Vehicule> findAll(int page, int size) {
        if (page < 0 || size <= 0) {
            LOGGER.log(Level.WARNING, "Pagination invalide : page={0}, size={1}", new Object[]{page, size});
            return new ArrayList<>();
        }
        List<Vehicule> vehicules = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_ALL_PAGED)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, page * size);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    vehicules.add(mapResultSetToVehicule(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération paginée des Vehicules", e);
        }
        return vehicules;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vehicule save(Vehicule entity) {
        if (entity == null) return null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_SAVE, Statement.RETURN_GENERATED_KEYS);
            mapVehiculeToPreparedStatement(entity, pstmt, false);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("La création de Vehicule a échoué, aucune ligne affectée.");
            }
            generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                entity.setIdVehicule(generatedKeys.getInt(1));
            } else {
                conn.rollback();
                throw new SQLException("La création de Vehicule a échoué, aucun ID généré retourné.");
            }
            conn.commit();
            return entity;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la sauvegarde de Vehicule", e);
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
    public Vehicule update(Vehicule entity) {
        if (entity == null || entity.getIdVehicule() == null) return null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(SQL_UPDATE);
            mapVehiculeToPreparedStatement(entity, pstmt, true);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                conn.commit();
                return entity;
            } else {
                conn.rollback();
                LOGGER.log(Level.WARNING, "Aucune ligne mise à jour pour Vehicule ID: {0}", entity.getIdVehicule());
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de Vehicule ID: " + entity.getIdVehicule(), e);
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
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de Vehicule ID: " + id, e);
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
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des Vehicules", e);
        }
        return count;
    }

    /**
     * Recherche les véhicules par leur état.
     * @param etat L'état du véhicule à rechercher.
     * @return Une liste de véhicules correspondant à l'état spécifié.
     */
    public List<Vehicule> findByEtat(EtatVoiture etat) {
        if (etat == null || etat.getIdEtatVoiture() == null) return new ArrayList<>();
        List<Vehicule> vehicules = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_BY_ETAT)) {
            pstmt.setInt(1, etat.getIdEtatVoiture());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    vehicules.add(mapResultSetToVehicule(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de Vehicules par état: " + etat.getLibEtatVoiture(), e);
        }
        return vehicules;
    }

    /**
     * Recherche les véhicules nécessitant une maintenance.
     * @param kmThreshold Le seuil de kilométrage.
     * @return Une liste de véhicules nécessitant une maintenance.
     */
    public List<Vehicule> findRequiringMaintenance(int kmThreshold) {
        List<Vehicule> vehicules = new ArrayList<>();
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_FIND_REQUIRING_MAINTENANCE)) {
            pstmt.setInt(1, kmThreshold);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    vehicules.add(mapResultSetToVehicule(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de Vehicules nécessitant une maintenance (seuil km: " + kmThreshold + ")", e);
        }
        return vehicules;
    }

    private Vehicule mapResultSetToVehicule(ResultSet rs) throws SQLException {
        Vehicule vehicule = new Vehicule();
        vehicule.setIdVehicule(rs.getInt("id_vehicule"));
        vehicule.setImmatriculation(rs.getString("immatriculation"));
        vehicule.setMarque(rs.getString("marque"));
        vehicule.setModele(rs.getString("modele"));
        vehicule.setCouleur(rs.getString("couleur"));
        vehicule.setKmActuels(rs.getInt("km_actuels"));

        Timestamp dateAcquisitionTs = rs.getTimestamp("date_acquisition");
        if (dateAcquisitionTs != null) {
            vehicule.setDateAcquisition(dateAcquisitionTs.toLocalDateTime());
        }
        vehicule.setNumeroChassi(rs.getString("numero_chassi"));

        int idEtatVoiture = rs.getInt("ev_id");
        if (!rs.wasNull()) {
            EtatVoiture etat = new EtatVoiture();
            etat.setIdEtatVoiture(idEtatVoiture);
            etat.setLibEtatVoiture(rs.getString("ev_lib"));
            vehicule.setEtatVoiture(etat);
        }

        int idTypeEnergieDb = rs.getInt("te_id");
        if (!rs.wasNull()) {
            TypeEnergie typeEnergieMapped = null;
            for(TypeEnergie te : TypeEnergie.values()){
                if(te.getId() == idTypeEnergieDb){ // Assumes TypeEnergie has getId()
                    typeEnergieMapped = te;
                    break;
                }
            }
            if (typeEnergieMapped == null) {
                LOGGER.log(Level.WARNING, "TypeEnergie non trouvé en base pour ID: " + idTypeEnergieDb + " pour véhicule ID: " + vehicule.getIdVehicule());
            }
            vehicule.setEnergie(typeEnergieMapped); // Uses setEnergie
        }

        vehicule.setNbPlaces(rs.getInt("nb_places"));
        Timestamp dateAmortissementTs = rs.getTimestamp("date_ammortissement");
        if (dateAmortissementTs != null) {
            vehicule.setDateAmortissement(dateAmortissementTs.toLocalDateTime());
        }
        Timestamp dateMiseEnServiceTs = rs.getTimestamp("date_mise_en_service");
        if (dateMiseEnServiceTs != null) {
            vehicule.setDateMiseEnService(dateMiseEnServiceTs.toLocalDateTime());
        }
        vehicule.setPuissance(rs.getInt("puissance"));
        vehicule.setPrixVehicule(rs.getBigDecimal("prix_vehicule"));
        Timestamp dateEtatTs = rs.getTimestamp("date_etat");
        if (dateEtatTs != null) {
            vehicule.setDateEtat(dateEtatTs.toLocalDateTime());
        }

        return vehicule;
    }

    private void mapVehiculeToPreparedStatement(Vehicule entity, PreparedStatement pstmt, boolean isUpdate) throws SQLException {
        int paramIndex = 1;
        pstmt.setString(paramIndex++, entity.getImmatriculation());
        pstmt.setString(paramIndex++, entity.getMarque());
        pstmt.setString(paramIndex++, entity.getModele());
        pstmt.setString(paramIndex++, entity.getCouleur());
        pstmt.setInt(paramIndex++, entity.getKmActuels());

        if (entity.getDateAcquisition() != null) { // Uses getDateAcquisition
            pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(entity.getDateAcquisition()));
        } else {
            pstmt.setNull(paramIndex++, Types.TIMESTAMP);
        }
        pstmt.setString(paramIndex++, entity.getNumeroChassi()); // Uses getNumeroChassi

        if (entity.getEtatVoiture() != null && entity.getEtatVoiture().getIdEtatVoiture() != null) {
            pstmt.setInt(paramIndex++, entity.getEtatVoiture().getIdEtatVoiture());
        } else {
            pstmt.setNull(paramIndex++, Types.INTEGER);
        }
        if (entity.getEnergie() != null) { // Uses getEnergie
            pstmt.setInt(paramIndex++, entity.getEnergie().getId()); // Assumes TypeEnergie has getId()
        } else {
            pstmt.setNull(paramIndex++, Types.INTEGER);
        }

        pstmt.setObject(paramIndex++, entity.getNbPlaces(), Types.INTEGER); // Use setObject for potential null Integers

        if (entity.getDateAmortissement() != null) {
            pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(entity.getDateAmortissement()));
        } else {
            pstmt.setNull(paramIndex++, Types.TIMESTAMP);
        }
        if (entity.getDateMiseEnService() != null) {
            pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(entity.getDateMiseEnService()));
        } else {
            pstmt.setNull(paramIndex++, Types.TIMESTAMP);
        }
        pstmt.setObject(paramIndex++, entity.getPuissance(), Types.INTEGER);
        pstmt.setBigDecimal(paramIndex++, entity.getPrixVehicule());

        if (entity.getDateEtat() != null) {
            pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(entity.getDateEtat()));
        } else {
            pstmt.setNull(paramIndex++, Types.TIMESTAMP);
        }

        if (isUpdate) {
            pstmt.setInt(paramIndex, entity.getIdVehicule());
        }
    }

    private void dbUtilRollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction Vehicule.", ex);
            }
        }
    }
}