package com.escapegame.ui;

import com.escapegame.world.Zone;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Fournit une image pour chaque zone du jeu.
 *
 * <p>Trois zones disposent d'une vraie photo (récupérée du projet d'origine, embarquée
 * dans le jar) ; les autres sont rendues à la volée en panneaux dégradés portant le nom
 * de la zone. Toutes les images ont ainsi le même format, sans dépendance externe.</p>
 */
public class ImagesZones {

    private static final int LARGEUR = 800;
    private static final int HAUTEUR = 460;

    /** Photos disponibles, par position de zone. */
    private static final Map<Integer, String> PHOTOS = Map.of(
            1, "/images/bureau.jpg",
            2, "/images/cantine.jpg",
            7, "/images/bateau.jpg"
    );

    /** Deux couleurs de dégradé par zone, pour différencier les visuels générés. */
    private static final Color[][] THEMES = {
            {new Color(40, 44, 60), new Color(20, 22, 33)},     // 0 cellule
            {new Color(60, 50, 40), new Color(33, 27, 20)},     // 1 bureau
            {new Color(45, 60, 45), new Color(24, 33, 24)},     // 2 cantine
            {new Color(55, 45, 60), new Color(28, 22, 33)},     // 3 salle de pause
            {new Color(30, 55, 70), new Color(16, 30, 40)},     // 4 ponton 1
            {new Color(30, 60, 75), new Color(16, 32, 42)},     // 5 ponton 2
            {new Color(30, 65, 80), new Color(16, 34, 44)},     // 6 ponton 3
            {new Color(25, 50, 75), new Color(14, 28, 42)},     // 7 bateau
            {new Color(30, 60, 35), new Color(16, 33, 18)},     // 8 forêt
            {new Color(70, 40, 35), new Color(38, 20, 18)}      // 9 hangar
    };

    private final Map<Integer, BufferedImage> cache = new HashMap<>();

    /** Renvoie l'image de la zone (photo si disponible, sinon visuel généré). Jamais nulle. */
    public BufferedImage pourZone(int position) {
        return cache.computeIfAbsent(position, this::construire);
    }

    private BufferedImage construire(int position) {
        BufferedImage photo = chargerPhoto(position);
        return (photo != null) ? photo : genererVisuel(position);
    }

    private BufferedImage chargerPhoto(int position) {
        String chemin = PHOTOS.get(position);
        if (chemin == null) {
            return null;
        }
        try (InputStream in = getClass().getResourceAsStream(chemin)) {
            return (in == null) ? null : ImageIO.read(in);
        } catch (IOException e) {
            return null; // on retombera sur le visuel généré
        }
    }

    private BufferedImage genererVisuel(int position) {
        BufferedImage img = new BufferedImage(LARGEUR, HAUTEUR, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Color[] theme = THEMES[Math.floorMod(position, THEMES.length)];
        g.setPaint(new GradientPaint(0, 0, theme[0], 0, HAUTEUR, theme[1]));
        g.fillRect(0, 0, LARGEUR, HAUTEUR);

        String titre = Zone.nom(position);
        g.setColor(new Color(255, 255, 255, 235));
        g.setFont(new Font("SansSerif", Font.BOLD, 44));
        dessinerCentre(g, titre, HAUTEUR / 2);

        g.setColor(new Color(255, 255, 255, 140));
        g.setFont(new Font("SansSerif", Font.PLAIN, 18));
        dessinerCentre(g, "Escape Game — L'Évasion", HAUTEUR / 2 + 44);

        g.dispose();
        return img;
    }

    private void dessinerCentre(Graphics2D g, String texte, int y) {
        FontMetrics fm = g.getFontMetrics();
        int x = (LARGEUR - fm.stringWidth(texte)) / 2;
        g.drawString(texte, x, y);
    }
}
