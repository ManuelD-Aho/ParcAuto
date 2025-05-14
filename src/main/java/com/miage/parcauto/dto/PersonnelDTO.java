package main.java.com.miage.parcauto.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class PersonnelDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer idPersonnel;
    private String matricule;
    private String nom;
    private String prenom;
    private String nomComplet;
    private String email;
    private String telephone;
    private String adresse;
    private LocalDate dateNaissance;
    private String sexe; // 'M' or 'F' from DB enum

    private Integer idService; // From PERSONNEL.id_service
    private String libelleService; // Derived from SERVICE table
    private Integer idFonction; // From PERSONNEL.id_fonction
    private String libelleFonction; // Derived from FONCTION table

    private Integer idVehiculeAttribution; // From PERSONNEL.id_vehicule
    private LocalDateTime dateAttributionVehicule; // From PERSONNEL.date_attribution

    // User-related fields, potentially from UTILISATEUR table
    private Integer idUtilisateur;
    private String nomUtilisateur; // login from UTILISATEUR
    private String roleUtilisateur; // role from UTILISATEUR (U1, U2, U3, U4)
    private boolean actif; // General status, could be app specific or user status

    public PersonnelDTO() {
        this.actif = true;
    }

    public PersonnelDTO(Integer idPersonnel, String matricule, String nom, String prenom, String email, String telephone, String adresse, LocalDate dateNaissance, String sexe, Integer idService, String libelleService, Integer idFonction, String libelleFonction, Integer idVehiculeAttribution, LocalDateTime dateAttributionVehicule, Integer idUtilisateur, String nomUtilisateur, String roleUtilisateur, boolean actif) {
        this.idPersonnel = idPersonnel;
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
        updateNomComplet();
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
        this.dateNaissance = dateNaissance;
        this.sexe = sexe;
        this.idService = idService;
        this.libelleService = libelleService;
        this.idFonction = idFonction;
        this.libelleFonction = libelleFonction;
        this.idVehiculeAttribution = idVehiculeAttribution;
        this.dateAttributionVehicule = dateAttributionVehicule;
        this.idUtilisateur = idUtilisateur;
        this.nomUtilisateur = nomUtilisateur;
        this.roleUtilisateur = roleUtilisateur;
        this.actif = actif;
    }

    private void updateNomComplet() {
        String p = (this.prenom != null && !this.prenom.trim().isEmpty()) ? this.prenom.trim() : "";
        String n = (this.nom != null && !this.nom.trim().isEmpty()) ? this.nom.trim() : "";
        this.nomComplet = (p + " " + n).trim();
    }

    public Integer getIdPersonnel() {
        return idPersonnel;
    }

    public void setIdPersonnel(Integer idPersonnel) {
        this.idPersonnel = idPersonnel;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
        updateNomComplet();
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
        updateNomComplet();
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public Integer getIdService() {
        return idService;
    }

    public void setIdService(Integer idService) {
        this.idService = idService;
    }

    public String getLibelleService() {
        return libelleService;
    }

    public void setLibelleService(String libelleService) {
        this.libelleService = libelleService;
    }

    public Integer getIdFonction() {
        return idFonction;
    }

    public void setIdFonction(Integer idFonction) {
        this.idFonction = idFonction;
    }

    public String getLibelleFonction() {
        return libelleFonction;
    }

    public void setLibelleFonction(String libelleFonction) {
        this.libelleFonction = libelleFonction;
    }

    public Integer getIdVehiculeAttribution() {
        return idVehiculeAttribution;
    }

    public void setIdVehiculeAttribution(Integer idVehiculeAttribution) {
        this.idVehiculeAttribution = idVehiculeAttribution;
    }

    public LocalDateTime getDateAttributionVehicule() {
        return dateAttributionVehicule;
    }

    public void setDateAttributionVehicule(LocalDateTime dateAttributionVehicule) {
        this.dateAttributionVehicule = dateAttributionVehicule;
    }

    public Integer getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Integer idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getRoleUtilisateur() {
        return roleUtilisateur;
    }

    public void setRoleUtilisateur(String roleUtilisateur) {
        this.roleUtilisateur = roleUtilisateur;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonnelDTO that = (PersonnelDTO) o;
        return Objects.equals(idPersonnel, that.idPersonnel) || (matricule != null && Objects.equals(matricule, that.matricule));
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPersonnel, matricule);
    }

    @Override
    public String toString() {
        return "PersonnelDTO{" +
                "idPersonnel=" + idPersonnel +
                ", matricule='" + matricule + '\'' +
                ", nomComplet='" + nomComplet + '\'' +
                ", email='" + email + '\'' +
                ", nomUtilisateur='" + nomUtilisateur + '\'' +
                ", roleUtilisateur='" + roleUtilisateur + '\'' +
                ", actif=" + actif +
                '}';
    }
}