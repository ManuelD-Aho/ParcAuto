package main.java.com.miage.parcauto.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class SocietaireCompteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idSocietaire;
    private Integer idPersonnel;
    private String personnelInfo; // Nom et prénom du personnel si lié
    private String nom;
    private String numero;
    private BigDecimal solde;
    private String email;
    private String telephone;
    private Integer idSocietaireCompte;
    private String numeroCompte;
    private Boolean actif;

    public SocietaireCompteDTO() {
    }

    public Integer getIdSocietaire() {
        return idSocietaire;
    }

    public void setIdSocietaire(Integer idSocietaire) {
        this.idSocietaire = idSocietaire;
    }

    public Integer getIdPersonnel() {
        return idPersonnel;
    }

    public void setIdPersonnel(Integer idPersonnel) {
        this.idPersonnel = idPersonnel;
    }

    public String getPersonnelInfo() {
        return personnelInfo;
    }

    public void setPersonnelInfo(String personnelInfo) {
        this.personnelInfo = personnelInfo;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getIdSocietaireCompte() {
        return idSocietaireCompte;
    }

    public void setIdSocietaireCompte(Integer idSocietaireCompte) {
        this.idSocietaireCompte = idSocietaireCompte;
    }

    public String getNumeroCompte() {
        return numeroCompte;
    }

    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    @Override
    public String toString() {
        return "SocietaireCompteDTO{" +
                "idSocietaire=" + idSocietaire +
                ", nom='" + nom + '\'' +
                ", numero='" + numero + '\'' +
                ", solde=" + solde +
                '}';
    }
}