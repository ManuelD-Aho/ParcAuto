package main.java.com.miage.parcauto.model.entretien;

public enum StatutOT {
    OUVERT("Ouvert"),
    EN_COURS("EnCours"),
    CLOTURE("Cloture");

    private final String libelle;

    StatutOT(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public static StatutOT fromLibelle(String libelle) {
        for (StatutOT statut : StatutOT.values()) {
            // Gestion de "EnCours" vs "EN_COURS"
            if (statut.libelle.equalsIgnoreCase(libelle) || statut.name().replace("_", "").equalsIgnoreCase(libelle)) {
                return statut;
            }
        }
        throw new IllegalArgumentException("Aucun statut OT ne correspond au libellé : " + libelle);
    }

    @Override
    public String toString() {
        return this.libelle;
    }
}