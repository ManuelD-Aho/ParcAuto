package main.java.com.miage.parcauto.model.entretien;

public enum TypeEntretien {
    PREVENTIF("Preventif"),
    CORRECTIF("Correctif");

    private final String libelle;

    TypeEntretien(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public static TypeEntretien fromLibelle(String libelle) {
        for (TypeEntretien type : TypeEntretien.values()) {
            if (type.libelle.equalsIgnoreCase(libelle)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Aucun type d'entretien ne correspond au libellé : " + libelle);
    }

    @Override
    public String toString() {
        return this.libelle;
    }
}