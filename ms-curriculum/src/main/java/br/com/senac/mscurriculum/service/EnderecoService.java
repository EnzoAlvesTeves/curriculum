package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.EnderecoDTO;
import br.com.senac.mscurriculum.repository.EnderecoRepository;
import org.springframework.stereotype.Service;

@Service
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;

    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    public EnderecoDTO create(EnderecoDTO enderecoDTO) {
        // Lógica para criar um novo endereço
        return null;
    }

    public EnderecoDTO update(EnderecoDTO enderecoDTO) {
        // Lógica para atualizar um endereço existente
        return null;
    }

    public EnderecoDTO getById(Long enderecoId) {
        // Lógica para buscar um endereço por ID
        return null;
    }

    public void delete(Long enderecoId) {
        // Lógica para deletar um endereço
    }
}
