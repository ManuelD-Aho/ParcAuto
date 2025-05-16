package main.java.com.miage.parcauto.model.rh;

public enum Sexe {
    M("Masculin"),
    F("Féminin");

    private final String libelle;

    Sexe(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public static Sexe fromChar(char c) {
        if (c == 'M' || c == 'm') {
            return M;
        }
        if (c == 'F' || c == 'f') {
            return F;
        }
        throw new IllegalArgumentException("Caractère de sexe invalide : " + c);
    }

    public static Sexe fromString(String s) {
        if (s == null || s.isEmpty()) {
            throw new IllegalArgumentException("Chaîne de sexe invalide : " + s);
        }
        return fromChar(s.charAt(0));
    }


    @Override
    public String toString() {
        // Pour correspondre au SQL enum('M','F')
        return this.name();
    }
}