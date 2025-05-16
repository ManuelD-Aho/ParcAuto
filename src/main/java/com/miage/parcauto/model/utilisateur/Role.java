package main.java.com.miage.parcauto.model.utilisateur;

public enum Role {
    U1("Utilisateur Standard"), // A adapter selon la signification de U1, U2 etc.
    U2("Gestionnaire"),
    U3("Administrateur"),
    U4("Super Administrateur");

    private final String libelle;

    Role(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public static Role fromString(String text) {
        for (Role r : Role.values()) {
            if (r.name().equalsIgnoreCase(text) || r.libelle.equalsIgnoreCase(text)) {
                return r;
            }
        }
        // Pour la validation 'trim' in 'Role', on va supposer que ValidationService reçoit un String
        // et doit le convertir en Role. Le .trim() sera appliqué sur le String avant la conversion.
        throw new IllegalArgumentException("Aucun rôle ne correspond à : " + text);
    }

    @Override
    public String toString() {
        // Pour correspondre à enum('U1','U2','U3','U4') dans SQL
        return this.name();
    }
}