package main.java.com.miage.parcauto.model.vehicule;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum StatutOT {
    PLANIFIE("Planifié"),
    EN_COURS("En Cours"),
    TERMINE("Terminé"),
    ANNULE("Annulé");

    private final String valeurDb;

    StatutOT(String valeurDb) {
        this.valeurDb = valeurDb;
    }

    public String getValeurDb() {
        return valeurDb;
    }

    public static StatutOT fromString(String text) {
        for (StatutOT b : StatutOT.values()) {
            if (b.valeurDb.equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Aucun StatutOT avec la valeur: " + text);
    }

    public static String getValidValues() {
        return Arrays.stream(StatutOT.values())
                .map(e -> "'" + e.valeurDb + "'")
                .collect(Collectors.joining(", "));
    }
}