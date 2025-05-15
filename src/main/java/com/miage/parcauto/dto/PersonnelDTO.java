package main.java.com.miage.parcauto.dto;

import main.java.com.miage.parcauto.model.rh.Sexe;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PersonnelDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idPersonnel;
    private String serviceInfo;
    private String fonctionInfo;
    private String matricule;
    private String nomPersonnel;
    private String prenomPersonnel;
    private String email;
    private String telephone;
    private String adresse;
    private LocalDate dateNaissance;
    private Sexe sexe;
    private LocalDateTime dateAttributionVehicule;
    private String vehiculeAttributionInfo;

    public PersonnelDTO() {
    }

    public Integer getIdPersonnel() {
        return idPersonnel;
    }

    public void setIdPersonnel(Integer idPersonnel) {
        this.idPersonnel = idPersonnel;
    }

    public String getServiceInfo() {
        return serviceInfo;
    }

    public void setServiceInfo(String serviceInfo) {
        this.serviceInfo = serviceInfo;
    }

    public String getFonctionInfo() {
        return fonctionInfo;
    }

    public void setFonctionInfo(String fonctionInfo) {
        this.fonctionInfo = fonctionInfo;
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

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public LocalDateTime getDateAttributionVehicule() {
        return dateAttributionVehicule;
    }

    public void setDateAttributionVehicule(LocalDateTime dateAttributionVehicule) {
        this.dateAttributionVehicule = dateAttributionVehicule;
    }

    public String getVehiculeAttributionInfo() {
        return vehiculeAttributionInfo;
    }

    public void setVehiculeAttributionInfo(String vehiculeAttributionInfo) {
        this.vehiculeAttributionInfo = vehiculeAttributionInfo;
    }

    @Override
    public String toString() {
        return "PersonnelDTO{" +
                "idPersonnel=" + idPersonnel +
                ", nom='" + prenomPersonnel + " " + nomPersonnel + '\'' +
                ", matricule='" + matricule + '\'' +
                ", fonction='" + fonctionInfo + '\'' +
                '}';
    }
}