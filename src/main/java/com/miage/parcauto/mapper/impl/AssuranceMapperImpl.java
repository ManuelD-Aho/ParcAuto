package main.java.com.miage.parcauto.mapper.impl;

import main.java.com.miage.parcauto.dto.AssuranceDTO;
import main.java.com.miage.parcauto.mapper.AssuranceMapper;
import main.java.com.miage.parcauto.model.assurance.Assurance;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation de {@link AssuranceMapper}.
 */
public class AssuranceMapperImpl implements AssuranceMapper {
    /**
     * {@inheritDoc}
     */
    @Override
    public AssuranceDTO toDTO(Assurance assurance) {
        if (assurance == null)
            return null;
        AssuranceDTO dto = new AssuranceDTO();
        dto.setNumCarteAssurance(assurance.getNumCarteAssurance());
        dto.setDateDebut(assurance.getDateDebut());
        dto.setDateFin(assurance.getDateFin());
        dto.setCout(assurance.getCout());
        return dto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Assurance toEntity(AssuranceDTO dto) {
        if (dto == null)
            return null;
        Assurance assurance = new Assurance();
        assurance.setNumCarteAssurance(dto.getNumCarteAssurance());
        assurance.setDateDebut(dto.getDateDebut());
        assurance.setDateFin(dto.getDateFin());
        assurance.setCout(dto.getCout());
        return assurance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AssuranceDTO> toDTOList(List<Assurance> assurances) {
        if (assurances == null)
            return Collections.emptyList();
        return assurances.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Assurance> toEntityList(List<AssuranceDTO> assuranceDTOs) {
        if (assuranceDTOs == null)
            return Collections.emptyList();
        return assuranceDTOs.stream().map(this::toEntity).collect(Collectors.toList());
    }
}