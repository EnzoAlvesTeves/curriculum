package br.com.senac.mscurriculum.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Classe CandidatoDTO")
class CandidatoDTOTest {

    @Test
    @DisplayName("Deve criar DTO com construtor vazio e listas inicializadas")
    void testConstrutorVazio() {
        CandidatoDTO dto = new CandidatoDTO();

        assertNotNull(dto.getEducacoes());
        assertNotNull(dto.getExperiencias());
        assertNotNull(dto.getHabilidades());
        assertTrue(dto.getEducacoes().isEmpty());
        assertTrue(dto.getExperiencias().isEmpty());
        assertTrue(dto.getHabilidades().isEmpty());
    }

    @Test
    @DisplayName("Deve criar DTO com todos os campos")
    void testConstrutorCompleto() {
        EnderecoDTO endereco = new EnderecoDTO(
                1L,
                10L,
                "Rua A",
                "100",
                "Apto 1",
                "São Paulo",
                "SP",
                "01000-000",
                "Centro",
                new BigDecimal("-23.550520"),
                new BigDecimal("-46.633308")
        );
        List<EducacaoDTO> educacoes = List.of(new EducacaoDTO(1L, 10L, "Curso", "Grau", "Instituição", LocalDate.of(2010, 1, 1), LocalDate.of(2012, 12, 31)));
        List<ExperienciaDTO> experiencias = List.of(new ExperienciaDTO(2L, 10L, "Cargo", "Empresa", "Resumo", LocalDate.of(2020, 1, 1), LocalDate.of(2021, 12, 31)));
        List<HabilidadeDTO> habilidades = List.of(new HabilidadeDTO(3L, 10L, "Java", "Avançado"));

        CandidatoDTO dto = new CandidatoDTO(
                1L,
                10L,
                "João da Silva",
                "joao@email.com",
                "MASCULINO",
                "11999999999",
                LocalDate.of(1990, 1, 15),
                "Desenvolvedor Java",
                endereco,
                educacoes,
                experiencias,
                habilidades
        );

        assertAll(
                () -> assertEquals(1L, dto.getId()),
                () -> assertEquals(10L, dto.getIdUsuario()),
                () -> assertEquals("João da Silva", dto.getNome()),
                () -> assertEquals("joao@email.com", dto.getEmail()),
                () -> assertEquals("MASCULINO", dto.getSexo()),
                () -> assertEquals("11999999999", dto.getTelefone()),
                () -> assertEquals(LocalDate.of(1990, 1, 15), dto.getDataNascimento()),
                () -> assertEquals("Desenvolvedor Java", dto.getResumoProfissional()),
                () -> assertEquals(endereco, dto.getEndereco()),
                () -> assertEquals(educacoes, dto.getEducacoes()),
                () -> assertEquals(experiencias, dto.getExperiencias()),
                () -> assertEquals(habilidades, dto.getHabilidades())
        );
    }

    @Test
    @DisplayName("Deve permitir atualizar campos via setters")
    void testSetters() {
        CandidatoDTO dto = new CandidatoDTO();
        EnderecoDTO endereco = new EnderecoDTO();

        dto.setId(1L);
        dto.setIdUsuario(10L);
        dto.setNome("João");
        dto.setEmail("joao@email.com");
        dto.setSexo("MASCULINO");
        dto.setTelefone("11999999999");
        dto.setDataNascimento(LocalDate.of(1990, 1, 15));
        dto.setResumoProfissional("Resumo");
        dto.setEndereco(endereco);
        dto.setEducacoes(List.of());
        dto.setExperiencias(List.of());
        dto.setHabilidades(List.of());

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getIdUsuario());
        assertEquals("João", dto.getNome());
        assertEquals("joao@email.com", dto.getEmail());
        assertEquals("MASCULINO", dto.getSexo());
        assertEquals("11999999999", dto.getTelefone());
        assertEquals(LocalDate.of(1990, 1, 15), dto.getDataNascimento());
        assertEquals("Resumo", dto.getResumoProfissional());
        assertEquals(endereco, dto.getEndereco());
        assertTrue(dto.getEducacoes().isEmpty());
        assertTrue(dto.getExperiencias().isEmpty());
        assertTrue(dto.getHabilidades().isEmpty());
    }
}