package main.java.com.miage.parcauto.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Classe représentant un sociétaire de ParcAuto.
 * Les sociétaires sont les conducteurs potentiels des véhicules.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class Societaire {
    private Integer idSocietaire;
    private String nom;
    private String prenom;
    private String numeroCin;
    private String numeroPermis;
    private String email;
    private String telephone;
    private LocalDate dateNaissance;
    private LocalDate datePermis;
    private String adresse;
    private boolean actif;

    /**
     * Constructeur vide.
     */
    public Societaire() {
        this.actif = true;
    }

    /**
     * Constructeur avec paramètres essentiels.
     *
     * @param nom Nom du sociétaire
     * @param prenom Prénom du sociétaire
     * @param numeroCin Numéro de la carte d'identité nationale
     * @param numeroPermis Numéro de permis de conduire
     * @param email Adresse email
     * @param telephone Numéro de téléphone
     */
    public Societaire(String nom, String prenom, String numeroCin, String numeroPermis, 
                      String email, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.numeroCin = numeroCin;
        this.numeroPermis = numeroPermis;
        this.email = email;
        this.telephone = telephone;
        this.actif = true;
    }

    /**
     * Constructeur complet.
     *
     * @param idSocietaire Identifiant unique du sociétaire
     * @param nom Nom du sociétaire
     * @param prenom Prénom du sociétaire
     * @param numeroCin Numéro de la carte d'identité nationale
     * @param numeroPermis Numéro de permis de conduire
     * @param email Adresse email
     * @param telephone Numéro de téléphone
     * @param dateNaissance Date de naissance
     * @param datePermis Date d'obtention du permis
     * @param adresse Adresse postale
     * @param actif État d'activité du sociétaire
     */
    public Societaire(Integer idSocietaire, String nom, String prenom, String numeroCin,
                      String numeroPermis, String email, String telephone, LocalDate dateNaissance,
                      LocalDate datePermis, String adresse, boolean actif) {
        this.idSocietaire = idSocietaire;
        this.nom = nom;
        this.prenom = prenom;
        this.numeroCin = numeroCin;
        this.numeroPermis = numeroPermis;
        this.email = email;
        this.telephone = telephone;
        this.dateNaissance = dateNaissance;
        this.datePermis = datePermis;
        this.adresse = adresse;
        this.actif = actif;
    }

    // Getters et setters

    public Integer getIdSocietaire() {
        return idSocietaire;
    }

    public void setIdSocietaire(Integer idSocietaire) {
        this.idSocietaire = idSocietaire;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNumeroCin() {
        return numeroCin;
    }

    public void setNumeroCin(String numeroCin) {
        this.numeroCin = numeroCin;
    }

    public String getNumeroPermis() {
        return numeroPermis;
    }

    public void setNumeroPermis(String numeroPermis) {
        this.numeroPermis = numeroPermis;
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

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public LocalDate getDatePermis() {
        return datePermis;
    }

    public void setDatePermis(LocalDate datePermis) {
        this.datePermis = datePermis;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    /**
     * Retourne le nom complet du sociétaire (prénom + nom).
     *
     * @return Le nom complet du sociétaire
     */
    public String getNomComplet() {
        return this.prenom + " " + this.nom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Societaire that = (Societaire) o;
        return Objects.equals(idSocietaire, that.idSocietaire) || 
               (Objects.equals(numeroCin, that.numeroCin) && 
                Objects.equals(numeroPermis, that.numeroPermis));
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSocietaire, numeroCin, numeroPermis);
    }

    @Override
    public String toString() {
        return "Societaire{" +
                "idSocietaire=" + idSocietaire +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", numeroCin='" + numeroCin + '\'' +
                ", numeroPermis='" + numeroPermis + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", actif=" + actif +
                '}';
    }
}