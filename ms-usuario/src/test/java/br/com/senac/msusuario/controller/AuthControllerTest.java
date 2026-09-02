package br.com.senac.msusuario.controller;

@org.junit.jupiter.api.DisplayName("Testes da Classe AuthController")
@org.junit.jupiter.api.extension.ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AuthControllerTest {

    @org.mockito.Mock
    private br.com.senac.msusuario.service.KeycloakService keycloakService;

    @org.mockito.InjectMocks
    private AuthController authController;

    @org.junit.jupiter.api.Test
    void testLoginComSucesso() {
        br.com.senac.msusuario.dto.AuthLoginRequest request = new br.com.senac.msusuario.dto.AuthLoginRequest("joao@email.com", "senha123");
        br.com.senac.msusuario.dto.AuthTokenResponse response = criarAuthTokenResponse();

        org.mockito.Mockito.when(keycloakService.login("joao@email.com", "senha123")).thenReturn(response);

        br.com.senac.msusuario.dto.AuthTokenResponse resultado = authController.login(request);

        org.junit.jupiter.api.Assertions.assertNotNull(resultado);
        org.junit.jupiter.api.Assertions.assertEquals("access-token", resultado.accessToken());
        org.junit.jupiter.api.Assertions.assertEquals("refresh-token", resultado.refreshToken());
        org.junit.jupiter.api.Assertions.assertEquals(300L, resultado.expiresIn());
        org.junit.jupiter.api.Assertions.assertEquals(1800L, resultado.refreshExpiresIn());
        org.junit.jupiter.api.Assertions.assertEquals("Bearer", resultado.tokenType());
        org.junit.jupiter.api.Assertions.assertEquals("openid profile email", resultado.scope());
        org.mockito.Mockito.verify(keycloakService).login("joao@email.com", "senha123");
    }

    @org.junit.jupiter.api.Test
    void testRefreshComSucesso() {
        br.com.senac.msusuario.dto.AuthRefreshTokenRequest request = new br.com.senac.msusuario.dto.AuthRefreshTokenRequest("refresh-token");
        br.com.senac.msusuario.dto.AuthTokenResponse response = criarAuthTokenResponse();

        org.mockito.Mockito.when(keycloakService.refreshToken("refresh-token")).thenReturn(response);

        br.com.senac.msusuario.dto.AuthTokenResponse resultado = authController.refresh(request);

        org.junit.jupiter.api.Assertions.assertNotNull(resultado);
        org.junit.jupiter.api.Assertions.assertEquals("access-token", resultado.accessToken());
        org.junit.jupiter.api.Assertions.assertEquals("refresh-token", resultado.refreshToken());
        org.junit.jupiter.api.Assertions.assertEquals(300L, resultado.expiresIn());
        org.junit.jupiter.api.Assertions.assertEquals(1800L, resultado.refreshExpiresIn());
        org.junit.jupiter.api.Assertions.assertEquals("Bearer", resultado.tokenType());
        org.junit.jupiter.api.Assertions.assertEquals("openid profile email", resultado.scope());
        org.mockito.Mockito.verify(keycloakService).refreshToken("refresh-token");
    }

    private br.com.senac.msusuario.dto.AuthTokenResponse criarAuthTokenResponse() {
        return new br.com.senac.msusuario.dto.AuthTokenResponse(
                "access-token",
                "refresh-token",
                300L,
                1800L,
                "Bearer",
                "openid profile email"
        );
    }
}
