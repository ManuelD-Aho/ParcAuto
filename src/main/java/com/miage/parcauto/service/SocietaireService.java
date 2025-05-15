package main.java.com.miage.parcauto.service;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.com.miage.parcauto.dao.SocieteCompteDao;
import main.java.com.miage.parcauto.model.rh.Societaire;
import main.java.com.miage.parcauto.model.finance.SocieteCompte;
import main.java.com.miage.parcauto.model.rh.Personnel;

/**
 * Service de gestion des sociétaires (comptes sociétaires).
 * Cette classe implémente la couche service pour les opérations liées aux
 * sociétaires.
 * Elle sert d'intermédiaire entre la couche DAO et la couche de présentation
 * (contrôleurs).
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class SocietaireService {

    private static final Logger LOGGER = Logger.getLogger(SocietaireService.class.getName());

    private final SocieteCompteDao societeCompteDao;

    /**
     * Constructeur par défaut.
     */
    public SocietaireService() {
        this.societeCompteDao = new SocieteCompteDao();
    }

    /**
     * Constructeur avec injection de dépendance pour les tests.
     *
     * @param societeCompteDao Instance de SocieteCompteDao à utiliser
     */
    public SocietaireService(SocieteCompteDao societeCompteDao) {
        this.societeCompteDao = societeCompteDao;
    }

    /**
     * Récupère tous les comptes sociétaires.
     *
     * @return Liste de tous les comptes sociétaires
     */
    public List<SocieteCompte> getAllSocietaires() {
        try {
            return societeCompteDao.findAll();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des comptes sociétaires", e);
            return Collections.emptyList();
        }
    }

    /**
     * Recherche un compte sociétaire par son ID.
     *
     * @param id ID du compte sociétaire
     * @return Optional contenant le compte s'il existe
     */
    public Optional<SocieteCompte> getSocietaireById(int id) {
        try {
            return societeCompteDao.findById(id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération du compte sociétaire par ID: " + id, e);
            return Optional.empty();
        }
    }

    /**
     * Récupère l'ID du personnel associé à un compte sociétaire.
     * À utiliser à la place de getIdResponsable() qui n'existe pas.
     *
     * @param idSocietaire ID du compte sociétaire
     * @return Optional contenant l'ID du personnel associé s'il existe
     */
    public Optional<Integer> getIdPersonnel(int idSocietaire) {
        try {
            Optional<SocieteCompte> societeCompte = societeCompteDao.findById(idSocietaire);
            return societeCompte.map(SocieteCompte::getIdPersonnel);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "Erreur lors de la récupération de l'ID du personnel pour le sociétaire: " + idSocietaire, e);
            return Optional.empty();
        }
    }

    /**
     * Récupère le personnel associé à un compte sociétaire.
     *
     * @param idSocietaire ID du compte sociétaire
     * @return Optional contenant le personnel associé s'il existe
     */
    public Optional<Personnel> getPersonnelForSocietaire(int idSocietaire) {
        try {
            Optional<SocieteCompte> societeCompte = societeCompteDao.findById(idSocietaire);
            return societeCompte.map(SocieteCompte::getPersonnel);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération du personnel pour le sociétaire: " + idSocietaire,
                    e);
            return Optional.empty();
        }
    }

    /**
     * Crée un nouveau compte sociétaire.
     *
     * @param societaire Le compte sociétaire à créer
     * @return Le compte créé avec son ID généré ou null en cas d'erreur
     */
    public SocieteCompte createSocietaire(SocieteCompte societaire) {
        try {
            return societeCompteDao.create(societaire);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création du sociétaire", e);
            return null;
        }
    }

    /**
     * Met à jour un compte sociétaire existant.
     *
     * @param societaire Le compte sociétaire à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateSocietaire(SocieteCompte societaire) {
        try {
            return societeCompteDao.update(societaire);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du sociétaire", e);
            return false;
        }
    }

    /**
     * Supprime un compte sociétaire.
     *
     * @param id ID du compte sociétaire à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deleteSocietaire(int id) {
        try {
            return societeCompteDao.delete(id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression du sociétaire", e);
            return false;
        }
    }

    /**
     * Récupère le compte sociétaire (SocieteCompte) associé à un utilisateur (via
     * idPersonnel).
     *
     * @param idUtilisateur L'identifiant de l'utilisateur
     * @return Le compte sociétaire associé ou null si non trouvé
     */
    public SocieteCompte getSocieteCompteByIdUtilisateur(Integer idUtilisateur) {
        if (idUtilisateur == null)
            return null;
        try {
            // On suppose que l'utilisateur a un idPersonnel associé
            UtilisateurDao utilisateurDao = new UtilisateurDao();
            Optional<UtilisateurDao.Utilisateur> userOpt = utilisateurDao.findById(idUtilisateur);
            if (userOpt.isPresent() && userOpt.get().getIdPersonnel() != null) {
                Optional<SocieteCompte> compteOpt = societeCompteDao.findByIdPersonnel(userOpt.get().getIdPersonnel());
                return compteOpt.orElse(null);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération du compte sociétaire par idUtilisateur", e);
        }
        return null;
    }

    /**
     * Récupère le sociétaire (objet métier) associé à un utilisateur (via
     * idUtilisateur).
     *
     * @param idUtilisateur L'identifiant de l'utilisateur
     * @return Le sociétaire associé ou null si non trouvé
     */
    public Societaire getSocietaireByIdUtilisateur(Integer idUtilisateur) {
        SocieteCompte compte = getSocieteCompteByIdUtilisateur(idUtilisateur);
        if (compte == null)
            return null;
        // Conversion SocieteCompte -> Societaire (modèle métier)
        Societaire s = new Societaire();
        s.setIdSocietaire(compte.getIdSocietaire());
        s.setNom(compte.getNom());
        // Si le prénom est disponible via le personnel associé
        if (compte.getPersonnel() != null) {
            s.setPrenom(compte.getPersonnel().getPrenomPersonnel());
        }
        s.setEmail(compte.getEmail());
        s.setTelephone(compte.getTelephone());
        // Les autres champs ne sont pas disponibles dans SocieteCompte
        return s;
    }
}