package main.java.com.miage.parcauto.model.document;

import java.time.LocalDateTime;

// Assurez-vous que l'enum TypeDocument est bien défini dans ce package ou importé correctement
// import main.java.com.miage.parcauto.model.document.TypeDocument;

public class DocumentSocietaire {

    private Integer idDocument; // Corresponds à id_doc
    private Integer idSocietaire;
    private TypeDocument typeDocument; // Corresponds à type_doc
    private String cheminFichier;
    private LocalDateTime dateUpload;
    private Integer idCompteSocietaire;
    private TypeDocument type;
    private String nomFichier;
    private LocalDateTime dateAjout;
    private LocalDateTime dateExpiration;
    private String observation;

    public DocumentSocietaire() {
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

    public TypeDocument getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(TypeDocument typeDocument) {
        this.typeDocument = typeDocument;
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

    public Integer getIdCompteSocietaire() {
        return idCompteSocietaire != null ? idCompteSocietaire : idSocietaire;
    }

    public void setIdCompteSocietaire(Integer idCompteSocietaire) {
        this.idCompteSocietaire = idCompteSocietaire;
        this.idSocietaire = idCompteSocietaire;
    }

    public TypeDocument getType() {
        return type != null ? type : typeDocument;
    }

    public void setType(TypeDocument type) {
        this.type = type;
        this.typeDocument = type;
    }

    public String getNomFichier() {
        return nomFichier != null ? nomFichier : cheminFichier;
    }

    public void setNomFichier(String nomFichier) {
        this.nomFichier = nomFichier;
        this.cheminFichier = nomFichier;
    }

    public LocalDateTime getDateAjout() {
        return dateAjout != null ? dateAjout : dateUpload;
    }

    public void setDateAjout(LocalDateTime dateAjout) {
        this.dateAjout = dateAjout;
        this.dateUpload = dateAjout;
    }

    public LocalDateTime getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDateTime dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}