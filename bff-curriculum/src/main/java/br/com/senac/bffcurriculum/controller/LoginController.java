package br.com.senac.bffcurriculum.controller;

import br.com.senac.bffcurriculum.controller.request.Login;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	@GetMapping
	public String login(Model model) {
		Login login = new Login();

		model.addAttribute("login", login);
		return "login";
	}
}
