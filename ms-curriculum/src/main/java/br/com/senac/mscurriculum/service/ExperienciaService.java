package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.ExperienciaDTO;
import br.com.senac.mscurriculum.mapper.ExperienciaMapper;
import br.com.senac.mscurriculum.repository.ExperienciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExperienciaService {
    private final ExperienciaRepository experienciaRepository;

    public ExperienciaDTO cadastrar(Long candidatoId, ExperienciaDTO dto) {
        var entity = ExperienciaMapper.toEntity(dto, candidatoId);
        var experiencia = experienciaRepository.save(entity);

        return ExperienciaMapper.toDTO(experiencia);
    }

    public List<ExperienciaDTO> buscarPorCandidato(Long candidatoId) {
        var experiencias = experienciaRepository.findByIdCandidato(candidatoId);

        return experiencias.stream()
                .map(ExperienciaMapper::toDTO)
                .toList();
    }
}

