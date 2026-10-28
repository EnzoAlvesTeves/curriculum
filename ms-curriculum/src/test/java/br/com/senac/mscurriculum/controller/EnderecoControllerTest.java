package br.com.senac.mscurriculum.controller;

import br.com.senac.mscurriculum.dto.EnderecoDTO;
import br.com.senac.mscurriculum.service.EnderecoService;
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

@DisplayName("Testes da Classe EnderecoController")
@ExtendWith(MockitoExtension.class)
class EnderecoControllerTest {

    @Mock
    private EnderecoService enderecoService;

    @InjectMocks
    private EnderecoController enderecoController;

    @Mock
    private Jwt jwt;

    private static final Long CANDIDATO_ID = 20L;

    @Test
    void testCadastrarComSucesso() {
        EnderecoDTO request = criarEnderecoDTO();
        EnderecoDTO response = criarEnderecoDTOCompleto();

        Mockito.when(enderecoService.cadastrar(
                        ArgumentMatchers.eq(CANDIDATO_ID),
                        ArgumentMatchers.any(EnderecoDTO.class),
                        ArgumentMatchers.any(Jwt.class)))
                .thenReturn(response);

        EnderecoDTO resultado = enderecoController.cadastrar(CANDIDATO_ID, request, jwt);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(CANDIDATO_ID, resultado.getIdCandidato());
        Assertions.assertEquals("Rua A", resultado.getRua());
        Mockito.verify(enderecoService).cadastrar(
                ArgumentMatchers.eq(CANDIDATO_ID),
                ArgumentMatchers.any(EnderecoDTO.class),
                ArgumentMatchers.any(Jwt.class));
    }

    @Test
    void testAlterarComSucesso() {
        EnderecoDTO request = criarEnderecoDTO();
        EnderecoDTO response = criarEnderecoDTOCompleto();

        Mockito.when(enderecoService.alterar(
                        ArgumentMatchers.eq(CANDIDATO_ID),
                        ArgumentMatchers.any(EnderecoDTO.class),
                        ArgumentMatchers.any(Jwt.class)))
                .thenReturn(response);

        EnderecoDTO resultado = enderecoController.alterar(CANDIDATO_ID, request, jwt);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(CANDIDATO_ID, resultado.getIdCandidato());
        Assertions.assertEquals("Rua A", resultado.getRua());
        Mockito.verify(enderecoService).alterar(
                ArgumentMatchers.eq(CANDIDATO_ID),
                ArgumentMatchers.any(EnderecoDTO.class),
                ArgumentMatchers.any(Jwt.class));
    }

    @Test
    void testBuscarComSucesso() {
        EnderecoDTO response = criarEnderecoDTOCompleto();

        Mockito.when(enderecoService.buscarPorCandidato(CANDIDATO_ID)).thenReturn(response);

        EnderecoDTO resultado = enderecoController.buscar(CANDIDATO_ID);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(CANDIDATO_ID, resultado.getIdCandidato());
        Assertions.assertEquals("Rua A", resultado.getRua());
        Mockito.verify(enderecoService).buscarPorCandidato(CANDIDATO_ID);
    }

    @Test
    void testDeletarComSucesso() {
        enderecoController.deletar(CANDIDATO_ID, jwt);

        Mockito.verify(enderecoService).deletar(
                ArgumentMatchers.eq(CANDIDATO_ID),
                ArgumentMatchers.any(Jwt.class));
    }

    private EnderecoDTO criarEnderecoDTO() {
        return new EnderecoDTO(
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

    private EnderecoDTO criarEnderecoDTOCompleto() {
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
}
