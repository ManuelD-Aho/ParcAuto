package main.java.com.miage.parcauto.model.finance;

import java.math.BigDecimal;
import java.time.LocalDateTime;
// import main.java.com.miage.parcauto.model.finance.TypeMouvement;

public class Mouvement {

    private Integer id;
    private Integer idSocietaire;
    private LocalDateTime dateMouvement; // SQL: date
    private TypeMouvement typeMouvement; // SQL: type
    private BigDecimal montant;

    public Mouvement() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdSocietaire() {
        return idSocietaire;
    }

    public void setIdSocietaire(Integer idSocietaire) {
        this.idSocietaire = idSocietaire;
    }

    public LocalDateTime getDateMouvement() {
        return dateMouvement;
    }

    public void setDateMouvement(LocalDateTime dateMouvement) {
        this.dateMouvement = dateMouvement;
    }

    public TypeMouvement getTypeMouvement() {
        return typeMouvement;
    }

    public void setTypeMouvement(TypeMouvement typeMouvement) {
        this.typeMouvement = typeMouvement;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }
}