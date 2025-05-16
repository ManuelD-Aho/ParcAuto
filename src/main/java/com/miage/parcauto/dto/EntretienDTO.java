package main.java.com.miage.parcauto.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EntretienDTO {
    private Integer idEntretien;
    private Integer idVehicule;
    private LocalDateTime dateEntree;
    private LocalDateTime dateSortie;
    private String motif;
    private String observations;
    private BigDecimal coutEstime;
    private BigDecimal coutReel;
    private String lieu;
    private String typeEntretien;
    private String statutOT;

    public EntretienDTO() {
    }

    public EntretienDTO(Integer idEntretien, Integer idVehicule, java.time.LocalDateTime dateEntree,
            java.time.LocalDateTime dateSortie, String motif, String observations, java.math.BigDecimal coutEstime,
            java.math.BigDecimal coutReel, String lieu, String typeEntretien, String statutOT) {
        this.idEntretien = idEntretien;
        this.idVehicule = idVehicule;
        this.dateEntree = dateEntree;
        this.dateSortie = dateSortie;
        this.motif = motif;
        this.observations = observations;
        this.coutEstime = coutEstime;
        this.coutReel = coutReel;
        this.lieu = lieu;
        this.typeEntretien = typeEntretien;
        this.statutOT = statutOT;
    }

    public Integer getIdEntretien() {
        return idEntretien;
    }

    public void setIdEntretien(Integer idEntretien) {
        this.idEntretien = idEntretien;
    }

    public Integer getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(Integer idVehicule) {
        this.idVehicule = idVehicule;
    }

    public LocalDateTime getDateEntree() {
        return dateEntree;
    }

    public void setDateEntree(LocalDateTime dateEntree) {
        this.dateEntree = dateEntree;
    }

    public LocalDateTime getDateSortie() {
        return dateSortie;
    }

    public void setDateSortie(LocalDateTime dateSortie) {
        this.dateSortie = dateSortie;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public BigDecimal getCoutEstime() {
        return coutEstime;
    }

    public void setCoutEstime(BigDecimal coutEstime) {
        this.coutEstime = coutEstime;
    }

    public BigDecimal getCoutReel() {
        return coutReel;
    }

    public void setCoutReel(BigDecimal coutReel) {
        this.coutReel = coutReel;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getTypeEntretien() {
        return typeEntretien;
    }

    public void setTypeEntretien(String typeEntretien) {
        this.typeEntretien = typeEntretien;
    }

    public String getStatutOT() {
        return statutOT;
    }

    public void setStatutOT(String statutOT) {
        this.statutOT = statutOT;
    }
}