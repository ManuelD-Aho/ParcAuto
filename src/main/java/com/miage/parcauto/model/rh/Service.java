package main.java.com.miage.parcauto.model.rh;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Entité représentant un service ou un département au sein de l'entreprise.
 * Correspond à un enregistrement de la table SERVICE.
 */
public class Service implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer idService;
    private String libService; // Libellé du service
    private String localisationService; // Localisation géographique ou bureau du service

    /**
     * Constructeur par défaut.
     */
    public Service() {
    }

    /**
     * Constructeur avec tous les paramètres.
     *
     * @param idService            L'identifiant unique du service.
     * @param libService           Le libellé du service (ex: "Direction", "Logistique", "Maintenance").
     * @param localisationService  La localisation du service.
     */
    public Service(Integer idService, String libService, String localisationService) {
        this.idService = idService;
        this.libService = libService;
        this.localisationService = localisationService;
    }

    // Getters et Setters

    public Integer getIdService() {
        return idService;
    }

    public void setIdService(Integer idService) {
        this.idService = idService;
    }

    public String getLibService() {
        return libService;
    }

    public void setLibService(String libService) {
        this.libService = libService;
    }

    public String getLocalisationService() {
        return localisationService;
    }

    public void setLocalisationService(String localisationService) {
        this.localisationService = localisationService;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return Objects.equals(idService, service.idService);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idService);
    }

    @Override
    public String toString() {
        return "Service{" +
                "idService=" + idService +
                ", libService='" + libService + '\'' +
                ", localisationService='" + localisationService + '\'' +
                '}';
    }
}