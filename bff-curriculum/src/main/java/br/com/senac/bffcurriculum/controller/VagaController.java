package br.com.senac.bffcurriculum.controller;

import br.com.senac.bffcurriculum.client.UsuarioClient;
import br.com.senac.bffcurriculum.client.VagaClient;
import br.com.senac.bffcurriculum.dto.CandidatoDTO;
import br.com.senac.bffcurriculum.dto.UsuarioDTO;
import br.com.senac.bffcurriculum.dto.VagaDTO;
import br.com.senac.bffcurriculum.service.CandidatoService;
import br.com.senac.bffcurriculum.service.UsuarioService;
import br.com.senac.bffcurriculum.service.VagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/vaga")
public class VagaController {

    private UsuarioService usuarioService;
    private CandidatoService candidatoService;
    private VagaService vagaService;

    VagaController(
            UsuarioService usuarioService,
            CandidatoService candidatoService,
            VagaService vagaService
    ) {
        this.usuarioService = usuarioService;
        this.candidatoService = candidatoService;
        this.vagaService = vagaService;
    }

	@PostMapping("cadastrar")
	public String cadastrar(@ModelAttribute VagaDTO vagaDTO) {
		vagaService.cadastrar(vagaDTO);
		return "redirect:/vaga/lista/usuario/1";
	}

	@GetMapping("/cadastrar/usuario/{usuarioId}")
	public String cadastrarVaga(@PathVariable Long usuarioId, Model model) {
		UsuarioDTO usuarioDTO = usuarioService.getById(usuarioId);
		model.addAttribute("usuario", usuarioDTO);
		return "vaga/cadastro";
	}

	@GetMapping("/lista/usuario/{usuarioId}")
	public String listaVaga(@PathVariable Long usuarioId, Model model) {
		UsuarioDTO usuarioDTO = usuarioService.getById(usuarioId);
        CandidatoDTO candidatoDTO = candidatoService.getByUsuarioId(usuarioDTO.getId());
        List<VagaDTO> vagas = vagaService.getAll();

		model.addAttribute("usuario", usuarioDTO);
		model.addAttribute("candidato", candidatoDTO);
        model.addAttribute("vagas", vagas);
		return "vaga/lista";
	}

}
