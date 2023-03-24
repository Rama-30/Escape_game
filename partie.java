package game;

import map.Zone;
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
	
	

	public void passerAuNiveauSuivant(){
		this.niveau++;
	}
	
	
	
		
	public void jouer(int position) {
		// appeler la méthode our s'authentifier
		Partie partie = new Partie(); 
		//enigmes.repondre_enigme(partie.niveau);
		int i=0;
		System.out.println("Vous est dans :  "+zone.LesZones[this.position]);
		while(i<zone.LesZones.length)
		{
			
			
			
		}
		
		
	
	}
}
