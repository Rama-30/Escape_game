package tools;

import map.Zone;
import player.Joueur;

public class Objet {

	private static String[] mesObjets; // tableau d'objet 
	private static int nbrObjet=9;
	public Zone MaZone;
	
	
	public static void initObjet() {
		
		mesObjets = new String[nbrObjet];
		
		mesObjets[0]="cléCellule";
		mesObjets[1]="cléBureauCommandant";
		mesObjets[2]="cléCantine";
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
	
	

	public static boolean recupeObjet(int position) // affichage de l'objet qu'il a recuperé, retourne vrai si objet
	{									//recuperé besoin pour l'inventaire
		if (Joueur.repondreEnigme()==true)
		{
			switch(position)
			{
			case 0: System.out.println("Vous avez recuperé la clé de la cellule"); break;
			case 1: System.out.println("Vous avez recuperé la clé du bureau du commandant"); break;
			case 2: System.out.println("Vous avez recuperé la clé de la cantine"); break;
			case 3: System.out.println("Vous avez recuperé la clé de la salle de pause"); break;
			case 4: System.out.println("Vous avez recuperé la carte"); break;
			case 5: System.out.println("Vous avez recuperé le téléphone"); break;
			case 6: System.out.println("Vous avez recuperé la pelle"); break;
			case 7: System.out.println("Vous avez recuperé la boisson energisante"); break;
			case 8: System.out.println("Vous avez recuperé le sac"); break;
			}
			return true;
		}
		else 
			return false; 
		
	}
	
	
	
}
