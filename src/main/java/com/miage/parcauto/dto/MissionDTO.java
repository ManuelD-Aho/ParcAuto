package main.java.com.miage.parcauto.dto;

import main.java.com.miage.parcauto.model.mission.Mission.StatutMission;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DTO (Data Transfer Object) pour la représentation des missions.
 * Utilisé pour le transfert des données entre les couches service et
 * présentation.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class MissionDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private Integer idMission;
    private Integer idVehicule;
    private String infoVehicule; // marque + modèle + immatriculation
    private String immatriculation;
    private Integer idSocietaire;
    private String nomPrenomSocietaire;
    private String destination;
    private String motif;
    private LocalDateTime dateDepart;
    private LocalDateTime dateRetourPrevue;
    private StatutMission statut;
    private int kmDepart;
    private int kmRetour;
    private LocalDateTime dateRetourReelle;
    private String observations;
    private BigDecimal coutTotal;
    private List<DepenseDTO> depenses;

    /**
     * Constructeur par défaut.
     */
    public MissionDTO() {
        this.statut = StatutMission.Planifiee;
        this.depenses = new ArrayList<>();
        this.coutTotal = BigDecimal.ZERO;
    }

    /**
     * Constructeur complet.
     *
     * @param idMission           l'identifiant de la mission
     * @param idVehicule          l'identifiant du véhicule
     * @param infoVehicule        les informations du véhicule (marque + modèle +
     *                            immatriculation)
     * @param immatriculation     l'immatriculation du véhicule
     * @param idSocietaire        l'identifiant du sociétaire
     * @param nomPrenomSocietaire le nom et prénom du sociétaire
     * @param destination         la destination de la mission
     * @param motif               le motif de la mission
     * @param dateDepart          la date de départ de la mission
     * @param dateRetourPrevue    la date de retour prévue de la mission
     * @param statut              le statut de la mission
     * @param kmDepart            le kilométrage au départ de la mission
     * @param kmRetour            le kilométrage au retour de la mission
     * @param dateRetourReelle    la date de retour réelle de la mission
     * @param observations        les observations de la mission
     * @param coutTotal           le coût total de la mission
     * @param depenses            la liste des dépenses de la mission
     */
    public MissionDTO(Integer idMission, Integer idVehicule, String infoVehicule, String immatriculation,
            Integer idSocietaire, String nomPrenomSocietaire, String destination, String motif,
            LocalDateTime dateDepart, LocalDateTime dateRetourPrevue, StatutMission statut, int kmDepart, int kmRetour,
            LocalDateTime dateRetourReelle, String observations, BigDecimal coutTotal, List<DepenseDTO> depenses) {
        this.idMission = idMission;
        this.idVehicule = idVehicule;
        this.infoVehicule = infoVehicule;
        this.immatriculation = immatriculation;
        this.idSocietaire = idSocietaire;
        this.nomPrenomSocietaire = nomPrenomSocietaire;
        this.destination = destination;
        this.motif = motif;
        this.dateDepart = dateDepart;
        this.dateRetourPrevue = dateRetourPrevue;
        this.statut = statut;
        this.kmDepart = kmDepart;
        this.kmRetour = kmRetour;
        this.dateRetourReelle = dateRetourReelle;
        this.observations = observations;
        this.coutTotal = coutTotal;
        this.depenses = depenses != null ? depenses : new ArrayList<>();
    }

    /**
     * @return l'identifiant de la mission
     */
    public Integer getIdMission() {
        return idMission;
    }

    /**
     * @param idMission l'identifiant de la mission à définir
     */
    public void setIdMission(Integer idMission) {
        this.idMission = idMission;
    }

    /**
     * @return l'identifiant du véhicule
     */
    public Integer getIdVehicule() {
        return idVehicule;
    }

    /**
     * @param idVehicule l'identifiant du véhicule à définir
     */
    public void setIdVehicule(Integer idVehicule) {
        this.idVehicule = idVehicule;
    }

    /**
     * @return les informations du véhicule (marque + modèle + immatriculation)
     */
    public String getInfoVehicule() {
        return infoVehicule;
    }

    /**
     * @param infoVehicule les informations du véhicule à définir
     */
    public void setInfoVehicule(String infoVehicule) {
        this.infoVehicule = infoVehicule;
    }

    /**
     * @return l'immatriculation du véhicule
     */
    public String getImmatriculation() {
        return immatriculation;
    }

    /**
     * @param immatriculation l'immatriculation du véhicule à définir
     */
    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    /**
     * @return l'identifiant du sociétaire
     */
    public Integer getIdSocietaire() {
        return idSocietaire;
    }

    /**
     * @param idSocietaire l'identifiant du sociétaire à définir
     */
    public void setIdSocietaire(Integer idSocietaire) {
        this.idSocietaire = idSocietaire;
    }

    /**
     * @return le nom et prénom du sociétaire
     */
    public String getNomPrenomSocietaire() {
        return nomPrenomSocietaire;
    }

    /**
     * @param nomPrenomSocietaire le nom et prénom du sociétaire à définir
     */
    public void setNomPrenomSocietaire(String nomPrenomSocietaire) {
        this.nomPrenomSocietaire = nomPrenomSocietaire;
    }

    /**
     * @return la destination de la mission
     */
    public String getDestination() {
        return destination;
    }

    /**
     * @param destination la destination de la mission à définir
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * @return le motif de la mission
     */
    public String getMotif() {
        return motif;
    }

    /**
     * @param motif le motif de la mission à définir
     */
    public void setMotif(String motif) {
        this.motif = motif;
    }

    /**
     * @return la date de départ de la mission
     */
    public LocalDateTime getDateDepart() {
        return dateDepart;
    }

    /**
     * @param dateDepart la date de départ de la mission à définir
     */
    public void setDateDepart(LocalDateTime dateDepart) {
        this.dateDepart = dateDepart;
    }

    /**
     * @return la date de retour prévue de la mission
     */
    public LocalDateTime getDateRetourPrevue() {
        return dateRetourPrevue;
    }

    /**
     * @param dateRetourPrevue la date de retour prévue de la mission à définir
     */
    public void setDateRetourPrevue(LocalDateTime dateRetourPrevue) {
        this.dateRetourPrevue = dateRetourPrevue;
    }

    /**
     * @return le statut de la mission
     */
    public StatutMission getStatut() {
        return statut;
    }

    /**
     * @param statut le statut de la mission à définir
     */
    public void setStatut(StatutMission statut) {
        this.statut = statut;
    }

    /**
     * @return le kilométrage au départ de la mission
     */
    public int getKmDepart() {
        return kmDepart;
    }

    /**
     * @param kmDepart le kilométrage au départ de la mission à définir
     */
    public void setKmDepart(int kmDepart) {
        this.kmDepart = kmDepart;
    }

    /**
     * @return le kilométrage au retour de la mission
     */
    public int getKmRetour() {
        return kmRetour;
    }

    /**
     * @param kmRetour le kilométrage au retour de la mission à définir
     */
    public void setKmRetour(int kmRetour) {
        this.kmRetour = kmRetour;
    }

    /**
     * @return la date de retour réelle de la mission
     */
    public LocalDateTime getDateRetourReelle() {
        return dateRetourReelle;
    }

    /**
     * @param dateRetourReelle la date de retour réelle de la mission à définir
     */
    public void setDateRetourReelle(LocalDateTime dateRetourReelle) {
        this.dateRetourReelle = dateRetourReelle;
    }

    /**
     * @return les observations de la mission
     */
    public String getObservations() {
        return observations;
    }

    /**
     * @param observations les observations de la mission à définir
     */
    public void setObservations(String observations) {
        this.observations = observations;
    }

    /**
     * @return le coût total de la mission
     */
    public BigDecimal getCoutTotal() {
        return coutTotal;
    }

    /**
     * @param coutTotal le coût total de la mission à définir
     */
    public void setCoutTotal(BigDecimal coutTotal) {
        this.coutTotal = coutTotal;
    }

    /**
     * @return la liste des dépenses de la mission
     */
    public List<DepenseDTO> getDepenses() {
        return depenses;
    }

    /**
     * @param depenses la liste des dépenses de la mission à définir
     */
    public void setDepenses(List<DepenseDTO> depenses) {
        this.depenses = depenses;
    }

    /**
     * Ajoute une dépense à la mission et met à jour le coût total.
     *
     * @param depense La dépense à ajouter
     */
    public void ajouterDepense(DepenseDTO depense) {
        if (depense != null) {
            this.depenses.add(depense);
            if (depense.getMontant() != null) {
                this.coutTotal = this.coutTotal.add(depense.getMontant());
            }
        }
    }

    /**
     * Supprime une dépense de la mission et met à jour le coût total.
     *
     * @param depense La dépense à supprimer
     * @return true si la dépense a été supprimée, false sinon
     */
    public boolean supprimerDepense(DepenseDTO depense) {
        if (depense != null && this.depenses.remove(depense)) {
            if (depense.getMontant() != null) {
                this.coutTotal = this.coutTotal.subtract(depense.getMontant());
            }
            return true;
        }
        return false;
    }

    /**
     * Calcule la durée prévue de la mission en jours.
     *
     * @return La durée prévue en jours, ou 0 si les dates ne sont pas définies
     */
    public long getDureePrevueJours() {
        if (dateDepart != null && dateRetourPrevue != null) {
            return ChronoUnit.DAYS.between(dateDepart, dateRetourPrevue);
        }
        return 0;
    }

    /**
     * Calcule la durée réelle de la mission en jours.
     *
     * @return La durée réelle en jours, ou la durée prévue si la mission n'est pas
     *         terminée
     */
    public long getDureeReelleJours() {
        if (dateDepart != null && dateRetourReelle != null) {
            return ChronoUnit.DAYS.between(dateDepart, dateRetourReelle);
        }
        return getDureePrevueJours();
    }

    /**
     * Calcule la distance parcourue en kilomètres.
     *
     * @return La distance parcourue, ou 0 si le kilométrage de retour n'est pas
     *         défini
     */
    public int getDistanceParcourue() {
        if (kmRetour > 0 && kmDepart >= 0) {
            return kmRetour - kmDepart;
        }
        return 0;
    }

    /**
     * Vérifie si la mission est en retard par rapport à la date de retour prévue.
     *
     * @return true si la mission est en retard, false sinon
     */
    public boolean isEnRetard() {
        if (statut != StatutMission.Terminee && dateRetourPrevue != null) {
            return LocalDateTime.now().isAfter(dateRetourPrevue);
        }
        return false;
    }

    /**
     * Retourne la date de départ formatée.
     *
     * @return Date de départ formatée ou "Non définie" si null
     */
    public String getDateDepartFormatee() {
        return dateDepart != null ? dateDepart.format(DATE_FORMATTER) : "Non définie";
    }

    /**
     * Retourne la date de retour prévue formatée.
     *
     * @return Date de retour prévue formatée ou "Non définie" si null
     */
    public String getDateRetourPrevueFormatee() {
        return dateRetourPrevue != null ? dateRetourPrevue.format(DATE_FORMATTER) : "Non définie";
    }

    /**
     * Retourne la date de retour réelle formatée.
     *
     * @return Date de retour réelle formatée ou "Non définie" si null
     */
    public String getDateRetourReelleFormatee() {
        return dateRetourReelle != null ? dateRetourReelle.format(DATE_FORMATTER) : "Non définie";
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
                ", statut=" + statut +
                ", dateDepart=" + dateDepart +
                ", dateRetourPrevue=" + dateRetourPrevue +
                '}';
    }
}
