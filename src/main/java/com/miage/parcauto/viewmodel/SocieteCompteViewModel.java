package main.java.com.miage.parcauto.viewmodel;

import main.java.com.miage.parcauto.dto.SocieteCompteDTO;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * ViewModel pour afficher et manipuler les comptes dans l'interface
 * utilisateur.
 * Fournit des propriétés JavaFX pour la liaison de données avec les contrôles
 * UI.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class SocieteCompteViewModel {

    private final StringProperty idCompte = new SimpleStringProperty();
    private final StringProperty numeroCompte = new SimpleStringProperty();
    private final StringProperty libelleCompte = new SimpleStringProperty();
    private final StringProperty soldeCompteFormate = new SimpleStringProperty();
    private final StringProperty typeCompte = new SimpleStringProperty();
    private final StringProperty dateCreationFormatee = new SimpleStringProperty();
    private final StringProperty idPersonnel = new SimpleStringProperty();
    private final StringProperty nomPersonnel = new SimpleStringProperty();
    private final BooleanProperty actif = new SimpleBooleanProperty();
    private final ObjectProperty<BigDecimal> soldeCompte = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> dateCreation = new SimpleObjectProperty<>();

    private SocieteCompteDTO compteDTO;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(Locale.FRANCE);

    /**
     * Constructeur par défaut.
     */
    public SocieteCompteViewModel() {
    }

    /**
     * Constructeur avec DTO.
     *
     * @param compteDTO Le DTO à utiliser pour initialiser le ViewModel
     */
    public SocieteCompteViewModel(SocieteCompteDTO compteDTO) {
        updateFromDTO(compteDTO);
    }

    /**
     * Met à jour le ViewModel à partir d'un DTO.
     *
     * @param dto Le DTO source
     */
    public final void updateFromDTO(SocieteCompteDTO dto) {
        if (dto == null) {
            return;
        }

        this.compteDTO = dto;

        idCompte.set(dto.getIdCompte() != null ? dto.getIdCompte().toString() : "");
        numeroCompte.set(dto.getNumeroCompte());
        libelleCompte.set(dto.getLibelleCompte());
        soldeCompte.set(dto.getSoldeCompte());
        soldeCompteFormate.set(formatMontant(dto.getSoldeCompte()));
        typeCompte.set(dto.getTypeCompte());
        dateCreation.set(dto.getDateCreation());
        dateCreationFormatee.set(formatDate(dto.getDateCreation()));
        idPersonnel.set(dto.getIdPersonnel() != null ? dto.getIdPersonnel().toString() : "");
        nomPersonnel.set(dto.getNomPersonnel());
        actif.set(dto.isActif());
    }

    /**
     * Convertit ce ViewModel en DTO.
     *
     * @return Le DTO correspondant
     */
    public SocieteCompteDTO toDTO() {
        SocieteCompteDTO dto = new SocieteCompteDTO();

        if (!idCompte.get().isEmpty()) {
            dto.setIdCompte(Integer.parseInt(idCompte.get()));
        }

        dto.setNumeroCompte(numeroCompte.get());
        dto.setLibelleCompte(libelleCompte.get());
        dto.setSoldeCompte(soldeCompte.get());
        dto.setTypeCompte(typeCompte.get());
        dto.setDateCreation(dateCreation.get());

        if (!idPersonnel.get().isEmpty()) {
            dto.setIdPersonnel(Integer.parseInt(idPersonnel.get()));
        }

        dto.setNomPersonnel(nomPersonnel.get());
        dto.setActif(actif.get());

        return dto;
    }

    /**
     * Formate un montant en devise.
     *
     * @param montant Le montant à formater
     * @return Le montant formaté en devise
     */
    private String formatMontant(BigDecimal montant) {
        return montant != null ? CURRENCY_FORMATTER.format(montant) : "";
    }

    /**
     * Formate une date.
     *
     * @param date La date à formater
     * @return La date formatée
     */
    private String formatDate(LocalDateTime date) {
        return date != null ? date.format(DATE_FORMATTER) : "";
    }

    /**
     * Détermine la classe CSS à appliquer en fonction du solde.
     *
     * @return La classe CSS correspondante
     */
    public String getSoldeClass() {
        if (soldeCompte.get() == null) {
            return "";
        }

        if (soldeCompte.get().compareTo(BigDecimal.ZERO) < 0) {
            return "solde-negatif";
        } else if (soldeCompte.get().compareTo(new BigDecimal("1000")) < 0) {
            return "solde-attention";
        } else {
            return "solde-positif";
        }
    }

    /**
     * Détermine la classe CSS à appliquer en fonction du statut actif.
     *
     * @return La classe CSS correspondante
     */
    public String getStatutClass() {
        return actif.get() ? "compte-actif" : "compte-inactif";
    }

    // Getters pour les propriétés JavaFX

    public StringProperty idCompteProperty() {
        return idCompte;
    }

    public StringProperty numeroCompteProperty() {
        return numeroCompte;
    }

    public StringProperty libelleCompteProperty() {
        return libelleCompte;
    }

    public StringProperty soldeCompteFormateProperty() {
        return soldeCompteFormate;
    }

    public StringProperty typeCompteProperty() {
        return typeCompte;
    }

    public StringProperty dateCreationFormateeProperty() {
        return dateCreationFormatee;
    }

    public StringProperty idPersonnelProperty() {
        return idPersonnel;
    }

    public StringProperty nomPersonnelProperty() {
        return nomPersonnel;
    }

    public BooleanProperty actifProperty() {
        return actif;
    }

    public ObjectProperty<BigDecimal> soldeCompteProperty() {
        return soldeCompte;
    }

    public ObjectProperty<LocalDateTime> dateCreationProperty() {
        return dateCreation;
    }

    // Getters et setters classiques

    public String getIdCompte() {
        return idCompte.get();
    }

    public void setIdCompte(String idCompte) {
        this.idCompte.set(idCompte);
    }

    public String getNumeroCompte() {
        return numeroCompte.get();
    }

    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte.set(numeroCompte);
    }

    public String getLibelleCompte() {
        return libelleCompte.get();
    }

    public void setLibelleCompte(String libelleCompte) {
        this.libelleCompte.set(libelleCompte);
    }

    public String getSoldeCompteFormate() {
        return soldeCompteFormate.get();
    }

    public void setSoldeCompte(BigDecimal soldeCompte) {
        this.soldeCompte.set(soldeCompte);
        this.soldeCompteFormate.set(formatMontant(soldeCompte));
    }

    public String getTypeCompte() {
        return typeCompte.get();
    }

    public void setTypeCompte(String typeCompte) {
        this.typeCompte.set(typeCompte);
    }

    public String getDateCreationFormatee() {
        return dateCreationFormatee.get();
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation.set(dateCreation);
        this.dateCreationFormatee.set(formatDate(dateCreation));
    }

    public String getNomPersonnel() {
        return nomPersonnel.get();
    }

    public void setNomPersonnel(String nomPersonnel) {
        this.nomPersonnel.set(nomPersonnel);
    }

    public boolean isActif() {
        return actif.get();
    }

    public void setActif(boolean actif) {
        this.actif.set(actif);
    }

    public BigDecimal getSoldeCompte() {
        return soldeCompte.get();
    }

    public LocalDateTime getDateCreation() {
        return dateCreation.get();
    }

    public String getIdPersonnel() {
        return idPersonnel.get();
    }

    public void setIdPersonnel(String idPersonnel) {
        this.idPersonnel.set(idPersonnel);
    }
}
