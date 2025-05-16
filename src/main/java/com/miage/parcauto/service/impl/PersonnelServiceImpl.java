package main.java.com.miage.parcauto.service.impl;

import main.java.com.miage.parcauto.dao.DbUtil;
import main.java.com.miage.parcauto.dao.PersonnelRepository;
import main.java.com.miage.parcauto.dao.ServiceRHRepository;
import main.java.com.miage.parcauto.dao.FonctionRepository;
import main.java.com.miage.parcauto.dao.impl.PersonnelRepositoryImpl;
import main.java.com.miage.parcauto.dao.impl.ServiceRHRepositoryImpl;
import main.java.com.miage.parcauto.dao.impl.FonctionRepositoryImpl;
import main.java.com.miage.parcauto.dto.PersonnelDTO;
import main.java.com.miage.parcauto.exception.*;
import main.java.com.miage.parcauto.mapper.PersonnelMapper;
import main.java.com.miage.parcauto.mapper.impl.PersonnelMapperImpl;
import main.java.com.miage.parcauto.model.rh.Personnel;
import main.java.com.miage.parcauto.service.PersonnelService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PersonnelServiceImpl implements PersonnelService {

    private final PersonnelRepository personnelRepository;
    private final ServiceRHRepository serviceRHRepository;
    private final FonctionRepository fonctionRepository;
    private final PersonnelMapper personnelMapper;
    // private final ValidationService validationService;

    public PersonnelServiceImpl() {
        this.personnelRepository = new PersonnelRepositoryImpl();
        this.serviceRHRepository = new ServiceRHRepositoryImpl();
        this.fonctionRepository = new FonctionRepositoryImpl();
        this.personnelMapper = new PersonnelMapperImpl();
        // this.validationService = new ValidationServiceImpl();
    }

    public PersonnelServiceImpl(PersonnelRepository personnelRepository, ServiceRHRepository serviceRHRepository,
                                FonctionRepository fonctionRepository, PersonnelMapper personnelMapper) {
        this.personnelRepository = personnelRepository;
        this.serviceRHRepository = serviceRHRepository;
        this.fonctionRepository = fonctionRepository;
        this.personnelMapper = personnelMapper;
    }

    @Override
    public PersonnelDTO createPersonnel(PersonnelDTO personnelDTO) throws ValidationException, DuplicateEntityException, OperationFailedException {
        // validationService.validatePersonnel(personnelDTO);
        if (personnelDTO == null || personnelDTO.getMatricule() == null || personnelDTO.getMatricule().trim().isEmpty() ||
                personnelDTO.getNom() == null || personnelDTO.getNom().trim().isEmpty() ||
                personnelDTO.getEmail() == null || personnelDTO.getEmail().trim().isEmpty()) {
            throw new ValidationException("Matricule, nom et email sont requis.");
        }
        if (personnelDTO.getIdService() == null || personnelDTO.getIdFonction() == null) {
            throw new ValidationException("Service et Fonction sont requis.");
        }


        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            if (personnelRepository.findByMatricule(conn, personnelDTO.getMatricule()).isPresent()) {
                throw new DuplicateEntityException("Un membre du personnel avec le matricule '" + personnelDTO.getMatricule() + "' existe déjà.");
            }
            if (personnelRepository.findByEmail(conn, personnelDTO.getEmail()).isPresent()) {
                throw new DuplicateEntityException("Un membre du personnel avec l'email '" + personnelDTO.getEmail() + "' existe déjà.");
            }
            if (serviceRHRepository.findById(conn, personnelDTO.getIdService()).isEmpty()){
                throw new ValidationException("Le service avec ID " + personnelDTO.getIdService() + " n'existe pas.");
            }
            if (fonctionRepository.findById(conn, personnelDTO.getIdFonction()).isEmpty()){
                throw new ValidationException("La fonction avec ID " + personnelDTO.getIdFonction() + " n'existe pas.");
            }


            Personnel personnel = personnelMapper.toEntity(personnelDTO);
            Personnel savedPersonnel = personnelRepository.save(conn, personnel);
            conn.commit();
            return personnelMapper.toDTO(savedPersonnel);

        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors de la création du membre du personnel.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public Optional<PersonnelDTO> getPersonnelById(Integer idPersonnel) throws OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            Optional<Personnel> personnelOpt = personnelRepository.findById(conn, idPersonnel);
            return personnelOpt.map(personnelMapper::toDTO);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération du personnel par ID.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public Optional<PersonnelDTO> getPersonnelByMatricule(String matricule) throws OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            Optional<Personnel> personnelOpt = personnelRepository.findByMatricule(conn, matricule);
            return personnelOpt.map(personnelMapper::toDTO);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération du personnel par matricule.", e);
        } finally {
            DbUtil.close(conn);
        }
    }


    @Override
    public List<PersonnelDTO> getAllPersonnel() throws OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            List<Personnel> personnels = personnelRepository.findAll(conn);
            return personnelMapper.toDTOList(personnels);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération de tous les membres du personnel.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public PersonnelDTO updatePersonnel(PersonnelDTO personnelDTO) throws ValidationException, EntityNotFoundException, DuplicateEntityException, OperationFailedException {
        // validationService.validatePersonnel(personnelDTO);
        if (personnelDTO == null || personnelDTO.getIdPersonnel() == null) {
            throw new ValidationException("ID Personnel est requis pour la mise à jour.");
        }

        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            Personnel existingPersonnel = personnelRepository.findById(conn, personnelDTO.getIdPersonnel())
                    .orElseThrow(() -> new EntityNotFoundException("Membre du personnel non trouvé avec l'ID: " + personnelDTO.getIdPersonnel()));

            if (personnelDTO.getMatricule() != null && !personnelDTO.getMatricule().equals(existingPersonnel.getMatricule())) {
                personnelRepository.findByMatricule(conn, personnelDTO.getMatricule()).ifPresent(p -> {
                    if(!p.getIdPersonnel().equals(existingPersonnel.getIdPersonnel())) {
                        throw new RuntimeException(new DuplicateEntityException("Un autre membre du personnel avec le matricule '" + personnelDTO.getMatricule() + "' existe déjà."));
                    }
                });
                existingPersonnel.setMatricule(personnelDTO.getMatricule());
            }
            if (personnelDTO.getEmail() != null && !personnelDTO.getEmail().equals(existingPersonnel.getEmail())) {
                personnelRepository.findByEmail(conn, personnelDTO.getEmail()).ifPresent(p -> {
                    if(!p.getIdPersonnel().equals(existingPersonnel.getIdPersonnel())) {
                        throw new RuntimeException(new DuplicateEntityException("Un autre membre du personnel avec l'email '" + personnelDTO.getEmail() + "' existe déjà."));
                    }
                });
                existingPersonnel.setEmail(personnelDTO.getEmail());
            }

            if (personnelDTO.getIdService() != null) {
                if (serviceRHRepository.findById(conn, personnelDTO.getIdService()).isEmpty()){
                    throw new ValidationException("Le service avec ID " + personnelDTO.getIdService() + " n'existe pas.");
                }
                existingPersonnel.setIdService(personnelDTO.getIdService());
            }
            if (personnelDTO.getIdFonction() != null) {
                if (fonctionRepository.findById(conn, personnelDTO.getIdFonction()).isEmpty()){
                    throw new ValidationException("La fonction avec ID " + personnelDTO.getIdFonction() + " n'existe pas.");
                }
                existingPersonnel.setIdFonction(personnelDTO.getIdFonction());
            }

            if(personnelDTO.getNom() != null) existingPersonnel.setNom(personnelDTO.getNom());
            if(personnelDTO.getPrenom() != null) existingPersonnel.setPrenom(personnelDTO.getPrenom());
            if(personnelDTO.getTelephone() != null) existingPersonnel.setTelephone(personnelDTO.getTelephone());
            if(personnelDTO.getAdresse() != null) existingPersonnel.setAdresse(personnelDTO.getAdresse());
            if(personnelDTO.getDateNaissance() != null) existingPersonnel.setDateNaissance(personnelDTO.getDateNaissance());
            if(personnelDTO.getSexe() != null) existingPersonnel.setSexe(main.java.com.miage.parcauto.model.rh.Sexe.valueOf(personnelDTO.getSexe()));
            if(personnelDTO.getDateEmbauche() != null) existingPersonnel.setDateEmbauche(personnelDTO.getDateEmbauche());
            if(personnelDTO.getObservation() != null) existingPersonnel.setObservation(personnelDTO.getObservation());


            Personnel updatedPersonnel = personnelRepository.update(conn, existingPersonnel);
            conn.commit();
            return personnelMapper.toDTO(updatedPersonnel);

        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors de la mise à jour du membre du personnel.", e);
        } catch (RuntimeException e) {
            DbUtil.rollback(conn);
            if (e.getCause() instanceof DuplicateEntityException) {
                throw (DuplicateEntityException) e.getCause();
            }
            throw new OperationFailedException("Erreur lors de la mise à jour du personnel.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public void deletePersonnel(Integer idPersonnel) throws EntityNotFoundException, OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            if (personnelRepository.findById(conn, idPersonnel).isEmpty()) {
                throw new EntityNotFoundException("Membre du personnel non trouvé avec l'ID: " + idPersonnel);
            }
            // Gérer les dépendances (Utilisateur, Affectation, SocietaireCompte, Mission)
            // Exemple: si un personnel est lié à un utilisateur, la suppression pourrait échouer ou nécessiter une action.

            boolean deleted = personnelRepository.delete(conn, idPersonnel);
            if(!deleted) {
                throw new OperationFailedException("La suppression du membre du personnel a échoué.");
            }
            conn.commit();
        } catch (SQLException e) {
            DbUtil.rollback(conn);
            if (e.getSQLState().startsWith("23")) {
                throw new OperationFailedException("Impossible de supprimer le membre du personnel car il est référencé (ex: utilisateur, affectations).", e);
            }
            throw new OperationFailedException("Erreur technique lors de la suppression du membre du personnel.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public List<PersonnelDTO> getPersonnelByServiceId(Integer idService) throws OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            List<Personnel> personnels = personnelRepository.findByServiceId(conn, idService);
            return personnelMapper.toDTOList(personnels);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur lors de la récupération du personnel par service.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    @Override
    public List<PersonnelDTO> getPersonnelByFonctionId(Integer idFonction) throws OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            List<Personnel> personnels = personnelRepository.findByFonctionId(conn, idFonction);
            return personnelMapper.toDTOList(personnels);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur lors de la récupération du personnel par fonction.", e);
        } finally {
            DbUtil.close(conn);
        }
    }
}