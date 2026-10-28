package br.com.senac.mscurriculum.controller;

import br.com.senac.mscurriculum.dto.HabilidadeDTO;
import br.com.senac.mscurriculum.service.HabilidadeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@DisplayName("Testes da Classe HabilidadeController")
@ExtendWith(MockitoExtension.class)
class HabilidadeControllerTest {

    @Mock
    private HabilidadeService habilidadeService;

    @InjectMocks
    private HabilidadeController habilidadeController;

    @Mock
    private Jwt jwt;

    private static final Long CANDIDATO_ID = 20L;
    private static final Long HABILIDADE_ID = 10L;

    @Test
    void testCadastrarComSucesso() {
        HabilidadeDTO request = criarHabilidadeDTO();
        HabilidadeDTO response = criarHabilidadeDTOCompleto();

        Mockito.when(habilidadeService.cadastrar(
                        eq(CANDIDATO_ID),
                        any(HabilidadeDTO.class),
                        any(Jwt.class)))
                .thenReturn(response);

        HabilidadeDTO resultado = habilidadeController.cadastrar(CANDIDATO_ID, request, jwt);

        assertNotNull(resultado);
        assertEquals(HABILIDADE_ID, resultado.getId());
        assertEquals(CANDIDATO_ID, resultado.getIdCandidato());
        assertEquals("Java", resultado.getDescricao());
        verify(habilidadeService).cadastrar(
                eq(CANDIDATO_ID),
                any(HabilidadeDTO.class),
                any(Jwt.class));
    }

    @Test
    void testAlterarComSucesso() {
        HabilidadeDTO request = criarHabilidadeDTO();
        HabilidadeDTO response = criarHabilidadeDTOCompleto();

        org.mockito.Mockito.when(habilidadeService.alterar(
                        eq(CANDIDATO_ID),
                        eq(HABILIDADE_ID),
                        any(HabilidadeDTO.class),
                        any(Jwt.class)))
                .thenReturn(response);

        HabilidadeDTO resultado = habilidadeController.alterar(CANDIDATO_ID, HABILIDADE_ID, request, jwt);

        assertNotNull(resultado);
        assertEquals(HABILIDADE_ID, resultado.getId());
        assertEquals("Java", resultado.getDescricao());
        verify(habilidadeService).alterar(
                eq(CANDIDATO_ID),
                eq(HABILIDADE_ID),
                any(HabilidadeDTO.class),
                any(Jwt.class));
    }

    @Test
    void testBuscarPorCandidatoComSucesso() {
        List<HabilidadeDTO> response = criarListaHabilidadesDTO();

        Mockito.when(habilidadeService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(response);

        List<HabilidadeDTO> resultado = habilidadeController.buscarPorCandidato(CANDIDATO_ID);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(HABILIDADE_ID, resultado.get(0).getId());
        verify(habilidadeService).buscarPorCandidato(CANDIDATO_ID);
    }

    @Test
    void testDeletarComSucesso() {
        habilidadeController.deletar(CANDIDATO_ID, HABILIDADE_ID, jwt);

        verify(habilidadeService).deletar(
                eq(CANDIDATO_ID),
                eq(HABILIDADE_ID),
                any(Jwt.class));
    }

    private HabilidadeDTO criarHabilidadeDTO() {
        return new HabilidadeDTO(
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

    private List<HabilidadeDTO> criarListaHabilidadesDTO() {
        return List.of(criarHabilidadeDTOCompleto());
    }
}
