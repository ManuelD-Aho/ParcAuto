package main.java.com.miage.parcauto.model.mission;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Classe modèle représentant une dépense liée à une mission.
 * Cette classe correspond à la table DEPENSE_MISSION dans la base de données.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class DepenseMission implements Serializable, Comparable<DepenseMission> {

    private static final long serialVersionUID = 1L;

    /**
     * Types de dépenses possibles
     */
    public enum NatureDepense {
        Carburant("Carburant"),
        FraisAnnexes("Frais annexes");

        private final String libelle;

        NatureDepense(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }

        /**
         * Convertit une chaîne en nature de dépense
         * @param value La valeur à convertir
         * @return La NatureDepense correspondante ou Carburant par défaut
         */
        public static NatureDepense fromString(String value) {
            if (value == null) return Carburant;

            try {
                return NatureDepense.valueOf(value);
            } catch (IllegalArgumentException e) {
                return Carburant;
            }
        }
    }

    // Attributs correspondant aux colonnes de la table DEPENSE_MISSION
    private Integer id;
    private Integer idMission;
    private NatureDepense nature;
    private BigDecimal montant;
    private String justificatif;

    /**
     * Constructeur par défaut
     */
    public DepenseMission() {
        this.nature = NatureDepense.Carburant;
        this.montant = BigDecimal.ZERO;
    }

    /**
     * Constructeur avec attributs essentiels
     *
     * @param idMission ID de la mission associée
     * @param nature Nature de la dépense (carburant ou frais annexes)
     * @param montant Montant de la dépense
     */
    public DepenseMission(Integer idMission, NatureDepense nature, BigDecimal montant) {
        this.idMission = idMission;
        this.nature = nature;
        this.montant = montant;
    }

    /**
     * Constructeur complet
     *
     * @param idMission ID de la mission associée
     * @param nature Nature de la dépense (carburant ou frais annexes)
     * @param montant Montant de la dépense
     * @param justificatif Chemin ou description du justificatif
     */
    public DepenseMission(Integer idMission, NatureDepense nature, BigDecimal montant, String justificatif) {
        this.idMission = idMission;
        this.nature = nature;
        this.montant = montant;
        this.justificatif = justificatif;
    }

    // Getters et Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdMission() {
        return idMission;
    }

    public void setIdMission(Integer idMission) {
        this.idMission = idMission;
    }

    public NatureDepense getNature() {
        return nature;
    }

    public void setNature(NatureDepense nature) {
        this.nature = nature;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant != null ? montant : BigDecimal.ZERO;
    }

    public String getJustificatif() {
        return justificatif;
    }

    public void setJustificatif(String justificatif) {
        this.justificatif = justificatif;
    }

    // Méthodes métier

    /**
     * Vérifie si la dépense est liée à du carburant
     *
     * @return true si c'est une dépense de carburant
     */
    public boolean estCarburant() {
        return nature == NatureDepense.Carburant;
    }

    /**
     * Vérifie si la dépense concerne des frais annexes
     *
     * @return true si ce sont des frais annexes
     */
    public boolean estFraisAnnexes() {
        return nature == NatureDepense.FraisAnnexes;
    }

    /**
     * Vérifie si un justificatif est présent
     *
     * @return true si un justificatif est associé à la dépense
     */
    public boolean aJustificatif() {
        return justificatif != null && !justificatif.trim().isEmpty();
    }

    /**
     * Récupère le libellé de la nature de dépense
     *
     * @return Le libellé de la nature de dépense
     */
    public String getNatureLibelle() {
        return nature != null ? nature.getLibelle() : "";
    }

    /**
     * Récupère le montant formaté avec symbole monétaire
     *
     * @return Le montant formaté
     */
    public String getMontantFormate() {
        if (montant == null) {
            return "0,00 €";
        }
        return String.format("%,.2f €", montant);
    }

    /**
     * Obtient le nom du fichier justificatif à partir du chemin
     *
     * @return Le nom du fichier sans le chemin
     */
    public String getNomFichierJustificatif() {
        if (!aJustificatif()) {
            return "";
        }

        int lastSeparator = Math.max(
                justificatif.lastIndexOf('/'),
                justificatif.lastIndexOf('\\')
        );

        if (lastSeparator >= 0 && lastSeparator < justificatif.length() - 1) {
            return justificatif.substring(lastSeparator + 1);
        }

        return justificatif;
    }

    /**
     * Retourne la classe CSS en fonction de la nature de dépense
     *
     * @return La classe CSS
     */
    public String getNatureClass() {
        switch (nature) {
            case Carburant: return "depense-carburant";
            case FraisAnnexes: return "depense-frais";
            default: return "";
        }
    }

    /**
     * Retourne la couleur CSS pour l'affichage
     *
     * @return Code couleur hex
     */
    public String getColorStyle() {
        switch (nature) {
            case Carburant: return "#0275d8"; // Bleu
            case FraisAnnexes: return "#5cb85c"; // Vert
            default: return "#6c757d"; // Gris
        }
    }

    /**
     * Contrôle la validité des données de la dépense
     *
     * @return true si les données sont valides, false sinon
     */
    public boolean isValid() {
        return idMission != null
                && nature != null
                && montant != null
                && montant.compareTo(BigDecimal.ZERO) > 0;
    }

    // Méthodes standard

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DepenseMission depense = (DepenseMission) obj;
        return Objects.equals(id, depense.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s: %s", getNatureLibelle(), getMontantFormate());
    }

    /**
     * Compare les dépenses par montant (ordre décroissant)
     *
     * @param autre L'autre dépense à comparer
     * @return résultat de la comparaison
     */
    @Override
    public int compareTo(DepenseMission autre) {
        if (autre == null || autre.getMontant() == null) {
            return -1;
        }
        if (this.montant == null) {
            return 1;
        }
        // Tri par montant décroissant (plus grand montant d'abord)
        return autre.getMontant().compareTo(this.montant);
    }
}