# ParcAuto – Système de Gestion de Parc Automobile

## Table des matières
1. [Présentation du projet](#présentation-du-projet)
2. [Architecture](#architecture)
3. [Fonctionnalités principales](#fonctionnalités-principales)
4. [Modèle de données](#modèle-de-données)
5. [Système de sécurité RBAC](#système-de-sécurité-rbac)
6. [Installation et démarrage](#installation-et-démarrage)
7. [Structure du projet](#structure-du-projet)
8. [Docker et déploiement](#docker-et-déploiement)
9. [Remarques](#remarques)](#modèle-de-données)
5. [Système de sécurité RBAC](#système-de-sécurité-rbac)
6. [Installation et démarrage](#installation-et-démarrage)
7. [Structure du projet](#structure-du-projet)
8. [Docker et déploiement](#docker-et-déploiement)
9. [Remarques](#remarques)

## Présentation du projet

ParcAuto est un système complet de gestion de parc automobile permettant de suivre les véhicules, les missions, les entretiens, les documents et les finances associées au parc de véhicules d'une entreprise. L'application est conçue avec une architecture en couches (DAO, Service, Controller) et utilise JavaFX pour l'interface utilisateur, JDBC pour l'accès aux données MySQL.

### Acteurs du système
- **Responsable Logistique (U1)** : Supervise la flotte, valide les missions, planifie les entretiens et approuve les dépenses
- **Agent Logistique (U2)** : Met à jour le kilométrage, crée les ordres d'entretien, enregistre les mouvements financiers
- **Sociétaire / Conducteur (U3)** : Consulte son planning de mission et l'état de son véhicule affecté
- **Administrateur Système (U4)** : Gère les comptes utilisateurs, la sécurité et la sauvegarde de la base

## Architecture

L'application ParcAuto est structurée selon une architecture en couches traditionnelle :

### Couches de l'application

1. **Couche Présentation** (JavaFX + FXML)
   - Interfaces utilisateur définies en FXML
   - Contrôleurs JavaFX pour la logique de présentation
   - Feuilles de style CSS pour l'apparence

2. **Couche Service** (Logique métier)
   - Services qui encapsulent les opérations métier
   - Point de coordination entre l'interface utilisateur et l'accès aux données
   - Gestion des transactions

3. **Couche DAO** (Accès aux données)
   - Classes DAO pour interagir avec la base de données
   - Requêtes SQL et conversion des résultats en objets
   - Gestion des connexions via DbUtil (pattern Singleton)

4. **Couche Modèle** (Entités métier)
   - POJOs représentant les entités du domaine
   - Structures de données fonctionnelles

5. **Utilitaires transversaux**
   - Gestion de sécurité (SecurityManager)
   - Gestion de session (SessionManager)
   - Classes utilitaires diverses

## Fonctionnalités principales

### Gestion des véhicules
- Ajout, modification, suppression de véhicules
- Suivi de l'état des véhicules (Disponible, En mission, En entretien, Hors service)
- Consultation des caractéristiques techniques et historique d'utilisation
- Déclaration de pannes et changement d'état automatisé
- Suivi kilométrique et historique des mouvements
- Statistiques d'utilisation et disponibilité du parc
- Calcul de rentabilité et coût par kilomètre
- Gestion des affectations (temporaires ou permanentes)

### Gestion des missions
- Planification et attribution des missions
- Suivi des déplacements et des activités
- Enregistrement des dépenses liées aux missions
- Validation des dépenses et justificatifs
- Calcul des coûts estimés et réels
- Gestion du cycle de vie (planification → démarrage → clôture)
- Suivi du kilométrage prévisionnel vs. réel
- Rapports d'activité et statistiques

### Gestion des entretiens
- Planification des entretiens préventifs
- Enregistrement des entretiens correctifs
- Historique complet des interventions
- Alertes pour les entretiens à venir
- Suivi des coûts d'entretien par véhicule
- Planification des visites techniques
- Gestion des fournisseurs et prestataires
- Analyse de la fiabilité des véhicules

### Gestion financière
- Suivi des dépenses par véhicule
- Calcul du TCO (Total Cost of Ownership)
- Gestion des contrats d'assurance
- Rapports financiers détaillés
- Gestion des comptes sociétaires
- Suivi des dépôts et retraits
- Calcul d'amortissement des véhicules
- Analyse de rentabilité et optimisation des coûts

### Gestion des documents
- Upload et stockage des documents (assurance, carte grise, etc.)
- Validation des documents
- Alertes pour les documents à renouveler
- Gestion des échéances (assurances, contrôles techniques)
- Association de documents aux véhicules et sociétaires
- Système de classification et recherche
- Contrôle des accès et confidentialité

### Module d'administration
- Gestion des utilisateurs et des droits
- Paramétrage de l'application (personnel, services, fonctions)
- Sauvegarde/restauration de la base de données
- Surveillance des logs et audit de sécurité
- Configuration des alertes et notifications
- Gestion des profils et rôles
- Maintenance technique

### Tableau de bord et reporting
- Tableau de bord personnalisé selon le rôle utilisateur
- Indicateurs clés de performance (KPI)
- Rapports périodiques (quotidiens, mensuels, annuels)
- Analyse des tendances et prévisions
- Visualisation graphique des données
- Export des données et rapports
- Alertes et notifications personnalisables

### Système de sécurité RBAC
- Authentification des utilisateurs
- Contrôle d'accès basé sur les rôles (permissions)
- Support MFA (authentification à deux facteurs)

Le système de sécurité implémente un modèle RBAC (Role-Based Access Control) complet :

### Rôles principaux
- **Responsable Logistique (U1)** : Accès complet à la gestion des véhicules, missions, entretiens et finances
- **Agent Logistique (U2)** : Accès à la gestion quotidienne (kilométrage, entretiens, missions)
- **Sociétaire (U3)** : Accès limité à ses propres véhicules et missions
- **Administrateur (U4)** : Accès aux fonctionnalités système et paramétrages techniques

### Gestion des permissions
- Permissions définies dans l'énumération `Permission` (230+ permissions)
- Permissions organisées par modules fonctionnels (Véhicules, Missions, Entretiens, etc.)
- `SecurityManager` (singleton) : attribue les permissions aux rôles et vérifie les droits
- `SessionManager` (singleton) : gère l'utilisateur connecté et sa session

### Contrôle d'accès
- Les contrôleurs héritent de `BaseController` qui fournit des méthodes de vérification de droits
- Méthode `checkPermissions()` dans chaque contrôleur pour vérifier les droits spécifiques
- Interface utilisateur adaptée aux permissions (masquage des boutons, menus, etc.)
- Support de l'authentification à deux facteurs (MFA) pour les opérations sensibles

## Modèle de données

Le schéma de base de données comprend les principales tables suivantes :

### Tables principales
- **VEHICULES** : Informations sur les véhicules du parc (immatriculation, marque, modèle, état, etc.)
- **ETAT_VOITURE** : États possibles des véhicules (Disponible, En mission, En entretien, etc.)
- **ENTRETIEN** : Historique des entretiens préventifs et correctifs
- **PERSONNEL** : Informations sur les employés de l'entreprise
- **SERVICE** : Départements organisationnels
- **FONCTION** : Fonctions professionnelles des employés
- **SOCIETAIRE_COMPTE** : Utilisateurs autorisés à conduire les véhicules et leur solde financier
- **MISSION** : Planification et suivi des missions (dates, lieux, kilométrage, etc.)
- **AFFECTATION** : Attribution des véhicules aux conducteurs (temporaire ou permanente)
- **MOUVEMENT** : Mouvements financiers liés au parc (dépôts, retraits, mensualités)
- **ASSURANCE** : Contrats d'assurance pour les véhicules
- **DEPENSE_MISSION** : Dépenses engagées lors des missions
- **DOCUMENT_SOCIETAIRE** : Documents associés aux sociétaires (permis, cartes grises, etc.)
- **UTILISATEUR** : Comptes d'accès au système (authentification et rôles)
- **COUVRIR** : Relation entre véhicules et assurances

### Relations principales
- Un **VEHICULE** est dans un **ETAT_VOITURE** spécifique
- Un **VEHICULE** peut avoir plusieurs **ENTRETIEN**
- Un **VEHICULE** peut être affecté à plusieurs **MISSION**
- Un **PERSONNEL** appartient à un **SERVICE** et a une **FONCTION**
- Un **UTILISATEUR** est lié à un **PERSONNEL**
- Un **SOCIETAIRE_COMPTE** peut être lié à un **PERSONNEL**
- Une **AFFECTATION** associe un **VEHICULE** à un **PERSONNEL** ou **SOCIETAIRE**
- Une **MISSION** génère des **DEPENSE_MISSION**
- Un **VEHICULE** peut être couvert par plusieurs **ASSURANCE** via **COUVRIR**

### Vues et fonctions
- **v_TCO** : Vue pour calculer le TCO (Total Cost of Ownership) par véhicule

### Déclencheurs (Triggers)
- **trg_mission_cloturee** : Met à jour le kilométrage du véhicule lors de la clôture d'une mission

## Installation et démarrage

### Prérequis
- Java 17 ou supérieur
- MySQL 8.x
- Docker et Docker Compose (optionnel, pour le déploiement conteneurisé)

### Option 1 : Exécution locale directe

#### 1. Installation de la base de données
- Installez MySQL 8.x sur votre système
- Créez une base de données nommée `ParcAuto` 
- Exécutez le script `db/ParcAuto.sql` pour créer le schéma

#### 2. Configuration de la connexion
- Modifiez les paramètres de connexion dans `src/main/java/com/miage/parcauto/dao/DbUtil.java` si nécessaire

#### 3. Compilation et exécution (Windows)

1. **Compiler le projet** :
   ```cmd
   build.bat
   ```

2. **Exécuter l'application** :
   ```cmd
   java -cp "lib/*;target/classes" main.java.com.miage.parcauto.MainApp
   ```

3. **Lancer les tests unitaires** :
   ```cmd
   run_tests.bat
   ```

### Option 2 : Déploiement Docker

1. **Construire et démarrer les conteneurs** :
   ```cmd
   docker-compose up -d
   ```

2. **Vérifier l'état des services** :
   ```cmd
   docker-compose ps
   ```

3. **Accéder à l'application** :
   - Interface utilisateur : lancée automatiquement dans le conteneur `app`
   - Interface MySQL : accessible sur le port 3306
   - phpMyAdmin : accessible via http://localhost:8080

4. **Arrêter les services** :
   ```cmd
   docker-compose down
   ```

### Authentification initiale

L'accès à l'application se fait avec les identifiants suivants :
- **Responsable Logistique** : user_u1 / password_u1
- **Agent Logistique** : user_u2 / password_u2
- **Sociétaire** : user_u3 / password_u3
- **Administrateur** : admin / admin123

*Note : Changez ces mots de passe par défaut lors de la première utilisation.*

## Structure du projet

## Structure du projet

```
parcauto/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── miage/
│   │   │           └── parcauto/
│   │   │               ├── controller/    # Contrôleurs JavaFX (UI)
│   │   │               ├── dao/           # Objets d'accès aux données (SQL)
│   │   │               ├── model/         # Entités métier
│   │   │               │   ├── rh/        # Ressources humaines
│   │   │               │   └── vehicule/  # Véhicules et composants
│   │   │               ├── service/       # Couche métier
│   │   │               ├── util/          # Utilitaires
│   │   │               └── MainApp.java   # Point d'entrée
│   │   └── resources/
│   │       ├── css/                       # Styles
│   │       ├── fxml/                      # Définitions d'interface
│   │       ├── images/                    # Ressources graphiques
│   │       └── js/                        # Scripts JavaScript
│   └── test/
│       └── java/                          # Tests unitaires
├── db/
│   └── ParcAuto.sql                       # Script de création du schéma
├── lib/                                   # JAR externes
│   ├── javafx-*.jar                       # Modules JavaFX
│   ├── mysql-connector-j-8.0.33.jar       # Pilote MySQL
│   └── spring-security-crypto-6.4.5.jar   # BCrypt pour MDP
├── target/
│   ├── classes/                           # Classes compilées  
│   └── test-classes/                      # Tests compilés
├── .env                                   # Configuration Docker
├── build.bat                              # Script de compilation Windows
├── docker-compose.yml                     # Configuration Docker Compose
├── Dockerfile                             # Pour construire l'image Docker
└── README.md                              # Documentation
```

## Structure du projet

- `src/main/java` : code source principal (Java)
- `src/main/resources` : ressources (FXML, CSS, images, JS)
- `src/test/java` : tests unitaires (JUnit 5, Mockito)
- `lib/` : dépendances externes (JARs)
- `build.bat` : script de compilation Windows
- `run_tests.bat` : script d'exécution des tests unitaires
- `target/classes` : classes compilées
- `target/test-classes` : classes de test compilées
- `docker-compose.yml` : orchestration MySQL, phpMyAdmin, app
- `db/ParcAuto.sql` : schéma SQL complet

## Compilation et exécution (Windows)

1. **Compiler le projet** :

   ```cmd
   build.bat
   ```

2. **Exécuter l’application** :

   ```cmd
   java -cp "lib/*;target/classes" com.miage.parcauto.main.java.com.miage.parcauto.MainApp
   ```

3. **Lancer les tests unitaires** :

   ```cmd
   run_tests.bat
   ```

## Gestion des dépendances

Placez tous les fichiers JAR nécessaires dans le dossier `lib/` :
- JavaFX 17 (modules nécessaires)
- mysql-connector-j-8.0.33.jar
- spring-security-crypto-6.4.5.jar (pour BCrypt)
- JUnit 5, Mockito, etc.

## Remarques
- Le projet ne dépend plus de Maven.
- Les scripts batch assurent la compilation et l’exécution.
- Adaptez les chemins si vous êtes sous Linux/Mac (scripts non fournis).
- Pour ajouter une dépendance, placez le JAR dans `lib/` et relancez la compilation.

## Docker & Base de données

- Lancer l’écosystème :
  ```cmd
  docker-compose up
  ```
- Accès MySQL : localhost:3306 (root/parcauto_root_pw)
- phpMyAdmin : http://localhost:8081

---

Pour toute question, consultez la documentation interne ou contactez l’équipe ParcAuto.