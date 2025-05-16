package main.java.com.miage.parcauto.mapper;

import main.java.com.miage.parcauto.dto.DepenseMissionDTO;
import main.java.com.miage.parcauto.model.mission.DepenseMission;
import java.util.List;

public interface DepenseMissionMapper {
    DepenseMissionDTO toDTO(DepenseMission depenseMission);
    DepenseMission toEntity(DepenseMissionDTO depenseMissionDTO);
    List<DepenseMissionDTO> toDTOList(List<DepenseMission> depenseMissions);
    List<DepenseMission> toEntityList(List<DepenseMissionDTO> depenseMissionDTOs);
}