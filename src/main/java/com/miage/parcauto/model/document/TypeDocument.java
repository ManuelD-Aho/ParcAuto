package main.java.com.miage.parcauto.model.document;

public enum TypeDocument {
    CARTE_GRISE("CarteGrise"),
    ASSURANCE("Assurance"),
    ID("ID"),
    PERMIS("Permis");

    private final String libelle;

    TypeDocument(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public static TypeDocument fromLibelle(String libelle) {
        for (TypeDocument type : TypeDocument.values()) {
            if (type.libelle.equalsIgnoreCase(libelle)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Aucun type de document ne correspond au libellé : " + libelle);
    }

    @Override
    public String toString() {
        return this.libelle;
    }
}