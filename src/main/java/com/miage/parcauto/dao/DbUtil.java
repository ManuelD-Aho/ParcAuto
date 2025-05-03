package com.miage.parcauto.dao;

import java.io.FileInputStream;
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
 * Classe utilitaire pour gérer les connexions à la base de données.
 * Implémente le pattern Singleton pour maintenir un pool de connexions.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class DbUtil {

    private static final Logger LOGGER = Logger.getLogger(DbUtil.class.getName());

    // Paramètres de connexion à la base de données
    private static String url;
    private static String user;
    private static String password;
    private static int maxPoolSize;

    // Pool de connexions simple
    private static Connection[] connectionPool;
    private static boolean[] connectionStatus; // true si connexion disponible

    // Instance unique (Singleton)
    private static DbUtil instance;

    /**
     * Constructeur privé pour implémenter le pattern Singleton.
     * Initialise le pool de connexions.
     */
    private DbUtil() {
        try {
            // Enregistrement du driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Chargement des propriétés
            loadProperties();

            // Initialisation du pool
            initializePool();

            LOGGER.log(Level.INFO, "Pool de connexions initialisé avec {0} connexions", maxPoolSize);
        } catch (ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Driver JDBC MySQL introuvable", ex);
            throw new RuntimeException("Driver JDBC MySQL introuvable", ex);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'initialisation du pool de connexions", ex);
            throw new RuntimeException("Erreur lors de l'initialisation du pool de connexions", ex);
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
     * Charge les paramètres de connexion depuis un fichier de propriétés
     * ou depuis les variables d'environnement.
     */
    private void loadProperties() {
        Properties props = new Properties();

        // Essayer de charger depuis un fichier
        try (InputStream input = new FileInputStream("db.properties")) {
            props.load(input);

            url = props.getProperty("db.url");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");
            maxPoolSize = Integer.parseInt(props.getProperty("db.maxPoolSize", "10"));

        } catch (IOException ex) {
            // Si le fichier n'est pas trouvé, utiliser les variables d'environnement
            LOGGER.log(Level.INFO, "Fichier db.properties non trouvé, utilisation des variables d'environnement");

            String dbHost = System.getenv("MYSQL_HOST") != null ? System.getenv("MYSQL_HOST") : "db";
            String dbPort = System.getenv("MYSQL_PORT") != null ? System.getenv("MYSQL_PORT") : "3306";
            String dbName = System.getenv("MYSQL_DATABASE") != null ? System.getenv("MYSQL_DATABASE") : "ParcAuto";

            url = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            user = System.getenv("MYSQL_USER") != null ? System.getenv("MYSQL_USER") : "root";
            password = System.getenv("MYSQL_PASSWORD") != null ? System.getenv("MYSQL_ROOT_PASSWORD") : "Root!23";
            maxPoolSize = System.getenv("DB_MAX_POOL_SIZE") != null ?
                    Integer.parseInt(System.getenv("DB_MAX_POOL_SIZE")) : 10;
        }

        // Valider les paramètres
        if (url == null || user == null || password == null) {
            throw new IllegalStateException("Paramètres de connexion à la base de données manquants");
        }
    }

    /**
     * Initialise le pool de connexions.
     *
     * @throws SQLException Si une erreur se produit lors de la création des connexions
     */
    private void initializePool() throws SQLException {
        connectionPool = new Connection[maxPoolSize];
        connectionStatus = new boolean[maxPoolSize];

        // Initialiser les connexions
        for (int i = 0; i < maxPoolSize; i++) {
            connectionPool[i] = createConnection();
            connectionStatus[i] = true; // Disponible
        }
    }

    /**
     * Crée une nouvelle connexion à la base de données.
     *
     * @return Une nouvelle connexion
     * @throws SQLException Si une erreur se produit lors de la création de la connexion
     */
    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Obtient une connexion du pool.
     *
     * @return Une connexion à la base de données
     * @throws SQLException Si aucune connexion n'est disponible ou si une erreur se produit
     */
    public synchronized Connection getConnection() throws SQLException {
        // Chercher une connexion disponible
        for (int i = 0; i < maxPoolSize; i++) {
            if (connectionStatus[i]) {
                // Vérifier si la connexion est toujours valide
                if (connectionPool[i] == null || connectionPool[i].isClosed() || !connectionPool[i].isValid(2)) {
                    connectionPool[i] = createConnection();
                }

                connectionStatus[i] = false; // Marquer comme utilisée
                return connectionPool[i];
            }
        }

        // Aucune connexion disponible, en créer une nouvelle (hors pool)
        LOGGER.log(Level.WARNING, "Pool de connexions saturé, création d'une connexion temporaire");
        return createConnection();
    }

    /**
     * Libère une connexion et la remet dans le pool.
     *
     * @param conn La connexion à libérer
     */
    public synchronized void releaseConnection(Connection conn) {
        if (conn != null) {
            // Chercher la connexion dans le pool
            for (int i = 0; i < maxPoolSize; i++) {
                if (connectionPool[i] == conn) {
                    connectionStatus[i] = true; // Marquer comme disponible
                    return;
                }
            }

            // Si ce n'est pas une connexion du pool, la fermer
            try {
                conn.close();
            } catch (SQLException ex) {
                LOGGER.log(Level.WARNING, "Erreur lors de la fermeture d'une connexion", ex);
            }
        }
    }

    /**
     * Ferme une ressource JDBC (Statement, PreparedStatement, ResultSet).
     * Méthode générique pour fermer les différents types de ressources.
     *
     * @param <T> Type de la ressource (AutoCloseable)
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
     *
     * @param conn La connexion sur laquelle effectuer le rollback
     */
    public void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LOGGER.log(Level.WARNING, "Erreur lors du rollback", ex);
            }
        }
    }

    /**
     * Ferme toutes les connexions du pool.
     * À utiliser lors de l'arrêt de l'application.
     */
    public synchronized void shutdown() {
        for (int i = 0; i < maxPoolSize; i++) {
            if (connectionPool[i] != null) {
                try {
                    connectionPool[i].close();
                } catch (SQLException ex) {
                    LOGGER.log(Level.WARNING, "Erreur lors de la fermeture d'une connexion", ex);
                } finally {
                    connectionPool[i] = null;
                }
            }
        }
        LOGGER.log(Level.INFO, "Pool de connexions fermé");
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
            testConn = createConnection();
            stmt = testConn.createStatement();
            rs = stmt.executeQuery("SELECT 1");

            if (rs.next() && rs.getInt(1) == 1) {
                isValid = true;
                LOGGER.log(Level.INFO, "Connexion à la base de données validée");
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Échec de test de connexion à la base de données", ex);
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
}