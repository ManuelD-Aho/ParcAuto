package main.java.com.miage.parcauto.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class TCODTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal coutsTotaux;
    private Integer idVehicule;
    private String vehiculeInfo;
    private Integer kmActuels;
    private BigDecimal coutParKm;

    public TCODTO() {
    }

    public BigDecimal getCoutsTotaux() {
        return coutsTotaux;
    }

    public void setCoutsTotaux(BigDecimal coutsTotaux) {
        this.coutsTotaux = coutsTotaux;
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

    @Override
    public String toString() {
        return "TCO pour " + (vehiculeInfo != null ? vehiculeInfo : "Véhicule ID " + idVehicule) + ": " + coutsTotaux + "€";
    }
}