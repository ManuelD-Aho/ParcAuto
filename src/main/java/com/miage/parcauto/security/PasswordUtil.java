package main.java.com.miage.parcauto.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public final class PasswordUtil {

    private static final String HASHING_ALGORITHM = "SHA-256";
    private static final int SALT_LENGTH_BYTES = 16;

    private PasswordUtil() {
        throw new UnsupportedOperationException("Cette classe utilitaire ne doit pas être instanciée.");
    }

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH_BYTES];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hashPassword(String password, String salt) throws ParcAutoSecurityException {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe ne peut être nul ou vide.");
        }
        if (salt == null || salt.isEmpty()) {
            throw new IllegalArgumentException("Le sel ne peut être nul ou vide.");
        }

        try {
            String saltedPassword = salt + password;
            MessageDigest digest = MessageDigest.getInstance(HASHING_ALGORITHM);
            byte[] hashedBytes = digest.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new ParcAutoSecurityException("Algorithme de hachage non disponible: " + HASHING_ALGORITHM, e);
        }
    }

    public static boolean verifyPassword(String providedPassword, String storedSalt, String storedHashedPassword) throws ParcAutoSecurityException {
        if (providedPassword == null || providedPassword.isEmpty() ||
                storedSalt == null || storedSalt.isEmpty() ||
                storedHashedPassword == null || storedHashedPassword.isEmpty()) {
            return false;
        }

        String newHashedPassword = hashPassword(providedPassword, storedSalt);
        return newHashedPassword.equals(storedHashedPassword);
    }

    public static class ParcAutoSecurityException extends Exception {
        public ParcAutoSecurityException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}