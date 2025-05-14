package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.utilisateur.Utilisateur;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour la gestion des utilisateurs.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public interface UtilisateurRepository extends Repository<Utilisateur, Integer> {
    Optional<Utilisateur> findByLogin(String login);

    Optional<Utilisateur> authenticate(String login, String passwordHash);
    // ...autres méthodes spécifiques...
}
