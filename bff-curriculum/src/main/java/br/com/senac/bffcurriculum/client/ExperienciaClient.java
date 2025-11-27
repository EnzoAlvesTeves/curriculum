package br.com.senac.bffcurriculum.client;

import br.com.senac.bffcurriculum.dto.CandidatoVagaDTO;
import br.com.senac.bffcurriculum.dto.ExperienciaDTO;
import br.com.senac.bffcurriculum.dto.UsuarioDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ExperienciaClient {
    private static final String BASE_URL = "http://localhost:8090/experiencias";

    private RestTemplate restTemplate;

    ExperienciaClient(
            RestTemplate restTemplate
    ) {
        this.restTemplate = restTemplate;
    }

    public ExperienciaDTO create(ExperienciaDTO experienciaDTO) throws URISyntaxException {
        ResponseEntity<ExperienciaDTO> response = restTemplate.exchange(
                new URI(BASE_URL),
                HttpMethod.POST,
                new HttpEntity<>(experienciaDTO),
								ExperienciaDTO.class
        );

        return response.getBody();
    }

	public List<ExperienciaDTO> getByCandidatoId(Long candidatoId) throws URISyntaxException {
		ResponseEntity<ExperienciaDTO[]> response = restTemplate.exchange(
						new URI(BASE_URL + "/candidato/" + candidatoId),
						HttpMethod.GET,
						null,
						ExperienciaDTO[].class
		);

		return  response.getBody() != null
						? Arrays.stream(response.getBody()).toList()
						: new ArrayList<>();
	}

    public ExperienciaDTO update(ExperienciaDTO experienciaDTO) throws URISyntaxException {
        ResponseEntity<ExperienciaDTO> response = restTemplate.exchange(
                new URI(BASE_URL),
                HttpMethod.PUT,
                new HttpEntity<>(experienciaDTO),
								ExperienciaDTO.class
        );

        return response.getBody();
    }

    public void delete(Long id) throws URISyntaxException {
        restTemplate.exchange(
                new URI(BASE_URL + "/" + id),
                HttpMethod.DELETE,
                null,
                Void.class
        );
    }
}
