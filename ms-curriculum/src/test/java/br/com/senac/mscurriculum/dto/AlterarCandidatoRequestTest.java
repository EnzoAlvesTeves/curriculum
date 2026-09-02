package br.com.senac.mscurriculum.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlterarCandidatoRequestTest {

    @Test
    void testConstrutorVazio() {
        AlterarCandidatoRequest request = new AlterarCandidatoRequest();

        assertNull(request.getId());
        assertNull(request.getIdUsuario());
        assertNull(request.getNome());
        assertNull(request.getEmail());
        assertNull(request.getSexo());
        assertNull(request.getTelefone());
        assertNull(request.getDataNascimento());
        assertNull(request.getResumoProfissional());
    }

    @Test
    void testConstrutorCompleto() {
        java.time.LocalDate dataNascimento = java.time.LocalDate.of(1989, 6, 10);
        AlterarCandidatoRequest request = new AlterarCandidatoRequest(
                1L,
                10L,
                "Guilherme Lopes",
                "guilherme@email.com",
                "MASCULINO",
                "11999999999",
                dataNascimento,
                "Desenvolvedor Java com experiência em microserviços"
        );

        assertAll(
                () -> assertEquals(1L, request.getId()),
                () -> assertEquals(10L, request.getIdUsuario()),
                () -> assertEquals("Guilherme Lopes", request.getNome()),
                () -> assertEquals("guilherme@email.com", request.getEmail()),
                () -> assertEquals("MASCULINO", request.getSexo()),
                () -> assertEquals("11999999999", request.getTelefone()),
                () -> assertEquals(dataNascimento, request.getDataNascimento()),
                () -> assertEquals("Desenvolvedor Java com experiência em microserviços", request.getResumoProfissional())
        );
    }

    @Test
    void testSetters() {
        AlterarCandidatoRequest request = new AlterarCandidatoRequest();

        request.setId(1L);
        request.setIdUsuario(10L);
        request.setNome("João");
        request.setEmail("joao@email.com");
        request.setSexo("MASCULINO");
        request.setTelefone("11999999999");
        request.setDataNascimento(java.time.LocalDate.of(1990, 1, 15));
        request.setResumoProfissional("Resumo");

        assertEquals(1L, request.getId());
        assertEquals(10L, request.getIdUsuario());
        assertEquals("João", request.getNome());
        assertEquals("joao@email.com", request.getEmail());
        assertEquals("MASCULINO", request.getSexo());
        assertEquals("11999999999", request.getTelefone());
        assertEquals(java.time.LocalDate.of(1990, 1, 15), request.getDataNascimento());
        assertEquals("Resumo", request.getResumoProfissional());
    }
}