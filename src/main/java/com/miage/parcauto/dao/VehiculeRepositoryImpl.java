package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.vehicule.EtatVoiture;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.math.BigDecimal;

/**
 * Implémentation concrète du repository pour la gestion des véhicules.
 * Cette classe encapsule la logique JDBC pour l'accès aux données des
 * véhicules.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class VehiculeRepositoryImpl implements VehiculeRepository {
    private static final Logger LOGGER = Logger.getLogger(VehiculeRepositoryImpl.class.getName());
    private final DbUtil dbUtil;

    /**
     * Constructeur par défaut.
     */
    public VehiculeRepositoryImpl() {
        this.dbUtil = DbUtil.getInstance();
    }

    /**
     * Constructeur avec injection de dépendance (pour tests).
     * 
     * @param dbUtil Instance de DbUtil à utiliser
     */
    public VehiculeRepositoryImpl(DbUtil dbUtil) {
        this.dbUtil = dbUtil;
    }

    @Override
    public Optional<Vehicule> findById(Integer id) {
        String sql = "SELECT * FROM VEHICULE WHERE ID_VEHICULE = ?";

        try (Connection conn = dbUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(buildVehiculeFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du véhicule par ID", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Vehicule> findAll() {
        String sql = "SELECT * FROM VEHICULE ORDER BY MARQUE, MODELE";
        List<Vehicule> vehicules = new ArrayList<>();

        try (Connection conn = dbUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                vehicules.add(buildVehiculeFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les véhicules", e);
        }

        return vehicules;
    }

    @Override
    public List<Vehicule> findAll(int page, int size) {
        String sql = "SELECT * FROM VEHICULE ORDER BY MARQUE, MODELE LIMIT ? OFFSET ?";
        List<Vehicule> vehicules = new ArrayList<>();

        try (Connection conn = dbUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, size);
            stmt.setInt(2, page * size);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vehicules.add(buildVehiculeFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération paginée des véhicules", e);
        }

        return vehicules;
    }

    @Override
    public Vehicule save(Vehicule vehicule) {
        String sql = "INSERT INTO VEHICULE (ETAT_VOITURE, ENERGIE, NUMERO_CHASSI, IMMATRICULATION, " +
                "MARQUE, MODELE, NB_PLACES, DATE_ACQUISITION, DATE_AMMORTISSEMENT, " +
                "DATE_MISE_EN_SERVICE, PUISSANCE, COULEUR, PRIX_VEHICULE, KM_ACTUELS) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Paramétrage de la requête
            setVehiculeParameters(stmt, vehicule);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("La création du véhicule a échoué, aucune ligne affectée.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    vehicule.setIdVehicule(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("La création du véhicule a échoué, aucun ID obtenu.");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'enregistrement du véhicule", e);
        }

        return vehicule;
    }

    @Override
    public Vehicule update(Vehicule vehicule) {
        String sql = "UPDATE VEHICULE SET ETAT_VOITURE = ?, ENERGIE = ?, NUMERO_CHASSI = ?, " +
                "IMMATRICULATION = ?, MARQUE = ?, MODELE = ?, NB_PLACES = ?, " +
                "DATE_ACQUISITION = ?, DATE_AMMORTISSEMENT = ?, DATE_MISE_EN_SERVICE = ?, " +
                "PUISSANCE = ?, COULEUR = ?, PRIX_VEHICULE = ?, KM_ACTUELS = ? " +
                "WHERE ID_VEHICULE = ?";

        try (Connection conn = dbUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Paramétrage de la requête
            setVehiculeParameters(stmt, vehicule);
            stmt.setInt(15, vehicule.getIdVehicule());

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du véhicule", e);
        }

        return vehicule;
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM VEHICULE WHERE ID_VEHICULE = ?";

        try (Connection conn = dbUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression du véhicule", e);
            return false;
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM VEHICULE";

        try (Connection conn = dbUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des véhicules", e);
        }

        return 0;
    }

    @Override
    public List<Vehicule> findByEtat(EtatVoiture etat) {
        String sql = "SELECT * FROM VEHICULE WHERE ETAT_VOITURE = ?";
        List<Vehicule> vehicules = new ArrayList<>();

        try (Connection conn = dbUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, etat.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vehicules.add(buildVehiculeFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des véhicules par état", e);
        }

        return vehicules;
    }

    @Override
    public List<Vehicule> findRequiringMaintenance(int kmThreshold) {
        String sql = "SELECT v.* FROM VEHICULE v " +
                "LEFT JOIN ENTRETIEN e ON v.ID_VEHICULE = e.ID_VEHICULE " +
                "GROUP BY v.ID_VEHICULE " +
                "HAVING MAX(e.DATE_ENTRETIEN) IS NULL OR " +
                "v.KM_ACTUELS - MAX(CASE WHEN e.KM_ENTRETIEN IS NULL THEN 0 ELSE e.KM_ENTRETIEN END) >= ?";

        List<Vehicule> vehicules = new ArrayList<>();

        try (Connection conn = dbUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, kmThreshold);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vehicules.add(buildVehiculeFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des véhicules nécessitant un entretien", e);
        }

        return vehicules;
    }

    /**
     * Construit un objet Vehicule à partir d'un ResultSet.
     * 
     * @param rs ResultSet contenant les données du véhicule
     * @return Objet Vehicule construit
     * @throws SQLException En cas d'erreur d'accès aux données
     */
    private Vehicule buildVehiculeFromResultSet(ResultSet rs) throws SQLException {
        Vehicule vehicule = new Vehicule();

        vehicule.setIdVehicule(rs.getInt("ID_VEHICULE"));
        vehicule.setEtatVoiture(EtatVoiture.valueOf(rs.getString("ETAT_VOITURE")));
        vehicule.setEnergie(Vehicule.TypeEnergie.fromString(rs.getString("ENERGIE")));
        vehicule.setNumeroChassi(rs.getString("NUMERO_CHASSI"));
        vehicule.setImmatriculation(rs.getString("IMMATRICULATION"));
        vehicule.setMarque(rs.getString("MARQUE"));
        vehicule.setModele(rs.getString("MODELE"));
        vehicule.setNbPlaces(rs.getInt("NB_PLACES"));

        Timestamp dateAcquisition = rs.getTimestamp("DATE_ACQUISITION");
        if (dateAcquisition != null) {
            vehicule.setDateAcquisition(dateAcquisition.toLocalDateTime());
        }

        Timestamp dateAmmortissement = rs.getTimestamp("DATE_AMMORTISSEMENT");
        if (dateAmmortissement != null) {
            vehicule.setDateAmmortissement(dateAmmortissement.toLocalDateTime());
        }

        Timestamp dateMiseEnService = rs.getTimestamp("DATE_MISE_EN_SERVICE");
        if (dateMiseEnService != null) {
            vehicule.setDateMiseEnService(dateMiseEnService.toLocalDateTime());
        }

        vehicule.setPuissance(rs.getInt("PUISSANCE"));
        vehicule.setCouleur(rs.getString("COULEUR"));
        vehicule.setPrixVehicule(rs.getBigDecimal("PRIX_VEHICULE"));
        vehicule.setKmActuels(rs.getInt("KM_ACTUELS"));

        return vehicule;
    }

    /**
     * Configure les paramètres d'une requête préparée à partir d'un objet Vehicule.
     * 
     * @param stmt     PreparedStatement à configurer
     * @param vehicule Objet Vehicule source des données
     * @throws SQLException En cas d'erreur de configuration
     */
    private void setVehiculeParameters(PreparedStatement stmt, Vehicule vehicule) throws SQLException {
        stmt.setString(1, vehicule.getEtatVoiture().name());
        stmt.setString(2, vehicule.getEnergie().name());
        stmt.setString(3, vehicule.getNumeroChassi());
        stmt.setString(4, vehicule.getImmatriculation());
        stmt.setString(5, vehicule.getMarque());
        stmt.setString(6, vehicule.getModele());
        stmt.setInt(7, vehicule.getNbPlaces());

        if (vehicule.getDateAcquisition() != null) {
            stmt.setTimestamp(8, Timestamp.valueOf(vehicule.getDateAcquisition()));
        } else {
            stmt.setNull(8, Types.TIMESTAMP);
        }

        if (vehicule.getDateAmmortissement() != null) {
            stmt.setTimestamp(9, Timestamp.valueOf(vehicule.getDateAmmortissement()));
        } else {
            stmt.setNull(9, Types.TIMESTAMP);
        }

        if (vehicule.getDateMiseEnService() != null) {
            stmt.setTimestamp(10, Timestamp.valueOf(vehicule.getDateMiseEnService()));
        } else {
            stmt.setNull(10, Types.TIMESTAMP);
        }

        stmt.setInt(11, vehicule.getPuissance());
        stmt.setString(12, vehicule.getCouleur());
        stmt.setBigDecimal(13, vehicule.getPrixVehicule());
        stmt.setInt(14, vehicule.getKmActuels());
    }
}
