package game;

import map.Zone;
import player.Joueur;
import player.Personnage;
import tools.Enigme;
import java.util.Scanner;

public class Partie {
	
	private int niveau=0;
	private int duree;
	private	int position;
	private Enigme enigmes;
	public Joueur Lejoueur;
	public Zone LaZone;
	
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
		//Partie partie = new Partie(); 
		Zone zones = new Zone();
		Joueur LeJoueur = new Joueur("Jasmine");
		
		Scanner scanner = new Scanner(System.in);
        String commande;

		//enigmes.repondre_enigme(partie.niveau);
		int i=0;
		System.out.println("Vous est dans :  "+zones.LesZones[position]);
		//while(i<zone.LesZones.length)
		
			LeJoueur.repondre_enigme(niveau); // il me faut un boolean si il a une bonne reponse alors il recupere l'objet
			Personnage.recupeObjet(position); // sil a une bonne reponse il recupere l'objet
			
			System.out.println("Bravo tu as reussi a sortir de la cellule maintenant choisi où tu veux aller");
			int cles=0;
	
			while(cles==3)
			{
			
				System.out.println("Tape B --> pour le bureau du commandant ");
				System.out.println("Tape C --> pour la cantine ");
				System.out.println("Tape S --> pour la salle de pause ");
				commande = scanner.nextLine();
	         
				position = Personnage.seDeplacer(commande);
	            LeJoueur.repondre_enigme(niveau);
	            Personnage.recupeObjet(position);
	            cles++;
			}
			System.out.println("Bravo tu as reussi a sortir de la prison ");
		
	}	
			

		/// methode pour savoir si il est deja entrer dans cette piece (piece de prison) 
			/*public class ZoneManager {
			    
			    private static HashMap<String, Boolean> zones = new HashMap<>();
			    
			    public static boolean entrerZone(String nomZone) {
			        if (zones.containsKey(nomZone) && zones.get(nomZone)) {
			            // Si le joueur a déjà visité la zone, il ne peut plus y entrer
			            return false;
			        } else {
			            // Sinon, on marque la zone comme visitée et on permet au joueur d'y entrer
			            zones.put(nomZone, true);
			            return true;
			        }
			    }
			    
			}
*/
			
		
		/// faire une methode sortir prison
		// trouver pontons
		
		/// trouvers sac dans le quelle recupere la pelle et objet
		/// libererMafille dans le quellle il va directement au hangard
	
	
	/// afficher enigmes 
	///repondre enigmes 
	/// methode se deplacer choisit les pieces 
	
	
	// pour les pontons if isoler 
	
	
	//ecris moi un code en java dans le quelle le joueur apres avoir sauvegarder sa partie il reprend la où il s'est arreter
}
