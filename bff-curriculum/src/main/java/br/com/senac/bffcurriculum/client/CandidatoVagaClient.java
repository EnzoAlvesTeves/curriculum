package br.com.senac.bffcurriculum.client;

import br.com.senac.bffcurriculum.dto.CandidatoVagaDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CandidatoVagaClient {
    @Value("${base-url.ms-vagas}")
    private String baseUrl;

    private static final String CANDIDATO_VAGA = "/candidato-vaga";

    private RestTemplate restTemplate;

    CandidatoVagaClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CandidatoVagaDTO create(CandidatoVagaDTO candidatoVagaDTO) {
        ResponseEntity<CandidatoVagaDTO> response = null;
        try {
            response = restTemplate.exchange(
                    new URI(baseUrl + CANDIDATO_VAGA),
                    HttpMethod.POST,
                    new HttpEntity<>(candidatoVagaDTO),
                    CandidatoVagaDTO.class
            );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return response.getBody();
    }

    public void delete(Long id) {
        try {
            restTemplate.exchange(
                    new URI(baseUrl + CANDIDATO_VAGA + "/" + id),
                    HttpMethod.DELETE,
                    null,
                    Void.class
            );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public List<CandidatoVagaDTO> getByCandidatoId(Long candidatoId) {
        ResponseEntity<CandidatoVagaDTO[]> response = null;
        try {
            response = restTemplate.exchange(
                    new URI(baseUrl + CANDIDATO_VAGA + "/candidato/" + candidatoId),
                    HttpMethod.GET,
                    null,
                    CandidatoVagaDTO[].class
            );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return response.getBody() != null
                ? Arrays.stream(response.getBody()).toList()
                : new ArrayList<>();
    }

    public List<CandidatoVagaDTO> getByVagaId(Long vagaId) {
        ResponseEntity<CandidatoVagaDTO[]> response = null;
        try {
            response = restTemplate.exchange(
                    new URI(baseUrl + CANDIDATO_VAGA + "/vaga/" + vagaId),
                    HttpMethod.GET,
                    null,
                    CandidatoVagaDTO[].class
            );
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return response.getBody() != null
                ? Arrays.stream(response.getBody()).toList()
                : new ArrayList<>();
    }
}
