package main.java.com.miage.parcauto.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.com.miage.parcauto.dto.UtilisateurDTO;
import main.java.com.miage.parcauto.exception.AuthenticationException;

/**
 * Gestionnaire de sécurité pour l'application ParcAuto.
 * S'occupe de l'authentification, du hachage des mots de passe et de la gestion
 * des permissions.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class SecurityManager {

    private static final Logger LOGGER = Logger.getLogger(SecurityManager.class.getName());

    /**
     * Algorithme de hachage utilisé.
     */
    private static final String HASH_ALGORITHM = "SHA-256";

    /**
     * Nombre d'itérations pour le hachage.
     */
    private static final int HASH_ITERATIONS = 10000;

    /**
     * Longueur du sel en octets.
     */
    private static final int SALT_LENGTH = 16;

    /**
     * Générateur de nombres aléatoires sécurisé.
     */
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Constructeur privé pour empêcher l'instanciation de cette classe utilitaire.
     */
    private SecurityManager() {
        throw new IllegalStateException("Classe utilitaire non instanciable");
    }

    /**
     * Génère un sel aléatoire pour le hachage des mots de passe.
     * 
     * @return Sel aléatoire encodé en Base64
     */
    public static String generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hache un mot de passe avec un sel donné.
     * 
     * @param password Mot de passe en clair
     * @param salt     Sel pour le hachage
     * @return Mot de passe haché ou vide en cas d'erreur
     */
    public static Optional<String> hashPassword(String password, String salt) {
        try {
            // Décodage du sel
            byte[] saltBytes = Base64.getDecoder().decode(salt);

            // Création de l'objet MessageDigest
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);

            // Application du sel
            md.update(saltBytes);

            // Conversion du mot de passe en bytes
            byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);

            // Hachage itératif
            byte[] hashedPassword = passwordBytes;
            for (int i = 0; i < HASH_ITERATIONS; i++) {
                md.reset();
                md.update(saltBytes);
                md.update(hashedPassword);
                hashedPassword = md.digest();
            }

            // Encodage en Base64
            return Optional.of(Base64.getEncoder().encodeToString(hashedPassword));

        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du hachage du mot de passe", e);
            return Optional.empty();
        }
    }

    /**
     * Vérifie si un mot de passe correspond à un hash stocké.
     * 
     * @param password   Mot de passe en clair à vérifier
     * @param salt       Sel utilisé pour le hachage
     * @param storedHash Hash stocké à comparer
     * @return true si le mot de passe correspond, false sinon
     */
    public static boolean verifyPassword(String password, String salt, String storedHash) {
        Optional<String> computedHash = hashPassword(password, salt);

        if (computedHash.isEmpty()) {
            return false;
        }

        return MessageDigest.isEqual(
                computedHash.get().getBytes(StandardCharsets.UTF_8),
                storedHash.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Génère un token de session sécurisé.
     * 
     * @param utilisateur Utilisateur pour lequel générer le token
     * @return Token de session
     */
    public static String generateSecureToken(UtilisateurDTO utilisateur) {
        if (utilisateur == null) {
            throw new AuthenticationException("Impossible de générer un token: utilisateur est null");
        }

        StringBuilder tokenBuilder = new StringBuilder();

        // Ajout d'un identifiant unique (UUID)
        tokenBuilder.append(UUID.randomUUID().toString());

        // Ajout d'un timestamp
        tokenBuilder.append("_").append(System.currentTimeMillis());

        // Ajout de l'identifiant utilisateur
        tokenBuilder.append("_").append(utilisateur.getIdUtilisateur());

        // Hachage du token pour le sécuriser davantage
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(tokenBuilder.toString().getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);

        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la génération du token", e);
            throw new AuthenticationException("Erreur lors de la génération du token", e);
        }
    }

    /**
     * Vérifie si l'utilisateur actuel possède une permission donnée.
     * Délègue au SessionManager.
     * 
     * @param permission Permission à vérifier
     * @return true si l'utilisateur a la permission, false sinon
     */
    public static boolean hasPermission(Permission permission) {
        return SessionManager.getInstance().hasPermission(permission);
    }

    /**
     * Vérifie si l'utilisateur actuel possède au moins une des permissions
     * spécifiées.
     * Délègue au SessionManager.
     * 
     * @param permissions Permissions à vérifier
     * @return true si l'utilisateur a au moins une des permissions, false sinon
     */
    public static boolean hasAnyPermission(Set<Permission> permissions) {
        return SessionManager.getInstance().hasAnyPermission(permissions);
    }

    /**
     * Vérifie si l'utilisateur actuel possède toutes les permissions spécifiées.
     * Délègue au SessionManager.
     * 
     * @param permissions Permissions à vérifier
     * @return true si l'utilisateur a toutes les permissions, false sinon
     */
    public static boolean hasAllPermissions(Set<Permission> permissions) {
        return SessionManager.getInstance().hasAllPermissions(permissions);
    }

    /**
     * Vérifie si la session utilisateur est active.
     * Délègue au SessionManager.
     * 
     * @return true si une session est active, false sinon
     */
    public static boolean isAuthenticated() {
        return SessionManager.getInstance().isSessionActive();
    }

    /**
     * Rafraîchit la session de l'utilisateur actuel.
     * Délègue au SessionManager.
     */
    public static void refreshSession() {
        SessionManager.getInstance().refreshSession();
    }
}
