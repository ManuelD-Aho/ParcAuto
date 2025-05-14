package main.java.com.miage.parcauto.viewmodel;

import javafx.beans.property.*;
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
    private final IntegerProperty idVehicule = new SimpleIntegerProperty();
    private final IntegerProperty idConducteur = new SimpleIntegerProperty();
    private final StringProperty immatriculation = new SimpleStringProperty();
    private final StringProperty marqueModele = new SimpleStringProperty();
    private final StringProperty nomConducteur = new SimpleStringProperty();
    private final StringProperty destination = new SimpleStringProperty();
    private final StringProperty motif = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> dateDepart = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> dateRetourPrevue = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> dateRetourReelle = new SimpleObjectProperty<>();
    private final ObjectProperty<Integer> kilometrageDepart = new SimpleObjectProperty<>(0);
    private final ObjectProperty<Integer> kilometrageRetour = new SimpleObjectProperty<>(0);
    private final StringProperty statut = new SimpleStringProperty();
    private final ObjectProperty<BigDecimal> coutTotal = new SimpleObjectProperty<>(BigDecimal.ZERO);
    private final ListProperty<DepenseDTO> depenses = new SimpleListProperty<>(FXCollections.observableArrayList());

    // Propriétés calculées pour l'affichage formaté
    private final StringProperty dateDepartFormatee = new SimpleStringProperty();
    private final StringProperty dateRetourPrevueFormatee = new SimpleStringProperty();
    private final StringProperty dateRetourReelleFormatee = new SimpleStringProperty();
    private final IntegerProperty dureeJours = new SimpleIntegerProperty();
    private final IntegerProperty distanceKm = new SimpleIntegerProperty();
    private final StringProperty coutTotalFormate = new SimpleStringProperty();
    private final StringProperty statutBadgeClass = new SimpleStringProperty();
    private final BooleanProperty terminee = new SimpleBooleanProperty();
    private final BooleanProperty enCours = new SimpleBooleanProperty();

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
        updateFromDTO(dto);
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
        idVehicule.set(dto.getIdVehicule() != null ? dto.getIdVehicule() : 0);
        idConducteur.set(dto.getIdConducteur() != null ? dto.getIdConducteur() : 0);
        immatriculation.set(dto.getImmatriculation());
        marqueModele.set(dto.getMarqueModele());
        nomConducteur.set(dto.getNomConducteur());
        destination.set(dto.getDestination());
        motif.set(dto.getMotif());
        dateDepart.set(dto.getDateDepart());
        dateRetourPrevue.set(dto.getDateRetourPrevue());
        dateRetourReelle.set(dto.getDateRetourReelle());
        kilometrageDepart.set(dto.getKilometrageDepart());
        kilometrageRetour.set(dto.getKilometrageRetour());
        statut.set(dto.getStatut());
        coutTotal.set(dto.getCoutTotal());

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
        dto.setIdVehicule(idVehicule.get() > 0 ? idVehicule.get() : null);
        dto.setIdConducteur(idConducteur.get() > 0 ? idConducteur.get() : null);
        dto.setImmatriculation(immatriculation.get());
        dto.setMarqueModele(marqueModele.get());
        dto.setNomConducteur(nomConducteur.get());
        dto.setDestination(destination.get());
        dto.setMotif(motif.get());
        dto.setDateDepart(dateDepart.get());
        dto.setDateRetourPrevue(dateRetourPrevue.get());
        dto.setDateRetourReelle(dateRetourReelle.get());
        dto.setKilometrageDepart(kilometrageDepart.get());
        dto.setKilometrageRetour(kilometrageRetour.get());
        dto.setStatut(statut.get());
        dto.setCoutTotal(coutTotal.get());
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
        if (dateDepart.get() != null) {
            dateDepartFormatee.set(dateDepart.get().format(DATE_FORMATTER));
        } else {
            dateDepartFormatee.set("Non définie");
        }

        if (dateRetourPrevue.get() != null) {
            dateRetourPrevueFormatee.set(dateRetourPrevue.get().format(DATE_FORMATTER));
        } else {
            dateRetourPrevueFormatee.set("Non définie");
        }

        if (dateRetourReelle.get() != null) {
            dateRetourReelleFormatee.set(dateRetourReelle.get().format(DATE_FORMATTER));
        } else {
            dateRetourReelleFormatee.set("En cours");
        }

        // Calcul de la durée
        if (dateDepart.get() != null && dateRetourPrevue.get() != null) {
            dureeJours.set((int) ChronoUnit.DAYS.between(dateDepart.get(), dateRetourPrevue.get()) + 1);
        } else {
            dureeJours.set(0);
        }

        // Calcul de la distance
        if (kilometrageRetour.get() > 0 && kilometrageDepart.get() > 0) {
            distanceKm.set(kilometrageRetour.get() - kilometrageDepart.get());
        } else {
            distanceKm.set(0);
        }

        // Format du coût total
        if (coutTotal.get() != null) {
            coutTotalFormate.set(coutTotal.get() + " €");
        } else {
            coutTotalFormate.set("0,00 €");
        }

        // Définir la classe CSS selon le statut
        if (statut.get() != null) {
            String status = statut.get().toLowerCase();
            if (status.contains("planifiée") || status.contains("planifiee")) {
                statutBadgeClass.set("mission-planifiee");
            } else if (status.contains("cours")) {
                statutBadgeClass.set("mission-encours");
            } else if (status.contains("terminée") || status.contains("terminee")) {
                statutBadgeClass.set("mission-terminee");
            } else if (status.contains("annulée") || status.contains("annulee")) {
                statutBadgeClass.set("mission-annulee");
            } else {
                statutBadgeClass.set("");
            }
        } else {
            statutBadgeClass.set("");
        }

        // Déterminer si la mission est terminée ou en cours
        if (statut.get() != null) {
            String status = statut.get().toLowerCase();
            terminee.set(status.contains("terminée") || status.contains("terminee"));
            enCours.set(status.contains("cours"));
        } else {
            terminee.set(false);
            enCours.set(false);
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
            if (depenses.get() != null) {
                depenses.get().add(depense);
            } else {
                ObservableList<DepenseDTO> list = FXCollections.observableArrayList();
                list.add(depense);
                depenses.set(list);
            }

            // Mettre à jour le coût total
            recalculerCoutTotal();
        }
    }

    /**
     * Supprime une dépense de la liste des dépenses.
     *
     * @param depense Dépense à supprimer
     * @return true si supprimée, false sinon
     */
    public boolean supprimerDepense(DepenseDTO depense) {
        if (depense != null && depenses.get() != null) {
            boolean result = depenses.get().remove(depense);
            if (result) {
                // Mettre à jour le coût total
                recalculerCoutTotal();
            }
            return result;
        }
        return false;
    }

    /**
     * Recalcule le coût total de la mission en fonction des dépenses.
     */
    private void recalculerCoutTotal() {
        if (depenses.get() != null) {
            BigDecimal total = depenses.get().stream()
                    .map(DepenseDTO::getMontant)
                    .filter(m -> m != null)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            coutTotal.set(total);
            coutTotalFormate.set(total + " €");
        } else {
            coutTotal.set(BigDecimal.ZERO);
            coutTotalFormate.set("0,00 €");
        }
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

    public IntegerProperty idVehiculeProperty() {
        return idVehicule;
    }

    public IntegerProperty idConducteurProperty() {
        return idConducteur;
    }

    public StringProperty immatriculationProperty() {
        return immatriculation;
    }

    public StringProperty marqueModeleProperty() {
        return marqueModele;
    }

    public StringProperty nomConducteurProperty() {
        return nomConducteur;
    }

    public StringProperty destinationProperty() {
        return destination;
    }

    public StringProperty motifProperty() {
        return motif;
    }

    public ObjectProperty<LocalDate> dateDepartProperty() {
        return dateDepart;
    }

    public ObjectProperty<LocalDate> dateRetourPrevueProperty() {
        return dateRetourPrevue;
    }

    public ObjectProperty<LocalDate> dateRetourReelleProperty() {
        return dateRetourReelle;
    }

    public ObjectProperty<Integer> kilometrageDepartProperty() {
        return kilometrageDepart;
    }

    public ObjectProperty<Integer> kilometrageRetourProperty() {
        return kilometrageRetour;
    }

    public StringProperty statutProperty() {
        return statut;
    }

    public ObjectProperty<BigDecimal> coutTotalProperty() {
        return coutTotal;
    }

    public ListProperty<DepenseDTO> depensesProperty() {
        return depenses;
    }

    public StringProperty dateDepartFormateeProperty() {
        return dateDepartFormatee;
    }

    public StringProperty dateRetourPrevueFormateeProperty() {
        return dateRetourPrevueFormatee;
    }

    public StringProperty dateRetourReelleFormateeProperty() {
        return dateRetourReelleFormatee;
    }

    public IntegerProperty dureeJoursProperty() {
        return dureeJours;
    }

    public IntegerProperty distanceKmProperty() {
        return distanceKm;
    }

    public StringProperty coutTotalFormateProperty() {
        return coutTotalFormate;
    }

    public StringProperty statutBadgeClassProperty() {
        return statutBadgeClass;
    }

    public BooleanProperty termineeProperty() {
        return terminee;
    }

    public BooleanProperty enCoursProperty() {
        return enCours;
    }

    // Getters et Setters standards

    public Integer getIdMission() {
        return idMission.get();
    }

    public void setIdMission(Integer id) {
        idMission.set(id != null ? id : 0);
    }

    public Integer getIdVehicule() {
        return idVehicule.get();
    }

    public void setIdVehicule(Integer id) {
        idVehicule.set(id != null ? id : 0);
    }

    public Integer getIdConducteur() {
        return idConducteur.get();
    }

    public void setIdConducteur(Integer id) {
        idConducteur.set(id != null ? id : 0);
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

    public String getNomConducteur() {
        return nomConducteur.get();
    }

    public void setNomConducteur(String value) {
        nomConducteur.set(value);
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

    public LocalDate getDateDepart() {
        return dateDepart.get();
    }

    public void setDateDepart(LocalDate date) {
        dateDepart.set(date);
        updateFormattedProperties();
    }

    public LocalDate getDateRetourPrevue() {
        return dateRetourPrevue.get();
    }

    public void setDateRetourPrevue(LocalDate date) {
        dateRetourPrevue.set(date);
        updateFormattedProperties();
    }

    public LocalDate getDateRetourReelle() {
        return dateRetourReelle.get();
    }

    public void setDateRetourReelle(LocalDate date) {
        dateRetourReelle.set(date);
        updateFormattedProperties();
    }

    public Integer getKilometrageDepart() {
        return kilometrageDepart.get();
    }

    public void setKilometrageDepart(Integer km) {
        kilometrageDepart.set(km != null ? km : 0);
        updateFormattedProperties();
    }

    public Integer getKilometrageRetour() {
        return kilometrageRetour.get();
    }

    public void setKilometrageRetour(Integer km) {
        kilometrageRetour.set(km != null ? km : 0);
        updateFormattedProperties();
    }

    public String getStatut() {
        return statut.get();
    }

    public void setStatut(String value) {
        statut.set(value);
        updateFormattedProperties();
    }

    public BigDecimal getCoutTotal() {
        return coutTotal.get();
    }

    public void setCoutTotal(BigDecimal value) {
        coutTotal.set(value != null ? value : BigDecimal.ZERO);
        updateFormattedProperties();
    }

    public ObservableList<DepenseDTO> getDepenses() {
        return depenses.get();
    }

    public void setDepenses(List<DepenseDTO> list) {
        if (list != null) {
            depenses.set(FXCollections.observableArrayList(list));
        } else {
            depenses.set(FXCollections.observableArrayList());
        }
        recalculerCoutTotal();
    }

    public String getDateDepartFormatee() {
        return dateDepartFormatee.get();
    }

    public String getDateRetourPrevueFormatee() {
        return dateRetourPrevueFormatee.get();
    }

    public String getDateRetourReelleFormatee() {
        return dateRetourReelleFormatee.get();
    }

    public int getDureeJours() {
        return dureeJours.get();
    }

    public int getDistanceKm() {
        return distanceKm.get();
    }

    public String getCoutTotalFormate() {
        return coutTotalFormate.get();
    }

    public String getStatutBadgeClass() {
        return statutBadgeClass.get();
    }

    public boolean isTerminee() {
        return terminee.get();
    }

    public boolean isEnCours() {
        return enCours.get();
    }
}
