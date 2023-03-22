package player;

import map.*;
import tools.Objets;
import tools.Enigme;

public class Personnage // le personnage est celui qui appuis sur les commandes
{
  private final String  nom = "Marc";
  public Commande commande;
  public Zone Position;
  public Objets recup= new Objets();
  
  
  

  public String emunérationObjetrecupérés()
  {
	  String affiche = null;
    for (String objets : recup.getMesObjets()) 
    {
    	affiche="<------------liste des objets--------------->\n" + objets ;
    	System.out.println(affiche);
    }
	return affiche;
  }

  public Zone Position(Zone loc)
  {
    this.Position=loc;
    return(this.Position);
  }

  /*public static String VerifiReponse(String reponse)
  {
    String verif = "";
    	for (int i = 0; i < Enigme.reponses.length; i++) 
    	{
    		if(Enigme.reponses[i]==reponse)
  		  {
  		   verif="Bonne reponse " + reponse + "\n";
  		   return verif;
  		  }
  		  else
  		   verif="Bonne reponse " + reponse + "\n";
  		  return verif;
		}
		
	
    return reponse;
  }*/
  
  
  public static boolean contien(String[] reponses, String reponse) 
  {
	    for (String rep : reponses) 
	    {
	        if (rep.equals(reponse)) 
	        {
	            return true;
	        }
	    }
	    return false;
	}
  public static String VerifiReponse(String reponse) 
  {
	  if (contien(Enigme.reponses, reponse)) {
		    System.out.println("Bonne réponse "+ reponse + "\n");
		} else {
		    System.out.println("Mauvaise réponse " + reponse + "\n");
		}
	  
	  return reponse;
	    
	}
  
  

  
	
	


}

