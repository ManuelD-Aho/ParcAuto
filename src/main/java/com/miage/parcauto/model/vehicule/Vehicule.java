package main.java.com.miage.parcauto.model.vehicule;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Classe modèle représentant un véhicule du parc automobile.
 * Cette classe correspond à la table VEHICULES dans la base de données.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class Vehicule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Énumération des types d'énergie possibles pour un véhicule
     */
    public enum TypeEnergie {
        Diesel, Essence, Électrique, Hybride;

        public static TypeEnergie fromString(String value) {
            if (value == null) return Diesel;

            try {
                return TypeEnergie.valueOf(value);
            } catch (IllegalArgumentException e) {
                return Diesel;
            }
        }
    }

    // Attributs correspondant aux colonnes de la table VEHICULES
    private Integer idVehicule;
    private EtatVoiture etatVoiture;
    private TypeEnergie energie;
    private String numeroChassi;
    private String immatriculation;
    private String marque;
    private String modele;
    private Integer nbPlaces;
    private LocalDateTime dateAcquisition;
    private LocalDateTime dateAmmortissement;
    private LocalDateTime dateMiseEnService;
    private Integer puissance;
    private String couleur;
    private BigDecimal prixVehicule;
    private Integer kmActuels;
    private LocalDateTime dateEtat;

    /**
     * Constructeur par défaut
     */
    public Vehicule() {
        this.energie = TypeEnergie.Diesel;
        this.kmActuels = 0;
        this.dateEtat = LocalDateTime.now();
    }

    /**
     * Constructeur avec tous les attributs essentiels
     *
     * @param etatVoiture État actuel du véhicule
     * @param energie Type d'énergie du véhicule
     * @param numeroChassi Numéro de châssis unique
     * @param immatriculation Plaque d'immatriculation
     * @param marque Marque du véhicule
     * @param modele Modèle du véhicule
     * @param nbPlaces Nombre de places disponibles
     * @param dateAcquisition Date d'acquisition
     * @param prixVehicule Prix d'achat
     */
    public Vehicule(EtatVoiture etatVoiture, TypeEnergie energie, String numeroChassi,
                    String immatriculation, String marque, String modele,
                    Integer nbPlaces, LocalDateTime dateAcquisition, BigDecimal prixVehicule) {
        this.etatVoiture = etatVoiture;
        this.energie = energie;
        this.numeroChassi = numeroChassi;
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.modele = modele;
        this.nbPlaces = nbPlaces;
        this.dateAcquisition = dateAcquisition;
        this.prixVehicule = prixVehicule;
        this.kmActuels = 0;
        this.dateEtat = LocalDateTime.now();
    }

    // Getters et Setters

    public Integer getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(Integer idVehicule) {
        this.idVehicule = idVehicule;
    }

    public EtatVoiture getEtatVoiture() {
        return etatVoiture;
    }

    public void setEtatVoiture(EtatVoiture etatVoiture) {
        this.etatVoiture = etatVoiture;
        this.dateEtat = LocalDateTime.now();
    }

    public TypeEnergie getEnergie() {
        return energie;
    }

    public void setEnergie(TypeEnergie energie) {
        this.energie = energie;
    }

    public String getNumeroChassi() {
        return numeroChassi;
    }

    public void setNumeroChassi(String numeroChassi) {
        this.numeroChassi = numeroChassi;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public Integer getNbPlaces() {
        return nbPlaces;
    }

    public void setNbPlaces(Integer nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public LocalDateTime getDateAcquisition() {
        return dateAcquisition;
    }

    public void setDateAcquisition(LocalDateTime dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
    }

    public LocalDateTime getDateAmmortissement() {
        return dateAmmortissement;
    }

    public void setDateAmmortissement(LocalDateTime dateAmmortissement) {
        this.dateAmmortissement = dateAmmortissement;
    }

    public LocalDateTime getDateMiseEnService() {
        return dateMiseEnService;
    }

    public void setDateMiseEnService(LocalDateTime dateMiseEnService) {
        this.dateMiseEnService = dateMiseEnService;
    }

    public Integer getPuissance() {
        return puissance;
    }

    public void setPuissance(Integer puissance) {
        this.puissance = puissance;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public BigDecimal getPrixVehicule() {
        return prixVehicule;
    }

    public void setPrixVehicule(BigDecimal prixVehicule) {
        this.prixVehicule = prixVehicule;
    }

    public Integer getKmActuels() {
        return kmActuels;
    }

    public void setKmActuels(Integer kmActuels) {
        this.kmActuels = kmActuels;
    }

    public LocalDateTime getDateEtat() {
        return dateEtat;
    }

    public void setDateEtat(LocalDateTime dateEtat) {
        this.dateEtat = dateEtat;
    }

    // Méthodes métier

    /**
     * Vérifie si le véhicule est actuellement disponible pour une mission ou attribution
     * @return true si le véhicule est dans l'état "disponible", false sinon
     */
    public boolean estDisponible() {
        return etatVoiture != null && etatVoiture.getIdEtatVoiture() == EtatVoiture.DISPONIBLE;
    }

    /**
     * Vérifie si le véhicule est actuellement en mission
     * @return true si le véhicule est dans l'état "en mission", false sinon
     */
    public boolean estEnMission() {
        return etatVoiture != null && etatVoiture.getIdEtatVoiture() == EtatVoiture.EN_MISSION;
    }

    /**
     * Vérifie si le véhicule est actuellement hors service
     * @return true si le véhicule est dans l'état "hors service", false sinon
     */
    public boolean estHorsService() {
        return etatVoiture != null && etatVoiture.getIdEtatVoiture() == EtatVoiture.HORS_SERVICE;
    }

    /**
     * Vérifie si le véhicule est actuellement en panne
     * @return true si le véhicule est dans l'état "panne", false sinon
     */
    public boolean estEnPanne() {
        return etatVoiture != null && etatVoiture.getIdEtatVoiture() == EtatVoiture.PANNE;
    }

    /**
     * Vérifie si le véhicule est actuellement en entretien
     * @return true si le véhicule est dans l'état "en entretien", false sinon
     */
    public boolean estEnEntretien() {
        return etatVoiture != null && etatVoiture.getIdEtatVoiture() == EtatVoiture.EN_ENTRETIEN;
    }

    /**
     * Vérifie si le véhicule est actuellement attribué à un responsable
     * @return true si le véhicule est dans l'état "attribué", false sinon
     */
    public boolean estAttribue() {
        return etatVoiture != null && etatVoiture.getIdEtatVoiture() == EtatVoiture.ATTRIBUER;
    }

    /**
     * Change l'état du véhicule si la transition est autorisée
     *
     * @param nouvelEtat Le nouvel état du véhicule
     * @return true si le changement d'état a été effectué, false si la transition n'est pas autorisée
     */
    public boolean changerEtat(EtatVoiture nouvelEtat) {
        if (nouvelEtat == null) {
            return false;
        }

        // Vérifications spécifiques selon l'état actuel
        if (estEnMission() && nouvelEtat.getIdEtatVoiture() != EtatVoiture.DISPONIBLE &&
                nouvelEtat.getIdEtatVoiture() != EtatVoiture.PANNE) {
            return false; // En mission, on ne peut passer qu'à disponible ou panne
        }

        if (estAttribue() && nouvelEtat.getIdEtatVoiture() != EtatVoiture.DISPONIBLE &&
                nouvelEtat.getIdEtatVoiture() != EtatVoiture.PANNE) {
            return false; // Si attribué, on ne peut passer qu'à disponible ou panne
        }

        if (estHorsService() && nouvelEtat.getIdEtatVoiture() != EtatVoiture.DISPONIBLE) {
            return false; // Si hors service, on ne peut repasser qu'à disponible
        }

        // Si aucune règle ne bloque, on effectue le changement
        setEtatVoiture(nouvelEtat);
        return true;
    }

    /**
     * Calcule si le véhicule est amorti financièrement à la date actuelle
     *
     * @return true si la date d'amortissement est passée, false sinon
     */
    public boolean estAmorti() {
        return dateAmmortissement != null && dateAmmortissement.isBefore(LocalDateTime.now());
    }

    /**
     * Calcule la durée restante avant amortissement en mois
     *
     * @return le nombre de mois restants avant amortissement, ou 0 si déjà amorti
     */
    public long moisAvantAmortissement() {
        if (estAmorti()) {
            return 0;
        }

        if (dateAmmortissement == null) {
            return 0;
        }

        LocalDateTime now = LocalDateTime.now();
        // Calcul approximatif en mois
        return (dateAmmortissement.getYear() - now.getYear()) * 12 +
                (dateAmmortissement.getMonthValue() - now.getMonthValue());
    }

    /**
     * Calcule l'âge du véhicule en années depuis sa date d'acquisition
     *
     * @return l'âge du véhicule en années, ou 0 si la date d'acquisition n'est pas définie
     */
    public int ageVehicule() {
        if (dateAcquisition == null) {
            return 0;
        }

        return LocalDateTime.now().getYear() - dateAcquisition.getYear();
    }

    /**
     * Retourne une description complète du véhicule
     *
     * @return La description du véhicule au format "Marque Modele (Immatriculation)"
     */
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        if (marque != null) {
            sb.append(marque);
        }

        if (modele != null) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(modele);
        }

        if (immatriculation != null) {
            if (sb.length() > 0) {
                sb.append(" (");
            }
            sb.append(immatriculation);
            if (sb.length() > 0) {
                sb.append(")");
            }
        }

        return sb.toString();
    }

    /**
     * Vérifie si ce véhicule nécessite un entretien préventif basé sur le kilométrage
     *
     * @param intervalleKm l'intervalle de kilométrage entre entretiens préventifs
     * @return true si un entretien est recommandé
     */
    public boolean necessiteEntretien(int intervalleKm) {
        return kmActuels != null && kmActuels > 0 && kmActuels % intervalleKm < 500;
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
        Vehicule vehicule = (Vehicule) obj;
        return Objects.equals(idVehicule, vehicule.idVehicule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVehicule);
    }

    @Override
    public String toString() {
        return getDescription();
    }

    /**
     * Valide les données essentielles du véhicule
     *
     * @return true si les données sont valides, false sinon
     */
    public boolean isValid() {
        return numeroChassi != null && !numeroChassi.trim().isEmpty()
                && immatriculation != null && !immatriculation.trim().isEmpty()
                && marque != null && !marque.trim().isEmpty()
                && modele != null && !modele.trim().isEmpty()
                && etatVoiture != null
                && dateAcquisition != null
                && prixVehicule != null && prixVehicule.compareTo(BigDecimal.ZERO) > 0;
    }
}