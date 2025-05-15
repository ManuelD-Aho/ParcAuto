package main.java.com.miage.parcauto.viewmodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.com.miage.parcauto.dto.DepenseDTO;
import main.java.com.miage.parcauto.dto.MissionDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ViewModel pour la visualisation des missions dans l'interface JavaFX.
 * Cette classe adapte les MissionDTO pour l'affichage et la liaison aux
 * composants UI.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class MissionViewModel {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Propriétés JavaFX pour binding bidirectionnel avec l'UI
    private final IntegerProperty idMission = new SimpleIntegerProperty();
    private final StringProperty vehicule = new SimpleStringProperty();
    private final StringProperty immatriculation = new SimpleStringProperty();
    private final StringProperty conducteur = new SimpleStringProperty();
    private final StringProperty destination = new SimpleStringProperty();
    private final StringProperty motif = new SimpleStringProperty();
    private final StringProperty dateDepart = new SimpleStringProperty();
    private final StringProperty dateRetourPrevue = new SimpleStringProperty();
    private final StringProperty statut = new SimpleStringProperty();
    private final ObservableList<DepenseDTO> depenses = FXCollections.observableArrayList();

    /**
     * Constructeur par défaut.
     */
    public MissionViewModel() {
        // Initialiser les valeurs par défaut
        updateFormattedProperties();
    }

    /**
     * Constructeur avec initialisation depuis un DTO.
     *
     * @param dto DTO source pour l'initialisation
     */
    public MissionViewModel(MissionDTO dto) {
        this.idMission.set(dto.getIdMission());
        this.vehicule.set(dto.getInfoVehicule());
        this.immatriculation.set(dto.getImmatriculationVehicule());
        this.conducteur.set(dto.getNomPrenomSocietaire());
        this.destination.set(dto.getDestination());
        this.motif.set(dto.getMotif());
        this.dateDepart.set(dto.getDateDepart() != null ? dto.getDateDepart().format(DATE_FORMATTER) : "");
        this.dateRetourPrevue
                .set(dto.getDateRetourPrevue() != null ? dto.getDateRetourPrevue().format(DATE_FORMATTER) : "");
        this.statut.set(dto.getStatut() != null ? dto.getStatut().name() : "");
        if (dto.getDepenses() != null) {
            this.depenses.addAll(dto.getDepenses());
        }
    }

    /**
     * Met à jour ce ViewModel avec les données d'un DTO.
     *
     * @param dto DTO source des données
     */
    public void updateFromDTO(MissionDTO dto) {
        if (dto == null)
            return;

        idMission.set(dto.getIdMission() != null ? dto.getIdMission() : 0);
        vehicule.set(dto.getInfoVehicule());
        immatriculation.set(dto.getImmatriculationVehicule());
        conducteur.set(dto.getNomPrenomSocietaire());
        destination.set(dto.getDestination());
        motif.set(dto.getMotif());
        dateDepart.set(dto.getDateDepart() != null ? dto.getDateDepart().format(DATE_FORMATTER) : "");
        dateRetourPrevue.set(dto.getDateRetourPrevue() != null ? dto.getDateRetourPrevue().format(DATE_FORMATTER) : "");
        statut.set(dto.getStatut() != null ? dto.getStatut().name() : "");

        // Mettre à jour la liste des dépenses
        if (dto.getDepenses() != null) {
            depenses.setAll(FXCollections.observableArrayList(dto.getDepenses()));
        } else {
            depenses.clear();
        }

        updateFormattedProperties();
    }

    /**
     * Convertit ce ViewModel en DTO.
     *
     * @return Nouveau DTO avec les données de ce ViewModel
     */
    public MissionDTO toDTO() {
        MissionDTO dto = new MissionDTO();
        dto.setIdMission(idMission.get() > 0 ? idMission.get() : null);
        dto.setInfoVehicule(vehicule.get());
        dto.setImmatriculationVehicule(immatriculation.get());
        dto.setNomPrenomSocietaire(conducteur.get());
        dto.setDestination(destination.get());
        dto.setMotif(motif.get());
        dto.setDateDepart(LocalDate.parse(dateDepart.get(), DATE_FORMATTER));
        dto.setDateRetourPrevue(LocalDate.parse(dateRetourPrevue.get(), DATE_FORMATTER));
        // Pas de conversion pour dateRetourReelle, à gérer séparément si nécessaire
        dto.setStatut(statut.get());
        dto.setDepenses(depenses.stream().collect(Collectors.toList()));

        return dto;
    }

    /**
     * Met à jour toutes les propriétés formatées.
     * Cette méthode est appelée automatiquement lorsque les données brutes
     * changent.
     */
    private void updateFormattedProperties() {
        // Format des dates
        // Aucune action nécessaire, les dates sont déjà formatées en String
        // dans les propriétés dateDepart et dateRetourPrevue.

        // Définir la classe CSS selon le statut
        if (statut.get() != null) {
            String status = statut.get().toLowerCase();
            if (status.contains("planifiée") || status.contains("planifiee")) {
                // statutBadgeClass.set("mission-planifiee");
            } else if (status.contains("cours")) {
                // statutBadgeClass.set("mission-encours");
            } else if (status.contains("terminée") || status.contains("terminee")) {
                // statutBadgeClass.set("mission-terminee");
            } else if (status.contains("annulée") || status.contains("annulee")) {
                // statutBadgeClass.set("mission-annulee");
            } else {
                // statutBadgeClass.set("");
            }
        } else {
            // statutBadgeClass.set("");
        }

        // Déterminer si la mission est terminée ou en cours
        if (statut.get() != null) {
            String status = statut.get().toLowerCase();
            // terminee.set(status.contains("terminée") || status.contains("terminee"));
            // enCours.set(status.contains("cours"));
        } else {
            // terminee.set(false);
            // enCours.set(false);
        }
    }

    /**
     * Ajoute une dépense à la liste des dépenses.
     *
     * @param depense Dépense à ajouter
     */
    public void ajouterDepense(DepenseDTO depense) {
        if (depense != null) {
            if (depense.getIdMission() == null) {
                depense.setIdMission(idMission.get() > 0 ? idMission.get() : null);
            }
            if (depenses != null) {
                depenses.add(depense);
            } else {
                ObservableList<DepenseDTO> list = FXCollections.observableArrayList();
                list.add(depense);
                depenses.set(list);
            }

            // Mettre à jour le coût total
            // recalculerCoutTotal();
        }
    }

    /**
     * Supprime une dépense de la liste des dépenses.
     *
     * @param depense Dépense à supprimer
     * @return true si supprimée, false sinon
     */
    public boolean supprimerDepense(DepenseDTO depense) {
        if (depense != null && depenses != null) {
            boolean result = depenses.remove(depense);
            if (result) {
                // Mettre à jour le coût total
                // recalculerCoutTotal();
            }
            return result;
        }
        return false;
    }

    /**
     * Recalcule le coût total de la mission en fonction des dépenses.
     */
    private void recalculerCoutTotal() {
        // Pas de coût total à recalculer dans ce modèle
    }

    /**
     * Retourne la couleur de fond pour le statut de la mission.
     *
     * @return Style CSS pour le statut
     */
    public String getStatusBadgeStyle() {
        if (statut.get() == null)
            return "";

        String status = statut.get().toLowerCase();
        if (status.contains("planifiée") || status.contains("planifiee")) {
            return "-fx-background-color: #bbdefb; -fx-text-fill: #0d47a1;"; // Bleu pour planifiée
        } else if (status.contains("cours")) {
            return "-fx-background-color: #ffecb3; -fx-text-fill: #e65100;"; // Orange pour en cours
        } else if (status.contains("terminée") || status.contains("terminee")) {
            return "-fx-background-color: #c8e6c9; -fx-text-fill: #1b5e20;"; // Vert pour terminée
        } else if (status.contains("annulée") || status.contains("annulee")) {
            return "-fx-background-color: #ffcdd2; -fx-text-fill: #b71c1c;"; // Rouge pour annulée
        } else {
            return "-fx-background-color: #e0e0e0; -fx-text-fill: #424242;"; // Gris pour autre
        }
    }

    /**
     * Retourne la destination formatée avec le motif entre parenthèses.
     *
     * @return Destination formatée
     */
    public String getDestinationFormatee() {
        if (destination.get() != null && !destination.get().isEmpty()) {
            if (motif.get() != null && !motif.get().isEmpty()) {
                return destination.get() + " (" + motif.get() + ")";
            } else {
                return destination.get();
            }
        }
        return "Non définie";
    }

    // Getters pour toutes les propriétés JavaFX (pour le binding)

    public IntegerProperty idMissionProperty() {
        return idMission;
    }

    public StringProperty vehiculeProperty() {
        return vehicule;
    }

    public StringProperty immatriculationProperty() {
        return immatriculation;
    }

    public StringProperty conducteurProperty() {
        return conducteur;
    }

    public StringProperty destinationProperty() {
        return destination;
    }

    public StringProperty motifProperty() {
        return motif;
    }

    public StringProperty dateDepartProperty() {
        return dateDepart;
    }

    public StringProperty dateRetourPrevueProperty() {
        return dateRetourPrevue;
    }

    public StringProperty statutProperty() {
        return statut;
    }

    public ObservableList<DepenseDTO> getDepenses() {
        return depenses;
    }

    /**
     * Retourne la classe CSS à utiliser pour le badge de statut.
     */
    public String getStatusBadgeClass() {
        switch (statut.get()) {
            case "Planifiee":
                return "badge-planifiee";
            case "EnCours":
                return "badge-encours";
            case "Cloturee":
                return "badge-cloturee";
            default:
                return "badge-inconnu";
        }
    }

    /**
     * Retourne la destination formatée pour l'affichage.
     */
    public String getDestinationFormatee() {
        return destination.get() != null ? destination.get().toUpperCase() : "";
    }

    // Getters et Setters standards

    public Integer getIdMission() {
        return idMission.get();
    }

    public void setIdMission(Integer id) {
        idMission.set(id != null ? id : 0);
    }

    public String getVehicule() {
        return vehicule.get();
    }

    public void setVehicule(String value) {
        vehicule.set(value);
    }

    public String getImmatriculation() {
        return immatriculation.get();
    }

    public void setImmatriculation(String value) {
        immatriculation.set(value);
    }

    public String getConducteur() {
        return conducteur.get();
    }

    public void setConducteur(String value) {
        conducteur.set(value);
    }

    public String getDestination() {
        return destination.get();
    }

    public void setDestination(String value) {
        destination.set(value);
        updateFormattedProperties();
    }

    public String getMotif() {
        return motif.get();
    }

    public void setMotif(String value) {
        motif.set(value);
        updateFormattedProperties();
    }

    public String getDateDepart() {
        return dateDepart.get();
    }

    public void setDateDepart(String date) {
        dateDepart.set(date);
        updateFormattedProperties();
    }

    public String getDateRetourPrevue() {
        return dateRetourPrevue.get();
    }

    public void setDateRetourPrevue(String date) {
        dateRetourPrevue.set(date);
        updateFormattedProperties();
    }

    public String getStatut() {
        return statut.get();
    }

    public void setStatut(String value) {
        statut.set(value);
        updateFormattedProperties();
    }

    public ObservableList<DepenseDTO> getDepensesList() {
        return depenses;
    }

    public void setDepenses(List<DepenseDTO> list) {
        if (list != null) {
            depenses.setAll(FXCollections.observableArrayList(list));
        } else {
            depenses.setAll(FXCollections.observableArrayList());
        }
        // recalculerCoutTotal();
    }

    public String getDateDepartFormatee() {
        return dateDepart.get();
    }

    public String getDateRetourPrevueFormatee() {
        return dateRetourPrevue.get();
    }

    public int getDureeJours() {
        return 0; // Pas de calcul de durée dans ce modèle
    }

    public int getDistanceKm() {
        return 0; // Pas de calcul de distance dans ce modèle
    }

    public String getCoutTotalFormate() {
        return "0,00 €"; // Pas de coût total dans ce modèle
    }

    public String getStatutBadgeClass() {
        return getStatusBadgeClass();
    }

    public boolean isTerminee() {
        return false; // Pas de statut terminée dans ce modèle
    }

    public boolean isEnCours() {
        return false; // Pas de statut en cours dans ce modèle
    }

    // ATTENTION : Les imports javafx.* ne sont pas résolus. Vérifiez que JavaFX est
    // bien ajouté au classpath/modulepath du projet.
    // Pour un projet Maven/Gradle, ajoutez les dépendances javafx-base,
    // javafx-controls, javafx-graphics, etc. selon votre JDK.
    // Pour un projet classique, ajoutez les jars JavaFX 17+ dans le modulepath.
}
