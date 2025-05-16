package main.java.com.miage.parcauto.service;

import main.java.com.miage.parcauto.dto.BilanFinancierDTO;
import main.java.com.miage.parcauto.dto.CoutEntretienDTO;
import main.java.com.miage.parcauto.dto.TCODTO; // Supposons que ce DTO existe pour le TCO global
import main.java.com.miage.parcauto.dto.TcoVehiculeDTO;
import main.java.com.miage.parcauto.exception.OperationFailedException;
import main.java.com.miage.parcauto.exception.VehiculeNotFoundException;

import java.time.LocalDate;
import java.util.List;

/**
 * Service pour la génération de rapports et bilans financiers.
 */
public interface FinanceReportingService {

    /**
     * Calcule le bilan financier global pour une période donnée.
     *
     * @param dateDebut La date de début de la période.
     * @param dateFin La date de fin de la période.
     * @return Un BilanFinancierDTO contenant les informations agrégées.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    BilanFinancierDTO genererBilanFinancierPeriode(LocalDate dateDebut, LocalDate dateFin) throws OperationFailedException;

    /**
     * Calcule les coûts d'entretien par véhicule pour une année donnée.
     *
     * @param annee L'année pour laquelle calculer les coûts.
     * @return Une liste de CoutEntretienDTO.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    List<CoutEntretienDTO> getCoutsEntretiensParVehiculeAnnee(int annee) throws OperationFailedException;

    /**
     * Calcule le Coût Total de Possession (TCO) pour un véhicule spécifique sur sa durée de vie ou une période.
     *
     * @param idVehicule L'identifiant du véhicule.
     * @return Un TcoVehiculeDTO détaillé.
     * @throws VehiculeNotFoundException Si le véhicule n'est pas trouvé.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    TcoVehiculeDTO calculerTCOVehicule(Integer idVehicule) throws VehiculeNotFoundException, OperationFailedException;

    /**
     * Calcule le Coût Total de Possession (TCO) pour l'ensemble de la flotte sur une période donnée.
     * @param dateDebut Date de début de la période.
     * @param dateFin Date de fin de la période.
     * @return Un TCODTO global.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    TCODTO calculerTCOFlotte(LocalDate dateDebut, LocalDate dateFin) throws OperationFailedException;

}