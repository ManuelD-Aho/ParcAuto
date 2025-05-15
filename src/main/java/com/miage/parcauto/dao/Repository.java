package main.java.com.miage.parcauto.dao;

import java.util.List;
import java.util.Optional;

/**
 * Interface générique pour les opérations CRUD d'un repository.
 * @param <T> Le type de l'entité.
 * @param <ID> Le type de l'identifiant de l'entité.
 */
public interface Repository<T, ID> {

    /**
     * Récupère une entité par son identifiant.
     * @param id L'identifiant de l'entité.
     * @return Un Optional contenant l'entité si trouvée, sinon Optional.empty().
     */
    Optional<T> findById(ID id);

    /**
     * Récupère toutes les entités.
     * @return Une liste de toutes les entités.
     */
    List<T> findAll();

    /**
     * Récupère toutes les entités de manière paginée.
     * @param page Le numéro de la page (commence à 0).
     * @param size Le nombre d'éléments par page.
     * @return Une liste d'entités pour la page et la taille données.
     */
    List<T> findAll(int page, int size);

    /**
     * Sauvegarde une entité (création ou mise à jour).
     * Si l'entité a un ID nul, une création est tentée.
     * Si l'entité a un ID non nul, une mise à jour est tentée.
     * @param entity L'entité à sauvegarder.
     * @return L'entité sauvegardée (avec son ID mis à jour si création).
     */
    T save(T entity);

    /**
     * Met à jour une entité existante.
     * @param entity L'entité à mettre à jour.
     * @return L'entité mise à jour, ou null si la mise à jour a échoué (ex: entité non trouvée).
     */
    T update(T entity);


    /**
     * Supprime une entité par son identifiant.
     * @param id L'identifiant de l'entité à supprimer.
     * @return true si la suppression a réussi, false sinon.
     */
    boolean delete(ID id);

    /**
     * Compte le nombre total d'entités.
     * @return Le nombre total d'entités.
     */
    long count();
}