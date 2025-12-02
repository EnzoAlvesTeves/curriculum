package br.com.senac.bffcurriculum.service;

import br.com.senac.bffcurriculum.client.VagaClient;
import br.com.senac.bffcurriculum.dto.VagaDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VagaService {

    private final VagaClient vagaClient;

    public VagaService(VagaClient vagaClient) {
        this.vagaClient = vagaClient;
    }

    public void cadastrar(VagaDTO vagaDTO) {
        vagaClient.create(vagaDTO);
    }

    public List<VagaDTO> getAll() {
        return vagaClient.getall();
    }

    public VagaDTO getById(Long vagaId) {
        return vagaClient.getById(vagaId);
    }
}
