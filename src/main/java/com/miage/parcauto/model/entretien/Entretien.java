package main.java.com.miage.parcauto.model.entretien;

import java.math.BigDecimal;
import java.time.LocalDateTime;
// import main.java.com.miage.parcauto.model.entretien.TypeEntretien;
// import main.java.com.miage.parcauto.model.entretien.StatutOT;

public class Entretien {

    private Integer idEntretien;
    private Integer idVehicule;
    private LocalDateTime dateEntree; // SQL: date_entree_entr
    private LocalDateTime dateSortie; // SQL: date_sortie_entr
    private String motif;             // SQL: motif_entr
    private String observation;
    private BigDecimal cout;          // SQL: cout_entr (mappé à coutReel dans DTO, ou un champ générique ici)
    private String lieu;              // SQL: lieu_entr
    private TypeEntretien type;
    private StatutOT statut;       // SQL: statut_ot

    public Entretien() {
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

    public LocalDateTime getDateEntree() {
        return dateEntree;
    }

    public void setDateEntree(LocalDateTime dateEntree) {
        this.dateEntree = dateEntree;
    }

    public LocalDateTime getDateSortie() {
        return dateSortie;
    }

    public void setDateSortie(LocalDateTime dateSortie) {
        this.dateSortie = dateSortie;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public BigDecimal getCout() {
        return cout;
    }

    public void setCout(BigDecimal cout) {
        this.cout = cout;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public TypeEntretien getType() {
        return type;
    }

    public void setType(TypeEntretien type) {
        this.type = type;
    }

    public StatutOT getStatut() {
        return statut;
    }

    public void setStatut(StatutOT statut) {
        this.statut = statut;
    }
}