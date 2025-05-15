package main.java.com.miage.parcauto.dto;

import main.java.com.miage.parcauto.model.document.TypeDocument;

import java.io.Serializable;
import java.time.LocalDateTime;

public class DocumentSocietaireDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idDoc;
    private Integer idSocietaire;
    private String societaireInfo;
    private TypeDocument typeDoc;
    private String cheminFichier;
    private LocalDateTime dateUpload;

    public DocumentSocietaireDTO() {
    }

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

    public TypeDocument getTypeDoc() {
        return typeDoc;
    }

    public void setTypeDoc(TypeDocument typeDoc) {
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

    @Override
    public String toString() {
        return "DocumentSocietaireDTO{" +
                "idDoc=" + idDoc +
                ", societaireInfo='" + societaireInfo + '\'' +
                ", typeDoc=" + (typeDoc != null ? typeDoc.getValeur() : "N/A") +
                ", cheminFichier='" + cheminFichier + '\'' +
                '}';
    }
}