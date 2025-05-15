package main.java.com.miage.parcauto.service;

import main.java.com.miage.parcauto.dao.DocumentRepository;
import main.java.com.miage.parcauto.model.document.DocumentSocietaire;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service pour la gestion des documents.
 * Ce service offre une couche d'abstraction au-dessus du repository et gère les opérations métier.
 */
public class DocumentService {

    private static final Logger LOGGER = Logger.getLogger(DocumentService.class.getName());

    private final DocumentRepository documentRepository;
    private final String baseStoragePath;

    /**
     * Constructeur avec injection de dépendances.
     * @param documentRepository Le repository pour les opérations de base de données.
     * @param baseStoragePath Le chemin de base pour le stockage des documents.
     */
    public DocumentService(DocumentRepository documentRepository, String baseStoragePath) {
        this.documentRepository = documentRepository;
        this.baseStoragePath = baseStoragePath;
    }

    /**
     * Récupère un document par son ID.
     * @param id L'ID du document.
     * @return Un Optional contenant le document s'il est trouvé.
     */
    public Optional<DocumentSocietaire> getDocumentById(Integer id) {
        return documentRepository.findById(id);
    }

    /**
     * Récupère tous les documents d'un sociétaire.
     * @param societaire Le sociétaire concerné.
     * @return Une liste de documents.
     */
    public List<DocumentSocietaire> getDocumentsBySocietaire(Societaire societaire) {
        return documentRepository.findBySocietaire(societaire);
    }

    /**
     * Récupère tous les documents d'un type spécifique.
     * @param typeDocument Le type de document.
     * @return Une liste de documents.
     */
    public List<DocumentSocietaire> getDocumentsByType(TypeDocumentSocietaire typeDocument) {
        return documentRepository.findByType(typeDocument);
    }

    /**
     * Sauvegarde un nouveau document avec son contenu.
     * @param document Le document à sauvegarder.
     * @param fileContent Le contenu du fichier.
     * @return Le document sauvegardé.
     * @throws IOException Si une erreur survient lors de l'écriture du fichier.
     */
    public DocumentSocietaire saveDocument(DocumentSocietaire document, byte[] fileContent) throws IOException {
        if (document == null || document.getSocietaire() == null) {
            throw new IllegalArgumentException("Le document et son sociétaire ne peuvent pas être null.");
        }

        // Créer le dossier pour le sociétaire si nécessaire
        String societaireFolderPath = baseStoragePath + "/" + document.getSocietaire().getIdSocietaire();
        Files.createDirectories(Paths.get(societaireFolderPath));

        // Générer un nom unique pour le fichier
        String fileName = System.currentTimeMillis() + "_" + document.getNomDocument();
        String filePath = societaireFolderPath + "/" + fileName;

        // Écrire le fichier sur le disque
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(fileContent);
        }

        // Mettre à jour les détails du document
        document.setCheminAccesDocument(filePath);
        document.setDateUploadDocument(LocalDateTime.now());
        document.setTailleDocumentKo(fileContent.length / 1024); // Convertir en Ko

        // Sauvegarder en base de données
        return documentRepository.save(document);
    }

    /**
     * Supprime un document par son ID.
     * Cette méthode supprime à la fois l'enregistrement en base et le fichier physique.
     * @param id L'ID du document à supprimer.
     * @return true si la suppression a réussi, false sinon.
     */
    public boolean deleteDocument(Integer id) {
        Optional<DocumentSocietaire> documentOpt = documentRepository.findById(id);
        if (documentOpt.isEmpty()) {
            return false;
        }

        DocumentSocietaire document = documentOpt.get();

        // Supprimer le fichier physique
        File file = new File(document.getCheminAccesDocument());
        boolean fileDeleted = file.delete();

        if (!fileDeleted) {
            LOGGER.log(Level.WARNING, "Impossible de supprimer le fichier physique: {0}", document.getCheminAccesDocument());
        }

        // Supprimer l'enregistrement en base de données
        return documentRepository.delete(id);
    }

    /**
     * Récupère le contenu d'un document par son ID.
     * @param id L'ID du document.
     * @return Un tableau d'octets contenant le contenu du document, ou null si non trouvé.
     * @throws IOException Si une erreur survient lors de la lecture du fichier.
     */
    public byte[] getDocumentContent(Integer id) throws IOException {
        Optional<DocumentSocietaire> documentOpt = documentRepository.findById(id);
        if (documentOpt.isEmpty()) {
            return null;
        }

        DocumentSocietaire document = documentOpt.get();
        File file = new File(document.getCheminAccesDocument());
        if (!file.exists()) {
            throw new IOException("Le fichier n'existe pas: " + document.getCheminAccesDocument());
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            return fis.readAllBytes();
        }
    }
}