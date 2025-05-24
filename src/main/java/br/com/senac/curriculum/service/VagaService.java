package br.com.senac.curriculum.service;

import br.com.senac.curriculum.dto.VagaDTO;
import br.com.senac.curriculum.repository.vaga.VagaEntity;
import br.com.senac.curriculum.repository.vaga.VagaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VagaService {

    @Autowired
    private VagaRepository vagaRepository;

    @Transactional
    public VagaDTO cadastrar(VagaDTO vagaDTO) {
        VagaEntity vaga = new VagaEntity();
        vaga.setTitulo(vagaDTO.getTitulo());
        vaga.setDescricao(vagaDTO.getDescricao());
        vaga.setEmpresa(vagaDTO.getEmpresa());
        vaga.setBeneficios(vagaDTO.getBeneficios());
        vaga.setSalario(vagaDTO.getSalario());

        vaga = vagaRepository.save(vaga);

        return new VagaDTO(vaga);
    }

    public List<VagaDTO> listarVagas() {
        return vagaRepository.findAll()
                .stream()
                .map(VagaDTO::new)
                .toList();
    }
}
