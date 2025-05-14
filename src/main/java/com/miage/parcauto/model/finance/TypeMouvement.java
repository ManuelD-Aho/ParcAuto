package main.java.com.miage.parcauto.model.finance;

/**
 * Énumération représentant les différents types de mouvements financiers.
 * Correspond à la colonne `type` de la table `MOUVEMENT`.
 */
public enum TypeMouvement {
    DEPOT("Depot", "Dépôt"),
    RETRAIT("Retrait", "Retrait"),
    MENSUALITE("Mensualite", "Mensualité"); // 'Mensualite' dans la DB

    private final String valeurDb;
    private final String libelle;

    /**
     * Constructeur pour l'énumération TypeMouvement.
     * @param valeurDb La valeur telle qu'elle est stockée en base de données.
     * @param libelle Le libellé lisible du type de mouvement.
     */
    TypeMouvement(String valeurDb, String libelle) {
        this.valeurDb = valeurDb;
        this.libelle = libelle;
    }

    /**
     * Retourne le libellé lisible du type de mouvement.
     * @return Le libellé en français.
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Retourne la valeur du type de mouvement telle qu'elle est stockée en base de données.
     * @return La valeur pour la base de données.
     */
    public String getValeurDb() {
        return valeurDb;
    }

    /**
     * Retrouve un TypeMouvement à partir de sa valeur en base de données ou de son libellé.
     * @param value La chaîne de caractères à mapper.
     * @return Le TypeMouvement correspondant.
     * @throws IllegalArgumentException si la valeur n'est pas reconnue.
     */
    public static TypeMouvement fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("La valeur pour TypeMouvement ne peut pas être nulle.");
        }
        for (TypeMouvement type : values()) {
            if (type.valeurDb.equalsIgnoreCase(value) ||
                    type.libelle.equalsIgnoreCase(value) ||
                    type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Type de mouvement inconnu : " + value + ". Attendus: Depot, Retrait, Mensualite.");
    }

    /**
     * Retourne la valeur à stocker en base de données.
     * @return La valeur exacte telle que définie dans l'ENUM de la base de données.
     */
    public String toDbValue() {
        return this.valeurDb;
    }
}