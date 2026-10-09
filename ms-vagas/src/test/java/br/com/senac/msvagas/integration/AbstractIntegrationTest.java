package br.com.senac.msvagas.integration;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Classe base para os testes de integração do ms-vagas.
 *
 * Infraestrutura real:
 *  - MySQL 8.0 via Testcontainers (com Flyway rodando as migrations reais)
 *  - Keycloak 26.0 via Testcontainers (realm curriculum importado)
 *
 * O MsUsuarioClient (Feign) é mockado com @MockBean em cada teste,
 * pois o ms-vagas não gerencia usuários diretamente.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public abstract class AbstractIntegrationTest {

    @Container
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("ms_vagas")
            .withUsername("user_vagas")
            .withPassword("admin")
            .withReuse(true);

    @Container
    static final KeycloakContainer KEYCLOAK = new KeycloakContainer("quay.io/keycloak/keycloak:26.0")
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

        // Flyway com MySQL real
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.flyway.locations", () -> "classpath:db/migration");
        registry.add("spring.flyway.clean-disabled", () -> "false");
        registry.add("spring.flyway.baseline-on-migrate", () -> "true");
        registry.add("spring.flyway.baseline-version", () -> "1");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.MySQLDialect");

        // ── Keycloak → apontar para o container ──────────────────────────────
        String baseUrl  = KEYCLOAK.getAuthServerUrl();
        String issuerUri = baseUrl + "/realms/curriculum";

        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> issuerUri);
        registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri",
                () -> issuerUri + "/protocol/openid-connect/certs");

        // Feign para ms-usuario — URL fictícia; cada teste usa @MockBean no MsUsuarioClient
        registry.add("feign.ms-usuario.url", () -> "http://localhost:9999");
    }
}
