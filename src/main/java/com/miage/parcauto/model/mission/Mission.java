package main.java.com.miage.parcauto.model.mission;

import main.java.com.miage.parcauto.model.vehicule.Vehicule;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entité représentant une mission.
 */
public class Mission implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idMission;
    private Vehicule vehicule;
    private String libMission;
    private String site;
    private LocalDateTime dateDebutMission;
    private LocalDateTime dateFinMission;
    private Integer kmPrevu;
    private Integer kmReel;
    private StatutMission status;
    private BigDecimal coutTotal;
    private String circuitMission;
    private String observationMission;
    private List<DepenseMission> depenses = new ArrayList<>();

    public Mission() {
        this.status = StatutMission.PLANIFIEE;
    }

    // Getters et Setters
    public Integer getIdMission() {
        return idMission;
    }

    public void setIdMission(Integer idMission) {
        this.idMission = idMission;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    public String getLibMission() {
        return libMission;
    }

    public void setLibMission(String libMission) {
        this.libMission = libMission;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public LocalDateTime getDateDebutMission() {
        return dateDebutMission;
    }

    public void setDateDebutMission(LocalDateTime dateDebutMission) {
        this.dateDebutMission = dateDebutMission;
    }

    public LocalDateTime getDateFinMission() {
        return dateFinMission;
    }

    public void setDateFinMission(LocalDateTime dateFinMission) {
        this.dateFinMission = dateFinMission;
    }

    public Integer getKmPrevu() {
        return kmPrevu;
    }

    public void setKmPrevu(Integer kmPrevu) {
        this.kmPrevu = kmPrevu;
    }

    public Integer getKmReel() {
        return kmReel;
    }

    public void setKmReel(Integer kmReel) {
        this.kmReel = kmReel;
    }

    public StatutMission getStatus() {
        return status;
    }

    public void setStatus(StatutMission status) {
        this.status = status;
    }

    public BigDecimal getCoutTotal() {
        return coutTotal;
    }

    public void setCoutTotal(BigDecimal coutTotal) {
        this.coutTotal = coutTotal;
    }

    public String getCircuitMission() {
        return circuitMission;
    }

    public void setCircuitMission(String circuitMission) {
        this.circuitMission = circuitMission;
    }

    public String getObservationMission() {
        return observationMission;
    }

    public void setObservationMission(String observationMission) {
        this.observationMission = observationMission;
    }

    public List<DepenseMission> getDepenses() {
        return depenses;
    }

    public void setDepenses(List<DepenseMission> depenses) {
        this.depenses = depenses;
    }

    /**
     * Ajoute une dépense à la mission et met à jour le coût total.
     * @param depense La dépense à ajouter
     */
    public void addDepense(DepenseMission depense) {
        if (depenses == null) {
            depenses = new ArrayList<>();
        }
        depenses.add(depense);

        // Met à jour le coût total
        if (coutTotal == null) {
            coutTotal = depense.getMontant();
        } else {
            coutTotal = coutTotal.add(depense.getMontant());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mission mission = (Mission) o;
        return Objects.equals(idMission, mission.idMission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMission);
    }

    @Override
    public String toString() {
        return libMission + " (" + status + ")";
    }
}