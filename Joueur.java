
package player;

import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import game.AuthentificationJoueur;
import tools.Enigme;

public class Joueur {
	AuthentificationJoueur authentification = new AuthentificationJoueur();
	Enigme enigmes = new Enigme();
	Scanner scanner = new Scanner(System.in);
	private Set<String> choixFaits = new HashSet<>(); // ajouter ceci en tant que champ


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
		int nbEssais = 1;
		while (nbEssais <2 && choix!=numPonton) {
			System.out.println("Désolé, vous avez choisi le mauvais ponton. Réessayez");
			nbEssais++;
			choix = scanner.nextInt();
		}

		if (choix == numPonton) {
			System.out.println("Bravo! tu a réussi à trouver le bon ponton.");
			trouve = true;
		} else {
			
				System.out.println("Désolé, vous avez perdu!");
		}

		return trouve;

	}

	
	public void choisirLaZoneOuChercherLesCles(int position) {
	    // choisir dans quelle zone aller en premier
	    System.out.println("Tape B --> pour le bureau du commandant ");
	    System.out.println("Tape C --> pour la cantine ");
	    System.out.println("Tape S --> pour la salle de pause ");

	    String choix = scanner.nextLine();
	    if (!choixFaits.add(choix)) { // vérifier si le choix a déjà été fait
	        System.out.println("Vous avez déjà choisi cette zone. Veuillez choisir une autre zone.");
	        choisirLaZoneOuChercherLesCles(position); // appeler la méthode de manière récursive
	        return; // sortir de la méthode
	    }

	    if (choix.equals("B")) {
	        System.out.println("Vous êtes dans le bureau du commandant.");
	    } else if (choix.equals("C")) {
	        System.out.println("Vous êtes dans la cantine.");
	    } else if (choix.equals("S")) {
	        System.out.println("Vous êtes dans la salle de pause.");
	    } else {
	        System.out.println("Erreur de saisie! Veuillez réessayer.");
	        choisirLaZoneOuChercherLesCles(position);
	        return;
	    }
	}


	public boolean repondre_enigme(int niveau) {
		boolean trouve = false;
		int indice = enigmes.rendIndiceEnigmeParSonNiveau(niveau);
		System.out.println(Enigme.ENIGMES[indice]+"\n");

		String reponse_joueur = scanner.nextLine();
		int cpt = 1;
		while (cpt < 2 && !reponse_joueur.equals(Enigme.REPONSES[indice])) {
			System.out.println("************* Mauvaise réponse.***************\nVeuillez réessayer.");
			reponse_joueur = scanner.nextLine();
			cpt++;
		}

		if (reponse_joueur.equals(Enigme.REPONSES[indice])) {
			System.out.println("**************Bonne réponse !!!**************");
			trouve = true;
		} else {
			System.out.println("************** Mauvaise réponse.***************");
			System.out.println("Désolé, vous avez réessayé plus de 2 fois.\n****Vous avez perdu.****");

		}
		return trouve;
	}

	

	public void sauvegarder() {
		// Code de sauvegarde
	}

}
