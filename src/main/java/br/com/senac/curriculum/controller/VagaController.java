package br.com.senac.curriculum.controller;

import br.com.senac.curriculum.repository.vaga.VagaRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/vaga")
public class VagaController {

	private final VagaRepository vagaRepository;

	public VagaController(VagaRepository vagaRepository) {
		this.vagaRepository = vagaRepository;
	}

}
