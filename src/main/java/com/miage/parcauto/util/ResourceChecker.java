package main.java.com.miage.parcauto.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilitaire pour vérifier l'existence des ressources dans le projet.
 * Analyse les fichiers FXML et Java pour identifier les ressources référencées.
 *
 * @author MIAGE Holding
 * @version 1.0
 */
public class ResourceChecker {
    private static final Logger LOGGER = Logger.getLogger(ResourceChecker.class.getName());

    // Répertoires à scanner
    private static final String[] SCAN_DIRECTORIES = {
            "src/main/java",
            "src/main/resources/fxml"
    };

    // Patterns pour détecter les références aux ressources
    private static final String[] RESOURCE_PATTERNS = {
            // Dans les fichiers Java
            "getResource\\([\"'](.*?)[\"']\\)",
            "load\\([\"'](.*?)[\"']\\)",
            "FXMLLoader\\([\"'](.*?)[\"']\\)",
            "getStylesheets\\(\\)\\.add\\([\"'](.*?)[\"']\\)",
            "new Image\\([\"'](.*?)[\"']\\)",

            // Dans les fichiers FXML
            "fx:value=\"@(.*?)\"",
            "stylesheets=\"@(.*?)\"",
            "text=\"%(.*)\"",
            "image=\"@(.*?)\""
    };

    /**
     * Point d'entrée principal pour exécuter le vérificateur de ressources.
     *
     * @param args Arguments de ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        LOGGER.info("Démarrage de la vérification des ressources...");

        // S'assurer que les répertoires de ressources existent
        ResourceManager.ensureResourceDirectories();

        // Collecter toutes les références aux ressources
        List<String> referencedResources = scanForResourceReferences();

        // Vérifier l'existence de chaque ressource
        List<String> missingResources = checkResourcesExistence(referencedResources);

        // Afficher le rapport
        if (missingResources.isEmpty()) {
            LOGGER.info("✓ Toutes les ressources référencées sont disponibles.");
        } else {
            LOGGER.warning("❌ Ressources manquantes (" + missingResources.size() + ") :");
            missingResources.forEach(res -> LOGGER.warning("  - " + res));

            // Suggestion pour créer les ressources manquantes
            LOGGER.info("Vous pouvez créer les fichiers manquants avec les commandes suivantes:");
            for (String resource : missingResources) {
                String fullPath = "src/main/resources" + (resource.startsWith("/") ? resource : "/" + resource);
                LOGGER.info("touch \"" + fullPath.replace("/", File.separator) + "\"");
            }
        }

        LOGGER.info("Vérification terminée. " + referencedResources.size() + " références trouvées.");
    }

    /**
     * Scan les fichiers du projet pour trouver les références aux ressources.
     *
     * @return Liste des chemins de ressources référencées
     */
    private static List<String> scanForResourceReferences() {
        List<String> referencedResources = new ArrayList<>();

        for (String directory : SCAN_DIRECTORIES) {
            Path directoryPath = Paths.get(directory);
            if (!Files.exists(directoryPath)) {
                LOGGER.warning("Répertoire de scan introuvable: " + directory);
                continue;
            }

            try {
                Files.walk(directoryPath)
                        .filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".java") || path.toString().endsWith(".fxml"))
                        .forEach(file -> {
                            try {
                                String content = new String(Files.readAllBytes(file));
                                List<String> resources = extractResourcePaths(content);
                                for (String resource : resources) {
                                    if (!referencedResources.contains(resource)) {
                                        referencedResources.add(resource);
                                    }
                                }
                            } catch (IOException e) {
                                LOGGER.log(Level.WARNING, "Erreur lors de la lecture du fichier " + file, e);
                            }
                        });
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Erreur lors du parcours du répertoire " + directory, e);
            }
        }

        return referencedResources;
    }

    /**
     * Extrait les chemins de ressources à partir du contenu d'un fichier.
     *
     * @param content Contenu du fichier à analyser
     * @return Liste des chemins de ressources trouvés
     */
    private static List<String> extractResourcePaths(String content) {
        List<String> resources = new ArrayList<>();

        for (String patternStr : RESOURCE_PATTERNS) {
            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(content);

            while (matcher.find()) {
                String resource = matcher.group(1);

                // Traitement spécial pour certaines syntaxes FXML
                if (resource.startsWith("@")) {
                    resource = resource.substring(1);
                }

                // Ignorer les URLs et les chemins absolus
                if (resource.startsWith("http:") || resource.startsWith("https:") ||
                        resource.startsWith("file:") || resource.startsWith("jar:") ||
                        resource.startsWith("C:") || resource.startsWith("/C:")) {
                    continue;
                }

                // Nettoyer le chemin
                if (resource.contains("?")) {
                    resource = resource.substring(0, resource.indexOf("?"));
                }

                // Ajouter aux ressources si c'est pertinent
                if (!resource.isEmpty() && !resources.contains(resource)) {
                    resources.add(resource);
                }
            }
        }

        return resources;
    }

    /**
     * Vérifie l'existence des ressources référencées.
     *
     * @param referencedResources Liste des chemins de ressources à vérifier
     * @return Liste des ressources manquantes
     */
    private static List<String> checkResourcesExistence(List<String> referencedResources) {
        List<String> missingResources = new ArrayList<>();

        for (String resource : referencedResources) {
            if (ResourceManager.getResource(resource) == null) {
                missingResources.add(resource);
            }
        }

        return missingResources;
    }
}