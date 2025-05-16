package main.java.com.miage.parcauto.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VehiculeDTO {

    private Integer idVehicule;
    private Integer idEtatVoiture;
    private String libelleEtatVoiture;
    private String energie;
    private String numeroChassis;
    private String immatriculation;
    private String marque;
    private String modele;
    private Integer kilometrage;
    private LocalDateTime dateMiseEnService;
    private BigDecimal coutAchat;
    private LocalDateTime dateAmortissement;
    private Boolean actif;
    private Integer nbPlaces;
    private LocalDateTime dateAcquisition;
    private Integer puissance;
    private String couleur;
    private BigDecimal prixVehicule;
    private Integer kmActuels;
    private LocalDateTime dateEtat;

    public Integer getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(Integer idVehicule) {
        this.idVehicule = idVehicule;
    }

    public Integer getIdEtatVoiture() {
        return idEtatVoiture;
    }

    public void setIdEtatVoiture(Integer idEtatVoiture) {
        this.idEtatVoiture = idEtatVoiture;
    }

    public String getLibelleEtatVoiture() {
        return libelleEtatVoiture;
    }

    public void setLibelleEtatVoiture(String libelleEtatVoiture) {
        this.libelleEtatVoiture = libelleEtatVoiture;
    }

    public String getEnergie() {
        return energie;
    }

    public void setEnergie(String energie) {
        this.energie = energie;
    }

    public String getNumeroChassis() {
        return numeroChassis;
    }

    public void setNumeroChassis(String numeroChassis) {
        this.numeroChassis = numeroChassis;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public Integer getKilometrage() {
        return kilometrage;
    }

    public void setKilometrage(Integer kilometrage) {
        this.kilometrage = kilometrage;
    }

    public LocalDateTime getDateMiseEnService() {
        return dateMiseEnService;
    }

    public void setDateMiseEnService(LocalDateTime dateMiseEnService) {
        this.dateMiseEnService = dateMiseEnService;
    }

    public BigDecimal getCoutAchat() {
        return coutAchat;
    }

    public void setCoutAchat(BigDecimal coutAchat) {
        this.coutAchat = coutAchat;
    }

    public LocalDateTime getDateAmortissement() {
        return dateAmortissement;
    }

    public void setDateAmortissement(LocalDateTime dateAmortissement) {
        this.dateAmortissement = dateAmortissement;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Integer getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(Integer nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public LocalDateTime getDateAcquisition() {
        return dateAcquisition;
    }

    public void setDateAcquisition(LocalDateTime dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    public Integer getPuissance() {
        return puissance;
    }

    public void setPuissance(Integer puissance) {
        this.puissance = puissance;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public BigDecimal getPrixVehicule() {
        return prixVehicule;
    }

    public void setPrixVehicule(BigDecimal prixVehicule) {
        this.prixVehicule = prixVehicule;
    }

    public Integer getKmActuels() {
        return kmActuels;
    }

    public void setKmActuels(Integer kmActuels) {
        this.kmActuels = kmActuels;
    }

    public LocalDateTime getDateEtat() {
        return dateEtat;
    }

    public void setDateEtat(LocalDateTime dateEtat) {
        this.dateEtat = dateEtat;
    }
}