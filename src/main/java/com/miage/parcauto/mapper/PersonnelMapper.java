package main.java.com.miage.parcauto.mapper;

import main.java.com.miage.parcauto.dto.PersonnelDTO;
import main.java.com.miage.parcauto.model.utilisateur.Personnel;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe utilitaire pour la conversion entre les entités Personnel et les DTO
 * PersonnelDTO.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class PersonnelMapper {

    /**
     * Convertit une entité Personnel en DTO.
     *
     * @param personnel L'entité à convertir
     * @return Le DTO correspondant
     */
    public PersonnelDTO toDTO(Personnel personnel) {
        if (personnel == null) {
            return null;
        }

        PersonnelDTO dto = new PersonnelDTO();
        dto.setIdPersonnel(personnel.getIdPersonnel());
        dto.setNom(personnel.getNom());
        dto.setPrenom(personnel.getPrenom());
        dto.setEmail(personnel.getEmail());
        dto.setTelephone(personnel.getTelephone());
        dto.setNomUtilisateur(personnel.getNomUtilisateur());
        dto.setRole(personnel.getRole());
        dto.setActif(personnel.isActif());

        return dto;
    }

    /**
     * Convertit un DTO PersonnelDTO en entité.
     * Note: Le mot de passe et le sel ne sont pas inclus, ils doivent être gérés
     * séparément.
     *
     * @param dto Le DTO à convertir
     * @return L'entité correspondante
     */
    public Personnel toEntity(PersonnelDTO dto) {
        if (dto == null) {
            return null;
        }

        Personnel personnel = new Personnel();
        personnel.setIdPersonnel(dto.getIdPersonnel());
        personnel.setNom(dto.getNom());
        personnel.setPrenom(dto.getPrenom());
        personnel.setEmail(dto.getEmail());
        personnel.setTelephone(dto.getTelephone());
        personnel.setNomUtilisateur(dto.getNomUtilisateur());
        personnel.setRole(dto.getRole());
        personnel.setActif(dto.isActif());

        return personnel;
    }

    /**
     * Convertit une liste d'entités Personnel en liste de DTOs.
     *
     * @param personnels La liste d'entités à convertir
     * @return La liste de DTOs correspondante
     */
    public List<PersonnelDTO> toDTOList(List<Personnel> personnels) {
        return personnels.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste de DTOs PersonnelDTO en liste d'entités.
     *
     * @param dtos La liste de DTOs à convertir
     * @return La liste d'entités correspondante
     */
    public List<Personnel> toEntityList(List<PersonnelDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour une entité Personnel à partir d'un DTO.
     * Note: Le mot de passe et le sel ne sont pas mis à jour, ils doivent être
     * gérés séparément.
     *
     * @param personnel L'entité à mettre à jour
     * @param dto       Le DTO contenant les nouvelles valeurs
     * @return L'entité mise à jour
     */
    public Personnel updateEntityFromDTO(Personnel personnel, PersonnelDTO dto) {
        if (personnel == null || dto == null) {
            return personnel;
        }

        personnel.setNom(dto.getNom());
        personnel.setPrenom(dto.getPrenom());
        personnel.setEmail(dto.getEmail());
        personnel.setTelephone(dto.getTelephone());
        personnel.setNomUtilisateur(dto.getNomUtilisateur());
        personnel.setRole(dto.getRole());
        personnel.setActif(dto.isActif());

        return personnel;
    }
}
