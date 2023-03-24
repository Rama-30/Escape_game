package map;

import tools.Enigme;
import tools.Objets;

public class Zone {
	
	public int nbrZone=10;
	private Objets[] lesObjets;
	private Enigme[] lesEnigmes;
	
	protected Zone [] LesZones;
	
	public Zone()
	{
		LesZones = new Zone[this.nbrZone];
		int i=0;
		while(i<this.nbrZone)
		{
			LesZones[i] = new Zone();
			i++;
		}
		
	}
	
	public Zone CreerZone(int position)
	{
		return LesZones[position] = new Zone();
	}
	
	
}

