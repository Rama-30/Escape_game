package com.escapegame.io;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsoleTest {

    private Console consoleAvec(String entree) {
        return new Console(new ByteArrayInputStream(entree.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void lireLigneRenvoieLaLigneSaisie() {
        Console console = consoleAvec("bonjour\nmonde\n");
        assertEquals("bonjour", console.lireLigne());
        assertEquals("monde", console.lireLigne());
    }

    @Test
    void lireEntierIgnoreLesSaisiesNonNumeriques() {
        Console console = consoleAvec("abc\n12\n");
        assertEquals(12, console.lireEntier());
    }

    @Test
    void lecturesSuccessivesNeFermentPasLaSource() {
        // Régression : la version d'origine fermait le Scanner, cassant la lecture suivante.
        Console console = consoleAvec("a\nb\nc\n");
        assertEquals("a", console.lireLigne());
        assertEquals("b", console.lireLigne());
        assertEquals("c", console.lireLigne());
    }
}
