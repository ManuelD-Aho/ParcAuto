package main.java.com.miage.parcauto.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.com.miage.parcauto.dao.UtilisateurDao;
import main.java.com.miage.parcauto.service.EntretienService;
import main.java.com.miage.parcauto.service.FinanceService;
import main.java.com.miage.parcauto.service.MissionService;
import main.java.com.miage.parcauto.service.VehiculeService;
import main.java.com.miage.parcauto.util.Permission;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.java.com.miage.parcauto.dao.FinanceDao;

/**
 * Contrôleur pour la vue du tableau de bord.
 * Affiche différentes informations et options en fonction du rôle de
 * l'utilisateur connecté.
 * 100% JavaFX : alertes dynamiques sans WebView/JS, stylées via theme.css.
 * 
 * @author MIAGE Holding
 * @version 1.1
 */
public class DashboardController extends BaseController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(DashboardController.class.getName());

    // Services
    private final VehiculeService vehiculeService;
    private final MissionService missionService;
    private final FinanceService financeService;
    private final EntretienService entretienService;

    @FXML
    private Label lblUserName;

    @FXML
    private Label lblUserRole;

    @FXML
    private MenuButton menuVehicules;

    @FXML
    private MenuItem menuItemConsulterVehicules;

    @FXML
    private MenuItem menuItemAjouterVehicule;

    @FXML
    private MenuItem menuItemModifierVehicule;

    @FXML
    private MenuItem menuItemSupprimerVehicule;

    @FXML
    private MenuItem menuItemChangerEtatVehicule;

    @FXML
    private MenuItem menuItemDeclarerPanne;

    @FXML
    private MenuButton menuMissions;

    @FXML
    private MenuButton menuEntretien;

    @FXML
    private MenuButton menuFinances;

    @FXML
    private MenuButton menuDocuments;

    @FXML
    private MenuButton menuRapports;

    @FXML
    private MenuButton menuAdministration;

    @FXML
    private Button btnDeconnexion;

    @FXML
    private GridPane gridStatsVehicules;

    @FXML
    private GridPane gridStatsMissions;

    @FXML
    private GridPane gridStatsFinances;

    @FXML
    private GridPane gridStatsEntretien;

    @FXML
    private Pane paneAlertes;

    @FXML
    private Label lblNbVehicules;

    @FXML
    private Label lblNbMissionsEnCours;

    @FXML
    private Label lblSoldeGlobal;

    @FXML
    private Label lblNbEntretiensAVenir;

    /**
     * Constructeur par défaut.
     */
    public DashboardController() {
        this.vehiculeService = new VehiculeService();
        this.missionService = new MissionService();
        this.financeService = new FinanceService();
        this.entretienService = new EntretienService();
    }

    /**
     * Constructeur pour l'injection de dépendances (tests).
     * 
     * @param vehiculeService  Service de gestion des véhicules
     * @param missionService   Service de gestion des missions
     * @param financeService   Service de gestion des finances
     * @param entretienService Service de gestion des entretiens
     */
    public DashboardController(VehiculeService vehiculeService,
            MissionService missionService,
            FinanceService financeService,
            EntretienService entretienService) {
        this.vehiculeService = vehiculeService;
        this.missionService = missionService;
        this.financeService = financeService;
        this.entretienService = entretienService;
    }

    /**
     * Initialise le contrôleur.
     * 
     * @param url URL de localisation
     * @param rb  ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (!isAuthenticated()) {
            // Si l'utilisateur n'est pas authentifié, rediriger vers la page de login
            Platform.runLater(() -> {
                navigateTo(btnDeconnexion, "/fxml/login.fxml", "Gestion de Parc Automobile - Authentification");
            });
            return;
        }

        // Afficher les informations de l'utilisateur
        updateUserInfo();

        // Vérifier les permissions et configurer l'interface
        configureInterface();

        // Charger les statistiques
        loadStatistics();

        // Charger les alertes
        loadAlertesData();
    }

    /**
     * Vérifie les permissions de l'utilisateur pour accéder au tableau de bord.
     * Tous les utilisateurs authentifiés peuvent accéder au tableau de bord.
     * 
     * @return true si l'utilisateur est authentifié, false sinon
     */
    @Override
    protected boolean checkPermissions() {
        return isAuthenticated();
    }

    /**
     * Met à jour les informations d'utilisateur affichées.
     */
    public void updateUserInfo() {
        if (getCurrentUser() != null) {
            lblUserName.setText(getCurrentUser().getLogin());
            lblUserRole.setText(getCurrentUser().getRole().getLibelle());
        }
    }

    /**
     * Configure l'interface en fonction des permissions de l'utilisateur.
     */
    private void configureInterface() {
        // Configuration du menu Véhicules
        configureVehiculesMenu();

        // Configuration du menu Missions
        configureMissionsMenu();

        // Configuration du menu Entretien
        configureEntretienMenu();

        // Configuration du menu Finances
        configureFinancesMenu();

        // Configuration du menu Documents
        configureDocumentsMenu();

        // Configuration du menu Rapports
        configureRapportsMenu();

        // Configuration du menu Administration
        configureAdministrationMenu();

        // Configuration des tableaux de bord statistiques
        configureStatsPanels();
    }

    /**
     * Affiche dynamiquement les alertes dans le panneau dédié (100% JavaFX).
     * Les alertes sont issues des services FinanceService (assurances, entretiens).
     */
    private void loadAlertesData() {
        // Vérifier si le panneau d'alertes est disponible
        if (paneAlertes == null) {
            LOGGER.warning("Impossible de charger les alertes : paneAlertes est null");
            return;
        }

        try {
            paneAlertes.getChildren().clear();
            VBox vbox = new VBox(10);
            vbox.getStyleClass().add("alertes-pane");
            Label titre = new Label("Alertes");
            titre.getStyleClass().add("alertes-title");
            vbox.getChildren().add(titre);

            List<AlerteDashboard> alertes = new ArrayList<>();

            // Alertes d'assurance (ex: expiration dans 30 jours)
            try {
                List<FinanceDao.AlerteAssurance> alertesAssurance = financeService.getAlertesAssurances(30);
                for (FinanceDao.AlerteAssurance a : alertesAssurance) {
                    String titreA = "Assurance à renouveler";
                    String msg = String.format("%s %s (%s) - expire le %s, coût: %.2f €", a.getMarque(), a.getModele(),
                            a.getImmatriculation(), a.getDateFin().toLocalDate(), a.getCoutAssurance());
                    alertes.add(new AlerteDashboard(titreA, msg, a.getDateFin().toLocalDate().toString(),
                            "finance-alerte"));
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Erreur lors du chargement des alertes d'assurance", e);
            }

            // Alertes d'entretien (ex: entretien préventif à prévoir)
            try {
                List<FinanceDao.AlerteEntretien> alertesEntretien = financeService.getAlertesEntretiens(10000);
                for (FinanceDao.AlerteEntretien e : alertesEntretien) {
                    String titreE = "Entretien préventif à prévoir";
                    String msg = String.format("%s %s (%s) - %d km depuis dernier entretien", e.getMarque(),
                            e.getModele(), e.getImmatriculation(), e.getKmDepuisDernierEntretien());
                    String dateDernier = e.getDateDernierEntretien() != null
                            ? e.getDateDernierEntretien().toLocalDate().toString()
                            : "?";
                    alertes.add(new AlerteDashboard(titreE, msg, dateDernier, "entretien-alerte"));
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Erreur lors du chargement des alertes d'entretien", e);
            }

            if (alertes.isEmpty()) {
                Label info = new Label("Aucune alerte à afficher pour le moment.");
                info.getStyleClass().add("alerte-text");
                vbox.getChildren().add(info);
            } else {
                for (AlerteDashboard alerte : alertes) {
                    HBox box = new HBox(10);
                    box.getStyleClass().addAll("alerte-item", alerte.cssClass);
                    Label titreAlerte = new Label(alerte.titre);
                    titreAlerte.getStyleClass().add("alerte-date");
                    Label texte = new Label(alerte.message);
                    texte.getStyleClass().add("alerte-text");
                    Label date = new Label(alerte.date);
                    date.getStyleClass().add("alerte-date");
                    box.getChildren().addAll(titreAlerte, texte, date);
                    vbox.getChildren().add(box);
                }
            }
            paneAlertes.getChildren().add(vbox);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'affichage des alertes", e);
        }
    }

    /**
     * Structure simple pour une alerte dashboard (100% JavaFX, pas de JSON).
     * Permet d'afficher dynamiquement les alertes d'assurance et d'entretien.
     */
    private static class AlerteDashboard {
        final String titre;
        final String message;
        final String date;
        final String cssClass;

        AlerteDashboard(String titre, String message, String date, String cssClass) {
            this.titre = titre;
            this.message = message;
            this.date = date;
            this.cssClass = cssClass;
        }
    }

    /**
     * Charge les statistiques à afficher sur le dashboard.
     */
    private void loadStatistics() {
        try {
            // Véhicules
            int vehiculesCount = vehiculeService.getVehiculesCount();
            lblNbVehicules.setText(String.valueOf(vehiculesCount));

            // Missions en cours
            int missionsEnCours = missionService.getMissionsEnCoursCount();
            lblNbMissionsEnCours.setText(String.valueOf(missionsEnCours));

            // Solde global
            java.math.BigDecimal soldeGlobal = financeService.getSoldeGlobal();
            lblSoldeGlobal.setText(soldeGlobal.toString() + " €");

            // Entretiens à venir
            int entretiensAVenir = entretienService.getEntretiensAVenirCount();
            lblNbEntretiensAVenir.setText(String.valueOf(entretiensAVenir));

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement des statistiques", e);
        }
    }

    /**
     * Configure le menu Véhicules en fonction des permissions.
     */
    private void configureVehiculesMenu() {
        // Vérifier les permissions pour chaque action
        boolean canConsult = hasAnyPermission(
                Permission.CONSULTER_VEHICULES_TOUS,
                Permission.CONSULTER_VEHICULES_PERSONNEL,
                Permission.CONSULTER_VEHICULES_LECTURE);

        boolean canAdd = hasPermission(Permission.AJOUTER_VEHICULE);
        boolean canModify = hasPermission(Permission.MODIFIER_VEHICULE);
        boolean canDelete = hasPermission(Permission.SUPPRIMER_VEHICULE);

        boolean canChangeState = hasAnyPermission(
                Permission.CHANGER_ETAT_VEHICULE_TOUS,
                Permission.CHANGER_ETAT_VEHICULE_LIMITE);

        boolean canReportIssue = hasAnyPermission(
                Permission.DECLARER_PANNE_TOUS,
                Permission.DECLARER_PANNE_PERSONNEL);

        // Configurer la visibilité des éléments de menu
        menuItemConsulterVehicules.setVisible(canConsult);
        menuItemAjouterVehicule.setVisible(canAdd);
        menuItemModifierVehicule.setVisible(canModify);
        menuItemSupprimerVehicule.setVisible(canDelete);
        menuItemChangerEtatVehicule.setVisible(canChangeState);
        menuItemDeclarerPanne.setVisible(canReportIssue);

        // Si aucune permission, cacher le menu entier
        menuVehicules.setVisible(canConsult || canAdd || canModify || canDelete || canChangeState || canReportIssue);
    }

    // Autres méthodes de configuration des menus restent inchangées...
    // configureMissionsMenu(), configureEntretienMenu(), etc.

    /**
     * Configure le menu Missions en fonction des permissions.
     */
    private void configureMissionsMenu() {
        boolean hasAnyMissionPermission = hasAnyPermission(
                Permission.CREER_MISSION,
                Permission.PLANIFIER_MISSION,
                Permission.DEMARRER_MISSION,
                Permission.CLOTURER_MISSION,
                Permission.ANNULER_MISSION,
                Permission.ANNULER_MISSION_JUSTIFICATION,
                Permission.SAISIR_DEPENSES_MISSION,
                Permission.VALIDER_DEPENSES_MISSION);

        menuMissions.setVisible(hasAnyMissionPermission);
    }

    /**
     * Configure le menu Entretien en fonction des permissions.
     */
    private void configureEntretienMenu() {
        boolean hasAnyEntretienPermission = hasAnyPermission(
                Permission.PLANIFIER_ENTRETIEN_PREVENTIF,
                Permission.SUIVI_ENTRETIEN_TOUS,
                Permission.SUIVI_ENTRETIEN_PERSONNEL,
                Permission.SAISIR_INTERVENTION,
                Permission.CLOTURER_INTERVENTION,
                Permission.GERER_PRESTATAIRES);

        menuEntretien.setVisible(hasAnyEntretienPermission);
    }

    /**
     * Configure le menu Finances en fonction des permissions.
     */
    private void configureFinancesMenu() {
        boolean hasAnyFinancePermission = hasAnyPermission(
                Permission.CREER_COMPTE_SOCIETAIRE,
                Permission.CONSULTER_COMPTES_TOUS,
                Permission.CONSULTER_COMPTES_PERSONNEL,
                Permission.DEPOT_VALIDATION,
                Permission.DEPOT_DEMANDE,
                Permission.RETRAIT_VALIDATION,
                Permission.RETRAIT_DEMANDE,
                Permission.GERER_MENSUALITES,
                Permission.CONSULTER_MENSUALITES,
                Permission.CALCULER_AMORTISSEMENT);

        menuFinances.setVisible(hasAnyFinancePermission);
    }

    /**
     * Configure le menu Documents en fonction des permissions.
     */
    private void configureDocumentsMenu() {
        boolean hasAnyDocumentPermission = hasAnyPermission(
                Permission.UPLOAD_DOCUMENTS_TOUS,
                Permission.UPLOAD_DOCUMENTS_TECHNIQUE,
                Permission.UPLOAD_DOCUMENTS_PERSONNEL,
                Permission.CONSULTER_DOCUMENTS_TOUS,
                Permission.CONSULTER_DOCUMENTS_TECHNIQUE,
                Permission.CONSULTER_DOCUMENTS_PERSONNEL,
                Permission.VALIDER_DOCUMENTS,
                Permission.SUPPRIMER_DOCUMENTS);

        menuDocuments.setVisible(hasAnyDocumentPermission);
    }

    /**
     * Configure le menu Rapports en fonction des permissions.
     */
    private void configureRapportsMenu() {
        boolean hasAnyReportPermission = hasAnyPermission(
                Permission.TABLEAU_BORD_COMPLET,
                Permission.TABLEAU_BORD_OPERATIONNEL,
                Permission.TABLEAU_BORD_PERSONNEL,
                Permission.TABLEAU_BORD_TECHNIQUE,
                Permission.INVENTAIRE_PARC,
                Permission.STATISTIQUES_UTILISATION,
                Permission.STATISTIQUES_UTILISATION_LIMITE,
                Permission.ANALYSE_COUTS,
                Permission.ETATS_FINANCIERS,
                Permission.ETATS_FINANCIERS_PERSONNEL,
                Permission.RAPPORTS_PERSONNALISES,
                Permission.EXPORT_DONNEES,
                Permission.EXPORT_DONNEES_LIMITE,
                Permission.EXPORT_DONNEES_PERSONNEL,
                Permission.EXPORT_DONNEES_SYSTEM);

        menuRapports.setVisible(hasAnyReportPermission);
    }

    /**
     * Configure le menu Administration en fonction des permissions.
     */
    private void configureAdministrationMenu() {
        boolean hasAnyAdminPermission = hasAnyPermission(
                Permission.CREER_UTILISATEURS,
                Permission.GERER_DROITS_ACCES,
                Permission.PARAMETRAGE_FONCTIONNEL,
                Permission.PARAMETRAGE_TECHNIQUE,
                Permission.SAUVEGARDE_RESTAURATION,
                Permission.LOGS_AUDIT_LECTURE,
                Permission.LOGS_AUDIT_COMPLET,
                Permission.MAINTENANCE_BDD);

        menuAdministration.setVisible(hasAnyAdminPermission);
    }

    /**
     * Configure les panneaux de statistiques en fonction des permissions.
     */
    private void configureStatsPanels() {
        // Définir la visibilité des panneaux de statistiques selon le profil
        // utilisateur
        UtilisateurDao.Role role = getCurrentUser().getRole();

        // Vérifier si les éléments UI sont correctement chargés
        if (gridStatsVehicules == null || gridStatsMissions == null ||
                gridStatsFinances == null || gridStatsEntretien == null) {
            LOGGER.severe("Un ou plusieurs éléments GridPane sont null dans configureStatsPanels");
            return;
        }

        // Vérifier si paneAlertes est disponible
        boolean paneAlertesValid = (paneAlertes != null);
        if (!paneAlertesValid) {
            LOGGER.warning("Le panneau d'alertes n'est pas disponible dans le FXML");
        }

        switch (role) {
            case U1: // Responsable Logistique - accès complet
                gridStatsVehicules.setVisible(true);
                gridStatsMissions.setVisible(true);
                gridStatsFinances.setVisible(true);
                gridStatsEntretien.setVisible(true);
                if (paneAlertesValid)
                    paneAlertes.setVisible(true);
                break;

            case U2: // Agent Logistique - accès aux statistiques opérationnelles
                gridStatsVehicules.setVisible(true);
                gridStatsMissions.setVisible(true);
                gridStatsFinances.setVisible(false);
                gridStatsEntretien.setVisible(true);
                if (paneAlertesValid)
                    paneAlertes.setVisible(true);
                break;

            case U3: // Sociétaire - accès limité à ses véhicules et finances
                gridStatsVehicules.setVisible(hasPermission(Permission.CONSULTER_VEHICULES_PERSONNEL));
                gridStatsMissions.setVisible(false);
                gridStatsFinances.setVisible(hasPermission(Permission.CONSULTER_COMPTES_PERSONNEL));
                gridStatsEntretien.setVisible(hasPermission(Permission.SUIVI_ENTRETIEN_PERSONNEL));
                if (paneAlertesValid)
                    paneAlertes.setVisible(hasPermission(Permission.ALERTES_PERSONNEL));
                break;

            case U4: // Administrateur Système - accès aux statistiques techniques
                gridStatsVehicules.setVisible(hasPermission(Permission.CONSULTER_VEHICULES_LECTURE));
                gridStatsMissions.setVisible(false);
                gridStatsFinances.setVisible(false);
                gridStatsEntretien.setVisible(false);
                if (paneAlertesValid)
                    paneAlertes.setVisible(hasPermission(Permission.ALERTES_SYSTEME));
                break;

            default:
                // Par défaut, masquer tous les panneaux
                gridStatsVehicules.setVisible(false);
                gridStatsMissions.setVisible(false);
                gridStatsFinances.setVisible(false);
                gridStatsEntretien.setVisible(false);
                if (paneAlertesValid)
                    paneAlertes.setVisible(false);
                break;
        }
    }

    /**
     * Gère le clic sur le bouton de déconnexion.
     * 
     * @param event L'événement de clic
     */
    @FXML
    private void handleDeconnexion(ActionEvent event) {
        // Confirmation avant déconnexion
        if (showConfirmationAlert("Déconnexion", "Êtes-vous sûr de vouloir vous déconnecter ?")) {
            logout(btnDeconnexion);
        }
    }

    /**
     * Gère le clic sur l'élément de menu pour consulter les véhicules.
     * 
     * @param event L'événement de clic
     */
    @FXML
    private void handleConsulterVehicules(ActionEvent event) {
        navigateTo(menuVehicules, "/fxml/vehicule_form.fxml", "Gestion de Parc Automobile - Véhicules");
    }

    /**
     * Gère le clic sur l'élément de menu pour ajouter un véhicule.
     * 
     * @param event L'événement de clic
     */
    @FXML
    private void handleAjouterVehicule(ActionEvent event) {
        navigateTo(menuVehicules, "/fxml/vehicule_form.fxml", "Gestion de Parc Automobile - Ajouter un véhicule");
    }

    /**
     * Gère le clic sur l'élément de menu pour gérer les missions.
     * 
     * @param event L'événement de clic
     */
    @FXML
    private void handleGererMissions(ActionEvent event) {
        navigateTo(menuMissions, "/fxml/mission_planner.fxml", "Gestion de Parc Automobile - Missions");
    }

    /**
     * Gère le clic sur l'élément de menu pour gérer les entretiens.
     * 
     * @param event L'événement de clic
     */
    @FXML
    private void handleGererEntretiens(ActionEvent event) {
        navigateTo(menuEntretien, "/fxml/entretien_view.fxml", "Gestion de Parc Automobile - Entretiens");
    }

    /**
     * Gère le clic sur l'élément de menu pour gérer les finances.
     * 
     * @param event L'événement de clic
     */
    @FXML
    private void handleGererFinances(ActionEvent event) {
        navigateTo(menuFinances, "/fxml/finance_view.fxml", "Gestion de Parc Automobile - Finances");
    }

    /**
     * Gère le clic sur l'élément de menu pour gérer les documents.
     * 
     * @param event L'événement de clic
     */
    @FXML
    private void handleGererDocuments(ActionEvent event) {
        navigateTo(menuDocuments, "/fxml/document_view.fxml", "Gestion de Parc Automobile - Documents");
    }

    /**
     * Gère le clic sur l'élément de menu pour accéder aux rapports.
     * 
     * @param event L'événement de clic
     */
    @FXML
    private void handleRapports(ActionEvent event) {
        navigateTo(menuRapports, "/fxml/report_view.fxml", "Gestion de Parc Automobile - Rapports");
    }

    /**
     * Gère le clic sur l'élément de menu pour accéder à l'administration.
     * 
     * @param event L'événement de clic
     */
    @FXML
    private void handleAdministration(ActionEvent event) {
        showInfoAlert("Information", "Le module d'administration est en cours de développement.");
    }
}