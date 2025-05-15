package main.java.com.miage.parcauto.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class AlerteAssuranceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer numCarteAssurance;
    private String agenceAssurance;
    private LocalDateTime dateFinAssurance;
    private long joursRestants;
    private List<String> vehiculesConcernesInfo; // Liste de "Marque Modèle (Immatriculation)"

    public AlerteAssuranceDTO() {
    }

    public Integer getNumCarteAssurance() {
        return numCarteAssurance;
    }

    public void setNumCarteAssurance(Integer numCarteAssurance) {
        this.numCarteAssurance = numCarteAssurance;
    }

    public String getAgenceAssurance() {
        return agenceAssurance;
    }

    public void setAgenceAssurance(String agenceAssurance) {
        this.agenceAssurance = agenceAssurance;
    }

    public LocalDateTime getDateFinAssurance() {
        return dateFinAssurance;
    }

    public void setDateFinAssurance(LocalDateTime dateFinAssurance) {
        this.dateFinAssurance = dateFinAssurance;
    }

    public long getJoursRestants() {
        return joursRestants;
    }

    public void setJoursRestants(long joursRestants) {
        this.joursRestants = joursRestants;
    }

    public List<String> getVehiculesConcernesInfo() {
        return vehiculesConcernesInfo;
    }

    public void setVehiculesConcernesInfo(List<String> vehiculesConcernesInfo) {
        this.vehiculesConcernesInfo = vehiculesConcernesInfo;
    }

    @Override
    public String toString() {
        return "AlerteAssuranceDTO{" +
                "numCarteAssurance=" + numCarteAssurance +
                ", agence='" + agenceAssurance + '\'' +
                ", dateFin=" + dateFinAssurance +
                ", joursRestants=" + joursRestants +
                '}';
    }
}