package main.java.com.miage.parcauto.model.finance;

import main.java.com.miage.parcauto.model.rh.Personnel; // Si un compte est toujours lié à un personnel interne

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entité représentant le compte financier d'un sociétaire.
 * Un sociétaire peut être un membre du personnel ou une entité externe.
 * Correspond à un enregistrement de la table SOCIETAIRE_COMPTE.
 */
public class SocietaireCompte implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer idSocietaire;
    private Personnel personnel; // Clé étrangère optionnelle vers PERSONNEL (id_personnel)
    private String nom; // Nom du sociétaire ou du compte
    private String numero; // Numéro de compte unique
    private BigDecimal solde;
    private String email;
    private String telephone;

    private List<Mouvement> mouvements; // Liste des mouvements financiers sur ce compte

    /**
     * Constructeur par défaut.
     */
    public SocietaireCompte() {
        this.solde = BigDecimal.ZERO; // Solde initial par défaut
        this.mouvements = new ArrayList<>();
    }

    /**
     * Constructeur avec les paramètres essentiels.
     *
     * @param idSocietaire L'identifiant unique du compte sociétaire.
     * @param nom          Le nom associé au compte (personne ou entité).
     * @param numero       Le numéro unique du compte.
     * @param solde        Le solde actuel du compte.
     */
    public SocietaireCompte(Integer idSocietaire, String nom, String numero, BigDecimal solde) {
        this();
        this.idSocietaire = idSocietaire;
        this.nom = nom;
        this.numero = numero;
        this.solde = (solde != null) ? solde : BigDecimal.ZERO;
    }

    /**
     * Constructeur complet.
     * @param idSocietaire L'identifiant du compte.
     * @param personnel Le membre du personnel associé (peut être null).
     * @param nom Le nom du titulaire du compte.
     * @param numero Le numéro de compte.
     * @param solde Le solde du compte.
     * @param email L'email de contact.
     * @param telephone Le téléphone de contact.
     */
    public SocietaireCompte(Integer idSocietaire, Personnel personnel, String nom, String numero,
                            BigDecimal solde, String email, String telephone) {
        this(idSocietaire, nom, numero, solde);
        this.personnel = personnel;
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

    public void setPersonnel(Personnel personnel) {
        this.personnel = personnel;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public List<Mouvement> getMouvements() {
        return mouvements;
    }

    public void setMouvements(List<Mouvement> mouvements) {
        this.mouvements = mouvements;
    }

    /**
     * Ajoute un mouvement au compte et met à jour le solde.
     * @param mouvement Le mouvement à ajouter.
     */
    public void addMouvement(Mouvement mouvement) {
        if (mouvement != null) {
            this.mouvements.add(mouvement);
            mouvement.setSocietaireCompte(this); // Assurer la bidirectionnalité
            // Mise à jour du solde en fonction du type de mouvement
            // Cette logique pourrait aussi être dans un service pour plus de robustesse
            if (mouvement.getMontant() != null && mouvement.getType() != null) {
                if (mouvement.getType() == TypeMouvement.DEPOT) {
                    this.solde = this.solde.add(mouvement.getMontant());
                } else if (mouvement.getType() == TypeMouvement.RETRAIT || mouvement.getType() == TypeMouvement.MENSUALITE) {
                    this.solde = this.solde.subtract(mouvement.getMontant());
                }
            }
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocietaireCompte that = (SocietaireCompte) o;
        return Objects.equals(idSocietaire, that.idSocietaire) ||
                (numero != null && Objects.equals(numero, that.numero));
    }

    @Override
    public int hashCode() {
        // Baser le hash sur le numéro de compte s'il est unique et non nul, sinon l'ID.
        if (numero != null) {
            return Objects.hash(numero);
        }
        return Objects.hash(idSocietaire);
    }

    @Override
    public String toString() {
        return "SocietaireCompte{" +
                "idSocietaire=" + idSocietaire +
                ", nom='" + nom + '\'' +
                ", numero='" + numero + '\'' +
                ", solde=" + solde +
                '}';
    }
}