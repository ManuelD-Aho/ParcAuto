package main.java.com.miage.parcauto.mapper;

import main.java.com.miage.parcauto.dto.DocumentSocietaireDTO;
import main.java.com.miage.parcauto.model.document.DocumentSocietaire;
import java.util.List;

/**
 * Interface pour le mapping entre l'entité DocumentSocietaire et son DTO.
 */
public interface DocumentSocietaireMapper {

    /**
     * Convertit une entité DocumentSocietaire en DocumentSocietaireDTO.
     * @param document L'entité DocumentSocietaire.
     * @return Le DTO correspondant, ou null si l'entité est nulle.
     */
    DocumentSocietaireDTO toDTO(DocumentSocietaire document);

    /**
     * Convertit un DocumentSocietaireDTO en entité DocumentSocietaire.
     * @param documentDTO Le DTO DocumentSocietaire.
     * @return L'entité correspondante, ou null si le DTO est nul.
     */
    DocumentSocietaire toEntity(DocumentSocietaireDTO documentDTO);

    /**
     * Convertit une liste d'entités DocumentSocietaire en une liste de DocumentSocietaireDTO.
     * @param documents La liste des entités.
     * @return La liste des DTOs, ou une liste vide si l'entrée est nulle.
     */
    List<DocumentSocietaireDTO> toDTOList(List<DocumentSocietaire> documents);

    /**
     * Convertit une liste de DocumentSocietaireDTO en une liste d'entités DocumentSocietaire.
     * @param documentDTOs La liste des DTOs.
     * @return La liste des entités, ou une liste vide si l'entrée est nulle.
     */
    List<DocumentSocietaire> toEntityList(List<DocumentSocietaireDTO> documentDTOs);
}