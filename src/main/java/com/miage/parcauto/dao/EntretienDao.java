package main.java.com.miage.parcauto.dao;

/**
 * @deprecated Utiliser EntretienRepository et son implémentation EntretienRepositoryImpl.
 * Cette classe sera supprimée après migration complète vers le pattern Repository.
 */

import main.java.com.miage.parcauto.model.entretien.Entretien;
import main.java.com.miage.parcauto.model.vehicule.EtatVoiture;
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
 * Classe d'accès aux données pour les entretiens de véhicules.
 * Gère les opérations CRUD et les requêtes spécifiques liées aux entretiens.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class EntretienDao {

    private static final Logger LOGGER = Logger.getLogger(EntretienDao.class.getName());

    // Instance de DbUtil pour la gestion des connexions
    private final DbUtil dbUtil;

    // Instance de VehiculeDao pour les opérations liées aux véhicules
    private final VehiculeDao vehiculeDao;

    /**
     * Constructeur par défaut. Initialise les instances de DbUtil et VehiculeDao.
     */
    public EntretienDao() {
        this.dbUtil = DbUtil.getInstance();
        this.vehiculeDao = new VehiculeDao();
    }

    /**
     * Constructeur avec injection de dépendance pour les tests.
     *
     * @param dbUtil      Instance de DbUtil à utiliser
     * @param vehiculeDao Instance de VehiculeDao à utiliser
     */
    public EntretienDao(DbUtil dbUtil, VehiculeDao vehiculeDao) {
        this.dbUtil = dbUtil;
        this.vehiculeDao = vehiculeDao;
    }

    /**
     * Récupère tous les entretiens de la base de données.
     *
     * @return Liste des entretiens
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Entretien> findAll() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Entretien> entretiens = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT e.*, v.immatriculation, v.marque, v.modele " +
                    "FROM ENTRETIEN e " +
                    "JOIN VEHICULES v ON e.id_vehicule = v.id_vehicule " +
                    "ORDER BY e.date_entree_entr DESC";

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                entretiens.add(extractEntretienFromResultSet(rs));
            }

            return entretiens;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les entretiens", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère un entretien par son ID.
     *
     * @param id ID de l'entretien à récupérer
     * @return Optional contenant l'entretien s'il existe, vide sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Optional<Entretien> findById(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT e.*, v.immatriculation, v.marque, v.modele " +
                    "FROM ENTRETIEN e " +
                    "JOIN VEHICULES v ON e.id_vehicule = v.id_vehicule " +
                    "WHERE e.id_entretien = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(extractEntretienFromResultSet(rs));
            }

            return Optional.empty();

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de l'entretien par ID: " + id, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère les entretiens d'un véhicule.
     *
     * @param idVehicule ID du véhicule
     * @return Liste des entretiens du véhicule
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Entretien> findByVehicule(int idVehicule) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Entretien> entretiens = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT e.*, v.immatriculation, v.marque, v.modele " +
                    "FROM ENTRETIEN e " +
                    "JOIN VEHICULES v ON e.id_vehicule = v.id_vehicule " +
                    "WHERE e.id_vehicule = ? " +
                    "ORDER BY e.date_entree_entr DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idVehicule);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                entretiens.add(extractEntretienFromResultSet(rs));
            }

            return entretiens;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des entretiens pour le véhicule ID: " + idVehicule,
                    ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère les entretiens selon leur statut.
     *
     * @param statut Statut des entretiens à récupérer
     * @return Liste des entretiens ayant ce statut
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Entretien> findByStatut(Entretien.StatutOT statut) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Entretien> entretiens = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT e.*, v.immatriculation, v.marque, v.modele " +
                    "FROM ENTRETIEN e " +
                    "JOIN VEHICULES v ON e.id_vehicule = v.id_vehicule " +
                    "WHERE e.statut_ot = ? " +
                    "ORDER BY e.date_entree_entr DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, statut.name());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                entretiens.add(extractEntretienFromResultSet(rs));
            }

            return entretiens;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des entretiens par statut: " + statut, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère les entretiens selon leur type.
     *
     * @param type Type des entretiens à récupérer
     * @return Liste des entretiens de ce type
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Entretien> findByType(Entretien.TypeEntretien type) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Entretien> entretiens = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT e.*, v.immatriculation, v.marque, v.modele " +
                    "FROM ENTRETIEN e " +
                    "JOIN VEHICULES v ON e.id_vehicule = v.id_vehicule " +
                    "WHERE e.type = ? " +
                    "ORDER BY e.date_entree_entr DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, type.name());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                entretiens.add(extractEntretienFromResultSet(rs));
            }

            return entretiens;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des entretiens par type: " + type, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère les entretiens en cours.
     *
     * @return Liste des entretiens en cours
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Entretien> findEnCours() throws SQLException {
        return findByStatut(Entretien.StatutOT.EnCours);
    }

    /**
     * Récupère les entretiens planifiés (ouverts).
     *
     * @return Liste des entretiens planifiés
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Entretien> findPlanifies() throws SQLException {
        return findByStatut(Entretien.StatutOT.Ouvert);
    }

    /**
     * Récupère les entretiens terminés (clôturés).
     *
     * @return Liste des entretiens terminés
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Entretien> findTermines() throws SQLException {
        return findByStatut(Entretien.StatutOT.Cloture);
    }

    /**
     * Récupère les entretiens entre deux dates.
     *
     * @param debut Date de début
     * @param fin   Date de fin
     * @return Liste des entretiens dans cette période
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Entretien> findByPeriode(LocalDateTime debut, LocalDateTime fin) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Entretien> entretiens = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT e.*, v.immatriculation, v.marque, v.modele " +
                    "FROM ENTRETIEN e " +
                    "JOIN VEHICULES v ON e.id_vehicule = v.id_vehicule " +
                    "WHERE e.date_entree_entr BETWEEN ? AND ? " +
                    "ORDER BY e.date_entree_entr DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, Timestamp.valueOf(debut));
            pstmt.setTimestamp(2, Timestamp.valueOf(fin));

            rs = pstmt.executeQuery();

            while (rs.next()) {
                entretiens.add(extractEntretienFromResultSet(rs));
            }

            return entretiens;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des entretiens par période", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Crée un nouvel entretien dans la base de données.
     * Met également à jour l'état du véhicule si nécessaire.
     *
     * @param entretien          L'entretien à créer
     * @param updateVehiculeEtat Si true, met à jour l'état du véhicule
     * @return L'entretien créé avec son ID généré
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Entretien create(Entretien entretien, boolean updateVehiculeEtat) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();
            conn.setAutoCommit(false); // Début transaction

            String sql = "INSERT INTO ENTRETIEN (id_vehicule, date_entree_entr, date_sortie_entr, " +
                    "motif_entr, observation, cout_entr, lieu_entr, type, statut_ot) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Paramètres obligatoires
            pstmt.setInt(1, entretien.getIdVehicule());

            if (entretien.getDateEntreeEntr() != null) {
                pstmt.setTimestamp(2, Timestamp.valueOf(entretien.getDateEntreeEntr()));
            } else {
                pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            }

            // Paramètres optionnels
            if (entretien.getDateSortieEntr() != null) {
                pstmt.setTimestamp(3, Timestamp.valueOf(entretien.getDateSortieEntr()));
            } else {
                pstmt.setNull(3, Types.TIMESTAMP);
            }

            pstmt.setString(4, entretien.getMotifEntr());
            pstmt.setString(5, entretien.getObservation());

            if (entretien.getCoutEntr() != null) {
                pstmt.setBigDecimal(6, entretien.getCoutEntr());
            } else {
                pstmt.setBigDecimal(6, BigDecimal.ZERO);
            }

            pstmt.setString(7, entretien.getLieuEntr());
            pstmt.setString(8, entretien.getType().name());
            pstmt.setString(9, entretien.getStatutOt().name());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La création de l'entretien a échoué, aucune ligne affectée.");
            }

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                entretien.setIdEntretien(rs.getInt(1));
            } else {
                throw new SQLException("La création de l'entretien a échoué, aucun ID généré.");
            }

            // Si demandé, mettre à jour l'état du véhicule à "En entretien" (id = 4)
            if (updateVehiculeEtat) {
                vehiculeDao.updateEtat(entretien.getIdVehicule(), EtatVoiture.EN_ENTRETIEN);
            }

            conn.commit(); // Valider transaction
            return entretien;

        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback(); // Annuler transaction en cas d'erreur
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction", e);
                }
            }
            LOGGER.log(Level.SEVERE, "Erreur lors de la création de l'entretien", ex);
            throw ex;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Rétablir autocommit
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
     * Met à jour un entretien dans la base de données.
     *
     * @param entretien L'entretien à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean update(Entretien entretien) throws SQLException {
        if (entretien.getIdEntretien() == null) {
            throw new IllegalArgumentException("L'ID de l'entretien ne peut pas être null pour une mise à jour");
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbUtil.getConnection();
            conn.setAutoCommit(false); // Début transaction

            String sql = "UPDATE ENTRETIEN SET id_vehicule = ?, date_entree_entr = ?, date_sortie_entr = ?, " +
                    "motif_entr = ?, observation = ?, cout_entr = ?, lieu_entr = ?, type = ?, statut_ot = ? " +
                    "WHERE id_entretien = ?";

            pstmt = conn.prepareStatement(sql);

            // Paramètres obligatoires
            pstmt.setInt(1, entretien.getIdVehicule());

            if (entretien.getDateEntreeEntr() != null) {
                pstmt.setTimestamp(2, Timestamp.valueOf(entretien.getDateEntreeEntr()));
            } else {
                pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            }

            // Paramètres optionnels
            if (entretien.getDateSortieEntr() != null) {
                pstmt.setTimestamp(3, Timestamp.valueOf(entretien.getDateSortieEntr()));
            } else {
                pstmt.setNull(3, Types.TIMESTAMP);
            }

            pstmt.setString(4, entretien.getMotifEntr());
            pstmt.setString(5, entretien.getObservation());

            if (entretien.getCoutEntr() != null) {
                pstmt.setBigDecimal(6, entretien.getCoutEntr());
            } else {
                pstmt.setBigDecimal(6, BigDecimal.ZERO);
            }

            pstmt.setString(7, entretien.getLieuEntr());
            pstmt.setString(8, entretien.getType().name());
            pstmt.setString(9, entretien.getStatutOt().name());

            // ID de l'entretien à mettre à jour
            pstmt.setInt(10, entretien.getIdEntretien());

            int affectedRows = pstmt.executeUpdate();

            conn.commit(); // Valider transaction
            return affectedRows > 0;

        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback(); // Annuler transaction en cas d'erreur
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction", e);
                }
            }
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de l'entretien ID: " + entretien.getIdEntretien(),
                    ex);
            throw ex;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Rétablir autocommit
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Erreur lors du rétablissement de l'autocommit", e);
                }
            }
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Met à jour le statut d'un entretien.
     * Met également à jour l'état du véhicule si nécessaire.
     *
     * @param idEntretien        ID de l'entretien
     * @param statut             Nouveau statut
     * @param updateVehiculeEtat Si true, met à jour l'état du véhicule selon le
     *                           statut
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean updateStatut(int idEntretien, Entretien.StatutOT statut, boolean updateVehiculeEtat)
            throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();
            conn.setAutoCommit(false); // Début transaction

            // Récupérer d'abord l'ID du véhicule associé
            String sqlSelect = "SELECT id_vehicule FROM ENTRETIEN WHERE id_entretien = ?";
            pstmt = conn.prepareStatement(sqlSelect);
            pstmt.setInt(1, idEntretien);

            rs = pstmt.executeQuery();

            Integer idVehicule = null;
            if (rs.next()) {
                idVehicule = rs.getInt("id_vehicule");
            } else {
                return false; // Entretien non trouvé
            }

            // Mise à jour du statut de l'entretien
            dbUtil.closePreparedStatement(pstmt);
            String sqlUpdate = "UPDATE ENTRETIEN SET statut_ot = ? ";

            // Si on clôture l'entretien, ajouter la date de sortie
            if (statut == Entretien.StatutOT.Cloture) {
                sqlUpdate += ", date_sortie_entr = ? ";
            }

            sqlUpdate += "WHERE id_entretien = ?";

            pstmt = conn.prepareStatement(sqlUpdate);
            pstmt.setString(1, statut.name());

            if (statut == Entretien.StatutOT.Cloture) {
                pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                pstmt.setInt(3, idEntretien);
            } else {
                pstmt.setInt(2, idEntretien);
            }

            int affectedRows = pstmt.executeUpdate();

            // Si demandé et que la mise à jour a réussi, mettre à jour l'état du véhicule
            if (updateVehiculeEtat && affectedRows > 0 && idVehicule != null) {
                int nouvelEtatVehicule;

                // Déterminer le nouvel état du véhicule selon le statut de l'entretien
                if (statut == Entretien.StatutOT.Cloture) {
                    nouvelEtatVehicule = EtatVoiture.DISPONIBLE; // 1 = Disponible
                } else if (statut == Entretien.StatutOT.EnCours) {
                    nouvelEtatVehicule = EtatVoiture.EN_ENTRETIEN; // 4 = En entretien
                } else {
                    // Pour les autres statuts, laisser l'état actuel
                    conn.commit();
                    return true;
                }

                vehiculeDao.updateEtat(idVehicule, nouvelEtatVehicule);
            }

            conn.commit(); // Valider transaction
            return affectedRows > 0;

        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback(); // Annuler transaction en cas d'erreur
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction", e);
                }
            }
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du statut de l'entretien ID: " + idEntretien, ex);
            throw ex;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Rétablir autocommit
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
     * Démarre un entretien (passage au statut "en cours").
     *
     * @param idEntretien ID de l'entretien
     * @return true si le changement d'état a été effectué
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean demarrerEntretien(int idEntretien) throws SQLException {
        return updateStatut(idEntretien, Entretien.StatutOT.EnCours, true);
    }

    /**
     * Clôture un entretien (passage au statut "clôturé").
     *
     * @param idEntretien ID de l'entretien
     * @return true si le changement d'état a été effectué
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean cloturerEntretien(int idEntretien) throws SQLException {
        return updateStatut(idEntretien, Entretien.StatutOT.Cloture, true);
    }

    /**
     * Met à jour le coût d'un entretien.
     *
     * @param idEntretien ID de l'entretien
     * @param cout        Nouveau coût
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean updateCout(int idEntretien, BigDecimal cout) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "UPDATE ENTRETIEN SET cout_entr = ? WHERE id_entretien = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1, cout);
            pstmt.setInt(2, idEntretien);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du coût de l'entretien ID: " + idEntretien, ex);
            throw ex;
        } finally {
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Supprime un entretien de la base de données.
     *
     * @param id ID de l'entretien à supprimer
     * @return true si la suppression a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean delete(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "DELETE FROM ENTRETIEN WHERE id_entretien = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de l'entretien ID: " + id, ex);
            throw ex;
        } finally {
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Calcule le coût total des entretiens pour un véhicule.
     *
     * @param idVehicule ID du véhicule
     * @return Coût total des entretiens
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public BigDecimal calculateTotalCost(int idVehicule) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT SUM(cout_entr) AS total_cost FROM ENTRETIEN WHERE id_vehicule = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idVehicule);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                BigDecimal totalCost = rs.getBigDecimal("total_cost");
                return totalCost != null ? totalCost : BigDecimal.ZERO;
            }

            return BigDecimal.ZERO;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE,
                    "Erreur lors du calcul du coût total des entretiens pour le véhicule ID: " + idVehicule, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Calcule le coût moyen des entretiens par type.
     *
     * @param type Type d'entretien (null pour tous les types)
     * @return Coût moyen des entretiens
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public BigDecimal calculateAverageCost(Entretien.TypeEntretien type) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT AVG(cout_entr) AS avg_cost FROM ENTRETIEN";

            if (type != null) {
                sql += " WHERE type = ?";
            }

            pstmt = conn.prepareStatement(sql);

            if (type != null) {
                pstmt.setString(1, type.name());
            }

            rs = pstmt.executeQuery();

            if (rs.next()) {
                BigDecimal avgCost = rs.getBigDecimal("avg_cost");
                return avgCost != null ? avgCost : BigDecimal.ZERO;
            }

            return BigDecimal.ZERO;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul du coût moyen des entretiens", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Calcule les statistiques d'entretien.
     *
     * @param year Année pour les statistiques (0 pour toutes les années)
     * @return Les statistiques d'entretien
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public EntretienStats calculateStats(int year) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ");
            sql.append("COUNT(*) AS total_entretiens, ");
            sql.append("SUM(CASE WHEN statut_ot = 'Ouvert' THEN 1 ELSE 0 END) AS planifies, ");
            sql.append("SUM(CASE WHEN statut_ot = 'EnCours' THEN 1 ELSE 0 END) AS en_cours, ");
            sql.append("SUM(CASE WHEN statut_ot = 'Cloture' THEN 1 ELSE 0 END) AS termines, ");
            sql.append("SUM(CASE WHEN type = 'Preventif' THEN 1 ELSE 0 END) AS preventifs, ");
            sql.append("SUM(CASE WHEN type = 'Correctif' THEN 1 ELSE 0 END) AS correctifs, ");
            sql.append("SUM(cout_entr) AS cout_total, ");
            sql.append("AVG(cout_entr) AS cout_moyen ");
            sql.append("FROM ENTRETIEN ");

            if (year > 0) {
                sql.append("WHERE YEAR(date_entree_entr) = ?");
            }

            pstmt = conn.prepareStatement(sql.toString());

            if (year > 0) {
                pstmt.setInt(1, year);
            }

            rs = pstmt.executeQuery();

            if (rs.next()) {
                int totalEntretiens = rs.getInt("total_entretiens");
                int planifies = rs.getInt("planifies");
                int enCours = rs.getInt("en_cours");
                int termines = rs.getInt("termines");
                int preventifs = rs.getInt("preventifs");
                int correctifs = rs.getInt("correctifs");
                BigDecimal coutTotal = rs.getBigDecimal("cout_total");
                BigDecimal coutMoyen = rs.getBigDecimal("cout_moyen");

                coutTotal = coutTotal != null ? coutTotal : BigDecimal.ZERO;
                coutMoyen = coutMoyen != null ? coutMoyen : BigDecimal.ZERO;

                EntretienStats stats = new EntretienStats(
                        totalEntretiens, planifies, enCours, termines,
                        preventifs, correctifs, coutTotal, coutMoyen);

                return stats;
            }

            // Par défaut, retourner des stats vides
            return new EntretienStats(
                    0, 0, 0, 0, 0, 0, BigDecimal.ZERO, BigDecimal.ZERO);

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul des statistiques d'entretien", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère les entretiens à venir (planifiés) pour un véhicule.
     *
     * @param idVehicule ID du véhicule
     * @return Liste des entretiens planifiés pour ce véhicule
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Entretien> findEntretiensAVenir(int idVehicule) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Entretien> entretiens = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT e.*, v.immatriculation, v.marque, v.modele " +
                    "FROM ENTRETIEN e " +
                    "JOIN VEHICULES v ON e.id_vehicule = v.id_vehicule " +
                    "WHERE e.id_vehicule = ? AND e.statut_ot = 'Ouvert' " +
                    "ORDER BY e.date_entree_entr ASC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idVehicule);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                entretiens.add(extractEntretienFromResultSet(rs));
            }

            return entretiens;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la recherche des entretiens à venir pour le véhicule ID: " + idVehicule, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Extrait un objet Entretien à partir d'un ResultSet.
     *
     * @param rs ResultSet contenant les données de l'entretien
     * @return Objet Entretien créé à partir des données
     * @throws SQLException En cas d'erreur d'accès aux données du ResultSet
     */
    private Entretien extractEntretienFromResultSet(ResultSet rs) throws SQLException {
        Entretien entretien = new Entretien();

        // ID et véhicule
        entretien.setIdEntretien(rs.getInt("id_entretien"));
        entretien.setIdVehicule(rs.getInt("id_vehicule"));

        // Dates
        Timestamp dateEntree = rs.getTimestamp("date_entree_entr");
        if (dateEntree != null) {
            entretien.setDateEntreeEntr(dateEntree.toLocalDateTime());
        }

        Timestamp dateSortie = rs.getTimestamp("date_sortie_entr");
        if (dateSortie != null) {
            entretien.setDateSortieEntr(dateSortie.toLocalDateTime());
        }

        // Caractéristiques
        entretien.setMotifEntr(rs.getString("motif_entr"));
        entretien.setObservation(rs.getString("observation"));
        entretien.setCoutEntr(rs.getBigDecimal("cout_entr"));
        entretien.setLieuEntr(rs.getString("lieu_entr"));

        // Type et statut
        String typeStr = rs.getString("type");
        if (typeStr != null) {
            entretien.setType(Entretien.TypeEntretien.fromString(typeStr));
        }

        String statutStr = rs.getString("statut_ot");
        if (statutStr != null) {
            entretien.setStatutOt(Entretien.StatutOT.fromString(statutStr));
        }

        // Informations véhicule (si disponibles)
        // Stockons les informations du véhicule dans des variables liées à l'entretien
        // mais sans utiliser setVehicule (qui n'existe pas)
        try {
            String immatriculation = rs.getString("immatriculation");
            String marque = rs.getString("marque");
            String modele = rs.getString("modele");

            // Au lieu d'utiliser setVehicule, on peut stocker ces informations
            // dans des attributs transitoires ou les utiliser directement où nécessaire
            entretien.setInfoVehicule(immatriculation + " - " + marque + " " + modele);
        } catch (SQLException ex) {
            // Ignorer les colonnes non disponibles (cas où on ne joint pas avec VEHICULES)
        } catch (NoSuchMethodError e) {
            // Si la méthode setInfoVehicule n'existe pas, on peut l'ignorer
            LOGGER.log(Level.WARNING, "Méthode setInfoVehicule non disponible dans la classe Entretien", e);
        }

        return entretien;
    }

    /**
     * Classe interne représentant les statistiques d'entretien.
     */
    public static class EntretienStats {
        private final int totalEntretiens;
        private final int planifies;
        private final int enCours;
        private final int termines;
        private final int preventifs;
        private final int correctifs;
        private final BigDecimal coutTotal;
        private final BigDecimal coutMoyen;

        /**
         * Constructeur pour les statistiques d'entretien.
         *
         * @param totalEntretiens Nombre total d'entretiens
         * @param planifies       Nombre d'entretiens planifiés
         * @param enCours         Nombre d'entretiens en cours
         * @param termines        Nombre d'entretiens terminés
         * @param preventifs      Nombre d'entretiens préventifs
         * @param correctifs      Nombre d'entretiens correctifs
         * @param coutTotal       Coût total des entretiens
         * @param coutMoyen       Coût moyen par entretien
         */
        public EntretienStats(int totalEntretiens, int planifies, int enCours, int termines,
                int preventifs, int correctifs,
                BigDecimal coutTotal, BigDecimal coutMoyen) {
            this.totalEntretiens = totalEntretiens;
            this.planifies = planifies;
            this.enCours = enCours;
            this.termines = termines;
            this.preventifs = preventifs;
            this.correctifs = correctifs;
            this.coutTotal = coutTotal;
            this.coutMoyen = coutMoyen;
        }

        // Getters

        /**
         * @return Nombre total d'entretiens
         */
        public int getTotalEntretiens() {
            return totalEntretiens;
        }

        /**
         * @return Nombre d'entretiens planifiés
         */
        public int getPlanifies() {
            return planifies;
        }

        /**
         * @return Nombre d'entretiens en cours
         */
        public int getEnCours() {
            return enCours;
        }

        /**
         * @return Nombre d'entretiens terminés
         */
        public int getTermines() {
            return termines;
        }

        /**
         * @return Nombre d'entretiens préventifs
         */
        public int getPreventifs() {
            return preventifs;
        }

        /**
         * @return Nombre d'entretiens correctifs
         */
        public int getCorrectifs() {
            return correctifs;
        }

        /**
         * @return Coût total des entretiens
         */
        public BigDecimal getCoutTotal() {
            return coutTotal;
        }

        /**
         * @return Coût moyen par entretien
         */
        public BigDecimal getCoutMoyen() {
            return coutMoyen;
        }

        /**
         * @return Pourcentage d'entretiens préventifs
         */
        public double getPourcentagePreventifs() {
            if (totalEntretiens == 0)
                return 0;
            return (preventifs * 100.0) / totalEntretiens;
        }

        /**
         * @return Pourcentage d'entretiens correctifs
         */
        public double getPourcentageCorrectifs() {
            if (totalEntretiens == 0)
                return 0;
            return (correctifs * 100.0) / totalEntretiens;
        }

        /**
         * @return Ratio entre entretiens préventifs et correctifs
         */
        public double getRatioPreventifCorrectif() {
            if (correctifs == 0)
                return Double.POSITIVE_INFINITY;
            return (preventifs * 1.0) / correctifs;
        }
    }
}