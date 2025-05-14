package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.finance.BilanFinancier;
import main.java.com.miage.parcauto.model.finance.CoutEntretien;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository spécifique pour la gestion financière.
 * Fournit des méthodes de reporting et d'analyse des coûts.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public interface FinanceRepository extends Repository<BilanFinancier, Integer> {
    /**
     * Récupère le bilan financier sur une période donnée.
     * 
     * @param debut Date de début
     * @param fin   Date de fin
     * @return Bilan financier de la période
     */
    BilanFinancier getBilanPeriode(LocalDate debut, LocalDate fin);

    /**
     * Récupère le coût d'entretien par véhicule pour une année donnée.
     * 
     * @param annee Année concernée
     * @return Liste des coûts d'entretien par véhicule
     */
    List<CoutEntretien> getCoutEntretienParVehicule(int annee);
}
