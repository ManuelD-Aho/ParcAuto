package main.java.com.miage.parcauto.mapper;

import main.java.com.miage.parcauto.dto.SocieteCompteDTO;
import main.java.com.miage.parcauto.model.finance.SocieteCompte;
import main.java.com.miage.parcauto.dao.PersonnelRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

/**
 * Classe utilitaire pour la conversion entre les entités SocieteCompte et les
 * DTO SocieteCompteDTO.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class SocieteCompteMapper {

    private final PersonnelRepository personnelRepository;

    /**
     * Constructeur avec injection de dépendances.
     *
     * @param personnelRepository Le repository pour accéder aux données du
     *                            personnel
     */
    public SocieteCompteMapper(PersonnelRepository personnelRepository) {
        this.personnelRepository = personnelRepository;
    }

    /**
     * Convertit une entité SocieteCompte en DTO.
     *
     * @param compte L'entité à convertir
     * @return Le DTO correspondant
     */
    public SocieteCompteDTO toDTO(SocieteCompte compte) {
        if (compte == null) {
            return null;
        }

        SocieteCompteDTO dto = new SocieteCompteDTO();
        dto.setIdCompte(compte.getIdCompte());
        dto.setNumeroCompte(compte.getNumeroCompte());
        dto.setLibelleCompte(compte.getLibelleCompte());
        dto.setSoldeCompte(compte.getSoldeCompte());
        dto.setTypeCompte(compte.getTypeCompte());
        dto.setDateCreation(compte.getDateCreation());
        dto.setIdPersonnel(compte.getIdPersonnel());
        dto.setActif(compte.isActif());

        // Récupérer le nom du personnel si nécessaire
        if (compte.getIdPersonnel() != null) {
            personnelRepository.findById(compte.getIdPersonnel())
                    .ifPresent(personnel -> dto.setNomPersonnel(personnel.getNom() + " " + personnel.getPrenom()));
        }

        return dto;
    }

    /**
     * Convertit un DTO SocieteCompteDTO en entité.
     *
     * @param dto Le DTO à convertir
     * @return L'entité correspondante
     */
    public SocieteCompte toEntity(SocieteCompteDTO dto) {
        if (dto == null) {
            return null;
        }

        SocieteCompte compte = new SocieteCompte();
        compte.setIdCompte(dto.getIdCompte());
        compte.setNumeroCompte(dto.getNumeroCompte());
        compte.setLibelleCompte(dto.getLibelleCompte());
        compte.setSoldeCompte(dto.getSoldeCompte());
        compte.setTypeCompte(dto.getTypeCompte());
        compte.setDateCreation(dto.getDateCreation());
        compte.setIdPersonnel(dto.getIdPersonnel());
        compte.setActif(dto.isActif());

        return compte;
    }

    /**
     * Convertit une liste d'entités SocieteCompte en liste de DTOs.
     *
     * @param comptes La liste d'entités à convertir
     * @return La liste de DTOs correspondante
     */
    public List<SocieteCompteDTO> toDTOList(List<SocieteCompte> comptes) {
        return comptes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste de DTOs SocieteCompteDTO en liste d'entités.
     *
     * @param dtos La liste de DTOs à convertir
     * @return La liste d'entités correspondante
     */
    public List<SocieteCompte> toEntityList(List<SocieteCompteDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour une entité SocieteCompte à partir d'un DTO.
     *
     * @param compte L'entité à mettre à jour
     * @param dto    Le DTO contenant les nouvelles valeurs
     * @return L'entité mise à jour
     */
    public SocieteCompte updateEntityFromDTO(SocieteCompte compte, SocieteCompteDTO dto) {
        if (compte == null || dto == null) {
            return compte;
        }

        compte.setNumeroCompte(dto.getNumeroCompte());
        compte.setLibelleCompte(dto.getLibelleCompte());
        compte.setSoldeCompte(dto.getSoldeCompte());
        compte.setTypeCompte(dto.getTypeCompte());
        compte.setDateCreation(dto.getDateCreation());
        compte.setIdPersonnel(dto.getIdPersonnel());
        compte.setActif(dto.isActif());

        return compte;
    }
}
