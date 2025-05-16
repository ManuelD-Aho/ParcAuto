package main.java.com.miage.parcauto.mapper;

import main.java.com.miage.parcauto.dto.AffectationDTO;
import main.java.com.miage.parcauto.model.affectation.Affectation; // Assurez-vous du bon chemin
import java.util.List;

/**
 * Interface pour le mapping entre l'entité Affectation et son DTO.
 */
public interface AffectationMapper {

    /**
     * Convertit une entité Affectation en AffectationDTO.
     * @param affectation L'entité Affectation.
     * @return Le DTO correspondant.
     */
    AffectationDTO toDTO(Affectation affectation);

    /**
     * Convertit un AffectationDTO en entité Affectation.
     * @param affectationDTO Le DTO Affectation.
     * @return L'entité correspondante.
     */
    Affectation toEntity(AffectationDTO affectationDTO);

    /**
     * Convertit une liste d'entités Affectation en une liste d'AffectationDTO.
     * @param affectations La liste des entités.
     * @return La liste des DTOs.
     */
    List<AffectationDTO> toDTOList(List<Affectation> affectations);

    /**
     * Convertit une liste d'AffectationDTO en une liste d'entités Affectation.
     * @param affectationDTOs La liste des DTOs.
     * @return La liste des entités.
     */
    List<Affectation> toEntityList(List<AffectationDTO> affectationDTOs);
}