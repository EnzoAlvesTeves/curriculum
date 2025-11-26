package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.HabilidadeDTO;
import br.com.senac.mscurriculum.repository.CandidatoRepository;
import br.com.senac.mscurriculum.repository.HabilidadeRepository;
import br.com.senac.mscurriculum.repository.entity.CandidatoEntity;
import br.com.senac.mscurriculum.repository.entity.HabilidadeEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HabilidadeService {
    final HabilidadeRepository habilidadeRepository;
    final CandidatoRepository candidatoRepository;

    public HabilidadeService(
            HabilidadeRepository habilidadeRepository,
            CandidatoRepository candidatoRepository
    ) {
        this.habilidadeRepository = habilidadeRepository;
        this.candidatoRepository = candidatoRepository;
    }

    @Transactional
    public HabilidadeDTO create(HabilidadeDTO habilidadeDTO) {
        CandidatoEntity candidatoEntity = candidatoRepository.findById(habilidadeDTO.getCandidatoId())
                .orElseThrow(() -> new RuntimeException("Candidato não encontrado!"));

        try {
            HabilidadeEntity habilidadeEntity = habilidadeDTO.toEntity();
            habilidadeEntity.setCandidato(candidatoEntity);

            HabilidadeEntity novaHabilidade = habilidadeRepository.save(habilidadeEntity);

            return new HabilidadeDTO(novaHabilidade);
        }  catch (Exception e) {
            throw new RuntimeException("Erro ao gravar habilidade ", e);
        }
    }

    public List<HabilidadeDTO> getByCandidatoId(Long candidatoId) {
        List<HabilidadeEntity> habilidades = habilidadeRepository.findByCandidatoId(candidatoId);

        return habilidades.stream()
                .map(HabilidadeDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public HabilidadeDTO update(HabilidadeDTO habilidadeDTO) {
        HabilidadeEntity habilidadeEntity = habilidadeRepository.findById(habilidadeDTO.getId())
                .orElseThrow(() -> new RuntimeException("Habilidade não encontrada!"));
        try {
            habilidadeEntity.setDescricao(habilidadeDTO.getDescricao());
            habilidadeEntity.setNivel(habilidadeDTO.getNivel());
            habilidadeEntity.setEspecialidade(habilidadeDTO.getEspecialidade());
            habilidadeEntity.setId(habilidadeDTO.getId());

            HabilidadeEntity habilidadeAlterada = habilidadeRepository.save(habilidadeEntity);

            return new HabilidadeDTO(habilidadeAlterada);
        }   catch (Exception e) {
            throw new RuntimeException("Erro ao editar habilidade ", e);
        }
    }

    @Transactional
    public void delete(Long habilidadeId) {
        HabilidadeEntity habilidadeEntity = habilidadeRepository.findById(habilidadeId)
                .orElseThrow(() -> new RuntimeException("Habilidade não encontrada!"));

        try {
            habilidadeRepository.delete(habilidadeEntity);
        }  catch (Exception e) {
            throw new RuntimeException("Erro ao deletar habilidade ", e);
        }
    }
}
