package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.EducacaoDTO;
import br.com.senac.mscurriculum.mapper.EducacaoMapper;
import br.com.senac.mscurriculum.repository.EducacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EducacaoService {
    private final EducacaoRepository educacaoRepository;
    private final CandidatoAuthorizationService candidatoAuthorizationService;

    public EducacaoDTO cadastrar(Long candidatoId, EducacaoDTO dto, Jwt jwt) {
        log.info("Iniciando cadastro de educação para candidatoId={}", candidatoId);
        candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt);
        return cadastrar(candidatoId, dto);
    }

    public EducacaoDTO cadastrar(Long candidatoId, EducacaoDTO dto) {
        log.debug("Persistindo educação para candidatoId={} curso={} instituicao={}", candidatoId, dto.getCurso(), dto.getInstituicao());
        var entity = EducacaoMapper.toEntity(dto, candidatoId);
        var educacao = educacaoRepository.save(entity);

        log.info("Educação cadastrada com sucesso. candidatoId={} educacaoId={}", candidatoId, educacao.getId());
        return EducacaoMapper.toDTO(educacao);
    }

    public EducacaoDTO alterar(Long candidatoId, Long educacaoId, EducacaoDTO dto, Jwt jwt) {
        log.info("Iniciando alteração de educação. candidatoId={} educacaoId={}", candidatoId, educacaoId);
        candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt);
        var educacao = educacaoRepository.findById(educacaoId)
                .orElseThrow(() -> new RuntimeException("Educação não encontrada para o ID: " + educacaoId));

        if (!educacao.getIdCandidato().equals(candidatoId)) {
            throw new RuntimeException("Educação não pertence ao candidato ID: " + candidatoId);
        }

        educacao.setCurso(dto.getCurso());
        educacao.setGrau(dto.getGrau());
        educacao.setInstituicao(dto.getInstituicao());
        educacao.setDataInicio(dto.getDataInicio());
        educacao.setDataFim(dto.getDataFim());

        var educacaoAtualizada = educacaoRepository.save(educacao);
        log.info("Educação alterada com sucesso. candidatoId={} educacaoId={}", candidatoId, educacaoId);
        return EducacaoMapper.toDTO(educacaoAtualizada);
    }

    public void deletar(Long candidatoId, Long educacaoId, Jwt jwt) {
        log.info("Iniciando exclusão de educação. candidatoId={} educacaoId={}", candidatoId, educacaoId);
        candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt);
        var educacao = educacaoRepository.findById(educacaoId)
                .orElseThrow(() -> new RuntimeException("Educação não encontrada para o ID: " + educacaoId));

        if (!educacao.getIdCandidato().equals(candidatoId)) {
            throw new RuntimeException("Educação não pertence ao candidato ID: " + candidatoId);
        }

        educacaoRepository.delete(educacao);
        log.info("Educação excluída com sucesso. candidatoId={} educacaoId={}", candidatoId, educacaoId);
    }

    public List<EducacaoDTO> buscarPorCandidato(Long candidatoId) {
        log.debug("Buscando educações do candidatoId={}", candidatoId);
        var educacoes = educacaoRepository.findByIdCandidato(candidatoId);

        log.debug("Encontradas {} educação(ões) para candidatoId={}", educacoes.size(), candidatoId);
        return educacoes.stream()
                .map(EducacaoMapper::toDTO)
                .toList();
    }
}

