package main.java.com.miage.parcauto.mapper.impl;

import main.java.com.miage.parcauto.dto.VehiculeDTO;
import main.java.com.miage.parcauto.mapper.VehiculeMapper;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;
import main.java.com.miage.parcauto.model.vehicule.Energie; // Assurez-vous que Energie est importable
import main.java.com.miage.parcauto.model.vehicule.EtatVoiture; // Pour le libellé de l'état

import main.java.com.miage.parcauto.dao.EtatVoitureRepository; // Pour récupérer le libellé
import main.java.com.miage.parcauto.dao.impl.EtatVoitureRepositoryImpl; // Pour récupérer le libellé
import main.java.com.miage.parcauto.dao.DbUtil; // Pour la connexion

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VehiculeMapperImpl implements VehiculeMapper {

    // Optionnel: injecter le repository si on veut enrichir le DTO avec le libellé de l'état
    private EtatVoitureRepository etatVoitureRepository;

    public VehiculeMapperImpl() {
        // Pour des raisons de simplicité ici, on l'instancie directement.
        // Dans une application avec injection de dépendances, il serait injecté.
        this.etatVoitureRepository = new EtatVoitureRepositoryImpl();
    }
    public VehiculeMapperImpl(EtatVoitureRepository etatVoitureRepository) {
        this.etatVoitureRepository = etatVoitureRepository;
    }


    @Override
    public VehiculeDTO toDTO(Vehicule vehicule) {
        if (vehicule == null) {
            return null;
        }
        VehiculeDTO dto = new VehiculeDTO();
        dto.setIdVehicule(vehicule.getIdVehicule());
        dto.setIdEtatVoiture(vehicule.getIdEtatVoiture());

        // Enrichissement avec le libellé de l'état
        if (vehicule.getIdEtatVoiture() != null) {
            Connection conn = null;
            try {
                conn = DbUtil.getConnection(); // Attention à la gestion de la connexion ici
                Optional<EtatVoiture> etatOpt = etatVoitureRepository.findById(conn, vehicule.getIdEtatVoiture());
                etatOpt.ifPresent(etatVoiture -> dto.setLibelleEtatVoiture(etatVoiture.getLibelleEtatVoiture())));
            } catch (SQLException e) {
                System.err.println("Erreur lors de la récupération du libellé de l'état pour le véhicule ID " + vehicule.getIdVehicule() + ": " + e.getMessage());
                // Ne pas bloquer le mapping pour ça, le libellé sera juste null
            } finally {
                DbUtil.close(conn);
            }
        }


        dto.setEnergie(vehicule.getEnergie() != null ? vehicule.getEnergie().name() : null);
        dto.setNumeroChassis(vehicule.getNumeroChassis());
        dto.setImmatriculation(vehicule.getImmatriculation());
        dto.setMarque(vehicule.getMarque());
        dto.setModele(vehicule.getModele());
        dto.setNbPlaces(vehicule.getNbPlaces());
        dto.setDateAcquisition(vehicule.getDateAcquisition());
        dto.setDateMiseEnService(vehicule.getDateMiseEnService());
        dto.setPuissance(vehicule.getPuissance());
        dto.setCouleur(vehicule.getCouleur());
        dto.setPrixVehicule(vehicule.getPrixVehicule());
        dto.setKmActuels(vehicule.getKmActuels());
        dto.setDateEtat(vehicule.getDateEtat());
        return dto;
    }

    @Override
    public Vehicule toEntity(VehiculeDTO dto) {
        if (dto == null) {
            return null;
        }
        Vehicule entity = new Vehicule();
        entity.setIdVehicule(dto.getIdVehicule());
        entity.setIdEtatVoiture(dto.getIdEtatVoiture()); // Le service s'assurera de la validité de cet ID
        if (dto.getEnergie() != null) {
            try {
                entity.setEnergie(Energie.valueOf(dto.getEnergie()));
            } catch (IllegalArgumentException e) {
                System.err.println("Energie invalide dans DTO : " + dto.getEnergie());
            }
        }
        entity.setNumeroChassis(dto.getNumeroChassis());
        entity.setImmatriculation(dto.getImmatriculation());
        entity.setMarque(dto.getMarque());
        entity.setModele(dto.getModele());
        entity.setNbPlaces(dto.getNbPlaces());
        entity.setDateAcquisition(dto.getDateAcquisition());
        entity.setDateMiseEnService(dto.getDateMiseEnService());
        entity.setPuissance(dto.getPuissance());
        entity.setCouleur(dto.getCouleur());
        entity.setPrixVehicule(dto.getPrixVehicule());
        entity.setKmActuels(dto.getKmActuels());
        entity.setDateEtat(dto.getDateEtat());
        return entity;
    }

    @Override
    public List<VehiculeDTO> toDTOList(List<Vehicule> vehicules) {
        if (vehicules == null) {
            return Collections.emptyList();
        }
        return vehicules.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Vehicule> toEntityList(List<VehiculeDTO> vehiculeDTOs) {
        if (vehiculeDTOs == null) {
            return Collections.emptyList();
        }
        return vehiculeDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}