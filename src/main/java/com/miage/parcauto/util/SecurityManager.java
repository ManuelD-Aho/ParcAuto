package main.java.com.miage.parcauto.util;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import main.java.com.miage.parcauto.dao.UtilisateurDao;

/**
 * Gestionnaire de sécurité pour l'application.
 * Cette classe singleton définit et vérifie les permissions associées à chaque rôle utilisateur.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class SecurityManager {
    
    private static final Logger LOGGER = Logger.getLogger(SecurityManager.class.getName());
    private static SecurityManager instance;
    
    private final Map<UtilisateurDao.Role, Set<Permission>> rolePermissions;
    
    /**
     * Constructeur privé pour empêcher l'instanciation directe.
     * Initialise les permissions pour chaque rôle selon la matrice des responsabilités.
     */
    private SecurityManager() {
        rolePermissions = new EnumMap<>(UtilisateurDao.Role.class);
        initializePermissions();
    }
    
    /**
     * Obtient l'instance unique du gestionnaire de sécurité.
     * 
     * @return L'instance du SecurityManager
     */
    public static synchronized SecurityManager getInstance() {
        if (instance == null) {
            instance = new SecurityManager();
        }
        return instance;
    }
    
    /**
     * Vérifie si l'utilisateur courant a la permission spécifiée.
     * 
     * @param permission La permission à vérifier
     * @return true si l'utilisateur a la permission, false sinon ou si aucun utilisateur n'est connecté
     */
    public boolean hasPermission(Permission permission) {
        SessionManager sessionManager = SessionManager.getInstance();
        if (!sessionManager.isAuthenticated()) {
            return false;
        }
        
        UtilisateurDao.Role userRole = sessionManager.getCurrentUser().getRole();
        Set<Permission> permissions = rolePermissions.getOrDefault(userRole, Collections.emptySet());
        
        return permissions.contains(permission);
    }
    
    /**
     * Vérifie si l'utilisateur a au moins une des permissions spécifiées.
     * 
     * @param permissions Liste de permissions à vérifier
     * @return true si l'utilisateur a au moins une des permissions, false sinon
     */
    public boolean hasAnyPermission(Permission... permissions) {
        for (Permission permission : permissions) {
            if (hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Vérifie si l'utilisateur a toutes les permissions spécifiées.
     * 
     * @param permissions Liste de permissions à vérifier
     * @return true si l'utilisateur a toutes les permissions, false sinon
     */
    public boolean hasAllPermissions(Permission... permissions) {
        for (Permission permission : permissions) {
            if (!hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Initialise les permissions pour chaque rôle selon la matrice des responsabilités.
     */
    private void initializePermissions() {
        // ===== Initialisation des permissions pour chaque rôle =====
        
        // ----- U1: Responsable Logistique -----
        Set<Permission> u1Permissions = new HashSet<>();
        
        // Authentification
        u1Permissions.add(Permission.CONNEXION_SYSTEME);
        u1Permissions.add(Permission.CHANGEMENT_MDP_PERSONNEL);
        
        // Gestion des véhicules
        u1Permissions.add(Permission.CONSULTER_VEHICULES_TOUS);
        u1Permissions.add(Permission.AJOUTER_VEHICULE);
        u1Permissions.add(Permission.MODIFIER_VEHICULE);
        u1Permissions.add(Permission.SUPPRIMER_VEHICULE);
        u1Permissions.add(Permission.CHANGER_ETAT_VEHICULE_TOUS);
        u1Permissions.add(Permission.DECLARER_PANNE_TOUS);
        
        // Affectation des véhicules
        u1Permissions.add(Permission.AFFECTER_VEHICULE_RESPONSABLE);
        u1Permissions.add(Permission.AFFECTER_VEHICULE_MISSION);
        u1Permissions.add(Permission.SUIVI_AFFECTATIONS_TOUS);
        
        // Missions
        u1Permissions.add(Permission.CREER_MISSION);
        u1Permissions.add(Permission.PLANIFIER_MISSION);
        u1Permissions.add(Permission.DEMARRER_MISSION);
        u1Permissions.add(Permission.CLOTURER_MISSION);
        u1Permissions.add(Permission.ANNULER_MISSION);
        u1Permissions.add(Permission.SAISIR_DEPENSES_MISSION);
        u1Permissions.add(Permission.VALIDER_DEPENSES_MISSION);
        
        // Entretien et maintenance
        u1Permissions.add(Permission.PLANIFIER_ENTRETIEN_PREVENTIF);
        u1Permissions.add(Permission.SUIVI_ENTRETIEN_TOUS);
        u1Permissions.add(Permission.SAISIR_INTERVENTION);
        u1Permissions.add(Permission.CLOTURER_INTERVENTION);
        u1Permissions.add(Permission.GERER_PRESTATAIRES);
        
        // Assurances et visites techniques
        u1Permissions.add(Permission.SOUSCRIRE_ASSURANCE);
        u1Permissions.add(Permission.PLANIFIER_VISITE_TECHNIQUE);
        u1Permissions.add(Permission.ENREGISTRER_RESULTAT_VISITE);
        u1Permissions.add(Permission.ALERTES_ECHEANCES_TOUS);
        
        // Gestion financière
        u1Permissions.add(Permission.CREER_COMPTE_SOCIETAIRE);
        u1Permissions.add(Permission.CONSULTER_COMPTES_TOUS);
        u1Permissions.add(Permission.DEPOT_VALIDATION);
        u1Permissions.add(Permission.RETRAIT_VALIDATION);
        u1Permissions.add(Permission.GERER_MENSUALITES);
        u1Permissions.add(Permission.CALCULER_AMORTISSEMENT);
        
        // Gestion documentaire
        u1Permissions.add(Permission.UPLOAD_DOCUMENTS_TOUS);
        u1Permissions.add(Permission.CONSULTER_DOCUMENTS_TOUS);
        u1Permissions.add(Permission.VALIDER_DOCUMENTS);
        
        // Reporting et statistiques
        u1Permissions.add(Permission.TABLEAU_BORD_COMPLET);
        u1Permissions.add(Permission.INVENTAIRE_PARC);
        u1Permissions.add(Permission.STATISTIQUES_UTILISATION);
        u1Permissions.add(Permission.ANALYSE_COUTS);
        u1Permissions.add(Permission.ETATS_FINANCIERS);
        u1Permissions.add(Permission.RAPPORTS_PERSONNALISES);
        u1Permissions.add(Permission.EXPORT_DONNEES);
        
        // Administration système
        u1Permissions.add(Permission.CREER_UTILISATEURS);
        u1Permissions.add(Permission.PARAMETRAGE_FONCTIONNEL);
        u1Permissions.add(Permission.LOGS_AUDIT_LECTURE);
        
        // Alertes et notifications
        u1Permissions.add(Permission.CONFIGURER_ALERTES);
        u1Permissions.add(Permission.ALERTES_SYSTEME);
        u1Permissions.add(Permission.NOTIFICATIONS_ECHEANCES);
        
        // Kilométrage et carburant
        u1Permissions.add(Permission.SAISIR_KILOMETRAGE);
        u1Permissions.add(Permission.SUIVI_CARBURANT);
        u1Permissions.add(Permission.ANALYSER_PERFORMANCE);
        
        rolePermissions.put(UtilisateurDao.Role.U1, Collections.unmodifiableSet(u1Permissions));
        
        // ----- U2: Agent Logistique -----
        Set<Permission> u2Permissions = new HashSet<>();
        
        // Authentification
        u2Permissions.add(Permission.CONNEXION_SYSTEME);
        u2Permissions.add(Permission.CHANGEMENT_MDP_PERSONNEL);
        
        // Gestion des véhicules
        u2Permissions.add(Permission.CONSULTER_VEHICULES_TOUS);
        u2Permissions.add(Permission.CHANGER_ETAT_VEHICULE_LIMITE);
        u2Permissions.add(Permission.DECLARER_PANNE_TOUS);
        
        // Affectation des véhicules
        u2Permissions.add(Permission.AFFECTER_VEHICULE_MISSION);
        u2Permissions.add(Permission.SUIVI_AFFECTATIONS_LECTURE);
        
        // Missions
        u2Permissions.add(Permission.CREER_MISSION);
        u2Permissions.add(Permission.PLANIFIER_MISSION);
        u2Permissions.add(Permission.DEMARRER_MISSION);
        u2Permissions.add(Permission.CLOTURER_MISSION);
        u2Permissions.add(Permission.ANNULER_MISSION_JUSTIFICATION);
        u2Permissions.add(Permission.SAISIR_DEPENSES_MISSION);
        
        // Entretien et maintenance
        u2Permissions.add(Permission.PLANIFIER_ENTRETIEN_PREVENTIF);
        u2Permissions.add(Permission.SUIVI_ENTRETIEN_TOUS);
        u2Permissions.add(Permission.SAISIR_INTERVENTION);
        u2Permissions.add(Permission.CLOTURER_INTERVENTION);
        
        // Assurances et visites techniques
        u2Permissions.add(Permission.PLANIFIER_VISITE_TECHNIQUE);
        u2Permissions.add(Permission.ENREGISTRER_RESULTAT_VISITE);
        u2Permissions.add(Permission.ALERTES_ECHEANCES_TOUS);
        
        // Gestion documentaire
        u2Permissions.add(Permission.UPLOAD_DOCUMENTS_TECHNIQUE);
        u2Permissions.add(Permission.CONSULTER_DOCUMENTS_TECHNIQUE);
        
        // Reporting et statistiques
        u2Permissions.add(Permission.TABLEAU_BORD_OPERATIONNEL);
        u2Permissions.add(Permission.INVENTAIRE_PARC);
        u2Permissions.add(Permission.STATISTIQUES_UTILISATION_LIMITE);
        u2Permissions.add(Permission.EXPORT_DONNEES_LIMITE);
        
        // Alertes et notifications
        u2Permissions.add(Permission.ALERTES_OPERATIONNEL);
        u2Permissions.add(Permission.NOTIFICATIONS_ECHEANCES);
        
        // Kilométrage et carburant
        u2Permissions.add(Permission.SAISIR_KILOMETRAGE);
        u2Permissions.add(Permission.SUIVI_CARBURANT);
        
        rolePermissions.put(UtilisateurDao.Role.U2, Collections.unmodifiableSet(u2Permissions));
        
        // ----- U3: Sociétaire -----
        Set<Permission> u3Permissions = new HashSet<>();
        
        // Authentification
        u3Permissions.add(Permission.CONNEXION_SYSTEME);
        u3Permissions.add(Permission.CHANGEMENT_MDP_PERSONNEL);
        
        // Gestion des véhicules
        u3Permissions.add(Permission.CONSULTER_VEHICULES_PERSONNEL);
        u3Permissions.add(Permission.DECLARER_PANNE_PERSONNEL);
        
        // Affectation des véhicules
        u3Permissions.add(Permission.SUIVI_AFFECTATIONS_PERSONNEL);
        
        // Entretien et maintenance
        u3Permissions.add(Permission.SUIVI_ENTRETIEN_PERSONNEL);
        
        // Assurances et visites techniques
        u3Permissions.add(Permission.ALERTES_ECHEANCES_PERSONNEL);
        
        // Gestion financière
        u3Permissions.add(Permission.CONSULTER_COMPTES_PERSONNEL);
        u3Permissions.add(Permission.DEPOT_DEMANDE);
        u3Permissions.add(Permission.RETRAIT_DEMANDE);
        u3Permissions.add(Permission.CONSULTER_MENSUALITES);
        
        // Gestion documentaire
        u3Permissions.add(Permission.UPLOAD_DOCUMENTS_PERSONNEL);
        u3Permissions.add(Permission.CONSULTER_DOCUMENTS_PERSONNEL);
        
        // Reporting et statistiques
        u3Permissions.add(Permission.TABLEAU_BORD_PERSONNEL);
        u3Permissions.add(Permission.ETATS_FINANCIERS_PERSONNEL);
        u3Permissions.add(Permission.EXPORT_DONNEES_PERSONNEL);
        
        // Alertes et notifications
        u3Permissions.add(Permission.ALERTES_PERSONNEL);
        
        // Kilométrage et carburant
        u3Permissions.add(Permission.SAISIR_KILOMETRAGE_PERSONNEL);
        u3Permissions.add(Permission.SUIVI_CARBURANT_PERSONNEL);
        
        rolePermissions.put(UtilisateurDao.Role.U3, Collections.unmodifiableSet(u3Permissions));
        
        // ----- U4: Administrateur Système -----
        Set<Permission> u4Permissions = new HashSet<>();
        
        // Authentification
        u4Permissions.add(Permission.CONNEXION_SYSTEME);
        u4Permissions.add(Permission.CHANGEMENT_MDP_PERSONNEL);
        u4Permissions.add(Permission.REINIT_MDP_UTILISATEURS);
        
        // Gestion des véhicules
        u4Permissions.add(Permission.CONSULTER_VEHICULES_LECTURE);
        
        // Gestion documentaire
        u4Permissions.add(Permission.SUPPRIMER_DOCUMENTS);
        
        // Reporting et statistiques
        u4Permissions.add(Permission.TABLEAU_BORD_TECHNIQUE);
        u4Permissions.add(Permission.STATISTIQUES_UTILISATION);
        u4Permissions.add(Permission.EXPORT_DONNEES_SYSTEM);
        
        // Administration système
        u4Permissions.add(Permission.CREER_UTILISATEURS);
        u4Permissions.add(Permission.GERER_DROITS_ACCES);
        u4Permissions.add(Permission.PARAMETRAGE_TECHNIQUE);
        u4Permissions.add(Permission.SAUVEGARDE_RESTAURATION);
        u4Permissions.add(Permission.LOGS_AUDIT_COMPLET);
        u4Permissions.add(Permission.MAINTENANCE_BDD);
        
        // Alertes et notifications
        u4Permissions.add(Permission.CONFIGURER_ALERTES);
        u4Permissions.add(Permission.ALERTES_SYSTEME);
        
        // Gestion financière
        u4Permissions.add(Permission.CREER_COMPTE_SOCIETAIRE);
        
        rolePermissions.put(UtilisateurDao.Role.U4, Collections.unmodifiableSet(u4Permissions));
    }
    
    /**
     * Obtient toutes les permissions pour un rôle spécifique.
     * 
     * @param role Le rôle pour lequel obtenir les permissions
     * @return Un ensemble immuable de permissions pour le rôle spécifié
     */
    public Set<Permission> getPermissionsForRole(UtilisateurDao.Role role) {
        return rolePermissions.getOrDefault(role, Collections.emptySet());
    }
}