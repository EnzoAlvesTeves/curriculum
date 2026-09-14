package br.com.senac.bffcurriculum.controller;

import br.com.senac.bffcurriculum.controller.request.CadastroUsuario;
import br.com.senac.bffcurriculum.controller.request.EsqueciSenha;
import br.com.senac.bffcurriculum.controller.request.Login;
import br.com.senac.bffcurriculum.dto.CandidatoDTO;
import br.com.senac.bffcurriculum.dto.UsuarioDTO;
import br.com.senac.bffcurriculum.service.CandidatoService;
import br.com.senac.bffcurriculum.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    private static final String ERRO_CONFIRMAR_SENHA = "Confirmação de senha inválida!";
    private static final String ERRO_LOGIN_INVALIDO = "Usuário ou Senha inválidos!";
    private static final String ERRO_SENHA_RECUPERAR = "Não foi encontrado usuário para o email informado!";

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private CandidatoService candidatoService;

    @Mock
    private Model model;

    @InjectMocks
    private UsuarioController usuarioController;

    @Test
    @DisplayName("Deve abrir formulario de cadastro com objeto vazio")
    void deveAbrirFormularioDeCadastroComObjetoVazio() {
        String viewName = usuarioController.novoUsuario(model);

        assertEquals("usuario/cadastro", viewName);
        verify(model).addAttribute("cadastroUsuario", new CadastroUsuario());
    }

    @Test
    @DisplayName("Deve cadastrar usuario com sucesso e ir para home")
    void deveCadastrarUsuarioComSucessoEIrParaHome() {
        CadastroUsuario cadastroUsuario = novoCadastroUsuario();
        UsuarioDTO usuarioDTO = novoUsuarioDTO(1L);

        when(usuarioService.cadastroUsuario(cadastroUsuario)).thenReturn(usuarioDTO);

        String viewName = usuarioController.cadastrar(cadastroUsuario, model);

        assertEquals("home", viewName);
        verify(usuarioService).cadastroUsuario(cadastroUsuario);
        verify(model).addAttribute("usuario", usuarioDTO);
    }

    @Test
    @DisplayName("Deve retornar cadastro com erro quando senha for invalida")
    void deveRetornarCadastroComErroQuandoSenhaForInvalida() {
        CadastroUsuario cadastroUsuario = novoCadastroUsuario();
        when(usuarioService.cadastroUsuario(cadastroUsuario)).thenThrow(new RuntimeException(ERRO_CONFIRMAR_SENHA));

        String viewName = usuarioController.cadastrar(cadastroUsuario, model);

        assertEquals("usuario/cadastro", viewName);
        verify(model).addAttribute("errorConfirmarSenha", ERRO_CONFIRMAR_SENHA);
        verify(model).addAttribute("cadastroUsuario", cadastroUsuario);
    }

    @Test
    @DisplayName("Deve abrir formulario de esqueci senha com objeto vazio")
    void deveAbrirFormularioDeEsqueciSenhaComObjetoVazio() {
        String viewName = usuarioController.esqueciSenha(model);

        assertEquals("usuario/esqueci-senha", viewName);
        verify(model).addAttribute("esqueciSenha", new EsqueciSenha());
    }

    @Test
    @DisplayName("Deve recuperar senha com sucesso")
    void deveRecuperarSenhaComSucesso() {
        EsqueciSenha esqueciSenha = new EsqueciSenha("maria@email.com");
        when(usuarioService.esqueciMinhaSenha(esqueciSenha)).thenReturn("123456");

        String viewName = usuarioController.recuperarSenha(esqueciSenha, model);

        assertEquals("usuario/esqueci-senha", viewName);
        verify(usuarioService).esqueciMinhaSenha(esqueciSenha);
        verify(model).addAttribute("senharecuperada", "123456");
    }

    @Test
    @DisplayName("Deve retornar formulario de esqueci senha com erro ao recuperar senha")
    void deveRetornarFormularioDeEsqueciSenhaComErroAoRecuperarSenha() {
        EsqueciSenha esqueciSenha = new EsqueciSenha("inexistente@email.com");
        when(usuarioService.esqueciMinhaSenha(esqueciSenha)).thenThrow(new RuntimeException(ERRO_SENHA_RECUPERAR));

        String viewName = usuarioController.recuperarSenha(esqueciSenha, model);

        assertEquals("usuario/esqueci-senha", viewName);
        verify(model).addAttribute("error", ERRO_SENHA_RECUPERAR);
        verify(model).addAttribute("esqueciSenha", esqueciSenha);
    }

    @Test
    @DisplayName("Deve abrir home por id com usuario e candidato")
    void deveAbrirHomePorIdComUsuarioECandidato() {
        UsuarioDTO usuarioDTO = novoUsuarioDTO(1L);
        CandidatoDTO candidatoDTO = novoCandidatoDTO(10L, 1L);

        when(usuarioService.getById(1L)).thenReturn(usuarioDTO);
        when(candidatoService.getByUsuarioId(1L)).thenReturn(candidatoDTO);

        String viewName = usuarioController.login(1L, model);

        assertEquals("home", viewName);
        verify(usuarioService).getById(1L);
        verify(candidatoService).getByUsuarioId(1L);
        verify(model).addAttribute("usuario", usuarioDTO);
        verify(model).addAttribute("candidato", candidatoDTO);
    }

    @Test
    @DisplayName("Deve retornar login com erro ao abrir home por id")
    void deveRetornarLoginComErroAoAbrirHomePorId() {
        when(usuarioService.getById(1L)).thenThrow(new RuntimeException("erro"));

        String viewName = usuarioController.login(1L, model);

        assertEquals("login", viewName);
        verify(model).addAttribute("error", "erro");
        verify(model).addAttribute("login", new Login());
    }

    @Test
    @DisplayName("Deve logar com sucesso e ir para home")
    void deveLogarComSucessoEIrParaHome() {
        Login login = new Login("maria@email.com", "123456");
        UsuarioDTO usuarioDTO = novoUsuarioDTO(1L);
        CandidatoDTO candidatoDTO = novoCandidatoDTO(10L, 1L);

        when(usuarioService.login(login)).thenReturn(usuarioDTO);
        when(candidatoService.getByUsuarioId(1L)).thenReturn(candidatoDTO);

        String viewName = usuarioController.login(login, model);

        assertEquals("home", viewName);
        verify(usuarioService).login(login);
        verify(candidatoService).getByUsuarioId(1L);
        verify(model).addAttribute("usuario", usuarioDTO);
        verify(model).addAttribute("candidato", candidatoDTO);
    }

    @Test
    @DisplayName("Deve retornar login com erro quando autenticar falhar")
    void deveRetornarLoginComErroQuandoAutenticarFalhar() {
        Login login = new Login("maria@email.com", "123456");
        when(usuarioService.login(login)).thenThrow(new RuntimeException(ERRO_LOGIN_INVALIDO));

        String viewName = usuarioController.login(login, model);

        assertEquals("login", viewName);
        verify(model).addAttribute("error", ERRO_LOGIN_INVALIDO);
        verify(model).addAttribute("login", login);
    }

    private CadastroUsuario novoCadastroUsuario() {
        return new CadastroUsuario("Maria", "maria@email.com", "123456", "123456");
    }

    private UsuarioDTO novoUsuarioDTO(Long id) {
        return new UsuarioDTO(id, "Maria", "maria@email.com", "123456");
    }

    private CandidatoDTO novoCandidatoDTO(Long id, Long usuarioId) {
        CandidatoDTO candidatoDTO = new CandidatoDTO();
        candidatoDTO.setId(id);
        candidatoDTO.setUsuarioId(usuarioId);
        candidatoDTO.setNome("Maria");
        candidatoDTO.setEmail("maria@email.com");
        candidatoDTO.setUsuario(novoUsuarioDTO(usuarioId));
        return candidatoDTO;
    }
}
