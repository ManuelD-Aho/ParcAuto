package main.java.com.miage.parcauto.model.assurance;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Assurance {

    private Integer numCarteAssurance;
    private LocalDateTime dateDebut; // SQL: date_debut_assurance
    private LocalDateTime dateFin; // SQL: date_fin_assurance
    private String agence;
    private BigDecimal cout; // SQL: cout_assurance
    private String compagnie;
    private String adresse;
    private String telephone;
    private BigDecimal prix;

    public Assurance() {
    }

    public Integer getNumCarteAssurance() {
        return numCarteAssurance;
    }

    public void setNumCarteAssurance(Integer numCarteAssurance) {
        this.numCarteAssurance = numCarteAssurance;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public String getAgence() {
        return agence;
    }

    public void setAgence(String agence) {
        this.agence = agence;
    }

    public BigDecimal getCout() {
        return cout;
    }

    public void setCout(BigDecimal cout) {
        this.cout = cout;
    }

    public String getCompagnie() {
        return compagnie;
    }

    public void setCompagnie(String compagnie) {
        this.compagnie = compagnie;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }
}