package main.java.com.miage.parcauto.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) pour représenter les mouvements financiers.
 * Cette classe facilite le transfert de données entre les couches Service et
 * Vue.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class MouvementDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer idMouvement;
    private Integer idCompte;
    private String numeroCompte;
    private String libelleCompte;
    private LocalDate dateMouvement;
    private BigDecimal montant;
    private String libelle;
    private String typeMouvement;
    private Integer idMission;
    private String referenceMission;
    private Integer idEntretien;
    private String referenceEntretien;
    private String categorie;

    /**
     * Constructeur par défaut.
     */
    public MouvementDTO() {
    }

    /**
     * Constructeur avec tous les paramètres.
     *
     * @param idMouvement        Identifiant du mouvement
     * @param idCompte           Identifiant du compte associé
     * @param numeroCompte       Numéro du compte associé
     * @param libelleCompte      Libellé du compte associé
     * @param dateMouvement      Date du mouvement
     * @param montant            Montant du mouvement
     * @param libelle            Libellé du mouvement
     * @param typeMouvement      Type de mouvement (CREDIT ou DEBIT)
     * @param idMission          Identifiant de la mission associée, si applicable
     * @param referenceMission   Référence de la mission associée, si applicable
     * @param idEntretien        Identifiant de l'entretien associé, si applicable
     * @param referenceEntretien Référence de l'entretien associé, si applicable
     * @param categorie          Catégorie du mouvement
     */
    public MouvementDTO(Integer idMouvement, Integer idCompte, String numeroCompte, String libelleCompte,
            LocalDate dateMouvement, BigDecimal montant, String libelle, String typeMouvement,
            Integer idMission, String referenceMission, Integer idEntretien,
            String referenceEntretien, String categorie) {
        this.idMouvement = idMouvement;
        this.idCompte = idCompte;
        this.numeroCompte = numeroCompte;
        this.libelleCompte = libelleCompte;
        this.dateMouvement = dateMouvement;
        this.montant = montant;
        this.libelle = libelle;
        this.typeMouvement = typeMouvement;
        this.idMission = idMission;
        this.referenceMission = referenceMission;
        this.idEntretien = idEntretien;
        this.referenceEntretien = referenceEntretien;
        this.categorie = categorie;
    }

    /**
     * Retourne l'identifiant du mouvement.
     *
     * @return L'identifiant du mouvement
     */
    public Integer getIdMouvement() {
        return idMouvement;
    }

    /**
     * Définit l'identifiant du mouvement.
     *
     * @param idMouvement L'identifiant du mouvement
     */
    public void setIdMouvement(Integer idMouvement) {
        this.idMouvement = idMouvement;
    }

    /**
     * Retourne l'identifiant du compte associé.
     *
     * @return L'identifiant du compte
     */
    public Integer getIdCompte() {
        return idCompte;
    }

    /**
     * Définit l'identifiant du compte associé.
     *
     * @param idCompte L'identifiant du compte
     */
    public void setIdCompte(Integer idCompte) {
        this.idCompte = idCompte;
    }

    /**
     * Retourne le numéro du compte associé.
     *
     * @return Le numéro du compte
     */
    public String getNumeroCompte() {
        return numeroCompte;
    }

    /**
     * Définit le numéro du compte associé.
     *
     * @param numeroCompte Le numéro du compte
     */
    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    /**
     * Retourne le libellé du compte associé.
     *
     * @return Le libellé du compte
     */
    public String getLibelleCompte() {
        return libelleCompte;
    }

    /**
     * Définit le libellé du compte associé.
     *
     * @param libelleCompte Le libellé du compte
     */
    public void setLibelleCompte(String libelleCompte) {
        this.libelleCompte = libelleCompte;
    }

    /**
     * Retourne la date du mouvement.
     *
     * @return La date du mouvement
     */
    public LocalDate getDateMouvement() {
        return dateMouvement;
    }

    /**
     * Définit la date du mouvement.
     *
     * @param dateMouvement La date du mouvement
     */
    public void setDateMouvement(LocalDate dateMouvement) {
        this.dateMouvement = dateMouvement;
    }

    /**
     * Retourne le montant du mouvement.
     *
     * @return Le montant du mouvement
     */
    public BigDecimal getMontant() {
        return montant;
    }

    /**
     * Définit le montant du mouvement.
     *
     * @param montant Le montant du mouvement
     */
    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    /**
     * Retourne le libellé du mouvement.
     *
     * @return Le libellé du mouvement
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Définit le libellé du mouvement.
     *
     * @param libelle Le libellé du mouvement
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * Retourne le type de mouvement (CREDIT ou DEBIT).
     *
     * @return Le type de mouvement
     */
    public String getTypeMouvement() {
        return typeMouvement;
    }

    /**
     * Définit le type de mouvement.
     *
     * @param typeMouvement Le type de mouvement
     */
    public void setTypeMouvement(String typeMouvement) {
        this.typeMouvement = typeMouvement;
    }

    /**
     * Retourne l'identifiant de la mission associée.
     *
     * @return L'identifiant de la mission
     */
    public Integer getIdMission() {
        return idMission;
    }

    /**
     * Définit l'identifiant de la mission associée.
     *
     * @param idMission L'identifiant de la mission
     */
    public void setIdMission(Integer idMission) {
        this.idMission = idMission;
    }

    /**
     * Retourne la référence de la mission associée.
     *
     * @return La référence de la mission
     */
    public String getReferenceMission() {
        return referenceMission;
    }

    /**
     * Définit la référence de la mission associée.
     *
     * @param referenceMission La référence de la mission
     */
    public void setReferenceMission(String referenceMission) {
        this.referenceMission = referenceMission;
    }

    /**
     * Retourne l'identifiant de l'entretien associé.
     *
     * @return L'identifiant de l'entretien
     */
    public Integer getIdEntretien() {
        return idEntretien;
    }

    /**
     * Définit l'identifiant de l'entretien associé.
     *
     * @param idEntretien L'identifiant de l'entretien
     */
    public void setIdEntretien(Integer idEntretien) {
        this.idEntretien = idEntretien;
    }

    /**
     * Retourne la référence de l'entretien associé.
     *
     * @return La référence de l'entretien
     */
    public String getReferenceEntretien() {
        return referenceEntretien;
    }

    /**
     * Définit la référence de l'entretien associé.
     *
     * @param referenceEntretien La référence de l'entretien
     */
    public void setReferenceEntretien(String referenceEntretien) {
        this.referenceEntretien = referenceEntretien;
    }

    /**
     * Retourne la catégorie du mouvement.
     *
     * @return La catégorie du mouvement
     */
    public String getCategorie() {
        return categorie;
    }

    /**
     * Définit la catégorie du mouvement.
     *
     * @param categorie La catégorie du mouvement
     */
    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return "MouvementDTO{" +
                "idMouvement=" + idMouvement +
                ", idCompte=" + idCompte +
                ", numeroCompte='" + numeroCompte + '\'' +
                ", dateMouvement=" + dateMouvement +
                ", montant=" + montant +
                ", libelle='" + libelle + '\'' +
                ", typeMouvement='" + typeMouvement + '\'' +
                ", categorie='" + categorie + '\'' +
                '}';
    }
}
