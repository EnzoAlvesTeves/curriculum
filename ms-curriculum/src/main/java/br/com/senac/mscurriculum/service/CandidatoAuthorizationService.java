package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.TipoUsuario;
import br.com.senac.mscurriculum.dto.UsuarioMeResponse;
import br.com.senac.mscurriculum.repository.CandidatoRepository;
import br.com.senac.mscurriculum.repository.entity.CandidatoEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CandidatoAuthorizationService {

    private final UsuarioContextService usuarioContextService;
    private final CandidatoRepository candidatoRepository;

    public UsuarioMeResponse usuarioAtual(Jwt jwt) {
        if (jwt == null) {
            log.warn("Tentativa de acesso sem token JWT");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
        }

        log.debug("Buscando usuário atual no contexto autenticado");
        return usuarioContextService.buscarUsuarioMe("Bearer " + jwt.getTokenValue());
    }

    public UsuarioMeResponse validarTipoCandidato(Jwt jwt) {
        UsuarioMeResponse usuario = usuarioAtual(jwt);
        if (usuario.tipo() == null || usuario.tipo() != TipoUsuario.CANDIDATO) {
            log.warn("Acesso negado: usuário ID={} com tipo={} tentou executar operação restrita a CANDIDATO", usuario.id(), usuario.tipo());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Somente usuário do tipo CANDIDATO pode executar esta operação");
        }

        log.debug("Usuário autenticado validado como CANDIDATO. usuarioId={}", usuario.id());
        return usuario;
    }

    public CandidatoEntity validarAcessoDoUsuario(Long candidatoId, Jwt jwt) {
        log.info("Validando acesso do usuário ao candidatoId={}", candidatoId);
        UsuarioMeResponse usuario = validarTipoCandidato(jwt);
        CandidatoEntity candidato = candidatoRepository.findById(candidatoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidato não encontrado"));

        if (!candidato.getIdUsuario().equals(usuario.id())) {
            log.warn("Acesso negado: usuarioId={} tentou acessar candidatoId={} pertencente ao usuarioId={}",
                    usuario.id(), candidatoId, candidato.getIdUsuario());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não pode editar este candidato");
        }

        log.debug("Acesso autorizado ao candidatoId={} para usuarioId={}", candidatoId, usuario.id());
        return candidato;
    }
}

