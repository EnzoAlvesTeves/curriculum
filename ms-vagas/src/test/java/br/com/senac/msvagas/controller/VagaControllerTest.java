package br.com.senac.msvagas.controller;

import br.com.senac.msvagas.dto.CandidaturaResponse;
import br.com.senac.msvagas.dto.CreateVagaRequest;
import br.com.senac.msvagas.dto.TipoUsuario;
import br.com.senac.msvagas.dto.UpdateVagaRequest;
import br.com.senac.msvagas.dto.UsuarioResponse;
import br.com.senac.msvagas.dto.VagaResponse;
import br.com.senac.msvagas.service.VagaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VagaControllerTest {

    private static final String TOKEN = "token-123";
    private static final String AUTHORIZATION = "Bearer " + TOKEN;
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2026, 9, 3, 13, 0);
    private static final LocalDateTime UPDATED_AT = LocalDateTime.of(2026, 9, 3, 14, 0);

    @Mock
    private VagaService vagaService;

    @InjectMocks
    private VagaController vagaController;

    @Test
    @DisplayName("Deve criar vaga delegando o bearer token ao service")
    void deveCriarVagaDelegandoBearerTokenAoService() {
        CreateVagaRequest request = novaCreateVagaRequest();
        Jwt jwt = jwtComToken(TOKEN);
        VagaResponse expectedResponse = novaVagaResponse(1L);

        when(vagaService.criar(request, AUTHORIZATION)).thenReturn(expectedResponse);

        VagaResponse actualResponse = vagaController.criar(request, jwt);

        assertSame(expectedResponse, actualResponse);
        verify(vagaService).criar(request, AUTHORIZATION);
    }

    @Test
    @DisplayName("Deve atualizar vaga delegando o bearer token ao service")
    void deveAtualizarVagaDelegandoBearerTokenAoService() {
        UpdateVagaRequest request = novaUpdateVagaRequest();
        Jwt jwt = jwtComToken(TOKEN);
        VagaResponse expectedResponse = novaVagaResponse(2L);

        when(vagaService.atualizar(2L, request, AUTHORIZATION)).thenReturn(expectedResponse);

        VagaResponse actualResponse = vagaController.atualizar(2L, request, jwt);

        assertSame(expectedResponse, actualResponse);
        verify(vagaService).atualizar(2L, request, AUTHORIZATION);
    }

    @Test
    @DisplayName("Deve excluir vaga delegando o bearer token ao service")
    void deveExcluirVagaDelegandoBearerTokenAoService() {
        Jwt jwt = jwtComToken(TOKEN);

        vagaController.deletar(3L, jwt);

        verify(vagaService).deletar(3L, AUTHORIZATION);
    }

    @Test
    @DisplayName("Deve buscar vagas delegando o bearer token ao service")
    void deveBuscarVagasDelegandoBearerTokenAoService() {
        Jwt jwt = jwtComToken(TOKEN);
        List<VagaResponse> expectedResponse = List.of(novaVagaResponse(1L), novaVagaResponse(2L));

        when(vagaService.buscarVagas(AUTHORIZATION)).thenReturn(expectedResponse);

        List<VagaResponse> actualResponse = vagaController.buscarVagas(jwt);

        assertSame(expectedResponse, actualResponse);
        verify(vagaService).buscarVagas(AUTHORIZATION);
    }

    @Test
    @DisplayName("Deve buscar vagas por empresa delegando a chamada ao service")
    void deveBuscarVagasPorEmpresaDelegandoChamadaAoService() {
        List<VagaResponse> expectedResponse = List.of(novaVagaResponse(1L));

        when(vagaService.buscarPorEmpresa(9L)).thenReturn(expectedResponse);

        List<VagaResponse> actualResponse = vagaController.buscarPorEmpresa(9L);

        assertSame(expectedResponse, actualResponse);
        verify(vagaService).buscarPorEmpresa(9L);
    }

    @Test
    @DisplayName("Deve buscar minhas vagas criadas delegando o bearer token ao service")
    void deveBuscarMinhasCriadasDelegandoBearerTokenAoService() {
        Jwt jwt = jwtComToken(TOKEN);
        List<VagaResponse> expectedResponse = List.of(novaVagaResponse(4L));

        when(vagaService.buscarMinhasCriadas(AUTHORIZATION)).thenReturn(expectedResponse);

        List<VagaResponse> actualResponse = vagaController.buscarMinhasCriadas(jwt);

        assertSame(expectedResponse, actualResponse);
        verify(vagaService).buscarMinhasCriadas(AUTHORIZATION);
    }

    @Test
    @DisplayName("Deve buscar candidatos por vaga delegando o bearer token ao service")
    void deveBuscarCandidatosPorVagaDelegandoBearerTokenAoService() {
        Jwt jwt = jwtComToken(TOKEN);
        List<UsuarioResponse> expectedResponse = List.of(novoUsuarioResponse());

        when(vagaService.buscarCandidatosPorVaga(7L, AUTHORIZATION)).thenReturn(expectedResponse);

        List<UsuarioResponse> actualResponse = vagaController.buscarCandidatosPorVaga(7L, jwt);

        assertSame(expectedResponse, actualResponse);
        verify(vagaService).buscarCandidatosPorVaga(7L, AUTHORIZATION);
    }

    @Test
    @DisplayName("Deve candidatar-se a vaga devolvendo CREATED")
    void deveCandidatarSeAVagaDevolvendoCreated() {
        Jwt jwt = jwtComToken(TOKEN);
        CandidaturaResponse expectedBody = novaCandidaturaResponse();

        when(vagaService.candidatar(11L, AUTHORIZATION)).thenReturn(expectedBody);

        ResponseEntity<CandidaturaResponse> actualResponse = vagaController.candidatar(11L, jwt);

        assertEquals(HttpStatus.CREATED, actualResponse.getStatusCode());
        assertSame(expectedBody, actualResponse.getBody());
        verify(vagaService).candidatar(11L, AUTHORIZATION);
    }

    @Test
    @DisplayName("Deve remover candidatura delegando o bearer token ao service")
    void deveRemoverCandidaturaDelegandoBearerTokenAoService() {
        Jwt jwt = jwtComToken(TOKEN);

        vagaController.removerCandidatura(11L, jwt);

        verify(vagaService).removerCandidatura(11L, AUTHORIZATION);
    }

    @Test
    @DisplayName("Deve buscar minhas candidaturas delegando o bearer token ao service")
    void deveBuscarMinhasCandidaturasDelegandoBearerTokenAoService() {
        Jwt jwt = jwtComToken(TOKEN);
        List<VagaResponse> expectedResponse = List.of(novaVagaResponse(5L), novaVagaResponse(6L));

        when(vagaService.buscarMinhasCandidaturas(AUTHORIZATION)).thenReturn(expectedResponse);

        List<VagaResponse> actualResponse = vagaController.buscarMinhasCandidaturas(jwt);

        assertSame(expectedResponse, actualResponse);
        verify(vagaService).buscarMinhasCandidaturas(AUTHORIZATION);
    }

    private Jwt jwtComToken(String token) {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn(token);
        return jwt;
    }

    private CreateVagaRequest novaCreateVagaRequest() {
        return new CreateVagaRequest(
                "Desenvolvedor Java",
                "Vaga para backend",
                new BigDecimal("7500.00"),
                "VR, VA",
                10L
        );
    }

    private UpdateVagaRequest novaUpdateVagaRequest() {
        return new UpdateVagaRequest(
                "Desenvolvedor Java Sênior",
                "Vaga para backend sênior",
                new BigDecimal("9500.00"),
                "VR, VA, plano de saúde",
                10L
        );
    }

    private VagaResponse novaVagaResponse(Long id) {
        return new VagaResponse(
                id,
                "Desenvolvedor Java",
                "Vaga para backend",
                new BigDecimal("7500.00"),
                "VR, VA",
                10L,
                99L,
                CREATED_AT,
                UPDATED_AT
        );
    }

    private UsuarioResponse novoUsuarioResponse() {
        return new UsuarioResponse(
                1L,
                "keycloak-user-id",
                "Ana",
                "Silva",
                "ana@email.com",
                "11999999999",
                TipoUsuario.CANDIDATO,
                CREATED_AT,
                UPDATED_AT
        );
    }

    private CandidaturaResponse novaCandidaturaResponse() {
        return new CandidaturaResponse(1L, 1L, 11L, CREATED_AT, UPDATED_AT);
    }
}
