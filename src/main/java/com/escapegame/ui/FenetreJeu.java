package com.escapegame.ui;

import com.escapegame.auth.DepotJoueurs;
import com.escapegame.game.Partie;
import com.escapegame.io.EntreeSortie;
import com.escapegame.world.Zone;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Interface graphique Swing : une fenêtre unique affiche l'image de la zone, le récit
 * et les énigmes, et recueille les réponses du joueur dans un champ de saisie.
 *
 * <p>Implémente {@link EntreeSortie} : c'est exactement la même interface que la console,
 * donc le moteur {@link Partie} fonctionne sans modification. Le moteur s'exécute sur un
 * thread dédié ; les saisies transitent par une file bloquante alimentée par l'EDT.</p>
 *
 * <p>Le texte est rendu dans un {@link JTextPane} stylé : police soignée, interligne aéré,
 * marges, et code couleur selon le type de message (récit, invite, réussite, échec, saisie
 * du joueur). Deux animations rythment la partie : le texte s'écrit lettre par lettre (le
 * thread moteur attend la fin de la frappe ; un clic accélère l'affichage), et chaque
 * changement d'étape se fait en fondu enchaîné avec un carton-titre de zone.</p>
 */
public class FenetreJeu extends JFrame implements EntreeSortie {

    private static final long serialVersionUID = 1L;

    // Thème sombre, pour s'harmoniser avec les illustrations.
    private static final Color FOND = new Color(13, 13, 16);
    private static final Color FOND_CHAMP = new Color(28, 28, 34);
    private static final Color TEXTE = new Color(233, 233, 238);
    private static final Color ACCENT = new Color(208, 150, 74);
    private static final Color VERT = new Color(126, 197, 122);
    private static final Color ROUGE = new Color(226, 122, 108);
    private static final Color GRIS = new Color(150, 150, 160);

    /** Délai entre deux caractères de l'effet machine à écrire. */
    private static final int DELAI_FRAPPE_MS = 70;
    private static final int TAILLE_TEXTE = 16;

    /** Pause sur le texte (le temps de lire) avant de passer à l'étape suivante. */
    private static final int PAUSE_ETAPE_MS = 4000;

    private final PanneauImage panneauImage = new PanneauImage();
    private final JTextPane zoneTexte = new JTextPane();
    private final JTextField champSaisie = new JTextField();
    private final JButton boutonValider = new JButton("Valider");

    private final com.escapegame.ui.ImagesZones images = new com.escapegame.ui.ImagesZones();
    private final BlockingQueue<String> fileSaisie = new LinkedBlockingQueue<>();

    // Styles de texte (police soignée + code couleur).
    private AttributeSet styleNormal;
    private AttributeSet stylePositif;
    private AttributeSet styleNegatif;
    private AttributeSet styleInvite;
    private AttributeSet styleEcho;

    // État de la frappe en cours (manipulé uniquement sur l'EDT).
    private Timer timerFrappe;
    private String resteAFrapper = "";
    private AttributeSet styleFrappe;
    private CountDownLatch frappeTerminee;
    private volatile boolean toutAfficher;
    private volatile boolean premiereEtape = true;

    public FenetreJeu() {
        super("Escape Game — L'Évasion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        construireInterface();
        setMinimumSize(new Dimension(860, 780));
        pack();
        setLocationRelativeTo(null);
    }

    private void construireInterface() {
        getContentPane().setBackground(FOND);
        panneauImage.setPreferredSize(new Dimension(840, 450));

        construireStyles();

        zoneTexte.setEditable(false);
        zoneTexte.setBackground(FOND);
        zoneTexte.setForeground(TEXTE);
        zoneTexte.setCaretColor(FOND);
        zoneTexte.setSelectionColor(new Color(70, 60, 40));
        zoneTexte.setSelectedTextColor(Color.WHITE);
        zoneTexte.setMargin(new Insets(18, 24, 18, 24));
        zoneTexte.setToolTipText("Cliquez pour accélérer le texte");

        JScrollPane defilement = new JScrollPane(zoneTexte);
        defilement.setPreferredSize(new Dimension(840, 210));
        defilement.setBorder(BorderFactory.createEmptyBorder());
        defilement.getViewport().setBackground(FOND);
        defilement.getVerticalScrollBar().setUnitIncrement(16);

        String policeUi = choisirPolice("Noto Sans", "DejaVu Sans", "Segoe UI", "SansSerif");
        champSaisie.setFont(new Font(policeUi, Font.PLAIN, 15));
        champSaisie.setBackground(FOND_CHAMP);
        champSaisie.setForeground(TEXTE);
        champSaisie.setCaretColor(TEXTE);
        champSaisie.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 70), 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        boutonValider.setFont(new Font(policeUi, Font.BOLD, 14));
        boutonValider.setBackground(ACCENT);
        boutonValider.setForeground(new Color(22, 17, 10));
        boutonValider.setFocusPainted(false);
        boutonValider.setBorder(BorderFactory.createEmptyBorder(9, 20, 9, 20));
        boutonValider.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Entrée ou clic sur Valider → on soumet la saisie.
        champSaisie.addActionListener(e -> soumettre());
        boutonValider.addActionListener(e -> soumettre());

        // Un clic sur le texte ou l'image accélère la frappe en cours.
        MouseAdapter accelerer = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                toutAfficher = true;
                finirFrappe();
            }
        };
        zoneTexte.addMouseListener(accelerer);
        panneauImage.addMouseListener(accelerer);

        JPanel barreSaisie = new JPanel(new BorderLayout(10, 0));
        barreSaisie.setBackground(FOND);
        barreSaisie.setBorder(BorderFactory.createEmptyBorder(6, 16, 14, 16));
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

    /** Construit les styles de texte et l'espacement des paragraphes. */
    private void construireStyles() {
        String police = choisirPolice("Noto Serif", "DejaVu Serif", "Georgia", "Serif");

        styleNormal = style(police, TEXTE, false, false);
        stylePositif = style(police, VERT, true, false);
        styleNegatif = style(police, ROUGE, true, false);
        styleInvite = style(police, ACCENT, true, false);
        styleEcho = style(police, GRIS, false, true);

        // Interligne aéré et espace entre les blocs, appliqués à tout le texte.
        StyledDocument doc = zoneTexte.getStyledDocument();
        Style defaut = doc.getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setLineSpacing(defaut, 0.32f);
        StyleConstants.setSpaceBelow(defaut, 9f);
        StyleConstants.setLeftIndent(defaut, 2f);
    }

    private static AttributeSet style(String police, Color couleur, boolean gras, boolean italique) {
        SimpleAttributeSet s = new SimpleAttributeSet();
        StyleConstants.setFontFamily(s, police);
        StyleConstants.setFontSize(s, TAILLE_TEXTE);
        StyleConstants.setForeground(s, couleur);
        StyleConstants.setBold(s, gras);
        StyleConstants.setItalic(s, italique);
        return s;
    }

    /** Renvoie la première police installée parmi les préférences (dernier = repli logique). */
    private static String choisirPolice(String... preferences) {
        var dispo = Arrays.asList(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        for (String p : preferences) {
            if (dispo.contains(p)) {
                return p;
            }
        }
        return preferences[preferences.length - 1];
    }

    /** Crée la fenêtre puis lance la partie sur un thread dédié. */
    public static void lancer() {
        SwingUtilities.invokeLater(() -> {
            FenetreJeu fenetre = new FenetreJeu();
            fenetre.setVisible(true);
            fenetre.afficherAccueil();
            fenetre.demarrerPartie();
        });
    }

    /** Affiche l'écran d'accueil (image-titre) avant le début de la partie. */
    private void afficherAccueil() {
        panneauImage.afficherAccueil(images.accueil());
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
        toutAfficher = false;                  // la prochaine salve se réécrit lettre par lettre
        ajouterTexte(texte + "\n", styleEcho); // écho de la saisie (immédiat)
        fileSaisie.offer(texte);
    }

    private void terminer() {
        SwingUtilities.invokeLater(() -> {
            ajouterTexte("\n— Partie terminée. Fermez la fenêtre pour quitter. —\n", styleEcho);
            champSaisie.setEnabled(false);
            boutonValider.setEnabled(false);
        });
    }

    // ---------- Implémentation de EntreeSortie ----------

    @Override
    public void afficher(String message) {
        frapper(message + "\n", stylePour(message));
    }

    @Override
    public void afficherSansSaut(String message) {
        frapper(message, styleInvite);
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
        BufferedImage img = images.pourZone(position);
        String titre = Zone.nom(position);
        // On lance d'abord la transition (fondu + carton-titre)...
        SwingUtilities.invokeLater(() -> {
            separerEtape();
            panneauImage.changerZone(img, titre);
        });
        // ...puis on marque la pause : l'animation se joue pendant ce temps, et le joueur
        // a le temps de lire avant que le texte de la nouvelle étape ne s'écrive.
        // (Pas de pause tout au début de la partie.) Appelé sur le thread moteur, l'EDT reste libre.
        if (premiereEtape) {
            premiereEtape = false;
        } else {
            pauseEtape();
        }
    }

    /** Pause de lecture, interruptible par un clic (accélération). */
    private void pauseEtape() {
        long fin = System.currentTimeMillis() + PAUSE_ETAPE_MS;
        while (System.currentTimeMillis() < fin && !toutAfficher) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    /** Insère deux lignes vides entre deux étapes (zones) du jeu. */
    private void separerEtape() {
        StyledDocument doc = zoneTexte.getStyledDocument();
        if (doc.getLength() > 0) {
            ajouterTexte("\n\n", styleNormal);
        }
    }

    /** Choisit la couleur du message : rouge pour un échec, vert pour une réussite. */
    private AttributeSet stylePour(String message) {
        String m = message.toLowerCase();
        // Échec d'abord : un message d'échec a priorité même s'il contient un mot « positif ».
        if (contient(m, "perdue", "perdu", "raté", "reste close", "alarme", "repris", "reprennent",
                "verrouillé", "indéchiffrable", "perdez", "trop de temps", "désolé", "mauvais")) {
            return styleNegatif;
        }
        if (contient(m, "bravo", "bonne réponse", "réussi", "récupér", "montez à bord",
                "gagnez", "s'ouvre", "cède", "clés en poche", "libres")) {
            return stylePositif;
        }
        return styleNormal;
    }

    private static boolean contient(String texte, String... motsCles) {
        for (String mot : motsCles) {
            if (texte.contains(mot)) {
                return true;
            }
        }
        return false;
    }

    // ---------- Effet machine à écrire ----------

    /** Écrit le texte caractère par caractère (dans le style donné) et bloque jusqu'à la fin. */
    private void frapper(String texte, AttributeSet style) {
        CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(() -> demarrerFrappe(texte, style, latch));
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void demarrerFrappe(String texte, AttributeSet style, CountDownLatch latch) {
        frappeTerminee = latch;
        styleFrappe = style;
        if (toutAfficher) {                 // accéléré : on affiche d'un coup
            resteAFrapper = "";
            ajouterTexte(texte, style);
            libererFrappe();
            return;
        }
        resteAFrapper = texte;
        timerFrappe = new Timer(DELAI_FRAPPE_MS, e -> {
            if (resteAFrapper.isEmpty() || toutAfficher) {
                finirFrappe();
                return;
            }
            ajouterTexte(resteAFrapper.substring(0, 1), styleFrappe);
            resteAFrapper = resteAFrapper.substring(1);
        });
        timerFrappe.start();
    }

    /** Termine immédiatement la frappe en cours (fin naturelle ou accélération). */
    private void finirFrappe() {
        if (timerFrappe != null) {
            timerFrappe.stop();
            timerFrappe = null;
        }
        if (!resteAFrapper.isEmpty()) {
            ajouterTexte(resteAFrapper, styleFrappe);
            resteAFrapper = "";
        }
        libererFrappe();
    }

    private void libererFrappe() {
        if (frappeTerminee != null) {
            frappeTerminee.countDown();
            frappeTerminee = null;
        }
    }

    private void ajouterTexte(String texte, AttributeSet style) {
        try {
            StyledDocument doc = zoneTexte.getStyledDocument();
            doc.insertString(doc.getLength(), texte, style);
            zoneTexte.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            // position toujours valide (fin du document) : ne devrait pas survenir
        }
    }

    /**
     * Panneau d'image : peint l'illustration courante (mise à l'échelle, centrée), gère
     * le fondu enchaîné lors d'un changement de zone et le carton-titre de la zone.
     */
    private static final class PanneauImage extends JPanel {
        private static final long serialVersionUID = 1L;
        private static final long DUREE_TITRE_MS = 1900;

        private transient BufferedImage imageActuelle;
        private transient BufferedImage imagePrecedente;
        private float alphaFondu = 1f;

        private transient String titre;
        private long titreDebut;

        private transient Timer timerFondu;
        private transient Timer timerTitre;

        PanneauImage() {
            setBackground(Color.BLACK);
        }

        /** Affiche l'image d'accueil en fondu (sans surimpression : l'image porte son titre). */
        void afficherAccueil(BufferedImage image) {
            this.imagePrecedente = null;
            this.imageActuelle = image;
            this.alphaFondu = 0f;
            demarrerFondu();
        }

        void changerZone(BufferedImage image, String titre) {
            this.imagePrecedente = this.imageActuelle;
            this.imageActuelle = image;
            this.alphaFondu = 0f;
            this.titre = (titre == null) ? null : titre.toUpperCase();
            this.titreDebut = System.currentTimeMillis();
            demarrerFondu();
            demarrerTitre();
        }

        private void demarrerFondu() {
            if (timerFondu != null) {
                timerFondu.stop();
            }
            timerFondu = new Timer(16, e -> {
                alphaFondu += 0.05f;
                if (alphaFondu >= 1f) {
                    alphaFondu = 1f;
                    imagePrecedente = null;
                    timerFondu.stop();
                }
                repaint();
            });
            timerFondu.start();
        }

        private void demarrerTitre() {
            if (timerTitre != null) {
                timerTitre.stop();
            }
            timerTitre = new Timer(16, e -> {
                if (System.currentTimeMillis() - titreDebut > DUREE_TITRE_MS) {
                    titre = null;
                    timerTitre.stop();
                }
                repaint();
            });
            timerTitre.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            if (imagePrecedente != null && alphaFondu < 1f) {
                dessinerImage(g2, imagePrecedente, 1f - alphaFondu);
            }
            if (imageActuelle != null) {
                dessinerImage(g2, imageActuelle, alphaFondu);
            }
            if (titre != null) {
                float a = alphaTitre(System.currentTimeMillis() - titreDebut);
                if (a > 0f) {
                    dessinerTitre(g2, titre, a);
                }
            }
            g2.dispose();
        }

        private void dessinerImage(Graphics2D g2, BufferedImage img, float alpha) {
            float a = Math.max(0f, Math.min(1f, alpha));
            Composite ancien = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a));
            int pw = getWidth();
            int ph = getHeight();
            double echelle = Math.min((double) pw / img.getWidth(), (double) ph / img.getHeight());
            int w = (int) Math.round(img.getWidth() * echelle);
            int h = (int) Math.round(img.getHeight() * echelle);
            g2.drawImage(img, (pw - w) / 2, (ph - h) / 2, w, h, this);
            g2.setComposite(ancien);
        }

        private void dessinerTitre(Graphics2D g2, String texte, float alpha) {
            int pw = getWidth();
            int bandeH = 66;
            int bandeY = getHeight() / 2 - bandeH / 2;

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f * alpha));
            g2.setColor(Color.BLACK);
            g2.fillRect(0, bandeY, pw, bandeH);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Serif", Font.BOLD, 34));
            FontMetrics fm = g2.getFontMetrics();
            int tx = (pw - fm.stringWidth(texte)) / 2;
            int ty = bandeY + (bandeH + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(texte, tx, ty);
        }

        /** Courbe d'opacité du carton-titre : apparition, maintien, disparition. */
        private float alphaTitre(long ecoule) {
            if (ecoule < 300) {
                return ecoule / 300f;
            }
            if (ecoule < 1300) {
                return 1f;
            }
            if (ecoule < DUREE_TITRE_MS) {
                return 1f - (ecoule - 1300) / 600f;
            }
            return 0f;
        }
    }
}
