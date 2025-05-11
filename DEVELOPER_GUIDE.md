# Guide du Développeur - ParcAuto

Ce document est destiné aux développeurs qui travaillent sur le projet ParcAuto. Il fournit des informations détaillées sur l'architecture, les pratiques de développement et les procédures courantes.

## Table des matières

1. [Environnement de développement](#environnement-de-développement)
2. [Architecture détaillée](#architecture-détaillée)
3. [Flux de travail des fonctionnalités](#flux-de-travail-des-fonctionnalités)
4. [Guide de style de code](#guide-de-style-de-code)
5. [Procédures de test](#procédures-de-test)
6. [Gestion des branches Git](#gestion-des-branches-git)
7. [Pipeline CI/CD](#pipeline-cicd)
8. [Résolution des problèmes courants](#résolution-des-problèmes-courants)

## Environnement de développement

### IDE recommandé

IntelliJ IDEA ou Eclipse avec les plugins suivants :
- JavaFX Scene Builder
- SonarLint (pour l'analyse de code)
- Database Navigator (pour MySQL)

### Configuration de base

1. **Configuration de Java**
   - Java 17 (Oracle JDK ou OpenJDK)
   - Variables d'environnement : `JAVA_HOME` correctement défini

2. **Configuration de JavaFX**
   - JavaFX SDK 17 installé
   - Variables requises : `PATH_TO_FX` qui pointe vers le répertoire lib du SDK JavaFX

3. **Base de données locale**
   - MySQL 8.x avec le schéma ParcAuto
   - Configuration des paramètres de connexion dans le fichier `DbUtil.java`

4. **Compilation et Exécution**
   ```bash
   # Compilation
   build.bat
   
   # Exécution 
   java -cp "lib/*;target/classes" main.java.com.miage.parcauto.MainApp
   
   # Tests
   run_tests.bat
   ```

## Architecture détaillée

### Diagramme des packages

L'application suit un modèle en couches:

```
com.miage.parcauto
├── controller      # Contrôleurs JavaFX
├── dao             # Accès aux données
├── model           # Entités métier
├── service         # Logique métier
└── util            # Classes utilitaires
```

### Flux de données

1. **Interface utilisateur (controller)** - Le point d'entrée pour les actions utilisateur
2. **Services** - Traitent les demandes et appliquent la logique métier
3. **DAO** - Exécutent les requêtes SQL et convertissent les résultats en objets Java
4. **Modèle** - Représente les entités métier et leurs relations

### Patterns de conception utilisés

- **Singleton** : DbUtil, SecurityManager, SessionManager
- **DAO** : Classes d'accès aux données pour chaque entité
- **MVC** : Modèle (modèles), Vue (FXML), Contrôleur (contrôleurs JavaFX)
- **Factory** : Pour la création d'objets complexes

## Flux de travail des fonctionnalités

### CRUD Véhicule (exemple)

#### Affichage des véhicules
1. `VehiculeController` initialise la vue
2. `VehiculeController` appelle `VehiculeService.findAll()`
3. `VehiculeService` délègue à `VehiculeDao.findAll()`
4. `VehiculeDao` exécute la requête SQL et transforme les résultats
5. Les données sont renvoyées à la vue pour l'affichage

#### Ajout d'un véhicule
1. L'utilisateur remplit le formulaire
2. `VehiculeController` collecte les données et appelle `VehiculeService.create(vehicule)`
3. `VehiculeService` valide les données et les transmet à `VehiculeDao.create(vehicule)`
4. `VehiculeDao` exécute la requête SQL INSERT
5. `VehiculeController` actualise la vue

### Gestion de la sécurité (exemple)

1. L'utilisateur tente d'accéder à une fonctionnalité
2. `BaseController` vérifie les droits via `SecurityManager.hasPermission(permission)`
3. Si l'utilisateur a la permission, l'opération est autorisée
4. Sinon, un message d'erreur est affiché

## Guide de style de code

### Conventions de nommage

- **Classes** : PascalCase (ex: `VehiculeService`)
- **Méthodes et variables** : camelCase (ex: `findAllVehicules()`)
- **Constantes** : SNAKE_CASE majuscule (ex: `MAX_VEHICULES`)

### Documentation du code

- Javadoc pour toutes les classes et méthodes publiques
- Format standard:
  ```java
  /**
   * Description courte.
   * <p>
   * Description détaillée si nécessaire.
   *
   * @param parametre Description du paramètre
   * @return Description de la valeur retournée
   * @throws Exception Description de l'exception
   */
  ```

### Organisation du code

1. Déclarations de packages et imports
2. Javadoc de la classe
3. Déclaration de la classe
4. Constantes
5. Attributs
6. Constructeurs
7. Méthodes

## Procédures de test

### Tests unitaires

- Écrire des tests pour chaque service et DAO
- Utiliser JUnit 5 et Mockito
- Couvrir les cas normaux et exceptionnels
- Vérifier les contraintes métier

### Exemple de test unitaire

```java
@Test
void testFindVehiculeById() {
    // Arrange
    int id = 1;
    Vehicule expected = new Vehicule();
    expected.setId(id);
    when(vehiculeDao.findById(id)).thenReturn(expected);
    
    // Act
    Vehicule actual = vehiculeService.findById(id);
    
    // Assert
    assertEquals(expected, actual);
    verify(vehiculeDao).findById(id);
}
```

## Gestion des branches Git

- **main** : Code stable en production
- **develop** : Branche d'intégration principale
- **feature/xxx** : Nouvelles fonctionnalités (ex: `feature/gestion-entretien`)
- **bugfix/xxx** : Corrections de bugs (ex: `bugfix/login-error`)
- **release/x.y.z** : Préparation des versions

## Pipeline CI/CD

Le projet est configuré avec GitHub Actions pour l'intégration continue:

1. **Compilation** : Vérification que le code compile
2. **Tests** : Exécution des tests unitaires
3. **Vérification de qualité** : Analyse SonarQube
4. **Construction de l'image Docker** : Création de l'image
5. **Publication** : Push de l'image Docker dans le registry

## Résolution des problèmes courants

### Problème de base de données

**Symptôme**: Exception "Could not connect to database"

**Solution**:
1. Vérifiez que MySQL est en cours d'exécution
2. Vérifiez les paramètres de connexion dans DbUtil.java
3. Assurez-vous que la base ParcAuto existe et que le script SQL a été exécuté

### Problème d'exécution JavaFX

**Symptôme**: Exception "JavaFX runtime components are missing"

**Solution**:
1. Vérifiez que les JARs JavaFX sont dans le dossier lib/
2. Assurez-vous que le classpath inclut tous les modules JavaFX nécessaires

### Problème de compilation

**Symptôme**: Erreurs de compilation dans le script build.bat

**Solution**:
1. Vérifiez que JAVA_HOME pointe vers une installation Java 17
2. Assurez-vous que tous les JARs requis sont dans le dossier lib/
3. Supprimez les classes compilées et recompilez

---

## Ressources additionnelles

- [Documentation JavaFX](https://openjfx.io/javadoc/17/)
- [Documentation MySQL](https://dev.mysql.com/doc/)
- [Wiki interne du projet](https://wiki.miage.fr/parcauto/)

Pour toute question, contactez l'équipe de développement à dev.parcauto@miage.fr
