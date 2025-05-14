package main.java.com.miage.parcauto.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.Month;

/**
 * DTO représentant le bilan de la flotte de véhicules.
 * Contient les informations statistiques et financières globales
 * sur l'ensemble des véhicules.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class BilanFlotteDTO {

    private int annee;
    private int nombreVehicules;
    private int kmTotalParcourus;
    private BigDecimal coutTotalAcquisition;
    private BigDecimal coutTotalEntretien;
    private BigDecimal coutTotalAssurance;
    private BigDecimal coutTotalCarburant;
    private BigDecimal coutTotalAutres;
    private BigDecimal coutTotalGlobal;
    private BigDecimal coutMoyenParKm;
    private BigDecimal coutMoyenParVehicule;
    private Map<Month, BilanMensuelDTO> evolutionMensuelle;
    private Map<String, BigDecimal> repartitionBudgetaire;
    private List<VehiculeRentabiliteDTO> vehiculesParRentabilite;

    /**
     * Constructeur par défaut.
     */
    public BilanFlotteDTO() {
        this.nombreVehicules = 0;
        this.kmTotalParcourus = 0;
        this.coutTotalAcquisition = BigDecimal.ZERO;
        this.coutTotalEntretien = BigDecimal.ZERO;
        this.coutTotalAssurance = BigDecimal.ZERO;
        this.coutTotalCarburant = BigDecimal.ZERO;
        this.coutTotalAutres = BigDecimal.ZERO;
        this.coutTotalGlobal = BigDecimal.ZERO;
        this.coutMoyenParKm = BigDecimal.ZERO;
        this.coutMoyenParVehicule = BigDecimal.ZERO;
        this.evolutionMensuelle = new HashMap<>();
        this.repartitionBudgetaire = new HashMap<>();
        this.vehiculesParRentabilite = new ArrayList<>();
    }

    /**
     * Constructeur complet.
     *
     * @param annee                   l'année
     * @param nombreVehicules         le nombre de véhicules
     * @param kmTotalParcourus        le nombre total de km parcourus
     * @param coutTotalAcquisition    le coût total d'acquisition
     * @param coutTotalEntretien      le coût total d'entretien
     * @param coutTotalAssurance      le coût total d'assurance
     * @param coutTotalCarburant      le coût total de carburant
     * @param coutTotalAutres         les autres coûts totaux
     * @param coutTotalGlobal         le coût total global
     * @param coutMoyenParKm          le coût moyen par km
     * @param coutMoyenParVehicule    le coût moyen par véhicule
     * @param evolutionMensuelle      l'évolution mensuelle
     * @param repartitionBudgetaire   la répartition budgétaire
     * @param vehiculesParRentabilite les véhicules par rentabilité
     */
    public BilanFlotteDTO(int annee, int nombreVehicules, int kmTotalParcourus, BigDecimal coutTotalAcquisition,
            BigDecimal coutTotalEntretien, BigDecimal coutTotalAssurance, BigDecimal coutTotalCarburant,
            BigDecimal coutTotalAutres, BigDecimal coutTotalGlobal, BigDecimal coutMoyenParKm,
            BigDecimal coutMoyenParVehicule, Map<Month, BilanMensuelDTO> evolutionMensuelle,
            Map<String, BigDecimal> repartitionBudgetaire, List<VehiculeRentabiliteDTO> vehiculesParRentabilite) {
        this.annee = annee;
        this.nombreVehicules = nombreVehicules;
        this.kmTotalParcourus = kmTotalParcourus;
        this.coutTotalAcquisition = coutTotalAcquisition;
        this.coutTotalEntretien = coutTotalEntretien;
        this.coutTotalAssurance = coutTotalAssurance;
        this.coutTotalCarburant = coutTotalCarburant;
        this.coutTotalAutres = coutTotalAutres;
        this.coutTotalGlobal = coutTotalGlobal;
        this.coutMoyenParKm = coutMoyenParKm;
        this.coutMoyenParVehicule = coutMoyenParVehicule;
        this.evolutionMensuelle = evolutionMensuelle;
        this.repartitionBudgetaire = repartitionBudgetaire;
        this.vehiculesParRentabilite = vehiculesParRentabilite;
    }

    /**
     * @return l'année
     */
    public int getAnnee() {
        return annee;
    }

    /**
     * @param annee l'année à définir
     */
    public void setAnnee(int annee) {
        this.annee = annee;
    }

    /**
     * @return le nombre de véhicules
     */
    public int getNombreVehicules() {
        return nombreVehicules;
    }

    /**
     * @param nombreVehicules le nombre de véhicules à définir
     */
    public void setNombreVehicules(int nombreVehicules) {
        this.nombreVehicules = nombreVehicules;
    }

    /**
     * @return le nombre total de km parcourus
     */
    public int getKmTotalParcourus() {
        return kmTotalParcourus;
    }

    /**
     * @param kmTotalParcourus le nombre total de km parcourus à définir
     */
    public void setKmTotalParcourus(int kmTotalParcourus) {
        this.kmTotalParcourus = kmTotalParcourus;
    }

    /**
     * @return le coût total d'acquisition
     */
    public BigDecimal getCoutTotalAcquisition() {
        return coutTotalAcquisition;
    }

    /**
     * @param coutTotalAcquisition le coût total d'acquisition à définir
     */
    public void setCoutTotalAcquisition(BigDecimal coutTotalAcquisition) {
        this.coutTotalAcquisition = coutTotalAcquisition;
    }

    /**
     * @return le coût total d'entretien
     */
    public BigDecimal getCoutTotalEntretien() {
        return coutTotalEntretien;
    }

    /**
     * @param coutTotalEntretien le coût total d'entretien à définir
     */
    public void setCoutTotalEntretien(BigDecimal coutTotalEntretien) {
        this.coutTotalEntretien = coutTotalEntretien;
    }

    /**
     * @return le coût total d'assurance
     */
    public BigDecimal getCoutTotalAssurance() {
        return coutTotalAssurance;
    }

    /**
     * @param coutTotalAssurance le coût total d'assurance à définir
     */
    public void setCoutTotalAssurance(BigDecimal coutTotalAssurance) {
        this.coutTotalAssurance = coutTotalAssurance;
    }

    /**
     * @return le coût total de carburant
     */
    public BigDecimal getCoutTotalCarburant() {
        return coutTotalCarburant;
    }

    /**
     * @param coutTotalCarburant le coût total de carburant à définir
     */
    public void setCoutTotalCarburant(BigDecimal coutTotalCarburant) {
        this.coutTotalCarburant = coutTotalCarburant;
    }

    /**
     * @return les autres coûts totaux
     */
    public BigDecimal getCoutTotalAutres() {
        return coutTotalAutres;
    }

    /**
     * @param coutTotalAutres les autres coûts totaux à définir
     */
    public void setCoutTotalAutres(BigDecimal coutTotalAutres) {
        this.coutTotalAutres = coutTotalAutres;
    }

    /**
     * @return le coût total global
     */
    public BigDecimal getCoutTotalGlobal() {
        return coutTotalGlobal;
    }

    /**
     * @param coutTotalGlobal le coût total global à définir
     */
    public void setCoutTotalGlobal(BigDecimal coutTotalGlobal) {
        this.coutTotalGlobal = coutTotalGlobal;
    }

    /**
     * @return le coût moyen par km
     */
    public BigDecimal getCoutMoyenParKm() {
        return coutMoyenParKm;
    }

    /**
     * @param coutMoyenParKm le coût moyen par km à définir
     */
    public void setCoutMoyenParKm(BigDecimal coutMoyenParKm) {
        this.coutMoyenParKm = coutMoyenParKm;
    }

    /**
     * @return le coût moyen par véhicule
     */
    public BigDecimal getCoutMoyenParVehicule() {
        return coutMoyenParVehicule;
    }

    /**
     * @param coutMoyenParVehicule le coût moyen par véhicule à définir
     */
    public void setCoutMoyenParVehicule(BigDecimal coutMoyenParVehicule) {
        this.coutMoyenParVehicule = coutMoyenParVehicule;
    }

    /**
     * @return l'évolution mensuelle
     */
    public Map<Month, BilanMensuelDTO> getEvolutionMensuelle() {
        return evolutionMensuelle;
    }

    /**
     * @param evolutionMensuelle l'évolution mensuelle à définir
     */
    public void setEvolutionMensuelle(Map<Month, BilanMensuelDTO> evolutionMensuelle) {
        this.evolutionMensuelle = evolutionMensuelle;
    }

    /**
     * @return la répartition budgétaire
     */
    public Map<String, BigDecimal> getRepartitionBudgetaire() {
        return repartitionBudgetaire;
    }

    /**
     * @param repartitionBudgetaire la répartition budgétaire à définir
     */
    public void setRepartitionBudgetaire(Map<String, BigDecimal> repartitionBudgetaire) {
        this.repartitionBudgetaire = repartitionBudgetaire;
    }

    /**
     * @return les véhicules par rentabilité
     */
    public List<VehiculeRentabiliteDTO> getVehiculesParRentabilite() {
        return vehiculesParRentabilite;
    }

    /**
     * @param vehiculesParRentabilite les véhicules par rentabilité à définir
     */
    public void setVehiculesParRentabilite(List<VehiculeRentabiliteDTO> vehiculesParRentabilite) {
        this.vehiculesParRentabilite = vehiculesParRentabilite;
    }
}
