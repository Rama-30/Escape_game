package map;

import tools.Enigme;
import tools.Objet;

public class Zone {
	
	public int nbrZone=10;
	//private Objet[] lesObjets;
//	private Enigme[] lesEnigmes;
	
	public String [] LesZones;
	
	public Zone()
	{
		LesZones = new String[this.nbrZone];
		
		LesZones[0]="la Cellule";
		LesZones[1]="le Bureau du commandant";
		LesZones[2]="la Cantine";
		LesZones[3]="le Salle de pause";
		LesZones[4]="le Ponton1";
		LesZones[5]="le Ponton2";
		LesZones[6]="le Ponton3";
		LesZones[7]="le Bateau";
		LesZones[8]="le Forêt";
		LesZones[9]="le Hangar";

	}
	
	
	 
	
}
