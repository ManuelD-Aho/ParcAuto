package main.java.com.miage.parcauto.model.finance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Classe modèle représentant un mouvement financier sur un compte sociétaire.
 * Cette classe correspond à la table MOUVEMENT dans la base de données.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class Mouvement implements Serializable, Comparable<Mouvement> {

    private static final long serialVersionUID = 1L;

    /**
     * Types de mouvements possibles
     */
    public enum TypeMouvement {
        Depot("Dépôt"),
        Retrait("Retrait"),
        Mensualite("Mensualité");

        private final String libelle;

        TypeMouvement(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }

        /**
         * Convertit une chaîne en type de mouvement
         * @param value La valeur à convertir
         * @return Le TypeMouvement correspondant ou Depot par défaut
         */
        public static TypeMouvement fromString(String value) {
            if (value == null) return Depot;

            try {
                return TypeMouvement.valueOf(value);
            } catch (IllegalArgumentException e) {
                return Depot;
            }
        }
    }

    // Attributs correspondant aux colonnes de la table MOUVEMENT
    private Integer id;
    private Integer idSocietaire;
    private LocalDateTime date;
    private TypeMouvement type;
    private BigDecimal montant;
    private String description;

    // Attribut pour référence au compte (non stocké en BDD)
    private transient SocieteCompte compte;

    /**
     * Constructeur par défaut
     */
    public Mouvement() {
        this.date = LocalDateTime.now();
        this.montant = BigDecimal.ZERO;
        this.type = TypeMouvement.Depot;
    }

    /**
     * Constructeur avec attributs essentiels
     *
     * @param idSocietaire ID du compte sociétaire concerné
     * @param type Type de mouvement
     * @param montant Montant du mouvement
     */
    public Mouvement(Integer idSocietaire, TypeMouvement type, BigDecimal montant) {
        this.idSocietaire = idSocietaire;
        this.type = type;
        this.montant = montant;
        this.date = LocalDateTime.now();
    }

    /**
     * Constructeur avec tous les attributs essentiels
     *
     * @param idSocietaire ID du compte sociétaire concerné
     * @param date Date et heure du mouvement
     * @param type Type de mouvement
     * @param montant Montant du mouvement
     * @param description Description optionnelle du mouvement
     */
    public Mouvement(Integer idSocietaire, LocalDateTime date, TypeMouvement type,
                     BigDecimal montant, String description) {
        this.idSocietaire = idSocietaire;
        this.date = date != null ? date : LocalDateTime.now();
        this.type = type;
        this.montant = montant;
        this.description = description;
    }

    /**
     * Constructeur avec compte sociétaire
     *
     * @param compte Le compte sociétaire associé
     * @param type Type de mouvement
     * @param montant Montant du mouvement
     */
    public Mouvement(SocieteCompte compte, TypeMouvement type, BigDecimal montant) {
        this.compte = compte;
        this.idSocietaire = compte != null ? compte.getIdSocietaire() : null;
        this.type = type;
        this.montant = montant;
        this.date = LocalDateTime.now();
    }

    // Getters et Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdSocietaire() {
        return idSocietaire;
    }

    public void setIdSocietaire(Integer idSocietaire) {
        this.idSocietaire = idSocietaire;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public TypeMouvement getType() {
        return type;
    }

    public void setType(TypeMouvement type) {
        this.type = type;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SocieteCompte getCompte() {
        return compte;
    }

    public void setCompte(SocieteCompte compte) {
        this.compte = compte;
        if (compte != null) {
            this.idSocietaire = compte.getIdSocietaire();
        }
    }

    // Méthodes métier

    /**
     * Vérifie si le mouvement est un crédit (dépôt)
     *
     * @return true si c'est un dépôt, false sinon
     */
    public boolean estCredit() {
        return type == TypeMouvement.Depot;
    }

    /**
     * Vérifie si le mouvement est un débit (retrait ou mensualité)
     *
     * @return true si c'est un débit, false sinon
     */
    public boolean estDebit() {
        return type == TypeMouvement.Retrait || type == TypeMouvement.Mensualite;
    }

    /**
     * Vérifie si le mouvement est une mensualité
     *
     * @return true si c'est une mensualité, false sinon
     */
    public boolean estMensualite() {
        return type == TypeMouvement.Mensualite;
    }

    /**
     * Calcule l'impact financier du mouvement sur le solde
     * Les dépôts sont positifs, les retraits et mensualités sont négatifs
     *
     * @return le montant avec le signe approprié
     */
    public BigDecimal getImpactFinancier() {
        if (estCredit()) {
            return montant;
        } else {
            return montant.negate();
        }
    }

    /**
     * Formate la date du mouvement selon un format donné
     *
     * @param format Format de date (ex: "dd/MM/yyyy HH:mm")
     * @return La date formatée ou chaîne vide si date null
     */
    public String getDateFormatted(String format) {
        if (date == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return date.format(formatter);
    }

    /**
     * Retourne la date formatée par défaut (dd/MM/yyyy HH:mm)
     *
     * @return La date formatée
     */
    public String getDateFormatted() {
        return getDateFormatted("dd/MM/yyyy HH:mm");
    }

    /**
     * Génère une description automatique si aucune n'est fournie
     *
     * @return Une description basée sur le type de mouvement
     */
    public String getDescriptionAuto() {
        if (description != null && !description.trim().isEmpty()) {
            return description;
        }

        switch (type) {
            case Depot:
                return "Dépôt sur le compte";
            case Retrait:
                return "Retrait depuis le compte";
            case Mensualite:
                return "Paiement mensualité véhicule";
            default:
                return "Opération sur compte";
        }
    }

    /**
     * Contrôle la validité des données du mouvement
     *
     * @return true si les données sont valides, false sinon
     */
    public boolean isValid() {
        return idSocietaire != null
                && type != null
                && montant != null
                && montant.compareTo(BigDecimal.ZERO) > 0
                && date != null;
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
        Mouvement mouvement = (Mouvement) obj;
        return Objects.equals(id, mouvement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s: %s %.2f € le %s",
                type.getLibelle(),
                estCredit() ? "+" : "-",
                montant,
                getDateFormatted("dd/MM/yyyy"));
    }

    /**
     * Compare les mouvements par date (ordre décroissant)
     *
     * @param other L'autre mouvement à comparer
     * @return résultat de la comparaison
     */
    @Override
    public int compareTo(Mouvement other) {
        if (other == null || other.getDate() == null) {
            return -1;
        }
        if (this.date == null) {
            return 1;
        }
        // Tri par date décroissante (plus récent d'abord)
        return other.getDate().compareTo(this.date);
    }
}