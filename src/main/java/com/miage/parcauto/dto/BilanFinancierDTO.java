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
    }

    /**
     * Constructeur complet.
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

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public BigDecimal getRecettes() {
        return recettes;
    }

    public void setRecettes(BigDecimal recettes) {
        this.recettes = recettes;
    }

    public BigDecimal getDepenses() {
        return depenses;
    }

    public void setDepenses(BigDecimal depenses) {
        this.depenses = depenses;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public BigDecimal getCoutEntretien() {
        return coutEntretien;
    }

    public void setCoutEntretien(BigDecimal coutEntretien) {
        this.coutEntretien = coutEntretien;
    }

    public BigDecimal getCoutCarburant() {
        return coutCarburant;
    }

    public void setCoutCarburant(BigDecimal coutCarburant) {
        this.coutCarburant = coutCarburant;
    }

    public BigDecimal getCoutAssurance() {
        return coutAssurance;
    }

    public void setCoutAssurance(BigDecimal coutAssurance) {
        this.coutAssurance = coutAssurance;
    }

    public BigDecimal getCoutAutres() {
        return coutAutres;
    }

    public void setCoutAutres(BigDecimal coutAutres) {
        this.coutAutres = coutAutres;
    }

    public int getNombreMissions() {
        return nombreMissions;
    }

    public void setNombreMissions(int nombreMissions) {
        this.nombreMissions = nombreMissions;
    }

    public int getNombreEntretiens() {
        return nombreEntretiens;
    }

    public void setNombreEntretiens(int nombreEntretiens) {
        this.nombreEntretiens = nombreEntretiens;
    }
}
