package main.java.com.miage.parcauto.service.impl;

import main.java.com.miage.parcauto.dao.*;
import main.java.com.miage.parcauto.dao.impl.*;
import main.java.com.miage.parcauto.dto.BilanFinancierDTO;
import main.java.com.miage.parcauto.dto.CoutEntretienDTO;
import main.java.com.miage.parcauto.dto.TCODTO;
import main.java.com.miage.parcauto.dto.TcoVehiculeDTO;
import main.java.com.miage.parcauto.exception.OperationFailedException;
import main.java.com.miage.parcauto.exception.VehiculeNotFoundException;
import main.java.com.miage.parcauto.model.assurance.Assurance;
import main.java.com.miage.parcauto.model.entretien.Entretien;
import main.java.com.miage.parcauto.model.finance.BilanFinancier; // Modèle à créer
import main.java.com.miage.parcauto.model.finance.CoutEntretienParVehicule; // Modèle à créer
import main.java.com.miage.parcauto.model.mission.DepenseMission;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;
import main.java.com.miage.parcauto.service.FinanceReportingService;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service de reporting financier.
 */
public class FinanceReportingServiceImpl implements FinanceReportingService {

    private final FinanceRepository financeRepository;
    private final VehiculeRepository vehiculeRepository;
    private final EntretienRepository entretienRepository;
    private final MissionRepository missionRepository;
    private final DepenseMissionRepository depenseMissionRepository;
    private final AssuranceRepository assuranceRepository;
    private final CouvrirRepository couvrirRepository;


    /**
     * Constructeur par défaut.
     */
    public FinanceReportingServiceImpl() {
        this.financeRepository = new FinanceRepositoryImpl();
        this.vehiculeRepository = new VehiculeRepositoryImpl();
        this.entretienRepository = new EntretienRepositoryImpl();
        this.missionRepository = new MissionRepositoryImpl();
        this.depenseMissionRepository = new DepenseMissionRepositoryImpl();
        this.assuranceRepository = new AssuranceRepositoryImpl();
        this.couvrirRepository = new CouvrirRepositoryImpl();
    }

    /**
     * Constructeur avec injection de dépendances.
     */
    public FinanceReportingServiceImpl(FinanceRepository financeRepository, VehiculeRepository vehiculeRepository,
                                       EntretienRepository entretienRepository, MissionRepository missionRepository,
                                       DepenseMissionRepository depenseMissionRepository, AssuranceRepository assuranceRepository,
                                       CouvrirRepository couvrirRepository) {
        this.financeRepository = financeRepository;
        this.vehiculeRepository = vehiculeRepository;
        this.entretienRepository = entretienRepository;
        this.missionRepository = missionRepository;
        this.depenseMissionRepository = depenseMissionRepository;
        this.assuranceRepository = assuranceRepository;
        this.couvrirRepository = couvrirRepository;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public BilanFinancierDTO genererBilanFinancierPeriode(LocalDate dateDebut, LocalDate dateFin) throws OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            BilanFinancier bilan = financeRepository.getBilanPeriode(conn, dateDebut, dateFin);
            // Mapper BilanFinancier (entité/modèle) vers BilanFinancierDTO
            BilanFinancierDTO dto = new BilanFinancierDTO();
            dto.setDateDebut(bilan.getDateDebut());
            dto.setDateFin(bilan.getDateFin());
            dto.setTotalRevenus(bilan.getTotalRevenus());
            dto.setTotalDepenses(bilan.getTotalDepenses());
            dto.setProfitOuPerte(bilan.getProfitOuPerte());
            // Remplir avec d'autres détails si nécessaire
            return dto;
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la génération du bilan financier.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CoutEntretienDTO> getCoutsEntretiensParVehiculeAnnee(int annee) throws OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            List<CoutEntretienParVehicule> couts = financeRepository.getCoutEntretienParVehiculeAnnee(conn, annee);
            // Mapper List<CoutEntretienParVehicule> vers List<CoutEntretienDTO>
            return couts.stream().map(cepv -> {
                CoutEntretienDTO dto = new CoutEntretienDTO();
                dto.setIdVehicule(cepv.getIdVehicule());
                dto.setImmatriculationVehicule(cepv.getImmatriculationVehicule());
                dto.setAnnee(cepv.getAnnee());
                dto.setTotalCoutsEntretien(cepv.getTotalCoutsEntretien());
                return dto;
            }).collect(Collectors.toList());
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors de la récupération des coûts d'entretien par véhicule.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TcoVehiculeDTO calculerTCOVehicule(Integer idVehicule) throws VehiculeNotFoundException, OperationFailedException {
        Connection conn = null;
        try {
            conn = DbUtil.getConnection();
            Vehicule vehicule = vehiculeRepository.findById(conn, idVehicule)
                    .orElseThrow(() -> new VehiculeNotFoundException("Véhicule non trouvé avec l'ID: " + idVehicule));

            TcoVehiculeDTO tco = new TcoVehiculeDTO();
            tco.setIdVehicule(idVehicule);
            tco.setImmatriculation(vehicule.getImmatriculation());
            tco.setMarque(vehicule.getMarque());
            tco.setModele(vehicule.getModele());

            BigDecimal coutAchat = vehicule.getPrixVehicule() != null ? vehicule.getPrixVehicule() : BigDecimal.ZERO;
            tco.setCoutAchat(coutAchat);

            List<Entretien> entretiens = entretienRepository.findByVehiculeId(conn, idVehicule);
            BigDecimal coutTotalEntretiens = entretiens.stream()
                    .filter(e -> e.getCoutReel() != null)
                    .map(Entretien::getCoutReel)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            tco.setCoutTotalEntretiens(coutTotalEntretiens);

            List<DepenseMission> depensesMissions = depenseMissionRepository.findByVehiculeId(conn,idVehicule); // Méthode à ajouter à DepenseMissionRepository
            BigDecimal coutTotalCarburant = depensesMissions.stream()
                    .filter(dm -> dm.getNature() == main.java.com.miage.parcauto.model.mission.NatureDepenseMission.CARBURANT && dm.getMontant() != null)
                    .map(DepenseMission::getMontant)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            tco.setCoutTotalCarburant(coutTotalCarburant);

            BigDecimal coutTotalAutresDepensesMission = depensesMissions.stream()
                    .filter(dm -> dm.getNature() != main.java.com.miage.parcauto.model.mission.NatureDepenseMission.CARBURANT && dm.getMontant() != null)
                    .map(DepenseMission::getMontant)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            tco.setCoutTotalAutresDepenses(coutTotalAutresDepensesMission);


            List<Assurance> assurances = couvrirRepository.findAssurancesByIdVehicule(conn, idVehicule);
            BigDecimal coutTotalAssurances = assurances.stream()
                    .filter(a -> a.getCoutAssurance() != null)
                    .map(Assurance::getCoutAssurance)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            tco.setCoutTotalAssurances(coutTotalAssurances);

            // Calcul de la dépréciation (simpliste, à affiner)
            // Supposons une valeur résiduelle nulle et une durée d'amortissement à calculer
            BigDecimal depreciation = coutAchat; // Simpliste
            if (vehicule.getDateAcquisition() != null) {
                long ageEnMois = ChronoUnit.MONTHS.between(vehicule.getDateAcquisition(), LocalDateTime.now());
                if (ageEnMois > 0 && vehicule.getDateAmortissement() != null) { // date_ammortissement est la date de fin d'ammortissement
                    long dureeAmortissementMois = ChronoUnit.MONTHS.between(vehicule.getDateAcquisition(), vehicule.getDateAmortissement());
                    if (dureeAmortissementMois > 0) {
                        depreciation = coutAchat.multiply(BigDecimal.valueOf(Math.min(ageEnMois, dureeAmortissementMois)))
                                .divide(BigDecimal.valueOf(dureeAmortissementMois), 2, BigDecimal.ROUND_HALF_UP);
                    }
                }
            }
            tco.setDepreciation(depreciation);


            BigDecimal tcoTotal = coutAchat.add(coutTotalEntretiens).add(coutTotalCarburant).add(coutTotalAutresDepensesMission).add(coutTotalAssurances); // Ou coutAchat est remplacé par dépréciation pour une période
            // Si on considère le TCO sur la période d'utilisation, la dépréciation est un coût, pas le coût d'achat.
            // Pour un TCO "depuis acquisition", on peut prendre coutAchat.
            // Pour un TCO "périodique", on prend la dépréciation sur la période.
            // Le modèle actuel semble plus orienté TCO depuis acquisition.
            tco.setTcoTotal(tcoTotal);

            if (vehicule.getKmActuels() != null && vehicule.getKmActuels() > 0) {
                tco.setCoutParKm(tcoTotal.divide(BigDecimal.valueOf(vehicule.getKmActuels()), 2, BigDecimal.ROUND_HALF_UP));
            } else {
                tco.setCoutParKm(BigDecimal.ZERO);
            }

            return tco;
        } catch (SQLException e) {
            throw new OperationFailedException("Erreur technique lors du calcul du TCO pour le véhicule.", e);
        } finally {
            DbUtil.close(conn);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TCODTO calculerTCOFlotte(LocalDate dateDebut, LocalDate dateFin) throws OperationFailedException {
        // Cette méthode nécessiterait d'agréger les TCO de tous les véhicules
        // ou de faire des requêtes SQL agrégées complexes.
        // Pour l'instant, une implémentation simplifiée ou un placeholder.
        TCODTO tcoFlotte = new TCODTO();
        tcoFlotte.setDateDebut(dateDebut);
        tcoFlotte.setDateFin(dateFin);
        tcoFlotte.setNombreVehicules(0); // À calculer
        tcoFlotte.setCoutTotalAchats(BigDecimal.ZERO); // À calculer
        tcoFlotte.setCoutTotalEntretiens(BigDecimal.ZERO); // À calculer
        tcoFlotte.setCoutTotalCarburant(BigDecimal.ZERO); // À calculer
        tcoFlotte.setCoutTotalAssurances(BigDecimal.ZERO); // À calculer
        tcoFlotte.setDepreciationTotale(BigDecimal.ZERO); // À calculer
        tcoFlotte.setTcoGlobalFlotte(BigDecimal.ZERO); // À calculer
        tcoFlotte.setCoutMoyenParKmFlotte(BigDecimal.ZERO); // À calculer

        // Logique d'itération sur tous les véhicules et d'agrégation des coûts sur la période.
        // Ou requêtes SQL spécifiques pour sommer les coûts par catégorie sur la période.

        System.err.println("calculerTCOFlotte n'est pas encore pleinement implémenté avec une logique d'agrégation détaillée.");
        return tcoFlotte; // Placeholder
    }
}