package main.java.com.miage.parcauto.service;

import main.java.com.miage.parcauto.dto.DepenseMissionDTO;
import main.java.com.miage.parcauto.dto.MissionDTO;
import main.java.com.miage.parcauto.exception.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des missions des véhicules.
 */
public interface MissionService {

    /**
     * Crée une nouvelle mission.
     *
     * @param missionDTO Le DTO de la mission à créer.
     * @return La MissionDTO créée avec son ID.
     * @throws ValidationException          Si les données de la mission sont
     *                                      invalides.
     * @throws VehiculeNotFoundException    Si le véhicule associé n'existe pas ou
     *                                      n'est pas disponible.
     * @throws UtilisateurNotFoundException Si le personnel associé n'existe pas.
     * @throws OperationFailedException     Si une erreur technique survient.
     */
    MissionDTO createMission(MissionDTO missionDTO) throws ValidationException, VehiculeNotFoundException,
            UtilisateurNotFoundException, OperationFailedException;

    /**
     * Récupère une mission par son identifiant.
     *
     * @param idMission L'identifiant de la mission.
     * @return Un Optional contenant la MissionDTO si trouvée.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    Optional<MissionDTO> getMissionById(Integer idMission) throws OperationFailedException;

    /**
     * Récupère toutes les missions enregistrées.
     *
     * @return Une liste de MissionDTO.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    List<MissionDTO> getAllMissions() throws OperationFailedException;

    /**
     * Récupère toutes les missions pour un véhicule spécifique.
     *
     * @param idVehicule L'identifiant du véhicule.
     * @return Une liste de MissionDTO.
     * @throws VehiculeNotFoundException Si le véhicule n'est pas trouvé.
     * @throws OperationFailedException  Si une erreur technique survient.
     */
    List<MissionDTO> getMissionsByVehiculeId(Integer idVehicule)
            throws VehiculeNotFoundException, OperationFailedException;

    /**
     * Récupère toutes les missions pour un membre du personnel spécifique.
     *
     * @param idPersonnel L'identifiant du personnel.
     * @return Une liste de MissionDTO.
     * @throws UtilisateurNotFoundException Si le personnel n'est pas trouvé.
     * @throws OperationFailedException     Si une erreur technique survient.
     */
    List<MissionDTO> getMissionsByPersonnelId(Integer idPersonnel)
            throws UtilisateurNotFoundException, OperationFailedException;

    /**
     * Met à jour les informations d'une mission existante.
     *
     * @param missionDTO Le DTO de la mission avec les informations mises à jour.
     * @return La MissionDTO mise à jour.
     * @throws ValidationException          Si les données de la mission sont
     *                                      invalides.
     * @throws MissionNotFoundException     Si la mission à mettre à jour n'est pas
     *                                      trouvée.
     * @throws VehiculeNotFoundException    Si le véhicule associé (s'il est
     *                                      modifié) n'existe pas ou n'est pas
     *                                      disponible.
     * @throws UtilisateurNotFoundException Si le personnel associé (s'il est
     *                                      modifié) n'existe pas.
     * @throws OperationFailedException     Si une erreur technique survient.
     */
    MissionDTO updateMission(MissionDTO missionDTO) throws ValidationException, MissionNotFoundException,
            VehiculeNotFoundException, UtilisateurNotFoundException, OperationFailedException;

    /**
     * Supprime une mission par son identifiant.
     * Les dépenses associées peuvent également être supprimées ou gérées selon la
     * logique métier.
     *
     * @param idMission L'identifiant de la mission à supprimer.
     * @throws MissionNotFoundException Si la mission à supprimer n'est pas trouvée.
     * @throws OperationFailedException Si une erreur technique survient (ex:
     *                                  mission non clôturée).
     */
    void deleteMission(Integer idMission) throws MissionNotFoundException, OperationFailedException;

    /**
     * Termine une mission, mettant à jour son statut, le kilométrage du véhicule,
     * etc.
     *
     * @param idMission        L'identifiant de la mission à terminer.
     * @param kmRetour         Le kilométrage du véhicule au retour de la mission.
     * @param dateFinEffective La date et heure de fin effective de la mission.
     * @param observations     Les observations finales sur la mission.
     * @return La MissionDTO mise à jour.
     * @throws MissionNotFoundException Si la mission n'est pas trouvée.
     * @throws ValidationException      Si les données de clôture sont invalides
     *                                  (ex: kmRetour < kmDepart).
     * @throws OperationFailedException Si une erreur technique survient.
     */
    MissionDTO terminerMission(Integer idMission, int kmRetour, LocalDateTime dateFinEffective, String observations)
            throws MissionNotFoundException, ValidationException, OperationFailedException;

    /**
     * Ajoute une dépense à une mission existante.
     *
     * @param idMission  L'identifiant de la mission.
     * @param depenseDTO Le DTO de la dépense à ajouter.
     * @return La DepenseMissionDTO créée avec son ID.
     * @throws MissionNotFoundException Si la mission n'est pas trouvée.
     * @throws ValidationException      Si les données de la dépense sont invalides.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    DepenseMissionDTO ajouterDepenseAMission(Integer idMission, DepenseMissionDTO depenseDTO)
            throws MissionNotFoundException, ValidationException, OperationFailedException;

    /**
     * Récupère toutes les dépenses associées à une mission.
     *
     * @param idMission L'identifiant de la mission.
     * @return Une liste de DepenseMissionDTO.
     * @throws MissionNotFoundException Si la mission n'est pas trouvée.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    List<DepenseMissionDTO> getDepensesByMissionId(Integer idMission)
            throws MissionNotFoundException, OperationFailedException;

    /**
     * Récupère les missions actives (non clôturées) pour un véhicule donné.
     * 
     * @param idVehicule L'identifiant du véhicule.
     * @return Liste des MissionDTO actives.
     * @throws VehiculeNotFoundException Si le véhicule n'est pas trouvé.
     * @throws OperationFailedException  Si une erreur technique survient.
     */
    List<MissionDTO> getMissionsActivesPourVehicule(Integer idVehicule)
            throws VehiculeNotFoundException, OperationFailedException;

    /**
     * Récupère les missions dont la date de début ou de fin se situe dans un
     * intervalle donné.
     *
     * @param dateDebut la date de début de l'intervalle.
     * @param dateFin   la date de fin de l'intervalle.
     * @return une liste des MissionDTO dans cet intervalle.
     * @throws OperationFailedException si une erreur d'accès à la base de données
     *                                  se produit.
     */
    List<MissionDTO> getMissionsParPeriode(LocalDateTime dateDebut, LocalDateTime dateFin)
            throws OperationFailedException;
}