package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.HabilidadeDTO;
import br.com.senac.mscurriculum.repository.CandidatoRepository;
import br.com.senac.mscurriculum.repository.HabilidadeRepository;
import br.com.senac.mscurriculum.repository.entity.HabilidadeEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HabilidadeService {
    final HabilidadeRepository habilidadeRepository;
    final CandidatoRepository candidatoRepository;

    public HabilidadeService(
            HabilidadeRepository habilidadeRepository,
            CandidatoRepository candidatoRepository
    ) {
        this.habilidadeRepository = habilidadeRepository;
        this.candidatoRepository = candidatoRepository;
    }

    public HabilidadeDTO create(HabilidadeDTO habilidadeDTO) {
			if (!candidatoRepository.existsById(habilidadeDTO.getCandidatoId())) {
				throw new RuntimeException("Candidato não encontrado!");
			}
			HabilidadeEntity habilidadeEntity = habilidadeDTO.toEntity();

			HabilidadeEntity novaHabilidade = habilidadeRepository.save(habilidadeEntity);

			return new HabilidadeDTO(novaHabilidade);
    }

    public List<HabilidadeDTO> getByCandidatoId(Long candidatoId) {
			List<HabilidadeEntity> habilidades = habilidadeRepository.findByCandidatoId(candidatoId);

			return habilidades.stream()
							.map(HabilidadeDTO::new)
							.collect(Collectors.toList());
    }

    public HabilidadeDTO update(HabilidadeDTO habilidadeDTO) {
			HabilidadeEntity habilidadeEntity = habilidadeRepository.findById(habilidadeDTO.getId())
							.orElseThrow(() -> new RuntimeException("Habilidade não encontrada!"));
			habilidadeEntity.setDescricao(habilidadeDTO.getDescricao());
			habilidadeEntity.setNivel(habilidadeDTO.getNivel());
			habilidadeEntity.setEspecialidade(habilidadeDTO.getEspecialidade());
			habilidadeEntity.setId(habilidadeDTO.getId());

			HabilidadeEntity habilidadeAlterada = habilidadeRepository.save(habilidadeEntity);

			return new HabilidadeDTO(habilidadeAlterada);
    }

    public void delete(Long habilidadeId) {
			HabilidadeEntity habilidadeEntity = habilidadeRepository.findById(habilidadeId)
							.orElseThrow(() -> new RuntimeException("Habilidade não encontrada!"));

			habilidadeRepository.delete(habilidadeEntity);
    }
}
