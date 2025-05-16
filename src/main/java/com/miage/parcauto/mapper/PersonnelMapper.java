package main.java.com.miage.parcauto.mapper;

import main.java.com.miage.parcauto.dto.PersonnelDTO;
import main.java.com.miage.parcauto.model.rh.Personnel;
import java.util.List;

public interface PersonnelMapper {
    PersonnelDTO toDTO(Personnel personnel);
    Personnel toEntity(PersonnelDTO personnelDTO);
    List<PersonnelDTO> toDTOList(List<Personnel> personnels);
    List<Personnel> toEntityList(List<PersonnelDTO> personnelDTOs);
}