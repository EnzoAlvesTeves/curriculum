package br.com.senac.curriculum.controller;


import br.com.senac.curriculum.controller.request.EsqueciSenhaRequest;
import br.com.senac.curriculum.controller.request.LoginRequest;
import br.com.senac.curriculum.dto.*;
import br.com.senac.curriculum.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService service;

	@GetMapping("/cadastrar")
	public String novoUsuario(Model model) {
		UsuarioDTO usuarioDTO = new UsuarioDTO();

		model.addAttribute("usuario", usuarioDTO);
		return "usuario/cadastro";
	}

	@PostMapping("/cadastrar")
	public String cadastrar(@ModelAttribute UsuarioDTO usuarioDTO, Model model){
		usuarioDTO = service.cadastrar(usuarioDTO);

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
			String senha = service.esqueciSenha(esqueciSenha.getEmail());

			model.addAttribute("senharecuperada", senha);
			return "usuario/esqueci-senha";
		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("esqueciSenha", esqueciSenha);
			return "usuario/esqueci-senha";
		}
	}

	@PostMapping("/login")
	public String login(@ModelAttribute LoginRequest login, Model model) {
		try {
			UsuarioDTO usuarioDTO = service.login(login.getEmail(), login.getSenha());

			model.addAttribute("usuario", usuarioDTO);
			model.addAttribute("candidato", usuarioDTO.getCandidato());
			return "home";
		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("login", login);
			return "login";
		}
	}

}


