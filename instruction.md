<!-- Ce fichier contient les instructions détaillées pour la refonte et l'amélioration du projet ParcAuto, y compris toutes les exigences de connexion FXML, les besoins de visualisation avancée, la sophistication de l'UI, la cohérence et l'arborescence cible. -->

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

## 6. ARBORESCENCE CIBLE DU PROJET

```
ParcAuto/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/miage/parcauto/
│   │   │       ├── MainApp.java
│   │   │       ├── controller/
│   │   │       │   ├── LoginController.java
│   │   │       │   ├── DashboardController.java
│   │   │       │   ├── VehiculeController.java
│   │   │       │   ├── EntretienController.java
│   │   │       │   ├── MissionController.java
│   │   │       │   ├── FinanceController.java
│   │   │       │   ├── ReportController.java
│   │   │       │   └── ...
│   │   │       ├── service/
│   │   │       │   ├── VehiculeService.java
│   │   │       │   ├── EntretienService.java
│   │   │       │   ├── MissionService.java
│   │   │       │   ├── ReportingService.java
│   │   │       │   ├── ValidationService.java
│   │   │       │   ├── NotificationService.java
│   │   │       │   ├── SecurityService.java
│   │   │       │   └── ...
│   │   │       ├── repository/
│   │   │       │   ├── Repository.java
│   │   │       │   ├── VehiculeRepository.java
│   │   │       │   ├── EntretienRepository.java
│   │   │       │   ├── MissionRepository.java
│   │   │       │   ├── FinanceRepository.java
│   │   │       │   └── ...
│   │   │       ├── model/
│   │   │       │   ├── Vehicule.java
│   │   │       │   ├── Entretien.java
│   │   │       │   ├── Mission.java
│   │   │       │   ├── ...
│   │   │       ├── dto/
│   │   │       │   ├── VehiculeDTO.java
│   │   │       │   ├── EntretienDTO.java
│   │   │       │   ├── MissionDTO.java
│   │   │       │   ├── ...
│   │   │       ├── viewmodel/
│   │   │       │   ├── VehiculeViewModel.java
│   │   │       │   ├── EntretienViewModel.java
│   │   │       │   ├── MissionViewModel.java
│   │   │       │   └── ...
│   │   │       ├── util/
│   │   │       │   ├── ErrorHandler.java
│   │   │       │   ├── ParcAutoException.java
│   │   │       │   ├── SecurityManager.java
│   │   │       │   ├── SessionManager.java
│   │   │       │   └── ...
│   │   └── resources/
│   │       ├── fxml/
│   │       │   ├── login.fxml
│   │       │   ├── dashboard.fxml
│   │       │   ├── vehicule_list.fxml
│   │       │   ├── vehicule_detail.fxml
│   │       │   ├── vehicule_form.fxml
│   │       │   ├── entretien_calendar.fxml
│   │       │   ├── entretien_form.fxml
│   │       │   ├── mission_planner.fxml
│   │       │   ├── mission_form.fxml
│   │       │   ├── finance_dashboard.fxml
│   │       │   ├── report_generator.fxml
│   │       │   └── ...
│   │       ├── css/
│   │       │   ├── theme-default.css
│   │       │   ├── theme-dark.css
│   │       │   ├── theme-contrast.css
│   │       │   ├── dashboard.css
│   │       │   ├── vehicules.css
│   │       │   ├── missions.css
│   │       │   ├── finances.css
│   │       │   ├── reports.css
│   │       │   └── ...
│   │       ├── images/
│   │       │   ├── logo-parcauto.png
│   │       │   └── ...
│   │       ├── js/
│   │       │   ├── dashboard-analytics.js
│   │       │   ├── vehicule-map.js
│   │       │   └── ...
│   │       └── ...
├── db/
│   └── ParcAuto.sql
├── docker-compose.yml
├── Dockerfile
├── README.md
├── instruction.md
└── ...
```

## 7. PROMPTS DE RÉALISATION

- Refactoriser l'architecture en couches strictes, chaque page FXML doit être connectée à son contrôleur et ViewModel.
- Intégrer des graphiques analytiques et des cartes de localisation (simulation) dans les vues FXML concernées.
- Créer des CSS modernes, élégants, accessibles et responsives pour chaque domaine.
- Garantir la cohérence totale entre modèles, services, contrôleurs, DTOs, ViewModels, FXML et CSS.
- Documenter chaque classe et méthode en français (Javadoc).
- Prioriser la robustesse, la performance, l'expérience utilisateur et la maintenabilité.
