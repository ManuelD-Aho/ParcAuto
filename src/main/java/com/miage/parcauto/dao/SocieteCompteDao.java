package com.miage.parcauto.dao;

import com.miage.parcauto.model.finance.SocieteCompte;
import com.miage.parcauto.model.finance.Mouvement;
import com.miage.parcauto.model.rh.Personnel;
import com.miage.parcauto.model.vehicule.Vehicule;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe DAO pour la gestion des comptes sociétaires dans la base de données.
 * Gère les opérations CRUD et les interactions spécifiques aux comptes financiers.
 *
 * @author MIAGE Holding - ParcAuto
 * @version 1.0
 */
public class SocieteCompteDao {

    private static final Logger LOGGER = Logger.getLogger(SocieteCompteDao.class.getName());

    /**
     * Récupère tous les comptes sociétaires de la base de données.
     *
     * @return liste de tous les comptes sociétaires
     * @throws SQLException si une erreur de base de données survient
     */
    public List<SocieteCompte> findAll() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<SocieteCompte> comptes = new ArrayList<>();

        try {
            conn = DbUtil.getConnection();

            String sql = "SELECT sc.*, p.nom_personnel, p.prenom_personnel, p.matricule " +
                    "FROM SOCIETAIRE_COMPTE sc " +
                    "LEFT JOIN PERSONNEL p ON sc.id_personnel = p.id_personnel " +
                    "ORDER BY sc.nom";
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                comptes.add(buildSocieteCompteFromResultSet(rs));
            }

            return comptes;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les comptes sociétaires", e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Récupère un compte sociétaire par son identifiant.
     *
     * @param idSocietaire identifiant du compte sociétaire
     * @return un Optional contenant le compte sociétaire ou un Optional vide si non trouvé
     * @throws SQLException si une erreur de base de données survient
     */
    public Optional<SocieteCompte> findById(int idSocietaire) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();

            String sql = "SELECT sc.*, p.nom_personnel, p.prenom_personnel, p.matricule " +
                    "FROM SOCIETAIRE_COMPTE sc " +
                    "LEFT JOIN PERSONNEL p ON sc.id_personnel = p.id_personnel " +
                    "WHERE sc.id_societaire = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idSocietaire);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(buildSocieteCompteFromResultSet(rs));
            }

            return Optional.empty();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération du compte sociétaire ID: " + idSocietaire, e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Récupère un compte sociétaire par son numéro.
     *
     * @param numero numéro du compte sociétaire
     * @return un Optional contenant le compte sociétaire ou un Optional vide si non trouvé
     * @throws SQLException si une erreur de base de données survient
     */
    public Optional<SocieteCompte> findByNumero(String numero) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();

            String sql = "SELECT sc.*, p.nom_personnel, p.prenom_personnel, p.matricule " +
                    "FROM SOCIETAIRE_COMPTE sc " +
                    "LEFT JOIN PERSONNEL p ON sc.id_personnel = p.id_personnel " +
                    "WHERE sc.numero = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, numero);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(buildSocieteCompteFromResultSet(rs));
            }

            return Optional.empty();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération du compte sociétaire par numéro: " + numero, e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Récupère un compte sociétaire associé à un personnel.
     *
     * @param idPersonnel identifiant du personnel
     * @return un Optional contenant le compte sociétaire ou un Optional vide si non trouvé
     * @throws SQLException si une erreur de base de données survient
     */
    public Optional<SocieteCompte> findByPersonnel(int idPersonnel) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();

            String sql = "SELECT sc.*, p.nom_personnel, p.prenom_personnel, p.matricule " +
                    "FROM SOCIETAIRE_COMPTE sc " +
                    "LEFT JOIN PERSONNEL p ON sc.id_personnel = p.id_personnel " +
                    "WHERE sc.id_personnel = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idPersonnel);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(buildSocieteCompteFromResultSet(rs));
            }

            return Optional.empty();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération du compte sociétaire pour personnel ID: " + idPersonnel, e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Recherche des comptes sociétaires par nom ou numéro.
     *
     * @param searchTerm terme de recherche (nom ou numéro)
     * @return liste des comptes sociétaires correspondant au terme de recherche
     * @throws SQLException si une erreur de base de données survient
     */
    public List<SocieteCompte> searchByNomOrNumero(String searchTerm) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<SocieteCompte> comptes = new ArrayList<>();

        try {
            conn = DbUtil.getConnection();

            String sql = "SELECT sc.*, p.nom_personnel, p.prenom_personnel, p.matricule " +
                    "FROM SOCIETAIRE_COMPTE sc " +
                    "LEFT JOIN PERSONNEL p ON sc.id_personnel = p.id_personnel " +
                    "WHERE sc.nom LIKE ? OR sc.numero LIKE ? " +
                    "ORDER BY sc.nom";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + searchTerm + "%");
            pstmt.setString(2, "%" + searchTerm + "%");

            rs = pstmt.executeQuery();

            while (rs.next()) {
                comptes.add(buildSocieteCompteFromResultSet(rs));
            }

            return comptes;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de comptes sociétaires: " + searchTerm, e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Crée un nouveau compte sociétaire dans la base de données.
     *
     * @param compte compte sociétaire à créer
     * @return identifiant du compte sociétaire créé
     * @throws SQLException si une erreur de base de données survient
     */
    public int save(SocieteCompte compte) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();

            String sql = "INSERT INTO SOCIETAIRE_COMPTE (nom, numero, solde, email, telephone, id_personnel) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, compte.getNom());
            pstmt.setString(2, compte.getNumero());
            pstmt.setBigDecimal(3, compte.getSolde() != null ? compte.getSolde() : BigDecimal.ZERO);
            pstmt.setString(4, compte.getEmail());
            pstmt.setString(5, compte.getTelephone());

            if (compte.getPersonnel() != null && compte.getPersonnel().getIdPersonnel() != null) {
                pstmt.setInt(6, compte.getPersonnel().getIdPersonnel());
            } else {
                pstmt.setNull(6, java.sql.Types.INTEGER);
            }

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La création du compte sociétaire a échoué, aucune ligne affectée.");
            }

            rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                int generatedId = rs.getInt(1);

                // Création d'un enregistrement de la date de création dans la base de données
                DbUtil.closePreparedStatement(pstmt);

                String updateSql = "UPDATE SOCIETAIRE_COMPTE SET date_creation = ? WHERE id_societaire = ?";
                pstmt = conn.prepareStatement(updateSql);
                pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                pstmt.setInt(2, generatedId);
                pstmt.executeUpdate();

                DbUtil.commitTransaction(conn);
                return generatedId;
            } else {
                throw new SQLException("La création du compte sociétaire a échoué, aucun ID obtenu.");
            }

        } catch (SQLException e) {
            DbUtil.rollbackTransaction(conn);
            LOGGER.log(Level.SEVERE, "Erreur lors de la création du compte sociétaire: " + compte.getNumero(), e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Met à jour un compte sociétaire existant dans la base de données.
     *
     * @param compte compte sociétaire à mettre à jour
     * @return true si la mise à jour a réussi
     * @throws SQLException si une erreur de base de données survient
     */
    public boolean update(SocieteCompte compte) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DbUtil.getConnection();

            String sql = "UPDATE SOCIETAIRE_COMPTE SET nom = ?, numero = ?, solde = ?, " +
                    "email = ?, telephone = ?, id_personnel = ? WHERE id_societaire = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, compte.getNom());
            pstmt.setString(2, compte.getNumero());
            pstmt.setBigDecimal(3, compte.getSolde() != null ? compte.getSolde() : BigDecimal.ZERO);
            pstmt.setString(4, compte.getEmail());
            pstmt.setString(5, compte.getTelephone());

            if (compte.getPersonnel() != null && compte.getPersonnel().getIdPersonnel() != null) {
                pstmt.setInt(6, compte.getPersonnel().getIdPersonnel());
            } else {
                pstmt.setNull(6, java.sql.Types.INTEGER);
            }

            pstmt.setInt(7, compte.getIdSocietaire());

            int affectedRows = pstmt.executeUpdate();
            DbUtil.commitTransaction(conn);

            return affectedRows > 0;

        } catch (SQLException e) {
            DbUtil.rollbackTransaction(conn);
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du compte sociétaire ID: " + compte.getIdSocietaire(), e);
            throw e;
        } finally {
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Met à jour le solde d'un compte sociétaire.
     *
     * @param idSocietaire identifiant du compte sociétaire
     * @param solde nouveau solde
     * @return true si la mise à jour a réussi
     * @throws SQLException si une erreur de base de données survient
     */
    public boolean updateSolde(int idSocietaire, BigDecimal solde) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DbUtil.getConnection();

            String sql = "UPDATE SOCIETAIRE_COMPTE SET solde = ? WHERE id_societaire = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1, solde);
            pstmt.setInt(2, idSocietaire);

            int affectedRows = pstmt.executeUpdate();
            DbUtil.commitTransaction(conn);

            return affectedRows > 0;

        } catch (SQLException e) {
            DbUtil.rollbackTransaction(conn);
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du solde du compte sociétaire ID: " + idSocietaire, e);
            throw e;
        } finally {
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Effectue un dépôt sur un compte sociétaire.
     *
     * @param idSocietaire identifiant du compte sociétaire
     * @param montant montant à déposer
     * @param description description du dépôt
     * @param idUtilisateur identifiant de l'utilisateur effectuant l'opération
     * @return identifiant du mouvement créé
     * @throws SQLException si une erreur de base de données survient
     */
    public int effectuerDepot(int idSocietaire, BigDecimal montant, String description, int idUtilisateur) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();

            // Récupérer le solde actuel
            String sqlSelect = "SELECT solde FROM SOCIETAIRE_COMPTE WHERE id_societaire = ?";
            pstmt = conn.prepareStatement(sqlSelect);
            pstmt.setInt(1, idSocietaire);

            rs = pstmt.executeQuery();

            if (!rs.next()) {
                throw new SQLException("Compte sociétaire non trouvé pour ID: " + idSocietaire);
            }

            BigDecimal soldeActuel = rs.getBigDecimal("solde");
            BigDecimal nouveauSolde = soldeActuel.add(montant);

            DbUtil.closePreparedStatement(pstmt);
            DbUtil.closeResultSet(rs);

            // Mettre à jour le solde
            String sqlUpdate = "UPDATE SOCIETAIRE_COMPTE SET solde = ? WHERE id_societaire = ?";
            pstmt = conn.prepareStatement(sqlUpdate);
            pstmt.setBigDecimal(1, nouveauSolde);
            pstmt.setInt(2, idSocietaire);

            pstmt.executeUpdate();

            DbUtil.closePreparedStatement(pstmt);

            // Créer le mouvement
            String sqlInsert = "INSERT INTO MOUVEMENT (id_societaire, date, type, montant, description, id_utilisateur) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, idSocietaire);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(3, "Depot");
            pstmt.setBigDecimal(4, montant);
            pstmt.setString(5, description);
            pstmt.setInt(6, idUtilisateur);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La création du mouvement de dépôt a échoué, aucune ligne affectée.");
            }

            rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                int generatedId = rs.getInt(1);
                DbUtil.commitTransaction(conn);
                return generatedId;
            } else {
                throw new SQLException("La création du mouvement de dépôt a échoué, aucun ID obtenu.");
            }

        } catch (SQLException e) {
            DbUtil.rollbackTransaction(conn);
            LOGGER.log(Level.SEVERE, "Erreur lors du dépôt sur le compte sociétaire ID: " + idSocietaire, e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Effectue un retrait sur un compte sociétaire.
     *
     * @param idSocietaire identifiant du compte sociétaire
     * @param montant montant à retirer
     * @param description description du retrait
     * @param idUtilisateur identifiant de l'utilisateur effectuant l'opération
     * @return identifiant du mouvement créé
     * @throws SQLException si une erreur de base de données survient
     * @throws IllegalArgumentException si le solde est insuffisant
     */
    public int effectuerRetrait(int idSocietaire, BigDecimal montant, String description, int idUtilisateur)
            throws SQLException, IllegalArgumentException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();

            // Récupérer le solde actuel
            String sqlSelect = "SELECT solde FROM SOCIETAIRE_COMPTE WHERE id_societaire = ?";
            pstmt = conn.prepareStatement(sqlSelect);
            pstmt.setInt(1, idSocietaire);

            rs = pstmt.executeQuery();

            if (!rs.next()) {
                throw new SQLException("Compte sociétaire non trouvé pour ID: " + idSocietaire);
            }

            BigDecimal soldeActuel = rs.getBigDecimal("solde");

            // Vérifier si le solde est suffisant
            if (soldeActuel.compareTo(montant) < 0) {
                throw new IllegalArgumentException("Solde insuffisant pour effectuer le retrait");
            }

            BigDecimal nouveauSolde = soldeActuel.subtract(montant);

            DbUtil.closePreparedStatement(pstmt);
            DbUtil.closeResultSet(rs);

            // Mettre à jour le solde
            String sqlUpdate = "UPDATE SOCIETAIRE_COMPTE SET solde = ? WHERE id_societaire = ?";
            pstmt = conn.prepareStatement(sqlUpdate);
            pstmt.setBigDecimal(1, nouveauSolde);
            pstmt.setInt(2, idSocietaire);

            pstmt.executeUpdate();

            DbUtil.closePreparedStatement(pstmt);

            // Créer le mouvement (montant négatif pour un retrait)
            String sqlInsert = "INSERT INTO MOUVEMENT (id_societaire, date, type, montant, description, id_utilisateur) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, idSocietaire);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(3, "Retrait");
            pstmt.setBigDecimal(4, montant.negate());  // Montant négatif pour un retrait
            pstmt.setString(5, description);
            pstmt.setInt(6, idUtilisateur);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La création du mouvement de retrait a échoué, aucune ligne affectée.");
            }

            rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                int generatedId = rs.getInt(1);
                DbUtil.commitTransaction(conn);
                return generatedId;
            } else {
                throw new SQLException("La création du mouvement de retrait a échoué, aucun ID obtenu.");
            }

        } catch (SQLException e) {
            DbUtil.rollbackTransaction(conn);
            LOGGER.log(Level.SEVERE, "Erreur lors du retrait sur le compte sociétaire ID: " + idSocietaire, e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Effectue un paiement de mensualité sur un compte sociétaire.
     *
     * @param idSocietaire identifiant du compte sociétaire
     * @param montant montant de la mensualité
     * @param description description de la mensualité
     * @param idUtilisateur identifiant de l'utilisateur effectuant l'opération
     * @return identifiant du mouvement créé
     * @throws SQLException si une erreur de base de données survient
     * @throws IllegalArgumentException si le solde est insuffisant
     */
    public int effectuerPaiementMensualite(int idSocietaire, BigDecimal montant, String description, int idUtilisateur)
            throws SQLException, IllegalArgumentException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();

            // Récupérer le solde actuel et le nombre de mensualités payées
            String sqlSelect = "SELECT sc.solde, sc.mensualites_payees, sc.total_mensualites " +
                    "FROM SOCIETAIRE_COMPTE sc WHERE sc.id_societaire = ?";
            pstmt = conn.prepareStatement(sqlSelect);
            pstmt.setInt(1, idSocietaire);

            rs = pstmt.executeQuery();

            if (!rs.next()) {
                throw new SQLException("Compte sociétaire non trouvé pour ID: " + idSocietaire);
            }

            BigDecimal soldeActuel = rs.getBigDecimal("solde");
            int mensualitesPayees = rs.getInt("mensualites_payees");
            int totalMensualites = rs.getInt("total_mensualites");

            // Vérifier si le solde est suffisant
            if (soldeActuel.compareTo(montant) < 0) {
                throw new IllegalArgumentException("Solde insuffisant pour effectuer le paiement de mensualité");
            }

            // Vérifier si toutes les mensualités sont déjà payées
            if (mensualitesPayees >= totalMensualites) {
                throw new IllegalArgumentException("Toutes les mensualités ont déjà été payées");
            }

            BigDecimal nouveauSolde = soldeActuel.subtract(montant);

            DbUtil.closePreparedStatement(pstmt);
            DbUtil.closeResultSet(rs);

            // Mettre à jour le solde et le nombre de mensualités payées
            String sqlUpdate = "UPDATE SOCIETAIRE_COMPTE SET solde = ?, mensualites_payees = ? WHERE id_societaire = ?";
            pstmt = conn.prepareStatement(sqlUpdate);
            pstmt.setBigDecimal(1, nouveauSolde);
            pstmt.setInt(2, mensualitesPayees + 1);
            pstmt.setInt(3, idSocietaire);

            pstmt.executeUpdate();

            DbUtil.closePreparedStatement(pstmt);

            // Créer le mouvement (montant négatif pour un paiement)
            String sqlInsert = "INSERT INTO MOUVEMENT (id_societaire, date, type, montant, description, id_utilisateur) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, idSocietaire);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(3, "Mensualite");
            pstmt.setBigDecimal(4, montant.negate());  // Montant négatif pour un paiement
            pstmt.setString(5, description);
            pstmt.setInt(6, idUtilisateur);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La création du mouvement de mensualité a échoué, aucune ligne affectée.");
            }

            rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                int generatedId = rs.getInt(1);
                DbUtil.commitTransaction(conn);
                return generatedId;
            } else {
                throw new SQLException("La création du mouvement de mensualité a échoué, aucun ID obtenu.");
            }

        } catch (SQLException e) {
            DbUtil.rollbackTransaction(conn);
            LOGGER.log(Level.SEVERE, "Erreur lors du paiement de mensualité sur le compte sociétaire ID: " + idSocietaire, e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Affecte un véhicule à un compte sociétaire.
     *
     * @param idSocietaire identifiant du compte sociétaire
     * @param idVehicule identifiant du véhicule
     * @param dateAffectation date d'affectation
     * @return true si l'affectation a réussi
     * @throws SQLException si une erreur de base de données survient
     */
    public boolean affecterVehicule(int idSocietaire, int idVehicule, LocalDateTime dateAffectation) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DbUtil.getConnection();

            // Vérifier si le véhicule est déjà affecté à un autre compte
            String sqlCheck = "SELECT COUNT(*) FROM SOCIETAIRE_COMPTE WHERE vehicule_attribue = ?";
            pstmt = conn.prepareStatement(sqlCheck);
            pstmt.setInt(1, idVehicule);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                throw new SQLException("Le véhicule est déjà affecté à un autre compte sociétaire");
            }

            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);

            // Affecter le véhicule au compte
            String sqlUpdate = "UPDATE SOCIETAIRE_COMPTE SET vehicule_attribue = ?, date_affectation_vehicule = ? " +
                    "WHERE id_societaire = ?";
            pstmt = conn.prepareStatement(sqlUpdate);
            pstmt.setInt(1, idVehicule);
            pstmt.setTimestamp(2, Timestamp.valueOf(dateAffectation));
            pstmt.setInt(3, idSocietaire);

            int affectedRows = pstmt.executeUpdate();
            DbUtil.commitTransaction(conn);

            return affectedRows > 0;

        } catch (SQLException e) {
            DbUtil.rollbackTransaction(conn);
            LOGGER.log(Level.SEVERE, "Erreur lors de l'affectation du véhicule au compte sociétaire", e);
            throw e;
        } finally {
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Définit les paramètres de mensualité pour un compte sociétaire.
     *
     * @param idSocietaire identifiant du compte sociétaire
     * @param mensualite montant de la mensualité
     * @param totalMensualites nombre total de mensualités
     * @return true si la mise à jour a réussi
     * @throws SQLException si une erreur de base de données survient
     */
    public boolean definirMensualite(int idSocietaire, BigDecimal mensualite, int totalMensualites) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DbUtil.getConnection();

            String sql = "UPDATE SOCIETAIRE_COMPTE SET mensualite = ?, total_mensualites = ? WHERE id_societaire = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1, mensualite);
            pstmt.setInt(2, totalMensualites);
            pstmt.setInt(3, idSocietaire);

            int affectedRows = pstmt.executeUpdate();
            DbUtil.commitTransaction(conn);

            return affectedRows > 0;

        } catch (SQLException e) {
            DbUtil.rollbackTransaction(conn);
            LOGGER.log(Level.SEVERE, "Erreur lors de la définition des mensualités pour le compte sociétaire ID: " + idSocietaire, e);
            throw e;
        } finally {
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Récupère les statistiques sur les comptes sociétaires.
     *
     * @return tableau contenant [nombre de comptes, solde total, moyenne des soldes]
     * @throws SQLException si une erreur de base de données survient
     */
    public Object[] getStatistiquesComptes() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();

            String sql = "SELECT COUNT(*) as nombre, SUM(solde) as total, AVG(solde) as moyenne FROM SOCIETAIRE_COMPTE";
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            Object[] stats = new Object[3];

            if (rs.next()) {
                stats[0] = rs.getInt("nombre");
                stats[1] = rs.getBigDecimal("total");
                stats[2] = rs.getBigDecimal("moyenne");
            }

            return stats;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des statistiques des comptes sociétaires", e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Supprime un compte sociétaire de la base de données.
     *
     * @param idSocietaire identifiant du compte sociétaire à supprimer
     * @return true si la suppression a réussi
     * @throws SQLException si une erreur de base de données survient
     */
    public boolean delete(int idSocietaire) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DbUtil.getConnection();

            // Vérifier si le compte a des mouvements associés
            String sqlCheck = "SELECT COUNT(*) FROM MOUVEMENT WHERE id_societaire = ?";
            pstmt = conn.prepareStatement(sqlCheck);
            pstmt.setInt(1, idSocietaire);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                throw new SQLException("Impossible de supprimer le compte sociétaire : des mouvements financiers sont associés");
            }

            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);

            // Vérifier si le compte a des affectations
            String sqlCheckAff = "SELECT COUNT(*) FROM AFFECTATION WHERE id_societaire = ?";
            pstmt = conn.prepareStatement(sqlCheckAff);
            pstmt.setInt(1, idSocietaire);

            rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                throw new SQLException("Impossible de supprimer le compte sociétaire : des affectations sont associées");
            }

            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);

            // Vérifier si le compte a des documents
            String sqlCheckDoc = "SELECT COUNT(*) FROM DOCUMENT_SOCIETAIRE WHERE id_societaire = ?";
            pstmt = conn.prepareStatement(sqlCheckDoc);
            pstmt.setInt(1, idSocietaire);

            rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                throw new SQLException("Impossible de supprimer le compte sociétaire : des documents sont associés");
            }

            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);

            // Supprimer le compte
            String sqlDelete = "DELETE FROM SOCIETAIRE_COMPTE WHERE id_societaire = ?";
            pstmt = conn.prepareStatement(sqlDelete);
            pstmt.setInt(1, idSocietaire);

            int affectedRows = pstmt.executeUpdate();
            DbUtil.commitTransaction(conn);

            return affectedRows > 0;

        } catch (SQLException e) {
            DbUtil.rollbackTransaction(conn);
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression du compte sociétaire ID: " + idSocietaire, e);
            throw e;
        } finally {
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Construit un objet SocieteCompte à partir d'un ResultSet.
     *
     * @param rs ResultSet contenant les données du compte sociétaire
     * @return objet SocieteCompte construit
     * @throws SQLException si une erreur de base de données survient
     */
    private SocieteCompte buildSocieteCompteFromResultSet(ResultSet rs) throws SQLException {
        SocieteCompte compte = new SocieteCompte();
        compte.setIdSocietaire(rs.getInt("id_societaire"));
        compte.setNom(rs.getString("nom"));
        compte.setNumero(rs.getString("numero"));
        compte.setSolde(rs.getBigDecimal("solde"));
        compte.setEmail(rs.getString("email"));
        compte.setTelephone(rs.getString("telephone"));

        // Récupérer les informations de personnel si associé
        if (rs.getObject("id_personnel") != null) {
            Personnel personnel = new Personnel(rs.getInt("id_personnel"));

            try {
                personnel.setNomPersonnel(rs.getString("nom_personnel"));
                personnel.setPrenomPersonnel(rs.getString("prenom_personnel"));
                personnel.setMatricule(rs.getString("matricule"));
            } catch (SQLException e) {
                // Ces colonnes peuvent ne pas être présentes dans certaines requêtes
                LOGGER.log(Level.FINE, "Certaines colonnes de personnel ne sont pas présentes dans le ResultSet");
            }

            compte.setPersonnel(personnel);
        }

        // Récupérer la date de création si présente
        Timestamp dateCreation = rs.getTimestamp("date_creation");
        if (dateCreation != null) {
            compte.setDateCreation(dateCreation.toLocalDateTime());
        }

        // Récupérer les informations de véhicule si présent
        if (rs.getObject("vehicule_attribue") != null) {
            Vehicule vehicule = new Vehicule(rs.getInt("vehicule_attribue"));
            compte.setVehiculeAttribue(vehicule);

            // Date d'affectation du véhicule
            Timestamp dateAffectation = rs.getTimestamp("date_affectation_vehicule");
            if (dateAffectation != null) {
                compte.setDateAffectationVehicule(dateAffectation.toLocalDateTime());
            }
        }

        // Récupérer les informations de mensualité
        try {
            compte.setMensualite(rs.getBigDecimal("mensualite"));
            compte.setTotalMensualites(rs.getInt("total_mensualites"));
            compte.setMensualitesPayees(rs.getInt("mensualites_payees"));
        } catch (SQLException e) {
            // Ces colonnes peuvent ne pas être présentes dans certaines versions de la base
            LOGGER.log(Level.FINE, "Certaines colonnes de mensualité ne sont pas présentes dans le ResultSet");
        }

        return compte;
    }
}