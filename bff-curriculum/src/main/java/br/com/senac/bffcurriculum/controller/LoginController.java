package br.com.senac.bffcurriculum.controller;

import br.com.senac.bffcurriculum.controller.request.LoginRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping
	public String login(Model model) {
		LoginRequest login = new LoginRequest();

		model.addAttribute("login", login);
		return "login";
	}
}
