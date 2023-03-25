package game;

import map.Zone;
import player.Joueur;
import player.Personnage;
import tools.Devinette;
import tools.Enigme;

import java.util.Random;
import java.util.Scanner;

import commande.Commande;

public class Partie {

	private int niveau = 0;
	private int duree;
	private int position;
	private Enigme enigmes = new Enigme();
	public Joueur joueur = new Joueur();
	public Zone zone = new Zone();
	public Personnage personnage = new Personnage();

	Scanner scanner = new Scanner(System.in);

	public Partie() {
		this.niveau = 1;
		this.duree = 0;
		this.position = 0;

	}

	public void authentifierLeJoueur() {
		System.out.println(
				"Bienvenue dans le le jeu Escape Game!\nTapez 1 pour vous connecter \nTapez 2 pour vous enregistrer si vous n'avez pas de compte.");
		int choix_joueur = scanner.nextInt();
		if (choix_joueur == 2) {
			this.joueur.s_enregistrer();
		} else {
			this.joueur.s_authentifier();
		}
	}

	public void passerAuNiveauSuivant() {
		this.niveau++;
	}

	public void passerALaZoneSuivante() {
		this.position++;
	}

	public void jouer() {

		// appel de la méthode pour s'authentifier
		this.authentifierLeJoueur();

		// ajouter un texte qui récapitule le scénario et les rèles du jeu.
		
		//début du jeu dans la cellule
		System.out.println("Vous êtes dans :  " + zone.LesZones[position]);
		System.out.println("Résolvez l'énigme pour pouvoir passer à la zone suivante.");
		this.joueur.repondre_enigme(this.niveau);
		System.out.println("Bravo tu as réussi à sortir de " + zone.LesZones[position] + ".");
		this.passerALaZoneSuivante();
		Commande.se_deplacer();
		
		//A la sortie de la cellule
		this.joueur.choisirLaZoneOuChercherLesCles(this.position);
		System.out.println("Bravo en tu as reussi à sortir de la prison ");
		this.passerALaZoneSuivante();
		this.passerAuNiveauSuivant();

		// arrivée au vieux-port
		while ((this.position >= 4 && this.position <= 6) || this.joueur.trouverPonton() == true) {
			if (this.position == 4) {
				System.out.println(
						"Maintenant que tu es sorti de prison, tu dois récuper la carte codée pour déchiffrer la localisation du sac et le portable pour te rendre à cette adresse. Pour cela il te faut trouver le bon ponton parmi les 3 pontons du vieux-port.\n"
								+ "Indice : ...");
			}
			else {
				this.joueur.trouverPonton();
				this.passerALaZoneSuivante();
			}
			
		}

		// passer directement à la zone 7 si le jour réussi du premier coup
		while (this.position != 7) {
			this.passerALaZoneSuivante();
		}
		if (this.position == 7) {
			System.out.println(
					"Vous êtes maintenat dans le bateau. Pour récupérer la carte et le téléphone, vous devez deviner un nombre entre 1 et 100.");
			Devinette.jeuDeDevinette();
			System.out.println(
					"Bravo! Vous avez récupérer la carte et le téléphone. Maintenant il faut déchiffrer le code de la carte. Pour cela résolvez l'énigme si dessous.");
			this.joueur.repondre_enigme(this.position);
			this.personnage.recupeObjet(this.position);
			this.personnage.recupeObjet(this.position);
			this.passerALaZoneSuivante();
			this.passerAuNiveauSuivant();
			
			
		} else if (this.position == 8) {
			System.out.println(
					"Bravo! vous avez réussi à vous téléporter j'usqu'à la forêt. Il y'a 3 objets à récupérer ici : la pelle, la boisson énergisante et le sac.Pour cela vous devez résoudre l'énigme suivante.");
			this.joueur.repondre_enigme(this.position);
			System.out.println("Bravo! tu a récupéré la pelle");
			this.joueur.repondre_enigme(this.position);
			System.out.println("Bravo! tu a récupéré la boisson énergisante.");
			this.joueur.repondre_enigme(this.position);
			System.out.println(
					"Bravo! tu a récupéré le dernier objet qui est le sac. Dépêche toi pour te rendre au hangar afin de libérer ta fille.");
			this.passerALaZoneSuivante();
			Commande.se_deplacer();
			
			//zone 9
		} else {
			System.out.println(
					"Bravo! Vous êtes arrivé à la dernière étape. Dans ce hangar se trouve votre fille. Résolvez l'énigme ci-dessous pour la récupérer et gagner la partie.");
			this.joueur.repondre_enigme(this.position);
			System.out.println("FÉLICATATION!!! TU A GAGNÉ LA PARTIE.");
		}

	}

}