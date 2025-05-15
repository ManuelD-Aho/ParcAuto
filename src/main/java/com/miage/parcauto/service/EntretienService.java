package main.java.com.miage.parcauto.service;

import main.java.com.miage.parcauto.dao.EntretienRepositoryImpl;
import main.java.com.miage.parcauto.dao.VehiculeRepositoryImpl;
import main.java.com.miage.parcauto.dto.EntretienDTO;
import main.java.com.miage.parcauto.exception.DatabaseException;
import main.java.com.miage.parcauto.exception.ValidationException;
import main.java.com.miage.parcauto.mapper.EntretienMapper;
import main.java.com.miage.parcauto.model.entretien.Entretien;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service de gestion des entretiens (version refactorisée Repository/DTO).
 * Utilise EntretienRepository, DTOs, ValidationService et exceptions
 * personnalisées.
 *
 * @author MIAGE Holding
 * @version 2.0
 */
public class EntretienService {

    private static final Logger LOGGER = Logger.getLogger(EntretienService.class.getName());
    private final EntretienRepository entretienRepository;
    private final VehiculeRepository vehiculeRepository;
    private final EntretienMapper entretienMapper;
    private final ValidationService validationService;

    public EntretienService() {
        this.entretienRepository = new EntretienRepositoryImpl();
        this.vehiculeRepository = new VehiculeRepositoryImpl();
        this.entretienMapper = new EntretienMapper(vehiculeRepository);
        this.validationService = new ValidationService();
    }

    public List<EntretienDTO> getAllEntretiens() {
        try {
            List<Entretien> entretiens = entretienRepository.findAll();
            return entretienMapper.toDTOList(entretiens);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des entretiens", e);
            throw new DatabaseException("Erreur lors de la récupération des entretiens", e);
        }
    }

    public Optional<EntretienDTO> getEntretienById(Integer id) {
        try {
            return entretienRepository.findById(id).map(entretienMapper::toDTO);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de l'entretien", e);
            throw new DatabaseException("Erreur lors de la recherche de l'entretien", e);
        }
    }

    public List<EntretienDTO> getEntretiensVehicule(Integer idVehicule) {
        try {
            List<Entretien> entretiens = entretienRepository.findByVehicule(idVehicule);
            return entretienMapper.toDTOList(entretiens);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des entretiens du véhicule", e);
            throw new DatabaseException("Erreur lors de la récupération des entretiens du véhicule", e);
        }
    }

    public boolean createEntretien(EntretienDTO entretienDTO) {
        ValidationService.ValidationResult result = validationService.validateEntretien(entretienDTO);
        if (!result.isValid()) {
            throw new ValidationException(result);
        }
        try {
            Entretien entretien = entretienMapper.toEntity(entretienDTO);
            entretienRepository.save(entretien);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création de l'entretien", e);
            throw new DatabaseException("Erreur lors de la création de l'entretien", e);
        }
    }

    public boolean updateEntretien(EntretienDTO entretienDTO) {
        ValidationService.ValidationResult result = validationService.validateEntretien(entretienDTO);
        if (!result.isValid() || entretienDTO.getIdEntretien() == null) {
            throw new ValidationException(result);
        }
        try {
            Entretien entretien = entretienMapper.toEntity(entretienDTO);
            entretienRepository.update(entretien);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de l'entretien", e);
            throw new DatabaseException("Erreur lors de la mise à jour de l'entretien", e);
        }
    }

    public boolean terminerEntretien(Integer id, String observations) {
        try {
            Optional<Entretien> opt = entretienRepository.findById(id);
            if (opt.isEmpty()) {
                throw new DatabaseException("Entretien non trouvé pour clôture");
            }
            Entretien entretien = opt.get();
            entretien.setStatutOt(Entretien.StatutOT.Cloture);
            entretien.setObservation(observations);
            entretien.setDateSortieEntr(LocalDateTime.now());
            entretienRepository.update(entretien);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la clôture de l'entretien", e);
            throw new DatabaseException("Erreur lors de la clôture de l'entretien", e);
        }
    }

    public boolean deleteEntretien(Integer id) {
        try {
            Optional<Entretien> entretien = entretienRepository.findById(id);
            if (entretien.isPresent() && entretien.get().getStatutOt() == Entretien.StatutOT.EnCours) {
                throw new ValidationException("Impossible de supprimer un entretien en cours");
            }
            return entretienRepository.delete(id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de l'entretien", e);
            throw new DatabaseException("Erreur lors de la suppression de l'entretien", e);
        }
    }

    public List<EntretienDTO> getEntretiensPlanifies() {
        try {
            List<Entretien> entretiens = entretienRepository.findPlanifies();
            return entretienMapper.toDTOList(entretiens);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des entretiens planifiés", e);
            throw new DatabaseException("Erreur lors de la récupération des entretiens planifiés", e);
        }
    }

    public List<EntretienDTO> getEntretiensTermines() {
        try {
            List<Entretien> entretiens = entretienRepository.findTermines();
            return entretienMapper.toDTOList(entretiens);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des entretiens terminés", e);
            throw new DatabaseException("Erreur lors de la récupération des entretiens terminés", e);
        }
    }

    public List<EntretienDTO> getEntretiensByPeriode(LocalDateTime debut, LocalDateTime fin) {
        try {
            List<Entretien> entretiens = entretienRepository.findByPeriode(debut, fin);
            return entretienMapper.toDTOList(entretiens);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des entretiens par période", e);
            throw new DatabaseException("Erreur lors de la récupération des entretiens par période", e);
        }
    }

    public BigDecimal calculateTotalCost(int idVehicule) {
        try {
            return entretienRepository.calculateTotalCost(idVehicule);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul du coût total des entretiens", e);
            throw new DatabaseException("Erreur lors du calcul du coût total des entretiens", e);
        }
    }

    public int getEntretiensAVenirCount() {
        try {
            List<EntretienDTO> planifies = getEntretiensPlanifies();
            return planifies != null ? planifies.size() : 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des entretiens à venir", e);
            return 0;
        }
    }
}