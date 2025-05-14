package main.java.com.miage.parcauto.model.document;

/**
 * Énumération représentant les types de documents pouvant être associés à un sociétaire.
 * Correspond à la colonne `type_doc` de la table `DOCUMENT_SOCIETAIRE`.
 */
public enum TypeDocumentSocietaire {
    CARTE_GRISE("CarteGrise", "Carte Grise"),
    ASSURANCE_DOCUMENT("Assurance", "Attestation d'Assurance"), // Nom distinct pour éviter confusion avec entité Assurance
    ID("ID", "Pièce d'Identité"), // CNI, Passeport, etc.
    PERMIS("Permis", "Permis de Conduire");

    private final String valeurDb;
    private final String libelle;

    /**
     * Constructeur pour l'énumération TypeDocumentSocietaire.
     * @param valeurDb La valeur telle qu'elle est stockée en base de données.
     * @param libelle Le libellé lisible du type de document.
     */
    TypeDocumentSocietaire(String valeurDb, String libelle) {
        this.valeurDb = valeurDb;
        this.libelle = libelle;
    }

    /**
     * Retourne le libellé lisible du type de document.
     * @return Le libellé en français.
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Retourne la valeur du type de document telle qu'elle est stockée en base de données.
     * @return La valeur pour la base de données.
     */
    public String getValeurDb() {
        return valeurDb;
    }

    /**
     * Retrouve un TypeDocumentSocietaire à partir de sa valeur en base de données ou de son libellé.
     * @param value La chaîne de caractères à mapper.
     * @return Le TypeDocumentSocietaire correspondant.
     * @throws IllegalArgumentException si la valeur n'est pas reconnue.
     */
    public static TypeDocumentSocietaire fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("La valeur pour TypeDocumentSocietaire ne peut pas être nulle.");
        }
        for (TypeDocumentSocietaire typeDoc : values()) {
            if (typeDoc.valeurDb.equalsIgnoreCase(value) ||
                    typeDoc.libelle.equalsIgnoreCase(value) ||
                    typeDoc.name().equalsIgnoreCase(value.replace(" ", "_"))) {
                return typeDoc;
            }
        }
        throw new IllegalArgumentException("Type de document sociétaire inconnu : " + value);
    }

    /**
     * Retourne la valeur à stocker en base de données.
     * @return La valeur exacte telle que définie dans l'ENUM de la base de données.
     */
    public String toDbValue() {
        return this.valeurDb;
    }
}