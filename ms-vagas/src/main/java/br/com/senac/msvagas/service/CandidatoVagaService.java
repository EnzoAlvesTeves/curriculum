package br.com.senac.msvagas.service;

import br.com.senac.msvagas.dto.CandidatoVagaDTO;
import br.com.senac.msvagas.repository.CandidatoVagaRepository;
import br.com.senac.msvagas.repository.VagaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
        //implementar a lógica de criação de candidatoVaga
        return null;
    }

    public void delete(Long id) {
        //verificar se existe candidatoVaga com o id
        //se existir, deletar
    }

    public List<CandidatoVagaDTO> getByCandidatoId(Long candidatoId) {
        // implementar a lógica de busca de candidatoVaga por candidatoId
        return null;
    }

    public List<Long> getByVagaId(Long vagaId) {
        // implementar a lógica de busca de candidatoVaga por vagaId
        return null;
    }
}
