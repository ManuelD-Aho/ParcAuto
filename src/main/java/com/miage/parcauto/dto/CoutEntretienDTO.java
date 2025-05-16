package main.java.com.miage.parcauto.dto;

import main.java.com.miage.parcauto.model.entretien.TypeEntretien;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class CoutEntretienDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idVehicule; // Optionnel, si c'est pour un véhicule spécifique
    private String vehiculeInfo; // Optionnel
    private LocalDate periodeDebut;
    private LocalDate periodeFin;
    private BigDecimal coutTotalEntretiens;
    private Map<TypeEntretien, BigDecimal> coutParTypeEntretien;
    private int nombreEntretiens;
    private BigDecimal coutMoyenParEntretien;

    private String immatriculationVehicule;
    private int annee;
    private BigDecimal totalCoutsEntretien;

    public CoutEntretienDTO() {
        this.coutTotalEntretiens = BigDecimal.ZERO;
        this.coutMoyenParEntretien = BigDecimal.ZERO;
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

    public LocalDate getPeriodeDebut() {
        return periodeDebut;
    }

    public void setPeriodeDebut(LocalDate periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public LocalDate getPeriodeFin() {
        return periodeFin;
    }

    public void setPeriodeFin(LocalDate periodeFin) {
        this.periodeFin = periodeFin;
    }

    public BigDecimal getCoutTotalEntretiens() {
        return coutTotalEntretiens;
    }

    public void setCoutTotalEntretiens(BigDecimal coutTotalEntretiens) {
        this.coutTotalEntretiens = coutTotalEntretiens;
    }

    public Map<TypeEntretien, BigDecimal> getCoutParTypeEntretien() {
        return coutParTypeEntretien;
    }

    public void setCoutParTypeEntretien(Map<TypeEntretien, BigDecimal> coutParTypeEntretien) {
        this.coutParTypeEntretien = coutParTypeEntretien;
    }

    public int getNombreEntretiens() {
        return nombreEntretiens;
    }

    public void setNombreEntretiens(int nombreEntretiens) {
        this.nombreEntretiens = nombreEntretiens;
    }

    public BigDecimal getCoutMoyenParEntretien() {
        return coutMoyenParEntretien;
    }

    public void setCoutMoyenParEntretien(BigDecimal coutMoyenParEntretien) {
        this.coutMoyenParEntretien = coutMoyenParEntretien;
    }

    public String getImmatriculationVehicule() {
        return immatriculationVehicule;
    }

    public void setImmatriculationVehicule(String immatriculationVehicule) {
        this.immatriculationVehicule = immatriculationVehicule;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public BigDecimal getTotalCoutsEntretien() {
        return totalCoutsEntretien;
    }

    public void setTotalCoutsEntretien(BigDecimal totalCoutsEntretien) {
        this.totalCoutsEntretien = totalCoutsEntretien;
    }

    @Override
    public String toString() {
        return "CoutEntretienDTO{" +
                "vehiculeInfo='" + (vehiculeInfo != null ? vehiculeInfo : "Global") + '\'' +
                ", periodeDebut=" + periodeDebut +
                ", periodeFin=" + periodeFin +
                ", coutTotalEntretiens=" + coutTotalEntretiens +
                '}';
    }
}