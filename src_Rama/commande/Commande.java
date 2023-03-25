package commande;

import java.util.Scanner;

public class Commande {

	public static void se_deplacer() {
		int playerX = 0; // position initiale en X du joueur
		int playerY = 0; // position initiale en Y du joueur

		// boucle pour permettre au joueur de se déplacer sur la carte
		while (true) {
			// afficher la carte et la position actuelle du joueur
			displayMap(playerX, playerY);

			// demander au joueur de saisir une direction (gauche, droite, haut ou bas)
			Scanner input = new Scanner(System.in);
			System.out.println("g= gauche/ d= droite/ g= haut et b= bas");
			System.out.print("Entrez une direction pour vous déplacer(g/d/h/b) : ");
			String direction = input.nextLine();

			// déplacer le joueur en fonction de la direction saisie
			if (direction.equals("g")) {
				playerX--;
			} else if (direction.equals("d")) {
				playerX++;
			} else if (direction.equals("h")) {
				playerY--;
			} else if (direction.equals("b")) {
				playerY++;
			} else {
				System.out.println("Direction invalide.");
			}
			input.close();
		}
		
	}

	// méthode pour afficher la carte avec la position du joueur
	public static void displayMap(int playerX, int playerY) {
		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 10; x++) {
				if (x == playerX && y == playerY) {
					System.out.print("P");
				} else {
					System.out.print(".");
				}
			}
			System.out.println();
		}
	}
}
