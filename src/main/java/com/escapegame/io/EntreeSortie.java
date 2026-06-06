package com.escapegame.io;

/**
 * Frontière entre le moteur de jeu et l'interface (entrées / sorties).
 *
 * <p>Le moteur ({@code com.escapegame.game.Partie}) ne connaît que cette abstraction :
 * il peut donc être piloté indifféremment par une interface console
 * ({@link Console}) ou par une interface graphique Swing
 * ({@code com.escapegame.ui.FenetreJeu}), sans changer une ligne de logique de jeu.</p>
 */
public interface EntreeSortie {

    /** Affiche un message suivi d'un retour à la ligne. */
    void afficher(String message);

    /** Affiche un message sans retour à la ligne (invite de saisie). */
    void afficherSansSaut(String message);

    /** Lit une ligne saisie par le joueur (bloquant jusqu'à la saisie). */
    String lireLigne();

    /** Lit un entier de façon robuste (redemande tant que ce n'est pas un entier). */
    int lireEntier();

    /**
     * Signale que le joueur entre dans une zone donnée.
     * <p>L'interface graphique en profite pour afficher l'image de la zone ;
     * l'interface console l'ignore (le récit textuel suffit).</p>
     */
    default void afficherZone(int position) {
        // par défaut : aucune représentation visuelle
    }
}
