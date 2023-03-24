
package player;

import java.util.Scanner;

import tools.Enigme;

public class Joueur 
{
	
	
	private String Nom;
	private String PassWord;
	public Personnage MonPersonnage;
	private Enigme enigmes;
	
	
	public String getNom() {
		return this.Nom;
	}


	public void setNom(String nom) {
		this.Nom = nom;
	}


	public String getPassWord() {
		return PassWord;
	}


	public void setPassWord(String passWord) {
		this.PassWord = passWord;
	}
	
	public boolean est_correcte(String reponseJoueur, int niveau) {
		 int indice = enigmes.rendIndiceEnigmeParSonNiveau(niveau);
		return reponseJoueur.equals(enigmes.REPONSES[indice]);
	}

	
	
	public  void repondre_enigme(int niveau) {
		int indice = enigmes.rendIndiceEnigmeParSonNiveau(niveau);
	    System.out.println(enigmes.ENIGMES[indice]);
	    int cpt = 1;
	    Scanner input = new Scanner(System.in);
	    String reponse_joueur = input.nextLine();

	    while (cpt <2 && !est_correcte(reponse_joueur,  niveau)) {
	        System.out.println("************* Mauvaise réponse.***************\nVeuillez réessayer.");
	        reponse_joueur = input.nextLine();
	        cpt++;
	    }
	    
	    if (est_correcte(reponse_joueur, niveau)) {
	        System.out.println("**************Bonne réponse !!!**************");
	    } else {
	        System.out.println("************** Mauvaise réponse.***************");
	        System.out.println("Désolé, vous avez réessayé plus de 2 fois.\n****Vous avez perdu.****");
	    }
	}
	
	
	public void sauvegarder(){
		// Code de sauvegarde
	}
	
	
	

}
