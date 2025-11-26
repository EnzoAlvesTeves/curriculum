package br.com.senac.msvagas.service;

import br.com.senac.msvagas.dto.CandidatoVagaDTO;
import br.com.senac.msvagas.repository.CandidatoVagaRepository;
import br.com.senac.msvagas.repository.VagaRepository;
import br.com.senac.msvagas.repository.entity.CandidatoVagaEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CandidatoVagaService {
    private final CandidatoVagaRepository candidatoVagaRepository;
    private final VagaRepository vagaRepository;

    public CandidatoVagaService(
            CandidatoVagaRepository candidatoVagaRepository,
            VagaRepository vagaRepository
    ){
        this.candidatoVagaRepository = candidatoVagaRepository;
        this.vagaRepository = vagaRepository;
    }

    public CandidatoVagaDTO create(CandidatoVagaDTO candidatoVagaDTO) {
			CandidatoVagaEntity candidatoVagaEntity = new CandidatoVagaEntity();
			candidatoVagaEntity.setCandidatoId(candidatoVagaDTO.getCandidatoId());
			candidatoVagaEntity.setVaga(candidatoVagaDTO.getVaga().toEntity());

			CandidatoVagaEntity savedEntity = candidatoVagaRepository.save(candidatoVagaEntity);

			return new CandidatoVagaDTO(savedEntity);
    }

    public void delete(Long id) {
			CandidatoVagaEntity candidatoVagaEntity = candidatoVagaRepository.findById(id)
							.orElseThrow(() -> new RuntimeException("Candidatura não encontrada!"));

			candidatoVagaRepository.delete(candidatoVagaEntity);
    }

    public List<CandidatoVagaDTO> getByCandidatoId(Long candidatoId) {
			List<CandidatoVagaEntity> candidaturas = candidatoVagaRepository.findByCandidatoId(candidatoId);

			return candidaturas.stream()
							.map(CandidatoVagaDTO::new)
							.collect(Collectors.toList());
    }

    public List<Long> getByVagaId(Long vagaId) {
			List<CandidatoVagaEntity> candidaturas = candidatoVagaRepository.findByVagaId(vagaId);

			return candidaturas.stream()
							.map(CandidatoVagaEntity::getCandidatoId)
							.collect(Collectors.toList());
    }
}
