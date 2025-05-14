package main.java.com.miage.parcauto.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Objet de transfert de données (DTO) pour les notifications.
 * Utilisé pour transférer des données entre la couche service et la couche
 * présentation.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class NotificationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Enum représentant les différents types de notification.
     */
    public enum TypeNotification {
        INFO("Information", "notification-info"),
        ALERTE("Alerte", "notification-warning"),
        ERREUR("Erreur", "notification-error"),
        SUCCES("Succès", "notification-success");

        private final String libelle;
        private final String cssClass;

        TypeNotification(String libelle, String cssClass) {
            this.libelle = libelle;
            this.cssClass = cssClass;
        }

        public String getLibelle() {
            return libelle;
        }

        public String getCssClass() {
            return cssClass;
        }
    }

    private Integer idNotification;
    private Integer idUtilisateur;
    private String titre;
    private String message;
    private TypeNotification type;
    private LocalDateTime dateCreation;
    private boolean vue;
    private String actionUrl;

    /**
     * Constructeur par défaut.
     */
    public NotificationDTO() {
        this.dateCreation = LocalDateTime.now();
        this.vue = false;
    }

    /**
     * Constructeur avec paramètres de base.
     * 
     * @param titre   Titre de la notification
     * @param message Message de la notification
     * @param type    Type de notification
     */
    public NotificationDTO(String titre, String message, TypeNotification type) {
        this();
        this.titre = titre;
        this.message = message;
        this.type = type;
    }

    /**
     * Constructeur avec paramètres complets.
     * 
     * @param idUtilisateur ID de l'utilisateur destinataire
     * @param titre         Titre de la notification
     * @param message       Message de la notification
     * @param type          Type de notification
     * @param actionUrl     URL d'action associée (optionnel)
     */
    public NotificationDTO(Integer idUtilisateur, String titre, String message, TypeNotification type,
            String actionUrl) {
        this(titre, message, type);
        this.idUtilisateur = idUtilisateur;
        this.actionUrl = actionUrl;
    }

    // Getters et Setters

    public Integer getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(Integer idNotification) {
        this.idNotification = idNotification;
    }

    public Integer getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Integer idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TypeNotification getType() {
        return type;
    }

    public void setType(TypeNotification type) {
        this.type = type;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public boolean isVue() {
        return vue;
    }

    public void setVue(boolean vue) {
        this.vue = vue;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    @Override
    public String toString() {
        return "NotificationDTO [idNotification=" + idNotification + ", type=" + type + ", titre=" + titre
                + ", dateCreation="
                + dateCreation + ", vue=" + vue + "]";
    }
}
