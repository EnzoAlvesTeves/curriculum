package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.client.MsUsuarioClient;
import br.com.senac.mscurriculum.dto.UsuarioMeResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioContextService {

    private final MsUsuarioClient msUsuarioClient;

    public UsuarioMeResponse buscarUsuarioMe(String authorization) {
        try {
            log.info("Buscando usuario logado no ms-usuario via /api/usuarios/me");
            UsuarioMeResponse response = msUsuarioClient.buscarUsuarioLogado(authorization);

            log.info("Usuario autenticado: id={}, tipo={}, email={}", response.id(), response.tipo(), response.email());
            return response;
        } catch (FeignException.Unauthorized e) {
            log.warn("Token invalido/expirado ao consultar ms-usuario");

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalido ou expirado");
        } catch (FeignException e) {
            log.error("Falha ao consultar ms-usuario: status={}, msg={}", e.status(), e.getMessage());

            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Falha ao consultar usuario autenticado no ms-usuario");
        }
    }
}
