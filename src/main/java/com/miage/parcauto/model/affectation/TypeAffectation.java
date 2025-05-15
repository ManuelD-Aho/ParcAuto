package main.java.com.miage.parcauto.model.affectation;

/**
 * Types d'affectation d'un véhicule.
 */
public enum TypeAffectation {
    CREDIT_5_ANS("Credit5Ans"),
    MISSION("Mission");

    private final String valeur;

    TypeAffectation(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }
    public static TypeAffectation fromString(String text) {
        for (TypeAffectation type : TypeAffectation.values()) {
            if (type.valeur.equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Type d'affectation inconnu: " + text);
    }
}