package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.finance.SocieteCompte;
import main.java.com.miage.parcauto.model.finance.Mouvement;
import main.java.com.miage.parcauto.model.mission.Mission;
import main.java.com.miage.parcauto.model.entretien.Entretien;
import main.java.com.miage.parcauto.model.entretien.Assurance;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe d'accès aux données pour les opérations financières avancées.
 * Fournit des fonctionnalités d'analyse financière, de reporting et de calcul de coûts.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class FinanceDao {

    private static final Logger LOGGER = Logger.getLogger(FinanceDao.class.getName());

    private final DbUtil dbUtil;
    private final SocieteCompteDao societeCompteDao;
    private final MouvementDao mouvementDao;
    private final EntretienDao entretienDao;
    private final MissionDao missionDao;

    // Constantes de calcul
    private static final double TAUX_AMORTISSEMENT_ANNUEL = 0.2; // 20% par an
    private static final int DUREE_AMORTISSEMENT_ANNEES = 5;     // 5 ans

    /**
     * Constructeur par défaut. Initialise les instances des différents DAOs.
     */
    public FinanceDao() {
        this.dbUtil = DbUtil.getInstance();
        this.societeCompteDao = new SocieteCompteDao();
        this.mouvementDao = new MouvementDao();
        this.entretienDao = new EntretienDao();
        this.missionDao = new MissionDao();
    }

    /**
     * Constructeur avec injection de dépendance pour les tests.
     *
     * @param dbUtil Instance de DbUtil à utiliser
     * @param societeCompteDao Instance de SocieteCompteDao à utiliser
     * @param mouvementDao Instance de MouvementDao à utiliser
     * @param entretienDao Instance de EntretienDao à utiliser
     * @param missionDao Instance de MissionDao à utiliser
     */
    public FinanceDao(DbUtil dbUtil, SocieteCompteDao societeCompteDao, MouvementDao mouvementDao,
                      EntretienDao entretienDao, MissionDao missionDao) {
        this.dbUtil = dbUtil;
        this.societeCompteDao = societeCompteDao;
        this.mouvementDao = mouvementDao;
        this.entretienDao = entretienDao;
        this.missionDao = missionDao;
    }

    /**
     * Calcule le total des recettes (dépôts) pour une période donnée.
     *
     * @param debut Date de début de la période
     * @param fin Date de fin de la période
     * @return Total des recettes
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public BigDecimal calculerRecettesPeriode(LocalDateTime debut, LocalDateTime fin) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT SUM(montant) AS total FROM MOUVEMENT " +
                    "WHERE type = 'Depot' AND date BETWEEN ? AND ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, Timestamp.valueOf(debut));
            pstmt.setTimestamp(2, Timestamp.valueOf(fin));

            rs = pstmt.executeQuery();

            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total");
                return total != null ? total : BigDecimal.ZERO;
            }

            return BigDecimal.ZERO;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul des recettes pour la période", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Calcule le total des dépenses (missions, entretiens, assurances) pour une période donnée.
     *
     * @param debut Date de début de la période
     * @param fin Date de fin de la période
     * @return Total des dépenses
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public BigDecimal calculerDepensesPeriode(LocalDateTime debut, LocalDateTime fin) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT " +
                    "COALESCE(SUM(m.cout_total), 0) + COALESCE(SUM(e.cout_entr), 0) + COALESCE(SUM(a.cout_assurance), 0) AS total_depenses " +
                    "FROM " +
                    "(SELECT 0) as dummy " +
                    "LEFT JOIN MISSION m ON (m.date_debut_mission BETWEEN ? AND ? OR m.date_fin_mission BETWEEN ? AND ?) " +
                    "LEFT JOIN ENTRETIEN e ON (e.date_entree_entr BETWEEN ? AND ? OR e.date_sortie_entr BETWEEN ? AND ?) " +
                    "LEFT JOIN COUVRIR c ON 1=1 " +
                    "LEFT JOIN ASSURANCE a ON c.num_carte_assurance = a.num_carte_assurance AND " +
                    "(a.date_debut_assurance BETWEEN ? AND ? OR a.date_fin_assurance BETWEEN ? AND ?)";

            pstmt = conn.prepareStatement(sql);

            // Paramètres pour les missions
            pstmt.setTimestamp(1, Timestamp.valueOf(debut));
            pstmt.setTimestamp(2, Timestamp.valueOf(fin));
            pstmt.setTimestamp(3, Timestamp.valueOf(debut));
            pstmt.setTimestamp(4, Timestamp.valueOf(fin));

            // Paramètres pour les entretiens
            pstmt.setTimestamp(5, Timestamp.valueOf(debut));
            pstmt.setTimestamp(6, Timestamp.valueOf(fin));
            pstmt.setTimestamp(7, Timestamp.valueOf(debut));
            pstmt.setTimestamp(8, Timestamp.valueOf(fin));

            // Paramètres pour les assurances
            pstmt.setTimestamp(9, Timestamp.valueOf(debut));
            pstmt.setTimestamp(10, Timestamp.valueOf(fin));
            pstmt.setTimestamp(11, Timestamp.valueOf(debut));
            pstmt.setTimestamp(12, Timestamp.valueOf(fin));

            rs = pstmt.executeQuery();

            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total_depenses");
                return total != null ? total : BigDecimal.ZERO;
            }

            return BigDecimal.ZERO;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul des dépenses pour la période", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Calcule le bilan financier (recettes - dépenses) pour une période donnée.
     *
     * @param debut Date de début de la période
     * @param fin Date de fin de la période
     * @return BilanFinancier contenant le détail des recettes et dépenses
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public BilanFinancier calculerBilanFinancier(LocalDateTime debut, LocalDateTime fin) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            // Récupération des recettes
            BigDecimal totalRecettes = calculerRecettesPeriode(debut, fin);

            // Récupération des dépenses par catégorie
            String sql = "SELECT " +
                    "COALESCE(SUM(m.cout_total), 0) AS total_missions, " +
                    "COALESCE(SUM(e.cout_entr), 0) AS total_entretiens, " +
                    "COALESCE(SUM(a.cout_assurance), 0) AS total_assurances " +
                    "FROM " +
                    "(SELECT 0) as dummy " +
                    "LEFT JOIN MISSION m ON (m.date_debut_mission BETWEEN ? AND ? OR m.date_fin_mission BETWEEN ? AND ?) " +
                    "LEFT JOIN ENTRETIEN e ON (e.date_entree_entr BETWEEN ? AND ? OR e.date_sortie_entr BETWEEN ? AND ?) " +
                    "LEFT JOIN COUVRIR c ON 1=1 " +
                    "LEFT JOIN ASSURANCE a ON c.num_carte_assurance = a.num_carte_assurance AND " +
                    "(a.date_debut_assurance BETWEEN ? AND ? OR a.date_fin_assurance BETWEEN ? AND ?)";

            pstmt = conn.prepareStatement(sql);

            // Paramètres pour les missions
            pstmt.setTimestamp(1, Timestamp.valueOf(debut));
            pstmt.setTimestamp(2, Timestamp.valueOf(fin));
            pstmt.setTimestamp(3, Timestamp.valueOf(debut));
            pstmt.setTimestamp(4, Timestamp.valueOf(fin));

            // Paramètres pour les entretiens
            pstmt.setTimestamp(5, Timestamp.valueOf(debut));
            pstmt.setTimestamp(6, Timestamp.valueOf(fin));
            pstmt.setTimestamp(7, Timestamp.valueOf(debut));
            pstmt.setTimestamp(8, Timestamp.valueOf(fin));

            // Paramètres pour les assurances
            pstmt.setTimestamp(9, Timestamp.valueOf(debut));
            pstmt.setTimestamp(10, Timestamp.valueOf(fin));
            pstmt.setTimestamp(11, Timestamp.valueOf(debut));
            pstmt.setTimestamp(12, Timestamp.valueOf(fin));

            rs = pstmt.executeQuery();

            BigDecimal totalMissions = BigDecimal.ZERO;
            BigDecimal totalEntretiens = BigDecimal.ZERO;
            BigDecimal totalAssurances = BigDecimal.ZERO;

            if (rs.next()) {
                totalMissions = rs.getBigDecimal("total_missions");
                totalEntretiens = rs.getBigDecimal("total_entretiens");
                totalAssurances = rs.getBigDecimal("total_assurances");

                totalMissions = totalMissions != null ? totalMissions : BigDecimal.ZERO;
                totalEntretiens = totalEntretiens != null ? totalEntretiens : BigDecimal.ZERO;
                totalAssurances = totalAssurances != null ? totalAssurances : BigDecimal.ZERO;
            }

            // Calcul du bilan
            BigDecimal totalDepenses = totalMissions.add(totalEntretiens).add(totalAssurances);
            BigDecimal solde = totalRecettes.subtract(totalDepenses);

            return new BilanFinancier(totalRecettes, totalMissions, totalEntretiens, totalAssurances, solde, debut, fin);

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul du bilan financier pour la période", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Calcule l'évolution des recettes et dépenses mensuelles sur une période donnée.
     *
     * @param annee Année pour l'analyse
     * @return Map contenant les données d'évolution mensuelles
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Map<Month, BilanMensuel> calculerEvolutionMensuelle(int annee) throws SQLException {
        Map<Month, BilanMensuel> evolution = new TreeMap<>();

        for (Month mois : Month.values()) {
            // Initialiser avec des valeurs par défaut
            evolution.put(mois, new BilanMensuel(BigDecimal.ZERO, BigDecimal.ZERO));
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            // Récupération des recettes mensuelles (dépôts)
            String sqlRecettes = "SELECT MONTH(date) as mois, SUM(montant) as total " +
                    "FROM MOUVEMENT " +
                    "WHERE type = 'Depot' AND YEAR(date) = ? " +
                    "GROUP BY MONTH(date)";

            pstmt = conn.prepareStatement(sqlRecettes);
            pstmt.setInt(1, annee);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                int moisIndex = rs.getInt("mois");
                BigDecimal totalRecettes = rs.getBigDecimal("total");
                Month mois = Month.of(moisIndex);

                BilanMensuel bilan = evolution.get(mois);
                bilan.setRecettes(totalRecettes);
            }

            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);

            // Récupération des dépenses mensuelles (missions, entretiens, assurances)
            String sqlDepensesMissions = "SELECT MONTH(COALESCE(date_fin_mission, date_debut_mission)) as mois, " +
                    "SUM(cout_total) as total " +
                    "FROM MISSION " +
                    "WHERE YEAR(COALESCE(date_fin_mission, date_debut_mission)) = ? " +
                    "GROUP BY MONTH(COALESCE(date_fin_mission, date_debut_mission))";

            pstmt = conn.prepareStatement(sqlDepensesMissions);
            pstmt.setInt(1, annee);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                int moisIndex = rs.getInt("mois");
                BigDecimal totalMissions = rs.getBigDecimal("total");
                if (totalMissions != null && moisIndex > 0 && moisIndex <= 12) {
                    Month mois = Month.of(moisIndex);

                    BilanMensuel bilan = evolution.get(mois);
                    bilan.ajouterDepenses(totalMissions);
                }
            }

            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);

            String sqlDepensesEntretiens = "SELECT MONTH(COALESCE(date_sortie_entr, date_entree_entr)) as mois, " +
                    "SUM(cout_entr) as total " +
                    "FROM ENTRETIEN " +
                    "WHERE YEAR(COALESCE(date_sortie_entr, date_entree_entr)) = ? " +
                    "GROUP BY MONTH(COALESCE(date_sortie_entr, date_entree_entr))";

            pstmt = conn.prepareStatement(sqlDepensesEntretiens);
            pstmt.setInt(1, annee);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                int moisIndex = rs.getInt("mois");
                BigDecimal totalEntretiens = rs.getBigDecimal("total");
                if (totalEntretiens != null && moisIndex > 0 && moisIndex <= 12) {
                    Month mois = Month.of(moisIndex);

                    BilanMensuel bilan = evolution.get(mois);
                    bilan.ajouterDepenses(totalEntretiens);
                }
            }

            return evolution;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul de l'évolution mensuelle pour l'année " + annee, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Calcule le coût total de possession (TCO) pour un véhicule.
     * Inclut l'amortissement, les coûts d'entretien, d'assurance et de mission.
     *
     * @param idVehicule ID du véhicule
     * @return Coût total de possession
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public TCOVehicule calculerCoutTotalPossession(int idVehicule) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            // Récupération des données du véhicule avec la vue TCO
            String sql = "SELECT v.id_vehicule, v.prix_vehicule, v.date_acquisition, v.km_actuels, " +
                    "v.marque, v.modele, v.immatriculation, " +
                    "tco.couts_totaux " +
                    "FROM VEHICULES v " +
                    "LEFT JOIN v_TCO tco ON v.id_vehicule = tco.id_vehicule " +
                    "WHERE v.id_vehicule = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idVehicule);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                BigDecimal prixAchat = rs.getBigDecimal("prix_vehicule");
                Timestamp dateAcquisition = rs.getTimestamp("date_acquisition");
                int kmActuels = rs.getInt("km_actuels");
                String marque = rs.getString("marque");
                String modele = rs.getString("modele");
                String immatriculation = rs.getString("immatriculation");
                BigDecimal coutsOperationnels = rs.getBigDecimal("couts_totaux");

                prixAchat = prixAchat != null ? prixAchat : BigDecimal.ZERO;
                coutsOperationnels = coutsOperationnels != null ? coutsOperationnels : BigDecimal.ZERO;

                // Calcul de l'amortissement
                BigDecimal valeurResiduelle;
                double tauxAmortissement;

                if (dateAcquisition != null) {
                    LocalDateTime dateAcq = dateAcquisition.toLocalDateTime();
                    LocalDateTime now = LocalDateTime.now();
                    int anneesEcoulees = now.getYear() - dateAcq.getYear();

                    if (anneesEcoulees >= DUREE_AMORTISSEMENT_ANNEES) {
                        valeurResiduelle = prixAchat.multiply(BigDecimal.valueOf(0.1)); // 10% de la valeur initiale
                        tauxAmortissement = 1.0 - 0.1; // 90% amorti
                    } else {
                        tauxAmortissement = TAUX_AMORTISSEMENT_ANNUEL * anneesEcoulees;
                        valeurResiduelle = prixAchat.multiply(BigDecimal.valueOf(1 - tauxAmortissement));
                    }
                } else {
                    valeurResiduelle = prixAchat;
                    tauxAmortissement = 0;
                }

                BigDecimal amortissement = prixAchat.subtract(valeurResiduelle);
                BigDecimal tco = amortissement.add(coutsOperationnels);

                BigDecimal coutParKm;
                if (kmActuels > 0) {
                    coutParKm = tco.divide(BigDecimal.valueOf(kmActuels), 2, RoundingMode.HALF_UP);
                } else {
                    coutParKm = BigDecimal.ZERO;
                }

                return new TCOVehicule(idVehicule, marque, modele, immatriculation, prixAchat, valeurResiduelle,
                        amortissement, coutsOperationnels, tco, coutParKm, kmActuels);
            }

            throw new SQLException("Véhicule non trouvé avec l'ID: " + idVehicule);

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul du coût total de possession pour le véhicule ID: " + idVehicule, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Calcule les répartitions budgétaires par type de dépense.
     *
     * @param annee Année pour l'analyse
     * @return Map contenant les montants par type de dépense
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Map<String, BigDecimal> calculerRepartitionBudgetaire(int annee) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Map<String, BigDecimal> repartition = new LinkedHashMap<>();

        // Initialiser avec des valeurs par défaut
        repartition.put("Missions", BigDecimal.ZERO);
        repartition.put("Entretiens", BigDecimal.ZERO);
        repartition.put("Assurances", BigDecimal.ZERO);

        try {
            conn = dbUtil.getConnection();

            // Dépenses de missions
            String sqlMissions = "SELECT SUM(cout_total) AS total FROM MISSION " +
                    "WHERE YEAR(COALESCE(date_fin_mission, date_debut_mission)) = ?";

            pstmt = conn.prepareStatement(sqlMissions);
            pstmt.setInt(1, annee);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total");
                if (total != null) {
                    repartition.put("Missions", total);
                }
            }

            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);

            // Dépenses d'entretiens
            String sqlEntretiens = "SELECT SUM(cout_entr) AS total FROM ENTRETIEN " +
                    "WHERE YEAR(COALESCE(date_sortie_entr, date_entree_entr)) = ?";

            pstmt = conn.prepareStatement(sqlEntretiens);
            pstmt.setInt(1, annee);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total");
                if (total != null) {
                    repartition.put("Entretiens", total);
                }
            }

            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);

            // Dépenses d'assurances
            String sqlAssurances = "SELECT SUM(cout_assurance) AS total FROM ASSURANCE " +
                    "WHERE YEAR(COALESCE(date_fin_assurance, date_debut_assurance)) = ?";

            pstmt = conn.prepareStatement(sqlAssurances);
            pstmt.setInt(1, annee);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total");
                if (total != null) {
                    repartition.put("Assurances", total);
                }
            }

            return repartition;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul de la répartition budgétaire pour l'année " + annee, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Génère un rapport de rentabilité des véhicules.
     *
     * @param annee Année pour le rapport
     * @return Liste des rentabilités des véhicules
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<RentabiliteVehicule> genererRapportRentabilite(int annee) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<RentabiliteVehicule> rapport = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            // Récupération des données de tous les véhicules
            String sql = "SELECT v.id_vehicule, v.immatriculation, v.marque, v.modele, v.prix_vehicule, " +
                    "v.date_acquisition, v.km_actuels, " +

                    // Coûts d'entretien pour l'année
                    "(SELECT SUM(e.cout_entr) FROM ENTRETIEN e " +
                    "WHERE e.id_vehicule = v.id_vehicule AND YEAR(COALESCE(e.date_sortie_entr, e.date_entree_entr)) = ?) AS couts_entretien, " +

                    // Coûts d'assurance pour l'année
                    "(SELECT SUM(a.cout_assurance) FROM ASSURANCE a " +
                    "JOIN COUVRIR c ON a.num_carte_assurance = c.num_carte_assurance " +
                    "WHERE c.id_vehicule = v.id_vehicule AND YEAR(COALESCE(a.date_fin_assurance, a.date_debut_assurance)) = ?) AS couts_assurance, " +

                    // Coûts de mission pour l'année
                    "(SELECT SUM(m.cout_total) FROM MISSION m " +
                    "WHERE m.id_vehicule = v.id_vehicule AND YEAR(COALESCE(m.date_fin_mission, m.date_debut_mission)) = ?) AS couts_mission, " +

                    // Kilométrage effectué pour l'année
                    "(SELECT SUM(m.km_reel) FROM MISSION m " +
                    "WHERE m.id_vehicule = v.id_vehicule AND YEAR(m.date_fin_mission) = ? AND m.status = 'Cloturee') AS km_annee " +

                    "FROM VEHICULES v " +
                    "ORDER BY v.id_vehicule";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, annee);
            pstmt.setInt(2, annee);
            pstmt.setInt(3, annee);
            pstmt.setInt(4, annee);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                int idVehicule = rs.getInt("id_vehicule");
                String immatriculation = rs.getString("immatriculation");
                String marque = rs.getString("marque");
                String modele = rs.getString("modele");
                BigDecimal prixAchat = rs.getBigDecimal("prix_vehicule");
                Timestamp dateAcquisition = rs.getTimestamp("date_acquisition");
                int kmActuels = rs.getInt("km_actuels");
                BigDecimal coutsEntretien = rs.getBigDecimal("couts_entretien");
                BigDecimal coutsAssurance = rs.getBigDecimal("couts_assurance");
                BigDecimal coutsMission = rs.getBigDecimal("couts_mission");
                Integer kmAnnee = rs.getInt("km_annee");

                // Normaliser les valeurs NULL
                prixAchat = prixAchat != null ? prixAchat : BigDecimal.ZERO;
                coutsEntretien = coutsEntretien != null ? coutsEntretien : BigDecimal.ZERO;
                coutsAssurance = coutsAssurance != null ? coutsAssurance : BigDecimal.ZERO;
                coutsMission = coutsMission != null ? coutsMission : BigDecimal.ZERO;

                if (rs.wasNull()) {
                    kmAnnee = 0;
                }

                // Calcul de l'amortissement annuel
                BigDecimal amortissementAnnuel;
                if (dateAcquisition != null) {
                    int anneesEcoulees = LocalDate.now().getYear() - dateAcquisition.toLocalDateTime().getYear() + 1;
                    if (anneesEcoulees <= DUREE_AMORTISSEMENT_ANNEES) {
                        amortissementAnnuel = prixAchat.multiply(BigDecimal.valueOf(TAUX_AMORTISSEMENT_ANNUEL));
                    } else {
                        amortissementAnnuel = BigDecimal.ZERO; // Véhicule totalement amorti
                    }
                } else {
                    amortissementAnnuel = BigDecimal.ZERO;
                }

                // Calcul du coût total annuel
                BigDecimal coutTotalAnnuel = amortissementAnnuel.add(coutsEntretien).add(coutsAssurance).add(coutsMission);

                // Calcul du coût par kilomètre
                BigDecimal coutParKm;
                if (kmAnnee > 0) {
                    coutParKm = coutTotalAnnuel.divide(BigDecimal.valueOf(kmAnnee), 2, RoundingMode.HALF_UP);
                } else {
                    coutParKm = BigDecimal.ZERO;
                }

                // Calcul de la rentabilité (somme des coûts / prix d'achat)
                double rentabilitePct;
                if (prixAchat.compareTo(BigDecimal.ZERO) > 0) {
                    rentabilitePct = (coutsMission.subtract(coutsEntretien).subtract(coutsAssurance))
                            .divide(prixAchat, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
                            .doubleValue();
                } else {
                    rentabilitePct = 0;
                }

                RentabiliteVehicule rv = new RentabiliteVehicule(
                        idVehicule, immatriculation, marque, modele,
                        prixAchat, amortissementAnnuel, coutsEntretien,
                        coutsAssurance, coutsMission, kmAnnee, coutParKm, rentabilitePct
                );

                rapport.add(rv);
            }

            return rapport;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la génération du rapport de rentabilité pour l'année " + annee, ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Vérifie et alerte sur les contrats d'assurance expirant prochainement.
     *
     * @param joursAvantExpiration Nombre de jours avant expiration pour l'alerte
     * @return Liste des assurances expirant prochainement
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<AlerteAssurance> verifierAssurancesExpirees(int joursAvantExpiration) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<AlerteAssurance> alertes = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            LocalDateTime dateReference = LocalDateTime.now().plusDays(joursAvantExpiration);

            String sql = "SELECT a.num_carte_assurance, a.date_debut_assurance, a.date_fin_assurance, " +
                    "a.agence, a.cout_assurance, v.id_vehicule, v.immatriculation, v.marque, v.modele " +
                    "FROM ASSURANCE a " +
                    "JOIN COUVRIR c ON a.num_carte_assurance = c.num_carte_assurance " +
                    "JOIN VEHICULES v ON c.id_vehicule = v.id_vehicule " +
                    "WHERE a.date_fin_assurance <= ? AND a.date_fin_assurance >= CURRENT_TIMESTAMP " +
                    "ORDER BY a.date_fin_assurance";

            pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, Timestamp.valueOf(dateReference));

            rs = pstmt.executeQuery();

            while (rs.next()) {
                int numCarteAssurance = rs.getInt("num_carte_assurance");
                LocalDateTime dateDebut = rs.getTimestamp("date_debut_assurance").toLocalDateTime();
                LocalDateTime dateFin = rs.getTimestamp("date_fin_assurance").toLocalDateTime();
                String agence = rs.getString("agence");
                BigDecimal coutAssurance = rs.getBigDecimal("cout_assurance");
                int idVehicule = rs.getInt("id_vehicule");
                String immatriculation = rs.getString("immatriculation");
                String marque = rs.getString("marque");
                String modele = rs.getString("modele");

                // Calcul du nombre de jours avant expiration
                long joursRestants = LocalDateTime.now().until(dateFin, ChronoUnit.DAYS);

                AlerteAssurance alerte = new AlerteAssurance(
                        numCarteAssurance, dateDebut, dateFin, agence, coutAssurance,
                        idVehicule, immatriculation, marque, modele, joursRestants
                );

                alertes.add(alerte);
            }

            return alertes;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la vérification des assurances expirées", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Vérifie les véhicules ayant dépassé leur kilométrage prévu pour l'entretien.
     *
     * @param kmEntretienPreventif Kilométrage à partir duquel un entretien préventif est recommandé
     * @return Liste des véhicules nécessitant un entretien
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<AlerteEntretien> verifierEntretiensNecessaires(int kmEntretienPreventif) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<AlerteEntretien> alertes = new ArrayList<>();

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT v.id_vehicule, v.immatriculation, v.marque, v.modele, v.km_actuels, " +
                    "(SELECT MAX(e.date_sortie_entr) FROM ENTRETIEN e " +
                    "WHERE e.id_vehicule = v.id_vehicule AND e.type = 'Preventif' AND e.statut_ot = 'Cloture') AS dernier_entretien, " +
                    "(SELECT km_reel FROM MISSION m " +
                    "WHERE m.id_vehicule = v.id_vehicule AND m.status = 'Cloturee' " +
                    "ORDER BY m.date_fin_mission DESC LIMIT 1) AS km_dernier_entretien " +
                    "FROM VEHICULES v " +
                    "WHERE v.id_etat_voiture NOT IN (3, 4, 6)"; // Exclure les véhicules hors service, en entretien, en panne

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int idVehicule = rs.getInt("id_vehicule");
                String immatriculation = rs.getString("immatriculation");
                String marque = rs.getString("marque");
                String modele = rs.getString("modele");
                int kmActuels = rs.getInt("km_actuels");
                Timestamp dernierEntretien = rs.getTimestamp("dernier_entretien");
                int kmDernierEntretien = rs.getInt("km_dernier_entretien");

                if (rs.wasNull()) {
                    kmDernierEntretien = 0;
                }

                LocalDateTime dateDernierEntretien = null;
                if (dernierEntretien != null) {
                    dateDernierEntretien = dernierEntretien.toLocalDateTime();
                }

                // Calculer le kilométrage depuis le dernier entretien
                int kmDepuisDernierEntretien = kmActuels - kmDernierEntretien;

                // Vérifier si un entretien est nécessaire
                boolean entretienNecessaire = kmDepuisDernierEntretien >= kmEntretienPreventif;

                if (entretienNecessaire) {
                    AlerteEntretien alerte = new AlerteEntretien(
                            idVehicule, immatriculation, marque, modele,
                            kmActuels, dateDernierEntretien, kmDernierEntretien, kmDepuisDernierEntretien
                    );

                    alertes.add(alerte);
                }
            }

            return alertes;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la vérification des entretiens nécessaires", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Classe interne représentant un bilan financier.
     */
    public static class BilanFinancier {
        private final BigDecimal totalRecettes;
        private final BigDecimal totalMissions;
        private final BigDecimal totalEntretiens;
        private final BigDecimal totalAssurances;
        private final BigDecimal solde;
        private final LocalDateTime dateDebut;
        private final LocalDateTime dateFin;

        /**
         * Constructeur pour un bilan financier.
         *
         * @param totalRecettes Total des recettes
         * @param totalMissions Total des dépenses de missions
         * @param totalEntretiens Total des dépenses d'entretiens
         * @param totalAssurances Total des dépenses d'assurances
         * @param solde Solde (recettes - dépenses totales)
         * @param dateDebut Date de début de la période
         * @param dateFin Date de fin de la période
         */
        public BilanFinancier(BigDecimal totalRecettes, BigDecimal totalMissions, BigDecimal totalEntretiens,
                              BigDecimal totalAssurances, BigDecimal solde, LocalDateTime dateDebut, LocalDateTime dateFin) {
            this.totalRecettes = totalRecettes;
            this.totalMissions = totalMissions;
            this.totalEntretiens = totalEntretiens;
            this.totalAssurances = totalAssurances;
            this.solde = solde;
            this.dateDebut = dateDebut;
            this.dateFin = dateFin;
        }

        // Getters

        /**
         * @return Total des recettes
         */
        public BigDecimal getTotalRecettes() {
            return totalRecettes;
        }

        /**
         * @return Total des dépenses de missions
         */
        public BigDecimal getTotalMissions() {
            return totalMissions;
        }

        /**
         * @return Total des dépenses d'entretiens
         */
        public BigDecimal getTotalEntretiens() {
            return totalEntretiens;
        }

        /**
         * @return Total des dépenses d'assurances
         */
        public BigDecimal getTotalAssurances() {
            return totalAssurances;
        }

        /**
         * @return Total des dépenses
         */
        public BigDecimal getTotalDepenses() {
            return totalMissions.add(totalEntretiens).add(totalAssurances);
        }

        /**
         * @return Solde (recettes - dépenses totales)
         */
        public BigDecimal getSolde() {
            return solde;
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
         * @return Marge (solde / recettes)
         */
        public BigDecimal getMargePct() {
            if (totalRecettes.compareTo(BigDecimal.ZERO) == 0) {
                return BigDecimal.ZERO;
            }
            return solde
                    .divide(totalRecettes, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }

        /**
         * @return Si le bilan est positif
         */
        public boolean isPositif() {
            return solde.compareTo(BigDecimal.ZERO) >= 0;
        }
    }

    /**
     * Classe interne représentant un bilan financier mensuel.
     */
    public static class BilanMensuel {
        private BigDecimal recettes;
        private BigDecimal depenses;

        /**
         * Constructeur pour un bilan mensuel.
         *
         * @param recettes Recettes du mois
         * @param depenses Dépenses du mois
         */
        public BilanMensuel(BigDecimal recettes, BigDecimal depenses) {
            this.recettes = recettes;
            this.depenses = depenses;
        }

        /**
         * @return Recettes du mois
         */
        public BigDecimal getRecettes() {
            return recettes;
        }

        /**
         * @param recettes Recettes du mois
         */
        public void setRecettes(BigDecimal recettes) {
            this.recettes = recettes;
        }

        /**
         * @return Dépenses du mois
         */
        public BigDecimal getDepenses() {
            return depenses;
        }

        /**
         * @param depenses Dépenses du mois
         */
        public void setDepenses(BigDecimal depenses) {
            this.depenses = depenses;
        }

        /**
         * Ajoute des dépenses au total mensuel.
         *
         * @param montant Montant à ajouter
         */
        public void ajouterDepenses(BigDecimal montant) {
            depenses = depenses.add(montant);
        }

        /**
         * @return Solde du mois (recettes - dépenses)
         */
        public BigDecimal getSolde() {
            return recettes.subtract(depenses);
        }

        /**
         * @return Si le bilan mensuel est positif
         */
        public boolean isPositif() {
            return getSolde().compareTo(BigDecimal.ZERO) >= 0;
        }
    }

    /**
     * Classe interne représentant le coût total de possession (TCO) d'un véhicule.
     */
    public static class TCOVehicule {
        private final int idVehicule;
        private final String marque;
        private final String modele;
        private final String immatriculation;
        private final BigDecimal prixAchat;
        private final BigDecimal valeurResiduelle;
        private final BigDecimal amortissement;
        private final BigDecimal coutsOperationnels;
        private final BigDecimal coutTotalPossession;
        private final BigDecimal coutParKm;
        private final int kmActuels;

        /**
         * Constructeur pour le TCO d'un véhicule.
         *
         * @param idVehicule ID du véhicule
         * @param marque Marque du véhicule
         * @param modele Modèle du véhicule
         * @param immatriculation Immatriculation du véhicule
         * @param prixAchat Prix d'achat du véhicule
         * @param valeurResiduelle Valeur résiduelle du véhicule
         * @param amortissement Amortissement du véhicule
         * @param coutsOperationnels Coûts opérationnels (entretien, assurance, missions)
         * @param coutTotalPossession Coût total de possession
         * @param coutParKm Coût par kilomètre
         * @param kmActuels Kilométrage actuel du véhicule
         */
        public TCOVehicule(int idVehicule, String marque, String modele, String immatriculation,
                           BigDecimal prixAchat, BigDecimal valeurResiduelle, BigDecimal amortissement,
                           BigDecimal coutsOperationnels, BigDecimal coutTotalPossession,
                           BigDecimal coutParKm, int kmActuels) {
            this.idVehicule = idVehicule;
            this.marque = marque;
            this.modele = modele;
            this.immatriculation = immatriculation;
            this.prixAchat = prixAchat;
            this.valeurResiduelle = valeurResiduelle;
            this.amortissement = amortissement;
            this.coutsOperationnels = coutsOperationnels;
            this.coutTotalPossession = coutTotalPossession;
            this.coutParKm = coutParKm;
            this.kmActuels = kmActuels;
        }

        // Getters

        public int getIdVehicule() {
            return idVehicule;
        }

        public String getMarque() {
            return marque;
        }

        public String getModele() {
            return modele;
        }

        public String getImmatriculation() {
            return immatriculation;
        }

        public BigDecimal getPrixAchat() {
            return prixAchat;
        }

        public BigDecimal getValeurResiduelle() {
            return valeurResiduelle;
        }

        public BigDecimal getAmortissement() {
            return amortissement;
        }

        public BigDecimal getCoutsOperationnels() {
            return coutsOperationnels;
        }

        public BigDecimal getCoutTotalPossession() {
            return coutTotalPossession;
        }

        public BigDecimal getCoutParKm() {
            return coutParKm;
        }

        public int getKmActuels() {
            return kmActuels;
        }

        public BigDecimal getPourcentageAmortissement() {
            if (prixAchat.compareTo(BigDecimal.ZERO) == 0) {
                return BigDecimal.ZERO;
            }
            return amortissement
                    .divide(prixAchat, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
    }

    /**
     * Classe interne représentant la rentabilité d'un véhicule.
     */
    public static class RentabiliteVehicule {
        private final int idVehicule;
        private final String immatriculation;
        private final String marque;
        private final String modele;
        private final BigDecimal prixAchat;
        private final BigDecimal amortissementAnnuel;
        private final BigDecimal coutsEntretien;
        private final BigDecimal coutsAssurance;
        private final BigDecimal coutsMission;
        private final int kmAnnee;
        private final BigDecimal coutParKm;
        private final double rentabilitePct;

        /**
         * Constructeur pour la rentabilité d'un véhicule.
         *
         * @param idVehicule ID du véhicule
         * @param immatriculation Immatriculation du véhicule
         * @param marque Marque du véhicule
         * @param modele Modèle du véhicule
         * @param prixAchat Prix d'achat du véhicule
         * @param amortissementAnnuel Amortissement annuel du véhicule
         * @param coutsEntretien Coûts d'entretien annuels
         * @param coutsAssurance Coûts d'assurance annuels
         * @param coutsMission Coûts de mission annuels
         * @param kmAnnee Kilomètres parcourus dans l'année
         * @param coutParKm Coût par kilomètre
         * @param rentabilitePct Rentabilité en pourcentage
         */
        public RentabiliteVehicule(int idVehicule, String immatriculation, String marque, String modele,
                                   BigDecimal prixAchat, BigDecimal amortissementAnnuel,
                                   BigDecimal coutsEntretien, BigDecimal coutsAssurance, BigDecimal coutsMission,
                                   int kmAnnee, BigDecimal coutParKm, double rentabilitePct) {
            this.idVehicule = idVehicule;
            this.immatriculation = immatriculation;
            this.marque = marque;
            this.modele = modele;
            this.prixAchat = prixAchat;
            this.amortissementAnnuel = amortissementAnnuel;
            this.coutsEntretien = coutsEntretien;
            this.coutsAssurance = coutsAssurance;
            this.coutsMission = coutsMission;
            this.kmAnnee = kmAnnee;
            this.coutParKm = coutParKm;
            this.rentabilitePct = rentabilitePct;
        }

        // Getters

        public int getIdVehicule() {
            return idVehicule;
        }

        public String getImmatriculation() {
            return immatriculation;
        }

        public String getMarque() {
            return marque;
        }

        public String getModele() {
            return modele;
        }

        public BigDecimal getPrixAchat() {
            return prixAchat;
        }

        public BigDecimal getAmortissementAnnuel() {
            return amortissementAnnuel;
        }

        public BigDecimal getCoutsEntretien() {
            return coutsEntretien;
        }

        public BigDecimal getCoutsAssurance() {
            return coutsAssurance;
        }

        public BigDecimal getCoutsMission() {
            return coutsMission;
        }

        public int getKmAnnee() {
            return kmAnnee;
        }

        public BigDecimal getCoutParKm() {
            return coutParKm;
        }

        public double getRentabilitePct() {
            return rentabilitePct;
        }

        public BigDecimal getCoutTotalAnnuel() {
            return amortissementAnnuel.add(coutsEntretien).add(coutsAssurance);
        }
    }

    /**
     * Classe interne représentant une alerte d'assurance.
     */
    public static class AlerteAssurance {
        private final int numCarteAssurance;
        private final LocalDateTime dateDebut;
        private final LocalDateTime dateFin;
        private final String agence;
        private final BigDecimal coutAssurance;
        private final int idVehicule;
        private final String immatriculation;
        private final String marque;
        private final String modele;
        private final long joursRestants;

        /**
         * Constructeur pour une alerte d'assurance.
         *
         * @param numCarteAssurance Numéro de la carte d'assurance
         * @param dateDebut Date de début de l'assurance
         * @param dateFin Date de fin de l'assurance
         * @param agence Agence d'assurance
         * @param coutAssurance Coût de l'assurance
         * @param idVehicule ID du véhicule associé
         * @param immatriculation Immatriculation du véhicule
         * @param marque Marque du véhicule
         * @param modele Modèle du véhicule
         * @param joursRestants Nombre de jours avant expiration
         */
        public AlerteAssurance(int numCarteAssurance, LocalDateTime dateDebut, LocalDateTime dateFin,
                               String agence, BigDecimal coutAssurance, int idVehicule,
                               String immatriculation, String marque, String modele, long joursRestants) {
            this.numCarteAssurance = numCarteAssurance;
            this.dateDebut = dateDebut;
            this.dateFin = dateFin;
            this.agence = agence;
            this.coutAssurance = coutAssurance;
            this.idVehicule = idVehicule;
            this.immatriculation = immatriculation;
            this.marque = marque;
            this.modele = modele;
            this.joursRestants = joursRestants;
        }

        // Getters

        public int getNumCarteAssurance() {
            return numCarteAssurance;
        }

        public LocalDateTime getDateDebut() {
            return dateDebut;
        }

        public LocalDateTime getDateFin() {
            return dateFin;
        }

        public String getAgence() {
            return agence;
        }

        public BigDecimal getCoutAssurance() {
            return coutAssurance;
        }

        public int getIdVehicule() {
            return idVehicule;
        }

        public String getImmatriculation() {
            return immatriculation;
        }

        public String getMarque() {
            return marque;
        }

        public String getModele() {
            return modele;
        }

        public long getJoursRestants() {
            return joursRestants;
        }

        public boolean isUrgent() {
            return joursRestants <= 7;
        }

        public String getFormattedDateFin() {
            return dateFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
    }

    /**
     * Classe interne représentant une alerte d'entretien.
     */
    public static class AlerteEntretien {
        private final int idVehicule;
        private final String immatriculation;
        private final String marque;
        private final String modele;
        private final int kmActuels;
        private final LocalDateTime dateDernierEntretien;
        private final int kmDernierEntretien;
        private final int kmDepuisDernierEntretien;

        /**
         * Constructeur pour une alerte d'entretien.
         *
         * @param idVehicule ID du véhicule
         * @param immatriculation Immatriculation du véhicule
         * @param marque Marque du véhicule
         * @param modele Modèle du véhicule
         * @param kmActuels Kilométrage actuel du véhicule
         * @param dateDernierEntretien Date du dernier entretien
         * @param kmDernierEntretien Kilométrage du dernier entretien
         * @param kmDepuisDernierEntretien Kilométrage depuis le dernier entretien
         */
        public AlerteEntretien(int idVehicule, String immatriculation, String marque, String modele,
                               int kmActuels, LocalDateTime dateDernierEntretien,
                               int kmDernierEntretien, int kmDepuisDernierEntretien) {
            this.idVehicule = idVehicule;
            this.immatriculation = immatriculation;
            this.marque = marque;
            this.modele = modele;
            this.kmActuels = kmActuels;
            this.dateDernierEntretien = dateDernierEntretien;
            this.kmDernierEntretien = kmDernierEntretien;
            this.kmDepuisDernierEntretien = kmDepuisDernierEntretien;
        }

        // Getters

        public int getIdVehicule() {
            return idVehicule;
        }

        public String getImmatriculation() {
            return immatriculation;
        }
        public String getMarque() {
            return marque;
        }

        public String getModele() {
            return modele;
        }

        public int getKmActuels() {
            return kmActuels;
        }

        public LocalDateTime getDateDernierEntretien() {
            return dateDernierEntretien;
        }

        public int getKmDernierEntretien() {
            return kmDernierEntretien;
        }

        public int getKmDepuisDernierEntretien() {
            return kmDepuisDernierEntretien;
        }

        public String getFormattedDateDernierEntretien() {
            return dateDernierEntretien != null ? dateDernierEntretien.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "Jamais";
        }

        public boolean isUrgent() {
            return kmDepuisDernierEntretien > 20000; // Entretien très urgent à partir de 20 000 km
        }
    }
}