package main.java.com.miage.parcauto.dto;

import main.java.com.miage.parcauto.model.vehicule.EtatVoiture;
import main.java.com.miage.parcauto.model.vehicule.Vehicule.TypeEnergie;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Objet de transfert de données (DTO) pour les véhicules.
 * Utilisé pour transférer des données entre la couche service et la couche
 * présentation.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class VehiculeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idVehicule;
    private EtatVoiture etatVoiture;
    private TypeEnergie energie;
    private String numeroChassi;
    private String immatriculation;
    private String marque;
    private String modele;
    private Integer nbPlaces;
    private LocalDateTime dateAcquisition;
    private LocalDateTime dateAmmortissement;
    private LocalDateTime dateMiseEnService;
    private Integer puissance;
    private String couleur;
    private BigDecimal prixVehicule;
    private Integer kmActuels;
    private LocalDateTime dernierEntretien;
    private Integer kmDernierEntretien;
    private Boolean enMission;

    /**
     * Constructeur par défaut.
     */
    public VehiculeDTO() {
        this.etatVoiture = EtatVoiture.EnService;
        this.energie = TypeEnergie.Diesel;
        this.kmActuels = 0;
        this.enMission = false;
    }

    /**
     * @return Identifiant unique du véhicule
     */
    public Integer getIdVehicule() {
        return idVehicule;
    }

    /**
     * @param idVehicule Nouvel identifiant du véhicule
     */
    public void setIdVehicule(Integer idVehicule) {
        this.idVehicule = idVehicule;
    }

    /**
     * @return État actuel du véhicule
     */
    public EtatVoiture getEtatVoiture() {
        return etatVoiture;
    }

    /**
     * @param etatVoiture Nouvel état du véhicule
     */
    public void setEtatVoiture(EtatVoiture etatVoiture) {
        this.etatVoiture = etatVoiture;
    }

    /**
     * @return Type d'énergie du véhicule
     */
    public TypeEnergie getEnergie() {
        return energie;
    }

    /**
     * @param energie Nouveau type d'énergie
     */
    public void setEnergie(TypeEnergie energie) {
        this.energie = energie;
    }

    /**
     * @return Numéro de châssis du véhicule
     */
    public String getNumeroChassi() {
        return numeroChassi;
    }

    /**
     * @param numeroChassi Nouveau numéro de châssis
     */
    public void setNumeroChassi(String numeroChassi) {
        this.numeroChassi = numeroChassi;
    }

    /**
     * @return Immatriculation du véhicule
     */
    public String getImmatriculation() {
        return immatriculation;
    }

    /**
     * @param immatriculation Nouvelle immatriculation
     */
    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    /**
     * @return Marque du véhicule
     */
    public String getMarque() {
        return marque;
    }

    /**
     * @param marque Nouvelle marque
     */
    public void setMarque(String marque) {
        this.marque = marque;
    }

    /**
     * @return Modèle du véhicule
     */
    public String getModele() {
        return modele;
    }

    /**
     * @param modele Nouveau modèle
     */
    public void setModele(String modele) {
        this.modele = modele;
    }

    /**
     * @return Nombre de places du véhicule
     */
    public Integer getNbPlaces() {
        return nbPlaces;
    }

    /**
     * @param nbPlaces Nouveau nombre de places
     */
    public void setNbPlaces(Integer nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    /**
     * @return Date d'acquisition du véhicule
     */
    public LocalDateTime getDateAcquisition() {
        return dateAcquisition;
    }

    /**
     * @param dateAcquisition Nouvelle date d'acquisition
     */
    public void setDateAcquisition(LocalDateTime dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    /**
     * @return Date d'amortissement du véhicule
     */
    public LocalDateTime getDateAmmortissement() {
        return dateAmmortissement;
    }

    /**
     * @param dateAmmortissement Nouvelle date d'amortissement
     */
    public void setDateAmmortissement(LocalDateTime dateAmmortissement) {
        this.dateAmmortissement = dateAmmortissement;
    }

    /**
     * @return Date de mise en service du véhicule
     */
    public LocalDateTime getDateMiseEnService() {
        return dateMiseEnService;
    }

    /**
     * @param dateMiseEnService Nouvelle date de mise en service
     */
    public void setDateMiseEnService(LocalDateTime dateMiseEnService) {
        this.dateMiseEnService = dateMiseEnService;
    }

    /**
     * @return Puissance du véhicule
     */
    public Integer getPuissance() {
        return puissance;
    }

    /**
     * @param puissance Nouvelle puissance
     */
    public void setPuissance(Integer puissance) {
        this.puissance = puissance;
    }

    /**
     * @return Couleur du véhicule
     */
    public String getCouleur() {
        return couleur;
    }

    /**
     * @param couleur Nouvelle couleur
     */
    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    /**
     * @return Prix d'achat du véhicule
     */
    public BigDecimal getPrixVehicule() {
        return prixVehicule;
    }

    /**
     * @param prixVehicule Nouveau prix d'achat
     */
    public void setPrixVehicule(BigDecimal prixVehicule) {
        this.prixVehicule = prixVehicule;
    }

    /**
     * @return Kilométrage actuel du véhicule
     */
    public Integer getKmActuels() {
        return kmActuels;
    }

    /**
     * @param kmActuels Nouveau kilométrage
     */
    public void setKmActuels(Integer kmActuels) {
        this.kmActuels = kmActuels;
    }

    /**
     * @return Date du dernier entretien
     */
    public LocalDateTime getDernierEntretien() {
        return dernierEntretien;
    }

    /**
     * @param dernierEntretien Nouvelle date du dernier entretien
     */
    public void setDernierEntretien(LocalDateTime dernierEntretien) {
        this.dernierEntretien = dernierEntretien;
    }

    /**
     * @return Kilométrage lors du dernier entretien
     */
    public Integer getKmDernierEntretien() {
        return kmDernierEntretien;
    }

    /**
     * @param kmDernierEntretien Nouveau kilométrage lors du dernier entretien
     */
    public void setKmDernierEntretien(Integer kmDernierEntretien) {
        this.kmDernierEntretien = kmDernierEntretien;
    }

    /**
     * @return Indique si le véhicule est actuellement en mission
     */
    public Boolean getEnMission() {
        return enMission;
    }

    /**
     * @param enMission Nouvel état de mission
     */
    public void setEnMission(Boolean enMission) {
        this.enMission = enMission;
    }

    @Override
    public String toString() {
        return marque + " " + modele + " (" + immatriculation + ")";
    }
}
