package com.escapegame.world;

/**
 * Les différentes zones traversées au cours de la partie, dans l'ordre.
 */
public class Zone {

    public static final String[] LES_ZONES = {
        "la Cellule",
        "le Bureau du commandant",
        "la Cantine",
        "la Salle de pause",
        "le Ponton 1",
        "le Ponton 2",
        "le Ponton 3",
        "le Bateau",
        "la Forêt",
        "le Hangar"
    };

    public static final int NOMBRE_ZONES = LES_ZONES.length;

    private Zone() {
        // classe utilitaire : pas d'instanciation
    }

    public static String nom(int position) {
        if (position < 0 || position >= NOMBRE_ZONES) {
            return "une zone inconnue";
        }
        return LES_ZONES[position];
    }
}
