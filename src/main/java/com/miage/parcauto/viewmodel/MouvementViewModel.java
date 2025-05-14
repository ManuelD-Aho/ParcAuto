package main.java.com.miage.parcauto.viewmodel;

import main.java.com.miage.parcauto.dto.MouvementDTO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * ViewModel pour afficher et manipuler les mouvements financiers dans
 * l'interface utilisateur.
 * Fournit des propriétés JavaFX pour la liaison de données avec les contrôles
 * UI.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class MouvementViewModel {

    private final StringProperty idMouvement = new SimpleStringProperty();
    private final StringProperty idCompte = new SimpleStringProperty();
    private final StringProperty numeroCompte = new SimpleStringProperty();
    private final StringProperty libelleCompte = new SimpleStringProperty();
    private final StringProperty dateMouvementFormatee = new SimpleStringProperty();
    private final StringProperty montantFormate = new SimpleStringProperty();
    private final StringProperty libelle = new SimpleStringProperty();
    private final StringProperty typeMouvement = new SimpleStringProperty();
    private final StringProperty idMission = new SimpleStringProperty();
    private final StringProperty referenceMission = new SimpleStringProperty();
    private final StringProperty idEntretien = new SimpleStringProperty();
    private final StringProperty referenceEntretien = new SimpleStringProperty();
    private final StringProperty categorie = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> dateMouvement = new SimpleObjectProperty<>();
    private final ObjectProperty<BigDecimal> montant = new SimpleObjectProperty<>();

    private MouvementDTO mouvementDTO;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(Locale.FRANCE);

    /**
     * Constructeur par défaut.
     */
    public MouvementViewModel() {
    }

    /**
     * Constructeur avec DTO.
     *
     * @param mouvementDTO Le DTO à utiliser pour initialiser le ViewModel
     */
    public MouvementViewModel(MouvementDTO mouvementDTO) {
        updateFromDTO(mouvementDTO);
    }

    /**
     * Met à jour le ViewModel à partir d'un DTO.
     *
     * @param dto Le DTO source
     */
    public final void updateFromDTO(MouvementDTO dto) {
        if (dto == null) {
            return;
        }

        this.mouvementDTO = dto;

        idMouvement.set(dto.getIdMouvement() != null ? dto.getIdMouvement().toString() : "");
        idCompte.set(dto.getIdCompte() != null ? dto.getIdCompte().toString() : "");
        numeroCompte.set(dto.getNumeroCompte());
        libelleCompte.set(dto.getLibelleCompte());
        dateMouvement.set(dto.getDateMouvement());
        dateMouvementFormatee.set(formatDate(dto.getDateMouvement()));
        montant.set(dto.getMontant());
        montantFormate.set(formatMontant(dto.getMontant()));
        libelle.set(dto.getLibelle());
        typeMouvement.set(dto.getTypeMouvement());
        idMission.set(dto.getIdMission() != null ? dto.getIdMission().toString() : "");
        referenceMission.set(dto.getReferenceMission());
        idEntretien.set(dto.getIdEntretien() != null ? dto.getIdEntretien().toString() : "");
        referenceEntretien.set(dto.getReferenceEntretien());
        categorie.set(dto.getCategorie());
    }

    /**
     * Convertit ce ViewModel en DTO.
     *
     * @return Le DTO correspondant
     */
    public MouvementDTO toDTO() {
        MouvementDTO dto = new MouvementDTO();

        if (!idMouvement.get().isEmpty()) {
            dto.setIdMouvement(Integer.parseInt(idMouvement.get()));
        }

        if (!idCompte.get().isEmpty()) {
            dto.setIdCompte(Integer.parseInt(idCompte.get()));
        }

        dto.setNumeroCompte(numeroCompte.get());
        dto.setLibelleCompte(libelleCompte.get());
        dto.setDateMouvement(dateMouvement.get());
        dto.setMontant(montant.get());
        dto.setLibelle(libelle.get());
        dto.setTypeMouvement(typeMouvement.get());

        if (!idMission.get().isEmpty()) {
            dto.setIdMission(Integer.parseInt(idMission.get()));
        }

        dto.setReferenceMission(referenceMission.get());

        if (!idEntretien.get().isEmpty()) {
            dto.setIdEntretien(Integer.parseInt(idEntretien.get()));
        }

        dto.setReferenceEntretien(referenceEntretien.get());
        dto.setCategorie(categorie.get());

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
    private String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : "";
    }

    /**
     * Détermine la classe CSS à appliquer en fonction du type de mouvement.
     *
     * @return La classe CSS correspondante
     */
    public String getTypeMouvementClass() {
        if (typeMouvement.get() == null) {
            return "";
        }

        return "DEBIT".equals(typeMouvement.get()) ? "mouvement-debit" : "mouvement-credit";
    }

    /**
     * Détermine le signe à afficher en fonction du type de mouvement.
     *
     * @return Le signe correspondant
     */
    public String getMontantAvecSigne() {
        if (montant.get() == null) {
            return "";
        }

        return "DEBIT".equals(typeMouvement.get())
                ? "- " + formatMontant(montant.get())
                : "+ " + formatMontant(montant.get());
    }

    /**
     * Indique si ce mouvement est lié à une mission.
     *
     * @return true si le mouvement est lié à une mission, false sinon
     */
    public boolean isLieAMission() {
        return idMission.get() != null && !idMission.get().isEmpty();
    }

    /**
     * Indique si ce mouvement est lié à un entretien.
     *
     * @return true si le mouvement est lié à un entretien, false sinon
     */
    public boolean isLieAEntretien() {
        return idEntretien.get() != null && !idEntretien.get().isEmpty();
    }

    /**
     * Détermine une catégorie d'affichage pour le mouvement.
     *
     * @return La catégorie d'affichage
     */
    public String getCategorieAffichage() {
        if (isLieAMission()) {
            return "Mission: " + referenceMission.get();
        } else if (isLieAEntretien()) {
            return "Entretien: " + referenceEntretien.get();
        } else {
            return categorie.get();
        }
    }

    // Getters pour les propriétés JavaFX

    public StringProperty idMouvementProperty() {
        return idMouvement;
    }

    public StringProperty idCompteProperty() {
        return idCompte;
    }

    public StringProperty numeroCompteProperty() {
        return numeroCompte;
    }

    public StringProperty libelleCompteProperty() {
        return libelleCompte;
    }

    public StringProperty dateMouvementFormateeProperty() {
        return dateMouvementFormatee;
    }

    public StringProperty montantFormateProperty() {
        return montantFormate;
    }

    public StringProperty libelleProperty() {
        return libelle;
    }

    public StringProperty typeMouvementProperty() {
        return typeMouvement;
    }

    public StringProperty idMissionProperty() {
        return idMission;
    }

    public StringProperty referenceMissionProperty() {
        return referenceMission;
    }

    public StringProperty idEntretienProperty() {
        return idEntretien;
    }

    public StringProperty referenceEntretienProperty() {
        return referenceEntretien;
    }

    public StringProperty categorieProperty() {
        return categorie;
    }

    public ObjectProperty<LocalDate> dateMouvementProperty() {
        return dateMouvement;
    }

    public ObjectProperty<BigDecimal> montantProperty() {
        return montant;
    }

    // Getters et setters classiques

    public String getIdMouvement() {
        return idMouvement.get();
    }

    public void setIdMouvement(String idMouvement) {
        this.idMouvement.set(idMouvement);
    }

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

    public String getDateMouvementFormatee() {
        return dateMouvementFormatee.get();
    }

    public LocalDate getDateMouvement() {
        return dateMouvement.get();
    }

    public void setDateMouvement(LocalDate date) {
        this.dateMouvement.set(date);
        this.dateMouvementFormatee.set(formatDate(date));
    }

    public String getMontantFormate() {
        return montantFormate.get();
    }

    public BigDecimal getMontant() {
        return montant.get();
    }

    public void setMontant(BigDecimal montant) {
        this.montant.set(montant);
        this.montantFormate.set(formatMontant(montant));
    }

    public String getLibelle() {
        return libelle.get();
    }

    public void setLibelle(String libelle) {
        this.libelle.set(libelle);
    }

    public String getTypeMouvement() {
        return typeMouvement.get();
    }

    public void setTypeMouvement(String typeMouvement) {
        this.typeMouvement.set(typeMouvement);
    }

    public String getIdMission() {
        return idMission.get();
    }

    public void setIdMission(String idMission) {
        this.idMission.set(idMission);
    }

    public String getReferenceMission() {
        return referenceMission.get();
    }

    public void setReferenceMission(String referenceMission) {
        this.referenceMission.set(referenceMission);
    }

    public String getIdEntretien() {
        return idEntretien.get();
    }

    public void setIdEntretien(String idEntretien) {
        this.idEntretien.set(idEntretien);
    }

    public String getReferenceEntretien() {
        return referenceEntretien.get();
    }

    public void setReferenceEntretien(String referenceEntretien) {
        this.referenceEntretien.set(referenceEntretien);
    }

    public String getCategorie() {
        return categorie.get();
    }

    public void setCategorie(String categorie) {
        this.categorie.set(categorie);
    }
}
