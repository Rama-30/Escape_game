package com.escapegame.game;

import com.escapegame.auth.DepotJoueurs;
import com.escapegame.auth.ServiceAuthentification;
import com.escapegame.io.Console;
import com.escapegame.player.Joueur;
import com.escapegame.puzzle.Devinette;
import com.escapegame.puzzle.Enigme;
import com.escapegame.world.Zone;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * Moteur du jeu : déroule la partie d'une zone à l'autre.
 *
 * <p>Réécriture de la boucle d'origine, qui contenait plusieurs boucles infinies
 * ({@code while(true)} sans sortie, compteur d'essais jamais incrémenté) et une
 * progression de zones incohérente. Ici chaque étape a une condition d'arrêt claire,
 * un nombre d'essais limité, et la partie se termine sur une vraie victoire ou défaite.</p>
 */
public class Partie {

    private static final int ESSAIS_MAX = 3;

    private final Console console;
    private final ServiceAuthentification authentification;
    private final Enigme enigmes;
    private final Devinette devinette;
    private final Random random;

    private Joueur joueur;

    /** Construction pour le jeu réel (entrée standard, fichier de comptes par défaut). */
    public Partie() {
        this(new Console(), new DepotJoueurs(), new Random());
    }

    /** Construction injectable, pour les tests (entrée scriptée, dépôt temporaire). */
    public Partie(Console console, DepotJoueurs depot, Random random) {
        this.console = console;
        this.authentification = new ServiceAuthentification(console, depot);
        this.enigmes = new Enigme(random);
        this.devinette = new Devinette(console, random);
        this.random = random;
    }

    /** Point d'entrée : déroule la partie complète. */
    public void jouer() {
        String pseudo = authentifier();
        joueur = new Joueur(pseudo);

        intro();

        if (!etapeCellule()) {
            return;
        }
        if (!etapeRecupererLesCles()) {
            return;
        }
        if (!etapeVieuxPort()) {
            return;
        }
        if (!etapeBateau()) {
            return;
        }
        if (!etapeForet()) {
            return;
        }
        etapeHangar();
    }

    private String authentifier() {
        console.afficher("===== ESCAPE GAME : L'Évasion =====");
        console.afficher("1) Se connecter");
        console.afficher("2) Créer un compte");
        console.afficherSansSaut("Votre choix : ");
        int choix = console.lireEntier();
        return (choix == 2) ? authentification.enregistrer() : authentification.connecter();
    }

    private void intro() {
        console.afficher("\nVous êtes incarcéré à tort. Votre fille est retenue dans un hangar.");
        console.afficher("Évadez-vous, traversez le vieux-port, et allez la libérer.");
        console.afficher("À chaque énigme, vous avez " + ESSAIS_MAX + " tentatives.\n");
    }

    // --- Zone 0 : la cellule ---
    private boolean etapeCellule() {
        console.afficher("Vous êtes dans " + Zone.nom(0) + ".");
        console.afficher("Résolvez l'énigme pour ouvrir la porte.");
        if (!resoudreEnigme(1)) {
            return perdu("La porte reste close.");
        }
        console.afficher("Bravo, vous sortez de la cellule !\n");
        return true;
    }

    // --- Zones 1 à 3 : récupérer les trois clés ---
    private boolean etapeRecupererLesCles() {
        Map<String, Integer> salles = new LinkedHashMap<>();
        salles.put("B", 1); // Bureau du commandant
        salles.put("C", 2); // Cantine
        salles.put("S", 3); // Salle de pause

        console.afficher("Trois clés sont cachées dans la prison. Récupérez-les toutes.");
        while (!salles.isEmpty()) {
            console.afficher("Salles restantes :");
            for (Integer pos : salles.values()) {
                console.afficher("  - " + Zone.nom(pos));
            }
            console.afficher("Tapez B (bureau), C (cantine) ou S (salle de pause) :");
            String choix = console.lireLigne().trim().toUpperCase();

            Integer position = salles.get(choix);
            if (position == null) {
                console.afficher("Choix invalide.");
                continue;
            }
            console.afficher("Vous fouillez " + Zone.nom(position) + ".");
            if (!resoudreEnigme(1)) {
                return perdu("Une alarme se déclenche, vous êtes repris.");
            }
            String cle = "la clé trouvée dans " + Zone.nom(position);
            joueur.ramasser(cle);
            console.afficher("Vous récupérez " + cle + ".\n");
            salles.remove(choix);
        }
        console.afficher("Vous avez les trois clés. Vous vous évadez de la prison !\n");
        return true;
    }

    // --- Zones 4 à 6 : trouver le bon ponton ---
    private boolean etapeVieuxPort() {
        console.afficher("Au vieux-port, trois pontons. Un seul mène au bon bateau.");
        int bonPonton = 1 + random.nextInt(3);
        for (int essai = 1; essai <= ESSAIS_MAX; essai++) {
            console.afficher("Tapez 1, 2 ou 3 pour choisir un ponton (essai "
                    + essai + "/" + ESSAIS_MAX + ") :");
            int choix = console.lireEntier();
            if (choix == bonPonton) {
                console.afficher("Bon ponton ! Vous montez à bord.\n");
                return true;
            }
            console.afficher("Mauvais ponton.");
        }
        return perdu("Vous avez perdu trop de temps sur les pontons.");
    }

    // --- Zone 7 : le bateau ---
    private boolean etapeBateau() {
        console.afficher("Sur " + Zone.nom(7) + ", un coffre verrouillé par un cadenas à chiffre.");
        devinette.jouer();
        console.afficher("Le coffre s'ouvre. Déchiffrez maintenant la carte codée.");
        if (!resoudreEnigme(2)) {
            return perdu("La carte reste indéchiffrable.");
        }
        joueur.ramasser("la carte codée");
        joueur.ramasser("le téléphone");
        console.afficher("Vous récupérez la carte et le téléphone.\n");
        return true;
    }

    // --- Zone 8 : la forêt ---
    private boolean etapeForet() {
        console.afficher("La carte vous mène à " + Zone.nom(8) + ".");
        String[] aRamasser = {"la pelle", "la boisson énergisante", "le sac"};
        for (String objet : aRamasser) {
            console.afficher("Résolvez une énigme pour trouver " + objet + ".");
            if (!resoudreEnigme(2)) {
                return perdu("Vous vous perdez dans la forêt.");
            }
            joueur.ramasser(objet);
            console.afficher("Vous récupérez " + objet + ".\n");
        }
        return true;
    }

    // --- Zone 9 : le hangar (fin) ---
    private void etapeHangar() {
        console.afficher("Dernière étape : " + Zone.nom(9) + ", où votre fille est retenue.");
        if (!resoudreEnigme(3)) {
            perdu("Le hangar reste verrouillé.");
            return;
        }
        console.afficher("\n********** FÉLICITATIONS, " + joueur.pseudo().toUpperCase()
                + " ! **********");
        console.afficher("Vous libérez votre fille et gagnez la partie.");
        console.afficher(joueur.inventaire().afficher());
    }

    /** Présente une énigme du niveau donné et laisse {@link #ESSAIS_MAX} tentatives. */
    private boolean resoudreEnigme(int niveau) {
        int indice = enigmes.indicePourNiveau(niveau);
        console.afficher(enigmes.enonce(indice));
        for (int essai = 1; essai <= ESSAIS_MAX; essai++) {
            console.afficherSansSaut("Votre réponse : ");
            String reponse = console.lireLigne();
            if (enigmes.estCorrecte(reponse, indice)) {
                console.afficher("Bonne réponse !");
                return true;
            }
            if (essai < ESSAIS_MAX) {
                console.afficher("Mauvaise réponse, réessayez (" + essai + "/" + ESSAIS_MAX + ").");
            }
        }
        console.afficher("Mauvaise réponse. La bonne était : " + enigmes.reponseAttendue(indice));
        return false;
    }

    private boolean perdu(String raison) {
        console.afficher("\n" + raison);
        console.afficher("***** PARTIE PERDUE *****");
        return false;
    }
}
