package main.java.com.miage.parcauto.service;

import main.java.com.miage.parcauto.dto.NotificationDTO;
import main.java.com.miage.parcauto.exception.EntityNotFoundException;
import main.java.com.miage.parcauto.exception.OperationFailedException;
import main.java.com.miage.parcauto.exception.ValidationException;
import main.java.com.miage.parcauto.exception.UtilisateurNotFoundException;

import java.util.List;

/**
 * Service pour la gestion des notifications.
 */
public interface NotificationService {

    /**
     * Envoie/crée une nouvelle notification.
     *
     * @param notificationDTO Le DTO de la notification à envoyer/créer.
     * @throws ValidationException          Si les données de la notification sont
     *                                      invalides.
     * @throws UtilisateurNotFoundException Si l'utilisateur destinataire n'existe
     *                                      pas.
     * @throws OperationFailedException     Si une erreur technique survient.
     */
    void createNotification(NotificationDTO notificationDTO)
            throws ValidationException, UtilisateurNotFoundException, OperationFailedException;

    /**
     * Récupère toutes les notifications non lues pour un utilisateur spécifique.
     *
     * @param idUtilisateur L'identifiant de l'utilisateur.
     * @return Une liste de NotificationDTO non lues.
     * @throws UtilisateurNotFoundException Si l'utilisateur n'est pas trouvé.
     * @throws OperationFailedException     Si une erreur technique survient.
     */
    List<NotificationDTO> getNotificationsNonLues(Integer idUtilisateur)
            throws UtilisateurNotFoundException, OperationFailedException;

    /**
     * Récupère toutes les notifications (lues et non lues) pour un utilisateur
     * spécifique.
     *
     * @param idUtilisateur L'identifiant de l'utilisateur.
     * @return Une liste de toutes les NotificationDTO pour cet utilisateur.
     * @throws UtilisateurNotFoundException Si l'utilisateur n'est pas trouvé.
     * @throws OperationFailedException     Si une erreur technique survient.
     */
    List<NotificationDTO> getAllNotificationsForUtilisateur(Integer idUtilisateur)
            throws UtilisateurNotFoundException, OperationFailedException;

    /**
     * Marque une notification spécifique comme lue.
     *
     * @param idNotification L'identifiant de la notification à marquer comme lue.
     * @throws EntityNotFoundException  Si la notification n'est pas trouvée.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    void marquerNotificationCommeLue(Integer idNotification) throws EntityNotFoundException, OperationFailedException;

    /**
     * Marque plusieurs notifications comme lues pour un utilisateur.
     *
     * @param idsNotification Liste des identifiants de notifications à marquer
     *                        comme lues.
     * @param idUtilisateur   L'identifiant de l'utilisateur propriétaire des
     *                        notifications.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    void marquerNotificationsCommeLues(List<Integer> idsNotification, Integer idUtilisateur)
            throws OperationFailedException;

    /**
     * Supprime une notification.
     *
     * @param idNotification L'identifiant de la notification à supprimer.
     * @throws EntityNotFoundException  Si la notification n'est pas trouvée.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    void deleteNotification(Integer idNotification) throws EntityNotFoundException, OperationFailedException;
}