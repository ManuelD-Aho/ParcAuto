package main.java.com.miage.parcauto.mapper;

import main.java.com.miage.parcauto.dto.VehiculeDTO;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe utilitaire pour mapper les objets Vehicule vers VehiculeDTO et vice
 * versa.
 * Implémente les méthodes de conversion entre les entités et les DTOs.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class VehiculeMapper {

    /**
     * Convertit une entité Vehicule en DTO VehiculeDTO.
     *
     * @param vehicule L'entité à convertir
     * @return Le DTO correspondant
     */
    public static VehiculeDTO toDTO(Vehicule vehicule) {
        if (vehicule == null) {
            return null;
        }

        VehiculeDTO dto = new VehiculeDTO();
        dto.setIdVehicule(vehicule.getIdVehicule());
        dto.setEtatVoiture(vehicule.getEtatVoiture());
        dto.setEnergie(vehicule.getEnergie());
        dto.setNumeroChassi(vehicule.getNumeroChassi());
        dto.setImmatriculation(vehicule.getImmatriculation());
        dto.setMarque(vehicule.getMarque());
        dto.setModele(vehicule.getModele());
        dto.setNbPlaces(vehicule.getNbPlaces());
        dto.setDateAcquisition(vehicule.getDateAcquisition());
        dto.setDateAmmortissement(vehicule.getDateAmmortissement());
        dto.setDateMiseEnService(vehicule.getDateMiseEnService());
        dto.setPuissance(vehicule.getPuissance());
        dto.setCouleur(vehicule.getCouleur());
        dto.setPrixVehicule(vehicule.getPrixVehicule());
        dto.setKmActuels(vehicule.getKmActuels());

        // Les champs supplémentaires dans le DTO qui ne sont pas dans l'entité
        // seront gérés dans les services appropriés

        return dto;
    }

    /**
     * Convertit un DTO VehiculeDTO en entité Vehicule.
     *
     * @param dto Le DTO à convertir
     * @return L'entité correspondante
     */
    public static Vehicule toEntity(VehiculeDTO dto) {
        if (dto == null) {
            return null;
        }

        Vehicule vehicule = new Vehicule();
        vehicule.setIdVehicule(dto.getIdVehicule());
        vehicule.setEtatVoiture(dto.getEtatVoiture());
        vehicule.setEnergie(dto.getEnergie());
        vehicule.setNumeroChassi(dto.getNumeroChassi());
        vehicule.setImmatriculation(dto.getImmatriculation());
        vehicule.setMarque(dto.getMarque());
        vehicule.setModele(dto.getModele());
        vehicule.setNbPlaces(dto.getNbPlaces());
        vehicule.setDateAcquisition(dto.getDateAcquisition());
        vehicule.setDateAmmortissement(dto.getDateAmmortissement());
        vehicule.setDateMiseEnService(dto.getDateMiseEnService());
        vehicule.setPuissance(dto.getPuissance());
        vehicule.setCouleur(dto.getCouleur());
        vehicule.setPrixVehicule(dto.getPrixVehicule());
        vehicule.setKmActuels(dto.getKmActuels());

        return vehicule;
    }

    /**
     * Convertit une liste d'entités Vehicule en liste de DTOs VehiculeDTO.
     *
     * @param vehicules La liste d'entités à convertir
     * @return La liste de DTOs correspondante
     */
    public static List<VehiculeDTO> toDTOList(List<Vehicule> vehicules) {
        if (vehicules == null) {
            return null;
        }

        return vehicules.stream()
                .map(VehiculeMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste de DTOs VehiculeDTO en liste d'entités Vehicule.
     *
     * @param dtos La liste de DTOs à convertir
     * @return La liste d'entités correspondante
     */
    public static List<Vehicule> toEntityList(List<VehiculeDTO> dtos) {
        if (dtos == null) {
            return null;
        }

        return dtos.stream()
                .map(VehiculeMapper::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * Mets à jour une entité existante avec les données d'un DTO.
     * Utile pour les opérations de mise à jour partielles.
     *
     * @param entity L'entité à mettre à jour
     * @param dto    Le DTO contenant les données à jour
     */
    public static void updateEntityFromDTO(Vehicule entity, VehiculeDTO dto) {
        if (entity == null || dto == null) {
            return;
        }

        entity.setEtatVoiture(dto.getEtatVoiture());
        entity.setEnergie(dto.getEnergie());
        entity.setNumeroChassi(dto.getNumeroChassi());
        entity.setImmatriculation(dto.getImmatriculation());
        entity.setMarque(dto.getMarque());
        entity.setModele(dto.getModele());
        entity.setNbPlaces(dto.getNbPlaces());
        entity.setDateAcquisition(dto.getDateAcquisition());
        entity.setDateAmmortissement(dto.getDateAmmortissement());
        entity.setDateMiseEnService(dto.getDateMiseEnService());
        entity.setPuissance(dto.getPuissance());
        entity.setCouleur(dto.getCouleur());
        entity.setPrixVehicule(dto.getPrixVehicule());
        entity.setKmActuels(dto.getKmActuels());
    }
}
