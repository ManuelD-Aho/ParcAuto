package main.java.com.miage.parcauto.service.impl;

import main.java.com.miage.parcauto.dao.DbUtil;
import main.java.com.miage.parcauto.dao.NotificationRepository;
import main.java.com.miage.parcauto.dao.UtilisateurRepository;
import main.java.com.miage.parcauto.dao.impl.NotificationRepositoryImpl;
import main.java.com.miage.parcauto.dao.impl.UtilisateurRepositoryImpl;
import main.java.com.miage.parcauto.dto.NotificationDTO;
import main.java.com.miage.parcauto.exception.*;
import main.java.com.miage.parcauto.mapper.NotificationMapper;
import main.java.com.miage.parcauto.mapper.impl.NotificationMapperImpl;
import main.java.com.miage.parcauto.model.notification.Notification;
import main.java.com.miage.parcauto.service.NotificationService;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Implémentation du service de gestion des notifications.
 */
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final NotificationMapper notificationMapper;

    /**
     * Constructeur par défaut.
     */
    public NotificationServiceImpl() {
        this.notificationRepository = new NotificationRepositoryImpl();
        this.utilisateurRepository = new UtilisateurRepositoryImpl();
        this.notificationMapper = new NotificationMapperImpl();
    }

    /**
     * Constructeur avec injection de dépendances.
     * @param notificationRepository Le repository pour les notifications.
     * @param utilisateurRepository Le repository pour les utilisateurs.
     * @param notificationMapper Le mapper pour les notifications.
     */
    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   UtilisateurRepository utilisateurRepository,
                                   NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.notificationMapper = notificationMapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createNotification(NotificationDTO notificationDTO) throws ValidationException, UtilisateurNotFoundException, OperationFailedException {
        if (notificationDTO == null || notificationDTO.getIdUtilisateur() == null ||
                notificationDTO.getTitre() == null || notificationDTO.getTitre().trim().isEmpty() ||
                notificationDTO.getMessage() == null || notificationDTO.getMessage().trim().isEmpty()) {
            throw new ValidationException("ID Utilisateur, titre et message sont requis pour une notification.");
        }

        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            if (utilisateurRepository.findById(conn, notificationDTO.getIdUtilisateur()).isEmpty()) {
                throw new UtilisateurNotFoundException("Utilisateur destinataire non trouvé avec l'ID: " + notificationDTO.getIdUtilisateur());
            }

            Notification notification = notificationMapper.toEntity(notificationDTO);
            if (notification.getDateCreation() == null) {
                notification.setDateCreation(LocalDateTime.now());
            }
            notification.setEstLue(false); // Nouvelle notification est non lue par défaut

            notificationRepository.save(conn, notification);
            conn.commit();
        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors de la création de la notification.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NotificationDTO> getNotificationsNonLues(Integer idUtilisateur) throws UtilisateurNotFoundException, OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            if (utilisateurRepository.findById(conn, idUtilisateur).isEmpty()) {
                throw new UtilisateurNotFoundException("Utilisateur non trouvé avec l'ID: " + idUtilisateur);
            }
            List<Notification> notifications = notificationRepository.findNonLuesByUtilisateurId(conn, idUtilisateur);
            return notificationMapper.toDTOList(notifications);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération des notifications non lues.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NotificationDTO> getAllNotificationsForUtilisateur(Integer idUtilisateur) throws UtilisateurNotFoundException, OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            if (utilisateurRepository.findById(conn, idUtilisateur).isEmpty()) {
                throw new UtilisateurNotFoundException("Utilisateur non trouvé avec l'ID: " + idUtilisateur);
            }
            List<Notification> notifications = notificationRepository.findAllByUtilisateurId(conn, idUtilisateur);
            return notificationMapper.toDTOList(notifications);
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération de toutes les notifications pour l'utilisateur.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void marquerNotificationCommeLue(Integer idNotification) throws EntityNotFoundException, OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            Notification notification = notificationRepository.findById(conn, idNotification)
                    .orElseThrow(() -> new EntityNotFoundException("Notification non trouvée avec l'ID: " + idNotification));

            if (!notification.isEstLue()) {
                notification.setEstLue(true);
                notificationRepository.update(conn, notification);
            }
            conn.commit();
        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors du marquage de la notification comme lue.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void marquerNotificationsCommeLues(List<Integer> idsNotification, Integer idUtilisateur) throws OperationFailedException {
        if (idsNotification == null || idsNotification.isEmpty()) {
            return; // Rien à faire
        }
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);
            // Valider que l'utilisateur existe
            if (utilisateurRepository.findById(conn, idUtilisateur).isEmpty()) {
                throw new UtilisateurNotFoundException("Utilisateur non trouvé avec l'ID: " + idUtilisateur);
            }

            for (Integer idNotification : idsNotification) {
                Optional<Notification> notificationOpt = notificationRepository.findByIdAndUtilisateurId(conn, idNotification, idUtilisateur);
                if (notificationOpt.isPresent()) {
                    Notification notification = notificationOpt.get();
                    if (!notification.isEstLue()) {
                        notification.setEstLue(true);
                        notificationRepository.update(conn, notification);
                    }
                } else {
                    // Logguer ou ignorer une notification non trouvée ou n'appartenant pas à l'utilisateur
                    System.err.println("Tentative de marquer comme lue une notification non trouvée ou non autorisée. ID: " + idNotification + " pour utilisateur ID: " + idUtilisateur);
                }
            }
            conn.commit();
        } catch (SQLException | UtilisateurNotFoundException e) { // UtilisateurNotFoundException peut être levée avant le rollback
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors du marquage de plusieurs notifications comme lues.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNotification(Integer idNotification) throws EntityNotFoundException, OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            conn.setAutoCommit(false);

            if (notificationRepository.findById(conn, idNotification).isEmpty()) {
                throw new EntityNotFoundException("Notification non trouvée avec l'ID: " + idNotification);
            }

            boolean deleted = notificationRepository.delete(conn, idNotification);
            if (!deleted) {
                throw new OperationFailedException("La suppression de la notification a échoué.");
            }
            conn.commit();
        } catch (SQLException e) {
            DbUtil.rollback(conn);
            throw new OperationFailedException("Erreur technique lors de la suppression de la notification.", e);
        } finally {
            DbUtil.close(conn);
        }
    }
}