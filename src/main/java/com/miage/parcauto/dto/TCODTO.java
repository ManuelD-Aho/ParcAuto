package main.java.com.miage.parcauto.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TCODTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal coutsTotaux;
    private Integer idVehicule;
    private String vehiculeInfo;
    private Integer kmActuels;
    private BigDecimal coutParKm;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private int nombreVehicules;
    private BigDecimal coutTotalAchats;
    private BigDecimal coutTotalEntretiens;
    private BigDecimal coutTotalCarburant;
    private BigDecimal coutTotalAssurances;
    private BigDecimal depreciationTotale;
    private BigDecimal tcoGlobalFlotte;
    private BigDecimal coutMoyenParKmFlotte;

    public TCODTO() {
    }

    public BigDecimal getCoutsTotaux() {
        return coutsTotaux;
    }

    public void setCoutsTotaux(BigDecimal coutsTotaux) {
        this.coutsTotaux = coutsTotaux;
    }

    public Integer getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(Integer idVehicule) {
        this.idVehicule = idVehicule;
    }

    public String getVehiculeInfo() {
        return vehiculeInfo;
    }

    public void setVehiculeInfo(String vehiculeInfo) {
        this.vehiculeInfo = vehiculeInfo;
    }

    public Integer getKmActuels() {
        return kmActuels;
    }

    public void setKmActuels(Integer kmActuels) {
        this.kmActuels = kmActuels;
    }

    public BigDecimal getCoutParKm() {
        return coutParKm;
    }

    public void setCoutParKm(BigDecimal coutParKm) {
        this.coutParKm = coutParKm;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public int getNombreVehicules() {
        return nombreVehicules;
    }

    public void setNombreVehicules(int nombreVehicules) {
        this.nombreVehicules = nombreVehicules;
    }

    public BigDecimal getCoutTotalAchats() {
        return coutTotalAchats;
    }

    public void setCoutTotalAchats(BigDecimal coutTotalAchats) {
        this.coutTotalAchats = coutTotalAchats;
    }

    public BigDecimal getCoutTotalEntretiens() {
        return coutTotalEntretiens;
    }

    public void setCoutTotalEntretiens(BigDecimal coutTotalEntretiens) {
        this.coutTotalEntretiens = coutTotalEntretiens;
    }

    public BigDecimal getCoutTotalCarburant() {
        return coutTotalCarburant;
    }

    public void setCoutTotalCarburant(BigDecimal coutTotalCarburant) {
        this.coutTotalCarburant = coutTotalCarburant;
    }

    public BigDecimal getCoutTotalAssurances() {
        return coutTotalAssurances;
    }

    public void setCoutTotalAssurances(BigDecimal coutTotalAssurances) {
        this.coutTotalAssurances = coutTotalAssurances;
    }

    public BigDecimal getDepreciationTotale() {
        return depreciationTotale;
    }

    public void setDepreciationTotale(BigDecimal depreciationTotale) {
        this.depreciationTotale = depreciationTotale;
    }

    public BigDecimal getTcoGlobalFlotte() {
        return tcoGlobalFlotte;
    }

    public void setTcoGlobalFlotte(BigDecimal tcoGlobalFlotte) {
        this.tcoGlobalFlotte = tcoGlobalFlotte;
    }

    public BigDecimal getCoutMoyenParKmFlotte() {
        return coutMoyenParKmFlotte;
    }

    public void setCoutMoyenParKmFlotte(BigDecimal coutMoyenParKmFlotte) {
        this.coutMoyenParKmFlotte = coutMoyenParKmFlotte;
    }

    @Override
    public String toString() {
        return "TCO pour " + (vehiculeInfo != null ? vehiculeInfo : "Véhicule ID " + idVehicule) + ": " + coutsTotaux
                + "€";
    }
}