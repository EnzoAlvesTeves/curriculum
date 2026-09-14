package br.com.senac.msvagas.controller;

import br.com.senac.msvagas.dto.CreateEmpresaRequest;
import br.com.senac.msvagas.dto.EmpresaResponse;
import br.com.senac.msvagas.dto.UpdateEmpresaRequest;
import br.com.senac.msvagas.service.EmpresaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmpresaControllerTest {

    private static final String TOKEN = "token-123";
    private static final String AUTHORIZATION = "Bearer " + TOKEN;
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2026, 9, 3, 13, 0);
    private static final LocalDateTime UPDATED_AT = LocalDateTime.of(2026, 9, 3, 14, 0);

    @Mock
    private EmpresaService empresaService;

    @InjectMocks
    private EmpresaController empresaController;

    @Test
    @DisplayName("Deve criar empresa delegando o bearer token ao service")
    void deveCriarEmpresaDelegandoBearerTokenAoService() {
        CreateEmpresaRequest request = new CreateEmpresaRequest("Empresa X", "SP", "São Paulo", "Centro");
        Jwt jwt = jwtComToken(TOKEN);
        EmpresaResponse expectedResponse = novaResposta();

        when(empresaService.criar(request, AUTHORIZATION)).thenReturn(expectedResponse);

        EmpresaResponse actualResponse = empresaController.criar(request, jwt);

        assertSame(expectedResponse, actualResponse);
        verify(empresaService).criar(request, AUTHORIZATION);
    }

    @Test
    @DisplayName("Deve listar empresas delegando a chamada ao service")
    void deveListarEmpresasDelegandoChamadaAoService() {
        List<EmpresaResponse> expectedResponse = List.of(novaResposta(), novaResposta(2L, "Empresa Y"));

        when(empresaService.listar()).thenReturn(expectedResponse);

        List<EmpresaResponse> actualResponse = empresaController.listar();

        assertSame(expectedResponse, actualResponse);
        verify(empresaService).listar();
    }

    @Test
    @DisplayName("Deve buscar empresa por id delegando a chamada ao service")
    void deveBuscarEmpresaPorIdDelegandoChamadaAoService() {
        EmpresaResponse expectedResponse = novaResposta();

        when(empresaService.buscarPorId(10L)).thenReturn(expectedResponse);

        EmpresaResponse actualResponse = empresaController.buscarPorId(10L);

        assertSame(expectedResponse, actualResponse);
        verify(empresaService).buscarPorId(10L);
    }

    @Test
    @DisplayName("Deve atualizar empresa delegando o bearer token ao service")
    void deveAtualizarEmpresaDelegandoBearerTokenAoService() {
        UpdateEmpresaRequest request = new UpdateEmpresaRequest("Empresa Z", "RJ", "Rio de Janeiro", "Copacabana");
        Jwt jwt = jwtComToken(TOKEN);
        EmpresaResponse expectedResponse = novaResposta(3L, "Empresa Z");

        when(empresaService.atualizar(3L, request, AUTHORIZATION)).thenReturn(expectedResponse);

        EmpresaResponse actualResponse = empresaController.atualizar(3L, request, jwt);

        assertSame(expectedResponse, actualResponse);
        verify(empresaService).atualizar(3L, request, AUTHORIZATION);
    }

    @Test
    @DisplayName("Deve excluir empresa delegando o bearer token ao service")
    void deveExcluirEmpresaDelegandoBearerTokenAoService() {
        Jwt jwt = jwtComToken(TOKEN);

        empresaController.excluir(7L, jwt);

        verify(empresaService).excluir(7L, AUTHORIZATION);
    }

    private Jwt jwtComToken(String token) {
        Jwt jwt = org.mockito.Mockito.mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn(token);
        return jwt;
    }

    private EmpresaResponse novaResposta() {
        return novaResposta(1L, "Empresa X");
    }

    private EmpresaResponse novaResposta(Long id, String nome) {
        return new EmpresaResponse(id, nome, "SP", "São Paulo", "Centro", CREATED_AT, UPDATED_AT);
    }
}