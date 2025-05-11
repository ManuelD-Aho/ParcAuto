package main.java.com.miage.parcauto.model.rh;

import java.io.Serializable;
import java.util.Objects;

/**
 * Classe modèle représentant une fonction professionnelle dans l'organisation.
 * Correspond à la table FONCTION dans la base de données.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class Fonction implements Serializable {

    private static final long serialVersionUID = 1L;

    // Attributs correspondant aux colonnes de la table FONCTION
    private Integer idFonction;
    private String libFonction;

    /**
     * Constructeur par défaut
     */
    public Fonction() {
    }

    /**
     * Constructeur avec tous les champs
     *
     * @param idFonction ID unique de la fonction
     * @param libFonction Libellé de la fonction
     */
    public Fonction(Integer idFonction, String libFonction) {
        this.idFonction = idFonction;
        this.libFonction = libFonction;
    }

    /**
     * Constructeur sans ID pour création d'une nouvelle fonction
     *
     * @param libFonction Libellé de la fonction
     */
    public Fonction(String libFonction) {
        this.libFonction = libFonction;
    }

    // Getters et setters

    /**
     * Obtient l'identifiant unique de la fonction
     *
     * @return l'identifiant de la fonction
     */
    public Integer getIdFonction() {
        return idFonction;
    }

    /**
     * Définit l'identifiant unique de la fonction
     *
     * @param idFonction l'identifiant à définir
     */
    public void setIdFonction(Integer idFonction) {
        this.idFonction = idFonction;
    }

    /**
     * Obtient le libellé de la fonction
     *
     * @return le libellé de la fonction
     */
    public String getLibFonction() {
        return libFonction;
    }

    /**
     * Définit le libellé de la fonction
     *
     * @param libFonction le libellé à définir
     */
    public void setLibFonction(String libFonction) {
        this.libFonction = libFonction;
    }

    /**
     * Représentation textuelle de la fonction
     *
     * @return une chaîne représentant la fonction
     */
    @Override
    public String toString() {
        return libFonction;
    }

    /**
     * Vérifie l'égalité entre cette fonction et un autre objet
     *
     * @param obj l'objet à comparer
     * @return true si les objets sont égaux, false sinon
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Fonction fonction = (Fonction) obj;
        return Objects.equals(idFonction, fonction.idFonction);
    }

    /**
     * Calcule le code de hachage pour cette fonction
     *
     * @return le code de hachage
     */
    @Override
    public int hashCode() {
        return Objects.hash(idFonction);
    }

    /**
     * Crée un clone de cet objet Fonction
     *
     * @return une nouvelle instance de Fonction avec les mêmes valeurs
     */
    public Fonction clone() {
        return new Fonction(idFonction, libFonction);
    }

    /**
     * Valide les données de la fonction
     *
     * @return true si les données sont valides, false sinon
     */
    public boolean isValid() {
        return libFonction != null && !libFonction.trim().isEmpty();
    }

    /**
     * Vérifie si la fonction est une position de management
     * Cette méthode pourrait être étendue pour vérifier des critères spécifiques
     *
     * @return true si la fonction est une position de management
     */
    public boolean isManagementPosition() {
        if (libFonction == null) {
            return false;
        }
        return libFonction.toLowerCase().contains("directeur") ||
                libFonction.toLowerCase().contains("manager") ||
                libFonction.toLowerCase().contains("chef") ||
                libFonction.toLowerCase().contains("responsable");
    }
}