package com.escapegame.game;

import com.escapegame.auth.DepotJoueurs;
import com.escapegame.io.Console;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PartieTest {

    @TempDir
    Path dossier;

    /**
     * Régression centrale : avec une entrée scriptée finie, la partie doit se dérouler
     * de l'inscription jusqu'à une fin de partie SANS boucle infinie ni
     * {@code NoSuchElementException} (les deux bugs majeurs de la version d'origine).
     */
    @Test
    void laPartieSeTermineSansBlocage() {
        // 2=créer un compte, identité, puis 3 mauvaises réponses à la 1re énigme -> défaite.
        String entree = String.join("\n",
                "2", "Jean", "Dupont", "jean", "motdepasse",
                "faux", "faux", "faux") + "\n";

        String sortie = assertTimeoutPreemptively(Duration.ofSeconds(5),
                () -> executerPartie(entree));

        assertTrue(sortie.contains("Inscription"), "l'inscription doit s'exécuter");
        assertTrue(sortie.contains("Cellule"), "le jeu doit démarrer après l'authentification");
        assertTrue(sortie.contains("PARTIE PERDUE"), "la partie doit se terminer proprement");
    }

    private String executerPartie(String entree) {
        ByteArrayOutputStream tampon = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(tampon, true, StandardCharsets.UTF_8));
        try {
            Console console = new Console(
                    new ByteArrayInputStream(entree.getBytes(StandardCharsets.UTF_8)));
            DepotJoueurs depot = new DepotJoueurs(dossier.resolve("joueurs.csv"));
            new Partie(console, depot, new Random(123)).jouer();
        } finally {
            System.setOut(original);
        }
        return tampon.toString(StandardCharsets.UTF_8);
    }
}
