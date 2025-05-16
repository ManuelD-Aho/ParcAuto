package main.java.com.miage.parcauto.mapper.impl;

import main.java.com.miage.parcauto.dto.UtilisateurDTO;
import main.java.com.miage.parcauto.mapper.UtilisateurMapper;
import main.java.com.miage.parcauto.model.utilisateur.Utilisateur;
import main.java.com.miage.parcauto.model.utilisateur.Role; // Assurez-vous que Role est importable

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UtilisateurMapperImpl implements UtilisateurMapper {

    @Override
    public UtilisateurDTO toDTO(Utilisateur utilisateur) {
        if (utilisateur == null) {
            return null;
        }
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.setId(utilisateur.getId());
        dto.setLogin(utilisateur.getLogin());
        // Le hash ne doit jamais être exposé dans le DTO par défaut
        // dto.setHash(utilisateur.getHash());
        dto.setRole(utilisateur.getRole() != null ? utilisateur.getRole().name() : null);
        dto.setIdPersonnel(utilisateur.getIdPersonnel());
        dto.setMfaSecret(utilisateur.getMfaSecret()); // Exposer avec prudence
        dto.setActif(utilisateur.isActif());
        dto.setDateCreation(utilisateur.getDateCreation());
        dto.setDateDerniereConnexion(utilisateur.getDateDerniereConnexion());
        return dto;
    }

    @Override
    public Utilisateur toEntity(UtilisateurDTO dto) {
        if (dto == null) {
            return null;
        }
        Utilisateur entity = new Utilisateur();
        entity.setId(dto.getId());
        entity.setLogin(dto.getLogin());
        // Le hash est géré par le service lors de la création/mise à jour de mot de passe
        // entity.setHash(dto.getHash());
        if (dto.getRole() != null) {
            try {
                entity.setRole(Role.valueOf(dto.getRole()));
            } catch (IllegalArgumentException e) {
                // Gérer le cas où la chaîne de rôle n'est pas valide, par exemple, logger ou affecter une valeur par défaut
                System.err.println("Rôle invalide dans DTO : " + dto.getRole());
            }
        }
        entity.setIdPersonnel(dto.getIdPersonnel());
        entity.setMfaSecret(dto.getMfaSecret());
        entity.setActif(dto.isActif());
        entity.setDateCreation(dto.getDateCreation());
        entity.setDateDerniereConnexion(dto.getDateDerniereConnexion());
        return entity;
    }

    @Override
    public List<UtilisateurDTO> toDTOList(List<Utilisateur> utilisateurs) {
        if (utilisateurs == null) {
            return Collections.emptyList();
        }
        return utilisateurs.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Utilisateur> toEntityList(List<UtilisateurDTO> utilisateurDTOs) {
        if (utilisateurDTOs == null) {
            return Collections.emptyList();
        }
        return utilisateurDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}