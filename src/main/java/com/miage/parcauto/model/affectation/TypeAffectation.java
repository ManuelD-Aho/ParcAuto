package main.java.com.miage.parcauto.model.affectation;

/**
 * Énumération représentant les types d'affectation d'un véhicule.
 * Correspond à la colonne `type` de la table `AFFECTATION`.
 */
public enum TypeAffectation {
    CREDIT_5_ANS("Credit5Ans", "Crédit 5 Ans"), // Affectation longue durée, potentiellement avec option d'achat
    MISSION("Mission", "Mission Ponctuelle");    // Affectation pour une mission spécifique et temporaire

    private final String valeurDb;
    private final String libelle;

    /**
     * Constructeur pour l'énumération TypeAffectation.
     * @param valeurDb La valeur telle qu'elle est stockée en base de données.
     * @param libelle Le libellé lisible du type d'affectation.
     */
    TypeAffectation(String valeurDb, String libelle) {
        this.valeurDb = valeurDb;
        this.libelle = libelle;
    }

    /**
     * Retourne le libellé lisible du type d'affectation.
     * @return Le libellé en français.
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Retourne la valeur du type d'affectation telle qu'elle est stockée en base de données.
     * @return La valeur pour la base de données.
     */
    public String getValeurDb() {
        return valeurDb;
    }

    /**
     * Retrouve un TypeAffectation à partir de sa valeur en base de données ou de son libellé.
     * @param value La chaîne de caractères à mapper.
     * @return Le TypeAffectation correspondant.
     * @throws IllegalArgumentException si la valeur n'est pas reconnue.
     */
    public static TypeAffectation fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("La valeur pour TypeAffectation ne peut pas être nulle.");
        }
        for (TypeAffectation typeAff : values()) {
            if (typeAff.valeurDb.equalsIgnoreCase(value) ||
                    typeAff.libelle.equalsIgnoreCase(value) ||
                    typeAff.name().equalsIgnoreCase(value.replace(" ", "_").replace("-","_"))) {
                return typeAff;
            }
        }
        throw new IllegalArgumentException("Type d'affectation inconnu : " + value);
    }

    /**
     * Retourne la valeur à stocker en base de données.
     * @return La valeur exacte telle que définie dans l'ENUM de la base de données.
     */
    public String toDbValue() {
        return this.valeurDb;
    }
}