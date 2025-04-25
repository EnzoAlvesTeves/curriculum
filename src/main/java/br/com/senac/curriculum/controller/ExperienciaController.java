package br.com.senac.curriculum.controller;

import br.com.senac.curriculum.dto.ExperienciaDTO;
import br.com.senac.curriculum.repository.candidato.CandidatoRepository;
import br.com.senac.curriculum.repository.experiencia.ExperienciaRepository;
import br.com.senac.curriculum.repository.experiencia.ExperienciaEntity;
import br.com.senac.curriculum.repository.candidato.CandidatoEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/experiencia")
public class ExperienciaController {

	private final ExperienciaRepository experienciaRepository;
	private final CandidatoRepository candidatoRepository;

	public ExperienciaController(ExperienciaRepository experienciaRepository, CandidatoRepository candidatoRepository) {
		this.experienciaRepository = experienciaRepository;
		this.candidatoRepository = candidatoRepository;
	}

	// cadastrar experiencia do candidato
	@PostMapping("/{candidatoId}")
	public ExperienciaDTO cadastrar(@PathVariable Long candidatoId, @RequestBody ExperienciaDTO experienciaDTO) {
		// buscar o candidato
		CandidatoEntity candidato = candidatoRepository.findById(candidatoId)
						.orElseThrow(() -> new RuntimeException("Candidato não encontrado!"));

		// cadastrar a experiencia para o candidato
		ExperienciaEntity experiencia = new ExperienciaEntity();
		experiencia.setCargo(experienciaDTO.getCargo());
		experiencia.setEmpresa(experienciaDTO.getEmpresa());
		experiencia.setDataInicio(experienciaDTO.getDataInicio());
		experiencia.setDataFim(experienciaDTO.getDataFim());
		experiencia.setCandidato(candidato);

		experiencia = experienciaRepository.save(experiencia);

		return new ExperienciaDTO(experiencia);
	}

	// editar experiencia
	@PutMapping("/{experienciaId}")
	public ExperienciaDTO editar(@PathVariable Long experienciaId, @RequestBody ExperienciaDTO experienciaDTO) {
		ExperienciaEntity experiencia = experienciaRepository.findById(experienciaId)
						.orElseThrow(() -> new RuntimeException("Experiência não encontrada!"));

		experiencia.setCargo(experienciaDTO.getCargo());
		experiencia.setEmpresa(experienciaDTO.getEmpresa());
		experiencia.setDataInicio(experienciaDTO.getDataInicio());
		experiencia.setDataFim(experienciaDTO.getDataFim());

		experiencia = experienciaRepository.save(experiencia);

		return new ExperienciaDTO(experiencia);
	}

	// deletar experiencia
	@DeleteMapping("/{experienciaId}")
	public void deletar(@PathVariable Long experienciaId) {
		ExperienciaEntity experiencia = experienciaRepository.findById(experienciaId)
						.orElseThrow(() -> new RuntimeException("Experiência não encontrada!"));

		experienciaRepository.delete(experiencia);
	}
}

