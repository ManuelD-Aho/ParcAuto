package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.assurance.Assurance;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface DAO pour l'entité Assurance.
 * Fournit des opérations de persistance spécifiques pour les contrats d'assurance.
 */
public interface AssuranceRepository extends Repository<Assurance, Integer> {

    /**
     * Recherche les assurances qui expirent bientôt, c'est-à-dire avant ou à la date limite fournie.
     *
     * @param conn la connexion à la base de données.
     * @param dateLimite la date limite d'expiration.
     * @return une liste des assurances expirant bientôt.
     * @throws SQLException si une erreur d'accès à la base de données se produit.
     */
    List<Assurance> findExpiringSoon(Connection conn, LocalDateTime dateLimite) throws SQLException;
}