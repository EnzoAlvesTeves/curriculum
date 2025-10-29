package br.com.senac.msvagas.service;

import br.com.senac.msvagas.dto.VagaDTO;
import br.com.senac.msvagas.repository.VagaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VagaService {
    private final VagaRepository vagaRepository;

    public VagaService(VagaRepository vagaRepository) {
        this.vagaRepository = vagaRepository;
    }

    public VagaDTO create(VagaDTO vagaDTO) {
        // implementar a lógica para criar uma nova vaga
        return null;
    }

    public VagaDTO update(VagaDTO vagaDTO) {
        // implementar a lógica para atualizar uma vaga existente
        return null;
    }

    public void delete(Long id) {
        // implementar a lógica para deletar uma vaga pelo id
    }

    public  VagaDTO getById(Long id) {
        // implementar a lógica para buscar uma vaga pelo id
        return null;
    }

    public List<VagaDTO> getall() {
        // implementar a lógica para listar todas as vagas
        return null;
    }

}
