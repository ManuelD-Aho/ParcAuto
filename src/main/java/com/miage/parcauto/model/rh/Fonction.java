package main.java.com.miage.parcauto.model.rh;

import java.io.Serializable;
import java.util.Objects;

/**
 * Entité représentant une fonction professionnelle.
 */
public class Fonction implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idFonction;
    private String libFonction;

    public Fonction() {
    }

    public Fonction(Integer idFonction, String libFonction) {
        this.idFonction = idFonction;
        this.libFonction = libFonction;
    }

    // Getters et Setters
    public Integer getIdFonction() {
        return idFonction;
    }

    public void setIdFonction(Integer idFonction) {
        this.idFonction = idFonction;
    }

    public String getLibFonction() {
        return libFonction;
    }

    public void setLibFonction(String libFonction) {
        this.libFonction = libFonction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fonction fonction = (Fonction) o;
        return Objects.equals(idFonction, fonction.idFonction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFonction);
    }

    @Override
    public String toString() {
        return libFonction;
    }
}