package com.miage.parcauto.dao;

import com.miage.parcauto.model.finance.Mouvement;
import com.miage.parcauto.model.finance.SocieteCompte;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe DAO pour la gestion des mouvements financiers dans la base de données.
 * Gère les opérations CRUD et les interactions spécifiques aux mouvements financiers.
 *
 * @author MIAGE Holding - ParcAuto
 * @version 1.0
 */
public class MouvementDao {

    private static final Logger LOGGER = Logger.getLogger(MouvementDao.class.getName());

    /**
     * Récupère tous les mouvements financiers de la base de données.
     *
     * @return liste de tous les mouvements
     * @throws SQLException si une erreur de base de données survient
     */
    public List<Mouvement> findAll() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Mouvement> mouvements = new ArrayList<>();

        try {
            conn = DbUtil.getConnection();

            String sql = "SELECT m.*, sc.numero, sc.nom " +
                    "FROM MOUVEMENT m " +
                    "JOIN SOCIETAIRE_COMPTE sc ON m.id_societaire = sc.id_societaire " +
                    "ORDER BY m.date DESC";
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                mouvements.add(buildMouvementFromResultSet(rs));
            }

            return mouvements;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les mouvements", e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Récupère un mouvement par son identifiant.
     *
     * @param id identifiant du mouvement
     * @return un Optional contenant le mouvement ou un Optional vide si non trouvé
     * @throws SQLException si une erreur de base de données survient
     */
    public Optional<Mouvement> findById(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();

            String sql = "SELECT m.*, sc.numero, sc.nom " +
                    "FROM MOUVEMENT m " +
                    "JOIN SOCIETAIRE_COMPTE sc ON m.id_societaire = sc.id_societaire " +
                    "WHERE m.id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(buildMouvementFromResultSet(rs));
            }

            return Optional.empty();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération du mouvement ID: " + id, e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Récupère les mouvements d'un compte sociétaire.
     *
     * @param idSocietaire identifiant du compte sociétaire
     * @return liste des mouvements du compte sociétaire
     * @throws SQLException si une erreur de base de données survient
     */
    public List<Mouvement> findBySocietaire(int idSocietaire) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Mouvement> mouvements = new ArrayList<>();

        try {
            conn = DbUtil.getConnection();

            String sql = "SELECT m.*, sc.numero, sc.nom " +
                    "FROM MOUVEMENT m " +
                    "JOIN SOCIETAIRE_COMPTE sc ON m.id_societaire = sc.id_societaire " +
                    "WHERE m.id_societaire = ? " +
                    "ORDER BY m.date DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idSocietaire);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                mouvements.add(buildMouvementFromResultSet(rs));
            }

            return mouvements;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des mouvements pour le compte sociétaire ID: " + idSocietaire, e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Récupère les mouvements d'un certain type.
     *
     * @param type type de mouvement (Depot, Retrait, Mensualite)
     * @return liste des mouvements du type spécifié
     * @throws SQLException si une erreur de base de données survient
     */
    public List<Mouvement> findByType(String type) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Mouvement> mouvements = new ArrayList<>();

        try {
            conn = DbUtil.getConnection();

            String sql = "SELECT m.*, sc.numero, sc.nom " +
                    "FROM MOUVEMENT m " +
                    "JOIN SOCIETAIRE_COMPTE sc ON m.id_societaire = sc.id_societaire " +
                    "WHERE m.type = ? " +
                    "ORDER BY m.date DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, type);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                mouvements.add(buildMouvementFromResultSet(rs));
            }

            return mouvements;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des mouvements de type: " + type, e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Récupère les mouvements effectués par un utilisateur.
     *
     * @param idUtilisateur identifiant de l'utilisateur
     * @return liste des mouvements effectués par l'utilisateur
     * @throws SQLException si une erreur de base de données survient
     */
    public List<Mouvement> findByUtilisateur(int idUtilisateur) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Mouvement> mouvements = new ArrayList<>();

        try {
            conn = DbUtil.getConnection();

            String sql = "SELECT m.*, sc.numero, sc.nom " +
                    "FROM MOUVEMENT m " +
                    "JOIN SOCIETAIRE_COMPTE sc ON m.id_societaire = sc.id_societaire " +
                    "WHERE m.id_utilisateur = ? " +
                    "ORDER BY m.date DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idUtilisateur);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                mouvements.add(buildMouvementFromResultSet(rs));
            }

            return mouvements;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des mouvements pour l'utilisateur ID: " + idUtilisateur, e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Récupère les mouvements effectués dans une période donnée.
     *
     * @param dateDebut date de début de la période
     * @param dateFin date de fin de la période
     * @return liste des mouvements de la période
     * @throws SQLException si une erreur de base de données survient
     */
    public List<Mouvement> findByPeriode(LocalDateTime dateDebut, LocalDateTime dateFin) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Mouvement> mouvements = new ArrayList<>();

        try {
            conn = DbUtil.getConnection();

            String sql = "SELECT m.*, sc.numero, sc.nom " +
                    "FROM MOUVEMENT m " +
                    "JOIN SOCIETAIRE_COMPTE sc ON m.id_societaire = sc.id_societaire " +
                    "WHERE m.date BETWEEN ? AND ? " +
                    "ORDER BY m.date DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, Timestamp.valueOf(dateDebut));
            pstmt.setTimestamp(2, Timestamp.valueOf(dateFin));

            rs = pstmt.executeQuery();

            while (rs.next()) {
                mouvements.add(buildMouvementFromResultSet(rs));
            }

            return mouvements;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des mouvements pour la période spécifiée", e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Recherche de mouvements selon plusieurs critères.
     *
     * @param idSocietaire identifiant du compte sociétaire (peut être null)
     * @param type type de mouvement (peut être null)
     * @param dateDebut date de début de la période (peut être null)
     * @param dateFin date de fin de la période (peut être null)
     * @param montantMin montant minimum (peut être null)
     * @param montantMax montant maximum (peut être null)
     * @return liste des mouvements correspondant aux critères
     * @throws SQLException si une erreur de base de données survient
     */
    public List<Mouvement> searchMouvements(Integer idSocietaire, String type,
                                            LocalDateTime dateDebut, LocalDateTime dateFin,
                                            BigDecimal montantMin, BigDecimal montantMax) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Mouvement> mouvements = new ArrayList<>();

        try {
            conn = DbUtil.getConnection();

            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT m.*, sc.numero, sc.nom ");
            sqlBuilder.append("FROM MOUVEMENT m ");
            sqlBuilder.append("JOIN SOCIETAIRE_COMPTE sc ON m.id_societaire = sc.id_societaire ");
            sqlBuilder.append("WHERE 1=1 ");

            List<Object> params = new ArrayList<>();

            if (idSocietaire != null) {
                sqlBuilder.append("AND m.id_societaire = ? ");
                params.add(idSocietaire);
            }

            if (type != null && !type.trim().isEmpty()) {
                sqlBuilder.append("AND m.type = ? ");
                params.add(type);
            }

            if (dateDebut != null) {
                sqlBuilder.append("AND m.date >= ? ");
                params.add(Timestamp.valueOf(dateDebut));
            }

            if (dateFin != null) {
                sqlBuilder.append("AND m.date <= ? ");
                params.add(Timestamp.valueOf(dateFin));
            }

            if (montantMin != null) {
                sqlBuilder.append("AND ABS(m.montant) >= ? ");
                params.add(montantMin);
            }

            if (montantMax != null) {
                sqlBuilder.append("AND ABS(m.montant) <= ? ");
                params.add(montantMax);
            }

            sqlBuilder.append("ORDER BY m.date DESC");

            pstmt = conn.prepareStatement(sqlBuilder.toString());

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                mouvements.add(buildMouvementFromResultSet(rs));
            }

            return mouvements;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de mouvements", e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Crée un nouveau mouvement dans la base de données.
     *
     * @param mouvement mouvement à créer
     * @return identifiant du mouvement créé
     * @throws SQLException si une erreur de base de données survient
     */
    public int save(Mouvement mouvement) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();

            // Vérification de l'existence du compte sociétaire
            if (mouvement.getCompte() == null || mouvement.getCompte().getIdSocietaire() == null) {
                throw new SQLException("Le compte sociétaire n'est pas défini pour ce mouvement");
            }

            String sql = "INSERT INTO MOUVEMENT (id_societaire, date, type, montant, description, id_utilisateur) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, mouvement.getCompte().getIdSocietaire());
            pstmt.setTimestamp(2, mouvement.getDate() != null ?
                    Timestamp.valueOf(mouvement.getDate()) :
                    Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(3, mouvement.getType());
            pstmt.setBigDecimal(4, mouvement.getMontant());
            pstmt.setString(5, mouvement.getDescription());

            if (mouvement.getIdUtilisateur() != null) {
                pstmt.setInt(6, mouvement.getIdUtilisateur());
            } else {
                pstmt.setNull(6, java.sql.Types.INTEGER);
            }

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La création du mouvement a échoué, aucune ligne affectée.");
            }

            rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                int generatedId = rs.getInt(1);

                // Si c'est un dépôt ou un retrait, mettre à jour le solde du compte
                if (mouvement.estDepot() || mouvement.estRetrait() || mouvement.estMensualite()) {
                    updateSoldeCompte(conn, mouvement.getCompte().getIdSocietaire(), mouvement.getMontant());
                }

                DbUtil.commitTransaction(conn);
                return generatedId;
            } else {
                throw new SQLException("La création du mouvement a échoué, aucun ID obtenu.");
            }

        } catch (SQLException e) {
            DbUtil.rollbackTransaction(conn);
            LOGGER.log(Level.SEVERE, "Erreur lors de la création du mouvement", e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Met à jour un mouvement existant dans la base de données.
     * Note: La mise à jour d'un mouvement est restreinte pour préserver l'intégrité des données financières.
     *
     * @param mouvement mouvement à mettre à jour
     * @return true si la mise à jour a réussi
     * @throws SQLException si une erreur de base de données survient
     */
    public boolean update(Mouvement mouvement) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DbUtil.getConnection();

            // Récupérer le mouvement original pour vérifier les différences de montant
            Optional<Mouvement> originalOpt = findById(mouvement.getId());
            if (!originalOpt.isPresent()) {
                throw new SQLException("Le mouvement à mettre à jour n'existe pas");
            }

            Mouvement original = originalOpt.get();
            BigDecimal montantOriginal = original.getMontant();

            // La modification du montant ou du type nécessite une compensation du solde
            if (!original.getType().equals(mouvement.getType()) ||
                    !original.getMontant().equals(mouvement.getMontant())) {

                // Annuler l'effet du mouvement original sur le solde
                updateSoldeCompte(conn, original.getCompte().getIdSocietaire(), montantOriginal.negate());

                // Appliquer l'effet du nouveau montant sur le solde
                updateSoldeCompte(conn, mouvement.getCompte().getIdSocietaire(), mouvement.getMontant());
            }

            String sql = "UPDATE MOUVEMENT SET id_societaire = ?, date = ?, type = ?, montant = ?, description = ? " +
                    "WHERE id = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, mouvement.getCompte().getIdSocietaire());
            pstmt.setTimestamp(2, Timestamp.valueOf(mouvement.getDate()));
            pstmt.setString(3, mouvement.getType());
            pstmt.setBigDecimal(4, mouvement.getMontant());
            pstmt.setString(5, mouvement.getDescription());
            pstmt.setInt(6, mouvement.getId());

            int affectedRows = pstmt.executeUpdate();
            DbUtil.commitTransaction(conn);

            return affectedRows > 0;

        } catch (SQLException e) {
            DbUtil.rollbackTransaction(conn);
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du mouvement ID: " + mouvement.getId(), e);
            throw e;
        } finally {
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Valide un mouvement financier.
     *
     * @param idMouvement identifiant du mouvement
     * @param idValidateur identifiant de l'utilisateur validant le mouvement
     * @return true si la validation a réussi
     * @throws SQLException si une erreur de base de données survient
     */
    public boolean validerMouvement(int idMouvement, int idValidateur) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DbUtil.getConnection();

            String sql = "UPDATE MOUVEMENT SET valide = TRUE, date_validation = ?, id_validateur = ? " +
                    "WHERE id = ?";
            pstmt = conn.prepareStatement(sql);

            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(2, idValidateur);
            pstmt.setInt(3, idMouvement);

            int affectedRows = pstmt.executeUpdate();
            DbUtil.commitTransaction(conn);

            return affectedRows > 0;

        } catch (SQLException e) {
            DbUtil.rollbackTransaction(conn);
            LOGGER.log(Level.SEVERE, "Erreur lors de la validation du mouvement ID: " + idMouvement, e);
            throw e;
        } finally {
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Supprime un mouvement de la base de données et ajuste le solde du compte.
     * Note: La suppression d'un mouvement est généralement déconseillée pour préserver l'audit trail.
     *
     * @param idMouvement identifiant du mouvement à supprimer
     * @return true si la suppression a réussi
     * @throws SQLException si une erreur de base de données survient
     */
    public boolean delete(int idMouvement) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DbUtil.getConnection();

            // Récupérer les informations du mouvement avant suppression pour ajuster le solde
            Optional<Mouvement> mouvementOpt = findById(idMouvement);
            if (!mouvementOpt.isPresent()) {
                return false; // Mouvement n'existe pas
            }

            Mouvement mouvement = mouvementOpt.get();

            // Si le mouvement est déjà validé, la suppression est interdite
            if (mouvement.isValide()) {
                throw new SQLException("Impossible de supprimer un mouvement déjà validé");
            }

            // Compenser l'effet du mouvement sur le solde
            if (mouvement.getCompte() != null && mouvement.getCompte().getIdSocietaire() != null) {
                // Inverser le montant pour annuler l'effet sur le solde
                updateSoldeCompte(conn, mouvement.getCompte().getIdSocietaire(), mouvement.getMontant().negate());
            }

            String sql = "DELETE FROM MOUVEMENT WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idMouvement);

            int affectedRows = pstmt.executeUpdate();
            DbUtil.commitTransaction(conn);

            return affectedRows > 0;

        } catch (SQLException e) {
            DbUtil.rollbackTransaction(conn);
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression du mouvement ID: " + idMouvement, e);
            throw e;
        } finally {
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Calcule le solde total des mouvements pour un compte sociétaire.
     *
     * @param idSocietaire identifiant du compte sociétaire
     * @return solde total calculé à partir des mouvements
     * @throws SQLException si une erreur de base de données survient
     */
    public BigDecimal calculateSoldeFromMouvements(int idSocietaire) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();

            String sql = "SELECT SUM(montant) FROM MOUVEMENT WHERE id_societaire = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idSocietaire);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                BigDecimal solde = rs.getBigDecimal(1);
                return solde != null ? solde : BigDecimal.ZERO;
            }

            return BigDecimal.ZERO;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul du solde pour le compte sociétaire ID: " + idSocietaire, e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Met à jour le solde d'un compte sociétaire en ajoutant le montant spécifié.
     *
     * @param conn connexion à la base de données
     * @param idSocietaire identifiant du compte sociétaire
     * @param montant montant à ajouter au solde (peut être négatif)
     * @throws SQLException si une erreur de base de données survient
     */
    private void updateSoldeCompte(Connection conn, int idSocietaire, BigDecimal montant) throws SQLException {
        PreparedStatement pstmt = null;

        try {
            // Récupérer le solde actuel
            String sqlSelect = "SELECT solde FROM SOCIETAIRE_COMPTE WHERE id_societaire = ? FOR UPDATE";
            pstmt = conn.prepareStatement(sqlSelect);
            pstmt.setInt(1, idSocietaire);

            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                throw new SQLException("Compte sociétaire non trouvé pour ID: " + idSocietaire);
            }

            BigDecimal soldeActuel = rs.getBigDecimal("solde");
            BigDecimal nouveauSolde = soldeActuel.add(montant);

            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);

            // Mettre à jour le solde
            String sqlUpdate = "UPDATE SOCIETAIRE_COMPTE SET solde = ? WHERE id_societaire = ?";
            pstmt = conn.prepareStatement(sqlUpdate);
            pstmt.setBigDecimal(1, nouveauSolde);
            pstmt.setInt(2, idSocietaire);

            pstmt.executeUpdate();

        } finally {
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Génère un rapport de mouvements par type et par mois pour une année donnée.
     *
     * @param annee année pour laquelle générer le rapport
     * @return Map contenant pour chaque mois les totaux par type de mouvement
     * @throws SQLException si une erreur de base de données survient
     */
    public Map<Integer, Map<String, BigDecimal>> generateRapportAnnuel(int annee) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();

            String sql = "SELECT MONTH(date) AS mois, type, SUM(montant) AS total " +
                    "FROM MOUVEMENT " +
                    "WHERE YEAR(date) = ? " +
                    "GROUP BY MONTH(date), type " +
                    "ORDER BY MONTH(date), type";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, annee);

            rs = pstmt.executeQuery();

            Map<Integer, Map<String, BigDecimal>> rapport = new HashMap<>();

            // Initialiser le rapport pour tous les mois
            for (int i = 1; i <= 12; i++) {
                Map<String, BigDecimal> typeTotals = new HashMap<>();
                typeTotals.put(Mouvement.TYPE_DEPOT, BigDecimal.ZERO);
                typeTotals.put(Mouvement.TYPE_RETRAIT, BigDecimal.ZERO);
                typeTotals.put(Mouvement.TYPE_MENSUALITE, BigDecimal.ZERO);
                rapport.put(i, typeTotals);
            }

            while (rs.next()) {
                int mois = rs.getInt("mois");
                String type = rs.getString("type");
                BigDecimal total = rs.getBigDecimal("total");

                Map<String, BigDecimal> typeTotals = rapport.get(mois);
                if (typeTotals == null) {
                    typeTotals = new HashMap<>();
                    rapport.put(mois, typeTotals);
                }

                typeTotals.put(type, total);
            }

            return rapport;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la génération du rapport annuel pour l'année " + annee, e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Récupère les mouvements les plus récents, limités par nombre.
     *
     * @param limit nombre maximum de mouvements à récupérer
     * @return liste des mouvements les plus récents
     * @throws SQLException si une erreur de base de données survient
     */
    public List<Mouvement> findMostRecent(int limit) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Mouvement> mouvements = new ArrayList<>();

        try {
            conn = DbUtil.getConnection();

            String sql = "SELECT m.*, sc.numero, sc.nom " +
                    "FROM MOUVEMENT m " +
                    "JOIN SOCIETAIRE_COMPTE sc ON m.id_societaire = sc.id_societaire " +
                    "ORDER BY m.date DESC " +
                    "LIMIT ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                mouvements.add(buildMouvementFromResultSet(rs));
            }

            return mouvements;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des mouvements récents", e);
            throw e;
        } finally {
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(pstmt);
        }
    }

    /**
     * Construit un objet Mouvement à partir d'un ResultSet.
     *
     * @param rs ResultSet contenant les données du mouvement
     * @return objet Mouvement construit
     * @throws SQLException si une erreur de base de données survient
     */
    private Mouvement buildMouvementFromResultSet(ResultSet rs) throws SQLException {
        Mouvement mouvement = new Mouvement();
        mouvement.setId(rs.getInt("id"));

        SocieteCompte compte = new SocieteCompte();
        compte.setIdSocietaire(rs.getInt("id_societaire"));

        // Récupérer le numéro et nom du compte si disponibles
        try {
            compte.setNumero(rs.getString("numero"));
            compte.setNom(rs.getString("nom"));
        } catch (SQLException e) {
            // Ces colonnes peuvent ne pas être présentes dans certaines requêtes
            LOGGER.log(Level.FINE, "Certaines colonnes de compte ne sont pas présentes dans le ResultSet");
        }

        mouvement.setCompte(compte);
        mouvement.setType(rs.getString("type"));
        mouvement.setMontant(rs.getBigDecimal("montant"));
        mouvement.setDescription(rs.getString("description"));

        // Récupérer la date
        Timestamp date = rs.getTimestamp("date");
        if (date != null) {
            mouvement.setDate(date.toLocalDateTime());
        }

        // Récupérer les informations de validation si disponibles
        try {
            mouvement.setValide(rs.getBoolean("valide"));

            Timestamp dateValidation = rs.getTimestamp("date_validation");
            if (dateValidation != null) {
                mouvement.setDateValidation(dateValidation.toLocalDateTime());
            }

            Object idValidateur = rs.getObject("id_validateur");
            if (idValidateur != null) {
                mouvement.setIdValidateur(rs.getInt("id_validateur"));
            }
        } catch (SQLException e) {
            // Ces colonnes peuvent ne pas être présentes dans certaines versions de la base
            LOGGER.log(Level.FINE, "Certaines colonnes de validation ne sont pas présentes dans le ResultSet");
        }

        // Récupérer l'ID utilisateur si disponible
        Object idUtilisateur = rs.getObject("id_utilisateur");
        if (idUtilisateur != null) {
            mouvement.setIdUtilisateur(rs.getInt("id_utilisateur"));
        }

        return mouvement;
    }
}