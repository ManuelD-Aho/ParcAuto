package main.java.com.miage.parcauto.model.vehicule;

/**
 * Énumération représentant les différents types d'énergie possibles pour un véhicule.
 * Correspond à la contrainte ENUM de la table VEHICULES en base de données.
 */
public enum TypeEnergie {
    DIESEL("Diesel"),
    ESSENCE("Essence"),
    ELECTRIQUE("Électrique"), // Notez l'accent pour correspondre à la DB
    HYBRIDE("Hybride");

    private final String libelle;

    /**
     * Constructeur pour l'énumération TypeEnergie.
     * @param libelle Le libellé lisible du type d'énergie.
     */
    TypeEnergie(String libelle) {
        this.libelle = libelle;
    }

    /**
     * Retourne le libellé lisible du type d'énergie.
     * @return Le libellé en français.
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Retrouve un TypeEnergie à partir de son libellé.
     * Attention, cette méthode peut retourner null si aucun type ne correspond.
     * Elle peut être utile pour le mapping depuis des chaînes de caractères.
     * @param libelle Le libellé à rechercher.
     * @return Le TypeEnergie correspondant, ou null.
     */
    public static TypeEnergie fromLibelle(String libelle) {
        if (libelle == null) {
            return null;
        }
        // Gestion de la différence d'accent pour "Électrique" vs "Electrique"
        String normalizedLibelle = libelle.replace("É", "E").replace("é", "e");

        for (TypeEnergie type : TypeEnergie.values()) {
            String normalizedTypeLibelle = type.libelle.replace("É", "E").replace("é", "e");
            if (normalizedTypeLibelle.equalsIgnoreCase(normalizedLibelle)) {
                return type;
            }
        }
        // Tentative de match direct si la normalisation échoue (pour être robuste)
        for (TypeEnergie type : TypeEnergie.values()) {
            if (type.libelle.equalsIgnoreCase(libelle)) {
                return type;
            }
        }
        return null; // Ou lever une exception IllegalArgumentException si un mapping strict est requis
    }


    /**
     * Retrouve un TypeEnergie à partir de sa valeur ordinale (utilisé par JDBC pour les ENUM MySQL parfois).
     * Note: MySQL ENUMs are 1-indexed, Java enums are 0-indexed.
     * Cette méthode est plus pour la forme, car le mapping String est généralement préféré.
     * @param dbValue La valeur string de la base de données (ex: 'Diesel').
     * @return Le TypeEnergie correspondant.
     * @throws IllegalArgumentException si la valeur n'est pas reconnue.
     */
    public static TypeEnergie fromDbValue(String dbValue) {
        if (dbValue == null) {
            throw new IllegalArgumentException("La valeur de la base de données pour TypeEnergie ne peut pas être nulle.");
        }
        // Gérer la différence d'accent pour "Électrique"
        if ("Électrique".equalsIgnoreCase(dbValue) || "Electrique".equalsIgnoreCase(dbValue)) {
            return ELECTRIQUE;
        }
        for (TypeEnergie type : values()) {
            if (type.name().equalsIgnoreCase(dbValue.toUpperCase()) || type.getLibelle().equalsIgnoreCase(dbValue)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Valeur TypeEnergie inconnue depuis la base de données: " + dbValue);
    }

    /**
     * Retourne la valeur à stocker en base de données.
     * @return Le libellé exact tel que défini dans l'ENUM de la base de données.
     */
    public String toDbValue() {
        // Assure que la valeur retournée correspond exactement à ce qui est dans l'ENUM SQL
        // Spécialement pour "Électrique"
        if (this == ELECTRIQUE) {
            return "Électrique";
        }
        return this.libelle;
    }
}