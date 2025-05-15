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

    @Override
    public String toString() {
        return "TcoVehiculeDTO{" +
                "vehiculeInfo='" + vehiculeInfo + '\'' +
                ", coutsTotauxPossession=" + coutsTotauxPossession +
                ", coutParKm=" + coutParKm +
                '}';
    }
}