package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.utilisateur.Personnel;
import java.util.List;
import java.util.Optional;

/**
 * Interface de repository pour la gestion du personnel.
 * Étend l'interface Repository générique avec des méthodes spécifiques au
 * personnel.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public interface PersonnelRepository extends Repository<Personnel, Integer> {

    /**
     * Trouve un membre du personnel par son nom d'utilisateur.
     *
     * @param nomUtilisateur Le nom d'utilisateur à rechercher
     * @return Un Optional contenant le personnel s'il existe
     */
    Optional<Personnel> findByNomUtilisateur(String nomUtilisateur);

    /**
     * Trouve un membre du personnel par son email.
     *
     * @param email L'email à rechercher
     * @return Un Optional contenant le personnel s'il existe
     */
    Optional<Personnel> findByEmail(String email);

    /**
     * Trouve tous les membres du personnel ayant un rôle spécifique.
     *
     * @param role Le rôle à rechercher
     * @return Liste du personnel ayant le rôle spécifié
     */
    List<Personnel> findAllByRole(String role);

    /**
     * Trouve tous les membres du personnel actifs.
     *
     * @return Liste du personnel actif
     */
    List<Personnel> findAllActifs();

    /**
     * Trouve tous les membres du personnel inactifs.
     *
     * @return Liste du personnel inactif
     */
    List<Personnel> findAllInactifs();

    /**
     * Met à jour le mot de passe d'un membre du personnel.
     *
     * @param idPersonnel       L'identifiant du membre du personnel
     * @param nouveauMotDePasse Le nouveau mot de passe haché
     * @param nouveauSel        Le nouveau sel pour le hachage
     * @return true si la mise à jour a réussi, false sinon
     */
    boolean updateMotDePasse(Integer idPersonnel, String nouveauMotDePasse, String nouveauSel);

    /**
     * Désactive un membre du personnel.
     *
     * @param idPersonnel L'identifiant du membre du personnel
     * @return true si la désactivation a réussi, false sinon
     */
    boolean desactiver(Integer idPersonnel);

    /**
     * Réactive un membre du personnel.
     *
     * @param idPersonnel L'identifiant du membre du personnel
     * @return true si la réactivation a réussi, false sinon
     */
    boolean reactiver(Integer idPersonnel);
}
