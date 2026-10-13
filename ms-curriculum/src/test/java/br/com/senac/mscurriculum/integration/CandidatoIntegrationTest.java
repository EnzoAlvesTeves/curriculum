package br.com.senac.mscurriculum.integration;

import br.com.senac.mscurriculum.client.MsUsuarioClient;
import br.com.senac.mscurriculum.dto.TipoUsuario;
import br.com.senac.mscurriculum.dto.UsuarioMeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de integração do ms-curriculum com MySQL real + Keycloak real via Testcontainers.
 *
 * O MsUsuarioClient (Feign) é mockado pois o ms-curriculum delega ao ms-usuario
 * apenas para obter os dados do usuário autenticado.
 *
 * Fluxos cobertos:
 *  - Cadastrar candidato (apenas CANDIDATO)
 *  - Buscar candidato por ID e pelo /me
 *  - Alterar candidato
 *  - Gerenciar endereço, educações, experiências, habilidades
 *  - Autorização: 403 quando tipo errado (RH tentando cadastrar candidato)
 *  - 401 sem token
 *  - 409 ao duplicar cadastro de candidato
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class CandidatoIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MsUsuarioClient msUsuarioClient;

    private static TokenHelper tokenHelper;
    private static String tokenCandidato;
    private static String tokenRh;

    private static Long candidatoId;
    private static Long educacaoId;
    private static Long experienciaId;
    private static Long habilidadeId;

    private static final UsuarioMeResponse USUARIO_CANDIDATO = new UsuarioMeResponse(
            100L, TipoUsuario.CANDIDATO, "Carlos", "Curriculo", "candidato@curriculum-test.com"
    );

    private static final UsuarioMeResponse USUARIO_RH = new UsuarioMeResponse(
            101L, TipoUsuario.RH, "Ana", "RH", "rh@curriculum-test.com"
    );

    @BeforeAll
    static void setup() {
        tokenHelper    = new TokenHelper(KEYCLOAK);
        tokenCandidato = tokenHelper.criarUsuarioEObterToken("candidato@curriculum-test.com", "Senha@123");
        tokenRh        = tokenHelper.criarUsuarioEObterToken("rh@curriculum-test.com", "Senha@123");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Candidato
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(1)
    @DisplayName("POST /api/candidatos — CANDIDATO cadastra perfil com sucesso")
    void deveCadastrarCandidato() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_CANDIDATO);

        String body = """
                {
                    "nome": "Carlos Curriculo",
                    "email": "candidato@curriculum-test.com",
                    "sexo": "MASCULINO",
                    "telefone": "(11) 91234-5678",
                    "dataNascimento": "1995-06-15",
                    "resumoProfissional": "Desenvolvedor Java Pleno com 5 anos de experiência"
                }
                """;

        String resp = mockMvc.perform(post("/api/candidatos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Carlos Curriculo"))
                .andExpect(jsonPath("$.idUsuario").value(USUARIO_CANDIDATO.id()))
                .andReturn().getResponse().getContentAsString();

        candidatoId = objectMapper.readTree(resp).get("id").asLong();
    }

    @Test
    @Order(2)
    @DisplayName("POST /api/candidatos — RH recebe 403")
    void deveRhReceberForbiddenAoCadastrarCandidato() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_RH);

        String body = """
                {
                    "nome": "RH Tentando",
                    "email": "rh@curriculum-test.com",
                    "sexo": "FEMININO"
                }
                """;

        mockMvc.perform(post("/api/candidatos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer " + tokenRh))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(3)
    @DisplayName("POST /api/candidatos — retorna 409 ao cadastrar duplicado")
    void deveRetornar409CandidatoDuplicado() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_CANDIDATO);

        String body = """
                {
                    "nome": "Carlos Duplicado",
                    "email": "candidato@curriculum-test.com",
                    "sexo": "MASCULINO"
                }
                """;

        mockMvc.perform(post("/api/candidatos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(4)
    @DisplayName("GET /api/candidatos/{id} — busca candidato por ID (público)")
    void deveBuscarCandidatoPorId() throws Exception {
        mockMvc.perform(get("/api/candidatos/" + candidatoId)
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(candidatoId))
                .andExpect(jsonPath("$.nome").value("Carlos Curriculo"));
    }

    @Test
    @Order(5)
    @DisplayName("GET /api/candidatos/me — retorna candidato do usuário autenticado")
    void deveBuscarCandidatoMe() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_CANDIDATO);

        mockMvc.perform(get("/api/candidatos/me")
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(USUARIO_CANDIDATO.id()))
                .andExpect(jsonPath("$.nome").value("Carlos Curriculo"));
    }

    @Test
    @Order(6)
    @DisplayName("GET /api/candidatos/{id} — retorna 404 para ID inexistente")
    void deveRetornar404IdInexistente() throws Exception {
        mockMvc.perform(get("/api/candidatos/999999")
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(7)
    @DisplayName("GET /api/candidatos/me — retorna 401 sem token")
    void deveRetornar401SemToken() throws Exception {
        mockMvc.perform(get("/api/candidatos/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(8)
    @DisplayName("PUT /api/candidatos — candidato atualiza seus dados")
    void deveAtualizarCandidato() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_CANDIDATO);

        String body = """
                {
                    "id": %d,
                    "nome": "Carlos Atualizado",
                    "email": "candidato@curriculum-test.com",
                    "sexo": "MASCULINO",
                    "telefone": "(11) 98888-8888",
                    "resumoProfissional": "Desenvolvedor Java Sênior com 7 anos de experiência",
                    "dataNascimento": "1995-06-15"
                }
                """.formatted(candidatoId);

        mockMvc.perform(put("/api/candidatos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Carlos Atualizado"))
                .andExpect(jsonPath("$.telefone").value("(11) 98888-8888"));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Endereço
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(9)
    @DisplayName("POST /api/candidatos/{id}/endereco — cadastra endereço")
    void deveCadastrarEndereco() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_CANDIDATO);

        String body = """
                {
                    "cep": "01310-100",
                    "rua": "Av. Paulista",
                    "numero": "1000",
                    "complemento": "Apto 42",
                    "bairro": "Bela Vista",
                    "cidade": "São Paulo",
                    "estado": "SP"
                }
                """;

        mockMvc.perform(post("/api/candidatos/" + candidatoId + "/endereco")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rua").value("Av. Paulista"))
                .andExpect(jsonPath("$.cidade").value("São Paulo"));
    }

    @Test
    @Order(10)
    @DisplayName("GET /api/candidatos/{id}/endereco — busca endereço")
    void deveBuscarEndereco() throws Exception {
        mockMvc.perform(get("/api/candidatos/" + candidatoId + "/endereco")
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cep").value("01310-100"));
    }

    @Test
    @Order(11)
    @DisplayName("PUT /api/candidatos/{id}/endereco — atualiza endereço")
    void deveAtualizarEndereco() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_CANDIDATO);

        String body = """
                {
                    "cep": "04538-133",
                    "rua": "Rua Funchal",
                    "numero": "200",
                    "bairro": "Vila Olímpia",
                    "cidade": "São Paulo",
                    "estado": "SP"
                }
                """;

        mockMvc.perform(put("/api/candidatos/" + candidatoId + "/endereco")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cep").value("04538-133"))
                .andExpect(jsonPath("$.bairro").value("Vila Olímpia"));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Educação
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(12)
    @DisplayName("POST /api/candidatos/{id}/educacoes — cadastra educação")
    void deveCadastrarEducacao() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_CANDIDATO);

        String body = """
                {
                    "instituicao": "USP",
                    "curso": "Ciência da Computação",
                    "grau": "Bacharelado",
                    "dataInicio": "2013-02-01",
                    "dataFim": "2017-12-01"
                }
                """;

        String resp = mockMvc.perform(post("/api/candidatos/" + candidatoId + "/educacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.instituicao").value("USP"))
                .andExpect(jsonPath("$.curso").value("Ciência da Computação"))
                .andReturn().getResponse().getContentAsString();

        educacaoId = objectMapper.readTree(resp).get("id").asLong();
    }

    @Test
    @Order(13)
    @DisplayName("GET /api/candidatos/{id}/educacoes — lista educações")
    void deveListarEducacoes() throws Exception {
        mockMvc.perform(get("/api/candidatos/" + candidatoId + "/educacoes")
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[*].instituicao", hasItem("USP")));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Experiência
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(14)
    @DisplayName("POST /api/candidatos/{id}/experiencias — cadastra experiência")
    void deveCadastrarExperiencia() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_CANDIDATO);

        String body = """
                {
                    "empresa": "TechCorp",
                    "cargo": "Desenvolvedor Pleno",
                    "resumo": "Desenvolvimento de microserviços em Java",
                    "dataInicio": "2018-01-01",
                    "dataFim": "2022-12-31"
                }
                """;

        String resp = mockMvc.perform(post("/api/candidatos/" + candidatoId + "/experiencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.empresa").value("TechCorp"))
                .andExpect(jsonPath("$.cargo").value("Desenvolvedor Pleno"))
                .andReturn().getResponse().getContentAsString();

        experienciaId = objectMapper.readTree(resp).get("id").asLong();
    }

    @Test
    @Order(15)
    @DisplayName("GET /api/candidatos/{id}/experiencias — lista experiências")
    void deveListarExperiencias() throws Exception {
        mockMvc.perform(get("/api/candidatos/" + candidatoId + "/experiencias")
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[*].empresa", hasItem("TechCorp")));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Habilidade
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(16)
    @DisplayName("POST /api/candidatos/{id}/habilidades — cadastra habilidade")
    void deveCadastrarHabilidade() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_CANDIDATO);

        String body = """
                {
                    "descricao": "Java",
                    "nivel": "Avançado"
                }
                """;

        String resp = mockMvc.perform(post("/api/candidatos/" + candidatoId + "/habilidades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descricao").value("Java"))
                .andReturn().getResponse().getContentAsString();

        habilidadeId = objectMapper.readTree(resp).get("id").asLong();
    }

    @Test
    @Order(17)
    @DisplayName("GET /api/candidatos/{id}/habilidades — lista habilidades")
    void deveListarHabilidades() throws Exception {
        mockMvc.perform(get("/api/candidatos/" + candidatoId + "/habilidades")
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[*].descricao", hasItem("Java")));
    }

    @Test
    @Order(18)
    @DisplayName("GET /api/candidatos/{id} — retorna candidato completo com todas as relações")
    void deveBuscarCandidatoCompleto() throws Exception {
        mockMvc.perform(get("/api/candidatos/" + candidatoId)
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(candidatoId))
                .andExpect(jsonPath("$.endereco.cep").value("04538-133"))
                .andExpect(jsonPath("$.educacoes", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.experiencias", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.habilidades", hasSize(greaterThanOrEqualTo(1))));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Limpeza / DELETE
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    @Order(19)
    @DisplayName("DELETE /api/candidatos/{id}/educacoes/{educacaoId} — remove educação")
    void deveDeletarEducacao() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_CANDIDATO);

        mockMvc.perform(delete("/api/candidatos/" + candidatoId + "/educacoes/" + educacaoId)
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(20)
    @DisplayName("DELETE /api/candidatos/{id}/experiencias/{experienciaId} — remove experiência")
    void deveDeletarExperiencia() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_CANDIDATO);

        mockMvc.perform(delete("/api/candidatos/" + candidatoId + "/experiencias/" + experienciaId)
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(21)
    @DisplayName("DELETE /api/candidatos/{id}/habilidades/{habilidadeId} — remove habilidade")
    void deveDeletarHabilidade() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_CANDIDATO);

        mockMvc.perform(delete("/api/candidatos/" + candidatoId + "/habilidades/" + habilidadeId)
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(22)
    @DisplayName("DELETE /api/candidatos — candidato exclui próprio perfil")
    void deveDeletarCandidato() throws Exception {
        Mockito.when(msUsuarioClient.buscarUsuarioLogado(anyString()))
                .thenReturn(USUARIO_CANDIDATO);

        mockMvc.perform(delete("/api/candidatos")
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isNoContent());

        // Confirma que não existe mais
        mockMvc.perform(get("/api/candidatos/" + candidatoId)
                        .header("Authorization", "Bearer " + tokenCandidato))
                .andExpect(status().isNotFound());
    }
}
