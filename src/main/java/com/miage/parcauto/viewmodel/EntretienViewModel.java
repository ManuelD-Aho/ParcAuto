package main.java.com.miage.parcauto.viewmodel;

import javafx.beans.property.*;
import main.java.com.miage.parcauto.dto.EntretienDTO;
import main.java.com.miage.parcauto.model.entretien.Entretien.StatutOT;
import main.java.com.miage.parcauto.model.entretien.Entretien.TypeEntretien;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ViewModel pour la visualisation des entretiens dans l'interface JavaFX.
 * Cette classe adapte les EntretienDTO pour l'affichage et la liaison aux
 * composants UI.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class EntretienViewModel {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Propriétés JavaFX pour binding bidirectionnel avec l'UI
    private final IntegerProperty idEntretien = new SimpleIntegerProperty();
    private final IntegerProperty idVehicule = new SimpleIntegerProperty();
    private final StringProperty immatriculation = new SimpleStringProperty();
    private final StringProperty marqueModele = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> dateEntree = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> dateSortie = new SimpleObjectProperty<>();
    private final StringProperty motif = new SimpleStringProperty();
    private final StringProperty observation = new SimpleStringProperty();
    private final ObjectProperty<BigDecimal> cout = new SimpleObjectProperty<>(BigDecimal.ZERO);
    private final StringProperty lieu = new SimpleStringProperty();
    private final ObjectProperty<TypeEntretien> type = new SimpleObjectProperty<>(TypeEntretien.Preventif);
    private final ObjectProperty<StatutOT> statut = new SimpleObjectProperty<>(StatutOT.Ouvert);

    // Propriétés calculées pour l'affichage formaté
    private final StringProperty dateEntreeFormatee = new SimpleStringProperty();
    private final StringProperty dateSortieFormatee = new SimpleStringProperty();
    private final StringProperty coutFormate = new SimpleStringProperty();
    private final StringProperty typeLibelle = new SimpleStringProperty();
    private final StringProperty statutLibelle = new SimpleStringProperty();
    private final StringProperty dureeFormatee = new SimpleStringProperty();
    private final StringProperty statutCssClass = new SimpleStringProperty();
    private final BooleanProperty termine = new SimpleBooleanProperty();

    /**
     * Constructeur par défaut.
     */
    public EntretienViewModel() {
        // Initialiser les valeurs par défaut
        updateFormattedProperties();
    }

    /**
     * Constructeur avec initialisation depuis un DTO.
     *
     * @param dto DTO source pour l'initialisation
     */
    public EntretienViewModel(EntretienDTO dto) {
        updateFromDTO(dto);
    }

    /**
     * Met à jour ce ViewModel avec les données d'un DTO.
     *
     * @param dto DTO source des données
     */
    public void updateFromDTO(EntretienDTO dto) {
        if (dto == null)
            return;

        idEntretien.set(dto.getIdEntretien() != null ? dto.getIdEntretien() : 0);
        idVehicule.set(dto.getIdVehicule() != null ? dto.getIdVehicule() : 0);
        immatriculation.set(dto.getImmatriculationVehicule());
        marqueModele.set(dto.getMarqueModeleVehicule());
        dateEntree.set(dto.getDateEntreeEntr());
        dateSortie.set(dto.getDateSortieEntr());
        motif.set(dto.getMotifEntr());
        observation.set(dto.getObservation());
        cout.set(dto.getCoutEntr());
        lieu.set(dto.getLieuEntr());
        type.set(dto.getType());
        statut.set(dto.getStatutOt());

        updateFormattedProperties();
    }

    /**
     * Convertit ce ViewModel en DTO.
     *
     * @return Nouveau DTO avec les données de ce ViewModel
     */
    public EntretienDTO toDTO() {
        return new EntretienDTO(
                idEntretien.get() > 0 ? idEntretien.get() : null,
                idVehicule.get() > 0 ? idVehicule.get() : null,
                immatriculation.get(),
                marqueModele.get(),
                dateEntree.get(),
                dateSortie.get(),
                motif.get(),
                observation.get(),
                cout.get(),
                lieu.get(),
                type.get(),
                statut.get());
    }

    /**
     * Met à jour toutes les propriétés formatées.
     * Cette méthode est appelée automatiquement lorsque les données brutes
     * changent.
     */
    private void updateFormattedProperties() {
        // Format date d'entrée
        if (dateEntree.get() != null) {
            dateEntreeFormatee.set(dateEntree.get().format(DATE_FORMATTER));
        } else {
            dateEntreeFormatee.set("Non planifiée");
        }

        // Format date de sortie
        if (dateSortie.get() != null) {
            dateSortieFormatee.set(dateSortie.get().format(DATE_FORMATTER));
        } else {
            dateSortieFormatee.set("En cours");
        }

        // Format coût
        if (cout.get() != null) {
            coutFormate.set(cout.get() + " €");
        } else {
            coutFormate.set("0,00 €");
        }

        // Format type et statut
        typeLibelle.set(type.get() != null ? type.get().getLibelle() : "");
        statutLibelle.set(statut.get() != null ? statut.get().getLibelle() : "");

        // Calcul de la durée
        if (dateEntree.get() != null && dateSortie.get() != null) {
            long dureeHeures = java.time.temporal.ChronoUnit.HOURS.between(dateEntree.get(), dateSortie.get());
            dureeFormatee.set(dureeHeures + " h");
        } else if (dateEntree.get() != null) {
            dureeFormatee.set("En cours");
        } else {
            dureeFormatee.set("Non déterminée");
        }

        // Définir le CSS selon le statut
        if (statut.get() != null) {
            switch (statut.get()) {
                case Ouvert:
                    statutCssClass.set("entretien-ouvert");
                    break;
                case EnCours:
                    statutCssClass.set("entretien-encours");
                    break;
                case Cloture:
                    statutCssClass.set("entretien-cloture");
                    break;
                default:
                    statutCssClass.set("");
            }
        } else {
            statutCssClass.set("");
        }

        // Déterminer si l'entretien est terminé
        termine.set(statut.get() == StatutOT.Cloture && dateSortie.get() != null);
    }

    /**
     * Retourne la couleur associée au statut pour le style CSS.
     *
     * @return Code couleur CSS selon le statut
     */
    public String getColorStyle() {
        if (statut.get() == null)
            return "";

        switch (statut.get()) {
            case Ouvert:
                return "-fx-background-color: #ffeb3b; -fx-text-fill: #000000;"; // Jaune
            case EnCours:
                return "-fx-background-color: #2196f3; -fx-text-fill: white;"; // Bleu
            case Cloture:
                return "-fx-background-color: #4caf50; -fx-text-fill: white;"; // Vert
            default:
                return "";
        }
    }

    // Getters pour toutes les propriétés JavaFX (pour le binding)

    public IntegerProperty idEntretienProperty() {
        return idEntretien;
    }

    public IntegerProperty idVehiculeProperty() {
        return idVehicule;
    }

    public StringProperty immatriculationProperty() {
        return immatriculation;
    }

    public StringProperty marqueModeleProperty() {
        return marqueModele;
    }

    public ObjectProperty<LocalDateTime> dateEntreeProperty() {
        return dateEntree;
    }

    public ObjectProperty<LocalDateTime> dateSortieProperty() {
        return dateSortie;
    }

    public StringProperty motifProperty() {
        return motif;
    }

    public StringProperty observationProperty() {
        return observation;
    }

    public ObjectProperty<BigDecimal> coutProperty() {
        return cout;
    }

    public StringProperty lieuProperty() {
        return lieu;
    }

    public ObjectProperty<TypeEntretien> typeProperty() {
        return type;
    }

    public ObjectProperty<StatutOT> statutProperty() {
        return statut;
    }

    public StringProperty dateEntreeFormateeProperty() {
        return dateEntreeFormatee;
    }

    public StringProperty dateSortieFormateeProperty() {
        return dateSortieFormatee;
    }

    public StringProperty coutFormateProperty() {
        return coutFormate;
    }

    public StringProperty typeLibelleProperty() {
        return typeLibelle;
    }

    public StringProperty statutLibelleProperty() {
        return statutLibelle;
    }

    public StringProperty dureeFormateeProperty() {
        return dureeFormatee;
    }

    public StringProperty statutCssClassProperty() {
        return statutCssClass;
    }

    public BooleanProperty termineProperty() {
        return termine;
    }

    // Getters et Setters standards

    public Integer getIdEntretien() {
        return idEntretien.get();
    }

    public void setIdEntretien(Integer id) {
        idEntretien.set(id != null ? id : 0);
    }

    public Integer getIdVehicule() {
        return idVehicule.get();
    }

    public void setIdVehicule(Integer id) {
        idVehicule.set(id != null ? id : 0);
        // Réinitialiser les infos véhicule si nécessaire
    }

    public String getImmatriculation() {
        return immatriculation.get();
    }

    public void setImmatriculation(String value) {
        immatriculation.set(value);
    }

    public String getMarqueModele() {
        return marqueModele.get();
    }

    public void setMarqueModele(String value) {
        marqueModele.set(value);
    }

    public LocalDateTime getDateEntree() {
        return dateEntree.get();
    }

    public void setDateEntree(LocalDateTime date) {
        dateEntree.set(date);
        updateFormattedProperties();
    }

    public LocalDateTime getDateSortie() {
        return dateSortie.get();
    }

    public void setDateSortie(LocalDateTime date) {
        dateSortie.set(date);
        updateFormattedProperties();
    }

    public String getMotif() {
        return motif.get();
    }

    public void setMotif(String value) {
        motif.set(value);
    }

    public String getObservation() {
        return observation.get();
    }

    public void setObservation(String value) {
        observation.set(value);
    }

    public BigDecimal getCout() {
        return cout.get();
    }

    public void setCout(BigDecimal value) {
        cout.set(value != null ? value : BigDecimal.ZERO);
        updateFormattedProperties();
    }

    public String getLieu() {
        return lieu.get();
    }

    public void setLieu(String value) {
        lieu.set(value);
    }

    public TypeEntretien getType() {
        return type.get();
    }

    public void setType(TypeEntretien value) {
        type.set(value != null ? value : TypeEntretien.Preventif);
        updateFormattedProperties();
    }

    public StatutOT getStatut() {
        return statut.get();
    }

    public void setStatut(StatutOT value) {
        statut.set(value != null ? value : StatutOT.Ouvert);
        updateFormattedProperties();
    }

    public String getDateEntreeFormatee() {
        return dateEntreeFormatee.get();
    }

    public String getDateSortieFormatee() {
        return dateSortieFormatee.get();
    }

    public String getCoutFormate() {
        return coutFormate.get();
    }

    public String getTypeLibelle() {
        return typeLibelle.get();
    }

    public String getStatutLibelle() {
        return statutLibelle.get();
    }

    public String getDureeFormatee() {
        return dureeFormatee.get();
    }

    public String getStatutCssClass() {
        return statutCssClass.get();
    }

    public boolean isTermine() {
        return termine.get();
    }
}
