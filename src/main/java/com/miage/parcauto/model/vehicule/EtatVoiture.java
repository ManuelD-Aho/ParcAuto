package main.java.com.miage.parcauto.model.vehicule;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Entité représentant l'état d'un véhicule.
 * Correspond à un enregistrement de la table ETAT_VOITURE.
 */
public class EtatVoiture implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer idEtatVoiture;
    private String libEtatVoiture;

    /**
     * Constructeur par défaut.
     */
    public EtatVoiture() {
    }

    /**
     * Constructeur avec tous les paramètres.
     * @param idEtatVoiture L'identifiant unique de l'état du véhicule.
     * @param libEtatVoiture Le libellé décrivant l'état.
     */
    public EtatVoiture(Integer idEtatVoiture, String libEtatVoiture) {
        this.idEtatVoiture = idEtatVoiture;
        this.libEtatVoiture = libEtatVoiture;
    }

    /**
     * Retourne l'identifiant de l'état du véhicule.
     * @return L'identifiant de l'état.
     */
    public Integer getIdEtatVoiture() {
        return idEtatVoiture;
    }

    /**
     * Définit l'identifiant de l'état du véhicule.
     * @param idEtatVoiture Le nouvel identifiant de l'état.
     */
    public void setIdEtatVoiture(Integer idEtatVoiture) {
        this.idEtatVoiture = idEtatVoiture;
    }

    /**
     * Retourne le libellé de l'état du véhicule.
     * @return Le libellé de l'état.
     */
    public String getLibEtatVoiture() {
        return libEtatVoiture;
    }

    /**
     * Définit le libellé de l'état du véhicule.
     * @param libEtatVoiture Le nouveau libellé de l'état.
     */
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
        return "EtatVoiture{" +
                "idEtatVoiture=" + idEtatVoiture +
                ", libEtatVoiture='" + libEtatVoiture + '\'' +
                '}';
    }
}