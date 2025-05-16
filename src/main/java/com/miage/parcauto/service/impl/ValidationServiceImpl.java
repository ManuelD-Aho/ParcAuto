package main.java.com.miage.parcauto.service.impl;

import main.java.com.miage.parcauto.dto.*;
import main.java.com.miage.parcauto.exception.ValidationException;
import main.java.com.miage.parcauto.service.ValidationService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern; // Pour la validation d'email par exemple

/**
 * Implémentation du service de validation.
 */
public class ValidationServiceImpl implements ValidationService {

    // Regex simple pour email, à améliorer pour la production
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final int MOT_DE_PASSE_MIN_LENGTH = 8; // Exemple de règle

    private void checkErrors(List<String> errors, String entityName) throws ValidationException {
        if (!errors.isEmpty()) {
            throw new ValidationException("Erreurs de validation pour " + entityName + ": " + String.join(", ", errors));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateVehicule(VehiculeDTO vehiculeDTO) throws ValidationException {
        List<String> errors = new ArrayList<>();
        if (vehiculeDTO == null) {
            errors.add("L'objet VehiculeDTO ne peut pas être nul.");
            checkErrors(errors, "Vehicule");
            return;
        }
        if (vehiculeDTO.getImmatriculation() == null || vehiculeDTO.getImmatriculation().trim().isEmpty()) {
            errors.add("L'immatriculation est requise.");
        } else if (vehiculeDTO.getImmatriculation().length() > 20) {
            errors.add("L'immatriculation ne doit pas dépasser 20 caractères.");
        }
        if (vehiculeDTO.getNumeroChassis() == null || vehiculeDTO.getNumeroChassis().trim().isEmpty()) {
            errors.add("Le numéro de châssis est requis.");
        } else if (vehiculeDTO.getNumeroChassis().length() > 20) {
            errors.add("Le numéro de châssis ne doit pas dépasser 20 caractères.");
        }
        if (vehiculeDTO.getIdEtatVoiture() == null) {
            errors.add("L'ID de l'état du véhicule est requis.");
        }
        if (vehiculeDTO.getEnergie() == null) {
            errors.add("Le type d'énergie est requis.");
        }
        if (vehiculeDTO.getKmActuels() != null && vehiculeDTO.getKmActuels() < 0) {
            errors.add("Le kilométrage actuel ne peut pas être négatif.");
        }
        if (vehiculeDTO.getNbPlaces() != null && vehiculeDTO.getNbPlaces() <= 0) {
            errors.add("Le nombre de places doit être positif.");
        }
        if (vehiculeDTO.getPrixVehicule() != null && vehiculeDTO.getPrixVehicule().compareTo(BigDecimal.ZERO) < 0) {
            errors.add("Le prix du véhicule ne peut pas être négatif.");
        }
        checkErrors(errors, "Vehicule");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateEntretien(EntretienDTO entretienDTO) throws ValidationException {
        List<String> errors = new ArrayList<>();
        if (entretienDTO == null) {
            errors.add("L'objet EntretienDTO ne peut pas être nul.");
            checkErrors(errors, "Entretien");
            return;
        }
        if (entretienDTO.getIdVehicule() == null) {
            errors.add("L'ID du véhicule est requis.");
        }
        if (entretienDTO.getDateEntree() == null) {
            errors.add("La date d'entrée est requise.");
        }
        if (entretienDTO.getMotif() == null || entretienDTO.getMotif().trim().isEmpty()) {
            errors.add("Le motif de l'entretien est requis.");
        }
        if (entretienDTO.getDateSortie() != null && entretienDTO.getDateEntree().isAfter(entretienDTO.getDateSortie())) {
            errors.add("La date de sortie ne peut pas être antérieure à la date d'entrée.");
        }
        if (entretienDTO.getCoutEstime() != null && entretienDTO.getCoutEstime().compareTo(BigDecimal.ZERO) < 0) {
            errors.add("Le coût estimé ne peut pas être négatif.");
        }
        if (entretienDTO.getCoutReel() != null && entretienDTO.getCoutReel().compareTo(BigDecimal.ZERO) < 0) {
            errors.add("Le coût réel ne peut pas être négatif.");
        }
        checkErrors(errors, "Entretien");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateMission(MissionDTO missionDTO) throws ValidationException {
        List<String> errors = new ArrayList<>();
        if (missionDTO == null) {
            errors.add("L'objet MissionDTO ne peut pas être nul.");
            checkErrors(errors, "Mission");
            return;
        }
        if (missionDTO.getIdVehicule() == null) {
            errors.add("L'ID du véhicule est requis.");
        }
        if (missionDTO.getDateDebut() == null) {
            errors.add("La date de début de mission est requise.");
        }
        if (missionDTO.getDateFinPrevue() != null && missionDTO.getDateDebut().isAfter(missionDTO.getDateFinPrevue())) {
            errors.add("La date de fin prévue ne peut pas être antérieure à la date de début.");
        }
        if (missionDTO.getDateFinEffective() != null && missionDTO.getDateDebut().isAfter(missionDTO.getDateFinEffective())) {
            errors.add("La date de fin effective ne peut pas être antérieure à la date de début.");
        }
        if (missionDTO.getKmPrevu() != null && missionDTO.getKmPrevu() < 0) {
            errors.add("Le kilométrage prévu ne peut pas être négatif.");
        }
        if (missionDTO.getKmReel() != null && missionDTO.getKmReel() < 0) {
            errors.add("Le kilométrage réel ne peut pas être négatif.");
        }
        checkErrors(errors, "Mission");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateDepenseMission(DepenseMissionDTO depenseDTO) throws ValidationException {
        List<String> errors = new ArrayList<>();
        if (depenseDTO == null) {
            errors.add("L'objet DepenseMissionDTO ne peut pas être nul.");
            checkErrors(errors, "Dépense de Mission");
            return;
        }
        if (depenseDTO.getIdMission() == null) {
            errors.add("L'ID de la mission est requis.");
        }
        if (depenseDTO.getNature() == null || depenseDTO.getNature().trim().isEmpty()) {
            errors.add("La nature de la dépense est requise.");
        }
        if (depenseDTO.getMontant() == null || depenseDTO.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Le montant de la dépense doit être positif.");
        }
        if (depenseDTO.getDateDepense() == null) {
            errors.add("La date de la dépense est requise.");
        }
        checkErrors(errors, "Dépense de Mission");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateUtilisateur(UtilisateurDTO utilisateurDTO, boolean isCreation) throws ValidationException {
        List<String> errors = new ArrayList<>();
        if (utilisateurDTO == null) {
            errors.add("L'objet UtilisateurDTO ne peut pas être nul.");
            checkErrors(errors, "Utilisateur");
            return;
        }
        if (utilisateurDTO.getLogin() == null || utilisateurDTO.getLogin().trim().isEmpty()) {
            errors.add("Le login est requis.");
        } else if (utilisateurDTO.getLogin().length() < 3 || utilisateurDTO.getLogin().length() > 50) {
            errors.add("Le login doit contenir entre 3 et 50 caractères.");
        }

        if (isCreation) {
            if (utilisateurDTO.getMotDePasse() == null || utilisateurDTO.getMotDePasse().isEmpty()) {
                errors.add("Le mot de passe est requis pour la création.");
            } else if (utilisateurDTO.getMotDePasse().length() < MOT_DE_PASSE_MIN_LENGTH) {
                errors.add("Le mot de passe doit contenir au moins " + MOT_DE_PASSE_MIN_LENGTH + " caractères.");
            }
            if (utilisateurDTO.getRole() == null || utilisateurDTO.getRole().trim().isEmpty()) {
                errors.add("Le rôle est requis pour la création.");
            }
        } else { // Mise à jour
            if (utilisateurDTO.getMotDePasse() != null && !utilisateurDTO.getMotDePasse().isEmpty() && utilisateurDTO.getMotDePasse().length() < MOT_DE_PASSE_MIN_LENGTH) {
                errors.add("Si fourni, le mot de passe doit contenir au moins " + MOT_DE_PASSE_MIN_LENGTH + " caractères.");
            }
        }
        checkErrors(errors, "Utilisateur");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validatePersonnel(PersonnelDTO personnelDTO) throws ValidationException {
        List<String> errors = new ArrayList<>();
        if (personnelDTO == null) {
            errors.add("L'objet PersonnelDTO ne peut pas être nul.");
            checkErrors(errors, "Personnel");
            return;
        }
        if (personnelDTO.getNom() == null || personnelDTO.getNom().trim().isEmpty()) {
            errors.add("Le nom du personnel est requis.");
        }
        if (personnelDTO.getPrenom() == null || personnelDTO.getPrenom().trim().isEmpty()) {
            errors.add("Le prénom du personnel est requis.");
        }
        if (personnelDTO.getMatricule() == null || personnelDTO.getMatricule().trim().isEmpty()) {
            errors.add("Le matricule est requis.");
        }
        if (personnelDTO.getEmail() == null || !EMAIL_PATTERN.matcher(personnelDTO.getEmail()).matches()) {
            errors.add("L'email est requis et doit être valide.");
        }
        if (personnelDTO.getIdService() == null) {
            errors.add("L'ID du service est requis.");
        }
        if (personnelDTO.getIdFonction() == null) {
            errors.add("L'ID de la fonction est requis.");
        }
        checkErrors(errors, "Personnel");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateSocietaireCompte(SocietaireCompteDTO compteDTO) throws ValidationException {
        List<String> errors = new ArrayList<>();
        if (compteDTO == null) {
            errors.add("L'objet SocietaireCompteDTO ne peut pas être nul.");
            checkErrors(errors, "Compte Sociétaire");
            return;
        }
        if (compteDTO.getNom() == null || compteDTO.getNom().trim().isEmpty()) {
            errors.add("Le nom du sociétaire est requis.");
        }
        if (compteDTO.getNumero() == null || compteDTO.getNumero().trim().isEmpty()) {
            errors.add("Le numéro de compte est requis.");
        }
        if (compteDTO.getSolde() == null) {
            errors.add("Le solde ne peut être nul (peut être zéro).");
        }
        if (compteDTO.getEmail() != null && !compteDTO.getEmail().trim().isEmpty() && !EMAIL_PATTERN.matcher(compteDTO.getEmail()).matches()) {
            errors.add("Si fourni, l'email doit être valide.");
        }
        checkErrors(errors, "Compte Sociétaire");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateMouvement(MouvementDTO mouvementDTO) throws ValidationException {
        List<String> errors = new ArrayList<>();
        if (mouvementDTO == null) {
            errors.add("L'objet MouvementDTO ne peut pas être nul.");
            checkErrors(errors, "Mouvement");
            return;
        }
        if (mouvementDTO.getIdSocietaireCompte() == null) {
            errors.add("L'ID du compte sociétaire est requis.");
        }
        if (mouvementDTO.getTypeMouvement() == null || mouvementDTO.getTypeMouvement().trim().isEmpty()) {
            errors.add("Le type de mouvement est requis.");
        }
        if (mouvementDTO.getMontant() == null || mouvementDTO.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Le montant du mouvement doit être positif.");
        }
        if (mouvementDTO.getDateMouvement() == null) {
            errors.add("La date du mouvement est requise.");
        }
        checkErrors(errors, "Mouvement");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateDocumentSocietaire(DocumentSocietaireDTO documentDTO) throws ValidationException {
        List<String> errors = new ArrayList<>();
        if (documentDTO == null) {
            errors.add("L'objet DocumentSocietaireDTO ne peut pas être nul.");
            checkErrors(errors, "Document Sociétaire");
            return;
        }
        if (documentDTO.getIdSocietaire() == null) {
            errors.add("L'ID du sociétaire est requis.");
        }
        if (documentDTO.getTypeDocument() == null || documentDTO.getTypeDocument().trim().isEmpty()) {
            errors.add("Le type de document est requis.");
        }
        if (documentDTO.getCheminFichier() == null || documentDTO.getCheminFichier().trim().isEmpty()) {
            errors.add("Le chemin du fichier est requis.");
        }
        checkErrors(errors, "Document Sociétaire");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateAssurance(AssuranceDTO assuranceDTO) throws ValidationException {
        List<String> errors = new ArrayList<>();
        if (assuranceDTO == null) {
            errors.add("L'objet AssuranceDTO ne peut pas être nul.");
            checkErrors(errors, "Assurance");
            return;
        }
        if (assuranceDTO.getAgence() == null || assuranceDTO.getAgence().trim().isEmpty()) {
            errors.add("L'agence d'assurance est requise.");
        }
        if (assuranceDTO.getDateDebut() == null) {
            errors.add("La date de début d'assurance est requise.");
        }
        if (assuranceDTO.getDateFin() == null) {
            errors.add("La date de fin d'assurance est requise.");
        }
        if (assuranceDTO.getDateDebut() != null && assuranceDTO.getDateFin() != null && assuranceDTO.getDateDebut().isAfter(assuranceDTO.getDateFin())) {
            errors.add("La date de début ne peut pas être postérieure à la date de fin.");
        }
        if (assuranceDTO.getCout() != null && assuranceDTO.getCout().compareTo(BigDecimal.ZERO) < 0) {
            errors.add("Le coût de l'assurance ne peut pas être négatif.");
        }
        checkErrors(errors, "Assurance");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateAffectation(AffectationDTO affectationDTO) throws ValidationException {
        List<String> errors = new ArrayList<>();
        if (affectationDTO == null) {
            errors.add("L'objet AffectationDTO ne peut pas être nul.");
            checkErrors(errors, "Affectation");
            return;
        }
        if (affectationDTO.getIdVehicule() == null) {
            errors.add("L'ID du véhicule est requis.");
        }
        if (affectationDTO.getTypeAffectation() == null || affectationDTO.getTypeAffectation().trim().isEmpty()) {
            errors.add("Le type d'affectation est requis.");
        }
        if (affectationDTO.getDateDebut() == null) {
            errors.add("La date de début d'affectation est requise.");
        }
        if (affectationDTO.getDateFin() != null && affectationDTO.getDateDebut().isAfter(affectationDTO.getDateFin())) {
            errors.add("La date de fin ne peut pas être antérieure à la date de début.");
        }
        if (affectationDTO.getIdPersonnel() == null && affectationDTO.getIdSocietaire() == null) {
            errors.add("L'affectation doit concerner un personnel ou un sociétaire.");
        }
        if (affectationDTO.getIdPersonnel() != null && affectationDTO.getIdSocietaire() != null) {
            errors.add("L'affectation ne peut pas concerner à la fois un personnel et un sociétaire.");
        }
        checkErrors(errors, "Affectation");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateNotification(NotificationDTO notificationDTO) throws ValidationException {
        List<String> errors = new ArrayList<>();
        if (notificationDTO == null) {
            errors.add("L'objet NotificationDTO ne peut pas être nul.");
            checkErrors(errors, "Notification");
            return;
        }
        if (notificationDTO.getIdUtilisateur() == null) {
            errors.add("L'ID de l'utilisateur destinataire est requis.");
        }
        if (notificationDTO.getTitre() == null || notificationDTO.getTitre().trim().isEmpty()) {
            errors.add("Le titre de la notification est requis.");
        }
        if (notificationDTO.getMessage() == null || notificationDTO.getMessage().trim().isEmpty()) {
            errors.add("Le message de la notification est requis.");
        }
        checkErrors(errors, "Notification");
    }
}