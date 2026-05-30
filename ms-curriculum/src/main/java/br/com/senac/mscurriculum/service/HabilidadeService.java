package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.HabilidadeDTO;
import br.com.senac.mscurriculum.mapper.HabilidadeMapper;
import br.com.senac.mscurriculum.repository.HabilidadeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HabilidadeService {
    private final HabilidadeRepository habilidadeRepository;
    private final CandidatoAuthorizationService candidatoAuthorizationService;

    public HabilidadeDTO cadastrar(Long candidatoId, HabilidadeDTO dto, Jwt jwt) {
        log.info("Iniciando cadastro de habilidade para candidatoId={}", candidatoId);
        candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt);
        return cadastrar(candidatoId, dto);
    }

    public HabilidadeDTO cadastrar(Long candidatoId, HabilidadeDTO dto) {
        log.debug("Persistindo habilidade para candidatoId={} descricao={}", candidatoId, dto.getDescricao());
        var entity = HabilidadeMapper.toEntity(dto, candidatoId);
        var habilidade = habilidadeRepository.save(entity);

        log.info("Habilidade cadastrada com sucesso. candidatoId={} habilidadeId={}", candidatoId, habilidade.getId());
        return HabilidadeMapper.toDTO(habilidade);
    }

    public HabilidadeDTO alterar(Long candidatoId, Long habilidadeId, HabilidadeDTO dto, Jwt jwt) {
        log.info("Iniciando alteração de habilidade. candidatoId={} habilidadeId={}", candidatoId, habilidadeId);
        candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt);
        var habilidade = habilidadeRepository.findById(habilidadeId)
                .orElseThrow(() -> new RuntimeException("Habilidade não encontrada para o ID: " + habilidadeId));

        if (!habilidade.getIdCandidato().equals(candidatoId)) {
            throw new RuntimeException("Habilidade não pertence ao candidato ID: " + candidatoId);
        }

        habilidade.setDescricao(dto.getDescricao());
        habilidade.setNivel(dto.getNivel());

        var habilidadeAtualizada = habilidadeRepository.save(habilidade);
        log.info("Habilidade alterada com sucesso. candidatoId={} habilidadeId={}", candidatoId, habilidadeId);
        return HabilidadeMapper.toDTO(habilidadeAtualizada);
    }

    public void deletar(Long candidatoId, Long habilidadeId, Jwt jwt) {
        log.info("Iniciando exclusão de habilidade. candidatoId={} habilidadeId={}", candidatoId, habilidadeId);
        candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt);
        var habilidade = habilidadeRepository.findById(habilidadeId)
                .orElseThrow(() -> new RuntimeException("Habilidade não encontrada para o ID: " + habilidadeId));

        if (!habilidade.getIdCandidato().equals(candidatoId)) {
            throw new RuntimeException("Habilidade não pertence ao candidato ID: " + candidatoId);
        }

        habilidadeRepository.delete(habilidade);
        log.info("Habilidade excluída com sucesso. candidatoId={} habilidadeId={}", candidatoId, habilidadeId);
    }

    public List<HabilidadeDTO> buscarPorCandidato(Long candidatoId) {
        log.debug("Buscando habilidades do candidatoId={}", candidatoId);
        var habilidades = habilidadeRepository.findByIdCandidato(candidatoId);

        log.debug("Encontradas {} habilidade(s) para candidatoId={}", habilidades.size(), candidatoId);
        return habilidades.stream()
                .map(HabilidadeMapper::toDTO)
                .toList();
    }
}

