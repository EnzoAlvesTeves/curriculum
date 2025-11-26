package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.EnderecoDTO;
import br.com.senac.mscurriculum.repository.EnderecoRepository;
import br.com.senac.mscurriculum.repository.entity.EnderecoEntity;
import org.springframework.stereotype.Service;

@Service
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;

    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    public EnderecoDTO create(EnderecoDTO enderecoDTO) {
			EnderecoEntity enderecoEntity = enderecoDTO.toEntity();

			EnderecoEntity novoEndereco = enderecoRepository.save(enderecoEntity);

			return new EnderecoDTO(novoEndereco);
    }

    public EnderecoDTO update(EnderecoDTO enderecoDTO) {
			EnderecoEntity enderecoEntity = enderecoRepository.findById(enderecoDTO.getId())
							.orElseThrow(() -> new RuntimeException("Endereço não encontrado!"));

			enderecoEntity.setCep(enderecoDTO.getCep());
			enderecoEntity.setNumero(enderecoDTO.getNumero());
			enderecoEntity.setComplemento(enderecoDTO.getComplemento());
			enderecoEntity.setBairro(enderecoDTO.getBairro());
			enderecoEntity.setCidade(enderecoDTO.getCidade());
			enderecoEntity.setEstado(enderecoDTO.getEstado());

			EnderecoEntity enderecoAlterado = enderecoRepository.save(enderecoEntity);

			return new EnderecoDTO(enderecoAlterado);
    }

    public EnderecoDTO getById(Long enderecoId) {
			EnderecoEntity enderecoEntity = enderecoRepository.findById(enderecoId)
							.orElseThrow(() -> new RuntimeException("Endereço não encontrado!"));

			return new EnderecoDTO(enderecoEntity);
    }

    public void delete(Long enderecoId) {
			EnderecoEntity enderecoEntity = enderecoRepository.findById(enderecoId)
							.orElseThrow(() -> new RuntimeException("Endereço não encontrado!"));

			enderecoRepository.delete(enderecoEntity);
    }
}
