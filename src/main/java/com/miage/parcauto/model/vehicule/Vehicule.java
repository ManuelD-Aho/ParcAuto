package main.java.com.miage.parcauto.model.vehicule;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entité représentant un véhicule dans le parc automobile.
 * Correspond à un enregistrement de la table VEHICULES.
 */
public class Vehicule implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer idVehicule;
    private EtatVoiture etatVoiture; // Relation avec EtatVoiture (via id_etat_voiture)
    private TypeEnergie energie;
    private String numeroChassi;
    private String immatriculation;
    private String marque;
    private String modele;
    private Integer nbPlaces;
    private LocalDateTime dateAcquisition;
    private LocalDateTime dateAmortissement; // Nommé date_ammortissement en DB
    private LocalDateTime dateMiseEnService;
    private Integer puissance; // Puissance fiscale ou chevaux
    private String couleur;
    private BigDecimal prixVehicule;
    private Integer kmActuels;
    private LocalDateTime dateEtat; // Date de la dernière mise à jour de l'état

    /**
     * Constructeur par défaut.
     */
    public Vehicule() {
    }

    /**
     * Constructeur avec tous les paramètres.
     * @param idVehicule L'identifiant unique du véhicule.
     * @param etatVoiture L'objet EtatVoiture associé.
     * @param energie Le type d'énergie du véhicule.
     * @param numeroChassi Le numéro de châssis.
     * @param immatriculation La plaque d'immatriculation.
     * @param marque La marque du véhicule.
     * @param modele Le modèle du véhicule.
     * @param nbPlaces Le nombre de places.
     * @param dateAcquisition La date d'acquisition.
     * @param dateAmortissement La date d'amortissement.
     * @param dateMiseEnService La date de mise en service.
     * @param puissance La puissance du véhicule.
     * @param couleur La couleur du véhicule.
     * @param prixVehicule Le prix d'achat du véhicule.
     * @param kmActuels Le kilométrage actuel.
     * @param dateEtat La date de la dernière modification de l'état du véhicule.
     */
    public Vehicule(Integer idVehicule, EtatVoiture etatVoiture, TypeEnergie energie, String numeroChassi,
                    String immatriculation, String marque, String modele, Integer nbPlaces,
                    LocalDateTime dateAcquisition, LocalDateTime dateAmortissement, LocalDateTime dateMiseEnService,
                    Integer puissance, String couleur, BigDecimal prixVehicule, Integer kmActuels, LocalDateTime dateEtat) {
        this.idVehicule = idVehicule;
        this.etatVoiture = etatVoiture;
        this.energie = energie;
        this.numeroChassi = numeroChassi;
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.modele = modele;
        this.nbPlaces = nbPlaces;
        this.dateAcquisition = dateAcquisition;
        this.dateAmortissement = dateAmortissement;
        this.dateMiseEnService = dateMiseEnService;
        this.puissance = puissance;
        this.couleur = couleur;
        this.prixVehicule = prixVehicule;
        this.kmActuels = kmActuels;
        this.dateEtat = dateEtat;
    }

    // Getters et Setters

    public Integer getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(Integer idVehicule) {
        this.idVehicule = idVehicule;
    }

    public EtatVoiture getEtatVoiture() {
        return etatVoiture;
    }

    public void setEtatVoiture(EtatVoiture etatVoiture) {
        this.etatVoiture = etatVoiture;
    }

    public TypeEnergie getEnergie() {
        return energie;
    }

    public void setEnergie(TypeEnergie energie) {
        this.energie = energie;
    }

    public String getNumeroChassi() {
        return numeroChassi;
    }

    public void setNumeroChassi(String numeroChassi) {
        this.numeroChassi = numeroChassi;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public Integer getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(Integer nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public LocalDateTime getDateAcquisition() {
        return dateAcquisition;
    }

    public void setDateAcquisition(LocalDateTime dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    public LocalDateTime getDateAmortissement() {
        return dateAmortissement;
    }

    public void setDateAmortissement(LocalDateTime dateAmortissement) {
        this.dateAmortissement = dateAmortissement;
    }

    public LocalDateTime getDateMiseEnService() {
        return dateMiseEnService;
    }

    public void setDateMiseEnService(LocalDateTime dateMiseEnService) {
        this.dateMiseEnService = dateMiseEnService;
    }

    public Integer getPuissance() {
        return puissance;
    }

    public void setPuissance(Integer puissance) {
        this.puissance = puissance;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public BigDecimal getPrixVehicule() {
        return prixVehicule;
    }

    public void setPrixVehicule(BigDecimal prixVehicule) {
        this.prixVehicule = prixVehicule;
    }

    public Integer getKmActuels() {
        return kmActuels;
    }

    public void setKmActuels(Integer kmActuels) {
        this.kmActuels = kmActuels;
    }

    public LocalDateTime getDateEtat() {
        return dateEtat;
    }

    public void setDateEtat(LocalDateTime dateEtat) {
        this.dateEtat = dateEtat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicule vehicule = (Vehicule) o;
        return Objects.equals(idVehicule, vehicule.idVehicule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVehicule);
    }

    @Override
    public String toString() {
        return "Vehicule{" +
                "idVehicule=" + idVehicule +
                ", marque='" + marque + '\'' +
                ", modele='" + modele + '\'' +
                ", immatriculation='" + immatriculation + '\'' +
                ", etatVoiture=" + (etatVoiture != null ? etatVoiture.getLibEtatVoiture() : "N/A") +
                '}';
    }
}