# Instructions d'amélioration du projet ParcAuto 🚗

## 📋 OBJECTIF GLOBAL

Moderniser et refactoriser intégralement le projet **ParcAuto** selon les recommandations de l'audit de code tout en respectant les contraintes techniques existantes. Le code doit être entièrement compilable, documenté (Javadoc), et rédigé **en français**.

## 1. STACK TECHNIQUE IMPOSÉE

| Domaine            | Choix technologique                                                                      |
|--------------------|------------------------------------------------------------------------------------------|
| Langage            | **Java 17** (backend + UI)                                                               |
| Interface          | **JavaFX 17** avec fichiers FXML                                                         |
| Styles             | Feuilles CSS modernes (importées via `scene.getStylesheets().add(...)`)                  |
| Persistance        | **MySQL 8** avec connexion JDBC native et HikariCP                                       |
| Conteneurisation   | `docker-compose.yml` avec services **mysql** + **phpmyadmin** + **app**                  |
| Sécurité           | Système de hachage personnalisé avec salage                                              |
| CI/CD              | GitHub Actions (compile → tests → image Docker)                                          |
| Interdits          | Pas de frameworks Spring, pas de format JSON, pas d'API REST                             |

## 2. ARCHITECTURE CIBLE ET FLUX DE DONNÉES

### 2.1 Architecture en couches strictes

```
+-----------------+        +-------------------+        +-----------------+        +-------------+
| Couche Vue      |        | Couche Contrôleur |        | Couche Service  |        | Couche DAO  |
| JavaFX + FXML   | -----> | Controllers       | -----> | Services        | -----> | Repository  |
+-----------------+        +-------------------+        +-----------------+        +-------------+
       |                           |                           |                         |
       v                           v                           v                         v
+----------------+       +-------------------+       +-------------------+      +---------------+
| CSS, Resources |       | ViewModels, DTOs  |       | Entités métier    |      | MySQL         |
+----------------+       +-------------------+       +-------------------+      +---------------+
```

### 2.2 Flux de données complet

1. **Authentification et sessions**
   ```
   LoginController → UtilisateurService → UtilisateurRepository → DB
                  ↓
   SecurityManager (validation mot de passe)
                  ↓
   SessionManager (création token + permissions)
                  ↓
   Navigation vers Dashboard (interface adaptée au rôle)
   ```

2. **Gestion des véhicules**
   ```
   VehiculeController → VehiculeService → VehiculeRepository → DB
                     ↓                  ↑
   Validation (DTO) → ValidationService -
                     ↓
   TableView/DetailView (affichage utilisateur)
   ```

3. **Missions et planification**
   ```
   MissionController → MissionService → MissionRepository → DB
                    ↓                ↑                     ↑
   VehiculesDisponibles → VehiculeService → VehiculeRepository
                    ↓
   Planning (affichage timeline)
   ```

4. **Finances et reporting**
   ```
   ReportController → ReportingService → Multiple DAOs → DB
                   ↓                   ↑
   Génération PDF → FormattingService -
                   ↓
   Affichage des graphiques/rapports
   ```

5. **Système de notification**
   ```
   NotificationService → NotificationRepository → DB
          ↑           ↓
   Déclencheurs → AlertService → Affichage notification
   ```

## 3. CLASSES ET MÉTHODES À DÉFINIR

### 3.1 Couche Repository

**Interface Repository<T, ID>**
- `Optional<T> findById(ID id)`
- `List<T> findAll()`
- `List<T> findAll(int page, int size)`
- `T save(T entity)`
- `T update(T entity)`
- `boolean delete(ID id)`
- `long count()`

**Implémentations spécifiques**
- `VehiculeRepository`
  - `List<Vehicule> findByEtat(EtatVoiture etat)`
  - `List<Vehicule> findRequiringMaintenance(int kmThreshold)`

- `EntretienRepository`
  - `List<Entretien> findByVehicule(int idVehicule)`
  - `List<Entretien> findScheduledBetween(LocalDate debut, LocalDate fin)`

- `MissionRepository`
  - `List<Mission> findActiveForVehicule(int idVehicule)`
  - `List<Mission> findByPeriod(LocalDate debut, LocalDate fin)`

- `FinanceRepository`
  - `BilanFinancier getBilanPeriode(LocalDate debut, LocalDate fin)`
  - `List<CoutEntretien> getCoutEntretienParVehicule(int annee)`

### 3.2 Couche Service

**Services métier**
- `VehiculeService`
  - `List<VehiculeDTO> getAllVehicules()`
  - `Optional<VehiculeDTO> getVehiculeById(Integer id)`
  - `boolean createVehicule(VehiculeDTO vehicule)`
  - `boolean updateVehicule(VehiculeDTO vehicule)`
  - `boolean updateKilometrage(Integer id, int nouveauKm)`
  - `boolean deleteVehicule(Integer id)`
  - `List<VehiculeDTO> getVehiculesDisponibles()`

- `EntretienService`
  - `List<EntretienDTO> getAllEntretiens()`
  - `Optional<EntretienDTO> getEntretienById(Integer id)`
  - `boolean createEntretien(EntretienDTO entretien)`
  - `boolean updateEntretien(EntretienDTO entretien)`
  - `boolean terminerEntretien(Integer id, String observations)`
  - `List<EntretienDTO> getEntretiensVehicule(Integer idVehicule)`

- `MissionService`
  - `List<MissionDTO> getAllMissions()`
  - `Optional<MissionDTO> getMissionById(Integer id)`
  - `boolean createMission(MissionDTO mission)`
  - `boolean updateMission(MissionDTO mission)`
  - `boolean terminerMission(Integer id, int nouveauKm)`
  - `boolean ajouterDepense(Integer idMission, DepenseDTO depense)`

- `ReportingService`
  - `RapportVehiculeDTO genererRapportVehicule(Integer idVehicule)`
  - `BilanFlotteDTO genererBilanFlotte()`
  - `TcoVehiculeDTO calculerTCO(Integer idVehicule)`
  - `byte[] exporterRapportPDF(RapportDTO rapport)`
  - `byte[] exporterRapportExcel(RapportDTO rapport)`

**Services transversaux**
- `ValidationService`
  - `ValidationResult validate(Object entity)`
  - `ValidationResult validateVehicule(VehiculeDTO vehicule)`
  - `ValidationResult validateEntretien(EntretienDTO entretien)`
  - `ValidationResult validateMission(MissionDTO mission)`

- `NotificationService`
  - `void envoyerNotification(NotificationDTO notification)`
  - `List<NotificationDTO> getNotificationsNonLues(Integer idUtilisateur)`
  - `void marquerCommeVue(Integer idNotification)`

- `SecurityService`
  - `Optional<UtilisateurDTO> authenticate(String login, String password)`
  - `Set<Permission> getUserPermissions(Integer idUtilisateur)`
  - `String generateSecureToken(UtilisateurDTO utilisateur)`
  - `boolean validateToken(String token)`

### 3.3 Modèles et DTOs

**Entités métier**
- `Vehicule`
- `Entretien`
- `Mission`
- `DepenseMission`
- `SocieteCompte`
- `Mouvement`
- `Utilisateur`
- `Personnel`
- `Societaire`

**DTOs (transfert de données)**
- `VehiculeDTO`
- `EntretienDTO`
- `MissionDTO`
- `DepenseDTO`
- `UtilisateurDTO`
- `NotificationDTO`
- `RapportDTO`
- `TcoVehiculeDTO`
- `BilanFlotteDTO`

**ViewModels (présentation)**
- `VehiculeViewModel`
  - `String getStatutClass()` (style CSS)
  - `String getKilometrageFormate()`

- `EntretienViewModel`
  - `String getColorStyle()`
  - `String getDureeFormatee()`

- `MissionViewModel`
  - `String getStatusBadgeClass()`
  - `String getDestinationFormatee()`

### 3.4 Contrôleurs JavaFX

**Contrôleurs principaux**
- `LoginController`
- `DashboardController`
- `VehiculeController`
- `EntretienController`
- `MissionController`
- `FinanceController`
- `ReportController`

**Fonctionnalités par contrôleur**
- `VehiculeController`
  - Vue liste avec filtrage/tri
  - Formulaire ajout/modification
  - Visualisation détaillée
  - Mise à jour kilométrage
  - Planification entretiens

- `EntretienController`
  - Calendrier des entretiens
  - Formulaire programmation entretien
  - Suivi des pièces détachées
  - Clôture d'entretien

- `MissionController`
  - Planning des missions
  - Assignation véhicule/conducteur
  - Enregistrement dépenses
  - Validation retour mission

- `FinanceController`
  - Tableau de bord financier
  - Enregistrement mouvements
  - Calcul TCO par véhicule
  - Gestion des budgets

### 3.5 Utilitaires et support

**Gestion des erreurs**
- `ParcAutoException` (classe racine)
  - `VehiculeNotFoundException`
  - `EntretienNotFoundException`
  - `MissionNotFoundException`
  - `ValidationException`
  - `AuthenticationException`
  - `DatabaseException`

- `ErrorHandler`
  - `void handleException(Throwable ex, String title)`
  - `void displayError(String message, String details)`

**Sécurité et sessions**
- `SecurityManager`
  - `boolean hasPermission(Permission permission)`
  - `boolean hasAnyPermission(Set<Permission> permissions)`
  - `boolean hasAllPermissions(Set<Permission> permissions)`

- `SessionManager`
  - `void ouvrirSession(Utilisateur user, String token)`
  - `void fermerSession()`
  - `boolean isSessionExpired()`
  - `void refreshSession()`

**Composants réutilisables**
- `PageableTableView<T>`
  - `void setItems(List<T> items, int totalItems, int currentPage)`
  - `void goToPage(int page)`

- `FilterableComboBox<T>`
  - `void setFilterConverter(StringConverter<T> converter)`
  - `void setItems(ObservableList<T> items)`

- `NotificationComponent`
  - `void showNotification(String title, String message)`
  - `int getUnreadCount()`

## 4. FICHIERS FXML ET INTERFACES UTILISATEUR

### 4.1 Structure des vues FXML

- `login.fxml` - Écran de connexion
- `dashboard.fxml` - Tableau de bord principal (navigation)
- `vehicule_list.fxml` - Liste des véhicules avec filtres
- `vehicule_detail.fxml` - Détails d'un véhicule
- `vehicule_form.fxml` - Formulaire ajout/édition véhicule
- `entretien_calendar.fxml` - Calendrier des entretiens
- `entretien_form.fxml` - Formulaire d'entretien
- `mission_planner.fxml` - Planning des missions
- `mission_form.fxml` - Formulaire de mission
- `finance_dashboard.fxml` - Tableau de bord financier
- `report_generator.fxml` - Générateur de rapports

### 4.2 Composants d'interface communs

- Barre de navigation latérale (sidebar)
- En-tête avec notifications et profil utilisateur
- Système de dialogue modal
- Composants de filtrage avancé
- Système de pagination pour tableaux
- Bannière d'alerte configurable
- Panneaux pliables/dépliables
- Thèmes sélectionnables (clair/sombre/contraste)

## 5. STRUCTURE DE LA BASE DE DONNÉES

### 5.1 Tables principales
- `VEHICULE` - Inventaire des véhicules
- `ENTRETIEN` - Historique et planification des entretiens
- `MISSION` - Missions et déplacements
- `DEPENSE_MISSION` - Dépenses liées aux missions
- `MOUVEMENT` - Mouvements financiers
- `SOCIETE_COMPTE` - Comptes et budgets
- `UTILISATEUR` - Comptes utilisateurs
- `PERSONNEL` - Employés de l'entreprise
- `SOCIETAIRE` - Conducteurs autorisés
- `ROLE` - Rôles utilisateurs
- `PERMISSION` - Permissions système
- `NOTIFICATION` - Système de notifications

### 5.2 Vues SQL optimisées
- `VEHICULE_ENTRETIEN` - Véhicules avec leur prochain entretien
- `VEHICULE_MISSION` - Véhicules avec leur mission en cours
- `MISSION_DETAIL` - Missions avec conducteur et véhicule
- `COUT_VEHICULE` - Coût par véhicule (TCO)
- `SYNTHESE_MENSUELLE` - Synthèse financière mensuelle

## 6. FLUX DE TRAVAIL POUR L'IMPLÉMENTATION

### 6.1 Ordre de refactorisation
1. Couche Repository et entités
2. Services métier et validation
3. DTOs et mappers
4. Système de gestion des exceptions
5. Contrôleurs et ViewModels
6. Interface utilisateur et FXML
7. Système de notifications
8. Export de rapports
9. Sécurité et permissions

### 6.2 Priorité des fonctionnalités
1. Correction de l'architecture (repository pattern)
2. Séparation métier/présentation (DTOs)
3. Gestion centralisée des exceptions
4. Optimisation des requêtes SQL
5. Système de validation robuste
6. Tasks JavaFX asynchrones
7. Pagination des données volumineuses
8. Système de notification pour les entretiens planifiés
9. Calcul TCO détaillé
10. Export des rapports en PDF/Excel

## 7. PROMPTS DE RÉALISATION

### Prompt 1: Refactorisation de l'architecture
"Implémenter le pattern Repository dans ParcAuto en créant l'interface Repository<T,ID> et ses implémentations concrètes. Modifier les services pour utiliser ces repositories au lieu des DAOs directs."

### Prompt 2: Création des DTOs et ViewModels
"Créer les classes DTO pour découpler la couche présentation des entités métier. Implémenter les ViewModels avec les méthodes de formatage et style pour l'UI."

### Prompt 3: Système d'exceptions personnalisées
"Créer une hiérarchie d'exceptions spécifiques à ParcAuto et implémenter un ErrorHandler global pour la gestion centralisée des erreurs."

### Prompt 4: Optimisation des performances SQL
"Optimiser les requêtes SQL dans toute l'application, en particulier dans ReportingService, et implémenter un système de pagination pour les résultats volumineux."

### Prompt 5: Système de validation centralisé
"Implémenter un service de validation centralisé avec des méthodes spécifiques pour chaque entité, avec des messages d'erreur explicites."

### Prompt 6: Tasks JavaFX pour opérations longues
"Refactoriser les contrôleurs JavaFX pour exécuter les opérations longues dans des Tasks asynchrones afin d'éviter le gel de l'interface."

### Prompt 7: Système de notification
"Développer un système de notification pour les événements importants (entretiens planifiés, missions à venir) avec affichage dans l'interface."

### Prompt 8: Export des rapports
"Implémenter l'export des rapports au format PDF et Excel, avec mise en forme professionnelle (en-têtes, pieds de page, formatage)."

### Prompt 9: Renforcement de la sécurité
"Améliorer la sécurité de l'application en renforçant le SessionManager avec timeout, token signé et vérification systématique des permissions."

**Important**: Garantir la cohérence totale entre les modèles, services, controllers et fichiers FXML. Toutes les modifications doivent être documentées avec Javadoc en français et respecter les contraintes techniques imposées.
- Intégrer des graphiques analytiques et des cartes de localisation (simulation) dans les vues FXML concernées.
- Créer des CSS modernes, élégants, accessibles et responsives pour chaque domaine.

Moderniser et refactoriser intégralement le projet **ParcAuto** selon les recommandations de l'audit de code tout en respectant les contraintes techniques existantes. Le code doit être entièrement compilable, documenté (Javadoc), et rédigé **en français**.

## 1. STACK TECHNIQUE IMPOSÉE

| Domaine            | Choix technologique                                                                      |
|--------------------|------------------------------------------------------------------------------------------|
| Langage            | **Java 17** (backend + UI)                                                               |
| Interface          | **JavaFX 17** avec fichiers FXML                                                         |
| Styles             | Feuilles CSS modernes (importées via `scene.getStylesheets().add(...)`)                  |
| Persistance        | **MySQL 8** avec connexion JDBC native et HikariCP                                       |
| Conteneurisation   | `docker-compose.yml` avec services **mysql** + **phpmyadmin** + **app**                  |
| Sécurité           | Système de hachage personnalisé avec salage                                              |
| CI/CD              | GitHub Actions (compile → tests → image Docker)                                          |
| Interdits          | Pas de frameworks Spring, pas de format JSON, pas d'API REST                             |

## 2. ARCHITECTURE CIBLE ET FLUX DE DONNÉES

### 2.1 Architecture en couches strictes

```
+-----------------+        +-------------------+        +-----------------+        +-------------+
| Couche Vue      |        | Couche Contrôleur |        | Couche Service  |        | Couche DAO  |
| JavaFX + FXML   | -----> | Controllers       | -----> | Services        | -----> | Repository  |
+-----------------+        +-------------------+        +-----------------+        +-------------+
       |                           |                           |                         |
       v                           v                           v                         v
+----------------+       +-------------------+       +-------------------+      +---------------+
| CSS, Resources |       | ViewModels, DTOs  |       | Entités métier    |      | MySQL         |
+----------------+       +-------------------+       +-------------------+      +---------------+
```

### 2.2 Flux de données complet

1. **Authentification et sessions**
   - Toutes les pages FXML sont connectées à leur contrôleur respectif.
   - Navigation dynamique selon le rôle utilisateur.
2. **Gestion des véhicules**
   - Visualisation avancée (tableaux, graphiques d'état, carte de localisation simulée des véhicules).
   - Toutes les opérations CRUD sont synchronisées avec la base et l'UI.
3. **Missions et planification**
   - Timeline graphique, carte de missions, statistiques d'affectation.
4. **Finances et reporting**
   - Graphiques analytiques (TCO, coûts, synthèses), export PDF/Excel.
5. **Notifications**
   - Système d'alertes visuelles, badge de notifications, affichage contextuel.

**Chaque vue FXML doit :**
- Être reliée à son contrôleur JavaFX.
- Utiliser des ViewModels pour l'affichage formaté.
- Intégrer des composants graphiques avancés (graphiques, cartes, timelines).
- Être stylée avec un CSS moderne, élégant, responsive et accessible (support clair/sombre/contraste, animations, transitions, icônes SVG, etc.).
- Garantir la cohérence visuelle et fonctionnelle sur toutes les pages.

## 3. CLASSES, MÉTHODES ET DÉCLARATIONS À DÉFINIR

- **Repository<T, ID>** (et toutes les implémentations spécifiques)
- **Services métier** (VehiculeService, EntretienService, MissionService, ReportingService...)
- **Services transversaux** (ValidationService, NotificationService, SecurityService...)
- **DTOs** (VehiculeDTO, EntretienDTO, MissionDTO, etc.)
- **ViewModels** (VehiculeViewModel, EntretienViewModel, MissionViewModel...)
- **Contrôleurs JavaFX** (un par vue FXML)
- **Gestion des erreurs** (ParcAutoException, ErrorHandler...)
- **Sécurité et sessions** (SecurityManager, SessionManager...)
- **Composants réutilisables** (PageableTableView, FilterableComboBox, NotificationComponent...)
- **Tous les fichiers FXML listés** (voir ci-dessous)
- **Feuilles CSS modernes** (voir ci-dessous)

## 4. FICHIERS FXML ET INTERFACES UTILISATEUR

- `login.fxml` : Connexion (avec animation, fond dynamique, feedback visuel)
- `dashboard.fxml` : Tableau de bord (graphiques analytiques, synthèses, notifications, accès rapide)
- `vehicule_list.fxml` : Liste véhicules (tableau, filtres, graphique d'état, carte de localisation)
- `vehicule_detail.fxml` : Détail véhicule (historique, entretiens, graphique d'utilisation, carte)
- `vehicule_form.fxml` : Formulaire véhicule (ajout/édition, validation visuelle)
- `entretien_calendar.fxml` : Calendrier entretiens (vue calendrier, alertes, graphique de répartition)
- `entretien_form.fxml` : Formulaire entretien (programmation, pièces, validation)
- `mission_planner.fxml` : Planning missions (timeline, carte, graphique d'affectation)
- `mission_form.fxml` : Formulaire mission (création, validation, dépenses)
- `finance_dashboard.fxml` : Finances (graphiques TCO, coûts, synthèses, export)
- `report_generator.fxml` : Générateur de rapports (sélection, export, visualisation)

**Composants communs :**
- Sidebar/navigation latérale
- Header avec notifications et profil
- Dialogues modaux
- Filtres avancés
- Pagination
- Bannière d'alerte
- Panneaux pliables
- Sélecteur de thème (clair/sombre/contraste)

## 5. CSS ET DESIGN

- Créer des feuilles CSS dédiées par domaine :
  - `theme-default.css`, `theme-dark.css`, `theme-contrast.css`, `dashboard.css`, `vehicules.css`, `missions.css`, `finances.css`, `reports.css`, etc.
- Utiliser des variables CSS, animations, transitions, icônes SVG, responsive design.
- Garantir une expérience utilisateur moderne, professionnelle et accessible.
- Les composants graphiques (graphiques, cartes, timelines) doivent être intégrés et stylés pour une expérience visuelle optimale.