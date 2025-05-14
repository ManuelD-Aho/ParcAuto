package main.java.com.miage.parcauto.dto;

import main.java.com.miage.parcauto.model.vehicule.EtatVoiture;
import main.java.com.miage.parcauto.model.vehicule.Vehicule.TypeEnergie;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class VehiculeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idVehicule;
    private EtatVoiture etatVoiture; // From ETAT_VOITURE table via id_etat_voiture
    private TypeEnergie energie; // DB Enum: Diesel, Essence, Électrique, Hybride
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
    private LocalDateTime dateEtat; // Corresponds to VEHICULES.date_etat

    // Fields from original DTO, potentially derived or for specific UI needs
    private LocalDateTime dernierEntretien;
    private Integer kmDernierEntretien;
    private Boolean enMission;


    public VehiculeDTO() {
        this.etatVoiture = EtatVoiture.EnService; // Default or map from DB's 'Disponible'
        this.energie = TypeEnergie.Diesel;
        this.kmActuels = 0;
        this.enMission = false;
    }

    public VehiculeDTO(Integer idVehicule, EtatVoiture etatVoiture, TypeEnergie energie, String numeroChassi, String immatriculation, String marque, String modele, Integer nbPlaces, LocalDateTime dateAcquisition, LocalDateTime dateAmmortissement, LocalDateTime dateMiseEnService, Integer puissance, String couleur, BigDecimal prixVehicule, Integer kmActuels, LocalDateTime dateEtat, LocalDateTime dernierEntretien, Integer kmDernierEntretien, Boolean enMission) {
        this.idVehicule = idVehicule;
        this.etatVoiture = etatVoiture;
        this.energie = energie;
        this.numeroChassi = numeroChassi;
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.modele = modele;
        this.nbPlaces = nbPlaces;
        this.dateAcquisition = dateAcquisition;
        this.dateAmmortissement = dateAmmortissement;
        this.dateMiseEnService = dateMiseEnService;
        this.puissance = puissance;
        this.couleur = couleur;
        this.prixVehicule = prixVehicule;
        this.kmActuels = kmActuels;
        this.dateEtat = dateEtat;
        this.dernierEntretien = dernierEntretien;
        this.kmDernierEntretien = kmDernierEntretien;
        this.enMission = enMission;
    }

    public Integer getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(Integer idVehicule) {
        this.idVehicule = idVehicule;
    }

    public EtatVoiture getEtatVoiture() {
        return etatVoiture;
    }

    public void setEtatVoiture(EtatVoiture etatVoiture) {
        this.etatVoiture = etatVoiture;
    }

    public TypeEnergie getEnergie() {
        return energie;
    }

    public void setEnergie(TypeEnergie energie) {
        this.energie = energie;
    }

    public String getNumeroChassi() {
        return numeroChassi;
    }

    public void setNumeroChassi(String numeroChassi) {
        this.numeroChassi = numeroChassi;
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

    public LocalDateTime getDateAmmortissement() {
        return dateAmmortissement;
    }

    public void setDateAmmortissement(LocalDateTime dateAmmortissement) {
        this.dateAmmortissement = dateAmmortissement;
    }

    public LocalDateTime getDateMiseEnService() {
        return dateMiseEnService;
    }

    public void setDateMiseEnService(LocalDateTime dateMiseEnService) {
        this.dateMiseEnService = dateMiseEnService;
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

    public LocalDateTime getDernierEntretien() {
        return dernierEntretien;
    }

    public void setDernierEntretien(LocalDateTime dernierEntretien) {
        this.dernierEntretien = dernierEntretien;
    }

    public Integer getKmDernierEntretien() {
        return kmDernierEntretien;
    }

    public void setKmDernierEntretien(Integer kmDernierEntretien) {
        this.kmDernierEntretien = kmDernierEntretien;
    }

    public Boolean getEnMission() {
        return enMission;
    }

    public void setEnMission(Boolean enMission) {
        this.enMission = enMission;
    }

    public String getPrixVehiculeFormate() {
        return prixVehicule != null ? prixVehicule.setScale(2, BigDecimal.ROUND_HALF_UP) + " FCFA" : "N/A";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehiculeDTO that = (VehiculeDTO) o;
        return Objects.equals(idVehicule, that.idVehicule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVehicule);
    }

    @Override
    public String toString() {
        return marque + " " + modele + " (" + immatriculation + ")";
    }
}