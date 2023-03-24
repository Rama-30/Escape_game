package game;

import java.util.Scanner;

import player.Joueur;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner Saisi =  new Scanner(System.in);
		System.out.println("------> SAISISEZ VOTRE NOM <-------\n");
		
		String nom=Saisi.nextLine();
		
		System.out.println("------> SAISISEZ VOTRE MOT DE PASS <-------\n");
		
		String password=Saisi.nextLine();
		
		Joueur perso= new Joueur(nom, password);
		
		
		
		perso.repondreEnigme();
		
		

	}

}  
