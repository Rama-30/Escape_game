package game;

import java.sql.SQLException;
import java.util.Scanner;

import dataSource.SourceDeDonnees;
import tools.Design;

public class AuthentificationJoueur {

	private String pseudo, motDePasse, nomJoueur, prenomJoueur;

	Scanner scanner = new Scanner(System.in);
	Design monDesign = new Design();
	SourceDeDonnees sdd;

	public void enregistrerNouveauJoueur() {
		String texte = " Veuillez vous enregistrer en renseignant les champs suivants: ";
		this.monDesign.changerCouleurTexte(texte);
		System.out.println(texte);

		// inscription du joueur
		System.out.print("Pseudo : ");
		this.pseudo = scanner.nextLine();
		System.out.print("Mot de passe : ");
		this.motDePasse = scanner.nextLine();
		System.out.print("Nom : ");
		this.nomJoueur = scanner.nextLine();
		System.out.print("Prénom : ");
		this.prenomJoueur = scanner.nextLine();

		// enregistrement dans la BD
		try {
			sdd.ajouter_joueur(this.prenomJoueur, this.nomJoueur, this.pseudo, this.motDePasse);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		System.out.println("Merci " + this.prenomJoueur + " " + this.nomJoueur
				+ "! Vos informations ont été enregistrées avec succès.\n Vous pouvez maintenant vous authentifier\n");
	}

	
	// Méthode pour authentifier un joueur avec son pseudo et son mot de passe
	public void authentifierJoueur() {

		System.out.println(" Veuillez vous authentifier : ");
		System.out.print("Pseudo : ");
		String pseudo = scanner.nextLine();
		System.out.print("Mot de passe : ");
		String motDePasse = scanner.nextLine();

		if (sdd.verifier_joueur(pseudo, motDePasse)) {
			System.out.println("Authentification réussie !");

		} else {
			System.out.println("pseudo ou mot de passe invalide. Veuillez réessayer.");
			this.authentifierJoueur();
		}
	}


}
