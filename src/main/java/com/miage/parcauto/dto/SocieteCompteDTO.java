package main.java.com.miage.parcauto.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) pour représenter les comptes de la société.
 * Cette classe facilite le transfert de données entre les couches Service et
 * Vue.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class SocieteCompteDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer idCompte;
    private String numeroCompte;
    private String libelleCompte;
    private BigDecimal soldeCompte;
    private String typeCompte;
    private LocalDateTime dateCreation;
    private Integer idPersonnel;
    private String nomPersonnel;
    private boolean actif;

    /**
     * Constructeur par défaut.
     */
    public SocieteCompteDTO() {
    }

    /**
     * Constructeur avec tous les paramètres.
     *
     * @param idCompte      Identifiant du compte
     * @param numeroCompte  Numéro du compte
     * @param libelleCompte Libellé du compte
     * @param soldeCompte   Solde du compte
     * @param typeCompte    Type du compte
     * @param dateCreation  Date de création du compte
     * @param idPersonnel   Identifiant du personnel responsable du compte
     * @param nomPersonnel  Nom du personnel responsable du compte
     * @param actif         Indique si le compte est actif
     */
    public SocieteCompteDTO(Integer idCompte, String numeroCompte, String libelleCompte, BigDecimal soldeCompte,
            String typeCompte, LocalDateTime dateCreation, Integer idPersonnel, String nomPersonnel,
            boolean actif) {
        this.idCompte = idCompte;
        this.numeroCompte = numeroCompte;
        this.libelleCompte = libelleCompte;
        this.soldeCompte = soldeCompte;
        this.typeCompte = typeCompte;
        this.dateCreation = dateCreation;
        this.idPersonnel = idPersonnel;
        this.nomPersonnel = nomPersonnel;
        this.actif = actif;
    }

    /**
     * Retourne l'identifiant du compte.
     *
     * @return L'identifiant du compte
     */
    public Integer getIdCompte() {
        return idCompte;
    }

    /**
     * Définit l'identifiant du compte.
     *
     * @param idCompte L'identifiant du compte
     */
    public void setIdCompte(Integer idCompte) {
        this.idCompte = idCompte;
    }

    /**
     * Retourne le numéro du compte.
     *
     * @return Le numéro du compte
     */
    public String getNumeroCompte() {
        return numeroCompte;
    }

    /**
     * Définit le numéro du compte.
     *
     * @param numeroCompte Le numéro du compte
     */
    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    /**
     * Retourne le libellé du compte.
     *
     * @return Le libellé du compte
     */
    public String getLibelleCompte() {
        return libelleCompte;
    }

    /**
     * Définit le libellé du compte.
     *
     * @param libelleCompte Le libellé du compte
     */
    public void setLibelleCompte(String libelleCompte) {
        this.libelleCompte = libelleCompte;
    }

    /**
     * Retourne le solde du compte.
     *
     * @return Le solde du compte
     */
    public BigDecimal getSoldeCompte() {
        return soldeCompte;
    }

    /**
     * Définit le solde du compte.
     *
     * @param soldeCompte Le solde du compte
     */
    public void setSoldeCompte(BigDecimal soldeCompte) {
        this.soldeCompte = soldeCompte;
    }

    /**
     * Retourne le type du compte.
     *
     * @return Le type du compte
     */
    public String getTypeCompte() {
        return typeCompte;
    }

    /**
     * Définit le type du compte.
     *
     * @param typeCompte Le type du compte
     */
    public void setTypeCompte(String typeCompte) {
        this.typeCompte = typeCompte;
    }

    /**
     * Retourne la date de création du compte.
     *
     * @return La date de création du compte
     */
    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    /**
     * Définit la date de création du compte.
     *
     * @param dateCreation La date de création du compte
     */
    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    /**
     * Retourne l'identifiant du personnel responsable du compte.
     *
     * @return L'identifiant du personnel
     */
    public Integer getIdPersonnel() {
        return idPersonnel;
    }

    /**
     * Définit l'identifiant du personnel responsable du compte.
     *
     * @param idPersonnel L'identifiant du personnel
     */
    public void setIdPersonnel(Integer idPersonnel) {
        this.idPersonnel = idPersonnel;
    }

    /**
     * Retourne le nom du personnel responsable du compte.
     *
     * @return Le nom du personnel
     */
    public String getNomPersonnel() {
        return nomPersonnel;
    }

    /**
     * Définit le nom du personnel responsable du compte.
     *
     * @param nomPersonnel Le nom du personnel
     */
    public void setNomPersonnel(String nomPersonnel) {
        this.nomPersonnel = nomPersonnel;
    }

    /**
     * Indique si le compte est actif.
     *
     * @return true si le compte est actif, false sinon
     */
    public boolean isActif() {
        return actif;
    }

    /**
     * Définit si le compte est actif.
     *
     * @param actif true si le compte est actif, false sinon
     */
    public void setActif(boolean actif) {
        this.actif = actif;
    }

    @Override
    public String toString() {
        return "SocieteCompteDTO{" +
                "idCompte=" + idCompte +
                ", numeroCompte='" + numeroCompte + '\'' +
                ", libelleCompte='" + libelleCompte + '\'' +
                ", soldeCompte=" + soldeCompte +
                ", typeCompte='" + typeCompte + '\'' +
                ", dateCreation=" + dateCreation +
                ", idPersonnel=" + idPersonnel +
                ", nomPersonnel='" + nomPersonnel + '\'' +
                ", actif=" + actif +
                '}';
    }
}
