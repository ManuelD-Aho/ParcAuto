package main.java.com.miage.parcauto.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe utilitaire simplifiée pour gérer les connexions à la base de données.
 * Implémente le pattern Singleton pour maintenir une connexion unique.
 *
 * @author MIAGE Holding
 * @version 1.3
 * @date 2025-05-09
 */
public class DbUtil {

    private static final Logger LOGGER = Logger.getLogger(DbUtil.class.getName());

    // Paramètres de connexion à la base de données
    private static String url;
    private static String user;
    private static String password;

    // Instance unique (Singleton)
    private static DbUtil instance;

    // Connexion courante
    private Connection connection;

    /**
     * Constructeur privé pour implémenter le pattern Singleton.
     * Initialise la connexion à la base de données.
     */
    private DbUtil() {
        try {
            // Chargement des propriétés
            loadProperties();

            // Enregistrement du driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            LOGGER.log(Level.INFO, "Configuration de la base de données chargée avec succès");
            // Initialisation des utilisateurs par défaut
            initializeDefaultUsers();

        } catch (ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Driver JDBC MySQL introuvable", ex);
            throw new RuntimeException("Driver JDBC MySQL introuvable", ex);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'initialisation de la connexion", ex);
            throw new RuntimeException("Erreur lors de l'initialisation de la connexion", ex);
        }
    }

    /**
     * Récupère l'instance unique de DbUtil (Singleton).
     *
     * @return L'instance unique de DbUtil
     */
    public static synchronized DbUtil getInstance() {
        if (instance == null) {
            instance = new DbUtil();
        }
        return instance;
    }

    /**
     * Charge les paramètres de connexion depuis le fichier de propriétés ou
     * l'environnement.
     */
    private void loadProperties() {
        Properties props = new Properties();
        boolean propertiesLoaded = false;

        // 1. Essayer de charger depuis le fichier dans le classpath
        try (InputStream input = getClass().getResourceAsStream("/db.properties")) {
            if (input != null) {
                props.load(input);
                propertiesLoaded = true;
                LOGGER.log(Level.INFO, "Paramètres de connexion chargés depuis db.properties (classpath)");
            }
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, "Impossible de charger db.properties depuis le classpath", ex);
        }

        // 2. Essayer de charger depuis le fichier dans dao/
        if (!propertiesLoaded) {
            try (InputStream input = getClass().getResourceAsStream("/dao/db.properties")) {
                if (input != null) {
                    props.load(input);
                    propertiesLoaded = true;
                    LOGGER.log(Level.INFO, "Paramètres de connexion chargés depuis dao/db.properties");
                }
            } catch (IOException ex) {
                LOGGER.log(Level.WARNING, "Impossible de charger dao/db.properties", ex);
            }
        }

        // 3. Essayer de lire des variables d'environnement Docker si disponibles
        if (!propertiesLoaded) {
            String dbHost = System.getenv("MYSQL_HOST");
            if (dbHost != null) {
                String dbPort = System.getenv("MYSQL_PORT") != null ? System.getenv("MYSQL_PORT") : "3306";
                String dbName = System.getenv("MYSQL_DATABASE") != null ? System.getenv("MYSQL_DATABASE") : "ParcAuto";

                url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName
                        + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
                user = "root";
                password = System.getenv("MYSQL_ROOT_PASSWORD") != null ? System.getenv("MYSQL_ROOT_PASSWORD")
                        : "Root!123";

                propertiesLoaded = true;
                LOGGER.log(Level.INFO, "Paramètres de connexion chargés depuis variables d'environnement Docker");
            }
        }

        // 4. Utiliser des valeurs par défaut si rien n'a été chargé
        if (!propertiesLoaded) {
            url = "jdbc:mysql://localhost:3306/ParcAuto?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            user = "root";
            password = "Root!123";
            LOGGER.log(Level.WARNING, "Utilisation des paramètres de connexion par défaut");
        } else {
            // Lire les propriétés si elles ont été chargées depuis un fichier
            if (props.size() > 0) {
                url = props.getProperty("db.url");
                user = props.getProperty("db.user");
                password = props.getProperty("db.password");
            }
        }

        // Valider les paramètres
        if (url == null || user == null || password == null) {
            throw new IllegalStateException("Paramètres de connexion à la base de données manquants");
        }

        LOGGER.log(Level.INFO, "URL de connexion configurée: {0}", url);
        LOGGER.log(Level.INFO, "Utilisateur configuré: {0}", user);
    }

    /**
     * Obtient une connexion à la base de données.
     *
     * @return Une connexion à la base de données
     * @throws SQLException Si une erreur se produit lors de la connexion
     */
    public synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, user, password);
            LOGGER.log(Level.INFO, "Nouvelle connexion établie à {0}", url);
        }
        return connection;
    }

    /**
     * Libère une connexion et la remet dans le pool.
     * Dans cette implémentation simplifiée, vérifie seulement si la connexion
     * passée est la même que la connexion stockée.
     *
     * @param conn La connexion à libérer
     */
    public synchronized void releaseConnection(Connection conn) {
        if (conn != null) {
            // Si c'est notre connexion stockée, on la laisse
            // (elle sera fermée via closeConnection si nécessaire)
            if (connection != conn) {
                // Si ce n'est pas notre connexion stockée, on la ferme
                try {
                    conn.close();
                    LOGGER.log(Level.INFO, "Connexion externe fermée");
                } catch (SQLException ex) {
                    LOGGER.log(Level.WARNING, "Erreur lors de la fermeture d'une connexion externe", ex);
                }
            }
        }
    }

    /**
     * Ferme la connexion à la base de données.
     */
    public synchronized void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                LOGGER.log(Level.INFO, "Connexion fermée");
            } catch (SQLException ex) {
                LOGGER.log(Level.WARNING, "Erreur lors de la fermeture de la connexion", ex);
            }
        }
    }

    /**
     * Ferme une ressource JDBC (Statement, PreparedStatement, ResultSet).
     *
     * @param <T>      Type de la ressource (AutoCloseable)
     * @param resource La ressource à fermer
     */
    public <T extends AutoCloseable> void closeResource(T resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception ex) {
                LOGGER.log(Level.WARNING, "Erreur lors de la fermeture d'une ressource", ex);
            }
        }
    }

    /**
     * Ferme un Statement.
     *
     * @param stmt Le Statement à fermer
     */
    public void closeStatement(Statement stmt) {
        closeResource(stmt);
    }

    /**
     * Ferme un PreparedStatement.
     *
     * @param pstmt Le PreparedStatement à fermer
     */
    public void closePreparedStatement(PreparedStatement pstmt) {
        closeResource(pstmt);
    }

    /**
     * Ferme un ResultSet.
     *
     * @param rs Le ResultSet à fermer
     */
    public void closeResultSet(ResultSet rs) {
        closeResource(rs);
    }

    /**
     * Effectue un rollback sur une connexion.
     */
    public void rollback(Connection conn) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                LOGGER.log(Level.WARNING, "Erreur lors du rollback", ex);
            }
        }
    }

    /**
     * Vérifie la validité de la connexion à la base de données.
     *
     * @return true si la connexion est valide, false sinon
     */
    public boolean testConnection() {
        Connection testConn = null;
        Statement stmt = null;
        ResultSet rs = null;
        boolean isValid = false;

        try {
            testConn = DriverManager.getConnection(url, user, password);
            stmt = testConn.createStatement();
            rs = stmt.executeQuery("SELECT 1");

            if (rs.next() && rs.getInt(1) == 1) {
                isValid = true;
                LOGGER.log(Level.INFO, "Connexion à la base de données validée");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Échec de test de connexion à la base de données: {0}", ex.getMessage());
        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            if (testConn != null) {
                try {
                    testConn.close();
                } catch (SQLException ex) {
                    LOGGER.log(Level.WARNING, "Erreur lors de la fermeture de la connexion de test", ex);
                }
            }
        }

        return isValid;
    }

    /**
     * Ferme toutes les connexions et ressources lors de l'arrêt de l'application.
     */
    public void shutdown() {
        closeConnection();
        LOGGER.log(Level.INFO, "Ressources de base de données libérées");
    }

    /**
     * Génère un hash SHA-256 avec sel pour un mot de passe donné.
     *
     * @param password Le mot de passe en clair
     * @param salt     Le sel à utiliser (aléatoire ou fixe)
     * @return Le hash encodé en base64 (format : salt$hash)
     */
    private static String hashPassword(String password, String salt) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            byte[] hashed = md.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            String hashBase64 = java.util.Base64.getEncoder().encodeToString(hashed);
            return salt + "$" + hashBase64;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du hachage du mot de passe", e);
        }
    }

    /**
     * Génère un sel aléatoire (16 caractères alphanumériques).
     *
     * @return Un sel aléatoire
     */
    private static String generateSalt() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        java.util.Random rnd = new java.util.Random();
        for (int i = 0; i < 16; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * Initialise les utilisateurs par défaut si la table est vide.
     * Ajoute un utilisateur pour chaque rôle (U1, U2, U3, U4) avec mot de passe
     * 'azerty'.
     */
    private void initializeDefaultUsers() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            // Vérifier si la table UTILISATEUR existe et est vide
            rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM UTILISATEUR");
            if (rs.next() && rs.getInt(1) == 0) {
                LOGGER.info("Aucun utilisateur trouvé, insertion des comptes par défaut...");
                String sql = "INSERT INTO UTILISATEUR (login, hash, role, id_personnel) VALUES (?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                // U1 Responsable Logistique
                String salt1 = generateSalt();
                pstmt.setString(1, "u1");
                pstmt.setString(2, hashPassword("azerty", salt1));
                pstmt.setString(3, "U1");
                pstmt.setNull(4, java.sql.Types.INTEGER);
                pstmt.executeUpdate();
                // U2 Agent Logistique
                String salt2 = generateSalt();
                pstmt.setString(1, "u2");
                pstmt.setString(2, hashPassword("azerty", salt2));
                pstmt.setString(3, "U2");
                pstmt.setNull(4, java.sql.Types.INTEGER);
                pstmt.executeUpdate();
                // U3 Sociétaire
                String salt3 = generateSalt();
                pstmt.setString(1, "u3");
                pstmt.setString(2, hashPassword("azerty", salt3));
                pstmt.setString(3, "U3");
                pstmt.setNull(4, java.sql.Types.INTEGER);
                pstmt.executeUpdate();
                // U4 Administrateur Système
                String salt4 = generateSalt();
                pstmt.setString(1, "admin");
                pstmt.setString(2, hashPassword("azerty", salt4));
                pstmt.setString(3, "U4");
                pstmt.setNull(4, java.sql.Types.INTEGER);
                pstmt.executeUpdate();
                LOGGER.info("Comptes utilisateurs par défaut créés (login: u1, u2, u3, admin / mot de passe: azerty)");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'initialisation des utilisateurs par défaut", ex);
        } finally {
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            releaseConnection(conn);
        }
    }
}