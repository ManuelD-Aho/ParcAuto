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
        dto.setLibelle(mission.getLibelle());
        dto.setSiteDestination(mission.getSiteDestination());
        dto.setDateDebut(mission.getDateDebut());
        dto.setDateFinPrevue(mission.getDateFin());
        dto.setStatut(mission.getStatut() != null ? mission.getStatut().name() : null);
        dto.setCoutTotalReel(mission.getCoutTotal());
        dto.setCircuit(mission.getCircuit());
        dto.setObservations(mission.getObservations());
        // Les champs non présents côté modèle sont ignorés
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
        entity.setLibelle(dto.getLibelle());
        entity.setSiteDestination(dto.getSiteDestination());
        entity.setDateDebut(dto.getDateDebut());
        entity.setDateFin(dto.getDateFinPrevue());
        entity.setStatut(dto.getStatut() != null ? StatutMission.valueOf(dto.getStatut()) : null);
        entity.setCoutTotal(dto.getCoutTotalReel());
        entity.setCircuit(dto.getCircuit());
        entity.setObservations(dto.getObservations());
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