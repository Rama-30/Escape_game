# 🔓 Escape Game — L'Évasion

> Jeu d'évasion textuel en Java. Incarcéré à tort, vous devez vous évader de prison,
> traverser le vieux-port et libérer votre fille retenue dans un hangar, en résolvant
> une série d'énigmes.

*A text-based escape room game in Java. (English summary below.)*

Projet académique réalisé en équipe de 4, **repris et retravaillé** pour le portfolio :
correction des bugs bloquants, restructuration en architecture propre, build Maven et
tests automatisés.

---

## 🎮 Jouer

Pré-requis : **JDK 17+** et **Maven 3.8+**.

```bash
# Compiler, lancer les tests et produire le jar exécutable
mvn clean package

# Lancer le jeu
java -jar target/escape-game.jar

# (ou directement, sans packager)
mvn exec:java
```

Le jeu tourne **immédiatement après un `git clone`** : aucune base de données ni
configuration externe n'est nécessaire (voir [Authentification](#-authentification)).

Un déroulé complet d'une partie gagnante est disponible dans
[`docs/transcript-partie.txt`](docs/transcript-partie.txt).

---

## 🧩 Le jeu

| Étape | Zone | Épreuve |
|------:|------|---------|
| 1 | La cellule | Une énigme pour ouvrir la porte |
| 2 | Bureau / Cantine / Salle de pause | Récupérer les 3 clés (ordre au choix) |
| 3 | Le vieux-port | Trouver le bon ponton parmi 3 |
| 4 | Le bateau | Cadenas à chiffre + déchiffrer la carte |
| 5 | La forêt | Récupérer pelle, boisson et sac |
| 6 | Le hangar | Énigme finale — libérer votre fille |

Chaque énigme laisse **3 tentatives** ; les échecs mènent à une vraie fin de partie.

---

## 🏗️ Architecture

Layout Maven standard, package racine `com.escapegame` :

```
src/main/java/com/escapegame/
├── App.java                         point d'entrée
├── io/Console.java                  E/S centralisées (un seul Scanner partagé)
├── game/Partie.java                 moteur de jeu (boucle, progression, fin)
├── player/Joueur.java, Inventaire.java
├── world/Zone.java                  les zones traversées
├── puzzle/Enigme.java, Devinette.java, Objet.java
└── auth/ServiceAuthentification.java, DepotJoueurs.java
```

### 🔑 Authentification

Les comptes sont stockés dans un simple fichier CSV (`data/joueurs.csv`, créé au besoin
et ignoré par git). Les mots de passe ne sont **jamais stockés en clair** : on conserve
un sel aléatoire et l'empreinte **SHA-256** de `sel + mot de passe`. Aucune dépendance
externe, aucun serveur de base de données.

---

## ♻️ Ce qui a été retravaillé

La version rendue était injouable. Les correctifs principaux :

| Problème d'origine | Correctif |
|--------------------|-----------|
| Chaque classe créait son `Scanner(System.in)` puis le **fermait** → `System.in` fermé pour tout le programme, crash à la 2ᵉ saisie | Une unique classe `Console` partagée, jamais fermée en cours de jeu |
| `Commande.se_deplacer()` : `while(true)` sans sortie | Boucle de jeu réécrite avec des conditions d'arrêt claires |
| `trouverPonton()` : compteur d'essais jamais incrémenté → boucle infinie | Nombre d'essais limité et borné |
| Sélection d'énigme `9 + rand.nextInt(14)` → index 22 sur un tableau de 15 → `ArrayIndexOutOfBoundsException` | Sous-plages par niveau disjointes et bornées |
| Authentification MySQL : dépôt jamais instancié (NPE), colonnes SQL incohérentes, injection SQL | Persistance fichier zéro-config + mots de passe hashés |
| `Design extends JFrame` (Swing inutile sur un jeu console) | Supprimé |

Bénéfices transverses : code **testable** (entrée injectable), séparation des
responsabilités (entité `Joueur` vs moteur `Partie`), et un dépôt nettoyé.

---

## ✅ Tests

```bash
mvn test
```

13 tests JUnit 5 couvrant : les bornes de sélection d'énigmes, la vérification tolérante
des réponses, l'inscription/connexion et le hachage des mots de passe, la robustesse de
la `Console`, et un test de bout en bout garantissant qu'une partie se termine **sans
boucle infinie** (anti-régression des deux bugs majeurs).

---

## 🛠️ Stack technique

Java 17 · Maven · JUnit 5 · `java.security.MessageDigest` (SHA-256).

---

## English summary

A text-based escape room game in Java, originally a 4-person academic project, **reworked
for portfolio use**. You escape from prison, cross the old port and free your daughter by
solving riddles. The submitted version was unplayable; this rewrite fixes the blocking
bugs (a shared `Scanner` that was being closed mid-game, infinite loops, an out-of-bounds
puzzle picker, broken MySQL auth), restructures the code into clean packages, replaces the
database with **zero-config file storage and SHA-256 hashed passwords**, and adds a Maven
build with JUnit 5 tests.

Run it with `mvn clean package && java -jar target/escape-game.jar`.
