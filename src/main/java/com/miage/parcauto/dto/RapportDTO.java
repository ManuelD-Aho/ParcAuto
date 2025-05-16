package main.java.com.miage.parcauto.dto;

import java.time.LocalDateTime;

// Classe de base pour les DTOs de rapport.
// Peut contenir des champs communs comme la date de génération, le type de rapport, etc.
public abstract class RapportDTO {

    private LocalDateTime dateGeneration;
    private String typeRapport;

    public RapportDTO() {
        this.dateGeneration = LocalDateTime.now();
    }

    public LocalDateTime getDateGeneration() {
        return dateGeneration;
    }

    public void setDateGeneration(LocalDateTime dateGeneration) {
        this.dateGeneration = dateGeneration;
    }

    public String getTypeRapport() {
        return typeRapport;
    }

    public void setTypeRapport(String typeRapport) {
        this.typeRapport = typeRapport;
    }
}