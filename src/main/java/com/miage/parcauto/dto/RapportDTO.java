package main.java.com.miage.parcauto.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RapportDTO implements Serializable {

    private static final long serialVersionUID = 1L; // Added serialVersionUID
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");


    private String titre;
    private String description;
    private LocalDateTime dateGeneration; // Changed to LocalDateTime
    private List<String> typesExport; // PDF, EXCEL, CSV etc.
    private Map<String, Object> donnees;
    private List<String> erreurs;

    public RapportDTO() {
        this.donnees = new HashMap<>();
        this.erreurs = new ArrayList<>();
        this.typesExport = new ArrayList<>();
        this.dateGeneration = LocalDateTime.now();
    }

    public RapportDTO(String titre, String description) {
        this();
        this.titre = titre;
        this.description = description;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateGeneration() {
        return dateGeneration;
    }

    public String getDateGenerationFormattee() {
        return dateGeneration != null ? dateGeneration.format(DATE_TIME_FORMATTER) : "N/A";
    }

    public void setDateGeneration(LocalDateTime dateGeneration) {
        this.dateGeneration = dateGeneration;
    }

    public List<String> getTypesExport() {
        return typesExport;
    }

    public void setTypesExport(List<String> typesExport) {
        this.typesExport = typesExport;
    }

    public void addTypeExport(String type) {
        if (type != null && !type.trim().isEmpty()) {
            this.typesExport.add(type.toUpperCase());
        }
    }

    public Map<String, Object> getDonnees() {
        return donnees;
    }

    public void setDonnees(Map<String, Object> donnees) {
        this.donnees = donnees;
    }

    public void addDonnee(String cle, Object valeur) {
        this.donnees.put(cle, valeur);
    }

    public List<String> getErreurs() {
        return erreurs;
    }

    public void setErreurs(List<String> erreurs) {
        this.erreurs = erreurs;
    }

    public void addErreur(String erreur) {
        this.erreurs.add(erreur);
    }

    public boolean hasErreurs() {
        return !this.erreurs.isEmpty();
    }

    @Override
    public String toString() {
        return "RapportDTO{" +
                "titre='" + titre + '\'' +
                ", dateGeneration=" + getDateGenerationFormattee() +
                ", typesExport=" + typesExport +
                ", hasErreurs=" + hasErreurs() +
                '}';
    }
}