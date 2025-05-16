package main.java.com.miage.parcauto.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe utilitaire pour la validation des données saisies par l'utilisateur.
 * Fournit des méthodes statiques pour valider différents types de données.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class Validator {

    private static final Logger LOGGER = Logger.getLogger(Validator.class.getName());

    /**
     * Pattern pour la validation d'une adresse email.
     * Format attendu: nom@domaine.extension
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    /**
     * Pattern pour la validation d'un numéro de téléphone français.
     * Format attendu: +33XXXXXXXXX ou 0XXXXXXXXX
     */
    private static final Pattern TELEPHONE_PATTERN = Pattern.compile("^(\\+33|0)[1-9](\\d{2}){4}$");

    /**
     * Pattern pour la validation d'un numéro d'immatriculation français.
     * Format attendu: AA-123-AA (nouveau format)
     */
    private static final Pattern IMMATRICULATION_PATTERN = Pattern.compile("^[A-Za-z]{2}-[0-9]{3}-[A-Za-z]{2}$");

    /**
     * Pattern pour la validation d'un code postal français.
     * Format attendu: 5 chiffres
     */
    private static final Pattern CODE_POSTAL_PATTERN = Pattern.compile("^[0-9]{5}$");

    /**
     * Pattern pour la validation d'un numéro de permis de conduire français.
     * Format attendu: 12 chiffres
     */
    private static final Pattern PERMIS_PATTERN = Pattern.compile("^[0-9]{12}$");

    /**
     * Pattern pour la validation d'un mot de passe fort.
     * Au moins 8 caractères, une majuscule, une minuscule, un chiffre.
     */
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$");

    /**
     * Constructeur privé pour empêcher l'instanciation de cette classe utilitaire.
     */
    private Validator() {
        throw new IllegalStateException("Classe utilitaire non instanciable");
    }

    /**
     * Valide une adresse email.
     *
     * @param email L'adresse email à valider
     * @return true si l'email est valide, false sinon
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    /**
     * Valide un numéro de téléphone.
     *
     * @param telephone Le numéro de téléphone à valider
     * @return true si le numéro de téléphone est valide, false sinon
     */
    public static boolean isValidTelephone(String telephone) {
        if (telephone == null) {
            return false;
        }
        Matcher matcher = TELEPHONE_PATTERN.matcher(telephone);
        return matcher.matches();
    }

    /**
     * Valide un numéro d'immatriculation.
     *
     * @param immatriculation Le numéro d'immatriculation à valider
     * @return true si l'immatriculation est valide, false sinon
     */
    public static boolean isValidImmatriculation(String immatriculation) {
        if (immatriculation == null) {
            return false;
        }
        Matcher matcher = IMMATRICULATION_PATTERN.matcher(immatriculation);
        return matcher.matches();
    }

    /**
     * Valide un code postal.
     *
     * @param codePostal Le code postal à valider
     * @return true si le code postal est valide, false sinon
     */
    public static boolean isValidCodePostal(String codePostal) {
        if (codePostal == null) {
            return false;
        }
        Matcher matcher = CODE_POSTAL_PATTERN.matcher(codePostal);
        return matcher.matches();
    }

    /**
     * Valide un numéro de permis de conduire.
     *
     * @param permis Le numéro de permis à valider
     * @return true si le numéro de permis est valide, false sinon
     */
    public static boolean isValidPermis(String permis) {
        if (permis == null) {
            return false;
        }
        Matcher matcher = PERMIS_PATTERN.matcher(permis);
        return matcher.matches();
    }

    /**
     * Valide un mot de passe.
     *
     * @param password Le mot de passe à valider
     * @return true si le mot de passe est valide, false sinon
     */
    public static boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        Matcher matcher = PASSWORD_PATTERN.matcher(password);
        return matcher.matches();
    }

    /**
     * Valide un kilométrage.
     *
     * @param kilometrage Le kilométrage à valider
     * @return true si le kilométrage est positif, false sinon
     */
    public static boolean isValidKilometrage(int kilometrage) {
        return kilometrage >= 0;
    }

    /**
     * Valide une date au format français (JJ/MM/AAAA).
     *
     * @param dateStr La date sous forme de chaîne
     * @return true si la date est valide et au format attendu, false sinon
     */
    public static boolean isValidDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return false;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(dateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Valide qu'une date est antérieure à la date actuelle.
     *
     * @param date La date à valider
     * @return true si la date est dans le passé, false sinon
     */
    public static boolean isDateInPast(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isBefore(LocalDate.now());
    }

    /**
     * Valide qu'une date est postérieure à la date actuelle.
     *
     * @param date La date à valider
     * @return true si la date est dans le futur, false sinon
     */
    public static boolean isDateInFuture(LocalDate date) {
        if (date == null) {
            return false;
        }
        return date.isAfter(LocalDate.now());
    }

    /**
     * Valide un montant financier.
     *
     * @param montant Le montant à valider
     * @return true si le montant est positif, false sinon
     */
    public static boolean isValidMontant(double montant) {
        return montant >= 0;
    }

    /**
     * Valide que la chaîne n'est pas vide ou null.
     *
     * @param str La chaîne à valider
     * @return true si la chaîne n'est pas vide ou null, false sinon
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
}