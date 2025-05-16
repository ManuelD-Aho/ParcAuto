package main.java.com.miage.parcauto.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MissionDTO {
    private Integer idMission;
    private Integer idVehicule;
    private String libelle;
    private String siteDestination;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFinPrevue;
    private LocalDateTime dateFinEffective;
    private String statut;
    private BigDecimal coutEstime;
    private BigDecimal coutTotalReel;
    private String circuit;
    private String observations;

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

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getSiteDestination() {
        return siteDestination;
    }

    public void setSiteDestination(String siteDestination) {
        this.siteDestination = siteDestination;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFinPrevue() {
        return dateFinPrevue;
    }

    public void setDateFinPrevue(LocalDateTime dateFinPrevue) {
        this.dateFinPrevue = dateFinPrevue;
    }

    public LocalDateTime getDateFinEffective() {
        return dateFinEffective;
    }

    public void setDateFinEffective(LocalDateTime dateFinEffective) {
        this.dateFinEffective = dateFinEffective;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public BigDecimal getCoutEstime() {
        return coutEstime;
    }

    public void setCoutEstime(BigDecimal coutEstime) {
        this.coutEstime = coutEstime;
    }

    public BigDecimal getCoutTotalReel() {
        return coutTotalReel;
    }

    public void setCoutTotalReel(BigDecimal coutTotalReel) {
        this.coutTotalReel = coutTotalReel;
    }

    public String getCircuit() {
        return circuit;
    }

    public void setCircuit(String circuit) {
        this.circuit = circuit;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}