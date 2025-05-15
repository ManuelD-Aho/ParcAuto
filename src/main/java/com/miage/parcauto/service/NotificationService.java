package main.java.com.miage.parcauto.service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.com.miage.parcauto.dao.NotificationRepositoryImpl;
import main.java.com.miage.parcauto.dto.NotificationDTO;
import main.java.com.miage.parcauto.dto.NotificationDTO.TypeNotification;
import main.java.com.miage.parcauto.exception.DatabaseException;
import main.java.com.miage.parcauto.mapper.NotificationMapper;
import main.java.com.miage.parcauto.model.notification.Notification;
import main.java.com.miage.parcauto.security.SessionManager;

/**
 * Service de gestion des notifications pour l'application ParcAuto.
 * Gère les notifications pour les utilisateurs et fournit des méthodes pour
 * créer, récupérer et marquer les notifications comme lues.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class NotificationService {

    private static final Logger LOGGER = Logger.getLogger(NotificationService.class.getName());

    private final NotificationRepository notificationRepository;
    private final ObservableList<NotificationDTO> notifications;

    /**
     * Instance unique du service (pattern Singleton).
     */
    private static NotificationService instance;

    /**
     * Constructeur privé.
     */
    private NotificationService() {
        this.notificationRepository = new NotificationRepositoryImpl();
        this.notifications = FXCollections.observableArrayList();
    }

    /**
     * Retourne l'instance unique du service.
     * 
     * @return Instance du service
     */
    public static synchronized NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }

    /**
     * Envoie une notification à un utilisateur.
     * 
     * @param notification La notification à envoyer
     * @return La notification créée avec son ID
     */
    public NotificationDTO envoyerNotification(NotificationDTO notification) {
        try {
            Notification entity = NotificationMapper.toEntity(notification);
            Notification savedEntity = notificationRepository.save(entity);
            NotificationDTO savedDTO = NotificationMapper.toDTO(savedEntity);

            // Ajoute la notification à la liste observable si l'utilisateur est le
            // destinataire
            if (estUtilisateurCourant(savedDTO.getIdUtilisateur())) {
                Platform.runLater(() -> {
                    notifications.add(0, savedDTO); // Ajoute en tête de liste
                });
            }

            return savedDTO;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'envoi d'une notification", e);
            throw new DatabaseException("Erreur lors de l'envoi d'une notification: " + e.getMessage(), e);
        }
    }

    /**
     * Crée et envoie une notification à un utilisateur.
     * 
     * @param idUtilisateur ID de l'utilisateur destinataire
     * @param titre         Titre de la notification
     * @param message       Message de la notification
     * @param type          Type de notification
     * @return La notification créée
     */
    public NotificationDTO envoyerNotification(Integer idUtilisateur, String titre, String message,
            TypeNotification type) {
        NotificationDTO notification = new NotificationDTO(titre, message, type);
        notification.setIdUtilisateur(idUtilisateur);
        return envoyerNotification(notification);
    }

    /**
     * Envoie une notification d'alerte à un utilisateur.
     * 
     * @param idUtilisateur ID de l'utilisateur destinataire
     * @param titre         Titre de l'alerte
     * @param message       Message de l'alerte
     * @return La notification créée
     */
    public NotificationDTO envoyerAlerte(Integer idUtilisateur, String titre, String message) {
        return envoyerNotification(idUtilisateur, titre, message, TypeNotification.ALERTE);
    }

    /**
     * Envoie une notification d'information à un utilisateur.
     * 
     * @param idUtilisateur ID de l'utilisateur destinataire
     * @param titre         Titre de l'information
     * @param message       Message de l'information
     * @return La notification créée
     */
    public NotificationDTO envoyerInfo(Integer idUtilisateur, String titre, String message) {
        return envoyerNotification(idUtilisateur, titre, message, TypeNotification.INFO);
    }

    /**
     * Envoie une notification de succès à un utilisateur.
     * 
     * @param idUtilisateur ID de l'utilisateur destinataire
     * @param titre         Titre du succès
     * @param message       Message du succès
     * @return La notification créée
     */
    public NotificationDTO envoyerSucces(Integer idUtilisateur, String titre, String message) {
        return envoyerNotification(idUtilisateur, titre, message, TypeNotification.SUCCES);
    }

    /**
     * Envoie une notification d'erreur à un utilisateur.
     * 
     * @param idUtilisateur ID de l'utilisateur destinataire
     * @param titre         Titre de l'erreur
     * @param message       Message de l'erreur
     * @return La notification créée
     */
    public NotificationDTO envoyerErreur(Integer idUtilisateur, String titre, String message) {
        return envoyerNotification(idUtilisateur, titre, message, TypeNotification.ERREUR);
    }

    /**
     * Retourne une notification par son ID.
     * 
     * @param idNotification ID de la notification
     * @return Optional contenant la notification si trouvée
     */
    public Optional<NotificationDTO> getNotificationById(Integer idNotification) {
        try {
            return notificationRepository.findById(idNotification)
                    .map(NotificationMapper::toDTO);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération d'une notification", e);
            throw new DatabaseException("Erreur lors de la récupération d'une notification: " + e.getMessage(), e);
        }
    }

    /**
     * Récupère toutes les notifications d'un utilisateur.
     * 
     * @param idUtilisateur ID de l'utilisateur
     * @return Liste des notifications
     */
    public List<NotificationDTO> getNotifications(Integer idUtilisateur) {
        try {
            List<Notification> entities = notificationRepository.findByUtilisateur(idUtilisateur);
            return NotificationMapper.toDTOList(entities);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des notifications", e);
            throw new DatabaseException("Erreur lors de la récupération des notifications: " + e.getMessage(), e);
        }
    }

    /**
     * Récupère les notifications non lues d'un utilisateur.
     * 
     * @param idUtilisateur ID de l'utilisateur
     * @return Liste des notifications non lues
     */
    public List<NotificationDTO> getNotificationsNonLues(Integer idUtilisateur) {
        try {
            List<Notification> entities = notificationRepository.findUnreadByUtilisateur(idUtilisateur);
            return NotificationMapper.toDTOList(entities);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des notifications non lues", e);
            throw new DatabaseException("Erreur lors de la récupération des notifications non lues: " + e.getMessage(),
                    e);
        }
    }

    /**
     * Marque une notification comme vue.
     * 
     * @param idNotification ID de la notification
     * @return true si la notification a été marquée comme vue, false sinon
     */
    public boolean marquerCommeVue(Integer idNotification) {
        try {
            boolean result = notificationRepository.markAsRead(idNotification);

            // Met à jour la liste observable si nécessaire
            if (result) {
                Platform.runLater(() -> {
                    notifications.stream()
                            .filter(n -> n.getIdNotification().equals(idNotification))
                            .findFirst()
                            .ifPresent(n -> n.setVue(true));
                });
            }

            return result;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du marquage d'une notification comme lue", e);
            throw new DatabaseException("Erreur lors du marquage d'une notification comme lue: " + e.getMessage(), e);
        }
    }

    /**
     * Marque toutes les notifications d'un utilisateur comme vues.
     * 
     * @param idUtilisateur ID de l'utilisateur
     * @return Nombre de notifications marquées comme vues
     */
    public int marquerToutesCommeVues(Integer idUtilisateur) {
        try {
            int count = notificationRepository.markAllAsRead(idUtilisateur);

            // Met à jour la liste observable si nécessaire
            if (count > 0 && estUtilisateurCourant(idUtilisateur)) {
                Platform.runLater(() -> {
                    notifications.forEach(n -> n.setVue(true));
                });
            }

            return count;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du marquage de toutes les notifications comme lues", e);
            throw new DatabaseException(
                    "Erreur lors du marquage de toutes les notifications comme lues: " + e.getMessage(), e);
        }
    }

    /**
     * Compte le nombre de notifications non lues pour un utilisateur.
     * 
     * @param idUtilisateur ID de l'utilisateur
     * @return Nombre de notifications non lues
     */
    public int getNombreNotificationsNonLues(Integer idUtilisateur) {
        try {
            return notificationRepository.countUnread(idUtilisateur);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des notifications non lues", e);
            throw new DatabaseException("Erreur lors du comptage des notifications non lues: " + e.getMessage(), e);
        }
    }

    /**
     * Charge les notifications de l'utilisateur connecté.
     * Cette méthode est appelée après la connexion.
     */
    public void chargerNotificationsUtilisateurCourant() {
        Integer idUtilisateur = getIdUtilisateurCourant();
        if (idUtilisateur != null) {
            try {
                List<NotificationDTO> userNotifications = getNotifications(idUtilisateur);
                Platform.runLater(() -> {
                    notifications.clear();
                    notifications.addAll(userNotifications);
                });
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erreur lors du chargement des notifications", e);
            }
        }
    }

    /**
     * Retourne la liste observable des notifications pour l'UI.
     * Cette liste est automatiquement mise à jour.
     * 
     * @return Liste observable des notifications
     */
    public ObservableList<NotificationDTO> getObservableNotifications() {
        return FXCollections.unmodifiableObservableList(notifications);
    }

    /**
     * Vérifie si l'ID fourni correspond à l'utilisateur actuellement connecté.
     * 
     * @param idUtilisateur ID à vérifier
     * @return true si l'ID correspond à l'utilisateur courant, false sinon
     */
    private boolean estUtilisateurCourant(Integer idUtilisateur) {
        Integer currentUserId = getIdUtilisateurCourant();
        return currentUserId != null && currentUserId.equals(idUtilisateur);
    }

    /**
     * Récupère l'ID de l'utilisateur actuellement connecté.
     * 
     * @return ID de l'utilisateur ou null si non connecté
     */
    private Integer getIdUtilisateurCourant() {
        if (SessionManager.getInstance().isSessionActive() &&
                SessionManager.getInstance().getUtilisateurConnecte() != null) {
            return SessionManager.getInstance().getUtilisateurConnecte().getIdUtilisateur();
        }
        return null;
    }
}
