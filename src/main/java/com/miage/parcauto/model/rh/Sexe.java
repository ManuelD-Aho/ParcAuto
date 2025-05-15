package main.java.com.miage.parcauto.model.rh;

/**
 * Sexe du personnel.
 */
public enum Sexe {
    M("M"),
    F("F");

    private final String valeur;

    Sexe(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }

    public static Sexe fromString(String text) {
        for (Sexe sexe : Sexe.values()) {
            if (sexe.valeur.equalsIgnoreCase(text)) {
                return sexe;
            }
        }
        throw new IllegalArgumentException("Sexe inconnu: " + text);
    }
}