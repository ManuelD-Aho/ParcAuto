package main.java.com.miage.parcauto.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class TcoVehiculeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idVehicule;
    private String vehiculeInfo; // Marque, modèle, immatriculation
    private BigDecimal coutsTotauxPossession; // Coût d'achat, entretiens, assurances, carburant (si applicable au TCO)
    private Integer kmActuels;
    private BigDecimal coutParKm;
    private BigDecimal valeurResiduelleEstimee; // Optionnel
    private String immatriculation;
    private String marque;
    private String modele;
    private BigDecimal coutAchat;
    private BigDecimal coutTotalEntretiens;
    private BigDecimal coutTotalCarburant;
    private BigDecimal coutTotalAutresDepenses;
    private BigDecimal coutTotalAssurances;
    private BigDecimal depreciation;
    private BigDecimal tcoTotal;

    public TcoVehiculeDTO() {
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

    public BigDecimal getCoutsTotauxPossession() {
        return coutsTotauxPossession;
    }

    public void setCoutsTotauxPossession(BigDecimal coutsTotauxPossession) {
        this.coutsTotauxPossession = coutsTotauxPossession;
    }

    public Integer getKmActuels() {
        return kmActuels;
    }

    public void setKmActuels(Integer kmActuels) {
        this.kmActuels = kmActuels;
    }

    public BigDecimal getCoutParKm() {
        return coutParKm;
    }

    public void setCoutParKm(BigDecimal coutParKm) {
        this.coutParKm = coutParKm;
    }

    public BigDecimal getValeurResiduelleEstimee() {
        return valeurResiduelleEstimee;
    }

    public void setValeurResiduelleEstimee(BigDecimal valeurResiduelleEstimee) {
        this.valeurResiduelleEstimee = valeurResiduelleEstimee;
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

    public BigDecimal getCoutAchat() {
        return coutAchat;
    }

    public void setCoutAchat(BigDecimal coutAchat) {
        this.coutAchat = coutAchat;
    }

    public BigDecimal getCoutTotalEntretiens() {
        return coutTotalEntretiens;
    }

    public void setCoutTotalEntretiens(BigDecimal coutTotalEntretiens) {
        this.coutTotalEntretiens = coutTotalEntretiens;
    }

    public BigDecimal getCoutTotalCarburant() {
        return coutTotalCarburant;
    }

    public void setCoutTotalCarburant(BigDecimal coutTotalCarburant) {
        this.coutTotalCarburant = coutTotalCarburant;
    }

    public BigDecimal getCoutTotalAutresDepenses() {
        return coutTotalAutresDepenses;
    }

    public void setCoutTotalAutresDepenses(BigDecimal coutTotalAutresDepenses) {
        this.coutTotalAutresDepenses = coutTotalAutresDepenses;
    }

    public BigDecimal getCoutTotalAssurances() {
        return coutTotalAssurances;
    }

    public void setCoutTotalAssurances(BigDecimal coutTotalAssurances) {
        this.coutTotalAssurances = coutTotalAssurances;
    }

    public BigDecimal getDepreciation() {
        return depreciation;
    }

    public void setDepreciation(BigDecimal depreciation) {
        this.depreciation = depreciation;
    }

    public BigDecimal getTcoTotal() {
        return tcoTotal;
    }

    public void setTcoTotal(BigDecimal tcoTotal) {
        this.tcoTotal = tcoTotal;
    }

    @Override
    public String toString() {
        return "TcoVehiculeDTO{" +
                "vehiculeInfo='" + vehiculeInfo + '\'' +
                ", coutsTotauxPossession=" + coutsTotauxPossession +
                ", coutParKm=" + coutParKm +
                '}';
    }
}