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
    public EntretienDTO toDTO(Entretien entretien) {
        if (entretien == null) {
            return null;
        }
        EntretienDTO dto = new EntretienDTO();
        dto.setIdEntretien(entretien.getIdEntretien());
        dto.setIdVehicule(entretien.getIdVehicule());
        dto.setDateEntree(entretien.getDateEntree());
        dto.setDateSortie(entretien.getDateSortie());
        dto.setMotif(entretien.getMotif());
        dto.setObservations(entretien.getObservations());
        dto.setCoutEstime(entretien.getCoutEstime()); // Renommé de cout_entr à coutEstime dans le DTO?
        dto.setCoutReel(entretien.getCoutReel());     // Ajouté pour la clôture
        dto.setLieu(entretien.getLieu());
        dto.setTypeEntretien(entretien.getType() != null ? entretien.getType().name() : null);
        dto.setStatutOt(entretien.getStatutOt() != null ? entretien.getStatutOt().name() : null);

        // Si vous avez des champs spécifiques dans EntretienDTO non présents dans Entretien, mappez-les ici
        // dto.setKmRealisation(entretien.getKmRealisation()); // Exemple
        // dto.setKmProchainEntretien(entretien.getKmProchainEntretien()); // Exemple

        return dto;
    }

    @Override
    public Entretien toEntity(EntretienDTO dto) {
        if (dto == null) {
            return null;
        }
        Entretien entity = new Entretien();
        entity.setIdEntretien(dto.getIdEntretien());
        entity.setIdVehicule(dto.getIdVehicule());
        entity.setDateEntree(dto.getDateEntree());
        entity.setDateSortie(dto.getDateSortie());
        entity.setMotif(dto.getMotif());
        entity.setObservations(dto.getObservations());
        entity.setCoutEstime(dto.getCoutEstime()); // Assurez-vous que le champ BD est cout_entr ou adaptez
        entity.setCoutReel(dto.getCoutReel());
        entity.setLieu(dto.getLieu());

        if (dto.getTypeEntretien() != null) {
            try {
                entity.setType(TypeEntretien.valueOf(dto.getTypeEntretien()));
            } catch (IllegalArgumentException e) {
                System.err.println("Type d'entretien invalide dans DTO: " + dto.getTypeEntretien());
            }
        }
        if (dto.getStatutOt() != null) {
            try {
                entity.setStatutOt(StatutOT.valueOf(dto.getStatutOt()));
            } catch (IllegalArgumentException e) {
                System.err.println("Statut OT invalide dans DTO: " + dto.getStatutOt());
            }
        }
        // entity.setKmRealisation(dto.getKmRealisation());
        // entity.setKmProchainEntretien(dto.getKmProchainEntretien());
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