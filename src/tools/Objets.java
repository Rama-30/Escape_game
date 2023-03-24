package tools;

import map.Zone;
import player.Joueur;
import player.Personnage;

public class Objets {

	private String[] mesObjets;
	private int nbrObjet=9;
	public Zone MaZone;
	
	
	public Objets() {
		
		mesObjets = new String[this.nbrObjet];
		
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
		
	public Objets(Zone LaZone) {
		this.MaZone = LaZone;
	}

	public String[] getMesObjets() {
		return mesObjets;
	}

	
	public int getNbrObjet() {
		return nbrObjet;
	}
	
	public void afficherLobjet(int position) {
		if (position>=0 && position<this.nbrObjet)
		{
			System.out.println("L'objet est : "+mesObjets[position]+"\n");
		}
		else
			System.out.println("Il n'y a pas d'objet dans la zone" + position + "\n");	
	}
	
	

	public boolean recupeObjet(int position) // affichage de l'objet qu'il a recuperé, retourne vrai si objet
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

