package main.java.com.miage.parcauto.model.rh;

import main.java.com.miage.parcauto.model.vehicule.Vehicule;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entité représentant un membre du personnel de l'entreprise.
 * Correspond à un enregistrement de la table PERSONNEL.
 */
public class Personnel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idPersonnel;
    private Service service; // Relation avec Service (via id_service)
    private Fonction fonction; // Relation avec Fonction (via id_fonction)
    private Vehicule vehiculeAttribution; // Véhicule directement attribué (via id_vehicule)
    private String matricule;
    private String nomPersonnel;
    private String prenomPersonnel;
    private String email;
    private String telephone;
    private String adresse;
    private LocalDate dateNaissance;
    private Sexe sexe;
    private LocalDateTime dateAttribution; // Date d'attribution du véhicule lié (PERSONNEL.id_vehicule)

    // Note: La relation avec Utilisateur (pour login, role) sera gérée via l'entité Utilisateur
    // qui aura une référence vers Personnel (id_personnel).

    /**
     * Constructeur par défaut.
     */
    public Personnel() {
    }

    /**
     * Constructeur avec tous les paramètres.
     *
     * @param idPersonnel        L'identifiant unique du membre du personnel.
     * @param service            Le service auquel le personnel est rattaché.
     * @param fonction           La fonction occupée par le personnel.
     * @param vehiculeAttribution Le véhicule potentiellement attribué directement au personnel.
     * @param matricule          Le matricule unique du personnel.
     * @param nomPersonnel       Le nom de famille du personnel.
     * @param prenomPersonnel    Le prénom du personnel.
     * @param email              L'adresse email du personnel.
     * @param telephone          Le numéro de téléphone du personnel.
     * @param adresse            L'adresse postale du personnel.
     * @param dateNaissance      La date de naissance du personnel.
     * @param sexe               Le sexe du personnel.
     * @param dateAttribution    La date d'attribution du véhicule (si un véhicule est directement attribué).
     */
    public Personnel(Integer idPersonnel, Service service, Fonction fonction, Vehicule vehiculeAttribution,
                     String matricule, String nomPersonnel, String prenomPersonnel, String email,
                     String telephone, String adresse, LocalDate dateNaissance, Sexe sexe,
                     LocalDateTime dateAttribution) {
        this.idPersonnel = idPersonnel;
        this.service = service;
        this.fonction = fonction;
        this.vehiculeAttribution = vehiculeAttribution;
        this.matricule = matricule;
        this.nomPersonnel = nomPersonnel;
        this.prenomPersonnel = prenomPersonnel;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
        this.dateNaissance = dateNaissance;
        this.sexe = sexe;
        this.dateAttribution = dateAttribution;
    }

    // Getters et Setters

    public Integer getIdPersonnel() {
        return idPersonnel;
    }

    public void setIdPersonnel(Integer idPersonnel) {
        this.idPersonnel = idPersonnel;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Fonction getFonction() {
        return fonction;
    }

    public void setFonction(Fonction fonction) {
        this.fonction = fonction;
    }

    public Vehicule getVehiculeAttribution() {
        return vehiculeAttribution;
    }

    public void setVehiculeAttribution(Vehicule vehiculeAttribution) {
        this.vehiculeAttribution = vehiculeAttribution;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNomPersonnel() {
        return nomPersonnel;
    }

    public void setNomPersonnel(String nomPersonnel) {
        this.nomPersonnel = nomPersonnel;
    }

    public String getPrenomPersonnel() {
        return prenomPersonnel;
    }

    public void setPrenomPersonnel(String prenomPersonnel) {
        this.prenomPersonnel = prenomPersonnel;
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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public LocalDateTime getDateAttribution() {
        return dateAttribution;
    }

    public void setDateAttribution(LocalDateTime dateAttribution) {
        this.dateAttribution = dateAttribution;
    }

    /**
     * Retourne le nom complet (prénom nom) du membre du personnel.
     * @return Le nom complet.
     */
    public String getNomComplet() {
        String prenom = (prenomPersonnel != null ? prenomPersonnel : "");
        String nom = (nomPersonnel != null ? nomPersonnel : "");
        return (prenom + " " + nom).trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Personnel personnel = (Personnel) o;
        return Objects.equals(idPersonnel, personnel.idPersonnel) ||
                (matricule != null && Objects.equals(matricule, personnel.matricule));
    }

    @Override
    public int hashCode() {
        // Utiliser matricule s'il est non nul et unique, sinon idPersonnel.
        // Pour être safe, on peut baser le hash sur l'ID si disponible, sinon matricule.
        if (idPersonnel != null) {
            return Objects.hash(idPersonnel);
        }
        return Objects.hash(matricule);
    }

    @Override
    public String toString() {
        return "Personnel{" +
                "idPersonnel=" + idPersonnel +
                ", matricule='" + matricule + '\'' +
                ", nomComplet='" + getNomComplet() + '\'' +
                ", email='" + email + '\'' +
                ", fonction=" + (fonction != null ? fonction.getLibFonction() : "N/A") +
                '}';
    }
}