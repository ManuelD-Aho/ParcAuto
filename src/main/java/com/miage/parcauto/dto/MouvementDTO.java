package main.java.com.miage.parcauto.dto;

import main.java.com.miage.parcauto.model.finance.TypeMouvement;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MouvementDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer idSocietaire;
    private String societaireInfo;
    private LocalDateTime date;
    private TypeMouvement type;
    private BigDecimal montant;

    public MouvementDTO() {
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

    public String getSocietaireInfo() {
        return societaireInfo;
    }

    public void setSocietaireInfo(String societaireInfo) {
        this.societaireInfo = societaireInfo;
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
    public String toString() {
        return "MouvementDTO{" +
                "id=" + id +
                ", societaireInfo='" + societaireInfo + '\'' +
                ", type=" + (type != null ? type.getValeur() : "N/A") +
                ", montant=" + montant +
                ", date=" + date +
                '}';
    }
}