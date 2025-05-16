package main.java.com.miage.parcauto.mapper.impl;

import main.java.com.miage.parcauto.dto.DepenseMissionDTO;
import main.java.com.miage.parcauto.mapper.DepenseMissionMapper;
import main.java.com.miage.parcauto.model.mission.DepenseMission;
import main.java.com.miage.parcauto.model.mission.NatureDepenseMission;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DepenseMissionMapperImpl implements DepenseMissionMapper {
    @Override
    public DepenseMissionDTO toDTO(DepenseMission depenseMission) {
        if (depenseMission == null)
            return null;
        DepenseMissionDTO dto = new DepenseMissionDTO();
        dto.setIdDepense(depenseMission.getId());
        dto.setIdMission(depenseMission.getIdMission());
        dto.setNatureDepense(depenseMission.getNature() != null ? depenseMission.getNature().name() : null);
        dto.setMontant(depenseMission.getMontant());
        dto.setJustificatifPath(depenseMission.getJustificatif());
        dto.setDateDepense(depenseMission.getDateDepense());
        return dto;
    }

    @Override
    public DepenseMission toEntity(DepenseMissionDTO dto) {
        if (dto == null)
            return null;
        DepenseMission depenseMission = new DepenseMission();
        depenseMission.setId(dto.getIdDepense());
        depenseMission.setIdMission(dto.getIdMission());
        depenseMission.setNature(
                dto.getNatureDepense() != null ? NatureDepenseMission.valueOf(dto.getNatureDepense()) : null);
        depenseMission.setMontant(dto.getMontant());
        depenseMission.setJustificatif(dto.getJustificatifPath());
        depenseMission.setDateDepense(dto.getDateDepense());
        return depenseMission;
    }

    @Override
    public List<DepenseMissionDTO> toDTOList(List<DepenseMission> depenseMissions) {
        if (depenseMissions == null)
            return Collections.emptyList();
        return depenseMissions.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<DepenseMission> toEntityList(List<DepenseMissionDTO> depenseMissionDTOs) {
        if (depenseMissionDTOs == null)
            return Collections.emptyList();
        return depenseMissionDTOs.stream().map(this::toEntity).collect(Collectors.toList());
    }
}