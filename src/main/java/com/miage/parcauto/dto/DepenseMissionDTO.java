package main.java.com.miage.parcauto.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO pour l'entité DepenseMission.
 */
public class DepenseMissionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer idMission;
    private String nature;
    private BigDecimal montant;
    private String justificatif;

    public DepenseMissionDTO() {
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdMission() {
        return idMission;
    }

    public void setIdMission(Integer idMission) {
        this.idMission = idMission;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public String getJustificatif() {
        return justificatif;
    }

    public void setJustificatif(String justificatif) {
        this.justificatif = justificatif;
    }

    @Override
    public String toString() {
        return nature + ": " + montant + "€";
    }
}