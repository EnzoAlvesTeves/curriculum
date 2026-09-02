package br.com.senac.msvagas.service;

import br.com.senac.msvagas.client.MsUsuarioClient;
import br.com.senac.msvagas.dto.TipoUsuario;
import br.com.senac.msvagas.dto.UsuarioMeResponse;
import br.com.senac.msvagas.dto.UsuarioResponse;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioContextServiceTest {

    @Mock
    private MsUsuarioClient msUsuarioClient;

    @InjectMocks
    private UsuarioContextService usuarioContextService;

    private static final String AUTH_TOKEN = "Bearer token";

    @Test
    @DisplayName("Deve buscar usuario logado com sucesso")
    void deveBuscarUsuarioMeComSucesso() {
        UsuarioMeResponse expectedResponse = new UsuarioMeResponse(
                1L, TipoUsuario.CANDIDATO, "João", "Silva", "joao@email.com");

        when(msUsuarioClient.buscarUsuarioLogado(AUTH_TOKEN)).thenReturn(expectedResponse);

        UsuarioMeResponse actualResponse = usuarioContextService.buscarUsuarioMe(AUTH_TOKEN);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());
        assertEquals(expectedResponse.email(), actualResponse.email());
        verify(msUsuarioClient).buscarUsuarioLogado(AUTH_TOKEN);
    }

    @Test
    @DisplayName("Deve lancar UNAUTHORIZED quando o token for invalido no buscarUsuarioMe")
    void deveLancarUnauthorizedAoBuscarUsuarioMe() {
        FeignException.Unauthorized unauthorizedException = mock(FeignException.Unauthorized.class);
        when(msUsuarioClient.buscarUsuarioLogado(anyString())).thenThrow(unauthorizedException);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            usuarioContextService.buscarUsuarioMe(AUTH_TOKEN);
        });

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Token invalido ou expirado", exception.getReason());
    }

    @Test
    @DisplayName("Deve lancar BAD_GATEWAY quando ocorrer erro generico no Feign ao buscarUsuarioMe")
    void deveLancarBadGatewayAoBuscarUsuarioMeComErroGenerico() {
        FeignException feignException = mock(FeignException.class);
        when(msUsuarioClient.buscarUsuarioLogado(anyString())).thenThrow(feignException);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            usuarioContextService.buscarUsuarioMe(AUTH_TOKEN);
        });

        assertEquals(HttpStatus.BAD_GATEWAY, exception.getStatusCode());
        assertEquals("Falha ao consultar usuario autenticado no ms-usuario", exception.getReason());
    }

    @Test
    @DisplayName("Deve buscar usuarios por ids com sucesso")
    void deveBuscarUsuariosPorIdsComSucesso() {
        List<Long> ids = List.of(1L, 2L);
        UsuarioResponse user1 = new UsuarioResponse(1L, "uuid1", "João", "Silva", "joao@email.com", "11999999999", TipoUsuario.CANDIDATO, LocalDateTime.now(), LocalDateTime.now());
        UsuarioResponse user2 = new UsuarioResponse(2L, "uuid2", "Maria", "Souza", "maria@email.com", "11888888888", TipoUsuario.RH, LocalDateTime.now(), LocalDateTime.now());
        List<UsuarioResponse> expectedResponse = List.of(user1, user2);

        when(msUsuarioClient.buscarUsuariosPorIds(eq(AUTH_TOKEN), anyString())).thenReturn(expectedResponse);

        List<UsuarioResponse> actualResponse = usuarioContextService.buscarUsuariosPorIds(ids, AUTH_TOKEN);

        assertNotNull(actualResponse);
        assertEquals(2, actualResponse.size());
        verify(msUsuarioClient).buscarUsuariosPorIds(eq(AUTH_TOKEN), contains("1,2"));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando ids for nulo ou vazio")
    void deveRetornarListaVaziaQuandoIdsForNuloOuVazio() {
        assertTrue(usuarioContextService.buscarUsuariosPorIds(null, AUTH_TOKEN).isEmpty());
        assertTrue(usuarioContextService.buscarUsuariosPorIds(List.of(), AUTH_TOKEN).isEmpty());
        verifyNoInteractions(msUsuarioClient);
    }

    @Test
    @DisplayName("Deve lancar UNAUTHORIZED ao buscar usuarios por ids com token invalido")
    void deveLancarUnauthorizedAoBuscarUsuariosPorIds() {
        FeignException.Unauthorized unauthorizedException = mock(FeignException.Unauthorized.class);
        when(msUsuarioClient.buscarUsuariosPorIds(anyString(), anyString())).thenThrow(unauthorizedException);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            usuarioContextService.buscarUsuariosPorIds(List.of(1L), AUTH_TOKEN);
        });

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Token invalido ou expirado", exception.getReason());
    }

    @Test
    @DisplayName("Deve lancar BAD_GATEWAY quando usuario nao for encontrado no buscarUsuariosPorIds")
    void deveLancarBadGatewayQuandoUsuarioNaoEncontrado() {
        FeignException.NotFound notFoundException = mock(FeignException.NotFound.class);
        when(msUsuarioClient.buscarUsuariosPorIds(anyString(), anyString())).thenThrow(notFoundException);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            usuarioContextService.buscarUsuariosPorIds(List.of(1L), AUTH_TOKEN);
        });

        assertEquals(HttpStatus.BAD_GATEWAY, exception.getStatusCode());
        assertEquals("Nem todos os candidatos foram encontrados no ms-usuario", exception.getReason());
    }

    @Test
    @DisplayName("Deve lancar BAD_GATEWAY em erro generico no buscarUsuariosPorIds")
    void deveLancarBadGatewayEmErroGenericoAoBuscarUsuariosPorIds() {
        FeignException feignException = mock(FeignException.class);
        when(msUsuarioClient.buscarUsuariosPorIds(anyString(), anyString())).thenThrow(feignException);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            usuarioContextService.buscarUsuariosPorIds(List.of(1L), AUTH_TOKEN);
        });

        assertEquals(HttpStatus.BAD_GATEWAY, exception.getStatusCode());
        assertEquals("Falha ao consultar candidatos no ms-usuario", exception.getReason());
    }
}
