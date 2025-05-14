package main.java.com.miage.parcauto.dto;

import main.java.com.miage.parcauto.model.entretien.Entretien.TypeEntretien;
import main.java.com.miage.parcauto.model.entretien.Entretien.StatutOT;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class EntretienDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

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
    private TypeEntretien type; // Matches DB Enum: Preventif, Correctif
    private StatutOT statutOt; // Matches DB Enum: Ouvert, EnCours, Cloture

    public EntretienDTO() {
        this.coutEntr = BigDecimal.ZERO;
        this.type = TypeEntretien.Preventif; // Default value
        this.statutOt = StatutOT.Ouvert; // Default value
    }

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

    public long getDureeHeures() {
        if (dateEntreeEntr == null || dateSortieEntr == null) {
            return 0L;
        }
        return ChronoUnit.HOURS.between(dateEntreeEntr, dateSortieEntr);
    }

    public String getDateEntreeFormatee() {
        return dateEntreeEntr != null ? dateEntreeEntr.format(DATE_FORMATTER) : "Non planifiée";
    }

    public String getDateSortieFormatee() {
        return dateSortieEntr != null ? dateSortieEntr.format(DATE_FORMATTER) : "En cours";
    }

    public boolean isTermine() {
        return statutOt == StatutOT.Cloture && dateSortieEntr != null;
    }

    public String getStatutCssClass() {
        if (statutOt == null) return "";
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

    public String getCoutFormate() {
        return coutEntr != null ? coutEntr.setScale(2, RoundingMode.HALF_UP) + " FCFA" : "0.00 FCFA";
    }

    public String getTypeLibelle() {
        return type != null ? type.getLibelle() : (TypeEntretien.Preventif != null ? TypeEntretien.Preventif.getLibelle() : "");
    }

    public String getStatutLibelle() {
        return statutOt != null ? statutOt.getLibelle() : (StatutOT.Ouvert != null ? StatutOT.Ouvert.getLibelle() : "");
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