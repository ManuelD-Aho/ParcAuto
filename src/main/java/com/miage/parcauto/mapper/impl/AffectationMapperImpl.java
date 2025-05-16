package main.java.com.miage.parcauto.mapper.impl;

import main.java.com.miage.parcauto.dto.AffectationDTO;
import main.java.com.miage.parcauto.mapper.AffectationMapper;
import main.java.com.miage.parcauto.model.affectation.Affectation;
import main.java.com.miage.parcauto.model.affectation.TypeAffectation;

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
        if (affectation == null)
            return null;
        AffectationDTO dto = new AffectationDTO();
        dto.setIdAffectation(affectation.getId());
        dto.setIdVehicule(affectation.getIdVehicule());
        dto.setIdPersonnel(affectation.getIdPersonnel());
        dto.setIdSocietaire(affectation.getIdSocietaire());
        dto.setTypeAffectation(
                affectation.getTypeAffectation() != null ? affectation.getTypeAffectation().name() : null);
        return dto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Affectation toEntity(AffectationDTO dto) {
        if (dto == null)
            return null;
        Affectation affectation = new Affectation();
        affectation.setId(dto.getIdAffectation());
        affectation.setIdVehicule(dto.getIdVehicule());
        affectation.setIdPersonnel(dto.getIdPersonnel());
        affectation.setIdSocietaire(dto.getIdSocietaire());
        affectation.setTypeAffectation(
                dto.getTypeAffectation() != null ? TypeAffectation.valueOf(dto.getTypeAffectation()) : null);
        return affectation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AffectationDTO> toDTOList(List<Affectation> affectations) {
        if (affectations == null)
            return Collections.emptyList();
        return affectations.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Affectation> toEntityList(List<AffectationDTO> affectationDTOs) {
        if (affectationDTOs == null)
            return Collections.emptyList();
        return affectationDTOs.stream().map(this::toEntity).collect(Collectors.toList());
    }
}