package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.CandidatoDTO;
import br.com.senac.mscurriculum.repository.*;
import br.com.senac.mscurriculum.repository.entity.CandidatoEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CandidatoService {
	private final CandidatoRepository candidatoRepository;
	private final EnderecoRepository enderecoRepository;
	private final ExperienciaRepository experienciaRepository;
	private final HabilidadeRepository habilidadeRepository;
	private final EducacaoRepository educacaoRepository;

	public CandidatoService(
					CandidatoRepository candidatoRepository,
					EnderecoRepository enderecoRepository,
					ExperienciaRepository experienciaRepository,
					HabilidadeRepository habilidadeRepository,
					EducacaoRepository educacaoRepository
	) {
		this.candidatoRepository = candidatoRepository;
		this.enderecoRepository = enderecoRepository;
		this.experienciaRepository = experienciaRepository;
		this.habilidadeRepository = habilidadeRepository;
		this.educacaoRepository = educacaoRepository;
	}

	public CandidatoDTO create(CandidatoDTO candidatoDTO) {
		CandidatoEntity candidateEntity = candidatoDTO.toEntity();

		CandidatoEntity novoCandidate = candidatoRepository.save(candidateEntity);

		return new CandidatoDTO(novoCandidate);
	}

	public CandidatoDTO getById(Long id) {
		CandidatoEntity candidateEntity = candidatoRepository.findById(id)
						.orElseThrow(() -> new RuntimeException("Candidato não encontrado!"));

		return new CandidatoDTO(candidateEntity);
	}

	public List<CandidatoDTO> getAll() {
		List<CandidatoEntity> candidates = candidatoRepository.findAll();

		return candidates.stream()
						.map(CandidatoDTO::new)
						.collect(Collectors.toList());
	}

	public CandidatoDTO update(CandidatoDTO candidatoDTO) {
		CandidatoEntity candidateEntity = candidatoRepository.findById(candidatoDTO.getId())
						.orElseThrow(() -> new RuntimeException("Candidato não encontrado!"));

		candidateEntity.setId(candidatoDTO.getId());
		candidateEntity.setNome(candidatoDTO.getNome());
		candidateEntity.setEmail(candidatoDTO.getEmail());
		candidateEntity.setTelefone(candidatoDTO.getTelefone());
		candidateEntity.setSexo(candidatoDTO.getSexo());
		candidateEntity.setDataNascimento(candidatoDTO.getDataNascimento());
		candidateEntity.setResumoProfissional(candidatoDTO.getResumoProfissional());
		candidateEntity.setUsuarioId(candidatoDTO.getUsuarioId());

		CandidatoEntity candidateAlterado = candidatoRepository.save(candidateEntity);

		return new CandidatoDTO(candidateAlterado);
	}

	public void delete(Long id) {
		CandidatoEntity candidateEntity = candidatoRepository.findById(id)
						.orElseThrow(() -> new RuntimeException("Candidato não encontrado!"));

		candidatoRepository.delete(candidateEntity);
	}
}
