package main.java.com.miage.parcauto.model.rh;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Entité représentant un sociétaire (peut être l'entreprise elle-même ou une entité associée).
 * Utilisé principalement pour lier les comptes financiers et les documents.
 */
public class Societaire implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer idSocietaire;
    private String nomSocietaire; // Nom de l'entreprise ou du sociétaire
    private String identifiantFiscal; // SIRET, NIF, etc.
    // Autres champs pertinents pour un sociétaire

    public Societaire() {
    }

    public Societaire(Integer idSocietaire, String nomSocietaire) {
        this.idSocietaire = idSocietaire;
        this.nomSocietaire = nomSocietaire;
    }

    public Integer getIdSocietaire() {
        return idSocietaire;
    }

    public void setIdSocietaire(Integer idSocietaire) {
        this.idSocietaire = idSocietaire;
    }

    public String getNomSocietaire() {
        return nomSocietaire;
    }

    public void setNomSocietaire(String nomSocietaire) {
        this.nomSocietaire = nomSocietaire;
    }

    public String getIdentifiantFiscal() {
        return identifiantFiscal;
    }

    public void setIdentifiantFiscal(String identifiantFiscal) {
        this.identifiantFiscal = identifiantFiscal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Societaire that = (Societaire) o;
        return Objects.equals(idSocietaire, that.idSocietaire);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSocietaire);
    }

    @Override
    public String toString() {
        return "Societaire{" +
                "idSocietaire=" + idSocietaire +
                ", nomSocietaire='" + nomSocietaire + '\'' +
                '}';
    }
}