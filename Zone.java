package map;

import player.Inventaire;
import tools.Objet;

public class Zone {
	
	
	public static final int NBR_ZONES = 10;
	private Objet[] objets;
	private String nomZone;

	
	public Zone(String nom, Objet[] objets) {
	    this.nomZone = nom;
	    this.objets = objets;
	}

	

	public Objet[] getObjets() {
		return objets;
	}


	public String getNomZone() {
		return this.nomZone;
	}


	public static int getNbrZones() {
		return NBR_ZONES;
	}

	//méthode qui permet d'ajouter les objets récupérés de la zone
	public void explorer() {
		System.out.println("\n----------------------------------------------------------------------------------\n");
	    System.out.println("Exploration de la zone : " + nomZone+"\n");
	    Inventaire inventaire = new Inventaire();
	    for (Objet objet : objets) {
	        inventaire.ajouterObjet(objet);
	    }
	    inventaire.afficherInventaire();
	    System.out.println("----------------------------------------------------------------------------------\n");
	}

	public static Zone[] initZones() {
		Zone[] zones = new Zone[NBR_ZONES];
	    zones[0] = new Zone("la Cellule", new Objet[] { new Objet("la clé", "ouvre la porte de la cellule") });
	    zones[1] = new Zone("le Bureau du commandant", new Objet[] { new Objet("clé", "ouvre la porte de la prison") });
	    zones[2] = new Zone("la Cantine", new Objet[] { new Objet("la clé", "ouvre la porte du hall") });
	    zones[3] = new Zone("le Salle de pause", new Objet[] { new Objet("la clé", "ouvre la porte d'un couloir") });
	    zones[4] = new Zone("le Ponton1", new Objet[] { new Objet("le bateau", "contient la carte et le portable qui te permettrront de localiser le sac") });
	    zones[5] = new Zone("le Ponton2", new Objet[] { new Objet("le bateau", "contient la carte et le portable qui te permettrront de localiser le sac")});
	    zones[6] = new Zone("le Ponton3", new Objet[] { new Objet("le bateau", "contient la carte et le portable qui te permettrront de localiser le sac") });
	    zones[7] = new Zone("le Bateau", new Objet[] { new Objet("la carte","en le déchiffrant, te permettra de trouver l'adresse du sac"), new Objet("le portable","te permettra de géolocaliser le sac")});
	    zones[8] = new Zone("la forêt", new Objet[] { new Objet("la pelle", "va te permettre de creuser pour récupérer le sac"), new Objet("la boisson énergisante", "pour reprendre des forces"), new Objet("le sac", "devra être remis aux kidnappeurs pour libérer la fille de Mark") });
	    zones[9] = new Zone("le hangar", new Objet[] { new Objet("la fille", "te permet de gagner la partie") });


	    return zones;

	}
	
	
	 
	
}
