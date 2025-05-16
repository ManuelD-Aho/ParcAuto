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
        // Note: id_vehicule dans PERSONNEL semble être pour une affectation permanente,
        // cela devrait être géré par AffectationService.
        // dto.setIdVehicule(personnel.getIdVehicule());
        dto.setMatricule(personnel.getMatricule());
        dto.setNom(personnel.getNom()); // nom_personnel dans DB
        dto.setPrenom(personnel.getPrenom()); // prenom_personnel dans DB
        dto.setEmail(personnel.getEmail());
        dto.setTelephone(personnel.getTelephone());
        dto.setAdresse(personnel.getAdresse());
        dto.setDateNaissance(personnel.getDateNaissance());
        dto.setSexe(personnel.getSexe() != null ? personnel.getSexe().name() : null);
        // dto.setDateAttributionVehicule(personnel.getDateAttribution()); // date_attribution dans DB
        dto.setDateEmbauche(personnel.getDateEmbauche()); // Manquant dans DB PERSONNEL, ajouter si nécessaire
        dto.setObservation(personnel.getObservation()); // Manquant dans DB PERSONNEL, ajouter si nécessaire
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
        // entity.setIdVehicule(dto.getIdVehicule());
        entity.setMatricule(dto.getMatricule());
        entity.setNom(dto.getNom());
        entity.setPrenom(dto.getPrenom());
        entity.setEmail(dto.getEmail());
        entity.setTelephone(dto.getTelephone());
        entity.setAdresse(dto.getAdresse());
        entity.setDateNaissance(dto.getDateNaissance());
        if (dto.getSexe() != null) {
            try {
                entity.setSexe(Sexe.valueOf(dto.getSexe()));
            } catch (IllegalArgumentException e) {
                System.err.println("Sexe invalide dans DTO: " + dto.getSexe());
            }
        }
        // entity.setDateAttribution(dto.getDateAttributionVehicule());
        entity.setDateEmbauche(dto.getDateEmbauche());
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