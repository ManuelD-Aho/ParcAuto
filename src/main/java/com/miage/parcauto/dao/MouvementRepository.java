package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.finance.Mouvement;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface de repository pour la gestion des mouvements financiers.
 * Étend l'interface Repository générique avec des méthodes spécifiques aux
 * mouvements.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public interface MouvementRepository extends Repository<Mouvement, Integer> {

    /**
     * Trouve tous les mouvements associés à un compte spécifique.
     *
     * @param idCompte L'identifiant du compte
     * @return Liste des mouvements associés au compte
     */
    List<Mouvement> findAllByCompte(Integer idCompte);

    /**
     * Trouve tous les mouvements dans une période donnée.
     *
     * @param dateDebut La date de début de la période
     * @param dateFin   La date de fin de la période
     * @return Liste des mouvements dans la période spécifiée
     */
    List<Mouvement> findAllByPeriode(LocalDate dateDebut, LocalDate dateFin);

    /**
     * Trouve tous les mouvements d'un certain type.
     *
     * @param typeMouvement Le type de mouvement à rechercher
     * @return Liste des mouvements du type spécifié
     */
    List<Mouvement> findAllByType(String typeMouvement);

    /**
     * Trouve tous les mouvements dont le montant est supérieur à une valeur donnée.
     *
     * @param montant Le montant minimum
     * @return Liste des mouvements avec un montant supérieur
     */
    List<Mouvement> findAllByMontantGreaterThan(BigDecimal montant);

    /**
     * Trouve tous les mouvements associés à une mission spécifique.
     *
     * @param idMission L'identifiant de la mission
     * @return Liste des mouvements associés à la mission
     */
    List<Mouvement> findAllByMission(Integer idMission);

    /**
     * Trouve tous les mouvements associés à un entretien spécifique.
     *
     * @param idEntretien L'identifiant de l'entretien
     * @return Liste des mouvements associés à l'entretien
     */
    List<Mouvement> findAllByEntretien(Integer idEntretien);

    /**
     * Calcule le solde total des mouvements pour une période donnée.
     *
     * @param dateDebut La date de début de la période
     * @param dateFin   La date de fin de la période
     * @return Le solde total des mouvements
     */
    BigDecimal calculerSoldePeriode(LocalDate dateDebut, LocalDate dateFin);

    /**
     * Calcule le total des dépenses par catégorie pour une période donnée.
     *
     * @param dateDebut La date de début de la période
     * @param dateFin   La date de fin de la période
     * @return Une liste de paires (catégorie, montant total)
     */
    List<Object[]> calculerDepensesParCategorie(LocalDate dateDebut, LocalDate dateFin);
}
