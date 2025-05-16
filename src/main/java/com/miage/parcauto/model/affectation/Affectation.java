package main.java.com.miage.parcauto.model.affectation;

import java.time.LocalDateTime;
// import main.java.com.miage.parcauto.model.affectation.TypeAffectation;

public class Affectation {

    private Integer id;
    private Integer idVehicule;
    private Integer idPersonnel;
    private Integer idSocietaire;
    private TypeAffectation typeAffectation; // SQL: type
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;

    public Affectation() {
    }

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

    public Integer getIdPersonnel() {
        return idPersonnel;
    }

    public void setIdPersonnel(Integer idPersonnel) {
        this.idPersonnel = idPersonnel;
    }

    public Integer getIdSocietaire() {
        return idSocietaire;
    }

    public void setIdSocietaire(Integer idSocietaire) {
        this.idSocietaire = idSocietaire;
    }

    public TypeAffectation getTypeAffectation() {
        return typeAffectation;
    }

    public void setTypeAffectation(TypeAffectation typeAffectation) {
        this.typeAffectation = typeAffectation;
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
}