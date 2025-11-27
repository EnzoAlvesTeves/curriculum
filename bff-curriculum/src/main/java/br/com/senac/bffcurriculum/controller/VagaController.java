package br.com.senac.bffcurriculum.controller;

import br.com.senac.bffcurriculum.client.UsuarioClient;
import br.com.senac.bffcurriculum.client.VagaClient;
import br.com.senac.bffcurriculum.dto.UsuarioDTO;
import br.com.senac.bffcurriculum.dto.VagaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/vaga")
public class VagaController {

	@Autowired
	private VagaClient vagaClient;

	@Autowired
	private UsuarioClient usuarioClient;

	//salvar vaga
	@PostMapping("cadastrar")
	public String cadastrar(@ModelAttribute VagaDTO vagaDTO) {
		vagaClient.create(vagaDTO);

		return "redirect:/vaga/lista/usuario/1";
	}

	@GetMapping("/cadastrar/usuario/{id}")
	public String cadastrarVaga(@PathVariable Long id, Model model) {
		UsuarioDTO usuarioDTO = usuarioClient.getById(id);

		model.addAttribute("usuario", usuarioDTO);
		return "vaga/cadastro";
	}

	@GetMapping("/lista/usuario/{id}")
	public String listaVaga(@PathVariable Long id, Model model) {
		UsuarioDTO usuarioDTO = usuarioClient.getById(id);
		List<VagaDTO> vagas = vagaClient.getall();

		model.addAttribute("usuario", usuarioDTO);
		model.addAttribute("candidato", null);
		model.addAttribute("vagas", vagas);
		return "vaga/lista";
	}

}
