package map;

import tools.Enigmes;
import tools.Objet;

public class Zone {
	
	public int nbrZone=10;
	private Objet[] lesObjets;
	private Enigmes[] lesEnigmes;
	
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
