package main.java.com.miage.parcauto.mapper.impl;

import main.java.com.miage.parcauto.dto.EntretienDTO;
import main.java.com.miage.parcauto.mapper.EntretienMapper;
import main.java.com.miage.parcauto.model.entretien.Entretien;
import main.java.com.miage.parcauto.model.entretien.TypeEntretien;
import main.java.com.miage.parcauto.model.entretien.StatutOT;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EntretienMapperImpl implements EntretienMapper {

    @Override
    public EntretienDTO toDTO(Entretien entity) {
        if (entity == null)
            return null;
        EntretienDTO dto = new EntretienDTO();
        dto.setIdEntretien(entity.getIdEntretien());
        dto.setIdVehicule(entity.getIdVehicule());
        dto.setDateEntree(entity.getDateEntree());
        dto.setDateSortie(entity.getDateSortie());
        dto.setMotif(entity.getMotif());
        dto.setObservations(entity.getObservation());
        dto.setCoutEstime(entity.getCout());
        dto.setCoutReel(entity.getCout());
        dto.setLieu(entity.getLieu());
        dto.setTypeEntretien(entity.getType() != null ? entity.getType().name() : null);
        dto.setStatutOT(entity.getStatut() != null ? entity.getStatut().name() : null);
        return dto;
    }

    @Override
    public Entretien toEntity(EntretienDTO dto) {
        if (dto == null)
            return null;
        Entretien entity = new Entretien();
        entity.setIdEntretien(dto.getIdEntretien());
        entity.setIdVehicule(dto.getIdVehicule());
        entity.setDateEntree(dto.getDateEntree());
        entity.setDateSortie(dto.getDateSortie());
        entity.setMotif(dto.getMotif());
        entity.setObservation(dto.getObservations());
        entity.setCout(dto.getCoutReel() != null ? dto.getCoutReel() : dto.getCoutEstime());
        entity.setLieu(dto.getLieu());
        entity.setType(dto.getTypeEntretien() != null ? TypeEntretien.valueOf(dto.getTypeEntretien()) : null);
        entity.setStatut(dto.getStatutOT() != null ? StatutOT.valueOf(dto.getStatutOT()) : null);
        return entity;
    }

    @Override
    public List<EntretienDTO> toDTOList(List<Entretien> entretiens) {
        if (entretiens == null) {
            return Collections.emptyList();
        }
        return entretiens.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Entretien> toEntityList(List<EntretienDTO> entretienDTOs) {
        if (entretienDTOs == null) {
            return Collections.emptyList();
        }
        return entretienDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}