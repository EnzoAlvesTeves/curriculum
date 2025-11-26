package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.ExperienciaDTO;
import br.com.senac.mscurriculum.repository.CandidatoRepository;
import br.com.senac.mscurriculum.repository.ExperienciaRepository;
import br.com.senac.mscurriculum.repository.entity.ExperienciaEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExperienciaService {
    final ExperienciaRepository experienciaRepository;
    final CandidatoRepository candidatoRepository;

    public ExperienciaService(
            ExperienciaRepository experienciaRepository,
            CandidatoRepository candidatoRepository
    ) {
        this.experienciaRepository = experienciaRepository;
        this.candidatoRepository = candidatoRepository;
    }

    public ExperienciaDTO create(ExperienciaDTO experienciaDTO) {
			if (!candidatoRepository.existsById(experienciaDTO.getCandidatoId())) {
				throw new RuntimeException("Candidato não encontrado!");
			}

			ExperienciaEntity experienciaEntity = experienciaDTO.toEntity();

			ExperienciaEntity novaExperiencia = experienciaRepository.save(experienciaEntity);

			return new ExperienciaDTO(novaExperiencia);
    }

    public List<ExperienciaDTO> getByCandidatoId(Long candidatoId) {
			List<ExperienciaEntity> experiencias = experienciaRepository.findByCandidatoId(candidatoId);

			return experiencias.stream()
							.map(ExperienciaDTO::new)
							.collect(Collectors.toList());
    }

    public ExperienciaDTO update(ExperienciaDTO experienciaDTO) {
			ExperienciaEntity experienciaEntity = experienciaRepository.findById(experienciaDTO.getId())
							.orElseThrow(() -> new RuntimeException("Experiência não encontrada!"));

			experienciaEntity.setCargo(experienciaDTO.getCargo());
			experienciaEntity.setEmpresa(experienciaDTO.getEmpresa());
			experienciaEntity.setDataInicio(experienciaDTO.getDataInicio());
			experienciaEntity.setDataFim(experienciaDTO.getDataFim());

			ExperienciaEntity experienciaAlterada = experienciaRepository.save(experienciaEntity);

			return new ExperienciaDTO(experienciaAlterada);
    }

    public void delete(Long experienciaId) {
			ExperienciaEntity experienciaEntity = experienciaRepository.findById(experienciaId)
							.orElseThrow(() -> new RuntimeException("Experiência não encontrada!"));

			experienciaRepository.delete(experienciaEntity);
    }
}
