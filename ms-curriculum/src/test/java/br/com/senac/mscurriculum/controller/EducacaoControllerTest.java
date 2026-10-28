package br.com.senac.mscurriculum.controller;

import br.com.senac.mscurriculum.dto.EducacaoDTO;
import br.com.senac.mscurriculum.service.EducacaoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

@DisplayName("Testes da Classe EducacaoController")
@ExtendWith(MockitoExtension.class)
class EducacaoControllerTest {

    @Mock
    private EducacaoService educacaoService;

    @InjectMocks
    private EducacaoController educacaoController;

    @Mock
    private Jwt jwt;

    private static final Long CANDIDATO_ID = 20L;
    private static final Long EDUCACAO_ID = 10L;

    @Test
    void testCadastrarComSucesso() {
        EducacaoDTO request = criarEducacaoDTO();
        EducacaoDTO response = criarEducacaoDTOCompleto();

        Mockito.when(educacaoService.cadastrar(ArgumentMatchers.eq(CANDIDATO_ID), ArgumentMatchers.any(EducacaoDTO.class), ArgumentMatchers.any(Jwt.class)))
                .thenReturn(response);

        EducacaoDTO resultado = educacaoController.cadastrar(CANDIDATO_ID, request, jwt);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(EDUCACAO_ID, resultado.getId());
        Assertions.assertEquals(CANDIDATO_ID, resultado.getIdCandidato());
        Assertions.assertEquals("Análise e Desenvolvimento de Sistemas", resultado.getCurso());
        Mockito.verify(educacaoService).cadastrar(ArgumentMatchers.eq(CANDIDATO_ID), ArgumentMatchers.any(EducacaoDTO.class), ArgumentMatchers.any(Jwt.class));
    }

    @Test
    void testAlterarComSucesso() {
        EducacaoDTO request = criarEducacaoDTO();
        EducacaoDTO response = criarEducacaoDTOCompleto();

        Mockito.when(educacaoService.alterar(
                        ArgumentMatchers.eq(CANDIDATO_ID),
                        ArgumentMatchers.eq(EDUCACAO_ID),
                        ArgumentMatchers.any(EducacaoDTO.class),
                        ArgumentMatchers.any(Jwt.class)))
                .thenReturn(response);

        EducacaoDTO resultado = educacaoController.alterar(CANDIDATO_ID, EDUCACAO_ID, request, jwt);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(EDUCACAO_ID, resultado.getId());
        Assertions.assertEquals("Análise e Desenvolvimento de Sistemas", resultado.getCurso());
        Mockito.verify(educacaoService).alterar(
                ArgumentMatchers.eq(CANDIDATO_ID),
                ArgumentMatchers.eq(EDUCACAO_ID),
                ArgumentMatchers.any(EducacaoDTO.class),
                ArgumentMatchers.any(Jwt.class));
    }

    @Test
    void testBuscarComSucesso() {
        java.util.List<EducacaoDTO> response = criarListaEducacoesDTO();

        Mockito.when(educacaoService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(response);

        java.util.List<EducacaoDTO> resultado = educacaoController.buscar(CANDIDATO_ID);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals(EDUCACAO_ID, resultado.get(0).getId());
        Mockito.verify(educacaoService).buscarPorCandidato(CANDIDATO_ID);
    }

    @Test
    void testDeletarComSucesso() {
        educacaoController.deletar(CANDIDATO_ID, EDUCACAO_ID, jwt);

        Mockito.verify(educacaoService).deletar(
                ArgumentMatchers.eq(CANDIDATO_ID),
                ArgumentMatchers.eq(EDUCACAO_ID),
                ArgumentMatchers.any(Jwt.class));
    }

    private EducacaoDTO criarEducacaoDTO() {
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

    private EducacaoDTO criarEducacaoDTOCompleto() {
        return new EducacaoDTO(
                EDUCACAO_ID,
                CANDIDATO_ID,
                "Análise e Desenvolvimento de Sistemas",
                "Tecnólogo",
                "Senac",
                java.time.LocalDate.of(2014, 1, 1),
                java.time.LocalDate.of(2016, 12, 31)
        );
    }

    private java.util.List<EducacaoDTO> criarListaEducacoesDTO() {
        return java.util.List.of(criarEducacaoDTOCompleto());
    }
}
