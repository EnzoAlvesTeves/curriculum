package br.com.senac.curriculum.controller;

import br.com.senac.curriculum.dto.VagaDTO;
import br.com.senac.curriculum.repository.vaga.VagaEntity;
import br.com.senac.curriculum.repository.vaga.VagaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/vaga")
public class VagaController {

	private final VagaRepository vagaRepository;

	public VagaController(VagaRepository vagaRepository) {
		this.vagaRepository = vagaRepository;
	}

	//salvar vaga
	@PostMapping
	public VagaDTO cadastrar(@RequestBody VagaDTO vagaDTO) {

		VagaEntity vaga = new VagaEntity();
		vaga.setTitulo(vagaDTO.getTitulo());
		vaga.setDescricao(vagaDTO.getDescricao());
		vaga.setEmpresa(vagaDTO.getEmpresa());
		vaga.setBeneficios(vagaDTO.getBeneficios());
		vaga.setSalario(vagaDTO.getSalario());

		vaga = vagaRepository.save(vaga);

		return new VagaDTO(vaga);
	}

	@GetMapping("/lista")
	public String listaVaga(Model model) {
		List<VagaEntity> vagas = vagaRepository.findAll();
		model.addAttribute("vagas", vagas);
		return "vaga/lista";
	}

}
