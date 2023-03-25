
package player;

import java.util.Random;
import java.util.Scanner;

import game.AuthentificationJoueur;
import tools.Enigme;

public class Joueur {
	AuthentificationJoueur authentification = new AuthentificationJoueur();
	Enigme enigmes = new Enigme();
	Scanner scanner = new Scanner(System.in);

	public void s_authentifier() {
		this.authentification.authentifierJoueur();
	}

	public void s_enregistrer() {
		this.authentification.enregistrerNouveauJoueur();
	}

	public boolean trouverPonton() {
		// numéro du bon ponton
		Random rand = new Random();
		int numPonton = 1 + rand.nextInt(3);

		boolean trouve = false;

		System.out.println("Tape 1 --> pour le ponton 1 ");
		System.out.println("Tape 2 --> pour le ponton 2 ");
		System.out.println("Tape 3 --> pour le ponton 3 ");
		int choix = scanner.nextInt();

		if (choix == numPonton) {
			System.out.println("Bravo! tu a réussi à trouvé le bon ponton.");
			trouve = true;
		} else {
			int nbEssais = 1;
			while (nbEssais <= 2) {
				System.out.println("Désolé, vous avez choisi le mauvais ponton. Réessayez");
			}
			System.out.println("Désolé, vous avez perdu!");
		}
		scanner.close();
		return trouve;

	}

	public void choisirLaZoneOuChercherLesCles(int position) {
		// choisir dans quelle zone aller en premier
		while (position >= 1 && position <= 3) {
			if (position == 1) {
				System.out.println(
						"Maintenant il faut récupérer les trois clés qui te permettront de t'évader de cette prison.\n "
								+ "Chacune de ses clés est cachée dans une zone de la prison.\nChoisis dans quelle zonne tu veux chercher en premier.");
			} else {
				System.out.println(
						"Bravo! tu a réusi a recupérer la clé. Maintenant choisis la prochaine zone dans quelle tu veux chercher la clé.");
			}

			System.out.println("Tape B --> pour le bureau du commandant ");
			System.out.println("Tape C --> pour la cantine ");
			System.out.println("Tape S --> pour la salle de pause ");
			String choix = scanner.nextLine();

		}
	}

	public boolean est_correcte(String reponseJoueur, int niveau) {
		int indice = enigmes.rendIndiceEnigmeParSonNiveau(niveau);
		return reponseJoueur.equals(Enigme.REPONSES[indice]);
	}

	public void repondre_enigme(int niveau) {
		int indice = enigmes.rendIndiceEnigmeParSonNiveau(niveau);
		System.out.println(Enigme.ENIGMES[indice]);
		int cpt = 1;
		String reponse_joueur = scanner.nextLine();

		while (cpt < 2 && !est_correcte(reponse_joueur, niveau)) {
			System.out.println("************* Mauvaise réponse.***************\nVeuillez réessayer.");
			reponse_joueur = scanner.nextLine();
			cpt++;
		}

		if (est_correcte(reponse_joueur, niveau)) {
			System.out.println("**************Bonne réponse !!!**************");
		} else {
			System.out.println("************** Mauvaise réponse.***************");
			System.out.println("Désolé, vous avez réessayé plus de 2 fois.\n****Vous avez perdu.****");
		}
		scanner.close();
	}

	public void sauvegarder() {
		// Code de sauvegarde
	}

}
