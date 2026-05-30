package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.HabilidadeDTO;
import br.com.senac.mscurriculum.mapper.HabilidadeMapper;
import br.com.senac.mscurriculum.repository.HabilidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HabilidadeService {
    private final HabilidadeRepository habilidadeRepository;

    public HabilidadeDTO cadastrar(Long candidatoId, HabilidadeDTO dto) {
        var entity = HabilidadeMapper.toEntity(dto, candidatoId);
        var habilidade = habilidadeRepository.save(entity);

        return HabilidadeMapper.toDTO(habilidade);
    }

    public List<HabilidadeDTO> buscarPorCandidato(Long candidatoId) {
        var habilidades = habilidadeRepository.findByIdCandidato(candidatoId);

        return habilidades.stream()
                .map(HabilidadeMapper::toDTO)
                .toList();
    }
}

