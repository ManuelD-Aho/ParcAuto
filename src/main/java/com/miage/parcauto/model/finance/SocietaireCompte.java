package main.java.com.miage.parcauto.model.finance;

import main.java.com.miage.parcauto.model.rh.Personnel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entité représentant un compte sociétaire.
 */
public class SocietaireCompte implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idSocietaire;
    private Personnel personnel;  // Lien éventuel avec un membre du personnel
    private String nom;
    private String numero;
    private BigDecimal solde;
    private String email;
    private String telephone;

    public SocietaireCompte() {
        this.solde = BigDecimal.ZERO;
    }

    // Getters et Setters
    public Integer getIdSocietaire() {
        return idSocietaire;
    }

    public void setIdSocietaire(Integer idSocietaire) {
        this.idSocietaire = idSocietaire;
    }

    public Personnel getPersonnel() {
        return personnel;
    }

    public void setPersonnel(Personnel personnel) {
        this.personnel = personnel;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocietaireCompte that = (SocietaireCompte) o;
        return Objects.equals(idSocietaire, that.idSocietaire);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSocietaire);
    }

    @Override
    public String toString() {
        return nom + " (" + numero + ")";
    }
}