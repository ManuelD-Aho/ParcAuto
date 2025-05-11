package main.java.com.miage.parcauto.model.rh;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Classe modèle représentant un membre du personnel dans l'organisation.
 * Correspond à la table PERSONNEL dans la base de données.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class Personnel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Énumération pour le sexe du personnel
     */
    public enum Sexe {
        M, F;

        public static Sexe fromString(String value) {
            if (value == null) return M;

            try {
                return Sexe.valueOf(value);
            } catch (IllegalArgumentException e) {
                return M;
            }
        }
    }

    // Attributs correspondant aux colonnes de la table PERSONNEL
    private Integer idPersonnel;
    private Service service;
    private Fonction fonction;
    private Integer idVehicule; // ID du véhicule attribué, si applicable
    private String matricule;
    private String nomPersonnel;
    private String prenomPersonnel;
    private String email;
    private String telephone;
    private String adresse;
    private LocalDate dateNaissance;
    private Sexe sexe;
    private LocalDateTime dateAttribution;

    /**
     * Constructeur par défaut
     */
    public Personnel() {
        this.sexe = Sexe.M;
    }

    /**
     * Constructeur avec les attributs essentiels
     *
     * @param matricule Matricule unique du personnel
     * @param nomPersonnel Nom de famille
     * @param prenomPersonnel Prénom
     * @param email Adresse email
     * @param service Service d'affectation
     * @param fonction Fonction occupée
     */
    public Personnel(String matricule, String nomPersonnel, String prenomPersonnel,
                     String email, Service service, Fonction fonction) {
        this.matricule = matricule;
        this.nomPersonnel = nomPersonnel;
        this.prenomPersonnel = prenomPersonnel;
        this.email = email;
        this.service = service;
        this.fonction = fonction;
        this.sexe = Sexe.M;
    }

    /**
     * Constructeur complet avec tous les attributs
     */
    public Personnel(String matricule, String nomPersonnel, String prenomPersonnel,
                     String email, String telephone, String adresse,
                     LocalDate dateNaissance, Sexe sexe,
                     Service service, Fonction fonction) {
        this.matricule = matricule;
        this.nomPersonnel = nomPersonnel;
        this.prenomPersonnel = prenomPersonnel;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
        this.dateNaissance = dateNaissance;
        this.sexe = sexe;
        this.service = service;
        this.fonction = fonction;
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

    public Integer getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(Integer idVehicule) {
        this.idVehicule = idVehicule;
        if (idVehicule != null) {
            this.dateAttribution = LocalDateTime.now();
        } else {
            this.dateAttribution = null;
        }
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

    // Méthodes métier

    /**
     * Vérifie si le personnel a un véhicule attribué
     *
     * @return true si un véhicule est attribué, false sinon
     */
    public boolean aVehiculeAttribue() {
        return idVehicule != null;
    }

    /**
     * Vérifie si le personnel est un manager basé sur sa fonction
     *
     * @return true si la fonction est une position de management
     */
    public boolean estManager() {
        return fonction != null && fonction.isManagementPosition();
    }

    /**
     * Calcule l'âge du personnel en années à partir de sa date de naissance
     *
     * @return l'âge en années, ou 0 si la date de naissance n'est pas définie
     */
    public int age() {
        if (dateNaissance == null) {
            return 0;
        }

        return LocalDate.now().getYear() - dateNaissance.getYear();
    }

    /**
     * Retourne le nom complet du personnel
     *
     * @return le prénom suivi du nom
     */
    public String getNomComplet() {
        StringBuilder sb = new StringBuilder();

        if (prenomPersonnel != null) {
            sb.append(prenomPersonnel);
        }

        if (nomPersonnel != null) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(nomPersonnel);
        }

        return sb.toString();
    }

    /**
     * Retourne le titre de civilité basé sur le sexe
     *
     * @return "M." pour les hommes, "Mme" pour les femmes
     */
    public String getCivilite() {
        return sexe == Sexe.F ? "Mme" : "M.";
    }

    /**
     * Vérifie si le personnel a des informations de contact complètes
     *
     * @return true si l'email et le téléphone sont renseignés
     */
    public boolean aContactsComplets() {
        return email != null && !email.trim().isEmpty() &&
                telephone != null && !telephone.trim().isEmpty();
    }

    /**
     * Vérifie si l'adresse email du personnel est valide (format basique)
     *
     * @return true si l'email est au format valide
     */
    public boolean aEmailValide() {
        return email != null && email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    /**
     * Calcule depuis combien de jours le véhicule actuel est attribué
     *
     * @return le nombre de jours depuis l'attribution, ou 0 si pas de véhicule attribué
     */
    public long joursDepuisAttribution() {
        if (!aVehiculeAttribue() || dateAttribution == null) {
            return 0;
        }

        return java.time.Duration.between(dateAttribution, LocalDateTime.now()).toDays();
    }

    // Méthodes standard

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Personnel personnel = (Personnel) obj;
        return Objects.equals(idPersonnel, personnel.idPersonnel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPersonnel);
    }

    @Override
    public String toString() {
        return getNomComplet();
    }

    /**
     * Valide les données essentielles du personnel
     *
     * @return true si les données sont valides, false sinon
     */
    public boolean isValid() {
        return matricule != null && !matricule.trim().isEmpty()
                && nomPersonnel != null && !nomPersonnel.trim().isEmpty()
                && prenomPersonnel != null && !prenomPersonnel.trim().isEmpty()
                && email != null && !email.trim().isEmpty()
                && aEmailValide();
    }
}