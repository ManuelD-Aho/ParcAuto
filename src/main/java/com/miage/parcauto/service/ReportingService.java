package main.java.com.miage.parcauto.service;

import main.java.com.miage.parcauto.dao.EntretienRepository;
import main.java.com.miage.parcauto.dao.EntretienRepositoryImpl;
import main.java.com.miage.parcauto.dao.FinanceRepository;
import main.java.com.miage.parcauto.dao.FinanceRepositoryImpl;
import main.java.com.miage.parcauto.dao.MissionRepository;
import main.java.com.miage.parcauto.dao.MissionRepositoryImpl;
import main.java.com.miage.parcauto.dao.VehiculeRepository;
import main.java.com.miage.parcauto.dao.VehiculeRepositoryImpl;
import main.java.com.miage.parcauto.dto.BilanFinancierDTO;
import main.java.com.miage.parcauto.dto.EntretienDTO;
import main.java.com.miage.parcauto.dto.MissionDTO;
import main.java.com.miage.parcauto.dto.RapportDTO;
import main.java.com.miage.parcauto.dto.RapportVehiculeDTO;
import main.java.com.miage.parcauto.dto.TcoVehiculeDTO;
import main.java.com.miage.parcauto.dto.VehiculeDTO;
import main.java.com.miage.parcauto.exception.DatabaseException;
import main.java.com.miage.parcauto.exception.FinanceNotFoundException;
import main.java.com.miage.parcauto.exception.VehiculeNotFoundException;
import main.java.com.miage.parcauto.mapper.EntretienMapper;
import main.java.com.miage.parcauto.mapper.MissionMapper;
import main.java.com.miage.parcauto.mapper.VehiculeMapper;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;
import main.java.com.miage.parcauto.model.vehicule.EtatVoiture;
import main.java.com.miage.parcauto.model.entretien.Entretien;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service de génération de rapports refactorisé.
 * Utilise l'architecture Repository et DTO pour générer des rapports.
 *
 * @author MIAGE Holding
 * @version 1.1
 */
public class ReportingService {

    private static final Logger LOGGER = Logger.getLogger(ReportingService.class.getName());

    // Répertoire pour l'export des rapports
    private static final String REPORTS_DIRECTORY = "./reports";

    private final VehiculeRepository vehiculeRepository;
    private final MissionRepository missionRepository;
    private final EntretienRepository entretienRepository;
    private final FinanceRepository financeRepository;
    private final FinanceService financeService;

    /**
     * Constructeur par défaut.
     */
    public ReportingService() {
        this.vehiculeRepository = new VehiculeRepositoryImpl();
        this.missionRepository = new MissionRepositoryImpl();
        this.entretienRepository = new EntretienRepositoryImpl();
        this.financeRepository = new FinanceRepositoryImpl();
        this.financeService = new FinanceService();

        // Initialisation du répertoire des rapports
        initializeReportsDirectory();
    }

    /**
     * Constructeur avec injection de dépendance pour les tests.
     *
     * @param vehiculeRepository  Repository des véhicules à utiliser
     * @param missionRepository   Repository des missions à utiliser
     * @param entretienRepository Repository des entretiens à utiliser
     * @param financeRepository   Repository des finances à utiliser
     * @param financeService      Service des finances à utiliser
     */
    public ReportingService(VehiculeRepository vehiculeRepository,
            MissionRepository missionRepository,
            EntretienRepository entretienRepository,
            FinanceRepository financeRepository,
            FinanceService financeService) {
        this.vehiculeRepository = vehiculeRepository;
        this.missionRepository = missionRepository;
        this.entretienRepository = entretienRepository;
        this.financeRepository = financeRepository;
        this.financeService = financeService;

        // Initialisation du répertoire des rapports
        initializeReportsDirectory();
    }

    /**
     * Initialise le répertoire des rapports.
     */
    private void initializeReportsDirectory() {
        try {
            Files.createDirectories(Paths.get(REPORTS_DIRECTORY));
            LOGGER.info("Répertoire des rapports créé ou vérifié: " + REPORTS_DIRECTORY);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création du répertoire des rapports", e);
        }
    }

    /**
     * Génère un rapport complet sur un véhicule.
     *
     * @param idVehicule ID du véhicule
     * @return DTO du rapport ou null si erreur
     */
    public RapportVehiculeDTO genererRapportVehicule(Integer idVehicule) {
        RapportVehiculeDTO rapport = new RapportVehiculeDTO();
        rapport.setIdVehicule(idVehicule);

        try {
            // Récupération du véhicule
            Optional<Vehicule> vehiculeOpt = vehiculeRepository.findById(idVehicule);
            if (!vehiculeOpt.isPresent()) {
                rapport.addErreur("Véhicule non trouvé avec l'ID: " + idVehicule);
                return rapport;
            }

            Vehicule vehicule = vehiculeOpt.get();
            VehiculeDTO vehiculeDTO = VehiculeMapper.toDTO(vehicule);

            // Informations de base du véhicule
            rapport.setImmatriculation(vehiculeDTO.getImmatriculation());
            rapport.setMarque(vehiculeDTO.getMarque());
            rapport.setModele(vehiculeDTO.getModele());
            rapport.setKilometrage(vehiculeDTO.getKilometrage());
            rapport.setDateMiseEnService(vehiculeDTO.getDateMiseEnService());
            rapport.setEtat(vehiculeDTO.getEtat().toString());
            rapport.setValeurAcquisition(vehiculeDTO.getPrixAchat());
            rapport.setValeurResiduelle(calculerValeurResiduelle(vehiculeDTO));

            // Récupération des missions associées
            List<MissionDTO> missions = missionRepository.findByVehicule(idVehicule)
                    .stream()
                    .map(MissionMapper::toDTO)
                    .collect(Collectors.toList());

            rapport.setNombreMissions(missions.size());
            rapport.setDernieresMissions(missions.stream()
                    .sorted((m1, m2) -> m2.getDateDepart().compareTo(m1.getDateDepart()))
                    .limit(5)
                    .collect(Collectors.toList()));

            // Récupération des entretiens
            List<EntretienDTO> entretiens = entretienRepository.findByVehicule(idVehicule)
                    .stream()
                    .map(EntretienMapper::toDTO)
                    .collect(Collectors.toList());

            rapport.setNombreEntretiens(entretiens.size());
            rapport.setHistoriquePrevEntretien(entretiens);

            // Calcul du coût total des entretiens
            BigDecimal coutTotalEntretiens = entretiens.stream()
                    .filter(e -> e.getCout() != null)
                    .map(EntretienDTO::getCout)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            rapport.setCoutTotalEntretiens(coutTotalEntretiens);

            // Entretiens nécessaires
            rapport.setEntretiensNecessaires(entretiens.stream()
                    .filter(e -> e.getStatut().equals("PLANIFIE"))
                    .collect(Collectors.toList()));

            // Statistiques d'utilisation
            LocalDate dateMiseEnService = vehiculeDTO.getDateMiseEnService();
            if (dateMiseEnService != null) {
                long joursDepuisAcquisition = ChronoUnit.DAYS.between(dateMiseEnService, LocalDate.now());
                rapport.setJoursDepuisAcquisition((int) joursDepuisAcquisition);

                // Calcul du taux d'utilisation (jours en mission / jours depuis acquisition)
                long joursEnMission = missions.stream()
                        .mapToLong(m -> {
                            LocalDateTime debut = m.getDateDepart();
                            LocalDateTime fin = m.getDateRetourReelle() != null ? m.getDateRetourReelle()
                                    : (m.getDateRetourPrevue() != null ? m.getDateRetourPrevue() : LocalDateTime.now());
                            return ChronoUnit.DAYS.between(debut, fin);
                        })
                        .sum();

                double tauxUtilisation = joursDepuisAcquisition > 0
                        ? (double) joursEnMission / joursDepuisAcquisition * 100
                        : 0;
                rapport.setTauxUtilisation(Math.min(100, tauxUtilisation)); // Limite à 100%
            }

            // TCO du véhicule
            try {
                TcoVehiculeDTO tco = financeService.getTCOVehicule(idVehicule);

                // Répartition des coûts
                Map<String, BigDecimal> repartitionCouts = new HashMap<>();
                repartitionCouts.put("Acquisition", tco.getCoutAcquisition().subtract(tco.getValeurResiduelle()));
                repartitionCouts.put("Entretien", tco.getCoutEntretien());
                repartitionCouts.put("Carburant", tco.getCoutCarburant());
                repartitionCouts.put("Assurance", tco.getCoutAssurance());
                repartitionCouts.put("Autres", tco.getCoutAutres());

                rapport.setRepartitionCouts(repartitionCouts);
                rapport.addDonnee("tcoTotal", tco.getTcoTotal());
                rapport.addDonnee("tcoMensuel", tco.getTcoMensuel());
                rapport.addDonnee("tcoParKm", tco.getTcoParKm());
            } catch (FinanceNotFoundException | DatabaseException e) {
                LOGGER.log(Level.WARNING, "Impossible de récupérer le TCO du véhicule: " + idVehicule, e);
                rapport.addErreur("Données TCO indisponibles: " + e.getMessage());
            }

            return rapport;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la génération du rapport pour le véhicule ID: " + idVehicule, e);
            rapport.addErreur("Erreur lors de la génération du rapport: " + e.getMessage());
            return rapport;
        }
    }

    /**
     * Calcule la valeur résiduelle d'un véhicule.
     * 
     * @param vehicule DTO du véhicule
     * @return la valeur résiduelle calculée
     */
    private BigDecimal calculerValeurResiduelle(VehiculeDTO vehicule) {
        if (vehicule == null || vehicule.getPrixAchat() == null || vehicule.getDateMiseEnService() == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal prixAchat = vehicule.getPrixAchat();
        LocalDate dateMiseEnService = vehicule.getDateMiseEnService();
        int anneesEcoulees = (int) ChronoUnit.YEARS.between(dateMiseEnService, LocalDate.now());

        // Formule simplifiée: 20% de dépréciation par an, avec un minimum de 10% de la
        // valeur initiale
        double tauxDepreciation = Math.min(0.9, anneesEcoulees * 0.2); // Maximum 90% de dépréciation
        BigDecimal valeur = prixAchat.multiply(BigDecimal.valueOf(1 - tauxDepreciation));

        return valeur.max(prixAchat.multiply(BigDecimal.valueOf(0.1))); // Minimum 10% de la valeur initiale
    }

    /**
     * Exporte un rapport au format PDF.
     *
     * @param rapport DTO du rapport à exporter
     * @return tableau de bytes contenant le PDF
     * @throws IOException       En cas d'erreur d'entrée/sortie
     * @throws DatabaseException En cas d'erreur de base de données
     */
    public byte[] exporterRapportPDF(RapportDTO rapport) throws IOException, DatabaseException {
        try {
            // Implémenter l'export PDF...
            throw new UnsupportedOperationException("Export PDF non implémenté");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'export du rapport en PDF", e);
            throw new DatabaseException("Impossible d'exporter le rapport en PDF", e);
        }
    }

    /**
     * Exporte un rapport au format Excel.
     *
     * @param rapport DTO du rapport à exporter
     * @return tableau de bytes contenant le fichier Excel
     * @throws IOException       En cas d'erreur d'entrée/sortie
     * @throws DatabaseException En cas d'erreur de base de données
     */
    public byte[] exporterRapportExcel(RapportDTO rapport) throws IOException, DatabaseException {
        try {
            // Implémenter l'export Excel...
            throw new UnsupportedOperationException("Export Excel non implémenté");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'export du rapport en Excel", e);
            throw new DatabaseException("Impossible d'exporter le rapport en Excel", e);
        }
    }
}
