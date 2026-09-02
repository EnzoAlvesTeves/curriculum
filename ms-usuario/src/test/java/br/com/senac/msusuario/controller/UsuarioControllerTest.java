package br.com.senac.msusuario.controller;

@org.junit.jupiter.api.DisplayName("Testes da Classe UsuarioController")
@org.junit.jupiter.api.extension.ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UsuarioControllerTest {

    @org.mockito.Mock
    private br.com.senac.msusuario.service.UsuarioService service;

    @org.mockito.InjectMocks
    private UsuarioController usuarioController;

    @org.mockito.Mock
    private org.springframework.security.oauth2.jwt.Jwt jwt;

    private static final Long USUARIO_ID = 10L;
    private static final String KEYCLOAK_USER_ID = "kc-user-id";

    @org.junit.jupiter.api.Test
    void testCriarComSucesso() {
        br.com.senac.msusuario.dto.CreateUsuarioRequest request = criarCreateUsuarioRequest();
        br.com.senac.msusuario.dto.UsuarioResponse response = criarUsuarioResponse();

        org.mockito.Mockito.when(service.criar(org.mockito.ArgumentMatchers.any(br.com.senac.msusuario.dto.CreateUsuarioRequest.class)))
                .thenReturn(response);

        br.com.senac.msusuario.dto.UsuarioResponse resultado = usuarioController.criar(request);

        org.junit.jupiter.api.Assertions.assertNotNull(resultado);
        org.junit.jupiter.api.Assertions.assertEquals(USUARIO_ID, resultado.id());
        org.junit.jupiter.api.Assertions.assertEquals(KEYCLOAK_USER_ID, resultado.keycloakUserId());
        org.junit.jupiter.api.Assertions.assertEquals("João", resultado.nome());
        org.mockito.Mockito.verify(service).criar(org.mockito.ArgumentMatchers.any(br.com.senac.msusuario.dto.CreateUsuarioRequest.class));
    }

    @org.junit.jupiter.api.Test
    void testListarComSucesso() {
        java.util.List<br.com.senac.msusuario.dto.UsuarioResponse> response = java.util.List.of(criarUsuarioResponse());

        org.mockito.Mockito.when(service.listar()).thenReturn(response);

        java.util.List<br.com.senac.msusuario.dto.UsuarioResponse> resultado = usuarioController.listar();

        org.junit.jupiter.api.Assertions.assertNotNull(resultado);
        org.junit.jupiter.api.Assertions.assertEquals(1, resultado.size());
        org.junit.jupiter.api.Assertions.assertEquals(USUARIO_ID, resultado.get(0).id());
        org.mockito.Mockito.verify(service).listar();
    }

    @org.junit.jupiter.api.Test
    void testBuscarPorIdsComSucesso() {
        java.util.List<Long> ids = java.util.List.of(USUARIO_ID, 11L);
        java.util.List<br.com.senac.msusuario.dto.UsuarioResponse> response = java.util.List.of(
                criarUsuarioResponse(),
                new br.com.senac.msusuario.dto.UsuarioResponse(
                        11L,
                        "kc-user-id-2",
                        "Maria",
                        "Silva",
                        "maria@email.com",
                        "11999999998",
                        "CANDIDATO",
                        java.time.LocalDateTime.of(2026, 1, 1, 10, 0),
                        java.time.LocalDateTime.of(2026, 1, 2, 10, 0)
                )
        );

        org.mockito.Mockito.when(service.buscarPorIds(ids)).thenReturn(response);

        java.util.List<br.com.senac.msusuario.dto.UsuarioResponse> resultado = usuarioController.buscarPorIds(ids);

        org.junit.jupiter.api.Assertions.assertNotNull(resultado);
        org.junit.jupiter.api.Assertions.assertEquals(2, resultado.size());
        org.junit.jupiter.api.Assertions.assertEquals(USUARIO_ID, resultado.get(0).id());
        org.mockito.Mockito.verify(service).buscarPorIds(ids);
    }

    @org.junit.jupiter.api.Test
    void testMeComSucesso() {
        br.com.senac.msusuario.dto.UsuarioResponse response = criarUsuarioResponse();

        org.mockito.Mockito.when(service.buscarMe(jwt)).thenReturn(response);

        br.com.senac.msusuario.dto.UsuarioResponse resultado = usuarioController.me(jwt);

        org.junit.jupiter.api.Assertions.assertNotNull(resultado);
        org.junit.jupiter.api.Assertions.assertEquals(USUARIO_ID, resultado.id());
        org.junit.jupiter.api.Assertions.assertEquals(KEYCLOAK_USER_ID, resultado.keycloakUserId());
        org.mockito.Mockito.verify(service).buscarMe(jwt);
    }

    @org.junit.jupiter.api.Test
    void testBuscarComSucesso() {
        br.com.senac.msusuario.dto.UsuarioResponse response = criarUsuarioResponse();

        org.mockito.Mockito.when(service.buscarPorId(USUARIO_ID)).thenReturn(response);

        br.com.senac.msusuario.dto.UsuarioResponse resultado = usuarioController.buscar(USUARIO_ID);

        org.junit.jupiter.api.Assertions.assertNotNull(resultado);
        org.junit.jupiter.api.Assertions.assertEquals(USUARIO_ID, resultado.id());
        org.mockito.Mockito.verify(service).buscarPorId(USUARIO_ID);
    }

    @org.junit.jupiter.api.Test
    void testAtualizarComSucesso() {
        br.com.senac.msusuario.dto.UpdateUsuarioRequest request = criarUpdateUsuarioRequest();
        br.com.senac.msusuario.dto.UsuarioResponse response = criarUsuarioResponse();

        org.mockito.Mockito.when(service.atualizar(org.mockito.ArgumentMatchers.eq(USUARIO_ID), org.mockito.ArgumentMatchers.any(br.com.senac.msusuario.dto.UpdateUsuarioRequest.class)))
                .thenReturn(response);

        br.com.senac.msusuario.dto.UsuarioResponse resultado = usuarioController.atualizar(USUARIO_ID, request);

        org.junit.jupiter.api.Assertions.assertNotNull(resultado);
        org.junit.jupiter.api.Assertions.assertEquals(USUARIO_ID, resultado.id());
        org.mockito.Mockito.verify(service).atualizar(org.mockito.ArgumentMatchers.eq(USUARIO_ID), org.mockito.ArgumentMatchers.any(br.com.senac.msusuario.dto.UpdateUsuarioRequest.class));
    }

    @org.junit.jupiter.api.Test
    void testDeletarComSucesso() {
        usuarioController.deletar(USUARIO_ID);

        org.mockito.Mockito.verify(service).deletar(USUARIO_ID);
    }

    @org.junit.jupiter.api.Test
    void testAlterarMinhaSenhaComSucesso() {
        br.com.senac.msusuario.dto.AlterarSenhaRequest request = new br.com.senac.msusuario.dto.AlterarSenhaRequest("senhaAtual123", "novaSenha123");

        usuarioController.alterarMinhaSenha(jwt, request);

        org.mockito.Mockito.verify(service).alterarMinhaSenha(jwt, request);
    }

    @org.junit.jupiter.api.Test
    void testAlterarSenhaPorUsernameComSucesso() {
        br.com.senac.msusuario.dto.AlterarSenhaPorUsernameRequest request = new br.com.senac.msusuario.dto.AlterarSenhaPorUsernameRequest("joao@email.com", "novaSenha123");

        usuarioController.alterarSenhaPorUsername(request);

        org.mockito.Mockito.verify(service).alterarSenhaPorUsername(request);
    }

    private br.com.senac.msusuario.dto.CreateUsuarioRequest criarCreateUsuarioRequest() {
        return new br.com.senac.msusuario.dto.CreateUsuarioRequest(
                "João",
                "Silva",
                "joao@email.com",
                "11999999999",
                "CANDIDATO",
                "senha123"
        );
    }

    private br.com.senac.msusuario.dto.UpdateUsuarioRequest criarUpdateUsuarioRequest() {
        return new br.com.senac.msusuario.dto.UpdateUsuarioRequest(
                "João",
                "Silva",
                "11999999999",
                "CANDIDATO"
        );
    }

    private br.com.senac.msusuario.dto.UsuarioResponse criarUsuarioResponse() {
        return new br.com.senac.msusuario.dto.UsuarioResponse(
                USUARIO_ID,
                KEYCLOAK_USER_ID,
                "João",
                "Silva",
                "joao@email.com",
                "11999999999",
                "CANDIDATO",
                java.time.LocalDateTime.of(2026, 1, 1, 10, 0),
                java.time.LocalDateTime.of(2026, 1, 2, 10, 0)
        );
    }
}
