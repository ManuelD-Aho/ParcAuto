package main.java.com.miage.parcauto.dto;

public class BilanFlotteDTO {
    private int nombreTotalVehicules;
    private int nombreVehiculesDisponibles;
    private int nombreVehiculesEnMission;
    private int nombreVehiculesEnEntretien;
    private int nombreVehiculesHorsService;
    private int nombreVehiculesEnMaintenance;

    public BilanFlotteDTO() {
    }

    public int getNombreTotalVehicules() {
        return nombreTotalVehicules;
    }

    public void setNombreTotalVehicules(int nombreTotalVehicules) {
        this.nombreTotalVehicules = nombreTotalVehicules;
    }

    public int getNombreVehiculesDisponibles() {
        return nombreVehiculesDisponibles;
    }

    public void setNombreVehiculesDisponibles(int nombreVehiculesDisponibles) {
        this.nombreVehiculesDisponibles = nombreVehiculesDisponibles;
    }

    public int getNombreVehiculesEnMission() {
        return nombreVehiculesEnMission;
    }

    public void setNombreVehiculesEnMission(int nombreVehiculesEnMission) {
        this.nombreVehiculesEnMission = nombreVehiculesEnMission;
    }

    public int getNombreVehiculesEnEntretien() {
        return nombreVehiculesEnEntretien;
    }

    public void setNombreVehiculesEnEntretien(int nombreVehiculesEnEntretien) {
        this.nombreVehiculesEnEntretien = nombreVehiculesEnEntretien;
    }

    public int getNombreVehiculesHorsService() {
        return nombreVehiculesHorsService;
    }

    public void setNombreVehiculesHorsService(int nombreVehiculesHorsService) {
        this.nombreVehiculesHorsService = nombreVehiculesHorsService;
    }

    public int getNombreVehiculesEnMaintenance() {
        return nombreVehiculesEnMaintenance;
    }

    public void setNombreVehiculesEnMaintenance(int nombreVehiculesEnMaintenance) {
        this.nombreVehiculesEnMaintenance = nombreVehiculesEnMaintenance;
    }
}