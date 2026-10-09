package br.com.senac.msvagas.integration;

import br.com.senac.msvagas.client.MsUsuarioClient;
import br.com.senac.msvagas.dto.TipoUsuario;
import br.com.senac.msvagas.dto.UsuarioMeResponse;
import br.com.senac.msvagas.dto.UsuarioResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração do ms-vagas com MySQL real + Keycloak real via Testcontainers.
 *
 * O MsUsuarioClient é mockado pois o ms-vagas não gerencia usuários — ele
 * apenas consulta o ms-usuario para obter dados do usuário autenticado.
 *
 * Fluxos cobertos:
 *  - CRUD de empresas (apenas usuário RH)
 *  - CRUD de vagas (apenas usuário RH)
 *  - Candidaturas (apenas usuário CANDIDATO)
 *  - Autorização: 403 quando tipo errado, 401 sem token
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class VagaIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MsUsuarioClient msUsuarioClient;

    private static TokenHelper tokenHelper;
    private static String tokenRh;
    private static String tokenCandidato;
    private static Long empresaId;
    private static Long vagaId;

    private static final UsuarioMeResponse USUARIO_RH = new UsuarioMeResponse(
            1L, TipoUsuario.RH, "Ana", "RH", "rh@vagas-test.com"
    );

    private static final UsuarioMeResponse USUARIO_CANDIDATO = new UsuarioMeResponse(
            2L, TipoUsuario.CANDIDATO, "Carlos", "Candidato", "candidato@vagas-test.com"
    );

    private static final UsuarioResponse USUARIO_RESPONSE_CANDIDATO = new UsuarioResponse(
            2L, "keycloak-id-fake", "Carlos", "Candidato", "candidato@vagas-test.com",
            "(11) 99999-9999", TipoUsuario.CANDIDATO, LocalDateTime.now(), null
    );

    @BeforeAll
    static void setup() {
        tokenHelper = new TokenHelper(KEYCLOAK);
        tokenRh        = tokenHelper.criarUsuarioEObterToken("rh@vagas-test.com", "Senha@123");
        tokenCandidato = tokenHelper.criarUsuarioEObterToken("candidato@vagas-test.com", "Senha@123");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Empresa
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(1)
    @DisplayName("POST /api/empresas — RH cria empresa com sucesso")
    void deveCriarEmpresa() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_RH);

        String body = """
                {
                    "nome": "Tech Corp",
                    "estado": "SP",
                    "cidade": "São Paulo",
                    "bairro": "Vila Olímpia"
                }
                """;

        String resp = mockMvc.perform(post("/api/empresas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer " + tokenRh))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Tech Corp"))
                .andExpect(jsonPath("$.cidade").value("São Paulo"))
                .andReturn().getResponse().getContentAsString();

        empresaId = objectMapper.readTree(resp).get("id").asLong();
    }

    @Test
    @Order(2)
    @DisplayName("POST /api/empresas — CANDIDATO recebe 403")
    void deveCandidatoReceberForbiddenAoCriarEmpresa() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_CANDIDATO);

        String body = """
                {
                    "nome": "Empresa Não Permitida",
                    "estado": "RJ",
                    "cidade": "Rio de Janeiro",
                    "bairro": "Centro"
                }
                """;

        mockMvc.perform(post("/api/empresas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(3)
    @DisplayName("GET /api/empresas — lista todas as empresas")
    void deveListarEmpresas() throws Exception {
        mockMvc.perform(get("/api/empresas")
                        .header("Authorization", "Bearer " + tokenRh))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[*].nome", hasItem("Tech Corp")));
    }

    @Test
    @Order(4)
    @DisplayName("GET /api/empresas/{id} — busca empresa por ID")
    void deveBuscarEmpresaPorId() throws Exception {
        mockMvc.perform(get("/api/empresas/" + empresaId)
                        .header("Authorization", "Bearer " + tokenRh))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(empresaId))
                .andExpect(jsonPath("$.nome").value("Tech Corp"));
    }

    @Test
    @Order(5)
    @DisplayName("PUT /api/empresas/{id} — RH atualiza empresa")
    void deveAtualizarEmpresa() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_RH);

        String body = """
                {
                    "nome": "Tech Corp Atualizada",
                    "estado": "SP",
                    "cidade": "Campinas",
                    "bairro": "Barão Geraldo"
                }
                """;

        mockMvc.perform(put("/api/empresas/" + empresaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer " + tokenRh))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Tech Corp Atualizada"))
                .andExpect(jsonPath("$.cidade").value("Campinas"));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Vaga
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(6)
    @DisplayName("POST /api/vagas — RH cria vaga com sucesso")
    void deveCriarVaga() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_RH);

        String body = """
                {
                    "titulo": "Desenvolvedor Java",
                    "descricao": "Vaga para desenvolvedor Java pleno",
                    "salario": 8000.00,
                    "beneficios": "VR, VT, Plano de Saúde",
                    "idEmpresa": %d
                }
                """.formatted(empresaId);

        String resp = mockMvc.perform(post("/api/vagas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer " + tokenRh))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.titulo").value("Desenvolvedor Java"))
                .andExpect(jsonPath("$.salario").value(8000.00))
                .andReturn().getResponse().getContentAsString();

        vagaId = objectMapper.readTree(resp).get("id").asLong();
    }

    @Test
    @Order(7)
    @DisplayName("POST /api/vagas — CANDIDATO recebe 403")
    void deveCandidatoReceberForbiddenAoCriarVaga() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_CANDIDATO);

        String body = """
                {
                    "titulo": "Vaga Não Permitida",
                    "descricao": "Candidato não pode criar vaga",
                    "idEmpresa": %d
                }
                """.formatted(empresaId);

        mockMvc.perform(post("/api/vagas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(8)
    @DisplayName("GET /api/vagas/empresa/{id} — lista vagas por empresa")
    void deveListarVagasPorEmpresa() throws Exception {
        mockMvc.perform(get("/api/vagas/empresa/" + empresaId)
                        .header("Authorization", "Bearer " + tokenRh))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[*].titulo", hasItem("Desenvolvedor Java")));
    }

    @Test
    @Order(9)
    @DisplayName("GET /api/vagas/minhas-criadas — RH lista vagas que criou")
    void deveListarMinhasCriadas() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_RH);

        mockMvc.perform(get("/api/vagas/minhas-criadas")
                        .header("Authorization", "Bearer " + tokenRh))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @Order(10)
    @DisplayName("PUT /api/vagas/{id} — RH atualiza vaga")
    void deveAtualizarVaga() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_RH);

        String body = """
                {
                    "titulo": "Desenvolvedor Java Senior",
                    "descricao": "Vaga atualizada para sênior",
                    "salario": 12000.00,
                    "beneficios": "VR, VT, Plano de Saúde, Gympass",
                    "idEmpresa": %d
                }
                """.formatted(empresaId);

        mockMvc.perform(put("/api/vagas/" + vagaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer " + tokenRh))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Desenvolvedor Java Senior"))
                .andExpect(jsonPath("$.salario").value(12000.00));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Candidatura
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(11)
    @DisplayName("POST /api/vagas/{id}/candidaturas — candidato se candidata com sucesso")
    void deveCandidatoSeCandidatar() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_CANDIDATO);

        mockMvc.perform(post("/api/vagas/" + vagaId + "/candidaturas")
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idVaga").value(vagaId))
                .andExpect(jsonPath("$.idUsuario").value(USUARIO_CANDIDATO.id()));
    }

    @Test
    @Order(12)
    @DisplayName("POST /api/vagas/{id}/candidaturas — retorna 409 ao candidatar duas vezes")
    void deveRetornar409CandidaturaDuplicada() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_CANDIDATO);

        mockMvc.perform(post("/api/vagas/" + vagaId + "/candidaturas")
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(13)
    @DisplayName("POST /api/vagas/{id}/candidaturas — RH recebe 403")
    void deveRhReceberForbiddenAoCandidatar() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_RH);

        mockMvc.perform(post("/api/vagas/" + vagaId + "/candidaturas")
                        .header("Authorization", "Bearer " + tokenRh))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(14)
    @DisplayName("GET /api/vagas/minhas-candidaturas — candidato lista suas candidaturas")
    void deveListarMinhasCandidaturas() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_CANDIDATO);

        mockMvc.perform(get("/api/vagas/minhas-candidaturas")
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @Order(15)
    @DisplayName("GET /api/vagas/{id}/candidatos — RH lista candidatos da vaga")
    void deveRhListarCandidatosDaVaga() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_RH);
        Mockito.when(msUsuarioClient.buscarUsuariosPorIds(anyString(), anyString()))
                .thenReturn(List.of(USUARIO_RESPONSE_CANDIDATO));

        mockMvc.perform(get("/api/vagas/" + vagaId + "/candidatos")
                        .header("Authorization", "Bearer " + tokenRh))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email").value(USUARIO_CANDIDATO.email()));
    }

    @Test
    @Order(16)
    @DisplayName("DELETE /api/vagas/{id}/candidaturas — candidato remove candidatura")
    void deveCandidatoRemoverCandidatura() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_CANDIDATO);

        mockMvc.perform(delete("/api/vagas/" + vagaId + "/candidaturas")
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isNoContent());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Limpeza / 401
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(17)
    @DisplayName("GET /api/vagas — retorna 401 sem token")
    void deveRetornar401SemToken() throws Exception {
        mockMvc.perform(get("/api/vagas"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(18)
    @DisplayName("DELETE /api/vagas/{id} — RH exclui vaga")
    void deveRhExcluirVaga() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_RH);

        mockMvc.perform(delete("/api/vagas/" + vagaId)
                        .header("Authorization", "Bearer " + tokenRh))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(19)
    @DisplayName("DELETE /api/empresas/{id} — RH exclui empresa")
    void deveRhExcluirEmpresa() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_RH);

        mockMvc.perform(delete("/api/empresas/" + empresaId)
                        .header("Authorization", "Bearer " + tokenRh))
                .andExpect(status().isNoContent());
    }
}
