package main.java.com.miage.parcauto.mapper.impl;

import main.java.com.miage.parcauto.dto.PersonnelDTO;
import main.java.com.miage.parcauto.mapper.PersonnelMapper;
import main.java.com.miage.parcauto.model.rh.Personnel;
import main.java.com.miage.parcauto.model.rh.Sexe;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PersonnelMapperImpl implements PersonnelMapper {

    @Override
    public PersonnelDTO toDTO(Personnel personnel) {
        if (personnel == null) {
            return null;
        }
        PersonnelDTO dto = new PersonnelDTO();
        dto.setIdPersonnel(personnel.getIdPersonnel());
        dto.setIdService(personnel.getIdService());
        dto.setIdFonction(personnel.getIdFonction());
        dto.setNom(personnel.getNom());
        dto.setPrenom(personnel.getPrenom());
        dto.setSexe(personnel.getSexe() != null ? personnel.getSexe().name() : null);
        dto.setDateEmbauche(personnel.getDateAttributionVehicule());
        dto.setObservation(personnel.getObservation());
        return dto;
    }

    @Override
    public Personnel toEntity(PersonnelDTO dto) {
        if (dto == null) {
            return null;
        }
        Personnel entity = new Personnel();
        entity.setIdPersonnel(dto.getIdPersonnel());
        entity.setIdService(dto.getIdService());
        entity.setIdFonction(dto.getIdFonction());
        entity.setNom(dto.getNom());
        entity.setPrenom(dto.getPrenom());
        entity.setSexe(dto.getSexe() != null ? Sexe.valueOf(dto.getSexe()) : null);
        entity.setDateAttributionVehicule(dto.getDateEmbauche());
        entity.setObservation(dto.getObservation());
        return entity;
    }

    @Override
    public List<PersonnelDTO> toDTOList(List<Personnel> personnels) {
        if (personnels == null) {
            return Collections.emptyList();
        }
        return personnels.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Personnel> toEntityList(List<PersonnelDTO> personnelDTOs) {
        if (personnelDTOs == null) {
            return Collections.emptyList();
        }
        return personnelDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}