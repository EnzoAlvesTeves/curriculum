package br.com.senac.bffcurriculum.controller;


import br.com.senac.bffcurriculum.client.UsuarioClient;
import br.com.senac.bffcurriculum.controller.request.EsqueciSenhaRequest;
import br.com.senac.bffcurriculum.controller.request.LoginRequest;
import br.com.senac.bffcurriculum.dto.LoginRequestDTO;
import br.com.senac.bffcurriculum.dto.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioClient usuarioClient;

	@GetMapping("/cadastrar")
	public String novoUsuario(Model model) {
		UsuarioDTO usuarioDTO = new UsuarioDTO();

		model.addAttribute("usuario", usuarioDTO);
		return "usuario/cadastro";
	}

	@PostMapping("/cadastrar")
	public String cadastrar(@ModelAttribute UsuarioDTO usuarioDTO, Model model){
		usuarioDTO = usuarioClient.create(usuarioDTO);

		model.addAttribute("usuario", usuarioDTO);
		return "home";
	}

	@GetMapping("/esqueci-senha")
	public String esqueciSenha(Model model) {
		EsqueciSenhaRequest esqueciSenha = new EsqueciSenhaRequest();

		model.addAttribute("esqueciSenha", esqueciSenha);
		return "usuario/esqueci-senha";
	}

	@PostMapping("/esqueci-senha")
	public String recuperarSenha(@ModelAttribute EsqueciSenhaRequest esqueciSenha, Model model) {
		try {
//			String senha = usuarioClient.esqueciSenha(esqueciSenha.getEmail());

			model.addAttribute("senharecuperada", null);
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
			UsuarioDTO usuarioDTO = usuarioClient.getById(id);

			model.addAttribute("usuario", usuarioDTO);
			model.addAttribute("candidato", null);
			return "home";
		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("login", new LoginRequest());
			return "login";
		}
	}

	@PostMapping("/login")
	public String login(@ModelAttribute LoginRequest login, Model model) {
		try {
			UsuarioDTO usuarioDTO = usuarioClient.login(login.toDTO());

			model.addAttribute("usuario", usuarioDTO);
			model.addAttribute("candidato", null);
			return "home";
		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("login", login);
			return "login";
		}
	}

}
