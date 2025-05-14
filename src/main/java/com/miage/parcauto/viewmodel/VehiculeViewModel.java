package main.java.com.miage.parcauto.viewmodel;

import main.java.com.miage.parcauto.dto.VehiculeDTO;
import main.java.com.miage.parcauto.model.vehicule.EtatVoiture;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * ViewModel pour représenter un véhicule dans l'interface utilisateur.
 * Encapsule la logique de formatage et de présentation des données d'un
 * véhicule.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class VehiculeViewModel {

    // Constantes de formatage
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(Locale.FRANCE);
    private static final NumberFormat NUMBER_FORMATTER = NumberFormat.getNumberInstance(Locale.FRANCE);

    // Propriétés JavaFX pour liaison bidirectionnelle avec l'interface
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final ObjectProperty<EtatVoiture> etat = new SimpleObjectProperty<>();
    private final StringProperty marque = new SimpleStringProperty();
    private final StringProperty modele = new SimpleStringProperty();
    private final StringProperty immatriculation = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> dateAcquisition = new SimpleObjectProperty<>();
    private final IntegerProperty kilometres = new SimpleIntegerProperty();
    private final ObjectProperty<BigDecimal> prix = new SimpleObjectProperty<>();
    private final BooleanProperty enMission = new SimpleBooleanProperty();
    private final StringProperty couleur = new SimpleStringProperty();
    private final IntegerProperty puissance = new SimpleIntegerProperty();
    private final IntegerProperty places = new SimpleIntegerProperty();

    // Propriétés dérivées pour l'affichage formaté
    private final StringProperty statutFormate = new SimpleStringProperty();
    private final StringProperty prixFormate = new SimpleStringProperty();
    private final StringProperty kilometrageFormate = new SimpleStringProperty();
    private final StringProperty dateFormatee = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();

    /**
     * Constructeur à partir d'un DTO.
     * 
     * @param dto VehiculeDTO source des données
     */
    public VehiculeViewModel(VehiculeDTO dto) {
        updateFromDTO(dto);
    }

    /**
     * Constructeur par défaut.
     */
    public VehiculeViewModel() {
        // Valeurs par défaut
    }

    /**
     * Met à jour le ViewModel à partir d'un DTO.
     * 
     * @param dto VehiculeDTO source des données
     */
    public final void updateFromDTO(VehiculeDTO dto) {
        if (dto == null) {
            return;
        }

        id.set(dto.getIdVehicule() == null ? 0 : dto.getIdVehicule());
        etat.set(dto.getEtatVoiture());
        marque.set(dto.getMarque());
        modele.set(dto.getModele());
        immatriculation.set(dto.getImmatriculation());
        dateAcquisition.set(dto.getDateAcquisition());
        kilometres.set(dto.getKmActuels() == null ? 0 : dto.getKmActuels());
        prix.set(dto.getPrixVehicule());
        enMission.set(dto.getEnMission() == null ? false : dto.getEnMission());
        couleur.set(dto.getCouleur());
        puissance.set(dto.getPuissance() == null ? 0 : dto.getPuissance());
        places.set(dto.getNbPlaces() == null ? 0 : dto.getNbPlaces());

        // Mise à jour des propriétés formatées
        updateFormattedProperties();
    }

    /**
     * Convertit ce ViewModel en DTO.
     * 
     * @return VehiculeDTO correspondant
     */
    public VehiculeDTO toDTO() {
        VehiculeDTO dto = new VehiculeDTO();

        dto.setIdVehicule(id.get() == 0 ? null : id.get());
        dto.setEtatVoiture(etat.get());
        dto.setMarque(marque.get());
        dto.setModele(modele.get());
        dto.setImmatriculation(immatriculation.get());
        dto.setDateAcquisition(dateAcquisition.get());
        dto.setKmActuels(kilometres.get());
        dto.setPrixVehicule(prix.get());
        dto.setEnMission(enMission.get());
        dto.setCouleur(couleur.get());
        dto.setPuissance(puissance.get());
        dto.setNbPlaces(places.get());

        return dto;
    }

    /**
     * Met à jour les propriétés formatées.
     */
    private void updateFormattedProperties() {
        // Format de l'état
        if (etat.get() != null) {
            statutFormate.set(etat.get().name());
        }

        // Format du prix
        if (prix.get() != null) {
            prixFormate.set(CURRENCY_FORMATTER.format(prix.get()));
        }

        // Format du kilométrage
        kilometrageFormate.set(NUMBER_FORMATTER.format(kilometres.get()) + " km");

        // Format de la date
        if (dateAcquisition.get() != null) {
            dateFormatee.set(dateAcquisition.get().format(DATE_FORMATTER));
        }

        // Description complète
        StringBuilder desc = new StringBuilder();
        if (marque.get() != null && modele.get() != null) {
            desc.append(marque.get()).append(" ").append(modele.get());
        }
        if (immatriculation.get() != null && !immatriculation.get().isEmpty()) {
            desc.append(" (").append(immatriculation.get()).append(")");
        }
        description.set(desc.toString());
    }

    /**
     * Retourne la classe CSS correspondant à l'état du véhicule.
     * 
     * @return Nom de la classe CSS
     */
    public String getStatutClass() {
        if (etat.get() == null) {
            return "status-unknown";
        }

        switch (etat.get()) {
            case EnService:
                return enMission.get() ? "status-on-mission" : "status-available";
            case EnEntretien:
                return "status-maintenance";
            case HorsService:
                return "status-out-of-service";
            case EnAttente:
                return "status-waiting";
            default:
                return "status-unknown";
        }
    }

    // Getters pour les propriétés JavaFX

    public IntegerProperty idProperty() {
        return id;
    }

    public ObjectProperty<EtatVoiture> etatProperty() {
        return etat;
    }

    public StringProperty marqueProperty() {
        return marque;
    }

    public StringProperty modeleProperty() {
        return modele;
    }

    public StringProperty immatriculationProperty() {
        return immatriculation;
    }

    public ObjectProperty<LocalDateTime> dateAcquisitionProperty() {
        return dateAcquisition;
    }

    public IntegerProperty kilometresProperty() {
        return kilometres;
    }

    public ObjectProperty<BigDecimal> prixProperty() {
        return prix;
    }

    public BooleanProperty enMissionProperty() {
        return enMission;
    }

    public StringProperty couleurProperty() {
        return couleur;
    }

    public IntegerProperty puissanceProperty() {
        return puissance;
    }

    public IntegerProperty placesProperty() {
        return places;
    }

    public StringProperty statutFormateProperty() {
        return statutFormate;
    }

    public StringProperty prixFormateProperty() {
        return prixFormate;
    }

    public StringProperty kilometrageFormateProperty() {
        return kilometrageFormate;
    }

    public StringProperty dateFormateeProperty() {
        return dateFormatee;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    // Getters et setters standards

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public EtatVoiture getEtat() {
        return etat.get();
    }

    public void setEtat(EtatVoiture etat) {
        this.etat.set(etat);
        updateFormattedProperties();
    }

    public String getMarque() {
        return marque.get();
    }

    public void setMarque(String marque) {
        this.marque.set(marque);
        updateFormattedProperties();
    }

    public String getModele() {
        return modele.get();
    }

    public void setModele(String modele) {
        this.modele.set(modele);
        updateFormattedProperties();
    }

    public String getImmatriculation() {
        return immatriculation.get();
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation.set(immatriculation);
        updateFormattedProperties();
    }

    public LocalDateTime getDateAcquisition() {
        return dateAcquisition.get();
    }

    public void setDateAcquisition(LocalDateTime dateAcquisition) {
        this.dateAcquisition.set(dateAcquisition);
        updateFormattedProperties();
    }

    public int getKilometres() {
        return kilometres.get();
    }

    public void setKilometres(int kilometres) {
        this.kilometres.set(kilometres);
        updateFormattedProperties();
    }

    public BigDecimal getPrix() {
        return prix.get();
    }

    public void setPrix(BigDecimal prix) {
        this.prix.set(prix);
        updateFormattedProperties();
    }

    public boolean isEnMission() {
        return enMission.get();
    }

    public void setEnMission(boolean enMission) {
        this.enMission.set(enMission);
        updateFormattedProperties();
    }

    public String getStatutFormate() {
        return statutFormate.get();
    }

    public String getPrixFormate() {
        return prixFormate.get();
    }

    public String getKilometrageFormate() {
        return kilometrageFormate.get();
    }

    public String getDateFormatee() {
        return dateFormatee.get();
    }

    public String getDescription() {
        return description.get();
    }
}
