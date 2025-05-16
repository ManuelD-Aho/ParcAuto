package main.java.com.miage.parcauto.dto;

import java.time.LocalDateTime;

public class DocumentSocietaireDTO {
    private Integer idDocument;
    private Integer idSocietaire;
    private String typeDocument;
    private LocalDateTime dateDebutValidite;
    private LocalDateTime dateFinValidite;
    private String cheminFichier;

    public DocumentSocietaireDTO() {
    }

    public Integer getIdDocument() {
        return idDocument;
    }

    public void setIdDocument(Integer idDocument) {
        this.idDocument = idDocument;
    }

    public Integer getIdSocietaire() {
        return idSocietaire;
    }

    public void setIdSocietaire(Integer idSocietaire) {
        this.idSocietaire = idSocietaire;
    }

    public String getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

    public LocalDateTime getDateDebutValidite() {
        return dateDebutValidite;
    }

    public void setDateDebutValidite(LocalDateTime dateDebutValidite) {
        this.dateDebutValidite = dateDebutValidite;
    }

    public LocalDateTime getDateFinValidite() {
        return dateFinValidite;
    }

    public void setDateFinValidite(LocalDateTime dateFinValidite) {
        this.dateFinValidite = dateFinValidite;
    }

    public String getCheminFichier() {
        return cheminFichier;
    }

    public void setCheminFichier(String cheminFichier) {
        this.cheminFichier = cheminFichier;
    }
}