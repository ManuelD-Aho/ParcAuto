package main.java.com.miage.parcauto.service;

import main.java.com.miage.parcauto.dto.DocumentSocietaireDTO;
import main.java.com.miage.parcauto.exception.DocumentNotFoundException;
import main.java.com.miage.parcauto.exception.OperationFailedException;
import main.java.com.miage.parcauto.exception.ValidationException;
import main.java.com.miage.parcauto.exception.SocietaireNotFoundException;
import main.java.com.miage.parcauto.model.document.TypeDocument;

import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des documents des sociétaires.
 */
public interface DocumentSocietaireService {

    /**
     * Enregistre un nouveau document pour un sociétaire.
     *
     * @param documentDTO Le DTO contenant les informations du document.
     * @return Le DocumentSocietaireDTO enregistré avec son ID.
     * @throws ValidationException Si les données du document sont invalides.
     * @throws SocietaireNotFoundException Si le compte sociétaire associé n'existe pas.
     * @throws OperationFailedException Si une erreur technique survient (ex: échec de sauvegarde du fichier).
     */
    DocumentSocietaireDTO createDocumentSocietaire(DocumentSocietaireDTO documentDTO) throws ValidationException, SocietaireNotFoundException, OperationFailedException;

    /**
     * Récupère un document par son identifiant.
     *
     * @param idDocument L'identifiant du document.
     * @return Un Optional contenant le DocumentSocietaireDTO si trouvé.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    Optional<DocumentSocietaireDTO> getDocumentSocietaireById(Integer idDocument) throws OperationFailedException;

    /**
     * Récupère tous les documents pour un compte sociétaire spécifique.
     *
     * @param idCompteSocietaire L'identifiant du compte sociétaire.
     * @return Une liste de DocumentSocietaireDTO.
     * @throws SocietaireNotFoundException Si le compte sociétaire n'est pas trouvé.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    List<DocumentSocietaireDTO> getDocumentsByCompteSocietaireId(Integer idCompteSocietaire) throws SocietaireNotFoundException, OperationFailedException;

    /**
     * Récupère tous les documents d'un type spécifique pour un compte sociétaire.
     *
     * @param idCompteSocietaire L'identifiant du compte sociétaire.
     * @param typeDocument Le type de document.
     * @return Une liste de DocumentSocietaireDTO.
     * @throws SocietaireNotFoundException Si le compte sociétaire n'est pas trouvé.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    List<DocumentSocietaireDTO> getDocumentsByCompteSocietaireIdAndType(Integer idCompteSocietaire, TypeDocument typeDocument) throws SocietaireNotFoundException, OperationFailedException;


    /**
     * Met à jour les informations d'un document existant.
     *
     * @param documentDTO Le DTO du document avec les informations mises à jour.
     * @return Le DocumentSocietaireDTO mis à jour.
     * @throws ValidationException Si les données sont invalides.
     * @throws DocumentNotFoundException Si le document à mettre à jour n'est pas trouvé.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    DocumentSocietaireDTO updateDocumentSocietaire(DocumentSocietaireDTO documentDTO) throws ValidationException, DocumentNotFoundException, OperationFailedException;

    /**
     * Supprime un document par son identifiant.
     * Ceci devrait aussi gérer la suppression du fichier physique si nécessaire.
     *
     * @param idDocument L'identifiant du document à supprimer.
     * @throws DocumentNotFoundException Si le document à supprimer n'est pas trouvé.
     * @throws OperationFailedException Si une erreur technique survient.
     */
    void deleteDocumentSocietaire(Integer idDocument) throws DocumentNotFoundException, OperationFailedException;
}