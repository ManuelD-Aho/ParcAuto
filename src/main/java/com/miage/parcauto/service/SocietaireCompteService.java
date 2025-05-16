package main.java.com.miage.parcauto.service;

import main.java.com.miage.parcauto.dto.SocietaireCompteDTO;
import main.java.com.miage.parcauto.exception.DuplicateEntityException;
import main.java.com.miage.parcauto.exception.EntityNotFoundException;
import main.java.com.miage.parcauto.exception.OperationFailedException;
import main.java.com.miage.parcauto.exception.ValidationException;
import main.java.com.miage.parcauto.exception.SocietaireNotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des comptes sociétaires.
 */
public interface SocietaireCompteService {

    /**
     * Crée un nouveau compte sociétaire.
     *
     * @param compteDTO Le DTO contenant les informations du compte.
     * @return Le SocietaireCompteDTO créé avec son ID.
     * @throws ValidationException      Si les données sont invalides.
     * @throws DuplicateEntityException Si un numéro de compte identique existe
     *                                  déjà.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    SocietaireCompteDTO createSocietaireCompte(SocietaireCompteDTO compteDTO)
            throws ValidationException, DuplicateEntityException, OperationFailedException;

    /**
     * Récupère un compte sociétaire par son identifiant.
     *
     * @param idCompteSocietaire L'identifiant du compte.
     * @return Un Optional contenant le SocietaireCompteDTO si trouvé.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    Optional<SocietaireCompteDTO> getSocietaireCompteById(Integer idCompteSocietaire) throws OperationFailedException;

    /**
     * Récupère un compte sociétaire par son numéro de compte.
     *
     * @param numeroCompte Le numéro de compte.
     * @return Un Optional contenant le SocietaireCompteDTO si trouvé.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    Optional<SocietaireCompteDTO> getSocietaireCompteByNumero(String numeroCompte) throws OperationFailedException;

    /**
     * Récupère un compte sociétaire par l'ID du personnel associé.
     *
     * @param idPersonnel L'ID du personnel.
     * @return Un Optional contenant le SocietaireCompteDTO si trouvé.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    Optional<SocietaireCompteDTO> getSocietaireCompteByPersonnelId(Integer idPersonnel) throws OperationFailedException;

    /**
     * Récupère tous les comptes sociétaires.
     *
     * @return Une liste de SocietaireCompteDTO.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    List<SocietaireCompteDTO> getAllSocietaireComptes() throws OperationFailedException;

    /**
     * Met à jour les informations d'un compte sociétaire.
     *
     * @param compteDTO Le DTO avec les informations mises à jour.
     * @return Le SocietaireCompteDTO mis à jour.
     * @throws ValidationException         Si les données sont invalides.
     * @throws SocietaireNotFoundException Si le compte n'est pas trouvé.
     * @throws DuplicateEntityException    Si le numéro de compte modifié entre en
     *                                     conflit.
     * @throws OperationFailedException    Si une erreur technique survient.
     */
    SocietaireCompteDTO updateSocietaireCompte(SocietaireCompteDTO compteDTO)
            throws ValidationException, SocietaireNotFoundException, DuplicateEntityException, OperationFailedException;

    /**
     * Supprime un compte sociétaire par son identifiant.
     *
     * @param idCompteSocietaire L'identifiant du compte à supprimer.
     * @throws SocietaireNotFoundException Si le compte n'est pas trouvé.
     * @throws OperationFailedException    Si une erreur technique survient (ex:
     *                                     solde non nul, dépendances).
     */
    void deleteSocietaireCompte(Integer idCompteSocietaire)
            throws SocietaireNotFoundException, OperationFailedException;

    /**
     * Met à jour le solde d'un compte sociétaire.
     *
     * @param idCompteSocietaire L'identifiant du compte.
     * @param nouveauSolde       Le nouveau solde du compte.
     * @throws SocietaireNotFoundException Si le compte n'est pas trouvé.
     * @throws ValidationException         Si le nouveau solde est invalide.
     * @throws OperationFailedException    Si une erreur technique survient.
     */
    void updateSolde(Integer idCompteSocietaire, BigDecimal nouveauSolde)
            throws SocietaireNotFoundException, ValidationException, OperationFailedException;
}