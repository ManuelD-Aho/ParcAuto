package com.miage.parcauto.service;

import com.miage.parcauto.dao.DocumentDao;
import com.miage.parcauto.dao.DocumentDao.Document;
import com.miage.parcauto.dao.DocumentDao.TypeDoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service de gestion documentaire.
 * Cette classe implémente la couche service pour toutes les opérations liées aux documents.
 * Elle sert d'intermédiaire entre la couche DAO et la couche de présentation (contrôleurs).
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class DocumentService {

    private static final Logger LOGGER = Logger.getLogger(DocumentService.class.getName());

    private final DocumentDao documentDao;

    /**
     * Constructeur par défaut.
     */
    public DocumentService() {
        this.documentDao = new DocumentDao();
    }

    /**
     * Constructeur avec injection de dépendance pour les tests.
     *
     * @param documentDao Instance de DocumentDao à utiliser
     */
    public DocumentService(DocumentDao documentDao) {
        this.documentDao = documentDao;
    }

    /**
     * Récupère tous les documents.
     *
     * @return Liste de tous les documents ou liste vide en cas d'erreur
     */
    public List<Document> getAllDocuments() {
        try {
            return documentDao.findAll();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les documents", e);
            return Collections.emptyList();
        }
    }

    /**
     * Recherche un document par son ID.
     *
     * @param id ID du document
     * @return Optional contenant le document s'il existe
     */
    public Optional<Document> getDocumentById(int id) {
        try {
            return documentDao.findById(id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du document par ID: " + id, e);
            return Optional.empty();
        }
    }

    /**
     * Récupère les documents d'un sociétaire.
     *
     * @param idSocietaire ID du sociétaire
     * @return Liste des documents du sociétaire ou liste vide en cas d'erreur
     */
    public List<Document> getDocumentsBySocietaire(int idSocietaire) {
        try {
            return documentDao.findBySocietaire(idSocietaire);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des documents pour le sociétaire ID: " + idSocietaire, e);
            return Collections.emptyList();
        }
    }

    /**
     * Récupère les documents d'un sociétaire par type.
     *
     * @param idSocietaire ID du sociétaire
     * @param typeDoc Type de document recherché
     * @return Liste des documents du sociétaire du type spécifié ou liste vide en cas d'erreur
     */
    public List<Document> getDocumentsBySocietaireAndType(int idSocietaire, TypeDoc typeDoc) {
        try {
            return documentDao.findBySocietaireAndType(idSocietaire, typeDoc);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des documents pour le sociétaire ID: " +
                    idSocietaire + " et type: " + typeDoc, e);
            return Collections.emptyList();
        }
    }

    /**
     * Récupère le document spécifique d'un sociétaire.
     *
     * @param idSocietaire ID du sociétaire
     * @param typeDoc Type de document recherché
     * @return Optional contenant le document s'il existe, vide sinon
     */
    public Optional<Document> getDocumentSpecifique(int idSocietaire, TypeDoc typeDoc) {
        try {
            return documentDao.findDocumentSpecifique(idSocietaire, typeDoc);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du document spécifique pour le sociétaire ID: " +
                    idSocietaire + " et type: " + typeDoc, e);
            return Optional.empty();
        }
    }

    /**
     * Récupère les documents par type.
     *
     * @param typeDoc Type de document recherché
     * @return Liste des documents du type spécifié ou liste vide en cas d'erreur
     */
    public List<Document> getDocumentsByType(TypeDoc typeDoc) {
        try {
            return documentDao.findByType(typeDoc);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des documents par type: " + typeDoc, e);
            return Collections.emptyList();
        }
    }

    /**
     * Recherche des documents par critères (nom, chemin, etc.).
     *
     * @param searchTerm Terme de recherche
     * @return Liste des documents correspondant aux critères ou liste vide en cas d'erreur
     */
    public List<Document> searchDocuments(String searchTerm) {
        try {
            return documentDao.search(searchTerm);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche des documents avec le terme: " + searchTerm, e);
            return Collections.emptyList();
        }
    }

    /**
     * Enregistre un nouveau document.
     *
     * @param filePath Chemin du fichier à enregistrer
     * @param idSocietaire ID du sociétaire associé au document
     * @param typeDoc Type du document
     * @param nomOriginal Nom original du fichier
     * @return Le document créé avec son ID généré ou null en cas d'erreur
     */
    public Document saveDocument(String filePath, int idSocietaire, TypeDoc typeDoc, String nomOriginal) {
        try {
            Path sourcePath = Paths.get(filePath);
            if (!Files.exists(sourcePath)) {
                LOGGER.warning("Le fichier n'existe pas: " + filePath);
                return null;
            }

            // Vérifier que le sociétaire existe (cette vérification devrait être faite par le DAO mais ajoutée ici pour plus de sécurité)
            if (idSocietaire <= 0) {
                LOGGER.warning("ID de sociétaire invalide: " + idSocietaire);
                return null;
            }

            // Vérifier que le type de document est valide
            if (typeDoc == null) {
                LOGGER.warning("Type de document invalide");
                return null;
            }

            return documentDao.save(sourcePath, idSocietaire, typeDoc, nomOriginal);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur SQL lors de l'enregistrement du document", e);
            return null;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur d'E/S lors de l'enregistrement du document", e);
            return null;
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Argument invalide lors de l'enregistrement du document: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Remplace un document existant.
     *
     * @param idDoc ID du document à remplacer
     * @param filePath Chemin du nouveau fichier
     * @param nomOriginal Nom original du nouveau fichier
     * @return Le document mis à jour ou null en cas d'erreur
     */
    public Document replaceDocument(int idDoc, String filePath, String nomOriginal) {
        try {
            Path sourcePath = Paths.get(filePath);
            if (!Files.exists(sourcePath)) {
                LOGGER.warning("Le fichier n'existe pas: " + filePath);
                return null;
            }

            // Vérifier que le document existe
            Optional<Document> existingDoc = getDocumentById(idDoc);
            if (!existingDoc.isPresent()) {
                LOGGER.warning("Document non trouvé avec l'ID: " + idDoc);
                return null;
            }

            return documentDao.replace(idDoc, sourcePath, nomOriginal);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur SQL lors du remplacement du document", e);
            return null;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur d'E/S lors du remplacement du document", e);
            return null;
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Argument invalide lors du remplacement du document: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Supprime un document.
     *
     * @param idDoc ID du document à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deleteDocument(int idDoc) {
        try {
            // Vérifier que le document existe
            Optional<Document> document = getDocumentById(idDoc);
            if (!document.isPresent()) {
                LOGGER.warning("Document non trouvé avec l'ID: " + idDoc);
                return false;
            }

            return documentDao.delete(idDoc);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression du document ID: " + idDoc, e);
            return false;
        }
    }

    /**
     * Vérifie si un document existe déjà pour un sociétaire et un type donné.
     *
     * @param idSocietaire ID du sociétaire
     * @param typeDoc Type de document
     * @return true si un document existe déjà, false sinon
     */
    public boolean documentExists(int idSocietaire, TypeDoc typeDoc) {
        try {
            return documentDao.documentExists(idSocietaire, typeDoc);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la vérification de l'existence du document", e);
            return false;
        }
    }

    /**
     * Récupère le chemin physique d'un document.
     *
     * @param idDoc ID du document
     * @return Optional contenant le chemin du document s'il existe, vide sinon
     */
    public Optional<Path> getDocumentPath(int idDoc) {
        try {
            return documentDao.getDocumentPath(idDoc);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération du chemin du document ID: " + idDoc, e);
            return Optional.empty();
        }
    }

    /**
     * Vérifie si les documents obligatoires sont présents pour un sociétaire.
     *
     * @param idSocietaire ID du sociétaire
     * @return true si tous les documents obligatoires sont présents, false sinon
     */
    public boolean hasRequiredDocuments(int idSocietaire) {
        try {
            return documentDao.hasRequiredDocuments(idSocietaire);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la vérification des documents obligatoires pour le sociétaire ID: " + idSocietaire, e);
            return false;
        }
    }

    /**
     * Compte les documents par type.
     *
     * @return Map contenant le nombre de documents par type ou map vide en cas d'erreur
     */
    public Map<TypeDoc, Integer> countDocumentsByType() {
        try {
            return documentDao.countDocumentsByType();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des documents par type", e);
            return Collections.emptyMap();
        }
    }

    /**
     * Compte les documents par sociétaire.
     *
     * @return Map contenant le nombre de documents par sociétaire ou map vide en cas d'erreur
     */
    public Map<Integer, Integer> countDocumentsBySocietaire() {
        try {
            return documentDao.countDocumentsBySocietaire();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des documents par sociétaire", e);
            return Collections.emptyMap();
        }
    }

    /**
     * Vérifie un fichier avant enregistrement (vérifie l'extension, la taille maximale, etc.).
     *
     * @param filePath Chemin du fichier à vérifier
     * @return true si le fichier est valide, false sinon
     */
    public boolean validateFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                LOGGER.warning("Le fichier n'existe pas: " + filePath);
                return false;
            }

            // Vérifier la taille du fichier (10 Mo maximum)
            long fileSize = Files.size(path);
            if (fileSize > 10 * 1024 * 1024) { // 10 Mo
                LOGGER.warning("Le fichier est trop volumineux: " + fileSize + " octets (maximum 10 Mo)");
                return false;
            }

            // Vérifier l'extension du fichier
            String fileName = path.getFileName().toString();
            String extension = getFileExtension(fileName).toLowerCase();

            if (extension.isEmpty() || !isAllowedExtension(extension)) {
                LOGGER.warning("Extension de fichier non autorisée: " + extension);
                return false;
            }

            return true;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la validation du fichier: " + filePath, e);
            return false;
        }
    }

    /**
     * Extrait l'extension d'un nom de fichier.
     *
     * @param fileName Nom du fichier
     * @return L'extension du fichier
     */
    private String getFileExtension(String fileName) {
        if (fileName == null) {
            return "";
        }
        int dot = fileName.lastIndexOf('.');
        if (dot < 0) {
            return "";
        }
        return fileName.substring(dot + 1);
    }

    /**
     * Vérifie si une extension est autorisée.
     *
     * @param extension Extension à vérifier
     * @return true si l'extension est autorisée, false sinon
     */
    private boolean isAllowedExtension(String extension) {
        return extension.equals("pdf") ||
                extension.equals("jpg") ||
                extension.equals("jpeg") ||
                extension.equals("png") ||
                extension.equals("doc") ||
                extension.equals("docx");
    }

    /**
     * Génère un rapport sur les documents manquants par sociétaire.
     *
     * @return Map avec l'ID du sociétaire comme clé et la liste des types de documents manquants comme valeur
     */
    public Map<Integer, List<TypeDoc>> getDocumentsManquants() {
        try {
            // Cette méthode est un exemple et devrait être implémentée dans le DAO ou adaptée selon les besoins
            // Pour l'instant, on renvoie une map vide
            return Collections.emptyMap();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la génération du rapport sur les documents manquants", e);
            return Collections.emptyMap();
        }
    }
}