package br.com.senac.curriculum.controller;


import br.com.senac.curriculum.controller.request.CandidaturaRequest;
import br.com.senac.curriculum.dto.CandidatoDTO;
import br.com.senac.curriculum.dto.VagaDTO;
import br.com.senac.curriculum.service.CandidatoVagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/candidato-vaga")
public class CandidatoVagaController {

	@Autowired
	private CandidatoVagaService service;

	@PostMapping("/candidatura")
	public String candidatarVaga(@ModelAttribute CandidaturaRequest candidaturaRequest, Model model){
		service.candidatarVaga(
				candidaturaRequest.getCandidatoId(),
				candidaturaRequest.getVagaId()
		);

		return "redirect:/candidato/vagas/" + candidaturaRequest.getCandidatoId();
	}

	//listagem de candidatos por vaga
	@GetMapping("/vaga/{id}")
	public String candidatosPorVaga(@PathVariable Long id, Model model) {
		List<CandidatoDTO> candidatos = service.listarCandidatosPorVaga(id);

		model.addAttribute("candidatos", candidatos);
		return "vaga/candidatos";
	}

	//listagem de vagas que candidato se inscreveu
	@GetMapping("/candidato/{id}")
	public String vagasPorCandidato(@PathVariable Long id, Model model) {
		List<VagaDTO> vagas = service.listarVagasPorCandidato(id);

		model.addAttribute("candidato", vagas);
		return "candidato/vagas";
	}

	@DeleteMapping("/{id}")
	public String deletarCandidatura(@PathVariable Long id) {
		service.removerCandidatura(id);
		return "redirect:/candidato/vagas";
	}
}


