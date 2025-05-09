package main.java.com.miage.parcauto.model.entretien;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Classe modèle représentant un contrat d'assurance pour un ou plusieurs véhicules.
 * Cette classe correspond à la table ASSURANCE dans la base de données.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class Assurance implements Serializable {

    private static final long serialVersionUID = 1L;

    // Attributs correspondant aux colonnes de la table ASSURANCE
    private Integer numCarteAssurance;
    private LocalDateTime dateDebutAssurance;
    private LocalDateTime dateFinAssurance;
    private String agence;
    private BigDecimal coutAssurance;

    // Liste des IDs de véhicules couverts par cette assurance (relation many-to-many)
    private List<Integer> vehiculesCouverts;

    /**
     * Constructeur par défaut
     */
    public Assurance() {
        this.dateDebutAssurance = LocalDateTime.now();
        this.coutAssurance = BigDecimal.ZERO;
        this.vehiculesCouverts = new ArrayList<>();
    }

    /**
     * Constructeur avec attributs essentiels
     *
     * @param dateDebutAssurance Date de début du contrat
     * @param dateFinAssurance Date de fin du contrat
     * @param agence Nom de l'agence d'assurance
     * @param coutAssurance Coût du contrat
     */
    public Assurance(LocalDateTime dateDebutAssurance, LocalDateTime dateFinAssurance,
                     String agence, BigDecimal coutAssurance) {
        this.dateDebutAssurance = dateDebutAssurance;
        this.dateFinAssurance = dateFinAssurance;
        this.agence = agence;
        this.coutAssurance = coutAssurance;
        this.vehiculesCouverts = new ArrayList<>();
    }

    // Getters et Setters

    public Integer getNumCarteAssurance() {
        return numCarteAssurance;
    }

    public void setNumCarteAssurance(Integer numCarteAssurance) {
        this.numCarteAssurance = numCarteAssurance;
    }

    public LocalDateTime getDateDebutAssurance() {
        return dateDebutAssurance;
    }

    public void setDateDebutAssurance(LocalDateTime dateDebutAssurance) {
        this.dateDebutAssurance = dateDebutAssurance;
    }

    public LocalDateTime getDateFinAssurance() {
        return dateFinAssurance;
    }

    public void setDateFinAssurance(LocalDateTime dateFinAssurance) {
        this.dateFinAssurance = dateFinAssurance;
    }

    public String getAgence() {
        return agence;
    }

    public void setAgence(String agence) {
        this.agence = agence;
    }

    public BigDecimal getCoutAssurance() {
        return coutAssurance;
    }

    public void setCoutAssurance(BigDecimal coutAssurance) {
        this.coutAssurance = coutAssurance;
    }

    public List<Integer> getVehiculesCouverts() {
        return vehiculesCouverts;
    }

    public void setVehiculesCouverts(List<Integer> vehiculesCouverts) {
        this.vehiculesCouverts = vehiculesCouverts != null ? vehiculesCouverts : new ArrayList<>();
    }

    // Méthodes de gestion des véhicules couverts

    /**
     * Ajoute un véhicule à la liste des véhicules couverts
     *
     * @param idVehicule ID du véhicule à couvrir
     * @return true si ajouté, false si déjà présent
     */
    public boolean addVehicule(Integer idVehicule) {
        if (idVehicule == null) {
            return false;
        }
        if (this.vehiculesCouverts == null) {
            this.vehiculesCouverts = new ArrayList<>();
        }
        if (!this.vehiculesCouverts.contains(idVehicule)) {
            return this.vehiculesCouverts.add(idVehicule);
        }
        return false;
    }

    /**
     * Retire un véhicule de la liste des véhicules couverts
     *
     * @param idVehicule ID du véhicule à retirer
     * @return true si retiré, false si absent
     */
    public boolean removeVehicule(Integer idVehicule) {
        if (idVehicule == null || this.vehiculesCouverts == null) {
            return false;
        }
        return this.vehiculesCouverts.remove(idVehicule);
    }

    /**
     * Vérifie si un véhicule est couvert par cette assurance
     *
     * @param idVehicule ID du véhicule à vérifier
     * @return true si le véhicule est couvert, false sinon
     */
    public boolean couvreVehicule(Integer idVehicule) {
        return idVehicule != null &&
                this.vehiculesCouverts != null &&
                this.vehiculesCouverts.contains(idVehicule);
    }

    /**
     * Nombre de véhicules couverts par cette assurance
     *
     * @return le nombre de véhicules
     */
    public int getNombreVehicules() {
        return this.vehiculesCouverts != null ? this.vehiculesCouverts.size() : 0;
    }

    // Méthodes métier

    /**
     * Vérifie si l'assurance est valide à la date spécifiée
     *
     * @param date Date à vérifier
     * @return true si l'assurance est valide à cette date
     */
    public boolean estValideA(LocalDateTime date) {
        if (date == null) {
            date = LocalDateTime.now();
        }

        return dateDebutAssurance != null &&
                dateFinAssurance != null &&
                (date.isEqual(dateDebutAssurance) || date.isAfter(dateDebutAssurance)) &&
                (date.isEqual(dateFinAssurance) || date.isBefore(dateFinAssurance));
    }

    /**
     * Vérifie si l'assurance est valide aujourd'hui
     *
     * @return true si l'assurance est valide
     */
    public boolean estValide() {
        return estValideA(LocalDateTime.now());
    }

    /**
     * Calcule le nombre de jours jusqu'à l'expiration de l'assurance
     *
     * @return Nombre de jours restants, ou 0 si déjà expirée ou date non définie
     */
    public long joursRestants() {
        if (dateFinAssurance == null) {
            return 0;
        }

        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(dateFinAssurance)) {
            return 0;
        }

        return ChronoUnit.DAYS.between(now, dateFinAssurance);
    }

    /**
     * Vérifie si l'assurance arrive à échéance prochainement
     *
     * @param joursAvant Nombre de jours avant échéance pour alerte
     * @return true si l'échéance est proche
     */
    public boolean procheEcheance(int joursAvant) {
        return estValide() && joursRestants() <= joursAvant;
    }

    /**
     * Calcule la durée du contrat en mois
     *
     * @return Nombre de mois de couverture
     */
    public int getDureeContratMois() {
        if (dateDebutAssurance == null || dateFinAssurance == null) {
            return 0;
        }

        // Calcul approximatif en mois
        return (int) (
                (dateFinAssurance.getYear() - dateDebutAssurance.getYear()) * 12 +
                        (dateFinAssurance.getMonthValue() - dateDebutAssurance.getMonthValue())
        );
    }

    /**
     * Calcule le coût mensuel de l'assurance
     *
     * @return Le coût mensuel ou 0 si durée nulle
     */
    public BigDecimal getCoutMensuel() {
        int duree = getDureeContratMois();
        if (duree <= 0 || coutAssurance == null) {
            return BigDecimal.ZERO;
        }

        return coutAssurance.divide(BigDecimal.valueOf(duree), 2, RoundingMode.HALF_UP);
    }

    /**
     * Renouvelle l'assurance pour une période additionnelle
     *
     * @param mois Nombre de mois de renouvellement
     */
    public void renouveler(int mois) {
        if (mois <= 0 || dateFinAssurance == null) {
            return;
        }

        // Si déjà expirée, partir de la date actuelle
        LocalDateTime dateReference = estValide() ? dateFinAssurance : LocalDateTime.now();

        // Ajout des mois
        dateFinAssurance = dateReference.plusMonths(mois);
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
        return formatDate(dateDebutAssurance, "dd/MM/yyyy");
    }

    /**
     * Retourne la date de fin formatée
     *
     * @return La date formatée (jj/mm/aaaa)
     */
    public String getDateFinFormatted() {
        return formatDate(dateFinAssurance, "dd/MM/yyyy");
    }

    /**
     * Contrôle la validité des données de l'assurance
     *
     * @return true si les données sont valides, false sinon
     */
    public boolean isValid() {
        return dateDebutAssurance != null
                && dateFinAssurance != null
                && dateDebutAssurance.isBefore(dateFinAssurance)
                && agence != null && !agence.trim().isEmpty()
                && coutAssurance != null && coutAssurance.compareTo(BigDecimal.ZERO) > 0;
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
        Assurance assurance = (Assurance) obj;
        return Objects.equals(numCarteAssurance, assurance.numCarteAssurance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numCarteAssurance);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Assurance #").append(numCarteAssurance);

        if (agence != null) {
            sb.append(" (").append(agence).append(")");
        }

        sb.append(" - Validité: ");
        sb.append(getDateDebutFormatted()).append(" au ").append(getDateFinFormatted());

        return sb.toString();
    }
}