package br.com.senac.bffcurriculum.controller;


import br.com.senac.bffcurriculum.dto.*;
import br.com.senac.bffcurriculum.service.CandidatoService;
import br.com.senac.bffcurriculum.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/candidato")
public class CandidatoController {

    private final UsuarioService usuarioService;
    private final CandidatoService candidatoService;

    CandidatoController(
            UsuarioService usuarioService,
            CandidatoService candidatoService
    ) {
        this.usuarioService = usuarioService;
        this.candidatoService = candidatoService;
    }

	@GetMapping("/{id}")
	public String buscar(@PathVariable Long id, Model model) {
		CandidatoDTO candidatoDTO = candidatoService.getById(id);
        UsuarioDTO usuarioDTO = usuarioService.getById(candidatoDTO.getUsuarioId());

        candidatoDTO.setUsuario(usuarioDTO);

		model.addAttribute("candidato", candidatoDTO);
		return "candidato/curriculo";
	}

	@GetMapping("/cadastrar")
	public String novoCandidato(Long usuarioId, Model model) {
        UsuarioDTO usuarioDTO = usuarioService.getById(usuarioId);

        CandidatoDTO candidatoDTO = new CandidatoDTO(usuarioDTO);
        candidatoDTO.setEndereco(new EnderecoDTO());
        candidatoDTO.getEducacoes().add(new EducacaoDTO());
        candidatoDTO.getExperiencias().add(new ExperienciaDTO());
        candidatoDTO.getHabilidades().add(new HabilidadeDTO());

		model.addAttribute("candidato", candidatoDTO);
		return "candidato/cadastro";
	}

	@GetMapping("/lista")
	public String listaCandidatos(Model model) {
		List<CandidatoDTO> candidatos = candidatoService.getall();

		model.addAttribute("candidatos", candidatos);
		return "candidato/lista";
	}

	@PostMapping("/cadastrar")
	public String cadastrar(@ModelAttribute CandidatoDTO candidatoDTO, Model model){
		CandidatoDTO candidato = candidatoService.create(candidatoDTO);
		return "redirect:/candidato/" + candidato.getId();
	}

	@PostMapping("/editar")
	public String editar(@ModelAttribute CandidatoDTO candidatoDTO, Model model){
		CandidatoDTO candidato = candidatoService.update(candidatoDTO);
		return "redirect:/candidato/" + candidato.getId();
	}
}


