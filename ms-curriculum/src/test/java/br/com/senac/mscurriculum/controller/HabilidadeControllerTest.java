package br.com.senac.mscurriculum.controller;

import br.com.senac.mscurriculum.dto.HabilidadeDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@DisplayName("Testes da Classe HabilidadeController")
@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class HabilidadeControllerTest {

    @org.mockito.Mock
    private br.com.senac.mscurriculum.service.HabilidadeService habilidadeService;

    @org.mockito.InjectMocks
    private HabilidadeController habilidadeController;

    @org.mockito.Mock
    private org.springframework.security.oauth2.jwt.Jwt jwt;

    private static final Long CANDIDATO_ID = 20L;
    private static final Long HABILIDADE_ID = 10L;

    @org.junit.jupiter.api.Test
    void testCadastrarComSucesso() {
        br.com.senac.mscurriculum.dto.HabilidadeDTO request = criarHabilidadeDTO();
        br.com.senac.mscurriculum.dto.HabilidadeDTO response = criarHabilidadeDTOCompleto();

        org.mockito.Mockito.when(habilidadeService.cadastrar(
                        eq(CANDIDATO_ID),
                        any(br.com.senac.mscurriculum.dto.HabilidadeDTO.class),
                        any(org.springframework.security.oauth2.jwt.Jwt.class)))
                .thenReturn(response);

        br.com.senac.mscurriculum.dto.HabilidadeDTO resultado = habilidadeController.cadastrar(CANDIDATO_ID, request, jwt);

        assertNotNull(resultado);
        assertEquals(HABILIDADE_ID, resultado.getId());
        assertEquals(CANDIDATO_ID, resultado.getIdCandidato());
        assertEquals("Java", resultado.getDescricao());
        verify(habilidadeService).cadastrar(
                eq(CANDIDATO_ID),
                any(br.com.senac.mscurriculum.dto.HabilidadeDTO.class),
                any(org.springframework.security.oauth2.jwt.Jwt.class));
    }

    @org.junit.jupiter.api.Test
    void testAlterarComSucesso() {
        br.com.senac.mscurriculum.dto.HabilidadeDTO request = criarHabilidadeDTO();
        br.com.senac.mscurriculum.dto.HabilidadeDTO response = criarHabilidadeDTOCompleto();

        org.mockito.Mockito.when(habilidadeService.alterar(
                        eq(CANDIDATO_ID),
                        eq(HABILIDADE_ID),
                        any(br.com.senac.mscurriculum.dto.HabilidadeDTO.class),
                        any(org.springframework.security.oauth2.jwt.Jwt.class)))
                .thenReturn(response);

        br.com.senac.mscurriculum.dto.HabilidadeDTO resultado = habilidadeController.alterar(CANDIDATO_ID, HABILIDADE_ID, request, jwt);

        assertNotNull(resultado);
        assertEquals(HABILIDADE_ID, resultado.getId());
        assertEquals("Java", resultado.getDescricao());
        verify(habilidadeService).alterar(
                eq(CANDIDATO_ID),
                eq(HABILIDADE_ID),
                any(br.com.senac.mscurriculum.dto.HabilidadeDTO.class),
                any(org.springframework.security.oauth2.jwt.Jwt.class));
    }

    @org.junit.jupiter.api.Test
    void testBuscarPorCandidatoComSucesso() {
        java.util.List<br.com.senac.mscurriculum.dto.HabilidadeDTO> response = criarListaHabilidadesDTO();

        org.mockito.Mockito.when(habilidadeService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(response);

        java.util.List<br.com.senac.mscurriculum.dto.HabilidadeDTO> resultado = habilidadeController.buscarPorCandidato(CANDIDATO_ID);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(HABILIDADE_ID, resultado.get(0).getId());
        verify(habilidadeService).buscarPorCandidato(CANDIDATO_ID);
    }

    @org.junit.jupiter.api.Test
    void testDeletarComSucesso() {
        habilidadeController.deletar(CANDIDATO_ID, HABILIDADE_ID, jwt);

        verify(habilidadeService).deletar(
                eq(CANDIDATO_ID),
                eq(HABILIDADE_ID),
                any(org.springframework.security.oauth2.jwt.Jwt.class));
    }

    private br.com.senac.mscurriculum.dto.HabilidadeDTO criarHabilidadeDTO() {
        return new br.com.senac.mscurriculum.dto.HabilidadeDTO(
                null,
                CANDIDATO_ID,
                "Java",
                "Avançado"
        );
    }

    private HabilidadeDTO criarHabilidadeDTOCompleto() {
        return new HabilidadeDTO(
                HABILIDADE_ID,
                CANDIDATO_ID,
                "Java",
                "Avançado"
        );
    }

    private java.util.List<br.com.senac.mscurriculum.dto.HabilidadeDTO> criarListaHabilidadesDTO() {
        return java.util.List.of(criarHabilidadeDTOCompleto());
    }
}
