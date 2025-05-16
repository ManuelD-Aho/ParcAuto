package main.java.com.miage.parcauto.service;

import main.java.com.miage.parcauto.dto.UtilisateurDTO;
import main.java.com.miage.parcauto.exception.AuthenticationException;
import main.java.com.miage.parcauto.exception.OperationFailedException;

import java.util.Optional;

/**
 * Service responsable de l'authentification des utilisateurs.
 */
public interface AuthenticationService {

    /**
     * Authentifie un utilisateur sur la base de son login et de son mot de passe.
     * Met à jour la date de dernière connexion en cas de succès.
     *
     * @param login      Le login de l'utilisateur.
     * @param motDePasse Le mot de passe en clair de l'utilisateur.
     * @return Un Optional contenant l'UtilisateurDTO si l'authentification réussit,
     *         sinon un Optional vide ou une exception est levée.
     * @throws AuthenticationException  Si les identifiants sont incorrects ou
     *                                  l'utilisateur inactif.
     * @throws OperationFailedException Si une erreur technique survient durant le
     *                                  processus.
     */
    Optional<UtilisateurDTO> authenticate(String login, String motDePasse)
            throws AuthenticationException, OperationFailedException;

}