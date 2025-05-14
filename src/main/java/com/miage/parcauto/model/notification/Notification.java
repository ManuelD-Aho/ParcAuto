package main.java.com.miage.parcauto.model.notification;

import java.time.LocalDateTime;

/**
 * Classe représentant une notification dans le système.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class Notification {

    /**
     * Types de notification possibles.
     */
    public enum TypeNotification {
        INFO,
        ALERTE,
        ERREUR,
        SUCCES
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
    public Notification() {
        this.dateCreation = LocalDateTime.now();
        this.vue = false;
    }

    /**
     * Constructeur avec paramètres de base.
     * 
     * @param idUtilisateur ID de l'utilisateur destinataire
     * @param titre         Titre de la notification
     * @param message       Message de la notification
     * @param type          Type de notification
     */
    public Notification(Integer idUtilisateur, String titre, String message, TypeNotification type) {
        this();
        this.idUtilisateur = idUtilisateur;
        this.titre = titre;
        this.message = message;
        this.type = type;
    }

    // Getters et setters

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
        return "Notification [idNotification=" + idNotification + ", type=" + type + ", titre=" + titre
                + ", dateCreation=" + dateCreation + ", vue=" + vue + "]";
    }
}
