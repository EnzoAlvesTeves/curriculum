package br.com.senac.bffcurriculum.service;

import br.com.senac.bffcurriculum.client.UsuarioClient;
import br.com.senac.bffcurriculum.controller.request.CadastroUsuario;
import br.com.senac.bffcurriculum.controller.request.EsqueciSenha;
import br.com.senac.bffcurriculum.controller.request.Login;
import br.com.senac.bffcurriculum.dto.LoginRequestDTO;
import br.com.senac.bffcurriculum.dto.UsuarioDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    @DisplayName("Deve cadastrar usuario com sucesso quando senha e confirmacao forem iguais")
    void deveCadastrarUsuarioComSucessoQuandoSenhaEConfirmacaoForemIguais() {
        CadastroUsuario cadastroUsuario = novoCadastroUsuario();
        UsuarioDTO expectedResponse = novoUsuarioDTO(1L, "Maria", "maria@email.com", "123456");

        when(usuarioClient.create(any(UsuarioDTO.class))).thenReturn(expectedResponse);

        UsuarioDTO actualResponse = usuarioService.cadastroUsuario(cadastroUsuario);

        assertSame(expectedResponse, actualResponse);

        ArgumentCaptor<UsuarioDTO> captor = ArgumentCaptor.forClass(UsuarioDTO.class);
        verify(usuarioClient).create(captor.capture());
        assertEquals(cadastroUsuario.getNome(), captor.getValue().getNome());
        assertEquals(cadastroUsuario.getEmail(), captor.getValue().getEmail());
        assertEquals(cadastroUsuario.getSenha(), captor.getValue().getSenha());
    }

    @Test
    @DisplayName("Deve lancar erro quando confirmacao de senha for invalida")
    void deveLancarErroQuandoConfirmacaoDeSenhaForInvalida() {
        CadastroUsuario cadastroUsuario = new CadastroUsuario("Maria", "maria@email.com", "123456", "654321");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> usuarioService.cadastroUsuario(cadastroUsuario));

        assertEquals("Confirmação de senha inválida!", exception.getMessage());
        verifyNoInteractions(usuarioClient);
    }

    @Test
    @DisplayName("Deve recuperar senha pelo email com sucesso")
    void deveRecuperarSenhaPeloEmailComSucesso() {
        EsqueciSenha esqueciSenha = new EsqueciSenha("maria@email.com");
        UsuarioDTO usuarioDTO = novoUsuarioDTO(1L, "Maria", "maria@email.com", "123456");

        when(usuarioClient.getByEmail(esqueciSenha.getEmail())).thenReturn(usuarioDTO);

        String actualResponse = usuarioService.esqueciMinhaSenha(esqueciSenha);

        assertEquals("123456", actualResponse);
        verify(usuarioClient).getByEmail(esqueciSenha.getEmail());
    }

    @Test
    @DisplayName("Deve lancar erro quando usuario nao for encontrado ao recuperar senha")
    void deveLancarErroQuandoUsuarioNaoForEncontradoAoRecuperarSenha() {
        EsqueciSenha esqueciSenha = new EsqueciSenha("inexistente@email.com");
        when(usuarioClient.getByEmail(esqueciSenha.getEmail())).thenThrow(new RuntimeException("erro"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> usuarioService.esqueciMinhaSenha(esqueciSenha));

        assertEquals("Não foi encontrado usuário para o email informado!", exception.getMessage());
        verify(usuarioClient).getByEmail(esqueciSenha.getEmail());
    }

    @Test
    @DisplayName("Deve buscar usuario por id com sucesso")
    void deveBuscarUsuarioPorIdComSucesso() {
        UsuarioDTO expectedResponse = novoUsuarioDTO(10L, "João", "joao@email.com", "abc123");
        when(usuarioClient.getById(10L)).thenReturn(expectedResponse);

        UsuarioDTO actualResponse = usuarioService.getById(10L);

        assertSame(expectedResponse, actualResponse);
        verify(usuarioClient).getById(10L);
    }

    @Test
    @DisplayName("Deve autenticar usuario com sucesso")
    void deveAutenticarUsuarioComSucesso() {
        Login login = new Login("joao@email.com", "abc123");
        UsuarioDTO expectedResponse = novoUsuarioDTO(10L, "João", "joao@email.com", "abc123");

        when(usuarioClient.login(any(LoginRequestDTO.class))).thenReturn(expectedResponse);

        UsuarioDTO actualResponse = usuarioService.login(login);

        assertSame(expectedResponse, actualResponse);

        ArgumentCaptor<LoginRequestDTO> captor = ArgumentCaptor.forClass(LoginRequestDTO.class);
        verify(usuarioClient).login(captor.capture());
        assertEquals(login.getEmail(), captor.getValue().getEmail());
        assertEquals(login.getSenha(), captor.getValue().getSenha());
    }

    @Test
    @DisplayName("Deve lancar erro quando login for invalido")
    void deveLancarErroQuandoLoginForInvalido() {
        Login login = new Login("joao@email.com", "abc123");
        when(usuarioClient.login(any(LoginRequestDTO.class))).thenThrow(new RuntimeException("erro"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> usuarioService.login(login));

        assertEquals("Usuário ou Senha inválidos!", exception.getMessage());
        verify(usuarioClient).login(any(LoginRequestDTO.class));
    }

    private CadastroUsuario novoCadastroUsuario() {
        return new CadastroUsuario("Maria", "maria@email.com", "123456", "123456");
    }

    private UsuarioDTO novoUsuarioDTO(Long id, String nome, String email, String senha) {
        return new UsuarioDTO(id, nome, email, senha);
    }
}
