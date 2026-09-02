package br.com.senac.mscurriculum.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import br.com.senac.mscurriculum.client.MsUsuarioClient;
import br.com.senac.mscurriculum.dto.UsuarioMeResponse;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioContextService Tests")
class UsuarioContextServiceTest {

    @Mock
    private MsUsuarioClient msUsuarioClient;

    @InjectMocks
    private UsuarioContextService usuarioContextService;

    private static final String AUTH_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
    private static final String INVALID_TOKEN = "Bearer invalid_token";

    private UsuarioMeResponse usuarioMeResponseValido;

    @BeforeEach
    void setUp() {
        usuarioMeResponseValido = new UsuarioMeResponse(
                1L,
                null,  // TipoUsuario - será ajustado conforme sua implementação
                "João",
                "Silva",
                "joao.silva@email.com"
        );
    }

    // ==================== buscarUsuarioMe Tests ====================

    @Test
    @DisplayName("Deve buscar usuario logado com sucesso")
    void deveBuscarUsuarioMeComSucesso() {
        // Arrange
        when(msUsuarioClient.buscarUsuarioLogado(AUTH_TOKEN))
                .thenReturn(usuarioMeResponseValido);

        // Act
        UsuarioMeResponse resultado = usuarioContextService.buscarUsuarioMe(AUTH_TOKEN);

        // Assert
        assertNotNull(resultado);
        assertEquals(usuarioMeResponseValido.id(), resultado.id());
        assertEquals(usuarioMeResponseValido.email(), resultado.email());
        assertEquals(usuarioMeResponseValido.nome(), resultado.nome());
        assertEquals(usuarioMeResponseValido.sobrenome(), resultado.sobrenome());

        verify(msUsuarioClient, times(1)).buscarUsuarioLogado(AUTH_TOKEN);
    }

    @Test
    @DisplayName("Deve lancar UNAUTHORIZED quando o token for invalido")
    void deveLancarUnauthorizedQuandoTokenInvalido() {
        // Arrange
        FeignException.Unauthorized unauthorizedException = mock(FeignException.Unauthorized.class);
        when(msUsuarioClient.buscarUsuarioLogado(INVALID_TOKEN))
                .thenThrow(unauthorizedException);

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> usuarioContextService.buscarUsuarioMe(INVALID_TOKEN)
        );

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Token invalido ou expirado", exception.getReason());
        verify(msUsuarioClient).buscarUsuarioLogado(INVALID_TOKEN);
    }

    @Test
    @DisplayName("Deve lancar UNAUTHORIZED quando o token for expirado")
    void deveLancarUnauthorizedQuandoTokenExpirado() {
        // Arrange
        FeignException.Unauthorized unauthorizedException = mock(FeignException.Unauthorized.class);
        when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenThrow(unauthorizedException);

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> usuarioContextService.buscarUsuarioMe(AUTH_TOKEN)
        );

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        verify(msUsuarioClient).buscarUsuarioLogado(AUTH_TOKEN);
    }

    @Test
    @DisplayName("Deve lancar BAD_GATEWAY quando ocorrer erro generico do Feign")
    void deveLancarBadGatewayAoOcorrerErroGenericoFeign() {
        // Arrange
        FeignException feignException = mock(FeignException.class);
        when(msUsuarioClient.buscarUsuarioLogado(AUTH_TOKEN))
                .thenThrow(feignException);

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> usuarioContextService.buscarUsuarioMe(AUTH_TOKEN)
        );

        assertEquals(HttpStatus.BAD_GATEWAY, exception.getStatusCode());
        assertEquals("Falha ao consultar usuario autenticado no ms-usuario", exception.getReason());
        verify(msUsuarioClient).buscarUsuarioLogado(AUTH_TOKEN);
    }

    @Test
    @DisplayName("Deve lancar BAD_GATEWAY quando servico estiver indisponivel")
    void deveLancarBadGatewayQuandoServicoIndisponivel() {
        // Arrange
        FeignException.ServiceUnavailable unavailableException = mock(FeignException.ServiceUnavailable.class);
        when(msUsuarioClient.buscarUsuarioLogado(AUTH_TOKEN))
                .thenThrow(unavailableException);

        // Act & Assert
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> usuarioContextService.buscarUsuarioMe(AUTH_TOKEN)
        );

        assertEquals(HttpStatus.BAD_GATEWAY, exception.getStatusCode());
    }

    @Test
    @DisplayName("Deve verificar que o cliente Feign foi chamado corretamente")
    void deveVerificarChamadaDoClienteFeign() {
        // Arrange
        when(msUsuarioClient.buscarUsuarioLogado(AUTH_TOKEN))
                .thenReturn(usuarioMeResponseValido);

        // Act
        usuarioContextService.buscarUsuarioMe(AUTH_TOKEN);

        // Assert
        verify(msUsuarioClient, times(1)).buscarUsuarioLogado(AUTH_TOKEN);
        verifyNoMoreInteractions(msUsuarioClient);
    }
}