package main.java.com.miage.parcauto.mapper;

import main.java.com.miage.parcauto.dto.MouvementDTO;
import main.java.com.miage.parcauto.model.finance.Mouvement;
import main.java.com.miage.parcauto.dao.SocieteCompteRepository;
import main.java.com.miage.parcauto.dao.MissionRepository;
import main.java.com.miage.parcauto.dao.EntretienRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe utilitaire pour la conversion entre les entités Mouvement et les DTO
 * MouvementDTO.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class MouvementMapper {

    private final SocieteCompteRepository compteRepository;
    private final MissionRepository missionRepository;
    private final EntretienRepository entretienRepository;

    /**
     * Constructeur avec injection de dépendances.
     *
     * @param compteRepository    Le repository pour accéder aux données des comptes
     * @param missionRepository   Le repository pour accéder aux données des
     *                            missions
     * @param entretienRepository Le repository pour accéder aux données des
     *                            entretiens
     */
    public MouvementMapper(SocieteCompteRepository compteRepository,
            MissionRepository missionRepository,
            EntretienRepository entretienRepository) {
        this.compteRepository = compteRepository;
        this.missionRepository = missionRepository;
        this.entretienRepository = entretienRepository;
    }

    /**
     * Convertit une entité Mouvement en DTO.
     *
     * @param mouvement L'entité à convertir
     * @return Le DTO correspondant
     */
    public MouvementDTO toDTO(Mouvement mouvement) {
        if (mouvement == null) {
            return null;
        }

        MouvementDTO dto = new MouvementDTO();
        dto.setIdMouvement(mouvement.getIdMouvement());
        dto.setIdCompte(mouvement.getIdCompte());
        dto.setDateMouvement(mouvement.getDateMouvement());
        dto.setMontant(mouvement.getMontant());
        dto.setLibelle(mouvement.getLibelle());
        dto.setTypeMouvement(mouvement.getTypeMouvement());
        dto.setIdMission(mouvement.getIdMission());
        dto.setIdEntretien(mouvement.getIdEntretien());
        dto.setCategorie(mouvement.getCategorie());

        // Récupérer les informations supplémentaires du compte
        compteRepository.findById(mouvement.getIdCompte()).ifPresent(compte -> {
            dto.setNumeroCompte(compte.getNumeroCompte());
            dto.setLibelleCompte(compte.getLibelleCompte());
        });

        // Récupérer les informations supplémentaires de la mission si applicable
        if (mouvement.getIdMission() != null) {
            missionRepository.findById(mouvement.getIdMission()).ifPresent(mission -> {
                dto.setReferenceMission(mission.getReferenceMission());
            });
        }

        // Récupérer les informations supplémentaires de l'entretien si applicable
        if (mouvement.getIdEntretien() != null) {
            entretienRepository.findById(mouvement.getIdEntretien()).ifPresent(entretien -> {
                dto.setReferenceEntretien("ENT-" + entretien.getIdEntretien());
            });
        }

        return dto;
    }

    /**
     * Convertit un DTO MouvementDTO en entité.
     *
     * @param dto Le DTO à convertir
     * @return L'entité correspondante
     */
    public Mouvement toEntity(MouvementDTO dto) {
        if (dto == null) {
            return null;
        }

        Mouvement mouvement = new Mouvement();
        mouvement.setIdMouvement(dto.getIdMouvement());
        mouvement.setIdCompte(dto.getIdCompte());
        mouvement.setDateMouvement(dto.getDateMouvement());
        mouvement.setMontant(dto.getMontant());
        mouvement.setLibelle(dto.getLibelle());
        mouvement.setTypeMouvement(dto.getTypeMouvement());
        mouvement.setIdMission(dto.getIdMission());
        mouvement.setIdEntretien(dto.getIdEntretien());
        mouvement.setCategorie(dto.getCategorie());

        return mouvement;
    }

    /**
     * Convertit une liste d'entités Mouvement en liste de DTOs.
     *
     * @param mouvements La liste d'entités à convertir
     * @return La liste de DTOs correspondante
     */
    public List<MouvementDTO> toDTOList(List<Mouvement> mouvements) {
        return mouvements.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste de DTOs MouvementDTO en liste d'entités.
     *
     * @param dtos La liste de DTOs à convertir
     * @return La liste d'entités correspondante
     */
    public List<Mouvement> toEntityList(List<MouvementDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour une entité Mouvement à partir d'un DTO.
     *
     * @param mouvement L'entité à mettre à jour
     * @param dto       Le DTO contenant les nouvelles valeurs
     * @return L'entité mise à jour
     */
    public Mouvement updateEntityFromDTO(Mouvement mouvement, MouvementDTO dto) {
        if (mouvement == null || dto == null) {
            return mouvement;
        }

        mouvement.setIdCompte(dto.getIdCompte());
        mouvement.setDateMouvement(dto.getDateMouvement());
        mouvement.setMontant(dto.getMontant());
        mouvement.setLibelle(dto.getLibelle());
        mouvement.setTypeMouvement(dto.getTypeMouvement());
        mouvement.setIdMission(dto.getIdMission());
        mouvement.setIdEntretien(dto.getIdEntretien());
        mouvement.setCategorie(dto.getCategorie());

        return mouvement;
    }
}
