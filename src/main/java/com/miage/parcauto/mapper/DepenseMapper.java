package main.java.com.miage.parcauto.mapper;

import main.java.com.miage.parcauto.dto.DepenseDTO;
import main.java.com.miage.parcauto.model.mission.DepenseMission;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe utilitaire pour convertir entre les entités DepenseMission et les
 * objets DepenseDTO.
 * Implémente le pattern Mapper pour la séparation des couches.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class DepenseMapper {

    /**
     * Convertit une entité DepenseMission en DTO.
     *
     * @param depense Entité à convertir
     * @return DTO correspondant à l'entité
     */
    public DepenseDTO toDTO(DepenseMission depense) {
        if (depense == null) {
            return null;
        }

        // Conversion de la nature de dépense en catégorie
        String categorie = depense.getNature() != null ? depense.getNature().getLibelle()
                : DepenseMission.NatureDepense.Carburant.getLibelle();

        return new DepenseDTO(
                depense.getIdDepense(),
                depense.getIdMission(),
                categorie,
                depense.getDescription(),
                depense.getMontant(),
                depense.getDate() != null ? depense.getDate() : LocalDate.now(),
                depense.getJustificatif());
    }

    /**
     * Convertit un DTO en entité DepenseMission.
     *
     * @param dto DTO à convertir
     * @return Entité correspondante au DTO
     */
    public DepenseMission toEntity(DepenseDTO dto) {
        if (dto == null) {
            return null;
        }

        DepenseMission depense = new DepenseMission();
        depense.setIdDepense(dto.getIdDepense());
        depense.setIdMission(dto.getIdMission());

        // Conversion de la catégorie en nature de dépense
        if (dto.getCategorie() != null) {
            if (dto.getCategorie().toLowerCase().contains("carburant")) {
                depense.setNature(DepenseMission.NatureDepense.Carburant);
            } else {
                depense.setNature(DepenseMission.NatureDepense.FraisAnnexes);
            }
        } else {
            depense.setNature(DepenseMission.NatureDepense.Carburant);
        }

        depense.setDescription(dto.getDescription());
        depense.setMontant(dto.getMontant());
        depense.setDate(dto.getDate());
        depense.setJustificatif(dto.getJustificatif());

        return depense;
    }

    /**
     * Convertit une liste d'entités en liste de DTOs.
     *
     * @param depenses Liste d'entités à convertir
     * @return Liste de DTOs
     */
    public List<DepenseDTO> toDTOList(List<DepenseMission> depenses) {
        if (depenses == null) {
            return List.of();
        }
        return depenses.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste de DTOs en liste d'entités.
     *
     * @param dtos Liste de DTOs à convertir
     * @return Liste d'entités
     */
    public List<DepenseMission> toEntityList(List<DepenseDTO> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour une entité existante avec les données d'un DTO.
     * Cette méthode est utile pour les mises à jour partielles.
     *
     * @param depense Entité à mettre à jour
     * @param dto     DTO contenant les nouvelles valeurs
     * @return Entité mise à jour
     */
    public DepenseMission updateEntityFromDTO(DepenseMission depense, DepenseDTO dto) {
        if (depense == null || dto == null) {
            return depense;
        }

        // Ne pas mettre à jour l'ID
        if (dto.getIdMission() != null) {
            depense.setIdMission(dto.getIdMission());
        }

        // Conversion de la catégorie en nature de dépense
        if (dto.getCategorie() != null) {
            if (dto.getCategorie().toLowerCase().contains("carburant")) {
                depense.setNature(DepenseMission.NatureDepense.Carburant);
            } else {
                depense.setNature(DepenseMission.NatureDepense.FraisAnnexes);
            }
        }

        if (dto.getDescription() != null) {
            depense.setDescription(dto.getDescription());
        }

        if (dto.getMontant() != null) {
            depense.setMontant(dto.getMontant());
        }

        if (dto.getDate() != null) {
            depense.setDate(dto.getDate());
        }

        if (dto.getJustificatif() != null) {
            depense.setJustificatif(dto.getJustificatif());
        }

        return depense;
    }
}
