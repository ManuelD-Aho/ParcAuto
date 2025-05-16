package main.java.com.miage.parcauto.model.mission;

import java.math.BigDecimal;
// import main.java.com.miage.parcauto.model.mission.NatureDepenseMission;
import java.time.LocalDateTime; // Ajout pour `dateDepense` si nécessaire

public class DepenseMission {

    private Integer id;
    private Integer idMission;
    private NatureDepenseMission nature;
    private BigDecimal montant;
    private String justificatif;
    private LocalDateTime dateDepense; // Ajouté car utilisé par ValidationServiceImpl
    private Integer idDepense;
    private String observation;

    public DepenseMission() {
    }

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

    public NatureDepenseMission getNature() {
        return nature;
    }

    public void setNature(NatureDepenseMission nature) {
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

    public LocalDateTime getDateDepense() {
        return dateDepense;
    }

    public void setDateDepense(LocalDateTime dateDepense) {
        this.dateDepense = dateDepense;
    }

    public Integer getIdDepense() {
        return idDepense != null ? idDepense : id;
    }

    public void setIdDepense(Integer idDepense) {
        this.idDepense = idDepense;
        this.id = idDepense;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}