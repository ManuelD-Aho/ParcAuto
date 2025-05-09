package main.java.com.miage.parcauto.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.com.miage.parcauto.dao.VehiculeDao;
import main.java.com.miage.parcauto.model.vehicule.EtatVoiture;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;

/**
 * Service de gestion des véhicules.
 * Cette classe implémente la couche service pour toutes les opérations liées aux véhicules.
 * Elle sert d'intermédiaire entre la couche DAO et la couche de présentation (contrôleurs).
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class VehiculeService {

    private static final Logger LOGGER = Logger.getLogger(VehiculeService.class.getName());

    private final VehiculeDao vehiculeDao;

    /**
     * Constructeur par défaut.
     */
    public VehiculeService() {
        this.vehiculeDao = new VehiculeDao();
    }

    /**
     * Constructeur avec injection de dépendance pour les tests.
     *
     * @param vehiculeDao Instance de VehiculeDao à utiliser
     */
    public VehiculeService(VehiculeDao vehiculeDao) {
        this.vehiculeDao = vehiculeDao;
    }

    /**
     * Récupère tous les véhicules.
     *
     * @return Liste de tous les véhicules ou liste vide en cas d'erreur
     */
    public List<Vehicule> getAllVehicules() {
        try {
            return vehiculeDao.findAll();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les véhicules", e);
            return Collections.emptyList();
        }
    }

    /**
     * Recherche un véhicule par son ID.
     *
     * @param id ID du véhicule
     * @return Optional contenant le véhicule s'il existe
     */
    public Optional<Vehicule> getVehiculeById(int id) {
        try {
            return vehiculeDao.findById(id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du véhicule par ID: " + id, e);
            return Optional.empty();
        }
    }

    /**
     * Recherche un véhicule par son immatriculation.
     *
     * @param immatriculation Immatriculation du véhicule
     * @return Optional contenant le véhicule s'il existe
     */
    public Optional<Vehicule> getVehiculeByImmatriculation(String immatriculation) {
        try {
            return vehiculeDao.findByImmatriculation(immatriculation);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du véhicule par immatriculation: " + immatriculation, e);
            return Optional.empty();
        }
    }

    /**
     * Recherche un véhicule par son numéro de châssis.
     *
     * @param numeroChassis Numéro de châssis du véhicule
     * @return Optional contenant le véhicule s'il existe
     */
    public Optional<Vehicule> getVehiculeByNumeroChassis(String numeroChassis) {
        try {
            return vehiculeDao.findByNumeroChassi(numeroChassis);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du véhicule par numéro de châssis: " + numeroChassis, e);
            return Optional.empty();
        }
    }

    /**
     * Récupère les véhicules selon leur état.
     *
     * @param idEtat ID de l'état du véhicule
     * @return Liste des véhicules dans cet état ou liste vide en cas d'erreur
     */
    public List<Vehicule> getVehiculesByEtat(int idEtat) {
        try {
            return vehiculeDao.findByEtat(idEtat);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des véhicules par état: " + idEtat, e);
            return Collections.emptyList();
        }
    }

    /**
     * Récupère les véhicules disponibles pour une mission.
     *
     * @return Liste des véhicules disponibles ou liste vide en cas d'erreur
     */
    public List<Vehicule> getVehiculesDisponibles() {
        try {
            return vehiculeDao.findDisponibles();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des véhicules disponibles", e);
            return Collections.emptyList();
        }
    }

    /**
     * Recherche des véhicules par critères (marque, modèle, immatriculation...).
     *
     * @param searchTerm Terme de recherche
     * @return Liste des véhicules correspondant aux critères ou liste vide en cas d'erreur
     */
    public List<Vehicule> searchVehicules(String searchTerm) {
        try {
            return vehiculeDao.search(searchTerm);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de véhicules avec le terme: " + searchTerm, e);
            return Collections.emptyList();
        }
    }

    /**
     * Crée un nouveau véhicule.
     *
     * @param vehicule Le véhicule à créer
     * @return Le véhicule créé avec son ID généré ou null en cas d'erreur
     */
    public Vehicule createVehicule(Vehicule vehicule) {
        try {
            // Validation des données
            if (!validateVehicule(vehicule)) {
                LOGGER.warning("Validation du véhicule échouée");
                return null;
            }

            // Vérifier que le numéro de châssis et l'immatriculation sont uniques
            if (isNumeroChassisDuplicate(vehicule.getNumeroChassi())) {
                LOGGER.warning("Numéro de châssis déjà utilisé: " + vehicule.getNumeroChassi());
                return null;
            }

            if (isImmatriculationDuplicate(vehicule.getImmatriculation())) {
                LOGGER.warning("Immatriculation déjà utilisée: " + vehicule.getImmatriculation());
                return null;
            }

            return vehiculeDao.create(vehicule);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création du véhicule", e);
            return null;
        }
    }

    /**
     * Met à jour un véhicule existant.
     *
     * @param vehicule Le véhicule à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateVehicule(Vehicule vehicule) {
        try {
            // Validation des données
            if (!validateVehicule(vehicule) || vehicule.getIdVehicule() == null) {
                LOGGER.warning("Validation du véhicule échouée ou ID manquant");
                return false;
            }

            // Vérifier que le véhicule existe
            Optional<Vehicule> existingVehicule = vehiculeDao.findById(vehicule.getIdVehicule());
            if (!existingVehicule.isPresent()) {
                LOGGER.warning("Véhicule non trouvé avec l'ID: " + vehicule.getIdVehicule());
                return false;
            }

            // Vérifier l'unicité du numéro de châssis et de l'immatriculation (sauf pour le même véhicule)
            Optional<Vehicule> byNumeroChassis = vehiculeDao.findByNumeroChassi(vehicule.getNumeroChassi());
            if (byNumeroChassis.isPresent() && !byNumeroChassis.get().getIdVehicule().equals(vehicule.getIdVehicule())) {
                LOGGER.warning("Numéro de châssis déjà utilisé par un autre véhicule: " + vehicule.getNumeroChassi());
                return false;
            }

            Optional<Vehicule> byImmatriculation = vehiculeDao.findByImmatriculation(vehicule.getImmatriculation());
            if (byImmatriculation.isPresent() && !byImmatriculation.get().getIdVehicule().equals(vehicule.getIdVehicule())) {
                LOGGER.warning("Immatriculation déjà utilisée par un autre véhicule: " + vehicule.getImmatriculation());
                return false;
            }

            return vehiculeDao.update(vehicule);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du véhicule", e);
            return false;
        }
    }

    /**
     * Met à jour l'état d'un véhicule.
     *
     * @param idVehicule ID du véhicule
     * @param idEtat Nouvel état du véhicule
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateVehiculeEtat(int idVehicule, int idEtat) {
        try {
            return vehiculeDao.updateEtat(idVehicule, idEtat);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de l'état du véhicule: " + idVehicule, e);
            return false;
        }
    }

    /**
     * Met à jour le kilométrage d'un véhicule.
     *
     * @param idVehicule ID du véhicule
     * @param kmActuels Nouveau kilométrage
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateVehiculeKilometrage(int idVehicule, int kmActuels) {
        try {
            // Vérification que le nouveau kilométrage est valide (supérieur ou égal à l'ancien)
            Optional<Vehicule> vehicule = getVehiculeById(idVehicule);
            if (vehicule.isPresent() && vehicule.get().getKmActuels() != null && kmActuels < vehicule.get().getKmActuels()) {
                LOGGER.warning("Le nouveau kilométrage ne peut pas être inférieur à l'ancien");
                return false;
            }

            return vehiculeDao.updateKilometrage(idVehicule, kmActuels);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du kilométrage du véhicule: " + idVehicule, e);
            return false;
        }
    }

    /**
     * Supprime un véhicule.
     *
     * @param idVehicule ID du véhicule à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deleteVehicule(int idVehicule) {
        try {
            return vehiculeDao.delete(idVehicule);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression du véhicule: " + idVehicule, e);
            return false;
        }
    }

    /**
     * Vérifie si un numéro de châssis est déjà utilisé.
     *
     * @param numeroChassis Numéro de châssis à vérifier
     * @return true si le numéro de châssis est déjà utilisé, false sinon
     */
    public boolean isNumeroChassisDuplicate(String numeroChassis) {
        try {
            return vehiculeDao.existsByNumeroChassi(numeroChassis);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la vérification du numéro de châssis: " + numeroChassis, e);
            return true; // Par sécurité, on considère qu'il y a un doublon en cas d'erreur
        }
    }

    /**
     * Vérifie si une immatriculation est déjà utilisée.
     *
     * @param immatriculation Immatriculation à vérifier
     * @return true si l'immatriculation est déjà utilisée, false sinon
     */
    public boolean isImmatriculationDuplicate(String immatriculation) {
        try {
            return vehiculeDao.existsByImmatriculation(immatriculation);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la vérification de l'immatriculation: " + immatriculation, e);
            return true; // Par sécurité, on considère qu'il y a un doublon en cas d'erreur
        }
    }

    /**
     * Récupère les informations TCO (Total Cost of Ownership) d'un véhicule.
     *
     * @param idVehicule ID du véhicule
     * @return Optional contenant les informations TCO si disponibles
     */
    public Optional<VehiculeDao.TCOInfo> getVehiculeTCO(int idVehicule) {
        try {
            return vehiculeDao.getTCOInfo(idVehicule);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des informations TCO du véhicule: " + idVehicule, e);
            return Optional.empty();
        }
    }

    /**
     * Calcule les statistiques du parc automobile.
     *
     * @return Statistiques du parc ou null en cas d'erreur
     */
    public VehiculeDao.ParcStats calculateParcStats() {
        try {
            return vehiculeDao.calculateParcStats();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul des statistiques du parc", e);
            return null;
        }
    }

    /**
     * Récupère les véhicules nécessitant un entretien préventif.
     *
     * @param intervalleKm Intervalle de kilomètres pour l'entretien préventif
     * @return Liste des véhicules nécessitant un entretien ou liste vide en cas d'erreur
     */
    public List<Vehicule> getVehiculesNeedingMaintenance(int intervalleKm) {
        try {
            return vehiculeDao.findNeedingMaintenance(intervalleKm);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des véhicules nécessitant un entretien", e);
            return Collections.emptyList();
        }
    }

    /**
     * Récupère tous les états de véhicule disponibles.
     *
     * @return Liste des états de véhicule ou liste vide en cas d'erreur
     */
    public List<EtatVoiture> getAllEtats() {
        try {
            return vehiculeDao.getAllEtats();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des états de véhicule", e);
            return Collections.emptyList();
        }
    }

    /**
     * Récupère les véhicules assignés à un utilisateur spécifique.
     *
     * @param idUtilisateur ID de l'utilisateur
     * @return Liste des véhicules assignés à cet utilisateur ou liste vide en cas d'erreur
     */
    public List<Vehicule> getVehiculesByUtilisateur(Integer idUtilisateur) {
        // Cette méthode doit être implémentée pour récupérer les véhicules assignés à un utilisateur
        // En attendant, on retourne une liste vide
        LOGGER.log(Level.WARNING, "Méthode getVehiculesByUtilisateur non implémentée");
        return Collections.emptyList();
    }

    /**
     * Vérifie si un véhicule est assigné à un utilisateur spécifique.
     *
     * @param idVehicule ID du véhicule
     * @param idUtilisateur ID de l'utilisateur
     * @return true si le véhicule est assigné à l'utilisateur, false sinon
     */
    public boolean isVehiculeAssignedToUser(Integer idVehicule, Integer idUtilisateur) {
        // Cette méthode doit être implémentée pour vérifier si un véhicule est assigné à un utilisateur
        // En attendant, pour éviter des erreurs, on implémente une version minimale
        
        if (idVehicule == null || idUtilisateur == null) {
            return false;
        }
        
        try {
            // Cette implémentation devrait normalement consulter une table d'association entre véhicules et utilisateurs
            List<Vehicule> vehiculesUtilisateur = getVehiculesByUtilisateur(idUtilisateur);
            return vehiculesUtilisateur.stream()
                .anyMatch(v -> v.getIdVehicule() != null && v.getIdVehicule().equals(idVehicule));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la vérification de l'assignation du véhicule", e);
            return false;
        }
    }

    /**
     * Retourne le nombre total de véhicules dans le parc.
     * @return nombre de véhicules, ou 0 en cas d'erreur
     */
    public int getVehiculesCount() {
        try {
            VehiculeDao.ParcStats stats = vehiculeDao.calculateParcStats();
            return stats != null ? stats.getTotalVehicules() : 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des véhicules", e);
            return 0;
        }
    }

    /**
     * Valide les données d'un véhicule.
     *
     * @param vehicule Le véhicule à valider
     * @return true si les données sont valides, false sinon
     */
    private boolean validateVehicule(Vehicule vehicule) {
        if (vehicule == null) {
            return false;
        }

        // Vérifier que les champs obligatoires sont renseignés
        if (vehicule.getEtatVoiture() == null ||
                vehicule.getNumeroChassi() == null || vehicule.getNumeroChassi().trim().isEmpty() ||
                vehicule.getImmatriculation() == null || vehicule.getImmatriculation().trim().isEmpty() ||
                vehicule.getMarque() == null || vehicule.getMarque().trim().isEmpty() ||
                vehicule.getModele() == null || vehicule.getModele().trim().isEmpty() ||
                vehicule.getEnergie() == null) {
            return false;
        }

        // Vérifier que le numéro de châssis a un format valide (alphanumérique de longueur 17)
        String numeroChassi = vehicule.getNumeroChassi().trim();
        if (numeroChassi.length() != 17 || !numeroChassi.matches("[A-HJ-NPR-Za-hj-npr-z0-9]{17}")) {
            return false;
        }

        // Vérifier que l'immatriculation a un format valide (format français: AB-123-CD ou ancien format: 123 ABC 45)
        String immatriculation = vehicule.getImmatriculation().trim();
        if (!immatriculation.matches("[A-Z]{2}-[0-9]{3}-[A-Z]{2}") &&
                !immatriculation.matches("[0-9]{1,4} [A-Z]{1,3} [0-9]{1,2}")) {
            return false;
        }

        // Vérifier que la date d'acquisition n'est pas dans le futur
        if (vehicule.getDateAcquisition() != null && vehicule.getDateAcquisition().isAfter(LocalDateTime.now())) {
            return false;
        }

        // Vérifier que le prix du véhicule est positif
        if (vehicule.getPrixVehicule() != null && vehicule.getPrixVehicule().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        // Vérifier que le kilométrage est positif
        if (vehicule.getKmActuels() != null && vehicule.getKmActuels() < 0) {
            return false;
        }

        // Vérifier que la puissance est positive
        if (vehicule.getPuissance() != null && vehicule.getPuissance() <= 0) {
            return false;
        }

        // Vérifier que le nombre de places est positif
        if (vehicule.getNbPlaces() != null && vehicule.getNbPlaces() <= 0) {
            return false;
        }

        return true;
    }
}