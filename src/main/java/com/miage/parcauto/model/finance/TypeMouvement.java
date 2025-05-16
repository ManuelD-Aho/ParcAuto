package main.java.com.miage.parcauto.model.finance;

public enum TypeMouvement {
    DEPOT("Depot"),
    RETRAIT("Retrait"),
    MENSUALITE("Mensualite");

    private final String libelle;

    TypeMouvement(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public static TypeMouvement fromLibelle(String libelle) {
        for (TypeMouvement type : TypeMouvement.values()) {
            if (type.libelle.equalsIgnoreCase(libelle)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Aucun type de mouvement ne correspond au libellé : " + libelle);
    }

    @Override
    public String toString() {
        return this.libelle;
    }
}