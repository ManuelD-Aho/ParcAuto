package main.java.com.miage.parcauto.model.entretien;

/**
 * Statuts possibles pour un ordre de travail d'entretien.
 */
public enum StatutOT {
    OUVERT("Ouvert"),
    EN_COURS("EnCours"),
    CLOTURE("Cloture");

    private final String valeur;

    StatutOT(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }

    public static StatutOT fromString(String text) {
        for (StatutOT statut : StatutOT.values()) {
            if (statut.valeur.equalsIgnoreCase(text)) {
                return statut;
            }
        }
        throw new IllegalArgumentException("Statut OT inconnu: " + text);
    }
}