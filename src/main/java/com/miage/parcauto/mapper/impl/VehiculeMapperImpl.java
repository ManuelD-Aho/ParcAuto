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

    // Optionnel: injecter le repository si on veut enrichir le DTO avec le libellé
    // de l'état
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
        if (vehicule == null)
            return null;
        VehiculeDTO dto = new VehiculeDTO();
        dto.setIdVehicule(vehicule.getIdVehicule());
        dto.setIdEtatVoiture(vehicule.getIdEtatVoiture());
        dto.setEnergie(vehicule.getEnergie() != null ? vehicule.getEnergie().name() : null);
        dto.setNumeroChassis(vehicule.getNumeroChassis());
        dto.setImmatriculation(vehicule.getImmatriculation());
        dto.setMarque(vehicule.getMarque());
        dto.setModele(vehicule.getModele());
        dto.setNbPlaces(vehicule.getNbPlaces());
        dto.setDateAcquisition(vehicule.getDateAcquisition());
        dto.setDateAmortissement(vehicule.getDateAmmortissement());
        dto.setDateMiseEnService(vehicule.getDateMiseEnService());
        dto.setPuissance(vehicule.getPuissance());
        dto.setCouleur(vehicule.getCouleur());
        dto.setPrixVehicule(vehicule.getPrixVehicule());
        dto.setKmActuels(vehicule.getKmActuels());
        dto.setDateEtat(vehicule.getDateEtat());
        dto.setActif(null); // À adapter si champ présent côté entité
        // Gestion du libellé d’état si besoin via un service externe
        return dto;
    }

    @Override
    public Vehicule toEntity(VehiculeDTO dto) {
        if (dto == null)
            return null;
        Vehicule vehicule = new Vehicule();
        vehicule.setIdVehicule(dto.getIdVehicule());
        vehicule.setIdEtatVoiture(dto.getIdEtatVoiture());
        vehicule.setEnergie(dto.getEnergie() != null ? Energie.valueOf(dto.getEnergie()) : null);
        vehicule.setNumeroChassis(dto.getNumeroChassis());
        vehicule.setImmatriculation(dto.getImmatriculation());
        vehicule.setMarque(dto.getMarque());
        vehicule.setModele(dto.getModele());
        vehicule.setNbPlaces(dto.getNbPlaces());
        vehicule.setDateAcquisition(dto.getDateAcquisition());
        vehicule.setDateAmmortissement(dto.getDateAmortissement());
        vehicule.setDateMiseEnService(dto.getDateMiseEnService());
        vehicule.setPuissance(dto.getPuissance());
        vehicule.setCouleur(dto.getCouleur());
        vehicule.setPrixVehicule(dto.getPrixVehicule());
        vehicule.setKmActuels(dto.getKmActuels());
        vehicule.setDateEtat(dto.getDateEtat());
        // Champ actif à adapter si présent côté entité
        return vehicule;
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