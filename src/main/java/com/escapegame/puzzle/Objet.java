package com.escapegame.puzzle;

/**
 * Objets à récupérer, indexés par la position de zone où ils se trouvent.
 */
public class Objet {

    public static final String[] OBJETS = {
        "la clé de la cellule",
        "la clé du bureau du commandant",
        "la clé de la cantine",
        "la clé de la salle de pause",
        "la carte codée",
        "le téléphone",
        "la pelle",
        "la boisson énergisante",
        "le sac"
    };

    private Objet() {
        // classe utilitaire
    }

    public static String pourPosition(int position) {
        if (position < 0 || position >= OBJETS.length) {
            return null;
        }
        return OBJETS[position];
    }
}
