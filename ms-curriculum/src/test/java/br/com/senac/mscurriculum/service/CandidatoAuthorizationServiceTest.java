package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.TipoUsuario;
import br.com.senac.mscurriculum.dto.UsuarioMeResponse;
import br.com.senac.mscurriculum.repository.CandidatoRepository;
import br.com.senac.mscurriculum.repository.entity.CandidatoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes da Classe CandidatoAuthorizationService")
class CandidatoAuthorizationServiceTest {

    @Mock
    private UsuarioContextService usuarioContextService;

    @Mock
    private CandidatoRepository candidatoRepository;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private CandidatoAuthorizationService candidatoAuthorizationService;

    private static final Long USUARIO_ID = 10L;
    private static final Long CANDIDATO_ID = 20L;
    private static final String TOKEN = "token-123";

    private UsuarioMeResponse usuarioCandidato;
    private UsuarioMeResponse usuarioRh;

    @BeforeEach
    void setUp() {
        usuarioCandidato = new UsuarioMeResponse(USUARIO_ID, TipoUsuario.CANDIDATO, "João", "Silva", "joao@email.com");
        usuarioRh = new UsuarioMeResponse(USUARIO_ID, TipoUsuario.RH, "RH", "User", "rh@email.com");
    }

    @Test
    @DisplayName("Deve buscar usuário atual com sucesso")
    void testUsuarioAtualComSucesso() {
        when(jwt.getTokenValue()).thenReturn(TOKEN);
        when(usuarioContextService.buscarUsuarioMe("Bearer " + TOKEN)).thenReturn(usuarioCandidato);

        UsuarioMeResponse resultado = candidatoAuthorizationService.usuarioAtual(jwt);

        assertNotNull(resultado);
        assertEquals(usuarioCandidato, resultado);
        verify(usuarioContextService, times(1)).buscarUsuarioMe("Bearer " + TOKEN);
    }

    @Test
    @DisplayName("Deve lançar unauthorized quando jwt for nulo")
    void testUsuarioAtualJwtNulo() {
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> candidatoAuthorizationService.usuarioAtual(null)
        );

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Usuário não autenticado", exception.getReason());
        verifyNoInteractions(usuarioContextService);
    }

    @Test
    @DisplayName("Deve validar tipo candidato com sucesso")
    void testValidarTipoCandidatoComSucesso() {
        when(jwt.getTokenValue()).thenReturn(TOKEN);
        when(usuarioContextService.buscarUsuarioMe("Bearer " + TOKEN)).thenReturn(usuarioCandidato);

        UsuarioMeResponse resultado = candidatoAuthorizationService.validarTipoCandidato(jwt);

        assertNotNull(resultado);
        assertEquals(TipoUsuario.CANDIDATO, resultado.tipo());
        verify(usuarioContextService, times(1)).buscarUsuarioMe("Bearer " + TOKEN);
    }

    @Test
    @DisplayName("Deve lançar forbidden quando usuário não for candidato")
    void testValidarTipoCandidatoNaoCandidato() {
        when(jwt.getTokenValue()).thenReturn(TOKEN);
        when(usuarioContextService.buscarUsuarioMe("Bearer " + TOKEN)).thenReturn(usuarioRh);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> candidatoAuthorizationService.validarTipoCandidato(jwt)
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Somente usuário do tipo CANDIDATO pode executar esta operação", exception.getReason());
        verify(usuarioContextService, times(1)).buscarUsuarioMe("Bearer " + TOKEN);
    }

    @Test
    @DisplayName("Deve lançar forbidden quando tipo do usuário for nulo")
    void testValidarTipoCandidatoTipoNulo() {
        UsuarioMeResponse usuarioSemTipo = new UsuarioMeResponse(USUARIO_ID, null, "João", "Silva", "joao@email.com");
        when(jwt.getTokenValue()).thenReturn(TOKEN);
        when(usuarioContextService.buscarUsuarioMe("Bearer " + TOKEN)).thenReturn(usuarioSemTipo);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> candidatoAuthorizationService.validarTipoCandidato(jwt)
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        verify(usuarioContextService, times(1)).buscarUsuarioMe("Bearer " + TOKEN);
    }

    @Test
    @DisplayName("Deve validar acesso do usuário com sucesso")
    void testValidarAcessoDoUsuarioComSucesso() {
        CandidatoEntity candidato = criarCandidatoEntity(USUARIO_ID);
        when(jwt.getTokenValue()).thenReturn(TOKEN);
        when(usuarioContextService.buscarUsuarioMe("Bearer " + TOKEN)).thenReturn(usuarioCandidato);
        when(candidatoRepository.findById(CANDIDATO_ID)).thenReturn(Optional.of(candidato));

        CandidatoEntity resultado = candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt);

        assertNotNull(resultado);
        assertEquals(candidato, resultado);
        verify(usuarioContextService, times(1)).buscarUsuarioMe("Bearer " + TOKEN);
        verify(candidatoRepository, times(1)).findById(CANDIDATO_ID);
    }

    @Test
    @DisplayName("Deve lançar not found quando candidato não existir")
    void testValidarAcessoDoUsuarioCandidatoNaoEncontrado() {
        when(jwt.getTokenValue()).thenReturn(TOKEN);
        when(usuarioContextService.buscarUsuarioMe("Bearer " + TOKEN)).thenReturn(usuarioCandidato);
        when(candidatoRepository.findById(CANDIDATO_ID)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Candidato não encontrado", exception.getReason());
        verify(usuarioContextService, times(1)).buscarUsuarioMe("Bearer " + TOKEN);
        verify(candidatoRepository, times(1)).findById(CANDIDATO_ID);
    }

    @Test
    @DisplayName("Deve lançar forbidden quando candidato pertencer a outro usuário")
    void testValidarAcessoDoUsuarioOutroUsuario() {
        CandidatoEntity candidato = criarCandidatoEntity(999L);
        when(jwt.getTokenValue()).thenReturn(TOKEN);
        when(usuarioContextService.buscarUsuarioMe("Bearer " + TOKEN)).thenReturn(usuarioCandidato);
        when(candidatoRepository.findById(CANDIDATO_ID)).thenReturn(Optional.of(candidato));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> candidatoAuthorizationService.validarAcessoDoUsuario(CANDIDATO_ID, jwt)
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("Você não pode editar este candidato", exception.getReason());
        verify(usuarioContextService, times(1)).buscarUsuarioMe("Bearer " + TOKEN);
        verify(candidatoRepository, times(1)).findById(CANDIDATO_ID);
    }

    private CandidatoEntity criarCandidatoEntity(Long idUsuario) {
        return new CandidatoEntity(
                CANDIDATO_ID,
                "João da Silva",
                "joao@email.com",
                "MASCULINO",
                "11999999999",
                LocalDate.of(1990, 1, 15),
                "Desenvolvedor Java",
                idUsuario,
                null,
                null
        );
    }
}