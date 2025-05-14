package main.java.com.miage.parcauto.model.assurance;

import main.java.com.miage.parcauto.model.vehicule.Vehicule;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime; // La DB utilise DATETIME
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Entité représentant un contrat d'assurance.
 * Un contrat peut couvrir un ou plusieurs véhicules (via la table de liaison COUVRIR).
 * Correspond à un enregistrement de la table ASSURANCE.
 */
public class Assurance implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer numCarteAssurance; // Clé primaire de la table ASSURANCE
    private LocalDateTime dateDebutAssurance;
    private LocalDateTime dateFinAssurance;
    private String agence; // Nom de l'agence d'assurance
    private BigDecimal coutAssurance; // Coût annuel ou périodique de l'assurance

    // Relation ManyToMany avec Vehicule, gérée par la table COUVRIR.
    // Chaque assurance peut couvrir plusieurs véhicules.
    private Set<Vehicule> vehiculesCouverts;

    /**
     * Constructeur par défaut.
     */
    public Assurance() {
        this.vehiculesCouverts = new HashSet<>();
    }

    /**
     * Constructeur avec les paramètres essentiels.
     *
     * @param numCarteAssurance   Le numéro unique du contrat ou de la carte d'assurance.
     * @param dateDebutAssurance  La date de début de validité de l'assurance.
     * @param dateFinAssurance    La date de fin de validité de l'assurance.
     * @param agence              Le nom de la compagnie ou de l'agence d'assurance.
     * @param coutAssurance       Le coût de l'assurance pour sa période de validité.
     */
    public Assurance(Integer numCarteAssurance, LocalDateTime dateDebutAssurance, LocalDateTime dateFinAssurance,
                     String agence, BigDecimal coutAssurance) {
        this();
        this.numCarteAssurance = numCarteAssurance;
        this.dateDebutAssurance = dateDebutAssurance;
        this.dateFinAssurance = dateFinAssurance;
        this.agence = agence;
        this.coutAssurance = coutAssurance;
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

    public Set<Vehicule> getVehiculesCouverts() {
        return vehiculesCouverts;
    }

    public void setVehiculesCouverts(Set<Vehicule> vehiculesCouverts) {
        this.vehiculesCouverts = vehiculesCouverts;
    }

    /**
     * Ajoute un véhicule à la liste des véhicules couverts par cette assurance.
     * Gère la relation bidirectionnelle si Vehicule a une collection d'Assurances.
     * @param vehicule Le véhicule à ajouter.
     */
    public void addVehiculeCouvert(Vehicule vehicule) {
        if (vehicule != null) {
            this.vehiculesCouverts.add(vehicule);
            // Si Vehicule a une collection d'assurances :
            // vehicule.getAssurances().add(this);
        }
    }

    /**
     * Retire un véhicule de la liste des véhicules couverts.
     * @param vehicule Le véhicule à retirer.
     */
    public void removeVehiculeCouvert(Vehicule vehicule) {
        if (vehicule != null) {
            this.vehiculesCouverts.remove(vehicule);
            // Si Vehicule a une collection d'assurances :
            // vehicule.getAssurances().remove(this);
        }
    }

    /**
     * Vérifie si l'assurance est active à la date actuelle.
     * @return true si l'assurance est active, false sinon.
     */
    public boolean isAssuranceActive() {
        LocalDateTime maintenant = LocalDateTime.now();
        boolean estCommencee = (dateDebutAssurance == null || !dateDebutAssurance.isAfter(maintenant));
        boolean pasTerminee = (dateFinAssurance == null || !dateFinAssurance.isBefore(maintenant));
        return estCommencee && pasTerminee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assurance assurance = (Assurance) o;
        return Objects.equals(numCarteAssurance, assurance.numCarteAssurance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numCarteAssurance);
    }

    @Override
    public String toString() {
        return "Assurance{" +
                "numCarteAssurance=" + numCarteAssurance +
                ", agence='" + agence + '\'' +
                ", dateFinAssurance=" + dateFinAssurance +
                ", coutAssurance=" + coutAssurance +
                '}';
    }
}