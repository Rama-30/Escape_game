package player;

import map.*;
import tools.Objet;
import tools.Enigme;

public class Personnage // le personnage est celui qui appuis sur les commandes
{
  private final String  nom = "Marc";
  public Commande commande;
  public int Position;
  
  
  public void recupeObjet(int position) 
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
	}
	
  public void seDeplacer(String commande) {
	  switch(commande) {
	  case "B": this.Position=1; break;
	  case "C": this.Position=2; break;
	  case "S": this.Position=3; break;
	  case "P1": this.Position=4; break;
	  case "P2": this.Position=5; break;
	  case "P3": this.Position=6; break;
	  default: System.out.println("Commande invalide");
	  }
	  
	  
  }
	


  
	


}
