package main.java.com.miage.parcauto.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe d'accès aux données pour les utilisateurs.
 * Gère l'authentification, la création et la gestion des utilisateurs.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class UtilisateurDao {

    private static final Logger LOGGER = Logger.getLogger(UtilisateurDao.class.getName());

    // Instance de DbUtil pour la gestion des connexions
    private final DbUtil dbUtil;

    /**
     * Énumération des rôles utilisateur
     */
    public enum Role {
        U1("Responsable Logistique"),
        U2("Agent Logistique"),
        U3("Sociétaire"),
        U4("Administrateur Système");

        private final String libelle;

        Role(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }
    }

    /**
     * Classe interne représentant un utilisateur du système.
     */
    public static class Utilisateur {
        private Integer id;
        private String login;
        private String hash; // Mot de passe haché
        private Role role;
        private Integer idPersonnel;
        private String mfaSecret;

        // Constructeur par défaut
        public Utilisateur() {
        }

        // Constructeur pour la création d'un utilisateur
        public Utilisateur(String login, Role role, Integer idPersonnel) {
            this.login = login;
            this.role = role;
            this.idPersonnel = idPersonnel;
        }

        // Constructeur complet
        public Utilisateur(Integer id, String login, String hash, Role role,
                Integer idPersonnel, String mfaSecret) {
            this.id = id;
            this.login = login;
            this.hash = hash;
            this.role = role;
            this.idPersonnel = idPersonnel;
            this.mfaSecret = mfaSecret;
        }

        // Getters et setters

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        /**
         * Alias pour getId() afin d'assurer la compatibilité avec la convention
         * de nommage utilisée dans d'autres classes.
         * 
         * @return L'identifiant unique de l'utilisateur
         */
        public Integer getIdUtilisateur() {
            return id;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public Role getRole() {
            return role;
        }

        public void setRole(Role role) {
            this.role = role;
        }

        public Integer getIdPersonnel() {
            return idPersonnel;
        }

        public void setIdPersonnel(Integer idPersonnel) {
            this.idPersonnel = idPersonnel;
        }

        public String getMfaSecret() {
            return mfaSecret;
        }

        public void setMfaSecret(String mfaSecret) {
            this.mfaSecret = mfaSecret;
        }

        public boolean hasMfaEnabled() {
            return mfaSecret != null && !mfaSecret.trim().isEmpty();
        }
    }

    /**
     * Constructeur par défaut. Initialise l'instance de DbUtil.
     */
    public UtilisateurDao() {
        this.dbUtil = DbUtil.getInstance();
    }

    /**
     * Authentifie un utilisateur avec son login et mot de passe.
     *
     * @param login    Login de l'utilisateur
     * @param password Mot de passe en clair
     * @return Optional contenant l'utilisateur si l'authentification réussit, vide
     *         sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Optional<Utilisateur> authentifier(String login, String password) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            // Requête pour récupérer l'utilisateur par son login
            String sql = "SELECT id, login, hash, role, id_personnel, mfa_secret FROM UTILISATEUR WHERE login = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, login);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                // Utilisateur trouvé, vérifier le mot de passe
                String storedHash = rs.getString("hash");

                if (verifyPassword(password, storedHash)) {
                    // Authentification réussie, créer l'objet Utilisateur
                    Utilisateur utilisateur = new Utilisateur(
                            rs.getInt("id"),
                            rs.getString("login"),
                            storedHash,
                            Role.valueOf(rs.getString("role")),
                            rs.getObject("id_personnel") != null ? rs.getInt("id_personnel") : null,
                            rs.getString("mfa_secret"));

                    return Optional.of(utilisateur);
                }
            }

            // Authentification échouée
            return Optional.empty();

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'authentification de l'utilisateur", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Authentifie un utilisateur avec un code MFA en plus du login et mot de passe.
     *
     * @param login    Login de l'utilisateur
     * @param password Mot de passe en clair
     * @param mfaCode  Code MFA fourni par l'utilisateur
     * @return Optional contenant l'utilisateur si l'authentification réussit, vide
     *         sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Optional<Utilisateur> authentifierAvecMfa(String login, String password, String mfaCode)
            throws SQLException {
        Optional<Utilisateur> userOpt = authentifier(login, password);

        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();

            // Si MFA est activé, vérifier le code
            if (user.hasMfaEnabled()) {
                if (verifyMfaCode(user.getMfaSecret(), mfaCode)) {
                    return userOpt;
                } else {
                    return Optional.empty(); // Code MFA invalide
                }
            } else {
                // MFA non activé, authentification réussie
                return userOpt;
            }
        }

        return Optional.empty(); // Utilisateur non trouvé ou mot de passe incorrect
    }

    /**
     * Vérifie si un login existe déjà.
     *
     * @param login Login à vérifier
     * @return true si le login existe déjà, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean loginExists(String login) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT 1 FROM UTILISATEUR WHERE login = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, login);

            rs = pstmt.executeQuery();

            return rs.next(); // true si un enregistrement existe

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la vérification de l'existence d'un login", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Crée un nouvel utilisateur.
     *
     * @param login       Login de l'utilisateur
     * @param password    Mot de passe en clair
     * @param role        Rôle de l'utilisateur
     * @param idPersonnel ID du personnel associé (peut être null)
     * @return L'utilisateur créé avec son ID généré
     * @throws SQLException             En cas d'erreur d'accès à la base de données
     * @throws IllegalArgumentException Si le login existe déjà
     */
    public Utilisateur creer(String login, String password, Role role, Integer idPersonnel)
            throws SQLException, IllegalArgumentException {

        // Vérifier si le login existe déjà
        if (loginExists(login)) {
            throw new IllegalArgumentException("Le login existe déjà");
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();
            conn.setAutoCommit(false); // Début transaction

            // Hasher le mot de passe
            String hash = hashPassword(password);

            // Insérer l'utilisateur
            String sql = "INSERT INTO UTILISATEUR (login, hash, role, id_personnel) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, login);
            pstmt.setString(2, hash);
            pstmt.setString(3, role.name());

            if (idPersonnel != null) {
                pstmt.setInt(4, idPersonnel);
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            }

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La création de l'utilisateur a échoué, aucune ligne affectée");
            }

            // Récupérer l'ID généré
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);

                // Créer et retourner l'objet Utilisateur
                Utilisateur utilisateur = new Utilisateur(id, login, hash, role, idPersonnel, null);

                conn.commit(); // Valider transaction
                return utilisateur;
            } else {
                throw new SQLException("La création de l'utilisateur a échoué, aucun ID généré");
            }

        } catch (SQLException ex) {
            if (conn != null) {
                dbUtil.rollback(conn);
            }
            LOGGER.log(Level.SEVERE, "Erreur lors de la création d'un utilisateur", ex);
            throw ex;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true); // Rétablir auto-commit
            }
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Met à jour le mot de passe d'un utilisateur.
     *
     * @param idUtilisateur   ID de l'utilisateur
     * @param nouveauPassword Nouveau mot de passe en clair
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean updatePassword(int idUtilisateur, String nouveauPassword) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbUtil.getConnection();

            // Hasher le nouveau mot de passe
            String hash = hashPassword(nouveauPassword);

            // Mettre à jour le hash
            String sql = "UPDATE UTILISATEUR SET hash = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, hash);
            pstmt.setInt(2, idUtilisateur);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du mot de passe", ex);
            throw ex;
        } finally {
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Active ou désactive l'authentification MFA pour un utilisateur.
     *
     * @param idUtilisateur ID de l'utilisateur
     * @param activer       true pour activer, false pour désactiver
     * @return Le secret MFA si activé, null si désactivé
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public String toggleMfa(int idUtilisateur, boolean activer) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbUtil.getConnection();

            String mfaSecret = null;

            if (activer) {
                // Générer un nouveau secret MFA
                mfaSecret = generateMfaSecret();
            }

            // Mettre à jour le secret MFA
            String sql = "UPDATE UTILISATEUR SET mfa_secret = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);

            if (mfaSecret != null) {
                pstmt.setString(1, mfaSecret);
            } else {
                pstmt.setNull(1, java.sql.Types.VARCHAR);
            }

            pstmt.setInt(2, idUtilisateur);

            pstmt.executeUpdate();

            return mfaSecret;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la gestion du MFA", ex);
            throw ex;
        } finally {
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère un utilisateur par son ID.
     *
     * @param id ID de l'utilisateur
     * @return Optional contenant l'utilisateur s'il existe, vide sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Optional<Utilisateur> findById(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT id, login, hash, role, id_personnel, mfa_secret FROM UTILISATEUR WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Utilisateur utilisateur = new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("hash"),
                        Role.valueOf(rs.getString("role")),
                        rs.getObject("id_personnel") != null ? rs.getInt("id_personnel") : null,
                        rs.getString("mfa_secret"));

                return Optional.of(utilisateur);
            }

            return Optional.empty();

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche d'un utilisateur par ID", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère un utilisateur par son login.
     *
     * @param login Login de l'utilisateur
     * @return Optional contenant l'utilisateur s'il existe, vide sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Optional<Utilisateur> findByLogin(String login) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT id, login, hash, role, id_personnel, mfa_secret FROM UTILISATEUR WHERE login = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, login);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Utilisateur utilisateur = new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("hash"),
                        Role.valueOf(rs.getString("role")),
                        rs.getObject("id_personnel") != null ? rs.getInt("id_personnel") : null,
                        rs.getString("mfa_secret"));

                return Optional.of(utilisateur);
            }

            return Optional.empty();

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche d'un utilisateur par login", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Récupère un utilisateur par l'ID du personnel associé.
     *
     * @param idPersonnel ID du personnel
     * @return Optional contenant l'utilisateur s'il existe, vide sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public Optional<Utilisateur> findByIdPersonnel(int idPersonnel) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT id, login, hash, role, id_personnel, mfa_secret FROM UTILISATEUR WHERE id_personnel = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idPersonnel);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Utilisateur utilisateur = new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("hash"),
                        Role.valueOf(rs.getString("role")),
                        rs.getInt("id_personnel"),
                        rs.getString("mfa_secret"));

                return Optional.of(utilisateur);
            }

            return Optional.empty();

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche d'un utilisateur par ID personnel", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Liste tous les utilisateurs.
     *
     * @return Liste des utilisateurs
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public List<Utilisateur> findAll() throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "SELECT id, login, hash, role, id_personnel, mfa_secret FROM UTILISATEUR ORDER BY login";
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            List<Utilisateur> utilisateurs = new ArrayList<>();

            while (rs.next()) {
                Utilisateur utilisateur = new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("hash"),
                        Role.valueOf(rs.getString("role")),
                        rs.getObject("id_personnel") != null ? rs.getInt("id_personnel") : null,
                        rs.getString("mfa_secret"));

                utilisateurs.add(utilisateur);
            }

            return utilisateurs;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les utilisateurs", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Supprime un utilisateur par son ID.
     *
     * @param id ID de l'utilisateur à supprimer
     * @return true si la suppression a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean delete(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "DELETE FROM UTILISATEUR WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression d'un utilisateur", ex);
            throw ex;
        } finally {
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Met à jour le rôle d'un utilisateur.
     *
     * @param idUtilisateur ID de l'utilisateur
     * @param nouveauRole   Nouveau rôle
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean updateRole(int idUtilisateur, Role nouveauRole) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbUtil.getConnection();

            String sql = "UPDATE UTILISATEUR SET role = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nouveauRole.name());
            pstmt.setInt(2, idUtilisateur);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du rôle", ex);
            throw ex;
        } finally {
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Met à jour les informations d'un utilisateur (login, rôle, idPersonnel).
     *
     * @param utilisateur L'utilisateur à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public boolean update(Utilisateur utilisateur) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = dbUtil.getConnection();
            String sql = "UPDATE UTILISATEUR SET login = ?, role = ?, id_personnel = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, utilisateur.getLogin());
            pstmt.setString(2, utilisateur.getRole().name());
            if (utilisateur.getIdPersonnel() != null) {
                pstmt.setInt(3, utilisateur.getIdPersonnel());
            } else {
                pstmt.setNull(3, java.sql.Types.INTEGER);
            }
            pstmt.setInt(4, utilisateur.getId());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour d'un utilisateur", ex);
            throw ex;
        } finally {
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    /**
     * Compte le nombre d'utilisateurs pour un rôle donné.
     *
     * @param role Le rôle à compter
     * @return Le nombre d'utilisateurs ayant ce rôle
     * @throws SQLException En cas d'erreur d'accès à la base de données
     */
    public int countByRole(Role role) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = dbUtil.getConnection();
            String sql = "SELECT COUNT(*) AS count FROM UTILISATEUR WHERE role = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, role.name());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
            return 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors du comptage des utilisateurs par rôle", ex);
            throw ex;
        } finally {
            dbUtil.closeResultSet(rs);
            dbUtil.closePreparedStatement(pstmt);
            dbUtil.releaseConnection(conn);
        }
    }

    // ====== Méthodes utilitaires pour la gestion des mots de passe et du MFA
    // ======

    /**
     * Hachage du mot de passe avec sel et SHA-256.
     *
     * @param password Mot de passe en clair
     * @return Chaîne formatée contenant le sel et le hash
     */
    private String hashPassword(String password) {
        try {
            // Générer un sel aléatoire
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            // Hasher le mot de passe avec le sel
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());

            // Encoder le sel et le hash en Base64
            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            String hashBase64 = Base64.getEncoder().encodeToString(hashedPassword);

            // Retourner le format sel:hash
            return saltBase64 + ":" + hashBase64;

        } catch (NoSuchAlgorithmException ex) {
            LOGGER.log(Level.SEVERE, "Algorithme de hachage non disponible", ex);
            throw new RuntimeException("Erreur lors du hachage du mot de passe", ex);
        }
    }

    /**
     * Vérifie si un mot de passe correspond au hash stocké.
     * Accepte les deux formats :
     * - salt$hash (utilisé par DbUtil pour les comptes par défaut)
     * - salt:hash (utilisé par UtilisateurDao)
     *
     * @param password   Mot de passe en clair à vérifier
     * @param storedHash Hash stocké au format salt$hash ou salt:hash
     * @return true si le mot de passe correspond, false sinon
     */
    private boolean verifyPassword(String password, String storedHash) {
        try {
            // Accepter les deux séparateurs
            String[] parts = storedHash.contains("$") ? storedHash.split("\\$") : storedHash.split(":");
            if (parts.length != 2) {
                return false; // Format invalide
            }
            byte[] salt = parts[0].length() == 16 ? parts[0].getBytes(java.nio.charset.StandardCharsets.UTF_8)
                    : java.util.Base64.getDecoder().decode(parts[0]);
            byte[] expectedHash;
            if (storedHash.contains("$")) {
                // Format DbUtil : salt (texte) + $ + hash (base64)
                expectedHash = java.util.Base64.getDecoder().decode(parts[1]);
            } else {
                // Format UtilisateurDao : salt (base64) + : + hash (base64)
                expectedHash = java.util.Base64.getDecoder().decode(parts[1]);
            }
            // Hasher le mot de passe fourni avec le même sel
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] actualHash = md.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            // Comparer les deux hash
            return java.security.MessageDigest.isEqual(expectedHash, actualHash);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la vérification du mot de passe", ex);
            return false;
        }
    }

    /**
     * Génère un secret MFA aléatoire.
     *
     * @return Secret encodé en Base32
     */
    private String generateMfaSecret() {
        // Générer 20 octets aléatoires
        SecureRandom random = new SecureRandom();
        byte[] secret = new byte[20];
        random.nextBytes(secret);

        // Encoder en Base32
        return Base32Encode(secret);
    }

    /**
     * Vérifie un code MFA.
     *
     * @param secret Secret MFA
     * @param code   Code fourni par l'utilisateur
     * @return true si le code est valide, false sinon
     */
    private boolean verifyMfaCode(String secret, String code) {
        // Note: Dans une implémentation réelle, nous utiliserions une bibliothèque TOTP
        // comme 'Google Authenticator' ou équivalent
        // Cette implémentation est simplifiée pour l'exemple

        // Vérifier si le code est vide
        if (code == null || code.trim().isEmpty()) {
            return false;
        }

        // Vérifier la longueur du code (généralement 6 chiffres)
        if (code.length() != 6) {
            return false;
        }

        try {
            // Vérifier que le code est numérique
            Integer.parseInt(code);

            // TODO: Implémenter la vérification réelle avec TOTP
            // Pour l'exemple, on considère que "123456" est toujours valide
            return "123456".equals(code);

        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /**
     * Encodage Base32 simplifié.
     *
     * @param data Données à encoder
     * @return Chaîne encodée en Base32
     */
    private String Base32Encode(byte[] data) {
        final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
        StringBuilder result = new StringBuilder();

        // Conversion simplifiée pour l'exemple
        // Dans une implémentation réelle, utilisez une bibliothèque comme Apache
        // Commons Codec
        for (byte b : data) {
            int val = b & 0x1F; // Masque pour ne garder que les 5 bits de poids faible
            result.append(ALPHABET.charAt(val));
        }

        return result.toString();
    }
}