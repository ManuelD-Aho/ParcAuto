package main.java.com.miage.parcauto.mapper;

import main.java.com.miage.parcauto.dto.EntretienDTO;
import main.java.com.miage.parcauto.model.entretien.Entretien;
import java.util.List;

public interface EntretienMapper {
    EntretienDTO toDTO(Entretien entretien);
    Entretien toEntity(EntretienDTO entretienDTO);
    List<EntretienDTO> toDTOList(List<Entretien> entretiens);
    List<Entretien> toEntityList(List<EntretienDTO> entretienDTOs);
}