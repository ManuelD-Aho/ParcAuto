package main.java.com.miage.parcauto.model.entretien;

/**
 * Énumération représentant les types d'entretien possibles pour un véhicule.
 * Correspond à la colonne `type` de la table `ENTRETIEN`.
 */
public enum TypeEntretien {
    PREVENTIF("Preventif"),
    CORRECTIF("Correctif");

    private final String libelle;

    /**
     * Constructeur pour l'énumération TypeEntretien.
     * @param libelle Le libellé lisible du type d'entretien.
     */
    TypeEntretien(String libelle) {
        this.libelle = libelle;
    }

    /**
     * Retourne le libellé lisible du type d'entretien.
     * @return Le libellé en français.
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Retrouve un TypeEntretien à partir de son libellé ou de son nom.
     * @param value La chaîne de caractères à mapper (libellé ou nom de l'enum).
     * @return Le TypeEntretien correspondant.
     * @throws IllegalArgumentException si la valeur n'est pas reconnue.
     */
    public static TypeEntretien fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("La valeur pour TypeEntretien ne peut pas être nulle.");
        }
        for (TypeEntretien type : values()) {
            if (type.libelle.equalsIgnoreCase(value) || type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Type d'entretien inconnu : " + value);
    }

    /**
     * Retourne la valeur à stocker en base de données (le libellé).
     * @return Le libellé exact tel que défini dans l'ENUM de la base de données.
     */
    public String toDbValue() {
        return this.libelle;
    }
}