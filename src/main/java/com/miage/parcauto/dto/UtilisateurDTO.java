package main.java.com.miage.parcauto.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO pour la représentation d'un utilisateur du système ParcAuto.
 * Sert à transférer les informations utilisateur entre les couches service et
 * présentation.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class UtilisateurDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer idUtilisateur;
    private String nom;
    private String prenom;
    private String email;
    private String login;
    private String motDePasseHash;
    private String role;
    private Set<String> permissions;
    private boolean actif;
    private LocalDateTime dateCreation;
    private LocalDateTime dateDerniereConnexion;

    /**
     * Constructeur par défaut.
     */
    public UtilisateurDTO() {
    }

    /**
     * Constructeur complet.
     */
    public UtilisateurDTO(Integer idUtilisateur, String nom, String prenom, String email, String login,
            String motDePasseHash, String role, Set<String> permissions, boolean actif, LocalDateTime dateCreation,
            LocalDateTime dateDerniereConnexion) {
        this.idUtilisateur = idUtilisateur;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.login = login;
        this.motDePasseHash = motDePasseHash;
        this.role = role;
        this.permissions = permissions;
        this.actif = actif;
        this.dateCreation = dateCreation;
        this.dateDerniereConnexion = dateDerniereConnexion;
    }

    /**
     * @return l'identifiant de l'utilisateur
     */
    public Integer getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Integer idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    /**
     * @return le nom de l'utilisateur
     */
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return le prénom de l'utilisateur
     */
    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * @return l'email de l'utilisateur
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return le login de l'utilisateur
     */
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return le hash du mot de passe
     */
    public String getMotDePasseHash() {
        return motDePasseHash;
    }

    public void setMotDePasseHash(String motDePasseHash) {
        this.motDePasseHash = motDePasseHash;
    }

    /**
     * @return le rôle de l'utilisateur
     */
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return les permissions de l'utilisateur
     */
    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    /**
     * @return si le compte est actif
     */
    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    /**
     * @return la date de création du compte
     */
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    /**
     * @return la date de dernière connexion
     */
    public LocalDateTime getDateDerniereConnexion() {
        return dateDerniereConnexion;
    }

    public void setDateDerniereConnexion(LocalDateTime dateDerniereConnexion) {
        this.dateDerniereConnexion = dateDerniereConnexion;
    }

    @Override
    public String toString() {
        return nom + " " + prenom + " (" + login + ")";
    }
}
