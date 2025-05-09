# 🎯 OBJECTIF

Agis comme un **ingénieur senior full‑stack** et rédige **tout le code finalisé** (zéro TODO) pour le projet **ParcAuto**.  
Le code doit être entièrement compilable, documenté (Javadoc), testé et rédigé **en français**.

# 1. STACK & CONTRAINTES TECHNIQUES

| Domaine            | Choix imposé                                                                               |
|--------------------|--------------------------------------------------------------------------------------------|
| Langage            | **Java 17** (backend + UI)                                                                 |
| IHM                | **JavaFX 17** avec fichiers FXML<br>• Feuilles de style CSS modernes (Bootstrap 5, Tailwind CSS ou équivalent, importées dans JavaFX)<br>• Possibilité d’injecter du **JavaScript pur (ECMAScript 2020)** via `WebView`/`Nashorn` **sans créer de pages HTML** |
| Persistance        | **MySQL 8**                                                                                |
| Conteneurs         | `docker-compose.yml` : services **mysql** + **phpmyadmin** + (`app` image JAR)             |
| Build              | **Maven**                                                                                  |
| Sécurité           | BCrypt (pas de Spring Boot)                                                                |
| Tests              | JUnit 5, Mockito                                                                           |
| CI/CD              | GitHub Actions (compile → tests → image Docker)                                            |
| API                | **Aucune API** (pas de REST ↔ pas de JSON)                                                 |
| Frameworks interdits | **Pas de Spring Boot**, **pas de JSON**                                                  |


# 2. DIRECTIVES POUR GITHUB COPILOT

- **Aucune dépendance Spring** ; utilisez **JavaFX + JDBC/HikariCP** directement.  
- Les contrôleurs JavaFX communiquent **uniquement** avec les *Services* (pas de couche REST).  
- **Pas de JSON** : préférer `Properties`, objets Java ou SQL direct pour transporter les données.  
- Les scripts SQL (`ParcAuto.sql`) créent toutes les tables, contraintes, déclencheurs et vues.  
- Les vues JavaFX peuvent intégrer **WebView** pour exécuter du JavaScript pur (graphes, animations) ; aucune page HTML externe.  
- Les feuilles de style CSS modernes (Bootstrap / Tailwind) sont chargées via `scene.getStylesheets().add(...)`.  
- Fournir **tests unitaires JUnit 5** couvrant ≥ 80 % de classe DAO + Services.  
- Pipeline GitHub Actions : Maven build → Tests → exec:java vérification → build image Docker → push.  
- `docker-compose.yml` doit démarrer :  
  ```yaml
  services:
    mysql:
      image: mysql:8
      environment:
        MYSQL_ROOT_PASSWORD: parcauto_root_pw
        MYSQL_DATABASE: parcauto
      ports: [3306:3306]
      volumes: ["./db:/docker-entrypoint-initdb.d"]
    phpmyadmin:
      image: phpmyadmin/phpmyadmin
      environment:
        PMA_HOST: mysql
      ports: [8081:80]
    app:
      build: .
      depends_on: [mysql]
      ports: [8080:8080]


ParcAuto – Vue d’ensemble des acteurs et des flux de données 📊
Ce document Markdown décrit, pour chaque acteur du système ParcAuto, les rôles, responsabilités et flux de données auxquels il participe.
Les schémas PlantUML inclus sont prêts à être copiés‑collés dans un outil compatible (PlantUML Server, VS Code PlantUML, etc.) pour générer les diagrammes.

1. Acteurs métier 👥
Code	Acteur métier	Rôle principal	Données manipulées¹
U1	Responsable Logistique	Supervise la flotte, valide les missions, planifie les entretiens et approuve les dépenses	Véhicules, Missions, Entretien, Dépenses, Rapports KPI
U2	Agent Logistique	Met à jour le kilométrage, crée les ordres d’entretien, enregistre les mouvements financiers	Véhicules, Entretien, Mouvements, Documents
U3	Sociétaire / Conducteur	Consulte son planning de mission et l’état de son véhicule affecté	Missions (lecture), Véhicules (lecture)
U4	Administrateur Système	Gère les comptes utilisateurs, la sécurité et la sauvegarde de la base	Utilisateurs, Permissions, Sauvegardes, Logs

<sub>¹ En plus des métadonnées de session (jeton, rôle, timestamp) gérées par SessionManager.</sub>

2. Acteurs techniques 🖥️
Acteur technique	Description
Client JavaFX	Application riche (fat‑JAR) exécutée sur poste utilisateur
Services métier	Couche Java regroupant la logique (ex. VehiculeService)
DAO / JDBC	Couche d’accès aux données (HikariCP → MySQL)
Base MySQL	Schéma parcauto : tables, vues, triggers
phpMyAdmin	Interface web d’administration SQL (port 8081)
GitHub Actions	CI : build, tests, analyse Sonar, build/push image Docker
Docker Compose	Orchestration : mysql, phpmyadmin, app

3. Diagramme de contexte (DFD 0) 🗺️
plantuml
Copier
Modifier
@startuml DFD0
actor "Responsable Logistique (U1)" as U1
actor "Agent Logistique (U2)" as U2
actor "Sociétaire (U3)" as U3
actor "Admin Système (U4)" as U4

rectangle "Client JavaFX" as FX
database "Base MySQL\n(schema parcauto)" as DB
rectangle "phpMyAdmin" as PMA

U1 --> FX : Planifier mission / consulter rapports
U2 --> FX : Saisir entretien / mouvements
U3 --> FX : Consulter planning
U4 --> FX : Créer comptes / exporter logs
U4 --> PMA : Maintenance DB

FX --> DB : Requêtes SQL (CRUD)
PMA --> DB : SQL admin
@enduml
4. Flots de données détaillés par module 🔄
4.1 Gestion des véhicules
Étape	Flux	Description
①	VehiculeController → VehiculeService	CRUD véhicule (ajout, édition, suppression)
②	VehiculeService → VehiculeDao	Exécution des requêtes SQL préparées
③	VehiculeDao → MySQL	INSERT, UPDATE, DELETE, SELECT
④	MySQL → VehiculeDao	Résultats JDBC (ResultSet)
⑤	VehiculeService → VehiculeController	Liste d’objets Vehicule
⑥	VehiculeController → JavaFX TableView	Affichage / rafraîchissement UI

4.2 Authentification
#	Flux	Détails
①	LoginController → UtilisateurService	Soumission username/password
②	UtilisateurService → UtilisateurDao	SELECT * FROM UTILISATEUR WHERE login=?
③	UtilisateurDao → MySQL	Exécution requête
④	MySQL → UtilisateurDao → UtilisateurService	Données utilisateur + hash BCrypt
⑤	SecurityManager.verifyPassword()	Vérif du hash
⑥	SessionManager.ouvrirSession()	Stockage de l’objet Personnel et de son rôle
⑦	Redirection vers Dashboard.fxml	Interface adaptée aux permissions

4.3 Finances
Flux principal : FinanceController → MouvementService → MouvementDao → DB

Données manipulées : recettes, dépenses mission, solde de SocieteCompte, pièces jointes PDF (stockées sur disque, chemin conservé en DB).

4.4 Entretien & assurance
Flux CRUD entretien : EntretienController → EntretienService → EntretienDao → DB.

Calcul TCO : ReportingService.calculeTCO() agrège Mouvement, Entretien et Vehicule.kilometrage.

5. Sécurité & permissions (RBAC) 🔐
plantuml
Copier
Modifier
@startuml rbac
class Permission <<enum>> {
  LIRE_VEHICULE
  CREER_VEHICULE
  ...
}
class Role {
  +String nom
  +Set<Permission> droits
}
Role "1" o-- "*" Permission

class SessionManager {
  +ouvrirSession(Personnel)
  +fermerSession()
  +estConnecte() : boolean
  +aDroit(Permission) : boolean
}
@enduml
Rôle	Extrait de permissions
Responsable Logistique	CREER_MISSION, VALIDER_DEPENSE, LIRE_RAPPORT
Agent Logistique	CREER_ENTRETIEN, LIRE_VEHICULE, CREER_MOUVEMENT
Sociétaire	LIRE_MISSION, LIRE_VEHICULE
Admin Système	* (toutes les permissions)

BaseController appelle SessionManager.aDroit() avant chaque action sensible et affiche une alerte si l’utilisateur manque de droits.

6. Flux d’intégration continue & déploiement 🚚
plantuml
Copier
Modifier
@startuml cicd
actor Dev
Dev -> "GitHub Repo" : push / PR
"GitHub Repo" -> "GitHub Actions CI" : déclenche workflow
"GitHub Actions CI" -> Maven : mvn clean verify
Maven --> "JUnit/Mockito" : tests
Maven -> SonarCloud : analyse qualité
"GitHub Actions CI" -> Docker : build image\n(parcauto:latest)
Docker -> "ghcr.io" : push
"GitHub Actions CI" -> Slack/Email : notification
@enduml
Déploiement local :

bash
Copier
Modifier
# Clone & build
git clone https://github.com/…/parcauto.git
cd parcauto
mvn clean package

# Lancer l’écosystème
docker-compose up
# ➜ MySQL  : localhost:3306   (root/parcauto_root_pw)
# ➜ phpMyAdmin : http://localhost:8081
# ➜ App JavaFX : java -jar target/parcauto.jar
7. Cycle de vie d’une donnée (exemple : ajout véhicule) 🔄
UI : l’Agent saisit les champs (FXML → TextFields).

Validation : Validator contrôle la plaque d’immatriculation (AB‑123‑CD).

Service : VehiculeService.ajouterVehicule() applique les règles métier (kilométrage ≥ 0).

DAO : VehiculeDao.save() prépare l’INSERT.

DB : déclencheur trg_log_insert écrit dans la table VEHICULE_LOG.

Service : retourne l’ID généré.

UI : TableView se rafraîchit et affiche le nouveau véhicule.

8. Glossaire abrégé
Terme	Définition courte
Mission	Déplacement planifié d’un véhicule pour un sociétaire
Entretien	Intervention de maintenance préventive ou corrective
Mouvement	Opération financière (dépense ou recette) rattachée à une mission
TCO	Total Cost of Ownership – coût complet d’un véhicule sur sa durée de vie

9. Points d’extension prévus 🔧
Module GPS / télématique (future intégration via WebSocket, pas REST).

Export Excel via Apache POI directement depuis ReportingService.

Archivage documents : stockage chiffré des factures PDF, indexation plein‑texte (Lucene).

// Suppression de toute référence à Maven :
// Ce projet ne doit plus mentionner Maven dans les instructions ou commentaires.