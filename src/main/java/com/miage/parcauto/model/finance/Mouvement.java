package main.java.com.miage.parcauto.model.finance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entité représentant un mouvement financier.
 */
public class Mouvement implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private SocietaireCompte societaire;
    private LocalDateTime date;
    private TypeMouvement type;
    private BigDecimal montant;

    public Mouvement() {
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SocietaireCompte getSocietaire() {
        return societaire;
    }

    public void setSocietaire(SocietaireCompte societaire) {
        this.societaire = societaire;
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
        return type + ": " + montant + "€ le " + date;
    }
}