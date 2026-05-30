package br.com.senac.msusuario.service;

import br.com.senac.msusuario.dto.CreateUsuarioRequest;
import br.com.senac.msusuario.dto.AlterarSenhaPorUsernameRequest;
import br.com.senac.msusuario.dto.AlterarSenhaRequest;
import br.com.senac.msusuario.dto.UpdateUsuarioRequest;
import br.com.senac.msusuario.dto.UsuarioResponse;
import br.com.senac.msusuario.repository.UsuarioRepository;
import br.com.senac.msusuario.repository.entity.UsuarioEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final KeycloakService keycloakService;

    // CREATE
    @Transactional
    public UsuarioResponse criar(CreateUsuarioRequest req) {
        repository.findByEmail(req.email()).ifPresent(u -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        });

        String keycloakId = keycloakService.criarUsuarioKeycloak(
                req.nome(),
                req.sobrenome(),
                req.email(),
                req.senha()
        );

        UsuarioEntity entity = new UsuarioEntity();
        entity.setKeycloakUserId(keycloakId);
        entity.setNome(req.nome());
        entity.setSobrenome(req.sobrenome());
        entity.setEmail(req.email());
        entity.setTelefone(req.telefone());
        entity.setTipo(req.tipo());

        return toResponse(repository.save(entity));
    }

    // READ ALL
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listar() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    // READ BY ID
    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {
        return toResponse(encontrarPorId(id));
    }

    // READ LOGGED USER
    @Transactional(readOnly = true)
    public UsuarioResponse buscarMe(Jwt jwt) {
        String keycloakUserId = jwt.getSubject();

        return repository.findByKeycloakUserId(keycloakUserId)
                .map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    // UPDATE
    @Transactional
    public UsuarioResponse atualizar(Long id, UpdateUsuarioRequest req) {
        UsuarioEntity entity = encontrarPorId(id);
        entity.setNome(req.nome());
        entity.setSobrenome(req.sobrenome());
        entity.setTelefone(req.telefone());
        entity.setTipo(req.tipo());
        entity.setUpdatedAt(LocalDateTime.now());
        return toResponse(repository.save(entity));
    }

    // CHANGE PASSWORD (AUTHENTICATED USER)
    @Transactional(readOnly = true)
    public void alterarMinhaSenha(Jwt jwt, AlterarSenhaRequest req) {
        String keycloakUserId = jwt.getSubject();

        UsuarioEntity entity = repository.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        try {
            keycloakService.login(entity.getEmail(), req.senhaAtual());
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha atual inválida");
        }

        keycloakService.alterarSenhaKeycloak(entity.getKeycloakUserId(), req.novaSenha());
    }

    // CHANGE PASSWORD BY USERNAME
    @Transactional(readOnly = true)
    public void alterarSenhaPorUsername(AlterarSenhaPorUsernameRequest req) {
        UsuarioEntity entity = repository.findByEmail(req.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        keycloakService.alterarSenhaKeycloak(entity.getKeycloakUserId(), req.novaSenha());
    }

    // DELETE
    @Transactional
    public void deletar(Long id) {
        UsuarioEntity entity = encontrarPorId(id);
        keycloakService.deletarUsuarioKeycloak(entity.getKeycloakUserId());
        repository.delete(entity);
    }

    // HELPERS
    private UsuarioEntity encontrarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Usuário não encontrado"));
    }

    private UsuarioResponse toResponse(UsuarioEntity e) {
        return new UsuarioResponse(
                e.getId(),
                e.getKeycloakUserId(),
                e.getNome(),
                e.getSobrenome(),
                e.getEmail(),
                e.getTelefone(),
                e.getTipo(),
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }
}

