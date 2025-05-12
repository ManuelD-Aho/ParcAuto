package main.java.com.miage.parcauto.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;

import main.java.com.miage.parcauto.dao.FinanceDao;
import main.java.com.miage.parcauto.dao.FinanceDao.AlerteAssurance;
import main.java.com.miage.parcauto.dao.FinanceDao.AlerteEntretien;
import main.java.com.miage.parcauto.dao.FinanceDao.BilanFinancier;
import main.java.com.miage.parcauto.dao.FinanceDao.BilanMensuel;
import main.java.com.miage.parcauto.dao.FinanceDao.RentabiliteVehicule;
import main.java.com.miage.parcauto.dao.FinanceDao.TCOVehicule;

/**
 * Service métier pour la gestion financière du parc automobile.
 * Sert d'interface entre les contrôleurs JavaFX et la couche DAO.
 * Toutes les méthodes sont documentées et relaient les exceptions SQL.
 *
 */
public class FinanceService {
    private final FinanceDao financeDao;

    /**
     * Constructeur par défaut.
     */
    public FinanceService() {
        this.financeDao = new FinanceDao();
    }

    /**
     * Constructeur pour l'injection de dépendances (tests).
     * 
     * @param financeDao DAO à utiliser
     */
    public FinanceService(FinanceDao financeDao) {
        this.financeDao = financeDao;
    }

    /**
     * Calcule le bilan financier sur une période donnée.
     * 
     * @param debut Date de début
     * @param fin   Date de fin
     * @return BilanFinancier
     * @throws SQLException en cas d'erreur SQL
     */
    public BilanFinancier getBilanFinancier(LocalDateTime debut, LocalDateTime fin) throws SQLException {
        return financeDao.calculerBilanFinancier(debut, fin);
    }

    /**
     * Calcule l'évolution mensuelle des finances pour une année.
     * 
     * @param annee Année
     * @return Map<Month, BilanMensuel>
     * @throws SQLException en cas d'erreur SQL
     */
    public Map<Month, BilanMensuel> getEvolutionMensuelle(int annee) throws SQLException {
        return financeDao.calculerEvolutionMensuelle(annee);
    }

    /**
     * Calcule la répartition budgétaire pour une année.
     * 
     * @param annee Année
     * @return Map<String, BigDecimal>
     * @throws SQLException en cas d'erreur SQL
     */
    public Map<String, BigDecimal> getRepartitionBudgetaire(int annee) throws SQLException {
        return financeDao.calculerRepartitionBudgetaire(annee);
    }

    /**
     * Calcule le coût total de possession (TCO) d'un véhicule.
     * 
     * @param idVehicule identifiant du véhicule
     * @return TCOVehicule
     * @throws SQLException en cas d'erreur SQL
     */
    public TCOVehicule getTCOVehicule(int idVehicule) throws SQLException {
        return financeDao.calculerCoutTotalPossession(idVehicule);
    }

    /**
     * Génère le rapport de rentabilité des véhicules pour une année.
     * 
     * @param annee Année
     * @return Liste de RentabiliteVehicule
     * @throws SQLException en cas d'erreur SQL
     */
    public List<RentabiliteVehicule> getRapportRentabilite(int annee) throws SQLException {
        return financeDao.genererRapportRentabilite(annee);
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