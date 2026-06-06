package com.escapegame.auth;

import com.escapegame.io.Console;

/**
 * Gère l'inscription et la connexion d'un joueur via la {@link Console} partagée.
 *
 * <p>Remplace l'ancienne {@code AuthentificationJoueur} : plus de dépôt {@code null}
 * (NullPointerException), plus de requêtes SQL incohérentes, et un dépôt injectable
 * pour les tests.</p>
 */
public class ServiceAuthentification {

    private final Console console;
    private final DepotJoueurs depot;

    public ServiceAuthentification(Console console, DepotJoueurs depot) {
        this.console = console;
        this.depot = depot;
    }

    /** Inscrit un nouveau joueur ; renvoie son pseudo une fois le compte créé. */
    public String enregistrer() {
        console.afficher("\n--- Inscription ---");
        String prenom = demander("Prénom : ");
        String nom = demander("Nom : ");

        String pseudo;
        while (true) {
            pseudo = demander("Pseudo : ");
            if (pseudo.isBlank()) {
                console.afficher("Le pseudo ne peut pas être vide.");
            } else if (depot.existe(pseudo)) {
                console.afficher("Ce pseudo est déjà pris, choisissez-en un autre.");
            } else {
                break;
            }
        }
        String motDePasse = demander("Mot de passe : ");

        depot.ajouter(prenom, nom, pseudo, motDePasse);
        console.afficher("Compte créé. Bienvenue " + prenom + " !");
        return pseudo;
    }

    /** Demande pseudo + mot de passe jusqu'à une authentification réussie. */
    public String connecter() {
        console.afficher("\n--- Connexion ---");
        while (true) {
            String pseudo = demander("Pseudo : ");
            String motDePasse = demander("Mot de passe : ");
            if (depot.verifier(pseudo, motDePasse)) {
                console.afficher("Authentification réussie !");
                return pseudo;
            }
            console.afficher("Pseudo ou mot de passe invalide. Réessayez.");
        }
    }

    private String demander(String invite) {
        console.afficherSansSaut(invite);
        return console.lireLigne();
    }
}
