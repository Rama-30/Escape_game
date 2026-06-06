package com.escapegame.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DepotJoueursTest {

    @TempDir
    Path dossier;

    @Test
    void inscriptionPuisConnexion() {
        DepotJoueurs depot = new DepotJoueurs(dossier.resolve("joueurs.csv"));
        assertTrue(depot.ajouter("Ada", "Lovelace", "ada", "secret"));
        assertTrue(depot.verifier("ada", "secret"));
        assertFalse(depot.verifier("ada", "mauvais"));
        assertFalse(depot.verifier("inconnu", "secret"));
    }

    @Test
    void pseudoEnDoubleRefuse() {
        DepotJoueurs depot = new DepotJoueurs(dossier.resolve("joueurs.csv"));
        assertTrue(depot.ajouter("Ada", "Lovelace", "ada", "secret"));
        assertFalse(depot.ajouter("Autre", "Personne", "ADA", "x"), "pseudo insensible à la casse");
    }

    @Test
    void comptesPersistesEntreDeuxInstances() {
        Path fichier = dossier.resolve("joueurs.csv");
        new DepotJoueurs(fichier).ajouter("Grace", "Hopper", "grace", "cobol");

        DepotJoueurs rechargé = new DepotJoueurs(fichier);
        assertTrue(rechargé.existe("grace"));
        assertTrue(rechargé.verifier("grace", "cobol"));
    }

    @Test
    void motDePasseJamaisStockeEnClair() throws Exception {
        Path fichier = dossier.resolve("joueurs.csv");
        new DepotJoueurs(fichier).ajouter("Grace", "Hopper", "grace", "motDePasseSecret");

        String contenu = Files.readString(fichier, StandardCharsets.UTF_8);
        assertFalse(contenu.contains("motDePasseSecret"), "le mot de passe en clair ne doit pas apparaître");
    }
}
