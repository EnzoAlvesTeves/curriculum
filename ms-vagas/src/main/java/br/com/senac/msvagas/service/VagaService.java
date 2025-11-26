package br.com.senac.msvagas.service;

import br.com.senac.msvagas.dto.VagaDTO;
import br.com.senac.msvagas.repository.VagaRepository;
import br.com.senac.msvagas.repository.entity.VagaEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VagaService {
    private final VagaRepository vagaRepository;

    public VagaService(VagaRepository vagaRepository) {
        this.vagaRepository = vagaRepository;
    }

    @Transactional
    public VagaDTO create(VagaDTO vagaDTO) {
        try {
            VagaEntity vagaEntity = vagaDTO.toEntity();

            VagaEntity novaVaga = vagaRepository.save(vagaEntity);

            return new VagaDTO(novaVaga);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gravar vaga", e);
        }
    }

    @Transactional
    public VagaDTO update(VagaDTO vagaDTO) {
        VagaEntity vagaEntity = vagaRepository.findById(vagaDTO.getId())
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada!"));

        try {
            vagaEntity.setTitulo(vagaDTO.getTitulo());
            vagaEntity.setDescricao(vagaDTO.getDescricao());
            vagaEntity.setEmpresa(vagaDTO.getEmpresa());
            vagaEntity.setBeneficios(vagaDTO.getBeneficios());
            vagaEntity.setSalario(vagaDTO.getSalario());

            VagaEntity vagaAlterada = vagaRepository.save(vagaEntity);

            return new VagaDTO(vagaAlterada);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao editar vaga", e);
        }
    }

    @Transactional
    public void delete(Long id) {
        VagaEntity vagaEntity = vagaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada!"));
        try {
            vagaRepository.delete(vagaEntity);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar vaga", e);
        }

    }

    public VagaDTO getById(Long id) {
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
