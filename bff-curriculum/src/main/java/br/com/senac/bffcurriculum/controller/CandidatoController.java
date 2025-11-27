package br.com.senac.bffcurriculum.controller;


import br.com.senac.bffcurriculum.client.CandidatoClient;
import br.com.senac.bffcurriculum.client.UsuarioClient;
import br.com.senac.bffcurriculum.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/candidato")
public class CandidatoController {

	@Autowired
	private CandidatoClient candidatoClient;

	@Autowired
	private UsuarioClient usuarioClient;

	@GetMapping("/{id}")
	public String buscar(@PathVariable Long id, Model model) {
		CandidatoDTO candidatoDTO = candidatoClient.getById(id);

		model.addAttribute("candidato", candidatoDTO);
		return "candidato/curriculo";
	}

	@GetMapping("/cadastrar")
	public String novoCandidato(Long usuarioId, Model model) {
        UsuarioDTO usuarioDTO = usuarioClient.getById(usuarioId);
        CandidatoDTO candidatoDTO = new CandidatoDTO(usuarioDTO);
        candidatoDTO.setEndereco(new EnderecoDTO());
        candidatoDTO.getEducacao().add(new EducacaoDTO());
        candidatoDTO.getExperiencia().add(new ExperienciaDTO());
        candidatoDTO.getHabilidade().add(new HabilidadeDTO());

		model.addAttribute("candidato", candidatoDTO);
		return "candidato/cadastro";
	}

	@GetMapping("/lista")
	public String listaCandidatos(Model model) {
		List<CandidatoDTO> candidatos = candidatoClient.getall();

		model.addAttribute("candidatos", candidatos);
		return "candidato/lista";
	}

	@PostMapping("/cadastrar")
	public String cadastrar(@ModelAttribute CandidatoDTO candidatoDTO, Model model){
		CandidatoDTO candidato = candidatoClient.create(candidatoDTO);

		return "redirect:/candidato/" + candidato.getId();
	}

	@PostMapping("/editar")
	public String editar(@ModelAttribute CandidatoDTO candidatoDTO, Model model){
		CandidatoDTO candidato = candidatoClient.update(candidatoDTO);

		return "redirect:/candidato/" + candidato.getId();
	}
}


