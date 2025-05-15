package main.java.com.miage.parcauto.mapper;

import main.java.com.miage.parcauto.dto.EntretienDTO;
import main.java.com.miage.parcauto.model.entretien.Entretien;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Classe utilitaire pour convertir entre les entités Entretien et les objets
 * EntretienDTO.
 * Implémente le pattern Mapper pour la séparation des couches.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class EntretienMapper {

    private final VehiculeRepository vehiculeRepository;

    /**
     * Constructeur par défaut.
     */
    public EntretienMapper() {
        this.vehiculeRepository = new VehiculeRepositoryImpl();
    }

    /**
     * Constructeur avec injection de dépendance (pour tests).
     * 
     * @param vehiculeRepository Repository de véhicules
     */
    public EntretienMapper(VehiculeRepository vehiculeRepository) {
        this.vehiculeRepository = vehiculeRepository;
    }

    /**
     * Convertit une entité Entretien en DTO.
     *
     * @param entretien Entité à convertir
     * @return DTO correspondant à l'entité
     */
    public EntretienDTO toDTO(Entretien entretien) {
        if (entretien == null) {
            return null;
        }

        // Récupérer les informations du véhicule si nécessaire
        String immatriculation = "";
        String marqueModele = "";

        // Si l'objet véhicule est déjà chargé dans l'entretien
        if (entretien.getVehicule() != null) {
            Vehicule vehicule = entretien.getVehicule();
            immatriculation = vehicule.getImmatriculation();
            marqueModele = vehicule.getMarque() + " " + vehicule.getModele();
        }
        // Sinon, charger depuis la base de données
        else if (entretien.getIdVehicule() != null) {
            Optional<Vehicule> vehiculeOpt = vehiculeRepository.findById(entretien.getIdVehicule());
            if (vehiculeOpt.isPresent()) {
                Vehicule vehicule = vehiculeOpt.get();
                immatriculation = vehicule.getImmatriculation();
                marqueModele = vehicule.getMarque() + " " + vehicule.getModele();
            }
        }

        return new EntretienDTO(
                entretien.getIdEntretien(),
                entretien.getIdVehicule(),
                immatriculation,
                marqueModele,
                entretien.getDateEntreeEntr(),
                entretien.getDateSortieEntr(),
                entretien.getMotifEntr(),
                entretien.getObservation(),
                entretien.getCoutEntr(),
                entretien.getLieuEntr(),
                entretien.getType(),
                entretien.getStatutOt());
    }

    /**
     * Convertit un DTO en entité Entretien.
     *
     * @param dto DTO à convertir
     * @return Entité correspondante au DTO
     */
    public Entretien toEntity(EntretienDTO dto) {
        if (dto == null) {
            return null;
        }

        Entretien entretien = new Entretien();
        entretien.setIdEntretien(dto.getIdEntretien());
        entretien.setIdVehicule(dto.getIdVehicule());
        entretien.setDateEntreeEntr(dto.getDateEntreeEntr());
        entretien.setDateSortieEntr(dto.getDateSortieEntr());
        entretien.setMotifEntr(dto.getMotifEntr());
        entretien.setObservation(dto.getObservation());
        entretien.setCoutEntr(dto.getCoutEntr());
        entretien.setLieuEntr(dto.getLieuEntr());
        entretien.setType(dto.getType());
        entretien.setStatutOt(dto.getStatutOt());

        // Charger le véhicule complet si nécessaire
        if (dto.getIdVehicule() != null) {
            vehiculeRepository.findById(dto.getIdVehicule()).ifPresent(entretien::setVehicule);
        }

        return entretien;
    }

    /**
     * Convertit une liste d'entités en liste de DTOs.
     *
     * @param entretiens Liste d'entités à convertir
     * @return Liste de DTOs
     */
    public List<EntretienDTO> toDTOList(List<Entretien> entretiens) {
        if (entretiens == null) {
            return List.of();
        }
        return entretiens.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste de DTOs en liste d'entités.
     *
     * @param dtos Liste de DTOs à convertir
     * @return Liste d'entités
     */
    public List<Entretien> toEntityList(List<EntretienDTO> dtos) {
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
     * @param entretien Entité à mettre à jour
     * @param dto       DTO contenant les nouvelles valeurs
     * @return Entité mise à jour
     */
    public Entretien updateEntityFromDTO(Entretien entretien, EntretienDTO dto) {
        if (entretien == null || dto == null) {
            return entretien;
        }

        // Ne pas mettre à jour l'ID
        entretien.setIdVehicule(dto.getIdVehicule());
        entretien.setDateEntreeEntr(dto.getDateEntreeEntr());
        entretien.setDateSortieEntr(dto.getDateSortieEntr());
        entretien.setMotifEntr(dto.getMotifEntr());
        entretien.setObservation(dto.getObservation());
        entretien.setCoutEntr(dto.getCoutEntr());
        entretien.setLieuEntr(dto.getLieuEntr());
        entretien.setType(dto.getType());
        entretien.setStatutOt(dto.getStatutOt());

        // Recharger le véhicule si l'ID a changé
        if (dto.getIdVehicule() != null && (entretien.getVehicule() == null
                || !dto.getIdVehicule().equals(entretien.getVehicule().getIdVehicule()))) {
            vehiculeRepository.findById(dto.getIdVehicule()).ifPresent(entretien::setVehicule);
        }

        return entretien;
    }
}
