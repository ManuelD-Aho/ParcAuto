package main.java.com.miage.parcauto.mapper.impl;

import main.java.com.miage.parcauto.dto.UtilisateurDTO;
import main.java.com.miage.parcauto.mapper.UtilisateurMapper;
import main.java.com.miage.parcauto.model.utilisateur.Utilisateur;
import main.java.com.miage.parcauto.model.utilisateur.Role;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UtilisateurMapperImpl implements UtilisateurMapper {
    @Override
    public UtilisateurDTO toDTO(Utilisateur utilisateur) {
        if (utilisateur == null)
            return null;
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setIdUtilisateur(utilisateur.getId());
        dto.setLogin(utilisateur.getLogin());
        dto.setRole(utilisateur.getRole() != null ? utilisateur.getRole().name() : null);
        dto.setIdPersonnel(utilisateur.getIdPersonnel());
        dto.setMfaSecret(utilisateur.getMfaSecret());
        // Les champs motDePasse, actif, dateCreation, dateDerniereConnexion ne sont pas
        // présents côté modèle
        return dto;
    }

    @Override
    public Utilisateur toEntity(UtilisateurDTO dto) {
        if (dto == null)
            return null;
        Utilisateur entity = new Utilisateur();
        entity.setId(dto.getIdUtilisateur());
        entity.setLogin(dto.getLogin());
        if (dto.getRole() != null) {
            try {
                entity.setRole(Role.valueOf(dto.getRole()));
            } catch (IllegalArgumentException e) {
                System.err.println("Rôle invalide dans DTO : " + dto.getRole());
            }
        }
        entity.setIdPersonnel(dto.getIdPersonnel());
        entity.setMfaSecret(dto.getMfaSecret());
        return entity;
    }

    @Override
    public List<UtilisateurDTO> toDTOList(List<Utilisateur> utilisateurs) {
        if (utilisateurs == null)
            return Collections.emptyList();
        return utilisateurs.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<Utilisateur> toEntityList(List<UtilisateurDTO> utilisateurDTOs) {
        if (utilisateurDTOs == null)
            return Collections.emptyList();
        return utilisateurDTOs.stream().map(this::toEntity).collect(Collectors.toList());
    }
}