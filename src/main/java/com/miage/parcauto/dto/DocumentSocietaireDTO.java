package main.java.com.miage.parcauto.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO pour l'entité DocumentSocietaire.
 */
public class DocumentSocietaireDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idDoc;
    private Integer idSocietaire;
    private String societaireInfo;
    private String typeDoc;
    private String cheminFichier;
    private LocalDateTime dateUpload;

    public DocumentSocietaireDTO() {
    }

    // Getters et Setters
    public Integer getIdDoc() {
        return idDoc;
    }

    public void setIdDoc(Integer idDoc) {
        this.idDoc = idDoc;
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

    public String getTypeDoc() {
        return typeDoc;
    }

    public void setTypeDoc(String typeDoc) {
        this.typeDoc = typeDoc;
    }

    public String getCheminFichier() {
        return cheminFichier;
    }

    public void setCheminFichier(String cheminFichier) {
        this.cheminFichier = cheminFichier;
    }

    public LocalDateTime getDateUpload() {
        return dateUpload;
    }

    public void setDateUpload(LocalDateTime dateUpload) {
        this.dateUpload = dateUpload;
    }

    public String getNomFichier() {
        if (cheminFichier == null) {
            return "";
        }
        int lastIndex = cheminFichier.lastIndexOf('/');
        if (lastIndex == -1) {
            lastIndex = cheminFichier.lastIndexOf('\\');
        }
        if (lastIndex == -1) {
            return cheminFichier;
        }
        return cheminFichier.substring(lastIndex + 1);
    }

    @Override
    public String toString() {
        return typeDoc + " - " + getNomFichier();
    }
}