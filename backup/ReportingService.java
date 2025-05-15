package main.java.com.miage.parcauto.service;

import main.java.com.miage.parcauto.dao.*;
import main.java.com.miage.parcauto.model.finance.Mouvement;
import main.java.com.miage.parcauto.model.mission.DepenseMission;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;
import main.java.com.miage.parcauto.model.mission.Mission;
import main.java.com.miage.parcauto.model.entretien.Entretien;
import main.java.com.miage.parcauto.model.finance.SocieteCompte;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.math.RoundingMode;

/**
 * Service de génération de rapports.
 * Cette classe implémente la couche service pour la génération de rapports et
 * d'analyses.
 * Elle sert d'intermédiaire entre les différents DAOs et la couche de
 * présentation (contrôleurs).
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class ReportingService {

    private static final Logger LOGGER = Logger.getLogger(ReportingService.class.getName());

    // Répertoire pour l'export des rapports
    private static final String REPORTS_DIRECTORY = "./reports";

    private final VehiculeDao vehiculeDao;
    private final MissionDao missionDao;
    private final EntretienDao entretienDao;
    private final FinanceDao financeDao;
    private final SocieteCompteDao societeCompteDao;
    private final MouvementDao mouvementDao;

    /**
     * Constructeur par défaut.
     */
    public ReportingService() {
        this.vehiculeDao = new VehiculeDao();
        this.missionDao = new MissionDao();
        this.entretienDao = new EntretienDao();
        this.financeDao = new FinanceDao();
        this.societeCompteDao = new SocieteCompteDao();
        this.mouvementDao = new MouvementDao(); // Ajouter cette ligne

        // Initialisation du répertoire des rapports
        initializeReportsDirectory();
    }

    /**
     * Constructeur avec injection de dépendance pour les tests.
     *
     * @param vehiculeDao      Instance de VehiculeDao à utiliser
     * @param missionDao       Instance de MissionDao à utiliser
     * @param entretienDao     Instance de EntretienDao à utiliser
     * @param financeDao       Instance de FinanceDao à utiliser
     * @param societeCompteDao Instance de SocieteCompteDao à utiliser
     */
    // Modifier également le constructeur avec paramètres
    public ReportingService(VehiculeDao vehiculeDao, MissionDao missionDao, EntretienDao entretienDao,
            FinanceDao financeDao, SocieteCompteDao societeCompteDao, MouvementDao mouvementDao) {
        this.vehiculeDao = vehiculeDao;
        this.missionDao = missionDao;
        this.entretienDao = entretienDao;
        this.financeDao = financeDao;
        this.societeCompteDao = societeCompteDao;
        this.mouvementDao = mouvementDao; // Ajouter cette ligne

        // Initialisation du répertoire des rapports
        initializeReportsDirectory();
    }

    /**
     * Initialise le répertoire des rapports.
     */
    private void initializeReportsDirectory() {
        try {
            Files.createDirectories(Paths.get(REPORTS_DIRECTORY));
            LOGGER.info("Répertoire des rapports créé: " + REPORTS_DIRECTORY);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création du répertoire des rapports", e);
        }
    }

    /**
     * Génère un tableau de bord pour un rapide aperçu du parc automobile.
     *
     * @return Map contenant les données du tableau de bord
     */
    public Map<String, Object> genererTableauDeBord() {
        Map<String, Object> dashboard = new HashMap<>();

        try {
            // Statistiques du parc automobile
            VehiculeDao.ParcStats parcStats = vehiculeDao.calculateParcStats();
            if (parcStats != null) {
                dashboard.put("totalVehicules", parcStats.getTotalVehicules());
                dashboard.put("vehiculesDisponibles", parcStats.getDisponibles());
                dashboard.put("vehiculesEnMission", parcStats.getEnMission());
                dashboard.put("vehiculesHorsService", parcStats.getHorsService());
                dashboard.put("vehiculesEnEntretien", parcStats.getEnEntretien());
                dashboard.put("vehiculesAttribues", parcStats.getAttribues());
                dashboard.put("vehiculesEnPanne", parcStats.getEnPanne());
                dashboard.put("kmMoyen", parcStats.getKmMoyen());
                dashboard.put("pourcentageDisponibilite", parcStats.getPourcentageDisponibilite());
                dashboard.put("pourcentageUtilisation", parcStats.getPourcentageUtilisation());
            }

            // Statistiques des missions
            LocalDateTime debutAnnee = LocalDateTime.of(LocalDate.now().getYear(), 1, 1, 0, 0);
            LocalDateTime finAnnee = LocalDateTime.of(LocalDate.now().getYear(), 12, 31, 23, 59, 59);

            MissionDao.MissionStats missionStats = missionDao.calculateStats(LocalDate.now().getYear());
            if (missionStats != null) {
                dashboard.put("totalMissions", missionStats.getTotalMissions());
                dashboard.put("missionsEnCours", missionStats.getEnCours());
                dashboard.put("missionsPlanifiees", missionStats.getPlanifiees());
                dashboard.put("missionsCloturees", missionStats.getCloturees());
                dashboard.put("tauxAchevement", missionStats.getTauxAchevement());
                dashboard.put("kmTotal", missionStats.getKmTotal());
                dashboard.put("kmMoyenMission", missionStats.getKmMoyen());
                dashboard.put("coutTotalMissions", missionStats.getCoutTotal());
                dashboard.put("coutMoyenParKm", missionStats.getCoutMoyenParKm());
            }

            // Statistiques des entretiens
            EntretienDao.EntretienStats entretienStats = entretienDao.calculateStats(LocalDate.now().getYear());
            if (entretienStats != null) {
                dashboard.put("totalEntretiens", entretienStats.getTotalEntretiens());
                dashboard.put("entretiensEnCours", entretienStats.getEnCours());
                dashboard.put("entretiensPlanifies", entretienStats.getPlanifies());
                dashboard.put("entretiensTermines", entretienStats.getTermines());
                dashboard.put("entretiensPreventifs", entretienStats.getPreventifs());
                dashboard.put("entretiensCorrectifs", entretienStats.getCorrectifs());
                dashboard.put("coutTotalEntretiens", entretienStats.getCoutTotal());
                dashboard.put("coutMoyenEntretien", entretienStats.getCoutMoyen());
                dashboard.put("pourcentagePreventifs", entretienStats.getPourcentagePreventifs());
                dashboard.put("pourcentageCorrectifs", entretienStats.getPourcentageCorrectifs());
                dashboard.put("ratioPreventifCorrectif", entretienStats.getRatioPreventifCorrectif());
            }

            // Bilan financier mensuel
            FinanceDao.BilanFinancier bilanMensuel = calculerBilanMensuel();
            if (bilanMensuel != null) {
                dashboard.put("recettesMensuelles", bilanMensuel.getTotalRecettes());
                dashboard.put("depensesMensuelles", bilanMensuel.getTotalDepenses());
                dashboard.put("soldeMensuel", bilanMensuel.getSolde());
                dashboard.put("isSoldeMensuelPositif", bilanMensuel.isPositif());
            }

            // Alertes assurances
            List<FinanceDao.AlerteAssurance> alertesAssurance = financeDao.verifierAssurancesExpirees(30); // 30 jours
            dashboard.put("alertesAssurance", alertesAssurance);
            dashboard.put("nombreAlertesAssurance", alertesAssurance.size());

            // Alertes entretiens
            List<FinanceDao.AlerteEntretien> alertesEntretien = financeDao.verifierEntretiensNecessaires(10000); // 10
                                                                                                                 // 000
                                                                                                                 // km
            dashboard.put("alertesEntretien", alertesEntretien);
            dashboard.put("nombreAlertesEntretien", alertesEntretien.size());

            // Missions en cours
            List<Mission> missionsEnCours = missionDao.findMissionsEnCours();
            dashboard.put("listeMissionsEnCours", missionsEnCours);
            dashboard.put("nombreMissionsEnCours", missionsEnCours.size());

            // Top 5 véhicules par rentabilité
            List<FinanceDao.RentabiliteVehicule> rentabilites = financeDao
                    .genererRapportRentabilite(LocalDate.now().getYear());
            List<FinanceDao.RentabiliteVehicule> topRentabilites = rentabilites.stream()
                    .sorted(Comparator.comparingDouble(FinanceDao.RentabiliteVehicule::getRentabilitePct).reversed())
                    .limit(5)
                    .collect(Collectors.toList());
            dashboard.put("topVehiculesRentabilite", topRentabilites);

            return dashboard;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la génération du tableau de bord", e);
            return Collections.emptyMap();
        }
    }

    /**
     * Génère un rapport détaillé sur l'état du parc automobile.
     *
     * @return Map contenant les données du rapport
     */
    public Map<String, Object> genererRapportParcAutomobile() {
        Map<String, Object> rapport = new HashMap<>();

        try {
            // Liste de tous les véhicules
            List<Vehicule> vehicules = vehiculeDao.findAll();
            rapport.put("vehicules", vehicules);
            rapport.put("nombreVehicules", vehicules.size());

            // Statistiques globales du parc
            VehiculeDao.ParcStats parcStats = vehiculeDao.calculateParcStats();
            rapport.put("parcStats", parcStats);

            // Répartition par état
            Map<String, Integer> repartitionParEtat = new HashMap<>();
            repartitionParEtat.put("Disponible", parcStats.getDisponibles());
            repartitionParEtat.put("En mission", parcStats.getEnMission());
            repartitionParEtat.put("Hors service", parcStats.getHorsService());
            repartitionParEtat.put("En entretien", parcStats.getEnEntretien());
            repartitionParEtat.put("Attribué", parcStats.getAttribues());
            repartitionParEtat.put("En panne", parcStats.getEnPanne());
            rapport.put("repartitionParEtat", repartitionParEtat);

            // Répartition par énergie
            Map<String, Integer> repartitionParEnergie = new HashMap<>();
            for (Vehicule.TypeEnergie energie : Vehicule.TypeEnergie.values()) {
                long count = vehicules.stream()
                        .filter(v -> v.getEnergie() == energie)
                        .count();
                repartitionParEnergie.put(energie.name(), (int) count);
            }
            rapport.put("repartitionParEnergie", repartitionParEnergie);

            // Véhicules nécessitant un entretien
            List<Vehicule> vehiculesEntretien = vehiculeDao.findNeedingMaintenance(10000); // 10 000 km
            rapport.put("vehiculesNecessitantEntretien", vehiculesEntretien);
            rapport.put("nombreVehiculesNecessitantEntretien", vehiculesEntretien.size());

            // Calcul des TCO pour chaque véhicule
            Map<Integer, VehiculeDao.TCOInfo> tcosParVehicule = new HashMap<>();
            for (Vehicule vehicule : vehicules) {
                try {
                    Optional<VehiculeDao.TCOInfo> tco = vehiculeDao.getTCOInfo(vehicule.getIdVehicule());
                    if (tco.isPresent()) {
                        tcosParVehicule.put(vehicule.getIdVehicule(), tco.get());
                    }
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING,
                            "Erreur lors du calcul du TCO pour le véhicule ID: " + vehicule.getIdVehicule(), e);
                }
            }
            rapport.put("tcosParVehicule", tcosParVehicule);

            // Âge moyen du parc
            double ageMoyen = vehicules.stream()
                    .mapToLong(Vehicule::ageVehicule) // Utiliser mapToLong au lieu de mapToInt
                    .average()
                    .orElse(0);
            rapport.put("ageMoyenParc", ageMoyen);

            // Répartition par marque
            Map<String, Long> repartitionParMarque = vehicules.stream()
                    .collect(Collectors.groupingBy(
                            Vehicule::getMarque,
                            Collectors.counting()));
            rapport.put("repartitionParMarque", repartitionParMarque);

            return rapport;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la génération du rapport sur le parc automobile", e);
            return Collections.emptyMap();
        }
    }

    /**
     * Génère un rapport d'activité des missions sur une période donnée.
     *
     * @param debut Date de début de la période
     * @param fin   Date de fin de la période
     * @return Map contenant les données du rapport
     */
    public Map<String, Object> genererRapportMissions(LocalDateTime debut, LocalDateTime fin) {
        Map<String, Object> rapport = new HashMap<>();

        try {
            // Missions sur la période
            List<Mission> missions = missionDao.findByPeriode(debut, fin);
            rapport.put("missions", missions);
            rapport.put("nombreMissions", missions.size());
            rapport.put("periode", Map.of("debut", debut, "fin", fin));

            // Statistiques des missions
            MissionDao.MissionStats missionStats = missionDao.calculateStats(debut.getYear());
            rapport.put("missionStats", missionStats);

            // Répartition par statut
            Map<String, Long> repartitionParStatut = missions.stream()
                    .collect(Collectors.groupingBy(
                            m -> m.getStatus().name(),
                            Collectors.counting()));
            rapport.put("repartitionParStatut", repartitionParStatut);

            // Répartition par site
            Map<String, Long> repartitionParSite = missions.stream()
                    .collect(Collectors.groupingBy(
                            Mission::getSite,
                            Collectors.counting()));
            rapport.put("repartitionParSite", repartitionParSite);

            // Répartition par mois
            Map<Month, Long> repartitionParMois = missions.stream()
                    .filter(m -> m.getDateDebutMission() != null)
                    .collect(Collectors.groupingBy(
                            m -> m.getDateDebutMission().getMonth(),
                            Collectors.counting()));
            rapport.put("repartitionParMois", repartitionParMois);

            // Durée moyenne des missions
            double dureeMoyenne = missions.stream()
                    .mapToLong(Mission::getDureeJours)
                    .average()
                    .orElse(0);
            rapport.put("dureeMoyenneMissions", dureeMoyenne);

            // Kilomètres totaux parcourus
            int kmTotaux = missions.stream()
                    .filter(m -> m.getKmReel() != null)
                    .mapToInt(Mission::getKmReel)
                    .sum();
            rapport.put("kmTotaux", kmTotaux);

            // Kilomètres moyen par mission
            double kmMoyen = missions.stream()
                    .filter(m -> m.getKmReel() != null)
                    .mapToInt(Mission::getKmReel)
                    .average()
                    .orElse(0);
            rapport.put("kmMoyen", kmMoyen);

            // Coût total des missions
            BigDecimal coutTotal = missions.stream()
                    .filter(m -> m.getCoutTotal() != null)
                    .map(Mission::getCoutTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            rapport.put("coutTotal", coutTotal);

            // Coût moyen par mission
            BigDecimal coutMoyen = missions.size() > 0
                    ? coutTotal.divide(BigDecimal.valueOf(missions.size()), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;
            rapport.put("coutMoyen", coutMoyen);

            return rapport;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la génération du rapport des missions", e);
            return Collections.emptyMap();
        }
    }

    /**
     * Génère un rapport détaillé sur les entretiens de véhicules.
     *
     * @param annee Année pour le rapport
     * @return Map contenant les données du rapport
     */
    public Map<String, Object> genererRapportEntretiens(int annee) {
        Map<String, Object> rapport = new HashMap<>();

        try {
            // Période de l'année
            LocalDateTime debut = LocalDateTime.of(annee, 1, 1, 0, 0);
            LocalDateTime fin = LocalDateTime.of(annee, 12, 31, 23, 59, 59);

            // Liste de tous les entretiens
            List<Entretien> entretiens = entretienDao.findByPeriode(debut, fin);
            rapport.put("entretiens", entretiens);
            rapport.put("nombreEntretiens", entretiens.size());
            rapport.put("annee", annee);

            // Statistiques des entretiens
            EntretienDao.EntretienStats entretienStats = entretienDao.calculateStats(annee);
            rapport.put("entretienStats", entretienStats);

            // Répartition par type (préventif/correctif)
            long preventifs = entretiens.stream()
                    .filter(Entretien::estPreventif)
                    .count();
            long correctifs = entretiens.stream()
                    .filter(Entretien::estCorrectif)
                    .count();
            rapport.put("entretiensPreventifs", preventifs);
            rapport.put("entretiensCorrectifs", correctifs);

            // Répartition par statut
            Map<String, Long> repartitionParStatut = entretiens.stream()
                    .collect(Collectors.groupingBy(
                            e -> e.getStatutOt().name(),
                            Collectors.counting()));
            rapport.put("repartitionParStatut", repartitionParStatut);

            // Durée moyenne des entretiens
            double dureeMoyenneHeures = entretiens.stream()
                    .mapToLong(Entretien::getDureeHeures)
                    .average()
                    .orElse(0);
            rapport.put("dureeMoyenneHeures", dureeMoyenneHeures);

            // Coût total des entretiens
            BigDecimal coutTotal = entretiens.stream()
                    .filter(e -> e.getCoutEntr() != null)
                    .map(Entretien::getCoutEntr)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            rapport.put("coutTotal", coutTotal);

            // Coût moyen par entretien
            BigDecimal coutMoyen = entretiens.size() > 0
                    ? coutTotal.divide(BigDecimal.valueOf(entretiens.size()), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;
            rapport.put("coutMoyen", coutMoyen);

            // Répartition par mois
            Map<Month, Long> repartitionParMois = entretiens.stream()
                    .filter(e -> e.getDateEntreeEntr() != null)
                    .collect(Collectors.groupingBy(
                            e -> e.getDateEntreeEntr().getMonth(),
                            Collectors.counting()));
            rapport.put("repartitionParMois", repartitionParMois);

            // Top véhicules par coûts d'entretien
            Map<Integer, BigDecimal> coutParVehicule = new HashMap<>();
            for (Entretien entretien : entretiens) {
                int idVehicule = entretien.getIdVehicule();
                BigDecimal cout = entretien.getCoutEntr() != null ? entretien.getCoutEntr() : BigDecimal.ZERO;

                coutParVehicule.merge(idVehicule, cout, BigDecimal::add);
            }

            // Tri par coût décroissant
            List<Map.Entry<Integer, BigDecimal>> topVehiculesEntretien = coutParVehicule.entrySet().stream()
                    .sorted(Map.Entry.<Integer, BigDecimal>comparingByValue().reversed())
                    .limit(5)
                    .collect(Collectors.toList());
            rapport.put("topVehiculesEntretien", topVehiculesEntretien);

            return rapport;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la génération du rapport des entretiens", e);
            return Collections.emptyMap();
        }
    }

    /**
     * Génère un rapport financier détaillé.
     *
     * @param annee Année pour le rapport
     * @return Map contenant les données du rapport
     */
    public Map<String, Object> genererRapportFinancier(int annee) {
        Map<String, Object> rapport = new HashMap<>();

        try {
            // Période de l'année
            LocalDateTime debut = LocalDateTime.of(annee, 1, 1, 0, 0);
            LocalDateTime fin = LocalDateTime.of(annee, 12, 31, 23, 59, 59);

            // Bilan financier
            FinanceDao.BilanFinancier bilanFinancier = financeDao.calculerBilanFinancier(debut, fin);
            rapport.put("bilanFinancier", bilanFinancier);
            rapport.put("annee", annee);

            // Évolution mensuelle
            Map<Month, FinanceDao.BilanMensuel> evolutionMensuelle = financeDao.calculerEvolutionMensuelle(annee);
            rapport.put("evolutionMensuelle", evolutionMensuelle);

            // Répartition budgétaire
            Map<String, BigDecimal> repartitionBudgetaire = financeDao.calculerRepartitionBudgetaire(annee);
            rapport.put("repartitionBudgetaire", repartitionBudgetaire);

            // Rentabilité des véhicules
            List<FinanceDao.RentabiliteVehicule> rentabiliteVehicules = financeDao.genererRapportRentabilite(annee);
            rapport.put("rentabiliteVehicules", rentabiliteVehicules);

            // Top 5 véhicules rentables
            List<FinanceDao.RentabiliteVehicule> topVehiculesRentables = rentabiliteVehicules.stream()
                    .sorted(Comparator.comparingDouble(FinanceDao.RentabiliteVehicule::getRentabilitePct).reversed())
                    .limit(5)
                    .collect(Collectors.toList());
            rapport.put("topVehiculesRentables", topVehiculesRentables);

            // Top 5 véhicules coûteux
            List<FinanceDao.RentabiliteVehicule> topVehiculesNonRentables = rentabiliteVehicules.stream()
                    .sorted(Comparator.comparingDouble(FinanceDao.RentabiliteVehicule::getRentabilitePct))
                    .limit(5)
                    .collect(Collectors.toList());
            rapport.put("topVehiculesNonRentables", topVehiculesNonRentables);

            // Assurances expirant prochainement
            List<FinanceDao.AlerteAssurance> alertesAssurance = financeDao.verifierAssurancesExpirees(90); // 90 jours
            rapport.put("assurancesExpiration", alertesAssurance);

            return rapport;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la génération du rapport financier", e);
            return Collections.emptyMap();
        }
    }

    /**
     * Génère un rapport d'utilisation des véhicules.
     *
     * @param annee Année pour le rapport
     * @return Map contenant les données du rapport
     */
    public Map<String, Object> genererRapportUtilisationVehicules(int annee) {
        Map<String, Object> rapport = new HashMap<>();

        try {
            // Liste de tous les véhicules
            List<Vehicule> vehicules = vehiculeDao.findAll();
            rapport.put("vehicules", vehicules);
            rapport.put("nombreVehicules", vehicules.size());
            rapport.put("annee", annee);

            // Période de l'année
            LocalDateTime debut = LocalDateTime.of(annee, 1, 1, 0, 0);
            LocalDateTime fin = LocalDateTime.of(annee, 12, 31, 23, 59, 59);

            // Missions pour chaque véhicule sur la période
            Map<Integer, List<Mission>> missionsParVehicule = new HashMap<>();
            Map<Integer, Long> joursUtilisationParVehicule = new HashMap<>();
            Map<Integer, Integer> kmParcoursParVehicule = new HashMap<>();

            for (Vehicule vehicule : vehicules) {
                int idVehicule = vehicule.getIdVehicule();
                List<Mission> missionsVehicule = missionDao.findByVehicule(idVehicule);

                // Filtrer les missions de la période
                List<Mission> missionsPeriode = missionsVehicule.stream()
                        .filter(m -> {
                            LocalDateTime dateDebut = m.getDateDebutMission();
                            LocalDateTime dateFin = m.getDateFinMission();
                            return dateDebut != null && dateFin != null &&
                                    !dateDebut.isAfter(fin) && !dateFin.isBefore(debut);
                        })
                        .collect(Collectors.toList());

                missionsParVehicule.put(idVehicule, missionsPeriode);

                // Calculer les jours d'utilisation
                // Calculer les jours d'utilisation
                long joursUtilisation = missionsPeriode.stream()
                        .mapToLong(Mission::getDureeJours) // Utiliser mapToLong au lieu de mapToInt
                        .sum();
                joursUtilisationParVehicule.put(idVehicule, joursUtilisation);

                // Calculer les kilomètres parcourus
                int kmParcourus = missionsPeriode.stream()
                        .filter(m -> m.getStatus() == Mission.StatusMission.Cloturee && m.getKmReel() != null)
                        .mapToInt(Mission::getKmReel)
                        .sum();
                kmParcoursParVehicule.put(idVehicule, kmParcourus);
            }

            rapport.put("missionsParVehicule", missionsParVehicule);
            rapport.put("joursUtilisationParVehicule", joursUtilisationParVehicule);
            rapport.put("kmParcoursParVehicule", kmParcoursParVehicule);

            // Calcul des taux d'utilisation (jours utilisés / 365)
            Map<Integer, Double> tauxUtilisationParVehicule = new HashMap<>();
            for (Map.Entry<Integer, Long> entry : joursUtilisationParVehicule.entrySet()) {
                double tauxUtilisation = entry.getValue() / 365.0 * 100.0;
                tauxUtilisationParVehicule.put(entry.getKey(), tauxUtilisation);
            }
            rapport.put("tauxUtilisationParVehicule", tauxUtilisationParVehicule);

            // Top 5 véhicules les plus utilisés
            List<Map.Entry<Integer, Double>> topVehiculesUtilises = tauxUtilisationParVehicule.entrySet().stream()
                    .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                    .limit(5)
                    .collect(Collectors.toList());
            rapport.put("topVehiculesUtilises", topVehiculesUtilises);

            // Top 5 véhicules les moins utilisés
            List<Map.Entry<Integer, Double>> bottomVehiculesUtilises = tauxUtilisationParVehicule.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue())
                    .limit(5)
                    .collect(Collectors.toList());
            rapport.put("bottomVehiculesUtilises", bottomVehiculesUtilises);

            return rapport;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la génération du rapport d'utilisation des véhicules", e);
            return Collections.emptyMap();
        }
    }

    /**
     * Exporte un rapport au format HTML.
     *
     * @param rapport Données du rapport
     * @param titre   Titre du rapport
     * @return Chemin du fichier HTML généré ou null en cas d'erreur
     */
    public String exporterRapportHTML(Map<String, Object> rapport, String titre) {
        // Génération d'un nom de fichier
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = REPORTS_DIRECTORY + "/rapport_" + titre.replaceAll("\\s+", "_").toLowerCase() + "_"
                + timestamp + ".html";

        try (OutputStream os = new FileOutputStream(fileName)) {
            // Création du contenu HTML
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>\n");
            html.append("<html lang=\"fr\">\n");
            html.append("<head>\n");
            html.append("  <meta charset=\"UTF-8\">\n");
            html.append("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
            html.append("  <title>").append(titre).append("</title>\n");
            html.append("  <style>\n");
            html.append("    body { font-family: Arial, sans-serif; margin: 20px; }\n");
            html.append("    h1 { color: #2c3e50; text-align: center; }\n");
            html.append("    h2 { color: #3498db; margin-top: 20px; }\n");
            html.append("    table { border-collapse: collapse; width: 100%; margin-bottom: 20px; }\n");
            html.append("    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n");
            html.append("    th { background-color: #f2f2f2; }\n");
            html.append("    tr:nth-child(even) { background-color: #f9f9f9; }\n");
            html.append("    .container { margin-bottom: 30px; }\n");
            html.append(
                    "    .alert { color: #721c24; background-color: #f8d7da; padding: 10px; border-radius: 5px; margin: 10px 0; }\n");
            html.append(
                    "    .success { color: #155724; background-color: #d4edda; padding: 10px; border-radius: 5px; margin: 10px 0; }\n");
            html.append(
                    "    .info { color: #0c5460; background-color: #d1ecf1; padding: 10px; border-radius: 5px; margin: 10px 0; }\n");
            html.append("  </style>\n");
            html.append("</head>\n");
            html.append("<body>\n");

            // En-tête
            html.append("  <h1>").append(titre).append("</h1>\n");
            html.append("  <p>Rapport généré le ")
                    .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm:ss")))
                    .append("</p>\n");

            // Contenu du rapport
            renderReportContent(html, rapport);

            // Pied de page
            html.append("  <footer>\n");
            html.append("    <p style=\"text-align:center; font-size:0.8em; margin-top:50px;\">&copy; ")
                    .append(Year.now().getValue()).append(" MIAGE Holding - ParcAuto</p>\n");
            html.append("  </footer>\n");
            html.append("</body>\n");
            html.append("</html>");

            // Écriture du fichier
            os.write(html.toString().getBytes());

            LOGGER.info("Rapport HTML généré: " + fileName);
            return fileName;

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'export du rapport HTML", e);
            return null;
        }
    }

    /**
     * Génère le contenu HTML d'un rapport en fonction de ses données.
     *
     * @param html    StringBuilder pour la génération HTML
     * @param rapport Données du rapport à afficher
     */
    private void renderReportContent(StringBuilder html, Map<String, Object> rapport) {
        // Ajout des sections selon le contenu du rapport

        // Si le rapport contient des véhicules
        if (rapport.containsKey("vehicules")) {
            @SuppressWarnings("unchecked")
            List<Vehicule> vehicules = (List<Vehicule>) rapport.get("vehicules");

            html.append("  <div class=\"container\">\n");
            html.append("    <h2>Liste des véhicules</h2>\n");

            if (vehicules != null && !vehicules.isEmpty()) {
                html.append("    <p>Nombre de véhicules: ").append(vehicules.size()).append("</p>\n");
                html.append("    <table>\n");
                html.append("      <tr>\n");
                html.append("        <th>ID</th>\n");
                html.append("        <th>Immatriculation</th>\n");
                html.append("        <th>Marque</th>\n");
                html.append("        <th>Modèle</th>\n");
                html.append("        <th>État</th>\n");
                html.append("        <th>Kilométrage</th>\n");
                html.append("      </tr>\n");

                for (Vehicule v : vehicules) {
                    html.append("      <tr>\n");
                    html.append("        <td>").append(v.getIdVehicule() != null ? v.getIdVehicule() : "")
                            .append("</td>\n");
                    html.append("        <td>").append(v.getImmatriculation() != null ? v.getImmatriculation() : "")
                            .append("</td>\n");
                    html.append("        <td>").append(v.getMarque() != null ? v.getMarque() : "").append("</td>\n");
                    html.append("        <td>").append(v.getModele() != null ? v.getModele() : "").append("</td>\n");
                    html.append("        <td>")
                            .append(v.getEtatVoiture() != null ? v.getEtatVoiture().getLibEtatVoiture() : "")
                            .append("</td>\n");
                    html.append("        <td>").append(v.getKmActuels() != null ? v.getKmActuels() : "")
                            .append(" km</td>\n");
                    html.append("      </tr>\n");
                }

                html.append("    </table>\n");
            } else {
                html.append("    <p>Aucun véhicule disponible.</p>\n");
            }

            html.append("  </div>\n");
        }

        // Si le rapport contient des missions
        if (rapport.containsKey("missions")) {
            @SuppressWarnings("unchecked")
            List<Mission> missions = (List<Mission>) rapport.get("missions");

            html.append("  <div class=\"container\">\n");
            html.append("    <h2>Liste des missions</h2>\n");

            if (missions != null && !missions.isEmpty()) {
                html.append("    <p>Nombre de missions: ").append(missions.size()).append("</p>\n");
                html.append("    <table>\n");
                html.append("      <tr>\n");
                html.append("        <th>ID</th>\n");
                html.append("        <th>Libellé</th>\n");
                html.append("        <th>Site</th>\n");
                html.append("        <th>Date début</th>\n");
                html.append("        <th>Date fin</th>\n");
                html.append("        <th>Statut</th>\n");
                html.append("        <th>Km réel</th>\n");
                html.append("        <th>Coût</th>\n");
                html.append("      </tr>\n");

                for (Mission m : missions) {
                    html.append("      <tr>\n");
                    html.append("        <td>").append(m.getIdMission() != null ? m.getIdMission() : "")
                            .append("</td>\n");
                    html.append("        <td>").append(m.getLibMission() != null ? m.getLibMission() : "")
                            .append("</td>\n");
                    html.append("        <td>").append(m.getSite() != null ? m.getSite() : "").append("</td>\n");
                    html.append("        <td>")
                            .append(m.getDateDebutMission() != null
                                    ? m.getDateDebutMission().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                    : "")
                            .append("</td>\n");
                    html.append("        <td>")
                            .append(m.getDateFinMission() != null
                                    ? m.getDateFinMission().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                    : "")
                            .append("</td>\n");
                    html.append("        <td>").append(m.getStatus() != null ? m.getStatus().getLibelle() : "")
                            .append("</td>\n");
                    html.append("        <td>").append(m.getKmReel() != null ? m.getKmReel() : "").append(" km</td>\n");
                    html.append("        <td>").append(m.getCoutTotal() != null ? m.getCoutTotal() : "")
                            .append(" €</td>\n");
                    html.append("      </tr>\n");
                }

                html.append("    </table>\n");
            } else {
                html.append("    <p>Aucune mission disponible.</p>\n");
            }

            html.append("  </div>\n");
        }

        // Si le rapport contient des entretiens
        if (rapport.containsKey("entretiens")) {
            @SuppressWarnings("unchecked")
            List<Entretien> entretiens = (List<Entretien>) rapport.get("entretiens");

            html.append("  <div class=\"container\">\n");
            html.append("    <h2>Liste des entretiens</h2>\n");

            if (entretiens != null && !entretiens.isEmpty()) {
                html.append("    <p>Nombre d'entretiens: ").append(entretiens.size()).append("</p>\n");
                html.append("    <table>\n");
                html.append("      <tr>\n");
                html.append("        <th>ID</th>\n");
                html.append("        <th>Véhicule ID</th>\n");
                html.append("        <th>Date entrée</th>\n");
                html.append("        <th>Date sortie</th>\n");
                html.append("        <th>Motif</th>\n");
                html.append("        <th>Type</th>\n");
                html.append("        <th>Statut</th>\n");
                html.append("        <th>Coût</th>\n");
                html.append("      </tr>\n");

                for (Entretien e : entretiens) {
                    html.append("      <tr>\n");
                    html.append("        <td>").append(e.getIdEntretien() != null ? e.getIdEntretien() : "")
                            .append("</td>\n");
                    html.append("        <td>").append(e.getIdVehicule() != null ? e.getIdVehicule() : "")
                            .append("</td>\n");
                    html.append("        <td>")
                            .append(e.getDateEntreeEntr() != null
                                    ? e.getDateEntreeEntr().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                    : "")
                            .append("</td>\n");
                    html.append("        <td>")
                            .append(e.getDateSortieEntr() != null
                                    ? e.getDateSortieEntr().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                    : "")
                            .append("</td>\n");
                    html.append("        <td>").append(e.getMotifEntr() != null ? e.getMotifEntr() : "")
                            .append("</td>\n");
                    html.append("        <td>").append(e.getType() != null ? e.getType().name() : "").append("</td>\n");
                    html.append("        <td>").append(e.getStatutOt() != null ? e.getStatutOt().name() : "")
                            .append("</td>\n");
                    html.append("        <td>").append(e.getCoutEntr() != null ? e.getCoutEntr() : "")
                            .append(" €</td>\n");
                    html.append("      </tr>\n");
                }

                html.append("    </table>\n");
            } else {
                html.append("    <p>Aucun entretien disponible.</p>\n");
            }

            html.append("  </div>\n");
        }

        // Si le rapport contient un bilan financier
        if (rapport.containsKey("bilanFinancier")) {
            FinanceDao.BilanFinancier bilan = (FinanceDao.BilanFinancier) rapport.get("bilanFinancier");

            html.append("  <div class=\"container\">\n");
            html.append("    <h2>Bilan financier</h2>\n");

            if (bilan != null) {
                html.append("    <table>\n");
                html.append("      <tr><td>Recettes totales:</td><td>").append(bilan.getTotalRecettes())
                        .append(" €</td></tr>\n");
                html.append("      <tr><td>Dépenses totales:</td><td>").append(bilan.getTotalDepenses())
                        .append(" €</td></tr>\n");
                html.append("      <tr><td>Dépenses missions:</td><td>").append(bilan.getTotalMissions())
                        .append(" €</td></tr>\n");
                html.append("      <tr><td>Dépenses entretiens:</td><td>").append(bilan.getTotalEntretiens())
                        .append(" €</td></tr>\n");
                html.append("      <tr><td>Dépenses assurances:</td><td>").append(bilan.getTotalAssurances())
                        .append(" €</td></tr>\n");
                html.append("      <tr><td>Solde:</td><td>").append(bilan.getSolde()).append(" €</td></tr>\n");
                html.append("      <tr><td>Marge:</td><td>").append(bilan.getMargePct()).append(" %</td></tr>\n");
                html.append("    </table>\n");

                if (bilan.isPositif()) {
                    html.append("    <div class=\"success\">Bilan positif</div>\n");
                } else {
                    html.append("    <div class=\"alert\">Bilan négatif</div>\n");
                }
            } else {
                html.append("    <p>Aucun bilan financier disponible.</p>\n");
            }

            html.append("  </div>\n");
        }

        // Si le rapport contient des alertes d'assurance
        if (rapport.containsKey("alertesAssurance")) {
            @SuppressWarnings("unchecked")
            List<FinanceDao.AlerteAssurance> alertes = (List<FinanceDao.AlerteAssurance>) rapport
                    .get("alertesAssurance");

            html.append("  <div class=\"container\">\n");
            html.append("    <h2>Alertes d'assurance</h2>\n");

            if (alertes != null && !alertes.isEmpty()) {
                html.append("    <p>Nombre d'alertes: ").append(alertes.size()).append("</p>\n");
                html.append("    <table>\n");
                html.append("      <tr>\n");
                html.append("        <th>Véhicule</th>\n");
                html.append("        <th>Immatriculation</th>\n");
                html.append("        <th>Date expiration</th>\n");
                html.append("        <th>Jours restants</th>\n");
                html.append("        <th>Agence</th>\n");
                html.append("        <th>Coût</th>\n");
                html.append("      </tr>\n");

                for (FinanceDao.AlerteAssurance alerte : alertes) {
                    html.append("      <tr>\n");
                    html.append("        <td>").append(alerte.getMarque()).append(" ").append(alerte.getModele())
                            .append("</td>\n");
                    html.append("        <td>").append(alerte.getImmatriculation()).append("</td>\n");
                    html.append("        <td>").append(alerte.getFormattedDateFin()).append("</td>\n");

                    if (alerte.getJoursRestants() <= 7) {
                        html.append("        <td style=\"color: red; font-weight: bold;\">")
                                .append(alerte.getJoursRestants()).append(" jours</td>\n");
                    } else if (alerte.getJoursRestants() <= 30) {
                        html.append("        <td style=\"color: orange; font-weight: bold;\">")
                                .append(alerte.getJoursRestants()).append(" jours</td>\n");
                    } else {
                        html.append("        <td>").append(alerte.getJoursRestants()).append(" jours</td>\n");
                    }

                    html.append("        <td>").append(alerte.getAgence()).append("</td>\n");
                    html.append("        <td>").append(alerte.getCoutAssurance()).append(" €</td>\n");
                    html.append("      </tr>\n");
                }

                html.append("    </table>\n");
            } else {
                html.append("    <p>Aucune alerte d'assurance à signaler.</p>\n");
            }

            html.append("  </div>\n");
        }

        // Affichage des statistiques globales si présentes
        if (rapport.containsKey("totalVehicules")) {
            html.append("  <div class=\"container\">\n");
            html.append("    <h2>Statistiques globales</h2>\n");
            html.append("    <table>\n");

            for (Map.Entry<String, Object> entry : rapport.entrySet()) {
                if (entry.getValue() instanceof Number || entry.getValue() instanceof String
                        || entry.getValue() instanceof Boolean) {
                    if (!entry.getKey().contains("liste") && !entry.getKey().contains("repartition")) {
                        html.append("      <tr><td>").append(formatKeyForDisplay(entry.getKey())).append(":</td><td>")
                                .append(formatValueForDisplay(entry.getValue())).append("</td></tr>\n");
                    }
                }
            }

            html.append("    </table>\n");
            html.append("  </div>\n");
        }
    }

    /**
     * Formate une clé pour l'affichage HTML.
     *
     * @param key La clé à formater
     * @return La clé formatée
     */
    private String formatKeyForDisplay(String key) {
        // Convertir camelCase en mots espacés et capitaliser
        String result = key.replaceAll("([a-z])([A-Z])", "$1 $2");
        result = Character.toUpperCase(result.charAt(0)) + result.substring(1);
        return result;
    }

    /**
     * Formate une valeur pour l'affichage HTML.
     *
     * @param value La valeur à formater
     * @return La valeur formatée
     */
    private String formatValueForDisplay(Object value) {
        if (value instanceof BigDecimal) {
            return value + " €";
        } else if (value instanceof Double || value instanceof Float) {
            double doubleValue = ((Number) value).doubleValue();
            if (doubleValue % 1 == 0) {
                return String.format("%.0f", doubleValue); // Pas de décimales pour les entiers
            } else {
                return String.format("%.2f", doubleValue); // Deux décimales pour les autres
            }
        } else if (value instanceof Boolean) {
            return ((Boolean) value) ? "Oui" : "Non";
        } else {
            return value.toString();
        }
    }

    /**
     * Exporte un rapport au format CSV.
     *
     * @param data       Données du rapport sous forme de liste de listes
     * @param headers    En-têtes des colonnes
     * @param nomFichier Nom du fichier sans extension
     * @return Chemin du fichier CSV généré ou null en cas d'erreur
     */
    public String exporterRapportCSV(List<List<String>> data, List<String> headers, String nomFichier) {
        // Génération d'un nom de fichier
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = REPORTS_DIRECTORY + "/" + nomFichier.replaceAll("\\s+", "_").toLowerCase() + "_" + timestamp
                + ".csv";

        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            // Écriture des en-têtes
            String headerLine = String.join(";", headers) + "\n";
            fos.write(headerLine.getBytes());

            // Écriture des données
            for (List<String> row : data) {
                String rowLine = String.join(";", row) + "\n";
                fos.write(rowLine.getBytes());
            }

            LOGGER.info("Rapport CSV généré: " + fileName);
            return fileName;

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'export du rapport CSV", e);
            return null;
        }
    }

    /**
     * Calcule le bilan financier du mois en cours.
     *
     * @return BilanFinancier du mois ou null en cas d'erreur
     */
    private FinanceDao.BilanFinancier calculerBilanMensuel() {
        try {
            LocalDateTime debut = LocalDateTime.now()
                    .withDayOfMonth(1)
                    .withHour(0)
                    .withMinute(0)
                    .withSecond(0);

            LocalDateTime fin = LocalDateTime.now()
                    .withDayOfMonth(LocalDateTime.now().toLocalDate().lengthOfMonth())
                    .withHour(23)
                    .withMinute(59)
                    .withSecond(59);

            return financeDao.calculerBilanFinancier(debut, fin);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul du bilan financier mensuel", e);
            return null;
        }
    }

    /**
     * Génère un rapport détaillé sur un véhicule spécifique.
     *
     * @param idVehicule ID du véhicule
     * @return Map contenant les données du rapport ou map vide en cas d'erreur
     */
    public Map<String, Object> genererRapportVehicule(int idVehicule) {
        Map<String, Object> rapport = new HashMap<>();

        try {
            // Récupération du véhicule
            Optional<Vehicule> vehiculeOpt = vehiculeDao.findById(idVehicule);
            if (!vehiculeOpt.isPresent()) {
                LOGGER.warning("Véhicule non trouvé avec l'ID: " + idVehicule);
                return Collections.emptyMap();
            }

            Vehicule vehicule = vehiculeOpt.get();
            rapport.put("vehicule", vehicule);

            // Informations générales
            rapport.put("immatriculation", vehicule.getImmatriculation());
            rapport.put("marque", vehicule.getMarque());
            rapport.put("modele", vehicule.getModele());
            rapport.put("numeroChassis", vehicule.getNumeroChassi());
            rapport.put("etatActuel", vehicule.getEtatVoiture().getLibEtatVoiture());
            rapport.put("kmActuels", vehicule.getKmActuels());
            rapport.put("energie", vehicule.getEnergie().name());

            // Coût Total de Possession
            FinanceDao.TCOVehicule tco = financeDao.calculerCoutTotalPossession(idVehicule);
            rapport.put("tco", tco);

            // Historique des missions
            List<Mission> missions = missionDao.findByVehicule(idVehicule);
            rapport.put("missions", missions);
            rapport.put("nombreMissions", missions.size());

            // Calcul du taux d'utilisation (jours en mission / jours depuis acquisition)
            // Code corrigé
            long joursUtilisation = missions.stream()
                    .mapToLong(Mission::getDureeJours) // Utiliser mapToLong au lieu de mapToInt
                    .sum();

            int joursDepuisAcquisition = 365; // Par défaut
            if (vehicule.getDateAcquisition() != null) {
                joursDepuisAcquisition = (int) ChronoUnit.DAYS.between(
                        vehicule.getDateAcquisition().toLocalDate(),
                        LocalDate.now()) + 1;
            }

            double tauxUtilisation = (joursUtilisation * 100.0) / joursDepuisAcquisition;
            rapport.put("joursUtilisation", joursUtilisation);
            rapport.put("joursDepuisAcquisition", joursDepuisAcquisition);
            rapport.put("tauxUtilisation", tauxUtilisation);

            // Historique des entretiens
            List<Entretien> entretiens = entretienDao.findByVehicule(idVehicule);
            rapport.put("entretiens", entretiens);
            rapport.put("nombreEntretiens", entretiens.size());

            // Coût total des entretiens
            BigDecimal coutTotalEntretiens = entretiens.stream()
                    .filter(e -> e.getCoutEntr() != null)
                    .map(Entretien::getCoutEntr)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            rapport.put("coutTotalEntretiens", coutTotalEntretiens);

            // Assurances actuelles
            List<Assurance> assurances = new ArrayList<>();
            // Code pour récupérer les assurances du véhicule (à implémenter)
            rapport.put("assurances", assurances);

            // Prochains entretiens prévus
            List<Entretien> entretiensPreventifs = entretiens.stream()
                    .filter(e -> e.getType() == Entretien.TypeEntretien.Preventif
                            && e.getStatutOt() == Entretien.StatutOT.Ouvert)
                    .collect(Collectors.toList());
            rapport.put("entretiensPreventifsPrevus", entretiensPreventifs);

            return rapport;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la génération du rapport pour le véhicule ID: " + idVehicule, e);
            return Collections.emptyMap();
        }
    }

    /**
     * Génère un rapport sur le cycle de vie d'une mission.
     *
     * @param idMission ID de la mission
     * @return Map contenant les données du rapport ou map vide en cas d'erreur
     */
    public Map<String, Object> genererRapportMission(int idMission) {
        Map<String, Object> rapport = new HashMap<>();

        try {
            // Récupération de la mission
            Optional<Mission> missionOpt = missionDao.findById(idMission);
            if (!missionOpt.isPresent()) {
                LOGGER.warning("Mission non trouvée avec l'ID: " + idMission);
                return Collections.emptyMap();
            }

            Mission mission = missionOpt.get();
            rapport.put("mission", mission);

            // Informations générales
            rapport.put("libelleMission", mission.getLibMission());
            rapport.put("site", mission.getSite());
            rapport.put("dateDebut", mission.getDateDebutMission());
            rapport.put("dateFin", mission.getDateFinMission());
            rapport.put("statut", mission.getStatus().getLibelle());
            rapport.put("kmPrevu", mission.getKmPrevu());
            rapport.put("kmReel", mission.getKmReel());
            rapport.put("coutTotal", mission.getCoutTotal());
            rapport.put("dureeJours", mission.getDureeJours());

            // Informations sur le véhicule associé
            int idVehicule = mission.getIdVehicule();
            Optional<Vehicule> vehiculeOpt = vehiculeDao.findById(idVehicule);
            if (vehiculeOpt.isPresent()) {
                Vehicule vehicule = vehiculeOpt.get();
                rapport.put("vehicule", vehicule);
                rapport.put("immatriculation", vehicule.getImmatriculation());
                rapport.put("marqueModele", vehicule.getMarque() + " " + vehicule.getModele());
            }

            // Dépenses de la mission
            List<DepenseMission> depenses = missionDao.getDepenses(idMission);
            rapport.put("depenses", depenses);

            // Répartition des dépenses par nature
            Map<DepenseMission.NatureDepense, BigDecimal> repartitionDepenses = depenses.stream()
                    .collect(Collectors.groupingBy(
                            DepenseMission::getNature,
                            Collectors.reducing(BigDecimal.ZERO, DepenseMission::getMontant, BigDecimal::add)));
            rapport.put("repartitionDepenses", repartitionDepenses);

            return rapport;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la génération du rapport pour la mission ID: " + idMission, e);
            return Collections.emptyMap();
        }
    }

    /**
     * Génère un rapport sur les activités d'un compte sociétaire.
     *
     * @param idSocietaire ID du compte sociétaire
     * @return Map contenant les données du rapport ou map vide en cas d'erreur
     */
    public Map<String, Object> genererRapportCompteSocietaire(int idSocietaire) {
        Map<String, Object> rapport = new HashMap<>();

        try {
            // Récupération du compte sociétaire
            Optional<SocieteCompte> compteOpt = societeCompteDao.findById(idSocietaire);
            if (!compteOpt.isPresent()) {
                LOGGER.warning("Compte sociétaire non trouvé avec l'ID: " + idSocietaire);
                return Collections.emptyMap();
            }

            SocieteCompte compte = compteOpt.get();
            rapport.put("compte", compte);

            // Informations générales
            rapport.put("nom", compte.getNom());
            rapport.put("numero", compte.getNumero());
            rapport.put("soldeActuel", compte.getSolde());
            rapport.put("email", compte.getEmail());
            rapport.put("telephone", compte.getTelephone());

            // Récupération des mouvements
            List<Mouvement> mouvements = mouvementDao.findBySocietaire(idSocietaire);
            rapport.put("mouvements", mouvements);
            rapport.put("nombreMouvements", mouvements.size());

            // Statistiques des mouvements
            BigDecimal totalDepots = mouvements.stream()
                    .filter(m -> m.getType() == Mouvement.TypeMouvement.Depot)
                    .map(Mouvement::getMontant)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            rapport.put("totalDepots", totalDepots);

            BigDecimal totalRetraits = mouvements.stream()
                    .filter(m -> m.getType() == Mouvement.TypeMouvement.Retrait)
                    .map(Mouvement::getMontant)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            rapport.put("totalRetraits", totalRetraits);

            BigDecimal totalMensualites = mouvements.stream()
                    .filter(m -> m.getType() == Mouvement.TypeMouvement.Mensualite)
                    .map(Mouvement::getMontant)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            rapport.put("totalMensualites", totalMensualites);

            // Si personnel associé
            if (compte.getIdPersonnel() != null) {
                // Code pour récupérer les informations du personnel associé
                rapport.put("idPersonnel", compte.getIdPersonnel());
            }

            return rapport;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la génération du rapport pour le compte sociétaire ID: " + idSocietaire, e);
            return Collections.emptyMap();
        }
    }
}