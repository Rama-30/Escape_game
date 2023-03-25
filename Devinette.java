package tools;
import java.util.Random;
import java.util.Scanner;

public class Devinette {


	        Random random = new Random();
	        int nombreAleatoire = random.nextInt(100) + 1;
	        Scanner scanner = new Scanner(System.in);
	        int nombrePropose = 0;
	        int nombreEssais = 0;
	        boolean trouve = false;

	        System.out.println("Je pense à un nombre entre 1 et 100. Devinez-le !");

	        while (!trouve) {
	            System.out.print("Proposez un nombre : ");
	            nombrePropose = scanner.nextInt();
	            nombreEssais++;

	            if (nombrePropose == nombreAleatoire) {
	                trouve = true;
	                System.out.println("Bravo ! Vous avez trouvé le nombre en " + nombreEssais + " essais.");
	            } else if (nombrePropose < nombreAleatoire) {
	                System.out.println("Le nombre que je pense est plus grand que " + nombrePropose + ".");
	            } else {
	                System.out.println("Le nombre que je pense est plus petit que " + nombrePropose + ".");
	            }
	        }

	        scanner.close();
	    }

	}
}
