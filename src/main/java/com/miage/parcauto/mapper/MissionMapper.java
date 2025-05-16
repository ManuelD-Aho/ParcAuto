package main.java.com.miage.parcauto.mapper;

import main.java.com.miage.parcauto.dto.MissionDTO;
import main.java.com.miage.parcauto.model.mission.Mission;
import java.util.List;

public interface MissionMapper {
    MissionDTO toDTO(Mission mission);
    Mission toEntity(MissionDTO missionDTO);
    List<MissionDTO> toDTOList(List<Mission> missions);
    List<Mission> toEntityList(List<MissionDTO> missionDTOs);
}