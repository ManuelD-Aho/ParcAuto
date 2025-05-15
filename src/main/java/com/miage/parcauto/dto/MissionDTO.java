package main.java.com.miage.parcauto.dto;

import main.java.com.miage.parcauto.model.mission.StatutMission;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MissionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idMission;
    private Integer idVehicule;
    private String vehiculeInfo;
    private String libMission;
    private String site;
    private LocalDateTime dateDebutMission;
    private LocalDateTime dateFinMission;
    private Integer kmPrevu;
    private Integer kmReel;
    private StatutMission status;
    private BigDecimal coutTotal;
    private String circuitMission;
    private String observationMission;
    private List<DepenseMissionDTO> depenses = new ArrayList<>();

    public MissionDTO() {
    }

    public Integer getIdMission() {
        return idMission;
    }

    public void setIdMission(Integer idMission) {
        this.idMission = idMission;
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

    public String getLibMission() {
        return libMission;
    }

    public void setLibMission(String libMission) {
        this.libMission = libMission;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public LocalDateTime getDateDebutMission() {
        return dateDebutMission;
    }

    public void setDateDebutMission(LocalDateTime dateDebutMission) {
        this.dateDebutMission = dateDebutMission;
    }

    public LocalDateTime getDateFinMission() {
        return dateFinMission;
    }

    public void setDateFinMission(LocalDateTime dateFinMission) {
        this.dateFinMission = dateFinMission;
    }

    public Integer getKmPrevu() {
        return kmPrevu;
    }

    public void setKmPrevu(Integer kmPrevu) {
        this.kmPrevu = kmPrevu;
    }

    public Integer getKmReel() {
        return kmReel;
    }

    public void setKmReel(Integer kmReel) {
        this.kmReel = kmReel;
    }

    public StatutMission getStatus() {
        return status;
    }

    public void setStatus(StatutMission status) {
        this.status = status;
    }

    public BigDecimal getCoutTotal() {
        return coutTotal;
    }

    public void setCoutTotal(BigDecimal coutTotal) {
        this.coutTotal = coutTotal;
    }

    public String getCircuitMission() {
        return circuitMission;
    }

    public void setCircuitMission(String circuitMission) {
        this.circuitMission = circuitMission;
    }

    public String getObservationMission() {
        return observationMission;
    }

    public void setObservationMission(String observationMission) {
        this.observationMission = observationMission;
    }

    public List<DepenseMissionDTO> getDepenses() {
        return depenses;
    }

    public void setDepenses(List<DepenseMissionDTO> depenses) {
        this.depenses = depenses;
    }

    @Override
    public String toString() {
        return libMission + " (" + (status != null ? status.getValeur() : "N/A") + ")";
    }
}