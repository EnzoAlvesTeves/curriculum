package br.com.senac.msusuario.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeycloakProperties {

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    /** Client com client_credentials para gerenciar usuários via Admin API */
    @Value("${keycloak.admin-client-id}")
    private String adminClientId;

    @Value("${keycloak.admin-client-secret}")
    private String adminClientSecret;

    /** Client público para autenticar usuários (password grant) */
    @Value("${keycloak.user-client-id}")
    private String userClientId;

    public String getServerUrl() { return serverUrl; }
    public String getRealm()     { return realm; }
    public String getAdminClientId()     { return adminClientId; }
    public String getAdminClientSecret() { return adminClientSecret; }
    public String getUserClientId()      { return userClientId; }
}

