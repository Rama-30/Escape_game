package com.escapegame;

import com.escapegame.game.Partie;
import com.escapegame.ui.FenetreJeu;

/**
 * Point d'entrée de l'application.
 *
 * <p>Par défaut, le jeu se lance dans une fenêtre graphique. L'option {@code --console}
 * permet de jouer entièrement dans le terminal (utile en environnement sans affichage).</p>
 */
public class App {

    public static void main(String[] args) {
        boolean console = false;
        for (String arg : args) {
            if ("--console".equals(arg)) {
                console = true;
            }
        }

        if (console || java.awt.GraphicsEnvironment.isHeadless()) {
            new Partie().jouer();
        } else {
            FenetreJeu.lancer();
        }
    }
}
