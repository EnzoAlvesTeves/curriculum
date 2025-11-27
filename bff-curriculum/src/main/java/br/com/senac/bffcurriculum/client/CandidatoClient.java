package br.com.senac.bffcurriculum.client;

import br.com.senac.bffcurriculum.dto.CandidatoDTO;
import br.com.senac.bffcurriculum.dto.CandidatoVagaDTO;
import br.com.senac.bffcurriculum.dto.UsuarioDTO;
import br.com.senac.bffcurriculum.dto.VagaDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CandidatoClient {
	private static final String BASE_URL = "http://localhost:8090/candidatos";

	private RestTemplate restTemplate;

	CandidatoClient(
					RestTemplate restTemplate
	) {
		this.restTemplate = restTemplate;
	}

	public CandidatoDTO create(CandidatoDTO candidatoDTO) throws URISyntaxException {
		ResponseEntity<CandidatoDTO> response = restTemplate.exchange(
						new URI(BASE_URL),
						HttpMethod.POST,
						new HttpEntity<>(candidatoDTO),
						CandidatoDTO.class
		);
		return response.getBody();
	}

	public CandidatoDTO getById(Long id) throws URISyntaxException {
		ResponseEntity<CandidatoDTO> response = restTemplate.exchange(
						new URI(BASE_URL + "/" + id),
						HttpMethod.GET,
						null,
						CandidatoDTO.class
		);

		return response.getBody();
	}

	public List<CandidatoDTO> getall() throws URISyntaxException {
		ResponseEntity<CandidatoDTO[]> response = restTemplate.exchange(
						new URI(BASE_URL),
						HttpMethod.GET,
						null,
						CandidatoDTO[].class
		);

		return response.getBody() != null
						? Arrays.stream(response.getBody()).toList()
						: new ArrayList<>();
	}

	public CandidatoDTO update(CandidatoDTO candidatoDTO) throws URISyntaxException {
		ResponseEntity<CandidatoDTO> response = restTemplate.exchange(
						new URI(BASE_URL),
						HttpMethod.PUT,
						new HttpEntity<>(candidatoDTO),
						CandidatoDTO.class
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


