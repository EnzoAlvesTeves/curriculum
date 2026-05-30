package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.TipoUsuario;
import br.com.senac.mscurriculum.dto.UsuarioMeResponse;
import br.com.senac.mscurriculum.repository.CandidatoRepository;
import br.com.senac.mscurriculum.repository.entity.CandidatoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CandidatoAuthorizationService {

    private final UsuarioContextService usuarioContextService;
    private final CandidatoRepository candidatoRepository;

    public UsuarioMeResponse usuarioAtual(Jwt jwt) {
        if (jwt == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
        }

        return usuarioContextService.buscarUsuarioMe("Bearer " + jwt.getTokenValue());
    }

    public UsuarioMeResponse validarTipoCandidato(Jwt jwt) {
        UsuarioMeResponse usuario = usuarioAtual(jwt);
        if (usuario.tipo() == null || usuario.tipo() != TipoUsuario.CANDIDATO) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Somente usuário do tipo CANDIDATO pode executar esta operação");
        }

        return usuario;
    }

    public CandidatoEntity validarAcessoDoUsuario(Long candidatoId, Jwt jwt) {
        UsuarioMeResponse usuario = validarTipoCandidato(jwt);
        CandidatoEntity candidato = candidatoRepository.findById(candidatoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidato não encontrado"));

        if (!candidato.getIdUsuario().equals(usuario.id())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não pode editar este candidato");
        }
        return candidato;
    }
}

