package main.java.com.miage.parcauto.model.utilisateur;

import java.util.Set;
import main.java.com.miage.parcauto.security.Permission; // Assumant que Permission est une enum dans ce package

/**
 * Énumération représentant les rôles des utilisateurs dans l'application.
 * Correspond à la colonne `role` (ENUM('U1','U2','U3','U4')) de la table `UTILISATEUR`.
 * Chaque rôle est associé à un ensemble de permissions.
 */
public enum RoleUtilisateur {
    // Les permissions exactes par rôle sont à définir selon les besoins de l'application.
    // Exemple de mapping :
    // U1: Administrateur système (tous droits)
    // U2: Gestionnaire de parc (gestion véhicules, entretiens, missions)
    // U3: Agent financier (gestion comptes, mouvements, rapports financiers)
    // U4: Utilisateur standard (consultation, demandes spécifiques)

    ADMINISTRATEUR_SYSTEME("U1", "Administrateur Système", Set.of(
            Permission.GERER_UTILISATEURS, Permission.GERER_VEHICULES, Permission.GERER_ENTRETIENS,
            Permission.GERER_MISSIONS, Permission.GERER_FINANCES, Permission.VOIR_RAPPORTS_COMPLETS,
            Permission.CONFIGURER_SYSTEME
    )),
    GESTIONNAIRE_PARC("U2", "Gestionnaire de Parc", Set.of(
            Permission.GERER_VEHICULES, Permission.VOIR_VEHICULES,
            Permission.GERER_ENTRETIENS, Permission.VOIR_ENTRETIENS,
            Permission.GERER_MISSIONS, Permission.VOIR_MISSIONS,
            Permission.VOIR_RAPPORTS_OPERATIONNELS
    )),
    AGENT_FINANCIER("U3", "Agent Financier", Set.of(
            Permission.GERER_FINANCES, Permission.VOIR_FINANCES,
            Permission.GERER_COMPTES_SOCIETAIRES, Permission.VOIR_COMPTES_SOCIETAIRES,
            Permission.VOIR_RAPPORTS_FINANCIERS
    )),
    UTILISATEUR_STANDARD("U4", "Utilisateur Standard", Set.of(
            Permission.VOIR_VEHICULES_ASSIGNES, Permission.VOIR_MISSIONS_PERSONNELLES,
            Permission.DEMANDER_MISSION // Exemple de permission spécifique
    ));
    // Ajoutez d'autres rôles si nécessaire, ou ajustez les permissions.

    private final String codeDb;
    private final String libelle;
    private final Set<Permission> permissions;

    /**
     * Constructeur pour l'énumération RoleUtilisateur.
     * @param codeDb Le code stocké en base de données (ex: "U1").
     * @param libelle Le libellé lisible du rôle.
     * @param permissions L'ensemble des permissions associées à ce rôle.
     */
    RoleUtilisateur(String codeDb, String libelle, Set<Permission> permissions) {
        this.codeDb = codeDb;
        this.libelle = libelle;
        this.permissions = permissions;
    }

    /**
     * Retourne le libellé lisible du rôle.
     * @return Le libellé en français.
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Retourne le code du rôle tel qu'il est stocké en base de données.
     * @return Le code pour la base de données (ex: "U1").
     */
    public String getCodeDb() {
        return codeDb;
    }

    /**
     * Retourne l'ensemble des permissions associées à ce rôle.
     * @return Un ensemble de {@link Permission}.
     */
    public Set<Permission> getPermissions() {
        return permissions;
    }

    /**
     * Vérifie si ce rôle possède une permission spécifique.
     * @param permission La permission à vérifier.
     * @return true si le rôle possède la permission, false sinon.
     */
    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }

    /**
     * Retrouve un RoleUtilisateur à partir de son code en base de données ou de son libellé.
     * @param value La chaîne de caractères à mapper (code 'U1'-'U4' ou libellé).
     * @return Le RoleUtilisateur correspondant.
     * @throws IllegalArgumentException si la valeur n'est pas reconnue.
     */
    public static RoleUtilisateur fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("La valeur pour RoleUtilisateur ne peut pas être nulle.");
        }
        for (RoleUtilisateur role : values()) {
            if (role.codeDb.equalsIgnoreCase(value) ||
                    role.libelle.equalsIgnoreCase(value) ||
                    role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Rôle utilisateur inconnu : " + value + ". Attendus: U1, U2, U3, U4 ou leurs libellés.");
    }

    /**
     * Retourne la valeur à stocker en base de données.
     * @return Le code 'U1', 'U2', etc.
     */
    public String toDbValue() {
        return this.codeDb;
    }
}