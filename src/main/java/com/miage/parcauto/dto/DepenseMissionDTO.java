package main.java.com.miage.parcauto.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DepenseMissionDTO {
    private Integer idDepense;
    private Integer idMission;
    private String natureDepense;
    private BigDecimal montant;
    private String justificatifPath;
    private LocalDateTime dateDepense;

    public DepenseMissionDTO() {
    }

    public Integer getIdDepense() {
        return idDepense;
    }

    public void setIdDepense(Integer idDepense) {
        this.idDepense = idDepense;
    }

    public Integer getIdMission() {
        return idMission;
    }

    public void setIdMission(Integer idMission) {
        this.idMission = idMission;
    }

    public String getNatureDepense() {
        return natureDepense;
    }

    public void setNatureDepense(String natureDepense) {
        this.natureDepense = natureDepense;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public String getJustificatifPath() {
        return justificatifPath;
    }

    public void setJustificatifPath(String justificatifPath) {
        this.justificatifPath = justificatifPath;
    }

    public LocalDateTime getDateDepense() {
        return dateDepense;
    }

    public void setDateDepense(LocalDateTime dateDepense) {
        this.dateDepense = dateDepense;
    }
}