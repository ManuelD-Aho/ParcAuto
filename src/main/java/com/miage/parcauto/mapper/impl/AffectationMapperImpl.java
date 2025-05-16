package main.java.com.miage.parcauto.mapper.impl;

import main.java.com.miage.parcauto.dto.AffectationDTO;
import main.java.com.miage.parcauto.mapper.AffectationMapper;
import main.java.com.miage.parcauto.model.affectation.Affectation; // Assurez-vous du bon chemin
import main.java.com.miage.parcauto.model.affectation.TypeAffectation; // Assurez-vous du bon chemin

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation de {@link AffectationMapper}.
 */
public class AffectationMapperImpl implements AffectationMapper {

    /**
     * {@inheritDoc}
     */
    @Override
    public AffectationDTO toDTO(Affectation affectation) {
        if (affectation == null) {
            return null;
        }
        AffectationDTO dto = new AffectationDTO();
        dto.setIdAffectation(affectation.getId());
        dto.setIdVehicule(affectation.getIdVehicule());
        dto.setIdPersonnel(affectation.getIdPersonnel());
        dto.setIdSocietaire(affectation.getIdSocietaire());
        dto.setTypeAffectation(affectation.getType() != null ? affectation.getType().name() : null);
        dto.setDateDebut(affectation.getDateDebut());
        dto.setDateFin(affectation.getDateFin());
        return dto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Affectation toEntity(AffectationDTO dto) {
        if (dto == null) {
            return null;
        }
        Affectation entity = new Affectation();
        entity.setId(dto.getIdAffectation());
        entity.setIdVehicule(dto.getIdVehicule());
        entity.setIdPersonnel(dto.getIdPersonnel());
        entity.setIdSocietaire(dto.getIdSocietaire());
        if (dto.getTypeAffectation() != null) {
            try {
                entity.setType(TypeAffectation.valueOf(dto.getTypeAffectation()));
            } catch (IllegalArgumentException e) {
                System.err.println("Type d'affectation invalide dans DTO: " + dto.getTypeAffectation());
            }
        }
        entity.setDateDebut(dto.getDateDebut());
        entity.setDateFin(dto.getDateFin());
        return entity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AffectationDTO> toDTOList(List<Affectation> affectations) {
        if (affectations == null) {
            return Collections.emptyList();
        }
        return affectations.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Affectation> toEntityList(List<AffectationDTO> affectationDTOs) {
        if (affectationDTOs == null) {
            return Collections.emptyList();
        }
        return affectationDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}