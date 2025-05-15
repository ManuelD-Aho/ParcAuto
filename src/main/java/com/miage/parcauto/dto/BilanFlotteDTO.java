package main.java.com.miage.parcauto.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class BilanFlotteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int nombreTotalVehicules;
    private Map<String, Integer> vehiculesParEtat; // Libellé de l'état -> Nombre
    private Map<String, Integer> vehiculesParEnergie; // Valeur de l'énergie -> Nombre
    private int nombreVehiculesEnEntretien;
    private int nombreVehiculesEnMission;
    private BigDecimal valeurTotaleFlotte; // Basée sur prix d'acquisition ou valeur actuelle
    private double ageMoyenFlotteAnnees;
    private double kmMoyenParVehicule;
    private List<VehiculeDTO> vehiculesRecemmentAjoutes; // Optionnel
    private List<AlerteEntretienDTO> alertesEntretienActives; // Optionnel
    private List<AlerteAssuranceDTO> alertesAssuranceActives; // Optionnel

    public BilanFlotteDTO() {
    }

    public int getNombreTotalVehicules() {
        return nombreTotalVehicules;
    }

    public void setNombreTotalVehicules(int nombreTotalVehicules) {
        this.nombreTotalVehicules = nombreTotalVehicules;
    }

    public Map<String, Integer> getVehiculesParEtat() {
        return vehiculesParEtat;
    }

    public void setVehiculesParEtat(Map<String, Integer> vehiculesParEtat) {
        this.vehiculesParEtat = vehiculesParEtat;
    }

    public Map<String, Integer> getVehiculesParEnergie() {
        return vehiculesParEnergie;
    }

    public void setVehiculesParEnergie(Map<String, Integer> vehiculesParEnergie) {
        this.vehiculesParEnergie = vehiculesParEnergie;
    }

    public int getNombreVehiculesEnEntretien() {
        return nombreVehiculesEnEntretien;
    }

    public void setNombreVehiculesEnEntretien(int nombreVehiculesEnEntretien) {
        this.nombreVehiculesEnEntretien = nombreVehiculesEnEntretien;
    }

    public int getNombreVehiculesEnMission() {
        return nombreVehiculesEnMission;
    }

    public void setNombreVehiculesEnMission(int nombreVehiculesEnMission) {
        this.nombreVehiculesEnMission = nombreVehiculesEnMission;
    }

    public BigDecimal getValeurTotaleFlotte() {
        return valeurTotaleFlotte;
    }

    public void setValeurTotaleFlotte(BigDecimal valeurTotaleFlotte) {
        this.valeurTotaleFlotte = valeurTotaleFlotte;
    }

    public double getAgeMoyenFlotteAnnees() {
        return ageMoyenFlotteAnnees;
    }

    public void setAgeMoyenFlotteAnnees(double ageMoyenFlotteAnnees) {
        this.ageMoyenFlotteAnnees = ageMoyenFlotteAnnees;
    }

    public double getKmMoyenParVehicule() {
        return kmMoyenParVehicule;
    }

    public void setKmMoyenParVehicule(double kmMoyenParVehicule) {
        this.kmMoyenParVehicule = kmMoyenParVehicule;
    }

    public List<VehiculeDTO> getVehiculesRecemmentAjoutes() {
        return vehiculesRecemmentAjoutes;
    }

    public void setVehiculesRecemmentAjoutes(List<VehiculeDTO> vehiculesRecemmentAjoutes) {
        this.vehiculesRecemmentAjoutes = vehiculesRecemmentAjoutes;
    }

    public List<AlerteEntretienDTO> getAlertesEntretienActives() {
        return alertesEntretienActives;
    }

    public void setAlertesEntretienActives(List<AlerteEntretienDTO> alertesEntretienActives) {
        this.alertesEntretienActives = alertesEntretienActives;
    }

    public List<AlerteAssuranceDTO> getAlertesAssuranceActives() {
        return alertesAssuranceActives;
    }

    public void setAlertesAssuranceActives(List<AlerteAssuranceDTO> alertesAssuranceActives) {
        this.alertesAssuranceActives = alertesAssuranceActives;
    }

    @Override
    public String toString() {
        return "BilanFlotteDTO{" +
                "nombreTotalVehicules=" + nombreTotalVehicules +
                ", valeurTotaleFlotte=" + valeurTotaleFlotte +
                '}';
    }
}