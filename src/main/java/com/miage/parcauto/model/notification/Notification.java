package main.java.com.miage.parcauto.model.notification;

import java.time.LocalDateTime;

public class Notification {
    private Integer idNotification;
    private Integer idUtilisateur;
    private String titre;
    private String message;
    private LocalDateTime dateCreation;
    private Boolean estLue;
    private String typeNotification;
    private Integer idEntiteLiee;
    private String typeEntiteLiee;

    public Notification() {
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

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Boolean isEstLue() {
        return estLue;
    }

    public void setEstLue(Boolean estLue) {
        this.estLue = estLue;
    }

    public String getTypeNotification() {
        return typeNotification;
    }

    public void setTypeNotification(String typeNotification) {
        this.typeNotification = typeNotification;
    }

    public Integer getIdEntiteLiee() {
        return idEntiteLiee;
    }

    public void setIdEntiteLiee(Integer idEntiteLiee) {
        this.idEntiteLiee = idEntiteLiee;
    }

    public String getTypeEntiteLiee() {
        return typeEntiteLiee;
    }

    public void setTypeEntiteLiee(String typeEntiteLiee) {
        this.typeEntiteLiee = typeEntiteLiee;
    }
}