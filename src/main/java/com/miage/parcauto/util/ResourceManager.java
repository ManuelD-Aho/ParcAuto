package main.java.com.miage.parcauto.util;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gestionnaire centralisé des ressources de l'application.
 * Fournit des méthodes pour accéder aux ressources (CSS, FXML, images) de
 * manière cohérente.
 */
public class ResourceManager {
    private static final Logger LOGGER = Logger.getLogger(ResourceManager.class.getName());

    // Cache pour éviter de rechercher plusieurs fois les mêmes ressources
    private static final Map<String, URL> resourceCache = new HashMap<>();

    // Répertoires de ressources
    private static final String[] RESOURCE_DIRS = {
            "/",
            "/css/",
            "/fxml/",
            "/images/",
            "/js/"
    };

    /**
     * Obtient l'URL d'une ressource avec gestion de fallback.
     * Cherche d'abord dans le cache, puis dans le classpath, puis en chemin absolu.
     *
     * @param resourcePath Le chemin de la ressource (ex: "/fxml/login.fxml")
     * @return L'URL de la ressource ou null si introuvable
     */
    public static URL getResource(String resourcePath) {
        // Normaliser le chemin de la ressource
        if (!resourcePath.startsWith("/")) {
            resourcePath = "/" + resourcePath;
        }

        // Vérifier d'abord dans le cache
        if (resourceCache.containsKey(resourcePath)) {
            return resourceCache.get(resourcePath);
        }

        // Essayer de charger depuis le classpath
        URL resource = ResourceManager.class.getResource(resourcePath);

        // Si non trouvé, essayer les différents répertoires de ressources
        if (resource == null) {
            for (String dir : RESOURCE_DIRS) {
                String adjustedPath = resourcePath.startsWith(dir) ? resourcePath : dir + resourcePath.substring(1);
                resource = ResourceManager.class.getResource(adjustedPath);
                if (resource != null) {
                    LOGGER.info("Ressource trouvée avec chemin ajusté: " + adjustedPath);
                    resourceCache.put(resourcePath, resource);
                    return resource;
                }
            }
        }

        // Si toujours non trouvé, essayer le chemin absolu (utile en développement)
        if (resource == null) {
            try {
                // Essayer le chemin direct depuis resources
                String basePath = resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath;
                Path absolutePath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", basePath);

                if (absolutePath.toFile().exists()) {
                    resource = absolutePath.toUri().toURL();
                    LOGGER.info("Ressource trouvée en chemin absolu: " + absolutePath);
                    resourceCache.put(resourcePath, resource);
                    return resource;
                }

                // Essayer avec différents répertoires
                for (String dir : RESOURCE_DIRS) {
                    String dirPath = dir.startsWith("/") ? dir.substring(1) : dir;
                    String fileName = resourcePath.startsWith("/")
                            ? resourcePath.substring(resourcePath.lastIndexOf('/') + 1)
                            : resourcePath.substring(resourcePath.lastIndexOf('/') + 1);

                    Path absoluteDirPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources",
                            dirPath, fileName);

                    if (absoluteDirPath.toFile().exists()) {
                        resource = absoluteDirPath.toUri().toURL();
                        LOGGER.info("Ressource trouvée en chemin absolu avec répertoire: " + absoluteDirPath);
                        resourceCache.put(resourcePath, resource);
                        return resource;
                    }
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Erreur lors de la recherche du chemin absolu pour " + resourcePath, e);
            }
        } else {
            // Ressource trouvée dans le classpath
            resourceCache.put(resourcePath, resource);
        }

        if (resource == null) {
            LOGGER.warning("Ressource introuvable: " + resourcePath);
        }

        return resource;
    }

    /**
     * Obtient le chemin externe d'une ressource CSS pour pouvoir l'ajouter aux
     * feuilles de style.
     *
     * @param cssPath Le chemin de la feuille de style CSS
     * @return Le chemin externe de la feuille de style ou null si introuvable
     */
    public static String getStylesheetPath(String cssPath) {
        URL resource = getResource(cssPath);
        return resource != null ? resource.toExternalForm() : null;
    }

    /**
     * Applique une liste de feuilles de style CSS à une scène JavaFX.
     *
     * @param scene    La scène JavaFX à styliser
     * @param cssPaths Liste des chemins de feuilles de style à appliquer
     */
    public static void applyStylesheets(javafx.scene.Scene scene, String... cssPaths) {
        for (String cssPath : cssPaths) {
            String stylesheetPath = getStylesheetPath(cssPath);
            if (stylesheetPath != null && !scene.getStylesheets().contains(stylesheetPath)) {
                scene.getStylesheets().add(stylesheetPath);
                LOGGER.fine("Feuille de style appliquée: " + cssPath);
            } else if (stylesheetPath == null) {
                LOGGER.warning("Impossible d'appliquer la feuille de style (introuvable): " + cssPath);
            }
        }
    }

    /**
     * Charge un fichier FXML.
     *
     * @param fxmlPath Le chemin du fichier FXML
     * @return Un chargeur FXML configuré avec la ressource
     * @throws IllegalArgumentException Si la ressource est introuvable
     */
    public static javafx.fxml.FXMLLoader getFXMLLoader(String fxmlPath) {
        URL resource = getResource(fxmlPath);
        if (resource == null) {
            throw new IllegalArgumentException("Fichier FXML introuvable: " + fxmlPath);
        }
        return new javafx.fxml.FXMLLoader(resource);
    }

    /**
     * Charge une image.
     *
     * @param imagePath Le chemin de l'image
     * @return L'image chargée ou null si introuvable
     */
    public static javafx.scene.image.Image getImage(String imagePath) {
        URL resource = getResource(imagePath);
        if (resource == null) {
            LOGGER.warning("Image introuvable: " + imagePath);
            return null;
        }
        try {
            return new javafx.scene.image.Image(resource.toExternalForm());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Erreur lors du chargement de l'image: " + imagePath, e);
            return null;
        }
    }

    /**
     * Vérifier que toutes les ressources nécessaires existent.
     * Utile pour valider la configuration au démarrage.
     *
     * @param resourcePaths Liste des chemins de ressources à vérifier
     * @return true si toutes les ressources existent, false sinon
     */
    public static boolean validateResources(String... resourcePaths) {
        boolean allValid = true;

        for (String path : resourcePaths) {
            if (getResource(path) == null) {
                LOGGER.warning("Ressource manquante: " + path);
                allValid = false;
            }
        }

        return allValid;
    }

    /**
     * Créer les dossiers de ressources s'ils n'existent pas.
     * Utile pour la première initialisation du projet.
     */
    public static void ensureResourceDirectories() {
        for (String dir : RESOURCE_DIRS) {
            String cleanDir = dir.startsWith("/") ? dir.substring(1) : dir;
            if (cleanDir.endsWith("/")) {
                cleanDir = cleanDir.substring(0, cleanDir.length() - 1);
            }

            if (!cleanDir.isEmpty()) {
                Path dirPath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", cleanDir);
                File dirFile = dirPath.toFile();

                if (!dirFile.exists()) {
                    boolean created = dirFile.mkdirs();
                    if (created) {
                        LOGGER.info("Répertoire de ressources créé: " + dirPath);
                    } else {
                        LOGGER.warning("Impossible de créer le répertoire de ressources: " + dirPath);
                    }
                }
            }
        }
    }
}