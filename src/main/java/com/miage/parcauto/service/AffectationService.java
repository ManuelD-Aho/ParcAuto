package main.java.com.miage.parcauto.service;

import main.java.com.miage.parcauto.dto.AffectationDTO;
import main.java.com.miage.parcauto.exception.*;

import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des affectations de véhicules.
 */
public interface AffectationService {

    /**
     * Crée une nouvelle affectation de véhicule.
     *
     * @param affectationDTO Le DTO de l'affectation.
     * @return L'AffectationDTO créée avec son ID.
     * @throws ValidationException Si les données sont invalides.
     * @throws VehiculeNotFoundException Si le véhicule n'est pas trouvé ou pas disponible.
     * @throws EntityNotFoundException Si le personnel ou le sociétaire n'est pas trouvé.
     * @throws OperationFailedException Si une erreur technique survient ou si le véhicule est déjà affecté sur la période.
     */
    AffectationDTO createAffectation(AffectationDTO affectationDTO) throws ValidationException, VehiculeNotFoundException, EntityNotFoundException, OperationFailedException;

    /**
     * Récupère une affectation par son identifiant.
     *
     * @param idAffectation L'identifiant de l'affectation.
     * @return Un Optional contenant l'AffectationDTO si trouvée.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    Optional<AffectationDTO> getAffectationById(Integer idAffectation) throws OperationFailedException;

    /**
     * Récupère toutes les affectations.
     *
     * @return Une liste d'AffectationDTO.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    List<AffectationDTO> getAllAffectations() throws OperationFailedException;

    /**
     * Récupère les affectations pour un véhicule spécifique.
     *
     * @param idVehicule L'identifiant du véhicule.
     * @return Une liste d'AffectationDTO.
     * @throws VehiculeNotFoundException Si le véhicule n'est pas trouvé.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    List<AffectationDTO> getAffectationsByVehiculeId(Integer idVehicule) throws VehiculeNotFoundException, OperationFailedException;

    /**
     * Récupère les affectations pour un membre du personnel spécifique.
     *
     * @param idPersonnel L'identifiant du personnel.
     * @return Une liste d'AffectationDTO.
     * @throws EntityNotFoundException Si le personnel n'est pas trouvé.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    List<AffectationDTO> getAffectationsByPersonnelId(Integer idPersonnel) throws EntityNotFoundException, OperationFailedException;

    /**
     * Récupère les affectations pour un compte sociétaire spécifique.
     *
     * @param idSocietaireCompte L'identifiant du compte sociétaire.
     * @return Une liste d'AffectationDTO.
     * @throws SocietaireNotFoundException Si le compte sociétaire n'est pas trouvé.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    List<AffectationDTO> getAffectationsBySocietaireId(Integer idSocietaireCompte) throws SocietaireNotFoundException, OperationFailedException;


    /**
     * Met à jour une affectation existante.
     *
     * @param affectationDTO Le DTO de l'affectation avec les informations mises à jour.
     * @return L'AffectationDTO mise à jour.
     * @throws ValidationException Si les données sont invalides.
     * @throws EntityNotFoundException Si l'affectation, le véhicule, le personnel ou le sociétaire n'est pas trouvé.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    AffectationDTO updateAffectation(AffectationDTO affectationDTO) throws ValidationException, EntityNotFoundException, OperationFailedException;

    /**
     * Supprime une affectation.
     *
     * @param idAffectation L'identifiant de l'affectation à supprimer.
     * @throws EntityNotFoundException Si l'affectation n'est pas trouvée.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    void deleteAffectation(Integer idAffectation) throws EntityNotFoundException, OperationFailedException;
}