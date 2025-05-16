package main.java.com.miage.parcauto.mapper.impl;

import main.java.com.miage.parcauto.dto.MissionDTO;
import main.java.com.miage.parcauto.mapper.MissionMapper;
import main.java.com.miage.parcauto.model.mission.Mission;
import main.java.com.miage.parcauto.model.mission.StatutMission;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MissionMapperImpl implements MissionMapper {

    @Override
    public MissionDTO toDTO(Mission mission) {
        if (mission == null) {
            return null;
        }
        MissionDTO dto = new MissionDTO();
        dto.setIdMission(mission.getIdMission());
        dto.setIdVehicule(mission.getIdVehicule());
        // dto.setIdPersonnel(mission.getIdPersonnel()); // Si vous ajoutez idPersonnel à Mission
        dto.setLibelle(mission.getLibelle());
        dto.setSiteDestination(mission.getSite());
        dto.setDateDebut(mission.getDateDebutMission());
        dto.setDateFinPrevue(mission.getDateFinMission()); // date_fin_mission est la date de fin prévue/effective
        dto.setDateFinEffective(mission.getDateFinEffective()); // Ajouter ce champ au modèle Mission et DTO si besoin de distinguer
        dto.setKmPrevu(mission.getKmPrevu());
        dto.setKmReel(mission.getKmReel());
        dto.setStatut(mission.getStatut() != null ? mission.getStatut().name() : null);
        dto.setCoutEstime(mission.getCoutEstime()); // Ajouter ce champ au modèle Mission et DTO
        dto.setCoutTotalReel(mission.getCoutTotal()); // cout_total est le coût réel
        dto.setCircuit(mission.getCircuitMission());
        dto.setObservations(mission.getObservationMission());
        return dto;
    }

    @Override
    public Mission toEntity(MissionDTO dto) {
        if (dto == null) {
            return null;
        }
        Mission entity = new Mission();
        entity.setIdMission(dto.getIdMission());
        entity.setIdVehicule(dto.getIdVehicule());
        // entity.setIdPersonnel(dto.getIdPersonnel());
        entity.setLibelle(dto.getLibelle());
        entity.setSite(dto.getSiteDestination());
        entity.setDateDebutMission(dto.getDateDebut());
        entity.setDateFinMission(dto.getDateFinPrevue());
        entity.setDateFinEffective(dto.getDateFinEffective());
        entity.setKmPrevu(dto.getKmPrevu());
        entity.setKmReel(dto.getKmReel());
        if (dto.getStatut() != null) {
            try {
                entity.setStatut(StatutMission.valueOf(dto.getStatut()));
            } catch (IllegalArgumentException e) {
                System.err.println("Statut de mission invalide dans DTO: " + dto.getStatut());
            }
        }
        entity.setCoutEstime(dto.getCoutEstime());
        entity.setCoutTotal(dto.getCoutTotalReel());
        entity.setCircuitMission(dto.getCircuit());
        entity.setObservationMission(dto.getObservations());
        return entity;
    }

    @Override
    public List<MissionDTO> toDTOList(List<Mission> missions) {
        if (missions == null) {
            return Collections.emptyList();
        }
        return missions.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Mission> toEntityList(List<MissionDTO> missionDTOs) {
        if (missionDTOs == null) {
            return Collections.emptyList();
        }
        return missionDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}