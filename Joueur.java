package player;

import java.util.Scanner;

import tools.Enigme;

public class Joueur 
{
	
	
	private String Nom;
	private String PassWord;
	public Personnage MonPersonnage;
	
	
	public Joueur(String nom, String passWord) {
		this.Nom = nom;
		this.PassWord = passWord;
		MonPersonnage = new Personnage();
	}


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
	
	public static boolean repondreEnigme()
	{
		Scanner Saisi =  new Scanner(System.in);
		System.out.println("------> SAISISEZ VOTRE REPONSE <-------\n");
		
		String reponse=Saisi.nextLine();
		
		if(Personnage.contien(Enigme.reponses, reponse)==true)
			{
			Personnage.VerifiReponse(reponse);
			return true;
			}
		else 
			Personnage.VerifiReponse(reponse);
			return false;
	}
	
	
	
	
	// méthode position
	
	
	
	
	
	
	
	
	
	
	
	

}
