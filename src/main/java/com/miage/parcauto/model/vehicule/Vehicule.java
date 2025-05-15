package main.java.com.miage.parcauto.model.vehicule;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entité représentant un véhicule du parc automobile.
 */
public class Vehicule implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idVehicule;
    private EtatVoiture etatVoiture;
    private Energie energie;
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
    private LocalDateTime dateEtat;

    public Vehicule() {
    }

    // Getters et Setters
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

    public Energie getEnergie() {
        return energie;
    }

    public void setEnergie(Energie energie) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicule vehicule = (Vehicule) o;
        return Objects.equals(idVehicule, vehicule.idVehicule);
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