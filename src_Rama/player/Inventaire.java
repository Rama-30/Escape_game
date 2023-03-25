package player;
import java.util.ArrayList;

public class Inventaire {

	private ArrayList<String> objets;
	
	public Inventaire() {
		objets = new ArrayList<String>();
	}
	
	public void ajouterObjet(String ajout) {
		
		objets.add(ajout);
	}
	
	public void afficherInventaire() {
		System.out.println("Les objets recuperer : \n ");
		for(String objet : objets) {
			System.out.println("- "+ objet);
		}
	}
	
	
}

