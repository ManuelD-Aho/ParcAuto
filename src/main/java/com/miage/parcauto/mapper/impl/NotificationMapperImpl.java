package main.java.com.miage.parcauto.mapper.impl;

import main.java.com.miage.parcauto.dto.NotificationDTO;
import main.java.com.miage.parcauto.mapper.NotificationMapper;
import main.java.com.miage.parcauto.model.Notification; // Assurez-vous du bon chemin

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation de {@link NotificationMapper}.
 */
public class NotificationMapperImpl implements NotificationMapper {

    /**
     * {@inheritDoc}
     */
    @Override
    public NotificationDTO toDTO(Notification notification) {
        if (notification == null) {
            return null;
        }
        NotificationDTO dto = new NotificationDTO();
        dto.setIdNotification(notification.getIdNotification());
        dto.setIdUtilisateur(notification.getIdUtilisateur());
        dto.setTitre(notification.getTitre());
        dto.setMessage(notification.getMessage());
        dto.setDateCreation(notification.getDateCreation());
        dto.setEstLue(notification.isEstLue());
        dto.setTypeNotification(notification.getTypeNotification()); // Assurez-vous que ce champ existe dans DTO et Entité
        dto.setIdEntiteLiee(notification.getIdEntiteLiee()); // Assurez-vous que ce champ existe
        dto.setTypeEntiteLiee(notification.getTypeEntiteLiee()); // Assurez-vous que ce champ existe
        return dto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Notification toEntity(NotificationDTO dto) {
        if (dto == null) {
            return null;
        }
        Notification entity = new Notification();
        entity.setIdNotification(dto.getIdNotification());
        entity.setIdUtilisateur(dto.getIdUtilisateur());
        entity.setTitre(dto.getTitre());
        entity.setMessage(dto.getMessage());
        entity.setDateCreation(dto.getDateCreation());
        entity.setEstLue(dto.isEstLue());
        entity.setTypeNotification(dto.getTypeNotification());
        entity.setIdEntiteLiee(dto.getIdEntiteLiee());
        entity.setTypeEntiteLiee(dto.getTypeEntiteLiee());
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NotificationDTO> toDTOList(List<Notification> notifications) {
        if (notifications == null) {
            return Collections.emptyList();
        }
        return notifications.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Notification> toEntityList(List<NotificationDTO> notificationDTOs) {
        if (notificationDTOs == null) {
            return Collections.emptyList();
        }
        return notificationDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}