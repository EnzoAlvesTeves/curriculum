package br.com.senac.msusuario.controller;

import br.com.senac.msusuario.dto.AuthLoginRequest;
import br.com.senac.msusuario.dto.AuthTokenResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

@DisplayName("Testes da Classe AuthController")
@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private br.com.senac.msusuario.service.KeycloakService keycloakService;

    @InjectMocks
    private AuthController authController;

    @Test
    void testLoginComSucesso() {
        AuthLoginRequest request = new AuthLoginRequest("joao@email.com", "senha123");
        AuthTokenResponse response = criarAuthTokenResponse();

        Mockito.when(keycloakService.login("joao@email.com", "senha123")).thenReturn(response);

        AuthTokenResponse resultado = authController.login(request);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("access-token", resultado.accessToken());
        Assertions.assertEquals("refresh-token", resultado.refreshToken());
        Assertions.assertEquals(300L, resultado.expiresIn());
        Assertions.assertEquals(1800L, resultado.refreshExpiresIn());
        Assertions.assertEquals("Bearer", resultado.tokenType());
        Assertions.assertEquals("openid profile email", resultado.scope());
        Mockito.verify(keycloakService).login("joao@email.com", "senha123");
    }

    @Test
    void testRefreshComSucesso() {
        br.com.senac.msusuario.dto.AuthRefreshTokenRequest request = new br.com.senac.msusuario.dto.AuthRefreshTokenRequest("refresh-token");
        br.com.senac.msusuario.dto.AuthTokenResponse response = criarAuthTokenResponse();

        Mockito.when(keycloakService.refreshToken("refresh-token")).thenReturn(response);

        AuthTokenResponse resultado = authController.refresh(request);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("access-token", resultado.accessToken());
        Assertions.assertEquals("refresh-token", resultado.refreshToken());
        Assertions.assertEquals(300L, resultado.expiresIn());
        Assertions.assertEquals(1800L, resultado.refreshExpiresIn());
        Assertions.assertEquals("Bearer", resultado.tokenType());
        Assertions.assertEquals("openid profile email", resultado.scope());
        Mockito.verify(keycloakService).refreshToken("refresh-token");
    }

    private AuthTokenResponse criarAuthTokenResponse() {
        return new AuthTokenResponse(
                "access-token",
                "refresh-token",
                300L,
                1800L,
                "Bearer",
                "openid profile email"
        );
    }
}
