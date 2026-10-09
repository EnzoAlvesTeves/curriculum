package br.com.senac.mscurriculum.integration;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Map;

/**
 * Obtém tokens JWT do Keycloak de teste para serem usados nas chamadas MockMvc.
 */
public class TokenHelper {

    private final KeycloakContainer keycloak;
    private final String realm;
    private final String adminClientId;
    private final String adminClientSecret;
    private final String userClientId;

    public TokenHelper(KeycloakContainer keycloak) {
        this.keycloak          = keycloak;
        this.realm             = "curriculum";
        this.adminClientId     = "curriculum-app-admin";
        this.adminClientSecret = "cJLeqvJ5KE8OUCPIUuIhULXmx3CCdwGO";
        this.userClientId      = "curriculum-app-user";
    }

    /** Cria um usuário no Keycloak e retorna um access token para ele. */
    public String criarUsuarioEObterToken(String email, String senha) {
        criarUsuarioNoKeycloak(email, senha);
        return obterToken(email, senha);
    }

    /** Obtém um token para um usuário já existente. */
    public String obterToken(String username, String senha) {
        RestClient restClient = RestClient.create();
        String tokenUrl = keycloak.getAuthServerUrl()
                + "/realms/" + realm + "/protocol/openid-connect/token";

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "password");
        form.add("client_id", userClientId);
        form.add("username", username);
        form.add("password", senha);

        @SuppressWarnings("unchecked")
        Map<String, Object> resp = restClient.post()
                .uri(tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(Map.class);

        if (resp == null || resp.get("access_token") == null) {
            throw new IllegalStateException("Falha ao obter token para: " + username);
        }
        return String.valueOf(resp.get("access_token"));
    }

    private void criarUsuarioNoKeycloak(String email, String senha) {
        RestClient restClient = RestClient.create();
        String adminToken = obterTokenAdmin(restClient);
        String adminUsersUrl = keycloak.getAuthServerUrl()
                + "/admin/realms/" + realm + "/users";

        Map<String, Object> payload = Map.of(
                "username", email,
                "email", email,
                "firstName", "Test",
                "lastName", "User",
                "emailVerified", true,
                "enabled", true,
                "credentials", java.util.List.of(Map.of(
                        "type", "password",
                        "value", senha,
                        "temporary", false
                ))
        );

        try {
            restClient.post()
                    .uri(adminUsersUrl)
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            // 409 = usuário já existe — ignorar
        }
    }

    private String obterTokenAdmin(RestClient restClient) {
        String tokenUrl = keycloak.getAuthServerUrl()
                + "/realms/" + realm + "/protocol/openid-connect/token";

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", adminClientId);
        form.add("client_secret", adminClientSecret);

        @SuppressWarnings("unchecked")
        Map<String, Object> resp = restClient.post()
                .uri(tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(Map.class);

        return String.valueOf(resp.get("access_token"));
    }
}
