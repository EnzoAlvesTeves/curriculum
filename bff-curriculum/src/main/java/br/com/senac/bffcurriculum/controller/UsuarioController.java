package br.com.senac.bffcurriculum.controller;


import br.com.senac.bffcurriculum.controller.request.CadastroUsuario;
import br.com.senac.bffcurriculum.controller.request.EsqueciSenha;
import br.com.senac.bffcurriculum.controller.request.Login;
import br.com.senac.bffcurriculum.dto.CandidatoDTO;
import br.com.senac.bffcurriculum.dto.UsuarioDTO;
import br.com.senac.bffcurriculum.service.CandidatoService;
import br.com.senac.bffcurriculum.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final CandidatoService candidatoService;

    public UsuarioController(
            UsuarioService usuarioService,
            CandidatoService candidatoService
    ) {
        this.usuarioService = usuarioService;
        this.candidatoService = candidatoService;
    }

	@GetMapping("/cadastrar")
	public String novoUsuario(Model model) {
		model.addAttribute("cadastroUsuario", new CadastroUsuario());
		return "usuario/cadastro";
	}

	@PostMapping("/cadastrar")
	public String cadastrar(@ModelAttribute CadastroUsuario cadastroUsuario, Model model){
        try {
            UsuarioDTO usuario = usuarioService.cadastroUsuario(cadastroUsuario);
            model.addAttribute("usuario", usuario);
            return "home";
        } catch (RuntimeException ex) {
            model.addAttribute("errorConfirmarSenha", ex.getMessage());
            model.addAttribute("cadastroUsuario", cadastroUsuario);
            return "usuario/cadastro";
        }
	}

	@GetMapping("/esqueci-senha")
	public String esqueciSenha(Model model) {
		EsqueciSenha esqueciSenha = new EsqueciSenha();
		model.addAttribute("esqueciSenha", esqueciSenha);
		return "usuario/esqueci-senha";
	}

	@PostMapping("/esqueci-senha")
	public String recuperarSenha(@ModelAttribute EsqueciSenha esqueciSenha, Model model) {
		try {
            String senha = usuarioService.esqueciMinhaSenha(esqueciSenha);

			model.addAttribute("senharecuperada", senha);
			return "usuario/esqueci-senha";
		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("esqueciSenha", esqueciSenha);
			return "usuario/esqueci-senha";
		}
	}

	@GetMapping("/home/{id}")
	public String login(@PathVariable Long id, Model model) {
		try {
			UsuarioDTO usuarioDTO = usuarioService.getById(id);
            CandidatoDTO candidatoDTO = candidatoService.getByUsuarioId(id);

			model.addAttribute("usuario", usuarioDTO);
			model.addAttribute("candidato", candidatoDTO);
			return "home";
		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("login", new Login());
			return "login";
		}
	}

	@PostMapping("/login")
	public String login(@ModelAttribute Login login, Model model) {
		try {
			UsuarioDTO usuarioDTO = usuarioService.login(login);
            CandidatoDTO candidatoDTO = candidatoService.getByUsuarioId(usuarioDTO.getId());

			model.addAttribute("usuario", usuarioDTO);
			model.addAttribute("candidato", candidatoDTO);
			return "home";
		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("login", login);
			return "login";
		}
	}

}
