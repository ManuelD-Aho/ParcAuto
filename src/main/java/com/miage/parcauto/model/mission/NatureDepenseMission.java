package main.java.com.miage.parcauto.model.mission;

public enum NatureDepenseMission {
    CARBURANT("Carburant"),
    FRAIS_ANNEXES("FraisAnnexes"); // SQL: FraisAnnexes

    private final String libelle;

    NatureDepenseMission(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public static NatureDepenseMission fromLibelle(String libelle) {
        for (NatureDepenseMission nature : NatureDepenseMission.values()) {
            if (nature.libelle.equalsIgnoreCase(libelle) || nature.name().replace("_", "").equalsIgnoreCase(libelle)) {
                return nature;
            }
        }
        throw new IllegalArgumentException("Aucune nature de dépense ne correspond au libellé : " + libelle);
    }

    @Override
    public String toString() {
        return this.libelle;
    }
}