package main.java.com.miage.parcauto.dto;

// Supposons que l'enum TypeNotification est défini dans votre modèle Notification:
// import main.java.com.miage.parcauto.model.notification.Notification.TypeNotification;

import java.io.Serializable;
import java.time.LocalDateTime;

public class NotificationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // Supposons l'existence de cet enum dans votre modèle Notification.java
    // Si ce n'est pas le cas, il faudra le créer ou utiliser un String ici.
    public enum TypeNotificationDTO {
        INFO,
        ALERTE,
        ERREUR,
        SUCCES
    }

    private Integer idNotification;
    private Integer idUtilisateur; // Ou String loginUtilisateur si plus pertinent pour affichage
    private String utilisateurInfo;
    private String titre;
    private String message;
    private TypeNotificationDTO type;
    private LocalDateTime dateCreation;
    private boolean vue;
    private String actionUrl;

    public NotificationDTO() {
    }

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

    public String getUtilisateurInfo() {
        return utilisateurInfo;
    }

    public void setUtilisateurInfo(String utilisateurInfo) {
        this.utilisateurInfo = utilisateurInfo;
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

    public TypeNotificationDTO getType() {
        return type;
    }

    public void setType(TypeNotificationDTO type) {
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
        return "NotificationDTO{" +
                "idNotification=" + idNotification +
                ", titre='" + titre + '\'' +
                ", type=" + type +
                ", vue=" + vue +
                '}';
    }
}