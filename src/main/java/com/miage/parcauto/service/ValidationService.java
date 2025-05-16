package main.java.com.miage.parcauto.service;

import main.java.com.miage.parcauto.dto.*; // Importer tous les DTOs nécessaires
import main.java.com.miage.parcauto.exception.ValidationException;

/**
 * Service transversal pour la validation des objets de transfert de données
 * (DTO)
 * et potentiellement des entités avant persistance ou traitement.
 */
public interface ValidationService {

    /**
     * Valide un VehiculeDTO.
     * 
     * @param vehiculeDTO Le DTO à valider.
     * @throws ValidationException si des erreurs de validation sont trouvées.
     */
    void validateVehicule(VehiculeDTO vehiculeDTO) throws ValidationException;

    /**
     * Valide un EntretienDTO.
     * 
     * @param entretienDTO Le DTO à valider.
     * @throws ValidationException si des erreurs de validation sont trouvées.
     */
    void validateEntretien(EntretienDTO entretienDTO) throws ValidationException;

    /**
     * Valide un MissionDTO.
     * 
     * @param missionDTO Le DTO à valider.
     * @throws ValidationException si des erreurs de validation sont trouvées.
     */
    void validateMission(MissionDTO missionDTO) throws ValidationException;

    /**
     * Valide un DepenseMissionDTO.
     * 
     * @param depenseDTO Le DTO à valider.
     * @throws ValidationException si des erreurs de validation sont trouvées.
     */
    void validateDepenseMission(DepenseMissionDTO depenseDTO) throws ValidationException;

    /**
     * Valide un UtilisateurDTO (pour création ou mise à jour).
     * 
     * @param utilisateurDTO Le DTO à valider.
     * @param isCreation     true si c'est pour une création (ex: mot de passe
     *                       obligatoire), false pour une mise à jour.
     * @throws ValidationException si des erreurs de validation sont trouvées.
     */
    void validateUtilisateur(UtilisateurDTO utilisateurDTO, boolean isCreation) throws ValidationException;

    /**
     * Valide un PersonnelDTO.
     * 
     * @param personnelDTO Le DTO à valider.
     * @throws ValidationException si des erreurs de validation sont trouvées.
     */
    void validatePersonnel(PersonnelDTO personnelDTO) throws ValidationException;

    /**
     * Valide un SocietaireCompteDTO.
     * 
     * @param compteDTO Le DTO à valider.
     * @throws ValidationException si des erreurs de validation sont trouvées.
     */
    void validateSocietaireCompte(SocietaireCompteDTO compteDTO) throws ValidationException;

    /**
     * Valide un MouvementDTO.
     * 
     * @param mouvementDTO Le DTO à valider.
     * @throws ValidationException si des erreurs de validation sont trouvées.
     */
    void validateMouvement(MouvementDTO mouvementDTO) throws ValidationException;

    /**
     * Valide un DocumentSocietaireDTO.
     * 
     * @param documentDTO Le DTO à valider.
     * @throws ValidationException si des erreurs de validation sont trouvées.
     */
    void validateDocumentSocietaire(DocumentSocietaireDTO documentDTO) throws ValidationException;

    /**
     * Valide un AssuranceDTO.
     * 
     * @param assuranceDTO Le DTO à valider.
     * @throws ValidationException si des erreurs de validation sont trouvées.
     */
    void validateAssurance(AssuranceDTO assuranceDTO) throws ValidationException;

    /**
     * Valide un AffectationDTO.
     * 
     * @param affectationDTO Le DTO à valider.
     * @throws ValidationException si des erreurs de validation sont trouvées.
     */
    void validateAffectation(AffectationDTO affectationDTO) throws ValidationException;

    /**
     * Valide une NotificationDTO.
     * 
     * @param notificationDTO Le DTO à valider.
     * @throws ValidationException si des erreurs de validation sont trouvées.
     */
    void validateNotification(NotificationDTO notificationDTO) throws ValidationException;

    // Ajouter d'autres méthodes de validation spécifiques au besoin.
    // Une méthode générique `validate(Object dto)` pourrait être envisagée
    // mais nécessiterait de la réflexion (instanceof, annotations, etc.)
    // ce qui sort du cadre de la stack "sans frameworks".
}