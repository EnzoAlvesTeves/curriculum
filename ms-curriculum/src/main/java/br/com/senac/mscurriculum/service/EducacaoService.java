package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.EducacaoDTO;
import br.com.senac.mscurriculum.mapper.EducacaoMapper;
import br.com.senac.mscurriculum.repository.EducacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducacaoService {
    private final EducacaoRepository educacaoRepository;
    private final CandidatoAuthorizationService candidatoAuthorizationService;

    public EducacaoDTO cadastrar(Long candidatoId, EducacaoDTO dto, Jwt jwt) {
        candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt);
        return cadastrar(candidatoId, dto);
    }

    public EducacaoDTO cadastrar(Long candidatoId, EducacaoDTO dto) {
        var entity = EducacaoMapper.toEntity(dto, candidatoId);
        var educacao = educacaoRepository.save(entity);

        return EducacaoMapper.toDTO(educacao);
    }

    public EducacaoDTO alterar(Long candidatoId, Long educacaoId, EducacaoDTO dto, Jwt jwt) {
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
        return EducacaoMapper.toDTO(educacaoAtualizada);
    }

    public void deletar(Long candidatoId, Long educacaoId, Jwt jwt) {
        candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt);
        var educacao = educacaoRepository.findById(educacaoId)
                .orElseThrow(() -> new RuntimeException("Educação não encontrada para o ID: " + educacaoId));

        if (!educacao.getIdCandidato().equals(candidatoId)) {
            throw new RuntimeException("Educação não pertence ao candidato ID: " + candidatoId);
        }

        educacaoRepository.delete(educacao);
    }

    public List<EducacaoDTO> buscarPorCandidato(Long candidatoId) {
        var educacoes = educacaoRepository.findByIdCandidato(candidatoId);

        return educacoes.stream()
                .map(EducacaoMapper::toDTO)
                .toList();
    }
}

