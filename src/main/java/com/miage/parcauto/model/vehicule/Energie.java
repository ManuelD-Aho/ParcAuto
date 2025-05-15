package main.java.com.miage.parcauto.model.vehicule;

/**
 * Types d'énergie/carburant pour les véhicules.
 */
public enum Energie {
    DIESEL("Diesel"),
    ESSENCE("Essence"),
    ELECTRIQUE("Électrique"),
    HYBRIDE("Hybride");

    private final String valeur;

    Energie(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }
    public static Energie fromString(String text) {
        for (Energie energie : Energie.values()) {
            if (energie.valeur.equalsIgnoreCase(text)) {
                return energie;
            }
        }
        throw new IllegalArgumentException("Énergie inconnue: " + text);
    }
}