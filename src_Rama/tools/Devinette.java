package tools;

import java.util.Scanner;

public class Devinette {
	
    public static void jeuDeDevinette(){
        int nombreAleatoire = (int) (Math.random() * 100) + 1;
        Scanner scanner = new Scanner(System.in);
        int tentative = 0;
        int guess = 0;

        System.out.println("Bienvenue dans le jeu de devinette! Trouvez un nombre compris entre 1 et 100.");
        
        while (guess != nombreAleatoire) {
            System.out.print("Entrez votre devinette : ");
            guess = scanner.nextInt();
            tentative++;
            
            if (guess < nombreAleatoire) {
                System.out.println("Trop petit.");
            } else if (guess > nombreAleatoire) {
                System.out.println("Trop grand.");
            } else {
                System.out.println("Bravo! Vous avez trouvé le nombre en " + tentative + " tentatives.");
            }
        }
        scanner.close();
    }
}
