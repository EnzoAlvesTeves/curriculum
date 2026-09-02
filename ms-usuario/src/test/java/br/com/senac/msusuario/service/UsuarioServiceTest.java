package br.com.senac.msusuario.service;

import br.com.senac.msusuario.dto.AlterarSenhaPorUsernameRequest;
import br.com.senac.msusuario.dto.AlterarSenhaRequest;
import br.com.senac.msusuario.dto.CreateUsuarioRequest;
import br.com.senac.msusuario.dto.UpdateUsuarioRequest;
import br.com.senac.msusuario.dto.UsuarioResponse;
import br.com.senac.msusuario.repository.UsuarioRepository;
import br.com.senac.msusuario.repository.entity.UsuarioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes da Classe UsuarioService")
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private UsuarioService service;

    private static final Long USUARIO_ID = 10L;
    private static final String KEYCLOAK_USER_ID = "kc-user-id";
    private static final String EMAIL = "joao@email.com";

    @BeforeEach
    void setUp() {
        lenient().when(jwt.getSubject()).thenReturn(KEYCLOAK_USER_ID);
    }

    @Test
    void testCriarComSucesso() {
        CreateUsuarioRequest request = criarCreateUsuarioRequest();
        UsuarioEntity entitySalvo = criarUsuarioEntity();

        when(repository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        when(keycloakService.criarUsuarioKeycloak("João", "Silva", EMAIL, "senha123")).thenReturn(KEYCLOAK_USER_ID);
        when(repository.save(any(UsuarioEntity.class))).thenReturn(entitySalvo);

        UsuarioResponse resultado = service.criar(request);

        assertNotNull(resultado);
        assertEquals(USUARIO_ID, resultado.id());
        assertEquals(KEYCLOAK_USER_ID, resultado.keycloakUserId());
        assertEquals("João", resultado.nome());
        assertEquals("Silva", resultado.sobrenome());
        assertEquals(EMAIL, resultado.email());
        verify(repository).findByEmail(EMAIL);
        verify(keycloakService).criarUsuarioKeycloak("João", "Silva", EMAIL, "senha123");
        ArgumentCaptor<UsuarioEntity> captor = ArgumentCaptor.forClass(UsuarioEntity.class);
        verify(repository).save(captor.capture());
        assertEquals(KEYCLOAK_USER_ID, captor.getValue().getKeycloakUserId());
        assertEquals("João", captor.getValue().getNome());
        assertEquals("Silva", captor.getValue().getSobrenome());
        assertEquals(EMAIL, captor.getValue().getEmail());
        assertEquals("11999999999", captor.getValue().getTelefone());
        assertEquals("CANDIDATO", captor.getValue().getTipo());
    }

    @Test
    void testCriarComEmailDuplicado() {
        CreateUsuarioRequest request = criarCreateUsuarioRequest();

        when(repository.findByEmail(EMAIL)).thenReturn(Optional.of(criarUsuarioEntity()));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.criar(request)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("E-mail já cadastrado", exception.getReason());
        verify(repository).findByEmail(EMAIL);
        verifyNoInteractions(keycloakService);
        verify(repository, never()).save(any());
    }

    @Test
    void testListarComSucesso() {
        when(repository.findAll()).thenReturn(List.of(criarUsuarioEntity()));

        List<UsuarioResponse> resultado = service.listar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(USUARIO_ID, resultado.get(0).id());
        assertEquals(KEYCLOAK_USER_ID, resultado.get(0).keycloakUserId());
        verify(repository).findAll();
    }

    @Test
    void testBuscarPorIdsComSucessoENormalizacao() {
        List<Long> ids = List.of(3L, null, 1L, 3L, 2L);
        UsuarioEntity usuario3 = criarUsuarioEntity(3L, "kc-3", "A", "A", "a@email.com", "111", "CANDIDATO");
        UsuarioEntity usuario1 = criarUsuarioEntity(1L, "kc-1", "B", "B", "b@email.com", "222", "RH");
        UsuarioEntity usuario2 = criarUsuarioEntity(2L, "kc-2", "C", "C", "c@email.com", "333", "CANDIDATO");

        when(repository.findAllById(List.of(3L, 1L, 2L))).thenReturn(List.of(usuario3, usuario1, usuario2));

        List<UsuarioResponse> resultado = service.buscarPorIds(ids);

        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertEquals(3L, resultado.get(0).id());
        assertEquals(1L, resultado.get(1).id());
        assertEquals(2L, resultado.get(2).id());
        verify(repository).findAllById(List.of(3L, 1L, 2L));
    }

    @Test
    void testBuscarPorIdsVazioOuNulo() {
        List<UsuarioResponse> resultadoNulo = service.buscarPorIds(null);
        List<UsuarioResponse> resultadoVazio = service.buscarPorIds(List.of(null, null));

        assertTrue(resultadoNulo.isEmpty());
        assertTrue(resultadoVazio.isEmpty());
        verifyNoInteractions(repository);
    }

    @Test
    void testBuscarPorIdsNaoEncontrados() {
        List<Long> ids = List.of(1L, 2L);
        UsuarioEntity usuario1 = criarUsuarioEntity(1L, "kc-1", "A", "A", "a@email.com", "111", "CANDIDATO");

        when(repository.findAllById(ids)).thenReturn(List.of(usuario1));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.buscarPorIds(ids)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Usuarios nao encontrados para os ids: [2]", exception.getReason());
    }

    @Test
    void testBuscarPorIdComSucesso() {
        when(repository.findById(USUARIO_ID)).thenReturn(Optional.of(criarUsuarioEntity()));

        UsuarioResponse resultado = service.buscarPorId(USUARIO_ID);

        assertNotNull(resultado);
        assertEquals(USUARIO_ID, resultado.id());
        assertEquals(KEYCLOAK_USER_ID, resultado.keycloakUserId());
        verify(repository).findById(USUARIO_ID);
    }

    @Test
    void testBuscarPorIdNaoEncontrado() {
        when(repository.findById(USUARIO_ID)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.buscarPorId(USUARIO_ID)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Usuário não encontrado", exception.getReason());
    }

    @Test
    void testBuscarMeComSucesso() {
        when(repository.findByKeycloakUserId(KEYCLOAK_USER_ID)).thenReturn(Optional.of(criarUsuarioEntity()));

        UsuarioResponse resultado = service.buscarMe(jwt);

        assertNotNull(resultado);
        assertEquals(USUARIO_ID, resultado.id());
        assertEquals(KEYCLOAK_USER_ID, resultado.keycloakUserId());
        verify(repository).findByKeycloakUserId(KEYCLOAK_USER_ID);
    }

    @Test
    void testBuscarMeNaoEncontrado() {
        when(repository.findByKeycloakUserId(KEYCLOAK_USER_ID)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.buscarMe(jwt)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Usuário não encontrado", exception.getReason());
    }

    @Test
    void testAtualizarComSucesso() {
        UpdateUsuarioRequest request = criarUpdateUsuarioRequest();
        UsuarioEntity entity = criarUsuarioEntity();

        when(repository.findById(USUARIO_ID)).thenReturn(Optional.of(entity));
        when(repository.save(any(UsuarioEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioResponse resultado = service.atualizar(USUARIO_ID, request);

        assertNotNull(resultado);
        assertEquals("João Atualizado", resultado.nome());
        assertEquals("Silva Atualizado", resultado.sobrenome());
        assertEquals("CANDIDATO", resultado.tipo());
        assertNotNull(entity.getUpdatedAt());
        verify(repository).findById(USUARIO_ID);
        verify(repository).save(entity);
    }

    @Test
    void testAtualizarNaoEncontrado() {
        UpdateUsuarioRequest request = criarUpdateUsuarioRequest();
        when(repository.findById(USUARIO_ID)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.atualizar(USUARIO_ID, request)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Usuário não encontrado", exception.getReason());
        verify(repository).findById(USUARIO_ID);
        verify(repository, never()).save(any());
    }

    @Test
    void testAlterarMinhaSenhaComSucesso() {
        AlterarSenhaRequest request = new AlterarSenhaRequest("senhaAtual123", "novaSenha123");
        UsuarioEntity entity = criarUsuarioEntity();

        when(repository.findByKeycloakUserId(KEYCLOAK_USER_ID)).thenReturn(Optional.of(entity));

        service.alterarMinhaSenha(jwt, request);

        verify(repository).findByKeycloakUserId(KEYCLOAK_USER_ID);
        verify(keycloakService).login(EMAIL, "senhaAtual123");
        verify(keycloakService).alterarSenhaKeycloak(KEYCLOAK_USER_ID, "novaSenha123");
    }

    @Test
    void testAlterarMinhaSenhaUsuarioNaoEncontrado() {
        AlterarSenhaRequest request = new AlterarSenhaRequest("senhaAtual123", "novaSenha123");
        when(repository.findByKeycloakUserId(KEYCLOAK_USER_ID)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.alterarMinhaSenha(jwt, request)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Usuário não encontrado", exception.getReason());
        verify(repository).findByKeycloakUserId(KEYCLOAK_USER_ID);
        verifyNoInteractions(keycloakService);
    }

    @Test
    void testAlterarMinhaSenhaComSenhaAtualInvalida() {
        AlterarSenhaRequest request = new AlterarSenhaRequest("senhaErrada", "novaSenha123");
        UsuarioEntity entity = criarUsuarioEntity();

        when(repository.findByKeycloakUserId(KEYCLOAK_USER_ID)).thenReturn(Optional.of(entity));
        when(keycloakService.login(EMAIL, "senhaErrada")).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.alterarMinhaSenha(jwt, request)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Senha atual inválida", exception.getReason());
        verify(keycloakService, never()).alterarSenhaKeycloak(any(), any());
    }

    @Test
    void testAlterarSenhaPorUsernameComSucesso() {
        AlterarSenhaPorUsernameRequest request = new AlterarSenhaPorUsernameRequest(EMAIL, "novaSenha123");
        UsuarioEntity entity = criarUsuarioEntity();

        when(repository.findByEmail(EMAIL)).thenReturn(Optional.of(entity));

        service.alterarSenhaPorUsername(request);

        verify(repository).findByEmail(EMAIL);
        verify(keycloakService).alterarSenhaKeycloak(KEYCLOAK_USER_ID, "novaSenha123");
    }

    @Test
    void testAlterarSenhaPorUsernameUsuarioNaoEncontrado() {
        AlterarSenhaPorUsernameRequest request = new AlterarSenhaPorUsernameRequest(EMAIL, "novaSenha123");
        when(repository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.alterarSenhaPorUsername(request)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Usuário não encontrado", exception.getReason());
        verify(repository).findByEmail(EMAIL);
        verifyNoInteractions(keycloakService);
    }

    @Test
    void testDeletarComSucesso() {
        UsuarioEntity entity = criarUsuarioEntity();

        when(repository.findById(USUARIO_ID)).thenReturn(Optional.of(entity));

        service.deletar(USUARIO_ID);

        verify(repository).findById(USUARIO_ID);
        verify(keycloakService).deletarUsuarioKeycloak(KEYCLOAK_USER_ID);
        verify(repository).delete(entity);
    }

    @Test
    void testDeletarNaoEncontrado() {
        when(repository.findById(USUARIO_ID)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.deletar(USUARIO_ID)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Usuário não encontrado", exception.getReason());
        verify(repository).findById(USUARIO_ID);
        verifyNoInteractions(keycloakService);
    }

    private CreateUsuarioRequest criarCreateUsuarioRequest() {
        return new CreateUsuarioRequest(
                "João",
                "Silva",
                EMAIL,
                "11999999999",
                "CANDIDATO",
                "senha123"
        );
    }

    private UpdateUsuarioRequest criarUpdateUsuarioRequest() {
        return new UpdateUsuarioRequest(
                "João Atualizado",
                "Silva Atualizado",
                "11999999998",
                "CANDIDATO"
        );
    }

    private UsuarioEntity criarUsuarioEntity() {
        return criarUsuarioEntity(USUARIO_ID, KEYCLOAK_USER_ID, "João", "Silva", EMAIL, "11999999999", "CANDIDATO");
    }

    private UsuarioEntity criarUsuarioEntity(Long id, String keycloakUserId, String nome, String sobrenome, String email, String telefone, String tipo) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(id);
        entity.setKeycloakUserId(keycloakUserId);
        entity.setNome(nome);
        entity.setSobrenome(sobrenome);
        entity.setEmail(email);
        entity.setTelefone(telefone);
        entity.setTipo(tipo);
        entity.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));
        entity.setUpdatedAt(LocalDateTime.of(2026, 1, 2, 10, 0));
        return entity;
    }
}
