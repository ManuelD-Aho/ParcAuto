package main.java.com.miage.parcauto.util;

import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

/**
 * Gestionnaire de thèmes pour l'application.
 * Permet de changer dynamiquement de thème et de mémoriser le choix de
 * l'utilisateur.
 */
public class ThemeManager {
    private static final Logger LOGGER = Logger.getLogger(ThemeManager.class.getName());
    private static final ThemeManager instance = new ThemeManager();

    private static final String PREF_KEY = "appTheme";
    private static final String DEFAULT_THEME = "default";

    private final Preferences prefs = Preferences.userNodeForPackage(ThemeManager.class);
    private String currentTheme;

    private ThemeManager() {
        currentTheme = prefs.get(PREF_KEY, DEFAULT_THEME);
    }

    /**
     * Obtient l'instance unique du gestionnaire de thèmes.
     *
     * @return L'instance du ThemeManager
     */
    public static ThemeManager getInstance() {
        return instance;
    }

    /**
     * Applique le thème courant à une scène JavaFX.
     *
     * @param scene           La scène à styler
     * @param viewSpecificCss Chemin CSS spécifique à la vue (optionnel)
     */
    public void applyTheme(Scene scene, String... viewSpecificCss) {
        scene.getStylesheets().clear();
        LOGGER.info("Application des styles à la scène. Thème courant: " + currentTheme);

        // Ajouter le thème de base
        String baseThemePath = "/css/theme-base.css";
        URL baseThemeURL = ResourceManager.getResource(baseThemePath);
        if (baseThemeURL != null) {
            scene.getStylesheets().add(baseThemeURL.toExternalForm());
            LOGGER.info("Chargé: " + baseThemePath + " (URL: " + baseThemeURL.toExternalForm() + ")");
        } else {
            LOGGER.warning("NON CHARGÉ (introuvable): " + baseThemePath);
        }

        // Ajouter le thème courant
        String themePath = "/css/theme-" + currentTheme + ".css";
        URL themeURL = ResourceManager.getResource(themePath);
        if (themeURL != null) {
            scene.getStylesheets().add(themeURL.toExternalForm());
            LOGGER.info("Chargé: " + themePath + " (URL: " + themeURL.toExternalForm() + ")");
        } else {
            LOGGER.warning("NON CHARGÉ (introuvable): " + themePath);
        }

        // Ajouter les CSS spécifiques à la vue
        if (viewSpecificCss != null && viewSpecificCss.length > 0) {
            for (String css : viewSpecificCss) {
                if (css != null && !css.trim().isEmpty()) {
                    URL cssURL = ResourceManager.getResource(css);
                    if (cssURL != null) {
                        scene.getStylesheets().add(cssURL.toExternalForm());
                        LOGGER.info("Chargé (spécifique vue): " + css + " (URL: " + cssURL.toExternalForm() + ")");
                    } else {
                        LOGGER.warning("NON CHARGÉ (spécifique vue, introuvable): " + css);
                    }
                }
            }
        } else {
            LOGGER.info("Aucun CSS spécifique à la vue à charger.");
        }
    }

    /**
     * Change le thème de l'application.
     *
     * @param themeName       Le nom du thème à appliquer
     * @param scene           La scène à mettre à jour
     * @param viewSpecificCss Chemin CSS spécifique à la vue (optionnel)
     */
    public void setTheme(String themeName, Scene scene, String... viewSpecificCss) {
        if (isValidTheme(themeName)) {
            currentTheme = themeName;
            prefs.put(PREF_KEY, currentTheme);
            applyTheme(scene, viewSpecificCss);
        } else {
            LOGGER.warning("Thème invalide: " + themeName);
        }
    }

    /**
     * Vérifie si le thème existe.
     *
     * @param themeName Le nom du thème à vérifier
     * @return true si le thème existe, false sinon
     */
    private boolean isValidTheme(String themeName) {
        String[] validThemes = { "default", "dark", "contrast", "corporate", "modern" };
        for (String theme : validThemes) {
            if (theme.equals(themeName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtient le nom du thème actuellement utilisé.
     *
     * @return Le nom du thème courant
     */
    public String getCurrentTheme() {
        return currentTheme;
    }

    /**
     * Applique le thème au niveau de l'application entière.
     *
     * @param stage Le stage principal de l'application
     */
    public void applyThemeToApplication(Stage stage) {
        // Applique aux scènes existantes
        if (stage.getScene() != null) {
            applyTheme(stage.getScene());
        }

        // S'assure que les nouvelles scènes créées auront le thème
        stage.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                applyTheme(newScene);
            }
        });
    }

    /**
     * Cycle au prochain thème disponible.
     *
     * @param scene           La scène à mettre à jour
     * @param viewSpecificCss Chemin CSS spécifique à la vue (optionnel)
     */
    public void cycleNextTheme(Scene scene, String... viewSpecificCss) {
        String nextTheme;
        switch (currentTheme) {
            case "default":
                nextTheme = "dark";
                break;
            case "dark":
                nextTheme = "contrast";
                break;
            case "contrast":
                nextTheme = "corporate";
                break;
            case "corporate":
                nextTheme = "modern";
                break;
            case "modern":
                nextTheme = "default";
                break;
            default:
                nextTheme = "default";
        }
        setTheme(nextTheme, scene, viewSpecificCss);
    }
}