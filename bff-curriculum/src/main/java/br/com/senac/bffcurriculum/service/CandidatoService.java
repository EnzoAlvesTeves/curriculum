package br.com.senac.bffcurriculum.service;

import br.com.senac.bffcurriculum.client.CandidatoClient;
import br.com.senac.bffcurriculum.dto.CandidatoDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidatoService {

    private final CandidatoClient candidatoClient;

    CandidatoService(CandidatoClient candidatoClient) {
        this.candidatoClient = candidatoClient;
    }

    public CandidatoDTO getByUsuarioId(Long usuarioId) {
        try {
            return candidatoClient.getByUsuarioId(usuarioId);
        }  catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public CandidatoDTO getById(Long id) {
        return candidatoClient.getById(id);
    }

    public List<CandidatoDTO> getall() {
        return candidatoClient.getall();
    }

    public CandidatoDTO create(CandidatoDTO candidatoDTO) {
        return candidatoClient.create(candidatoDTO);
    }

    public CandidatoDTO update(CandidatoDTO candidatoDTO) {
        return candidatoClient.update(candidatoDTO);
    }
}
