package br.com.senac.mscurriculum.controller;

@org.junit.jupiter.api.DisplayName("Testes da Classe EducacaoController")
@org.junit.jupiter.api.extension.ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class EducacaoControllerTest {

    @org.mockito.Mock
    private br.com.senac.mscurriculum.service.EducacaoService educacaoService;

    @org.mockito.InjectMocks
    private EducacaoController educacaoController;

    @org.mockito.Mock
    private org.springframework.security.oauth2.jwt.Jwt jwt;

    private static final Long CANDIDATO_ID = 20L;
    private static final Long EDUCACAO_ID = 10L;

    @org.junit.jupiter.api.Test
    void testCadastrarComSucesso() {
        br.com.senac.mscurriculum.dto.EducacaoDTO request = criarEducacaoDTO();
        br.com.senac.mscurriculum.dto.EducacaoDTO response = criarEducacaoDTOCompleto();

        org.mockito.Mockito.when(educacaoService.cadastrar(org.mockito.ArgumentMatchers.eq(CANDIDATO_ID), org.mockito.ArgumentMatchers.any(br.com.senac.mscurriculum.dto.EducacaoDTO.class), org.mockito.ArgumentMatchers.any(org.springframework.security.oauth2.jwt.Jwt.class)))
                .thenReturn(response);

        br.com.senac.mscurriculum.dto.EducacaoDTO resultado = educacaoController.cadastrar(CANDIDATO_ID, request, jwt);

        org.junit.jupiter.api.Assertions.assertNotNull(resultado);
        org.junit.jupiter.api.Assertions.assertEquals(EDUCACAO_ID, resultado.getId());
        org.junit.jupiter.api.Assertions.assertEquals(CANDIDATO_ID, resultado.getIdCandidato());
        org.junit.jupiter.api.Assertions.assertEquals("Análise e Desenvolvimento de Sistemas", resultado.getCurso());
        org.mockito.Mockito.verify(educacaoService).cadastrar(org.mockito.ArgumentMatchers.eq(CANDIDATO_ID), org.mockito.ArgumentMatchers.any(br.com.senac.mscurriculum.dto.EducacaoDTO.class), org.mockito.ArgumentMatchers.any(org.springframework.security.oauth2.jwt.Jwt.class));
    }

    @org.junit.jupiter.api.Test
    void testAlterarComSucesso() {
        br.com.senac.mscurriculum.dto.EducacaoDTO request = criarEducacaoDTO();
        br.com.senac.mscurriculum.dto.EducacaoDTO response = criarEducacaoDTOCompleto();

        org.mockito.Mockito.when(educacaoService.alterar(
                        org.mockito.ArgumentMatchers.eq(CANDIDATO_ID),
                        org.mockito.ArgumentMatchers.eq(EDUCACAO_ID),
                        org.mockito.ArgumentMatchers.any(br.com.senac.mscurriculum.dto.EducacaoDTO.class),
                        org.mockito.ArgumentMatchers.any(org.springframework.security.oauth2.jwt.Jwt.class)))
                .thenReturn(response);

        br.com.senac.mscurriculum.dto.EducacaoDTO resultado = educacaoController.alterar(CANDIDATO_ID, EDUCACAO_ID, request, jwt);

        org.junit.jupiter.api.Assertions.assertNotNull(resultado);
        org.junit.jupiter.api.Assertions.assertEquals(EDUCACAO_ID, resultado.getId());
        org.junit.jupiter.api.Assertions.assertEquals("Análise e Desenvolvimento de Sistemas", resultado.getCurso());
        org.mockito.Mockito.verify(educacaoService).alterar(
                org.mockito.ArgumentMatchers.eq(CANDIDATO_ID),
                org.mockito.ArgumentMatchers.eq(EDUCACAO_ID),
                org.mockito.ArgumentMatchers.any(br.com.senac.mscurriculum.dto.EducacaoDTO.class),
                org.mockito.ArgumentMatchers.any(org.springframework.security.oauth2.jwt.Jwt.class));
    }

    @org.junit.jupiter.api.Test
    void testBuscarComSucesso() {
        java.util.List<br.com.senac.mscurriculum.dto.EducacaoDTO> response = criarListaEducacoesDTO();

        org.mockito.Mockito.when(educacaoService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(response);

        java.util.List<br.com.senac.mscurriculum.dto.EducacaoDTO> resultado = educacaoController.buscar(CANDIDATO_ID);

        org.junit.jupiter.api.Assertions.assertNotNull(resultado);
        org.junit.jupiter.api.Assertions.assertEquals(1, resultado.size());
        org.junit.jupiter.api.Assertions.assertEquals(EDUCACAO_ID, resultado.get(0).getId());
        org.mockito.Mockito.verify(educacaoService).buscarPorCandidato(CANDIDATO_ID);
    }

    @org.junit.jupiter.api.Test
    void testDeletarComSucesso() {
        educacaoController.deletar(CANDIDATO_ID, EDUCACAO_ID, jwt);

        org.mockito.Mockito.verify(educacaoService).deletar(
                org.mockito.ArgumentMatchers.eq(CANDIDATO_ID),
                org.mockito.ArgumentMatchers.eq(EDUCACAO_ID),
                org.mockito.ArgumentMatchers.any(org.springframework.security.oauth2.jwt.Jwt.class));
    }

    private br.com.senac.mscurriculum.dto.EducacaoDTO criarEducacaoDTO() {
        return new br.com.senac.mscurriculum.dto.EducacaoDTO(
                null,
                CANDIDATO_ID,
                "Análise e Desenvolvimento de Sistemas",
                "Tecnólogo",
                "Senac",
                java.time.LocalDate.of(2014, 1, 1),
                java.time.LocalDate.of(2016, 12, 31)
        );
    }

    private br.com.senac.mscurriculum.dto.EducacaoDTO criarEducacaoDTOCompleto() {
        return new br.com.senac.mscurriculum.dto.EducacaoDTO(
                EDUCACAO_ID,
                CANDIDATO_ID,
                "Análise e Desenvolvimento de Sistemas",
                "Tecnólogo",
                "Senac",
                java.time.LocalDate.of(2014, 1, 1),
                java.time.LocalDate.of(2016, 12, 31)
        );
    }

    private java.util.List<br.com.senac.mscurriculum.dto.EducacaoDTO> criarListaEducacoesDTO() {
        return java.util.List.of(criarEducacaoDTOCompleto());
    }
}
