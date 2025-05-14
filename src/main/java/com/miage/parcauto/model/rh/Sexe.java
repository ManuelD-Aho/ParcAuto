package main.java.com.miage.parcauto.model.rh;

/**
 * Énumération représentant le sexe d'un membre du personnel.
 * Correspond à la colonne `sexe` (ENUM('M', 'F')) de la table `PERSONNEL`.
 */
public enum Sexe {
    MASCULIN("M", "Masculin"),
    FEMININ("F", "Féminin");

    private final String codeDb;
    private final String libelle;

    /**
     * Constructeur pour l'énumération Sexe.
     * @param codeDb Le code stocké en base de données ('M' ou 'F').
     * @param libelle Le libellé lisible.
     */
    Sexe(String codeDb, String libelle) {
        this.codeDb = codeDb;
        this.libelle = libelle;
    }

    /**
     * Retourne le libellé lisible du sexe.
     * @return Le libellé en français.
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Retourne le code du sexe tel qu'il est stocké en base de données.
     * @return Le code pour la base de données ('M' ou 'F').
     */
    public String getCodeDb() {
        return codeDb;
    }

    /**
     * Retrouve un Sexe à partir de son code en base de données ou de son libellé.
     * @param value La chaîne de caractères à mapper (code 'M'/'F' ou libellé "Masculin"/"Féminin").
     * @return Le Sexe correspondant.
     * @throws IllegalArgumentException si la valeur n'est pas reconnue.
     */
    public static Sexe fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("La valeur pour Sexe ne peut pas être nulle.");
        }
        for (Sexe s : values()) {
            if (s.codeDb.equalsIgnoreCase(value) ||
                    s.libelle.equalsIgnoreCase(value) ||
                    s.name().equalsIgnoreCase(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Valeur de sexe inconnue : " + value + ". Attendus: M, F, Masculin, Féminin.");
    }

    /**
     * Retourne la valeur à stocker en base de données.
     * @return Le code 'M' ou 'F'.
     */
    public String toDbValue() {
        return this.codeDb;
    }
}