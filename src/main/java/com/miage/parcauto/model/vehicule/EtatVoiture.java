package main.java.com.miage.parcauto.model.vehicule;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;

/**
 * Classe modèle représentant l'état d'une voiture dans le parc automobile.
 * Correspond à la table ETAT_VOITURE dans la base de données.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class EtatVoiture implements Serializable {

    private static final long serialVersionUID = 1L;

    // Constantes pour les identifiants d'état standards (correspondant aux ID en base)
    public static final int DISPONIBLE = 1;
    public static final int EN_MISSION = 2;
    public static final int HORS_SERVICE = 3;
    public static final int EN_ENTRETIEN = 4;
    public static final int ATTRIBUER = 5;
    public static final int PANNE = 6;

    // Attributs correspondant aux colonnes de la table ETAT_VOITURE
    private Integer idEtatVoiture;
    private String libEtatVoiture;

    /**
     * Constructeur par défaut
     */
    public EtatVoiture() {
    }

    /**
     * Constructeur avec tous les champs
     *
     * @param idEtatVoiture ID unique de l'état
     * @param libEtatVoiture Libellé de l'état
     */
    public EtatVoiture(Integer idEtatVoiture, String libEtatVoiture) {
        this.idEtatVoiture = idEtatVoiture;
        this.libEtatVoiture = libEtatVoiture;
    }

    /**
     * Fournit une instance d'EtatVoiture pour un ID standard
     *
     * @param idEtat l'identifiant de l'état
     * @return l'instance correspondante ou null si l'ID n'est pas standard
     */
    public static EtatVoiture fromId(int idEtat) {
        switch (idEtat) {
            case DISPONIBLE:
                return new EtatVoiture(DISPONIBLE, "Disponible");
            case EN_MISSION:
                return new EtatVoiture(EN_MISSION, "En mission");
            case HORS_SERVICE:
                return new EtatVoiture(HORS_SERVICE, "Hors Service");
            case EN_ENTRETIEN:
                return new EtatVoiture(EN_ENTRETIEN, "En entretien");
            case ATTRIBUER:
                return new EtatVoiture(ATTRIBUER, "Attribuer");
            case PANNE:
                return new EtatVoiture(PANNE, "Panne");
            default:
                return null;
        }
    }

    /**
     * Fournit une instance d'EtatVoiture à partir d'un libellé
     *
     * @param libelle le libellé de l'état recherché
     * @return un Optional contenant l'état s'il existe, vide sinon
     */
    public static Optional<EtatVoiture> fromLibelle(String libelle) {
        if (libelle == null || libelle.trim().isEmpty()) {
            return Optional.empty();
        }

        // Normalisation du libellé pour la comparaison
        String normalizedLib = libelle.trim().toLowerCase();

        return Arrays.asList(
                        new EtatVoiture(DISPONIBLE, "Disponible"),
                        new EtatVoiture(EN_MISSION, "En mission"),
                        new EtatVoiture(HORS_SERVICE, "Hors Service"),
                        new EtatVoiture(EN_ENTRETIEN, "En entretien"),
                        new EtatVoiture(ATTRIBUER, "Attribuer"),
                        new EtatVoiture(PANNE, "Panne")
                ).stream()
                .filter(etat -> etat.getLibEtatVoiture().toLowerCase().equals(normalizedLib))
                .findFirst();
    }

    /**
     * Vérifie si l'état actuel permet une opération d'entretien
     *
     * @return true si le véhicule peut être mis en entretien
     */
    public boolean isMaintenanceAllowed() {
        return idEtatVoiture != null &&
                (idEtatVoiture.equals(DISPONIBLE) || idEtatVoiture.equals(PANNE));
    }

    /**
     * Vérifie si l'état actuel permet une affectation à une mission
     *
     * @return true si le véhicule peut être affecté à une mission
     */
    public boolean isMissionAllowed() {
        return idEtatVoiture != null && idEtatVoiture.equals(DISPONIBLE);
    }

    /**
     * Vérifie si l'état actuel permet une attribution à un responsable
     *
     * @return true si le véhicule peut être attribué à un responsable
     */
    public boolean isAttributionAllowed() {
        return idEtatVoiture != null && idEtatVoiture.equals(DISPONIBLE);
    }

    // Getters et setters

    /**
     * Obtient l'identifiant unique de l'état
     *
     * @return l'identifiant de l'état
     */
    public Integer getIdEtatVoiture() {
        return idEtatVoiture;
    }

    /**
     * Définit l'identifiant unique de l'état
     *
     * @param idEtatVoiture l'identifiant à définir
     */
    public void setIdEtatVoiture(Integer idEtatVoiture) {
        this.idEtatVoiture = idEtatVoiture;
    }

    /**
     * Obtient le libellé de l'état
     *
     * @return le libellé de l'état
     */
    public String getLibEtatVoiture() {
        return libEtatVoiture;
    }

    /**
     * Définit le libellé de l'état
     *
     * @param libEtatVoiture le libellé à définir
     */
    public void setLibEtatVoiture(String libEtatVoiture) {
        this.libEtatVoiture = libEtatVoiture;
    }

    /**
     * Représentation textuelle de l'état
     *
     * @return une chaîne représentant l'état
     */
    @Override
    public String toString() {
        return libEtatVoiture;
    }

    /**
     * Vérifie l'égalité entre cet état et un autre objet
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

        EtatVoiture other = (EtatVoiture) obj;
        if (idEtatVoiture == null) {
            return other.idEtatVoiture == null;
        }
        return idEtatVoiture.equals(other.idEtatVoiture);
    }

    /**
     * Calcule le code de hachage pour cet état
     *
     * @return le code de hachage
     */
    @Override
    public int hashCode() {
        return idEtatVoiture != null ? idEtatVoiture.hashCode() : 0;
    }

    /**
     * Obtient la couleur CSS associée à cet état pour l'affichage dans l'interface
     *
     * @return une chaîne représentant la couleur CSS (code hex)
     */
    public String getColorStyle() {
        if (idEtatVoiture == null) {
            return "#888888"; // Gris par défaut
        }

        switch (idEtatVoiture) {
            case DISPONIBLE:
                return "#2ecc71"; // Vert
            case EN_MISSION:
                return "#3498db"; // Bleu
            case HORS_SERVICE:
                return "#7f8c8d"; // Gris foncé
            case EN_ENTRETIEN:
                return "#f39c12"; // Orange
            case ATTRIBUER:
                return "#9b59b6"; // Violet
            case PANNE:
                return "#e74c3c"; // Rouge
            default:
                return "#888888"; // Gris par défaut
        }
    }

    /**
     * Obtient une classe CSS associée à cet état pour l'affichage dans l'interface
     *
     * @return une chaîne représentant la classe CSS
     */
    public String getCssClass() {
        if (idEtatVoiture == null) {
            return "etat-default";
        }

        switch (idEtatVoiture) {
            case DISPONIBLE:
                return "etat-disponible";
            case EN_MISSION:
                return "etat-mission";
            case HORS_SERVICE:
                return "etat-hors-service";
            case EN_ENTRETIEN:
                return "etat-entretien";
            case ATTRIBUER:
                return "etat-attribuer";
            case PANNE:
                return "etat-panne";
            default:
                return "etat-default";
        }
    }
}