package main.java.com.miage.parcauto.dto;

import java.math.BigDecimal;

/**
 * DTO représentant la rentabilité d'un véhicule.
 * Utilisé pour les rapports et analyses financières.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class VehiculeRentabiliteDTO {

    private Integer idVehicule;
    private String marque;
    private String modele;
    private String immatriculation;
    private int nombreMissions;
    private int kmParcourus;
    private BigDecimal recettesGenerees;
    private BigDecimal coutTotal;
    private BigDecimal beneficeNet;
    private BigDecimal rentabilite; // en pourcentage

    /**
     * Constructeur par défaut.
     */
    public VehiculeRentabiliteDTO() {
        this.nombreMissions = 0;
        this.kmParcourus = 0;
        this.recettesGenerees = BigDecimal.ZERO;
        this.coutTotal = BigDecimal.ZERO;
        this.beneficeNet = BigDecimal.ZERO;
        this.rentabilite = BigDecimal.ZERO;
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
     * @return le nombre de missions
     */
    public int getNombreMissions() {
        return nombreMissions;
    }

    /**
     * @param nombreMissions le nombre de missions à définir
     */
    public void setNombreMissions(int nombreMissions) {
        this.nombreMissions = nombreMissions;
    }

    /**
     * @return le nombre de km parcourus
     */
    public int getKmParcourus() {
        return kmParcourus;
    }

    /**
     * @param kmParcourus le nombre de km parcourus à définir
     */
    public void setKmParcourus(int kmParcourus) {
        this.kmParcourus = kmParcourus;
    }

    /**
     * @return les recettes générées
     */
    public BigDecimal getRecettesGenerees() {
        return recettesGenerees;
    }

    /**
     * @param recettesGenerees les recettes générées à définir
     */
    public void setRecettesGenerees(BigDecimal recettesGenerees) {
        this.recettesGenerees = recettesGenerees;
    }

    /**
     * @return le coût total
     */
    public BigDecimal getCoutTotal() {
        return coutTotal;
    }

    /**
     * @param coutTotal le coût total à définir
     */
    public void setCoutTotal(BigDecimal coutTotal) {
        this.coutTotal = coutTotal;
    }

    /**
     * @return le bénéfice net
     */
    public BigDecimal getBeneficeNet() {
        return beneficeNet;
    }

    /**
     * @param beneficeNet le bénéfice net à définir
     */
    public void setBeneficeNet(BigDecimal beneficeNet) {
        this.beneficeNet = beneficeNet;
    }

    /**
     * @return la rentabilité (en pourcentage)
     */
    public BigDecimal getRentabilite() {
        return rentabilite;
    }

    /**
     * @param rentabilite la rentabilité à définir (en pourcentage)
     */
    public void setRentabilite(BigDecimal rentabilite) {
        this.rentabilite = rentabilite;
    }
}
