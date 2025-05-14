package main.java.com.miage.parcauto.model.mission;

import main.java.com.miage.parcauto.model.vehicule.Vehicule;
// Importer SocietaireCompte ou Personnel si un conducteur/responsable est directement lié ici.
// Pour l'instant, la table MISSION n'a pas de FK directe vers un conducteur,
// cela est géré par AFFECTATION. Si une mission a un responsable spécifique non lié
// à une affectation formelle, il faudrait un champ ici.

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entité représentant une mission effectuée avec un véhicule.
 * Correspond à un enregistrement de la table MISSION.
 */
public class Mission implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idMission;
    private Vehicule vehicule; // Relation avec Vehicule (via id_vehicule)
    private String libMission; // Motif ou libellé de la mission
    private String site; // Destination ou site principal de la mission
    private LocalDateTime dateDebutMission;
    private LocalDateTime dateFinMission; // Peut être la date de fin prévue ou réelle
    private Integer kmPrevu;
    private Integer kmReel;
    private StatutMission status;
    private BigDecimal coutTotal; // Coût total enregistré pour la mission (peut être fixe ou calculé)
    private String circuitMission; // Itinéraire ou circuit
    private String observationMission;

    private List<DepenseMission> depenses; // Liste des dépenses associées à cette mission

    /**
     * Constructeur par défaut.
     */
    public Mission() {
        this.depenses = new ArrayList<>();
        this.status = StatutMission.PLANIFIEE; // Valeur par défaut
    }

    /**
     * Constructeur avec tous les paramètres.
     *
     * @param idMission L'identifiant unique de la mission.
     * @param vehicule Le véhicule associé à la mission.
     * @param libMission Le libellé ou motif de la mission.
     * @param site Le site ou la destination principale.
     * @param dateDebutMission La date et heure de début de la mission.
     * @param dateFinMission La date et heure de fin (prévue ou réelle) de la mission.
     * @param kmPrevu Le kilométrage prévu pour la mission.
     * @param kmReel Le kilométrage réel effectué.
     * @param status Le statut actuel de la mission.
     * @param coutTotal Le coût total enregistré pour la mission.
     * @param circuitMission L'itinéraire ou le circuit de la mission.
     * @param observationMission Observations ou remarques concernant la mission.
     */
    public Mission(Integer idMission, Vehicule vehicule, String libMission, String site,
                   LocalDateTime dateDebutMission, LocalDateTime dateFinMission, Integer kmPrevu,
                   Integer kmReel, StatutMission status, BigDecimal coutTotal,
                   String circuitMission, String observationMission) {
        this.idMission = idMission;
        this.vehicule = vehicule;
        this.libMission = libMission;
        this.site = site;
        this.dateDebutMission = dateDebutMission;
        this.dateFinMission = dateFinMission;
        this.kmPrevu = kmPrevu;
        this.kmReel = kmReel;
        this.status = status;
        this.coutTotal = coutTotal;
        this.circuitMission = circuitMission;
        this.observationMission = observationMission;
        this.depenses = new ArrayList<>();
    }

    // Getters et Setters

    public Integer getIdMission() {
        return idMission;
    }

    public void setIdMission(Integer idMission) {
        this.idMission = idMission;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
    }

    public String getLibMission() {
        return libMission;
    }

    public void setLibMission(String libMission) {
        this.libMission = libMission;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public LocalDateTime getDateDebutMission() {
        return dateDebutMission;
    }

    public void setDateDebutMission(LocalDateTime dateDebutMission) {
        this.dateDebutMission = dateDebutMission;
    }

    public LocalDateTime getDateFinMission() {
        return dateFinMission;
    }

    public void setDateFinMission(LocalDateTime dateFinMission) {
        this.dateFinMission = dateFinMission;
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

    public StatutMission getStatus() {
        return status;
    }

    public void setStatus(StatutMission status) {
        this.status = status;
    }

    public BigDecimal getCoutTotal() {
        return coutTotal;
    }

    public void setCoutTotal(BigDecimal coutTotal) {
        this.coutTotal = coutTotal;
    }

    public String getCircuitMission() {
        return circuitMission;
    }

    public void setCircuitMission(String circuitMission) {
        this.circuitMission = circuitMission;
    }

    public String getObservationMission() {
        return observationMission;
    }

    public void setObservationMission(String observationMission) {
        this.observationMission = observationMission;
    }

    public List<DepenseMission> getDepenses() {
        return depenses;
    }

    public void setDepenses(List<DepenseMission> depenses) {
        this.depenses = depenses;
    }

    /**
     * Ajoute une dépense à la mission.
     * @param depense La dépense à ajouter.
     */
    public void addDepense(DepenseMission depense) {
        if (depense != null) {
            this.depenses.add(depense);
            depense.setMission(this); // Assurer la bidirectionnalité
        }
    }

    /**
     * Supprime une dépense de la mission.
     * @param depense La dépense à supprimer.
     */
    public void removeDepense(DepenseMission depense) {
        if (depense != null) {
            this.depenses.remove(depense);
            depense.setMission(null);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mission mission = (Mission) o;
        return Objects.equals(idMission, mission.idMission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMission);
    }

    @Override
    public String toString() {
        return "Mission{" +
                "idMission=" + idMission +
                ", libMission='" + libMission + '\'' +
                ", vehicule=" + (vehicule != null ? vehicule.getImmatriculation() : "N/A") +
                ", status=" + status +
                ", dateDebutMission=" + dateDebutMission +
                '}';
    }
}