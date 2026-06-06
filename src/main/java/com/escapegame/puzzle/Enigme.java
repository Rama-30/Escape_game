package com.escapegame.puzzle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Banque d'énigmes à choix multiple (ou réponse libre) et leurs solutions.
 *
 * <p>Corrections par rapport à la version d'origine :</p>
 * <ul>
 *   <li>la sélection d'un indice par niveau pouvait renvoyer des valeurs hors des bornes
 *       du tableau ({@code 9 + rand.nextInt(14)} allait jusqu'à 22 pour 15 cases) ; les trois
 *       niveaux pointent désormais vers des sous-plages disjointes et valides ;</li>
 *   <li>le tirage se faisait <i>avec remise</i>, si bien qu'une même énigme pouvait revenir
 *       plusieurs fois dans une partie ; il est désormais <i>sans remise</i> (chaque énigme
 *       n'apparaît qu'une fois tant que la plage du niveau n'est pas épuisée).</li>
 * </ul>
 */
public class Enigme {

    private final String[] enigmes = new String[15];
    private final String[] reponses = new String[15];
    private final Random random;
    private final Set<Integer> dejaUtilisees = new HashSet<>();

    public Enigme() {
        this(new Random());
    }

    /** Constructeur à graine contrôlée, pour des tests déterministes. */
    public Enigme(Random random) {
        this.random = random;
        initEnigmes();
        initReponses();
    }

    private void initEnigmes() {
        enigmes[0] = "On peut me trouver au fond d'un bateau de pêche ou au milieu d'un court de tennis.\n"
                + "Qui suis-je ?\n  A. Ancre   B. Poisson   C. Filet   D. Humain";
        enigmes[1] = "Je ne peux pas marcher, j'ai pourtant un dos et quatre pieds. Qui suis-je ?\n"
                + "  A. Chaussure   B. Meuble   C. Sac à dos   D. Chaise";
        enigmes[2] = "Trois poissons sont dans un seau, l'un d'entre eux meurt, combien en reste-t-il ?\n"
                + "  A. 2 poissons   B. 3 poissons   C. 1 poisson   D. 0 poisson";
        enigmes[3] = "Quelle figure géométrique n'a ni 4 ni 5 côtés, mais en a la moitié de 6 ?\n"
                + "  A. Pentagone   B. Octogone   C. Triangle   D. Hexagone";
        enigmes[4] = "Qu'est-ce qui est plus lourd : un kg de paille ou un kg de fer ?\n"
                + "  A. Le fer   B. La paille   C. Les deux sont égaux";
        enigmes[5] = "Combien de temps peut vivre une souris ?\n"
                + "  A. 2 ans   B. En fonction des chats   C. 10 ans   D. 8 ans";
        enigmes[6] = "Je suis Sophie, mais je ne suis pas Sophie. Qui suis-je ?\n"
                + "  A. Sophie   B. Son chien   C. Sa mère   D. Son vélo";
        enigmes[7] = "Considérez la suite : 0 1 1 2 3 5 8. Quel nombre suit le 8 ?";
        enigmes[8] = "Qu'est-ce qui t'appartient mais que les autres utilisent plus que toi ?";
        enigmes[9] = "Une balle et une batte coûtent ensemble 110 euros. La batte vaut 100 euros\n"
                + "de plus que la balle. Combien coûte la balle ?\n"
                + "  A. 5 euros   B. 10 euros   C. 0 euro";
        enigmes[10] = "Combien de gouttes d'eau peut-on mettre dans un verre vide ?\n"
                + "  A. 1 goutte   B. 200 gouttes   C. 0 goutte   D. 10 gouttes";
        enigmes[11] = "Pierre a deux fois plus de sœurs que de frères et Marie (sa sœur) a autant de\n"
                + "frères que de sœurs. Combien sont-ils d'enfants en tout ?";
        enigmes[12] = "Entre 3 personnes se trouvent deux parents et deux enfants. Comment ?\n"
                + "  A. Un grand-parent, un parent et un enfant   B. Des triplés   C. Frère, sœur et parent";
        enigmes[13] = "Vous êtes 12 frères et sœurs. Vous êtes le second à naître mais le plus petit\n"
                + "de tous. Quel mois êtes-vous né ? (indice : le plus court)";
        enigmes[14] = "C'est le jour de la paie. La poule reçoit 7 euros, l'abeille 21, l'araignée 28.\n"
                + "Combien reçoit le chien ?";
    }

    private void initReponses() {
        reponses[0] = "C";
        reponses[1] = "D";
        reponses[2] = "B";
        reponses[3] = "C";
        reponses[4] = "C";
        reponses[5] = "B";
        reponses[6] = "B";
        reponses[7] = "13";
        reponses[8] = "prénom";
        reponses[9] = "A";
        reponses[10] = "A";
        reponses[11] = "7";
        reponses[12] = "A";
        reponses[13] = "février";
        reponses[14] = "14";
    }

    public int nombreEnigmes() {
        return enigmes.length;
    }

    public String enonce(int indice) {
        return enigmes[indice];
    }

    public String reponseAttendue(int indice) {
        return reponses[indice];
    }

    /**
     * Renvoie l'indice d'une énigme tirée au hasard dans la sous-plage du niveau,
     * <b>sans remise</b> : une énigme déjà servie n'est pas reproposée tant que la plage
     * du niveau n'est pas entièrement épuisée (auquel cas la plage est réinitialisée).
     * Niveaux 1, 2, 3 → plages [0..4], [5..9], [10..14]. Tout autre niveau est ramené
     * dans [1..3].
     */
    public int indicePourNiveau(int niveau) {
        int n = Math.max(1, Math.min(3, niveau));
        int debut = (n - 1) * 5; // 0, 5 ou 10

        List<Integer> libres = new ArrayList<>();
        for (int i = debut; i < debut + 5; i++) {
            if (!dejaUtilisees.contains(i)) {
                libres.add(i);
            }
        }
        // Plage épuisée : on la réinitialise pour pouvoir continuer à jouer.
        if (libres.isEmpty()) {
            for (int i = debut; i < debut + 5; i++) {
                dejaUtilisees.remove(i);
                libres.add(i);
            }
        }

        int indice = libres.get(random.nextInt(libres.size()));
        dejaUtilisees.add(indice);
        return indice;
    }

    /** Comparaison tolérante : insensible à la casse et aux espaces de bord. */
    public boolean estCorrecte(String reponseJoueur, int indice) {
        if (reponseJoueur == null) {
            return false;
        }
        return reponseJoueur.trim().equalsIgnoreCase(reponses[indice].trim());
    }
}
