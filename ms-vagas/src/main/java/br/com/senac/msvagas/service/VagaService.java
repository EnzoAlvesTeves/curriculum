package br.com.senac.msvagas.service;

import br.com.senac.msvagas.dto.VagaDTO;
import br.com.senac.msvagas.repository.VagaRepository;
import br.com.senac.msvagas.repository.entity.VagaEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VagaService {
    private final VagaRepository vagaRepository;

    public VagaService(VagaRepository vagaRepository) {
        this.vagaRepository = vagaRepository;
    }

    public VagaDTO create(VagaDTO vagaDTO) {
        VagaEntity vagaEntity = vagaDTO.toEntity();

        VagaEntity novaVaga = vagaRepository.save(vagaEntity);

        return new  VagaDTO(novaVaga);
    }

    public VagaDTO update(VagaDTO vagaDTO) {
        VagaEntity vagaEntity = vagaRepository.findById(vagaDTO.getId())
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada!"));

        vagaEntity.setTitulo(vagaDTO.getTitulo());
        vagaEntity.setDescricao(vagaDTO.getDescricao());
        vagaEntity.setEmpresa(vagaDTO.getEmpresa());
        vagaEntity.setBeneficios(vagaDTO.getBeneficios());
        vagaEntity.setSalario(vagaDTO.getSalario());

        VagaEntity vagaAlterada = vagaRepository.save(vagaEntity);

        return new VagaDTO(vagaAlterada);
    }

    public void delete(Long id) {
        VagaEntity vagaEntity = vagaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada!"));

        vagaRepository.delete(vagaEntity);
    }

    public  VagaDTO getById(Long id) {
        VagaEntity vagaEntity = vagaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada!"));

        return new VagaDTO(vagaEntity);
    }

    public List<VagaDTO> getAll() {
        List<VagaEntity> vagas = vagaRepository.findAll();

        return vagas.stream()
                .map(VagaDTO::new)
                .toList();
    }

}
