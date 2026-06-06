package com.escapegame.game;

import com.escapegame.auth.DepotJoueurs;
import com.escapegame.auth.ServiceAuthentification;
import com.escapegame.io.Console;
import com.escapegame.io.EntreeSortie;
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

    private final EntreeSortie console;
    private final ServiceAuthentification authentification;
    private final Enigme enigmes;
    private final Devinette devinette;
    private final Random random;

    private Joueur joueur;

    /** Construction pour le jeu réel (entrée standard, fichier de comptes par défaut). */
    public Partie() {
        this(new Console(), new DepotJoueurs(), new Random());
    }

    /** Construction injectable : interface (console ou graphique), dépôt, aléa. */
    public Partie(EntreeSortie console, DepotJoueurs depot, Random random) {
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
        console.afficher("═══  ESCAPE GAME — L'Évasion  ═══");
        console.afficher("Avez-vous déjà un compte ?");
        console.afficher("   1) Oui — me connecter");
        console.afficher("   2) Non — créer un compte");
        console.afficherSansSaut("Votre choix (1 ou 2) : ");
        int choix = console.lireEntier();
        return (choix == 2) ? authentification.enregistrer() : authentification.connecter();
    }

    private void intro() {
        console.afficher("Nuit noire. Vous avez été incarcéré à tort — et votre fille est retenue "
                + "prisonnière dans un hangar, de l'autre côté de la baie.");
        console.afficher("Votre seule chance : vous évader de cette prison côtière, traverser le "
                + "vieux-port et la rejoindre avant l'aube.");
        console.afficher("Des énigmes barrent chaque étape de votre fuite. Vous disposez de "
                + ESSAIS_MAX + " tentatives par énigme : au-delà, c'est l'échec.");
        console.afficher("Bonne chance, " + joueur.pseudo() + ". Votre évasion commence maintenant.");
    }

    // --- Zone 0 : la cellule ---
    private boolean etapeCellule() {
        console.afficherZone(0);
        console.afficher("Tout commence ici, dans votre cellule : une porte cadenassée et, pour "
                + "seul espoir, une énigme gravée sur le mur.");
        console.afficher("Résolvez-la pour faire sauter la serrure.");
        if (!resoudreEnigme(1)) {
            return perdu("Sans la solution, la porte de la cellule reste close.");
        }
        console.afficher("Clic. La serrure cède : vous vous glissez hors de la cellule.");
        return true;
    }

    // --- Zones 1 à 3 : récupérer les trois clés ---
    private boolean etapeRecupererLesCles() {
        Map<String, Integer> salles = new LinkedHashMap<>();
        salles.put("B", 1); // Bureau du commandant
        salles.put("C", 2); // Cantine
        salles.put("S", 3); // Salle de pause

        console.afficher("Pour quitter le quartier pénitentiaire, il vous faut trois clés, "
                + "dissimulées chacune dans une salle de la prison.");
        while (!salles.isEmpty()) {
            console.afficher("Où souhaitez-vous fouiller ?");
            for (Map.Entry<String, Integer> salle : salles.entrySet()) {
                console.afficher("   • " + Zone.nom(salle.getValue()) + "  →  tapez " + salle.getKey());
            }
            console.afficherSansSaut("Votre choix : ");
            String choix = console.lireLigne().trim().toUpperCase();

            Integer position = salles.get(choix);
            if (position == null) {
                console.afficher("Ce n'est pas une salle valide. Tapez B, C ou S.");
                continue;
            }
            console.afficherZone(position);
            console.afficher("Vous fouillez " + Zone.nom(position) + " à la recherche d'une clé.");
            if (!resoudreEnigme(1)) {
                return perdu("Une alarme retentit : les gardiens vous reprennent.");
            }
            String cle = "la clé de " + Zone.nom(position).toLowerCase();
            joueur.ramasser(cle);
            console.afficher("Fouille réussie : vous empochez " + cle + ".");
            salles.remove(choix);
        }
        console.afficher("Les trois clés en poche, vous franchissez les dernières grilles. "
                + "Vous voilà dehors !");
        return true;
    }

    // --- Zones 4 à 6 : trouver le bon ponton ---
    private boolean etapeVieuxPort() {
        console.afficherZone(4);
        console.afficher("Le vieux-port s'étend devant vous. Trois pontons, trois bateaux — mais "
                + "un seul vous mènera vers votre fille. À vous de deviner le bon.");
        int bonPonton = 1 + random.nextInt(3);
        for (int essai = 1; essai <= ESSAIS_MAX; essai++) {
            console.afficherSansSaut("Quel ponton ? Tapez 1, 2 ou 3 (tentative "
                    + essai + "/" + ESSAIS_MAX + ") : ");
            int choix = console.lireEntier();
            if (choix == bonPonton) {
                console.afficher("C'est le bon ! Vous montez à bord et larguez les amarres.");
                return true;
            }
            console.afficher("Mauvais ponton : il ne mène à rien. Vous revenez sur vos pas.");
        }
        return perdu("Trop de temps perdu sur les quais : les gardiens du port vous repèrent.");
    }

    // --- Zone 7 : le bateau ---
    private boolean etapeBateau() {
        console.afficherZone(7);
        console.afficher("À bord, un coffre scellé par un cadenas à combinaison renferme ce qu'il "
                + "vous faut pour la suite.");
        if (!devinette.jouer()) {
            return perdu("Le cadenas tient bon : le coffre reste verrouillé.");
        }
        console.afficher("Le coffre s'ouvre. À l'intérieur, une carte chiffrée : déchiffrez-la "
                + "pour connaître votre destination.");
        if (!resoudreEnigme(2)) {
            return perdu("La carte garde son secret, indéchiffrable.");
        }
        joueur.ramasser("la carte codée");
        joueur.ramasser("le téléphone");
        console.afficher("Vous récupérez la carte et le téléphone, puis mettez le cap à terre.");
        return true;
    }

    // --- Zone 8 : la forêt ---
    private boolean etapeForet() {
        console.afficherZone(8);
        console.afficher("La carte vous conduit au cœur d'une forêt sombre. Trois objets vous y "
                + "attendent — une énigme protège chacun d'eux.");
        String[] aRamasser = {"la pelle", "la boisson énergisante", "le sac"};
        for (String objet : aRamasser) {
            console.afficher("Pour mettre la main sur " + objet + ", résolvez celle-ci :");
            if (!resoudreEnigme(2)) {
                return perdu("La nuit vous engloutit : vous vous perdez dans la forêt.");
            }
            joueur.ramasser(objet);
            console.afficher("Vous récupérez " + objet + ".");
        }
        console.afficher("Équipé de tout le nécessaire, vous repérez enfin le hangar au loin.");
        return true;
    }

    // --- Zone 9 : le hangar (fin) ---
    private void etapeHangar() {
        console.afficherZone(9);
        console.afficher("Le hangar, enfin. Derrière cette dernière porte se trouve votre fille. "
                + "Une ultime énigme vous en sépare.");
        if (!resoudreEnigme(3)) {
            perdu("La porte du hangar reste verrouillée. Si près du but…");
            return;
        }
        console.afficher("╔══════════════════════════════════════╗");
        console.afficher("   VICTOIRE — Bravo, " + joueur.pseudo().toUpperCase() + " !");
        console.afficher("╚══════════════════════════════════════╝");
        console.afficher("Vous forcez la porte et serrez enfin votre fille dans vos bras. "
                + "Vous êtes libres.");
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
        console.afficher("Raté. La solution était : " + enigmes.reponseAttendue(indice) + ".");
        return false;
    }

    private boolean perdu(String raison) {
        console.afficher(raison);
        console.afficher("✖  PARTIE PERDUE  ✖");
        return false;
    }
}
