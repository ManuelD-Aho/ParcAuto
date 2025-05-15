package main.java.com.miage.parcauto.dto;

/**
 * DTO représentant une alerte d'entretien.
 * Utilisé pour les notifications d'entretiens à prévoir.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class AlerteEntretienDTO {

    private Integer idVehicule;
    private String marque;
    private String modele;
    private String immatriculation;
    private int kilometrage;
    private int kmRestants;
    private String typeEntretien;
    private int kmPrevu;

    /**
     * Constructeur par défaut.
     */
    public AlerteEntretienDTO() {
        this.kilometrage = 0;
        this.kmRestants = 0;
        this.kmPrevu = 0;
    }

    /**
     * Constructeur complet.
     */
    public AlerteEntretienDTO(Integer idVehicule, String marque, String modele, String immatriculation, int kilometrage,
            int kmRestants, String typeEntretien, int kmPrevu) {
        this.idVehicule = idVehicule;
        this.marque = marque;
        this.modele = modele;
        this.immatriculation = immatriculation;
        this.kilometrage = kilometrage;
        this.kmRestants = kmRestants;
        this.typeEntretien = typeEntretien;
        this.kmPrevu = kmPrevu;
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
     * @return la marque du véhicule
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
     * @return le modèle du véhicule
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
     * @return l'immatriculation du véhicule
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
     * @return le kilométrage actuel
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
     * @return le nombre de kilomètres restants avant entretien
     */
    public int getKmRestants() {
        return kmRestants;
    }

    /**
     * @param kmRestants le nombre de kilomètres restants à définir
     */
    public void setKmRestants(int kmRestants) {
        this.kmRestants = kmRestants;
    }

    /**
     * @return le type d'entretien prévu
     */
    public String getTypeEntretien() {
        return typeEntretien;
    }

    /**
     * @param typeEntretien le type d'entretien à définir
     */
    public void setTypeEntretien(String typeEntretien) {
        this.typeEntretien = typeEntretien;
    }

    /**
     * @return le kilométrage prévu pour l'entretien
     */
    public int getKmPrevu() {
        return kmPrevu;
    }

    /**
     * @param kmPrevu le kilométrage prévu à définir
     */
    public void setKmPrevu(int kmPrevu) {
        this.kmPrevu = kmPrevu;
    }
}
