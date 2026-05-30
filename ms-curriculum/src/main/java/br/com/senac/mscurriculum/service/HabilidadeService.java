package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.HabilidadeDTO;
import br.com.senac.mscurriculum.mapper.HabilidadeMapper;
import br.com.senac.mscurriculum.repository.HabilidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HabilidadeService {
    private final HabilidadeRepository habilidadeRepository;
    private final CandidatoAuthorizationService candidatoAuthorizationService;

    public HabilidadeDTO cadastrar(Long candidatoId, HabilidadeDTO dto, Jwt jwt) {
        candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt);
        return cadastrar(candidatoId, dto);
    }

    public HabilidadeDTO cadastrar(Long candidatoId, HabilidadeDTO dto) {
        var entity = HabilidadeMapper.toEntity(dto, candidatoId);
        var habilidade = habilidadeRepository.save(entity);

        return HabilidadeMapper.toDTO(habilidade);
    }

    public HabilidadeDTO alterar(Long candidatoId, Long habilidadeId, HabilidadeDTO dto, Jwt jwt) {
        candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt);
        var habilidade = habilidadeRepository.findById(habilidadeId)
                .orElseThrow(() -> new RuntimeException("Habilidade não encontrada para o ID: " + habilidadeId));

        if (!habilidade.getIdCandidato().equals(candidatoId)) {
            throw new RuntimeException("Habilidade não pertence ao candidato ID: " + candidatoId);
        }

        habilidade.setDescricao(dto.getDescricao());
        habilidade.setNivel(dto.getNivel());

        var habilidadeAtualizada = habilidadeRepository.save(habilidade);
        return HabilidadeMapper.toDTO(habilidadeAtualizada);
    }

    public void deletar(Long candidatoId, Long habilidadeId, Jwt jwt) {
        candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt);
        var habilidade = habilidadeRepository.findById(habilidadeId)
                .orElseThrow(() -> new RuntimeException("Habilidade não encontrada para o ID: " + habilidadeId));

        if (!habilidade.getIdCandidato().equals(candidatoId)) {
            throw new RuntimeException("Habilidade não pertence ao candidato ID: " + candidatoId);
        }

        habilidadeRepository.delete(habilidade);
    }

    public List<HabilidadeDTO> buscarPorCandidato(Long candidatoId) {
        var habilidades = habilidadeRepository.findByIdCandidato(candidatoId);

        return habilidades.stream()
                .map(HabilidadeMapper::toDTO)
                .toList();
    }
}

