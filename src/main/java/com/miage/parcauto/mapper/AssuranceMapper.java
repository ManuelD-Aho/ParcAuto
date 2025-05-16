package main.java.com.miage.parcauto.mapper;

import main.java.com.miage.parcauto.dto.AssuranceDTO;
import main.java.com.miage.parcauto.model.assurance.Assurance; // Assurez-vous du bon chemin
import java.util.List;

/**
 * Interface pour le mapping entre l'entité Assurance et son DTO.
 */
public interface AssuranceMapper {

    /**
     * Convertit une entité Assurance en AssuranceDTO.
     * @param assurance L'entité Assurance.
     * @return Le DTO correspondant.
     */
    AssuranceDTO toDTO(Assurance assurance);

    /**
     * Convertit un AssuranceDTO en entité Assurance.
     * @param assuranceDTO Le DTO Assurance.
     * @return L'entité correspondante.
     */
    Assurance toEntity(AssuranceDTO assuranceDTO);

    /**
     * Convertit une liste d'entités Assurance en une liste d'AssuranceDTO.
     * @param assurances La liste des entités.
     * @return La liste des DTOs.
     */
    List<AssuranceDTO> toDTOList(List<Assurance> assurances);

    /**
     * Convertit une liste d'AssuranceDTO en une liste d'entités Assurance.
     * @param assuranceDTOs La liste des DTOs.
     * @return La liste des entités.
     */
    List<Assurance> toEntityList(List<AssuranceDTO> assuranceDTOs);
}