package main.java.com.miage.parcauto.service.impl;

import main.java.com.miage.parcauto.dao.DbUtil;
import main.java.com.miage.parcauto.dao.AssuranceRepository;
import main.java.com.miage.parcauto.dao.VehiculeRepository;
import main.java.com.miage.parcauto.dao.CouvrirRepository;
import main.java.com.miage.parcauto.dao.impl.AssuranceRepositoryImpl;
import main.java.com.miage.parcauto.dao.impl.VehiculeRepositoryImpl;
import main.java.com.miage.parcauto.dao.impl.CouvrirRepositoryImpl;
import main.java.com.miage.parcauto.dto.AssuranceDTO;
import main.java.com.miage.parcauto.dto.VehiculeDTO;
import main.java.com.miage.parcauto.exception.*;
import main.java.com.miage.parcauto.mapper.AssuranceMapper;
import main.java.com.miage.parcauto.mapper.VehiculeMapper;
import main.java.com.miage.parcauto.mapper.impl.AssuranceMapperImpl;
import main.java.com.miage.parcauto.mapper.impl.VehiculeMapperImpl;
import main.java.com.miage.parcauto.model.assurance.Assurance;
import main.java.com.miage.parcauto.model.assurance.Couvrir;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;
import main.java.com.miage.parcauto.service.AssuranceService;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des assurances.
 */
public class AssuranceServiceImpl implements AssuranceService {

    private final AssuranceRepository assuranceRepository;
    private final VehiculeRepository vehiculeRepository;
    private final CouvrirRepository couvrirRepository;
    private final AssuranceMapper assuranceMapper;
    private final VehiculeMapper vehiculeMapper;

    /**
     * Constructeur par défaut.
     */
    public AssuranceServiceImpl() {
        this.assuranceRepository = new AssuranceRepositoryImpl();
        this.vehiculeRepository = new VehiculeRepositoryImpl();
        this.couvrirRepository = new CouvrirRepositoryImpl();
        this.assuranceMapper = new AssuranceMapperImpl();
        this.vehiculeMapper = new VehiculeMapperImpl();
    }

    /**
     * Constructeur avec injection de dépendances.
     * @param assuranceRepository Le repository pour les assurances.
     * @param vehiculeRepository Le repository pour les véhicules.
     * @param couvrirRepository Le repository pour la liaison assurance-véhicule.
     * @param assuranceMapper Le mapper pour les assurances.
     * @param vehiculeMapper Le mapper pour les véhicules.
     */
    public AssuranceServiceImpl(AssuranceRepository assuranceRepository, VehiculeRepository vehiculeRepository, CouvrirRepository couvrirRepository, AssuranceMapper assuranceMapper, VehiculeMapper vehiculeMapper) {
        this.assuranceRepository = assuranceRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.couvrirRepository = couvrirRepository;
        this.assuranceMapper = assuranceMapper;
        this.vehiculeMapper = vehiculeMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssuranceDTO createAssurance(AssuranceDTO assuranceDTO) throws ValidationException, OperationFailedException {
        if (assuranceDTO == null || assuranceDTO.getDateDebut() == null || assuranceDTO.getDateFin() == null || assuranceDTO.getAgence() == null || assuranceDTO.getAgence().trim().isEmpty()) {
            throw new ValidationException("Agence, date de début et date de fin sont requises pour une assurance.");
        }
        if (assuranceDTO.getDateDebut().isAfter(assuranceDTO.getDateFin())) {
            throw new ValidationException("La date de début ne peut pas être postérieure à la date de fin.");
        }

        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            Assurance assurance = assuranceMapper.toEntity(assuranceDTO);
            Assurance savedAssurance = assuranceRepository.save(conn, assurance);
            conn.commit();
            return assuranceMapper.toDTO(savedAssurance);
        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors de la création de l'assurance.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<AssuranceDTO> getAssuranceByNumCarte(Integer numCarteAssurance) throws OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            return assuranceRepository.findById(conn, numCarteAssurance)
                    .map(assuranceMapper::toDTO);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération de l'assurance par numéro de carte.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AssuranceDTO> getAllAssurances() throws OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            return assuranceMapper.toDTOList(assuranceRepository.findAll(conn));
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération de toutes les assurances.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AssuranceDTO updateAssurance(AssuranceDTO assuranceDTO) throws ValidationException, EntityNotFoundException, OperationFailedException {
        if (assuranceDTO == null || assuranceDTO.getNumCarteAssurance() == null) {
            throw new ValidationException("Le numéro de carte d'assurance est requis pour la mise à jour.");
        }
        if (assuranceDTO.getDateDebut() != null && assuranceDTO.getDateFin() != null && assuranceDTO.getDateDebut().isAfter(assuranceDTO.getDateFin())) {
            throw new ValidationException("La date de début ne peut pas être postérieure à la date de fin.");
        }

        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            Assurance existingAssurance = assuranceRepository.findById(conn, assuranceDTO.getNumCarteAssurance())
                    .orElseThrow(() -> new EntityNotFoundException("Assurance non trouvée avec le numéro de carte: " + assuranceDTO.getNumCarteAssurance()));

            if(assuranceDTO.getDateDebut() != null) existingAssurance.setDateDebutAssurance(assuranceDTO.getDateDebut());
            if(assuranceDTO.getDateFin() != null) existingAssurance.setDateFinAssurance(assuranceDTO.getDateFin());
            if(assuranceDTO.getAgence() != null) existingAssurance.setAgence(assuranceDTO.getAgence());
            if(assuranceDTO.getCout() != null) existingAssurance.setCoutAssurance(assuranceDTO.getCout());

            Assurance updatedAssurance = assuranceRepository.update(conn, existingAssurance);
            conn.commit();
            return assuranceMapper.toDTO(updatedAssurance);
        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors de la mise à jour de l'assurance.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAssurance(Integer numCarteAssurance) throws EntityNotFoundException, OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            if (assuranceRepository.findById(conn, numCarteAssurance).isEmpty()) {
                throw new EntityNotFoundException("Assurance non trouvée avec le numéro de carte: " + numCarteAssurance);
            }
            // Supprimer d'abord les liaisons dans COUVRIR
            couvrirRepository.deleteByNumCarteAssurance(conn, numCarteAssurance);

            boolean deleted = assuranceRepository.delete(conn, numCarteAssurance);
            if(!deleted) {
                throw new OperationFailedException("La suppression de l'assurance a échoué.");
            }
            conn.commit();
        } catch (SQLException e) {
            DbUtil.rollback(conn);
            if (e.getSQLState().startsWith("23")) {
                throw new OperationFailedException("Impossible de supprimer l'assurance car elle est référencée ailleurs (cela ne devrait pas arriver si COUVRIR est bien géré).", e);
            }
            throw new OperationFailedException("Erreur technique lors de la suppression de l'assurance.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void lierVehiculeAssurance(Integer idVehicule, Integer numCarteAssurance) throws VehiculeNotFoundException, EntityNotFoundException, OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            if (vehiculeRepository.findById(conn, idVehicule).isEmpty()) {
                throw new VehiculeNotFoundException("Véhicule non trouvé avec l'ID: " + idVehicule);
            }
            if (assuranceRepository.findById(conn, numCarteAssurance).isEmpty()) {
                throw new EntityNotFoundException("Assurance non trouvée avec le numéro de carte: " + numCarteAssurance);
            }
            if (couvrirRepository.findByIdVehiculeAndIdAssurance(conn, idVehicule, numCarteAssurance).isPresent()) {
                throw new OperationFailedException("Le véhicule ID " + idVehicule + " est déjà lié à l'assurance " + numCarteAssurance + ".");
            }

            Couvrir lien = new Couvrir();
            lien.setIdVehicule(idVehicule);
            lien.setNumCarteAssurance(numCarteAssurance);
            couvrirRepository.save(conn, lien);
            conn.commit();
        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors de la liaison véhicule-assurance.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delierVehiculeAssurance(Integer idVehicule, Integer numCarteAssurance) throws VehiculeNotFoundException, EntityNotFoundException, OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            if (vehiculeRepository.findById(conn, idVehicule).isEmpty()) {
                throw new VehiculeNotFoundException("Véhicule non trouvé avec l'ID: " + idVehicule);
            }
            if (assuranceRepository.findById(conn, numCarteAssurance).isEmpty()) {
                throw new EntityNotFoundException("Assurance non trouvée avec le numéro de carte: " + numCarteAssurance);
            }

            boolean deleted = couvrirRepository.delete(conn, idVehicule, numCarteAssurance);
            if (!deleted) {
                throw new OperationFailedException("La liaison entre le véhicule ID " + idVehicule + " et l'assurance " + numCarteAssurance + " n'existait pas ou la suppression a échoué.");
            }
            conn.commit();
        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors de la suppression de la liaison véhicule-assurance.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<VehiculeDTO> getVehiculesByAssurance(Integer numCarteAssurance) throws EntityNotFoundException, OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            if (assuranceRepository.findById(conn, numCarteAssurance).isEmpty()) {
                throw new EntityNotFoundException("Assurance non trouvée avec le numéro de carte: " + numCarteAssurance);
            }
            List<Vehicule> vehicules = couvrirRepository.findVehiculesByNumCarteAssurance(conn, numCarteAssurance);
            return vehiculeMapper.toDTOList(vehicules);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération des véhicules pour l'assurance.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AssuranceDTO> getAssurancesByVehicule(Integer idVehicule) throws VehiculeNotFoundException, OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            if (vehiculeRepository.findById(conn, idVehicule).isEmpty()) {
                throw new VehiculeNotFoundException("Véhicule non trouvé avec l'ID: " + idVehicule);
            }
            List<Assurance> assurances = couvrirRepository.findAssurancesByIdVehicule(conn, idVehicule);
            return assuranceMapper.toDTOList(assurances);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération des assurances pour le véhicule.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AssuranceDTO> getAssurancesExpirantBientot(LocalDateTime dateLimite) throws OperationFailedException {
        if (dateLimite == null) {
            throw new OperationFailedException(new ValidationException("La date limite ne peut être nulle."));
        }
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            List<Assurance> assurances = assuranceRepository.findExpiringBefore(conn, dateLimite);
            return assuranceMapper.toDTOList(assurances);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération des assurances expirant bientôt.", e);
        } finally {
            DbUtil.close(conn);
        }
    }
}