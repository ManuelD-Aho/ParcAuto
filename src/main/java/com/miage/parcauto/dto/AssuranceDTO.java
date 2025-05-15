package main.java.com.miage.parcauto.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AssuranceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer numCarteAssurance;
    private LocalDateTime dateDebutAssurance;
    private LocalDateTime dateFinAssurance;
    private String agence;
    private BigDecimal coutAssurance;
    private List<Integer> idVehiculesCouvert = new ArrayList<>();
    private List<String> vehiculesInfos = new ArrayList<>();

    public AssuranceDTO() {
    }

    public Integer getNumCarteAssurance() {
        return numCarteAssurance;
    }

    public void setNumCarteAssurance(Integer numCarteAssurance) {
        this.numCarteAssurance = numCarteAssurance;
    }

    public LocalDateTime getDateDebutAssurance() {
        return dateDebutAssurance;
    }

    public void setDateDebutAssurance(LocalDateTime dateDebutAssurance) {
        this.dateDebutAssurance = dateDebutAssurance;
    }

    public LocalDateTime getDateFinAssurance() {
        return dateFinAssurance;
    }

    public void setDateFinAssurance(LocalDateTime dateFinAssurance) {
        this.dateFinAssurance = dateFinAssurance;
    }

    public String getAgence() {
        return agence;
    }

    public void setAgence(String agence) {
        this.agence = agence;
    }

    public BigDecimal getCoutAssurance() {
        return coutAssurance;
    }

    public void setCoutAssurance(BigDecimal coutAssurance) {
        this.coutAssurance = coutAssurance;
    }

    public List<Integer> getIdVehiculesCouvert() {
        return idVehiculesCouvert;
    }

    public void setIdVehiculesCouvert(List<Integer> idVehiculesCouvert) {
        this.idVehiculesCouvert = idVehiculesCouvert;
    }

    public List<String> getVehiculesInfos() {
        return vehiculesInfos;
    }

    public void setVehiculesInfos(List<String> vehiculesInfos) {
        this.vehiculesInfos = vehiculesInfos;
    }

    @Override
    public String toString() {
        return "AssuranceDTO{" +
                "numCarteAssurance=" + numCarteAssurance +
                ", agence='" + agence + '\'' +
                ", dateFinAssurance=" + dateFinAssurance +
                ", vehiculesCouvert=" + (vehiculesInfos != null ? vehiculesInfos.size() : 0) +
                '}';
    }
}