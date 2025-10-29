package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.HabilidadeDTO;
import br.com.senac.mscurriculum.repository.CandidatoRepository;
import br.com.senac.mscurriculum.repository.HabilidadeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabilidadeService {
    final HabilidadeRepository habilidadeRepository;
    final CandidatoRepository candidatoRepository;

    public HabilidadeService(
            HabilidadeRepository habilidadeRepository,
            CandidatoRepository candidatoRepository
    ) {
        this.habilidadeRepository = habilidadeRepository;
        this.candidatoRepository = candidatoRepository;
    }

    public HabilidadeDTO create(HabilidadeDTO habilidadeDTO) {
        // Lógica para criar uma nova entrada de habilidade
        return null;
    }

    public List<HabilidadeDTO> getByCandidatoId(Long candidatoId) {
        // Lógica para obter a habilidade por ID do candidato
        return null;
    }

    public HabilidadeDTO update(HabilidadeDTO habilidadeDTO) {
        // Lógica para atualizar uma entrada de habilidade existente
        return null;
    }

    public void delete(Long habilidadeId) {
        // Lógica para deletar uma entrada de habilidade
    }
}
