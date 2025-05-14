package main.java.com.miage.parcauto.dto;

import main.java.com.miage.parcauto.model.mission.Mission.StatutMission;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MissionDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private Integer idMission;
    private Integer idVehicule;
    private String infoVehicule;
    private String immatriculationVehicule; // Kept for convenience, maps to VEHICULES.immatriculation
    private Integer idSocietaire; // From AFFECTATION or dedicated mission assignment logic
    private String nomPrenomSocietaire;
    private String destination; // Maps to MISSION.site
    private String motif; // Maps to MISSION.lib_mission
    private LocalDateTime dateDepart; // Maps to MISSION.date_debut_mission
    private LocalDateTime dateRetourPrevue; // Maps to MISSION.date_fin_mission (if planned)
    private StatutMission statut; // Maps to MISSION.status (Planifiee, EnCours, Cloturee)
    private Integer kmDepart; // Initial vehicle km, not directly in MISSION table, but Vehicule.kmActuels at start
    private Integer kmRetour; // Vehicle km at end, used to update Vehicule.kmActuels if MISSION.km_reel
    private Integer kmPrevu; // Maps to MISSION.km_prevu
    private Integer kmReel; // Maps to MISSION.km_reel
    private LocalDateTime dateRetourReelle; // Actual return date, MISSION.date_fin_mission if status is Cloturee
    private String observations; // Maps to MISSION.observation_mission
    private String circuitMission; // Maps to MISSION.circuit_mission
    private BigDecimal coutTotalCalcule; // Calculated from expenses, distinct from MISSION.cout_total if that's stored fixed
    private BigDecimal coutTotalDeclare; // Maps to MISSION.cout_total (potentially fixed/declared amount)
    private List<DepenseDTO> depenses;

    public MissionDTO() {
        this.statut = StatutMission.Planifiee;
        this.depenses = new ArrayList<>();
        this.coutTotalCalcule = BigDecimal.ZERO;
        this.coutTotalDeclare = BigDecimal.ZERO;
    }

    public MissionDTO(Integer idMission, Integer idVehicule, String infoVehicule, String immatriculationVehicule,
                      Integer idSocietaire, String nomPrenomSocietaire, String destination, String motif,
                      LocalDateTime dateDepart, LocalDateTime dateRetourPrevue, StatutMission statut,
                      Integer kmDepart, Integer kmRetour, Integer kmPrevu, Integer kmReel,
                      LocalDateTime dateRetourReelle, String observations, String circuitMission,
                      BigDecimal coutTotalDeclare, List<DepenseDTO> depenses) {
        this.idMission = idMission;
        this.idVehicule = idVehicule;
        this.infoVehicule = infoVehicule;
        this.immatriculationVehicule = immatriculationVehicule;
        this.idSocietaire = idSocietaire;
        this.nomPrenomSocietaire = nomPrenomSocietaire;
        this.destination = destination;
        this.motif = motif;
        this.dateDepart = dateDepart;
        this.dateRetourPrevue = dateRetourPrevue;
        this.statut = statut;
        this.kmDepart = kmDepart;
        this.kmRetour = kmRetour;
        this.kmPrevu = kmPrevu;
        this.kmReel = kmReel;
        this.dateRetourReelle = dateRetourReelle;
        this.observations = observations;
        this.circuitMission = circuitMission;
        this.depenses = depenses != null ? depenses : new ArrayList<>();
        this.coutTotalDeclare = coutTotalDeclare != null ? coutTotalDeclare : BigDecimal.ZERO;
        recalculerCoutTotalCalcule();
    }

    private void recalculerCoutTotalCalcule() {
        this.coutTotalCalcule = BigDecimal.ZERO;
        if (this.depenses != null) {
            for (DepenseDTO depense : this.depenses) {
                if (depense != null && depense.getMontant() != null) {
                    this.coutTotalCalcule = this.coutTotalCalcule.add(depense.getMontant());
                }
            }
        }
    }

    public Integer getIdMission() {
        return idMission;
    }

    public void setIdMission(Integer idMission) {
        this.idMission = idMission;
    }

    public Integer getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(Integer idVehicule) {
        this.idVehicule = idVehicule;
    }

    public String getInfoVehicule() {
        return infoVehicule;
    }

    public void setInfoVehicule(String infoVehicule) {
        this.infoVehicule = infoVehicule;
    }

    public String getImmatriculationVehicule() {
        return immatriculationVehicule;
    }

    public void setImmatriculationVehicule(String immatriculationVehicule) {
        this.immatriculationVehicule = immatriculationVehicule;
    }

    public Integer getIdSocietaire() {
        return idSocietaire;
    }

    public void setIdSocietaire(Integer idSocietaire) {
        this.idSocietaire = idSocietaire;
    }

    public String getNomPrenomSocietaire() {
        return nomPrenomSocietaire;
    }

    public void setNomPrenomSocietaire(String nomPrenomSocietaire) {
        this.nomPrenomSocietaire = nomPrenomSocietaire;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public LocalDateTime getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(LocalDateTime dateDepart) {
        this.dateDepart = dateDepart;
    }

    public LocalDateTime getDateRetourPrevue() {
        return dateRetourPrevue;
    }

    public void setDateRetourPrevue(LocalDateTime dateRetourPrevue) {
        this.dateRetourPrevue = dateRetourPrevue;
    }

    public StatutMission getStatut() {
        return statut;
    }

    public void setStatut(StatutMission statut) {
        this.statut = statut;
    }

    public Integer getKmDepart() {
        return kmDepart;
    }

    public void setKmDepart(Integer kmDepart) {
        this.kmDepart = kmDepart;
    }

    public Integer getKmRetour() {
        return kmRetour;
    }

    public void setKmRetour(Integer kmRetour) {
        this.kmRetour = kmRetour;
    }

    public Integer getKmPrevu() {
        return kmPrevu;
    }

    public void setKmPrevu(Integer kmPrevu) {
        this.kmPrevu = kmPrevu;
    }

    public Integer getKmReel() {
        return kmReel;
    }

    public void setKmReel(Integer kmReel) {
        this.kmReel = kmReel;
    }

    public LocalDateTime getDateRetourReelle() {
        return dateRetourReelle;
    }

    public void setDateRetourReelle(LocalDateTime dateRetourReelle) {
        this.dateRetourReelle = dateRetourReelle;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getCircuitMission() {
        return circuitMission;
    }

    public void setCircuitMission(String circuitMission) {
        this.circuitMission = circuitMission;
    }

    public BigDecimal getCoutTotalCalcule() {
        return coutTotalCalcule;
    }

    public String getCoutTotalCalculeFormate() {
        return coutTotalCalcule != null ? coutTotalCalcule.setScale(2, RoundingMode.HALF_UP) + " FCFA" : "0.00 FCFA";
    }

    public BigDecimal getCoutTotalDeclare() {
        return coutTotalDeclare;
    }

    public void setCoutTotalDeclare(BigDecimal coutTotalDeclare) {
        this.coutTotalDeclare = coutTotalDeclare;
    }

    public String getCoutTotalDeclareFormate() {
        return coutTotalDeclare != null ? coutTotalDeclare.setScale(2, RoundingMode.HALF_UP) + " FCFA" : "0.00 FCFA";
    }

    public List<DepenseDTO> getDepenses() {
        return depenses;
    }

    public void setDepenses(List<DepenseDTO> depenses) {
        this.depenses = depenses;
        recalculerCoutTotalCalcule();
    }

    public void ajouterDepense(DepenseDTO depense) {
        if (depense != null) {
            if (this.depenses == null) {
                this.depenses = new ArrayList<>();
            }
            this.depenses.add(depense);
            recalculerCoutTotalCalcule();
        }
    }

    public boolean supprimerDepense(DepenseDTO depense) {
        boolean removed = false;
        if (depense != null && this.depenses != null) {
            removed = this.depenses.remove(depense);
            if (removed) {
                recalculerCoutTotalCalcule();
            }
        }
        return removed;
    }

    public long getDureePrevueJours() {
        if (dateDepart != null && dateRetourPrevue != null) {
            return ChronoUnit.DAYS.between(dateDepart.toLocalDate(), dateRetourPrevue.toLocalDate());
        }
        return 0;
    }

    public long getDureeReelleJours() {
        if (dateDepart != null && dateRetourReelle != null) {
            return ChronoUnit.DAYS.between(dateDepart.toLocalDate(), dateRetourReelle.toLocalDate());
        }
        return (statut == StatutMission.Cloturee || statut == StatutMission.Terminee) ? 0 : getDureePrevueJours();
    }

    public int getDistanceParcourue() {
        if (kmReel != null && kmReel > 0 && kmDepart != null && kmDepart >= 0) {
            return kmReel - kmDepart;
        } else if (kmRetour != null && kmRetour > 0 && kmDepart != null && kmDepart >= 0) { // fallback to kmRetour if kmReel not set
            return kmRetour - kmDepart;
        }
        return 0;
    }

    public boolean isEnRetard() {
        if (statut != StatutMission.Terminee && statut != StatutMission.Cloturee && dateRetourPrevue != null) {
            return LocalDateTime.now().isAfter(dateRetourPrevue);
        }
        return false;
    }

    public String getDateDepartFormatee() {
        return dateDepart != null ? dateDepart.format(DATE_FORMATTER) : "Non définie";
    }

    public String getDateRetourPrevueFormatee() {
        return dateRetourPrevue != null ? dateRetourPrevue.format(DATE_FORMATTER) : "Non définie";
    }

    public String getDateRetourReelleFormatee() {
        return dateRetourReelle != null ? dateRetourReelle.format(DATE_FORMATTER) : "Non définie";
    }

    public String getStatutLibelle() {
        return statut != null ? statut.getLibelle() : "";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MissionDTO that = (MissionDTO) o;
        return Objects.equals(idMission, that.idMission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMission);
    }

    @Override
    public String toString() {
        return "MissionDTO{" +
                "idMission=" + idMission +
                ", idVehicule=" + idVehicule +
                ", destination='" + destination + '\'' +
                ", statut=" + (statut != null ? statut.name() : "N/A") +
                ", dateDepart=" + getDateDepartFormatee() +
                ", dateRetourPrevue=" + getDateRetourPrevueFormatee() +
                '}';
    }
}