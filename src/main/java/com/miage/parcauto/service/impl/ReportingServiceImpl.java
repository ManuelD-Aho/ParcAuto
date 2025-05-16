package main.java.com.miage.parcauto.service.impl;

import main.java.com.miage.parcauto.dto.*;
import main.java.com.miage.parcauto.exception.OperationFailedException;
import main.java.com.miage.parcauto.exception.ReportGenerationException;
import main.java.com.miage.parcauto.exception.VehiculeNotFoundException;
import main.java.com.miage.parcauto.service.*; // Importer les autres services nécessaires

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service de génération et d'exportation de rapports.
 */
public class ReportingServiceImpl implements ReportingService {

    private final VehiculeService vehiculeService;
    private final EntretienService entretienService;
    private final MissionService missionService;
    // private final FinanceReportingService financeReportingService; // Si TCO est inclus dans RapportVehiculeDTO

    /**
     * Constructeur par défaut.
     */
    public ReportingServiceImpl() {
        // Pour une application réelle, ces services seraient injectés.
        // L'instanciation directe ici peut créer des chaînes de dépendance complexes ou des singletons non gérés.
        this.vehiculeService = new VehiculeServiceImpl();
        this.entretienService = new EntretienServiceImpl();
        this.missionService = new MissionServiceImpl();
        // this.financeReportingService = new FinanceReportingServiceImpl();
    }

    /**
     * Constructeur avec injection de dépendances.
     * @param vehiculeService Service de gestion des véhicules.
     * @param entretienService Service de gestion des entretiens.
     * @param missionService Service de gestion des missions.
     */
    public ReportingServiceImpl(VehiculeService vehiculeService, EntretienService entretienService, MissionService missionService) {
        this.vehiculeService = vehiculeService;
        this.entretienService = entretienService;
        this.missionService = missionService;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public RapportVehiculeDTO genererRapportVehicule(Integer idVehicule) throws VehiculeNotFoundException, OperationFailedException {
        VehiculeDTO vehicule = vehiculeService.getVehiculeById(idVehicule)
                .orElseThrow(() -> new VehiculeNotFoundException("Véhicule non trouvé pour le rapport: ID " + idVehicule));

        RapportVehiculeDTO rapport = new RapportVehiculeDTO();
        rapport.setVehicule(vehicule);

        List<EntretienDTO> entretiens = entretienService.getEntretiensByVehiculeId(idVehicule);
        rapport.setEntretiens(entretiens);

        List<MissionDTO> missions = missionService.getMissionsByVehiculeId(idVehicule);
        rapport.setMissions(missions);

        // Potentiellement, ajouter les infos TCO ici via financeReportingService.calculerTCOVehicule(idVehicule)
        // TcoVehiculeDTO tco = financeReportingService.calculerTCOVehicule(idVehicule);
        // rapport.setTco(tco);

        return rapport;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BilanFlotteDTO genererBilanFlotte() throws OperationFailedException {
        BilanFlotteDTO bilan = new BilanFlotteDTO();

        List<VehiculeDTO> tousLesVehicules = vehiculeService.getAllVehicules();
        bilan.setNombreTotalVehicules(tousLesVehicules.size());

        long disponibles = tousLesVehicules.stream().filter(v -> "Disponible".equalsIgnoreCase(v.getLibelleEtatVoiture())).count();
        bilan.setNombreVehiculesDisponibles((int) disponibles);

        long enMission = tousLesVehicules.stream().filter(v -> "En mission".equalsIgnoreCase(v.getLibelleEtatVoiture())).count();
        bilan.setNombreVehiculesEnMission((int) enMission);

        long enEntretien = tousLesVehicules.stream().filter(v -> "En entretien".equalsIgnoreCase(v.getLibelleEtatVoiture())).count();
        bilan.setNombreVehiculesEnEntretien((int) enEntretien);

        // Calculer d'autres statistiques agrégées si nécessaire
        // bilan.setCoutTotalEntretiensMoisEnCours(...);
        // bilan.setKilometrageTotalFlotte(...);

        return bilan;
    }

    private String formatRapportVehiculeToText(RapportVehiculeDTO rapportVehicule) {
        StringBuilder sb = new StringBuilder();
        VehiculeDTO v = rapportVehicule.getVehicule();
        sb.append("RAPPORT VÉHICULE\n");
        sb.append("====================================\n");
        sb.append("ID: ").append(v.getIdVehicule()).append("\n");
        sb.append("Immatriculation: ").append(v.getImmatriculation()).append("\n");
        sb.append("Marque: ").append(v.getMarque()).append(" Modèle: ").append(v.getModele()).append("\n");
        sb.append("État: ").append(v.getLibelleEtatVoiture()).append(" (depuis ").append(v.getDateEtat()).append(")\n");
        sb.append("Kilométrage actuel: ").append(v.getKmActuels()).append(" km\n\n");

        sb.append("Entretiens:\n");
        if (rapportVehicule.getEntretiens() == null || rapportVehicule.getEntretiens().isEmpty()) {
            sb.append("  Aucun entretien enregistré.\n");
        } else {
            for (EntretienDTO e : rapportVehicule.getEntretiens()) {
                sb.append("  - ID Entretien: ").append(e.getIdEntretien())
                        .append(", Date Entrée: ").append(e.getDateEntree())
                        .append(", Motif: ").append(e.getMotif())
                        .append(", Coût Réel: ").append(e.getCoutReel() != null ? e.getCoutReel() : "N/A")
                        .append(", Statut: ").append(e.getStatutOt()).append("\n");
            }
        }
        sb.append("\n");

        sb.append("Missions:\n");
        if (rapportVehicule.getMissions() == null || rapportVehicule.getMissions().isEmpty()) {
            sb.append("  Aucune mission enregistrée.\n");
        } else {
            for (MissionDTO m : rapportVehicule.getMissions()) {
                sb.append("  - ID Mission: ").append(m.getIdMission())
                        .append(", Libellé: ").append(m.getLibelle())
                        .append(", Début: ").append(m.getDateDebut())
                        .append(", Fin Prévue: ").append(m.getDateFinPrevue())
                        .append(", Statut: ").append(m.getStatut()).append("\n");
            }
        }
        // Ajouter TCO si disponible dans le rapportDTO
        return sb.toString();
    }

    private String formatBilanFlotteToText(BilanFlotteDTO bilanFlotte) {
        StringBuilder sb = new StringBuilder();
        sb.append("BILAN DE LA FLOTTE\n");
        sb.append("====================================\n");
        sb.append("Nombre total de véhicules: ").append(bilanFlotte.getNombreTotalVehicules()).append("\n");
        sb.append("Véhicules disponibles: ").append(bilanFlotte.getNombreVehiculesDisponibles()).append("\n");
        sb.append("Véhicules en mission: ").append(bilanFlotte.getNombreVehiculesEnMission()).append("\n");
        sb.append("Véhicules en entretien: ").append(bilanFlotte.getNombreVehiculesEnEntretien()).append("\n");
        // Ajouter d'autres informations du bilan
        return sb.toString();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] exporterRapportPDF(RapportDTO rapportDTO) throws ReportGenerationException {
        // Sans bibliothèque externe, la génération d'un vrai PDF est très complexe.
        // Nous allons retourner une représentation textuelle du rapport.
        // Pour un vrai PDF, il faudrait intégrer une librairie comme iText ou Apache PDFBox.
        String contenuTexte;
        if (rapportDTO instanceof RapportVehiculeDTO) {
            contenuTexte = formatRapportVehiculeToText((RapportVehiculeDTO) rapportDTO);
        } else if (rapportDTO instanceof BilanFlotteDTO) {
            contenuTexte = formatBilanFlotteToText((BilanFlotteDTO) rapportDTO);
        } else if (rapportDTO instanceof TcoVehiculeDTO) { // Supposons que TcoVehiculeDTO hérite de RapportDTO
            // Créer une méthode de formatage pour TcoVehiculeDTO
            contenuTexte = "Type de rapport TCO non formaté pour PDF texte.";
        }
        else {
            contenuTexte = "Type de rapport non supporté pour l'export PDF simplifié.";
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(baos, true, StandardCharsets.UTF_8)) {
            writer.println("-----------------------------------------");
            writer.println("       RAPPORT (Format PDF Simplifié)    ");
            writer.println("-----------------------------------------");
            writer.println(contenuTexte);
            writer.println("\n\n--- Fin du Rapport ---");
            writer.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new ReportGenerationException("Erreur lors de la génération du contenu texte pour PDF.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] exporterRapportExcel(RapportDTO rapportDTO) throws ReportGenerationException {
        // Sans bibliothèque externe, nous allons générer un format CSV simple.
        // Pour un vrai Excel (XLS/XLSX), il faudrait Apache POI.
        String contenuCsv;
        if (rapportDTO instanceof RapportVehiculeDTO) {
            contenuCsv = formatRapportVehiculeToCsv((RapportVehiculeDTO) rapportDTO);
        } else if (rapportDTO instanceof BilanFlotteDTO) {
            contenuCsv = formatBilanFlotteToCsv((BilanFlotteDTO) rapportDTO);
        } else {
            contenuCsv = "Type de rapport non supporté pour l'export CSV simplifié.";
        }

        return contenuCsv.getBytes(StandardCharsets.UTF_8);
    }

    private String formatRapportVehiculeToCsv(RapportVehiculeDTO rapport) {
        StringBuilder csv = new StringBuilder();
        VehiculeDTO v = rapport.getVehicule();
        // Section Véhicule
        csv.append("Section;Cle;Valeur\n");
        csv.append("Vehicule;ID;").append(v.getIdVehicule()).append("\n");
        csv.append("Vehicule;Immatriculation;").append(v.getImmatriculation()).append("\n");
        csv.append("Vehicule;Marque;").append(v.getMarque()).append("\n");
        csv.append("Vehicule;Modele;").append(v.getModele()).append("\n");
        csv.append("Vehicule;Etat;").append(v.getLibelleEtatVoiture()).append("\n");
        csv.append("Vehicule;Km Actuels;").append(v.getKmActuels()).append("\n\n");

        // Section Entretiens
        csv.append("Section;ID Entretien;Date Entree;Motif;Cout Reel;Statut\n");
        if (rapport.getEntretiens() != null && !rapport.getEntretiens().isEmpty()) {
            for (EntretienDTO e : rapport.getEntretiens()) {
                csv.append("Entretien;").append(e.getIdEntretien()).append(";")
                        .append(e.getDateEntree()).append(";")
                        .append("\"").append(e.getMotif().replace("\"", "\"\"")).append("\";") // Échapper les guillemets
                        .append(e.getCoutReel() != null ? e.getCoutReel() : "").append(";")
                        .append(e.getStatutOt()).append("\n");
            }
        } else {
            csv.append("Entretien;N/A;N/A;Aucun entretien;N/A;N/A\n");
        }
        csv.append("\n");

        // Section Missions
        csv.append("Section;ID Mission;Libelle;Date Debut;Date Fin Prevue;Statut\n");
        if (rapport.getMissions() != null && !rapport.getMissions().isEmpty()) {
            for (MissionDTO m : rapport.getMissions()) {
                csv.append("Mission;").append(m.getIdMission()).append(";")
                        .append("\"").append(m.getLibelle().replace("\"", "\"\"")).append("\";")
                        .append(m.getDateDebut()).append(";")
                        .append(m.getDateFinPrevue()).append(";")
                        .append(m.getStatut()).append("\n");
            }
        } else {
            csv.append("Mission;N/A;Aucune mission;N/A;N/A;N/A\n");
        }
        return csv.toString();
    }

    private String formatBilanFlotteToCsv(BilanFlotteDTO bilan) {
        StringBuilder csv = new StringBuilder();
        csv.append("Indicateur;Valeur\n");
        csv.append("Nombre total de vehicules;").append(bilan.getNombreTotalVehicules()).append("\n");
        csv.append("Vehicules disponibles;").append(bilan.getNombreVehiculesDisponibles()).append("\n");
        csv.append("Vehicules en mission;").append(bilan.getNombreVehiculesEnMission()).append("\n");
        csv.append("Vehicules en entretien;").append(bilan.getNombreVehiculesEnEntretien()).append("\n");
        // Ajouter d'autres indicateurs
        return csv.toString();
    }
}