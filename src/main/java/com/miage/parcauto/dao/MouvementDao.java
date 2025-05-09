package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.finance.Mouvement;
import main.java.com.miage.parcauto.model.finance.SocieteCompte;

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
 * Classe d'accès aux données pour les mouvements financiers.
 * Gère les opérations CRUD et les requêtes spécifiques liées aux mouvements financiers.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class MouvementDao {

    private static final Logger LOGGER = Logger.getLogger(MouvementDao.class.getName());

    // Instance de DbUtil pour la gestion des connexions
    private final DbUtil dbUtil;

    /**
     * Constructeur par défaut. Initialise l'instance de DbUtil.
     */
    public MouvementDao() {
        this.dbUtil = DbUtil.getInstance();
    }

    /**
     * Constructeur avec injection de dépendance pour les tests.
     *
     * @param dbUtil Instance de DbUtil à utiliser
     */
    public MouvementDao(DbUtil dbUtil) {
        this.dbUtil = dbUtil;
    }

    /**
     * Récupère tous les mouvements financiers de la base de données.
     *
     * @return Liste des mouvements financiers
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Mouvement> findAll() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Mouvement> mouvements = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT m.*, sc.nom, sc.numero " +
                    "FROM MOUVEMENT m " +
                    "JOIN SOCIETAIRE_COMPTE sc ON m.id_societaire = sc.id_societaire " +
                    "ORDER BY m.date DESC";

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                mouvements.add(extractMouvementFromResultSet(rs));
            }

            return mouvements;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les mouvements financiers", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère un mouvement financier par son ID.
     *
     * @param id ID du mouvement à récupérer
     * @return Optional contenant le mouvement s'il existe, vide sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Optional<Mouvement> findById(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT m.*, sc.nom, sc.numero " +
                    "FROM MOUVEMENT m " +
                    "JOIN SOCIETAIRE_COMPTE sc ON m.id_societaire = sc.id_societaire " +
                    "WHERE m.id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(extractMouvementFromResultSet(rs));
            }

            return Optional.empty();

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du mouvement par ID: " + id, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère les mouvements d'un compte sociétaire.
     *
     * @param idSocietaire ID du compte sociétaire
     * @return Liste des mouvements du compte
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Mouvement> findBySocietaire(int idSocietaire) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Mouvement> mouvements = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT m.*, sc.nom, sc.numero " +
                    "FROM MOUVEMENT m " +
                    "JOIN SOCIETAIRE_COMPTE sc ON m.id_societaire = sc.id_societaire " +
                    "WHERE m.id_societaire = ? " +
                    "ORDER BY m.date DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idSocietaire);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                mouvements.add(extractMouvementFromResultSet(rs));
            }

            return mouvements;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des mouvements pour le sociétaire ID: " + idSocietaire, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère les mouvements d'un compte sociétaire par période.
     *
     * @param idSocietaire ID du compte sociétaire
     * @param debut Date de début de la période
     * @param fin Date de fin de la période
     * @return Liste des mouvements du compte dans la période spécifiée
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Mouvement> findBySocietaireAndPeriode(int idSocietaire, LocalDateTime debut, LocalDateTime fin) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Mouvement> mouvements = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT m.*, sc.nom, sc.numero " +
                    "FROM MOUVEMENT m " +
                    "JOIN SOCIETAIRE_COMPTE sc ON m.id_societaire = sc.id_societaire " +
                    "WHERE m.id_societaire = ? AND m.date BETWEEN ? AND ? " +
                    "ORDER BY m.date DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idSocietaire);
            pstmt.setTimestamp(2, Timestamp.valueOf(debut));
            pstmt.setTimestamp(3, Timestamp.valueOf(fin));

            rs = pstmt.executeQuery();

            while (rs.next()) {
                mouvements.add(extractMouvementFromResultSet(rs));
            }

            return mouvements;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des mouvements pour le sociétaire ID: " + idSocietaire + " dans la période", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère les mouvements par type.
     *
     * @param type Type de mouvement (Depot, Retrait, Mensualite)
     * @return Liste des mouvements du type spécifié
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Mouvement> findByType(Mouvement.TypeMouvement type) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Mouvement> mouvements = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT m.*, sc.nom, sc.numero " +
                    "FROM MOUVEMENT m " +
                    "JOIN SOCIETAIRE_COMPTE sc ON m.id_societaire = sc.id_societaire " +
                    "WHERE m.type = ? " +
                    "ORDER BY m.date DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, type.name());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                mouvements.add(extractMouvementFromResultSet(rs));
            }

            return mouvements;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des mouvements par type: " + type, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }
    public List<Mouvement> findBySocieteCompteAndType(int idSocietaire, Mouvement.TypeMouvement type) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Mouvement> mouvements = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT m.*, sc.nom, sc.numero " +
                    "FROM MOUVEMENT m " +
                    "JOIN SOCIETAIRE_COMPTE sc ON m.id_societaire = sc.id_societaire " +
                    "WHERE m.id_societaire = ? AND m.type = ? " +
                    "ORDER BY m.date DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idSocietaire);
            pstmt.setString(2, type.name());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                mouvements.add(extractMouvementFromResultSet(rs));
            }

            return mouvements;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des mouvements pour le sociétaire ID: " +
                    idSocietaire + " et type: " + type, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }
    /**
     * Récupère les mouvements par période.
     *
     * @param debut Date de début de la période
     * @param fin Date de fin de la période
     * @return Liste des mouvements dans la période spécifiée
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Mouvement> findByPeriode(LocalDateTime debut, LocalDateTime fin) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Mouvement> mouvements = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT m.*, sc.nom, sc.numero " +
                    "FROM MOUVEMENT m " +
                    "JOIN SOCIETAIRE_COMPTE sc ON m.id_societaire = sc.id_societaire " +
                    "WHERE m.date BETWEEN ? AND ? " +
                    "ORDER BY m.date DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, Timestamp.valueOf(debut));
            pstmt.setTimestamp(2, Timestamp.valueOf(fin));

            rs = pstmt.executeQuery();

            while (rs.next()) {
                mouvements.add(extractMouvementFromResultSet(rs));
            }

            return mouvements;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des mouvements par période", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Crée un nouveau mouvement financier dans la base de données.
     *
     * @param mouvement Le mouvement financier à créer
     * @return Le mouvement créé avec son ID généré
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Mouvement create(Mouvement mouvement) throws SQLException {
        if (mouvement.getMontant() == null || mouvement.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant du mouvement doit être positif");
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "INSERT INTO MOUVEMENT (id_societaire, date, type, montant, description) " +
                    "VALUES (?, ?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, mouvement.getIdSocietaire());

            LocalDateTime dateOperation = mouvement.getDate();
            if (dateOperation == null) {
                dateOperation = LocalDateTime.now();
                mouvement.setDate(dateOperation);
            }
            pstmt.setTimestamp(2, Timestamp.valueOf(dateOperation));

            pstmt.setString(3, mouvement.getType().name());
            pstmt.setBigDecimal(4, mouvement.getMontant());
            pstmt.setString(5, mouvement.getDescription());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La création du mouvement a échoué, aucune ligne affectée.");
            }

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                mouvement.setId(rs.getInt(1));
            } else {
                throw new SQLException("La création du mouvement a échoué, aucun ID généré.");
            }

            return mouvement;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création du mouvement", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Met à jour un mouvement financier dans la base de données.
     *
     * @param mouvement Le mouvement financier à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean update(Mouvement mouvement) throws SQLException {
        if (mouvement.getId() == null) {
            throw new IllegalArgumentException("L'ID du mouvement ne peut pas être null pour une mise à jour");
        }

        if (mouvement.getMontant() == null || mouvement.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant du mouvement doit être positif");
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "UPDATE MOUVEMENT SET id_societaire = ?, date = ?, type = ?, montant = ?, description = ? " +
                    "WHERE id = ?";

            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, mouvement.getIdSocietaire());
            pstmt.setTimestamp(2, Timestamp.valueOf(mouvement.getDate()));
            pstmt.setString(3, mouvement.getType().name());
            pstmt.setBigDecimal(4, mouvement.getMontant());
            pstmt.setString(5, mouvement.getDescription());

            // ID du mouvement à mettre à jour
            pstmt.setInt(6, mouvement.getId());

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du mouvement ID: " + mouvement.getId(), ex);
            throw ex;
        } finally {
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Supprime un mouvement financier de la base de données.
     *
     * @param id ID du mouvement à supprimer
     * @return true si la suppression a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean delete(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "DELETE FROM MOUVEMENT WHERE id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression du mouvement ID: " + id, ex);
            throw ex;
        } finally {
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Calcule le total des dépôts pour un compte sociétaire.
     *
     * @param idSocietaire ID du compte sociétaire
     * @return Total des dépôts
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public BigDecimal calculateTotalDepots(int idSocietaire) throws SQLException {
        return calculateTotalByType(idSocietaire, Mouvement.TypeMouvement.Depot);
    }

    /**
     * Calcule le total des retraits pour un compte sociétaire.
     *
     * @param idSocietaire ID du compte sociétaire
     * @return Total des retraits
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public BigDecimal calculateTotalRetraits(int idSocietaire) throws SQLException {
        return calculateTotalByType(idSocietaire, Mouvement.TypeMouvement.Retrait);
    }

    /**
     * Calcule le total des mensualités pour un compte sociétaire.
     *
     * @param idSocietaire ID du compte sociétaire
     * @return Total des mensualités
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public BigDecimal calculateTotalMensualites(int idSocietaire) throws SQLException {
        return calculateTotalByType(idSocietaire, Mouvement.TypeMouvement.Mensualite);
    }

    /**
     * Calcule le total des mouvements d'un type spécifique pour un compte sociétaire.
     *
     * @param idSocietaire ID du compte sociétaire
     * @param type Type de mouvement
     * @return Total des mouvements du type spécifié
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    private BigDecimal calculateTotalByType(int idSocietaire, Mouvement.TypeMouvement type) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT SUM(montant) AS total FROM MOUVEMENT " +
                    "WHERE id_societaire = ? AND type = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idSocietaire);
            pstmt.setString(2, type.name());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total");
                return total != null ? total : BigDecimal.ZERO;
            }

            return BigDecimal.ZERO;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul du total des mouvements de type " + type +
                    " pour le sociétaire ID: " + idSocietaire, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Calcule les totaux des mouvements par type pour un compte sociétaire.
     *
     * @param idSocietaire ID du compte sociétaire
     * @return Map contenant les totaux par type de mouvement
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Map<Mouvement.TypeMouvement, BigDecimal> calculateTotauxParType(int idSocietaire) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Map<Mouvement.TypeMouvement, BigDecimal> totaux = new HashMap<>();

        // Initialisation des totaux à zéro
        for (Mouvement.TypeMouvement type : Mouvement.TypeMouvement.values()) {
            totaux.put(type, BigDecimal.ZERO);
        }

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT type, SUM(montant) AS total FROM MOUVEMENT " +
                    "WHERE id_societaire = ? " +
                    "GROUP BY type";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idSocietaire);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                String typeStr = rs.getString("type");
                BigDecimal total = rs.getBigDecimal("total");
                Mouvement.TypeMouvement type = Mouvement.TypeMouvement.valueOf(typeStr);
                totaux.put(type, total);
            }

            return totaux;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul des totaux par type pour le sociétaire ID: " + idSocietaire, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Calcule les totaux mensuels des mouvements par type pour une année donnée.
     *
     * @param annee Année pour laquelle calculer les statistiques
     * @return Map contenant les totaux mensuels par type de mouvement
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Map<Integer, Map<Mouvement.TypeMouvement, BigDecimal>> calculateStatsMensuelles(int annee) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Map<Integer, Map<Mouvement.TypeMouvement, BigDecimal>> statsMensuelles = new HashMap<>();

        // Initialisation des statistiques mensuelles
        for (int mois = 1; mois <= 12; mois++) {
            Map<Mouvement.TypeMouvement, BigDecimal> totauxMois = new HashMap<>();
            for (Mouvement.TypeMouvement type : Mouvement.TypeMouvement.values()) {
                totauxMois.put(type, BigDecimal.ZERO);
            }
            statsMensuelles.put(mois, totauxMois);
        }

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT MONTH(date) AS mois, type, SUM(montant) AS total FROM MOUVEMENT " +
                    "WHERE YEAR(date) = ? " +
                    "GROUP BY MONTH(date), type " +
                    "ORDER BY MONTH(date), type";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, annee);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                int mois = rs.getInt("mois");
                String typeStr = rs.getString("type");
                BigDecimal total = rs.getBigDecimal("total");

                Mouvement.TypeMouvement type = Mouvement.TypeMouvement.valueOf(typeStr);

                // Mise à jour des statistiques pour le mois et le type
                Map<Mouvement.TypeMouvement, BigDecimal> totauxMois = statsMensuelles.get(mois);
                totauxMois.put(type, total);
            }

            return statsMensuelles;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul des statistiques mensuelles pour l'année: " + annee, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère les mouvements pour une période de rapport spécifique.
     *
     * @param debut Date de début de la période
     * @param fin Date de fin de la période
     * @param typesFiltre Types de mouvement à inclure (null pour tous)
     * @return Liste des mouvements correspondant aux critères
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Mouvement> getReportData(LocalDateTime debut, LocalDateTime fin, List<Mouvement.TypeMouvement> typesFiltre)
            throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Mouvement> mouvements = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT m.*, sc.nom, sc.numero ")
                    .append("FROM MOUVEMENT m ")
                    .append("JOIN SOCIETAIRE_COMPTE sc ON m.id_societaire = sc.id_societaire ")
                    .append("WHERE m.date BETWEEN ? AND ? ");

            if (typesFiltre != null && !typesFiltre.isEmpty()) {
                sql.append("AND m.type IN (");
                for (int i = 0; i < typesFiltre.size(); i++) {
                    if (i > 0) {
                        sql.append(", ");
                    }
                    sql.append("?");
                }
                sql.append(") ");
            }

            sql.append("ORDER BY m.date DESC");

            pstmt = conn.prepareStatement(sql.toString());

            int paramIndex = 1;
            pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(debut));
            pstmt.setTimestamp(paramIndex++, Timestamp.valueOf(fin));

            if (typesFiltre != null && !typesFiltre.isEmpty()) {
                for (Mouvement.TypeMouvement type : typesFiltre) {
                    pstmt.setString(paramIndex++, type.name());
                }
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                mouvements.add(extractMouvementFromResultSet(rs));
            }

            return mouvements;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des données de rapport", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Calcule le bilan financier (entrées vs sorties) pour une période donnée.
     *
     * @param debut Date de début de la période
     * @param fin Date de fin de la période
     * @return Objet BilanFinancier contenant les statistiques de la période
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public BilanFinancier calculateBilanFinancier(LocalDateTime debut, LocalDateTime fin) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT " +
                    "SUM(CASE WHEN type = 'Depot' THEN montant ELSE 0 END) AS total_depots, " +
                    "SUM(CASE WHEN type = 'Retrait' THEN montant ELSE 0 END) AS total_retraits, " +
                    "SUM(CASE WHEN type = 'Mensualite' THEN montant ELSE 0 END) AS total_mensualites, " +
                    "COUNT(CASE WHEN type = 'Depot' THEN 1 END) AS nb_depots, " +
                    "COUNT(CASE WHEN type = 'Retrait' THEN 1 END) AS nb_retraits, " +
                    "COUNT(CASE WHEN type = 'Mensualite' THEN 1 END) AS nb_mensualites " +
                    "FROM MOUVEMENT " +
                    "WHERE date BETWEEN ? AND ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, Timestamp.valueOf(debut));
            pstmt.setTimestamp(2, Timestamp.valueOf(fin));

            rs = pstmt.executeQuery();

            if (rs.next()) {
                BigDecimal totalDepots = rs.getBigDecimal("total_depots");
                BigDecimal totalRetraits = rs.getBigDecimal("total_retraits");
                BigDecimal totalMensualites = rs.getBigDecimal("total_mensualites");
                int nbDepots = rs.getInt("nb_depots");
                int nbRetraits = rs.getInt("nb_retraits");
                int nbMensualites = rs.getInt("nb_mensualites");

                totalDepots = totalDepots != null ? totalDepots : BigDecimal.ZERO;
                totalRetraits = totalRetraits != null ? totalRetraits : BigDecimal.ZERO;
                totalMensualites = totalMensualites != null ? totalMensualites : BigDecimal.ZERO;

                BilanFinancier bilan = new BilanFinancier(
                        totalDepots, totalRetraits, totalMensualites,
                        nbDepots, nbRetraits, nbMensualites,
                        debut, fin
                );

                return bilan;
            }

            // Si aucun résultat, retourner un bilan financier vide
            return new BilanFinancier(
                    BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                    0, 0, 0,
                    debut, fin
            );

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul du bilan financier", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Extrait un objet Mouvement à partir d'un ResultSet.
     *
     * @param rs ResultSet contenant les données du mouvement
     * @return Objet Mouvement créé à partir des données
     * @throws SQLException En cas d'erreur d'accès aux données du ResultSet
     */
    private Mouvement extractMouvementFromResultSet(ResultSet rs) throws SQLException {
        Mouvement mouvement = new Mouvement();

        mouvement.setId(rs.getInt("id"));
        mouvement.setIdSocietaire(rs.getInt("id_societaire"));

        Timestamp date = rs.getTimestamp("date");
        if (date != null) {
            mouvement.setDate(date.toLocalDateTime());
        }

        String typeStr = rs.getString("type");
        mouvement.setType(Mouvement.TypeMouvement.valueOf(typeStr));

        mouvement.setMontant(rs.getBigDecimal("montant"));
        mouvement.setDescription(rs.getString("description"));

        // Informations du compte sociétaire (si disponibles)
        try {
            String nomSocietaire = rs.getString("nom");
            String numeroCompte = rs.getString("numero");

            if (nomSocietaire != null && numeroCompte != null) {
                SocieteCompte compte = new SocieteCompte();
                compte.setIdSocietaire(mouvement.getIdSocietaire());
                compte.setNom(nomSocietaire);
                compte.setNumero(numeroCompte);

                mouvement.setCompte(compte);  // CORRECTION: utilisez setCompte() au lieu de setSocieteCompte()
            }
        } catch (SQLException ex) {
            // Ignorer si les colonnes du compte sociétaire ne sont pas disponibles
        }

        return mouvement;
    }

    /**
     * Classe interne représentant un bilan financier pour une période donnée.
     */
    public static class BilanFinancier {
        private final BigDecimal totalDepots;
        private final BigDecimal totalRetraits;
        private final BigDecimal totalMensualites;
        private final int nbDepots;
        private final int nbRetraits;
        private final int nbMensualites;
        private final LocalDateTime dateDebut;
        private final LocalDateTime dateFin;

        /**
         * Constructeur pour un bilan financier.
         *
         * @param totalDepots Total des dépôts
         * @param totalRetraits Total des retraits
         * @param totalMensualites Total des mensualités
         * @param nbDepots Nombre de dépôts
         * @param nbRetraits Nombre de retraits
         * @param nbMensualites Nombre de mensualités
         * @param dateDebut Date de début de la période
         * @param dateFin Date de fin de la période
         */
        public BilanFinancier(BigDecimal totalDepots, BigDecimal totalRetraits, BigDecimal totalMensualites,
                              int nbDepots, int nbRetraits, int nbMensualites,
                              LocalDateTime dateDebut, LocalDateTime dateFin) {
            this.totalDepots = totalDepots;
            this.totalRetraits = totalRetraits;
            this.totalMensualites = totalMensualites;
            this.nbDepots = nbDepots;
            this.nbRetraits = nbRetraits;
            this.nbMensualites = nbMensualites;
            this.dateDebut = dateDebut;
            this.dateFin = dateFin;
        }

        /**
         * @return Total des dépôts
         */
        public BigDecimal getTotalDepots() {
            return totalDepots;
        }

        /**
         * @return Total des retraits
         */
        public BigDecimal getTotalRetraits() {
            return totalRetraits;
        }

        /**
         * @return Total des mensualités
         */
        public BigDecimal getTotalMensualites() {
            return totalMensualites;
        }

        /**
         * @return Nombre de dépôts
         */
        public int getNbDepots() {
            return nbDepots;
        }

        /**
         * @return Nombre de retraits
         */
        public int getNbRetraits() {
            return nbRetraits;
        }

        /**
         * @return Nombre de mensualités
         */
        public int getNbMensualites() {
            return nbMensualites;
        }

        /**
         * @return Date de début de la période
         */
        public LocalDateTime getDateDebut() {
            return dateDebut;
        }

        /**
         * @return Date de fin de la période
         */
        public LocalDateTime getDateFin() {
            return dateFin;
        }

        /**
         * @return Nombre total de transactions (dépôts + retraits + mensualités)
         */
        public int getTotalTransactions() {
            return nbDepots + nbRetraits + nbMensualites;
        }

        /**
         * @return Bilan net (dépôts - retraits - mensualités)
         */
        public BigDecimal getBilanNet() {
            return totalDepots.subtract(totalRetraits).subtract(totalMensualites);
        }

        /**
         * @return Total des sorties (retraits + mensualités)
         */
        public BigDecimal getTotalSorties() {
            return totalRetraits.add(totalMensualites);
        }
    }
}