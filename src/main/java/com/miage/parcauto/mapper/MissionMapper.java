package main.java.com.miage.parcauto.mapper;

import main.java.com.miage.parcauto.dao.VehiculeRepositoryImpl;
import main.java.com.miage.parcauto.dto.DepenseDTO;
import main.java.com.miage.parcauto.dto.MissionDTO;
import main.java.com.miage.parcauto.model.Personnel;
import main.java.com.miage.parcauto.model.mission.DepenseMission;
import main.java.com.miage.parcauto.model.mission.Mission;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Classe utilitaire pour convertir entre les entités Mission et les objets
 * MissionDTO.
 * Implémente le pattern Mapper pour la séparation des couches.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class MissionMapper {

    private final VehiculeRepository vehiculeRepository;
    private final DepenseMapper depenseMapper;

    /**
     * Constructeur par défaut.
     */
    public MissionMapper() {
        this.vehiculeRepository = new VehiculeRepositoryImpl();
        this.depenseMapper = new DepenseMapper();
    }

    /**
     * Constructeur avec injection de dépendance (pour tests).
     * 
     * @param vehiculeRepository Repository de véhicules
     * @param depenseMapper      Mapper de dépenses
     */
    public MissionMapper(VehiculeRepository vehiculeRepository, DepenseMapper depenseMapper) {
        this.vehiculeRepository = vehiculeRepository;
        this.depenseMapper = depenseMapper;
    }

    /**
     * Convertit une entité Mission en DTO.
     *
     * @param mission Entité à convertir
     * @return DTO correspondant à l'entité
     */
    public MissionDTO toDTO(Mission mission) {
        if (mission == null) {
            return null;
        }

        MissionDTO dto = new MissionDTO();
        dto.setIdMission(mission.getIdMission());
        dto.setIdVehicule(mission.getIdVehicule());
        dto.setIdConducteur(mission.getIdConducteur());

        // Récupérer les informations du véhicule si nécessaire
        if (mission.getVehicule() != null) {
            Vehicule vehicule = mission.getVehicule();
            dto.setImmatriculation(vehicule.getImmatriculation());
            dto.setMarqueModele(vehicule.getMarque() + " " + vehicule.getModele());
        } else if (mission.getIdVehicule() != null) {
            Optional<Vehicule> vehiculeOpt = vehiculeRepository.findById(mission.getIdVehicule());
            if (vehiculeOpt.isPresent()) {
                Vehicule vehicule = vehiculeOpt.get();
                dto.setImmatriculation(vehicule.getImmatriculation());
                dto.setMarqueModele(vehicule.getMarque() + " " + vehicule.getModele());
            }
        }

        // Récupérer les informations du conducteur si nécessaire
        if (mission.getConducteur() != null) {
            Personnel conducteur = mission.getConducteur();
            dto.setNomConducteur(conducteur.getNom() + " " + conducteur.getPrenom());
        }

        dto.setDestination(mission.getDestination());
        dto.setMotif(mission.getMotif());
        dto.setDateDepart(mission.getDateDepart());
        dto.setDateRetourPrevue(mission.getDateRetourPrevue());
        dto.setDateRetourReelle(mission.getDateRetourReelle());
        dto.setKilometrageDepart(mission.getKilometrageDepart());
        dto.setKilometrageRetour(mission.getKilometrageRetour());
        dto.setStatut(mission.getStatut());

        // Calculer le coût total à partir des dépenses
        BigDecimal coutTotal = BigDecimal.ZERO;
        List<DepenseDTO> depenseDTOs = new ArrayList<>();

        if (mission.getDepenses() != null) {
            for (DepenseMission depense : mission.getDepenses()) {
                DepenseDTO depenseDTO = depenseMapper.toDTO(depense);
                depenseDTOs.add(depenseDTO);
                if (depense.getMontant() != null) {
                    coutTotal = coutTotal.add(depense.getMontant());
                }
            }
        }

        dto.setCoutTotal(coutTotal);
        dto.setDepenses(depenseDTOs);

        return dto;
    }

    /**
     * Convertit un DTO en entité Mission.
     *
     * @param dto DTO à convertir
     * @return Entité correspondante au DTO
     */
    public Mission toEntity(MissionDTO dto) {
        if (dto == null) {
            return null;
        }

        Mission mission = new Mission();
        mission.setIdMission(dto.getIdMission());
        mission.setIdVehicule(dto.getIdVehicule());
        mission.setIdConducteur(dto.getIdConducteur());
        mission.setDestination(dto.getDestination());
        mission.setMotif(dto.getMotif());
        mission.setDateDepart(dto.getDateDepart());
        mission.setDateRetourPrevue(dto.getDateRetourPrevue());
        mission.setDateRetourReelle(dto.getDateRetourReelle());
        mission.setKilometrageDepart(dto.getKilometrageDepart());
        mission.setKilometrageRetour(dto.getKilometrageRetour());
        mission.setStatut(dto.getStatut());

        // Charger le véhicule complet si nécessaire
        if (dto.getIdVehicule() != null) {
            vehiculeRepository.findById(dto.getIdVehicule()).ifPresent(mission::setVehicule);
        }

        // Convertir les dépenses
        if (dto.getDepenses() != null && !dto.getDepenses().isEmpty()) {
            List<DepenseMission> depenses = dto.getDepenses().stream()
                    .map(depenseMapper::toEntity)
                    .collect(Collectors.toList());
            mission.setDepenses(depenses);
        }

        return mission;
    }

    /**
     * Convertit une liste d'entités en liste de DTOs.
     *
     * @param missions Liste d'entités à convertir
     * @return Liste de DTOs
     */
    public List<MissionDTO> toDTOList(List<Mission> missions) {
        if (missions == null) {
            return List.of();
        }
        return missions.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste de DTOs en liste d'entités.
     *
     * @param dtos Liste de DTOs à convertir
     * @return Liste d'entités
     */
    public List<Mission> toEntityList(List<MissionDTO> dtos) {
        if (dtos == null) {
            return List.of();
        }
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour une entité existante avec les données d'un DTO.
     * Cette méthode est utile pour les mises à jour partielles.
     *
     * @param mission Entité à mettre à jour
     * @param dto     DTO contenant les nouvelles valeurs
     * @return Entité mise à jour
     */
    public Mission updateEntityFromDTO(Mission mission, MissionDTO dto) {
        if (mission == null || dto == null) {
            return mission;
        }

        // Ne pas mettre à jour l'ID
        if (dto.getIdVehicule() != null) {
            mission.setIdVehicule(dto.getIdVehicule());

            // Recharger le véhicule si l'ID a changé
            if (mission.getVehicule() == null
                    || !dto.getIdVehicule().equals(mission.getVehicule().getIdVehicule())) {
                vehiculeRepository.findById(dto.getIdVehicule()).ifPresent(mission::setVehicule);
            }
        }

        if (dto.getIdConducteur() != null) {
            mission.setIdConducteur(dto.getIdConducteur());
        }

        if (dto.getDestination() != null) {
            mission.setDestination(dto.getDestination());
        }

        if (dto.getMotif() != null) {
            mission.setMotif(dto.getMotif());
        }

        if (dto.getDateDepart() != null) {
            mission.setDateDepart(dto.getDateDepart());
        }

        if (dto.getDateRetourPrevue() != null) {
            mission.setDateRetourPrevue(dto.getDateRetourPrevue());
        }

        if (dto.getDateRetourReelle() != null) {
            mission.setDateRetourReelle(dto.getDateRetourReelle());
        }

        if (dto.getKilometrageDepart() != null) {
            mission.setKilometrageDepart(dto.getKilometrageDepart());
        }

        if (dto.getKilometrageRetour() != null) {
            mission.setKilometrageRetour(dto.getKilometrageRetour());
        }

        if (dto.getStatut() != null) {
            mission.setStatut(dto.getStatut());
        }

        // Mettre à jour les dépenses si nécessaire
        if (dto.getDepenses() != null) {
            List<DepenseMission> depenses = dto.getDepenses().stream()
                    .map(depenseMapper::toEntity)
                    .collect(Collectors.toList());
            mission.setDepenses(depenses);
        }

        return mission;
    }
}
