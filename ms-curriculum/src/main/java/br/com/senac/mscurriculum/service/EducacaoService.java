package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.EducacaoDTO;
import br.com.senac.mscurriculum.mapper.EducacaoMapper;
import br.com.senac.mscurriculum.repository.EducacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducacaoService {
    private final EducacaoRepository educacaoRepository;

    public EducacaoDTO cadastrar(Long candidatoId, EducacaoDTO dto) {
        var entity = EducacaoMapper.toEntity(dto, candidatoId);
        var educacao = educacaoRepository.save(entity);

        return EducacaoMapper.toDTO(educacao);
    }

    public List<EducacaoDTO> buscarPorCandidato(Long candidatoId) {
        var educacoes = educacaoRepository.findByIdCandidato(candidatoId);

        return educacoes.stream()
                .map(EducacaoMapper::toDTO)
                .toList();
    }
}

