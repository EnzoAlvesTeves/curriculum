package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.CandidatoDTO;
import br.com.senac.mscurriculum.repository.*;
import br.com.senac.mscurriculum.repository.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
	public CandidatoDTO create(CandidatoDTO candidatoDTO) {
        try {
            EnderecoEntity enderecoEntity = candidatoDTO.getEndereco().toEntity();
            EnderecoEntity novoEndereco = enderecoRepository.save(enderecoEntity);

            CandidatoEntity candidatoEntity = candidatoDTO.toEntity();
            candidatoEntity.setEndereco(novoEndereco);

            CandidatoEntity novoCandidato = candidatoRepository.save(candidatoEntity);

            candidatoDTO.getEducacoes().forEach(educacao -> {
                EducacaoEntity educacaoEntity = educacao.toEntity();
                educacaoEntity.setCandidato(novoCandidato);

                educacaoRepository.save(educacaoEntity);
            });

            candidatoDTO.getExperiencias().forEach(experiencia -> {
                ExperienciaEntity experienciaEntity = experiencia.toEntity();
                experienciaEntity.setCandidato(novoCandidato);

                experienciaRepository.save(experienciaEntity);
            });

            candidatoDTO.getHabilidades().forEach(habilidade -> {
                HabilidadeEntity habilidadeEntity = habilidade.toEntity();
                habilidadeEntity.setCandidato(novoCandidato);

                habilidadeRepository.save(habilidadeEntity);
            });

            return new CandidatoDTO(novoCandidato);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar candidato", e);
        }
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

    @Transactional
	public CandidatoDTO update(CandidatoDTO candidatoDTO) {
        CandidatoEntity candidatoEntity = candidatoRepository.findById(candidatoDTO.getId())
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado!"));
        try {
            candidatoEntity.setId(candidatoDTO.getId());
            candidatoEntity.setNome(candidatoDTO.getNome());
            candidatoEntity.setEmail(candidatoDTO.getEmail());
            candidatoEntity.setTelefone(candidatoDTO.getTelefone());
            candidatoEntity.setSexo(candidatoDTO.getSexo());
            candidatoEntity.setDataNascimento(candidatoDTO.getDataNascimento());
            candidatoEntity.setResumoProfissional(candidatoDTO.getResumoProfissional());
            candidatoEntity.setUsuarioId(candidatoDTO.getUsuarioId());

            CandidatoEntity candidateAlterado = candidatoRepository.saveAndFlush(candidatoEntity);

            return new CandidatoDTO(candidateAlterado);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao editar candidato", e);
        }
	}

    @Transactional
	public void delete(Long id) {
		CandidatoEntity candidateEntity = candidatoRepository.findById(id)
						.orElseThrow(() -> new RuntimeException("Candidato não encontrado!"));

        try {
            candidatoRepository.delete(candidateEntity);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar candidato", e);
        }

	}

    public CandidatoDTO getByUsuarioId(Long usuarioId) {
        CandidatoEntity candidateEntity = candidatoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado!"));

        return new CandidatoDTO(candidateEntity);
    }
}
