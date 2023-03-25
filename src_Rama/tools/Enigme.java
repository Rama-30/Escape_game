package tools;

import java.util.Random;


public class Enigme {
	public static final String[] ENIGMES = new String[15];
	public static final String[] REPONSES = new String[15];
	public int indice= -1;
	
	

	public Enigme() {
		initEnigmes();
		initReponses();
	}

	private void initEnigmes() {

		ENIGMES[0] = "On peut me trouver au fond d’un bateau de pêche ou au milieu d’un court de tennis.\r\n"
				+ "Qui suis-je donc ?\r\n" + "A. Ancre B. poisson\r\n" + "C. Filet D. humain";
		ENIGMES[1] = "Je ne peux pas marcher, j’ai pourtant un dos et quatre pieds. Qui suis-je ?\r\n"
				+ "A. chaussure B. Meuble\r\n" + "C. sac à dos D. Chaise";
		ENIGMES[2] = "Trois poissons sont dans un seau, l’un d’entre eux meurt, combien en reste-t-il ?\r\n"
				+ "A. 2 poissons B. 3 poissons\r\n" + "C. 1 poisson D. 0 poisson";
		ENIGMES[3] = "Quelle est la figure géométrique qui n'a ni 4 ni 5 côtés, mais qui en a la moitié de 6 ?\r\n"
				+ "A. Pentagone B. Octogone\r\n" + "C. Triangle D. Hexagone";
		ENIGMES[4] = "Qu'est-ce qui est plus lourd, un kg de paille ou de fer ?\r\n"
				+ "A. Le fer B. La paille C. Les deux sont égaux";
		ENIGMES[5] = "Combien de temps peut vivre une souris ?\r\n" + "A. 2 ans B. En fonction des chats\r\n"
				+ "C. 10 ans D. 8 ans";
		ENIGMES[6] = "Je suis Sophie, mais je ne suis pas Sophie. Qui suis-je ?\r\n" + "A. Sophie B. Son chien\r\n"
				+ "C. Sa mère D. Son vélo";
		ENIGMES[7] = "Considérez la suite de chiffres suivante : 0 1 1 2 3 5 8.\n Quel chiffre suit le 8 ?";
		ENIGMES[8] = "Qu'est-ce qui t'appartient mais que les autres utilisent plus que toi ?";
		ENIGMES[9] = "Une balle et une batte coûtent ensemble 110 euros. La batte vaut 100 euros de plus que la balle. Combien coûte\r\n"
				+ "la balle ?\r\n" + "A. 5 euros la balle et 105 la batte\r\n"
				+ "B. 10 euros la balle et 100 la batte\r\n" + "C. 0 euros la balle et 110 la batte";
		ENIGMES[10] = "Combien de gouttes d’eau peut-on mettre dans un verre vide ?\r\n"
				+ "A. 1 goutte B. 200 gouttes\r\n" + "C. 0 goutte D. 10 gouttes";
		ENIGMES[11] = "Pierre et Marie sont deux enfants d'une famille nombreuse. Pierre a deux fois plus de\r\n"
				+ "sœurs que de frères et Marie a autant de frères que de sœurs. Combien d’enfants sont-ils?";
		ENIGMES[12] = "Entre 3 personnes se trouvent deux parents et deux enfants. Comment est-ce possible ?\r\n"
				+ "A. Un grand-parent, un parent et un enfant\r\n" + "B. Des triplés\r\n" + "C. Frère, Soeur et parent";
		ENIGMES[13] = "Vous êtes 12 frères et sœurs. Vous êtes le second à naître, mais vous êtes le plus petit de tous. Comment est-ce\r\n"
				+ "possible ?";
		ENIGMES[14] = "C'est le jour de la paie. La poule reçoit 7 euros. L'abeille reçoit 21 euros. L'araignée reçoit 28 euros. Combien\r\n"
				+ "reçoit le chien ?";

	}

	public void initReponses() {

		REPONSES[0] = "C";
		REPONSES[1] = "D";
		REPONSES[2] = "B";
		REPONSES[3] = "C";
		REPONSES[4] = "C";
		REPONSES[5] = "B";
		REPONSES[6] = "B";
		REPONSES[7] = "13";
		REPONSES[8] = "prénom";
		REPONSES[9] = "A";
		REPONSES[10] = "A";
		REPONSES[11] = "7";
		REPONSES[12] = "A";
		REPONSES[13] = "février";
		REPONSES[14] = "14";

	}

	public int rendIndiceEnigmeParSonNiveau(int niveau) {
		Random rand = new Random();
		int index;
		if (niveau == 1) {
			index = rand.nextInt(4);
		} else {
			if (niveau == 2) {
				index = 4 + rand.nextInt(9);
			} else {
				index = 9 + rand.nextInt(14);
			}
		}
		return index;

	}
	
	
}
