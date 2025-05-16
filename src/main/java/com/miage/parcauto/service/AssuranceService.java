package main.java.com.miage.parcauto.service;

import main.java.com.miage.parcauto.dto.AssuranceDTO;
import main.java.com.miage.parcauto.dto.VehiculeDTO;
import main.java.com.miage.parcauto.exception.EntityNotFoundException;
import main.java.com.miage.parcauto.exception.OperationFailedException;
import main.java.com.miage.parcauto.exception.ValidationException;
import main.java.com.miage.parcauto.exception.VehiculeNotFoundException;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des contrats d'assurance et leur liaison avec les véhicules.
 */
public interface AssuranceService {

    /**
     * Crée un nouveau contrat d'assurance.
     *
     * @param assuranceDTO Le DTO contenant les informations de l'assurance.
     * @return L'AssuranceDTO créée avec son numéro de carte.
     * @throws ValidationException Si les données sont invalides.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    AssuranceDTO createAssurance(AssuranceDTO assuranceDTO) throws ValidationException, OperationFailedException;

    /**
     * Récupère un contrat d'assurance par son numéro de carte.
     *
     * @param numCarteAssurance Le numéro de la carte d'assurance.
     * @return Un Optional contenant l'AssuranceDTO si trouvée.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    Optional<AssuranceDTO> getAssuranceByNumCarte(Integer numCarteAssurance) throws OperationFailedException;

    /**
     * Récupère tous les contrats d'assurance.
     *
     * @return Une liste d'AssuranceDTO.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    List<AssuranceDTO> getAllAssurances() throws OperationFailedException;

    /**
     * Met à jour les informations d'un contrat d'assurance.
     *
     * @param assuranceDTO Le DTO avec les informations mises à jour.
     * @return L'AssuranceDTO mise à jour.
     * @throws ValidationException Si les données sont invalides.
     * @throws EntityNotFoundException Si l'assurance n'est pas trouvée.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    AssuranceDTO updateAssurance(AssuranceDTO assuranceDTO) throws ValidationException, EntityNotFoundException, OperationFailedException;

    /**
     * Supprime un contrat d'assurance.
     * Les liaisons avec les véhicules (table COUVRIR) devront être gérées.
     *
     * @param numCarteAssurance Le numéro de la carte d'assurance à supprimer.
     * @throws EntityNotFoundException Si l'assurance n'est pas trouvée.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    void deleteAssurance(Integer numCarteAssurance) throws EntityNotFoundException, OperationFailedException;

    /**
     * Lie un véhicule à un contrat d'assurance.
     *
     * @param idVehicule L'identifiant du véhicule.
     * @param numCarteAssurance Le numéro de la carte d'assurance.
     * @throws VehiculeNotFoundException Si le véhicule n'est pas trouvé.
     * @throws EntityNotFoundException Si l'assurance n'est pas trouvée.
     * @throws OperationFailedException Si le lien existe déjà ou si une erreur technique survient.
     */
    void lierVehiculeAssurance(Integer idVehicule, Integer numCarteAssurance) throws VehiculeNotFoundException, EntityNotFoundException, OperationFailedException;

    /**
     * Délier un véhicule d'un contrat d'assurance.
     *
     * @param idVehicule L'identifiant du véhicule.
     * @param numCarteAssurance Le numéro de la carte d'assurance.
     * @throws VehiculeNotFoundException Si le véhicule n'est pas trouvé.
     * @throws EntityNotFoundException Si l'assurance n'est pas trouvée.
     * @throws OperationFailedException Si le lien n'existe pas ou si une erreur technique survient.
     */
    void delierVehiculeAssurance(Integer idVehicule, Integer numCarteAssurance) throws VehiculeNotFoundException, EntityNotFoundException, OperationFailedException;

    /**
     * Récupère tous les véhicules couverts par une assurance spécifique.
     *
     * @param numCarteAssurance Le numéro de la carte d'assurance.
     * @return Une liste de VehiculeDTO.
     * @throws EntityNotFoundException Si l'assurance n'est pas trouvée.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    List<VehiculeDTO> getVehiculesByAssurance(Integer numCarteAssurance) throws EntityNotFoundException, OperationFailedException;

    /**
     * Récupère toutes les assurances couvrant un véhicule spécifique.
     *
     * @param idVehicule L'identifiant du véhicule.
     * @return Une liste d'AssuranceDTO.
     * @throws VehiculeNotFoundException Si le véhicule n'est pas trouvé.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    List<AssuranceDTO> getAssurancesByVehicule(Integer idVehicule) throws VehiculeNotFoundException, OperationFailedException;

    /**
     * Récupère les assurances qui expirent avant une date donnée.
     * @param dateLimite La date limite d'expiration.
     * @return Liste d'AssuranceDTO.
     * @throws OperationFailedException si une erreur technique survient.
     */
    List<AssuranceDTO> getAssurancesExpirantBientot(LocalDateTime dateLimite) throws OperationFailedException;
}