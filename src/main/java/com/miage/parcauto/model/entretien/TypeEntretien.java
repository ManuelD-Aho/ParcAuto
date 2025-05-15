package main.java.com.miage.parcauto.model.entretien;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum TypeEntretien {
    PREVENTIF("Préventif"),
    CURATIF("Curatif"),
    REGLEMENTAIRE("Réglementaire"), // Contrôle technique par exemple
    ACCIDENT("Accident"),
    AUTRE("Autre");

    private final String valeurDb;

    TypeEntretien(String valeurDb) {
        this.valeurDb = valeurDb;
    }

    public String getValeurDb() {
        return valeurDb;
    }

    public static TypeEntretien fromString(String text) {
        for (TypeEntretien b : TypeEntretien.values()) {
            if (b.valeurDb.equalsIgnoreCase(text)) {
                return b;
            }
        }
        // Peut-être retourner AUTRE par défaut ou lever une exception plus spécifique
        throw new IllegalArgumentException("Aucun TypeEntretien avec la valeur: " + text);
    }

    public static String getValidValues() {
        return Arrays.stream(TypeEntretien.values())
                .map(e -> "'" + e.valeurDb + "'")
                .collect(Collectors.joining(", "));
    }
}