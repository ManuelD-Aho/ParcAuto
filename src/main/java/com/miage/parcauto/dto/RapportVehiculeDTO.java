package main.java.com.miage.parcauto.dto;

import java.util.List;

public class RapportVehiculeDTO {
    private VehiculeDTO vehicule;
    private List<EntretienDTO> entretiens;
    private List<MissionDTO> missions;

    public RapportVehiculeDTO() {
    }

    public VehiculeDTO getVehicule() {
        return vehicule;
    }

    public void setVehicule(VehiculeDTO vehicule) {
        this.vehicule = vehicule;
    }

    public List<EntretienDTO> getEntretiens() {
        return entretiens;
    }

    public void setEntretiens(List<EntretienDTO> entretiens) {
        this.entretiens = entretiens;
    }

    public List<MissionDTO> getMissions() {
        return missions;
    }

    public void setMissions(List<MissionDTO> missions) {
        this.missions = missions;
    }
}