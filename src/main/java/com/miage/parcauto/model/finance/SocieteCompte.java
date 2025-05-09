package main.java.com.miage.parcauto.model.finance;

import main.java.com.miage.parcauto.model.rh.Personnel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Classe modèle représentant un compte sociétaire dans le système financier.
 * Correspond à la table SOCIETAIRE_COMPTE dans la base de données.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class SocieteCompte implements Serializable {

    private static final long serialVersionUID = 1L;

    // Attributs correspondant aux colonnes de la table SOCIETAIRE_COMPTE
    private Integer idSocietaire;
    private Personnel personnel;  // Relation avec le personnel associé
    private String nom;
    private String numero;
    private BigDecimal solde;
    private String email;
    private String telephone;
    private Integer idPersonnel;

    /**
     * Constructeur par défaut
     */
    public SocieteCompte() {
        this.solde = BigDecimal.ZERO;
    }

    /**
     * Constructeur avec les attributs essentiels
     *
     * @param personnel Le personnel associé au compte
     * @param nom Nom du sociétaire
     * @param numero Numéro de compte unique
     */
    public SocieteCompte(Personnel personnel, String nom, String numero) {
        this.personnel = personnel;
        this.nom = nom;
        this.numero = numero;
        this.solde = BigDecimal.ZERO;
        // Récupérer les informations de contact du personnel
        if (personnel != null) {
            this.email = personnel.getEmail();
            this.telephone = personnel.getTelephone();
        }
    }

    /**
     * Constructeur complet avec tous les attributs
     *
     * @param personnel Le personnel associé au compte
     * @param nom Nom du sociétaire
     * @param numero Numéro de compte unique
     * @param solde Solde initial du compte
     * @param email Email de contact
     * @param telephone Téléphone de contact
     */
    public SocieteCompte(Personnel personnel, String nom, String numero,
                         BigDecimal solde, String email, String telephone) {
        this.personnel = personnel;
        this.nom = nom;
        this.numero = numero;
        this.solde = solde;
        this.email = email;
        this.telephone = telephone;
    }

    // Getters et Setters

    public Integer getIdSocietaire() {
        return idSocietaire;
    }

    public void setIdSocietaire(Integer idSocietaire) {
        this.idSocietaire = idSocietaire;
    }

    public Personnel getPersonnel() {
        return personnel;
    }
    /**
     * Récupère l'ID du personnel associé au compte.
     *
     * @return ID du personnel
     */
    public Integer getIdPersonnel() {
        return idPersonnel;
    }


    // Modifier la méthode setPersonnel pour mettre à jour idPersonnel
    public void setPersonnel(Personnel personnel) {
        this.personnel = personnel;
        // Mise à jour de l'ID et des infos de contact
        if (personnel != null) {
            this.idPersonnel = personnel.getIdPersonnel();

            if (email == null || email.trim().isEmpty()) {
                this.email = personnel.getEmail();
            }
            if (telephone == null || telephone.trim().isEmpty()) {
                this.telephone = personnel.getTelephone();
            }
        }
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public String getEmail() {
        return email;
    }
    /**
     * Définit l'ID du personnel associé au compte.
     *
     * @param idPersonnel ID du personnel
     */
    public void setIdPersonnel(Integer idPersonnel) {
        this.idPersonnel = idPersonnel;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    // Méthodes métier

    /**
     * Effectue un dépôt sur le compte
     *
     * @param montant Le montant à déposer
     * @return true si l'opération a réussi, false sinon
     * @throws IllegalArgumentException si le montant est négatif ou nul
     */
    public boolean depot(BigDecimal montant) {
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant du dépôt doit être positif");
        }

        solde = solde.add(montant);
        return true;
    }

    /**
     * Effectue un retrait sur le compte
     *
     * @param montant Le montant à retirer
     * @return true si l'opération a réussi, false si le solde est insuffisant
     * @throws IllegalArgumentException si le montant est négatif ou nul
     */
    public boolean retrait(BigDecimal montant) {
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant du retrait doit être positif");
        }

        if (solde.compareTo(montant) < 0) {
            return false; // Solde insuffisant
        }

        solde = solde.subtract(montant);
        return true;
    }

    /**
     * Effectue un paiement de mensualité pour un véhicule sur 5 ans
     *
     * @param montant Le montant de la mensualité
     * @return true si l'opération a réussi, false si le solde est insuffisant
     * @throws IllegalArgumentException si le montant est négatif ou nul
     */
    public boolean mensualite(BigDecimal montant) {
        return retrait(montant); // Utilise la méthode retrait avec les mêmes règles
    }

    /**
     * Vérifie si le solde est positif
     *
     * @return true si le solde est supérieur ou égal à zéro
     */
    public boolean soldePositif() {
        return solde != null && solde.compareTo(BigDecimal.ZERO) >= 0;
    }

    /**
     * Vérifie si le compte est associé à un personnel
     *
     * @return true si le personnel est défini
     */
    public boolean aPersonnelAssocie() {
        return personnel != null;
    }

    /**
     * Calcule le montant mensuel pour un crédit sur 5 ans (60 mois)
     *
     * @param prixTotal Prix total du véhicule
     * @param tauxAnnuel Taux d'intérêt annuel en pourcentage (ex: 5 pour 5%)
     * @return le montant mensuel de la mensualité
     */
    public static BigDecimal calculerMensualite(BigDecimal prixTotal, BigDecimal tauxAnnuel) {
        if (prixTotal == null || tauxAnnuel == null) {
            return BigDecimal.ZERO;
        }

        // Conversion du taux annuel en taux mensuel
        BigDecimal tauxMensuel = tauxAnnuel
                .divide(BigDecimal.valueOf(12 * 100), 6, RoundingMode.HALF_UP);

        // Calcul selon la formule de mensualité
        // M = P * r * (1 + r)^n / ((1 + r)^n - 1)
        BigDecimal puissance = BigDecimal.ONE.add(tauxMensuel).pow(60);
        BigDecimal numerateur = prixTotal.multiply(tauxMensuel).multiply(puissance);
        BigDecimal denominateur = puissance.subtract(BigDecimal.ONE);

        return numerateur.divide(denominateur, 2, RoundingMode.HALF_UP);
    }

    /**
     * Calcule le solde restant dû après un certain nombre de mensualités payées
     *
     * @param prixTotal Prix total initial du véhicule
     * @param tauxAnnuel Taux d'intérêt annuel en pourcentage
     * @param mensualitePayees Nombre de mensualités déjà payées
     * @return le montant restant à payer
     */
    public static BigDecimal calculerSoldeRestant(BigDecimal prixTotal,
                                                  BigDecimal tauxAnnuel, int mensualitePayees) {
        if (prixTotal == null || tauxAnnuel == null || mensualitePayees >= 60) {
            return BigDecimal.ZERO;
        }

        if (mensualitePayees <= 0) {
            return prixTotal;
        }

        // Calcul du montant de la mensualité
        BigDecimal mensualite = calculerMensualite(prixTotal, tauxAnnuel);

        // Calcul du capital restant dû après n mensualités
        BigDecimal tauxMensuel = tauxAnnuel
                .divide(BigDecimal.valueOf(12 * 100), 6, RoundingMode.HALF_UP);

        BigDecimal un = BigDecimal.ONE;
        BigDecimal facteur = un.add(tauxMensuel).pow(60 - mensualitePayees);
        BigDecimal capitalRestant = mensualite
                .multiply(facteur.subtract(un))
                .divide(tauxMensuel, 2, RoundingMode.HALF_UP);

        return capitalRestant;
    }

    // Méthodes standard

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SocieteCompte compte = (SocieteCompte) obj;
        return Objects.equals(idSocietaire, compte.idSocietaire);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSocietaire);
    }

    @Override
    public String toString() {
        return nom + " (N° " + numero + ")";
    }

    /**
     * Valide les données essentielles du compte
     *
     * @return true si les données sont valides, false sinon
     */
    public boolean isValid() {
        return nom != null && !nom.trim().isEmpty()
                && numero != null && !numero.trim().isEmpty()
                && solde != null;
    }
}