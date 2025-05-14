package main.java.com.miage.parcauto.model.document;

import main.java.com.miage.parcauto.model.finance.SocietaireCompte;

import java.io.Serializable;
import java.time.LocalDateTime; // La DB utilise DATETIME pour date_upload
import java.util.Objects;

/**
 * Entité représentant un document associé à un sociétaire (ex: carte grise, permis).
 * Correspond à un enregistrement de la table DOCUMENT_SOCIETAIRE.
 */
public class DocumentSocietaire implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idDoc;
    private SocietaireCompte societaireCompte; // Relation avec SocietaireCompte (via id_societaire)
    private TypeDocumentSocietaire typeDoc;
    private String cheminFichier; // Chemin d'accès au fichier sur le serveur ou URL
    private LocalDateTime dateUpload;

    /**
     * Constructeur par défaut.
     */
    public DocumentSocietaire() {
        this.dateUpload = LocalDateTime.now(); // Date d'upload par défaut
    }

    /**
     * Constructeur avec tous les paramètres.
     *
     * @param idDoc             L'identifiant unique du document.
     * @param societaireCompte  Le compte sociétaire auquel ce document est rattaché.
     * @param typeDoc           Le type de document.
     * @param cheminFichier     Le chemin d'accès ou l'URL du fichier.
     * @param dateUpload        La date et heure de l'upload du document.
     */
    public DocumentSocietaire(Integer idDoc, SocietaireCompte societaireCompte, TypeDocumentSocietaire typeDoc,
                              String cheminFichier, LocalDateTime dateUpload) {
        this.idDoc = idDoc;
        this.societaireCompte = societaireCompte;
        this.typeDoc = typeDoc;
        this.cheminFichier = cheminFichier;
        this.dateUpload = (dateUpload != null) ? dateUpload : LocalDateTime.now();
    }

    // Getters et Setters

    public Integer getIdDoc() {
        return idDoc;
    }

    public void setIdDoc(Integer idDoc) {
        this.idDoc = idDoc;
    }

    public SocietaireCompte getSocietaireCompte() {
        return societaireCompte;
    }

    public void setSocietaireCompte(SocietaireCompte societaireCompte) {
        this.societaireCompte = societaireCompte;
    }

    public TypeDocumentSocietaire getTypeDoc() {
        return typeDoc;
    }

    public void setTypeDoc(TypeDocumentSocietaire typeDoc) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentSocietaire that = (DocumentSocietaire) o;
        return Objects.equals(idDoc, that.idDoc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDoc);
    }

    @Override
    public String toString() {
        return "DocumentSocietaire{" +
                "idDoc=" + idDoc +
                ", typeDoc=" + typeDoc +
                ", cheminFichier='" + cheminFichier + '\'' +
                ", societaireId=" + (societaireCompte != null ? societaireCompte.getIdSocietaire() : "N/A") +
                '}';
    }
}