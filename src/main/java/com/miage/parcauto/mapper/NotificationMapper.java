package main.java.com.miage.parcauto.mapper;

import main.java.com.miage.parcauto.dto.NotificationDTO;
import main.java.com.miage.parcauto.model.Notification; // Assurez-vous du bon chemin
import java.util.List;

/**
 * Interface pour le mapping entre l'entité Notification et son DTO.
 */
public interface NotificationMapper {

    /**
     * Convertit une entité Notification en NotificationDTO.
     * @param notification L'entité Notification.
     * @return Le DTO correspondant.
     */
    NotificationDTO toDTO(Notification notification);

    /**
     * Convertit un NotificationDTO en entité Notification.
     * @param notificationDTO Le DTO Notification.
     * @return L'entité correspondante.
     */
    Notification toEntity(NotificationDTO notificationDTO);

    /**
     * Convertit une liste d'entités Notification en une liste de NotificationDTO.
     * @param notifications La liste des entités.
     * @return La liste des DTOs.
     */
    List<NotificationDTO> toDTOList(List<Notification> notifications);

    /**
     * Convertit une liste de NotificationDTO en une liste d'entités Notification.
     * @param notificationDTOs La liste des DTOs.
     * @return La liste des entités.
     */
    List<Notification> toEntityList(List<NotificationDTO> notificationDTOs);
}