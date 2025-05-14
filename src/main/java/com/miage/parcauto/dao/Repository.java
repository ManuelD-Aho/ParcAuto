package main.java.com.miage.parcauto.dao;

import java.util.List;
import java.util.Optional;

/**
 * Interface générique pour le pattern Repository.
 * Fournit les opérations CRUD de base pour toutes les entités.
 *
 * @param <T>  Type de l'entité
 * @param <ID> Type de l'identifiant de l'entité
 * @author MIAGE Holding
 * @version 1.0
 */
public interface Repository<T, ID> {
    /**
     * Recherche une entité par son identifiant.
     * 
     * @param id Identifiant de l'entité
     * @return Optional contenant l'entité si trouvée, sinon vide
     */
    Optional<T> findById(ID id);

    /**
     * Retourne la liste de toutes les entités.
     * 
     * @return Liste de toutes les entités
     */
    List<T> findAll();

    /**
     * Retourne une page d'entités.
     * 
     * @param page Numéro de page (0-based)
     * @param size Taille de la page
     * @return Liste paginée d'entités
     */
    List<T> findAll(int page, int size);

    /**
     * Sauvegarde une nouvelle entité.
     * 
     * @param entity Entité à sauvegarder
     * @return Entité persistée
     */
    T save(T entity);

    /**
     * Met à jour une entité existante.
     * 
     * @param entity Entité à mettre à jour
     * @return Entité mise à jour
     */
    T update(T entity);

    /**
     * Supprime une entité par son identifiant.
     * 
     * @param id Identifiant de l'entité
     * @return true si supprimée, false sinon
     */
    boolean delete(ID id);

    /**
     * Compte le nombre total d'entités.
     * 
     * @return Nombre total d'entités
     */
    long count();
}
