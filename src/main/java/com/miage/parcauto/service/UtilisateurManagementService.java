package main.java.com.miage.parcauto.service;

import main.java.com.miage.parcauto.dto.UtilisateurDTO;
import main.java.com.miage.parcauto.exception.DuplicateEntityException;
import main.java.com.miage.parcauto.exception.OperationFailedException;
import main.java.com.miage.parcauto.exception.UtilisateurNotFoundException;
import main.java.com.miage.parcauto.exception.ValidationException;

import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des utilisateurs (CRUD, rôles, etc.).
 */
public interface UtilisateurManagementService {

    /**
     * Crée un nouvel utilisateur dans le système.
     *
     * @param utilisateurDTO Le DTO contenant les informations du nouvel
     *                       utilisateur. Le mot de passe doit être en clair.
     * @return L'UtilisateurDTO créé, incluant son ID généré.
     * @throws ValidationException      Si les données de l'utilisateur ne sont pas
     *                                  valides.
     * @throws DuplicateEntityException Si un utilisateur avec le même login existe
     *                                  déjà.
     * @throws OperationFailedException Si une erreur technique survient durant la
     *                                  création.
     */
    UtilisateurDTO createUtilisateur(UtilisateurDTO utilisateurDTO)
            throws ValidationException, DuplicateEntityException, OperationFailedException;

    /**
     * Récupère un utilisateur par son identifiant.
     *
     * @param idUtilisateur L'identifiant de l'utilisateur.
     * @return Un Optional contenant l'UtilisateurDTO si trouvé.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    Optional<UtilisateurDTO> getUtilisateurById(Integer idUtilisateur) throws OperationFailedException;

    /**
     * Récupère un utilisateur par son login.
     *
     * @param login Le login de l'utilisateur.
     * @return Un Optional contenant l'UtilisateurDTO si trouvé.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    Optional<UtilisateurDTO> getUtilisateurByLogin(String login) throws OperationFailedException;

    /**
     * Récupère tous les utilisateurs enregistrés.
     *
     * @return Une liste de UtilisateurDTO.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    List<UtilisateurDTO> getAllUtilisateurs() throws OperationFailedException;

    /**
     * Met à jour les informations d'un utilisateur existant.
     * Si le mot de passe dans le DTO est fourni, il sera mis à jour.
     *
     * @param utilisateurDTO Le DTO contenant les informations mises à jour.
     * @return L'UtilisateurDTO mis à jour.
     * @throws ValidationException          Si les données de l'utilisateur ne sont
     *                                      pas valides.
     * @throws UtilisateurNotFoundException Si l'utilisateur à mettre à jour n'est
     *                                      pas trouvé.
     * @throws DuplicateEntityException     Si le nouveau login entre en conflit
     *                                      avec un autre utilisateur.
     * @throws OperationFailedException     Si une erreur technique survient.
     */
    UtilisateurDTO updateUtilisateur(UtilisateurDTO utilisateurDTO) throws ValidationException,
            UtilisateurNotFoundException, DuplicateEntityException, OperationFailedException;

    /**
     * Supprime un utilisateur par son identifiant.
     *
     * @param idUtilisateur L'identifiant de l'utilisateur à supprimer.
     * @throws UtilisateurNotFoundException Si l'utilisateur à supprimer n'est pas
     *                                      trouvé.
     * @throws OperationFailedException     Si une erreur technique survient (par
     *                                      exemple, contraintes d'intégrité).
     */
    void deleteUtilisateur(Integer idUtilisateur) throws UtilisateurNotFoundException, OperationFailedException;

    /**
     * Change le mot de passe d'un utilisateur.
     *
     * @param idUtilisateur     L'identifiant de l'utilisateur.
     * @param nouveauMotDePasse Le nouveau mot de passe en clair.
     * @throws ValidationException          Si le nouveau mot de passe n'est pas
     *                                      valide.
     * @throws UtilisateurNotFoundException Si l'utilisateur n'est pas trouvé.
     * @throws OperationFailedException     Si une erreur technique survient.
     */
    void changePassword(Integer idUtilisateur, String nouveauMotDePasse)
            throws ValidationException, UtilisateurNotFoundException, OperationFailedException;

    /**
     * Active ou désactive un compte utilisateur.
     *
     * @param idUtilisateur L'identifiant de l'utilisateur.
     * @param actif         true pour activer, false pour désactiver.
     * @throws UtilisateurNotFoundException Si l'utilisateur n'est pas trouvé.
     * @throws OperationFailedException     Si une erreur technique survient.
     */
    void setUtilisateurActif(Integer idUtilisateur, boolean actif)
            throws UtilisateurNotFoundException, OperationFailedException;
}