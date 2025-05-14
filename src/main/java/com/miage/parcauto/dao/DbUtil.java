package main.java.com.miage.parcauto.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utilitaire pour la gestion des connexions à la base de données MySQL.
 * Charge les propriétés de connexion depuis db.properties et fournit des méthodes
 * pour obtenir et fermer les connexions JDBC.
 */
public class DbUtil {

    private static final Logger LOGGER = Logger.getLogger(DbUtil.class.getName());
    private static final Properties dbProperties = new Properties();
    private static final String DB_PROPERTIES_FILE = "db.properties"; // Doit être dans le classpath
    // private static final String DB_PROPERTIES_FILE = "/db.properties"; // Si à la racine du classpath
    // private static final String DB_PROPERTIES_FILE = "main/resources/db.properties"; // Si chemin spécifique


    static {
        try {
            // Tentative de chargement depuis le classpath racine
            InputStream input = DbUtil.class.getClassLoader().getResourceAsStream(DB_PROPERTIES_FILE);
            if (input == null) {
                // Tentative de chargement si le fichier est dans un sous-dossier du classpath
                input = DbUtil.class.getResourceAsStream("/" + DB_PROPERTIES_FILE);
                if (input == null) {
                    LOGGER.log(Level.SEVERE, "Impossible de trouver le fichier " + DB_PROPERTIES_FILE + " dans le classpath.");
                    throw new IOException("Fichier " + DB_PROPERTIES_FILE + " non trouvé.");
                }
            }
            dbProperties.load(input);
            input.close();

            // Chargement du driver JDBC MySQL
            Class.forName(dbProperties.getProperty("db.driver"));
            LOGGER.info("Driver JDBC MySQL chargé avec succès.");

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement du fichier db.properties: " + e.getMessage(), e);
            // Il serait judicieux de lancer une RuntimeException ici pour arrêter l'application
            // si la configuration DB est essentielle au démarrage.
            // throw new RuntimeException("Impossible de charger la configuration de la base de données.", e);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Driver JDBC MySQL non trouvé: " + e.getMessage(), e);
            // throw new RuntimeException("Driver JDBC MySQL non trouvé.", e);
        }  catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur inattendue lors de l'initialisation de DbUtil: " + e.getMessage(), e);
            // throw new RuntimeException("Erreur d'initialisation de DbUtil.", e);
        }
    }

    /**
     * Établit et retourne une connexion à la base de données.
     * L'appelant est responsable de la fermeture de cette connexion.
     *
     * @return Une connexion JDBC à la base de données.
     * @throws SQLException Si une erreur d'accès à la base de données se produit.
     */
    public static Connection getConnection() throws SQLException {
        String url = dbProperties.getProperty("db.url");
        String user = dbProperties.getProperty("db.username");
        String pass = dbProperties.getProperty("db.password");

        if (url == null || user == null || pass == null) {
            LOGGER.log(Level.SEVERE, "Les propriétés de connexion (url, username, password) sont manquantes dans " + DB_PROPERTIES_FILE);
            throw new SQLException("Configuration de la base de données incomplète.");
        }

        // LOGGER.log(Level.INFO, "Tentative de connexion à : {0}", url);
        Connection connection = DriverManager.getConnection(url, user, pass);
        // LOGGER.log(Level.INFO, "Connexion à la base de données établie avec succès.");
        return connection;
    }

    /**
     * Ferme la connexion JDBC donnée.
     *
     * @param connection La connexion à fermer.
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                // LOGGER.log(Level.INFO, "Connexion JDBC fermée.");
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Erreur lors de la fermeture de la connexion JDBC: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Ferme le Statement JDBC donné.
     *
     * @param statement Le statement à fermer.
     */
    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Erreur lors de la fermeture du Statement JDBC: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Ferme le ResultSet JDBC donné.
     *
     * @param resultSet Le resultSet à fermer.
     */
    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Erreur lors de la fermeture du ResultSet JDBC: " + e.getMessage(), e);
            }
        }
    }

    /**
     * Ferme proprement une connexion, un statement et un result set.
     * Utile dans les blocs finally.
     * @param conn La connexion à fermer.
     * @param stmt Le statement à fermer.
     * @param rs Le result set à fermer.
     */
    public static void closeQuietly(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) { /* ignorer */ }
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) { /* ignorer */ }
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) { /* ignorer */ }
    }

    /**
     * Ferme proprement une connexion et un statement.
     * @param conn La connexion à fermer.
     * @param stmt Le statement à fermer.
     */
    public static void closeQuietly(Connection conn, Statement stmt) {
        closeQuietly(conn, stmt, null);
    }

    // Il pourrait être utile d'ajouter une méthode pour gérer les transactions
    // si elles sont gérées de manière centralisée, mais généralement,
    // la gestion des transactions (commit, rollback) se fait dans la logique métier (Services)
    // ou dans les méthodes de repository qui effectuent plusieurs opérations atomiques.
}