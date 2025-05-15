package main.java.com.miage.parcauto.dto;

import main.java.com.miage.parcauto.model.mission.NatureDepenseMission;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class DepenseMissionDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer idDepense;
    private Integer idMission;
    private NatureDepenseMission nature;
    private String description;
    private BigDecimal montant;
    private LocalDate date;
    private String justificatif;

    public DepenseMissionDTO() {
        this.montant = BigDecimal.ZERO;
        this.date = LocalDate.now();
    }

    public DepenseMissionDTO(Integer idDepense, Integer idMission, NatureDepenseMission nature, String description, BigDecimal montant,
                             LocalDate date, String justificatif) {
        this.idDepense = idDepense;
        this.idMission = idMission;
        this.nature = nature;
        this.description = description;
        this.montant = montant;
        this.date = date;
        this.justificatif = justificatif;
    }

    public boolean hasJustificatif() {
        return justificatif != null && !justificatif.isEmpty();
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

    public NatureDepenseMission getNature() {
        return nature;
    }

    public void setNature(NatureDepenseMission nature) {
        this.nature = nature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getJustificatif() {
        return justificatif;
    }

    public void setJustificatif(String justificatif) {
        this.justificatif = justificatif;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DepenseMissionDTO that = (DepenseMissionDTO) o;
        return Objects.equals(idDepense, that.idDepense);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDepense);
    }

    @Override
    public String toString() {
        return "DepenseMissionDTO{" +
                "idDepense=" + idDepense +
                ", nature=" + (nature != null ? nature.getValeur() : "N/A") +
                ", montant=" + montant +
                ", date=" + (date != null ? date.toString() : "N/A") +
                '}';
    }
}