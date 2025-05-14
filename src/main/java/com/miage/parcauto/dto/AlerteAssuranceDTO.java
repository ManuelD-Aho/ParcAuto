package main.java.com.miage.parcauto.dto;

import java.time.LocalDate;

/**
 * DTO représentant une alerte d'assurance.
 * Utilisé pour les notifications de fin de validité des assurances.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class AlerteAssuranceDTO {
    private Integer idVehicule;
    private String marque;
    private String modele;
    private String immatriculation;
    private LocalDate dateFinAssurance;
    private int joursRestants;
    private String statut;
    private String numeroContrat;
    private String compagnie;

    /**
     * Constructeur par défaut.
     */
    public AlerteAssuranceDTO() {
    }

    /**
     * Constructeur complet.
     */
    public AlerteAssuranceDTO(Integer idVehicule, String marque, String modele, String immatriculation, LocalDate dateFinAssurance, int joursRestants, String statut, String numeroContrat, String compagnie) {
        this.idVehicule = idVehicule;
        this.marque = marque;
        this.modele = modele;
        this.immatriculation = immatriculation;
        this.dateFinAssurance = dateFinAssurance;
        this.joursRestants = joursRestants;
        this.statut = statut;
        this.numeroContrat = numeroContrat;
        this.compagnie = compagnie;
    }

    /**
     * @return l'ID du véhicule
     */
    public Integer getIdVehicule() {
        return idVehicule;
    }

    /**
     * @param idVehicule l'ID du véhicule à définir
     */
    public void setIdVehicule(Integer idVehicule) {
        this.idVehicule = idVehicule;
    }

    /**
     * @return la marque
     */
    public String getMarque() {
        return marque;
    }

    /**
     * @param marque la marque à définir
     */
    public void setMarque(String marque) {
        this.marque = marque;
    }

    /**
     * @return le modèle
     */
    public String getModele() {
        return modele;
    }

    /**
     * @param modele le modèle à définir
     */
    public void setModele(String modele) {
        this.modele = modele;
    }

    /**
     * @return l'immatriculation
     */
    public String getImmatriculation() {
        return immatriculation;
    }

    /**
     * @param immatriculation l'immatriculation à définir
     */
    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    /**
     * @return la date de fin d'assurance
     */
    public LocalDate getDateFinAssurance() {
        return dateFinAssurance;
    }

    /**
     * @param dateFinAssurance la date de fin d'assurance à définir
     */
    public void setDateFinAssurance(LocalDate dateFinAssurance) {
        this.dateFinAssurance = dateFinAssurance;
    }

    /**
     * @return le nombre de jours restants
     */
    public int getJoursRestants() {
        return joursRestants;
    }

    /**
     * @param joursRestants le nombre de jours restants à définir
     */
    public void setJoursRestants(int joursRestants) {
        this.joursRestants = joursRestants;
    }

    /**
     * @return le statut
     */
    public String getStatut() {
        return statut;
    }

    /**
     * @param statut le statut à définir
     */
    public void setStatut(String statut) {
        this.statut = statut;
    }

    /**
     * @return le numéro de contrat
     */
    public String getNumeroContrat() {
        return numeroContrat;
    }

    /**
     * @param numeroContrat le numéro de contrat à définir
     */
    public void setNumeroContrat(String numeroContrat) {
        this.numeroContrat = numeroContrat;
    }

    /**
     * @return la compagnie d'assurance
     */
    public String getCompagnie() {
        return compagnie;
    }

    /**
     * @param compagnie la compagnie d'assurance à définir
     */
    public void setCompagnie(String compagnie) {
        this.compagnie = compagnie;
    }
}
