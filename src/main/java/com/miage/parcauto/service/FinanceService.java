package main.java.com.miage.parcauto.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.com.miage.parcauto.dto.BilanFinancierDTO;
import main.java.com.miage.parcauto.dto.BilanMensuelDTO;
import main.java.com.miage.parcauto.dto.TcoVehiculeDTO;
import main.java.com.miage.parcauto.dto.VehiculeRentabiliteDTO;
import main.java.com.miage.parcauto.exception.DatabaseException;
import main.java.com.miage.parcauto.exception.FinanceNotFoundException;
import main.java.com.miage.parcauto.mapper.FinanceMapper;
import main.java.com.miage.parcauto.model.finance.BilanFinancier;

/**
 * Service métier pour la gestion financière du parc automobile.
 * Sert d'interface entre les contrôleurs JavaFX et la couche Repository.
 * Toutes les méthodes sont documentées et utilisent les DTOs pour le transfert
 * de données.
 *
 * @author MIAGE Holding
 * @version 1.1
 */
public class FinanceService {
    private static final Logger LOGGER = Logger.getLogger(FinanceService.class.getName());

    private final FinanceRepository financeRepository;
    private final FinanceDao financeDao; // Temporaire pendant la migration
    private final ValidationService validationService;

    /**
     * Constructeur par défaut.
     */
    public FinanceService() {
        this.financeRepository = new FinanceRepositoryImpl();
        this.financeDao = new FinanceDao(); // Temporaire pendant la migration
        this.validationService = new ValidationService();
    }

    /**
     * Constructeur pour l'injection de dépendances (tests).
     * 
     * @param financeRepository Repository à utiliser
     * @param financeDao        DAO à utiliser pendant la migration
     * @param validationService Service de validation à utiliser
     */
    public FinanceService(FinanceRepository financeRepository, FinanceDao financeDao,
            ValidationService validationService) {
        this.financeRepository = financeRepository;
        this.financeDao = financeDao;
        this.validationService = validationService;
    }

    /**
     * Calcule le bilan financier sur une période donnée.
     * 
     * @param debut Date de début
     * @param fin   Date de fin
     * @return BilanFinancierDTO
     * @throws DatabaseException en cas d'erreur de base de données
     */
    public BilanFinancierDTO getBilanFinancier(LocalDate debut, LocalDate fin) throws DatabaseException {
        try {
            BilanFinancier bilan = financeRepository.getBilanPeriode(debut, fin);
            return FinanceMapper.toDTO(bilan);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul du bilan financier", e);
            throw new DatabaseException("Impossible de calculer le bilan financier", e);
        }
    }

    /**
     * Calcule l'évolution mensuelle des finances pour une année.
     * 
     * @param annee Année
     * @return Map<Month, BilanMensuelDTO>
     * @throws DatabaseException en cas d'erreur de base de données
     */
    public Map<Month, BilanMensuelDTO> getEvolutionMensuelle(int annee) throws DatabaseException {
        try {
            // Utilisation temporaire du DAO jusqu'à la migration complète
            Map<Month, FinanceDao.BilanMensuel> evolution = financeDao.calculerEvolutionMensuelle(annee);
            return FinanceMapper.toDTO(evolution);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul de l'évolution mensuelle pour l'année: " + annee, e);
            throw new DatabaseException("Impossible de calculer l'évolution mensuelle", e);
        }
    }

    /**
     * Calcule la répartition budgétaire pour une année.
     * 
     * @param annee Année
     * @return Map<String, BigDecimal>
     * @throws DatabaseException en cas d'erreur de base de données
     */
    public Map<String, BigDecimal> getRepartitionBudgetaire(int annee) throws DatabaseException {
        try {
            // Utilisation temporaire du DAO jusqu'à la migration complète
            return financeDao.calculerRepartitionBudgetaire(annee);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul de la répartition budgétaire pour l'année: " + annee, e);
            throw new DatabaseException("Impossible de calculer la répartition budgétaire", e);
        }
    }

    /**
     * Calcule le coût total de possession (TCO) d'un véhicule.
     * 
     * @param idVehicule identifiant du véhicule
     * @return TcoVehiculeDTO
     * @throws DatabaseException        en cas d'erreur de base de données
     * @throws FinanceNotFoundException si le véhicule n'est pas trouvé
     */
    public TcoVehiculeDTO getTCOVehicule(int idVehicule) throws DatabaseException, FinanceNotFoundException {
        try {
            // Utilisation temporaire du DAO jusqu'à la migration complète
            FinanceDao.TCOVehicule tco = financeDao.calculerCoutTotalPossession(idVehicule);
            if (tco == null) {
                throw new FinanceNotFoundException(
                        "Aucune donnée financière trouvée pour le véhicule avec l'ID: " + idVehicule);
            }
            return FinanceMapper.toDTO(tco);
        } catch (FinanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul du TCO du véhicule: " + idVehicule, e);
            throw new DatabaseException("Impossible de calculer le TCO du véhicule", e);
        }
    }

    /**
     * Génère le rapport de rentabilité des véhicules pour une année.
     * 
     * @param annee Année
     * @return Liste de VehiculeRentabiliteDTO
     * @throws DatabaseException en cas d'erreur de base de données
     */
    public List<VehiculeRentabiliteDTO> getRapportRentabilite(int annee) throws DatabaseException {
        try {
            // Utilisation temporaire du DAO jusqu'à la migration complète
            List<FinanceDao.RentabiliteVehicule> rapport = financeDao.genererRapportRentabilite(annee);
            return FinanceMapper.toDTO(rapport);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la génération du rapport de rentabilité pour l'année: " + annee,
                    e);
            throw new DatabaseException("Impossible de générer le rapport de rentabilité", e);
        }
    }

    /**
     * Retourne la liste des alertes d'assurances expirant prochainement.
     * 
     * @param joursAvantExpiration nombre de jours avant expiration
     * @return Liste d'alertes
     * @throws SQLException en cas d'erreur SQL
     */
    public List<AlerteAssurance> getAlertesAssurances(int joursAvantExpiration) throws SQLException {
        return financeDao.verifierAssurancesExpirees(joursAvantExpiration);
    }

    /**
     * Retourne la liste des alertes d'entretiens à prévoir.
     * 
     * @param kmEntretienPreventif seuil kilométrique
     * @return Liste d'alertes
     * @throws SQLException en cas d'erreur SQL
     */
    public List<AlerteEntretien> getAlertesEntretiens(int kmEntretienPreventif) throws SQLException {
        return financeDao.verifierEntretiensNecessaires(kmEntretienPreventif);
    }

    /**
     * Retourne le solde global du parc (toutes périodes).
     * 
     * @return solde global, ou BigDecimal.ZERO en cas d'erreur
     */
    public BigDecimal getSoldeGlobal() {
        try {
            BilanFinancier bilan = getBilanFinancier(null, null);
            return bilan != null ? bilan.getSolde() : BigDecimal.ZERO;
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    // Pas d'implémentation ici car la méthode est abstraite ou à compléter selon le
    // DAO
}