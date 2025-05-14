package main.java.com.miage.parcauto.dto;

import main.java.com.miage.parcauto.model.entretien.Entretien.TypeEntretien;
import main.java.com.miage.parcauto.model.entretien.Entretien.StatutOT;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * DTO (Data Transfer Object) pour la classe Entretien.
 * Cette classe est utilisée pour transférer les données d'entretien entre les
 * couches sans exposer les détails d'implémentation du modèle.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class EntretienDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Attributs correspondant aux données d'entretien nécessaires à l'interface
    private Integer idEntretien;
    private Integer idVehicule;
    private String immatriculationVehicule;
    private String marqueModeleVehicule;
    private LocalDateTime dateEntreeEntr;
    private LocalDateTime dateSortieEntr;
    private String motifEntr;
    private String observation;
    private BigDecimal coutEntr;
    private String lieuEntr;
    private TypeEntretien type;
    private StatutOT statutOt;

    /**
     * Constructeur par défaut
     */
    public EntretienDTO() {
        // Initialisation par défaut
        this.coutEntr = BigDecimal.ZERO;
        this.type = TypeEntretien.Preventif;
        this.statutOt = StatutOT.Ouvert;
    }

    /**
     * Constructeur avec tous les paramètres principaux.
     *
     * @param idEntretien             ID de l'entretien
     * @param idVehicule              ID du véhicule
     * @param immatriculationVehicule Immatriculation du véhicule
     * @param marqueModeleVehicule    Marque et modèle du véhicule
     * @param dateEntreeEntr          Date d'entrée en atelier
     * @param dateSortieEntr          Date de sortie d'atelier
     * @param motifEntr               Motif de l'entretien
     * @param observation             Observations techniques
     * @param coutEntr                Coût de l'entretien
     * @param lieuEntr                Lieu de l'entretien
     * @param type                    Type d'entretien
     * @param statutOt                Statut de l'ordre de travail
     */
    public EntretienDTO(Integer idEntretien, Integer idVehicule, String immatriculationVehicule,
            String marqueModeleVehicule, LocalDateTime dateEntreeEntr, LocalDateTime dateSortieEntr,
            String motifEntr, String observation, BigDecimal coutEntr,
            String lieuEntr, TypeEntretien type, StatutOT statutOt) {
        this.idEntretien = idEntretien;
        this.idVehicule = idVehicule;
        this.immatriculationVehicule = immatriculationVehicule;
        this.marqueModeleVehicule = marqueModeleVehicule;
        this.dateEntreeEntr = dateEntreeEntr;
        this.dateSortieEntr = dateSortieEntr;
        this.motifEntr = motifEntr;
        this.observation = observation;
        this.coutEntr = coutEntr;
        this.lieuEntr = lieuEntr;
        this.type = type;
        this.statutOt = statutOt;
    }

    /**
     * Calcule la durée de l'entretien en heures.
     *
     * @return Durée de l'entretien en heures, ou 0 si l'entretien est toujours en
     *         cours
     */
    public long getDureeHeures() {
        if (dateEntreeEntr == null || dateSortieEntr == null) {
            return 0L;
        }
        return ChronoUnit.HOURS.between(dateEntreeEntr, dateSortieEntr);
    }

    /**
     * Formatage de la date d'entrée en atelier.
     *
     * @return Date d'entrée formatée, ou "Non planifiée" si null
     */
    public String getDateEntreeFormatee() {
        return dateEntreeEntr != null ? dateEntreeEntr.format(DATE_FORMATTER) : "Non planifiée";
    }

    /**
     * Formatage de la date de sortie d'atelier.
     *
     * @return Date de sortie formatée, ou "En cours" si null
     */
    public String getDateSortieFormatee() {
        return dateSortieEntr != null ? dateSortieEntr.format(DATE_FORMATTER) : "En cours";
    }

    /**
     * Vérifie si l'entretien est terminé.
     *
     * @return true si l'entretien est terminé, false sinon
     */
    public boolean isTermine() {
        return statutOt == StatutOT.Cloture && dateSortieEntr != null;
    }

    /**
     * Récupère une classe CSS appropriée pour le styling de l'entretien selon son
     * statut.
     *
     * @return Classe CSS à appliquer
     */
    public String getStatutCssClass() {
        switch (statutOt) {
            case Ouvert:
                return "entretien-ouvert";
            case EnCours:
                return "entretien-encours";
            case Cloture:
                return "entretien-cloture";
            default:
                return "";
        }
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

    public String getImmatriculationVehicule() {
        return immatriculationVehicule;
    }

    public void setImmatriculationVehicule(String immatriculationVehicule) {
        this.immatriculationVehicule = immatriculationVehicule;
    }

    public String getMarqueModeleVehicule() {
        return marqueModeleVehicule;
    }

    public void setMarqueModeleVehicule(String marqueModeleVehicule) {
        this.marqueModeleVehicule = marqueModeleVehicule;
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

    /**
     * Formate le coût de l'entretien en texte avec le symbole €.
     *
     * @return Coût formaté
     */
    public String getCoutFormate() {
        return coutEntr + " €";
    }

    /**
     * Retourne une représentation textuelle du type d'entretien.
     *
     * @return Libellé du type d'entretien
     */
    public String getTypeLibelle() {
        return type != null ? type.getLibelle() : TypeEntretien.Preventif.getLibelle();
    }

    /**
     * Retourne une représentation textuelle du statut de l'entretien.
     *
     * @return Libellé du statut de l'entretien
     */
    public String getStatutLibelle() {
        return statutOt != null ? statutOt.getLibelle() : StatutOT.Ouvert.getLibelle();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        EntretienDTO that = (EntretienDTO) o;
        return Objects.equals(idEntretien, that.idEntretien);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEntretien);
    }

    @Override
    public String toString() {
        return "EntretienDTO{" +
                "idEntretien=" + idEntretien +
                ", véhicule=" + (marqueModeleVehicule != null ? marqueModeleVehicule : "") +
                " (" + (immatriculationVehicule != null ? immatriculationVehicule : "") + ")" +
                ", entrée=" + getDateEntreeFormatee() +
                ", sortie=" + getDateSortieFormatee() +
                ", type=" + getTypeLibelle() +
                ", statut=" + getStatutLibelle() +
                ", coût=" + getCoutFormate() +
                '}';
    }
}
