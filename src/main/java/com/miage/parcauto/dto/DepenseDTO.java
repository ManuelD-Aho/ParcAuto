package main.java.com.miage.parcauto.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * DTO (Data Transfer Object) pour les dépenses de mission.
 * Cette classe est utilisée pour transférer les données de dépenses entre les
 * couches.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class DepenseDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Integer idDepense;
    private Integer idMission;
    private String categorie;
    private String description;
    private BigDecimal montant;
    private LocalDate date;
    private String justificatif;

    /**
     * Constructeur par défaut.
     */
    public DepenseDTO() {
        this.montant = BigDecimal.ZERO;
        this.date = LocalDate.now();
    }

    /**
     * Constructeur avec tous les paramètres.
     *
     * @param idDepense    ID de la dépense
     * @param idMission    ID de la mission associée
     * @param categorie    Catégorie de la dépense (carburant, péage, etc.)
     * @param description  Description détaillée
     * @param montant      Montant de la dépense
     * @param date         Date de la dépense
     * @param justificatif Référence au justificatif
     */
    public DepenseDTO(Integer idDepense, Integer idMission, String categorie, String description, BigDecimal montant,
            LocalDate date, String justificatif) {
        this.idDepense = idDepense;
        this.idMission = idMission;
        this.categorie = categorie;
        this.description = description;
        this.montant = montant;
        this.date = date;
        this.justificatif = justificatif;
    }

    /**
     * Formate la date de la dépense.
     *
     * @return Date formatée
     */
    public String getDateFormatee() {
        return date != null ? date.format(DATE_FORMATTER) : "";
    }

    /**
     * Formate le montant avec le symbole €.
     *
     * @return Montant formaté avec symbole
     */
    public String getMontantFormate() {
        return montant != null ? montant.setScale(2, RoundingMode.HALF_UP) + " €" : "0.00 €";
    }

    /**
     * Vérifie si un justificatif existe pour cette dépense.
     *
     * @return true si un justificatif est associé, false sinon
     */
    public boolean hasJustificatif() {
        return justificatif != null && !justificatif.isEmpty();
    }

    /**
     * Détermine la classe CSS à utiliser selon la catégorie de dépense.
     *
     * @return Classe CSS appropriée
     */
    public String getCategorieClass() {
        if (categorie == null)
            return "";
        switch (categorie.toLowerCase()) {
            case "carburant":
                return "depense-carburant";
            case "péage":
                return "depense-peage";
            case "entretien":
                return "depense-entretien";
            case "autre":
                return "depense-autre";
            default:
                return "depense-autre";
        }
    }

    // Getters et Setters

    public Integer getIdDepense() {
        return idDepense;
    }

    public void setIdDepense(Integer idDepense) {
        this.idDepense = idDepense;
    }

    public Integer getIdMission() {
        return idMission;
    }

    public void setIdMission(Integer idMission) {
        this.idMission = idMission;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getJustificatif() {
        return justificatif;
    }

    public void setJustificatif(String justificatif) {
        this.justificatif = justificatif;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DepenseDTO that = (DepenseDTO) o;
        return Objects.equals(idDepense, that.idDepense);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDepense);
    }

    @Override
    public String toString() {
        return "DepenseDTO{" +
                "idDepense=" + idDepense +
                ", catégorie='" + categorie + '\'' +
                ", montant=" + getMontantFormate() +
                ", date=" + getDateFormatee() +
                '}';
    }
}
