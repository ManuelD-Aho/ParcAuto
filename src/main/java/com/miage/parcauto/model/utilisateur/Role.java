package main.java.com.miage.parcauto.model.utilisateur;

/**
 * Rôles des utilisateurs du système.
 */
public enum Role {
    U1("U1"), // Responsable Logistique
    U2("U2"), // Agent Logistique
    U3("U3"), // Sociétaire
    U4("U4"); // Administrateur Système

    private final String valeur;

    Role(String valeur) {
        this.valeur = valeur;
    }

    public String getValeur() {
        return valeur;
    }

    public static Role fromString(String text) {
        for (Role role : Role.values()) {
            if (role.valeur.equalsIgnoreCase(text)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Rôle inconnu: " + text);
    }
}