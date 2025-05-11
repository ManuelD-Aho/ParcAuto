package main.java.com.miage.parcauto.model.entretien;

import main.java.com.miage.parcauto.model.vehicule.Vehicule;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Classe modèle représentant un entretien de véhicule.
 * Cette classe correspond à la table ENTRETIEN dans la base de données.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class Entretien implements Serializable, Comparable<Entretien> {

    private static final long serialVersionUID = 1L;

    /**
     * Types d'entretien possibles
     */
    public enum TypeEntretien {
        Preventif("Entretien préventif"),
        Correctif("Réparation corrective");

        private final String libelle;

        TypeEntretien(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }

        /**
         * Convertit une chaîne en type d'entretien
         * @param value La valeur à convertir
         * @return Le TypeEntretien correspondant ou Preventif par défaut
         */
        public static TypeEntretien fromString(String value) {
            if (value == null) return Preventif;

            try {
                return TypeEntretien.valueOf(value);
            } catch (IllegalArgumentException e) {
                return Preventif;
            }
        }
    }

    /**
     * Statuts possibles d'un ordre de travail
     */
    public enum StatutOT {
        Ouvert("Ouvert"),
        EnCours("En cours"),
        Cloture("Clôturé");

        private final String libelle;

        StatutOT(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }

        /**
         * Convertit une chaîne en statut OT
         * @param value La valeur à convertir
         * @return Le StatutOT correspondant ou Ouvert par défaut
         */
        public static StatutOT fromString(String value) {
            if (value == null) return Ouvert;

            try {
                return StatutOT.valueOf(value);
            } catch (IllegalArgumentException e) {
                return Ouvert;
            }
        }
    }

    // Attributs correspondant aux colonnes de la table ENTRETIEN
    private Integer idEntretien;
    private Integer idVehicule;
    private LocalDateTime dateEntreeEntr;
    private LocalDateTime dateSortieEntr;
    private String motifEntr;
    private String observation;
    private BigDecimal coutEntr;
    private String lieuEntr;
    private TypeEntretien type;
    private StatutOT statutOt;
    private Vehicule vehicule;  // Ajouter ce champ
    private String infoVehicule; // Information textuelle sur le véhicule

    /**
     * Constructeur par défaut
     */
    public Entretien() {
        this.dateEntreeEntr = LocalDateTime.now();
        this.type = TypeEntretien.Preventif;
        this.statutOt = StatutOT.Ouvert;
        this.coutEntr = BigDecimal.ZERO;
    }

    /**
     * Constructeur avec attributs essentiels
     *
     * @param idVehicule ID du véhicule concerné
     * @param motifEntr Motif de l'entretien
     * @param type Type d'entretien (préventif/correctif)
     * @param lieuEntr Lieu de l'entretien
     */
    public Entretien(Integer idVehicule, String motifEntr, TypeEntretien type, String lieuEntr) {
        this.idVehicule = idVehicule;
        this.motifEntr = motifEntr;
        this.type = type;
        this.lieuEntr = lieuEntr;
        this.dateEntreeEntr = LocalDateTime.now();
        this.statutOt = StatutOT.Ouvert;
        this.coutEntr = BigDecimal.ZERO;
    }

    /**
     * Constructeur avec attributs complets pour création
     *
     * @param idVehicule ID du véhicule concerné
     * @param dateEntree Date d'entrée en entretien
     * @param motifEntr Motif de l'entretien
     * @param lieuEntr Lieu de l'entretien
     * @param type Type d'entretien (préventif/correctif)
     * @param cout Coût estimé ou réel
     */
    public Entretien(Integer idVehicule, LocalDateTime dateEntree, String motifEntr,
                     String lieuEntr, TypeEntretien type, BigDecimal cout) {
        this.idVehicule = idVehicule;
        this.dateEntreeEntr = dateEntree != null ? dateEntree : LocalDateTime.now();
        this.motifEntr = motifEntr;
        this.lieuEntr = lieuEntr;
        this.type = type;
        this.coutEntr = cout != null ? cout : BigDecimal.ZERO;
        this.statutOt = StatutOT.Ouvert;
    }

    // Getters et Setters

    public Integer getIdEntretien() {
        return idEntretien;
    }

    public void setIdEntretien(Integer idEntretien) {
        this.idEntretien = idEntretien;
    }

    public Integer getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(Integer idVehicule) {
        this.idVehicule = idVehicule;
    }

    public LocalDateTime getDateEntreeEntr() {
        return dateEntreeEntr;
    }

    public void setDateEntreeEntr(LocalDateTime dateEntreeEntr) {
        this.dateEntreeEntr = dateEntreeEntr;
    }

    public LocalDateTime getDateSortieEntr() {
        return dateSortieEntr;
    }

    public void setDateSortieEntr(LocalDateTime dateSortieEntr) {
        this.dateSortieEntr = dateSortieEntr;
    }

    public String getMotifEntr() {
        return motifEntr;
    }

    public void setMotifEntr(String motifEntr) {
        this.motifEntr = motifEntr;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public BigDecimal getCoutEntr() {
        return coutEntr;
    }

    public void setCoutEntr(BigDecimal coutEntr) {
        this.coutEntr = coutEntr != null ? coutEntr : BigDecimal.ZERO;
    }

    public String getLieuEntr() {
        return lieuEntr;
    }

    public void setLieuEntr(String lieuEntr) {
        this.lieuEntr = lieuEntr;
    }

    public TypeEntretien getType() {
        return type;
    }

    public void setType(TypeEntretien type) {
        this.type = type;
    }

    public StatutOT getStatutOt() {
        return statutOt;
    }

    public void setStatutOt(StatutOT statutOt) {
        this.statutOt = statutOt;
    }

    // Méthodes métier

    /**
     * Vérifie si l'entretien est en cours
     *
     * @return true si l'entretien est au statut "en cours"
     */
    public boolean estEnCours() {
        return statutOt == StatutOT.EnCours;
    }

    /**
     * Vérifie si l'entretien est clôturé
     *
     * @return true si l'entretien est au statut "clôturé"
     */
    public boolean estCloture() {
        return statutOt == StatutOT.Cloture;
    }

    /**
     * Vérifie si l'entretien est ouvert (pas encore commencé)
     *
     * @return true si l'entretien est au statut "ouvert"
     */
    public boolean estOuvert() {
        return statutOt == StatutOT.Ouvert;
    }

    /**
     * Démarre l'entretien (passage au statut "en cours")
     *
     * @return true si le changement d'état a été effectué
     */
    public boolean demarrer() {
        if (estOuvert()) {
            statutOt = StatutOT.EnCours;
            return true;
        }
        return false;
    }
    /**
     * Obtient le véhicule associé à cet entretien.
     * @return Le véhicule associé
     */
    public Vehicule getVehicule() {
        return vehicule;
    }

    /**
     * Définit le véhicule associé à cet entretien.
     * @param vehicule Le véhicule à associer
     */
    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
        if (vehicule != null) {
            this.idVehicule = vehicule.getIdVehicule();
        }
    }

    /**
     * Définit les informations textuelles sur le véhicule.
     * Utile quand on n'a pas l'objet véhicule complet.
     * @param infoVehicule Informations sur le véhicule (ex: "AA-123-BB - Renault Clio")
     */
    public void setInfoVehicule(String infoVehicule) {
        this.infoVehicule = infoVehicule;
    }

    /**
     * Obtient les informations textuelles sur le véhicule.
     * @return Les informations sur le véhicule
     */
    public String getInfoVehicule() {
        if (infoVehicule != null) {
            return infoVehicule;
        } else if (vehicule != null) {
            return vehicule.getImmatriculation() + " - " + vehicule.getMarque() + " " + vehicule.getModele();
        } else {
            return "Véhicule ID: " + idVehicule;
        }
    }

    /**
     * Clôture l'entretien (passage au statut "clôturé")
     *
     * @return true si le changement d'état a été effectué
     */
    public boolean cloturer() {
        if (estEnCours()) {
            statutOt = StatutOT.Cloture;
            dateSortieEntr = LocalDateTime.now();
            return true;
        }
        return false;
    }

    /**
     * Calcule la durée de l'entretien en heures
     *
     * @return Nombre d'heures entre entrée et sortie, ou depuis l'entrée si toujours en cours
     */
    public long getDureeHeures() {
        LocalDateTime debut = dateEntreeEntr;
        LocalDateTime fin = dateSortieEntr != null ? dateSortieEntr : LocalDateTime.now();

        if (debut == null) {
            return 0;
        }

        return ChronoUnit.HOURS.between(debut, fin);
    }

    /**
     * Calcule la durée de l'entretien en jours
     *
     * @return Nombre de jours entre entrée et sortie, ou depuis l'entrée si toujours en cours
     */
    public long getDureeJours() {
        LocalDateTime debut = dateEntreeEntr;
        LocalDateTime fin = dateSortieEntr != null ? dateSortieEntr : LocalDateTime.now();

        if (debut == null) {
            return 0;
        }

        return ChronoUnit.DAYS.between(debut, fin);
    }

    /**
     * Vérifie si l'entretien dure depuis longtemps (plus de N jours)
     *
     * @param joursMax Nombre de jours maximum considéré comme normal
     * @return true si l'entretien dure depuis longtemps
     */
    public boolean estLong(int joursMax) {
        return !estCloture() && getDureeJours() > joursMax;
    }

    /**
     * Vérifie si l'entretien est préventif
     *
     * @return true si c'est un entretien préventif
     */
    public boolean estPreventif() {
        return type == TypeEntretien.Preventif;
    }

    /**
     * Vérifie si l'entretien est correctif (réparation)
     *
     * @return true si c'est une réparation
     */
    public boolean estCorrectif() {
        return type == TypeEntretien.Correctif;
    }

    /**
     * Formate une date selon un format donné
     *
     * @param date La date à formater
     * @param format Format de date (ex: "dd/MM/yyyy")
     * @return La date formatée ou chaîne vide si date null
     */
    public String formatDate(LocalDateTime date, String format) {
        if (date == null) {
            return "N/A";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return date.format(formatter);
    }

    /**
     * Retourne la date d'entrée formatée
     *
     * @return La date formatée (jj/mm/aaaa)
     */
    public String getDateEntreeFormatted() {
        return formatDate(dateEntreeEntr, "dd/MM/yyyy");
    }

    /**
     * Retourne la date de sortie formatée
     *
     * @return La date formatée (jj/mm/aaaa) ou "En cours" si non clôturé
     */
    public String getDateSortieFormatted() {
        if (estCloture()) {
            return formatDate(dateSortieEntr, "dd/MM/yyyy");
        } else {
            return "En cours";
        }
    }

    /**
     * Contrôle la validité des données de l'entretien
     *
     * @return true si les données sont valides, false sinon
     */
    public boolean isValid() {
        return idVehicule != null
                && dateEntreeEntr != null
                && type != null
                && motifEntr != null && !motifEntr.trim().isEmpty()
                && (dateSortieEntr == null || dateEntreeEntr.isBefore(dateSortieEntr));
    }

    /**
     * Retourne la couleur CSS associée au statut
     *
     * @return Code couleur hex pour affichage en UI
     */
    public String getColorStyle() {
        switch (statutOt) {
            case Ouvert: return "#ffc107"; // Jaune/orange
            case EnCours: return "#17a2b8"; // Bleu
            case Cloture: return "#28a745"; // Vert
            default: return "#6c757d";      // Gris
        }
    }

    /**
     * Retourne la classe CSS associée au type d'entretien
     *
     * @return Nom de classe CSS
     */
    public String getTypeClass() {
        return estPreventif() ? "preventif" : "correctif";
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
        Entretien entretien = (Entretien) obj;
        return Objects.equals(idEntretien, entretien.idEntretien);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEntretien);
    }

    @Override
    public String toString() {
        return String.format("%s #%d (%s) - %s - %s",
                type.getLibelle(),
                idEntretien != null ? idEntretien : 0,
                statutOt.getLibelle(),
                getMotifEntrCourt(),
                getDateEntreeFormatted()
        );
    }

    /**
     * Compare les entretiens par date d'entrée (ordre décroissant)
     *
     * @param autre L'autre entretien à comparer
     * @return résultat de la comparaison
     */
    @Override
    public int compareTo(Entretien autre) {
        // Tri par date d'entrée décroissante (plus récent d'abord)
        if (autre == null || autre.getDateEntreeEntr() == null) {
            return -1;
        }
        if (this.dateEntreeEntr == null) {
            return 1;
        }
        return autre.getDateEntreeEntr().compareTo(this.dateEntreeEntr);
    }

    /**
     * Retourne une version courte du motif (limité à 40 caractères)
     *
     * @return Motif limité en longueur
     */
    public String getMotifEntrCourt() {
        if (motifEntr == null) {
            return "";
        }
        if (motifEntr.length() <= 40) {
            return motifEntr;
        }
        return motifEntr.substring(0, 37) + "...";
    }
}