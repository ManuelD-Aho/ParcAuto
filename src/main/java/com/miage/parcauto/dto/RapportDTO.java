package main.java.com.miage.parcauto.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DTO générique pour les rapports.
 * Cette classe contient les données communes à tous les types de rapport.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class RapportDTO {

    private String titre;
    private String description;
    private String dateGeneration;
    private String typesExport; // Liste des types d'export disponibles séparés par des virgules (PDF, EXCEL,
                                // etc.)
    private Map<String, Object> donnees; // Données spécifiques au rapport
    private List<String> erreurs; // Erreurs éventuelles lors de la génération

    /**
     * Constructeur par défaut.
     */
    public RapportDTO() {
        this.donnees = new HashMap<>();
        this.erreurs = new ArrayList<>();
    }

    /**
     * Constructeur avec titre et description.
     * 
     * @param titre       Titre du rapport
     * @param description Description du rapport
     */
    public RapportDTO(String titre, String description) {
        this();
        this.titre = titre;
        this.description = description;
    }

    /**
     * @return le titre
     */
    public String getTitre() {
        return titre;
    }

    /**
     * @param titre le titre à définir
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * @return la description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description la description à définir
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return la date de génération
     */
    public String getDateGeneration() {
        return dateGeneration;
    }

    /**
     * @param dateGeneration la date de génération à définir
     */
    public void setDateGeneration(String dateGeneration) {
        this.dateGeneration = dateGeneration;
    }

    /**
     * @return les types d'export disponibles
     */
    public String getTypesExport() {
        return typesExport;
    }

    /**
     * @param typesExport les types d'export disponibles à définir
     */
    public void setTypesExport(String typesExport) {
        this.typesExport = typesExport;
    }

    /**
     * @return les données du rapport
     */
    public Map<String, Object> getDonnees() {
        return donnees;
    }

    /**
     * @param donnees les données du rapport à définir
     */
    public void setDonnees(Map<String, Object> donnees) {
        this.donnees = donnees;
    }

    /**
     * Ajoute une donnée au rapport.
     * 
     * @param cle    Clé de la donnée
     * @param valeur Valeur de la donnée
     */
    public void addDonnee(String cle, Object valeur) {
        this.donnees.put(cle, valeur);
    }

    /**
     * @return les erreurs
     */
    public List<String> getErreurs() {
        return erreurs;
    }

    /**
     * @param erreurs les erreurs à définir
     */
    public void setErreurs(List<String> erreurs) {
        this.erreurs = erreurs;
    }

    /**
     * Ajoute une erreur au rapport.
     * 
     * @param erreur Message d'erreur
     */
    public void addErreur(String erreur) {
        this.erreurs.add(erreur);
    }

    /**
     * @return true si le rapport contient des erreurs
     */
    public boolean hasErreurs() {
        return !this.erreurs.isEmpty();
    }
}
