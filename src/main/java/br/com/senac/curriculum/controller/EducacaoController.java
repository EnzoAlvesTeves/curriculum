package br.com.senac.curriculum.controller;

import br.com.senac.curriculum.dto.EducacaoDTO;
import br.com.senac.curriculum.repository.candidato.CandidatoEntity;
import br.com.senac.curriculum.repository.candidato.CandidatoRepository;
import br.com.senac.curriculum.repository.educacao.EducacaoEntity;
import br.com.senac.curriculum.repository.educacao.EducacaoRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/educacao")
public class EducacaoController {

	private EducacaoRepository educacaoRepository;
	private final CandidatoRepository candidatoRepository;

	public EducacaoController(EducacaoRepository educacaoRepository, CandidatoRepository candidatoRepository) {
		this.educacaoRepository = educacaoRepository;
		this.candidatoRepository = candidatoRepository;
	}

	// cadastrar educacao do candidato
	@PostMapping("/{candidatoId}")
	public EducacaoDTO cadastrar(@PathVariable Long candidatoId, @RequestBody EducacaoDTO educacaoDTO) {
		// buscar o candidato
		CandidatoEntity candidato = candidatoRepository.findById(candidatoId)
						.orElseThrow(() -> new RuntimeException("Candidato não encontrado!"));

		// cadastrar a educacao para o candidato
		EducacaoEntity educacao = new EducacaoEntity();
		educacao.setGrau(educacaoDTO.getGrau());
		educacao.setDataInicio(educacaoDTO.getDataInicio());
		educacao.setDataFim(educacaoDTO.getDataFim());
		educacao.setInstituicao(educacaoDTO.getInstituicao());
		educacao.setCurso(educacaoDTO.getCurso());
		educacao.setCandidato(candidato);

		educacao = educacaoRepository.save(educacao);

		return new EducacaoDTO(educacao);
	}

	// editar educacao
	@PutMapping("/{educacaoId}")
	public EducacaoDTO editar(@PathVariable Long educacaoId, @RequestBody EducacaoDTO educacaoDTO) {
		EducacaoEntity educacao = educacaoRepository.findById(educacaoId)
						.orElseThrow(() -> new RuntimeException("Educação não encontrada!"));

		educacao.setGrau(educacaoDTO.getGrau());
		educacao.setDataInicio(educacaoDTO.getDataInicio());
		educacao.setDataFim(educacaoDTO.getDataFim());
		educacao.setInstituicao(educacaoDTO.getInstituicao());
		educacao.setCurso(educacaoDTO.getCurso());

		educacao = educacaoRepository.save(educacao);

		return new EducacaoDTO(educacao);
	}

	// deletar educacao
	@DeleteMapping("/{educacaoId}")
	public void deletar(@PathVariable Long educacaoId) {
		EducacaoEntity educacao = educacaoRepository.findById(educacaoId)
						.orElseThrow(() -> new RuntimeException("Educação não encontrada!"));

		educacaoRepository.delete(educacao);
	}

}

// cadastrar educacao do candidato
// um endpoint POST, vai ter um path variable com o ID do candidato e um BODY com o JSON da educacao que vai ser cadastrada
// um método que vai receber como parametro um ID de candidato e cadastrar uma educacao para esse candidato
// buscar o candidato
// cadastrar a educacao para o candidato

// editar educacao

// deletar educacao
