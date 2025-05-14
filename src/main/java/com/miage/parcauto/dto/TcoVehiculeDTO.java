package main.java.com.miage.parcauto.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO représentant le coût total de possession (TCO) d'un véhicule.
 * Cette classe permet de transférer les données de coût entre les couches de
 * l'application.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class TcoVehiculeDTO {

    private Integer idVehicule;
    private String marque;
    private String modele;
    private String immatriculation;
    private LocalDate dateMiseEnService;
    private BigDecimal coutAcquisition;
    private BigDecimal valeurResiduelle;
    private BigDecimal coutEntretien;
    private BigDecimal coutAssurance;
    private BigDecimal coutCarburant;
    private BigDecimal coutAutres;
    private BigDecimal tcoTotal;
    private BigDecimal tcoMensuel;
    private BigDecimal tcoParKm;
    private int kmParcourus;
    private int dureeEnMois;

    /**
     * Constructeur par défaut.
     */
    public TcoVehiculeDTO() {
        this.coutAcquisition = BigDecimal.ZERO;
        this.valeurResiduelle = BigDecimal.ZERO;
        this.coutEntretien = BigDecimal.ZERO;
        this.coutAssurance = BigDecimal.ZERO;
        this.coutCarburant = BigDecimal.ZERO;
        this.coutAutres = BigDecimal.ZERO;
        this.tcoTotal = BigDecimal.ZERO;
        this.tcoMensuel = BigDecimal.ZERO;
        this.tcoParKm = BigDecimal.ZERO;
        this.kmParcourus = 0;
        this.dureeEnMois = 0;
    }

    /**
     * @return l'ID du véhicule
     */
    public Integer getIdVehicule() {
        return idVehicule;
    }

    /**
     * @param idVehicule l'ID du véhicule à définir
     */
    public void setIdVehicule(Integer idVehicule) {
        this.idVehicule = idVehicule;
    }

    /**
     * @return la marque
     */
    public String getMarque() {
        return marque;
    }

    /**
     * @param marque la marque à définir
     */
    public void setMarque(String marque) {
        this.marque = marque;
    }

    /**
     * @return le modèle
     */
    public String getModele() {
        return modele;
    }

    /**
     * @param modele le modèle à définir
     */
    public void setModele(String modele) {
        this.modele = modele;
    }

    /**
     * @return l'immatriculation
     */
    public String getImmatriculation() {
        return immatriculation;
    }

    /**
     * @param immatriculation l'immatriculation à définir
     */
    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    /**
     * @return la date de mise en service
     */
    public LocalDate getDateMiseEnService() {
        return dateMiseEnService;
    }

    /**
     * @param dateMiseEnService la date de mise en service à définir
     */
    public void setDateMiseEnService(LocalDate dateMiseEnService) {
        this.dateMiseEnService = dateMiseEnService;
    }

    /**
     * @return le coût d'acquisition
     */
    public BigDecimal getCoutAcquisition() {
        return coutAcquisition;
    }

    /**
     * @param coutAcquisition le coût d'acquisition à définir
     */
    public void setCoutAcquisition(BigDecimal coutAcquisition) {
        this.coutAcquisition = coutAcquisition;
    }

    /**
     * @return la valeur résiduelle
     */
    public BigDecimal getValeurResiduelle() {
        return valeurResiduelle;
    }

    /**
     * @param valeurResiduelle la valeur résiduelle à définir
     */
    public void setValeurResiduelle(BigDecimal valeurResiduelle) {
        this.valeurResiduelle = valeurResiduelle;
    }

    /**
     * @return le coût d'entretien
     */
    public BigDecimal getCoutEntretien() {
        return coutEntretien;
    }

    /**
     * @param coutEntretien le coût d'entretien à définir
     */
    public void setCoutEntretien(BigDecimal coutEntretien) {
        this.coutEntretien = coutEntretien;
    }

    /**
     * @return le coût d'assurance
     */
    public BigDecimal getCoutAssurance() {
        return coutAssurance;
    }

    /**
     * @param coutAssurance le coût d'assurance à définir
     */
    public void setCoutAssurance(BigDecimal coutAssurance) {
        this.coutAssurance = coutAssurance;
    }

    /**
     * @return le coût de carburant
     */
    public BigDecimal getCoutCarburant() {
        return coutCarburant;
    }

    /**
     * @param coutCarburant le coût de carburant à définir
     */
    public void setCoutCarburant(BigDecimal coutCarburant) {
        this.coutCarburant = coutCarburant;
    }

    /**
     * @return les autres coûts
     */
    public BigDecimal getCoutAutres() {
        return coutAutres;
    }

    /**
     * @param coutAutres les autres coûts à définir
     */
    public void setCoutAutres(BigDecimal coutAutres) {
        this.coutAutres = coutAutres;
    }

    /**
     * @return le TCO total
     */
    public BigDecimal getTcoTotal() {
        return tcoTotal;
    }

    /**
     * @param tcoTotal le TCO total à définir
     */
    public void setTcoTotal(BigDecimal tcoTotal) {
        this.tcoTotal = tcoTotal;
    }

    /**
     * @return le TCO mensuel
     */
    public BigDecimal getTcoMensuel() {
        return tcoMensuel;
    }

    /**
     * @param tcoMensuel le TCO mensuel à définir
     */
    public void setTcoMensuel(BigDecimal tcoMensuel) {
        this.tcoMensuel = tcoMensuel;
    }

    /**
     * @return le TCO par km
     */
    public BigDecimal getTcoParKm() {
        return tcoParKm;
    }

    /**
     * @param tcoParKm le TCO par km à définir
     */
    public void setTcoParKm(BigDecimal tcoParKm) {
        this.tcoParKm = tcoParKm;
    }

    /**
     * @return le nombre de km parcourus
     */
    public int getKmParcourus() {
        return kmParcourus;
    }

    /**
     * @param kmParcourus le nombre de km parcourus à définir
     */
    public void setKmParcourus(int kmParcourus) {
        this.kmParcourus = kmParcourus;
    }

    /**
     * @return la durée en mois
     */
    public int getDureeEnMois() {
        return dureeEnMois;
    }

    /**
     * @param dureeEnMois la durée en mois à définir
     */
    public void setDureeEnMois(int dureeEnMois) {
        this.dureeEnMois = dureeEnMois;
    }
}
