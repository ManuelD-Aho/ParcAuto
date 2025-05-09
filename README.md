# ParcAuto – Projet JavaFX sans Maven

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