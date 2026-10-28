package br.com.senac.bffcurriculum.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SexoTest {

    @Test
    void shouldReturnSexoFromDescricaoIgnoringCase() {
        assertEquals(Sexo.MASCULINO, Sexo.fromDescricao("Masculino"));
        assertEquals(Sexo.FEMININO, Sexo.fromDescricao("feminino"));
        assertEquals(Sexo.OUTRO, Sexo.fromDescricao("OUTRO"));
    }

    @Test
    void shouldThrowWhenDescricaoIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> Sexo.fromDescricao("invalido"));
    }
}
