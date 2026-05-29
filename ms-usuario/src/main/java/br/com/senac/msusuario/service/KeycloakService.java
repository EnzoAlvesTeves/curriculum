package br.com.senac.msusuario.service;

import br.com.senac.msusuario.config.KeycloakProperties;
import br.com.senac.msusuario.dto.AuthTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Service
public class KeycloakService {

    private static final Logger log = LoggerFactory.getLogger(KeycloakService.class);

    private final RestClient restClient;
    private final KeycloakProperties props;

    public KeycloakService(RestClient.Builder builder, KeycloakProperties props) {
        this.restClient = builder.build();
        this.props = props;
    }

    // -------------------------------------------------------------------
    // Login de usuário — grant_type=password, client=curriculum-app-user
    // -------------------------------------------------------------------
    public AuthTokenResponse login(String username, String senha) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", props.getUserClientId());
        form.add("username", username);
        form.add("password", senha);

        log.debug("Login Keycloak - username={} client={}", username, props.getUserClientId());

        Map<?, ?> resp = restClient.post()
                .uri(tokenUrl())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    log.error("Erro ao fazer login - status={}", res.getStatusCode());
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
                })
                .body(Map.class);

        return toTokenResponse(resp);
    }

    // -------------------------------------------------------------------
    // Criar usuário — Admin API com token client_credentials
    // -------------------------------------------------------------------
    public String criarUsuarioKeycloak(String nome, String sobrenome, String email, String senha) {
        String adminToken = obterTokenAdmin();

        Map<String, Object> payload = Map.of(
                "username", email,
                "firstName", nome,
                "lastName", sobrenome,
                "email", email,
                "emailVerified", true,
                "enabled", true,
                "credentials", List.of(Map.of(
                        "type", "password",
                        "value", senha,
                        "temporary", false
                ))
        );

        log.info("Criando usuário no Keycloak - email={}", email);

        URI location = restClient.post()
                .uri(adminUsersUrl())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .onStatus(s -> s.value() == 409, (req, res) -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuário já existe no Keycloak");
                })
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    log.error("Erro ao criar usuário - status={}", res.getStatusCode());
                    throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Falha ao criar usuário no Keycloak");
                })
                .toBodilessEntity()
                .getHeaders()
                .getLocation();

        if (location == null) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Keycloak não retornou ID do usuário");
        }

        String path = location.getPath();
        String keycloakId = path.substring(path.lastIndexOf('/') + 1);

        log.debug("Usuário criado no Keycloak - id={}", keycloakId);
        return keycloakId;
    }

    // -------------------------------------------------------------------
    // Deletar usuário no Keycloak
    // -------------------------------------------------------------------
    public void deletarUsuarioKeycloak(String keycloakUserId) {
        String adminToken = obterTokenAdmin();

        log.debug("Deletando usuário no Keycloak - keycloakUserId={}", keycloakUserId);

        restClient.delete()
                .uri(adminUsersUrl() + "/" + keycloakUserId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    log.error("Erro ao deletar usuário - status={}", res.getStatusCode());
                    throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                            "Falha ao deletar usuário no Keycloak");
                })
                .toBodilessEntity();
    }

    // -------------------------------------------------------------------
    // Obter token de admin — grant_type=client_credentials
    // -------------------------------------------------------------------
    private String obterTokenAdmin() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", props.getAdminClientId());
        form.add("client_secret", props.getAdminClientSecret());

        log.debug("Obtendo token admin - clientId={}", props.getAdminClientId());

        Map<?, ?> resp = restClient.post()
                .uri(tokenUrl())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    log.error("Falha ao obter token admin - status={}", res.getStatusCode());
                    throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                            "Falha ao autenticar admin no Keycloak");
                })
                .body(Map.class);

        if (resp == null || resp.get("access_token") == null) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Token admin não encontrado na resposta do Keycloak");
        }

        return String.valueOf(resp.get("access_token"));
    }

    // -------------------------------------------------------------------
    // URLs
    // -------------------------------------------------------------------
    private String tokenUrl() {
        return props.getServerUrl() + "/realms/" + props.getRealm()
                + "/protocol/openid-connect/token";
    }

    private String adminUsersUrl() {
        return props.getServerUrl() + "/admin/realms/" + props.getRealm() + "/users";
    }

    // -------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    private AuthTokenResponse toTokenResponse(Map<?, ?> map) {
        if (map == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Resposta vazia do Keycloak");
        }
        return new AuthTokenResponse(
                str(map.get("access_token")),
                str(map.get("refresh_token")),
                toLong(map.get("expires_in")),
                toLong(map.get("refresh_expires_in")),
                str(map.get("token_type")),
                str(map.get("scope"))
        );
    }

    private static String str(Object o) {
        return o == null ? null : String.valueOf(o);
    }

    private static Long toLong(Object o) {
        return o instanceof Number n ? n.longValue() : null;
    }
}

