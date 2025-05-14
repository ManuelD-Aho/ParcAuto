package main.java.com.miage.parcauto.viewmodel;

import main.java.com.miage.parcauto.dto.NotificationDTO;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * ViewModel pour représenter une notification dans l'interface utilisateur.
 * Encapsule la logique de formatage et de présentation des données d'une
 * notification.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class NotificationViewModel {

    // Constantes de formatage
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Propriétés JavaFX liées aux attributs de la notification
    private final IntegerProperty idNotification = new SimpleIntegerProperty();
    private final IntegerProperty idUtilisateur = new SimpleIntegerProperty();
    private final StringProperty titre = new SimpleStringProperty();
    private final StringProperty message = new SimpleStringProperty();
    private final ObjectProperty<NotificationDTO.TypeNotification> type = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> dateCreation = new SimpleObjectProperty<>();
    private final BooleanProperty vue = new SimpleBooleanProperty();
    private final StringProperty actionUrl = new SimpleStringProperty();

    // Propriétés dérivées pour l'affichage
    private final StringProperty dateCreationFormatee = new SimpleStringProperty();
    private final StringProperty tempsEcoule = new SimpleStringProperty();
    private final StringProperty typeLibelle = new SimpleStringProperty();
    private final StringProperty cssClass = new SimpleStringProperty();

    /**
     * Constructeur par défaut.
     */
    public NotificationViewModel() {
        // Initialise les propriétés dérivées lorsque les propriétés de base changent
        dateCreation.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                dateCreationFormatee.set(newValue.format(DATE_FORMATTER));
                updateTempsEcoule();
            }
        });

        type.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                typeLibelle.set(newValue.getLibelle());
                cssClass.set(newValue.getCssClass());
            }
        });
    }

    /**
     * Constructeur avec DTO.
     * 
     * @param dto NotificationDTO à convertir en ViewModel
     */
    public NotificationViewModel(NotificationDTO dto) {
        this();
        updateFromDTO(dto);
    }

    /**
     * Met à jour les propriétés du ViewModel à partir d'un DTO.
     * 
     * @param dto NotificationDTO source des données
     */
    public final void updateFromDTO(NotificationDTO dto) {
        if (dto == null) {
            return;
        }

        idNotification.set(dto.getIdNotification() != null ? dto.getIdNotification() : 0);
        idUtilisateur.set(dto.getIdUtilisateur() != null ? dto.getIdUtilisateur() : 0);
        titre.set(dto.getTitre());
        message.set(dto.getMessage());
        type.set(dto.getType());
        dateCreation.set(dto.getDateCreation());
        vue.set(dto.isVue());
        actionUrl.set(dto.getActionUrl());

        // Les propriétés dérivées sont mises à jour automatiquement via les listeners
    }

    /**
     * Convertit le ViewModel en DTO.
     * 
     * @return NotificationDTO correspondant au ViewModel
     */
    public NotificationDTO toDTO() {
        NotificationDTO dto = new NotificationDTO();

        if (idNotification.get() > 0) {
            dto.setIdNotification(idNotification.get());
        }

        if (idUtilisateur.get() > 0) {
            dto.setIdUtilisateur(idUtilisateur.get());
        }

        dto.setTitre(titre.get());
        dto.setMessage(message.get());
        dto.setType(type.get());
        dto.setDateCreation(dateCreation.get());
        dto.setVue(vue.get());
        dto.setActionUrl(actionUrl.get());

        return dto;
    }

    /**
     * Met à jour le temps écoulé depuis la création de la notification.
     * Peut être appelé régulièrement pour actualiser l'affichage.
     */
    public void updateTempsEcoule() {
        if (dateCreation.get() == null) {
            tempsEcoule.set("");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime date = dateCreation.get();

        long minutes = ChronoUnit.MINUTES.between(date, now);
        long heures = ChronoUnit.HOURS.between(date, now);
        long jours = ChronoUnit.DAYS.between(date, now);

        if (minutes < 1) {
            tempsEcoule.set("À l'instant");
        } else if (minutes < 60) {
            tempsEcoule.set("Il y a " + minutes + (minutes == 1 ? " minute" : " minutes"));
        } else if (heures < 24) {
            tempsEcoule.set("Il y a " + heures + (heures == 1 ? " heure" : " heures"));
        } else if (jours < 7) {
            tempsEcoule.set("Il y a " + jours + (jours == 1 ? " jour" : " jours"));
        } else {
            tempsEcoule.set(date.format(DATE_FORMATTER));
        }
    }

    // Getters pour les propriétés

    public IntegerProperty idNotificationProperty() {
        return idNotification;
    }

    public IntegerProperty idUtilisateurProperty() {
        return idUtilisateur;
    }

    public StringProperty titreProperty() {
        return titre;
    }

    public StringProperty messageProperty() {
        return message;
    }

    public ObjectProperty<NotificationDTO.TypeNotification> typeProperty() {
        return type;
    }

    public ObjectProperty<LocalDateTime> dateCreationProperty() {
        return dateCreation;
    }

    public BooleanProperty vueProperty() {
        return vue;
    }

    public StringProperty actionUrlProperty() {
        return actionUrl;
    }

    public StringProperty dateCreationFormateeProperty() {
        return dateCreationFormatee;
    }

    public StringProperty tempsEcouleProperty() {
        return tempsEcoule;
    }

    public StringProperty typeLibelleProperty() {
        return typeLibelle;
    }

    public StringProperty cssClassProperty() {
        return cssClass;
    }

    // Getters et Setters standards

    public int getIdNotification() {
        return idNotification.get();
    }

    public void setIdNotification(int value) {
        idNotification.set(value);
    }

    public int getIdUtilisateur() {
        return idUtilisateur.get();
    }

    public void setIdUtilisateur(int value) {
        idUtilisateur.set(value);
    }

    public String getTitre() {
        return titre.get();
    }

    public void setTitre(String value) {
        titre.set(value);
    }

    public String getMessage() {
        return message.get();
    }

    public void setMessage(String value) {
        message.set(value);
    }

    public NotificationDTO.TypeNotification getType() {
        return type.get();
    }

    public void setType(NotificationDTO.TypeNotification value) {
        type.set(value);
    }

    public LocalDateTime getDateCreation() {
        return dateCreation.get();
    }

    public void setDateCreation(LocalDateTime value) {
        dateCreation.set(value);
    }

    public boolean isVue() {
        return vue.get();
    }

    public void setVue(boolean value) {
        vue.set(value);
    }

    public String getActionUrl() {
        return actionUrl.get();
    }

    public void setActionUrl(String value) {
        actionUrl.set(value);
    }

    public String getDateCreationFormatee() {
        return dateCreationFormatee.get();
    }

    public String getTempsEcoule() {
        return tempsEcoule.get();
    }

    public String getTypeLibelle() {
        return typeLibelle.get();
    }

    public String getCssClass() {
        return cssClass.get();
    }
}
