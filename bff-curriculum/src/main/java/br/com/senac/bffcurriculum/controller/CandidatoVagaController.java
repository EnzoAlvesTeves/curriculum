package br.com.senac.bffcurriculum.controller;


import br.com.senac.bffcurriculum.controller.request.Candidatura;
import br.com.senac.bffcurriculum.dto.CandidatoDTO;
import br.com.senac.bffcurriculum.dto.CandidatoVagaDTO;
import br.com.senac.bffcurriculum.dto.UsuarioDTO;
import br.com.senac.bffcurriculum.dto.VagaDTO;
import br.com.senac.bffcurriculum.service.CandidatoService;
import br.com.senac.bffcurriculum.service.CandidatoVagaService;
import br.com.senac.bffcurriculum.service.UsuarioService;
import br.com.senac.bffcurriculum.service.VagaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/candidato-vaga")
public class CandidatoVagaController {

    private final CandidatoVagaService candidatoVagaService;
    private final VagaService vagaService;
    private final CandidatoService candidatoService;
    private final UsuarioService usuarioService;

    CandidatoVagaController(
            CandidatoVagaService candidatoVagaService,
            VagaService vagaService,
            CandidatoService candidatoService,
            UsuarioService usuarioService
    ) {
        this.candidatoVagaService = candidatoVagaService;
        this.vagaService = vagaService;
        this.candidatoService = candidatoService;
        this.usuarioService = usuarioService;
    }

	@PostMapping("/candidatura")
	public String candidatarVaga(@ModelAttribute Candidatura candidatura, Model model) {
        VagaDTO vaga = vagaService.getById(candidatura.getVagaId());
        CandidatoDTO candidato = candidatoService.getById(candidatura.getCandidatoId());
        UsuarioDTO usuario = usuarioService.getById(candidato.getUsuarioId());

        try {
            candidatoVagaService.candidatarVaga(vaga, candidato);
            return "redirect:/candidato-vaga/candidato/" + candidato.getId();
        } catch (Exception e) {
            String errorMessage = "Erro ao candidatar-se à vaga: Candidato já inscrito nesta vaga.";
            model.addAttribute("errorMessage", errorMessage);
            return  "redirect:/vaga/lista/usuario/" + usuario.getId() + "?error=" + errorMessage;
        }
	}

	//listagem de candidatos por vaga
	@GetMapping("/vaga/{vagaId}/usuario/{usuarioId}")
	public String candidatosPorVaga(@PathVariable Long vagaId, @PathVariable Long usuarioId, Model model) {
        UsuarioDTO usuarioDTO = usuarioService.getById(usuarioId);

        List<CandidatoVagaDTO> candidatoVagas = candidatoVagaService.candidatosPorVaga(vagaId);
        List<CandidatoDTO> candidatos = candidatoVagas.stream().map(candidatoVagaDTO -> {
            CandidatoDTO candidato = candidatoService.getById(candidatoVagaDTO.getCandidatoId());
            UsuarioDTO usuario = usuarioService.getById(candidato.getUsuarioId());

            candidato.setUsuario(usuario);
            return candidato;
        }).toList();

        model.addAttribute("usuario", usuarioDTO);
		model.addAttribute("candidatos", candidatos);
		return "vaga/candidatos";
	}

	//listagem de vagas que candidato se inscreveu
	@GetMapping("/candidato/{candidatoId}")
	public String vagasPorCandidato(@PathVariable Long candidatoId, Model model) {
        CandidatoDTO candidato = candidatoService.getById(candidatoId);
        UsuarioDTO usuario = usuarioService.getById(candidato.getUsuarioId());
        candidato.setUsuario(usuario);

        List<CandidatoVagaDTO> candidatoVagas = candidatoVagaService.vagasPorCandidato(candidato.getId());
        List<VagaDTO> vagas = candidatoVagas.stream().map(candidatoVagaDTO ->
                vagaService.getById(candidatoVagaDTO.getVaga().getId())
        ).toList();

		model.addAttribute("candidato", candidato);
        model.addAttribute("usuario", usuario);
		model.addAttribute("vagas", vagas);
		return "candidato/vagas";
	}

	@DeleteMapping("/{id}")
	public String deletarCandidatura(@PathVariable Long id) {
		candidatoVagaService.removerCandidatura(id);
		return "redirect:/candidato/vagas";
	}
}


