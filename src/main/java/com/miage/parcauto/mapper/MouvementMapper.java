package main.java.com.miage.parcauto.mapper;

import main.java.com.miage.parcauto.dto.MouvementDTO;
import main.java.com.miage.parcauto.model.finance.Mouvement;
import java.util.List;

/**
 * Interface pour le mapping entre l'entité Mouvement et son DTO.
 */
public interface MouvementMapper {

    /**
     * Convertit une entité Mouvement en MouvementDTO.
     * @param mouvement L'entité Mouvement.
     * @return Le DTO correspondant, ou null si l'entité est nulle.
     */
    MouvementDTO toDTO(Mouvement mouvement);

    /**
     * Convertit un MouvementDTO en entité Mouvement.
     * @param mouvementDTO Le DTO Mouvement.
     * @return L'entité correspondante, ou null si le DTO est nul.
     */
    Mouvement toEntity(MouvementDTO mouvementDTO);

    /**
     * Convertit une liste d'entités Mouvement en une liste de MouvementDTO.
     * @param mouvements La liste des entités.
     * @return La liste des DTOs, ou une liste vide si l'entrée est nulle.
     */
    List<MouvementDTO> toDTOList(List<Mouvement> mouvements);

    /**
     * Convertit une liste de MouvementDTO en une liste d'entités Mouvement.
     * @param mouvementDTOs La liste des DTOs.
     * @return La liste des entités, ou une liste vide si l'entrée est nulle.
     */
    List<Mouvement> toEntityList(List<MouvementDTO> mouvementDTOs);
}