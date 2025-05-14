package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.notification.Notification;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface Repository pour les notifications.
 * Définit les méthodes spécifiques aux notifications en plus des méthodes CRUD
 * de base.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public interface NotificationRepository extends Repository<Notification, Integer> {

    /**
     * Recherche les notifications d'un utilisateur.
     * 
     * @param idUtilisateur Identifiant de l'utilisateur
     * @return Liste des notifications de l'utilisateur
     */
    List<Notification> findByUtilisateur(Integer idUtilisateur);

    /**
     * Recherche les notifications non lues d'un utilisateur.
     * 
     * @param idUtilisateur Identifiant de l'utilisateur
     * @return Liste des notifications non lues
     */
    List<Notification> findUnreadByUtilisateur(Integer idUtilisateur);

    /**
     * Recherche les notifications créées entre deux dates.
     * 
     * @param debut Date de début
     * @param fin   Date de fin
     * @return Liste des notifications dans l'intervalle
     */
    List<Notification> findByDateCreationBetween(LocalDateTime debut, LocalDateTime fin);

    /**
     * Marque une notification comme vue.
     * 
     * @param idNotification Identifiant de la notification
     * @return true si la notification a été marquée comme vue, false sinon
     */
    boolean markAsRead(Integer idNotification);

    /**
     * Marque toutes les notifications d'un utilisateur comme vues.
     * 
     * @param idUtilisateur Identifiant de l'utilisateur
     * @return Nombre de notifications marquées comme vues
     */
    int markAllAsRead(Integer idUtilisateur);

    /**
     * Compte le nombre de notifications non lues pour un utilisateur.
     * 
     * @param idUtilisateur Identifiant de l'utilisateur
     * @return Nombre de notifications non lues
     */
    int countUnread(Integer idUtilisateur);
}
