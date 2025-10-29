package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.ExperienciaDTO;
import br.com.senac.mscurriculum.repository.CandidatoRepository;
import br.com.senac.mscurriculum.repository.ExperienciaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExperienciaService {
    final ExperienciaRepository experienciaRepository;
    final CandidatoRepository candidatoRepository;

    public ExperienciaService(
            ExperienciaRepository experienciaRepository,
            CandidatoRepository candidatoRepository
    ) {
        this.experienciaRepository = experienciaRepository;
        this.candidatoRepository = candidatoRepository;
    }

    public ExperienciaDTO create(ExperienciaDTO experienciaDTO) {
        // Lógica para criar uma nova entrada de experiência
        return null;
    }

    public List<ExperienciaDTO> getByCandidatoId(Long candidatoId) {
        // Lógica para obter a experiência por ID do candidato
        return null;
    }

    public ExperienciaDTO update(ExperienciaDTO experienciaDTO) {
        // Lógica para atualizar uma entrada de experiência existente
        return null;
    }

    public void delete(Long experienciaId) {
        // Lógica para deletar uma entrada de experiência
    }
}
