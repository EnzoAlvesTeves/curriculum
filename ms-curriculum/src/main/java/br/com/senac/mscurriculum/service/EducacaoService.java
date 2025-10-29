package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.EducacaoDTO;
import br.com.senac.mscurriculum.repository.CandidatoRepository;
import br.com.senac.mscurriculum.repository.EducacaoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EducacaoService {
    final EducacaoRepository educacaoRepository;
    final CandidatoRepository candidatoRepository;

    public EducacaoService(
            EducacaoRepository educacaoRepository,
            CandidatoRepository candidatoRepository
    ) {
        this.educacaoRepository = educacaoRepository;
        this.candidatoRepository = candidatoRepository;
    }

    public EducacaoDTO create(EducacaoDTO educacaoDTO) {
        // Lógica para criar uma nova entrada de educação
        return null;
    }

    public List<EducacaoDTO> getByCandidatoId(Long candidatoId) {
        // Lógica para obter a educação por ID do candidato
        return null;
    }

    public EducacaoDTO update(EducacaoDTO educacaoDTO) {
        // Lógica para atualizar uma entrada de educação existente
        return null;
    }

    public void delete(Long educacaoId) {
        // Lógica para deletar uma entrada de educação
    }
}
