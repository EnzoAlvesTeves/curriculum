package br.com.senac.msvagas.service;

import br.com.senac.msvagas.dto.CandidatoVagaDTO;
import br.com.senac.msvagas.repository.CandidatoVagaRepository;
import br.com.senac.msvagas.repository.VagaRepository;
import br.com.senac.msvagas.repository.entity.CandidatoVagaEntity;
import br.com.senac.msvagas.repository.entity.VagaEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CandidatoVagaService {
    private final CandidatoVagaRepository candidatoVagaRepository;
    private final VagaRepository vagaRepository;

    public CandidatoVagaService(
            CandidatoVagaRepository candidatoVagaRepository,
            VagaRepository vagaRepository
    ) {
        this.candidatoVagaRepository = candidatoVagaRepository;
        this.vagaRepository = vagaRepository;
    }

    @Transactional
    public CandidatoVagaDTO create(CandidatoVagaDTO candidatoVagaDTO) {
        VagaEntity vagaEntity = vagaRepository.findById(candidatoVagaDTO.getVaga().getId())
                .orElseThrow(() -> new RuntimeException("Vaga inexistente!"));

        try {
            CandidatoVagaEntity candidatoVagaEntity = new CandidatoVagaEntity();
            candidatoVagaEntity.setDataInscricao(LocalDateTime.now());
            candidatoVagaEntity.setCandidatoId(candidatoVagaDTO.getCandidatoId());
            candidatoVagaEntity.setVaga(vagaEntity);

            CandidatoVagaEntity savedEntity = candidatoVagaRepository.save(candidatoVagaEntity);

            return new CandidatoVagaDTO(savedEntity);
        }  catch (Exception e) {
            throw new RuntimeException("Erro ao salvar candidatoVaga", e);
        }
    }

    @Transactional
    public void delete(Long id) {
        CandidatoVagaEntity candidatoVagaEntity = candidatoVagaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidatura não encontrada!"));

        try {
            candidatoVagaRepository.delete(candidatoVagaEntity);
        }   catch (Exception e) {
            throw new RuntimeException("Erro ao deletar candidatoVaga", e);
        }
    }

    public List<CandidatoVagaDTO> getByCandidatoId(Long candidatoId) {
        List<CandidatoVagaEntity> candidaturas = candidatoVagaRepository.findByCandidatoId(candidatoId);

        return candidaturas.stream()
                .map(CandidatoVagaDTO::new)
                .toList();
    }

    public List<Long> getByVagaId(Long vagaId) {
        List<CandidatoVagaEntity> candidaturas = candidatoVagaRepository.findByVagaId(vagaId);

        return candidaturas.stream()
                .map(CandidatoVagaEntity::getCandidatoId)
                .toList();
    }
}
