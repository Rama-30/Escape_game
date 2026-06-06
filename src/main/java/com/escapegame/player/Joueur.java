package com.escapegame.player;

/**
 * Le joueur : son identité et son inventaire.
 *
 * <p>Toute la logique de saisie/énigmes qui se trouvait ici dans la version d'origine
 * a été déplacée vers le moteur de jeu ({@code com.escapegame.game.Partie}) : une entité
 * ne devrait pas piloter la boucle de jeu.</p>
 */
public class Joueur {

    private final String pseudo;
    private final Inventaire inventaire = new Inventaire();

    public Joueur(String pseudo) {
        this.pseudo = pseudo;
    }

    public String pseudo() {
        return pseudo;
    }

    public Inventaire inventaire() {
        return inventaire;
    }

    public void ramasser(String objet) {
        inventaire.ajouter(objet);
    }
}
