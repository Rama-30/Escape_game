package com.escapegame.puzzle;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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
    void aucuneEnigmeNeSeRepeteAvantEpuisementDuNiveau() {
        Enigme enigme = new Enigme(new Random(3));
        // Une plage compte 5 énigmes : 5 tirages doivent donner 5 indices distincts.
        for (int niveau = 1; niveau <= 3; niveau++) {
            Set<Integer> vus = new HashSet<>();
            for (int i = 0; i < 5; i++) {
                assertTrue(vus.add(enigme.indicePourNiveau(niveau)),
                        "énigme répétée au niveau " + niveau + " avant épuisement");
            }
            // La 6e demande réinitialise la plage : l'indice doit rester valide.
            int apresReset = enigme.indicePourNiveau(niveau);
            assertTrue(vus.contains(apresReset), "après reset, indice hors de la plage du niveau");
        }
    }

    @Test
    void uneSequenceDePartieNeRepeteAucuneEnigme() {
        // Reproduit la consommation d'une partie : niveau 1 ×4, niveau 2 ×4, niveau 3 ×1.
        Enigme enigme = new Enigme(new Random(99));
        Set<Integer> tous = new HashSet<>();
        for (int i = 0; i < 4; i++) {
            assertTrue(tous.add(enigme.indicePourNiveau(1)), "répétition au niveau 1");
        }
        for (int i = 0; i < 4; i++) {
            assertTrue(tous.add(enigme.indicePourNiveau(2)), "répétition au niveau 2");
        }
        assertTrue(tous.add(enigme.indicePourNiveau(3)), "répétition au niveau 3");
        assertEquals(9, tous.size(), "9 énigmes distinctes attendues sur une partie");
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
