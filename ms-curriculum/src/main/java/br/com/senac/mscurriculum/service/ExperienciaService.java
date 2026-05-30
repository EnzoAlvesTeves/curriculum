package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.ExperienciaDTO;
import br.com.senac.mscurriculum.mapper.ExperienciaMapper;
import br.com.senac.mscurriculum.repository.ExperienciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExperienciaService {
    private final ExperienciaRepository experienciaRepository;
    private final CandidatoAuthorizationService candidatoAuthorizationService;

    public ExperienciaDTO cadastrar(Long candidatoId, ExperienciaDTO dto, Jwt jwt) {
        candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt);
        return cadastrar(candidatoId, dto);
    }

    public ExperienciaDTO cadastrar(Long candidatoId, ExperienciaDTO dto) {
        var entity = ExperienciaMapper.toEntity(dto, candidatoId);
        var experiencia = experienciaRepository.save(entity);

        return ExperienciaMapper.toDTO(experiencia);
    }

    public ExperienciaDTO alterar(Long candidatoId, Long experienciaId, ExperienciaDTO dto, Jwt jwt) {
        candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt);
        var experiencia = experienciaRepository.findById(experienciaId)
                .orElseThrow(() -> new RuntimeException("Experiência não encontrada para o ID: " + experienciaId));

        if (!experiencia.getIdCandidato().equals(candidatoId)) {
            throw new RuntimeException("Experiência não pertence ao candidato ID: " + candidatoId);
        }

        experiencia.setCargo(dto.getCargo());
        experiencia.setEmpresa(dto.getEmpresa());
        experiencia.setResumo(dto.getResumo());
        experiencia.setDataInicio(dto.getDataInicio());
        experiencia.setDataFim(dto.getDataFim());

        var experienciaAtualizada = experienciaRepository.save(experiencia);
        return ExperienciaMapper.toDTO(experienciaAtualizada);
    }

    public void deletar(Long candidatoId, Long experienciaId, Jwt jwt) {
        candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt);
        var experiencia = experienciaRepository.findById(experienciaId)
                .orElseThrow(() -> new RuntimeException("Experiência não encontrada para o ID: " + experienciaId));

        if (!experiencia.getIdCandidato().equals(candidatoId)) {
            throw new RuntimeException("Experiência não pertence ao candidato ID: " + candidatoId);
        }

        experienciaRepository.delete(experiencia);
    }

    public List<ExperienciaDTO> buscarPorCandidato(Long candidatoId) {
        var experiencias = experienciaRepository.findByIdCandidato(candidatoId);

        return experiencias.stream()
                .map(ExperienciaMapper::toDTO)
                .toList();
    }
}

