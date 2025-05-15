package main.java.com.miage.parcauto.model.vehicule;

import java.io.Serializable;
import java.util.Objects;

/**
 * Entité représentant l'état d'un véhicule.
 */
public class EtatVoiture implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idEtatVoiture;
    private String libEtatVoiture;

    public EtatVoiture() {
    }

    public EtatVoiture(Integer idEtatVoiture, String libEtatVoiture) {
        this.idEtatVoiture = idEtatVoiture;
        this.libEtatVoiture = libEtatVoiture;
    }

    public Integer getIdEtatVoiture() {
        return idEtatVoiture;
    }

    public void setIdEtatVoiture(Integer idEtatVoiture) {
        this.idEtatVoiture = idEtatVoiture;
    }

    public String getLibEtatVoiture() {
        return libEtatVoiture;
    }

    public void setLibEtatVoiture(String libEtatVoiture) {
        this.libEtatVoiture = libEtatVoiture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EtatVoiture that = (EtatVoiture) o;
        return Objects.equals(idEtatVoiture, that.idEtatVoiture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEtatVoiture);
    }

    @Override
    public String toString() {
        return libEtatVoiture;
    }
}