package com.escapegame.io;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Point d'entrée/sortie unique du jeu.
 *
 * <p>Toutes les saisies passent par <b>un seul</b> {@link Scanner}. C'est volontaire :
 * dans la version d'origine, chaque classe créait son propre {@code new Scanner(System.in)}
 * puis appelait {@code scanner.close()}, ce qui fermait l'entrée standard pour tout le
 * programme et provoquait une {@code NoSuchElementException} à la saisie suivante.</p>
 *
 * <p>Le {@code Scanner} interne n'est donc jamais fermé en cours de partie. La source
 * d'entrée est injectable, ce qui rend le jeu testable avec une entrée scriptée.</p>
 */
public class Console implements EntreeSortie {

    private final Scanner scanner;

    /** Console branchée sur l'entrée standard (usage normal du jeu). */
    public Console() {
        this(System.in);
    }

    /** Console branchée sur une source arbitraire (utile pour les tests). */
    public Console(InputStream in) {
        this.scanner = new Scanner(in);
    }

    public void afficher(String message) {
        System.out.println(message);
    }

    public void afficherSansSaut(String message) {
        System.out.print(message);
    }

    /** Lit une ligne complète, sans la fermer la source. */
    public String lireLigne() {
        return scanner.nextLine();
    }

    /**
     * Lit un entier de façon robuste : ignore les saisies non numériques
     * et redemande tant que l'entrée n'est pas un entier valide.
     */
    public int lireEntier() {
        while (true) {
            String ligne = scanner.nextLine().trim();
            try {
                return Integer.parseInt(ligne);
            } catch (NumberFormatException e) {
                afficher("Veuillez saisir un nombre entier valide.");
            }
        }
    }
}
