package br.com.senac.mscurriculum.controller;

@org.junit.jupiter.api.DisplayName("Testes da Classe ExperienciaController")
@org.junit.jupiter.api.extension.ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ExperienciaControllerTest {

    @org.mockito.Mock
    private br.com.senac.mscurriculum.service.ExperienciaService experienciaService;

    @org.mockito.InjectMocks
    private ExperienciaController experienciaController;

    @org.mockito.Mock
    private org.springframework.security.oauth2.jwt.Jwt jwt;

    private static final Long CANDIDATO_ID = 20L;
    private static final Long EXPERIENCIA_ID = 10L;

    @org.junit.jupiter.api.Test
    void testCadastrarComSucesso() {
        br.com.senac.mscurriculum.dto.ExperienciaDTO request = criarExperienciaDTO();
        br.com.senac.mscurriculum.dto.ExperienciaDTO response = criarExperienciaDTOCompleto();

        org.mockito.Mockito.when(experienciaService.cadastrar(
                        org.mockito.ArgumentMatchers.eq(CANDIDATO_ID),
                        org.mockito.ArgumentMatchers.any(br.com.senac.mscurriculum.dto.ExperienciaDTO.class),
                        org.mockito.ArgumentMatchers.any(org.springframework.security.oauth2.jwt.Jwt.class)))
                .thenReturn(response);

        br.com.senac.mscurriculum.dto.ExperienciaDTO resultado = experienciaController.cadastrar(CANDIDATO_ID, request, jwt);

        org.junit.jupiter.api.Assertions.assertNotNull(resultado);
        org.junit.jupiter.api.Assertions.assertEquals(EXPERIENCIA_ID, resultado.getId());
        org.junit.jupiter.api.Assertions.assertEquals(CANDIDATO_ID, resultado.getIdCandidato());
        org.junit.jupiter.api.Assertions.assertEquals("Desenvolvedor Pleno", resultado.getCargo());
        org.mockito.Mockito.verify(experienciaService).cadastrar(
                org.mockito.ArgumentMatchers.eq(CANDIDATO_ID),
                org.mockito.ArgumentMatchers.any(br.com.senac.mscurriculum.dto.ExperienciaDTO.class),
                org.mockito.ArgumentMatchers.any(org.springframework.security.oauth2.jwt.Jwt.class));
    }

    @org.junit.jupiter.api.Test
    void testAlterarComSucesso() {
        br.com.senac.mscurriculum.dto.ExperienciaDTO request = criarExperienciaDTO();
        br.com.senac.mscurriculum.dto.ExperienciaDTO response = criarExperienciaDTOCompleto();

        org.mockito.Mockito.when(experienciaService.alterar(
                        org.mockito.ArgumentMatchers.eq(CANDIDATO_ID),
                        org.mockito.ArgumentMatchers.eq(EXPERIENCIA_ID),
                        org.mockito.ArgumentMatchers.any(br.com.senac.mscurriculum.dto.ExperienciaDTO.class),
                        org.mockito.ArgumentMatchers.any(org.springframework.security.oauth2.jwt.Jwt.class)))
                .thenReturn(response);

        br.com.senac.mscurriculum.dto.ExperienciaDTO resultado = experienciaController.alterar(CANDIDATO_ID, EXPERIENCIA_ID, request, jwt);

        org.junit.jupiter.api.Assertions.assertNotNull(resultado);
        org.junit.jupiter.api.Assertions.assertEquals(EXPERIENCIA_ID, resultado.getId());
        org.junit.jupiter.api.Assertions.assertEquals("Desenvolvedor Pleno", resultado.getCargo());
        org.mockito.Mockito.verify(experienciaService).alterar(
                org.mockito.ArgumentMatchers.eq(CANDIDATO_ID),
                org.mockito.ArgumentMatchers.eq(EXPERIENCIA_ID),
                org.mockito.ArgumentMatchers.any(br.com.senac.mscurriculum.dto.ExperienciaDTO.class),
                org.mockito.ArgumentMatchers.any(org.springframework.security.oauth2.jwt.Jwt.class));
    }

    @org.junit.jupiter.api.Test
    void testBuscarComSucesso() {
        java.util.List<br.com.senac.mscurriculum.dto.ExperienciaDTO> response = criarListaExperienciasDTO();

        org.mockito.Mockito.when(experienciaService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(response);

        java.util.List<br.com.senac.mscurriculum.dto.ExperienciaDTO> resultado = experienciaController.buscar(CANDIDATO_ID);

        org.junit.jupiter.api.Assertions.assertNotNull(resultado);
        org.junit.jupiter.api.Assertions.assertEquals(1, resultado.size());
        org.junit.jupiter.api.Assertions.assertEquals(EXPERIENCIA_ID, resultado.get(0).getId());
        org.mockito.Mockito.verify(experienciaService).buscarPorCandidato(CANDIDATO_ID);
    }

    @org.junit.jupiter.api.Test
    void testDeletarComSucesso() {
        experienciaController.deletar(CANDIDATO_ID, EXPERIENCIA_ID, jwt);

        org.mockito.Mockito.verify(experienciaService).deletar(
                org.mockito.ArgumentMatchers.eq(CANDIDATO_ID),
                org.mockito.ArgumentMatchers.eq(EXPERIENCIA_ID),
                org.mockito.ArgumentMatchers.any(org.springframework.security.oauth2.jwt.Jwt.class));
    }

    private br.com.senac.mscurriculum.dto.ExperienciaDTO criarExperienciaDTO() {
        return new br.com.senac.mscurriculum.dto.ExperienciaDTO(
                null,
                CANDIDATO_ID,
                "Desenvolvedor Pleno",
                "Empresa XYZ",
                "APIs REST",
                java.time.LocalDate.of(2020, 1, 1),
                java.time.LocalDate.of(2021, 12, 31)
        );
    }

    private br.com.senac.mscurriculum.dto.ExperienciaDTO criarExperienciaDTOCompleto() {
        return new br.com.senac.mscurriculum.dto.ExperienciaDTO(
                EXPERIENCIA_ID,
                CANDIDATO_ID,
                "Desenvolvedor Pleno",
                "Empresa XYZ",
                "APIs REST",
                java.time.LocalDate.of(2020, 1, 1),
                java.time.LocalDate.of(2021, 12, 31)
        );
    }

    private java.util.List<br.com.senac.mscurriculum.dto.ExperienciaDTO> criarListaExperienciasDTO() {
        return java.util.List.of(criarExperienciaDTOCompleto());
    }
}
