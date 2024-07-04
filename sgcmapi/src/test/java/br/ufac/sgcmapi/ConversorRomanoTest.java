package br.ufac.sgcmapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.ufac.sgcmapi.model.ConversorRomano;

public class ConversorRomanoTest {

    ConversorRomano conversor;

    @BeforeEach
    public void setUp() {
        conversor = new ConversorRomano(); 
    }

    @Test
    public void testUmAlgarismo() {
        assertEquals(5, conversor.convert("V"));
    }

    @Test
    public void testVariosAlgarismos() {
        assertEquals(8, conversor.convert("VIII"));
    }

    @Test
    public void testAlgarismoSubtrativo() {
        assertEquals(9, conversor.convert("IX"));
    }
}
