package main.java.com.miage.parcauto.mapper;

import main.java.com.miage.parcauto.dto.AlerteAssuranceDTO;
import main.java.com.miage.parcauto.dto.AlerteEntretienDTO;
import main.java.com.miage.parcauto.dto.BilanFinancierDTO;
import main.java.com.miage.parcauto.dto.BilanMensuelDTO;
import main.java.com.miage.parcauto.dto.CoutEntretienDTO;
import main.java.com.miage.parcauto.dto.TcoVehiculeDTO;
import main.java.com.miage.parcauto.dto.VehiculeRentabiliteDTO;
import main.java.com.miage.parcauto.model.finance.BilanFinancier;
import main.java.com.miage.parcauto.model.finance.CoutEntretien;

import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mapper pour convertir les entités financières en DTOs et vice-versa.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class FinanceMapper {

    /**
     * Convertit un BilanFinancier en BilanFinancierDTO.
     * 
     * @param bilan l'entité BilanFinancier
     * @return le DTO correspondant
     */
    public static BilanFinancierDTO toDTO(BilanFinancier bilan) {
        if (bilan == null) {
            return null;
        }

        BilanFinancierDTO dto = new BilanFinancierDTO();
        dto.setDateDebut(bilan.getDateDebut());
        dto.setDateFin(bilan.getDateFin());
        dto.setRecettes(bilan.getRecettes());
        dto.setDepenses(bilan.getDepenses());
        dto.setSolde(bilan.getSolde());
        dto.setCoutEntretien(bilan.getCoutEntretien());
        dto.setCoutCarburant(bilan.getCoutCarburant());
        dto.setCoutAssurance(bilan.getCoutAssurance());
        dto.setCoutAutres(bilan.getCoutAutres());
        dto.setNombreMissions(bilan.getNombreMissions());
        dto.setNombreEntretiens(bilan.getNombreEntretiens());

        return dto;
    }

    /**
     * Convertit un BilanFinancierDTO en BilanFinancier.
     * 
     * @param dto le DTO BilanFinancierDTO
     * @return l'entité correspondante
     */
    public static BilanFinancier toEntity(BilanFinancierDTO dto) {
        if (dto == null) {
            return null;
        }

        BilanFinancier bilan = new BilanFinancier();
        bilan.setDateDebut(dto.getDateDebut());
        bilan.setDateFin(dto.getDateFin());
        bilan.setRecettes(dto.getRecettes());
        bilan.setDepenses(dto.getDepenses());
        bilan.setSolde(dto.getSolde());
        bilan.setCoutEntretien(dto.getCoutEntretien());
        bilan.setCoutCarburant(dto.getCoutCarburant());
        bilan.setCoutAssurance(dto.getCoutAssurance());
        bilan.setCoutAutres(dto.getCoutAutres());
        bilan.setNombreMissions(dto.getNombreMissions());
        bilan.setNombreEntretiens(dto.getNombreEntretiens());

        return bilan;
    }

    /**
     * Convertit un CoutEntretien en CoutEntretienDTO.
     * 
     * @param cout l'entité CoutEntretien
     * @return le DTO correspondant
     */
    public static CoutEntretienDTO toDTO(CoutEntretien cout) {
        if (cout == null) {
            return null;
        }

        CoutEntretienDTO dto = new CoutEntretienDTO();
        dto.setIdVehicule(cout.getIdVehicule());
        dto.setMarque(cout.getMarque());
        dto.setModele(cout.getModele());
        dto.setImmatriculation(cout.getImmatriculation());
        dto.setNombreEntretiens(cout.getNombreEntretiens());
        dto.setCoutTotal(cout.getCoutTotal());
        dto.setCoutMoyen(cout.getCoutMoyen());

        return dto;
    }

    /**
     * Convertit une liste de CoutEntretien en liste de CoutEntretienDTO.
     * 
     * @param couts la liste d'entités CoutEntretien
     * @return la liste de DTOs correspondante
     */
    public static List<CoutEntretienDTO> toDTO(List<CoutEntretien> couts) {
        if (couts == null) {
            return new ArrayList<>();
        }

        return couts.stream()
                .map(FinanceMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit un TCOVehicule en TcoVehiculeDTO.
     * 
     * @param tco l'entité TCOVehicule
     * @return le DTO correspondant
     */
    public static TcoVehiculeDTO toDTO(TCOVehicule tco) {
        if (tco == null) {
            return null;
        }

        TcoVehiculeDTO dto = new TcoVehiculeDTO();
        dto.setIdVehicule(tco.getIdVehicule());
        dto.setMarque(tco.getMarque());
        dto.setModele(tco.getModele());
        dto.setImmatriculation(tco.getImmatriculation());
        dto.setDateMiseEnService(tco.getDateMiseEnService().toLocalDate());
        dto.setCoutAcquisition(tco.getCoutAcquisition());
        dto.setValeurResiduelle(tco.getValeurResiduelle());
        dto.setCoutEntretien(tco.getCoutEntretien());
        dto.setCoutAssurance(tco.getCoutAssurance());
        dto.setCoutCarburant(tco.getCoutCarburant());
        dto.setCoutAutres(tco.getCoutAutres());
        dto.setTcoTotal(tco.getTcoTotal());
        dto.setTcoMensuel(tco.getTcoMensuel());
        dto.setTcoParKm(tco.getTcoParKm());
        dto.setKmParcourus(tco.getKmParcourus());
        dto.setDureeEnMois(tco.getDureeEnMois());

        return dto;
    }

    /**
     * Convertit une Map de Month à BilanMensuel en Map de Month à BilanMensuelDTO.
     * 
     * @param bilans la Map de Month à BilanMensuel
     * @return la Map de Month à BilanMensuelDTO correspondante
     */
    public static Map<Month, BilanMensuelDTO> toDTO(Map<Month, BilanMensuel> bilans) {
        if (bilans == null) {
            return new HashMap<>();
        }

        Map<Month, BilanMensuelDTO> result = new HashMap<>();

        for (Map.Entry<Month, BilanMensuel> entry : bilans.entrySet()) {
            BilanMensuel bilan = entry.getValue();
            BilanMensuelDTO dto = new BilanMensuelDTO();

            dto.setMois(bilan.getMois());
            dto.setNombreMissions(bilan.getNombreMissions());
            dto.setNombreEntretiens(bilan.getNombreEntretiens());
            dto.setRecettes(bilan.getRecettes());
            dto.setDepenses(bilan.getDepenses());
            dto.setSolde(bilan.getSolde());

            result.put(entry.getKey(), dto);
        }

        return result;
    }

    /**
     * Convertit une liste de RentabiliteVehicule en liste de
     * VehiculeRentabiliteDTO.
     * 
     * @param rentabilites la liste d'entités RentabiliteVehicule
     * @return la liste de DTOs correspondante
     */
    public static List<VehiculeRentabiliteDTO> toDTO(List<RentabiliteVehicule> rentabilites) {
        if (rentabilites == null) {
            return new ArrayList<>();
        }

        return rentabilites.stream()
                .map(rv -> {
                    VehiculeRentabiliteDTO dto = new VehiculeRentabiliteDTO();
                    dto.setIdVehicule(rv.getIdVehicule());
                    dto.setMarque(rv.getMarque());
                    dto.setModele(rv.getModele());
                    dto.setImmatriculation(rv.getImmatriculation());
                    dto.setNombreMissions(rv.getNombreMissions());
                    dto.setKmParcourus(rv.getKmParcourus());
                    dto.setRecettesGenerees(rv.getRecettesGenerees());
                    dto.setCoutTotal(rv.getCoutTotal());
                    dto.setBeneficeNet(rv.getBeneficeNet());
                    dto.setRentabilite(rv.getRentabilite());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste d'AlerteAssurance en liste d'AlerteAssuranceDTO.
     * 
     * @param alertes la liste d'entités AlerteAssurance
     * @return la liste de DTOs correspondante
     */
    public static List<AlerteAssuranceDTO> toAssuranceDTO(List<AlerteAssurance> alertes) {
        if (alertes == null) {
            return new ArrayList<>();
        }

        return alertes.stream()
                .map(a -> {
                    AlerteAssuranceDTO dto = new AlerteAssuranceDTO();
                    dto.setIdVehicule(a.getIdVehicule());
                    dto.setMarque(a.getMarque());
                    dto.setModele(a.getModele());
                    dto.setImmatriculation(a.getImmatriculation());
                    dto.setDateFinAssurance(a.getDateFinAssurance().toLocalDate());
                    dto.setJoursRestants(a.getJoursRestants());
                    dto.setStatut(a.getStatut());
                    dto.setNumeroContrat(a.getNumeroContrat());
                    dto.setCompagnie(a.getCompagnie());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste d'AlerteEntretien en liste d'AlerteEntretienDTO.
     * 
     * @param alertes la liste d'entités AlerteEntretien
     * @return la liste de DTOs correspondante
     */
    public static List<AlerteEntretienDTO> toEntretienDTO(List<AlerteEntretien> alertes) {
        if (alertes == null) {
            return new ArrayList<>();
        }

        return alertes.stream()
                .map(a -> {
                    AlerteEntretienDTO dto = new AlerteEntretienDTO();
                    dto.setIdVehicule(a.getIdVehicule());
                    dto.setMarque(a.getMarque());
                    dto.setModele(a.getModele());
                    dto.setImmatriculation(a.getImmatriculation());
                    dto.setKilometrage(a.getKilometrage());
                    dto.setKmRestants(a.getKmRestants());
                    dto.setTypeEntretien(a.getTypeEntretien());
                    dto.setKmPrevu(a.getKmPrevu());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
