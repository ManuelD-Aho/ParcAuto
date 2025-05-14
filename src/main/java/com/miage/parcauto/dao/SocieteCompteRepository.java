package main.java.com.miage.parcauto.dao;

import main.java.com.miage.parcauto.model.finance.SocieteCompte;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Interface de repository pour la gestion des comptes sociétaires.
 * Étend l'interface Repository générique avec des méthodes spécifiques aux
 * comptes.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public interface SocieteCompteRepository extends Repository<SocieteCompte, Integer> {

    /**
     * Trouve un compte sociétaire par son numéro de compte.
     *
     * @param numeroCompte Le numéro du compte à rechercher
     * @return Un Optional contenant le compte s'il existe
     */
    Optional<SocieteCompte> findByNumeroCompte(String numeroCompte);

    /**
     * Récupère tous les comptes avec un solde supérieur à un montant spécifié.
     *
     * @param montant Le montant minimum du solde
     * @return Liste des comptes avec un solde supérieur au montant spécifié
     */
    List<SocieteCompte> findAllBySoldeGreaterThan(BigDecimal montant);

    /**
     * Récupère tous les comptes avec un solde inférieur à un montant spécifié.
     *
     * @param montant Le montant maximum du solde
     * @return Liste des comptes avec un solde inférieur au montant spécifié
     */
    List<SocieteCompte> findAllBySoldeLessThan(BigDecimal montant);

    /**
     * Récupère les comptes appartenant à un membre du personnel spécifique.
     *
     * @param idPersonnel L'identifiant du membre du personnel
     * @return Liste des comptes appartenant au personnel spécifié
     */
    List<SocieteCompte> findAllByPersonnel(Integer idPersonnel);

    /**
     * Met à jour le solde d'un compte.
     *
     * @param idCompte     L'identifiant du compte
     * @param nouveauSolde Le nouveau solde à appliquer
     * @return true si la mise à jour a réussi, false sinon
     */
    boolean updateSolde(Integer idCompte, BigDecimal nouveauSolde);
}
