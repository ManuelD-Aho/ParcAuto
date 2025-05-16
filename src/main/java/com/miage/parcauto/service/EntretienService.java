package main.java.com.miage.parcauto.service;

import main.java.com.miage.parcauto.dto.EntretienDTO;
import main.java.com.miage.parcauto.exception.EntretienNotFoundException;
import main.java.com.miage.parcauto.exception.OperationFailedException;
import main.java.com.miage.parcauto.exception.ValidationException;
import main.java.com.miage.parcauto.exception.VehiculeNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des entretiens des véhicules.
 */
public interface EntretienService {

    /**
     * Crée un nouvel enregistrement d'entretien.
     *
     * @param entretienDTO Le DTO de l'entretien à créer.
     * @return L'EntretienDTO créé avec son ID.
     * @throws ValidationException Si les données de l'entretien sont invalides.
     * @throws VehiculeNotFoundException Si le véhicule associé n'existe pas.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    EntretienDTO createEntretien(EntretienDTO entretienDTO) throws ValidationException, VehiculeNotFoundException, OperationFailedException;

    /**
     * Récupère un entretien par son identifiant.
     *
     * @param idEntretien L'identifiant de l'entretien.
     * @return Un Optional contenant l'EntretienDTO si trouvé.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    Optional<EntretienDTO> getEntretienById(Integer idEntretien) throws OperationFailedException;

    /**
     * Récupère tous les entretiens enregistrés.
     *
     * @return Une liste d'EntretienDTO.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    List<EntretienDTO> getAllEntretiens() throws OperationFailedException;

    /**
     * Récupère tous les entretiens pour un véhicule spécifique.
     *
     * @param idVehicule L'identifiant du véhicule.
     * @return Une liste d'EntretienDTO pour le véhicule spécifié.
     * @throws VehiculeNotFoundException Si le véhicule n'est pas trouvé.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    List<EntretienDTO> getEntretiensByVehiculeId(Integer idVehicule) throws VehiculeNotFoundException, OperationFailedException;

    /**
     * Met à jour les informations d'un entretien existant.
     *
     * @param entretienDTO Le DTO de l'entretien avec les informations mises à jour.
     * @return L'EntretienDTO mis à jour.
     * @throws ValidationException Si les données de l'entretien sont invalides.
     * @throws EntretienNotFoundException Si l'entretien à mettre à jour n'est pas trouvé.
     * @throws VehiculeNotFoundException Si le véhicule associé (s'il est modifié) n'existe pas.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    EntretienDTO updateEntretien(EntretienDTO entretienDTO) throws ValidationException, EntretienNotFoundException, VehiculeNotFoundException, OperationFailedException;

    /**
     * Supprime un entretien par son identifiant.
     *
     * @param idEntretien L'identifiant de l'entretien à supprimer.
     * @throws EntretienNotFoundException Si l'entretien à supprimer n'est pas trouvé.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    void deleteEntretien(Integer idEntretien) throws EntretienNotFoundException, OperationFailedException;

    /**
     * Marque un entretien comme terminé, en enregistrant les informations finales.
     *
     * @param idEntretien L'identifiant de l'entretien à terminer.
     * @param dateRealisation La date effective de fin d'entretien.
     * @param coutReel Le coût réel de l'entretien.
     * @param kmVehicule Le kilométrage du véhicule au moment de la fin de l'entretien.
     * @param kmProchainEntretien Le kilométrage prévu pour le prochain entretien (si applicable).
     * @param observations Les observations finales sur l'entretien.
     * @return L'EntretienDTO mis à jour.
     * @throws EntretienNotFoundException Si l'entretien n'est pas trouvé.
     * @throws ValidationException Si les données de clôture sont invalides.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    EntretienDTO terminerEntretien(Integer idEntretien, LocalDateTime dateRealisation, BigDecimal coutReel, Integer kmVehicule, Integer kmProchainEntretien, String observations) throws EntretienNotFoundException, ValidationException, OperationFailedException;

    /**
     * Récupère les entretiens planifiés (ou dont la date prévue est) dans un intervalle de dates.
     *
     * @param dateDebut Date de début de l'intervalle.
     * @param dateFin Date de fin de l'intervalle.
     * @return Liste des EntretienDTO.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    List<EntretienDTO> getEntretiensPlanifiesEntre(LocalDateTime dateDebut, LocalDateTime dateFin) throws OperationFailedException;
}