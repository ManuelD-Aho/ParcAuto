package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.notification.Notification;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface DAO pour l'entité Notification.
 * Fournit des opérations de persistance pour les notifications système ou utilisateur.
 */
public interface NotificationRepository extends Repository<Notification, Integer> {

    /**
     * Recherche toutes les notifications pour un utilisateur spécifique.
     *
     * @param conn la connexion à la base de données.
     * @param idUtilisateur l'identifiant de l'utilisateur.
     * @return une liste des notifications pour cet utilisateur.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<Notification> findByUtilisateurId(Connection conn, Integer idUtilisateur) throws SQLException;

    /**
     * Recherche toutes les notifications pour un utilisateur spécifique, filtrées par l'état de lecture.
     *
     * @param conn la connexion à la base de données.
     * @param idUtilisateur l'identifiant de l'utilisateur.
     * @param estLu true pour ne récupérer que les notifications lues, false pour les non lues.
     * @return une liste des notifications correspondantes.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<Notification> findByUtilisateurIdAndEstLu(Connection conn, Integer idUtilisateur, boolean estLu) throws SQLException;

    /**
     * Marque plusieurs notifications comme lues.
     * @param conn la connexion à la base de données.
     * @param idsNotification la liste des IDs des notifications à marquer comme lues.
     * @return le nombre de notifications mises à jour.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    int markAsRead(Connection conn, List<Integer> idsNotification) throws SQLException;
}