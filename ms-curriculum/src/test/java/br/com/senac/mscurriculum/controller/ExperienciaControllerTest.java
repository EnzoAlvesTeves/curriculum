package br.com.senac.mscurriculum.controller;

import br.com.senac.mscurriculum.dto.ExperienciaDTO;
import br.com.senac.mscurriculum.service.ExperienciaService;
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

import java.util.List;

@DisplayName("Testes da Classe ExperienciaController")
@ExtendWith(MockitoExtension.class)
class ExperienciaControllerTest {

    @Mock
    private ExperienciaService experienciaService;

    @InjectMocks
    private ExperienciaController experienciaController;

    @Mock
    private Jwt jwt;

    private static final Long CANDIDATO_ID = 20L;
    private static final Long EXPERIENCIA_ID = 10L;

    @Test
    void testCadastrarComSucesso() {
        ExperienciaDTO request = criarExperienciaDTO();
        ExperienciaDTO response = criarExperienciaDTOCompleto();

        Mockito.when(experienciaService.cadastrar(
                        ArgumentMatchers.eq(CANDIDATO_ID),
                        ArgumentMatchers.any(ExperienciaDTO.class),
                        ArgumentMatchers.any(Jwt.class)))
                .thenReturn(response);

        ExperienciaDTO resultado = experienciaController.cadastrar(CANDIDATO_ID, request, jwt);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(EXPERIENCIA_ID, resultado.getId());
        Assertions.assertEquals(CANDIDATO_ID, resultado.getIdCandidato());
        Assertions.assertEquals("Desenvolvedor Pleno", resultado.getCargo());
        Mockito.verify(experienciaService).cadastrar(
                ArgumentMatchers.eq(CANDIDATO_ID),
                ArgumentMatchers.any(ExperienciaDTO.class),
                ArgumentMatchers.any(Jwt.class));
    }

    @Test
    void testAlterarComSucesso() {
        ExperienciaDTO request = criarExperienciaDTO();
        ExperienciaDTO response = criarExperienciaDTOCompleto();

        Mockito.when(experienciaService.alterar(
                        ArgumentMatchers.eq(CANDIDATO_ID),
                        ArgumentMatchers.eq(EXPERIENCIA_ID),
                        ArgumentMatchers.any(ExperienciaDTO.class),
                        ArgumentMatchers.any(Jwt.class)))
                .thenReturn(response);

        ExperienciaDTO resultado = experienciaController.alterar(CANDIDATO_ID, EXPERIENCIA_ID, request, jwt);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(EXPERIENCIA_ID, resultado.getId());
        Assertions.assertEquals("Desenvolvedor Pleno", resultado.getCargo());
        Mockito.verify(experienciaService).alterar(
                ArgumentMatchers.eq(CANDIDATO_ID),
                ArgumentMatchers.eq(EXPERIENCIA_ID),
                ArgumentMatchers.any(ExperienciaDTO.class),
                ArgumentMatchers.any(Jwt.class));
    }

    @Test
    void testBuscarComSucesso() {
        List<ExperienciaDTO> response = criarListaExperienciasDTO();

        Mockito.when(experienciaService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(response);

        List<ExperienciaDTO> resultado = experienciaController.buscar(CANDIDATO_ID);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals(EXPERIENCIA_ID, resultado.get(0).getId());
        Mockito.verify(experienciaService).buscarPorCandidato(CANDIDATO_ID);
    }

    @Test
    void testDeletarComSucesso() {
        experienciaController.deletar(CANDIDATO_ID, EXPERIENCIA_ID, jwt);

        Mockito.verify(experienciaService).deletar(
                ArgumentMatchers.eq(CANDIDATO_ID),
                ArgumentMatchers.eq(EXPERIENCIA_ID),
                ArgumentMatchers.any(Jwt.class));
    }

    private ExperienciaDTO criarExperienciaDTO() {
        return new ExperienciaDTO(
                null,
                CANDIDATO_ID,
                "Desenvolvedor Pleno",
                "Empresa XYZ",
                "APIs REST",
                java.time.LocalDate.of(2020, 1, 1),
                java.time.LocalDate.of(2021, 12, 31)
        );
    }

    private ExperienciaDTO criarExperienciaDTOCompleto() {
        return new ExperienciaDTO(
                EXPERIENCIA_ID,
                CANDIDATO_ID,
                "Desenvolvedor Pleno",
                "Empresa XYZ",
                "APIs REST",
                java.time.LocalDate.of(2020, 1, 1),
                java.time.LocalDate.of(2021, 12, 31)
        );
    }

    private List<ExperienciaDTO> criarListaExperienciasDTO() {
        return List.of(criarExperienciaDTOCompleto());
    }
}
