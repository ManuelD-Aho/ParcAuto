package main.java.com.miage.parcauto.security;

/**
 * Énumération des permissions granulaires au sein de l'application ParcAuto.
 * Ces permissions sont ensuite regroupées pour définir les capacités de chaque {@link main.java.com.miage.parcauto.model.utilisateur.RoleUtilisateur}.
 */
public enum Permission {
    // --- Gestion des Accès et Utilisateurs ---
    VOIR_LISTE_UTILISATEURS("Consulter la liste des utilisateurs"),
    VOIR_DETAILS_UTILISATEUR("Consulter les détails d'un utilisateur spécifique"),
    CREER_UTILISATEUR("Créer de nouveaux comptes utilisateurs"),
    MODIFIER_UTILISATEUR("Modifier les informations des comptes utilisateurs (hors rôle/mot de passe critiques)"),
    MODIFIER_ROLE_UTILISATEUR("Changer le rôle d'un utilisateur"),
    REINITIALISER_MOTDEPASSE_UTILISATEUR("Réinitialiser le mot de passe d'un utilisateur"),
    ACTIVER_DESACTIVER_UTILISATEUR("Activer ou désactiver un compte utilisateur"),
    GERER_MFA_UTILISATEUR("Gérer les configurations MFA des utilisateurs"), // Activer/désactiver/réinitialiser MFA
    SUPPRIMER_UTILISATEUR("Supprimer des comptes utilisateurs"), // À utiliser avec précaution

    // --- Gestion du Personnel (RH) ---
    VOIR_LISTE_PERSONNEL("Consulter la liste du personnel"),
    VOIR_DETAILS_PERSONNEL("Consulter le dossier complet d'un membre du personnel"),
    CREER_FICHE_PERSONNEL("Créer de nouvelles fiches personnel"),
    MODIFIER_FICHE_PERSONNEL("Modifier les informations des fiches personnel (service, fonction, contact)"),
    GERER_ATTRIBUTION_VEHICULE_PERSONNEL("Gérer l'attribution directe de véhicule au personnel"),
    SUPPRIMER_FICHE_PERSONNEL("Supprimer des fiches personnel"), // À utiliser avec précaution

    // --- Gestion des Référentiels RH (Fonctions, Services) ---
    GERER_FONCTIONS("Créer, modifier, supprimer les fonctions/postes de l'entreprise"),
    VOIR_FONCTIONS("Consulter la liste des fonctions/postes"),
    GERER_SERVICES("Créer, modifier, supprimer les services/départements de l'entreprise"),
    VOIR_SERVICES("Consulter la liste des services/départements"),

    // --- Gestion des Véhicules ---
    VOIR_LISTE_VEHICULES("Consulter la liste de tous les véhicules du parc"),
    VOIR_DETAILS_VEHICULE("Consulter les détails complets d'un véhicule (y compris historique)"),
    CREER_VEHICULE("Ajouter un nouveau véhicule au parc"),
    MODIFIER_VEHICULE("Modifier les informations d'un véhicule (marque, modèle, km, etc.)"),
    CHANGER_ETAT_VEHICULE("Modifier l'état d'un véhicule (Disponible, En mission, En entretien, etc.)"),
    SUPPRIMER_VEHICULE("Retirer un véhicule du parc (désaffectation)"), // À utiliser avec précaution

    // --- Gestion des Affectations de Véhicules ---
    VOIR_AFFECTATIONS("Consulter toutes les affectations de véhicules (personnel, sociétaires)"),
    CREER_AFFECTATION_VEHICULE("Créer une nouvelle affectation de véhicule (mission, crédit-bail)"),
    MODIFIER_AFFECTATION_VEHICULE("Modifier une affectation existante"),
    TERMINER_AFFECTATION_VEHICULE("Mettre fin à une affectation de véhicule"),

    // --- Gestion des Entretiens ---
    VOIR_LISTE_ENTRETIENS("Consulter la liste de tous les entretiens"),
    VOIR_DETAILS_ENTRETIEN("Consulter les détails d'un entretien spécifique"),
    PROGRAMMER_ENTRETIEN("Programmer un nouvel entretien (préventif ou correctif)"),
    MODIFIER_ENTRETIEN_PLANIFIE("Modifier les informations d'un entretien planifié"),
    ENREGISTRER_INTERVENTION_ENTRETIEN("Enregistrer les détails d'une intervention d'entretien (coûts, observations)"),
    CLOTURER_ENTRETIEN("Clôturer un ordre de travail d'entretien"),
    ANNULER_ENTRETIEN("Annuler un entretien planifié"),

    // --- Gestion des Missions ---
    VOIR_LISTE_MISSIONS("Consulter la liste de toutes les missions"),
    VOIR_DETAILS_MISSION("Consulter les détails d'une mission spécifique"),
    CREER_DEMANDE_MISSION("Créer une nouvelle demande de mission"), // Pour utilisateurs standards
    PLANIFIER_MISSION("Planifier une mission directement (pour gestionnaires)"),
    MODIFIER_MISSION("Modifier les informations d'une mission (dates, destination, véhicule)"),
    VALIDER_REJETER_DEMANDE_MISSION("Valider ou rejeter les demandes de mission soumises"),
    CLOTURER_MISSION("Clôturer une mission (enregistrement km réels, observations)"),
    ANNULER_MISSION("Annuler une mission planifiée ou en cours"),
    ENREGISTRER_DEPENSES_MISSION("Enregistrer les dépenses associées à une mission"),
    VOIR_DEPENSES_MISSION("Consulter les dépenses d'une mission"),
    // VOIR_MISSIONS_PERSONNELLES("Consulter ses propres missions (pour un conducteur/personnel)"), // Déjà présent, mais bon à garder en tête

    // --- Gestion Financière et Comptes Sociétaires ---
    VOIR_LISTE_COMPTES_SOCIETAIRES("Consulter la liste des comptes sociétaires"),
    VOIR_DETAILS_COMPTE_SOCIETAIRE("Consulter les détails et l'historique d'un compte sociétaire"),
    CREER_COMPTE_SOCIETAIRE("Créer un nouveau compte sociétaire"),
    MODIFIER_COMPTE_SOCIETAIRE("Modifier les informations d'un compte sociétaire"),
    GERER_SOLDES_COMPTES_SOCIETAIRES("Effectuer des ajustements de solde manuels"), // Dépôts, retraits manuels
    ENREGISTRER_MOUVEMENT_MANUEL("Enregistrer un mouvement financier manuel sur un compte"),
    GERER_MENSUALITES_COMPTES("Gérer les prélèvements de mensualités (ex: crédit véhicule)"),
    CLOTURER_COMPTE_SOCIETAIRE("Clôturer un compte sociétaire"),
    VOIR_SYNTHESE_FINANCIERE_GLOBALE("Consulter les tableaux de bord financiers globaux"),

    // --- Gestion des Assurances Véhicules ---
    VOIR_LISTE_ASSURANCES("Consulter la liste des contrats d'assurance"),
    VOIR_DETAILS_ASSURANCE("Consulter les détails d'un contrat d'assurance"),
    AJOUTER_CONTRAT_ASSURANCE("Ajouter un nouveau contrat d'assurance pour un véhicule"),
    MODIFIER_CONTRAT_ASSURANCE("Modifier un contrat d'assurance existant"),
    renouveler_contrat_assurance("Renouveler un contrat d'assurance arrivant à échéance"),
    SUPPRIMER_CONTRAT_ASSURANCE("Supprimer un contrat d'assurance"), // Si archivage préférable, renommer
    GERER_LIEN_VEHICULE_ASSURANCE("Associer/Dissocier des véhicules aux contrats d'assurance (table COUVRIR)"),


    // --- Gestion des Documents (Sociétaires, Véhicules) ---
    VOIR_DOCUMENTS_SOCIETAIRE("Consulter les documents d'un sociétaire"),
    UPLOAD_DOCUMENT_SOCIETAIRE("Uploader de nouveaux documents pour un sociétaire (carte grise, permis)"),
    SUPPRIMER_DOCUMENT_SOCIETAIRE("Supprimer des documents d'un sociétaire"),
    // VOIR_DOCUMENTS_VEHICULE("Consulter les documents spécifiques à un véhicule (hors assurance)"), // Si pertinent
    // UPLOAD_DOCUMENT_VEHICULE("Uploader des documents pour un véhicule"), // Si pertinent

    // --- Rapports et Exports ---
    GENERER_RAPPORT_TCO_VEHICULE("Générer le rapport TCO pour un ou plusieurs véhicules"),
    GENERER_RAPPORT_ACTIVITE_FLOTTE("Générer des rapports sur l'activité de la flotte (missions, km)"),
    GENERER_RAPPORT_ENTRETIENS("Générer des rapports sur les entretiens (coûts, fréquence)"),
    GENERER_BILAN_FINANCIER_PERIODE("Générer des bilans financiers sur une période donnée"),
    EXPORTER_DONNEES_PDF("Exporter des données et rapports au format PDF"),
    EXPORTER_DONNEES_EXCEL("Exporter des données et rapports au format Excel/CSV"),
    // VOIR_RAPPORTS_COMPLETS, VOIR_RAPPORTS_OPERATIONNELS, VOIR_RAPPORTS_FINANCIERS (déjà là, peuvent être des regroupements)

    // --- Configuration et Paramétrage Système ---
    ACCEDER_CONFIGURATION_SYSTEME("Accéder au panneau de configuration du système"),
    GERER_PARAMETRES_GENERAUX("Modifier les paramètres généraux de l'application (alertes, devises par défaut si besoin)"),
    GERER_LISTES_DEROULANTES_REFERENTIEL("Gérer les valeurs des listes de référentiel (ex: types de dépenses, motifs d'entretien si dynamiques)");

    private final String description;

    /**
     * Constructeur pour l'énumération Permission.
     * @param description Une description lisible de ce que la permission autorise.
     */
    Permission(String description) {
        this.description = description;
    }

    /**
     * Retourne la description de la permission.
     * @return La description en français.
     */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        // Pour une meilleure lisibilité dans les logs ou UI de débogage
        return this.name() + " - " + description;
    }
}