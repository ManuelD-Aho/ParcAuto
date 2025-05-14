package main.java.com.miage.parcauto.model.rh;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Entité représentant une fonction ou un poste au sein de l'entreprise.
 * Correspond à un enregistrement de la table FONCTION.
 */
public class Fonction implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer idFonction;
    private String libFonction; // Libellé de la fonction

    /**
     * Constructeur par défaut.
     */
    public Fonction() {
    }

    /**
     * Constructeur avec tous les paramètres.
     *
     * @param idFonction  L'identifiant unique de la fonction.
     * @param libFonction Le libellé de la fonction (ex: "Chauffeur", "Manager", "Technicien").
     */
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
        return "Fonction{" +
                "idFonction=" + idFonction +
                ", libFonction='" + libFonction + '\'' +
                '}';
    }
}