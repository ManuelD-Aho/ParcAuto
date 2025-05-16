package main.java.com.miage.parcauto.model.vehicule;

public enum Energie {
    DIESEL("Diesel"),
    ESSENCE("Essence"),
    ELECTRIQUE("Électrique"), // Notez l'accent
    HYBRIDE("Hybride");

    private final String libelle;

    Energie(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public static Energie fromLibelle(String libelle) {
        for (Energie energie : Energie.values()) {
            if (energie.libelle.equalsIgnoreCase(libelle) || energie.name().equalsIgnoreCase(libelle)) {
                return energie;
            }
        }
        // Cas spécial pour "Électrique" avec ou sans accent pour la robustesse
        if ("Electrique".equalsIgnoreCase(libelle)) return ELECTRIQUE;
        throw new IllegalArgumentException("Aucune énergie ne correspond au libellé : " + libelle);
    }

    @Override
    public String toString() {
        return this.libelle;
    }
}