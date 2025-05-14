package main.java.com.miage.parcauto.model.finance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime; // La table MOUVEMENT a un champ `date` de type DATETIME
import java.util.Objects;

/**
 * Entité représentant un mouvement financier (dépôt, retrait, mensualité)
 * sur un compte sociétaire.
 * Correspond à un enregistrement de la table MOUVEMENT.
 */
public class Mouvement implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id; // Nommé 'id' dans la table
    private SocietaireCompte societaireCompte; // Relation avec SocietaireCompte (via id_societaire)
    private LocalDateTime date; // Date et heure du mouvement
    private TypeMouvement type;
    private BigDecimal montant;
    // La table MOUVEMENT n'a pas de champ "libellé" direct pour le mouvement lui-même.
    // Si un libellé est nécessaire, il faudrait l'ajouter à la table ou le déduire.

    /**
     * Constructeur par défaut.
     */
    public Mouvement() {
    }

    /**
     * Constructeur avec tous les paramètres.
     *
     * @param id                L'identifiant unique du mouvement.
     * @param societaireCompte  Le compte sociétaire concerné par le mouvement.
     * @param date              La date et heure du mouvement.
     * @param type              Le type de mouvement (Depot, Retrait, Mensualite).
     * @param montant           Le montant du mouvement.
     */
    public Mouvement(Integer id, SocietaireCompte societaireCompte, LocalDateTime date,
                     TypeMouvement type, BigDecimal montant) {
        this.id = id;
        this.societaireCompte = societaireCompte;
        this.date = date;
        this.type = type;
        this.montant = montant;
    }

    // Getters et Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SocietaireCompte getSocietaireCompte() {
        return societaireCompte;
    }

    public void setSocietaireCompte(SocietaireCompte societaireCompte) {
        this.societaireCompte = societaireCompte;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public TypeMouvement getType() {
        return type;
    }

    public void setType(TypeMouvement type) {
        this.type = type;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mouvement mouvement = (Mouvement) o;
        return Objects.equals(id, mouvement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Mouvement{" +
                "id=" + id +
                ", type=" + type +
                ", montant=" + montant +
                ", date=" + date +
                ", compteId=" + (societaireCompte != null ? societaireCompte.getIdSocietaire() : "N/A") +
                '}';
    }
}