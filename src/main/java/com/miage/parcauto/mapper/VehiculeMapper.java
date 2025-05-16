package main.java.com.miage.parcauto.mapper;

import main.java.com.miage.parcauto.dto.VehiculeDTO;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;
import java.util.List;

public interface VehiculeMapper {
    VehiculeDTO toDTO(Vehicule vehicule);
    Vehicule toEntity(VehiculeDTO vehiculeDTO);
    List<VehiculeDTO> toDTOList(List<Vehicule> vehicules);
    List<Vehicule> toEntityList(List<VehiculeDTO> vehiculeDTOs);
}