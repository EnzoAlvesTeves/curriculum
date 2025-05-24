package br.com.senac.curriculum.controller;


import br.com.senac.curriculum.dto.*;
import br.com.senac.curriculum.service.CandidatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/candidato")
public class CandidatoController {

	@Autowired
	private CandidatoService service;

	@GetMapping("/{id}")
	public String buscar(@PathVariable Long id, Model model) {
		CandidatoDTO candidatoDTO = service.buscarPorId(id);

		model.addAttribute("candidato", candidatoDTO);
		return "candidato/curriculo";
	}

	@GetMapping("/cadastrar")
	public String novoCandidato(Model model) {
		CandidatoDTO candidatoDTO = new CandidatoDTO();
		candidatoDTO.setEndereco(new EnderecoDTO());
		candidatoDTO.getEducacoes().add(new EducacaoDTO());
		candidatoDTO.getExperiencias().add(new ExperienciaDTO());
		candidatoDTO.getHabilidades().add(new HabilidadeDTO());

		model.addAttribute("candidato", candidatoDTO);
		return "candidato/cadastro";
	}

	@GetMapping("/lista")
	public String listaCandidatos(Model model) {
		List<CandidatoDTO> candidatos = service.listarTodos();

		model.addAttribute("candidatos", candidatos);
		return "candidato/lista";
	}

	@PostMapping("/cadastrar")
	public String cadastrar(@ModelAttribute CandidatoDTO candidatoDTO, Model model){
		CandidatoDTO candidato = service.cadastrar(candidatoDTO);
//		return "redirect:/candidato/" + candidato.getId();

		model.addAttribute("candidato", candidato);
		return "candidato/curriculo";
	}
}


