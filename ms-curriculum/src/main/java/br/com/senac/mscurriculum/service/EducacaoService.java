package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.EducacaoDTO;
import br.com.senac.mscurriculum.repository.CandidatoRepository;
import br.com.senac.mscurriculum.repository.EducacaoRepository;
import br.com.senac.mscurriculum.repository.entity.CandidatoEntity;
import br.com.senac.mscurriculum.repository.entity.EducacaoEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public EducacaoDTO create(EducacaoDTO educacaoDTO) {
        CandidatoEntity candidatoEntity = candidatoRepository.findById(educacaoDTO.getCandidatoId())
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado!"));

        try {
            EducacaoEntity educacaoEntity = educacaoDTO.toEntity();
            educacaoEntity.setCandidato(candidatoEntity);

            EducacaoEntity novaEducacao = educacaoRepository.save(educacaoEntity);

            return new EducacaoDTO(novaEducacao);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao cadastrar Educação", e);
        }
    }

    public List<EducacaoDTO> getByCandidatoId(Long candidatoId) {
        List<EducacaoEntity> educacoes = educacaoRepository.findByCandidatoId(candidatoId);

        return educacoes.stream()
                .map(EducacaoDTO::new)
                .toList();
    }

    @Transactional
    public EducacaoDTO update(EducacaoDTO educacaoDTO) {
        EducacaoEntity educacaoEntity = educacaoRepository.findById(educacaoDTO.getId())
                .orElseThrow(() -> new RuntimeException("Educação não encontrada!"));

        try {
            educacaoEntity.setInstituicao(educacaoDTO.getInstituicao());
            educacaoEntity.setCurso(educacaoDTO.getCurso());
            educacaoEntity.setDataInicio(educacaoDTO.getDataInicio());
            educacaoEntity.setDataFim(educacaoDTO.getDataFim());
            educacaoEntity.setGrau(educacaoDTO.getGrau());

            EducacaoEntity educacaoAlterada = educacaoRepository.save(educacaoEntity);

            return new EducacaoDTO(educacaoAlterada);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao editar Educação", e);
        }
    }

    @Transactional
    public void delete(Long educacaoId) {
        EducacaoEntity educacaoEntity = educacaoRepository.findById(educacaoId)
                .orElseThrow(() -> new RuntimeException("Educação não encontrada!"));

        try {
            educacaoRepository.delete(educacaoEntity);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar Educação", e);
        }

    }
}
