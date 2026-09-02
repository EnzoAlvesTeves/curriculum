package br.com.senac.mscurriculum.controller;

@org.junit.jupiter.api.DisplayName("Testes da Classe EnderecoController")
@org.junit.jupiter.api.extension.ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class EnderecoControllerTest {

    @org.mockito.Mock
    private br.com.senac.mscurriculum.service.EnderecoService enderecoService;

    @org.mockito.InjectMocks
    private EnderecoController enderecoController;

    @org.mockito.Mock
    private org.springframework.security.oauth2.jwt.Jwt jwt;

    private static final Long CANDIDATO_ID = 20L;

    @org.junit.jupiter.api.Test
    void testCadastrarComSucesso() {
        br.com.senac.mscurriculum.dto.EnderecoDTO request = criarEnderecoDTO();
        br.com.senac.mscurriculum.dto.EnderecoDTO response = criarEnderecoDTOCompleto();

        org.mockito.Mockito.when(enderecoService.cadastrar(
                        org.mockito.ArgumentMatchers.eq(CANDIDATO_ID),
                        org.mockito.ArgumentMatchers.any(br.com.senac.mscurriculum.dto.EnderecoDTO.class),
                        org.mockito.ArgumentMatchers.any(org.springframework.security.oauth2.jwt.Jwt.class)))
                .thenReturn(response);

        br.com.senac.mscurriculum.dto.EnderecoDTO resultado = enderecoController.cadastrar(CANDIDATO_ID, request, jwt);

        org.junit.jupiter.api.Assertions.assertNotNull(resultado);
        org.junit.jupiter.api.Assertions.assertEquals(CANDIDATO_ID, resultado.getIdCandidato());
        org.junit.jupiter.api.Assertions.assertEquals("Rua A", resultado.getRua());
        org.mockito.Mockito.verify(enderecoService).cadastrar(
                org.mockito.ArgumentMatchers.eq(CANDIDATO_ID),
                org.mockito.ArgumentMatchers.any(br.com.senac.mscurriculum.dto.EnderecoDTO.class),
                org.mockito.ArgumentMatchers.any(org.springframework.security.oauth2.jwt.Jwt.class));
    }

    @org.junit.jupiter.api.Test
    void testAlterarComSucesso() {
        br.com.senac.mscurriculum.dto.EnderecoDTO request = criarEnderecoDTO();
        br.com.senac.mscurriculum.dto.EnderecoDTO response = criarEnderecoDTOCompleto();

        org.mockito.Mockito.when(enderecoService.alterar(
                        org.mockito.ArgumentMatchers.eq(CANDIDATO_ID),
                        org.mockito.ArgumentMatchers.any(br.com.senac.mscurriculum.dto.EnderecoDTO.class),
                        org.mockito.ArgumentMatchers.any(org.springframework.security.oauth2.jwt.Jwt.class)))
                .thenReturn(response);

        br.com.senac.mscurriculum.dto.EnderecoDTO resultado = enderecoController.alterar(CANDIDATO_ID, request, jwt);

        org.junit.jupiter.api.Assertions.assertNotNull(resultado);
        org.junit.jupiter.api.Assertions.assertEquals(CANDIDATO_ID, resultado.getIdCandidato());
        org.junit.jupiter.api.Assertions.assertEquals("Rua A", resultado.getRua());
        org.mockito.Mockito.verify(enderecoService).alterar(
                org.mockito.ArgumentMatchers.eq(CANDIDATO_ID),
                org.mockito.ArgumentMatchers.any(br.com.senac.mscurriculum.dto.EnderecoDTO.class),
                org.mockito.ArgumentMatchers.any(org.springframework.security.oauth2.jwt.Jwt.class));
    }

    @org.junit.jupiter.api.Test
    void testBuscarComSucesso() {
        br.com.senac.mscurriculum.dto.EnderecoDTO response = criarEnderecoDTOCompleto();

        org.mockito.Mockito.when(enderecoService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(response);

        br.com.senac.mscurriculum.dto.EnderecoDTO resultado = enderecoController.buscar(CANDIDATO_ID);

        org.junit.jupiter.api.Assertions.assertNotNull(resultado);
        org.junit.jupiter.api.Assertions.assertEquals(CANDIDATO_ID, resultado.getIdCandidato());
        org.junit.jupiter.api.Assertions.assertEquals("Rua A", resultado.getRua());
        org.mockito.Mockito.verify(enderecoService).buscarPorCandidato(CANDIDATO_ID);
    }

    @org.junit.jupiter.api.Test
    void testDeletarComSucesso() {
        enderecoController.deletar(CANDIDATO_ID, jwt);

        org.mockito.Mockito.verify(enderecoService).deletar(
                org.mockito.ArgumentMatchers.eq(CANDIDATO_ID),
                org.mockito.ArgumentMatchers.any(org.springframework.security.oauth2.jwt.Jwt.class));
    }

    private br.com.senac.mscurriculum.dto.EnderecoDTO criarEnderecoDTO() {
        return new br.com.senac.mscurriculum.dto.EnderecoDTO(
                null,
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

    private br.com.senac.mscurriculum.dto.EnderecoDTO criarEnderecoDTOCompleto() {
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
}
