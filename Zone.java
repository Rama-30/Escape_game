package map;

import tools.Enigme;
import tools.Objet;

public class Zone {
	
	public int nbrZone=10;
	//private Objet[] lesObjets;
//	private Enigme[] lesEnigmes;
	
	public Zone [] LesZones;
	
	public Zone()
	{
		LesZones = new Zone[this.nbrZone];
		int i=0;
		while(i<=this.nbrZone)
		{
			LesZones[i] = new Zone();
			i++;
		}
		
	}
	
	
	
	
}
