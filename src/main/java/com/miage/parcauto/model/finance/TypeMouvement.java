package main.java.com.miage.parcauto.model.finance;

/**
 * Types de mouvement financier.
 */
public enum TypeMouvement {
    DEPOT("Depot"),
    RETRAIT("Retrait"),
    MENSUALITE("Mensualite");

    private final String valeur;

    TypeMouvement(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }

    public static TypeMouvement fromString(String text) {
        for (TypeMouvement type : TypeMouvement.values()) {
            if (type.valeur.equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Type de mouvement inconnu: " + text);
    }
}