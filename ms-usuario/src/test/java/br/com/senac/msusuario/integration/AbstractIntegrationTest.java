package br.com.senac.msusuario.integration;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Classe base para todos os testes de integração do ms-usuario.
 *
 * Sobe um MySQL real e um Keycloak real em containers Docker.
 * Os containers são compartilhados entre todos os testes que estendem esta classe
 * (Ryuk os derruba ao final da suíte).
 *
 * O realm "curriculum" é importado automaticamente a partir do arquivo
 * src/test/resources/keycloak/realm-export.json (cópia do realm de dev).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public abstract class AbstractIntegrationTest {

    @Container
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("ms_usuario")
            .withUsername("user_usuario")
            .withPassword("admin")
            .withReuse(true);

    @Container
    static final KeycloakContainer KEYCLOAK = new KeycloakContainer("quay.io/keycloak/keycloak:26.0")
            // O arquivo deve estar em src/test/resources/keycloak/realm-export.json
            .withRealmImportFile("keycloak/realm-export.json")
            .withAdminUsername("admin")
            .withAdminPassword("admin")
            .withReuse(true);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // ── Datasource → MySQL container ─────────────────────────────────────
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");

        // Usar Flyway com MySQL real (desabilita H2/ddl-auto do application-test.yml)
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.flyway.locations", () -> "classpath:db/migration");
        registry.add("spring.flyway.baseline-on-migrate", () -> "true");
        registry.add("spring.flyway.baseline-version", () -> "1");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.MySQLDialect");

        // ── Keycloak → apontar para o container ──────────────────────────────
        String baseUrl = KEYCLOAK.getAuthServerUrl();
        String issuerUri = baseUrl + "/realms/curriculum";

        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> issuerUri);
        registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri",
                () -> issuerUri + "/protocol/openid-connect/certs");

        registry.add("keycloak.server-url", () -> baseUrl);
        registry.add("keycloak.realm", () -> "curriculum");
        registry.add("keycloak.admin-client-id", () -> "curriculum-app-admin");
        registry.add("keycloak.admin-client-secret", () -> "cJLeqvJ5KE8OUCPIUuIhULXmx3CCdwGO");
        registry.add("keycloak.user-client-id", () -> "curriculum-app-user");
    }
}
