package br.com.senac.msusuario.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração do ms-usuario com MySQL real + Keycloak real via Testcontainers.
 *
 * Fluxos cobertos:
 *  - Criar usuário (candidato e RH) — endpoint público
 *  - Login via Keycloak
 *  - GET /me com token válido
 *  - Listar usuários (autenticado)
 *  - Buscar por ID
 *  - Atualizar usuário
 *  - Alterar senha
 *  - Deletar usuário
 *  - 401 em endpoint protegido sem token
 *  - 409 ao cadastrar e-mail duplicado
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsuarioIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Estado compartilhado entre os testes (executados em ordem)
    private static Long usuarioCandidatoId;
    private static Long usuarioRhId;
    private static String tokenCandidato;
    private static String tokenRh;

    private static final String EMAIL_CANDIDATO = "candidato.teste@integration.com";
    private static final String SENHA_CANDIDATO  = "Senha@123";
    private static final String EMAIL_RH         = "rh.teste@integration.com";
    private static final String SENHA_RH         = "Senha@123";

    // ─────────────────────────────────────────────────────────────────────────
    // Criação de usuários (endpoints públicos — sem auth)
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(1)
    @DisplayName("POST /api/usuarios — cria candidato com sucesso")
    void deveCriarCandidato() throws Exception {
        String body = """
                {
                    "nome": "Carlos",
                    "sobrenome": "Candidato",
                    "email": "%s",
                    "telefone": "(11) 91111-1111",
                    "tipo": "CANDIDATO",
                    "senha": "%s"
                }
                """.formatted(EMAIL_CANDIDATO, SENHA_CANDIDATO);

        String resp = mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email").value(EMAIL_CANDIDATO))
                .andExpect(jsonPath("$.tipo").value("CANDIDATO"))
                .andReturn().getResponse().getContentAsString();

        usuarioCandidatoId = objectMapper.readTree(resp).get("id").asLong();
    }

    @Test
    @Order(2)
    @DisplayName("POST /api/usuarios — cria usuário RH com sucesso")
    void deveCriarUsuarioRh() throws Exception {
        String body = """
                {
                    "nome": "Ana",
                    "sobrenome": "RH",
                    "email": "%s",
                    "telefone": "(11) 92222-2222",
                    "tipo": "RH",
                    "senha": "%s"
                }
                """.formatted(EMAIL_RH, SENHA_RH);

        String resp = mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.tipo").value("RH"))
                .andReturn().getResponse().getContentAsString();

        usuarioRhId = objectMapper.readTree(resp).get("id").asLong();
    }

    @Test
    @Order(3)
    @DisplayName("POST /api/usuarios — retorna 409 quando e-mail já está cadastrado")
    void deveRetornar409EmailDuplicado() throws Exception {
        String body = """
                {
                    "nome": "Duplicado",
                    "sobrenome": "Teste",
                    "email": "%s",
                    "tipo": "CANDIDATO",
                    "senha": "Senha@123"
                }
                """.formatted(EMAIL_CANDIDATO);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(4)
    @DisplayName("POST /api/usuarios — retorna 400 para payload inválido")
    void deveRetornar400PayloadInvalido() throws Exception {
        String body = """
                {
                    "nome": "",
                    "sobrenome": "Teste",
                    "email": "nao-e-email",
                    "tipo": "CANDIDATO",
                    "senha": "123"
                }
                """;

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Login / tokens
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(5)
    @DisplayName("POST /api/auth/login — autentica candidato e retorna JWT")
    void deveAutenticarCandidato() throws Exception {
        String body = """
                {
                    "username": "%s",
                    "senha": "%s"
                }
                """.formatted(EMAIL_CANDIDATO, SENHA_CANDIDATO);

        String resp = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andReturn().getResponse().getContentAsString();

        tokenCandidato = objectMapper.readTree(resp).get("accessToken").asText();
    }

    @Test
    @Order(6)
    @DisplayName("POST /api/auth/login — autentica usuário RH e retorna JWT")
    void deveAutenticarRh() throws Exception {
        String body = """
                {
                    "username": "%s",
                    "senha": "%s"
                }
                """.formatted(EMAIL_RH, SENHA_RH);

        String resp = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andReturn().getResponse().getContentAsString();

        tokenRh = objectMapper.readTree(resp).get("accessToken").asText();
    }

    @Test
    @Order(7)
    @DisplayName("POST /api/auth/login — retorna 401 com credenciais erradas")
    void deveRetornar401CredenciaisErradas() throws Exception {
        String body = """
                {
                    "username": "%s",
                    "senha": "senhaErrada"
                }
                """.formatted(EMAIL_CANDIDATO);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Endpoints autenticados
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(8)
    @DisplayName("GET /api/usuarios/me — retorna dados do candidato autenticado")
    void deveBuscarMeCandidato() throws Exception {
        mockMvc.perform(get("/api/usuarios/me")
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(EMAIL_CANDIDATO))
                .andExpect(jsonPath("$.tipo").value("CANDIDATO"));
    }

    @Test
    @Order(9)
    @DisplayName("GET /api/usuarios/me — retorna 401 sem token")
    void deveRetornar401SemToken() throws Exception {
        mockMvc.perform(get("/api/usuarios/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(10)
    @DisplayName("GET /api/usuarios — lista todos os usuários")
    void deveListarUsuarios() throws Exception {
        mockMvc.perform(get("/api/usuarios")
                        .header("Authorization", "Bearer " + tokenRh))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[*].email",
                        hasItems(EMAIL_CANDIDATO, EMAIL_RH)));
    }

    @Test
    @Order(11)
    @DisplayName("GET /api/usuarios/{id} — busca candidato por ID")
    void deveBuscarPorId() throws Exception {
        mockMvc.perform(get("/api/usuarios/" + usuarioCandidatoId)
                        .header("Authorization", "Bearer " + tokenRh))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(usuarioCandidatoId))
                .andExpect(jsonPath("$.email").value(EMAIL_CANDIDATO));
    }

    @Test
    @Order(12)
    @DisplayName("GET /api/usuarios/{id} — retorna 404 para ID inexistente")
    void deveRetornar404IdInexistente() throws Exception {
        mockMvc.perform(get("/api/usuarios/999999")
                        .header("Authorization", "Bearer " + tokenRh))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(13)
    @DisplayName("GET /api/usuarios?ids=1,2 — busca por lista de IDs")
    void deveBuscarPorIds() throws Exception {
        mockMvc.perform(get("/api/usuarios")
                        .param("ids", usuarioCandidatoId.toString() + "," + usuarioRhId.toString())
                        .header("Authorization", "Bearer " + tokenRh))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @Order(14)
    @DisplayName("PUT /api/usuarios/{id} — atualiza dados do candidato")
    void deveAtualizarUsuario() throws Exception {
        String body = """
                {
                    "nome": "Carlos Atualizado",
                    "sobrenome": "Candidato",
                    "telefone": "(11) 99999-9999",
                    "tipo": "CANDIDATO"
                }
                """;

        mockMvc.perform(put("/api/usuarios/" + usuarioCandidatoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Carlos Atualizado"))
                .andExpect(jsonPath("$.telefone").value("(11) 99999-9999"));
    }

    @Test
    @Order(15)
    @DisplayName("PATCH /api/usuarios/me/senha — altera senha do candidato autenticado")
    void deveAlterarSenha() throws Exception {
        String body = """
                {
                    "senhaAtual": "%s",
                    "novaSenha": "NovaSenha@456"
                }
                """.formatted(SENHA_CANDIDATO);

        mockMvc.perform(patch("/api/usuarios/me/senha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isNoContent());

        // Restaura a senha original para não quebrar testes subsequentes
        String bodyRestore = """
                {
                    "senhaAtual": "NovaSenha@456",
                    "novaSenha": "%s"
                }
                """.formatted(SENHA_CANDIDATO);

        // Re-autentica com nova senha para obter token válido
        String loginBody = """
                {
                    "username": "%s",
                    "senha": "NovaSenha@456"
                }
                """.formatted(EMAIL_CANDIDATO);

        String resp = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        String tokenNovoSenha = objectMapper.readTree(resp).get("accessToken").asText();

        mockMvc.perform(patch("/api/usuarios/me/senha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyRestore)
                        .header("Authorization", "Bearer " + tokenNovoSenha))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(16)
    @DisplayName("DELETE /api/usuarios/{id} — remove o usuário RH")
    void deveDeletarUsuarioRh() throws Exception {
        mockMvc.perform(delete("/api/usuarios/" + usuarioRhId)
                        .header("Authorization", "Bearer " + tokenRh))
                .andExpect(status().isNoContent());

        // Confirma que não existe mais
        mockMvc.perform(get("/api/usuarios/" + usuarioRhId)
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(17)
    @DisplayName("DELETE /api/usuarios/{id} — remove o usuário candidato")
    void deveDeletarUsuarioCandidato() throws Exception {
        // Re-autentica o candidato (o token pode ter expirado após as outras operações)
        String loginBody = """
                {
                    "username": "%s",
                    "senha": "%s"
                }
                """.formatted(EMAIL_CANDIDATO, SENHA_CANDIDATO);

        String resp = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andReturn().getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(resp);
        String tokenAtual = jsonNode.has("accessToken")
                ? jsonNode.get("accessToken").asText()
                : tokenCandidato;

        mockMvc.perform(delete("/api/usuarios/" + usuarioCandidatoId)
                        .header("Authorization", "Bearer " + tokenAtual))
                .andExpect(status().isNoContent());
    }
}
