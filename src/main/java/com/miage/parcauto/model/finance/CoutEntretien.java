package main.java.com.miage.parcauto.model.finance;

import java.math.BigDecimal;

/**
 * Classe représentant le coût d'entretien d'un véhicule.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class CoutEntretien {

    private Integer idVehicule;
    private String marque;
    private String modele;
    private String immatriculation;
    private int nombreEntretiens;
    private BigDecimal coutTotal;
    private BigDecimal coutMoyen;

    /**
     * Constructeur par défaut.
     */
    public CoutEntretien() {
        this.nombreEntretiens = 0;
        this.coutTotal = BigDecimal.ZERO;
        this.coutMoyen = BigDecimal.ZERO;
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
     * @return le coût moyen
     */
    public BigDecimal getCoutMoyen() {
        return coutMoyen;
    }

    /**
     * @param coutMoyen le coût moyen à définir
     */
    public void setCoutMoyen(BigDecimal coutMoyen) {
        this.coutMoyen = coutMoyen;
    }
}
