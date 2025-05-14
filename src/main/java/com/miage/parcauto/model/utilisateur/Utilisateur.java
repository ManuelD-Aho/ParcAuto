package main.java.com.miage.parcauto.model.utilisateur;

import main.java.com.miage.parcauto.model.rh.Personnel;

import java.io.Serializable;
import java.util.Objects;
// Pour mfa_secret, si vous utilisez une librairie spécifique pour le TOTP,
// des types de données spécifiques pourraient être nécessaires. Pour l'instant, un String.

/**
 * Entité représentant un utilisateur de l'application avec ses credentials et son rôle.
 * Correspond à un enregistrement de la table UTILISATEUR.
 */
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id; // Nommé 'id' dans la table
    private String login; // Nom d'utilisateur unique pour la connexion
    private String hashMotDePasse; // Le hash du mot de passe (nommé 'hash' dans la DB)
    private RoleUtilisateur role; // Le rôle de l'utilisateur
    private Personnel personnel; // Le membre du personnel associé à ce compte utilisateur (via id_personnel)
    private String mfaSecret; // Secret pour l'authentification multi-facteurs (TOTP)

    /**
     * Constructeur par défaut.
     */
    public Utilisateur() {
    }

    /**
     * Constructeur avec les paramètres essentiels.
     *
     * @param id             L'identifiant unique de l'utilisateur.
     * @param login          Le nom d'utilisateur pour la connexion.
     * @param hashMotDePasse Le mot de passe haché.
     * @param role           Le rôle de l'utilisateur.
     * @param personnel      Le membre du personnel associé à ce compte.
     */
    public Utilisateur(Integer id, String login, String hashMotDePasse, RoleUtilisateur role, Personnel personnel) {
        this.id = id;
        this.login = login;
        this.hashMotDePasse = hashMotDePasse;
        this.role = role;
        this.personnel = personnel;
    }

    /**
     * Constructeur complet incluant le secret MFA.
     *
     * @param id             L'identifiant unique de l'utilisateur.
     * @param login          Le nom d'utilisateur pour la connexion.
     * @param hashMotDePasse Le mot de passe haché.
     * @param role           Le rôle de l'utilisateur.
     * @param personnel      Le membre du personnel associé à ce compte.
     * @param mfaSecret      Le secret pour l'authentification à deux facteurs.
     */
    public Utilisateur(Integer id, String login, String hashMotDePasse, RoleUtilisateur role,
                       Personnel personnel, String mfaSecret) {
        this(id, login, hashMotDePasse, role, personnel);
        this.mfaSecret = mfaSecret;
    }

    // Getters et Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHashMotDePasse() {
        return hashMotDePasse;
    }

    public void setHashMotDePasse(String hashMotDePasse) {
        this.hashMotDePasse = hashMotDePasse;
    }

    public RoleUtilisateur getRole() {
        return role;
    }

    public void setRole(RoleUtilisateur role) {
        this.role = role;
    }

    public Personnel getPersonnel() {
        return personnel;
    }

    public void setPersonnel(Personnel personnel) {
        this.personnel = personnel;
    }

    public String getMfaSecret() {
        return mfaSecret;
    }

    public void setMfaSecret(String mfaSecret) {
        this.mfaSecret = mfaSecret;
    }

    /**
     * Vérifie si l'authentification multi-facteurs (MFA) est activée pour cet utilisateur.
     * @return true si un secret MFA est configuré, false sinon.
     */
    public boolean isMfaEnabled() {
        return this.mfaSecret != null && !this.mfaSecret.trim().isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateur that = (Utilisateur) o;
        return Objects.equals(id, that.id) ||
                (login != null && Objects.equals(login, that.login));
    }

    @Override
    public int hashCode() {
        // Baser le hash sur le login s'il est unique et non nul, sinon l'ID.
        if (login != null) {
            return Objects.hash(login);
        }
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", role=" + role +
                ", personnelId=" + (personnel != null ? personnel.getIdPersonnel() : "N/A") +
                '}';
    }
}