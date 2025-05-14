package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.utilisateur.Societaire;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour la gestion des sociétaires.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public interface SocietaireRepository extends Repository<Societaire, Integer> {
    List<Societaire> findByNom(String nom);
    // ...autres méthodes spécifiques...
}
