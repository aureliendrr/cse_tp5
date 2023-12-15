# Projet Cinema - Rapport

## Introduction
Ce projet vise à simuler un cinéma en utilisant du multi-threading avec des sémaphores, et les moniteurs. Cette simulation comprend une salle de projection, une billetterie, des clients et un employé. Ce rapport exprimera les choix de conception, les éléments de synchronisation et les problèmes potentiels rencontrés dans le code.

## Choix de Conception

### Structure du Code
Le code est organisé en plusieurs classes pour une meilleure lisibilité :
- **Cinema** : Gère l'initialisation du cinéma, le lancement des threads clients et employé.
- **ProjectionRoom** : Représente la salle de projection avec des états, la gestion des clients et la synchronisation des actions.
- **Employe** : Un thread simulant le comportement d'un employé effectuant des opérations cycliques dans la salle de projection.
- **Client** : Un thread représentant un client avec différents états décrivant son parcours dans le cinéma.
- **TicketOffice** : Gère la vente de billets avec un sémaphore pour synchroniser l'accès concurrent.

### Utilisation des Threads
Les éléments principaux du système sont modélisés comme des threads pour simuler le comportement concurrent dans le cinéma. Chaque client est un thread indépendant, tout comme l'employé.

## Problèmes de Synchronisation Potentiels

### Accès Concurrent aux Données
L'accès concurrent à la salle de projection est contrôlé par un sémaphore dans la méthode `enterProjectionRoom` de la classe `ProjectionRoom`. Cela évite que plusieurs clients entrent en même temps, ce qui pourrait dupliquer le nombre de clients.

### Synchronisation des États
La classe `ProjectionRoom` utilise la synchronisation pour manipuler les états. Par exemple, la méthode `setState` synchronise l'accès aux états de la salle de projection afin qu'ils ne rentrent qu'un par un et notifie les clients en attente lorsque l'état change.

### Billetterie
La classe `TicketOffice` utilise un sémaphore pour synchroniser l'achat de billets et éviter que plusieurs clients n'achètent simultanément le même billet (duplication de tickets).

## Explications de Code

### ProjectionRoom
La méthode `setState` gère les transitions d'états de la salle de projection. Elle utilise la synchronisation pour garantir que les états sont modifiés de manière sécurisée.

### Employe
Les méthodes `openRoom`, `startProjection`, `waitUntilEmpty`, `cleanRoom`, et `closeRoom` permettent de définir le comportement cyclique de l'employé cela la doc. En fonction de la salle de projection et du nombre de clients, l'employé réalise les différentes actions, puis ferme la salle lorsque qu'il n'y a plus de clients dans le cinéma.

### Client
La méthode `behavior` simule le parcours du client dans le cinéma. La synchronisation avec les objets `watchingFilm` et `waitingNext` permet de gérer l'attente des clients pour la prochaine séance si il n'y a plus de place, ou la fin du film avant de sortir.

### Cinema
Les méthodes telles que `enterCinema` et `exitCinema` sont synchronisées pour éviter les accès concurrents aux données partagées (la liste des clients) et ainsi ne pas avoir de clients dupliqué ou supprimés.
