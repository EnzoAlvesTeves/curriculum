package br.com.senac.bffcurriculum.controller;


import br.com.senac.bffcurriculum.client.CandidatoVagaClient;
import br.com.senac.bffcurriculum.controller.request.CandidaturaRequest;
import br.com.senac.bffcurriculum.dto.CandidatoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/candidato-vaga")
public class CandidatoVagaController {

	@Autowired
	private CandidatoVagaClient candidatoVagaClient;


	@PostMapping("/candidatura")
	public String candidatarVaga(@ModelAttribute CandidaturaRequest candidaturaRequest, Model model){
//        candidatoVagaClient.create(
//				candidaturaRequest.getCandidatoId(),
//				candidaturaRequest.getVagaId()
//		);

		return "redirect:/candidato-vaga/candidato/" + candidaturaRequest.getCandidatoId();
	}

	//listagem de candidatos por vaga
	@GetMapping("/vaga/{id}")
	public String candidatosPorVaga(@PathVariable Long id, Model model) {
		List<CandidatoDTO> candidatos = new ArrayList<>();


		model.addAttribute("candidatos", candidatos);
		return "vaga/candidatos";
	}

	//listagem de vagas que candidato se inscreveu
	@GetMapping("/candidato/{id}")
	public String vagasPorCandidato(@PathVariable Long id, Model model) {

		model.addAttribute("candidato", null);
		model.addAttribute("vagas", null);
		return "candidato/vagas";
	}

	@DeleteMapping("/{id}")
	public String deletarCandidatura(@PathVariable Long id) {
		candidatoVagaClient.delete(id);
		return "redirect:/candidato/vagas";
	}
}


