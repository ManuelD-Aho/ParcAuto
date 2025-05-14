package main.java.com.miage.parcauto.model.mission;

import java.io.Serializable;
import java.math.BigDecimal;
// La date n'est pas dans la table DEPENSE_MISSION, elle est implicitement liée à la mission.
// Si une date spécifique par dépense est nécessaire, il faudrait l'ajouter à la table et ici.
import java.util.Objects;

/**
 * Entité représentant une dépense spécifique associée à une mission.
 * Correspond à un enregistrement de la table DEPENSE_MISSION.
 */
public class DepenseMission implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id; // Nommé 'id' dans la table
    private Mission mission; // Relation avec Mission (via id_mission)
    private NatureDepenseMission nature;
    private BigDecimal montant;
    private String justificatif; // Chemin ou référence vers le justificatif

    /**
     * Constructeur par défaut.
     */
    public DepenseMission() {
    }

    /**
     * Constructeur avec tous les paramètres.
     *
     * @param id L'identifiant unique de la dépense.
     * @param mission La mission à laquelle cette dépense est rattachée.
     * @param nature La nature de la dépense (Carburant, FraisAnnexes).
     * @param montant Le montant de la dépense.
     * @param justificatif Le chemin ou la référence du justificatif.
     */
    public DepenseMission(Integer id, Mission mission, NatureDepenseMission nature,
                          BigDecimal montant, String justificatif) {
        this.id = id;
        this.mission = mission;
        this.nature = nature;
        this.montant = montant;
        this.justificatif = justificatif;
    }

    // Getters et Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public NatureDepenseMission getNature() {
        return nature;
    }

    public void setNature(NatureDepenseMission nature) {
        this.nature = nature;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public String getJustificatif() {
        return justificatif;
    }

    public void setJustificatif(String justificatif) {
        this.justificatif = justificatif;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DepenseMission that = (DepenseMission) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DepenseMission{" +
                "id=" + id +
                ", nature=" + nature +
                ", montant=" + montant +
                ", missionId=" + (mission != null ? mission.getIdMission() : "N/A") +
                '}';
    }
}