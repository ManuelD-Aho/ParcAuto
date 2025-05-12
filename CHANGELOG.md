# Journal des modifications - ParcAuto

Ce document retrace l'historique des modifications majeures apportées au projet ParcAuto.

## Version 1.2.0 (Mai 2025)

### Améliorations
- Ajout du module de gestion des documents avec support PDF
- Amélioration du tableau de bord avec nouveaux indicateurs de performance
- Refonte de l'interface utilisateur de gestion des missions
- Support complet pour l'authentification à deux facteurs (MFA)
- Ajout de notifications temps réel pour les alertes critiques

### Corrections
- Correction du calcul du TCO qui ne prenait pas en compte les entretiens externes
- Résolution du problème de latence lors de l'affichage des véhicules (>100 véhicules)
- Correction de la synchronisation des données entre utilisateurs simultanés
- Correction des problèmes d'encodage dans les exports PDF

### Technique
- Migration vers MySQL 8.3
- Mise à jour de JavaFX vers la version 17 LTS
- Optimisation des requêtes SQL pour améliorer les performances
- Ajout de la conteneurisation Docker complète
- Mise à jour du système de journalisation

## Version 1.1.0 (Janvier 2025)

### Nouvelles fonctionnalités
- Système de rapports personnalisables
- Module de gestion des assurances
- Support pour les documents dématérialisés
- Export des données en Excel et PDF
- Système de notifications par email

### Améliorations
- Refonte du module financier
- Interface utilisateur adaptative pour différentes tailles d'écran
- Optimisation des performances sur les grands volumes de données
- Amélioration de la gestion des permissions

### Corrections
- Résolution des difficultés de mémorisation de session
- Correction des erreurs d'affichage sur certains navigateurs
- Correction des erreurs de calcul kilométrique
- Résolution des conflits de données en édition concurrente

## Version 1.0.0 (Octobre 2024)

### Première version stable
- Gestion complète des véhicules (ajout, modification, suppression)
- Suivi des missions et des déplacements
- Module de planification des entretiens
- Gestion financière de base
- Système de gestion des utilisateurs et des droits

### Fonctionnalités techniques
- Architecture en couches DAO/Service/Controller
- Interface utilisateur JavaFX
- Persistance MySQL avec JDBC
- Système d'authentification sécurisé
- Tests unitaires JUnit pour les fonctionnalités critiques

---

## Notes de développement

### Version 1.2.1 (Prévue Juillet 2025)
- Support planifié pour l'intégration avec des systèmes GPS externes
- Amélioration du module de statistiques avec graphiques avancés
- Extension du système de rapports pour inclure des données prévisionnelles
- Optimisation des performances pour les grands parcs (>500 véhicules)
- Refactoring du code legacy dans le module de gestion des entretiens
