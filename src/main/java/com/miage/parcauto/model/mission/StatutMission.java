package main.java.com.miage.parcauto.model.mission;

/**
 * Statuts possibles pour une mission.
 */
public enum StatutMission {
    PLANIFIEE("Planifiee"),
    EN_COURS("EnCours"),
    CLOTUREE("Cloturee");

    private final String valeur;

    StatutMission(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }

    public static StatutMission fromString(String text) {
        for (StatutMission statut : StatutMission.values()) {
            if (statut.valeur.equalsIgnoreCase(text)) {
                return statut;
            }
        }
        throw new IllegalArgumentException("Statut de mission inconnu: " + text);
    }
}