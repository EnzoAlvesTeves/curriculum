package br.com.senac.bffcurriculum.client;

import br.com.senac.bffcurriculum.config.RestTemplateConfig;
import br.com.senac.bffcurriculum.config.RestTemplateLoggingInterceptor;
import br.com.senac.bffcurriculum.dto.*;
import br.com.senac.bffcurriculum.enums.Sexo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BffClientCoverageTest {

    private static final String BASE_URL = "http://localhost:8080";

    @Mock
    private RestTemplate restTemplate;

    @Test
    void restTemplateConfigShouldIncludeLoggingInterceptor() {
        RestTemplateConfig config = new RestTemplateConfig();

        RestTemplate template = config.restTemplate();

        assertNotNull(template);
        assertEquals(1, template.getInterceptors().size());
        assertInstanceOf(RestTemplateLoggingInterceptor.class, template.getInterceptors().get(0));
    }

    @Test
    void loggingInterceptorShouldCallExecutionAndReturnResponse() throws Exception {
        RestTemplateLoggingInterceptor interceptor = new RestTemplateLoggingInterceptor();
        HttpRequest request = mock(HttpRequest.class);
        ClientHttpRequestExecution execution = mock(ClientHttpRequestExecution.class);
        ClientHttpResponse response = mock(ClientHttpResponse.class);
        byte[] body = "body".getBytes();

        when(request.getMethod()).thenReturn(HttpMethod.GET);
        when(request.getURI()).thenReturn(new URI("http://localhost:8080/test"));
        when(execution.execute(request, body)).thenReturn(response);
        when(response.getStatusCode()).thenReturn(HttpStatus.OK);

        ClientHttpResponse actual = interceptor.intercept(request, body, execution);

        assertSame(response, actual);
        verify(execution).execute(request, body);
        verify(response).getStatusCode();
    }

    @Test
    void enderecoClientShouldCreateUpdateGetByIdAndDelete() throws Exception {
        EnderecoClient client = new EnderecoClient(restTemplate);
        ReflectionTestUtils.setField(client, "baseUrl", BASE_URL);
        EnderecoDTO dto = new EnderecoDTO(10L, "Rua A", "123", "Bloco 1", "São Paulo", "SP", "01000-000", "Centro");

        when(restTemplate.exchange(eq(new URI(BASE_URL + "/enderecos")), eq(HttpMethod.POST), any(HttpEntity.class), eq(EnderecoDTO.class)))
                .thenReturn(ResponseEntity.ok(dto));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/enderecos")), eq(HttpMethod.PUT), any(HttpEntity.class), eq(EnderecoDTO.class)))
                .thenReturn(ResponseEntity.ok(dto));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/enderecos/10")), eq(HttpMethod.GET), isNull(), eq(EnderecoDTO.class)))
                .thenReturn(ResponseEntity.ok(dto));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/enderecos/10")), eq(HttpMethod.DELETE), isNull(), eq(Void.class)))
                .thenReturn(ResponseEntity.noContent().build());

        assertSame(dto, client.create(dto));
        assertSame(dto, client.update(dto));
        assertSame(dto, client.getById(10L));
        client.delete(10L);

        verify(restTemplate).exchange(eq(new URI(BASE_URL + "/enderecos/10")), eq(HttpMethod.DELETE), isNull(), eq(Void.class));
    }

    @Test
    void habilidadeClientShouldCreateUpdateListByCandidatoAndDelete() throws Exception {
        HabilidadeClient client = new HabilidadeClient(restTemplate);
        ReflectionTestUtils.setField(client, "baseUrl", BASE_URL);
        HabilidadeDTO habilidade = new HabilidadeDTO(1L, "Java", "Avançado", "Backend", 42L);

        when(restTemplate.exchange(eq(new URI(BASE_URL + "/habilidades")), eq(HttpMethod.POST), any(HttpEntity.class), eq(HabilidadeDTO.class)))
                .thenReturn(ResponseEntity.ok(habilidade));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/habilidades")), eq(HttpMethod.PUT), any(HttpEntity.class), eq(HabilidadeDTO.class)))
                .thenReturn(ResponseEntity.ok(habilidade));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/habilidades/candidato/42")), eq(HttpMethod.GET), isNull(), eq(HabilidadeDTO[].class)))
                .thenReturn(ResponseEntity.ok(new HabilidadeDTO[]{habilidade}));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/habilidades/1")), eq(HttpMethod.DELETE), isNull(), eq(Void.class)))
                .thenReturn(ResponseEntity.noContent().build());

        assertSame(habilidade, client.create(habilidade));
        assertSame(habilidade, client.update(habilidade));
        List<HabilidadeDTO> result = client.getByCandidatoId(42L);
        assertEquals(1, result.size());
        assertSame(habilidade, result.get(0));
        client.delete(1L);
    }

    @Test
    void candidatoVagaClientShouldCreateListByCandidatoAndDelete() throws Exception {
        CandidatoVagaClient client = new CandidatoVagaClient(restTemplate);
        ReflectionTestUtils.setField(client, "baseUrl", BASE_URL);
        CandidatoVagaDTO dto = new CandidatoVagaDTO(7L, new VagaDTO(11L, "Analista", "Descrição", "Empresa", "Vale refeição", 5000.0), LocalDateTime.now(), 99L);

        when(restTemplate.exchange(eq(new URI(BASE_URL + "/candidato-vaga")), eq(HttpMethod.POST), any(HttpEntity.class), eq(CandidatoVagaDTO.class)))
                .thenReturn(ResponseEntity.ok(dto));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/candidato-vaga/candidato/99")), eq(HttpMethod.GET), isNull(), eq(CandidatoVagaDTO[].class)))
                .thenReturn(ResponseEntity.ok(new CandidatoVagaDTO[]{dto}));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/candidato-vaga/vaga/11")), eq(HttpMethod.GET), isNull(), eq(CandidatoVagaDTO[].class)))
                .thenReturn(ResponseEntity.ok(new CandidatoVagaDTO[]{dto}));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/candidato-vaga/7")), eq(HttpMethod.DELETE), isNull(), eq(Void.class)))
                .thenReturn(ResponseEntity.noContent().build());

        assertSame(dto, client.create(dto));
        List<CandidatoVagaDTO> byCandidato = client.getByCandidatoId(99L);
        List<CandidatoVagaDTO> byVaga = client.getByVagaId(11L);
        assertEquals(1, byCandidato.size());
        assertEquals(1, byVaga.size());
        client.delete(7L);
    }

    @Test
    void candidatoClientShouldCreateGetUpdateDeleteAndList() throws Exception {
        CandidatoClient client = new CandidatoClient(restTemplate);
        ReflectionTestUtils.setField(client, "baseUrl", BASE_URL);
        CandidatoDTO candidato = new CandidatoDTO();
        candidato.setId(3L);
        candidato.setNome("Ana");
        candidato.setEmail("ana@email.com");
        candidato.setTelefone("99999-0000");
        candidato.setSexo(Sexo.FEMININO);
        candidato.setDataNascimento(LocalDate.of(1995, 3, 15));
        candidato.setResumoProfissional("Resumo");
        candidato.setUsuarioId(40L);

        when(restTemplate.exchange(eq(new URI(BASE_URL + "/candidatos")), eq(HttpMethod.POST), any(HttpEntity.class), eq(CandidatoDTO.class)))
                .thenReturn(ResponseEntity.ok(candidato));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/candidatos/3")), eq(HttpMethod.GET), isNull(), eq(CandidatoDTO.class)))
                .thenReturn(ResponseEntity.ok(candidato));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/candidatos")), eq(HttpMethod.GET), isNull(), eq(CandidatoDTO[].class)))
                .thenReturn(ResponseEntity.ok(new CandidatoDTO[]{candidato}));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/candidatos")), eq(HttpMethod.PUT), any(HttpEntity.class), eq(CandidatoDTO.class)))
                .thenReturn(ResponseEntity.ok(candidato));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/candidatos/3")), eq(HttpMethod.DELETE), isNull(), eq(Void.class)))
                .thenReturn(ResponseEntity.noContent().build());
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/candidatos/usuario/40")), eq(HttpMethod.GET), isNull(), eq(CandidatoDTO.class)))
                .thenReturn(ResponseEntity.ok(candidato));

        assertSame(candidato, client.create(candidato));
        assertSame(candidato, client.getById(3L));
        assertEquals(1, client.getall().size());
        assertSame(candidato, client.update(candidato));
        client.delete(3L);
        assertSame(candidato, client.getByUsuarioId(40L));
    }

    @Test
    void usuarioClientShouldCreateUpdateGetLoginAndEmail() throws Exception {
        UsuarioClient client = new UsuarioClient(restTemplate);
        ReflectionTestUtils.setField(client, "baseUrl", BASE_URL);
        UsuarioDTO usuario = new UsuarioDTO(5L, "Maria", "maria@email.com", "senha123");
        LoginRequestDTO loginRequest = new LoginRequestDTO("maria@email.com", "senha123");

        when(restTemplate.exchange(eq(new URI(BASE_URL + "/usuarios")), eq(HttpMethod.POST), any(HttpEntity.class), eq(UsuarioDTO.class)))
                .thenReturn(ResponseEntity.ok(usuario));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/usuarios")), eq(HttpMethod.PUT), any(HttpEntity.class), eq(UsuarioDTO.class)))
                .thenReturn(ResponseEntity.ok(usuario));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/usuarios/5")), eq(HttpMethod.GET), isNull(), eq(UsuarioDTO.class)))
                .thenReturn(ResponseEntity.ok(usuario));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/usuarios/login")), eq(HttpMethod.POST), any(HttpEntity.class), eq(UsuarioDTO.class)))
                .thenReturn(ResponseEntity.ok(usuario));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/usuarios/email/maria@email.com")), eq(HttpMethod.GET), isNull(), eq(UsuarioDTO.class)))
                .thenReturn(ResponseEntity.ok(usuario));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/usuarios/5")), eq(HttpMethod.DELETE), isNull(), eq(Void.class)))
                .thenReturn(ResponseEntity.noContent().build());

        assertSame(usuario, client.create(usuario));
        assertSame(usuario, client.update(usuario));
        assertSame(usuario, client.getById(5L));
        assertSame(usuario, client.login(loginRequest));
        assertSame(usuario, client.getByEmail("maria@email.com"));
        client.delete(5L);
    }

    @Test
    void vagaClientShouldCreateUpdateGetByIdAndListDelete() throws Exception {
        VagaClient client = new VagaClient(restTemplate);
        ReflectionTestUtils.setField(client, "baseUrl", BASE_URL);
        VagaDTO vaga = new VagaDTO(8L, "Desenvolvedor Java", "Desenvolver APIs", "ACME", "Vale refeição", 7000.0);

        when(restTemplate.exchange(eq(new URI(BASE_URL + "/vagas")), eq(HttpMethod.POST), any(HttpEntity.class), eq(VagaDTO.class)))
                .thenReturn(ResponseEntity.ok(vaga));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/vagas")), eq(HttpMethod.PUT), any(HttpEntity.class), eq(VagaDTO.class)))
                .thenReturn(ResponseEntity.ok(vaga));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/vagas/8")), eq(HttpMethod.GET), isNull(), eq(VagaDTO.class)))
                .thenReturn(ResponseEntity.ok(vaga));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/vagas")), eq(HttpMethod.GET), isNull(), eq(VagaDTO[].class)))
                .thenReturn(ResponseEntity.ok(new VagaDTO[]{vaga}));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/vagas/8")), eq(HttpMethod.DELETE), isNull(), eq(Void.class)))
                .thenReturn(ResponseEntity.noContent().build());

        assertSame(vaga, client.create(vaga));
        assertSame(vaga, client.update(vaga));
        assertSame(vaga, client.getById(8L));
        assertEquals(1, client.getall().size());
        client.delete(8L);
    }

    @Test
    void educacaoClientShouldCreateUpdateListByCandidatoAndDeleteIncludingEmptyResponse() throws Exception {
        EducacaoClient client = new EducacaoClient(restTemplate);
        ReflectionTestUtils.setField(client, "baseUrl", BASE_URL);
        EducacaoDTO educacao = new EducacaoDTO(1L, "Bacharelado", LocalDate.of(2020, 1, 1), LocalDate.of(2023, 12, 31), "SENAC", "ADS", 77L);

        when(restTemplate.exchange(eq(new URI(BASE_URL + "/educacoes")), eq(HttpMethod.POST), any(HttpEntity.class), eq(EducacaoDTO.class)))
                .thenReturn(ResponseEntity.ok(educacao));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/educacoes")), eq(HttpMethod.PUT), any(HttpEntity.class), eq(EducacaoDTO.class)))
                .thenReturn(ResponseEntity.ok(educacao));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/educacoes/candidato/77")), eq(HttpMethod.GET), isNull(), eq(EducacaoDTO[].class)))
                .thenReturn(ResponseEntity.ok(new EducacaoDTO[]{educacao}))
                .thenReturn(ResponseEntity.ok(null));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/educacoes/1")), eq(HttpMethod.DELETE), isNull(), eq(Void.class)))
                .thenReturn(ResponseEntity.noContent().build());

        assertSame(educacao, client.create(educacao));
        assertSame(educacao, client.update(educacao));
        List<EducacaoDTO> result = client.getByCandidatoId(77L);
        assertEquals(1, result.size());
        assertSame(educacao, result.get(0));
        assertEquals(0, client.getByCandidatoId(77L).size());
        client.delete(1L);
    }

    @Test
    void experienciaClientShouldCreateUpdateListByCandidatoAndDeleteIncludingEmptyResponse() throws Exception {
        ExperienciaClient client = new ExperienciaClient(restTemplate);
        ReflectionTestUtils.setField(client, "baseUrl", BASE_URL);
        ExperienciaDTO experiencia = new ExperienciaDTO(5L, "Dev", "ACME", LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), 88L);

        when(restTemplate.exchange(eq(new URI(BASE_URL + "/experiencias")), eq(HttpMethod.POST), any(HttpEntity.class), eq(ExperienciaDTO.class)))
                .thenReturn(ResponseEntity.ok(experiencia));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/experiencias")), eq(HttpMethod.PUT), any(HttpEntity.class), eq(ExperienciaDTO.class)))
                .thenReturn(ResponseEntity.ok(experiencia));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/experiencias/candidato/88")), eq(HttpMethod.GET), isNull(), eq(ExperienciaDTO[].class)))
                .thenReturn(ResponseEntity.ok(new ExperienciaDTO[]{experiencia}))
                .thenReturn(ResponseEntity.ok(null));
        when(restTemplate.exchange(eq(new URI(BASE_URL + "/experiencias/5")), eq(HttpMethod.DELETE), isNull(), eq(Void.class)))
                .thenReturn(ResponseEntity.noContent().build());

        assertSame(experiencia, client.create(experiencia));
        assertSame(experiencia, client.update(experiencia));
        List<ExperienciaDTO> result = client.getByCandidatoId(88L);
        assertEquals(1, result.size());
        assertSame(experiencia, result.get(0));
        assertEquals(0, client.getByCandidatoId(88L).size());
        client.delete(5L);
    }
}
