package main.java.com.miage.parcauto.model.finance;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Classe représentant un bilan financier pour une période donnée.
 * Contient les informations sur les recettes, dépenses, solde et autres
 * métriques financières.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class BilanFinancier {

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
    public BilanFinancier() {
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
     * @return la date de début
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
     * @return la date de fin
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
     * @return les recettes
     */
    public BigDecimal getRecettes() {
        return recettes;
    }

    /**
     * @param recettes les recettes à définir
     */
    public void setRecettes(BigDecimal recettes) {
        this.recettes = recettes;
    }

    /**
     * @return les dépenses
     */
    public BigDecimal getDepenses() {
        return depenses;
    }

    /**
     * @param depenses les dépenses à définir
     */
    public void setDepenses(BigDecimal depenses) {
        this.depenses = depenses;
    }

    /**
     * @return le solde
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
     * @return le nombre de missions
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
     * @return le nombre d'entretiens
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
