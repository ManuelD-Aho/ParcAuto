package main.java.com.miage.parcauto.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class VehiculeRentabiliteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idVehicule;
    private String vehiculeInfo; // Marque, modèle, immatriculation
    private LocalDate dateDebutAnalyse;
    private LocalDate dateFinAnalyse;
    private BigDecimal revenusGeneres; // Par ex. mensualités de sociétaires, facturation de missions
    private BigDecimal coutTotalAcquisition; // Prix d'achat initial
    private BigDecimal coutTotalEntretiens;
    private BigDecimal coutTotalAssurances;
    private BigDecimal coutTotalCarburantMissions; // Spécifique aux missions
    private BigDecimal autresCoutsOperationnels; // Taxes, parking, etc.
    private BigDecimal coutTotalOperationnel; // Somme des coûts hors acquisition
    private BigDecimal beneficeNetVehicule; // Revenus - Coûts Opérationnels
    private BigDecimal retourSurInvestissementInitial; // (Bénéfice Net / Coût Acquisition) * 100
    private Integer nombreMissionsEffectuees;
    private Integer joursAffectationCredit; // Nombre de jours où le véhicule a été affecté en crédit

    public VehiculeRentabiliteDTO() {
        this.revenusGeneres = BigDecimal.ZERO;
        this.coutTotalAcquisition = BigDecimal.ZERO;
        this.coutTotalEntretiens = BigDecimal.ZERO;
        this.coutTotalAssurances = BigDecimal.ZERO;
        this.coutTotalCarburantMissions = BigDecimal.ZERO;
        this.autresCoutsOperationnels = BigDecimal.ZERO;
        this.coutTotalOperationnel = BigDecimal.ZERO;
        this.beneficeNetVehicule = BigDecimal.ZERO;
        this.retourSurInvestissementInitial = BigDecimal.ZERO;
    }

    public Integer getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(Integer idVehicule) {
        this.idVehicule = idVehicule;
    }

    public String getVehiculeInfo() {
        return vehiculeInfo;
    }

    public void setVehiculeInfo(String vehiculeInfo) {
        this.vehiculeInfo = vehiculeInfo;
    }

    public LocalDate getDateDebutAnalyse() {
        return dateDebutAnalyse;
    }

    public void setDateDebutAnalyse(LocalDate dateDebutAnalyse) {
        this.dateDebutAnalyse = dateDebutAnalyse;
    }

    public LocalDate getDateFinAnalyse() {
        return dateFinAnalyse;
    }

    public void setDateFinAnalyse(LocalDate dateFinAnalyse) {
        this.dateFinAnalyse = dateFinAnalyse;
    }

    public BigDecimal getRevenusGeneres() {
        return revenusGeneres;
    }

    public void setRevenusGeneres(BigDecimal revenusGeneres) {
        this.revenusGeneres = revenusGeneres;
    }

    public BigDecimal getCoutTotalAcquisition() {
        return coutTotalAcquisition;
    }

    public void setCoutTotalAcquisition(BigDecimal coutTotalAcquisition) {
        this.coutTotalAcquisition = coutTotalAcquisition;
    }

    public BigDecimal getCoutTotalEntretiens() {
        return coutTotalEntretiens;
    }

    public void setCoutTotalEntretiens(BigDecimal coutTotalEntretiens) {
        this.coutTotalEntretiens = coutTotalEntretiens;
    }

    public BigDecimal getCoutTotalAssurances() {
        return coutTotalAssurances;
    }

    public void setCoutTotalAssurances(BigDecimal coutTotalAssurances) {
        this.coutTotalAssurances = coutTotalAssurances;
    }

    public BigDecimal getCoutTotalCarburantMissions() {
        return coutTotalCarburantMissions;
    }

    public void setCoutTotalCarburantMissions(BigDecimal coutTotalCarburantMissions) {
        this.coutTotalCarburantMissions = coutTotalCarburantMissions;
    }

    public BigDecimal getAutresCoutsOperationnels() {
        return autresCoutsOperationnels;
    }

    public void setAutresCoutsOperationnels(BigDecimal autresCoutsOperationnels) {
        this.autresCoutsOperationnels = autresCoutsOperationnels;
    }

    public BigDecimal getCoutTotalOperationnel() {
        return coutTotalOperationnel;
    }

    public void setCoutTotalOperationnel(BigDecimal coutTotalOperationnel) {
        this.coutTotalOperationnel = coutTotalOperationnel;
    }

    public BigDecimal getBeneficeNetVehicule() {
        return beneficeNetVehicule;
    }

    public void setBeneficeNetVehicule(BigDecimal beneficeNetVehicule) {
        this.beneficeNetVehicule = beneficeNetVehicule;
    }

    public BigDecimal getRetourSurInvestissementInitial() {
        return retourSurInvestissementInitial;
    }

    public void setRetourSurInvestissementInitial(BigDecimal retourSurInvestissementInitial) {
        this.retourSurInvestissementInitial = retourSurInvestissementInitial;
    }

    public Integer getNombreMissionsEffectuees() {
        return nombreMissionsEffectuees;
    }

    public void setNombreMissionsEffectuees(Integer nombreMissionsEffectuees) {
        this.nombreMissionsEffectuees = nombreMissionsEffectuees;
    }

    public Integer getJoursAffectationCredit() {
        return joursAffectationCredit;
    }

    public void setJoursAffectationCredit(Integer joursAffectationCredit) {
        this.joursAffectationCredit = joursAffectationCredit;
    }

    @Override
    public String toString() {
        return "VehiculeRentabiliteDTO{" +
                "vehiculeInfo='" + vehiculeInfo + '\'' +
                ", beneficeNetVehicule=" + beneficeNetVehicule +
                ", periode='" + dateDebutAnalyse + " à " + dateFinAnalyse + '\'' +
                '}';
    }
}