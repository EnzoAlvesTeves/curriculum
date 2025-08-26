package br.com.senac.curriculum.service;

import br.com.senac.curriculum.dto.EducacaoDTO;
import br.com.senac.curriculum.repository.candidato.CandidatoEntity;
import br.com.senac.curriculum.repository.candidato.CandidatoRepository;
import br.com.senac.curriculum.repository.educacao.EducacaoEntity;
import br.com.senac.curriculum.repository.educacao.EducacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EducacaoService {

    @Autowired
    private EducacaoRepository educacaoRepository;

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Transactional
    public EducacaoDTO cadastrar(Long candidatoId, EducacaoDTO educacaoDTO) {
        // buscar o candidato
        CandidatoEntity candidato = candidatoRepository.findById(candidatoId)
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado!"));

        // cadastrar a educacao para o candidato
        EducacaoEntity educacao = new EducacaoEntity();
        educacao.setGrau(educacaoDTO.getGrau());
        educacao.setDataInicio(educacaoDTO.getDataInicio());
        educacao.setDataFim(educacaoDTO.getDataFim());
        educacao.setInstituicao(educacaoDTO.getInstituicao());
        educacao.setCurso(educacaoDTO.getCurso());
        educacao.setCandidato(candidato);

        educacao = educacaoRepository.save(educacao);

        return new EducacaoDTO(educacao);
    }

    @Transactional
    public EducacaoDTO editar(Long educacaoId, EducacaoDTO educacaoDTO) {
        EducacaoEntity educacao = educacaoRepository.findById(educacaoId)
                .orElseThrow(() -> new RuntimeException("Educação não encontrada!"));

        educacao.setGrau(educacaoDTO.getGrau());
        educacao.setDataInicio(educacaoDTO.getDataInicio());
        educacao.setDataFim(educacaoDTO.getDataFim());
        educacao.setInstituicao(educacaoDTO.getInstituicao());
        educacao.setCurso(educacaoDTO.getCurso());

        educacao = educacaoRepository.save(educacao);

        return new EducacaoDTO(educacao);
    }

    @Transactional
    public void deletar(Long educacaoId) {
        EducacaoEntity educacao = educacaoRepository.findById(educacaoId)
                .orElseThrow(() -> new RuntimeException("Educação não encontrada!"));

        educacaoRepository.delete(educacao);
    }
}
