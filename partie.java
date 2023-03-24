package game;

import tools.Enigme;

public class Partie {
	
	private int niveau=0;
	private int duree;
	private	int position;
	private Enigme enigmes;
	private Zone zone;
	
	public Partie() {
		this.niveau = 1;
		this.duree = 0;
		this.position=0;
		enigmes= new Enigme();
		
	}
	
	public void sauvegarder(){
		// Code de sauvegarde
	}
	

	public void passerAuNiveauSuivant(){
		this.niveau++;
	}
	
	public void changerZone(){
		this.position++;
	}
	
		
	public void jouer() {
		// appeler la méthode our s'authentifier
		Partie partie = new Partie(); 
		partie.enigmes.repondre_enigme(partie.niveau);
		
		if()
	
	}
}
