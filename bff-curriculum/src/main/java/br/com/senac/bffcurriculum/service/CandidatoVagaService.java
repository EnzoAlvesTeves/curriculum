package br.com.senac.bffcurriculum.service;

import br.com.senac.bffcurriculum.client.CandidatoVagaClient;
import br.com.senac.bffcurriculum.dto.CandidatoDTO;
import br.com.senac.bffcurriculum.dto.CandidatoVagaDTO;
import br.com.senac.bffcurriculum.dto.VagaDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CandidatoVagaService {
    private final CandidatoVagaClient candidatoVagaClient;

    CandidatoVagaService(CandidatoVagaClient candidatoVagaClient) {
        this.candidatoVagaClient = candidatoVagaClient ;
    }

    public void candidatarVaga(VagaDTO vagaDTO, CandidatoDTO candidatoDTO) {
        CandidatoVagaDTO candidatoVagaDTO = new CandidatoVagaDTO();
        candidatoVagaDTO.setCandidatoId(candidatoDTO.getId());
        candidatoVagaDTO.setVaga(vagaDTO);
        candidatoVagaDTO.setDataInscricao(LocalDateTime.now());

        candidatoVagaClient.create(candidatoVagaDTO);
    }

    public List<CandidatoVagaDTO> candidatosPorVaga(Long vagaId) {
        return candidatoVagaClient.getByVagaId(vagaId);
    }

    public List<CandidatoVagaDTO> vagasPorCandidato(Long candidatoId) {
        return candidatoVagaClient.getByCandidatoId(candidatoId);
    }

    public void removerCandidatura(Long id) {
        candidatoVagaClient.delete(id);
    }
}
