package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.vehicule.EtatVoiture;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;

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
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe d'accès aux données pour les véhicules.
 * Gère les opérations CRUD et les requêtes spécifiques liées aux véhicules.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class VehiculeDao {

    private static final Logger LOGGER = Logger.getLogger(VehiculeDao.class.getName());

    // Instance de DbUtil pour la gestion des connexions
    private final DbUtil dbUtil;

    /**
     * Constructeur par défaut. Initialise l'instance de DbUtil.
     */
    public VehiculeDao() {
        this.dbUtil = DbUtil.getInstance();
    }

    /**
     * Constructeur avec injection de dépendance pour les tests.
     *
     * @param dbUtil Instance de DbUtil à utiliser
     */
    public VehiculeDao(DbUtil dbUtil) {
        this.dbUtil = dbUtil;
    }

    /**
     * Récupère tous les véhicules de la base de données.
     *
     * @return Liste des véhicules
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Vehicule> findAll() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Vehicule> vehicules = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT v.*, e.lib_etat_voiture FROM VEHICULES v " +
                    "JOIN ETAT_VOITURE e ON v.id_etat_voiture = e.id_etat_voiture " +
                    "ORDER BY v.immatriculation";

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                vehicules.add(extractVehiculeFromResultSet(rs));
            }

            return vehicules;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les véhicules", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère un véhicule par son ID.
     *
     * @param id ID du véhicule à récupérer
     * @return Optional contenant le véhicule s'il existe, vide sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Optional<Vehicule> findById(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT v.*, e.lib_etat_voiture FROM VEHICULES v " +
                    "JOIN ETAT_VOITURE e ON v.id_etat_voiture = e.id_etat_voiture " +
                    "WHERE v.id_vehicule = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(extractVehiculeFromResultSet(rs));
            }

            return Optional.empty();

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du véhicule par ID: " + id, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère un véhicule par son immatriculation.
     *
     * @param immatriculation Immatriculation du véhicule à récupérer
     * @return Optional contenant le véhicule s'il existe, vide sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Optional<Vehicule> findByImmatriculation(String immatriculation) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT v.*, e.lib_etat_voiture FROM VEHICULES v " +
                    "JOIN ETAT_VOITURE e ON v.id_etat_voiture = e.id_etat_voiture " +
                    "WHERE v.immatriculation = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, immatriculation);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(extractVehiculeFromResultSet(rs));
            }

            return Optional.empty();

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du véhicule par immatriculation: " + immatriculation, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère un véhicule par son numéro de châssis.
     *
     * @param numeroChassi Numéro de châssis du véhicule à récupérer
     * @return Optional contenant le véhicule s'il existe, vide sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Optional<Vehicule> findByNumeroChassi(String numeroChassi) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT v.*, e.lib_etat_voiture FROM VEHICULES v " +
                    "JOIN ETAT_VOITURE e ON v.id_etat_voiture = e.id_etat_voiture " +
                    "WHERE v.numero_chassi = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, numeroChassi);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(extractVehiculeFromResultSet(rs));
            }

            return Optional.empty();

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du véhicule par numéro de châssis: " + numeroChassi, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère les véhicules selon leur état.
     *
     * @param idEtat ID de l'état à rechercher
     * @return Liste des véhicules dans l'état spécifié
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Vehicule> findByEtat(int idEtat) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Vehicule> vehicules = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT v.*, e.lib_etat_voiture FROM VEHICULES v " +
                    "JOIN ETAT_VOITURE e ON v.id_etat_voiture = e.id_etat_voiture " +
                    "WHERE v.id_etat_voiture = ? " +
                    "ORDER BY v.immatriculation";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idEtat);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                vehicules.add(extractVehiculeFromResultSet(rs));
            }

            return vehicules;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des véhicules par état: " + idEtat, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère les véhicules disponibles pour une mission.
     *
     * @return Liste des véhicules disponibles
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Vehicule> findDisponibles() throws SQLException {
        // Les véhicules disponibles sont dans l'état DISPONIBLE (id=1)
        return findByEtat(EtatVoiture.DISPONIBLE);
    }

    /**
     * Recherche des véhicules par critères (marque, modèle, etc.).
     *
     * @param searchTerm Terme de recherche
     * @return Liste des véhicules correspondant aux critères
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Vehicule> search(String searchTerm) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Vehicule> vehicules = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            // Préparer le terme de recherche pour LIKE
            String searchPattern = "%" + searchTerm + "%";

            String sql = "SELECT v.*, e.lib_etat_voiture FROM VEHICULES v " +
                    "JOIN ETAT_VOITURE e ON v.id_etat_voiture = e.id_etat_voiture " +
                    "WHERE v.immatriculation LIKE ? " +
                    "OR v.numero_chassi LIKE ? " +
                    "OR v.marque LIKE ? " +
                    "OR v.modele LIKE ? " +
                    "ORDER BY v.immatriculation";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                vehicules.add(extractVehiculeFromResultSet(rs));
            }

            return vehicules;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des véhicules avec le terme: " + searchTerm, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Crée un nouveau véhicule dans la base de données.
     *
     * @param vehicule Le véhicule à créer
     * @return Le véhicule créé avec son ID généré
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Vehicule create(Vehicule vehicule) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();
            conn.setAutoCommit(false);  // Début transaction

            String sql = "INSERT INTO VEHICULES (id_etat_voiture, energie, numero_chassi, immatriculation, " +
                    "marque, modele, nb_places, date_acquisition, date_ammortissement, date_mise_en_service, " +
                    "puissance, couleur, prix_vehicule, km_actuels, date_etat) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Paramètres obligatoires
            pstmt.setInt(1, vehicule.getEtatVoiture().getIdEtatVoiture());
            pstmt.setString(2, vehicule.getEnergie().name());
            pstmt.setString(3, vehicule.getNumeroChassi());
            pstmt.setString(4, vehicule.getImmatriculation());
            pstmt.setString(5, vehicule.getMarque());
            pstmt.setString(6, vehicule.getModele());

            // Paramètres optionnels
            if (vehicule.getNbPlaces() != null) {
                pstmt.setInt(7, vehicule.getNbPlaces());
            } else {
                pstmt.setNull(7, Types.INTEGER);
            }

            if (vehicule.getDateAcquisition() != null) {
                pstmt.setTimestamp(8, Timestamp.valueOf(vehicule.getDateAcquisition()));
            } else {
                pstmt.setNull(8, Types.TIMESTAMP);
            }

            if (vehicule.getDateAmmortissement() != null) {
                pstmt.setTimestamp(9, Timestamp.valueOf(vehicule.getDateAmmortissement()));
            } else {
                pstmt.setNull(9, Types.TIMESTAMP);
            }

            if (vehicule.getDateMiseEnService() != null) {
                pstmt.setTimestamp(10, Timestamp.valueOf(vehicule.getDateMiseEnService()));
            } else {
                pstmt.setNull(10, Types.TIMESTAMP);
            }

            if (vehicule.getPuissance() != null) {
                pstmt.setInt(11, vehicule.getPuissance());
            } else {
                pstmt.setNull(11, Types.INTEGER);
            }

            pstmt.setString(12, vehicule.getCouleur());

            if (vehicule.getPrixVehicule() != null) {
                pstmt.setBigDecimal(13, vehicule.getPrixVehicule());
            } else {
                pstmt.setNull(13, Types.DECIMAL);
            }

            pstmt.setInt(14, vehicule.getKmActuels() != null ? vehicule.getKmActuels() : 0);

            if (vehicule.getDateEtat() != null) {
                pstmt.setTimestamp(15, Timestamp.valueOf(vehicule.getDateEtat()));
            } else {
                pstmt.setTimestamp(15, Timestamp.valueOf(LocalDateTime.now()));
            }

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La création du véhicule a échoué, aucune ligne affectée.");
            }

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                vehicule.setIdVehicule(rs.getInt(1));
            } else {
                throw new SQLException("La création du véhicule a échoué, aucun ID généré.");
            }

            conn.commit();  // Valider transaction
            return vehicule;

        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback();  // Annuler transaction en cas d'erreur
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction", e);
                }
            }
            LOGGER.log(Level.SEVERE, "Erreur lors de la création du véhicule", ex);
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
     * Met à jour un véhicule dans la base de données.
     *
     * @param vehicule Le véhicule à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean update(Vehicule vehicule) throws SQLException {
        if (vehicule.getIdVehicule() == null) {
            throw new IllegalArgumentException("L'ID du véhicule ne peut pas être null pour une mise à jour");
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbUtil.getConnection();
            conn.setAutoCommit(false);  // Début transaction

            String sql = "UPDATE VEHICULES SET id_etat_voiture = ?, energie = ?, numero_chassi = ?, " +
                    "immatriculation = ?, marque = ?, modele = ?, nb_places = ?, date_acquisition = ?, " +
                    "date_ammortissement = ?, date_mise_en_service = ?, puissance = ?, couleur = ?, " +
                    "prix_vehicule = ?, km_actuels = ?, date_etat = ? " +
                    "WHERE id_vehicule = ?";

            pstmt = conn.prepareStatement(sql);

            // Paramètres obligatoires
            pstmt.setInt(1, vehicule.getEtatVoiture().getIdEtatVoiture());
            pstmt.setString(2, vehicule.getEnergie().name());
            pstmt.setString(3, vehicule.getNumeroChassi());
            pstmt.setString(4, vehicule.getImmatriculation());
            pstmt.setString(5, vehicule.getMarque());
            pstmt.setString(6, vehicule.getModele());

            // Paramètres optionnels
            if (vehicule.getNbPlaces() != null) {
                pstmt.setInt(7, vehicule.getNbPlaces());
            } else {
                pstmt.setNull(7, Types.INTEGER);
            }

            if (vehicule.getDateAcquisition() != null) {
                pstmt.setTimestamp(8, Timestamp.valueOf(vehicule.getDateAcquisition()));
            } else {
                pstmt.setNull(8, Types.TIMESTAMP);
            }

            if (vehicule.getDateAmmortissement() != null) {
                pstmt.setTimestamp(9, Timestamp.valueOf(vehicule.getDateAmmortissement()));
            } else {
                pstmt.setNull(9, Types.TIMESTAMP);
            }

            if (vehicule.getDateMiseEnService() != null) {
                pstmt.setTimestamp(10, Timestamp.valueOf(vehicule.getDateMiseEnService()));
            } else {
                pstmt.setNull(10, Types.TIMESTAMP);
            }

            if (vehicule.getPuissance() != null) {
                pstmt.setInt(11, vehicule.getPuissance());
            } else {
                pstmt.setNull(11, Types.INTEGER);
            }

            pstmt.setString(12, vehicule.getCouleur());

            if (vehicule.getPrixVehicule() != null) {
                pstmt.setBigDecimal(13, vehicule.getPrixVehicule());
            } else {
                pstmt.setNull(13, Types.DECIMAL);
            }

            pstmt.setInt(14, vehicule.getKmActuels() != null ? vehicule.getKmActuels() : 0);

            if (vehicule.getDateEtat() != null) {
                pstmt.setTimestamp(15, Timestamp.valueOf(vehicule.getDateEtat()));
            } else {
                pstmt.setTimestamp(15, Timestamp.valueOf(LocalDateTime.now()));
            }

            // ID du véhicule à mettre à jour
            pstmt.setInt(16, vehicule.getIdVehicule());

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
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du véhicule ID: " + vehicule.getIdVehicule(), ex);
            throw ex;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);  // Rétablir autocommit
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Erreur lors du rétablissement de l'autocommit", e);
                }
            }
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Change l'état d'un véhicule.
     *
     * @param idVehicule ID du véhicule
     * @param idEtatVoiture ID du nouvel état
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean updateEtat(int idVehicule, int idEtatVoiture) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "UPDATE VEHICULES SET id_etat_voiture = ?, date_etat = ? WHERE id_vehicule = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idEtatVoiture);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(3, idVehicule);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de l'état du véhicule ID: " + idVehicule, ex);
            throw ex;
        } finally {
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Met à jour le kilométrage d'un véhicule.
     *
     * @param idVehicule ID du véhicule
     * @param kmActuels Nouveau kilométrage
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean updateKilometrage(int idVehicule, int kmActuels) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "UPDATE VEHICULES SET km_actuels = ? WHERE id_vehicule = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, kmActuels);
            pstmt.setInt(2, idVehicule);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du kilométrage du véhicule ID: " + idVehicule, ex);
            throw ex;
        } finally {
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Supprime un véhicule de la base de données.
     *
     * @param id ID du véhicule à supprimer
     * @return true si la suppression a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean delete(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbUtil.getConnection();
            conn.setAutoCommit(false);  // Début transaction

            // Vérifier si le véhicule peut être supprimé (pas de contraintes)
            if (hasRelatedRecords(conn, id)) {
                conn.rollback();
                LOGGER.log(Level.WARNING, "Impossible de supprimer le véhicule ID: " + id + " car il est référencé par d'autres entités");
                return false;
            }

            String sql = "DELETE FROM VEHICULES WHERE id_vehicule = ?";

            pstmt = conn.prepareStatement(sql);
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
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression du véhicule ID: " + id, ex);
            throw ex;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);  // Rétablir autocommit
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Erreur lors du rétablissement de l'autocommit", e);
                }
            }
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Vérifie si un véhicule existe par son immatriculation.
     *
     * @param immatriculation Immatriculation à vérifier
     * @return true si un véhicule avec cette immatriculation existe, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean existsByImmatriculation(String immatriculation) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT 1 FROM VEHICULES WHERE immatriculation = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, immatriculation);

            rs = pstmt.executeQuery();

            return rs.next();  // true si un résultat est trouvé

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la vérification de l'existence du véhicule par immatriculation", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Vérifie si un véhicule existe par son numéro de châssis.
     *
     * @param numeroChassi Numéro de châssis à vérifier
     * @return true si un véhicule avec ce numéro de châssis existe, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean existsByNumeroChassi(String numeroChassi) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT 1 FROM VEHICULES WHERE numero_chassi = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, numeroChassi);

            rs = pstmt.executeQuery();

            return rs.next();  // true si un résultat est trouvé

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la vérification de l'existence du véhicule par numéro de châssis", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère les informations TCO (Total Cost of Ownership) d'un véhicule.
     *
     * @param idVehicule ID du véhicule
     * @return Optional contenant le coût total et le kilométrage, vide en cas d'erreur
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Optional<TCOInfo> getTCOInfo(int idVehicule) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT * FROM v_TCO WHERE id_vehicule = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idVehicule);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                BigDecimal coutsTotaux = rs.getBigDecimal("couts_totaux");
                int kmActuels = rs.getInt("km_actuels");

                TCOInfo tcoInfo = new TCOInfo(coutsTotaux, kmActuels);
                return Optional.of(tcoInfo);
            }

            return Optional.empty();

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des informations TCO pour le véhicule ID: " + idVehicule, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Calcule les statistiques du parc automobile.
     *
     * @return Les statistiques du parc
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public ParcStats calculateParcStats() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            // Statistiques générales
            String sql = "SELECT " +
                    "COUNT(*) AS total_vehicules, " +
                    "SUM(CASE WHEN id_etat_voiture = 1 THEN 1 ELSE 0 END) AS disponibles, " +
                    "SUM(CASE WHEN id_etat_voiture = 2 THEN 1 ELSE 0 END) AS en_mission, " +
                    "SUM(CASE WHEN id_etat_voiture = 3 THEN 1 ELSE 0 END) AS hors_service, " +
                    "SUM(CASE WHEN id_etat_voiture = 4 THEN 1 ELSE 0 END) AS en_entretien, " +
                    "SUM(CASE WHEN id_etat_voiture = 5 THEN 1 ELSE 0 END) AS attribues, " +
                    "SUM(CASE WHEN id_etat_voiture = 6 THEN 1 ELSE 0 END) AS en_panne, " +
                    "AVG(CASE WHEN km_actuels IS NOT NULL THEN km_actuels ELSE 0 END) AS km_moyen " +
                    "FROM VEHICULES";

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int totalVehicules = rs.getInt("total_vehicules");
                int disponibles = rs.getInt("disponibles");
                int enMission = rs.getInt("en_mission");
                int horsService = rs.getInt("hors_service");
                int enEntretien = rs.getInt("en_entretien");
                int attribues = rs.getInt("attribues");
                int enPanne = rs.getInt("en_panne");
                double kmMoyen = rs.getDouble("km_moyen");

                ParcStats stats = new ParcStats(
                        totalVehicules, disponibles, enMission,
                        horsService, enEntretien, attribues,
                        enPanne, kmMoyen
                );

                return stats;
            }

            // Par défaut, retourner des stats vides
            return new ParcStats(0, 0, 0, 0, 0, 0, 0, 0);

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul des statistiques du parc", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère les véhicules nécessitant un entretien préventif.
     *
     * @param intervalleKm Intervalle de kilomètres pour l'entretien préventif
     * @return Liste des véhicules nécessitant un entretien
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Vehicule> findNeedingMaintenance(int intervalleKm) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Vehicule> vehicules = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            // Véhicules dont le kilométrage est proche d'un multiple de l'intervalle
            String sql = "SELECT v.*, e.lib_etat_voiture FROM VEHICULES v " +
                    "JOIN ETAT_VOITURE e ON v.id_etat_voiture = e.id_etat_voiture " +
                    "WHERE (v.km_actuels % ?) <= 500 " +
                    "AND v.km_actuels > 0 " +
                    "AND v.id_etat_voiture NOT IN (4, 6) " +  // Pas déjà en entretien ou en panne
                    "ORDER BY v.immatriculation";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, intervalleKm);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                vehicules.add(extractVehiculeFromResultSet(rs));
            }

            return vehicules;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des véhicules nécessitant un entretien", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère les états de véhicule disponibles.
     *
     * @return Liste des états de véhicule
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<EtatVoiture> getAllEtats() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<EtatVoiture> etats = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT id_etat_voiture, lib_etat_voiture FROM ETAT_VOITURE ORDER BY id_etat_voiture";

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                EtatVoiture etat = new EtatVoiture(
                        rs.getInt("id_etat_voiture"),
                        rs.getString("lib_etat_voiture")
                );

                etats.add(etat);
            }

            return etats;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des états de véhicule", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Extrait un objet Vehicule à partir d'un ResultSet.
     *
     * @param rs ResultSet contenant les données du véhicule
     * @return Objet Vehicule créé à partir des données
     * @throws SQLException En cas d'erreur d'accès aux données du ResultSet
     */
    private Vehicule extractVehiculeFromResultSet(ResultSet rs) throws SQLException {
        Vehicule vehicule = new Vehicule();

        // ID et état
        vehicule.setIdVehicule(rs.getInt("id_vehicule"));

        EtatVoiture etatVoiture = new EtatVoiture(
                rs.getInt("id_etat_voiture"),
                rs.getString("lib_etat_voiture")
        );
        vehicule.setEtatVoiture(etatVoiture);

        // Énergie
        String energieStr = rs.getString("energie");
        vehicule.setEnergie(Vehicule.TypeEnergie.fromString(energieStr));

        // Caractéristiques principales
        vehicule.setNumeroChassi(rs.getString("numero_chassi"));
        vehicule.setImmatriculation(rs.getString("immatriculation"));
        vehicule.setMarque(rs.getString("marque"));
        vehicule.setModele(rs.getString("modele"));

        // Caractéristiques secondaires
        vehicule.setNbPlaces(rs.getObject("nb_places") != null ? rs.getInt("nb_places") : null);
        vehicule.setPuissance(rs.getObject("puissance") != null ? rs.getInt("puissance") : null);
        vehicule.setCouleur(rs.getString("couleur"));

        // Dates
        Timestamp dateAcquisition = rs.getTimestamp("date_acquisition");
        if (dateAcquisition != null) {
            vehicule.setDateAcquisition(dateAcquisition.toLocalDateTime());
        }

        Timestamp dateAmmortissement = rs.getTimestamp("date_ammortissement");
        if (dateAmmortissement != null) {
            vehicule.setDateAmmortissement(dateAmmortissement.toLocalDateTime());
        }

        Timestamp dateMiseEnService = rs.getTimestamp("date_mise_en_service");
        if (dateMiseEnService != null) {
            vehicule.setDateMiseEnService(dateMiseEnService.toLocalDateTime());
        }

        Timestamp dateEtat = rs.getTimestamp("date_etat");
        if (dateEtat != null) {
            vehicule.setDateEtat(dateEtat.toLocalDateTime());
        }

        // Financier et kilométrage
        vehicule.setPrixVehicule(rs.getBigDecimal("prix_vehicule"));
        vehicule.setKmActuels(rs.getInt("km_actuels"));

        return vehicule;
    }

    /**
     * Vérifie si un véhicule a des enregistrements liés qui empêcheraient sa suppression.
     *
     * @param conn Connexion à la base de données
     * @param idVehicule ID du véhicule à vérifier
     * @return true si le véhicule a des enregistrements liés, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    private boolean hasRelatedRecords(Connection conn, int idVehicule) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // Vérifier les missions
            pstmt = conn.prepareStatement("SELECT 1 FROM MISSION WHERE id_vehicule = ? LIMIT 1");
            pstmt.setInt(1, idVehicule);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }

            // Vérifier les entretiens
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.closeResultSet(rs);

            pstmt = conn.prepareStatement("SELECT 1 FROM ENTRETIEN WHERE id_vehicule = ? LIMIT 1");
            pstmt.setInt(1, idVehicule);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }

            // Vérifier les assurances
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.closeResultSet(rs);

            pstmt = conn.prepareStatement("SELECT 1 FROM COUVRIR WHERE id_vehicule = ? LIMIT 1");
            pstmt.setInt(1, idVehicule);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }

            // Vérifier les affectations
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.closeResultSet(rs);

            pstmt = conn.prepareStatement("SELECT 1 FROM AFFECTATION WHERE id_vehicule = ? LIMIT 1");
            pstmt.setInt(1, idVehicule);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }

            // Vérifier le personnel avec véhicule attribué
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.closeResultSet(rs);

            pstmt = conn.prepareStatement("SELECT 1 FROM PERSONNEL WHERE id_vehicule = ? LIMIT 1");
            pstmt.setInt(1, idVehicule);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }

            // Pas d'enregistrement lié trouvé
            return false;

        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Classe interne représentant les statistiques des véhicules.
     */
    public static class ParcStats {
        private final int totalVehicules;
        private final int disponibles;
        private final int enMission;
        private final int horsService;
        private final int enEntretien;
        private final int attribues;
        private final int enPanne;
        private final double kmMoyen;

        public ParcStats(int totalVehicules, int disponibles, int enMission,
                         int horsService, int enEntretien, int attribues, int enPanne, double kmMoyen) {
            this.totalVehicules = totalVehicules;
            this.disponibles = disponibles;
            this.enMission = enMission;
            this.horsService = horsService;
            this.enEntretien = enEntretien;
            this.attribues = attribues;
            this.enPanne = enPanne;
            this.kmMoyen = kmMoyen;
        }

        // Getters
        public int getTotalVehicules() { return totalVehicules; }
        public int getDisponibles() { return disponibles; }
        public int getEnMission() { return enMission; }
        public int getHorsService() { return horsService; }
        public int getEnEntretien() { return enEntretien; }
        public int getAttribues() { return attribues; }
        public int getEnPanne() { return enPanne; }
        public double getKmMoyen() { return kmMoyen; }

        public double getPourcentageDisponibilite() {
            if (totalVehicules == 0) return 0;
            return (double) disponibles / totalVehicules * 100;
        }

        public double getPourcentageUtilisation() {
            if (totalVehicules == 0) return 0;
            return (double) (enMission + attribues) / totalVehicules * 100;
        }
    }

    /**
     * Classe interne représentant les informations de coût total de possession.
     */
    public static class TCOInfo {
        private final int idVehicule;
        private final BigDecimal coutsTotaux;
        private final int kmActuels;
        private final BigDecimal coutParKm;

        public TCOInfo(int idVehicule, BigDecimal coutsTotaux, int kmActuels) {
            this.idVehicule = idVehicule;
            this.coutsTotaux = coutsTotaux != null ? coutsTotaux : BigDecimal.ZERO;
            this.kmActuels = kmActuels;

            // Calcul du coût par km
            if (kmActuels > 0) {
                this.coutParKm = this.coutsTotaux.divide(BigDecimal.valueOf(kmActuels), 2, RoundingMode.HALF_UP);
            } else {
                this.coutParKm = BigDecimal.ZERO;
            }
        }

        // Constructeur utilisé dans la méthode getTCOInfo
        public TCOInfo(BigDecimal coutsTotaux, int kmActuels) {
            this(0, coutsTotaux, kmActuels);
        }

        // Getters
        public int getIdVehicule() { return idVehicule; }
        public BigDecimal getCoutsTotaux() { return coutsTotaux; }
        public int getKmActuels() { return kmActuels; }
        public BigDecimal getCoutParKm() { return coutParKm; }
    }
}