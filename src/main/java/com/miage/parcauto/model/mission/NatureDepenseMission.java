package main.java.com.miage.parcauto.model.mission;

/**
 * Nature des dépenses liées à une mission.
 */
public enum NatureDepenseMission {
    CARBURANT("Carburant"),
    FRAIS_ANNEXES("FraisAnnexes");

    private final String valeur;

    NatureDepenseMission(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }

    public static NatureDepenseMission fromString(String text) {
        for (NatureDepenseMission nature : NatureDepenseMission.values()) {
            if (nature.valeur.equalsIgnoreCase(text)) {
                return nature;
            }
        }
        throw new IllegalArgumentException("Nature de dépense inconnue: " + text);
    }
}