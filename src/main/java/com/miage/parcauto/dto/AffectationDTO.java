package main.java.com.miage.parcauto.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO pour l'entité Affectation.
 */
public class AffectationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer idVehicule;
    private String vehiculeInfo;
    private Integer idPersonnel;
    private String personnelInfo;
    private Integer idSocietaire;
    private String societaireInfo;
    private String type;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;

    public AffectationDTO() {
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getIdPersonnel() {
        return idPersonnel;
    }

    public void setIdPersonnel(Integer idPersonnel) {
        this.idPersonnel = idPersonnel;
    }

    public String getPersonnelInfo() {
        return personnelInfo;
    }

    public void setPersonnelInfo(String personnelInfo) {
        this.personnelInfo = personnelInfo;
    }

    public Integer getIdSocietaire() {
        return idSocietaire;
    }

    public void setIdSocietaire(Integer idSocietaire) {
        this.idSocietaire = idSocietaire;
    }

    public String getSocietaireInfo() {
        return societaireInfo;
    }

    public void setSocietaireInfo(String societaireInfo) {
        this.societaireInfo = societaireInfo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @Override
    public String toString() {
        String beneficiaire = personnelInfo != null ? personnelInfo :
                (societaireInfo != null ? societaireInfo : "Inconnu");
        return "Affectation de " + vehiculeInfo + " à " + beneficiaire;
    }
}