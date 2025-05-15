package main.java.com.miage.parcauto.model.entretien;

/**
 * Types d'entretien pour un véhicule.
 */
public enum TypeEntretien {
    PREVENTIF("Preventif"),
    CORRECTIF("Correctif");

    private final String valeur;

    TypeEntretien(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }

    public static TypeEntretien fromString(String text) {
        for (TypeEntretien type : TypeEntretien.values()) {
            if (type.valeur.equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Type d'entretien inconnu: " + text);
    }
}