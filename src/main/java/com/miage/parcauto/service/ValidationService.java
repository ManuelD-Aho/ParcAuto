package main.java.com.miage.parcauto.service;

import main.java.com.miage.parcauto.dto.EntretienDTO;
import main.java.com.miage.parcauto.dto.MissionDTO;
import main.java.com.miage.parcauto.dto.VehiculeDTO;
import main.java.com.miage.parcauto.dto.DepenseDTO;
import main.java.com.miage.parcauto.util.Validator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service centralisé pour la validation des DTOs avant leur traitement par les
 * services métier.
 * Cette classe utilise le Validator existant et ajoute des validations
 * spécifiques aux entités.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class ValidationService {

    /**
     * Classe représentant le résultat de la validation.
     * Contient un booléen indiquant si la validation est réussie et une liste de
     * messages d'erreur.
     */
    public static class ValidationResult {
        private final boolean isValid;
        private final Map<String, List<String>> errors;

        /**
         * Constructeur pour un résultat de validation.
         * 
         * @param isValid Indique si la validation est réussie
         * @param errors  Map des erreurs par champ
         */
        public ValidationResult(boolean isValid, Map<String, List<String>> errors) {
            this.isValid = isValid;
            this.errors = errors != null ? errors : new HashMap<>();
        }

        /**
         * Indique si la validation est réussie.
         * 
         * @return true si valide, false sinon
         */
        public boolean isValid() {
            return isValid;
        }

        /**
         * Retourne les erreurs de validation.
         * 
         * @return Map des erreurs par champ
         */
        public Map<String, List<String>> getErrors() {
            return Collections.unmodifiableMap(errors);
        }

        /**
         * Retourne toutes les erreurs sous forme de liste unique.
         * 
         * @return Liste de tous les messages d'erreur
         */
        public List<String> getAllErrors() {
            List<String> allErrors = new ArrayList<>();
            errors.forEach((field, messages) -> {
                messages.forEach(message -> allErrors.add(field + ": " + message));
            });
            return allErrors;
        }
    }

    /**
     * Valide un objet quelconque.
     * Détermine le type d'objet et appelle la méthode de validation appropriée.
     * 
     * @param entity L'objet à valider
     * @return Le résultat de la validation
     */
    public ValidationResult validate(Object entity) {
        if (entity == null) {
            return new ValidationResult(false,
                    Collections.singletonMap("global", List.of("L'objet à valider est null")));
        }

        if (entity instanceof VehiculeDTO) {
            return validateVehicule((VehiculeDTO) entity);
        }
        if (entity instanceof EntretienDTO) {
            return validateEntretien((EntretienDTO) entity);
        }
        if (entity instanceof MissionDTO) {
            return validateMission((MissionDTO) entity);
        }
        if (entity instanceof DepenseDTO) {
            return validateDepense((DepenseDTO) entity);
        }

        return new ValidationResult(false,
                Collections.singletonMap("global", List.of("Type d'objet non supporté pour la validation")));
    }

    /**
     * Valide un DTO de véhicule.
     * 
     * @param vehicule Le véhicule à valider
     * @return Le résultat de la validation
     */
    public ValidationResult validateVehicule(VehiculeDTO vehicule) {
        Map<String, List<String>> errors = new HashMap<>();

        if (vehicule == null) {
            return new ValidationResult(false, Collections.singletonMap("vehicule", List.of("Le véhicule est null")));
        }

        // Validation des champs obligatoires
        if (vehicule.getMarque() == null || vehicule.getMarque().trim().isEmpty()) {
            addError(errors, "marque", "La marque est obligatoire");
        }

        if (vehicule.getModele() == null || vehicule.getModele().trim().isEmpty()) {
            addError(errors, "modele", "Le modèle est obligatoire");
        }

        if (vehicule.getNumeroChassi() == null || vehicule.getNumeroChassi().trim().isEmpty()) {
            addError(errors, "numeroChassi", "Le numéro de chassis est obligatoire");
        }

        // Immatriculation
        if (vehicule.getImmatriculation() == null || vehicule.getImmatriculation().trim().isEmpty()) {
            addError(errors, "immatriculation", "L'immatriculation est obligatoire");
        } else if (!Validator.isValidImmatriculation(vehicule.getImmatriculation())) {
            addError(errors, "immatriculation", "Format d'immatriculation invalide (format attendu: AA-123-AA)");
        }

        // Nombre de places
        if (vehicule.getNbPlaces() == null || vehicule.getNbPlaces() <= 0) {
            addError(errors, "nbPlaces", "Le nombre de places doit être supérieur à 0");
        }

        // Kilométrage
        if (vehicule.getKilometrage() == null || vehicule.getKilometrage() < 0) {
            addError(errors, "kilometrage", "Le kilométrage ne peut pas être négatif");
        }

        // Prix achat
        if (vehicule.getPrixAchat() == null || vehicule.getPrixAchat().doubleValue() <= 0) {
            addError(errors, "prixAchat", "Le prix d'achat doit être supérieur à 0");
        }

        // Validation des dates
        if (vehicule.getDateAcquisition() == null) {
            addError(errors, "dateAcquisition", "La date d'acquisition est obligatoire");
        }

        if (vehicule.getDateAcquisition() != null && vehicule.getDateAmmortissement() != null
                && vehicule.getDateAcquisition().isAfter(vehicule.getDateAmmortissement())) {
            addError(errors, "dateAmmortissement",
                    "La date d'amortissement doit être postérieure à la date d'acquisition");
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

    /**
     * Valide un DTO d'entretien.
     * 
     * @param entretien L'entretien à valider
     * @return Le résultat de la validation
     */
    public ValidationResult validateEntretien(EntretienDTO entretien) {
        Map<String, List<String>> errors = new HashMap<>();

        if (entretien == null) {
            return new ValidationResult(false, Collections.singletonMap("entretien", List.of("L'entretien est null")));
        }

        // Validation des champs obligatoires
        if (entretien.getIdVehicule() == null) {
            addError(errors, "idVehicule", "Le véhicule est obligatoire");
        }

        if (entretien.getType() == null) {
            addError(errors, "type", "Le type d'entretien est obligatoire");
        }

        if (entretien.getDescription() == null || entretien.getDescription().trim().isEmpty()) {
            addError(errors, "description", "La description est obligatoire");
        }

        // Validation des dates
        LocalDateTime now = LocalDateTime.now();

        if (entretien.getDateDebut() == null) {
            addError(errors, "dateDebut", "La date de début est obligatoire");
        }

        if (entretien.getDateFin() != null && entretien.getDateDebut() != null
                && entretien.getDateFin().isBefore(entretien.getDateDebut())) {
            addError(errors, "dateFin", "La date de fin doit être postérieure à la date de début");
        }

        // Coût
        if (entretien.getCout() != null && entretien.getCout().doubleValue() < 0) {
            addError(errors, "cout", "Le coût ne peut pas être négatif");
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

    /**
     * Valide un DTO de mission.
     * 
     * @param mission La mission à valider
     * @return Le résultat de la validation
     */
    public ValidationResult validateMission(MissionDTO mission) {
        Map<String, List<String>> errors = new HashMap<>();

        if (mission == null) {
            return new ValidationResult(false, Collections.singletonMap("mission", List.of("La mission est null")));
        }

        // Validation des champs obligatoires
        if (mission.getIdVehicule() == null) {
            addError(errors, "idVehicule", "Le véhicule est obligatoire");
        }

        if (mission.getObjectif() == null || mission.getObjectif().trim().isEmpty()) {
            addError(errors, "objectif", "L'objectif est obligatoire");
        }

        if (mission.getDestination() == null || mission.getDestination().trim().isEmpty()) {
            addError(errors, "destination", "La destination est obligatoire");
        }

        // Validation des dates
        if (mission.getDateDepart() == null) {
            addError(errors, "dateDepart", "La date de départ est obligatoire");
        }

        if (mission.getDateRetourPrevue() == null) {
            addError(errors, "dateRetourPrevue", "La date de retour prévue est obligatoire");
        }

        if (mission.getDateDepart() != null && mission.getDateRetourPrevue() != null
                && mission.getDateRetourPrevue().isBefore(mission.getDateDepart())) {
            addError(errors, "dateRetourPrevue", "La date de retour prévue doit être postérieure à la date de départ");
        }

        if (mission.getDateRetourEffective() != null && mission.getDateDepart() != null
                && mission.getDateRetourEffective().isBefore(mission.getDateDepart())) {
            addError(errors, "dateRetourEffective",
                    "La date de retour effective doit être postérieure à la date de départ");
        }

        // Kilométrage
        if (mission.getKilometrageDep() != null && mission.getKilometrageDep() < 0) {
            addError(errors, "kilometrageDep", "Le kilométrage de départ ne peut pas être négatif");
        }

        if (mission.getKilometrageRet() != null && mission.getKilometrageDep() != null
                && mission.getKilometrageRet() < mission.getKilometrageDep()) {
            addError(errors, "kilometrageRet", "Le kilométrage de retour doit être supérieur au kilométrage de départ");
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

    /**
     * Valide un DTO de dépense.
     * 
     * @param depense La dépense à valider
     * @return Le résultat de la validation
     */
    public ValidationResult validateDepense(DepenseDTO depense) {
        Map<String, List<String>> errors = new HashMap<>();

        if (depense == null) {
            return new ValidationResult(false, Collections.singletonMap("depense", List.of("La dépense est null")));
        }

        // Validation des champs obligatoires
        if (depense.getDescription() == null || depense.getDescription().trim().isEmpty()) {
            addError(errors, "description", "La description est obligatoire");
        }

        if (depense.getCategorie() == null) {
            addError(errors, "categorie", "La catégorie est obligatoire");
        }

        if (depense.getDate() == null) {
            addError(errors, "date", "La date est obligatoire");
        }

        // Montant
        if (depense.getMontant() == null || depense.getMontant().doubleValue() <= 0) {
            addError(errors, "montant", "Le montant doit être supérieur à 0");
        }

        return new ValidationResult(errors.isEmpty(), errors);
    }

    /**
     * Ajoute une erreur à la map des erreurs.
     * 
     * @param errors  Map des erreurs
     * @param field   Champ en erreur
     * @param message Message d'erreur
     */
    private void addError(Map<String, List<String>> errors, String field, String message) {
        errors.computeIfAbsent(field, k -> new ArrayList<>()).add(message);
    }
}
