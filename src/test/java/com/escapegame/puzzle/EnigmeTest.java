package com.escapegame.puzzle;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnigmeTest {

    @Test
    void indicePourNiveauResteToujoursDansLesBornes() {
        Enigme enigme = new Enigme(new Random(1));
        for (int niveau = 1; niveau <= 3; niveau++) {
            for (int i = 0; i < 1000; i++) {
                int indice = enigme.indicePourNiveau(niveau);
                assertTrue(indice >= 0 && indice < enigme.nombreEnigmes(),
                        "indice hors bornes pour niveau " + niveau + " : " + indice);
            }
        }
    }

    @Test
    void chaqueNiveauVisiteSaPropreSousPlage() {
        Enigme enigme = new Enigme(new Random(42));
        assertSousPlage(enigme, 1, 0, 4);
        assertSousPlage(enigme, 2, 5, 9);
        assertSousPlage(enigme, 3, 10, 14);
    }

    private void assertSousPlage(Enigme enigme, int niveau, int min, int max) {
        for (int i = 0; i < 500; i++) {
            int indice = enigme.indicePourNiveau(niveau);
            assertTrue(indice >= min && indice <= max,
                    "niveau " + niveau + " a renvoyé " + indice);
        }
    }

    @Test
    void niveauHorsBornesEstRamene() {
        Enigme enigme = new Enigme(new Random(7));
        for (int i = 0; i < 200; i++) {
            assertTrue(enigme.indicePourNiveau(0) <= 4, "niveau 0 -> plage niveau 1");
            assertTrue(enigme.indicePourNiveau(99) >= 10, "niveau 99 -> plage niveau 3");
        }
    }

    @Test
    void verificationDeReponseToleranteCasseEtEspaces() {
        Enigme enigme = new Enigme();
        // énigme 0 : réponse attendue "C"
        assertTrue(enigme.estCorrecte("c", 0));
        assertTrue(enigme.estCorrecte("  C  ", 0));
        assertFalse(enigme.estCorrecte("A", 0));
        assertFalse(enigme.estCorrecte(null, 0));
    }

    @Test
    void reponsesLibresConnues() {
        Enigme enigme = new Enigme();
        assertEquals("13", enigme.reponseAttendue(7));
        assertTrue(enigme.estCorrecte("Prénom", 8));
    }
}
