package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.document.DocumentSocietaire;

import java.util.List;

/**
 * Interface spécifique pour les opérations sur les documents.
 * Étend l'interface générique Repository pour les DocumentSocietaire.
 */
public interface DocumentRepository extends Repository<DocumentSocietaire, Integer> {

    /**
     * Recherche les documents par sociétaire.
     * @param societaire Le sociétaire concerné.
     * @return La liste des documents associés à ce sociétaire.
     */
    List<DocumentSocietaire> findBySocietaire(Societaire societaire);

    /**
     * Recherche les documents par type.
     * @param typeDocument Le type de document recherché.
     * @return La liste des documents de ce type.
     */
    List<DocumentSocietaire> findByType(TypeDocumentSocietaire typeDocument);

    /**
     * Recherche les documents par nom ou partie du nom.
     * @param nomPartiel La partie du nom recherchée.
     * @return La liste des documents dont le nom contient la chaîne recherchée.
     */
    List<DocumentSocietaire> findByNomContaining(String nomPartiel);
}