package br.com.senac.mscurriculum.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.oauth2.jwt.Jwt;

@DisplayName("Testes da Classe CandidatoController")
@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class CandidatoControllerTest {

    @Mock
    private br.com.senac.mscurriculum.service.CandidatoService candidatoService;

    @InjectMocks
    private CandidatoController candidatoController;

    @Mock
    private Jwt jwt;

    private static final Long CANDIDATO_ID = 20L;
    private static final Long USUARIO_ID = 10L;

    @org.junit.jupiter.api.Test
    void testCadastrarComSucesso() {
        br.com.senac.mscurriculum.dto.CandidatoDTO request = criarCandidatoDTO();
        br.com.senac.mscurriculum.dto.CandidatoDTO response = criarCandidatoDTOCompleto();

        org.mockito.Mockito.when(candidatoService.cadastrar(org.mockito.ArgumentMatchers.any(br.com.senac.mscurriculum.dto.CandidatoDTO.class), org.mockito.ArgumentMatchers.any(org.springframework.security.oauth2.jwt.Jwt.class)))
                .thenReturn(response);

        br.com.senac.mscurriculum.dto.CandidatoDTO resultado = candidatoController.cadastrar(request, jwt);

        org.junit.jupiter.api.Assertions.assertNotNull(resultado);
        org.junit.jupiter.api.Assertions.assertEquals(CANDIDATO_ID, resultado.getId());
        org.junit.jupiter.api.Assertions.assertEquals(USUARIO_ID, resultado.getIdUsuario());
        org.junit.jupiter.api.Assertions.assertEquals("João da Silva", resultado.getNome());
        org.junit.jupiter.api.Assertions.assertEquals("joao@email.com", resultado.getEmail());
        org.mockito.Mockito.verify(candidatoService).cadastrar(org.mockito.ArgumentMatchers.any(br.com.senac.mscurriculum.dto.CandidatoDTO.class), org.mockito.ArgumentMatchers.any(org.springframework.security.oauth2.jwt.Jwt.class));
    }

    @org.junit.jupiter.api.Test
    void testAlterarComSucesso() {
        br.com.senac.mscurriculum.dto.AlterarCandidatoRequest request = criarAlterarRequest();
        br.com.senac.mscurriculum.dto.CandidatoDTO response = criarCandidatoDTOCompleto();

        org.mockito.Mockito.when(candidatoService.alterar(org.mockito.ArgumentMatchers.any(br.com.senac.mscurriculum.dto.AlterarCandidatoRequest.class), org.mockito.ArgumentMatchers.any(org.springframework.security.oauth2.jwt.Jwt.class)))
                .thenReturn(response);

        br.com.senac.mscurriculum.dto.CandidatoDTO resultado = candidatoController.alterar(request, jwt);

        org.junit.jupiter.api.Assertions.assertNotNull(resultado);
        org.junit.jupiter.api.Assertions.assertEquals(CANDIDATO_ID, resultado.getId());
        org.junit.jupiter.api.Assertions.assertEquals("João da Silva", resultado.getNome());
        org.mockito.Mockito.verify(candidatoService).alterar(org.mockito.ArgumentMatchers.any(br.com.senac.mscurriculum.dto.AlterarCandidatoRequest.class), org.mockito.ArgumentMatchers.any(org.springframework.security.oauth2.jwt.Jwt.class));
    }

    @org.junit.jupiter.api.Test
    void testBuscarComSucesso() {
        br.com.senac.mscurriculum.dto.CandidatoDTO response = criarCandidatoDTOCompleto();

        org.mockito.Mockito.when(candidatoService.buscarPorId(CANDIDATO_ID)).thenReturn(response);

        br.com.senac.mscurriculum.dto.CandidatoDTO resultado = candidatoController.buscar(CANDIDATO_ID);

        org.junit.jupiter.api.Assertions.assertNotNull(resultado);
        org.junit.jupiter.api.Assertions.assertEquals(CANDIDATO_ID, resultado.getId());
        org.junit.jupiter.api.Assertions.assertEquals(USUARIO_ID, resultado.getIdUsuario());
        org.mockito.Mockito.verify(candidatoService).buscarPorId(CANDIDATO_ID);
    }

    @org.junit.jupiter.api.Test
    void testMeComSucesso() {
        br.com.senac.mscurriculum.dto.CandidatoDTO response = criarCandidatoDTOCompleto();

        org.mockito.Mockito.when(candidatoService.me(org.mockito.ArgumentMatchers.any(org.springframework.security.oauth2.jwt.Jwt.class))).thenReturn(response);

        br.com.senac.mscurriculum.dto.CandidatoDTO resultado = candidatoController.me(jwt);

        org.junit.jupiter.api.Assertions.assertNotNull(resultado);
        org.junit.jupiter.api.Assertions.assertEquals(CANDIDATO_ID, resultado.getId());
        org.junit.jupiter.api.Assertions.assertEquals(USUARIO_ID, resultado.getIdUsuario());
        org.mockito.Mockito.verify(candidatoService).me(org.mockito.ArgumentMatchers.any(org.springframework.security.oauth2.jwt.Jwt.class));
    }

    @org.junit.jupiter.api.Test
    void testDeletarComSucesso() {
        candidatoController.deletar(jwt);

        org.mockito.Mockito.verify(candidatoService).deletar(org.mockito.ArgumentMatchers.any(org.springframework.security.oauth2.jwt.Jwt.class));
    }

    private br.com.senac.mscurriculum.dto.CandidatoDTO criarCandidatoDTO() {
        br.com.senac.mscurriculum.dto.CandidatoDTO dto = new br.com.senac.mscurriculum.dto.CandidatoDTO();
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

    private br.com.senac.mscurriculum.dto.CandidatoDTO criarCandidatoDTOCompleto() {
        br.com.senac.mscurriculum.dto.CandidatoDTO dto = criarCandidatoDTO();
        dto.setId(CANDIDATO_ID);
        dto.setIdUsuario(USUARIO_ID);
        return dto;
    }

    private br.com.senac.mscurriculum.dto.AlterarCandidatoRequest criarAlterarRequest() {
        return new br.com.senac.mscurriculum.dto.AlterarCandidatoRequest(
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

    private br.com.senac.mscurriculum.dto.EnderecoDTO criarEnderecoDTO() {
        return new br.com.senac.mscurriculum.dto.EnderecoDTO(
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

    private java.util.List<br.com.senac.mscurriculum.dto.EducacaoDTO> criarEducacoesDTO() {
        return java.util.List.of(
                new br.com.senac.mscurriculum.dto.EducacaoDTO(
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

    private java.util.List<br.com.senac.mscurriculum.dto.ExperienciaDTO> criarExperienciasDTO() {
        return java.util.List.of(
                new br.com.senac.mscurriculum.dto.ExperienciaDTO(
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

    private java.util.List<br.com.senac.mscurriculum.dto.HabilidadeDTO> criarHabilidadesDTO() {
        return java.util.List.of(
                new br.com.senac.mscurriculum.dto.HabilidadeDTO(1L, CANDIDATO_ID, "Java", "Avançado"),
                new br.com.senac.mscurriculum.dto.HabilidadeDTO(2L, CANDIDATO_ID, "Spring Boot", "Intermediário")
        );
    }
}
