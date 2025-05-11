package main.java.com.miage.parcauto.model.utilisateur;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Classe modèle représentant un sociétaire du parc auto.
 * Les sociétaires sont les personnes qui peuvent utiliser les véhicules du parc.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class Societaire implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Integer id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String numeroPermis;
    private LocalDate dateValiditePermis;
    private String adresse;
    private String codePostal;
    private String ville;
    private boolean actif;
    private LocalDate dateCreation;
    private Integer idResponsable;
    
    /**
     * Constructeur par défaut
     */
    public Societaire() {
        this.actif = true;
        this.dateCreation = LocalDate.now();
    }
    
    /**
     * Constructeur avec nom et prénom
     * 
     * @param nom Le nom du sociétaire
     * @param prenom Le prénom du sociétaire
     */
    public Societaire(String nom, String prenom) {
        this();
        this.nom = nom;
        this.prenom = prenom;
    }
    
    /**
     * Constructeur complet
     * 
     * @param id L'identifiant du sociétaire
     * @param nom Le nom du sociétaire
     * @param prenom Le prénom du sociétaire
     * @param email L'email du sociétaire
     * @param telephone Le téléphone du sociétaire
     * @param numeroPermis Le numéro de permis de conduire
     * @param dateValiditePermis La date de validité du permis
     * @param adresse L'adresse du sociétaire
     * @param codePostal Le code postal
     * @param ville La ville
     * @param actif Le statut actif/inactif du sociétaire
     * @param dateCreation La date de création du compte
     * @param idResponsable L'identifiant du responsable
     */
    public Societaire(Integer id, String nom, String prenom, String email, String telephone,
            String numeroPermis, LocalDate dateValiditePermis, String adresse, String codePostal,
            String ville, boolean actif, LocalDate dateCreation, Integer idResponsable) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.numeroPermis = numeroPermis;
        this.dateValiditePermis = dateValiditePermis;
        this.adresse = adresse;
        this.codePostal = codePostal;
        this.ville = ville;
        this.actif = actif;
        this.dateCreation = dateCreation != null ? dateCreation : LocalDate.now();
        this.idResponsable = idResponsable;
    }
    
    /**
     * Vérifie si le permis de conduire est valide à la date actuelle
     * 
     * @return true si le permis est valide, false sinon
     */
    public boolean permisValide() {
        return dateValiditePermis != null && !dateValiditePermis.isBefore(LocalDate.now());
    }
    
    /**
     * Calcule le nombre de jours avant expiration du permis
     * 
     * @return le nombre de jours avant expiration, ou 0 si le permis est expiré
     */
    public long joursAvantExpirationPermis() {
        if (dateValiditePermis == null) {
            return 0;
        }
        
        LocalDate today = LocalDate.now();
        if (dateValiditePermis.isBefore(today)) {
            return 0;
        }
        
        return java.time.temporal.ChronoUnit.DAYS.between(today, dateValiditePermis);
    }
    
    /**
     * Indique si le permis expire dans moins de n jours
     * 
     * @param jours Le nombre de jours pour le seuil d'alerte
     * @return true si le permis expire dans moins de n jours, false sinon
     */
    public boolean permisExpireBientot(int jours) {
        return permisValide() && joursAvantExpirationPermis() <= jours;
    }
    
    /**
     * Retourne le nom complet du sociétaire (prénom nom)
     * 
     * @return Le nom complet formaté
     */
    public String getNomComplet() {
        return prenom + " " + nom;
    }

    // Getters et Setters
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getNumeroPermis() {
        return numeroPermis;
    }

    public void setNumeroPermis(String numeroPermis) {
        this.numeroPermis = numeroPermis;
    }

    public LocalDate getDateValiditePermis() {
        return dateValiditePermis;
    }

    public void setDateValiditePermis(LocalDate dateValiditePermis) {
        this.dateValiditePermis = dateValiditePermis;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Integer getIdResponsable() {
        return idResponsable;
    }

    public void setIdResponsable(Integer idResponsable) {
        this.idResponsable = idResponsable;
    }
    
    @Override
    public String toString() {
        return nom + " " + prenom;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Societaire other = (Societaire) obj;
        return id != null && id.equals(other.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}