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

    public VagaDTO salvar(VagaDTO vagaDTO) {

    }

    public VagaDTO atualizar(VagaDTO vagaDTO) {

    }

    public void deletar(Long id) {

    }

    public  VagaDTO consultarPorId(Long id) {

    }

    public List<VagaDTO> listarTodas() {

    }

}
