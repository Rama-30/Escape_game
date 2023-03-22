package player;

import map.*;
import tools.Objets;
import tools.Enigme;

public class Personnage // le personnage est celui qui appuis sur les commandes
{
  private String nom = "Marc";
  public Commande commande;
  public Zone Position= null;
  public Objets recup= new Objets();
  private int nbObjet;
  
  

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

  public String VerifiReponse(String reponse)
  {
    String verif = null;
    try {
		for(int i=0; i < 15; i++)
		{
		  if(Enigme.reponses[i]==reponse)
		  {
		   verif="Bonne reponse " + reponse + "\n";
		  }
		  else
		   verif="Bonne reponse " + reponse + "\n";
		}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 
    return verif;
  }

  public String getNom() 
  {
	return nom;
  }

  public void setNom(String nom) 
	{
		this.nom = nom;
	}

  public int getNbObjet()
	{
		return nbObjet;
	}

  public void setNbObjet(int nbObjet) 
	{
		this.nbObjet = nbObjet;
	}
	
	


}
