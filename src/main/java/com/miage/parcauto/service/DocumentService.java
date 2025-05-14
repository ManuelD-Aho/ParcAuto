package main.java.com.miage.parcauto.service;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.com.miage.parcauto.dao.DocumentRepository;
import main.java.com.miage.parcauto.dao.DocumentRepositoryImpl;
import main.java.com.miage.parcauto.dao.DocumentRepository.Document;
import main.java.com.miage.parcauto.dao.DocumentRepository.TypeDoc;

/**
 * Service pour la gestion des documents.
 * Implémente la logique métier liée aux documents et fait le lien entre le
 * contrôleur et le DAO.
 * 
 */
public class DocumentService {

    private static final Logger LOGGER = Logger.getLogger(DocumentService.class.getName());
    private final DocumentRepository documentRepository;

    /**
     * Constructeur par défaut.
     */
    public DocumentService() {
        this.documentRepository = new DocumentRepositoryImpl();
    }

    /**
     * Constructeur avec injection de dépendance pour les tests.
     * 
     * @param documentRepository DAO des documents à utiliser
     */
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    /**
     * Récupère tous les documents.
     * 
     * @return Liste des documents
     */
    public List<Document> getAllDocuments() {
        try {
            return documentRepository.findAll();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les documents", e);
            return Collections.emptyList();
        }
    }

    /**
     * Récupère un document par son ID.
     * 
     * @param id ID du document
     * @return Document trouvé ou null si non trouvé
     */
    public Document getDocumentById(int id) {
        try {
            Optional<Document> doc = documentRepository.findById(id);
            return doc.orElse(null);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération du document par ID: " + id, e);
            return null;
        }
    }

    /**
     * Récupère les documents d'un sociétaire.
     * 
     * @param idSocietaire ID du sociétaire
     * @return Liste des documents du sociétaire
     */
    public List<Document> getDocumentsBySocietaire(int idSocietaire) {
        try {
            return documentRepository.findBySocietaire(idSocietaire);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des documents du sociétaire: " + idSocietaire, e);
            return Collections.emptyList();
        }
    }

    /**
     * Récupère les documents d'un type spécifique.
     * 
     * @param typeDoc Type de document
     * @return Liste des documents du type spécifié
     */
    public List<Document> getDocumentsByType(TypeDoc typeDoc) {
        try {
            return documentRepository.findByType(typeDoc);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des documents du type: " + typeDoc, e);
            return Collections.emptyList();
        }
    }

    /**
     * Récupère les documents de plusieurs types spécifiques.
     * 
     * @param types Types de documents
     * @return Liste des documents des types spécifiés
     */
    public List<Document> getDocumentsByTypes(TypeDoc... types) {
        List<Document> result = new java.util.ArrayList<>();

        for (TypeDoc type : types) {
            try {
                List<Document> docs = documentRepository.findByType(type);
                result.addAll(docs);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des documents du type: " + type, e);
            }
        }

        return result;
    }

    /**
     * Vérifie si un document d'un type spécifique existe pour un sociétaire.
     * 
     * @param idSocietaire ID du sociétaire
     * @param typeDoc      Type de document
     * @return true si un document existe, false sinon
     */
    public boolean documentExists(int idSocietaire, TypeDoc typeDoc) {
        try {
            return documentRepository.documentExists(idSocietaire, typeDoc);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la vérification d'existence du document", e);
            return false;
        }
    }

    /**
     * Upload un nouveau document.
     * 
     * @param sourcePath   Chemin du fichier à uploader
     * @param idSocietaire ID du sociétaire associé
     * @param typeDoc      Type du document
     * @param nomOriginal  Nom original du fichier
     * @return Document créé ou null si erreur
     */
    public Document uploadDocument(Path sourcePath, int idSocietaire, TypeDoc typeDoc, String nomOriginal) {
        try {
            return documentRepository.save(sourcePath, idSocietaire, typeDoc, nomOriginal);
        } catch (SQLException | IOException | IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'upload du document", e);
            return null;
        }
    }

    /**
     * Remplace un document existant.
     * 
     * @param idDoc       ID du document à remplacer
     * @param sourcePath  Chemin du nouveau fichier
     * @param nomOriginal Nom original du nouveau fichier
     * @return Document mis à jour ou null si erreur
     */
    public Document replaceDocument(int idDoc, Path sourcePath, String nomOriginal) {
        try {
            return documentRepository.replace(idDoc, sourcePath, nomOriginal);
        } catch (SQLException | IOException | IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du remplacement du document", e);
            return null;
        }
    }

    /**
     * Met à jour les informations d'un document.
     * 
     * @param document Document à mettre à jour
     * @return true si la mise à jour est réussie, false sinon
     */
    public boolean updateDocument(Document document) {
        try {
            return documentRepository.update(document);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du document", e);
            return false;
        }
    }

    /**
     * Supprime un document.
     * 
     * @param idDoc ID du document à supprimer
     * @return true si la suppression est réussie, false sinon
     */
    public boolean deleteDocument(int idDoc) {
        try {
            return documentRepository.delete(idDoc);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression du document", e);
            return false;
        }
    }

    /**
     * Récupère le chemin d'un document pour l'afficher.
     * 
     * @param idDoc ID du document
     * @return Optional contenant le chemin du document s'il existe
     */
    public Optional<Path> getDocumentPath(int idDoc) {
        try {
            return documentRepository.getDocumentPath(idDoc);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération du chemin du document", e);
            return Optional.empty();
        }
    }

    /**
     * Vérifie si un sociétaire possède tous les documents obligatoires.
     * 
     * @param idSocietaire ID du sociétaire
     * @return true si tous les documents obligatoires sont présents, false sinon
     */
    public boolean hasRequiredDocuments(int idSocietaire) {
        try {
            return documentRepository.hasRequiredDocuments(idSocietaire);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la vérification des documents obligatoires", e);
            return false;
        }
    }

    /**
     * Recherche de documents par terme de recherche.
     * 
     * @param searchTerm Terme de recherche
     * @return Liste des documents correspondants
     */
    public List<Document> searchDocuments(String searchTerm) {
        try {
            return documentRepository.search(searchTerm);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche de documents", e);
            return Collections.emptyList();
        }
    }
}