package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.EnderecoDTO;
import br.com.senac.mscurriculum.repository.EnderecoRepository;
import br.com.senac.mscurriculum.repository.entity.EnderecoEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;

    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    @Transactional
    public EnderecoDTO create(EnderecoDTO enderecoDTO) {
        try {
            EnderecoEntity enderecoEntity = enderecoDTO.toEntity();

            EnderecoEntity novoEndereco = enderecoRepository.save(enderecoEntity);

            return new EnderecoDTO(novoEndereco);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao cadastrar endereço", e);
        }
    }

    @Transactional
    public EnderecoDTO update(EnderecoDTO enderecoDTO) {
        EnderecoEntity enderecoEntity = enderecoRepository.findById(enderecoDTO.getId())
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado!"));

        try {
            enderecoEntity.setCep(enderecoDTO.getCep());
            enderecoEntity.setNumero(enderecoDTO.getNumero());
            enderecoEntity.setComplemento(enderecoDTO.getComplemento());
            enderecoEntity.setBairro(enderecoDTO.getBairro());
            enderecoEntity.setCidade(enderecoDTO.getCidade());
            enderecoEntity.setEstado(enderecoDTO.getEstado());

            EnderecoEntity enderecoAlterado = enderecoRepository.save(enderecoEntity);

            return new EnderecoDTO(enderecoAlterado);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao editar endereço", e);
        }
    }

    public EnderecoDTO getById(Long enderecoId) {
        EnderecoEntity enderecoEntity = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado!"));

        return new EnderecoDTO(enderecoEntity);
    }

    @Transactional
    public void delete(Long enderecoId) {
        EnderecoEntity enderecoEntity = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado!"));

        try {
            enderecoRepository.delete(enderecoEntity);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar endereço", e);
        }
    }
}
