package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.finance.SocieteCompte;
import main.java.com.miage.parcauto.model.finance.Mouvement;
import main.java.com.miage.parcauto.model.rh.Personnel;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe d'accès aux données pour les comptes sociétaires.
 * Gère les opérations CRUD et les requêtes spécifiques liées aux comptes sociétaires.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class SocieteCompteDao {

    private static final Logger LOGGER = Logger.getLogger(SocieteCompteDao.class.getName());

    // Instance de DbUtil pour la gestion des connexions
    private final DbUtil dbUtil;

    // Instance de MouvementDao pour les opérations financières
    private MouvementDao mouvementDao;

    /**
     * Constructeur par défaut. Initialise l'instance de DbUtil.
     */
    public SocieteCompteDao() {
        this.dbUtil = DbUtil.getInstance();
        this.mouvementDao = new MouvementDao();
    }

    /**
     * Constructeur avec injection de dépendance pour les tests.
     *
     * @param dbUtil Instance de DbUtil à utiliser
     * @param mouvementDao Instance de MouvementDao à utiliser
     */
    public SocieteCompteDao(DbUtil dbUtil, MouvementDao mouvementDao) {
        this.dbUtil = dbUtil;
        this.mouvementDao = mouvementDao;
    }

    /**
     * Récupère tous les comptes sociétaires de la base de données.
     *
     * @return Liste des comptes sociétaires
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<SocieteCompte> findAll() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<SocieteCompte> comptes = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT sc.*, p.nom_personnel, p.prenom_personnel " +
                    "FROM SOCIETAIRE_COMPTE sc " +
                    "LEFT JOIN PERSONNEL p ON sc.id_personnel = p.id_personnel " +
                    "ORDER BY sc.nom";

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                comptes.add(extractSocieteCompteFromResultSet(rs));
            }

            return comptes;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les comptes sociétaires", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère un compte sociétaire par son ID.
     *
     * @param id ID du compte sociétaire à récupérer
     * @return Optional contenant le compte sociétaire s'il existe, vide sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Optional<SocieteCompte> findById(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT sc.*, p.nom_personnel, p.prenom_personnel " +
                    "FROM SOCIETAIRE_COMPTE sc " +
                    "LEFT JOIN PERSONNEL p ON sc.id_personnel = p.id_personnel " +
                    "WHERE sc.id_societaire = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(extractSocieteCompteFromResultSet(rs));
            }

            return Optional.empty();

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du compte sociétaire par ID: " + id, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère un compte sociétaire par son numéro.
     *
     * @param numero Numéro du compte sociétaire à récupérer
     * @return Optional contenant le compte sociétaire s'il existe, vide sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Optional<SocieteCompte> findByNumero(String numero) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT sc.*, p.nom_personnel, p.prenom_personnel " +
                    "FROM SOCIETAIRE_COMPTE sc " +
                    "LEFT JOIN PERSONNEL p ON sc.id_personnel = p.id_personnel " +
                    "WHERE sc.numero = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, numero);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(extractSocieteCompteFromResultSet(rs));
            }

            return Optional.empty();

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du compte sociétaire par numéro: " + numero, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère un compte sociétaire associé à un membre du personnel.
     *
     * @param idPersonnel ID du personnel associé au compte sociétaire
     * @return Optional contenant le compte sociétaire s'il existe, vide sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Optional<SocieteCompte> findByIdPersonnel(int idPersonnel) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT sc.*, p.nom_personnel, p.prenom_personnel " +
                    "FROM SOCIETAIRE_COMPTE sc " +
                    "LEFT JOIN PERSONNEL p ON sc.id_personnel = p.id_personnel " +
                    "WHERE sc.id_personnel = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idPersonnel);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(extractSocieteCompteFromResultSet(rs));
            }

            return Optional.empty();

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du compte sociétaire par ID personnel: " + idPersonnel, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Recherche des comptes sociétaires par nom.
     *
     * @param nom Nom ou partie du nom à rechercher
     * @return Liste des comptes sociétaires correspondants
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<SocieteCompte> searchByNom(String nom) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<SocieteCompte> comptes = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT sc.*, p.nom_personnel, p.prenom_personnel " +
                    "FROM SOCIETAIRE_COMPTE sc " +
                    "LEFT JOIN PERSONNEL p ON sc.id_personnel = p.id_personnel " +
                    "WHERE sc.nom LIKE ? " +
                    "ORDER BY sc.nom";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + nom + "%");

            rs = pstmt.executeQuery();

            while (rs.next()) {
                comptes.add(extractSocieteCompteFromResultSet(rs));
            }

            return comptes;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des comptes sociétaires par nom: " + nom, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère les comptes sociétaires avec un solde supérieur à un montant donné.
     *
     * @param montant Montant minimal du solde
     * @return Liste des comptes sociétaires avec un solde supérieur au montant
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<SocieteCompte> findBySoldeSuperieurA(BigDecimal montant) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<SocieteCompte> comptes = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT sc.*, p.nom_personnel, p.prenom_personnel " +
                    "FROM SOCIETAIRE_COMPTE sc " +
                    "LEFT JOIN PERSONNEL p ON sc.id_personnel = p.id_personnel " +
                    "WHERE sc.solde >= ? " +
                    "ORDER BY sc.solde DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1, montant);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                comptes.add(extractSocieteCompteFromResultSet(rs));
            }

            return comptes;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des comptes sociétaires avec un solde supérieur à: " + montant, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère les comptes sociétaires avec un solde inférieur à un montant donné.
     *
     * @param montant Montant maximal du solde
     * @return Liste des comptes sociétaires avec un solde inférieur au montant
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<SocieteCompte> findBySoldeInferieurA(BigDecimal montant) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<SocieteCompte> comptes = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT sc.*, p.nom_personnel, p.prenom_personnel " +
                    "FROM SOCIETAIRE_COMPTE sc " +
                    "LEFT JOIN PERSONNEL p ON sc.id_personnel = p.id_personnel " +
                    "WHERE sc.solde <= ? " +
                    "ORDER BY sc.solde ASC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1, montant);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                comptes.add(extractSocieteCompteFromResultSet(rs));
            }

            return comptes;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des comptes sociétaires avec un solde inférieur à: " + montant, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Crée un nouveau compte sociétaire dans la base de données.
     *
     * @param compte Le compte sociétaire à créer
     * @return Le compte sociétaire créé avec son ID généré
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public SocieteCompte create(SocieteCompte compte) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();
            conn.setAutoCommit(false);  // Début transaction

            String sql = "INSERT INTO SOCIETAIRE_COMPTE (id_personnel, nom, numero, solde, email, telephone) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Paramètres
            if (compte.getIdPersonnel() != null) {
                pstmt.setInt(1, compte.getIdPersonnel());
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }

            pstmt.setString(2, compte.getNom());
            pstmt.setString(3, compte.getNumero());
            pstmt.setBigDecimal(4, compte.getSolde() != null ? compte.getSolde() : BigDecimal.ZERO);
            pstmt.setString(5, compte.getEmail());
            pstmt.setString(6, compte.getTelephone());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La création du compte sociétaire a échoué, aucune ligne affectée.");
            }

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                compte.setIdSocietaire(rs.getInt(1));
            } else {
                throw new SQLException("La création du compte sociétaire a échoué, aucun ID généré.");
            }

            conn.commit();  // Valider transaction
            return compte;

        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback();  // Annuler transaction en cas d'erreur
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction", e);
                }
            }
            LOGGER.log(Level.SEVERE, "Erreur lors de la création du compte sociétaire", ex);
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
     * Met à jour un compte sociétaire dans la base de données.
     *
     * @param compte Le compte sociétaire à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean update(SocieteCompte compte) throws SQLException {
        if (compte.getIdSocietaire() == null) {
            throw new IllegalArgumentException("L'ID du compte sociétaire ne peut pas être null pour une mise à jour");
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbUtil.getConnection();
            conn.setAutoCommit(false);  // Début transaction

            String sql = "UPDATE SOCIETAIRE_COMPTE SET id_personnel = ?, nom = ?, numero = ?, " +
                    "solde = ?, email = ?, telephone = ? " +
                    "WHERE id_societaire = ?";

            pstmt = conn.prepareStatement(sql);

            // Paramètres
            if (compte.getIdPersonnel() != null) {
                pstmt.setInt(1, compte.getIdPersonnel());
            } else {
                pstmt.setNull(1, Types.INTEGER);
            }

            pstmt.setString(2, compte.getNom());
            pstmt.setString(3, compte.getNumero());
            pstmt.setBigDecimal(4, compte.getSolde() != null ? compte.getSolde() : BigDecimal.ZERO);
            pstmt.setString(5, compte.getEmail());
            pstmt.setString(6, compte.getTelephone());
            pstmt.setInt(7, compte.getIdSocietaire());

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
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du compte sociétaire ID: " + compte.getIdSocietaire(), ex);
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
     * Met à jour le solde d'un compte sociétaire.
     *
     * @param idSocietaire ID du compte sociétaire
     * @param nouveauSolde Nouveau solde
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean updateSolde(int idSocietaire, BigDecimal nouveauSolde) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "UPDATE SOCIETAIRE_COMPTE SET solde = ? WHERE id_societaire = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1, nouveauSolde);
            pstmt.setInt(2, idSocietaire);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du solde du compte sociétaire ID: " + idSocietaire, ex);
            throw ex;
        } finally {
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Effectue un dépôt sur un compte sociétaire.
     * Crée un mouvement et met à jour le solde.
     *
     * @param idSocietaire ID du compte sociétaire
     * @param montant Montant à déposer
     * @param description Description du dépôt (optionnel)
     * @return true si le dépôt a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     * @throws IllegalArgumentException Si le montant est négatif ou nul
     */
    public boolean effectuerDepot(int idSocietaire, BigDecimal montant, String description)
            throws SQLException, IllegalArgumentException {
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant du dépôt doit être positif");
        }

        Connection conn = null;

        try {
            conn = dbUtil.getConnection();
            conn.setAutoCommit(false);  // Début transaction

            // Récupérer le compte sociétaire
            Optional<SocieteCompte> compteOpt = findById(idSocietaire);
            if (!compteOpt.isPresent()) {
                throw new SQLException("Compte sociétaire non trouvé avec ID: " + idSocietaire);
            }

            SocieteCompte compte = compteOpt.get();

            // Calculer le nouveau solde
            BigDecimal nouveauSolde = compte.getSolde().add(montant);

            // Mettre à jour le solde
            updateSolde(idSocietaire, nouveauSolde);

            // Créer un mouvement de dépôt
            Mouvement mouvement = new Mouvement();
            mouvement.setIdSocietaire(idSocietaire);
            mouvement.setType(Mouvement.TypeMouvement.Depot);
            mouvement.setMontant(montant);
            mouvement.setDate(LocalDateTime.now());
            mouvement.setDescription(description);

            mouvementDao.create(mouvement);

            conn.commit();  // Valider transaction
            return true;

        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback();  // Annuler transaction en cas d'erreur
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction", e);
                }
            }
            LOGGER.log(Level.SEVERE, "Erreur lors du dépôt sur le compte sociétaire ID: " + idSocietaire, ex);
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
     * Effectue un retrait sur un compte sociétaire.
     * Crée un mouvement et met à jour le solde.
     *
     * @param idSocietaire ID du compte sociétaire
     * @param montant Montant à retirer
     * @param description Description du retrait (optionnel)
     * @return true si le retrait a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     * @throws IllegalArgumentException Si le montant est négatif ou nul
     * @throws IllegalStateException Si le solde est insuffisant
     */
    public boolean effectuerRetrait(int idSocietaire, BigDecimal montant, String description)
            throws SQLException, IllegalArgumentException, IllegalStateException {
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant du retrait doit être positif");
        }

        Connection conn = null;

        try {
            conn = dbUtil.getConnection();
            conn.setAutoCommit(false);  // Début transaction

            // Récupérer le compte sociétaire
            Optional<SocieteCompte> compteOpt = findById(idSocietaire);
            if (!compteOpt.isPresent()) {
                throw new SQLException("Compte sociétaire non trouvé avec ID: " + idSocietaire);
            }

            SocieteCompte compte = compteOpt.get();

            // Vérifier si le solde est suffisant
            if (compte.getSolde().compareTo(montant) < 0) {
                throw new IllegalStateException("Solde insuffisant pour effectuer le retrait");
            }

            // Calculer le nouveau solde
            BigDecimal nouveauSolde = compte.getSolde().subtract(montant);

            // Mettre à jour le solde
            updateSolde(idSocietaire, nouveauSolde);

            // Créer un mouvement de retrait
            Mouvement mouvement = new Mouvement();
            mouvement.setIdSocietaire(idSocietaire);
            mouvement.setType(Mouvement.TypeMouvement.Retrait);
            mouvement.setMontant(montant);
            mouvement.setDate(LocalDateTime.now());
            mouvement.setDescription(description);

            mouvementDao.create(mouvement);

            conn.commit();  // Valider transaction
            return true;

        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback();  // Annuler transaction en cas d'erreur
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction", e);
                }
            }
            LOGGER.log(Level.SEVERE, "Erreur lors du retrait sur le compte sociétaire ID: " + idSocietaire, ex);
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
     * Effectue un prélèvement mensuel sur un compte sociétaire.
     * Crée un mouvement de type Mensualite et met à jour le solde.
     *
     * @param idSocietaire ID du compte sociétaire
     * @param montant Montant à prélever
     * @param description Description du prélèvement (optionnel)
     * @return true si le prélèvement a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     * @throws IllegalArgumentException Si le montant est négatif ou nul
     * @throws IllegalStateException Si le solde est insuffisant
     */
    public boolean effectuerPrelevementMensuel(int idSocietaire, BigDecimal montant, String description)
            throws SQLException, IllegalArgumentException, IllegalStateException {
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant du prélèvement doit être positif");
        }

        Connection conn = null;

        try {
            conn = dbUtil.getConnection();
            conn.setAutoCommit(false);  // Début transaction

            // Récupérer le compte sociétaire
            Optional<SocieteCompte> compteOpt = findById(idSocietaire);
            if (!compteOpt.isPresent()) {
                throw new SQLException("Compte sociétaire non trouvé avec ID: " + idSocietaire);
            }

            SocieteCompte compte = compteOpt.get();

            // Vérifier si le solde est suffisant
            if (compte.getSolde().compareTo(montant) < 0) {
                throw new IllegalStateException("Solde insuffisant pour effectuer le prélèvement mensuel");
            }

            // Calculer le nouveau solde
            BigDecimal nouveauSolde = compte.getSolde().subtract(montant);

            // Mettre à jour le solde
            updateSolde(idSocietaire, nouveauSolde);

            // Créer un mouvement de mensualité
            Mouvement mouvement = new Mouvement();
            mouvement.setIdSocietaire(idSocietaire);
            mouvement.setType(Mouvement.TypeMouvement.Mensualite);
            mouvement.setMontant(montant);
            mouvement.setDate(LocalDateTime.now());
            mouvement.setDescription(description);

            mouvementDao.create(mouvement);

            conn.commit();  // Valider transaction
            return true;

        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback();  // Annuler transaction en cas d'erreur
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Erreur lors du rollback de la transaction", e);
                }
            }
            LOGGER.log(Level.SEVERE, "Erreur lors du prélèvement mensuel sur le compte sociétaire ID: " + idSocietaire, ex);
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
     * Supprime un compte sociétaire de la base de données.
     * Attention : cette opération supprimera également tous les mouvements associés.
     *
     * @param id ID du compte sociétaire à supprimer
     * @return true si la suppression a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean delete(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbUtil.getConnection();
            conn.setAutoCommit(false);  // Début transaction

            // Vérifier s'il y a des affectations liées au compte
            if (hasAffectations(conn, id)) {
                LOGGER.log(Level.WARNING, "Impossible de supprimer le compte sociétaire ID: " + id +
                        " car il est associé à des affectations");
                return false;
            }

            // Supprimer d'abord les mouvements associés
            String sqlMouvements = "DELETE FROM MOUVEMENT WHERE id_societaire = ?";
            pstmt = conn.prepareStatement(sqlMouvements);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            // Supprimer ensuite les documents associés
            dbUtil.closePreparedStatement(pstmt);
            String sqlDocuments = "DELETE FROM DOCUMENT_SOCIETAIRE WHERE id_societaire = ?";
            pstmt = conn.prepareStatement(sqlDocuments);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            // Enfin, supprimer le compte sociétaire
            dbUtil.closePreparedStatement(pstmt);
            String sqlCompte = "DELETE FROM SOCIETAIRE_COMPTE WHERE id_societaire = ?";
            pstmt = conn.prepareStatement(sqlCompte);
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
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression du compte sociétaire ID: " + id, ex);
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
     * Vérifie si un numéro de compte existe déjà.
     *
     * @param numero Numéro de compte à vérifier
     * @return true si le numéro existe déjà, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean numeroExiste(String numero) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT 1 FROM SOCIETAIRE_COMPTE WHERE numero = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, numero);

            rs = pstmt.executeQuery();

            return rs.next();  // true si un résultat est trouvé

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la vérification de l'existence du numéro de compte: " + numero, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère le solde total de tous les comptes sociétaires.
     *
     * @return Solde total
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public BigDecimal getSoldeTotal() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT SUM(solde) AS total FROM SOCIETAIRE_COMPTE";

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total");
                return total != null ? total : BigDecimal.ZERO;
            }

            return BigDecimal.ZERO;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul du solde total des comptes sociétaires", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère le nombre total de comptes sociétaires.
     *
     * @return Nombre de comptes
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public int countComptes() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT COUNT(*) AS count FROM SOCIETAIRE_COMPTE";

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("count");
            }

            return 0;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des comptes sociétaires", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Calcule les statistiques des comptes sociétaires.
     *
     * @return Objet contenant les statistiques
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public ComptesStats calculateStats() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT " +
                    "COUNT(*) AS total, " +
                    "SUM(solde) AS solde_total, " +
                    "AVG(solde) AS solde_moyen, " +
                    "MAX(solde) AS solde_max, " +
                    "MIN(solde) AS solde_min, " +
                    "SUM(CASE WHEN solde > 0 THEN 1 ELSE 0 END) AS comptes_crediteurs, " +
                    "SUM(CASE WHEN solde < 0 THEN 1 ELSE 0 END) AS comptes_debiteurs, " +
                    "SUM(CASE WHEN solde = 0 THEN 1 ELSE 0 END) AS comptes_zero " +
                    "FROM SOCIETAIRE_COMPTE";

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int total = rs.getInt("total");
                BigDecimal soldeTotal = rs.getBigDecimal("solde_total");
                BigDecimal soldeMoyen = rs.getBigDecimal("solde_moyen");
                BigDecimal soldeMax = rs.getBigDecimal("solde_max");
                BigDecimal soldeMin = rs.getBigDecimal("solde_min");
                int comptesCredits = rs.getInt("comptes_crediteurs");
                int comptesDebits = rs.getInt("comptes_debiteurs");
                int comptesZero = rs.getInt("comptes_zero");

                soldeTotal = soldeTotal != null ? soldeTotal : BigDecimal.ZERO;
                soldeMoyen = soldeMoyen != null ? soldeMoyen : BigDecimal.ZERO;
                soldeMax = soldeMax != null ? soldeMax : BigDecimal.ZERO;
                soldeMin = soldeMin != null ? soldeMin : BigDecimal.ZERO;

                return new ComptesStats(
                        total, soldeTotal, soldeMoyen, soldeMax, soldeMin,
                        comptesCredits, comptesDebits, comptesZero
                );
            }

            // Par défaut, retourner des stats vides
            return new ComptesStats(
                    0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                    0, 0, 0
            );

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul des statistiques des comptes sociétaires", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Vérifie si le compte sociétaire a des affectations.
     *
     * @param conn Connexion à la base de données
     * @param idSocietaire ID du compte sociétaire
     * @return true si des affectations existent, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    private boolean hasAffectations(Connection conn, int idSocietaire) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT 1 FROM AFFECTATION WHERE id_societaire = ? LIMIT 1";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idSocietaire);

            rs = pstmt.executeQuery();

            return rs.next();  // true si un résultat est trouvé

        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Extrait un objet SocieteCompte à partir d'un ResultSet.
     *
     * @param rs ResultSet contenant les données du compte sociétaire
     * @return Objet SocieteCompte créé à partir des données
     * @throws SQLException En cas d'erreur d'accès aux données du ResultSet
     */
    private SocieteCompte extractSocieteCompteFromResultSet(ResultSet rs) throws SQLException {
        SocieteCompte compte = new SocieteCompte();

        compte.setIdSocietaire(rs.getInt("id_sociétaire"));

        // ID personnel peut être null
        Object idPersonnel = rs.getObject("id_personnel");
        if (idPersonnel != null) {
            compte.setIdPersonnel((Integer) idPersonnel);
        }

        compte.setNom(rs.getString("nom"));
        compte.setNumero(rs.getString("numero"));
        compte.setSolde(rs.getBigDecimal("solde"));
        compte.setEmail(rs.getString("email"));
        compte.setTelephone(rs.getString("telephone"));

        // Essayer de récupérer les informations du personnel (si présentes)
        try {
            String nomPersonnel = rs.getString("nom_personnel");
            String prenomPersonnel = rs.getString("prenom_personnel");

            if (nomPersonnel != null && prenomPersonnel != null) {
                Personnel personnel = new Personnel();
                personnel.setIdPersonnel(compte.getIdPersonnel());
                personnel.setNomPersonnel(nomPersonnel);
                personnel.setPrenomPersonnel(prenomPersonnel);

                compte.setPersonnel(personnel);
            }
        } catch (SQLException ex) {
            // Ignorer les colonnes non disponibles
        }

        return compte;
    }

    /**
     * Classe interne représentant les statistiques des comptes sociétaires.
     */
    public static class ComptesStats {
        private final int totalComptes;
        private final BigDecimal soldeTotal;
        private final BigDecimal soldeMoyen;
        private final BigDecimal soldeMax;
        private final BigDecimal soldeMin;
        private final int comptesCredits;
        private final int comptesDebits;
        private final int comptesZero;

        /**
         * Constructeur pour les statistiques des comptes.
         *
         * @param totalComptes Nombre total de comptes
         * @param soldeTotal Solde total de tous les comptes
         * @param soldeMoyen Solde moyen des comptes
         * @param soldeMax Solde maximum
         * @param soldeMin Solde minimum
         * @param comptesCredits Nombre de comptes créditeurs (solde > 0)
         * @param comptesDebits Nombre de comptes débiteurs (solde < 0)
         * @param comptesZero Nombre de comptes avec solde nul
         */
        public ComptesStats(int totalComptes, BigDecimal soldeTotal, BigDecimal soldeMoyen,
                            BigDecimal soldeMax, BigDecimal soldeMin, int comptesCredits,
                            int comptesDebits, int comptesZero) {
            this.totalComptes = totalComptes;
            this.soldeTotal = soldeTotal;
            this.soldeMoyen = soldeMoyen;
            this.soldeMax = soldeMax;
            this.soldeMin = soldeMin;
            this.comptesCredits = comptesCredits;
            this.comptesDebits = comptesDebits;
            this.comptesZero = comptesZero;
        }

        // Getters

        public int getTotalComptes() {
            return totalComptes;
        }

        public BigDecimal getSoldeTotal() {
            return soldeTotal;
        }

        public BigDecimal getSoldeMoyen() {
            return soldeMoyen;
        }

        public BigDecimal getSoldeMax() {
            return soldeMax;
        }

        public BigDecimal getSoldeMin() {
            return soldeMin;
        }

        public int getComptesCredits() {
            return comptesCredits;
        }

        public int getComptesDebits() {
            return comptesDebits;
        }

        public int getComptesZero() {
            return comptesZero;
        }

        /**
         * @return Pourcentage de comptes créditeurs
         */
        public double getPourcentageComptesCredits() {
            if (totalComptes == 0) return 0;
            return (comptesCredits * 100.0) / totalComptes;
        }

        /**
         * @return Pourcentage de comptes débiteurs
         */
        public double getPourcentageComptesDebits() {
            if (totalComptes == 0) return 0;
            return (comptesDebits * 100.0) / totalComptes;
        }

        /**
         * @return Pourcentage de comptes avec solde nul
         */
        public double getPourcentageComptesZero() {
            if (totalComptes == 0) return 0;
            return (comptesZero * 100.0) / totalComptes;
        }
    }
}