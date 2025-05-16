package main.java.com.miage.parcauto.service;

import main.java.com.miage.parcauto.dto.BilanFinancierDTO;
import main.java.com.miage.parcauto.dto.RapportDTO; // DTO générique pour encapsuler les données d'un rapport
import main.java.com.miage.parcauto.dto.RapportVehiculeDTO;
import main.java.com.miage.parcauto.dto.BilanFlotteDTO;
import main.java.com.miage.parcauto.dto.TcoVehiculeDTO;
import main.java.com.miage.parcauto.exception.OperationFailedException;
import main.java.com.miage.parcauto.exception.ReportGenerationException;
import main.java.com.miage.parcauto.exception.VehiculeNotFoundException;

/**
 * Service pour la génération et l'exportation de rapports.
 */
public interface ReportingService {

    /**
     * Génère un rapport détaillé pour un véhicule spécifique.
     * Les données sont récupérées via d'autres services (VehiculeService,
     * EntretienService, MissionService).
     *
     * @param idVehicule L'identifiant du véhicule.
     * @return Un RapportVehiculeDTO contenant toutes les informations pertinentes.
     * @throws VehiculeNotFoundException Si le véhicule n'est pas trouvé.
     * @throws OperationFailedException  Si une erreur technique survient lors de la
     *                                   collecte des données.
     */
    RapportVehiculeDTO genererRapportVehicule(Integer idVehicule)
            throws VehiculeNotFoundException, OperationFailedException;

    /**
     * Génère un bilan global de la flotte.
     * Les données sont récupérées via d'autres services.
     * 
     * @return Un BilanFlotteDTO.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    BilanFlotteDTO genererBilanFlotte() throws OperationFailedException;

    /**
     * Exporte un rapport (préalablement généré et stocké dans un RapportDTO) au
     * format PDF.
     *
     * @param rapportDTO Le DTO contenant les données du rapport à exporter.
     * @return Un tableau d'octets représentant le fichier PDF.
     * @throws ReportGenerationException Si une erreur survient lors de la
     *                                   génération du PDF.
     */
    byte[] exporterRapportPDF(RapportDTO rapportDTO) throws ReportGenerationException;

    /**
     * Exporte un rapport (préalablement généré et stocké dans un RapportDTO) au
     * format Excel.
     *
     * @param rapportDTO Le DTO contenant les données du rapport à exporter.
     * @return Un tableau d'octets représentant le fichier Excel.
     * @throws ReportGenerationException Si une erreur survient lors de la
     *                                   génération du fichier Excel.
     */
    byte[] exporterRapportExcel(RapportDTO rapportDTO) throws ReportGenerationException;
}