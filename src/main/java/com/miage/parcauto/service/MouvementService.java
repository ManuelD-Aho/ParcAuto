package main.java.com.miage.parcauto.service;

import main.java.com.miage.parcauto.dto.MouvementDTO;
import main.java.com.miage.parcauto.exception.EntityNotFoundException;
import main.java.com.miage.parcauto.exception.OperationFailedException;
import main.java.com.miage.parcauto.exception.ValidationException;
import main.java.com.miage.parcauto.exception.SocietaireNotFoundException;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des mouvements financiers sur les comptes sociétaires.
 */
public interface MouvementService {

    /**
     * Enregistre un nouveau mouvement financier.
     * Cette opération mettra à jour le solde du compte sociétaire concerné.
     *
     * @param mouvementDTO Le DTO contenant les informations du mouvement.
     * @return Le MouvementDTO enregistré avec son ID.
     * @throws ValidationException Si les données du mouvement sont invalides.
     * @throws SocietaireNotFoundException Si le compte sociétaire associé n'existe pas.
     * @throws OperationFailedException Si une erreur technique survient ou si le solde devient insuffisant pour un retrait.
     */
    MouvementDTO createMouvement(MouvementDTO mouvementDTO) throws ValidationException, SocietaireNotFoundException, OperationFailedException;

    /**
     * Récupère un mouvement par son identifiant.
     *
     * @param idMouvement L'identifiant du mouvement.
     * @return Un Optional contenant le MouvementDTO si trouvé.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    Optional<MouvementDTO> getMouvementById(Integer idMouvement) throws OperationFailedException;

    /**
     * Récupère tous les mouvements pour un compte sociétaire spécifique.
     *
     * @param idCompteSocietaire L'identifiant du compte sociétaire.
     * @return Une liste de MouvementDTO.
     * @throws SocietaireNotFoundException Si le compte sociétaire n'est pas trouvé.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    List<MouvementDTO> getMouvementsByCompteSocietaireId(Integer idCompteSocietaire) throws SocietaireNotFoundException, OperationFailedException;

    /**
     * Récupère tous les mouvements financiers enregistrés.
     *
     * @return Une liste de MouvementDTO.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    List<MouvementDTO> getAllMouvements() throws OperationFailedException;

    /**
     * Récupère les mouvements financiers pour une période donnée.
     *
     * @param dateDebut Date de début de la période.
     * @param dateFin Date de fin de la période.
     * @return Liste de MouvementDTO.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    List<MouvementDTO> getMouvementsByDateRange(LocalDateTime dateDebut, LocalDateTime dateFin) throws OperationFailedException;

    // La mise à jour et la suppression de mouvements peuvent être complexes
    // car elles impliquent de recalculer les soldes.
    // Pour l'instant, nous ne les incluons pas, mais elles pourraient être ajoutées
    // avec une logique métier appropriée (par exemple, suppression uniquement si c'est le dernier mouvement).
}