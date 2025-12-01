package br.com.senac.bffcurriculum.service;

import br.com.senac.bffcurriculum.client.CandidatoClient;
import br.com.senac.bffcurriculum.dto.CandidatoDTO;
import org.springframework.stereotype.Service;

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
}
