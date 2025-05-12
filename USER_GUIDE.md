# Guide d'utilisation - ParcAuto

Ce document présente les cas d'utilisation courants de l'application ParcAuto à travers des exemples concrets pour chaque profil d'utilisateur.

## Table des matières

1. [Introduction](#introduction)
2. [Cas d'utilisation pour le Responsable Logistique](#cas-dutilisation-pour-le-responsable-logistique)
3. [Cas d'utilisation pour l'Agent Logistique](#cas-dutilisation-pour-lagent-logistique)
4. [Cas d'utilisation pour le Sociétaire](#cas-dutilisation-pour-le-sociétaire)
5. [Cas d'utilisation pour l'Administrateur](#cas-dutilisation-pour-ladministrateur)
6. [Procédures courantes](#procédures-courantes)

## Introduction

ParcAuto est une application complète de gestion de parc automobile. Ce guide propose des exemples concrets d'utilisation par fonctionnalité et par profil utilisateur. Chaque exemple est accompagné d'instructions étape par étape pour vous guider dans votre utilisation quotidienne.

## Cas d'utilisation pour le Responsable Logistique

### 1. Validation d'une demande de mission

**Scénario** : Le service marketing a demandé un véhicule pour une mission commerciale de 3 jours.

**Étapes** :
1. Connectez-vous avec votre compte Responsable Logistique
2. Cliquez sur "Missions" > "Missions en attente"
3. Sélectionnez la mission à valider
4. Vérifiez les détails (dates, kilomètres estimés, motif)
5. Cliquez sur "Attribuer un véhicule"
6. Sélectionnez un véhicule disponible pour la période
7. Validez la mission en cliquant sur "Approuver"
8. Un email de confirmation est automatiquement envoyé au demandeur

### 2. Analyse des coûts d'exploitation

**Scénario** : Vous souhaitez analyser les coûts par véhicule pour le trimestre écoulé.

**Étapes** :
1. Connectez-vous avec votre compte Responsable Logistique
2. Cliquez sur "Rapports" > "Analyse financière"
3. Sélectionnez la période (trimestre précédent)
4. Choisissez "TCO par véhicule" dans le menu déroulant
5. Cliquez sur "Générer"
6. Examinez les données présentées dans le graphique et le tableau
7. Utilisez les filtres pour isoler certains véhicules ou catégories de dépenses
8. Exportez le rapport en PDF ou Excel en cliquant sur "Exporter"

### 3. Planification des entretiens préventifs

**Scénario** : Vous devez planifier les entretiens préventifs pour le mois prochain.

**Étapes** :
1. Connectez-vous avec votre compte Responsable Logistique
2. Cliquez sur "Entretiens" > "Planification"
3. Visualisez la liste des véhicules nécessitant un entretien
4. Sélectionnez les véhicules à planifier
5. Cliquez sur "Planifier un entretien groupé"
6. Sélectionnez le type d'entretien et le fournisseur
7. Choisissez les dates disponibles dans le calendrier
8. Validez la planification

## Cas d'utilisation pour l'Agent Logistique

### 1. Enregistrement d'un retour de mission

**Scénario** : Un véhicule revient de mission et vous devez enregistrer son retour.

**Étapes** :
1. Connectez-vous avec votre compte Agent Logistique
2. Cliquez sur "Missions" > "Missions en cours"
3. Sélectionnez la mission concernée
4. Cliquez sur "Enregistrer le retour"
5. Saisissez le kilométrage final
6. Ajoutez les notes concernant l'état du véhicule
7. Uploadez les photos du véhicule si nécessaire
8. Validez la clôture de mission

### 2. Déclaration d'un entretien correctif

**Scénario** : Un véhicule nécessite une réparation suite à une panne.

**Étapes** :
1. Connectez-vous avec votre compte Agent Logistique
2. Cliquez sur "Véhicules" > "Liste des véhicules"
3. Recherchez et sélectionnez le véhicule concerné
4. Cliquez sur "Déclarer une panne"
5. Remplissez le formulaire avec les détails de la panne
6. Sélectionnez la gravité et l'impact sur la disponibilité
7. Choisissez le prestataire de réparation
8. Ajoutez les documents pertinents (photos, devis)
9. Soumettez la déclaration

### 3. Mise à jour des documents d'un véhicule

**Scénario** : Vous avez reçu une nouvelle carte grise pour un véhicule.

**Étapes** :
1. Connectez-vous avec votre compte Agent Logistique
2. Cliquez sur "Documents" > "Gestion documentaire"
3. Recherchez le véhicule par immatriculation
4. Dans l'onglet "Documents administratifs", cliquez sur "Ajouter"
5. Sélectionnez "Carte grise" comme type de document
6. Uploadez le scan du document
7. Remplissez les métadonnées (date d'émission, validité, etc.)
8. Cliquez sur "Enregistrer"

## Cas d'utilisation pour le Sociétaire

### 1. Consultation de son planning de missions

**Scénario** : Un sociétaire souhaite vérifier ses missions à venir.

**Étapes** :
1. Connectez-vous avec votre compte Sociétaire
2. Sur le tableau de bord, consultez la section "Mes missions"
3. Visualisez les missions à venir dans le calendrier
4. Cliquez sur une mission pour voir les détails
5. Utilisez les filtres pour afficher les missions par période ou statut

### 2. Réservation d'un véhicule pour une mission

**Scénario** : Un sociétaire doit se rendre à une réunion et a besoin d'un véhicule.

**Étapes** :
1. Connectez-vous avec votre compte Sociétaire
2. Cliquez sur "Missions" > "Nouvelle demande"
3. Remplissez le formulaire avec les détails de la mission
   - Date et heure de début/fin
   - Destination et motif
   - Kilomètres estimés
4. Sélectionnez vos préférences de véhicule (optionnel)
5. Joignez les documents justificatifs si nécessaire
6. Soumettez la demande pour approbation

### 3. Déclaration des frais de mission

**Scénario** : Au retour d'une mission, le sociétaire doit déclarer ses frais.

**Étapes** :
1. Connectez-vous avec votre compte Sociétaire
2. Cliquez sur "Missions" > "Mes missions"
3. Sélectionnez la mission concernée
4. Cliquez sur l'onglet "Dépenses"
5. Ajoutez chaque dépense en précisant :
   - Type (carburant, péage, stationnement, etc.)
   - Montant
   - Justificatif (scan du reçu)
6. Vérifiez le total des dépenses
7. Soumettez la note de frais pour validation

## Cas d'utilisation pour l'Administrateur

### 1. Création d'un nouvel utilisateur

**Scénario** : Un nouvel employé rejoint l'entreprise et doit avoir accès à ParcAuto.

**Étapes** :
1. Connectez-vous avec votre compte Administrateur
2. Cliquez sur "Administration" > "Gestion des utilisateurs"
3. Cliquez sur "Nouvel utilisateur"
4. Remplissez les informations personnelles
5. Sélectionnez le rôle approprié (Agent Logistique, Sociétaire, etc.)
6. Attribuez les permissions spécifiques si nécessaire
7. Activez l'option "Changement de mot de passe à la première connexion"
8. Cliquez sur "Créer" pour finaliser

### 2. Sauvegarde de la base de données

**Scénario** : Vous devez effectuer une sauvegarde manuelle de la base de données.

**Étapes** :
1. Connectez-vous avec votre compte Administrateur
2. Cliquez sur "Administration" > "Maintenance"
3. Dans la section "Base de données", cliquez sur "Sauvegarde"
4. Sélectionnez les options de sauvegarde :
   - Sauvegarde complète ou partielle
   - Inclusion des documents
   - Compression
5. Choisissez l'emplacement de la sauvegarde
6. Lancez la sauvegarde en cliquant sur "Démarrer"
7. Attendez la confirmation de fin de sauvegarde

### 3. Analyse des journaux d'activité

**Scénario** : Vous devez vérifier les actions récentes d'un utilisateur spécifique.

**Étapes** :
1. Connectez-vous avec votre compte Administrateur
2. Cliquez sur "Administration" > "Journaux d'activité"
3. Utilisez les filtres pour affiner la recherche :
   - Période (derniers jours/semaines)
   - Utilisateur spécifique
   - Type d'action (connexion, modification, suppression)
4. Examinez les entrées du journal
5. Cliquez sur une entrée pour voir les détails complets
6. Exportez les journaux filtrés en CSV si nécessaire

## Procédures courantes

### Processus complet de gestion d'une mission

1. **Demande** (Sociétaire)
   - Création de la demande de mission
   - Soumission pour approbation

2. **Validation** (Responsable Logistique)
   - Vérification de la disponibilité des véhicules
   - Attribution d'un véhicule approprié
   - Approbation de la mission

3. **Exécution** (Sociétaire)
   - Récupération du véhicule
   - Réalisation de la mission
   - Saisie des dépenses

4. **Clôture** (Agent Logistique)
   - Enregistrement du retour du véhicule
   - Vérification de l'état du véhicule
   - Mise à jour du kilométrage

5. **Validation financière** (Responsable Logistique)
   - Validation des dépenses déclarées
   - Clôture financière de la mission

### Processus d'entretien préventif

1. **Planification** (Responsable Logistique)
   - Identification des véhicules à entretenir
   - Planification des rendez-vous

2. **Préparation** (Agent Logistique)
   - Mise à jour du statut du véhicule
   - Préparation des documents

3. **Exécution** (Prestataire externe)
   - Réalisation de l'entretien
   - Documentation des travaux effectués

4. **Finalisation** (Agent Logistique)
   - Vérification des travaux
   - Mise à jour des informations du véhicule
   - Enregistrement des coûts

---

Pour plus d'informations ou pour toute demande d'assistance, contactez le support technique à support.parcauto@miage.fr
