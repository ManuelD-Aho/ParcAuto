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
    private final IntegerProperty idVehicule = new SimpleIntegerProperty();
    private final StringProperty marque = new SimpleStringProperty();
    private final StringProperty modele = new SimpleStringProperty();
    private final StringProperty immatriculation = new SimpleStringProperty();
    private final ObjectProperty<EtatVoiture> etat = new SimpleObjectProperty<>();
    private final IntegerProperty kilometrage = new SimpleIntegerProperty();
    private final BooleanProperty enMission = new SimpleBooleanProperty();
    private final ObjectProperty<BigDecimal> prixVehicule = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> dateAcquisition = new SimpleObjectProperty<>();

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.FRANCE);

    /**
     * Constructeur à partir d'un DTO Vehicule.
     * 
     * @param dto VehiculeDTO source
     */
    public VehiculeViewModel(VehiculeDTO dto) {
        this.idVehicule.set(dto.getIdVehicule());
        this.marque.set(dto.getMarque());
        this.modele.set(dto.getModele());
        this.immatriculation.set(dto.getImmatriculation());
        this.etat.set(dto.getEtatVoiture());
        this.kilometrage.set(dto.getKmActuels() != null ? dto.getKmActuels() : 0);
        this.enMission.set(dto.getEnMission() != null ? dto.getEnMission() : false);
        this.prixVehicule.set(dto.getPrixVehicule());
        this.dateAcquisition.set(dto.getDateAcquisition());
    }

    public IntegerProperty idVehiculeProperty() {
        return idVehicule;
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

    public ObjectProperty<EtatVoiture> etatProperty() {
        return etat;
    }

    public IntegerProperty kilometrageProperty() {
        return kilometrage;
    }

    public BooleanProperty enMissionProperty() {
        return enMission;
    }

    public ObjectProperty<BigDecimal> prixVehiculeProperty() {
        return prixVehicule;
    }

    public ObjectProperty<LocalDateTime> dateAcquisitionProperty() {
        return dateAcquisition;
    }

    /**
     * Retourne la classe CSS à utiliser selon l'état du véhicule.
     */
    public String getStatutClass() {
        if (etat.get() == null)
            return "statut-inconnu";
        switch (etat.get()) {
            case DISPONIBLE:
                return "statut-disponible";
            case EN_MAINTENANCE:
                return "statut-maintenance";
            case EN_MISSION:
                return "statut-mission";
            case HORS_SERVICE:
                return "statut-horsservice";
            default:
                return "statut-inconnu";
        }
    }

    /**
     * Retourne le kilométrage formaté pour l'affichage.
     */
    public String getKilometrageFormate() {
        return NumberFormat.getInstance(Locale.FRANCE).format(kilometrage.get()) + " km";
    }

    /**
     * Retourne le prix du véhicule formaté en euros.
     */
    public String getPrixFormate() {
        return prixVehicule.get() != null ? CURRENCY_FORMAT.format(prixVehicule.get()) : "0 €";
    }

    /**
     * Retourne la date d'acquisition formatée.
     */
    public String getDateAcquisitionFormatee() {
        return dateAcquisition.get() != null ? dateAcquisition.get().format(FORMATTER) : "";
    }
}
