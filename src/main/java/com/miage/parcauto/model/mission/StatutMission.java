package main.java.com.miage.parcauto.model.mission;

public enum StatutMission {
    PLANIFIEE("Planifiee"),
    EN_COURS("EnCours"),
    CLOTUREE("Cloturee");

    private final String libelle;

    StatutMission(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public static StatutMission fromLibelle(String libelle) {
        for (StatutMission statut : StatutMission.values()) {
            if (statut.libelle.equalsIgnoreCase(libelle) || statut.name().replace("_", "").equalsIgnoreCase(libelle)) {
                return statut;
            }
        }
        throw new IllegalArgumentException("Aucun statut de mission ne correspond au libellé : " + libelle);
    }

    @Override
    public String toString() {
        return this.libelle;
    }
}