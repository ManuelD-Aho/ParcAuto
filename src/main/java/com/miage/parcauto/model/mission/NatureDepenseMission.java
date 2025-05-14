package main.java.com.miage.parcauto.model.mission;

/**
 * Énumération représentant la nature d'une dépense de mission.
 * Correspond à la colonne `nature` de la table `DEPENSE_MISSION`.
 */
public enum NatureDepenseMission {
    CARBURANT("Carburant"),
    FRAIS_ANNEXES("FraisAnnexes", "Frais Annexes"); // Valeur DB, Libellé UI

    private final String valeurDb;
    private final String libelle;

    /**
     * Constructeur pour l'énumération NatureDepenseMission.
     * @param valeurDb La valeur telle qu'elle est stockée en base de données.
     */
    NatureDepenseMission(String valeurDb) {
        this.valeurDb = valeurDb;
        this.libelle = valeurDb; // Par défaut, libellé = valeurDb
    }

    /**
     * Constructeur pour l'énumération NatureDepenseMission avec libellé distinct.
     * @param valeurDb La valeur telle qu'elle est stockée en base de données.
     * @param libelle Le libellé lisible.
     */
    NatureDepenseMission(String valeurDb, String libelle) {
        this.valeurDb = valeurDb;
        this.libelle = libelle;
    }

    /**
     * Retourne le libellé lisible de la nature de la dépense.
     * @return Le libellé en français.
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Retourne la valeur de la nature telle qu'elle est stockée en base de données.
     * @return La valeur pour la base de données.
     */
    public String getValeurDb() {
        return valeurDb;
    }

    /**
     * Retrouve une NatureDepenseMission à partir de sa valeur en base de données ou de son libellé.
     * @param value La chaîne de caractères à mapper.
     * @return La NatureDepenseMission correspondante.
     * @throws IllegalArgumentException si la valeur n'est pas reconnue.
     */
    public static NatureDepenseMission fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("La valeur pour NatureDepenseMission ne peut pas être nulle.");
        }
        for (NatureDepenseMission nature : values()) {
            if (nature.valeurDb.equalsIgnoreCase(value) ||
                    nature.libelle.equalsIgnoreCase(value) ||
                    nature.name().equalsIgnoreCase(value.replace(" ", "_"))) { // Gère "Frais Annexes" -> FRAIS_ANNEXES
                return nature;
            }
        }
        throw new IllegalArgumentException("Nature de dépense de mission inconnue : " + value + ". Attendus: Carburant, FraisAnnexes.");
    }

    /**
     * Retourne la valeur à stocker en base de données.
     * @return La valeur exacte telle que définie dans l'ENUM de la base de données.
     */
    public String toDbValue() {
        return this.valeurDb;
    }
}