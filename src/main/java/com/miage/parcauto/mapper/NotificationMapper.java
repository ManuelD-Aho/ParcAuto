package main.java.com.miage.parcauto.mapper;

import main.java.com.miage.parcauto.dto.NotificationDTO;
import main.java.com.miage.parcauto.model.notification.Notification;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe utilitaire pour mapper les objets Notification vers NotificationDTO et
 * vice versa.
 * Implémente les méthodes de conversion entre les entités et les DTOs.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class NotificationMapper {

    /**
     * Convertit une entité Notification en DTO NotificationDTO.
     *
     * @param notification L'entité à convertir
     * @return Le DTO correspondant
     */
    public static NotificationDTO toDTO(Notification notification) {
        if (notification == null) {
            return null;
        }

        NotificationDTO dto = new NotificationDTO();
        dto.setIdNotification(notification.getIdNotification());
        dto.setIdUtilisateur(notification.getIdUtilisateur());
        dto.setTitre(notification.getTitre());
        dto.setMessage(notification.getMessage());
        dto.setDateCreation(notification.getDateCreation());
        dto.setVue(notification.isVue());
        dto.setActionUrl(notification.getActionUrl());

        // Conversion du type de notification
        switch (notification.getType()) {
            case INFO:
                dto.setType(NotificationDTO.TypeNotification.INFO);
                break;
            case ALERTE:
                dto.setType(NotificationDTO.TypeNotification.ALERTE);
                break;
            case ERREUR:
                dto.setType(NotificationDTO.TypeNotification.ERREUR);
                break;
            case SUCCES:
                dto.setType(NotificationDTO.TypeNotification.SUCCES);
                break;
            default:
                dto.setType(NotificationDTO.TypeNotification.INFO);
        }

        return dto;
    }

    /**
     * Convertit un DTO NotificationDTO en entité Notification.
     *
     * @param dto Le DTO à convertir
     * @return L'entité correspondante
     */
    public static Notification toEntity(NotificationDTO dto) {
        if (dto == null) {
            return null;
        }

        Notification notification = new Notification();
        notification.setIdNotification(dto.getIdNotification());
        notification.setIdUtilisateur(dto.getIdUtilisateur());
        notification.setTitre(dto.getTitre());
        notification.setMessage(dto.getMessage());
        notification.setDateCreation(dto.getDateCreation());
        notification.setVue(dto.isVue());
        notification.setActionUrl(dto.getActionUrl());

        // Conversion du type de notification
        switch (dto.getType()) {
            case INFO:
                notification.setType(Notification.TypeNotification.INFO);
                break;
            case ALERTE:
                notification.setType(Notification.TypeNotification.ALERTE);
                break;
            case ERREUR:
                notification.setType(Notification.TypeNotification.ERREUR);
                break;
            case SUCCES:
                notification.setType(Notification.TypeNotification.SUCCES);
                break;
            default:
                notification.setType(Notification.TypeNotification.INFO);
        }

        return notification;
    }

    /**
     * Convertit une liste d'entités en liste de DTOs.
     *
     * @param notifications Liste d'entités à convertir
     * @return Liste de DTOs
     */
    public static List<NotificationDTO> toDTOList(List<Notification> notifications) {
        if (notifications == null) {
            return null;
        }
        return notifications.stream()
                .map(NotificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste de DTOs en liste d'entités.
     *
     * @param dtos Liste de DTOs à convertir
     * @return Liste d'entités
     */
    public static List<Notification> toEntityList(List<NotificationDTO> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
                .map(NotificationMapper::toEntity)
                .collect(Collectors.toList());
    }
}
