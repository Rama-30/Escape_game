package com.escapegame.puzzle;

import com.escapegame.io.EntreeSortie;

import java.util.Random;

/**
 * Mini-jeu : deviner un nombre entre 1 et 100.
 *
 * <p>Ne ferme plus la {@link Console} (le bug d'origine fermait {@code System.in}).</p>
 */
public class Devinette {

    private static final int TENTATIVES_MAX = 10;

    private final EntreeSortie console;
    private final Random random;

    public Devinette(EntreeSortie console) {
        this(console, new Random());
    }

    public Devinette(EntreeSortie console, Random random) {
        this.console = console;
        this.random = random;
    }

    /**
     * Joue une partie : le joueur dispose de {@link #TENTATIVES_MAX} tentatives.
     * Renvoie {@code true} s'il trouve le nombre, {@code false} s'il épuise ses tentatives.
     */
    public boolean jouer() {
        int cible = 1 + random.nextInt(100);

        console.afficher("Trouvez un nombre compris entre 1 et 100 pour le déverrouiller. Vous avez "
                + TENTATIVES_MAX + " tentatives.");
        for (int tentative = 1; tentative <= TENTATIVES_MAX; tentative++) {
            console.afficherSansSaut("Proposition " + tentative + "/" + TENTATIVES_MAX + " : ");
            int proposition = console.lireEntier();

            if (proposition == cible) {
                console.afficher("Bravo ! Trouvé en " + tentative + " tentative(s).");
                return true;
            }
            console.afficher(proposition < cible ? "Trop petit." : "Trop grand.");
        }

        console.afficher("Vous avez épuisé vos " + TENTATIVES_MAX
                + " tentatives. Le bon nombre était " + cible + ".");
        return false;
    }
}
