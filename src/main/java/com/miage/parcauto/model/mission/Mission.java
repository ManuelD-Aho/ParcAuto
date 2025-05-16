package main.java.com.miage.parcauto.model.mission;

import java.math.BigDecimal;
import java.time.LocalDateTime;
// import main.java.com.miage.parcauto.model.mission.StatutMission;


public class Mission {

    private Integer idMission;
    private Integer idVehicule;
    private String libelle;           // SQL: lib_mission
    private String siteDestination;  // SQL: site
    private LocalDateTime dateDebut; // SQL: date_debut_mission
    private LocalDateTime dateFin;   // SQL: date_fin_mission (sera mappé à dateFinPrevue ou dateFinEffective dans DTO)
    private Integer kmPrevu;
    private Integer kmReel;
    private StatutMission statut;     // SQL: status
    private BigDecimal coutTotal;     // SQL: cout_total (sera mappé à coutTotalReel dans DTO)
    private String circuit;           // SQL: circuit_mission
    private String observations;      // SQL: observation_mission

    public Mission() {
    }

    public Integer getIdMission() {
        return idMission;
    }

    public void setIdMission(Integer idMission) {
        this.idMission = idMission;
    }

    public Integer getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(Integer idVehicule) {
        this.idVehicule = idVehicule;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getSiteDestination() {
        return siteDestination;
    }

    public void setSiteDestination(String siteDestination) {
        this.siteDestination = siteDestination;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
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

    public StatutMission getStatut() {
        return statut;
    }

    public void setStatut(StatutMission statut) {
        this.statut = statut;
    }

    public BigDecimal getCoutTotal() {
        return coutTotal;
    }

    public void setCoutTotal(BigDecimal coutTotal) {
        this.coutTotal = coutTotal;
    }

    public String getCircuit() {
        return circuit;
    }

    public void setCircuit(String circuit) {
        this.circuit = circuit;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}