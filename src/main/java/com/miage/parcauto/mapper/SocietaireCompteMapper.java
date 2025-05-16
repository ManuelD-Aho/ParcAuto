package main.java.com.miage.parcauto.mapper;

import main.java.com.miage.parcauto.dto.SocietaireCompteDTO;
import main.java.com.miage.parcauto.model.finance.SocietaireCompte;
import java.util.List;

/**
 * Interface pour le mapping entre l'entité SocietaireCompte et son DTO.
 */
public interface SocietaireCompteMapper {

    /**
     * Convertit une entité SocietaireCompte en SocietaireCompteDTO.
     * @param compte L'entité SocietaireCompte.
     * @return Le DTO correspondant, ou null si l'entité est nulle.
     */
    SocietaireCompteDTO toDTO(SocietaireCompte compte);

    /**
     * Convertit un SocietaireCompteDTO en entité SocietaireCompte.
     * @param compteDTO Le DTO SocietaireCompte.
     * @return L'entité correspondante, ou null si le DTO est nul.
     */
    SocietaireCompte toEntity(SocietaireCompteDTO compteDTO);

    /**
     * Convertit une liste d'entités SocietaireCompte en une liste de SocietaireCompteDTO.
     * @param comptes La liste des entités.
     * @return La liste des DTOs, ou une liste vide si l'entrée est nulle.
     */
    List<SocietaireCompteDTO> toDTOList(List<SocietaireCompte> comptes);

    /**
     * Convertit une liste de SocietaireCompteDTO en une liste d'entités SocietaireCompte.
     * @param compteDTOs La liste des DTOs.
     * @return La liste des entités, ou une liste vide si l'entrée est nulle.
     */
    List<SocietaireCompte> toEntityList(List<SocietaireCompteDTO> compteDTOs);
}