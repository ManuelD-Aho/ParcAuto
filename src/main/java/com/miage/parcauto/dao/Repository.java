package main.java.com.miage.parcauto.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Interface générique pour les opérations de persistance des données (DAO).
 * Définit les méthodes CRUD de base ainsi que des fonctionnalités de recherche.
 *
 * @param <T>  Le type de l'entité gérée par ce repository.
 * @param <ID> Le type de l'identifiant de l'entité (généralement Integer ou Long).
 */
public interface Repository<T, ID extends Serializable> {

    /**
     * Recherche une entité par son identifiant unique.
     *
     * @param id L'identifiant de l'entité à rechercher.
     * @return Un Optional contenant l'entité si trouvée, sinon un Optional vide.
     */
    Optional<T> findById(ID id);

    /**
     * Récupère toutes les entités de ce type.
     * Attention : peut être coûteux pour de grandes tables.
     *
     * @return Une liste de toutes les entités.
     */
    List<T> findAll();

    /**
     * Récupère une page d'entités.
     *
     * @param page Le numéro de la page à récupérer (commençant à 0 ou 1 selon l'implémentation).
     * @param size Le nombre d'entités par page.
     * @return Une liste d'entités pour la page demandée.
     */
    List<T> findAll(int page, int size);

    /**
     * Sauvegarde une nouvelle entité ou met à jour une entité existante.
     * Si l'entité a un ID null ou non existant, elle est généralement créée.
     * Sinon, elle est mise à jour.
     *
     * @param entity L'entité à sauvegarder ou à mettre à jour.
     * @return L'entité sauvegardée (peut inclure un ID généré ou des champs mis à jour).
     */
    T save(T entity);

    /**
     * Met à jour une entité existante.
     * Contrairement à save, cette méthode est spécifiquement pour la mise à jour.
     *
     * @param entity L'entité à mettre à jour.
     * @return L'entité mise à jour.
     * @throws java.util.NoSuchElementException si l'entité n'existe pas pour la mise à jour.
     */
    T update(T entity);

    /**
     * Supprime une entité par son identifiant.
     *
     * @param id L'identifiant de l'entité à supprimer.
     * @return true si la suppression a réussi, false sinon.
     */
    boolean delete(ID id);

    /**
     * Compte le nombre total d'entités de ce type.
     *
     * @return Le nombre total d'entités.
     */
    long count();
}