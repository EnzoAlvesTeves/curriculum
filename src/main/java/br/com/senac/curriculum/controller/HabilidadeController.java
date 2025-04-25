package br.com.senac.curriculum.controller;

import br.com.senac.curriculum.dto.HabilidadeDTO;
import br.com.senac.curriculum.repository.candidato.CandidatoRepository;
import br.com.senac.curriculum.repository.habilidade.HabilidadeEntity;
import br.com.senac.curriculum.repository.habilidade.HabilidadeRepository;
import br.com.senac.curriculum.repository.candidato.CandidatoEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/habilidade")
public class HabilidadeController {

	private final HabilidadeRepository habilidadeRepository;
	private final CandidatoRepository candidatoRepository;

	public HabilidadeController(HabilidadeRepository habilidadeRepository, CandidatoRepository candidatoRepository) {
		this.habilidadeRepository = habilidadeRepository;
		this.candidatoRepository = candidatoRepository;
	}

	// cadastrar habilidade do candidato
	@PostMapping("/{candidatoId}")
	public HabilidadeDTO cadastrar(@PathVariable Long candidatoId, @RequestBody HabilidadeDTO habilidadeDTO) {
		// buscar o candidato
		CandidatoEntity candidato = candidatoRepository.findById(candidatoId)
						.orElseThrow(() -> new RuntimeException("Candidato não encontrado!"));

		// cadastrar a habilidade para o candidato
		HabilidadeEntity habilidade = new HabilidadeEntity();
		habilidade.setDescricao(habilidadeDTO.getDescricao());
		habilidade.setNivel(habilidadeDTO.getNivel());
		habilidade.setEspecialidade(habilidadeDTO.getEspecialidade());
		habilidade.setCandidato(candidato);

		habilidade = habilidadeRepository.save(habilidade);

		return new HabilidadeDTO(habilidade);
	}

	// editar habilidade
	@PutMapping("/{habilidadeId}")
	public HabilidadeDTO editar(@PathVariable Long habilidadeId, @RequestBody HabilidadeDTO habilidadeDTO) {
		HabilidadeEntity habilidade = habilidadeRepository.findById(habilidadeId)
						.orElseThrow(() -> new RuntimeException("Habilidade não encontrada!"));

		habilidade.setDescricao(habilidadeDTO.getDescricao());
		habilidade.setNivel(habilidadeDTO.getNivel());
		habilidade.setEspecialidade(habilidadeDTO.getEspecialidade());

		habilidade = habilidadeRepository.save(habilidade);

		return new HabilidadeDTO(habilidade);
	}

	// deletar habilidade
	@DeleteMapping("/{habilidadeId}")
	public void deletar(@PathVariable Long habilidadeId) {
		HabilidadeEntity habilidade = habilidadeRepository.findById(habilidadeId)
						.orElseThrow(() -> new RuntimeException("Habilidade não encontrada!"));

		habilidadeRepository.delete(habilidade);
	}
}

