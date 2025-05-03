package com.miage.parcauto.service;

import com.miage.parcauto.dao.FinanceDao;
import com.miage.parcauto.dao.SocieteCompteDao;
import com.miage.parcauto.dao.MouvementDao;
import com.miage.parcauto.dao.VehiculeDao;
import com.miage.parcauto.dao.EntretienDao;
import com.miage.parcauto.model.finance.SocieteCompte;
import com.miage.parcauto.model.finance.Mouvement;
import com.miage.parcauto.model.finance.Mouvement.TypeMouvement;
import com.miage.parcauto.model.entretien.Assurance;
import com.miage.parcauto.model.vehicule.Vehicule;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service de gestion financière.
 * Cette classe implémente la couche service pour toutes les opérations financières.
 * Elle sert d'intermédiaire entre la couche DAO et la couche de présentation (contrôleurs).
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class FinanceService {

    private static final Logger LOGGER = Logger.getLogger(FinanceService.class.getName());

    private final FinanceDao financeDao;
    private final SocieteCompteDao societeCompteDao;
    private final MouvementDao mouvementDao;
    private final VehiculeDao vehiculeDao;
    private final EntretienDao entretienDao;

    /**
     * Constructeur par défaut.
     */
    public FinanceService() {
        this.financeDao = new FinanceDao();
        this.societeCompteDao = new SocieteCompteDao();
        this.mouvementDao = new MouvementDao();
        this.vehiculeDao = new VehiculeDao();
        this.entretienDao = new EntretienDao();
    }

    /**
     * Constructeur avec injection de dépendance pour les tests.
     *
     * @param financeDao Instance de FinanceDao à utiliser
     * @param societeCompteDao Instance de SocieteCompteDao à utiliser
     * @param mouvementDao Instance de MouvementDao à utiliser
     * @param vehiculeDao Instance de VehiculeDao à utiliser
     * @param entretienDao Instance de EntretienDao à utiliser
     */
    public FinanceService(FinanceDao financeDao, SocieteCompteDao societeCompteDao,
                          MouvementDao mouvementDao, VehiculeDao vehiculeDao,
                          EntretienDao entretienDao) {
        this.financeDao = financeDao;
        this.societeCompteDao = societeCompteDao;
        this.mouvementDao = mouvementDao;
        this.vehiculeDao = vehiculeDao;
        this.entretienDao = entretienDao;
    }

    /**
     * Récupère tous les comptes sociétaires.
     *
     * @return Liste de tous les comptes ou liste vide en cas d'erreur
     */
    public List<SocieteCompte> getAllSocieteComptes() {
        try {
            return societeCompteDao.findAll();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les comptes sociétaires", e);
            return Collections.emptyList();
        }
    }

    /**
     * Recherche un compte sociétaire par son ID.
     *
     * @param id ID du compte
     * @return Optional contenant le compte s'il existe
     */
    public Optional<SocieteCompte> getSocieteCompteById(int id) {
        try {
            return societeCompteDao.findById(id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du compte sociétaire par ID: " + id, e);
            return Optional.empty();
        }
    }

    /**
     * Recherche un compte sociétaire par son numéro.
     *
     * @param numero Numéro du compte
     * @return Optional contenant le compte s'il existe
     */
    public Optional<SocieteCompte> getSocieteCompteByNumero(String numero) {
        try {
            return societeCompteDao.findByNumero(numero);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du compte sociétaire par numéro: " + numero, e);
            return Optional.empty();
        }
    }

    /**
     * Récupère les comptes associés à un membre du personnel.
     *
     * @param idPersonnel ID du membre du personnel
     * @return Liste des comptes associés au personnel ou liste vide en cas d'erreur
     */
    public List<SocieteCompte> getSocieteComptesByPersonnel(int idPersonnel) {
        try {
            return Collections.singletonList(societeCompteDao.findByIdPersonnel(idPersonnel).orElse(null));
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des comptes pour le personnel ID: " + idPersonnel, e);
            return Collections.emptyList();
        }
    }

    /**
     * Crée un nouveau compte sociétaire.
     *
     * @param compte Le compte à créer
     * @return Le compte créé avec son ID généré ou null en cas d'erreur
     */
    public SocieteCompte createSocieteCompte(SocieteCompte compte) {
        try {
            // Validation des données
            if (!validateSocieteCompte(compte)) {
                LOGGER.warning("Validation du compte sociétaire échouée");
                return null;
            }

            // Vérifier que le numéro de compte est unique
            if (isNumeroCompteDuplicate(compte.getNumero())) {
                LOGGER.warning("Numéro de compte déjà utilisé: " + compte.getNumero());
                return null;
            }

            return societeCompteDao.create(compte);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création du compte sociétaire", e);
            return null;
        }
    }

    /**
     * Met à jour un compte sociétaire existant.
     *
     * @param compte Le compte à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateSocieteCompte(SocieteCompte compte) {
        try {
            // Validation des données
            if (!validateSocieteCompte(compte) || compte.getIdSocietaire() == null) {
                LOGGER.warning("Validation du compte sociétaire échouée ou ID manquant");
                return false;
            }

            // Vérifier que le compte existe
            Optional<SocieteCompte> existingCompte = getSocieteCompteById(compte.getIdSocietaire());
            if (!existingCompte.isPresent()) {
                LOGGER.warning("Compte sociétaire non trouvé avec l'ID: " + compte.getIdSocietaire());
                return false;
            }

            // Vérifier que le numéro de compte est unique (sauf pour le même compte)
            Optional<SocieteCompte> byNumero = getSocieteCompteByNumero(compte.getNumero());
            if (byNumero.isPresent() && !byNumero.get().getIdSocietaire().equals(compte.getIdSocietaire())) {
                LOGGER.warning("Numéro de compte déjà utilisé par un autre compte: " + compte.getNumero());
                return false;
            }

            return societeCompteDao.update(compte);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du compte sociétaire", e);
            return false;
        }
    }

    /**
     * Supprime un compte sociétaire.
     *
     * @param id ID du compte à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deleteSocieteCompte(int id) {
        try {
            return societeCompteDao.delete(id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression du compte sociétaire: " + id, e);
            return false;
        }
    }

    /**
     * Vérifie si un numéro de compte est déjà utilisé.
     *
     * @param numero Numéro de compte à vérifier
     * @return true si le numéro de compte est déjà utilisé, false sinon
     */
    public boolean isNumeroCompteDuplicate(String numero) {
        return getSocieteCompteByNumero(numero).isPresent();
    }

    /**
     * Récupère les mouvements d'un compte sociétaire.
     *
     * @param idSocietaire ID du compte sociétaire
     * @return Liste des mouvements du compte ou liste vide en cas d'erreur
     */
    public List<Mouvement> getMouvementsBySocieteCompte(int idSocietaire) {
        try {
            return mouvementDao.findBySocietaire(idSocietaire);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des mouvements pour le compte ID: " + idSocietaire, e);
            return Collections.emptyList();
        }
    }

    /**
     * Récupère les mouvements par type pour un compte sociétaire.
     *
     * @param idSocietaire ID du compte sociétaire
     * @param type Type de mouvement
     * @return Liste des mouvements du type spécifié ou liste vide en cas d'erreur
     */
    public List<Mouvement> getMouvementsBySocieteCompteAndType(int idSocietaire, TypeMouvement type) {
        try {
            return mouvementDao.findBySocieteCompteAndType(idSocietaire, type);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des mouvements pour le compte ID: " +
                    idSocietaire + " et type: " + type, e);
            return Collections.emptyList();
        }
    }

    /**
     * Récupère les mouvements par période pour un compte sociétaire.
     *
     * @param idSocietaire ID du compte sociétaire
     * @param debut Date de début de la période
     * @param fin Date de fin de la période
     * @return Liste des mouvements dans cette période ou liste vide en cas d'erreur
     */
    public List<Mouvement> getMouvementsBySocieteCompteAndPeriode(int idSocietaire,
                                                                  LocalDateTime debut, LocalDateTime fin) {
        try {
            // Utiliser findBySocietaireAndPeriode qui est la méthode correcte
            return mouvementDao.findBySocietaireAndPeriode(idSocietaire, debut, fin);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des mouvements pour le compte ID: " +
                    idSocietaire + " et période", e);
            return Collections.emptyList();
        }
    }

    /**
     * Crée un nouveau mouvement financier.
     *
     * @param mouvement Le mouvement à créer
     * @param updateSolde Si true, met à jour le solde du compte sociétaire
     * @return Le mouvement créé avec son ID généré ou null en cas d'erreur
     */
    public Mouvement createMouvement(Mouvement mouvement, boolean updateSolde) {
        try {
            // Validation des données
            if (!validateMouvement(mouvement)) {
                LOGGER.warning("Validation du mouvement échouée");
                return null;
            }

            // Vérifier que le compte sociétaire existe
            Optional<SocieteCompte> societeCompte = getSocieteCompteById(mouvement.getIdSocietaire());
            if (!societeCompte.isPresent()) {
                LOGGER.warning("Compte sociétaire non trouvé avec l'ID: " + mouvement.getIdSocietaire());
                return null;
            }

            // Vérifier le montant disponible pour les retraits et mensualités
            if ((mouvement.getType() == TypeMouvement.Retrait || mouvement.getType() == TypeMouvement.Mensualite) &&
                    societeCompte.get().getSolde().compareTo(mouvement.getMontant()) < 0) {
                LOGGER.warning("Solde insuffisant pour le retrait");
                return null;
            }

            // Créer le mouvement
            Mouvement createdMouvement = mouvementDao.create(mouvement);

            // Mettre à jour le solde du compte si demandé
            if (updateSolde && createdMouvement != null) {
                SocieteCompte compte = societeCompte.get();
                if (mouvement.getType() == TypeMouvement.Depot) {
                    compte.depot(mouvement.getMontant());
                } else {
                    compte.retrait(mouvement.getMontant());
                }

                societeCompteDao.update(compte);
            }

            return createdMouvement;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création du mouvement", e);
            return null;
        }
    }

    /**
     * Effectue un dépôt sur un compte sociétaire.
     *
     * @param idSocietaire ID du compte sociétaire
     * @param montant Montant du dépôt
     * @param description Description du dépôt
     * @return Le mouvement créé avec son ID généré ou null en cas d'erreur
     */
    public Mouvement effectuerDepot(int idSocietaire, BigDecimal montant, String description) {
        // Créer le mouvement
        Mouvement mouvement = new Mouvement();
        mouvement.setIdSocietaire(idSocietaire);
        mouvement.setDate(LocalDateTime.now());
        mouvement.setType(TypeMouvement.Depot);
        mouvement.setMontant(montant);
        mouvement.setDescription(description);

        return createMouvement(mouvement, true);
    }

    /**
     * Effectue un retrait sur un compte sociétaire.
     *
     * @param idSocietaire ID du compte sociétaire
     * @param montant Montant du retrait
     * @param description Description du retrait
     * @return Le mouvement créé avec son ID généré ou null en cas d'erreur
     */
    public Mouvement effectuerRetrait(int idSocietaire, BigDecimal montant, String description) {
        // Créer le mouvement
        Mouvement mouvement = new Mouvement();
        mouvement.setIdSocietaire(idSocietaire);
        mouvement.setDate(LocalDateTime.now());
        mouvement.setType(TypeMouvement.Retrait);
        mouvement.setMontant(montant);
        mouvement.setDescription(description);

        return createMouvement(mouvement, true);
    }

    /**
     * Enregistre un paiement de mensualité pour un crédit véhicule.
     *
     * @param idSocietaire ID du compte sociétaire
     * @param montant Montant de la mensualité
     * @param description Description de la mensualité
     * @return Le mouvement créé avec son ID généré ou null en cas d'erreur
     */
    public Mouvement payerMensualite(int idSocietaire, BigDecimal montant, String description) {
        // Créer le mouvement
        Mouvement mouvement = new Mouvement();
        mouvement.setIdSocietaire(idSocietaire);
        mouvement.setDate(LocalDateTime.now());
        mouvement.setType(TypeMouvement.Mensualite);
        mouvement.setMontant(montant);
        mouvement.setDescription(description);

        return createMouvement(mouvement, true);
    }

    /**
     * Calcule le total des recettes (dépôts) pour une période donnée.
     *
     * @param debut Date de début de la période
     * @param fin Date de fin de la période
     * @return Total des recettes ou BigDecimal.ZERO en cas d'erreur
     */
    public BigDecimal calculerRecettesPeriode(LocalDateTime debut, LocalDateTime fin) {
        try {
            return financeDao.calculerRecettesPeriode(debut, fin);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul des recettes pour la période", e);
            return BigDecimal.ZERO;
        }
    }

    /**
     * Calcule le total des dépenses pour une période donnée.
     *
     * @param debut Date de début de la période
     * @param fin Date de fin de la période
     * @return Total des dépenses ou BigDecimal.ZERO en cas d'erreur
     */
    public BigDecimal calculerDepensesPeriode(LocalDateTime debut, LocalDateTime fin) {
        try {
            return financeDao.calculerDepensesPeriode(debut, fin);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul des dépenses pour la période", e);
            return BigDecimal.ZERO;
        }
    }

    /**
     * Calcule le bilan financier pour une période donnée.
     *
     * @param debut Date de début de la période
     * @param fin Date de fin de la période
     * @return BilanFinancier contenant les détails ou null en cas d'erreur
     */
    public FinanceDao.BilanFinancier calculerBilanFinancier(LocalDateTime debut, LocalDateTime fin) {
        try {
            return financeDao.calculerBilanFinancier(debut, fin);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul du bilan financier pour la période", e);
            return null;
        }
    }

    /**
     * Calcule l'évolution des recettes et dépenses mensuelles sur une année.
     *
     * @param annee Année pour l'analyse
     * @return Map contenant les données d'évolution mensuelles ou map vide en cas d'erreur
     */
    public Map<Month, FinanceDao.BilanMensuel> calculerEvolutionMensuelle(int annee) {
        try {
            return financeDao.calculerEvolutionMensuelle(annee);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul de l'évolution mensuelle pour l'année " + annee, e);
            return Collections.emptyMap();
        }
    }

    /**
     * Calcule le coût total de possession (TCO) pour un véhicule.
     *
     * @param idVehicule ID du véhicule
     * @return TCOVehicule contenant les détails ou null en cas d'erreur
     */
    public FinanceDao.TCOVehicule calculerCoutTotalPossession(int idVehicule) {
        try {
            return financeDao.calculerCoutTotalPossession(idVehicule);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul du coût total de possession pour le véhicule ID: " + idVehicule, e);
            return null;
        }
    }

    /**
     * Calcule les répartitions budgétaires par type de dépense.
     *
     * @param annee Année pour l'analyse
     * @return Map contenant les montants par type de dépense ou map vide en cas d'erreur
     */
    public Map<String, BigDecimal> calculerRepartitionBudgetaire(int annee) {
        try {
            return financeDao.calculerRepartitionBudgetaire(annee);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul de la répartition budgétaire pour l'année " + annee, e);
            return Collections.emptyMap();
        }
    }

    /**
     * Génère un rapport de rentabilité des véhicules.
     *
     * @param annee Année pour le rapport
     * @return Liste des rentabilités des véhicules ou liste vide en cas d'erreur
     */
    public List<FinanceDao.RentabiliteVehicule> genererRapportRentabilite(int annee) {
        try {
            return financeDao.genererRapportRentabilite(annee);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la génération du rapport de rentabilité pour l'année " + annee, e);
            return Collections.emptyList();
        }
    }

    /**
     * Vérifie et alerte sur les contrats d'assurance expirant prochainement.
     *
     * @param joursAvantExpiration Nombre de jours avant expiration pour l'alerte
     * @return Liste des assurances expirant prochainement ou liste vide en cas d'erreur
     */
    public List<FinanceDao.AlerteAssurance> verifierAssurancesExpirees(int joursAvantExpiration) {
        try {
            return financeDao.verifierAssurancesExpirees(joursAvantExpiration);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la vérification des assurances expirées", e);
            return Collections.emptyList();
        }
    }

    /**
     * Calcule le montant mensuel pour un crédit sur 5 ans.
     *
     * @param prixTotal Prix total du véhicule
     * @param tauxAnnuel Taux d'intérêt annuel en pourcentage
     * @return le montant mensuel de la mensualité
     */
    public BigDecimal calculerMensualite(BigDecimal prixTotal, BigDecimal tauxAnnuel) {
        return SocieteCompte.calculerMensualite(prixTotal, tauxAnnuel);
    }

    /**
     * Calcule le solde restant dû après un certain nombre de mensualités payées.
     *
     * @param prixTotal Prix total initial du véhicule
     * @param tauxAnnuel Taux d'intérêt annuel en pourcentage
     * @param mensualitePayees Nombre de mensualités déjà payées
     * @return le montant restant à payer
     */
    public BigDecimal calculerSoldeRestant(BigDecimal prixTotal, BigDecimal tauxAnnuel, int mensualitePayees) {
        return SocieteCompte.calculerSoldeRestant(prixTotal, tauxAnnuel, mensualitePayees);
    }

    /**
     * Génère un tableau d'amortissement pour un crédit véhicule.
     *
     * @param prixTotal Prix total du véhicule
     * @param tauxAnnuel Taux d'intérêt annuel en pourcentage
     * @return Liste des lignes du tableau d'amortissement
     */
    public List<Map<String, Object>> genererTableauAmortissement(BigDecimal prixTotal, BigDecimal tauxAnnuel) {
        List<Map<String, Object>> tableau = new java.util.ArrayList<>();
        BigDecimal mensualite = calculerMensualite(prixTotal, tauxAnnuel);
        BigDecimal capitalRestant = prixTotal;
        BigDecimal tauxMensuel = tauxAnnuel.divide(BigDecimal.valueOf(12 * 100), 10, BigDecimal.ROUND_HALF_UP);

        for (int i = 1; i <= 60; i++) {
            Map<String, Object> ligne = new HashMap<>();
            BigDecimal interets = capitalRestant.multiply(tauxMensuel).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal capitalRembourse = mensualite.subtract(interets);
            capitalRestant = capitalRestant.subtract(capitalRembourse);

            ligne.put("mois", i);
            ligne.put("mensualite", mensualite);
            ligne.put("interets", interets);
            ligne.put("capitalRembourse", capitalRembourse);
            ligne.put("capitalRestant", capitalRestant);

            tableau.add(ligne);
        }

        return tableau;
    }

    /**
     * Génère un bilan financier annuel.
     *
     * @param annee Année pour le bilan
     * @return Map contenant les données du bilan ou map vide en cas d'erreur
     */
    public Map<String, Object> genererBilanAnnuel(int annee) {
        Map<String, Object> bilan = new HashMap<>();

        try {
            // Période de l'année
            LocalDateTime debut = LocalDateTime.of(annee, 1, 1, 0, 0);
            LocalDateTime fin = LocalDateTime.of(annee, 12, 31, 23, 59, 59);

            // Bilan financier
            FinanceDao.BilanFinancier bilanFinancier = calculerBilanFinancier(debut, fin);
            if (bilanFinancier != null) {
                bilan.put("recettes", bilanFinancier.getTotalRecettes());
                bilan.put("depenses", bilanFinancier.getTotalDepenses());
                bilan.put("solde", bilanFinancier.getSolde());
                bilan.put("marge", bilanFinancier.getMargePct());
                bilan.put("isPositif", bilanFinancier.isPositif());

                // Détail des dépenses
                bilan.put("depensesMissions", bilanFinancier.getTotalMissions());
                bilan.put("depensesEntretiens", bilanFinancier.getTotalEntretiens());
                bilan.put("depensesAssurances", bilanFinancier.getTotalAssurances());
            }

            // Évolution mensuelle
            bilan.put("evolutionMensuelle", calculerEvolutionMensuelle(annee));

            // Répartition budgétaire
            bilan.put("repartitionBudgetaire", calculerRepartitionBudgetaire(annee));

            // Rentabilité des véhicules
            bilan.put("rentabiliteVehicules", genererRapportRentabilite(annee));

            return bilan;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la génération du bilan annuel pour l'année " + annee, e);
            return Collections.emptyMap();
        }
    }

    /**
     * Valide les données d'un compte sociétaire.
     *
     * @param compte Le compte à valider
     * @return true si les données sont valides, false sinon
     */
    private boolean validateSocieteCompte(SocieteCompte compte) {
        if (compte == null) {
            return false;
        }

        // Vérifier que les champs obligatoires sont renseignés
        if (compte.getNom() == null || compte.getNom().trim().isEmpty() ||
                compte.getNumero() == null || compte.getNumero().trim().isEmpty() ||
                compte.getSolde() == null) {
            return false;
        }

        // Vérifier que le numéro de compte a un format valide
        String numero = compte.getNumero().trim();
        if (!numero.matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}") && !numero.matches("SC-\\d{6}-[A-Z]{2}")) {
            return false;
        }

        // Vérifier que l'email est valide (si renseigné)
        if (compte.getEmail() != null && !compte.getEmail().trim().isEmpty()) {
            String email = compte.getEmail().trim();
            if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
                return false;
            }
        }

        return true;
    }

    /**
     * Valide les données d'un mouvement.
     *
     * @param mouvement Le mouvement à valider
     * @return true si les données sont valides, false sinon
     */
    private boolean validateMouvement(Mouvement mouvement) {
        if (mouvement == null) {
            return false;
        }

        // Vérifier que les champs obligatoires sont renseignés
        if (mouvement.getIdSocietaire() == null ||
                mouvement.getType() == null ||
                mouvement.getMontant() == null ||
                mouvement.getDate() == null) {
            return false;
        }

        // Vérifier que le montant est positif
        if (mouvement.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        return true;
    }
}