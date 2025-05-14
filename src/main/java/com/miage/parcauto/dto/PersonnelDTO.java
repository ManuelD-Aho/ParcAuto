package main.java.com.miage.parcauto.dto;

import java.io.Serializable;

/**
 * DTO (Data Transfer Object) pour représenter les membres du personnel.
 * Cette classe facilite le transfert de données entre les couches Service et
 * Vue.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class PersonnelDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer idPersonnel;
    private String nom;
    private String prenom;
    private String nomComplet;
    private String email;
    private String telephone;
    private String nomUtilisateur;
    private String role;
    private boolean actif;

    /**
     * Constructeur par défaut.
     */
    public PersonnelDTO() {
    }

    /**
     * Constructeur avec tous les paramètres.
     *
     * @param idPersonnel    Identifiant du membre du personnel
     * @param nom            Nom du membre du personnel
     * @param prenom         Prénom du membre du personnel
     * @param email          Email du membre du personnel
     * @param telephone      Téléphone du membre du personnel
     * @param nomUtilisateur Nom d'utilisateur du membre du personnel
     * @param role           Rôle du membre du personnel
     * @param actif          Indique si le membre du personnel est actif
     */
    public PersonnelDTO(Integer idPersonnel, String nom, String prenom, String email, String telephone,
            String nomUtilisateur, String role, boolean actif) {
        this.idPersonnel = idPersonnel;
        this.nom = nom;
        this.prenom = prenom;
        this.nomComplet = prenom + " " + nom;
        this.email = email;
        this.telephone = telephone;
        this.nomUtilisateur = nomUtilisateur;
        this.role = role;
        this.actif = actif;
    }

    /**
     * Retourne l'identifiant du membre du personnel.
     *
     * @return L'identifiant du membre du personnel
     */
    public Integer getIdPersonnel() {
        return idPersonnel;
    }

    /**
     * Définit l'identifiant du membre du personnel.
     *
     * @param idPersonnel L'identifiant du membre du personnel
     */
    public void setIdPersonnel(Integer idPersonnel) {
        this.idPersonnel = idPersonnel;
    }

    /**
     * Retourne le nom du membre du personnel.
     *
     * @return Le nom du membre du personnel
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom du membre du personnel.
     *
     * @param nom Le nom du membre du personnel
     */
    public void setNom(String nom) {
        this.nom = nom;
        this.nomComplet = (prenom != null ? prenom + " " : "") + nom;
    }

    /**
     * Retourne le prénom du membre du personnel.
     *
     * @return Le prénom du membre du personnel
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Définit le prénom du membre du personnel.
     *
     * @param prenom Le prénom du membre du personnel
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
        this.nomComplet = prenom + " " + (nom != null ? nom : "");
    }

    /**
     * Retourne le nom complet du membre du personnel.
     *
     * @return Le nom complet du membre du personnel
     */
    public String getNomComplet() {
        return nomComplet;
    }

    /**
     * Retourne l'email du membre du personnel.
     *
     * @return L'email du membre du personnel
     */
    public String getEmail() {
        return email;
    }

    /**
     * Définit l'email du membre du personnel.
     *
     * @param email L'email du membre du personnel
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retourne le téléphone du membre du personnel.
     *
     * @return Le téléphone du membre du personnel
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Définit le téléphone du membre du personnel.
     *
     * @param telephone Le téléphone du membre du personnel
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * Retourne le nom d'utilisateur du membre du personnel.
     *
     * @return Le nom d'utilisateur du membre du personnel
     */
    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    /**
     * Définit le nom d'utilisateur du membre du personnel.
     *
     * @param nomUtilisateur Le nom d'utilisateur du membre du personnel
     */
    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    /**
     * Retourne le rôle du membre du personnel.
     *
     * @return Le rôle du membre du personnel
     */
    public String getRole() {
        return role;
    }

    /**
     * Définit le rôle du membre du personnel.
     *
     * @param role Le rôle du membre du personnel
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Indique si le membre du personnel est actif.
     *
     * @return true si le membre du personnel est actif, false sinon
     */
    public boolean isActif() {
        return actif;
    }

    /**
     * Définit si le membre du personnel est actif.
     *
     * @param actif true si le membre du personnel est actif, false sinon
     */
    public void setActif(boolean actif) {
        this.actif = actif;
    }

    @Override
    public String toString() {
        return "PersonnelDTO{" +
                "idPersonnel=" + idPersonnel +
                ", nomComplet='" + nomComplet + '\'' +
                ", email='" + email + '\'' +
                ", nomUtilisateur='" + nomUtilisateur + '\'' +
                ", role='" + role + '\'' +
                ", actif=" + actif +
                '}';
    }
}
