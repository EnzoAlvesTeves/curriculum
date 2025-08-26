package br.com.senac.curriculum.service;

import br.com.senac.curriculum.dto.CandidatoDTO;
import br.com.senac.curriculum.dto.CandidatoVagasDTO;
import br.com.senac.curriculum.dto.VagaDTO;
import br.com.senac.curriculum.repository.candidato.CandidatoEntity;
import br.com.senac.curriculum.repository.candidato.CandidatoRepository;
import br.com.senac.curriculum.repository.candidatoVaga.CandidatoVagaEntity;
import br.com.senac.curriculum.repository.candidatoVaga.CandidatoVagaRepository;
import br.com.senac.curriculum.repository.vaga.VagaEntity;
import br.com.senac.curriculum.repository.vaga.VagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CandidatoVagaService {

    @Autowired
    private CandidatoVagaRepository candidatoVagaRepository;

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private VagaRepository vagaRepository;

    @Transactional
    public void candidatarVaga(Long candidatoId, Long vagaId) {
        CandidatoEntity candidato = candidatoRepository.findById(candidatoId)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

        VagaEntity vaga = vagaRepository.findById(vagaId)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));

        var candidatoVaga = new CandidatoVagaEntity();
        candidatoVaga.setCandidato(candidato);
        candidatoVaga.setVaga(vaga);
        candidatoVaga.setDataInscricao(LocalDateTime.now());

        candidatoVagaRepository.save(candidatoVaga);
    }

    @Transactional
    public void removerCandidatura(Long id) {
        CandidatoVagaEntity candidatoVaga = candidatoVagaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidatura não encontrada"));

        candidatoVagaRepository.delete(candidatoVaga);
    }

    public List<CandidatoDTO> listarCandidatosPorVaga(Long vagaId) {
        VagaEntity vaga = vagaRepository.findById(vagaId)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));

        List<CandidatoVagaEntity> candidatosVaga = candidatoVagaRepository.findByVaga(vaga);

        return candidatosVaga.stream()
                .map(candidatoVaga -> new CandidatoDTO(candidatoVaga.getCandidato()))
                .toList();
    }

    public CandidatoVagasDTO listarVagasPorCandidato(Long candidatoId) {
        CandidatoEntity candidato = candidatoRepository.findById(candidatoId)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado"));

        List<CandidatoVagaEntity> vagasCandidato = candidatoVagaRepository.findByCandidato(candidato);

        List<VagaDTO> vagas = vagasCandidato.stream()
                .map(candidatoVaga -> new VagaDTO(candidatoVaga.getVaga()))
                .toList();

        return new CandidatoVagasDTO(new CandidatoDTO(candidato), vagas);
    }

}
