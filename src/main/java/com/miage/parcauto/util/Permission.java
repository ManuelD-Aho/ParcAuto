package main.java.com.miage.parcauto.util;

/**
 * Enumération des permissions possibles dans l'application.
 * Cette classe définit toutes les actions qui peuvent être autorisées ou refusées
 * en fonction du profil utilisateur.
 * <p>
 * Les permissions sont organisées par module fonctionnel pour faciliter la gestion
 * des droits d'accès et la maintenance du code.
 *
 * @author MIAGE Holding
 * @version 1.1
 */
public enum Permission {
    // =========================================================================
    // MODULE AUTHENTIFICATION
    // =========================================================================
    /** Permission de se connecter au système */
    CONNEXION_SYSTEME,
    /** Permission de changer son propre mot de passe */
    CHANGEMENT_MDP_PERSONNEL,
    /** Permission de réinitialiser les mots de passe des utilisateurs */
    REINIT_MDP_UTILISATEURS,
    
    // =========================================================================
    // MODULE GESTION DES VÉHICULES
    // =========================================================================
    /** Permission de consulter tous les véhicules du parc */
    CONSULTER_VEHICULES_TOUS,
    /** Permission de consulter uniquement les véhicules assignés à l'utilisateur */
    CONSULTER_VEHICULES_PERSONNEL,
    /** Permission de consulter les véhicules en lecture seule */
    CONSULTER_VEHICULES_LECTURE,
    /** Permission d'ajouter un nouveau véhicule au parc */
    AJOUTER_VEHICULE,
    /** Permission de modifier les données d'un véhicule */
    MODIFIER_VEHICULE,
    /** Permission de supprimer un véhicule du parc */
    SUPPRIMER_VEHICULE,
    /** Permission de changer l'état de tous les véhicules (service/panne/hors service) */
    CHANGER_ETAT_VEHICULE_TOUS,
    /** Permission de changer l'état des véhicules avec restrictions */
    CHANGER_ETAT_VEHICULE_LIMITE,
    /** Permission de déclarer une panne sur n'importe quel véhicule */
    DECLARER_PANNE_TOUS,
    /** Permission de déclarer une panne uniquement sur les véhicules personnels */
    DECLARER_PANNE_PERSONNEL,
    
    // =========================================================================
    // MODULE AFFECTATION DES VÉHICULES
    // =========================================================================
    /** Permission d'affecter un véhicule à un responsable (5 ans) */
    AFFECTER_VEHICULE_RESPONSABLE,
    /** Permission d'affecter un véhicule à une mission */
    AFFECTER_VEHICULE_MISSION,
    /** Permission de consulter toutes les affectations */
    SUIVI_AFFECTATIONS_TOUS,
    /** Permission de consulter les affectations en lecture seule */
    SUIVI_AFFECTATIONS_LECTURE,
    /** Permission de consulter uniquement ses affectations personnelles */
    SUIVI_AFFECTATIONS_PERSONNEL,
    
    // =========================================================================
    // MODULE MISSIONS
    // =========================================================================
    /** Permission de créer une nouvelle mission */
    CREER_MISSION,
    /** Permission de planifier des missions */
    PLANIFIER_MISSION,
    /** Permission de démarrer une mission */
    DEMARRER_MISSION,
    /** Permission de clôturer une mission */
    CLOTURER_MISSION,
    /** Permission d'annuler une mission sans justification */
    ANNULER_MISSION,
    /** Permission d'annuler une mission avec obligation de justification */
    ANNULER_MISSION_JUSTIFICATION,
    /** Permission de saisir les dépenses d'une mission */
    SAISIR_DEPENSES_MISSION,
    /** Permission de valider les dépenses d'une mission */
    VALIDER_DEPENSES_MISSION,
    
    // =========================================================================
    // MODULE ENTRETIEN ET MAINTENANCE
    // =========================================================================
    /** Permission de planifier des entretiens préventifs */
    PLANIFIER_ENTRETIEN_PREVENTIF,
    /** Permission de suivre tous les entretiens en cours */
    SUIVI_ENTRETIEN_TOUS,
    /** Permission de suivre uniquement ses entretiens personnels */
    SUIVI_ENTRETIEN_PERSONNEL,
    /** Permission de saisir des interventions */
    SAISIR_INTERVENTION,
    /** Permission de clôturer des interventions */
    CLOTURER_INTERVENTION,
    /** Permission de gérer les prestataires d'entretien */
    GERER_PRESTATAIRES,
    
    // =========================================================================
    // MODULE ASSURANCES ET VISITES TECHNIQUES
    // =========================================================================
    /** Permission de souscrire ou renouveler une assurance */
    SOUSCRIRE_ASSURANCE,
    /** Permission de planifier des visites techniques */
    PLANIFIER_VISITE_TECHNIQUE,
    /** Permission d'enregistrer les résultats d'une visite technique */
    ENREGISTRER_RESULTAT_VISITE,
    /** Permission de voir toutes les alertes d'échéances */
    ALERTES_ECHEANCES_TOUS,
    /** Permission de voir uniquement ses alertes d'échéances personnelles */
    ALERTES_ECHEANCES_PERSONNEL,
    
    // =========================================================================
    // MODULE GESTION FINANCIÈRE
    // =========================================================================
    /** Permission de créer un compte sociétaire */
    CREER_COMPTE_SOCIETAIRE,
    /** Permission de consulter tous les comptes */
    CONSULTER_COMPTES_TOUS,
    /** Permission de consulter uniquement son compte personnel */
    CONSULTER_COMPTES_PERSONNEL,
    /** Permission de valider des dépôts d'argent */
    DEPOT_VALIDATION,
    /** Permission d'effectuer une demande de dépôt d'argent */
    DEPOT_DEMANDE,
    /** Permission de valider des retraits d'argent */
    RETRAIT_VALIDATION,
    /** Permission d'effectuer une demande de retrait d'argent */
    RETRAIT_DEMANDE,
    /** Permission de gérer les mensualités */
    GERER_MENSUALITES,
    /** Permission de consulter les mensualités */
    CONSULTER_MENSUALITES,
    /** Permission de calculer l'amortissement des véhicules */
    CALCULER_AMORTISSEMENT,
    
    // =========================================================================
    // MODULE GESTION DOCUMENTAIRE
    // =========================================================================
    /** Permission d'uploader tous types de documents */
    UPLOAD_DOCUMENTS_TOUS,
    /** Permission d'uploader des documents techniques */
    UPLOAD_DOCUMENTS_TECHNIQUE,
    /** Permission d'uploader des documents personnels */
    UPLOAD_DOCUMENTS_PERSONNEL,
    /** Permission de consulter tous les documents */
    CONSULTER_DOCUMENTS_TOUS,
    /** Permission de consulter les documents techniques */
    CONSULTER_DOCUMENTS_TECHNIQUE,
    /** Permission de consulter ses documents personnels */
    CONSULTER_DOCUMENTS_PERSONNEL,
    /** Permission de valider les documents */
    VALIDER_DOCUMENTS,
    /** Permission de supprimer des documents */
    SUPPRIMER_DOCUMENTS,
    
    // =========================================================================
    // MODULE REPORTING ET STATISTIQUES
    // =========================================================================
    /** Permission d'accéder à un tableau de bord complet */
    TABLEAU_BORD_COMPLET,
    /** Permission d'accéder à un tableau de bord opérationnel */
    TABLEAU_BORD_OPERATIONNEL,
    /** Permission d'accéder à un tableau de bord personnel */
    TABLEAU_BORD_PERSONNEL,
    /** Permission d'accéder à un tableau de bord technique */
    TABLEAU_BORD_TECHNIQUE,
    /** Permission d'accéder à l'inventaire du parc */
    INVENTAIRE_PARC,
    /** Permission d'accéder à toutes les statistiques d'utilisation */
    STATISTIQUES_UTILISATION,
    /** Permission d'accéder aux statistiques d'utilisation limitées */
    STATISTIQUES_UTILISATION_LIMITE,
    /** Permission d'accéder à l'analyse des coûts */
    ANALYSE_COUTS,
    /** Permission d'accéder à tous les états financiers */
    ETATS_FINANCIERS,
    /** Permission d'accéder à ses états financiers personnels */
    ETATS_FINANCIERS_PERSONNEL,
    /** Permission de créer des rapports personnalisés */
    RAPPORTS_PERSONNALISES,
    /** Permission d'exporter toutes les données */
    EXPORT_DONNEES,
    /** Permission d'exporter des données avec restrictions */
    EXPORT_DONNEES_LIMITE,
    /** Permission d'exporter ses données personnelles */
    EXPORT_DONNEES_PERSONNEL,
    /** Permission d'exporter des données système */
    EXPORT_DONNEES_SYSTEM,
    
    // =========================================================================
    // MODULE ADMINISTRATION SYSTÈME
    // =========================================================================
    /** Permission de créer des utilisateurs */
    CREER_UTILISATEURS,
    /** Permission de gérer les droits d'accès */
    GERER_DROITS_ACCES,
    /** Permission d'effectuer des paramétrages fonctionnels */
    PARAMETRAGE_FONCTIONNEL,
    /** Permission d'effectuer des paramétrages techniques */
    PARAMETRAGE_TECHNIQUE,
    /** Permission de sauvegarder et restaurer le système */
    SAUVEGARDE_RESTAURATION,
    /** Permission de consulter les logs d'audit */
    LOGS_AUDIT_LECTURE,
    /** Permission de gérer complètement les logs d'audit */
    LOGS_AUDIT_COMPLET,
    /** Permission d'effectuer la maintenance de la base de données */
    MAINTENANCE_BDD,
    
    // =========================================================================
    // MODULE ALERTES ET NOTIFICATIONS
    // =========================================================================
    /** Permission de configurer les alertes */
    CONFIGURER_ALERTES,
    /** Permission de recevoir les alertes système */
    ALERTES_SYSTEME,
    /** Permission de recevoir les alertes opérationnelles */
    ALERTES_OPERATIONNEL,
    /** Permission de recevoir les alertes personnelles */
    ALERTES_PERSONNEL,
    /** Permission de recevoir les notifications d'échéances */
    NOTIFICATIONS_ECHEANCES,
    
    // =========================================================================
    // MODULE KILOMÉTRAGE ET CARBURANT
    // =========================================================================
    /** Permission de saisir le kilométrage de tous les véhicules */
    SAISIR_KILOMETRAGE,
    /** Permission de saisir le kilométrage de ses véhicules personnels */
    SAISIR_KILOMETRAGE_PERSONNEL,
    /** Permission de suivre la consommation de carburant de tous les véhicules */
    SUIVI_CARBURANT,
    /** Permission de suivre la consommation de carburant de ses véhicules personnels */
    SUIVI_CARBURANT_PERSONNEL,
    /** Permission d'analyser les performances des véhicules */
    ANALYSER_PERFORMANCE;
    
    /**
     * Retourne une représentation lisible de la permission
     * 
     * @return Le nom de la permission formaté pour l'affichage
     */
    @Override
    public String toString() {
        return name().replace('_', ' ').toLowerCase();
    }
    
    /**
     * Vérifie si cette permission appartient au module d'administration
     * 
     * @return true si la permission concerne l'administration système
     */
    public boolean isAdminPermission() {
        return name().startsWith("CREER_UTILISATEURS") || 
               name().startsWith("GERER_DROITS") || 
               name().startsWith("PARAMETRAGE_") ||
               name().startsWith("SAUVEGARDE_") ||
               name().startsWith("LOGS_AUDIT_") ||
               name().equals("MAINTENANCE_BDD");
    }
}