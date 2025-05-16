package main.java.com.miage.parcauto.service;

import main.java.com.miage.parcauto.dto.VehiculeDTO;
import main.java.com.miage.parcauto.exception.OperationFailedException;
import main.java.com.miage.parcauto.exception.ValidationException;
import main.java.com.miage.parcauto.exception.VehiculeNotFoundException;
import main.java.com.miage.parcauto.exception.DuplicateEntityException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des véhicules du parc.
 */
public interface VehiculeService {

    /**
     * Crée un nouveau véhicule.
     *
     * @param vehiculeDTO Le DTO du véhicule à créer.
     * @return Le VehiculeDTO créé avec son ID.
     * @throws ValidationException Si les données du véhicule sont invalides.
     * @throws DuplicateEntityException Si un véhicule avec la même immatriculation ou numéro de châssis existe déjà.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    VehiculeDTO createVehicule(VehiculeDTO vehiculeDTO) throws ValidationException, DuplicateEntityException, OperationFailedException;

    /**
     * Récupère un véhicule par son identifiant.
     *
     * @param idVehicule L'identifiant du véhicule.
     * @return Un Optional contenant le VehiculeDTO si trouvé.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    Optional<VehiculeDTO> getVehiculeById(Integer idVehicule) throws OperationFailedException;

    /**
     * Récupère tous les véhicules du parc.
     *
     * @return Une liste de VehiculeDTO.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    List<VehiculeDTO> getAllVehicules() throws OperationFailedException;

    /**
     * Met à jour les informations d'un véhicule existant.
     *
     * @param vehiculeDTO Le DTO du véhicule avec les informations mises à jour.
     * @return Le VehiculeDTO mis à jour.
     * @throws ValidationException Si les données du véhicule sont invalides.
     * @throws VehiculeNotFoundException Si le véhicule à mettre à jour n'est pas trouvé.
     * @throws DuplicateEntityException Si la nouvelle immatriculation ou numéro de châssis entre en conflit.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    VehiculeDTO updateVehicule(VehiculeDTO vehiculeDTO) throws ValidationException, VehiculeNotFoundException, DuplicateEntityException, OperationFailedException;

    /**
     * Supprime un véhicule par son identifiant.
     *
     * @param idVehicule L'identifiant du véhicule à supprimer.
     * @throws VehiculeNotFoundException Si le véhicule à supprimer n'est pas trouvé.
     * @throws OperationFailedException Si une erreur technique survient (ex: véhicule impliqué dans des missions actives).
     */
    void deleteVehicule(Integer idVehicule) throws VehiculeNotFoundException, OperationFailedException;

    /**
     * Met à jour le kilométrage d'un véhicule.
     *
     * @param idVehicule L'identifiant du véhicule.
     * @param nouveauKilometrage Le nouveau kilométrage.
     * @throws VehiculeNotFoundException Si le véhicule n'est pas trouvé.
     * @throws ValidationException Si le nouveau kilométrage est invalide (ex: inférieur à l'actuel).
     * @throws OperationFailedException Si une erreur technique survient.
     */
    void updateKilometrage(Integer idVehicule, int nouveauKilometrage) throws VehiculeNotFoundException, ValidationException, OperationFailedException;

    /**
     * Récupère la liste des véhicules disponibles pour une affectation ou une mission
     * sur une période donnée. Un véhicule est disponible s'il est dans un état approprié
     * (ex: "Disponible") et n'a pas de mission ou d'affectation conflictuelle.
     *
     * @param dateDebut La date de début de la période souhaitée.
     * @param dateFin La date de fin de la période souhaitée.
     * @return Une liste de VehiculeDTO disponibles.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    List<VehiculeDTO> getVehiculesDisponibles(LocalDateTime dateDebut, LocalDateTime dateFin) throws OperationFailedException;

    /**
     * Récupère les véhicules nécessitant une maintenance.
     * La logique exacte dépendra des critères (ex: km depuis dernier entretien, date prochain entretien).
     *
     * @return Une liste de VehiculeDTO.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    List<VehiculeDTO> getVehiculesRequerantMaintenance() throws OperationFailedException;
}