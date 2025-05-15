package main.java.com.miage.parcauto.model.rh;

import main.java.com.miage.parcauto.model.vehicule.Vehicule;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entité représentant un membre du personnel.
 */
public class Personnel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idPersonnel;
    private Service service;
    private Fonction fonction;
    private Vehicule vehicule;  // Véhicule attribué si applicable
    private String matricule;
    private String nomPersonnel;
    private String prenomPersonnel;
    private String email;
    private String telephone;
    private String adresse;
    private LocalDate dateNaissance;
    private Sexe sexe;
    private LocalDateTime dateAttribution;  // Date d'attribution du véhicule

    public Personnel() {
    }

    // Getters et Setters
    public Integer getIdPersonnel() {
        return idPersonnel;
    }

    public void setIdPersonnel(Integer idPersonnel) {
        this.idPersonnel = idPersonnel;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Fonction getFonction() {
        return fonction;
    }

    public void setFonction(Fonction fonction) {
        this.fonction = fonction;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
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

    public LocalDateTime getDateAttribution() {
        return dateAttribution;
    }

    public void setDateAttribution(LocalDateTime dateAttribution) {
        this.dateAttribution = dateAttribution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Personnel personnel = (Personnel) o;
        return Objects.equals(idPersonnel, personnel.idPersonnel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPersonnel);
    }

    @Override
    public String toString() {
        return prenomPersonnel + " " + nomPersonnel;
    }
}