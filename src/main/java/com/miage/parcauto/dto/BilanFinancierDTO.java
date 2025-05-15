package main.java.com.miage.parcauto.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BilanFinancierDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private BigDecimal totalRevenus; // Provenant par exemple des mensualités des sociétaires
    private BigDecimal totalDepensesVehicules; // Coûts d'entretien, assurances, etc.
    private BigDecimal totalDepensesMissions; // Carburant, frais annexes des missions
    private BigDecimal soldeGlobal;
    private List<MouvementDTO> detailsMouvements; // Liste des mouvements inclus dans le bilan
    private List<String> notes; // Pour des observations ou des points importants

    // Pourrait inclure des agrégats plus spécifiques
    // private Map<String, BigDecimal> depensesParCategorieVehicule;
    // private Map<String, BigDecimal> revenusParTypeSocietaire;


    public BilanFinancierDTO() {
        this.totalRevenus = BigDecimal.ZERO;
        this.totalDepensesVehicules = BigDecimal.ZERO;
        this.totalDepensesMissions = BigDecimal.ZERO;
        this.soldeGlobal = BigDecimal.ZERO;
        this.detailsMouvements = new ArrayList<>();
        this.notes = new ArrayList<>();
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public BigDecimal getTotalRevenus() {
        return totalRevenus;
    }

    public void setTotalRevenus(BigDecimal totalRevenus) {
        this.totalRevenus = totalRevenus;
    }

    public BigDecimal getTotalDepensesVehicules() {
        return totalDepensesVehicules;
    }

    public void setTotalDepensesVehicules(BigDecimal totalDepensesVehicules) {
        this.totalDepensesVehicules = totalDepensesVehicules;
    }

    public BigDecimal getTotalDepensesMissions() {
        return totalDepensesMissions;
    }

    public void setTotalDepensesMissions(BigDecimal totalDepensesMissions) {
        this.totalDepensesMissions = totalDepensesMissions;
    }

    public BigDecimal getSoldeGlobal() {
        return soldeGlobal;
    }

    public void setSoldeGlobal(BigDecimal soldeGlobal) {
        this.soldeGlobal = soldeGlobal;
    }

    public List<MouvementDTO> getDetailsMouvements() {
        return detailsMouvements;
    }

    public void setDetailsMouvements(List<MouvementDTO> detailsMouvements) {
        this.detailsMouvements = detailsMouvements;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "BilanFinancierDTO{" +
                "dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", soldeGlobal=" + soldeGlobal +
                '}';
    }
}