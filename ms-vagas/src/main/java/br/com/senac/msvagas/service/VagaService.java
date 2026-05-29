package br.com.senac.msvagas.service;

import br.com.senac.msvagas.dto.*;
import br.com.senac.msvagas.repository.CandidatoVagaRepository;
import br.com.senac.msvagas.repository.EmpresaRepository;
import br.com.senac.msvagas.repository.VagaRepository;
import br.com.senac.msvagas.repository.entity.CandidatoVagaEntity;
import br.com.senac.msvagas.repository.entity.VagaEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VagaService {

    private final VagaRepository vagaRepository;
    private final EmpresaRepository empresaRepository;
    private final CandidatoVagaRepository candidatoVagaRepository;
    private final UsuarioContextService usuarioContextService;

    @Transactional
    public VagaResponse criar(CreateVagaRequest request, String authorization) {
        UsuarioMeResponse usuario = usuarioContextService.buscarUsuarioMe(authorization);
        validarRh(usuario);

        if (!empresaRepository.existsById(request.idEmpresa())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa nao encontrada");
        }

        VagaEntity entity = new VagaEntity();
        entity.setTitulo(request.titulo());
        entity.setDescricao(request.descricao());
        entity.setSalario(request.salario());
        entity.setBeneficios(request.beneficios());
        entity.setIdEmpresa(request.idEmpresa());
        entity.setCreatedBy(usuario.id());

        VagaEntity salva = vagaRepository.save(entity);

        log.info("Vaga criada: id={}, empresa={}, createdBy={}", salva.getId(), salva.getIdEmpresa(), salva.getCreatedBy());
        return toResponse(salva);
    }

    @Transactional
    public VagaResponse atualizar(Long id, UpdateVagaRequest request, String authorization) {
        UsuarioMeResponse usuario = usuarioContextService.buscarUsuarioMe(authorization);
        validarRh(usuario);

        VagaEntity entity = buscarVaga(id);

        if (!empresaRepository.existsById(request.idEmpresa())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa nao encontrada");
        }

        entity.setTitulo(request.titulo());
        entity.setDescricao(request.descricao());
        entity.setSalario(request.salario());
        entity.setBeneficios(request.beneficios());
        entity.setIdEmpresa(request.idEmpresa());

        VagaEntity salva = vagaRepository.save(entity);

        log.info("Vaga atualizada: id={}", salva.getId());
        return toResponse(salva);
    }

    @Transactional
    public void deletar(Long id, String authorization) {
        UsuarioMeResponse usuario = usuarioContextService.buscarUsuarioMe(authorization);
        validarRh(usuario);

        VagaEntity entity = buscarVaga(id);
        vagaRepository.delete(entity);
        log.info("Vaga removida: id={}", id);
    }

    @Transactional(readOnly = true)
    public List<VagaResponse> buscarPorEmpresa(Long idEmpresa) {
        return vagaRepository.findByIdEmpresa(idEmpresa)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VagaResponse> buscarMinhasCriadas(String authorization) {
        UsuarioMeResponse usuario = usuarioContextService.buscarUsuarioMe(authorization);

        return vagaRepository.findByCreatedBy(usuario.id())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public CandidaturaResponse candidatar(Long idVaga, String authorization) {
        UsuarioMeResponse usuario = usuarioContextService.buscarUsuarioMe(authorization);
        validarCandidato(usuario);

        buscarVaga(idVaga);

        if (candidatoVagaRepository.existsByIdUsuarioAndIdVaga(usuario.id(), idVaga)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuario ja se candidatou para essa vaga");
        }

        CandidatoVagaEntity candidatura = new CandidatoVagaEntity();
        candidatura.setIdUsuario(usuario.id());
        candidatura.setIdVaga(idVaga);

        CandidatoVagaEntity salva = candidatoVagaRepository.save(candidatura);

        log.info("Candidatura criada: id={}, vaga={}, usuario={}", salva.getId(), idVaga, usuario.id());
        return toResponse(salva);
    }

    @Transactional
    public void removerCandidatura(Long idVaga, String authorization) {
        UsuarioMeResponse usuario = usuarioContextService.buscarUsuarioMe(authorization);
        validarCandidato(usuario);

        CandidatoVagaEntity entity = candidatoVagaRepository
                .findByIdUsuarioAndIdVaga(usuario.id(), idVaga)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidatura nao encontrada"));

        candidatoVagaRepository.delete(entity);
        log.info("Candidatura removida: id={}, vaga={}, usuario={}", entity.getId(), idVaga, usuario.id());
    }

    @Transactional(readOnly = true)
    public List<VagaResponse> buscarMinhasCandidaturas(String authorization) {
        UsuarioMeResponse usuario = usuarioContextService.buscarUsuarioMe(authorization);

        List<Long> vagasIds = candidatoVagaRepository.findByIdUsuario(usuario.id())
                .stream()
                .map(CandidatoVagaEntity::getIdVaga)
                .toList();

        if (vagasIds.isEmpty()) {
            return List.of();
        }

        return vagaRepository.findAllById(vagasIds)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private VagaEntity buscarVaga(Long id) {
        return vagaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vaga nao encontrada"));
    }

    private void validarRh(UsuarioMeResponse usuario) {
        if (usuario.tipo() == null || !usuario.tipo().ehRh()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas usuarios do tipo RH podem cadastrar, alterar e deletar vagas");
        }
    }

    private void validarCandidato(UsuarioMeResponse usuario) {
        if (usuario.tipo() == null || !usuario.tipo().ehCandidato()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Apenas usuarios do tipo CANDIDATO podem se candidatar ou remover candidatura");
        }
    }

    private VagaResponse toResponse(VagaEntity entity) {
        return new VagaResponse(
                entity.getId(),
                entity.getTitulo(),
                entity.getDescricao(),
                entity.getSalario(),
                entity.getBeneficios(),
                entity.getIdEmpresa(),
                entity.getCreatedBy(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private CandidaturaResponse toResponse(CandidatoVagaEntity entity) {
        return new CandidaturaResponse(
                entity.getId(),
                entity.getIdUsuario(),
                entity.getIdVaga(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}

