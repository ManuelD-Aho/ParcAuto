package main.java.com.miage.parcauto.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO représentant un bilan financier pour une période donnée.
 * Cette classe est utilisée pour transférer les informations financières
 * entre les différentes couches de l'application.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class BilanFinancierDTO {

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private BigDecimal recettes;
    private BigDecimal depenses;
    private BigDecimal solde;
    private BigDecimal coutEntretien;
    private BigDecimal coutCarburant;
    private BigDecimal coutAssurance;
    private BigDecimal coutAutres;
    private int nombreMissions;
    private int nombreEntretiens;

    /**
     * Constructeur par défaut.
     */
    public BilanFinancierDTO() {
        this.recettes = BigDecimal.ZERO;
        this.depenses = BigDecimal.ZERO;
        this.solde = BigDecimal.ZERO;
        this.coutEntretien = BigDecimal.ZERO;
        this.coutCarburant = BigDecimal.ZERO;
        this.coutAssurance = BigDecimal.ZERO;
        this.coutAutres = BigDecimal.ZERO;
        this.nombreMissions = 0;
        this.nombreEntretiens = 0;
    }

    /**
     * Constructeur complet.
     * 
     * @param dateDebut        la date de début
     * @param dateFin          la date de fin
     * @param recettes         les recettes
     * @param depenses         les dépenses
     * @param solde            le solde
     * @param coutEntretien    le coût d'entretien
     * @param coutCarburant    le coût de carburant
     * @param coutAssurance    le coût d'assurance
     * @param coutAutres       les autres coûts
     * @param nombreMissions   le nombre de missions
     * @param nombreEntretiens le nombre d'entretiens
     */
    public BilanFinancierDTO(LocalDate dateDebut, LocalDate dateFin, BigDecimal recettes, BigDecimal depenses,
            BigDecimal solde, BigDecimal coutEntretien, BigDecimal coutCarburant, BigDecimal coutAssurance,
            BigDecimal coutAutres, int nombreMissions, int nombreEntretiens) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.recettes = recettes;
        this.depenses = depenses;
        this.solde = solde;
        this.coutEntretien = coutEntretien;
        this.coutCarburant = coutCarburant;
        this.coutAssurance = coutAssurance;
        this.coutAutres = coutAutres;
        this.nombreMissions = nombreMissions;
        this.nombreEntretiens = nombreEntretiens;
    }

    /**
     * @return la date de début de la période
     */
    public LocalDate getDateDebut() {
        return dateDebut;
    }

    /**
     * @param dateDebut la date de début à définir
     */
    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * @return la date de fin de la période
     */
    public LocalDate getDateFin() {
        return dateFin;
    }

    /**
     * @param dateFin la date de fin à définir
     */
    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * @return le montant total des recettes
     */
    public BigDecimal getRecettes() {
        return recettes;
    }

    /**
     * @param recettes le montant des recettes à définir
     */
    public void setRecettes(BigDecimal recettes) {
        this.recettes = recettes;
    }

    /**
     * @return le montant total des dépenses
     */
    public BigDecimal getDepenses() {
        return depenses;
    }

    /**
     * @param depenses le montant des dépenses à définir
     */
    public void setDepenses(BigDecimal depenses) {
        this.depenses = depenses;
    }

    /**
     * @return le solde final
     */
    public BigDecimal getSolde() {
        return solde;
    }

    /**
     * @param solde le solde à définir
     */
    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    /**
     * @return le coût total des entretiens
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
     * @return le coût total du carburant
     */
    public BigDecimal getCoutCarburant() {
        return coutCarburant;
    }

    /**
     * @param coutCarburant le coût du carburant à définir
     */
    public void setCoutCarburant(BigDecimal coutCarburant) {
        this.coutCarburant = coutCarburant;
    }

    /**
     * @return le coût total de l'assurance
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
     * @return le coût des autres dépenses
     */
    public BigDecimal getCoutAutres() {
        return coutAutres;
    }

    /**
     * @param coutAutres le coût des autres dépenses à définir
     */
    public void setCoutAutres(BigDecimal coutAutres) {
        this.coutAutres = coutAutres;
    }

    /**
     * @return le nombre de missions effectuées
     */
    public int getNombreMissions() {
        return nombreMissions;
    }

    /**
     * @param nombreMissions le nombre de missions à définir
     */
    public void setNombreMissions(int nombreMissions) {
        this.nombreMissions = nombreMissions;
    }

    /**
     * @return le nombre d'entretiens réalisés
     */
    public int getNombreEntretiens() {
        return nombreEntretiens;
    }

    /**
     * @param nombreEntretiens le nombre d'entretiens à définir
     */
    public void setNombreEntretiens(int nombreEntretiens) {
        this.nombreEntretiens = nombreEntretiens;
    }
}
