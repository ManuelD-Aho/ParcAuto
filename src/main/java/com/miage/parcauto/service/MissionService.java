package main.java.com.miage.parcauto.service;

import main.java.com.miage.parcauto.dao.MissionDao;
import main.java.com.miage.parcauto.dao.VehiculeDao;
import main.java.com.miage.parcauto.model.mission.DepenseMission;
import main.java.com.miage.parcauto.model.mission.Mission;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;
import main.java.com.miage.parcauto.model.vehicule.EtatVoiture;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service de gestion des missions.
 * Cette classe implémente la couche service pour toutes les opérations liées
 * aux missions.
 * Elle sert d'intermédiaire entre la couche DAO et la couche de présentation
 * (contrôleurs).
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class MissionService {

    private static final Logger LOGGER = Logger.getLogger(MissionService.class.getName());

    private final MissionDao missionDao;
    private final VehiculeDao vehiculeDao;

    /**
     * Constructeur par défaut.
     */
    public MissionService() {
        this.missionDao = new MissionDao();
        this.vehiculeDao = new VehiculeDao();
    }

    /**
     * Constructeur avec injection de dépendance pour les tests.
     *
     * @param missionDao  Instance de MissionDao à utiliser
     * @param vehiculeDao Instance de VehiculeDao à utiliser
     */
    public MissionService(MissionDao missionDao, VehiculeDao vehiculeDao) {
        this.missionDao = missionDao;
        this.vehiculeDao = vehiculeDao;
    }

    /**
     * Récupère toutes les missions.
     *
     * @return Liste de toutes les missions ou liste vide en cas d'erreur
     */
    public List<Mission> getAllMissions() {
        try {
            return missionDao.findAll();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de toutes les missions", e);
            return Collections.emptyList();
        }
    }

    /**
     * Recherche une mission par son ID.
     *
     * @param id ID de la mission
     * @return Optional contenant la mission si elle existe
     */
    public Optional<Mission> getMissionById(int id) {
        try {
            return missionDao.findById(id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de la mission par ID: " + id, e);
            return Optional.empty();
        }
    }

    /**
     * Récupère les missions pour un véhicule spécifique.
     *
     * @param idVehicule ID du véhicule
     * @return Liste des missions associées au véhicule ou liste vide en cas
     *         d'erreur
     */
    public List<Mission> getMissionsByVehicule(int idVehicule) {
        try {
            return missionDao.findByVehicule(idVehicule);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des missions pour le véhicule ID: " + idVehicule,
                    e);
            return Collections.emptyList();
        }
    }

    /**
     * Récupère les missions selon leur statut.
     *
     * @param status Statut des missions à récupérer
     * @return Liste des missions ayant ce statut ou liste vide en cas d'erreur
     */
    public List<Mission> getMissionsByStatus(Mission.StatusMission status) {
        try {
            return missionDao.findByStatus(status);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des missions par statut: " + status, e);
            return Collections.emptyList();
        }
    }

    /**
     * Récupère les missions par période.
     *
     * @param debut Date de début de la période
     * @param fin   Date de fin de la période
     * @return Liste des missions pendant cette période ou liste vide en cas
     *         d'erreur
     */
    public List<Mission> getMissionsByPeriode(LocalDateTime debut, LocalDateTime fin) {
        try {
            return missionDao.findByPeriode(debut, fin);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des missions par période", e);
            return Collections.emptyList();
        }
    }

    /**
     * Récupère les missions par site.
     *
     * @param site Site des missions à récupérer
     * @return Liste des missions pour ce site ou liste vide en cas d'erreur
     */
    public List<Mission> getMissionsBySite(String site) {
        try {
            return missionDao.findBySite(site);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des missions par site: " + site, e);
            return Collections.emptyList();
        }
    }

    /**
     * Recherche de missions par critères textuels.
     *
     * @param searchTerm Terme de recherche
     * @return Liste des missions correspondant aux critères ou liste vide en cas
     *         d'erreur
     */
    public List<Mission> searchMissions(String searchTerm) {
        try {
            return missionDao.search(searchTerm);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des missions avec le terme: " + searchTerm, e);
            return Collections.emptyList();
        }
    }

    /**
     * Récupère les missions en cours.
     *
     * @return Liste des missions en cours ou liste vide en cas d'erreur
     */
    public List<Mission> getMissionsEnCours() {
        try {
            return missionDao.findMissionsEnCours();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des missions en cours", e);
            return Collections.emptyList();
        }
    }

    /**
     * Récupère les missions planifiées.
     *
     * @return Liste des missions planifiées ou liste vide en cas d'erreur
     */
    public List<Mission> getMissionsPlanifiees() {
        try {
            return missionDao.findMissionsPlanifiees();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des missions planifiées", e);
            return Collections.emptyList();
        }
    }

    /**
     * Récupère les missions clôturées.
     *
     * @return Liste des missions clôturées ou liste vide en cas d'erreur
     */
    public List<Mission> getMissionsCloturees() {
        try {
            return missionDao.findMissionsCloturees();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des missions clôturées", e);
            return Collections.emptyList();
        }
    }

    /**
     * Crée une nouvelle mission.
     *
     * @param mission            La mission à créer
     * @param updateVehiculeEtat Si true, met à jour l'état du véhicule
     * @return La mission créée avec son ID généré ou null en cas d'erreur
     */
    public Mission createMission(Mission mission, boolean updateVehiculeEtat) {
        try {
            // Validation des données
            if (!validateMission(mission)) {
                LOGGER.warning("Validation de la mission échouée");
                return null;
            }

            // Vérifier que le véhicule existe
            Optional<Vehicule> vehicule = vehiculeDao.findById(mission.getIdVehicule());
            if (!vehicule.isPresent()) {
                LOGGER.warning("Véhicule non trouvé avec l'ID: " + mission.getIdVehicule());
                return null;
            }

            // Vérifier que le véhicule est disponible pour la période
            if (!isVehiculeDisponiblePourMission(mission)) {
                LOGGER.warning("Véhicule non disponible pour la période demandée");
                return null;
            }

            // Pour les missions immédiatement en cours, on vérifie que le véhicule est
            // disponible
            if (updateVehiculeEtat && mission.getStatus() == Mission.StatusMission.EnCours &&
                    !vehicule.get().estDisponible()) {
                LOGGER.warning("Le véhicule n'est pas disponible pour démarrer une mission");
                return null;
            }

            return missionDao.create(mission, updateVehiculeEtat);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création de la mission", e);
            return null;
        }
    }

    /**
     * Met à jour une mission.
     *
     * @param mission            La mission à mettre à jour
     * @param updateVehiculeEtat Si true, met à jour l'état du véhicule si
     *                           nécessaire
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateMission(Mission mission, boolean updateVehiculeEtat) {
        try {
            // Validation des données
            if (!validateMission(mission) || mission.getIdMission() == null) {
                LOGGER.warning("Validation de la mission échouée ou ID manquant");
                return false;
            }

            // Vérifier que la mission existe
            Optional<Mission> existingMission = missionDao.findById(mission.getIdMission());
            if (!existingMission.isPresent()) {
                LOGGER.warning("Mission non trouvée avec l'ID: " + mission.getIdMission());
                return false;
            }

            // Si le véhicule a changé, vérifier que le nouveau véhicule existe et est
            // disponible
            if (!mission.getIdVehicule().equals(existingMission.get().getIdVehicule())) {
                Optional<Vehicule> vehicule = vehiculeDao.findById(mission.getIdVehicule());
                if (!vehicule.isPresent()) {
                    LOGGER.warning("Véhicule non trouvé avec l'ID: " + mission.getIdVehicule());
                    return false;
                }

                // Vérifier que le véhicule est disponible pour la période
                if (!isVehiculeDisponiblePourMission(mission)) {
                    LOGGER.warning("Véhicule non disponible pour la période demandée");
                    return false;
                }
            } else {
                // Même véhicule, on vérifie que la période n'entre pas en conflit avec d'autres
                // missions
                if (!isVehiculeDisponiblePourMission(mission)) {
                    LOGGER.warning("Véhicule non disponible pour la période demandée");
                    return false;
                }
            }

            return missionDao.update(mission, updateVehiculeEtat);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de la mission", e);
            return false;
        }
    }

    /**
     * Met à jour le statut d'une mission.
     *
     * @param idMission          ID de la mission
     * @param status             Nouveau statut
     * @param kmReel             Kilométrage réel (obligatoire pour les missions
     *                           clôturées)
     * @param updateVehiculeEtat Si true, met à jour l'état du véhicule selon le
     *                           statut
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateMissionStatus(int idMission, Mission.StatusMission status, Integer kmReel,
            boolean updateVehiculeEtat) {
        try {
            // Vérifier que la mission existe
            Optional<Mission> mission = missionDao.findById(idMission);
            if (!mission.isPresent()) {
                LOGGER.warning("Mission non trouvée avec l'ID: " + idMission);
                return false;
            }

            // Vérifier que le changement de statut est logique
            if (!isValidStatusTransition(mission.get().getStatus(), status)) {
                LOGGER.warning("Transition de statut non valide: " + mission.get().getStatus() + " -> " + status);
                return false;
            }

            // Pour les missions clôturées, le kilométrage réel est obligatoire
            if (status == Mission.StatusMission.Cloturee && (kmReel == null || kmReel < 0)) {
                LOGGER.warning("Kilométrage réel manquant ou invalide pour clôturer une mission");
                return false;
            }

            // Pour les missions en cours, vérifier que le véhicule est disponible
            if (status == Mission.StatusMission.EnCours && updateVehiculeEtat) {
                Optional<Vehicule> vehicule = vehiculeDao.findById(mission.get().getIdVehicule());
                if (vehicule.isPresent() && !vehicule.get().estDisponible()) {
                    LOGGER.warning("Le véhicule n'est pas disponible pour démarrer une mission");
                    return false;
                }
            }

            return missionDao.updateStatus(idMission, status, kmReel, updateVehiculeEtat);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du statut de la mission", e);
            return false;
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING,
                    "Argument invalide lors de la mise à jour du statut de la mission: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Démarre une mission (passage au statut "en cours").
     *
     * @param idMission ID de la mission
     * @return true si le changement d'état a été effectué, false sinon
     */
    public boolean demarrerMission(int idMission) {
        try {
            return missionDao.demarrerMission(idMission);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du démarrage de la mission: " + idMission, e);
            return false;
        }
    }

    /**
     * Clôture une mission (passage au statut "clôturée").
     *
     * @param idMission ID de la mission
     * @param kmReel    Kilométrage réel (obligatoire)
     * @return true si le changement d'état a été effectué, false sinon
     */
    public boolean cloturerMission(int idMission, int kmReel) {
        try {
            // Vérifier que le kilométrage réel est valide
            if (kmReel < 0) {
                LOGGER.warning("Kilométrage réel invalide: " + kmReel);
                return false;
            }

            return missionDao.cloturerMission(idMission, kmReel);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la clôture de la mission: " + idMission, e);
            return false;
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Argument invalide lors de la clôture de la mission: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Supprime une mission.
     *
     * @param idMission   ID de la mission à supprimer
     * @param forceDelete Si true, supprime la mission même si elle est en cours
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deleteMission(int idMission, boolean forceDelete) {
        try {
            return missionDao.delete(idMission, forceDelete);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de la mission: " + idMission, e);
            return false;
        } catch (IllegalStateException e) {
            LOGGER.log(Level.WARNING, "État illégal lors de la suppression de la mission: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Ajoute une dépense à une mission.
     *
     * @param depense La dépense à ajouter
     * @return La dépense créée avec son ID généré ou null en cas d'erreur
     */
    public DepenseMission addDepense(DepenseMission depense) {
        try {
            // Validation des données
            if (!validateDepense(depense)) {
                LOGGER.warning("Validation de la dépense échouée");
                return null;
            }

            // Vérifier que la mission existe
            Optional<Mission> mission = missionDao.findById(depense.getIdMission());
            if (!mission.isPresent()) {
                LOGGER.warning("Mission non trouvée avec l'ID: " + depense.getIdMission());
                return null;
            }

            return missionDao.addDepense(depense);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'ajout de la dépense", e);
            return null;
        }
    }

    /**
     * Récupère les dépenses d'une mission.
     *
     * @param idMission ID de la mission
     * @return Liste des dépenses associées à la mission ou liste vide en cas
     *         d'erreur
     */
    public List<DepenseMission> getDepensesByMission(int idMission) {
        try {
            return missionDao.getDepenses(idMission);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des dépenses pour la mission ID: " + idMission, e);
            return Collections.emptyList();
        }
    }

    /**
     * Supprime une dépense de mission.
     *
     * @param idDepense ID de la dépense à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deleteDepense(int idDepense) {
        try {
            return missionDao.deleteDepense(idDepense);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de la dépense: " + idDepense, e);
            return false;
        }
    }

    /**
     * Calcule les statistiques des missions.
     *
     * @param annee Année pour les statistiques (0 pour toutes les années)
     * @return Les statistiques des missions ou null en cas d'erreur
     */
    public MissionDao.MissionStats calculateMissionStats(int annee) {
        try {
            return missionDao.calculateStats(annee);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul des statistiques des missions", e);
            return null;
        }
    }

    /**
     * Vérifie la disponibilité d'un véhicule pour une période de mission.
     *
     * @param idVehicule       ID du véhicule
     * @param debut            Date de début de la période
     * @param fin              Date de fin de la période
     * @param exclureMissionId ID d'une mission à exclure (utile pour les mises à
     *                         jour)
     * @return true si le véhicule est disponible, false sinon
     */
    public boolean isVehiculeDisponible(int idVehicule, LocalDateTime debut, LocalDateTime fin,
            Integer exclureMissionId) {
        try {
            return missionDao.isVehiculeDisponible(idVehicule, debut, fin, exclureMissionId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la vérification de la disponibilité du véhicule ID: " + idVehicule,
                    e);
            return false;
        }
    }

    /**
     * Planifie une mission pour un véhicule.
     *
     * @param idVehicule ID du véhicule
     * @param libMission Libellé de la mission
     * @param site       Site de la mission
     * @param dateDebut  Date de début de la mission
     * @param dateFin    Date de fin de la mission
     * @param kmPrevu    Kilométrage prévu
     * @return La mission créée avec son ID généré ou null en cas d'erreur
     */
    public Mission planifierMission(int idVehicule, String libMission, String site, LocalDateTime dateDebut,
            LocalDateTime dateFin, Integer kmPrevu) {
        Mission mission = new Mission();
        mission.setIdVehicule(idVehicule);
        mission.setLibMission(libMission);
        mission.setSite(site);
        mission.setDateDebutMission(dateDebut);
        mission.setDateFinMission(dateFin);
        mission.setKmPrevu(kmPrevu);
        mission.setStatus(Mission.StatusMission.Planifiee);
        mission.setCoutTotal(BigDecimal.ZERO);

        return createMission(mission, false);
    }

    /**
     * Recherche des missions qui ont lieu aujourd'hui.
     *
     * @return Liste des missions du jour ou liste vide en cas d'erreur
     */
    public List<Mission> getMissionsDuJour() {
        try {
            LocalDateTime debut = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
            LocalDateTime fin = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

            return missionDao.findByPeriode(debut, fin);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des missions du jour", e);
            return Collections.emptyList();
        }
    }

    /**
     * Recherche des missions en retard (dépassant la date de fin prévue).
     *
     * @return Liste des missions en retard ou liste vide en cas d'erreur
     */
    public List<Mission> getMissionsEnRetard() {
        try {
            List<Mission> missionsEnCours = missionDao.findMissionsEnCours();

            // Filtrer les missions dont la date de fin est dépassée
            LocalDateTime now = LocalDateTime.now();
            return missionsEnCours.stream()
                    .filter(m -> m.getDateFinMission() != null && now.isAfter(m.getDateFinMission()))
                    .toList();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des missions en retard", e);
            return Collections.emptyList();
        }
    }

    /**
     * Retourne le nombre de missions en cours.
     * 
     * @return nombre de missions en cours, ou 0 en cas d'erreur
     */
    public int getMissionsEnCoursCount() {
        try {
            MissionDao.MissionStats stats = missionDao.calculateStats(0);
            return stats != null ? stats.getEnCours() : 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des missions en cours", e);
            return 0;
        }
    }

    /**
     * Valide les données d'une mission.
     *
     * @param mission La mission à valider
     * @return true si les données sont valides, false sinon
     */
    private boolean validateMission(Mission mission) {
        if (mission == null) {
            return false;
        }

        // Vérifier que les champs obligatoires sont renseignés
        if (mission.getIdVehicule() == null ||
                mission.getLibMission() == null || mission.getLibMission().trim().isEmpty() ||
                mission.getSite() == null || mission.getSite().trim().isEmpty() ||
                mission.getDateDebutMission() == null) {
            return false;
        }

        // Vérifier que la date de fin est après la date de début
        if (mission.getDateFinMission() != null &&
                mission.getDateDebutMission().isAfter(mission.getDateFinMission())) {
            return false;
        }

        // Vérifier que le kilométrage prévu est positif
        if (mission.getKmPrevu() != null && mission.getKmPrevu() < 0) {
            return false;
        }

        // Vérifier que le kilométrage réel est positif
        if (mission.getKmReel() != null && mission.getKmReel() < 0) {
            return false;
        }

        // Vérifier que le coût total est positif
        if (mission.getCoutTotal() != null && mission.getCoutTotal().compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }

        return true;
    }

    /**
     * Valide les données d'une dépense.
     *
     * @param depense La dépense à valider
     * @return true si les données sont valides, false sinon
     */
    private boolean validateDepense(DepenseMission depense) {
        if (depense == null) {
            return false;
        }

        // Vérifier que les champs obligatoires sont renseignés
        if (depense.getIdMission() == null ||
                depense.getNature() == null ||
                depense.getMontant() == null) {
            return false;
        }

        // Vérifier que le montant est positif
        if (depense.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        return true;
    }

    /**
     * Vérifie que la transition de statut est valide.
     *
     * @param currentStatus Statut actuel
     * @param newStatus     Nouveau statut
     * @return true si la transition est valide, false sinon
     */
    private boolean isValidStatusTransition(Mission.StatusMission currentStatus, Mission.StatusMission newStatus) {
        if (currentStatus == newStatus) {
            return true; // Même statut, pas de changement
        }

        switch (currentStatus) {
            case Planifiee:
                // De Planifiée, on peut passer à En cours ou Clôturée
                return newStatus == Mission.StatusMission.EnCours || newStatus == Mission.StatusMission.Cloturee;
            case EnCours:
                // De En cours, on ne peut passer qu'à Clôturée
                return newStatus == Mission.StatusMission.Cloturee;
            case Cloturee:
                // De Clôturée, on ne devrait pas changer de statut
                return false;
            default:
                return false;
        }
    }

    /**
     * Vérifie si un véhicule est disponible pour une mission (nouvelle ou mise à
     * jour).
     *
     * @param mission La mission à vérifier
     * @return true si le véhicule est disponible, false sinon
     */
    private boolean isVehiculeDisponiblePourMission(Mission mission) {
        if (mission.getDateDebutMission() == null || mission.getDateFinMission() == null) {
            return false; // Dates manquantes, considéré comme non disponible par précaution
        }

        try {
            // Pour une mise à jour, on exclut l'ID de la mission actuelle
            Integer exclureMissionId = mission.getIdMission();

            return missionDao.isVehiculeDisponible(
                    mission.getIdVehicule(),
                    mission.getDateDebutMission(),
                    mission.getDateFinMission(),
                    exclureMissionId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la vérification de la disponibilité du véhicule", e);
            return false;
        }
    }

    /**
     * Formatte une date selon un format donné.
     *
     * @param date   La date à formatter
     * @param format Format de date (ex: "dd/MM/yyyy")
     * @return La date formattée ou chaîne vide si date null
     */
    public String formatDate(LocalDateTime date, String format) {
        if (date == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return date.format(formatter);
    }

    /**
     * Génère un rapport de l'activité des missions sur une période.
     *
     * @param debut Date de début de la période
     * @param fin   Date de fin de la période
     * @return Map avec des statistiques d'activité ou map vide en cas d'erreur
     */
    public Map<String, Object> genererRapportActivite(LocalDateTime debut, LocalDateTime fin) {
        try {
            // Cette méthode est un exemple et devrait être adaptée selon les besoins
            // Pour l'instant, on renvoie une map vide
            return Collections.emptyMap();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la génération du rapport d'activité", e);
            return Collections.emptyMap();
        }
    }
}