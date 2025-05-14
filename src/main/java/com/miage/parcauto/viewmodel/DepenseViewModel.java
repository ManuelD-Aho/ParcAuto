package main.java.com.miage.parcauto.viewmodel;

import javafx.beans.property.*;
import main.java.com.miage.parcauto.dto.DepenseDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * ViewModel pour la visualisation des dépenses dans l'interface JavaFX.
 * Cette classe adapte les DepenseDTO pour l'affichage et la liaison aux
 * composants UI.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class DepenseViewModel {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Propriétés JavaFX pour binding bidirectionnel avec l'UI
    private final IntegerProperty idDepense = new SimpleIntegerProperty();
    private final IntegerProperty idMission = new SimpleIntegerProperty();
    private final StringProperty categorie = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final ObjectProperty<BigDecimal> montant = new SimpleObjectProperty<>(BigDecimal.ZERO);
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>(LocalDate.now());
    private final StringProperty justificatif = new SimpleStringProperty();

    // Propriétés calculées pour l'affichage formaté
    private final StringProperty dateFormatee = new SimpleStringProperty();
    private final StringProperty montantFormate = new SimpleStringProperty();
    private final StringProperty categorieClass = new SimpleStringProperty();
    private final BooleanProperty hasJustificatif = new SimpleBooleanProperty();

    /**
     * Constructeur par défaut.
     */
    public DepenseViewModel() {
        // Initialiser les valeurs par défaut
        updateFormattedProperties();
    }

    /**
     * Constructeur avec initialisation depuis un DTO.
     *
     * @param dto DTO source pour l'initialisation
     */
    public DepenseViewModel(DepenseDTO dto) {
        updateFromDTO(dto);
    }

    /**
     * Met à jour ce ViewModel avec les données d'un DTO.
     *
     * @param dto DTO source des données
     */
    public void updateFromDTO(DepenseDTO dto) {
        if (dto == null)
            return;

        idDepense.set(dto.getIdDepense() != null ? dto.getIdDepense() : 0);
        idMission.set(dto.getIdMission() != null ? dto.getIdMission() : 0);
        categorie.set(dto.getCategorie());
        description.set(dto.getDescription());
        montant.set(dto.getMontant());
        date.set(dto.getDate());
        justificatif.set(dto.getJustificatif());

        updateFormattedProperties();
    }

    /**
     * Convertit ce ViewModel en DTO.
     *
     * @return Nouveau DTO avec les données de ce ViewModel
     */
    public DepenseDTO toDTO() {
        return new DepenseDTO(
                idDepense.get() > 0 ? idDepense.get() : null,
                idMission.get() > 0 ? idMission.get() : null,
                categorie.get(),
                description.get(),
                montant.get(),
                date.get(),
                justificatif.get());
    }

    /**
     * Met à jour toutes les propriétés formatées.
     * Cette méthode est appelée automatiquement lorsque les données brutes
     * changent.
     */
    private void updateFormattedProperties() {
        // Format date
        if (date.get() != null) {
            dateFormatee.set(date.get().format(DATE_FORMATTER));
        } else {
            dateFormatee.set(LocalDate.now().format(DATE_FORMATTER));
        }

        // Format montant
        if (montant.get() != null) {
            montantFormate.set(montant.get() + " €");
        } else {
            montantFormate.set("0,00 €");
        }

        // Déterminer si un justificatif est disponible
        hasJustificatif.set(justificatif.get() != null && !justificatif.get().isBlank());

        // Définir la classe CSS selon la catégorie
        if (categorie.get() != null) {
            String cat = categorie.get().toLowerCase();
            if (cat.contains("carburant")) {
                categorieClass.set("depense-carburant");
            } else if (cat.contains("péage") || cat.contains("peage")) {
                categorieClass.set("depense-peage");
            } else if (cat.contains("hôtel") || cat.contains("hotel")) {
                categorieClass.set("depense-hotel");
            } else if (cat.contains("restauration")) {
                categorieClass.set("depense-resto");
            } else {
                categorieClass.set("depense-divers");
            }
        } else {
            categorieClass.set("depense-divers");
        }
    }

    /**
     * Retourne le style CSS à appliquer à cette dépense.
     *
     * @return Style CSS basé sur la catégorie
     */
    public String getStyle() {
        if (categorie.get() == null)
            return "";

        String cat = categorie.get().toLowerCase();
        if (cat.contains("carburant")) {
            return "-fx-background-color: #ffcdd2; -fx-text-fill: #c62828;"; // Rouge pour carburant
        } else if (cat.contains("péage") || cat.contains("peage")) {
            return "-fx-background-color: #bbdefb; -fx-text-fill: #1565c0;"; // Bleu pour péage
        } else if (cat.contains("hôtel") || cat.contains("hotel")) {
            return "-fx-background-color: #c8e6c9; -fx-text-fill: #2e7d32;"; // Vert pour hôtel
        } else if (cat.contains("restauration")) {
            return "-fx-background-color: #fff9c4; -fx-text-fill: #f9a825;"; // Jaune pour restauration
        } else {
            return "-fx-background-color: #e1bee7; -fx-text-fill: #6a1b9a;"; // Violet pour divers
        }
    }

    // Getters pour toutes les propriétés JavaFX (pour le binding)

    public IntegerProperty idDepenseProperty() {
        return idDepense;
    }

    public IntegerProperty idMissionProperty() {
        return idMission;
    }

    public StringProperty categorieProperty() {
        return categorie;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public ObjectProperty<BigDecimal> montantProperty() {
        return montant;
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public StringProperty justificatifProperty() {
        return justificatif;
    }

    public StringProperty dateFormateeProperty() {
        return dateFormatee;
    }

    public StringProperty montantFormateProperty() {
        return montantFormate;
    }

    public StringProperty categorieClassProperty() {
        return categorieClass;
    }

    public BooleanProperty hasJustificatifProperty() {
        return hasJustificatif;
    }

    // Getters et Setters standards

    public Integer getIdDepense() {
        return idDepense.get();
    }

    public void setIdDepense(Integer id) {
        idDepense.set(id != null ? id : 0);
    }

    public Integer getIdMission() {
        return idMission.get();
    }

    public void setIdMission(Integer id) {
        idMission.set(id != null ? id : 0);
    }

    public String getCategorie() {
        return categorie.get();
    }

    public void setCategorie(String value) {
        categorie.set(value);
        updateFormattedProperties();
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String value) {
        description.set(value);
    }

    public BigDecimal getMontant() {
        return montant.get();
    }

    public void setMontant(BigDecimal value) {
        montant.set(value != null ? value : BigDecimal.ZERO);
        updateFormattedProperties();
    }

    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(LocalDate value) {
        date.set(value != null ? value : LocalDate.now());
        updateFormattedProperties();
    }

    public String getJustificatif() {
        return justificatif.get();
    }

    public void setJustificatif(String value) {
        justificatif.set(value);
        updateFormattedProperties();
    }

    public String getDateFormatee() {
        return dateFormatee.get();
    }

    public String getMontantFormate() {
        return montantFormate.get();
    }

    public String getCategorieClass() {
        return categorieClass.get();
    }

    public boolean isHasJustificatif() {
        return hasJustificatif.get();
    }
}
