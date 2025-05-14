package main.java.com.miage.parcauto.dto;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MouvementDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer idMouvement;
    private Integer idCompteSocietaire; // Renamed for clarity, maps to SOCIETAIRE_COMPTE.id_societaire
    private String numeroCompteSocietaire;
    private String libelleCompteSocietaire;
    private LocalDateTime dateMouvement; // Changed to LocalDateTime to match DB's datetime
    private BigDecimal montant;
    private String libelleMouvement;
    private String typeMouvement; // e.g., Depot, Retrait, Mensualite from DB Enum
    private Integer idMission;
    private String referenceMission;
    private Integer idEntretien;
    private String referenceEntretien;
    private String categorieMouvement;

    public MouvementDTO() {
    }

    public MouvementDTO(Integer idMouvement, Integer idCompteSocietaire, String numeroCompteSocietaire, String libelleCompteSocietaire,
                        LocalDateTime dateMouvement, BigDecimal montant, String libelleMouvement, String typeMouvement,
                        Integer idMission, String referenceMission, Integer idEntretien,
                        String referenceEntretien, String categorieMouvement) {
        this.idMouvement = idMouvement;
        this.idCompteSocietaire = idCompteSocietaire;
        this.numeroCompteSocietaire = numeroCompteSocietaire;
        this.libelleCompteSocietaire = libelleCompteSocietaire;
        this.dateMouvement = dateMouvement;
        this.montant = montant;
        this.libelleMouvement = libelleMouvement;
        this.typeMouvement = typeMouvement;
        this.idMission = idMission;
        this.referenceMission = referenceMission;
        this.idEntretien = idEntretien;
        this.referenceEntretien = referenceEntretien;
        this.categorieMouvement = categorieMouvement;
    }

    public Integer getIdMouvement() {
        return idMouvement;
    }

    public void setIdMouvement(Integer idMouvement) {
        this.idMouvement = idMouvement;
    }

    public Integer getIdCompteSocietaire() {
        return idCompteSocietaire;
    }

    public void setIdCompteSocietaire(Integer idCompteSocietaire) {
        this.idCompteSocietaire = idCompteSocietaire;
    }

    public String getNumeroCompteSocietaire() {
        return numeroCompteSocietaire;
    }

    public void setNumeroCompteSocietaire(String numeroCompteSocietaire) {
        this.numeroCompteSocietaire = numeroCompteSocietaire;
    }

    public String getLibelleCompteSocietaire() {
        return libelleCompteSocietaire;
    }

    public void setLibelleCompteSocietaire(String libelleCompteSocietaire) {
        this.libelleCompteSocietaire = libelleCompteSocietaire;
    }

    public LocalDateTime getDateMouvement() {
        return dateMouvement;
    }

    public void setDateMouvement(LocalDateTime dateMouvement) {
        this.dateMouvement = dateMouvement;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public String getLibelleMouvement() {
        return libelleMouvement;
    }

    public void setLibelleMouvement(String libelleMouvement) {
        this.libelleMouvement = libelleMouvement;
    }

    public String getTypeMouvement() {
        return typeMouvement;
    }

    public void setTypeMouvement(String typeMouvement) {
        this.typeMouvement = typeMouvement;
    }

    public Integer getIdMission() {
        return idMission;
    }

    public void setIdMission(Integer idMission) {
        this.idMission = idMission;
    }

    public String getReferenceMission() {
        return referenceMission;
    }

    public void setReferenceMission(String referenceMission) {
        this.referenceMission = referenceMission;
    }

    public Integer getIdEntretien() {
        return idEntretien;
    }

    public void setIdEntretien(Integer idEntretien) {
        this.idEntretien = idEntretien;
    }

    public String getReferenceEntretien() {
        return referenceEntretien;
    }

    public void setReferenceEntretien(String referenceEntretien) {
        this.referenceEntretien = referenceEntretien;
    }

    public String getCategorieMouvement() {
        return categorieMouvement;
    }

    public void setCategorieMouvement(String categorieMouvement) {
        this.categorieMouvement = categorieMouvement;
    }

    @Override
    public String toString() {
        return "MouvementDTO{" +
                "idMouvement=" + idMouvement +
                ", idCompteSocietaire=" + idCompteSocietaire +
                ", numeroCompteSocietaire='" + numeroCompteSocietaire + '\'' +
                ", dateMouvement=" + dateMouvement +
                ", montant=" + (montant != null ? montant.toPlainString() + " FCFA" : "N/A") +
                ", libelleMouvement='" + libelleMouvement + '\'' +
                ", typeMouvement='" + typeMouvement + '\'' +
                ", categorieMouvement='" + categorieMouvement + '\'' +
                '}';
    }
}