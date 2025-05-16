package main.java.com.miage.parcauto.service.impl;

import main.java.com.miage.parcauto.dao.*;
import main.java.com.miage.parcauto.dao.impl.*;
import main.java.com.miage.parcauto.dto.AffectationDTO;
import main.java.com.miage.parcauto.exception.*;
import main.java.com.miage.parcauto.mapper.AffectationMapper;
import main.java.com.miage.parcauto.mapper.impl.AffectationMapperImpl;
import main.java.com.miage.parcauto.model.affectation.Affectation;
import main.java.com.miage.parcauto.service.AffectationService;
import main.java.com.miage.parcauto.service.VehiculeService; // Pour vérifier la disponibilité

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service de gestion des affectations de véhicules.
 */
public class AffectationServiceImpl implements AffectationService {

    private final AffectationRepository affectationRepository;
    private final VehiculeRepository vehiculeRepository;
    private final PersonnelRepository personnelRepository;
    private final SocietaireCompteRepository societaireCompteRepository;
    private final AffectationMapper affectationMapper;
    private final VehiculeService vehiculeService; // Pour la logique de disponibilité

    /**
     * Constructeur par défaut.
     */
    public AffectationServiceImpl() {
        this.affectationRepository = new AffectationRepositoryImpl();
        this.vehiculeRepository = new VehiculeRepositoryImpl();
        this.personnelRepository = new PersonnelRepositoryImpl();
        this.societaireCompteRepository = new SocietaireCompteRepositoryImpl();
        this.affectationMapper = new AffectationMapperImpl();
        this.vehiculeService = new VehiculeServiceImpl(); // Risque de dépendance circulaire si VehiculeService utilise AffectationService
    }

    /**
     * Constructeur avec injection de dépendances.
     */
    public AffectationServiceImpl(AffectationRepository affectationRepository, VehiculeRepository vehiculeRepository,
                                  PersonnelRepository personnelRepository, SocietaireCompteRepository societaireCompteRepository,
                                  AffectationMapper affectationMapper, VehiculeService vehiculeService) {
        this.affectationRepository = affectationRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.personnelRepository = personnelRepository;
        this.societaireCompteRepository = societaireCompteRepository;
        this.affectationMapper = affectationMapper;
        this.vehiculeService = vehiculeService;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public AffectationDTO createAffectation(AffectationDTO affectationDTO) throws ValidationException, VehiculeNotFoundException, EntityNotFoundException, OperationFailedException {
        if (affectationDTO == null || affectationDTO.getIdVehicule() == null || affectationDTO.getTypeAffectation() == null || affectationDTO.getDateDebut() == null) {
            throw new ValidationException("ID Véhicule, type d'affectation et date de début sont requis.");
        }
        if (affectationDTO.getIdPersonnel() == null && affectationDTO.getIdSocietaire() == null) {
            throw new ValidationException("Une affectation doit être liée soit à un personnel, soit à un sociétaire.");
        }
        if (affectationDTO.getIdPersonnel() != null && affectationDTO.getIdSocietaire() != null) {
            throw new ValidationException("Une affectation ne peut pas être liée à la fois à un personnel et à un sociétaire.");
        }
        if (affectationDTO.getDateFin() != null && affectationDTO.getDateDebut().isAfter(affectationDTO.getDateFin())) {
            throw new ValidationException("La date de début ne peut être postérieure à la date de fin.");
        }

        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            if (vehiculeRepository.findById(conn, affectationDTO.getIdVehicule()).isEmpty()) {
                throw new VehiculeNotFoundException("Véhicule non trouvé avec l'ID: " + affectationDTO.getIdVehicule());
            }
            if (affectationDTO.getIdPersonnel() != null && personnelRepository.findById(conn, affectationDTO.getIdPersonnel()).isEmpty()) {
                throw new EntityNotFoundException("Personnel non trouvé avec l'ID: " + affectationDTO.getIdPersonnel());
            }
            if (affectationDTO.getIdSocietaire() != null && societaireCompteRepository.findById(conn, affectationDTO.getIdSocietaire()).isEmpty()) {
                throw new EntityNotFoundException("Compte sociétaire non trouvé avec l'ID: " + affectationDTO.getIdSocietaire());
            }

            // Vérifier la disponibilité du véhicule sur la période
            if (!vehiculeService.getVehiculesDisponibles(affectationDTO.getDateDebut(), affectationDTO.getDateFin() != null ? affectationDTO.getDateFin() : affectationDTO.getDateDebut().plusYears(5) /* Pour affectation longue durée sans date de fin explicite */)
                    .stream().anyMatch(v -> v.getIdVehicule().equals(affectationDTO.getIdVehicule()))) {
                throw new OperationFailedException("Le véhicule ID " + affectationDTO.getIdVehicule() + " n'est pas disponible pour la période demandée.");
            }


            Affectation affectation = affectationMapper.toEntity(affectationDTO);
            Affectation savedAffectation = affectationRepository.save(conn, affectation);
            conn.commit();
            return affectationMapper.toDTO(savedAffectation);
        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors de la création de l'affectation.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<AffectationDTO> getAffectationById(Integer idAffectation) throws OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            return affectationRepository.findById(conn, idAffectation)
                    .map(affectationMapper::toDTO);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération de l'affectation par ID.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AffectationDTO> getAllAffectations() throws OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            return affectationMapper.toDTOList(affectationRepository.findAll(conn));
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération de toutes les affectations.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AffectationDTO> getAffectationsByVehiculeId(Integer idVehicule) throws VehiculeNotFoundException, OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            if (vehiculeRepository.findById(conn, idVehicule).isEmpty()) {
                throw new VehiculeNotFoundException("Véhicule non trouvé avec l'ID: " + idVehicule);
            }
            return affectationMapper.toDTOList(affectationRepository.findByVehiculeId(conn, idVehicule));
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération des affectations pour le véhicule ID: " + idVehicule, e);
        } finally {
            DbUtil.close(conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AffectationDTO> getAffectationsByPersonnelId(Integer idPersonnel) throws EntityNotFoundException, OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            if (personnelRepository.findById(conn, idPersonnel).isEmpty()) {
                throw new EntityNotFoundException("Personnel non trouvé avec l'ID: " + idPersonnel);
            }
            return affectationMapper.toDTOList(affectationRepository.findByPersonnelId(conn, idPersonnel));
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération des affectations pour le personnel ID: " + idPersonnel, e);
        } finally {
            DbUtil.close(conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AffectationDTO> getAffectationsBySocietaireId(Integer idSocietaireCompte) throws SocietaireNotFoundException, OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            if (societaireCompteRepository.findById(conn, idSocietaireCompte).isEmpty()) {
                throw new SocietaireNotFoundException("Compte sociétaire non trouvé avec l'ID: " + idSocietaireCompte);
            }
            return affectationMapper.toDTOList(affectationRepository.findBySocietaireId(conn, idSocietaireCompte));
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération des affectations pour le sociétaire ID: " + idSocietaireCompte, e);
        } finally {
            DbUtil.close(conn);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public AffectationDTO updateAffectation(AffectationDTO affectationDTO) throws ValidationException, EntityNotFoundException, OperationFailedException {
        if (affectationDTO == null || affectationDTO.getIdAffectation() == null) {
            throw new ValidationException("L'ID de l'affectation est requis pour la mise à jour.");
        }
        if (affectationDTO.getDateFin() != null && affectationDTO.getDateDebut() != null && affectationDTO.getDateDebut().isAfter(affectationDTO.getDateFin())) {
            throw new ValidationException("La date de début ne peut être postérieure à la date de fin.");
        }


        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            Affectation existingAffectation = affectationRepository.findById(conn, affectationDTO.getIdAffectation())
                    .orElseThrow(() -> new EntityNotFoundException("Affectation non trouvée avec l'ID: " + affectationDTO.getIdAffectation()));

            if (affectationDTO.getIdVehicule() != null && !affectationDTO.getIdVehicule().equals(existingAffectation.getIdVehicule())) {
                if (vehiculeRepository.findById(conn, affectationDTO.getIdVehicule()).isEmpty()) {
                    throw new VehiculeNotFoundException("Nouveau véhicule non trouvé avec l'ID: " + affectationDTO.getIdVehicule());
                }
                existingAffectation.setIdVehicule(affectationDTO.getIdVehicule());
            }
            if (affectationDTO.getIdPersonnel() != null && (existingAffectation.getIdPersonnel() == null || !affectationDTO.getIdPersonnel().equals(existingAffectation.getIdPersonnel()))) {
                if (personnelRepository.findById(conn, affectationDTO.getIdPersonnel()).isEmpty()) {
                    throw new EntityNotFoundException("Nouveau personnel non trouvé avec l'ID: " + affectationDTO.getIdPersonnel());
                }
                existingAffectation.setIdPersonnel(affectationDTO.getIdPersonnel());
                existingAffectation.setIdSocietaire(null); // Assurer l'exclusivité
            } else if (affectationDTO.getIdSocietaire() != null && (existingAffectation.getIdSocietaire() == null || !affectationDTO.getIdSocietaire().equals(existingAffectation.getIdSocietaire()))) {
                if (societaireCompteRepository.findById(conn, affectationDTO.getIdSocietaire()).isEmpty()) {
                    throw new EntityNotFoundException("Nouveau compte sociétaire non trouvé avec l'ID: " + affectationDTO.getIdSocietaire());
                }
                existingAffectation.setIdSocietaire(affectationDTO.getIdSocietaire());
                existingAffectation.setIdPersonnel(null); // Assurer l'exclusivité
            }


            if(affectationDTO.getTypeAffectation() != null) existingAffectation.setType(main.java.com.miage.parcauto.model.affectation.TypeAffectation.valueOf(affectationDTO.getTypeAffectation()));
            if(affectationDTO.getDateDebut() != null) existingAffectation.setDateDebut(affectationDTO.getDateDebut());
            if(affectationDTO.getDateFin() != null) existingAffectation.setDateFin(affectationDTO.getDateFin());


            Affectation updatedAffectation = affectationRepository.update(conn, existingAffectation);
            conn.commit();
            return affectationMapper.toDTO(updatedAffectation);
        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors de la mise à jour de l'affectation.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAffectation(Integer idAffectation) throws EntityNotFoundException, OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            if (affectationRepository.findById(conn, idAffectation).isEmpty()) {
                throw new EntityNotFoundException("Affectation non trouvée avec l'ID: " + idAffectation);
            }

            boolean deleted = affectationRepository.delete(conn, idAffectation);
            if(!deleted) {
                throw new OperationFailedException("La suppression de l'affectation a échoué.");
            }
            conn.commit();
        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors de la suppression de l'affectation.", e);
        } finally {
            DbUtil.close(conn);
        }
    }
}