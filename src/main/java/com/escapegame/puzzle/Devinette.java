package com.escapegame.puzzle;

import com.escapegame.io.EntreeSortie;

import java.util.Random;

/**
 * Mini-jeu : deviner un nombre entre 1 et 100.
 *
 * <p>Ne ferme plus la {@link Console} (le bug d'origine fermait {@code System.in}).</p>
 */
public class Devinette {

    private final EntreeSortie console;
    private final Random random;

    public Devinette(EntreeSortie console) {
        this(console, new Random());
    }

    public Devinette(EntreeSortie console, Random random) {
        this.console = console;
        this.random = random;
    }

    /** Joue une partie complète et renvoie le nombre de tentatives. */
    public int jouer() {
        int cible = 1 + random.nextInt(100);
        int tentatives = 0;
        int proposition;

        console.afficher("Trouvez un nombre compris entre 1 et 100.");
        do {
            console.afficherSansSaut("Votre proposition : ");
            proposition = console.lireEntier();
            tentatives++;

            if (proposition < cible) {
                console.afficher("Trop petit.");
            } else if (proposition > cible) {
                console.afficher("Trop grand.");
            }
        } while (proposition != cible);

        console.afficher("Bravo ! Trouvé en " + tentatives + " tentative(s).");
        return tentatives;
    }
}
