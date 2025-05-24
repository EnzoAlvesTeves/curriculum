package br.com.senac.curriculum.controller;

import br.com.senac.curriculum.dto.VagaDTO;
import br.com.senac.curriculum.service.VagaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/vaga")
public class VagaController {

	@Autowired
	private VagaService service;

	//salvar vaga
	@PostMapping
	public VagaDTO cadastrar(@RequestBody VagaDTO vagaDTO) {
		return service.cadastrar(vagaDTO);
	}

	@GetMapping("/lista")
	public String listaVaga(Model model) {
		List<VagaDTO> vagas = service.listarVagas();

		model.addAttribute("vagas", vagas);
		return "vaga/lista";
	}

}
