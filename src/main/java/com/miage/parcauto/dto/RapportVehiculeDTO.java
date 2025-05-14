package main.java.com.miage.parcauto.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * DTO pour les rapports concernant un véhicule.
 * Cette classe étend RapportDTO et ajoute des informations spécifiques aux
 * véhicules.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class RapportVehiculeDTO extends RapportDTO {

    private Integer idVehicule;
    private String immatriculation;
    private String marque;
    private String modele;
    private int kilometrage;
    private LocalDate dateMiseEnService;
    private String etat;
    private BigDecimal valeurAcquisition;
    private BigDecimal valeurResiduelle;
    private int nombreMissions;
    private int nombreEntretiens;
    private BigDecimal coutTotalEntretiens;
    private int joursDepuisAcquisition;
    private double tauxUtilisation; // en pourcentage
    private List<EntretienDTO> historiquePrevEntretien;
    private List<EntretienDTO> entretiensNecessaires;
    private List<MissionDTO> dernieresMissions;
    private Map<String, BigDecimal> repartitionCouts;

    /**
     * Constructeur par défaut.
     */
    public RapportVehiculeDTO() {
        super("Rapport véhicule", "Données complètes sur un véhicule");
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
     * @return le kilométrage
     */
    public int getKilometrage() {
        return kilometrage;
    }

    /**
     * @param kilometrage le kilométrage à définir
     */
    public void setKilometrage(int kilometrage) {
        this.kilometrage = kilometrage;
    }

    /**
     * @return la date de mise en service
     */
    public LocalDate getDateMiseEnService() {
        return dateMiseEnService;
    }

    /**
     * @param dateMiseEnService la date de mise en service à définir
     */
    public void setDateMiseEnService(LocalDate dateMiseEnService) {
        this.dateMiseEnService = dateMiseEnService;
    }

    /**
     * @return l'état
     */
    public String getEtat() {
        return etat;
    }

    /**
     * @param etat l'état à définir
     */
    public void setEtat(String etat) {
        this.etat = etat;
    }

    /**
     * @return la valeur d'acquisition
     */
    public BigDecimal getValeurAcquisition() {
        return valeurAcquisition;
    }

    /**
     * @param valeurAcquisition la valeur d'acquisition à définir
     */
    public void setValeurAcquisition(BigDecimal valeurAcquisition) {
        this.valeurAcquisition = valeurAcquisition;
    }

    /**
     * @return la valeur résiduelle
     */
    public BigDecimal getValeurResiduelle() {
        return valeurResiduelle;
    }

    /**
     * @param valeurResiduelle la valeur résiduelle à définir
     */
    public void setValeurResiduelle(BigDecimal valeurResiduelle) {
        this.valeurResiduelle = valeurResiduelle;
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
     * @return le nombre d'entretiens
     */
    public int getNombreEntretiens() {
        return nombreEntretiens;
    }

    /**
     * @param nombreEntretiens le nombre d'entretiens à définir
     */
    public void setNombreEntretiens(int nombreEntretiens) {
        this.nombreEntretiens = nombreEntretiens;
    }

    /**
     * @return le coût total des entretiens
     */
    public BigDecimal getCoutTotalEntretiens() {
        return coutTotalEntretiens;
    }

    /**
     * @param coutTotalEntretiens le coût total des entretiens à définir
     */
    public void setCoutTotalEntretiens(BigDecimal coutTotalEntretiens) {
        this.coutTotalEntretiens = coutTotalEntretiens;
    }

    /**
     * @return le nombre de jours depuis l'acquisition
     */
    public int getJoursDepuisAcquisition() {
        return joursDepuisAcquisition;
    }

    /**
     * @param joursDepuisAcquisition le nombre de jours depuis l'acquisition à
     *                               définir
     */
    public void setJoursDepuisAcquisition(int joursDepuisAcquisition) {
        this.joursDepuisAcquisition = joursDepuisAcquisition;
    }

    /**
     * @return le taux d'utilisation
     */
    public double getTauxUtilisation() {
        return tauxUtilisation;
    }

    /**
     * @param tauxUtilisation le taux d'utilisation à définir
     */
    public void setTauxUtilisation(double tauxUtilisation) {
        this.tauxUtilisation = tauxUtilisation;
    }

    /**
     * @return l'historique des entretiens préventifs
     */
    public List<EntretienDTO> getHistoriquePrevEntretien() {
        return historiquePrevEntretien;
    }

    /**
     * @param historiquePrevEntretien l'historique des entretiens préventifs à
     *                                définir
     */
    public void setHistoriquePrevEntretien(List<EntretienDTO> historiquePrevEntretien) {
        this.historiquePrevEntretien = historiquePrevEntretien;
    }

    /**
     * @return les entretiens nécessaires
     */
    public List<EntretienDTO> getEntretiensNecessaires() {
        return entretiensNecessaires;
    }

    /**
     * @param entretiensNecessaires les entretiens nécessaires à définir
     */
    public void setEntretiensNecessaires(List<EntretienDTO> entretiensNecessaires) {
        this.entretiensNecessaires = entretiensNecessaires;
    }

    /**
     * @return les dernières missions
     */
    public List<MissionDTO> getDernieresMissions() {
        return dernieresMissions;
    }

    /**
     * @param dernieresMissions les dernières missions à définir
     */
    public void setDernieresMissions(List<MissionDTO> dernieresMissions) {
        this.dernieresMissions = dernieresMissions;
    }

    /**
     * @return la répartition des coûts
     */
    public Map<String, BigDecimal> getRepartitionCouts() {
        return repartitionCouts;
    }

    /**
     * @param repartitionCouts la répartition des coûts à définir
     */
    public void setRepartitionCouts(Map<String, BigDecimal> repartitionCouts) {
        this.repartitionCouts = repartitionCouts;
    }
}
