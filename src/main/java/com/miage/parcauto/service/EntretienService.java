package main.java.com.miage.parcauto.service;

import main.java.com.miage.parcauto.dao.EntretienDao;
import main.java.com.miage.parcauto.dao.VehiculeDao;
import main.java.com.miage.parcauto.model.entretien.Entretien;
import main.java.com.miage.parcauto.model.entretien.Assurance;
import main.java.com.miage.parcauto.model.vehicule.EtatVoiture;
import main.java.com.miage.parcauto.model.vehicule.Vehicule;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service de gestion des entretiens de véhicules.
 * Cette classe implémente la couche service pour toutes les opérations liées
 * aux entretiens.
 * Elle sert d'intermédiaire entre la couche DAO et la couche de présentation
 * (contrôleurs).
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class EntretienService {

    private static final Logger LOGGER = Logger.getLogger(EntretienService.class.getName());

    private final EntretienDao entretienDao;
    private final VehiculeDao vehiculeDao;

    /**
     * Constructeur par défaut.
     */
    public EntretienService() {
        this.entretienDao = new EntretienDao();
        this.vehiculeDao = new VehiculeDao();
    }

    /**
     * Constructeur avec injection de dépendance pour les tests.
     *
     * @param entretienDao Instance de EntretienDao à utiliser
     * @param vehiculeDao  Instance de VehiculeDao à utiliser
     */
    public EntretienService(EntretienDao entretienDao, VehiculeDao vehiculeDao) {
        this.entretienDao = entretienDao;
        this.vehiculeDao = vehiculeDao;
    }

    /**
     * Récupère tous les entretiens.
     *
     * @return Liste de tous les entretiens ou liste vide en cas d'erreur
     */
    public List<Entretien> getAllEntretiens() {
        try {
            return entretienDao.findAll();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les entretiens", e);
            return Collections.emptyList();
        }
    }

    /**
     * Recherche un entretien par son ID.
     *
     * @param id ID de l'entretien
     * @return Optional contenant l'entretien s'il existe
     */
    public Optional<Entretien> getEntretienById(int id) {
        try {
            return entretienDao.findById(id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de l'entretien par ID: " + id, e);
            return Optional.empty();
        }
    }

    /**
     * Récupère les entretiens pour un véhicule.
     *
     * @param idVehicule ID du véhicule
     * @return Liste des entretiens pour ce véhicule ou liste vide en cas d'erreur
     */
    public List<Entretien> getEntretiensByVehicule(int idVehicule) {
        try {
            return entretienDao.findByVehicule(idVehicule);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des entretiens pour le véhicule ID: " + idVehicule,
                    e);
            return Collections.emptyList();
        }
    }

    /**
     * Récupère les entretiens selon leur statut.
     *
     * @param statut Statut des entretiens à récupérer
     * @return Liste des entretiens ayant ce statut ou liste vide en cas d'erreur
     */
    public List<Entretien> getEntretiensByStatut(Entretien.StatutOT statut) {
        try {
            return entretienDao.findByStatut(statut);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des entretiens par statut: " + statut, e);
            return Collections.emptyList();
        }
    }

    /**
     * Récupère les entretiens selon leur type.
     *
     * @param type Type des entretiens à récupérer
     * @return Liste des entretiens de ce type ou liste vide en cas d'erreur
     */
    public List<Entretien> getEntretiensByType(Entretien.TypeEntretien type) {
        try {
            return entretienDao.findByType(type);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des entretiens par type: " + type, e);
            return Collections.emptyList();
        }
    }

    /**
     * Récupère les entretiens en cours.
     *
     * @return Liste des entretiens en cours ou liste vide en cas d'erreur
     */
    public List<Entretien> getEntretiensEnCours() {
        try {
            return entretienDao.findEnCours();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des entretiens en cours", e);
            return Collections.emptyList();
        }
    }

    /**
     * Récupère les entretiens planifiés (ouverts).
     *
     * @return Liste des entretiens planifiés ou liste vide en cas d'erreur
     */
    public List<Entretien> getEntretiensPlanifies() {
        try {
            return entretienDao.findPlanifies();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des entretiens planifiés", e);
            return Collections.emptyList();
        }
    }

    /**
     * Récupère les entretiens terminés (clôturés).
     *
     * @return Liste des entretiens terminés ou liste vide en cas d'erreur
     */
    public List<Entretien> getEntretiensTermines() {
        try {
            return entretienDao.findTermines();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des entretiens terminés", e);
            return Collections.emptyList();
        }
    }

    /**
     * Récupère les entretiens entre deux dates.
     *
     * @param debut Date de début
     * @param fin   Date de fin
     * @return Liste des entretiens dans cette période ou liste vide en cas d'erreur
     */
    public List<Entretien> getEntretiensByPeriode(LocalDateTime debut, LocalDateTime fin) {
        try {
            return entretienDao.findByPeriode(debut, fin);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des entretiens par période", e);
            return Collections.emptyList();
        }
    }

    /**
     * Crée un nouvel entretien.
     * Met également à jour l'état du véhicule si demandé.
     *
     * @param entretien          L'entretien à créer
     * @param updateVehiculeEtat Si true, met à jour l'état du véhicule à "En
     *                           entretien"
     * @return L'entretien créé avec son ID généré ou null en cas d'erreur
     */
    public Entretien createEntretien(Entretien entretien, boolean updateVehiculeEtat) {
        try {
            // Validation des données
            if (!validateEntretien(entretien)) {
                LOGGER.warning("Validation de l'entretien échouée");
                return null;
            }

            // Vérifier que le véhicule existe
            Optional<Vehicule> vehicule = vehiculeDao.findById(entretien.getIdVehicule());
            if (!vehicule.isPresent()) {
                LOGGER.warning("Véhicule non trouvé avec l'ID: " + entretien.getIdVehicule());
                return null;
            }

            // Vérifier que le véhicule n'est pas déjà en entretien
            if (vehicule.get().estEnEntretien() && updateVehiculeEtat) {
                LOGGER.warning("Le véhicule est déjà en entretien");
                return null;
            }

            // Vérifier que le véhicule peut être mis en entretien (non en mission)
            if (vehicule.get().estEnMission() && updateVehiculeEtat) {
                LOGGER.warning("Le véhicule est en mission et ne peut pas être mis en entretien");
                return null;
            }

            return entretienDao.create(entretien, updateVehiculeEtat);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création de l'entretien", e);
            return null;
        }
    }

    /**
     * Met à jour un entretien existant.
     *
     * @param entretien L'entretien à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateEntretien(Entretien entretien) {
        try {
            // Validation des données
            if (!validateEntretien(entretien) || entretien.getIdEntretien() == null) {
                LOGGER.warning("Validation de l'entretien échouée ou ID manquant");
                return false;
            }

            // Vérifier que l'entretien existe
            Optional<Entretien> existingEntretien = entretienDao.findById(entretien.getIdEntretien());
            if (!existingEntretien.isPresent()) {
                LOGGER.warning("Entretien non trouvé avec l'ID: " + entretien.getIdEntretien());
                return false;
            }

            return entretienDao.update(entretien);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de l'entretien", e);
            return false;
        }
    }

    /**
     * Met à jour le statut d'un entretien.
     * Met également à jour l'état du véhicule si demandé.
     *
     * @param idEntretien        ID de l'entretien
     * @param statut             Nouveau statut
     * @param updateVehiculeEtat Si true, met à jour l'état du véhicule selon le
     *                           statut
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateEntretienStatut(int idEntretien, Entretien.StatutOT statut, boolean updateVehiculeEtat) {
        try {
            return entretienDao.updateStatut(idEntretien, statut, updateVehiculeEtat);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du statut de l'entretien: " + idEntretien, e);
            return false;
        }
    }

    /**
     * Démarre un entretien (passage au statut "en cours").
     *
     * @param idEntretien ID de l'entretien
     * @return true si le changement d'état a été effectué, false sinon
     */
    public boolean demarrerEntretien(int idEntretien) {
        try {
            return entretienDao.demarrerEntretien(idEntretien);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du démarrage de l'entretien: " + idEntretien, e);
            return false;
        }
    }

    /**
     * Clôture un entretien (passage au statut "clôturé").
     *
     * @param idEntretien ID de l'entretien
     * @return true si le changement d'état a été effectué, false sinon
     */
    public boolean cloturerEntretien(int idEntretien) {
        try {
            return entretienDao.cloturerEntretien(idEntretien);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la clôture de l'entretien: " + idEntretien, e);
            return false;
        }
    }

    /**
     * Met à jour le coût d'un entretien.
     *
     * @param idEntretien ID de l'entretien
     * @param cout        Nouveau coût
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateEntretienCout(int idEntretien, BigDecimal cout) {
        try {
            if (cout == null || cout.compareTo(BigDecimal.ZERO) < 0) {
                LOGGER.warning("Le coût ne peut pas être négatif");
                return false;
            }

            return entretienDao.updateCout(idEntretien, cout);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du coût de l'entretien: " + idEntretien, e);
            return false;
        }
    }

    /**
     * Supprime un entretien.
     *
     * @param idEntretien ID de l'entretien à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deleteEntretien(int idEntretien) {
        try {
            // Vérifier que l'entretien n'est pas en cours
            Optional<Entretien> entretien = entretienDao.findById(idEntretien);
            if (entretien.isPresent() && entretien.get().getStatutOt() == Entretien.StatutOT.EnCours) {
                LOGGER.warning("Impossible de supprimer un entretien en cours");
                return false;
            }

            return entretienDao.delete(idEntretien);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de l'entretien: " + idEntretien, e);
            return false;
        }
    }

    /**
     * Calcule le coût total des entretiens pour un véhicule.
     *
     * @param idVehicule ID du véhicule
     * @return Coût total des entretiens ou BigDecimal.ZERO en cas d'erreur
     */
    public BigDecimal calculateTotalCost(int idVehicule) {
        try {
            return entretienDao.calculateTotalCost(idVehicule);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Erreur lors du calcul du coût total des entretiens pour le véhicule: " + idVehicule, e);
            return BigDecimal.ZERO;
        }
    }

    /**
     * Calcule le coût moyen des entretiens par type.
     *
     * @param type Type d'entretien (null pour tous les types)
     * @return Coût moyen des entretiens ou BigDecimal.ZERO en cas d'erreur
     */
    public BigDecimal calculateAverageCost(Entretien.TypeEntretien type) {
        try {
            return entretienDao.calculateAverageCost(type);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul du coût moyen des entretiens", e);
            return BigDecimal.ZERO;
        }
    }

    /**
     * Calcule les statistiques d'entretien.
     *
     * @param year Année pour les statistiques (0 pour toutes les années)
     * @return Les statistiques d'entretien ou null en cas d'erreur
     */
    public EntretienDao.EntretienStats calculateStats(int year) {
        try {
            return entretienDao.calculateStats(year);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul des statistiques d'entretien", e);
            return null;
        }
    }

    /**
     * Récupère les entretiens à venir (planifiés) pour un véhicule.
     *
     * @param idVehicule ID du véhicule
     * @return Liste des entretiens planifiés pour ce véhicule ou liste vide en cas
     *         d'erreur
     */
    public List<Entretien> getEntretiensAVenir(int idVehicule) {
        try {
            return entretienDao.findEntretiensAVenir(idVehicule);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la récupération des entretiens à venir pour le véhicule: " + idVehicule, e);
            return Collections.emptyList();
        }
    }

    /**
     * Planifie un entretien préventif pour un véhicule.
     *
     * @param idVehicule ID du véhicule
     * @param dateEntree Date d'entrée prévue
     * @param motif      Motif de l'entretien
     * @param lieu       Lieu de l'entretien
     * @return L'entretien créé avec son ID généré ou null en cas d'erreur
     */
    public Entretien planifierEntretienPreventif(int idVehicule, LocalDateTime dateEntree, String motif, String lieu) {
        Entretien entretien = new Entretien();
        entretien.setIdVehicule(idVehicule);
        entretien.setDateEntreeEntr(dateEntree);
        entretien.setMotifEntr(motif);
        entretien.setLieuEntr(lieu);
        entretien.setType(Entretien.TypeEntretien.Preventif);
        entretien.setStatutOt(Entretien.StatutOT.Ouvert);
        entretien.setCoutEntr(BigDecimal.ZERO);

        return createEntretien(entretien, false);
    }

    /**
     * Enregistre un entretien correctif (réparation) pour un véhicule.
     *
     * @param idVehicule         ID du véhicule
     * @param motif              Motif de la réparation
     * @param lieu               Lieu de la réparation
     * @param updateVehiculeEtat Si true, met à jour l'état du véhicule
     * @return L'entretien créé avec son ID généré ou null en cas d'erreur
     */
    public Entretien enregistrerEntretienCorrectif(int idVehicule, String motif, String lieu,
            boolean updateVehiculeEtat) {
        Entretien entretien = new Entretien();
        entretien.setIdVehicule(idVehicule);
        entretien.setDateEntreeEntr(LocalDateTime.now());
        entretien.setMotifEntr(motif);
        entretien.setLieuEntr(lieu);
        entretien.setType(Entretien.TypeEntretien.Correctif);
        entretien.setStatutOt(Entretien.StatutOT.EnCours);
        entretien.setCoutEntr(BigDecimal.ZERO);

        return createEntretien(entretien, updateVehiculeEtat);
    }

    /**
     * Valide les données d'un entretien.
     *
     * @param entretien L'entretien à valider
     * @return true si les données sont valides, false sinon
     */
    private boolean validateEntretien(Entretien entretien) {
        if (entretien == null) {
            return false;
        }

        // Vérifier que les champs obligatoires sont renseignés
        if (entretien.getIdVehicule() == null ||
                entretien.getDateEntreeEntr() == null ||
                entretien.getMotifEntr() == null || entretien.getMotifEntr().trim().isEmpty() ||
                entretien.getType() == null ||
                entretien.getStatutOt() == null) {
            return false;
        }

        // Vérifier que la date d'entrée n'est pas dans le futur (sauf pour les
        // entretiens planifiés)
        if (entretien.getDateEntreeEntr().isAfter(LocalDateTime.now()) &&
                entretien.getStatutOt() != Entretien.StatutOT.Ouvert) {
            return false;
        }

        // Vérifier que la date de sortie est après la date d'entrée
        if (entretien.getDateSortieEntr() != null &&
                entretien.getDateEntreeEntr().isAfter(entretien.getDateSortieEntr())) {
            return false;
        }

        // Vérifier que le coût n'est pas négatif
        if (entretien.getCoutEntr() != null && entretien.getCoutEntr().compareTo(BigDecimal.ZERO) < 0) {
            return false;
        }

        return true;
    }

    /**
     * Retourne le nombre d'entretiens à venir (planifiés).
     * 
     * @return nombre d'entretiens planifiés, ou 0 en cas d'erreur
     */
    public int getEntretiensAVenirCount() {
        try {
            List<Entretien> planifies = getEntretiensPlanifies();
            return planifies != null ? planifies.size() : 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des entretiens à venir", e);
            return 0;
        }
    }
}