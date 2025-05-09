package main.java.com.miage.parcauto.model.mission;

import main.java.com.miage.parcauto.model.vehicule.Vehicule;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Classe modèle représentant une mission dans le système ParcAuto.
 * Cette classe correspond à la table MISSION dans la base de données.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class Mission implements Serializable, Comparable<Mission> {

    private static final long serialVersionUID = 1L;

    /**
     * Statuts possibles d'une mission
     */
    public enum StatusMission {
        Planifiee("Planifiée"),
        EnCours("En cours"),
        Cloturee("Clôturée");

        private final String libelle;

        StatusMission(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }

        /**
         * Convertit une chaîne en statut mission
         * @param value La valeur à convertir
         * @return Le StatusMission correspondant ou Planifiee par défaut
         */
        public static StatusMission fromString(String value) {
            if (value == null) return Planifiee;

            try {
                return StatusMission.valueOf(value);
            } catch (IllegalArgumentException e) {
                return Planifiee;
            }
        }
    }

    // Attributs correspondant aux colonnes de la table MISSION
    private Integer idMission;
    private Integer idVehicule;
    private String libMission;
    private String site;
    private LocalDateTime dateDebutMission;
    private LocalDateTime dateFinMission;
    private Integer kmPrevu;
    private Integer kmReel;
    private StatusMission status;
    private BigDecimal coutTotal;
    private String circuitMission;
    private String observationMission;

    // Attributs non stockés en BDD
    private Vehicule vehicule;  // Relation avec le véhicule utilisé
    private List<DepenseMission> depenses;  // Liste des dépenses associées

    /**
     * Constructeur par défaut
     */
    public Mission() {
        this.status = StatusMission.Planifiee;
        this.coutTotal = BigDecimal.ZERO;
        this.kmPrevu = 0;
        this.kmReel = 0;
        this.depenses = new ArrayList<>();
    }

    /**
     * Constructeur avec attributs essentiels
     *
     * @param idVehicule ID du véhicule affecté à la mission
     * @param libMission Libellé de la mission
     * @param site Site de destination
     * @param dateDebutMission Date de début prévue
     * @param dateFinMission Date de fin prévue
     * @param kmPrevu Kilométrage prévu
     */
    public Mission(Integer idVehicule, String libMission, String site,
                   LocalDateTime dateDebutMission, LocalDateTime dateFinMission, Integer kmPrevu) {
        this.idVehicule = idVehicule;
        this.libMission = libMission;
        this.site = site;
        this.dateDebutMission = dateDebutMission;
        this.dateFinMission = dateFinMission;
        this.kmPrevu = kmPrevu != null ? kmPrevu : 0;
        this.kmReel = 0;
        this.status = StatusMission.Planifiee;
        this.coutTotal = BigDecimal.ZERO;
        this.depenses = new ArrayList<>();
    }

    /**
     * Constructeur avec véhicule
     *
     * @param vehicule Véhicule affecté à la mission
     * @param libMission Libellé de la mission
     * @param site Site de destination
     * @param dateDebutMission Date de début prévue
     * @param dateFinMission Date de fin prévue
     * @param kmPrevu Kilométrage prévu
     */
    public Mission(Vehicule vehicule, String libMission, String site,
                   LocalDateTime dateDebutMission, LocalDateTime dateFinMission, Integer kmPrevu) {
        this.vehicule = vehicule;
        this.idVehicule = vehicule != null ? vehicule.getIdVehicule() : null;
        this.libMission = libMission;
        this.site = site;
        this.dateDebutMission = dateDebutMission;
        this.dateFinMission = dateFinMission;
        this.kmPrevu = kmPrevu != null ? kmPrevu : 0;
        this.kmReel = 0;
        this.status = StatusMission.Planifiee;
        this.coutTotal = BigDecimal.ZERO;
        this.depenses = new ArrayList<>();
    }

    // Getters et Setters

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

    public StatusMission getStatus() {
        return status;
    }

    public void setStatus(StatusMission status) {
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

    public Vehicule getVehicule() {
        return vehicule;
    }

    public void setVehicule(Vehicule vehicule) {
        this.vehicule = vehicule;
        if (vehicule != null) {
            this.idVehicule = vehicule.getIdVehicule();
        }
    }

    // Gestion des dépenses

    public List<DepenseMission> getDepenses() {
        return depenses;
    }

    public void setDepenses(List<DepenseMission> depenses) {
        this.depenses = depenses != null ? depenses : new ArrayList<>();
        calculerCoutTotal();
    }

    /**
     * Ajoute une dépense à la mission
     *
     * @param depense La dépense à ajouter
     * @return true si la dépense a été ajoutée
     */
    public boolean ajouterDepense(DepenseMission depense) {
        if (depense == null) {
            return false;
        }

        if (this.depenses == null) {
            this.depenses = new ArrayList<>();
        }

        depense.setIdMission(this.idMission);
        boolean resultat = this.depenses.add(depense);
        calculerCoutTotal();
        return resultat;
    }

    /**
     * Supprime une dépense de la mission
     *
     * @param depense La dépense à supprimer
     * @return true si la dépense a été supprimée
     */
    public boolean supprimerDepense(DepenseMission depense) {
        if (depense == null || this.depenses == null) {
            return false;
        }

        boolean resultat = this.depenses.remove(depense);
        calculerCoutTotal();
        return resultat;
    }

    /**
     * Calcule le coût total en additionnant toutes les dépenses
     */
    public void calculerCoutTotal() {
        BigDecimal total = BigDecimal.ZERO;

        if (this.depenses != null) {
            for (DepenseMission depense : this.depenses) {
                if (depense.getMontant() != null) {
                    total = total.add(depense.getMontant());
                }
            }
        }

        this.coutTotal = total;
    }

    // Méthodes métier

    /**
     * Vérifie si la mission est planifiée mais pas encore démarrée
     *
     * @return true si la mission est planifiée
     */
    public boolean estPlanifiee() {
        return status == StatusMission.Planifiee;
    }

    /**
     * Vérifie si la mission est en cours
     *
     * @return true si la mission est en cours
     */
    public boolean estEnCours() {
        return status == StatusMission.EnCours;
    }

    /**
     * Vérifie si la mission est clôturée
     *
     * @return true si la mission est clôturée
     */
    public boolean estCloturee() {
        return status == StatusMission.Cloturee;
    }

    /**
     * Démarre la mission (passage au statut "en cours")
     *
     * @return true si le changement d'état a été effectué
     */
    public boolean demarrer() {
        if (estPlanifiee()) {
            status = StatusMission.EnCours;
            return true;
        }
        return false;
    }

    /**
     * Clôture la mission (passage au statut "clôturée")
     *
     * @param kmReel Kilométrage réel parcouru
     * @return true si le changement d'état a été effectué
     */
    public boolean cloturer(Integer kmReel) {
        if (estEnCours()) {
            status = StatusMission.Cloturee;
            this.kmReel = kmReel;
            return true;
        }
        return false;
    }

    /**
     * Vérifie si la mission est actuellement active (en cours ou prévue aujourd'hui)
     *
     * @return true si la mission est active
     */
    public boolean estActive() {
        if (estEnCours()) {
            return true;
        }

        LocalDateTime now = LocalDateTime.now();
        return estPlanifiee() &&
                dateDebutMission != null &&
                dateDebutMission.toLocalDate().isEqual(now.toLocalDate());
    }

    /**
     * Calcule la durée de la mission en jours
     *
     * @return Nombre de jours entre début et fin
     */
    public long getDureeJours() {
        if (dateDebutMission == null || dateFinMission == null) {
            return 0;
        }

        return ChronoUnit.DAYS.between(dateDebutMission.toLocalDate(), dateFinMission.toLocalDate()) + 1;
    }

    /**
     * Calcule l'écart entre kilométrage prévu et réel
     *
     * @return Ecart en km (positif si dépassement)
     */
    public int getEcartKm() {
        if (kmReel == null || kmReel == 0) {
            return 0;
        }

        return kmReel - (kmPrevu != null ? kmPrevu : 0);
    }

    /**
     * Vérifie si le kilométrage réel dépasse le prévu
     *
     * @return true si dépassement
     */
    public boolean aDepassementKm() {
        return getEcartKm() > 0;
    }

    /**
     * Calcule le coût par kilomètre
     *
     * @return Coût par km ou zéro si pas de km
     */
    public BigDecimal getCoutParKm() {
        if (kmReel == null || kmReel <= 0 || coutTotal == null) {
            return BigDecimal.ZERO;
        }

        return coutTotal.divide(new BigDecimal(kmReel), 2, RoundingMode.HALF_UP);
    }

    /**
     * Vérifie si la mission est en retard (toujours en cours après la date de fin prévue)
     *
     * @return true si la mission est en retard
     */
    public boolean estEnRetard() {
        return estEnCours() &&
                dateFinMission != null &&
                LocalDateTime.now().isAfter(dateFinMission);
    }

    /**
     * Formate une date selon un format donné
     *
     * @param date La date à formater
     * @param format Format de date (ex: "dd/MM/yyyy")
     * @return La date formatée ou chaîne vide si date null
     */
    public String formatDate(LocalDateTime date, String format) {
        if (date == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return date.format(formatter);
    }

    /**
     * Retourne la date de début formatée
     *
     * @return La date formatée (jj/mm/aaaa)
     */
    public String getDateDebutFormatted() {
        return formatDate(dateDebutMission, "dd/MM/yyyy");
    }

    /**
     * Retourne la date de fin formatée
     *
     * @return La date formatée (jj/mm/aaaa)
     */
    public String getDateFinFormatted() {
        return formatDate(dateFinMission, "dd/MM/yyyy");
    }

    /**
     * Détermine si la mission chevauche une autre mission
     *
     * @param autreMission La mission à comparer
     * @return true si les périodes se chevauchent
     */
    public boolean chevauche(Mission autreMission) {
        if (autreMission == null ||
                dateDebutMission == null || dateFinMission == null ||
                autreMission.getDateDebutMission() == null || autreMission.getDateFinMission() == null) {
            return false;
        }

        // Vérifier si les missions concernent le même véhicule
        if (!Objects.equals(idVehicule, autreMission.getIdVehicule())) {
            return false;
        }

        // Chevauchement si une mission commence pendant l'autre
        boolean debutDansPeriode = (dateDebutMission.isEqual(autreMission.getDateDebutMission()) ||
                dateDebutMission.isAfter(autreMission.getDateDebutMission())) &&
                dateDebutMission.isBefore(autreMission.getDateFinMission());

        boolean finDansPeriode = (dateFinMission.isEqual(autreMission.getDateFinMission()) ||
                dateFinMission.isBefore(autreMission.getDateFinMission())) &&
                dateFinMission.isAfter(autreMission.getDateDebutMission());

        boolean englobePeriode = dateDebutMission.isBefore(autreMission.getDateDebutMission()) &&
                dateFinMission.isAfter(autreMission.getDateFinMission());

        return debutDansPeriode || finDansPeriode || englobePeriode;
    }

    /**
     * Retourne la couleur CSS associée au statut
     *
     * @return Code couleur hex pour affichage en UI
     */
    public String getColorStyle() {
        switch (status) {
            case Planifiee: return "#ffc107"; // Jaune/orange
            case EnCours: return "#17a2b8";   // Bleu
            case Cloturee: return "#28a745";  // Vert
            default: return "#6c757d";        // Gris
        }
    }

    /**
     * Retourne la classe CSS associée au statut
     *
     * @return Nom de classe CSS
     */
    public String getStatusClass() {
        switch (status) {
            case Planifiee: return "mission-planifiee";
            case EnCours: return "mission-en-cours";
            case Cloturee: return "mission-cloturee";
            default: return "";
        }
    }

    /**
     * Contrôle la validité des données de la mission
     *
     * @return true si les données sont valides, false sinon
     */
    public boolean isValid() {
        return idVehicule != null
                && libMission != null && !libMission.trim().isEmpty()
                && site != null && !site.trim().isEmpty()
                && dateDebutMission != null
                && dateFinMission != null
                && dateDebutMission.isBefore(dateFinMission)
                && kmPrevu != null && kmPrevu >= 0;
    }

    // Méthodes standard

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Mission mission = (Mission) obj;
        return Objects.equals(idMission, mission.idMission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMission);
    }

    @Override
    public String toString() {
        return String.format("Mission '%s' à %s - %s",
                libMission,
                site,
                status.getLibelle());
    }

    /**
     * Compare les missions par date de début
     *
     * @param autre L'autre mission à comparer
     * @return résultat de la comparaison
     */
    @Override
    public int compareTo(Mission autre) {
        if (autre == null || autre.getDateDebutMission() == null) {
            return -1;
        }
        if (this.dateDebutMission == null) {
            return 1;
        }
        return this.dateDebutMission.compareTo(autre.getDateDebutMission());
    }

    /**
     * Retourne un comparateur pour trier les missions par statut
     *
     * @return Comparateur de missions par statut
     */
    public static Comparator<Mission> getComparateurParStatus() {
        return Comparator.comparing(
                mission -> mission.getStatus() != null ? mission.getStatus().ordinal() : 0
        );
    }
}