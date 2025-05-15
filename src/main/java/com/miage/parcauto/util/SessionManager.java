package main.java.com.miage.parcauto.util;

import java.util.logging.Logger;

/**
 * Gestionnaire de session pour l'application.
 * Cette classe singleton permet de stocker et d'accéder à l'utilisateur actuellement connecté.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class SessionManager {
    
    private static final Logger LOGGER = Logger.getLogger(SessionManager.class.getName());
    private static SessionManager instance;
    private Utilisateur currentUser;
    
    /**
     * Constructeur privé pour empêcher l'instanciation directe.
     */
    private SessionManager() {
        // Constructeur privé pour pattern singleton
    }
    
    /**
     * Obtient l'instance unique du gestionnaire de session.
     * 
     * @return L'instance du SessionManager
     */
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    /**
     * Définit l'utilisateur actuellement connecté.
     * 
     * @param user L'utilisateur connecté
     */
    public void setCurrentUser(Utilisateur user) {
        this.currentUser = user;
        LOGGER.info("Utilisateur connecté: " + (user != null ? user.getLogin() : "null"));
    }
    
    /**
     * Obtient l'utilisateur actuellement connecté.
     * 
     * @return L'utilisateur courant ou null si aucun utilisateur n'est connecté
     */
    public Utilisateur getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Vérifie si un utilisateur est connecté.
     * 
     * @return true si un utilisateur est connecté, false sinon
     */
    public boolean isAuthenticated() {
        return currentUser != null;
    }
    
    /**
     * Déconnecte l'utilisateur actuel.
     */
    public void logout() {
        if (currentUser != null) {
            LOGGER.info("Déconnexion de l'utilisateur: " + currentUser.getLogin());
        }
        currentUser = null;
    }
}