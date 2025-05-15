package main.java.com.miage.parcauto.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO pour l'entité Personnel.
 */
public class PersonnelDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idPersonnel;
    private Integer idService;
    private String libService;
    private Integer idFonction;
    private String libFonction;
    private Integer idVehicule;
    private String vehiculeInfo;
    private String matricule;
    private String nomPersonnel;
    private String prenomPersonnel;
    private String email;
    private String telephone;
    private String adresse;
    private LocalDate dateNaissance;
    private String sexe;
    private LocalDateTime dateAttribution;

    public PersonnelDTO() {
    }

    // Getters et Setters
    public Integer getIdPersonnel() {
        return idPersonnel;
    }

    public void setIdPersonnel(Integer idPersonnel) {
        this.idPersonnel = idPersonnel;
    }

    public Integer getIdService() {
        return idService;
    }

    public void setIdService(Integer idService) {
        this.idService = idService;
    }

    public String getLibService() {
        return libService;
    }

    public void setLibService(String libService) {
        this.libService = libService;
    }

    public Integer getIdFonction() {
        return idFonction;
    }

    public void setIdFonction(Integer idFonction) {
        this.idFonction = idFonction;
    }

    public String getLibFonction() {
        return libFonction;
    }

    public void setLibFonction(String libFonction) {
        this.libFonction = libFonction;
    }

    public Integer getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(Integer idVehicule) {
        this.idVehicule = idVehicule;
    }

    public String getVehiculeInfo() {
        return vehiculeInfo;
    }

    public void setVehiculeInfo(String vehiculeInfo) {
        this.vehiculeInfo = vehiculeInfo;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNomPersonnel() {
        return nomPersonnel;
    }

    public void setNomPersonnel(String nomPersonnel) {
        this.nomPersonnel = nomPersonnel;
    }

    public String getPrenomPersonnel() {
        return prenomPersonnel;
    }

    public void setPrenomPersonnel(String prenomPersonnel) {
        this.prenomPersonnel = prenomPersonnel;
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

    public LocalDateTime getDateAttribution() {
        return dateAttribution;
    }

    public void setDateAttribution(LocalDateTime dateAttribution) {
        this.dateAttribution = dateAttribution;
    }

    @Override
    public String toString() {
        return prenomPersonnel + " " + nomPersonnel + " [" + matricule + "]";
    }

    public String getNomComplet() {
        return prenomPersonnel + " " + nomPersonnel;
    }
}