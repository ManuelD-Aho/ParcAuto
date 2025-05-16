package main.java.com.miage.parcauto.service.impl;

import main.java.com.miage.parcauto.dao.*;
import main.java.com.miage.parcauto.dao.impl.*;
import main.java.com.miage.parcauto.dto.DepenseMissionDTO;
import main.java.com.miage.parcauto.dto.MissionDTO;
import main.java.com.miage.parcauto.exception.*;
import main.java.com.miage.parcauto.mapper.DepenseMissionMapper;
import main.java.com.miage.parcauto.mapper.MissionMapper;
import main.java.com.miage.parcauto.mapper.impl.DepenseMissionMapperImpl;
import main.java.com.miage.parcauto.mapper.impl.MissionMapperImpl;
import main.java.com.miage.parcauto.model.mission.DepenseMission;
import main.java.com.miage.parcauto.model.mission.Mission;
import main.java.com.miage.parcauto.model.mission.StatutMission;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;
import main.java.com.miage.parcauto.service.MissionService;
import main.java.com.miage.parcauto.service.VehiculeService; // Pour vérifier disponibilité

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class MissionServiceImpl implements MissionService {

    private final MissionRepository missionRepository;
    private final VehiculeRepository vehiculeRepository;
    private final PersonnelRepository personnelRepository; // Ou UtilisateurRepository si idPersonnel est dans Utilisateur
    private final DepenseMissionRepository depenseMissionRepository;
    private final MissionMapper missionMapper;
    private final DepenseMissionMapper depenseMissionMapper;
    private final VehiculeService vehiculeService; // Pour la logique de disponibilité

    public MissionServiceImpl() {
        this.missionRepository = new MissionRepositoryImpl();
        this.vehiculeRepository = new VehiculeRepositoryImpl();
        this.personnelRepository = new PersonnelRepositoryImpl();
        this.depenseMissionRepository = new DepenseMissionRepositoryImpl();
        this.missionMapper = new MissionMapperImpl();
        this.depenseMissionMapper = new DepenseMissionMapperImpl();
        this.vehiculeService = new VehiculeServiceImpl(); // Attention aux dépendances cycliques si VehiculeService utilise MissionService
    }

    // Constructeur pour injection
    public MissionServiceImpl(MissionRepository missionRepository, VehiculeRepository vehiculeRepository,
                              PersonnelRepository personnelRepository, DepenseMissionRepository depenseMissionRepository,
                              MissionMapper missionMapper, DepenseMissionMapper depenseMissionMapper, VehiculeService vehiculeService) {
        this.missionRepository = missionRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.personnelRepository = personnelRepository;
        this.depenseMissionRepository = depenseMissionRepository;
        this.missionMapper = missionMapper;
        this.depenseMissionMapper = depenseMissionMapper;
        this.vehiculeService = vehiculeService;
    }


    @Override
    public MissionDTO createMission(MissionDTO missionDTO) throws ValidationException, VehiculeNotFoundException, UtilisateurNotFoundException, OperationFailedException {
        // validationService.validateMission(missionDTO);
        if (missionDTO == null || missionDTO.getIdVehicule() == null || missionDTO.getDateDebut() == null || missionDTO.getDateFinPrevue() == null) {
            throw new ValidationException("ID Véhicule, date de début et date de fin prévue sont requis.");
        }
        if (missionDTO.getDateDebut().isAfter(missionDTO.getDateFinPrevue())) {
            throw new ValidationException("La date de début ne peut pas être après la date de fin prévue.");
        }
        // if (missionDTO.getIdPersonnel() == null) { // Si le personnel est obligatoire pour une mission
        //    throw new ValidationException("Un personnel doit être assigné à la mission.");
        // }

        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            if (vehiculeRepository.findById(conn, missionDTO.getIdVehicule()).isEmpty()) {
                throw new VehiculeNotFoundException("Véhicule non trouvé avec l'ID: " + missionDTO.getIdVehicule());
            }
            // Vérifier la disponibilité du véhicule sur la période
            List<VehiculeDTO> vehiculesDispo = vehiculeService.getVehiculesDisponibles(missionDTO.getDateDebut(), missionDTO.getDateFinPrevue());
            boolean estDisponible = vehiculesDispo.stream().anyMatch(v -> v.getIdVehicule().equals(missionDTO.getIdVehicule()));
            if (!estDisponible) {
                throw new OperationFailedException("Le véhicule ID " + missionDTO.getIdVehicule() + " n'est pas disponible pour la période demandée.");
            }


            // if (missionDTO.getIdPersonnel() != null && personnelRepository.findById(conn, missionDTO.getIdPersonnel()).isEmpty()) {
            //    throw new UtilisateurNotFoundException("Personnel non trouvé avec l'ID: " + missionDTO.getIdPersonnel());
            // }

            Mission mission = missionMapper.toEntity(missionDTO);
            if (mission.getStatut() == null) {
                mission.setStatut(StatutMission.PLANIFIEE);
            }

            Mission savedMission = missionRepository.save(conn, mission);
            conn.commit();
            return missionMapper.toDTO(savedMission);

        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors de la création de la mission.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public Optional<MissionDTO> getMissionById(Integer idMission) throws OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            Optional<Mission> missionOpt = missionRepository.findById(conn, idMission);
            return missionOpt.map(missionMapper::toDTO);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération de la mission par ID.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public List<MissionDTO> getAllMissions() throws OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            List<Mission> missions = missionRepository.findAll(conn);
            return missionMapper.toDTOList(missions);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération de toutes les missions.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public List<MissionDTO> getMissionsByVehiculeId(Integer idVehicule) throws VehiculeNotFoundException, OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            if (vehiculeRepository.findById(conn, idVehicule).isEmpty()) {
                throw new VehiculeNotFoundException("Véhicule non trouvé avec l'ID: " + idVehicule);
            }
            List<Mission> missions = missionRepository.findByVehiculeId(conn, idVehicule);
            return missionMapper.toDTOList(missions);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération des missions pour le véhicule ID: " + idVehicule, e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public List<MissionDTO> getMissionsByPersonnelId(Integer idPersonnel) throws UtilisateurNotFoundException, OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            // Assumant que idPersonnel est dans Mission et PersonnelRepository existe
            // if (personnelRepository.findById(conn, idPersonnel).isEmpty()) {
            //    throw new UtilisateurNotFoundException("Personnel non trouvé avec l'ID: " + idPersonnel);
            // }
            // List<Mission> missions = missionRepository.findByPersonnelId(conn, idPersonnel); // Méthode à ajouter au repo
            // return missionMapper.toDTOList(missions);
            return Collections.emptyList(); // Placeholder
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération des missions pour le personnel ID: " + idPersonnel, e);
        } finally {
            DbUtil.close(conn);
        }
    }


    @Override
    public MissionDTO updateMission(MissionDTO missionDTO) throws ValidationException, MissionNotFoundException, VehiculeNotFoundException, UtilisateurNotFoundException, OperationFailedException {
        // validationService.validateMission(missionDTO);
        if (missionDTO == null || missionDTO.getIdMission() == null) {
            throw new ValidationException("ID Mission est requis pour la mise à jour.");
        }

        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            Mission existingMission = missionRepository.findById(conn, missionDTO.getIdMission())
                    .orElseThrow(() -> new MissionNotFoundException("Mission non trouvée avec l'ID: " + missionDTO.getIdMission()));

            // Logique de validation pour les modifications (ex: on ne modifie pas une mission clôturée)
            if (existingMission.getStatut() == StatutMission.CLOTUREE && missionDTO.getStatut() != null && !StatutMission.CLOTUREE.name().equals(missionDTO.getStatut())) {
                throw new ValidationException("Une mission clôturée ne peut pas changer de statut (sauf potentiellement pour réouverture admin).");
            }


            if (missionDTO.getIdVehicule() != null && !missionDTO.getIdVehicule().equals(existingMission.getIdVehicule())) {
                if (vehiculeRepository.findById(conn, missionDTO.getIdVehicule()).isEmpty()) {
                    throw new VehiculeNotFoundException("Nouveau véhicule associé non trouvé avec l'ID: " + missionDTO.getIdVehicule());
                }
                // Vérifier disponibilité si dates changent ou véhicule change
                LocalDateTime debutCheck = missionDTO.getDateDebut() != null ? missionDTO.getDateDebut() : existingMission.getDateDebutMission();
                LocalDateTime finCheck = missionDTO.getDateFinPrevue() != null ? missionDTO.getDateFinPrevue() : existingMission.getDateFinMission();
                if (debutCheck != null && finCheck != null) {
                    List<VehiculeDTO> vehiculesDispo = vehiculeService.getVehiculesDisponibles(debutCheck, finCheck);
                    boolean estDisponible = vehiculesDispo.stream().anyMatch(v -> v.getIdVehicule().equals(missionDTO.getIdVehicule()));
                    if (!estDisponible && !missionRepository.findOverlappingMissionsForVehicule(conn, missionDTO.getIdVehicule(), debutCheck, finCheck)
                            .stream().allMatch(m -> m.getIdMission().equals(existingMission.getIdMission()))) { // Exclure la mission actuelle de la vérification de conflit
                        throw new OperationFailedException("Le véhicule ID " + missionDTO.getIdVehicule() + " n'est pas disponible pour la période modifiée.");
                    }
                }
                existingMission.setIdVehicule(missionDTO.getIdVehicule());
            }

            // if (missionDTO.getIdPersonnel() != null && !missionDTO.getIdPersonnel().equals(existingMission.getIdPersonnel())) {
            //    if (personnelRepository.findById(conn, missionDTO.getIdPersonnel()).isEmpty()) {
            //        throw new UtilisateurNotFoundException("Nouveau personnel associé non trouvé avec l'ID: " + missionDTO.getIdPersonnel());
            //    }
            //    existingMission.setIdPersonnel(missionDTO.getIdPersonnel());
            // }

            if(missionDTO.getLibelle() != null) existingMission.setLibelle(missionDTO.getLibelle());
            if(missionDTO.getSiteDestination() != null) existingMission.setSite(missionDTO.getSiteDestination());
            if(missionDTO.getDateDebut() != null) existingMission.setDateDebutMission(missionDTO.getDateDebut());
            if(missionDTO.getDateFinPrevue() != null) existingMission.setDateFinMission(missionDTO.getDateFinPrevue());
            if(missionDTO.getDateFinEffective() != null) existingMission.setDateFinEffective(missionDTO.getDateFinEffective());
            if(missionDTO.getKmPrevu() != null) existingMission.setKmPrevu(missionDTO.getKmPrevu());
            if(missionDTO.getKmReel() != null) existingMission.setKmReel(missionDTO.getKmReel());
            if(missionDTO.getStatut() != null) existingMission.setStatut(StatutMission.valueOf(missionDTO.getStatut()));
            if(missionDTO.getCoutEstime() != null) existingMission.setCoutEstime(missionDTO.getCoutEstime());
            if(missionDTO.getCoutTotalReel() != null) existingMission.setCoutTotal(missionDTO.getCoutTotalReel());
            if(missionDTO.getCircuit() != null) existingMission.setCircuitMission(missionDTO.getCircuit());
            if(missionDTO.getObservations() != null) existingMission.setObservationMission(missionDTO.getObservations());


            Mission updatedMission = missionRepository.update(conn, existingMission);
            conn.commit();
            return missionMapper.toDTO(updatedMission);

        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors de la mise à jour de la mission.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public void deleteMission(Integer idMission) throws MissionNotFoundException, OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            Mission mission = missionRepository.findById(conn, idMission)
                    .orElseThrow(() -> new MissionNotFoundException("Mission non trouvée avec l'ID: " + idMission));

            if (mission.getStatut() == StatutMission.ENCOURS) {
                throw new OperationFailedException("Impossible de supprimer une mission en cours.");
            }
            // Supprimer d'abord les dépenses associées
            List<DepenseMission> depenses = depenseMissionRepository.findByMissionId(conn, idMission);
            for (DepenseMission depense : depenses) {
                depenseMissionRepository.delete(conn, depense.getId());
            }

            boolean deleted = missionRepository.delete(conn, idMission);
            if (!deleted) {
                throw new OperationFailedException("La suppression de la mission a échoué.");
            }
            conn.commit();
        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors de la suppression de la mission.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public MissionDTO terminerMission(Integer idMission, int kmRetour, LocalDateTime dateFinEffective, String observations) throws MissionNotFoundException, ValidationException, OperationFailedException {
        if (kmRetour < 0) {
            throw new ValidationException("Le kilométrage de retour ne peut pas être négatif.");
        }
        if (dateFinEffective == null) {
            throw new ValidationException("La date de fin effective est requise.");
        }

        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            Mission mission = missionRepository.findById(conn, idMission)
                    .orElseThrow(() -> new MissionNotFoundException("Mission non trouvée avec l'ID: " + idMission));

            if (mission.getStatut() == StatutMission.CLOTUREE) {
                throw new ValidationException("La mission est déjà clôturée.");
            }
            if (mission.getDateDebutMission() != null && dateFinEffective.isBefore(mission.getDateDebutMission())) {
                throw new ValidationException("La date de fin effective ne peut pas être antérieure à la date de début.");
            }

            Vehicule vehicule = vehiculeRepository.findById(conn, mission.getIdVehicule())
                    .orElseThrow(() -> new OperationFailedException("Véhicule associé à la mission non trouvé. ID: " + mission.getIdVehicule()));

            if (kmRetour < vehicule.getKmActuels() && (mission.getKmPrevu() != null && kmRetour < mission.getKmPrevu())) { // Tolérance si kmPrevu était déjà plus bas
                System.err.println("Avertissement: Kilométrage de retour ("+kmRetour+") inférieur au kilométrage actuel du véhicule ("+vehicule.getKmActuels()+") ou au km prévu ("+mission.getKmPrevu()+").");
            }


            mission.setKmReel(kmRetour);
            mission.setDateFinEffective(dateFinEffective);
            mission.setStatut(StatutMission.CLOTUREE);
            mission.setObservationMission((mission.getObservationMission() == null ? "" : mission.getObservationMission() + "\n") + "Clôture: " + (observations == null ? "" : observations));

            // Le trigger `trg_mission_cloturee` dans la DB devrait mettre à jour `VEHICULES.km_actuels`
            // Si le trigger n'est pas fiable ou si on veut le faire en code :
            // if (kmRetour > vehicule.getKmActuels()) {
            //     vehicule.setKmActuels(kmRetour);
            //     vehiculeRepository.update(conn, vehicule);
            // }

            Mission updatedMission = missionRepository.update(conn, mission);
            conn.commit();
            return missionMapper.toDTO(updatedMission);

        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors de la clôture de la mission.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public DepenseMissionDTO ajouterDepenseAMission(Integer idMission, DepenseMissionDTO depenseDTO) throws MissionNotFoundException, ValidationException, OperationFailedException {
        // validationService.validateDepenseMission(depenseDTO);
        if (depenseDTO == null || depenseDTO.getMontant() == null || depenseDTO.getNature() == null) {
            throw new ValidationException("Montant et nature sont requis pour une dépense.");
        }
        if (depenseDTO.getMontant().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Le montant de la dépense doit être positif.");
        }

        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            Mission mission = missionRepository.findById(conn, idMission)
                    .orElseThrow(() -> new MissionNotFoundException("Mission non trouvée avec l'ID: " + idMission));

            if (mission.getStatut() == StatutMission.CLOTUREE) {
                throw new ValidationException("Impossible d'ajouter une dépense à une mission clôturée.");
            }


            DepenseMission depense = depenseMissionMapper.toEntity(depenseDTO);
            depense.setIdMission(idMission); // Assurer la liaison
            if (depense.getDateDepense() == null) {
                depense.setDateDepense(LocalDateTime.now());
            }


            DepenseMission savedDepense = depenseMissionRepository.save(conn, depense);

            // Mettre à jour le coût total de la mission
            List<DepenseMission> toutesLesDepenses = depenseMissionRepository.findByMissionId(conn, idMission);
            java.math.BigDecimal coutTotalDepenses = toutesLesDepenses.stream()
                    .map(DepenseMission::getMontant)
                    .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            mission.setCoutTotal(coutTotalDepenses); // cout_total dans la table MISSION
            missionRepository.update(conn, mission);

            conn.commit();
            return depenseMissionMapper.toDTO(savedDepense);

        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors de l'ajout de la dépense à la mission.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public List<DepenseMissionDTO> getDepensesByMissionId(Integer idMission) throws MissionNotFoundException, OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            if (missionRepository.findById(conn, idMission).isEmpty()) {
                throw new MissionNotFoundException("Mission non trouvée avec l'ID: " + idMission);
            }
            List<DepenseMission> depenses = depenseMissionRepository.findByMissionId(conn, idMission);
            return depenseMissionMapper.toDTOList(depenses);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération des dépenses pour la mission ID: " + idMission, e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public List<MissionDTO> getMissionsActivesPourVehicule(Integer idVehicule) throws VehiculeNotFoundException, OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            if (vehiculeRepository.findById(conn, idVehicule).isEmpty()) {
                throw new VehiculeNotFoundException("Véhicule non trouvé avec l'ID: " + idVehicule);
            }
            List<Mission> missions = missionRepository.findActiveByVehiculeId(conn, idVehicule);
            return missionMapper.toDTOList(missions);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur lors de la recherche des missions actives pour le véhicule " + idVehicule, e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public List<MissionDTO> getMissionsParPeriode(LocalDateTime dateDebut, LocalDateTime dateFin) throws OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            List<Mission> missions = missionRepository.findByPeriod(conn, dateDebut, dateFin);
            return missionMapper.toDTOList(missions);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur lors de la recherche des missions pour la période.", e);
        } finally {
            DbUtil.close(conn);
        }
    }
}