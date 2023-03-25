package tools;

import map.Zone;
import player.Joueur;

public class Objet {

	private static String[] mesObjets; // tableau d'objet 
	private static int nbrObjet=9;
	public Zone MaZone;
	public Joueur joueurs;
	
	
	public static void initObjet() {
		
		mesObjets = new String[nbrObjet];
		
		mesObjets[0]="cléCellule";
		mesObjets[1]="cléBureauCommandant";
		mesObjets[2]="cléCan0t0ine";
		mesObjets[3]="CléSalleDePause";
		mesObjets[4]="Carte"; 
		mesObjets[5]="Téléphone";
		mesObjets[6]="Pelle";
		mesObjets[7]="BoissonEnergisante";
		mesObjets[8]="Sac";
	}
		
	public Objet(Zone LaZone) {
		this.MaZone = LaZone;
	}

	public String[] getMesObjets() {
		return mesObjets;
	}

	
	public int getNbrObjet() {
		return nbrObjet;
	}
	
	public static void afficherLobjet(int position) {
		if (position>=0 && position<10)
		{
			System.out.println("L'objet est : "+mesObjets[position]+"\n");
		}
		else
			System.out.println("Il n'y a pas d'objet dans la zone" + position + "\n");	
	}
	
	
	
	
}
