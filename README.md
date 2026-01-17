# Energy Tycoon - Simulateur de Gestion de Ville

Energy Tycoon est un jeu de simulation urbaine développé en Java dans lequel le joueur incarne le maire d'une ville en pleine croissance. L'objectif principal est de gérer le réseau électrique de la ville tout en développant les zones résidentielles et en maintenant la satisfaction des citoyens.

## 🚀 Fonctionnalités

- **Gestion Complexe du Réseau Électrique** : Choisir entre les énergies Solaire, Éolienne, Charbon ou Nucléaire.
- **Économie Dynamique** : L'impôts est collecté toutes les 6 heures et le joueur reçoit des bonus nocturnes (le nombre total de batiments x 10 en argent).

- **Gestion des Bâtiments** : 5 types de résidences sont disponibles, de la petite Maison au Gratte-ciel massif.

- **Gestion des Ressources** : 4 types de ressources sont disponibles, Solaire, Éolienne, Charbon ou Nucléaire.

- **Simulation en Temps Réel** : Inclut un cycle temporel complet (heures, jours, mois) et une production d'énergie dépendante de la météo (des variations des valeurs produites par les ressources, vent, soleil, ... ont été introduites).

- **Maintenance et Améliorations** : Les bâtiments nécessitent des réparations (les centrales électriques ont une probabilité de tomber en panne) et des améliorations stratégiques pour répondre à la demande énergétique croissante.

- **Mécaniques de Survie** : Gérer la satisfaction des citoyens pour éviter le Game Over.

## 🛠️ Pile Technique
- **Langage** : Java 17+

- **Interface Graphique** : Swing (rendu vectoriel Graphics2D)

- **Architecture** : Modèle-Vue-Contrôleur (MVC)

## 👥 Équipe de Projet & Collaboration

Ce projet a été développé grâce à un effort collaboratif entre **TEPE Paulin Kossi** et **BOTRE LARE Aboudou**, en travaillant à la fois en présentiel et à distance. En raison de notre flux de travail décentralisé, la fréquence des commits peut varier d'un membre à l'autre.

### **TEPE Paulin Kossi**
- **Ressources & Centrales Électriques** : Gestion de la logique de production d'énergie et des types de ressources.
- **Types de Bâtiments** : Définition et configuration des différentes catégories de bâtiments.
- **Documentation** : Rédaction de la documentation technique et du guide de projet.
- **Conception Partagée** : Définition conjointe des règles métier, de la mise en page de l'interface utilisateur, des visuels et des animations.

### **BOTRE LARE Aboudou**
- **Gestion des Bâtiments** : Implémentation du cycle de vie et des états des bâtiments.
- **Simulation** : Développement de la boucle principale de simulation et de la logique temporelle.
- **Modèle Joueur & Ville** : Contribution à la conception des structures de données `Player` et `City`.
- **Conception Partagée** : Définition conjointe des règles métier, de la mise en page de l'interface utilisateur, des visuels et des animations.

## 🎮 Comment Exécuter
Le point d'entrée de l'application est le fichier `Main.java`.

1. Assurez-vous d'avoir installé Java 17 ou une version supérieure.
2. Compilez les fichiers sources.
3. Exécutez `src/com/inf2328/energytycoon/Main.java`.

## Structure des Dossiers

L'espace de travail contient par défaut deux dossiers :

- `src` : le dossier contenant les codes sources.
- `lib` : le dossier contenant les dépendances.

Les fichiers de sortie compilés seront générés dans le dossier `bin` par défaut.

## Documentation

Des fichier txt ont été créés pour documenter le projet, dans ces fichiers vous trouverez:
    - une description des règles mise en place (REGLES_DU_JEU.txt)
    - une description de l'architecture du model (fiche_tech.txt)

## Auteurs

- TEPE Paulin Kossi
- BOTRE LARE Aboudou

---
Développé dans le cadre du projet INF2328.

