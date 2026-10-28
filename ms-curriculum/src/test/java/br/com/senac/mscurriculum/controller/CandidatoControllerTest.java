package br.com.senac.mscurriculum.controller;

import br.com.senac.mscurriculum.dto.*;
import br.com.senac.mscurriculum.service.CandidatoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

@DisplayName("Testes da Classe CandidatoController")
@ExtendWith(MockitoExtension.class)
class CandidatoControllerTest {

    @Mock
    private CandidatoService candidatoService;

    @InjectMocks
    private CandidatoController candidatoController;

    @Mock
    private Jwt jwt;

    private static final Long CANDIDATO_ID = 20L;
    private static final Long USUARIO_ID = 10L;

    @Test
    void testCadastrarComSucesso() {
        CandidatoDTO request = criarCandidatoDTO();
        CandidatoDTO response = criarCandidatoDTOCompleto();

        Mockito.when(candidatoService.cadastrar(Mockito.any(CandidatoDTO.class), Mockito.any(Jwt.class)))
                .thenReturn(response);

        CandidatoDTO resultado = candidatoController.cadastrar(request, jwt);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(CANDIDATO_ID, resultado.getId());
        Assertions.assertEquals(USUARIO_ID, resultado.getIdUsuario());
        Assertions.assertEquals("João da Silva", resultado.getNome());
        Assertions.assertEquals("joao@email.com", resultado.getEmail());
        Mockito.verify(candidatoService).cadastrar(Mockito.any(CandidatoDTO.class), Mockito.any(Jwt.class));
    }

    @Test
    void testAlterarComSucesso() {
        AlterarCandidatoRequest request = criarAlterarRequest();
        CandidatoDTO response = criarCandidatoDTOCompleto();

        Mockito.when(candidatoService.alterar(org.mockito.ArgumentMatchers.any(br.com.senac.mscurriculum.dto.AlterarCandidatoRequest.class), org.mockito.ArgumentMatchers.any(org.springframework.security.oauth2.jwt.Jwt.class)))
                .thenReturn(response);

        CandidatoDTO resultado = candidatoController.alterar(request, jwt);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(CANDIDATO_ID, resultado.getId());
        Assertions.assertEquals("João da Silva", resultado.getNome());
        Mockito.verify(candidatoService).alterar(Mockito.any(AlterarCandidatoRequest.class), Mockito.any(Jwt.class));
    }

    @Test
    void testBuscarComSucesso() {
        CandidatoDTO response = criarCandidatoDTOCompleto();

        Mockito.when(candidatoService.buscarPorId(CANDIDATO_ID)).thenReturn(response);

        CandidatoDTO resultado = candidatoController.buscar(CANDIDATO_ID);

        Assertions. assertNotNull(resultado);
        Assertions.assertEquals(CANDIDATO_ID, resultado.getId());
        Assertions.assertEquals(USUARIO_ID, resultado.getIdUsuario());
        Mockito.verify(candidatoService).buscarPorId(CANDIDATO_ID);
    }

    @Test
    void testMeComSucesso() {
        CandidatoDTO response = criarCandidatoDTOCompleto();

        Mockito.when(candidatoService.me(Mockito.any(Jwt.class))).thenReturn(response);

        CandidatoDTO resultado = candidatoController.me(jwt);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(CANDIDATO_ID, resultado.getId());
        Assertions.assertEquals(USUARIO_ID, resultado.getIdUsuario());
        Mockito.verify(candidatoService).me(Mockito.any(Jwt.class));
    }

    @Test
    void testDeletarComSucesso() {
        candidatoController.deletar(jwt);

        Mockito.verify(candidatoService).deletar(Mockito.any(Jwt.class));
    }

    private CandidatoDTO criarCandidatoDTO() {
        CandidatoDTO dto = new CandidatoDTO();
        dto.setNome("João da Silva");
        dto.setEmail("joao@email.com");
        dto.setSexo("MASCULINO");
        dto.setTelefone("11999999999");
        dto.setDataNascimento(java.time.LocalDate.of(1990, 1, 15));
        dto.setResumoProfissional("Desenvolvedor Java");
        dto.setEndereco(criarEnderecoDTO());
        dto.setEducacoes(criarEducacoesDTO());
        dto.setExperiencias(criarExperienciasDTO());
        dto.setHabilidades(criarHabilidadesDTO());
        return dto;
    }

    private CandidatoDTO criarCandidatoDTOCompleto() {
        CandidatoDTO dto = criarCandidatoDTO();
        dto.setId(CANDIDATO_ID);
        dto.setIdUsuario(USUARIO_ID);
        return dto;
    }

    private AlterarCandidatoRequest criarAlterarRequest() {
        return new AlterarCandidatoRequest(
                CANDIDATO_ID,
                USUARIO_ID,
                "João da Silva",
                "joao@email.com",
                "MASCULINO",
                "11999999999",
                java.time.LocalDate.of(1990, 1, 15),
                "Desenvolvedor Java"
        );
    }

    private EnderecoDTO criarEnderecoDTO() {
        return new EnderecoDTO(
                1L,
                CANDIDATO_ID,
                "Rua A",
                "100",
                "Apto 1",
                "São Paulo",
                "SP",
                "01000-000",
                "Centro",
                new java.math.BigDecimal("-23.550520"),
                new java.math.BigDecimal("-46.633308")
        );
    }

    private List<EducacaoDTO> criarEducacoesDTO() {
        return List.of(
                new EducacaoDTO(
                        1L,
                        CANDIDATO_ID,
                        "Análise e Desenvolvimento de Sistemas",
                        "Tecnólogo",
                        "Senac",
                        java.time.LocalDate.of(2014, 1, 1),
                        java.time.LocalDate.of(2016, 12, 31)
                )
        );
    }

    private List<ExperienciaDTO> criarExperienciasDTO() {
        return List.of(
                new ExperienciaDTO(
                        1L,
                        CANDIDATO_ID,
                        "Desenvolvedor Pleno",
                        "Empresa XYZ",
                        "APIs REST",
                        java.time.LocalDate.of(2020, 1, 1),
                        java.time.LocalDate.of(2021, 12, 31)
                )
        );
    }

    private List<HabilidadeDTO> criarHabilidadesDTO() {
        return List.of(
                new HabilidadeDTO(1L, CANDIDATO_ID, "Java", "Avançado"),
                new HabilidadeDTO(2L, CANDIDATO_ID, "Spring Boot", "Intermediário")
        );
    }
}
