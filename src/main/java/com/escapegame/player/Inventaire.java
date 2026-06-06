package com.escapegame.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Inventaire du joueur : la liste des objets récupérés au fil de la partie.
 */
public class Inventaire {

    private final List<String> objets = new ArrayList<>();

    public void ajouter(String objet) {
        if (objet != null) {
            objets.add(objet);
        }
    }

    public boolean estVide() {
        return objets.isEmpty();
    }

    public int taille() {
        return objets.size();
    }

    public List<String> objets() {
        return Collections.unmodifiableList(objets);
    }

    public String afficher() {
        if (objets.isEmpty()) {
            return "Inventaire vide.";
        }
        StringBuilder sb = new StringBuilder("Objets récupérés :");
        for (String objet : objets) {
            sb.append("\n  - ").append(objet);
        }
        return sb.toString();
    }
}
