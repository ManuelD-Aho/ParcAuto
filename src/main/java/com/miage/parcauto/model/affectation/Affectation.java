package main.java.com.miage.parcauto.model.affectation;

import main.java.com.miage.parcauto.model.finance.SocietaireCompte;
import main.java.com.miage.parcauto.model.rh.Personnel;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entité représentant une affectation de véhicule.
 */
public class Affectation implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Vehicule vehicule;
    private Personnel personnel;  // Personnel à qui le véhicule est affecté (facultatif)
    private SocietaireCompte societaire;  // Sociétaire à qui le véhicule est affecté (facultatif)
    private TypeAffectation type;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;  // Peut être null si l'affectation est en cours

    public Affectation() {
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    public Personnel getPersonnel() {
        return personnel;
    }

    public void setPersonnel(Personnel personnel) {
        this.personnel = personnel;
    }

    public SocietaireCompte getSocietaire() {
        return societaire;
    }

    public void setSocietaire(SocietaireCompte societaire) {
        this.societaire = societaire;
    }

    public TypeAffectation getType() {
        return type;
    }

    public void setType(TypeAffectation type) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Affectation that = (Affectation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        String beneficiaire = personnel != null ? personnel.toString() :
                (societaire != null ? societaire.toString() : "Inconnu");
        return "Affectation de " + vehicule + " à " + beneficiaire;
    }
}