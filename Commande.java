package Commande;


public class Commande {
	
	public String Sauvegarde="S";
	public String Fin="F";

	
}
Voici un exemple de code Java qui permet à un joueur de se déplacer en fonction de la commande qu'il choisit :

csharp
Copy code
import java.util.Scanner;

public class Joueur {
    private int x;
    private int y;
    
    public Joueur(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void deplacer(String commande) {
        switch(commande) {
            case "haut":
                y--;
                break;
            case "bas":
                y++;
                break;
            case "gauche":
                x--;
                break;
            case "droite":
                x++;
                break;
            default:
                System.out.println("Commande invalide.");
        }
    }
    
    public void afficherPosition() {
        System.out.println("Position du joueur : (" + x + ", " + y + ")");
    }
    
    public static void main(String[] args) {
        Joueur joueur = new Joueur(0, 0);
        Scanner scanner = new Scanner(System.in);
        String commande;
        
        do {
            System.out.println("Entrez une commande (haut, bas, gauche, droite) ou 'quitter' pour terminer :");
            commande = scanner.nextLine();
            joueur.deplacer(commande);
            joueur.afficherPosition();
        } while(!commande.equals("quitter"));
    }
}
Explication du code :

La classe Joueur représente le joueur et sa position est définie par les variables x et y.
Le constructeur Joueur(int x, int y) permet de créer un joueur avec une position initiale spécifiée.
La méthode deplacer(String commande) permet de déplacer le joueur en fonction de la commande spécifiée. Cette méthode utilise une instruction switch pour exécuter une action en fonction de la commande choisie (haut, bas, gauche, droite). Si la commande n'est pas valide, un message d'erreur est affiché.
La méthode afficherPosition() permet d'afficher la position actuelle du joueur.
La méthode main(String[] args) est le point d'entrée du programme. Elle crée un joueur avec une position initiale de (0, 0) et utilise un objet Scanner pour permettre à l'utilisateur de saisir des commandes. Le programme boucle jusqu'à ce que l'utilisateur entre la commande "quitter". À chaque itération de la boucle, la méthode deplacer(String commande) est appelée pour déplacer le joueur en fonction de la commande entrée, puis la méthode afficherPosition() est appelée pour afficher la nouvelle position du joueur.
Notez que ce code est un exemple très simple et ne gère pas les erreurs ou les situations où le joueur sortirait de la zone de jeu. Dans un jeu plus complexe, il faudrait inclure une logique plus élaborée pour gérer ces cas.

