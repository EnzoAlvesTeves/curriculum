package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.ExperienciaDTO;
import br.com.senac.mscurriculum.mapper.ExperienciaMapper;
import br.com.senac.mscurriculum.repository.ExperienciaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExperienciaService {
    private final ExperienciaRepository experienciaRepository;
    private final CandidatoAuthorizationService candidatoAuthorizationService;

    public ExperienciaDTO cadastrar(Long candidatoId, ExperienciaDTO dto, Jwt jwt) {
        log.info("Iniciando cadastro de experiência para candidatoId={}", candidatoId);
        candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt);
        return cadastrar(candidatoId, dto);
    }

    public ExperienciaDTO cadastrar(Long candidatoId, ExperienciaDTO dto) {
        log.debug("Persistindo experiência para candidatoId={} cargo={} empresa={}", candidatoId, dto.getCargo(), dto.getEmpresa());
        var entity = ExperienciaMapper.toEntity(dto, candidatoId);
        var experiencia = experienciaRepository.save(entity);

        log.info("Experiência cadastrada com sucesso. candidatoId={} experienciaId={}", candidatoId, experiencia.getId());
        return ExperienciaMapper.toDTO(experiencia);
    }

    public ExperienciaDTO alterar(Long candidatoId, Long experienciaId, ExperienciaDTO dto, Jwt jwt) {
        log.info("Iniciando alteração de experiência. candidatoId={} experienciaId={}", candidatoId, experienciaId);
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
        log.info("Experiência alterada com sucesso. candidatoId={} experienciaId={}", candidatoId, experienciaId);
        return ExperienciaMapper.toDTO(experienciaAtualizada);
    }

    public void deletar(Long candidatoId, Long experienciaId, Jwt jwt) {
        log.info("Iniciando exclusão de experiência. candidatoId={} experienciaId={}", candidatoId, experienciaId);
        candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt);
        var experiencia = experienciaRepository.findById(experienciaId)
                .orElseThrow(() -> new RuntimeException("Experiência não encontrada para o ID: " + experienciaId));

        if (!experiencia.getIdCandidato().equals(candidatoId)) {
            throw new RuntimeException("Experiência não pertence ao candidato ID: " + candidatoId);
        }

        experienciaRepository.delete(experiencia);
        log.info("Experiência excluída com sucesso. candidatoId={} experienciaId={}", candidatoId, experienciaId);
    }

    public List<ExperienciaDTO> buscarPorCandidato(Long candidatoId) {
        log.debug("Buscando experiências do candidatoId={}", candidatoId);
        var experiencias = experienciaRepository.findByIdCandidato(candidatoId);

        log.debug("Encontradas {} experiência(s) para candidatoId={}", experiencias.size(), candidatoId);
        return experiencias.stream()
                .map(ExperienciaMapper::toDTO)
                .toList();
    }
}

