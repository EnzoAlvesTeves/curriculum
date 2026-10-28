package br.com.senac.msusuario.service;

import br.com.senac.msusuario.config.KeycloakProperties;
import br.com.senac.msusuario.dto.AuthTokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakServiceTest {

    @Mock
    private KeycloakProperties keycloakProperties;

    @Mock
    private RestClient.Builder restClientBuilder;

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    private KeycloakService keycloakService;

    @BeforeEach
    void setUp() {
        when(restClientBuilder.build()).thenReturn(restClient);
        lenient().when(keycloakProperties.getServerUrl()).thenReturn("http://keycloak.local");
        lenient().when(keycloakProperties.getRealm()).thenReturn("curriculum");
        lenient().when(keycloakProperties.getUserClientId()).thenReturn("user-client");
        lenient().when(keycloakProperties.getAdminClientId()).thenReturn("admin-client");
        lenient().when(keycloakProperties.getAdminClientSecret()).thenReturn("admin-secret");
        keycloakService = new KeycloakService(restClientBuilder, keycloakProperties);
    }

    @Test
    @DisplayName("Deve validar construtor de KeycloakService")
    void deveValidarConstrutor() {
        assertNotNull(keycloakService);
    }

    @Test
    @DisplayName("Deve executar login com sucesso")
    void deveExecutarLoginComSucesso() {
        Map<String, Object> payload = Map.of(
                "access_token", "access-token",
                "refresh_token", "refresh-token",
                "expires_in", 300,
                "refresh_expires_in", 1800,
                "token_type", "Bearer",
                "scope", "openid profile"
        );

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.contentType(any(MediaType.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.body(any(MultiValueMap.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.body(Map.class)).thenReturn(payload);

        AuthTokenResponse result = keycloakService.login("joao@email.com", "senha123");

        assertNotNull(result);
        assertEquals("access-token", result.accessToken());
        assertEquals("refresh-token", result.refreshToken());
        assertEquals(300L, result.expiresIn());
        assertEquals(1800L, result.refreshExpiresIn());
        assertEquals("Bearer", result.tokenType());
        assertEquals("openid profile", result.scope());
    }

    @Test
    @DisplayName("Deve executar refresh token com sucesso")
    void deveExecutarRefreshTokenComSucesso() {
        Map<String, Object> payload = Map.of(
                "access_token", "new-access-token",
                "refresh_token", "new-refresh-token",
                "expires_in", 600,
                "refresh_expires_in", 3600,
                "token_type", "Bearer",
                "scope", "email"
        );

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.contentType(any(MediaType.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.body(any(MultiValueMap.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.body(Map.class)).thenReturn(payload);

        AuthTokenResponse result = keycloakService.refreshToken("old-refresh-token");

        assertNotNull(result);
        assertEquals("new-access-token", result.accessToken());
        assertEquals("new-refresh-token", result.refreshToken());
        assertEquals(600L, result.expiresIn());
        assertEquals(3600L, result.refreshExpiresIn());
    }

    @Test
    @DisplayName("Deve lançar exceção quando a resposta do Keycloak for nula")
    void deveLancarExcecaoQuandoRespostaForNula() throws Exception {
        Method method = KeycloakService.class.getDeclaredMethod("toTokenResponse", Map.class);
        method.setAccessible(true);

        Throwable thrown = assertThrows(InvocationTargetException.class,
                () -> method.invoke(keycloakService, (Object) null));
        ResponseStatusException exception = (ResponseStatusException) ((InvocationTargetException) thrown).getCause();

        assertEquals(401, exception.getStatusCode().value());
        assertEquals("Resposta vazia do Keycloak", exception.getReason());
    }

    @Test
    @DisplayName("Deve cobrir fluxo de erro do login do Keycloak")
    void deveLancarExcecaoQuandoLoginForInvalido() {
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.contentType(any(MediaType.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.body(any(MultiValueMap.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        });

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> keycloakService.login("joao@email.com", "senha-errada"));

        assertEquals(401, ex.getStatusCode().value());
        assertEquals("Credenciais inválidas", ex.getReason());
    }

    @Test
    @DisplayName("Deve cobrir fluxo de erro do refresh token do Keycloak")
    void deveLancarExcecaoQuandoRefreshTokenForInvalido() {
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.contentType(any(MediaType.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.body(any(MultiValueMap.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Refresh token inválido ou expirado");
        });

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> keycloakService.refreshToken("token-invalido"));

        assertEquals(401, ex.getStatusCode().value());
        assertEquals("Refresh token inválido ou expirado", ex.getReason());
    }

    @Test
    @DisplayName("Deve cobrir falha ao obter token admin sem access token")
    void deveLancarExcecaoQuandoTokenAdminNaoVierNaResposta() {
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.contentType(any(MediaType.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.body(any(MultiValueMap.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.body(Map.class)).thenReturn(Map.of());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> keycloakService.criarUsuarioKeycloak("Joao", "Silva", "joao@email.com", "Senha123!"));

        assertEquals(502, ex.getStatusCode().value());
        assertEquals("Token admin não encontrado na resposta do Keycloak", ex.getReason());
    }

    @Test
    @DisplayName("Deve cobrir conflito ao criar usuario no Keycloak")
    void deveLancarConflitoAoCriarUsuarioNoKeycloak() {
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        lenient().when(requestBodyUriSpec.header(anyString(), anyString())).thenReturn(requestBodyUriSpec);
        lenient().when(requestBodyUriSpec.contentType(any(MediaType.class))).thenReturn(requestBodyUriSpec);
        lenient().when(requestBodyUriSpec.body(any(Map.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT, "Usuário já existe no Keycloak");
        });

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> keycloakService.criarUsuarioKeycloak("Joao", "Silva", "joao@email.com", "Senha123!"));

        assertEquals(409, ex.getStatusCode().value());
        assertEquals("Usuário já existe no Keycloak", ex.getReason());
    }

    @Test
    @DisplayName("Deve cobrir falha ao deletar usuario no Keycloak")
    void deveLancarExcecaoQuandoDeletarUsuarioFalhar() {
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        lenient().when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        lenient().when(requestBodyUriSpec.contentType(any(MediaType.class))).thenReturn(requestBodyUriSpec);
        lenient().when(requestBodyUriSpec.body(any(MultiValueMap.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        lenient().when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.body(Map.class)).thenReturn(Map.of("access_token", "admin-token"));

        when(restClient.delete()).thenReturn((RestClient.RequestHeadersUriSpec) requestHeadersUriSpec);
        lenient().when(((RestClient.RequestHeadersUriSpec) requestHeadersUriSpec).uri(anyString())).thenReturn((RestClient.RequestHeadersUriSpec) requestHeadersUriSpec);
        lenient().when(((RestClient.RequestHeadersUriSpec) requestHeadersUriSpec).header(anyString(), anyString())).thenReturn((RestClient.RequestHeadersUriSpec) requestHeadersUriSpec);
        lenient().when(((RestClient.RequestHeadersUriSpec) requestHeadersUriSpec).retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_GATEWAY, "Falha ao deletar usuário no Keycloak");
        });

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> keycloakService.deletarUsuarioKeycloak("user-123"));

        assertEquals(502, ex.getStatusCode().value());
        assertEquals("Falha ao deletar usuário no Keycloak", ex.getReason());
    }

    @Test
    @DisplayName("Deve cobrir falha ao alterar senha do usuario no Keycloak")
    void deveLancarExcecaoQuandoAlterarSenhaFalhar() {
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        lenient().when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        lenient().when(requestBodyUriSpec.contentType(any(MediaType.class))).thenReturn(requestBodyUriSpec);
        lenient().when(requestBodyUriSpec.body(any(MultiValueMap.class))).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        lenient().when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.body(Map.class)).thenReturn(Map.of("access_token", "admin-token"));

        when(restClient.put()).thenReturn((RestClient.RequestBodyUriSpec) requestBodyUriSpec);
        lenient().when(((RestClient.RequestBodyUriSpec) requestBodyUriSpec).uri(anyString())).thenReturn((RestClient.RequestBodyUriSpec) requestBodyUriSpec);
        lenient().when(((RestClient.RequestBodyUriSpec) requestBodyUriSpec).header(anyString(), anyString())).thenReturn((RestClient.RequestBodyUriSpec) requestBodyUriSpec);
        lenient().when(((RestClient.RequestBodyUriSpec) requestBodyUriSpec).contentType(any(MediaType.class))).thenReturn((RestClient.RequestBodyUriSpec) requestBodyUriSpec);
        lenient().when(((RestClient.RequestBodyUriSpec) requestBodyUriSpec).body(any(Map.class))).thenReturn((RestClient.RequestBodyUriSpec) requestBodyUriSpec);
        lenient().when(((RestClient.RequestBodyUriSpec) requestBodyUriSpec).retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_GATEWAY, "Falha ao alterar senha no Keycloak");
        });

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> keycloakService.alterarSenhaKeycloak("user-123", "novaSenha123"));

        assertEquals(502, ex.getStatusCode().value());
        assertEquals("Falha ao alterar senha no Keycloak", ex.getReason());
    }

    @Test
    @DisplayName("Deve converter valores nulos em helper str e toLong")
    void deveCobrirHelpersComValoresNulos() throws Exception {
        Method str = KeycloakService.class.getDeclaredMethod("str", Object.class);
        Method toLong = KeycloakService.class.getDeclaredMethod("toLong", Object.class);
        str.setAccessible(true);
        toLong.setAccessible(true);

        assertNull(str.invoke(keycloakService, (Object) null));
        assertNull(toLong.invoke(keycloakService, (Object) null));
        assertEquals(123L, toLong.invoke(keycloakService, 123));
        assertEquals("123", str.invoke(keycloakService, 123));
    }
}