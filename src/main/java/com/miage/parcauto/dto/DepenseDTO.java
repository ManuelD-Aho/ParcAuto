package main.java.com.miage.parcauto.dto;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DepenseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Integer idDepense;
    private Integer idMission;
    private String categorie;
    private String description;
    private BigDecimal montant;
    private LocalDate date;
    private String justificatif;

    public DepenseDTO() {
        this.montant = BigDecimal.ZERO;
        this.date = LocalDate.now();
    }

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

    public String getDateFormatee() {
        return date != null ? date.format(DATE_FORMATTER) : "";
    }

    public String getMontantFormate() {
        return montant != null ? montant.setScale(2, RoundingMode.HALF_UP) + " FCFA" : "0.00 FCFA";
    }

    public boolean hasJustificatif() {
        return justificatif != null && !justificatif.isEmpty();
    }

    public String getCategorieClass() {
        if (categorie == null)
            return "";
        switch (categorie.toLowerCase()) {
            case "carburant":
                return "depense-carburant";
            case "péage":
            case "fraisannexes": // Aligning with potential DB enum 'FraisAnnexes'
                return "depense-peage"; // Or a more generic "depense-frais"
            case "entretien":
                return "depense-entretien";
            case "autre":
                return "depense-autre";
            default:
                return "depense-autre";
        }
    }

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