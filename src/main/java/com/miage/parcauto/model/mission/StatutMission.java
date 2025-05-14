package main.java.com.miage.parcauto.model.mission;

/**
 * Énumération représentant les différents statuts possibles d'une mission.
 * Correspond à la colonne `status` de la table `MISSION`.
 */
public enum StatutMission {
    PLANIFIEE("Planifiee", "Planifiée"), // Valeur DB, Libellé UI
    EN_COURS("EnCours", "En Cours"),
    CLOTUREE("Cloturee", "Clôturée"); // Terminee n'est pas dans la DB, Cloturee l'est.

    private final String valeurDb;
    private final String libelle;

    /**
     * Constructeur pour l'énumération StatutMission.
     * @param valeurDb La valeur telle qu'elle est stockée en base de données.
     * @param libelle Le libellé lisible du statut.
     */
    StatutMission(String valeurDb, String libelle) {
        this.valeurDb = valeurDb;
        this.libelle = libelle;
    }

    /**
     * Retourne le libellé lisible du statut de la mission.
     * @return Le libellé en français.
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Retourne la valeur du statut telle qu'elle est stockée en base de données.
     * @return La valeur pour la base de données.
     */
    public String getValeurDb() {
        return valeurDb;
    }

    /**
     * Retrouve un StatutMission à partir de sa valeur en base de données ou de son libellé.
     * @param value La chaîne de caractères à mapper.
     * @return Le StatutMission correspondant.
     * @throws IllegalArgumentException si la valeur n'est pas reconnue.
     */
    public static StatutMission fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("La valeur pour StatutMission ne peut pas être nulle.");
        }
        for (StatutMission statut : values()) {
            if (statut.valeurDb.equalsIgnoreCase(value) ||
                    statut.libelle.equalsIgnoreCase(value) ||
                    statut.name().equalsIgnoreCase(value)) {
                return statut;
            }
        }
        // Cas particulier pour "Terminee" qui pourrait venir d'anciens DTOs mais qui correspond à "Cloturee"
        if ("Terminee".equalsIgnoreCase(value)) {
            return CLOTUREE;
        }
        throw new IllegalArgumentException("Statut de mission inconnu : " + value + ". Attendus: Planifiee, EnCours, Cloturee.");
    }

    /**
     * Retourne la valeur à stocker en base de données.
     * @return La valeur exacte telle que définie dans l'ENUM de la base de données.
     */
    public String toDbValue() {
        return this.valeurDb;
    }
}