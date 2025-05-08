package com.miage.parcauto.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import com.miage.parcauto.dao.UtilisateurDao;
import com.miage.parcauto.util.Permission;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * Contrôleur pour la vue du tableau de bord.
 * Affiche différentes informations et options en fonction du rôle de l'utilisateur connecté.
 * 
 * @author MIAGE Holding
 * @version 1.0
 */
public class DashboardController extends BaseController implements Initializable {
    
    private static final Logger LOGGER = Logger.getLogger(DashboardController.class.getName());
    
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
    
    /**
     * Initialise le contrôleur.
     * 
     * @param url URL de localisation
     * @param rb ResourceBundle
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
    private void updateUserInfo() {
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
        // Définir la visibilité des panneaux de statistiques selon le profil utilisateur
        UtilisateurDao.Role role = getCurrentUser().getRole();
        
        switch (role) {
            case U1: // Responsable Logistique - accès complet
                gridStatsVehicules.setVisible(true);
                gridStatsMissions.setVisible(true);
                gridStatsFinances.setVisible(true);
                gridStatsEntretien.setVisible(true);
                paneAlertes.setVisible(true);
                break;
                
            case U2: // Agent Logistique - accès aux statistiques opérationnelles
                gridStatsVehicules.setVisible(true);
                gridStatsMissions.setVisible(true);
                gridStatsFinances.setVisible(false);
                gridStatsEntretien.setVisible(true);
                paneAlertes.setVisible(true);
                break;
                
            case U3: // Sociétaire - accès limité à ses véhicules et finances
                gridStatsVehicules.setVisible(hasPermission(Permission.CONSULTER_VEHICULES_PERSONNEL));
                gridStatsMissions.setVisible(false);
                gridStatsFinances.setVisible(hasPermission(Permission.CONSULTER_COMPTES_PERSONNEL));
                gridStatsEntretien.setVisible(hasPermission(Permission.SUIVI_ENTRETIEN_PERSONNEL));
                paneAlertes.setVisible(hasPermission(Permission.ALERTES_PERSONNEL));
                break;
                
            case U4: // Administrateur Système - accès aux statistiques techniques
                gridStatsVehicules.setVisible(hasPermission(Permission.CONSULTER_VEHICULES_LECTURE));
                gridStatsMissions.setVisible(false);
                gridStatsFinances.setVisible(false);
                gridStatsEntretien.setVisible(false);
                paneAlertes.setVisible(hasPermission(Permission.ALERTES_SYSTEME));
                break;
                
            default:
                // Par défaut, masquer tous les panneaux
                gridStatsVehicules.setVisible(false);
                gridStatsMissions.setVisible(false);
                gridStatsFinances.setVisible(false);
                gridStatsEntretien.setVisible(false);
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
        // Le module d'administration n'est pas encore implémenté
        showInfoAlert("Information", "Le module d'administration est en cours de développement.");
    }
}