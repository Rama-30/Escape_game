package player;

import java.util.Scanner;

public class Joueuer 
{
	
	
	private String Nom;
	private String PassWord;
	public Personnage MonPersonnage;
	
	
	public Joueuer(String nom, String passWord) {
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
	
	public void repondreEnigme()
	{
		Scanner Saisi =  new Scanner(System.in);
		System.out.println("------> SAISISEZ VOTRE REPONSE <-------\n");
		
		String reponse=Saisi.nextLine();
		
		Personnage.VerifiReponse(reponse);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
