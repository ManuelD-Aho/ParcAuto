package main.java.com.miage.parcauto.model.rh;

import java.io.Serializable;
import java.util.Objects;

/**
 * Classe modèle représentant un service dans l'organisation.
 * Correspond à la table SERVICE dans la base de données.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class Service implements Serializable {

    private static final long serialVersionUID = 1L;

    // Attributs correspondant aux colonnes de la table SERVICE
    private Integer idService;
    private String libService;
    private String localisationService;

    /**
     * Constructeur par défaut
     */
    public Service() {
    }

    /**
     * Constructeur avec tous les champs
     *
     * @param idService ID unique du service
     * @param libService Libellé du service
     * @param localisationService Localisation physique du service
     */
    public Service(Integer idService, String libService, String localisationService) {
        this.idService = idService;
        this.libService = libService;
        this.localisationService = localisationService;
    }

    /**
     * Constructeur sans ID pour création d'un nouveau service
     *
     * @param libService Libellé du service
     * @param localisationService Localisation physique du service
     */
    public Service(String libService, String localisationService) {
        this.libService = libService;
        this.localisationService = localisationService;
    }

    // Getters et setters

    /**
     * Obtient l'identifiant unique du service
     *
     * @return l'identifiant du service
     */
    public Integer getIdService() {
        return idService;
    }

    /**
     * Définit l'identifiant unique du service
     *
     * @param idService l'identifiant à définir
     */
    public void setIdService(Integer idService) {
        this.idService = idService;
    }

    /**
     * Obtient le libellé du service
     *
     * @return le libellé du service
     */
    public String getLibService() {
        return libService;
    }

    /**
     * Définit le libellé du service
     *
     * @param libService le libellé à définir
     */
    public void setLibService(String libService) {
        this.libService = libService;
    }

    /**
     * Obtient la localisation physique du service
     *
     * @return la localisation du service
     */
    public String getLocalisationService() {
        return localisationService;
    }

    /**
     * Définit la localisation physique du service
     *
     * @param localisationService la localisation à définir
     */
    public void setLocalisationService(String localisationService) {
        this.localisationService = localisationService;
    }

    /**
     * Représentation textuelle du service
     *
     * @return une chaîne représentant le service
     */
    @Override
    public String toString() {
        return libService + " (" + localisationService + ")";
    }

    /**
     * Vérifie l'égalité entre ce service et un autre objet
     *
     * @param obj l'objet à comparer
     * @return true si les objets sont égaux, false sinon
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Service service = (Service) obj;
        return Objects.equals(idService, service.idService);
    }

    /**
     * Calcule le code de hachage pour ce service
     *
     * @return le code de hachage
     */
    @Override
    public int hashCode() {
        return Objects.hash(idService);
    }

    /**
     * Crée un clone de cet objet Service
     *
     * @return une nouvelle instance de Service avec les mêmes valeurs
     */
    public Service clone() {
        return new Service(idService, libService, localisationService);
    }

    /**
     * Valide les données du service
     *
     * @return true si les données sont valides, false sinon
     */
    public boolean isValid() {
        return libService != null && !libService.trim().isEmpty()
                && localisationService != null && !localisationService.trim().isEmpty();
    }
}