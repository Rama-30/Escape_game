package com.escapegame.auth;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Persistance zéro-configuration des comptes joueurs dans un simple fichier CSV.
 *
 * <p>Remplace l'ancienne {@code SourceDeDonnees} basée sur MySQL : le jeu tourne
 * désormais immédiatement après un {@code git clone}, sans serveur de base de données.</p>
 *
 * <p>Les mots de passe ne sont jamais stockés en clair : on conserve un sel aléatoire et
 * l'empreinte SHA-256 de {@code sel + motDePasse}. Format d'une ligne :
 * {@code pseudo,sel(base64),hash(base64),nom,prenom}.</p>
 */
public class DepotJoueurs {

    /** Emplacement par défaut du fichier de comptes (ignoré par git). */
    public static final Path FICHIER_PAR_DEFAUT = Path.of("data", "joueurs.csv");

    private final Path fichier;
    private final Map<String, Compte> comptes = new LinkedHashMap<>();
    private final SecureRandom random = new SecureRandom();

    public DepotJoueurs() {
        this(FICHIER_PAR_DEFAUT);
    }

    public DepotJoueurs(Path fichier) {
        this.fichier = fichier;
        charger();
    }

    private record Compte(String pseudo, String selB64, String hashB64, String nom, String prenom) {
        String versLigne() {
            return String.join(",", pseudo, selB64, hashB64, nom, prenom);
        }
    }

    public boolean existe(String pseudo) {
        return comptes.containsKey(cle(pseudo));
    }

    /**
     * Enregistre un nouveau joueur. Renvoie {@code false} si le pseudo est déjà pris.
     */
    public boolean ajouter(String prenom, String nom, String pseudo, String motDePasse) {
        if (pseudo == null || pseudo.isBlank() || existe(pseudo)) {
            return false;
        }
        byte[] sel = new byte[16];
        random.nextBytes(sel);
        String selB64 = Base64.getEncoder().encodeToString(sel);
        String hashB64 = hacher(sel, motDePasse);

        Compte compte = new Compte(
                pseudo.trim(), selB64, hashB64, propre(nom), propre(prenom));
        comptes.put(cle(pseudo), compte);
        sauvegarder();
        return true;
    }

    /** Vérifie le couple pseudo / mot de passe. */
    public boolean verifier(String pseudo, String motDePasse) {
        Compte compte = comptes.get(cle(pseudo));
        if (compte == null) {
            return false;
        }
        byte[] sel = Base64.getDecoder().decode(compte.selB64());
        return MessageDigest.isEqual(
                compte.hashB64().getBytes(StandardCharsets.UTF_8),
                hacher(sel, motDePasse).getBytes(StandardCharsets.UTF_8));
    }

    private String hacher(byte[] sel, String motDePasse) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(sel);
            md.update((motDePasse == null ? "" : motDePasse).getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 indisponible", e);
        }
    }

    private void charger() {
        if (!Files.exists(fichier)) {
            return;
        }
        try {
            List<String> lignes = Files.readAllLines(fichier, StandardCharsets.UTF_8);
            for (String ligne : lignes) {
                if (ligne.isBlank()) {
                    continue;
                }
                String[] p = ligne.split(",", -1);
                if (p.length >= 5) {
                    comptes.put(cle(p[0]), new Compte(p[0], p[1], p[2], p[3], p[4]));
                }
            }
        } catch (IOException e) {
            // Système de fichiers indisponible (ex. navigateur via CheerpJ) :
            // on démarre sans comptes persistés plutôt que de planter.
            System.err.println("Comptes : lecture impossible, démarrage à vide (" + e.getMessage() + ")");
        }
    }

    private void sauvegarder() {
        try {
            if (fichier.getParent() != null) {
                Files.createDirectories(fichier.getParent());
            }
            StringBuilder sb = new StringBuilder();
            for (Compte c : comptes.values()) {
                sb.append(c.versLigne()).append(System.lineSeparator());
            }
            Files.writeString(fichier, sb.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            // En lecture seule (navigateur), on conserve les comptes en mémoire pour la session.
            System.err.println("Comptes : sauvegarde impossible, conservés en mémoire (" + e.getMessage() + ")");
        }
    }

    private static String cle(String pseudo) {
        return pseudo == null ? "" : pseudo.trim().toLowerCase();
    }

    /** Évite que les virgules ne cassent le format CSV. */
    private static String propre(String champ) {
        return champ == null ? "" : champ.replace(",", " ").trim();
    }
}
