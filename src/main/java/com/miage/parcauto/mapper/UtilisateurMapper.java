package main.java.com.miage.parcauto.mapper;

import main.java.com.miage.parcauto.dto.UtilisateurDTO;
import main.java.com.miage.parcauto.model.utilisateur.Utilisateur;
import main.java.com.miage.parcauto.model.utilisateur.Role; // Assurez-vous que Role est importable

import java.util.List;
import java.util.stream.Collectors;

public interface UtilisateurMapper {

    UtilisateurDTO toDTO(Utilisateur utilisateur);

    Utilisateur toEntity(UtilisateurDTO utilisateurDTO);

    List<UtilisateurDTO> toDTOList(List<Utilisateur> utilisateurs);

    List<Utilisateur> toEntityList(List<UtilisateurDTO> utilisateurDTOs);
}