package br.com.senac.curriculum.controller;

import br.com.senac.curriculum.dto.UsuarioDTO;
import br.com.senac.curriculum.dto.VagaDTO;
import br.com.senac.curriculum.service.UsuarioService;
import br.com.senac.curriculum.service.VagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/vaga")
public class VagaController {

	@Autowired
	private VagaService service;

	@Autowired
	private UsuarioService usuarioService;

	//salvar vaga
	@PostMapping("cadastrar")
	public String cadastrar(@ModelAttribute VagaDTO vagaDTO) {
		service.cadastrar(vagaDTO);

		return "redirect:/vaga/lista/usuario/1";
	}

	@GetMapping("/cadastrar/usuario/{id}")
	public String cadastrarVaga(@PathVariable Long id, Model model) {
		UsuarioDTO usuarioDTO = usuarioService.buscarPorId(id);

		model.addAttribute("usuario", usuarioDTO);
		return "vaga/cadastro";
	}

	@GetMapping("/lista/usuario/{id}")
	public String listaVaga(@PathVariable Long id, Model model) {
		UsuarioDTO usuarioDTO = usuarioService.buscarPorId(id);
		List<VagaDTO> vagas = service.listarVagas();

		model.addAttribute("usuario", usuarioDTO);
		model.addAttribute("candidato", usuarioDTO.getCandidato());
		model.addAttribute("vagas", vagas);
		return "vaga/lista";
	}

}
