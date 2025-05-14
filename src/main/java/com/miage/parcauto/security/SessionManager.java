package main.java.com.miage.parcauto.security;

import java.time.LocalDateTime;
import java.util.Set;

import main.java.com.miage.parcauto.dto.UtilisateurDTO;
import main.java.com.miage.parcauto.exception.AuthenticationException;

/**
 * Gestionnaire de session utilisateur pour l'application ParcAuto.
 * S'occupe de la création, validation et destruction des sessions.
 * Implémente le pattern Singleton.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class SessionManager {

    /**
     * Durée d'inactivité avant expiration de la session (en minutes).
     */
    private static final int SESSION_TIMEOUT_MINUTES = 30;

    /**
     * Instance unique du SessionManager (pattern Singleton).
     */
    private static SessionManager instance;

    /**
     * Utilisateur actuellement connecté.
     */
    private UtilisateurDTO utilisateurConnecte;

    /**
     * Token de session.
     */
    private String sessionToken;

    /**
     * Timestamp de la dernière activité.
     */
    private LocalDateTime derniereActivite;

    /**
     * Permissions de l'utilisateur connecté.
     */
    private Set<Permission> permissions;

    /**
     * Constructeur privé (Singleton).
     */
    private SessionManager() {
        // Constructeur privé pour empêcher l'instanciation directe
    }

    /**
     * Retourne l'instance unique du SessionManager.
     * 
     * @return Instance du SessionManager
     */
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Ouvre une session pour un utilisateur.
     * 
     * @param utilisateur Utilisateur à connecter
     * @param token       Token de session
     * @param permissions Permissions de l'utilisateur
     */
    public void ouvrirSession(UtilisateurDTO utilisateur, String token, Set<Permission> permissions) {
        if (utilisateur == null) {
            throw new AuthenticationException("Impossible d'ouvrir une session: utilisateur est null");
        }
        if (token == null || token.trim().isEmpty()) {
            throw new AuthenticationException("Impossible d'ouvrir une session: token invalide");
        }

        this.utilisateurConnecte = utilisateur;
        this.sessionToken = token;
        this.derniereActivite = LocalDateTime.now();
        this.permissions = permissions;
    }

    /**
     * Ferme la session courante.
     */
    public void fermerSession() {
        this.utilisateurConnecte = null;
        this.sessionToken = null;
        this.derniereActivite = null;
        this.permissions = null;
    }

    /**
     * Vérifie si une session est active.
     * 
     * @return true si une session est active, false sinon
     */
    public boolean isSessionActive() {
        return utilisateurConnecte != null && sessionToken != null && !isSessionExpired();
    }

    /**
     * Vérifie si la session est expirée.
     * 
     * @return true si la session est expirée, false sinon
     */
    public boolean isSessionExpired() {
        if (derniereActivite == null) {
            return true;
        }

        LocalDateTime expirationTime = derniereActivite.plusMinutes(SESSION_TIMEOUT_MINUTES);
        return LocalDateTime.now().isAfter(expirationTime);
    }

    /**
     * Rafraîchit la session en mettant à jour le timestamp d'activité.
     * 
     * @throws AuthenticationException si aucune session n'est active
     */
    public void refreshSession() {
        if (!isSessionActive()) {
            throw new AuthenticationException("Aucune session active à rafraîchir");
        }
        this.derniereActivite = LocalDateTime.now();
    }

    /**
     * Vérifie si le token fourni correspond au token de session.
     * 
     * @param token Token à vérifier
     * @return true si le token est valide, false sinon
     */
    public boolean validateToken(String token) {
        return isSessionActive() && this.sessionToken.equals(token);
    }

    /**
     * Retourne l'utilisateur connecté.
     * 
     * @return Utilisateur connecté ou null si aucune session active
     */
    public UtilisateurDTO getUtilisateurConnecte() {
        if (!isSessionActive()) {
            return null;
        }
        return utilisateurConnecte;
    }

    /**
     * Retourne les permissions de l'utilisateur connecté.
     * 
     * @return Set des permissions ou null si aucune session active
     */
    public Set<Permission> getPermissions() {
        if (!isSessionActive()) {
            return null;
        }
        return permissions;
    }

    /**
     * Vérifie si l'utilisateur connecté possède une permission spécifique.
     * 
     * @param permission Permission à vérifier
     * @return true si l'utilisateur a la permission, false sinon
     */
    public boolean hasPermission(Permission permission) {
        return isSessionActive() && permissions != null && permissions.contains(permission);
    }

    /**
     * Vérifie si l'utilisateur connecté possède au moins une des permissions
     * spécifiées.
     * 
     * @param perms Permissions à vérifier
     * @return true si l'utilisateur a au moins une des permissions, false sinon
     */
    public boolean hasAnyPermission(Set<Permission> perms) {
        if (!isSessionActive() || permissions == null || perms == null) {
            return false;
        }

        for (Permission perm : perms) {
            if (permissions.contains(perm)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Vérifie si l'utilisateur connecté possède toutes les permissions spécifiées.
     * 
     * @param perms Permissions à vérifier
     * @return true si l'utilisateur a toutes les permissions, false sinon
     */
    public boolean hasAllPermissions(Set<Permission> perms) {
        if (!isSessionActive() || permissions == null || perms == null) {
            return false;
        }

        return permissions.containsAll(perms);
    }
}
