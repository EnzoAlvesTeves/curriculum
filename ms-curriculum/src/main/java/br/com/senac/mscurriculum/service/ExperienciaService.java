package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.ExperienciaDTO;
import br.com.senac.mscurriculum.repository.CandidatoRepository;
import br.com.senac.mscurriculum.repository.ExperienciaRepository;
import br.com.senac.mscurriculum.repository.entity.CandidatoEntity;
import br.com.senac.mscurriculum.repository.entity.ExperienciaEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public ExperienciaDTO create(ExperienciaDTO experienciaDTO) {
        CandidatoEntity candidatoEntity = candidatoRepository.findById(experienciaDTO.getCandidatoId())
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado!"));
        try {
            ExperienciaEntity experienciaEntity = experienciaDTO.toEntity();
            experienciaEntity.setCandidato(candidatoEntity);

            ExperienciaEntity novaExperiencia = experienciaRepository.save(experienciaEntity);

            return new ExperienciaDTO(novaExperiencia);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar experiencia!", e);
        }
    }

    public List<ExperienciaDTO> getByCandidatoId(Long candidatoId) {
        List<ExperienciaEntity> experiencias = experienciaRepository.findByCandidatoId(candidatoId);

        return experiencias.stream()
                .map(ExperienciaDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ExperienciaDTO update(ExperienciaDTO experienciaDTO) {
        ExperienciaEntity experienciaEntity = experienciaRepository.findById(experienciaDTO.getId())
                .orElseThrow(() -> new RuntimeException("Experiência não encontrada!"));
        try {
            experienciaEntity.setCargo(experienciaDTO.getCargo());
            experienciaEntity.setEmpresa(experienciaDTO.getEmpresa());
            experienciaEntity.setDataInicio(experienciaDTO.getDataInicio());
            experienciaEntity.setDataFim(experienciaDTO.getDataFim());

            ExperienciaEntity experienciaAlterada = experienciaRepository.save(experienciaEntity);

            return new ExperienciaDTO(experienciaAlterada);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao editar experiencia!", e);
        }
    }

    @Transactional
    public void delete(Long experienciaId) {
        ExperienciaEntity experienciaEntity = experienciaRepository.findById(experienciaId)
                .orElseThrow(() -> new RuntimeException("Experiência não encontrada!"));

        try {
            experienciaRepository.delete(experienciaEntity);
        }  catch (Exception e) {
            throw new RuntimeException("Erro ao deletar experiencia!", e);
        }
    }
}
