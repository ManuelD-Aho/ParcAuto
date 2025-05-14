package main.java.com.miage.parcauto.dao;

import java.util.List;
import java.util.Optional;
import java.nio.file.Path;

/**
 * Repository pour la gestion des documents (remplace DocumentDao).
 * Fournit les opérations CRUD et des méthodes spécifiques pour les documents.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public interface DocumentRepository {
    List<Document> findAll();

    Optional<Document> findById(Integer id);

    List<Document> findBySocietaire(Integer idSocietaire);

    List<Document> findByType(TypeDoc typeDoc);

    boolean documentExists(Integer idSocietaire, TypeDoc typeDoc);

    boolean save(Path sourcePath, Integer idSocietaire, TypeDoc typeDoc, String nomOriginal);

    boolean replace(Integer idDoc, Path sourcePath, String nomOriginal);
    // ... autres méthodes spécifiques si besoin ...

    /**
     * Enumération des types de documents.
     */
    enum TypeDoc {
        CARTE_GRISE, ASSURANCE, CONTRAT_ENTRETIEN, AUTRE
    }

    /**
     * Classe représentant un document (à migrer depuis DocumentDao).
     */
    class Document {
        private Integer id;
        private Integer idSocietaire;
        private TypeDoc type;
        private String nomOriginal;
        private String chemin;
        // ...getters/setters...
    }
}
