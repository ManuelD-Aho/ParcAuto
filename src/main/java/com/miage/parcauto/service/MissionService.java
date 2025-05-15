package main.java.com.miage.parcauto.service;

import main.java.com.miage.parcauto.dao.MissionRepositoryImpl;
import main.java.com.miage.parcauto.dao.VehiculeRepositoryImpl;
import main.java.com.miage.parcauto.dto.DepenseDTO;
import main.java.com.miage.parcauto.dto.MissionDTO;
import main.java.com.miage.parcauto.exception.DatabaseException;
import main.java.com.miage.parcauto.exception.MissionNotFoundException;
import main.java.com.miage.parcauto.exception.ValidationException;
import main.java.com.miage.parcauto.exception.VehiculeNotFoundException;
import main.java.com.miage.parcauto.model.mission.DepenseMission;
import main.java.com.miage.parcauto.model.mission.Mission;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;
import main.java.com.miage.parcauto.model.vehicule.EtatVoiture;
import main.java.com.miage.parcauto.service.ValidationService.ValidationResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Service de gestion des missions.
 * Cette classe implémente la couche service pour toutes les opérations liées
 * aux missions.
 * Elle sert d'intermédiaire entre la couche repository et la couche de
 * présentation
 * (contrôleurs).
 *
 * @author MIAGE Holding
 * @version 1.1
 */
public class MissionService {

    private static final Logger LOGGER = Logger.getLogger(MissionService.class.getName());

    private final MissionRepository missionRepository;
    private final VehiculeRepository vehiculeRepository;
    private final ValidationService validationService;

    /**
     * Constructeur par défaut.
     */
    public MissionService() {
        this.missionRepository = new MissionRepositoryImpl();
        this.vehiculeRepository = new VehiculeRepositoryImpl();
        this.validationService = new ValidationService();
    }

    /**
     * Constructeur avec injection de dépendance pour les tests.
     *
     * @param missionRepository  Instance de MissionRepository à utiliser
     * @param vehiculeRepository Instance de VehiculeRepository à utiliser
     * @param validationService  Instance de ValidationService à utiliser
     */
    public MissionService(MissionRepository missionRepository, VehiculeRepository vehiculeRepository,
            ValidationService validationService) {
        this.missionRepository = missionRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.validationService = validationService;
    }

    /**
     * Récupère toutes les missions.
     *
     * @return Liste de toutes les missions DTO
     * @throws DatabaseException Si une erreur de base de données survient
     */
    public List<MissionDTO> getAllMissions() throws DatabaseException {
        try {
            List<Mission> missions = missionRepository.findAll();
            return missions.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de toutes les missions", e);
            throw new DatabaseException("Impossible de récupérer les missions", e);
        }
    }

    /**
     * Recherche une mission par son ID.
     *
     * @param id ID de la mission
     * @return MissionDTO si elle existe
     * @throws MissionNotFoundException Si la mission n'existe pas
     * @throws DatabaseException        Si une erreur de base de données survient
     */
    public MissionDTO getMissionById(Integer id) throws MissionNotFoundException, DatabaseException {
        try {
            Optional<Mission> missionOpt = missionRepository.findById(id);
            if (missionOpt.isPresent()) {
                return convertToDTO(missionOpt.get());
            } else {
                throw new MissionNotFoundException("Aucune mission trouvée avec l'ID: " + id);
            }
        } catch (MissionNotFoundException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de la mission par ID: " + id, e);
            throw new DatabaseException("Impossible de récupérer la mission", e);
        }
    }

    /**
     * Récupère les missions actives pour un véhicule donné.
     *
     * @param idVehicule ID du véhicule
     * @return Liste des missions actives pour ce véhicule
     * @throws DatabaseException Si une erreur de base de données survient
     */
    public List<MissionDTO> getMissionsByVehicule(int idVehicule) throws DatabaseException {
        try {
            List<Mission> missions = missionRepository.findActiveForVehicule(idVehicule);
            return missions.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des missions pour le véhicule ID: " + idVehicule,
                    e);
            throw new DatabaseException("Impossible de récupérer les missions pour ce véhicule", e);
        }
    }

    /**
     * Récupère les missions selon leur statut.
     *
     * @param statut Statut des missions à récupérer
     * @return Liste des missions ayant ce statut
     * @throws DatabaseException Si une erreur de base de données survient
     */
    public List<MissionDTO> getMissionsByStatut(Mission.StatutMission statut) throws DatabaseException {
        try {
            List<Mission> missions = missionRepository.findByStatut(statut);
            return missions.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des missions par statut: " + statut, e);
            throw new DatabaseException("Impossible de récupérer les missions par statut", e);
        }
    }

    /**
     * Récupère les missions par période.
     *
     * @param debut Date de début de la période
     * @param fin   Date de fin de la période
     * @return Liste des missions pendant cette période
     * @throws DatabaseException Si une erreur de base de données survient
     */
    public List<MissionDTO> getMissionsByPeriode(LocalDate debut, LocalDate fin) throws DatabaseException {
        try {
            List<Mission> missions = missionRepository.findByPeriod(debut, fin);
            return missions.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des missions par période", e);
            throw new DatabaseException("Impossible de récupérer les missions par période", e);
        }
    }

    /**
     * Crée une nouvelle mission.
     *
     * @param missionDTO DTO de la mission à créer
     * @return true si la création a réussi
     * @throws ValidationException       Si les données de la mission sont invalides
     * @throws DatabaseException         Si une erreur de base de données survient
     * @throws VehiculeNotFoundException Si le véhicule associé n'existe pas
     */
    public boolean createMission(MissionDTO missionDTO)
            throws ValidationException, DatabaseException, VehiculeNotFoundException {
        // Validation des données
        ValidationResult validationResult = validationService.validateMission(missionDTO);
        if (!validationResult.isValid()) {
            throw new ValidationException("Données de mission invalides", validationResult.getErrors());
        }

        try {
            // Vérification que le véhicule existe
            Optional<Vehicule> vehiculeOpt = vehiculeRepository.findById(missionDTO.getIdVehicule());
            if (vehiculeOpt.isEmpty()) {
                throw new VehiculeNotFoundException(
                        "Le véhicule spécifié n'existe pas: ID " + missionDTO.getIdVehicule());
            }

            // Conversion DTO -> Entité
            Mission mission = convertFromDTO(missionDTO);

            // Sauvegarde dans le repository
            Mission savedMission = missionRepository.save(mission);

            // Mise à jour du statut du véhicule si la mission démarre immédiatement
            Vehicule vehicule = vehiculeOpt.get();
            if (mission.getStatut() == Mission.StatutMission.EnCours) {
                vehicule.setEtat(EtatVoiture.EN_MISSION);
                vehiculeRepository.update(vehicule);
            }

            return savedMission != null && savedMission.getIdMission() != null;
        } catch (VehiculeNotFoundException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création de la mission", e);
            throw new DatabaseException("Impossible de créer la mission", e);
        }
    }

    /**
     * Met à jour une mission existante.
     *
     * @param missionDTO DTO de la mission à mettre à jour
     * @return true si la mise à jour a réussi
     * @throws ValidationException      Si les données de la mission sont invalides
     * @throws MissionNotFoundException Si la mission n'existe pas
     * @throws DatabaseException        Si une erreur de base de données survient
     */
    public boolean updateMission(MissionDTO missionDTO)
            throws ValidationException, MissionNotFoundException, DatabaseException {
        // Validation des données
        ValidationResult validationResult = validationService.validateMission(missionDTO);
        if (!validationResult.isValid()) {
            throw new ValidationException("Données de mission invalides", validationResult.getErrors());
        }

        if (missionDTO.getIdMission() == null) {
            throw new ValidationException("L'ID de mission est requis pour la mise à jour");
        }

        try {
            // Vérification que la mission existe
            if (!missionRepository.findById(missionDTO.getIdMission()).isPresent()) {
                throw new MissionNotFoundException("Mission non trouvée avec l'ID: " + missionDTO.getIdMission());
            }

            // Conversion DTO -> Entité
            Mission mission = convertFromDTO(missionDTO);

            // Mise à jour dans le repository
            Mission updatedMission = missionRepository.update(mission);

            return updatedMission != null;
        } catch (MissionNotFoundException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de la mission: " + missionDTO.getIdMission(), e);
            throw new DatabaseException("Impossible de mettre à jour la mission", e);
        }
    }

    /**
     * Termine une mission et met à jour le kilométrage du véhicule.
     *
     * @param idMission ID de la mission à terminer
     * @param nouveauKm Nouveau kilométrage du véhicule
     * @return true si la mission a été terminée avec succès
     * @throws MissionNotFoundException Si la mission n'existe pas
     * @throws DatabaseException        Si une erreur de base de données survient
     * @throws ValidationException      Si les données sont invalides
     */
    public boolean terminerMission(Integer idMission, int nouveauKm)
            throws MissionNotFoundException, DatabaseException, ValidationException {
        if (nouveauKm <= 0) {
            throw new ValidationException("Le kilométrage doit être supérieur à zéro");
        }

        try {
            // Récupération de la mission
            Optional<Mission> missionOpt = missionRepository.findById(idMission);
            if (missionOpt.isEmpty()) {
                throw new MissionNotFoundException("Mission non trouvée avec l'ID: " + idMission);
            }

            Mission mission = missionOpt.get();

            // Vérification que la mission n'est pas déjà terminée
            if (mission.getStatut() == Mission.StatutMission.Terminee) {
                throw new ValidationException("Cette mission est déjà terminée");
            }

            // Vérification que le nouveau kilométrage est supérieur au kilométrage de
            // départ
            if (nouveauKm <= mission.getKmDepart()) {
                throw new ValidationException("Le kilométrage de retour doit être supérieur au kilométrage de départ");
            }

            // Mise à jour de la mission
            mission.setStatut(Mission.StatutMission.Terminee);
            mission.setKmRetour(nouveauKm);
            mission.setDateRetourReelle(LocalDateTime.now());

            // Mise à jour dans le repository
            missionRepository.update(mission);

            // Mise à jour du véhicule
            Optional<Vehicule> vehiculeOpt = vehiculeRepository.findById(mission.getIdVehicule());
            if (vehiculeOpt.isPresent()) {
                Vehicule vehicule = vehiculeOpt.get();
                vehicule.setKilometrage(nouveauKm);
                vehicule.setEtat(EtatVoiture.DISPONIBLE);
                vehiculeRepository.update(vehicule);
            }

            return true;
        } catch (MissionNotFoundException | ValidationException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la clôture de la mission: " + idMission, e);
            throw new DatabaseException("Impossible de terminer la mission", e);
        }
    }

    /**
     * Ajoute une dépense à une mission.
     *
     * @param idMission  ID de la mission
     * @param depenseDTO DTO de la dépense à ajouter
     * @return true si l'ajout a réussi
     * @throws ValidationException      Si les données de la dépense sont invalides
     * @throws MissionNotFoundException Si la mission n'existe pas
     * @throws DatabaseException        Si une erreur de base de données survient
     */
    public boolean ajouterDepense(Integer idMission, DepenseDTO depenseDTO)
            throws ValidationException, MissionNotFoundException, DatabaseException {
        // Validation des données
        ValidationResult validationResult = validationService.validateDepense(depenseDTO);
        if (!validationResult.isValid()) {
            throw new ValidationException("Données de dépense invalides", validationResult.getErrors());
        }

        try {
            // Vérification que la mission existe
            Optional<Mission> missionOpt = missionRepository.findById(idMission);
            if (missionOpt.isEmpty()) {
                throw new MissionNotFoundException("Mission non trouvée avec l'ID: " + idMission);
            }

            Mission mission = missionOpt.get();

            // Création de la dépense
            DepenseMission depense = new DepenseMission();
            depense.setIdMission(idMission);
            depense.setCategorie(depenseDTO.getCategorie());
            depense.setDescription(depenseDTO.getDescription());
            depense.setMontant(depenseDTO.getMontant());
            depense.setDate(depenseDTO.getDate());
            depense.setJustificatif(depenseDTO.getJustificatif());

            // Ajout de la dépense à la mission
            return missionRepository.addDepense(mission, depense);
        } catch (MissionNotFoundException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors d'ajout d'une dépense à la mission: " + idMission, e);
            throw new DatabaseException("Impossible d'ajouter la dépense", e);
        }
    }

    /**
     * Convertit une entité Mission en DTO.
     *
     * @param mission L'entité Mission
     * @return Le DTO correspondant
     */
    private MissionDTO convertToDTO(Mission mission) {
        MissionDTO dto = new MissionDTO();
        dto.setIdMission(mission.getIdMission());
        dto.setIdVehicule(mission.getIdVehicule());
        dto.setIdSocietaire(mission.getIdSocietaire());
        dto.setDestination(mission.getDestination());
        dto.setMotif(mission.getMotif());
        dto.setDateDepart(mission.getDateDepart());
        dto.setDateRetourPrevue(mission.getDateRetourPrevue());
        dto.setStatut(mission.getStatut());
        dto.setKmDepart(mission.getKmDepart());
        dto.setKmRetour(mission.getKmRetour());
        dto.setDateRetourReelle(mission.getDateRetourReelle());
        dto.setObservations(mission.getObservations());

        // Informations véhicule et sociétaire si disponibles
        if (mission.getMarqueVehicule() != null && mission.getModeleVehicule() != null) {
            dto.setInfoVehicule(mission.getMarqueVehicule() + " " + mission.getModeleVehicule() +
                    (mission.getImmatriculationVehicule() != null ? " (" + mission.getImmatriculationVehicule() + ")"
                            : ""));
            dto.setImmatriculation(mission.getImmatriculationVehicule());
        }

        if (mission.getNomSocietaire() != null && mission.getPrenomSocietaire() != null) {
            dto.setNomPrenomSocietaire(mission.getNomSocietaire() + " " + mission.getPrenomSocietaire());
        }

        // Conversion des dépenses en DTO
        List<DepenseDTO> depenseDTOs = new ArrayList<>();
        BigDecimal coutTotal = BigDecimal.ZERO;

        for (DepenseMission depense : mission.getDepenses()) {
            DepenseDTO depenseDTO = new DepenseDTO();
            depenseDTO.setIdDepense(depense.getIdDepense());
            depenseDTO.setIdMission(depense.getIdMission());
            depenseDTO.setCategorie(depense.getCategorie());
            depenseDTO.setDescription(depense.getDescription());
            depenseDTO.setMontant(depense.getMontant());
            depenseDTO.setDate(depense.getDate());
            depenseDTO.setJustificatif(depense.getJustificatif());

            depenseDTOs.add(depenseDTO);

            if (depense.getMontant() != null) {
                coutTotal = coutTotal.add(depense.getMontant());
            }
        }

        dto.setDepenses(depenseDTOs);
        dto.setCoutTotal(coutTotal);

        return dto;
    }

    /**
     * Convertit un DTO Mission en entité.
     *
     * @param dto Le DTO Mission
     * @return L'entité correspondante
     */
    private Mission convertFromDTO(MissionDTO dto) {
        Mission mission = new Mission();
        mission.setIdMission(dto.getIdMission());
        mission.setIdVehicule(dto.getIdVehicule());
        mission.setIdSocietaire(dto.getIdSocietaire());
        mission.setDestination(dto.getDestination());
        mission.setMotif(dto.getMotif());
        mission.setDateDepart(dto.getDateDepart());
        mission.setDateRetourPrevue(dto.getDateRetourPrevue());
        mission.setStatut(dto.getStatut());
        mission.setKmDepart(dto.getKmDepart());
        mission.setKmRetour(dto.getKmRetour());
        mission.setDateRetourReelle(dto.getDateRetourReelle());
        mission.setObservations(dto.getObservations());

        // Conversion des dépenses DTO en entités
        List<DepenseMission> depenses = new ArrayList<>();
        if (dto.getDepenses() != null) {
            for (DepenseDTO depenseDTO : dto.getDepenses()) {
                DepenseMission depense = new DepenseMission();
                depense.setIdDepense(depenseDTO.getIdDepense());
                depense.setIdMission(depenseDTO.getIdMission());
                depense.setCategorie(depenseDTO.getCategorie());
                depense.setDescription(depenseDTO.getDescription());
                depense.setMontant(depenseDTO.getMontant());
                depense.setDate(depenseDTO.getDate());
                depense.setJustificatif(depenseDTO.getJustificatif());

                depenses.add(depense);
            }
        }

        mission.setDepenses(depenses);

        return mission;
    }
}