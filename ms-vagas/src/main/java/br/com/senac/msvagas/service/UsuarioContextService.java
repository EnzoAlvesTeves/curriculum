package br.com.senac.msvagas.service;

import br.com.senac.msvagas.client.MsUsuarioClient;
import br.com.senac.msvagas.dto.UsuarioMeResponse;
import br.com.senac.msvagas.dto.UsuarioResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    public List<UsuarioResponse> buscarUsuariosPorIds(List<Long> ids, String authorization) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }

        String idsParam = ids.stream()
                .map(String::valueOf)
                .distinct()
                .reduce((atual, proximo) -> atual + "," + proximo)
                .orElse("");

        try {
            log.info("Buscando {} usuarios no ms-usuario via /api/usuarios?ids=...", ids.size());
            return msUsuarioClient.buscarUsuariosPorIds(authorization, idsParam);
        } catch (FeignException.Unauthorized e) {
            log.warn("Token invalido/expirado ao consultar usuarios por ids no ms-usuario");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalido ou expirado");
        } catch (FeignException.NotFound e) {
            log.error("Um ou mais usuarios nao foram encontrados no ms-usuario para ids={}", idsParam);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Nem todos os candidatos foram encontrados no ms-usuario");
        } catch (FeignException e) {
            log.error("Falha ao consultar usuarios por ids no ms-usuario: status={}, msg={}", e.status(), e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Falha ao consultar candidatos no ms-usuario");
        }
    }
}

