package com.miage.parcauto.dao;

import com.miage.parcauto.model.entretien.Entretien;
import com.miage.parcauto.model.vehicule.Vehicule;

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
     * @param dbUtil Instance de DbUtil à utiliser
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
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des entretiens pour le véhicule ID: " + idVehicule, ex);
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
     * @param fin Date de fin
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
     * @param entretien L'entretien à créer
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
            conn.setAutoCommit(false);  // Début transaction

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
                vehiculeDao.updateEtat(entretien.getIdVehicule(), 4); // 4 = En entretien
            }

            conn.commit();  // Valider transaction
            return entretien;

        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback();  // Annuler transaction en cas d'erreur
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction", e);
                }
            }
            LOGGER.log(Level.SEVERE, "Erreur lors de la création de l'entretien", ex);
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
            conn.setAutoCommit(false);  // Début transaction

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
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de l'entretien ID: " + entretien.getIdEntretien(), ex);
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
     * Met à jour le statut d'un entretien.
     * Met également à jour l'état du véhicule si nécessaire.
     *
     * @param idEntretien ID de l'entretien
     * @param statut Nouveau statut
     * @param updateVehiculeEtat Si true, met à jour l'état du véhicule selon le statut
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean updateStatut(int idEntretien, Entretien.StatutOT statut, boolean updateVehiculeEtat) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();
            conn.setAutoCommit(false);  // Début transaction

            // Récupérer d'abord l'ID du véhicule associé
            String sqlSelect = "SELECT id_vehicule FROM ENTRETIEN WHERE id_entretien = ?";
            pstmt = conn.prepareStatement(sqlSelect);
            pstmt.setInt(1, idEntretien);

            rs = pstmt.executeQuery();

            Integer idVehicule = null;
            if (rs.next()) {
                idVehicule = rs.getInt("id_vehicule");
            } else {
                return false;  // Entretien non trouvé
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
                    nouvelEtatVehicule = 1;  // 1 = Disponible
                } else if (statut == Entretien.StatutOT.EnCours) {
                    nouvelEtatVehicule = 4;  // 4 = En entretien
                } else {
                    // Pour les autres statuts, laisser l'état actuel
                    conn.commit();
                    return true;
                }

                vehiculeDao.updateEtat(idVehicule, nouvelEtatVehicule);
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
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du statut de l'entretien ID: " + idEntretien, ex);
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
     * @param cout Nouveau coût
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
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul du coût total des entretiens pour le véhicule ID: " + idVehicule, ex);
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
    public Entretien.EntretienStats calculateStats(int year) throws SQLException {
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

                Entretien.EntretienStats stats = new Entretien.EntretienStats(
                        totalEntretiens, planifies, enCours, termines,
                        preventifs, correctifs, coutTotal, coutMoyen
                );

                return stats;
            }

            // Par défaut, retourner des stats vides
            return new Entretien.EntretienStats(
                    0, 0, 0, 0, 0, 0, BigDecimal.ZERO, BigDecimal.ZERO
            );

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
        try {
            String immatriculation = rs.getString("immatriculation");
            String marque = rs.getString("marque");
            String modele = rs.getString("modele");

            if (immatriculation != null && marque != null && modele != null) {
                // Créer un objet Vehicule partiel pour l'affichage
                Vehicule vehicule = new Vehicule();
                vehicule.setIdVehicule(entretien.getIdVehicule());
                vehicule.setImmatriculation(immatriculation);
                vehicule.setMarque(marque);
                vehicule.setModele(modele);

                entretien.setVehicule(vehicule);
            }
        } catch (SQLException ex) {
            // Ignorer les colonnes non disponibles (cas où on ne joint pas avec VEHICULES)
        }

        return entretien;
    }

    // Classes statiques internes ajoutées pour répondre aux besoins spécifiques
    // et complétées dans la classe Entretien
    static {
        // Ajout de la classe EntretienStats à la classe Entretien
        try {
            Class.forName("com.miage.parcauto.model.entretien.Entretien");
        } catch (ClassNotFoundException e) {
            // Ignorer, car la classe Entretien devrait déjà être chargée
        }
    }
}