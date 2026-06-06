package com.escapegame.ui;

import com.escapegame.auth.DepotJoueurs;
import com.escapegame.game.Partie;
import com.escapegame.io.EntreeSortie;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Interface graphique Swing : une fenêtre unique affiche l'image de la zone, le récit
 * et les énigmes, et recueille les réponses du joueur dans un champ de saisie.
 *
 * <p>Implémente {@link EntreeSortie} : c'est exactement la même interface que la console,
 * donc le moteur {@link Partie} fonctionne sans modification. Le moteur s'exécute sur un
 * thread dédié ; les saisies transitent par une file bloquante alimentée par l'EDT,
 * tandis que tous les affichages sont renvoyés sur l'EDT via {@code invokeLater}.</p>
 */
public class FenetreJeu extends JFrame implements EntreeSortie {

    private static final long serialVersionUID = 1L;

    // Thème sombre, pour s'harmoniser avec les illustrations.
    private static final Color FOND = new Color(13, 13, 16);
    private static final Color FOND_CHAMP = new Color(28, 28, 34);
    private static final Color TEXTE = new Color(233, 233, 238);
    private static final Color ACCENT = new Color(196, 132, 60);

    private final PanneauImage panneauImage = new PanneauImage();
    private final JTextArea zoneTexte = new JTextArea();
    private final JTextField champSaisie = new JTextField();
    private final JButton boutonValider = new JButton("Valider");

    private final ImagesZones images = new ImagesZones();
    private final BlockingQueue<String> fileSaisie = new LinkedBlockingQueue<>();

    public FenetreJeu() {
        super("Escape Game — L'Évasion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        construireInterface();
        setMinimumSize(new Dimension(840, 760));
        pack();
        setLocationRelativeTo(null);
    }

    private void construireInterface() {
        getContentPane().setBackground(FOND);
        panneauImage.setPreferredSize(new Dimension(820, 440));

        zoneTexte.setEditable(false);
        zoneTexte.setLineWrap(true);
        zoneTexte.setWrapStyleWord(true);
        zoneTexte.setFont(new Font("SansSerif", Font.PLAIN, 15));
        zoneTexte.setBackground(FOND);
        zoneTexte.setForeground(TEXTE);
        zoneTexte.setCaretColor(TEXTE);
        zoneTexte.setSelectionColor(ACCENT);
        zoneTexte.setSelectedTextColor(Color.WHITE);
        zoneTexte.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));

        JScrollPane defilement = new JScrollPane(zoneTexte);
        defilement.setPreferredSize(new Dimension(820, 200));
        defilement.setBorder(BorderFactory.createEmptyBorder());
        defilement.getViewport().setBackground(FOND);

        champSaisie.setFont(new Font("SansSerif", Font.PLAIN, 15));
        champSaisie.setBackground(FOND_CHAMP);
        champSaisie.setForeground(TEXTE);
        champSaisie.setCaretColor(TEXTE);
        champSaisie.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(55, 55, 64), 1, true),
                BorderFactory.createEmptyBorder(7, 10, 7, 10)));

        boutonValider.setFont(new Font("SansSerif", Font.BOLD, 14));
        boutonValider.setBackground(ACCENT);
        boutonValider.setForeground(new Color(20, 16, 10));
        boutonValider.setFocusPainted(false);
        boutonValider.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        boutonValider.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // Entrée ou clic sur Valider → on soumet la saisie.
        champSaisie.addActionListener(e -> soumettre());
        boutonValider.addActionListener(e -> soumettre());

        JPanel barreSaisie = new JPanel(new BorderLayout(10, 0));
        barreSaisie.setBackground(FOND);
        barreSaisie.setBorder(BorderFactory.createEmptyBorder(4, 12, 12, 12));
        barreSaisie.add(champSaisie, BorderLayout.CENTER);
        barreSaisie.add(boutonValider, BorderLayout.EAST);

        JPanel bas = new JPanel();
        bas.setBackground(FOND);
        bas.setLayout(new BoxLayout(bas, BoxLayout.Y_AXIS));
        bas.add(defilement);
        bas.add(barreSaisie);

        add(panneauImage, BorderLayout.CENTER);
        add(bas, BorderLayout.SOUTH);
    }

    /** Crée la fenêtre puis lance la partie sur un thread dédié. */
    public static void lancer() {
        SwingUtilities.invokeLater(() -> {
            FenetreJeu fenetre = new FenetreJeu();
            fenetre.setVisible(true);
            fenetre.demarrerPartie();
        });
    }

    private void demarrerPartie() {
        Thread moteur = new Thread(() -> {
            try {
                new Partie(this, new DepotJoueurs(), new Random()).jouer();
            } finally {
                terminer();
            }
        }, "moteur-jeu");
        moteur.setDaemon(true);
        moteur.start();
    }

    private void soumettre() {
        String texte = champSaisie.getText();
        champSaisie.setText("");
        ajouterTexte(texte + "\n");     // écho de la saisie
        fileSaisie.offer(texte);
    }

    private void terminer() {
        SwingUtilities.invokeLater(() -> {
            ajouterTexte("\n— Partie terminée. Fermez la fenêtre pour quitter. —\n");
            champSaisie.setEnabled(false);
            boutonValider.setEnabled(false);
        });
    }

    // ---------- Implémentation de EntreeSortie ----------

    @Override
    public void afficher(String message) {
        ajouterTexte(message + "\n");
    }

    @Override
    public void afficherSansSaut(String message) {
        ajouterTexte(message);
    }

    @Override
    public String lireLigne() {
        try {
            return fileSaisie.take();   // bloque le thread moteur jusqu'à une saisie
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "";
        }
    }

    @Override
    public int lireEntier() {
        while (true) {
            String ligne = lireLigne().trim();
            try {
                return Integer.parseInt(ligne);
            } catch (NumberFormatException e) {
                afficher("Veuillez saisir un nombre entier valide.");
            }
        }
    }

    @Override
    public void afficherZone(int position) {
        SwingUtilities.invokeLater(() -> panneauImage.definirImage(images.pourZone(position)));
    }

    private void ajouterTexte(String texte) {
        SwingUtilities.invokeLater(() -> {
            zoneTexte.append(texte);
            zoneTexte.setCaretPosition(zoneTexte.getDocument().getLength());
        });
    }

    /** Panneau qui peint l'image courante, mise à l'échelle et centrée (letterbox). */
    private static final class PanneauImage extends JPanel {
        private static final long serialVersionUID = 1L;
        private transient BufferedImage image;

        PanneauImage() {
            setBackground(Color.BLACK);
        }

        void definirImage(BufferedImage image) {
            this.image = image;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image == null) {
                return;
            }
            int pw = getWidth();
            int ph = getHeight();
            double echelle = Math.min((double) pw / image.getWidth(), (double) ph / image.getHeight());
            int w = (int) Math.round(image.getWidth() * echelle);
            int h = (int) Math.round(image.getHeight() * echelle);
            int x = (pw - w) / 2;
            int y = (ph - h) / 2;
            g.drawImage(image, x, y, w, h, this);
        }
    }
}
