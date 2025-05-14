package main.java.com.miage.parcauto.dto;

import java.math.BigDecimal;

/**
 * DTO représentant le bilan mensuel pour les analyses financières.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class BilanMensuelDTO {

    private int mois;
    private int nombreMissions;
    private int nombreEntretiens;
    private BigDecimal recettes;
    private BigDecimal depenses;
    private BigDecimal solde;

    /**
     * Constructeur par défaut.
     */
    public BilanMensuelDTO() {
        this.recettes = BigDecimal.ZERO;
        this.depenses = BigDecimal.ZERO;
        this.solde = BigDecimal.ZERO;
    }

    /**
     * @return le mois
     */
    public int getMois() {
        return mois;
    }

    /**
     * @param mois le mois à définir
     */
    public void setMois(int mois) {
        this.mois = mois;
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
}
