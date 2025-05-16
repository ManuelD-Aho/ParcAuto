package main.java.com.miage.parcauto.service.impl;

import main.java.com.miage.parcauto.dao.*;
import main.java.com.miage.parcauto.dao.impl.*;
import main.java.com.miage.parcauto.dto.VehiculeDTO;
import main.java.com.miage.parcauto.exception.*;
import main.java.com.miage.parcauto.mapper.VehiculeMapper;
import main.java.com.miage.parcauto.mapper.impl.VehiculeMapperImpl;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;
import main.java.com.miage.parcauto.model.vehicule.EtatVoiture; // Pour l'ID de l'état "Disponible"
import main.java.com.miage.parcauto.service.VehiculeService;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VehiculeServiceImpl implements VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final EtatVoitureRepository etatVoitureRepository;
    private final MissionRepository missionRepository; // Pour vérifier la disponibilité
    private final AffectationRepository affectationRepository; // Pour vérifier la disponibilité
    private final VehiculeMapper vehiculeMapper;
    // private final ValidationService validationService;

    public VehiculeServiceImpl() {
        this.vehiculeRepository = new VehiculeRepositoryImpl();
        this.etatVoitureRepository = new EtatVoitureRepositoryImpl();
        this.missionRepository = new MissionRepositoryImpl();
        this.affectationRepository = new AffectationRepositoryImpl();
        this.vehiculeMapper = new VehiculeMapperImpl(this.etatVoitureRepository); // Passe le repo pour le libellé
        // this.validationService = new ValidationServiceImpl();
    }

    // Constructeur pour injection de dépendances (tests ou futur DI framework)
    public VehiculeServiceImpl(VehiculeRepository vehiculeRepository, EtatVoitureRepository etatVoitureRepository,
                               MissionRepository missionRepository, AffectationRepository affectationRepository,
                               VehiculeMapper vehiculeMapper) {
        this.vehiculeRepository = vehiculeRepository;
        this.etatVoitureRepository = etatVoitureRepository;
        this.missionRepository = missionRepository;
        this.affectationRepository = affectationRepository;
        this.vehiculeMapper = vehiculeMapper;
    }


    @Override
    public VehiculeDTO createVehicule(VehiculeDTO vehiculeDTO) throws ValidationException, DuplicateEntityException, OperationFailedException {
        // validationService.validateVehicule(vehiculeDTO);
        if (vehiculeDTO == null || vehiculeDTO.getImmatriculation() == null || vehiculeDTO.getImmatriculation().trim().isEmpty() ||
                vehiculeDTO.getNumeroChassis() == null || vehiculeDTO.getNumeroChassis().trim().isEmpty()) {
            throw new ValidationException("Immatriculation et numéro de châssis sont requis.");
        }
        if (vehiculeDTO.getIdEtatVoiture() == null) {
            throw new ValidationException("L'état initial du véhicule est requis.");
        }


        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            if (vehiculeRepository.findByImmatriculation(conn, vehiculeDTO.getImmatriculation()).isPresent()) {
                throw new DuplicateEntityException("Un véhicule avec l'immatriculation '" + vehiculeDTO.getImmatriculation() + "' existe déjà.");
            }
            if (vehiculeRepository.findByNumeroChassis(conn, vehiculeDTO.getNumeroChassis()).isPresent()) {
                throw new DuplicateEntityException("Un véhicule avec le numéro de châssis '" + vehiculeDTO.getNumeroChassis() + "' existe déjà.");
            }
            if (etatVoitureRepository.findById(conn, vehiculeDTO.getIdEtatVoiture()).isEmpty()){
                throw new ValidationException("L'état de voiture spécifié avec l'ID " + vehiculeDTO.getIdEtatVoiture() + " n'existe pas.");
            }


            Vehicule vehicule = vehiculeMapper.toEntity(vehiculeDTO);
            vehicule.setDateEtat(LocalDateTime.now()); // Date de l'état initial

            Vehicule savedVehicule = vehiculeRepository.save(conn, vehicule);
            conn.commit();
            return vehiculeMapper.toDTO(savedVehicule);

        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors de la création du véhicule.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public Optional<VehiculeDTO> getVehiculeById(Integer idVehicule) throws OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            Optional<Vehicule> vehiculeOpt = vehiculeRepository.findById(conn, idVehicule);
            return vehiculeOpt.map(vehiculeMapper::toDTO);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération du véhicule par ID.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public List<VehiculeDTO> getAllVehicules() throws OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            List<Vehicule> vehicules = vehiculeRepository.findAll(conn);
            return vehiculeMapper.toDTOList(vehicules);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération de tous les véhicules.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public VehiculeDTO updateVehicule(VehiculeDTO vehiculeDTO) throws ValidationException, VehiculeNotFoundException, DuplicateEntityException, OperationFailedException {
        // validationService.validateVehicule(vehiculeDTO);
        if (vehiculeDTO == null || vehiculeDTO.getIdVehicule() == null) {
            throw new ValidationException("ID véhicule est requis pour la mise à jour.");
        }

        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            Vehicule existingVehicule = vehiculeRepository.findById(conn, vehiculeDTO.getIdVehicule())
                    .orElseThrow(() -> new VehiculeNotFoundException("Véhicule non trouvé avec l'ID: " + vehiculeDTO.getIdVehicule()));

            // Vérifier unicité immatriculation si changée
            if (vehiculeDTO.getImmatriculation() != null && !vehiculeDTO.getImmatriculation().equals(existingVehicule.getImmatriculation())) {
                vehiculeRepository.findByImmatriculation(conn, vehiculeDTO.getImmatriculation()).ifPresent(v -> {
                    if (!v.getIdVehicule().equals(existingVehicule.getIdVehicule())) { // S'assure que ce n'est pas le même véhicule
                        throw new RuntimeException(new DuplicateEntityException("Un autre véhicule avec l'immatriculation '" + vehiculeDTO.getImmatriculation() + "' existe déjà."));
                    }
                });
                existingVehicule.setImmatriculation(vehiculeDTO.getImmatriculation());
            }
            // Vérifier unicité numéro de chassis si changé
            if (vehiculeDTO.getNumeroChassis() != null && !vehiculeDTO.getNumeroChassis().equals(existingVehicule.getNumeroChassis())) {
                vehiculeRepository.findByNumeroChassis(conn, vehiculeDTO.getNumeroChassis()).ifPresent(v -> {
                    if (!v.getIdVehicule().equals(existingVehicule.getIdVehicule())) {
                        throw new RuntimeException(new DuplicateEntityException("Un autre véhicule avec le numéro de châssis '" + vehiculeDTO.getNumeroChassis() + "' existe déjà."));
                    }
                });
                existingVehicule.setNumeroChassis(vehiculeDTO.getNumeroChassis());
            }

            // Mettre à jour les autres champs
            if (vehiculeDTO.getIdEtatVoiture() != null) {
                if (etatVoitureRepository.findById(conn, vehiculeDTO.getIdEtatVoiture()).isEmpty()){
                    throw new ValidationException("L'état de voiture spécifié avec l'ID " + vehiculeDTO.getIdEtatVoiture() + " n'existe pas.");
                }
                if(!existingVehicule.getIdEtatVoiture().equals(vehiculeDTO.getIdEtatVoiture())){
                    existingVehicule.setIdEtatVoiture(vehiculeDTO.getIdEtatVoiture());
                    existingVehicule.setDateEtat(LocalDateTime.now()); // Mettre à jour la date de l'état
                }
            }
            if (vehiculeDTO.getEnergie() != null) existingVehicule.setEnergie(main.java.com.miage.parcauto.model.vehicule.Energie.valueOf(vehiculeDTO.getEnergie()));
            if (vehiculeDTO.getMarque() != null) existingVehicule.setMarque(vehiculeDTO.getMarque());
            if (vehiculeDTO.getModele() != null) existingVehicule.setModele(vehiculeDTO.getModele());
            if (vehiculeDTO.getNbPlaces() != null) existingVehicule.setNbPlaces(vehiculeDTO.getNbPlaces());
            if (vehiculeDTO.getDateAcquisition() != null) existingVehicule.setDateAcquisition(vehiculeDTO.getDateAcquisition());
            if (vehiculeDTO.getDateMiseEnService() != null) existingVehicule.setDateMiseEnService(vehiculeDTO.getDateMiseEnService());
            if (vehiculeDTO.getPuissance() != null) existingVehicule.setPuissance(vehiculeDTO.getPuissance());
            if (vehiculeDTO.getCouleur() != null) existingVehicule.setCouleur(vehiculeDTO.getCouleur());
            if (vehiculeDTO.getPrixVehicule() != null) existingVehicule.setPrixVehicule(vehiculeDTO.getPrixVehicule());
            if (vehiculeDTO.getKmActuels() != null && vehiculeDTO.getKmActuels() >= existingVehicule.getKmActuels()) { // Kilométrage ne peut que augmenter ou rester égal
                existingVehicule.setKmActuels(vehiculeDTO.getKmActuels());
            } else if (vehiculeDTO.getKmActuels() != null && vehiculeDTO.getKmActuels() < existingVehicule.getKmActuels()){
                throw new ValidationException("Le nouveau kilométrage ne peut pas être inférieur à l'actuel.");
            }


            Vehicule updatedVehicule = vehiculeRepository.update(conn, existingVehicule);
            conn.commit();
            return vehiculeMapper.toDTO(updatedVehicule);

        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors de la mise à jour du véhicule.", e);
        } catch (RuntimeException e) { // Pour attraper les DuplicateEntityException lancées dans les lambdas
            DbUtil.rollback(conn);
            if (e.getCause() instanceof DuplicateEntityException) {
                throw (DuplicateEntityException) e.getCause();
            }
            throw new OperationFailedException("Erreur lors de la mise à jour du véhicule.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public void deleteVehicule(Integer idVehicule) throws VehiculeNotFoundException, OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            if (vehiculeRepository.findById(conn, idVehicule).isEmpty()) {
                throw new VehiculeNotFoundException("Véhicule non trouvé avec l'ID: " + idVehicule);
            }
            // Vérifier les dépendances (missions, entretiens, affectations, assurances)
            if (!missionRepository.findActiveByVehiculeId(conn, idVehicule).isEmpty()) {
                throw new OperationFailedException("Impossible de supprimer le véhicule: il a des missions actives ou planifiées.");
            }
            // Ajouter vérifications pour entretiens, affectations, etc.

            boolean deleted = vehiculeRepository.delete(conn, idVehicule);
            if (!deleted) {
                throw new OperationFailedException("La suppression du véhicule a échoué.");
            }
            conn.commit();
        } catch (SQLException e) {
            DbUtil.rollback(conn);
            if (e.getSQLState().startsWith("23")) {
                throw new OperationFailedException("Impossible de supprimer le véhicule car il est référencé ailleurs (ex: entretiens, assurances). Supprimez d'abord ces références.", e);
            }
            throw new OperationFailedException("Erreur technique lors de la suppression du véhicule.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public void updateKilometrage(Integer idVehicule, int nouveauKilometrage) throws VehiculeNotFoundException, ValidationException, OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            Vehicule vehicule = vehiculeRepository.findById(conn, idVehicule)
                    .orElseThrow(() -> new VehiculeNotFoundException("Véhicule non trouvé avec l'ID: " + idVehicule));

            if (nouveauKilometrage < vehicule.getKmActuels()) {
                throw new ValidationException("Le nouveau kilométrage (" + nouveauKilometrage + ") ne peut pas être inférieur au kilométrage actuel (" + vehicule.getKmActuels() + ").");
            }
            vehicule.setKmActuels(nouveauKilometrage);
            // vehicule.setDateEtat(LocalDateTime.now()); // Optionnel: mettre à jour la date de l'état si le km change l'état implicitement

            vehiculeRepository.update(conn, vehicule); // S'assurer que updateKilometrage existe ou que update gère bien ce cas
            conn.commit();
        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors de la mise à jour du kilométrage.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public List<VehiculeDTO> getVehiculesDisponibles(LocalDateTime dateDebut, LocalDateTime dateFin) throws OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            // 1. Récupérer tous les véhicules potentiellement disponibles (état "Disponible")
            // L'ID pour "Disponible" doit être connu. Supposons que c'est 1.
            // Il serait mieux de récupérer l'ID de l'état "Disponible" dynamiquement.
            Optional<EtatVoiture> etatDisponibleOpt = etatVoitureRepository.findByLibelle(conn, "Disponible");
            if (etatDisponibleOpt.isEmpty()) {
                return Collections.emptyList(); // Ou lever une exception si l'état "Disponible" est critique
            }
            Integer idEtatDisponible = etatDisponibleOpt.get().getIdEtatVoiture();

            List<Vehicule> vehiculesAvecEtatOk = vehiculeRepository.findByEtatId(conn, idEtatDisponible);

            // 2. Filtrer ceux qui n'ont pas de missions ou affectations conflictuelles
            List<Vehicule> vehiculesDisponibles = vehiculesAvecEtatOk.stream().filter(v -> {
                try {
                    boolean aMissionConflictuelle = !missionRepository.findOverlappingMissionsForVehicule(conn, v.getIdVehicule(), dateDebut, dateFin).isEmpty();
                    boolean aAffectationConflictuelle = !affectationRepository.findOverlappingAffectationsForVehicule(conn, v.getIdVehicule(), dateDebut, dateFin).isEmpty();
                    return !aMissionConflictuelle && !aAffectationConflictuelle;
                } catch (SQLException e) {
                    // Logguer l'erreur, mais continuer pour ne pas bloquer toute la liste
                    System.err.println("Erreur lors de la vérification de disponibilité pour véhicule " + v.getIdVehicule() + ": " + e.getMessage());
                    return false; // Exclure en cas d'erreur
                }
            }).collect(Collectors.toList());

            return vehiculeMapper.toDTOList(vehiculesDisponibles);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération des véhicules disponibles.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public List<VehiculeDTO> getVehiculesRequerantMaintenance() throws OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            // Cette logique dépendra de vos critères exacts (ex: kmThreshold, date prochain entretien)
            // Supposons une méthode simple dans VehiculeRepository pour l'instant
            // Par exemple, se baser sur un kmThreshold (ex: tous les 10000km depuis dernier entretien)
            // ou une date de prochain entretien prévisionnel.
            // Pour l'instant, nous allons retourner une liste vide car la logique est complexe et non définie.
            // List<Vehicule> vehicules = vehiculeRepository.findRequiringMaintenance(conn, SOME_KM_THRESHOLD);
            // return vehiculeMapper.toDTOList(vehicules);
            // Pour un exemple plus concret, si vous aviez un champ `km_prochain_entretien` dans VEHICULES:
            // List<Vehicule> vehicules = vehiculeRepository.findAll(conn).stream()
            //    .filter(v -> v.getKmProchainEntretien() != null && v.getKmActuels() >= v.getKmProchainEntretien())
            //    .collect(Collectors.toList());
            // return vehiculeMapper.toDTOList(vehicules);

            // Pour l'instant, implémentation basique, à affiner selon les règles métier exactes
            return Collections.emptyList();
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération des véhicules nécessitant maintenance.", e);
        } finally {
            DbUtil.close(conn);
        }
    }
}