package main.java.com.miage.parcauto.dto;

import main.java.com.miage.parcauto.model.entretien.StatutOT;
import main.java.com.miage.parcauto.model.entretien.TypeEntretien;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EntretienDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idEntretien;
    private Integer idVehicule;
    private String vehiculeInfo;
    private LocalDateTime dateEntreeEntr;
    private LocalDateTime dateSortieEntr;
    private String motifEntr;
    private String observation;
    private BigDecimal coutEntr;
    private String lieuEntr;
    private TypeEntretien type;
    private StatutOT statutOt;

    public EntretienDTO() {
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

    public String getVehiculeInfo() {
        return vehiculeInfo;
    }

    public void setVehiculeInfo(String vehiculeInfo) {
        this.vehiculeInfo = vehiculeInfo;
    }

    public LocalDateTime getDateEntreeEntr() {
        return dateEntreeEntr;
    }

    public void setDateEntreeEntr(LocalDateTime dateEntreeEntr) {
        this.dateEntreeEntr = dateEntreeEntr;
    }

    public LocalDateTime getDateSortieEntr() {
        return dateSortieEntr;
    }

    public void setDateSortieEntr(LocalDateTime dateSortieEntr) {
        this.dateSortieEntr = dateSortieEntr;
    }

    public String getMotifEntr() {
        return motifEntr;
    }

    public void setMotifEntr(String motifEntr) {
        this.motifEntr = motifEntr;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public BigDecimal getCoutEntr() {
        return coutEntr;
    }

    public void setCoutEntr(BigDecimal coutEntr) {
        this.coutEntr = coutEntr;
    }

    public String getLieuEntr() {
        return lieuEntr;
    }

    public void setLieuEntr(String lieuEntr) {
        this.lieuEntr = lieuEntr;
    }

    public TypeEntretien getType() {
        return type;
    }

    public void setType(TypeEntretien type) {
        this.type = type;
    }

    public StatutOT getStatutOt() {
        return statutOt;
    }

    public void setStatutOt(StatutOT statutOt) {
        this.statutOt = statutOt;
    }

    @Override
    public String toString() {
        return "Entretien #" + idEntretien + " pour " + vehiculeInfo + " (" + (statutOt != null ? statutOt.getValeur() : "N/A") + ")";
    }
}