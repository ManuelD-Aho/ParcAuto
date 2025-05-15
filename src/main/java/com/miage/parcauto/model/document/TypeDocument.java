package main.java.com.miage.parcauto.model.document;

/**
 * Types de documents associés à un sociétaire.
 */
public enum TypeDocument {
    CARTE_GRISE("CarteGrise"),
    ASSURANCE("Assurance"),
    ID("ID"),
    FACTURE("Facture"),
    CONTRAT("Contrat"),
    ACCORD("Accord"),
    ATTESTATION("Attestation"),
    AUTRE("Autre"),
    PERMIS("Permis");

    private final String valeur;

    TypeDocument(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }

    public static TypeDocument fromString(String text) {
        for (TypeDocument type : TypeDocument.values()) {
            if (type.valeur.equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Type de document inconnu: " + text);
    }
}