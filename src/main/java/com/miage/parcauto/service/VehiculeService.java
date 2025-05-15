package main.java.com.miage.parcauto.service;

import main.java.com.miage.parcauto.dto.VehiculeDTO;
import main.java.com.miage.parcauto.exception.DatabaseException;
import main.java.com.miage.parcauto.exception.ValidationException;
import main.java.com.miage.parcauto.mapper.VehiculeMapper;
import main.java.com.miage.parcauto.model.vehicule.EtatVoiture;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service de gestion des véhicules (version refactorisée Repository/DTO).
 * Utilise VehiculeRepository, DTOs, ValidationService et exceptions
 * personnalisées.
 *
 * @author MIAGE Holding
 * @version 2.0
 */
public class VehiculeService {

    private static final Logger LOGGER = Logger.getLogger(VehiculeService.class.getName());
    private final VehiculeRepository vehiculeRepository;
    private final ValidationService validationService;

    public VehiculeService() {
        this.vehiculeRepository = new VehiculeRepositoryImpl();
        this.validationService = new ValidationService();
    }

    public List<VehiculeDTO> getAllVehicules() {
        try {
            List<Vehicule> vehicules = vehiculeRepository.findAll();
            return VehiculeMapper.toDTOList(vehicules);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des véhicules", e);
            throw new DatabaseException("Erreur lors de la récupération des véhicules", e);
        }
    }

    public Optional<VehiculeDTO> getVehiculeById(Integer id) {
        try {
            return vehiculeRepository.findById(id).map(VehiculeMapper::toDTO);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération du véhicule", e);
            throw new DatabaseException("Erreur lors de la récupération du véhicule", e);
        }
    }

    public boolean createVehicule(VehiculeDTO vehiculeDTO) {
        ValidationService.ValidationResult result = validationService.validateVehicule(vehiculeDTO);
        if (!result.isValid()) {
            throw new ValidationException(result);
        }
        try {
            Vehicule vehicule = VehiculeMapper.toEntity(vehiculeDTO);
            vehiculeRepository.save(vehicule);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création du véhicule", e);
            throw new DatabaseException("Erreur lors de la création du véhicule", e);
        }
    }

    public boolean updateVehicule(VehiculeDTO vehiculeDTO) {
        ValidationService.ValidationResult result = validationService.validateVehicule(vehiculeDTO);
        if (!result.isValid()) {
            throw new ValidationException(result);
        }
        try {
            Vehicule vehicule = VehiculeMapper.toEntity(vehiculeDTO);
            vehiculeRepository.update(vehicule);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du véhicule", e);
            throw new DatabaseException("Erreur lors de la mise à jour du véhicule", e);
        }
    }

    public boolean updateKilometrage(Integer id, int nouveauKm) {
        try {
            Optional<Vehicule> optVehicule = vehiculeRepository.findById(id);
            if (optVehicule.isEmpty()) {
                throw new DatabaseException("Véhicule non trouvé pour la mise à jour du kilométrage");
            }
            Vehicule vehicule = optVehicule.get();
            vehicule.setKilometrage(nouveauKm);
            vehiculeRepository.update(vehicule);
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du kilométrage", e);
            throw new DatabaseException("Erreur lors de la mise à jour du kilométrage", e);
        }
    }

    public boolean deleteVehicule(Integer id) {
        try {
            return vehiculeRepository.delete(id);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression du véhicule", e);
            throw new DatabaseException("Erreur lors de la suppression du véhicule", e);
        }
    }

    public List<VehiculeDTO> getVehiculesDisponibles() {
        try {
            List<Vehicule> vehicules = vehiculeRepository.findByEtat(EtatVoiture.DISPONIBLE);
            return VehiculeMapper.toDTOList(vehicules);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des véhicules disponibles", e);
            throw new DatabaseException("Erreur lors de la récupération des véhicules disponibles", e);
        }
    }
}