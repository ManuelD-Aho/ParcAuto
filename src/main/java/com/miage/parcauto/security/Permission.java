package main.java.com.miage.parcauto.security;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Enum définissant les permissions disponibles dans l'application.
 * Chaque permission correspond à une action possible sur une ressource.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public enum Permission {
    // Permissions véhicules
    VEHICULE_LISTER("Lister les véhicules"),
    VEHICULE_AFFICHER("Afficher les détails d'un véhicule"),
    VEHICULE_CREER("Créer un véhicule"),
    VEHICULE_MODIFIER("Modifier un véhicule"),
    VEHICULE_SUPPRIMER("Supprimer un véhicule"),

    // Permissions entretiens
    ENTRETIEN_LISTER("Lister les entretiens"),
    ENTRETIEN_AFFICHER("Afficher les détails d'un entretien"),
    ENTRETIEN_CREER("Créer un entretien"),
    ENTRETIEN_MODIFIER("Modifier un entretien"),
    ENTRETIEN_SUPPRIMER("Supprimer un entretien"),
    ENTRETIEN_TERMINER("Terminer un entretien"),

    // Permissions missions
    MISSION_LISTER("Lister les missions"),
    MISSION_AFFICHER("Afficher les détails d'une mission"),
    MISSION_CREER("Créer une mission"),
    MISSION_MODIFIER("Modifier une mission"),
    MISSION_SUPPRIMER("Supprimer une mission"),
    MISSION_TERMINER("Terminer une mission"),
    MISSION_DEPENSE("Gérer les dépenses de mission"),

    // Permissions finances
    FINANCE_LISTER("Lister les mouvements financiers"),
    FINANCE_AFFICHER("Afficher les détails d'un mouvement"),
    FINANCE_CREER("Créer un mouvement financier"),
    FINANCE_MODIFIER("Modifier un mouvement financier"),
    FINANCE_SUPPRIMER("Supprimer un mouvement financier"),
    FINANCE_BUDGET("Gérer les budgets"),

    // Permissions rapports
    RAPPORT_GENERER("Générer des rapports"),
    RAPPORT_EXPORTER("Exporter des rapports"),

    // Permissions utilisateurs
    UTILISATEUR_LISTER("Lister les utilisateurs"),
    UTILISATEUR_AFFICHER("Afficher les détails d'un utilisateur"),
    UTILISATEUR_CREER("Créer un utilisateur"),
    UTILISATEUR_MODIFIER("Modifier un utilisateur"),
    UTILISATEUR_SUPPRIMER("Supprimer un utilisateur"),

    // Permissions administration
    ADMIN_PARAMETRES("Gérer les paramètres système"),
    ADMIN_ROLES("Gérer les rôles et permissions");

    private final String description;

    /**
     * Constructeur.
     * 
     * @param description Description de la permission
     */
    Permission(String description) {
        this.description = description;
    }

    /**
     * Retourne la description de la permission.
     * 
     * @return Description de la permission
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retourne toutes les permissions liées aux véhicules.
     * 
     * @return Ensemble des permissions véhicules
     */
    public static Set<Permission> getVehiculePermissions() {
        return EnumSet.of(
                VEHICULE_LISTER, VEHICULE_AFFICHER, VEHICULE_CREER,
                VEHICULE_MODIFIER, VEHICULE_SUPPRIMER);
    }

    /**
     * Retourne toutes les permissions liées aux entretiens.
     * 
     * @return Ensemble des permissions entretiens
     */
    public static Set<Permission> getEntretienPermissions() {
        return EnumSet.of(
                ENTRETIEN_LISTER, ENTRETIEN_AFFICHER, ENTRETIEN_CREER,
                ENTRETIEN_MODIFIER, ENTRETIEN_SUPPRIMER, ENTRETIEN_TERMINER);
    }

    /**
     * Retourne toutes les permissions liées aux missions.
     * 
     * @return Ensemble des permissions missions
     */
    public static Set<Permission> getMissionPermissions() {
        return EnumSet.of(
                MISSION_LISTER, MISSION_AFFICHER, MISSION_CREER, MISSION_MODIFIER,
                MISSION_SUPPRIMER, MISSION_TERMINER, MISSION_DEPENSE);
    }

    /**
     * Retourne toutes les permissions liées aux finances.
     * 
     * @return Ensemble des permissions finances
     */
    public static Set<Permission> getFinancePermissions() {
        return EnumSet.of(
                FINANCE_LISTER, FINANCE_AFFICHER, FINANCE_CREER,
                FINANCE_MODIFIER, FINANCE_SUPPRIMER, FINANCE_BUDGET);
    }

    /**
     * Retourne toutes les permissions liées aux rapports.
     * 
     * @return Ensemble des permissions rapports
     */
    public static Set<Permission> getRapportPermissions() {
        return EnumSet.of(RAPPORT_GENERER, RAPPORT_EXPORTER);
    }

    /**
     * Retourne toutes les permissions liées aux utilisateurs.
     * 
     * @return Ensemble des permissions utilisateurs
     */
    public static Set<Permission> getUtilisateurPermissions() {
        return EnumSet.of(
                UTILISATEUR_LISTER, UTILISATEUR_AFFICHER, UTILISATEUR_CREER,
                UTILISATEUR_MODIFIER, UTILISATEUR_SUPPRIMER);
    }

    /**
     * Retourne toutes les permissions d'administration.
     * 
     * @return Ensemble des permissions d'administration
     */
    public static Set<Permission> getAdminPermissions() {
        return EnumSet.of(ADMIN_PARAMETRES, ADMIN_ROLES);
    }

    /**
     * Retourne toutes les permissions (super admin).
     * 
     * @return Toutes les permissions disponibles
     */
    public static Set<Permission> getAllPermissions() {
        return EnumSet.allOf(Permission.class);
    }

    /**
     * Retourne les permissions de lecture seule.
     * 
     * @return Ensemble des permissions de lecture seule
     */
    public static Set<Permission> getReadOnlyPermissions() {
        Set<Permission> readOnly = new HashSet<>();
        readOnly.add(VEHICULE_LISTER);
        readOnly.add(VEHICULE_AFFICHER);
        readOnly.add(ENTRETIEN_LISTER);
        readOnly.add(ENTRETIEN_AFFICHER);
        readOnly.add(MISSION_LISTER);
        readOnly.add(MISSION_AFFICHER);
        readOnly.add(FINANCE_LISTER);
        readOnly.add(FINANCE_AFFICHER);
        return readOnly;
    }
}
