package main.java.com.miage.parcauto.model.assurance;

import main.java.com.miage.parcauto.model.vehicule.Vehicule;

import java.io.Serializable;
import java.util.Objects;

/**
 * Entité représentant la relation entre un véhicule et son assurance.
 */
public class Couvrir implements Serializable {

    private static final long serialVersionUID = 1L;

    private Vehicule vehicule;
    private Assurance assurance;

    public Couvrir() {
    }

    public Couvrir(Vehicule vehicule, Assurance assurance) {
        this.vehicule = vehicule;
        this.assurance = assurance;
    }

    // Getters et Setters
    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    public Assurance getAssurance() {
        return assurance;
    }

    public void setAssurance(Assurance assurance) {
        this.assurance = assurance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Couvrir couvrir = (Couvrir) o;
        return Objects.equals(vehicule.getIdVehicule(), couvrir.vehicule.getIdVehicule()) &&
                Objects.equals(assurance.getNumCarteAssurance(), couvrir.assurance.getNumCarteAssurance());
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicule.getIdVehicule(), assurance.getNumCarteAssurance());
    }

    @Override
    public String toString() {
        return "Couverture de " + vehicule + " par " + assurance;
    }
}