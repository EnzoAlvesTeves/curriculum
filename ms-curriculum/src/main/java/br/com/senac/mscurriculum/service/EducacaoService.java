package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.EducacaoDTO;
import br.com.senac.mscurriculum.repository.CandidatoRepository;
import br.com.senac.mscurriculum.repository.EducacaoRepository;
import br.com.senac.mscurriculum.repository.entity.EducacaoEntity;
import org.springframework.stereotype.Service;

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

    public EducacaoDTO create(EducacaoDTO educacaoDTO) {
			EducacaoEntity educacaoEntity = educacaoDTO.toEntity();

			EducacaoEntity novaEducacao = educacaoRepository.save(educacaoEntity);

			return new EducacaoDTO(novaEducacao);
    }

    public List<EducacaoDTO> getByCandidatoId(Long candidatoId) {
			List<EducacaoEntity> educacoes = educacaoRepository.findByCandidatoId(candidatoId);
			return educacoes.stream()
							.map(EducacaoDTO::new)
							.toList();
    }

    public EducacaoDTO update(EducacaoDTO educacaoDTO) {
			EducacaoEntity educacaoEntity = educacaoRepository.findById(educacaoDTO.getId())
							.orElseThrow(() -> new RuntimeException("Educação não encontrada!"));

			educacaoEntity.setInstituicao(educacaoDTO.getInstituicao());
			educacaoEntity.setCurso(educacaoDTO.getCurso());
			educacaoEntity.setDataInicio(educacaoDTO.getDataInicio());
			educacaoEntity.setDataFim(educacaoDTO.getDataFim());
			educacaoEntity.setGrau(educacaoDTO.getGrau());

			EducacaoEntity educacaoAlterada = educacaoRepository.save(educacaoEntity);

			return new EducacaoDTO(educacaoAlterada);
    }

    public void delete(Long educacaoId) {
			EducacaoEntity educacaoEntity = educacaoRepository.findById(educacaoId)
							.orElseThrow(() -> new RuntimeException("Educação não encontrada!"));

			educacaoRepository.delete(educacaoEntity);
    }
}
