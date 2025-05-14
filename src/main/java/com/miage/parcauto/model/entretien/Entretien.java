package main.java.com.miage.parcauto.model.entretien;

import main.java.com.miage.parcauto.model.vehicule.Vehicule;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entité représentant un entretien effectué ou à effectuer sur un véhicule.
 * Correspond à un enregistrement de la table ENTRETIEN.
 */
public class Entretien implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer idEntretien;
    private Vehicule vehicule; // Relation avec Vehicule (via id_vehicule)
    private LocalDateTime dateEntreeEntr;
    private LocalDateTime dateSortieEntr;
    private String motifEntr;
    private String observation;
    private BigDecimal coutEntr;
    private String lieuEntr;
    private TypeEntretien type;
    private StatutOT statutOt;

    /**
     * Constructeur par défaut.
     */
    public Entretien() {
    }

    /**
     * Constructeur avec tous les paramètres.
     *
     * @param idEntretien      L'identifiant unique de l'entretien.
     * @param vehicule         Le véhicule concerné par l'entretien.
     * @param dateEntreeEntr   La date d'entrée du véhicule pour l'entretien.
     * @param dateSortieEntr   La date de sortie du véhicule après l'entretien.
     * @param motifEntr        Le motif principal de l'entretien.
     * @param observation      Les observations techniques ou remarques.
     * @param coutEntr         Le coût total de l'entretien.
     * @param lieuEntr         Le lieu où l'entretien a été réalisé (garage, etc.).
     * @param type             Le type d'entretien (Préventif, Correctif).
     * @param statutOt         Le statut de l'ordre de travail associé.
     */
    public Entretien(Integer idEntretien, Vehicule vehicule, LocalDateTime dateEntreeEntr,
                     LocalDateTime dateSortieEntr, String motifEntr, String observation,
                     BigDecimal coutEntr, String lieuEntr, TypeEntretien type, StatutOT statutOt) {
        this.idEntretien = idEntretien;
        this.vehicule = vehicule;
        this.dateEntreeEntr = dateEntreeEntr;
        this.dateSortieEntr = dateSortieEntr;
        this.motifEntr = motifEntr;
        this.observation = observation;
        this.coutEntr = coutEntr;
        this.lieuEntr = lieuEntr;
        this.type = type;
        this.statutOt = statutOt;
    }

    // Getters et Setters

    public Integer getIdEntretien() {
        return idEntretien;
    }

    public void setIdEntretien(Integer idEntretien) {
        this.idEntretien = idEntretien;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
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
        this.coutEntr = coutEntr;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entretien entretien = (Entretien) o;
        return Objects.equals(idEntretien, entretien.idEntretien);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEntretien);
    }

    @Override
    public String toString() {
        return "Entretien{" +
                "idEntretien=" + idEntretien +
                ", vehicule=" + (vehicule != null ? vehicule.getImmatriculation() : "N/A") +
                ", motifEntr='" + motifEntr + '\'' +
                ", type=" + type +
                ", statutOt=" + statutOt +
                ", coutEntr=" + coutEntr +
                '}';
    }
}