package main.java.com.miage.parcauto.service;

import main.java.com.miage.parcauto.dto.PersonnelDTO;
import main.java.com.miage.parcauto.exception.DuplicateEntityException;
import main.java.com.miage.parcauto.exception.EntityNotFoundException;
import main.java.com.miage.parcauto.exception.OperationFailedException;
import main.java.com.miage.parcauto.exception.ValidationException;

import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des membres du personnel.
 */
public interface PersonnelService {

    /**
     * Crée un nouveau membre du personnel.
     *
     * @param personnelDTO Le DTO contenant les informations du personnel.
     * @return Le PersonnelDTO créé avec son ID.
     * @throws ValidationException Si les données sont invalides.
     * @throws DuplicateEntityException Si un matricule ou email identique existe déjà.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    PersonnelDTO createPersonnel(PersonnelDTO personnelDTO) throws ValidationException, DuplicateEntityException, OperationFailedException;

    /**
     * Récupère un membre du personnel par son identifiant.
     *
     * @param idPersonnel L'identifiant du personnel.
     * @return Un Optional contenant le PersonnelDTO si trouvé.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    Optional<PersonnelDTO> getPersonnelById(Integer idPersonnel) throws OperationFailedException;

    /**
     * Récupère un membre du personnel par son matricule.
     *
     * @param matricule Le matricule du personnel.
     * @return Un Optional contenant le PersonnelDTO si trouvé.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    Optional<PersonnelDTO> getPersonnelByMatricule(String matricule) throws OperationFailedException;

    /**
     * Récupère tous les membres du personnel.
     *
     * @return Une liste de PersonnelDTO.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    List<PersonnelDTO> getAllPersonnel() throws OperationFailedException;

    /**
     * Met à jour les informations d'un membre du personnel.
     *
     * @param personnelDTO Le DTO avec les informations mises à jour.
     * @return Le PersonnelDTO mis à jour.
     * @throws ValidationException Si les données sont invalides.
     * @throws EntityNotFoundException Si le personnel n'est pas trouvé.
     * @throws DuplicateEntityException Si le matricule ou email modifié entre en conflit.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    PersonnelDTO updatePersonnel(PersonnelDTO personnelDTO) throws ValidationException, EntityNotFoundException, DuplicateEntityException, OperationFailedException;

    /**
     * Supprime un membre du personnel par son identifiant.
     *
     * @param idPersonnel L'identifiant du personnel à supprimer.
     * @throws EntityNotFoundException Si le personnel n'est pas trouvé.
     * @throws OperationFailedException Si une erreur technique survient (ex: dépendances existantes).
     */
    void deletePersonnel(Integer idPersonnel) throws EntityNotFoundException, OperationFailedException;

    /**
     * Récupère les membres du personnel par identifiant de service.
     * @param idService L'identifiant du service.
     * @return Liste de PersonnelDTO.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    List<PersonnelDTO> getPersonnelByServiceId(Integer idService) throws OperationFailedException;

    /**
     * Récupère les membres du personnel par identifiant de fonction.
     * @param idFonction L'identifiant de la fonction.
     * @return Liste de PersonnelDTO.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    List<PersonnelDTO> getPersonnelByFonctionId(Integer idFonction) throws OperationFailedException;
}