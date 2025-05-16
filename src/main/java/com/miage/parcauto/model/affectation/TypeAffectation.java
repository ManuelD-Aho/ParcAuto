package main.java.com.miage.parcauto.model.affectation;

public enum TypeAffectation {
    CREDIT_5_ANS("Credit5Ans"),
    MISSION("Mission");

    private final String libelle;

    TypeAffectation(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public static TypeAffectation fromLibelle(String libelle) {
        for (TypeAffectation type : TypeAffectation.values()) {
            if (type.libelle.equalsIgnoreCase(libelle) || type.name().replace("_", "").equalsIgnoreCase(libelle)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Aucun type d'affectation ne correspond au libellé : " + libelle);
    }

    @Override
    public String toString() {
        return this.libelle;
    }
}