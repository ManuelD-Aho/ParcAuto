package main.java.com.miage.parcauto.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MouvementDTO {
    private Integer idMouvement;
    private Integer idSocietaireCompte;
    private String typeMouvement;
    private BigDecimal montant;
    private LocalDateTime dateMouvement;
    private String description;

    public MouvementDTO() {
    }

    public Integer getIdMouvement() {
        return idMouvement;
    }

    public void setIdMouvement(Integer idMouvement) {
        this.idMouvement = idMouvement;
    }

    public Integer getIdSocietaireCompte() {
        return idSocietaireCompte;
    }

    public void setIdSocietaireCompte(Integer idSocietaireCompte) {
        this.idSocietaireCompte = idSocietaireCompte;
    }

    public String getTypeMouvement() {
        return typeMouvement;
    }

    public void setTypeMouvement(String typeMouvement) {
        this.typeMouvement = typeMouvement;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public LocalDateTime getDateMouvement() {
        return dateMouvement;
    }

    public void setDateMouvement(LocalDateTime dateMouvement) {
        this.dateMouvement = dateMouvement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}