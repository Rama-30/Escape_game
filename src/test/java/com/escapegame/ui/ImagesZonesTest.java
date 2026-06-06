package com.escapegame.ui;

import com.escapegame.world.Zone;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Vérifie le rendu d'images (sans ouvrir de fenêtre — exécutable en mode headless).
 */
class ImagesZonesTest {

    @Test
    void chaqueZoneFournitUneImageNonNulle() {
        ImagesZones images = new ImagesZones();
        for (int position = 0; position < Zone.NOMBRE_ZONES; position++) {
            BufferedImage img = images.pourZone(position);
            assertNotNull(img, "image manquante pour la zone " + position);
            assertTrue(img.getWidth() > 0 && img.getHeight() > 0);
        }
    }

    @Test
    void toutesLesIllustrationsSontEmbarquees() {
        String[] fichiers = {
                "cellule", "bureau", "cantine", "salle_de_pause",
                "pontons", "bateau", "foret", "hangar"
        };
        for (String nom : fichiers) {
            assertNotNull(getClass().getResource("/images/" + nom + ".jpg"),
                    "image manquante dans le classpath : " + nom + ".jpg");
        }
    }

    @Test
    void lImageEstMiseEnCache() {
        ImagesZones images = new ImagesZones();
        assertSame(images.pourZone(0), images.pourZone(0), "même instance attendue (cache)");
    }
}
