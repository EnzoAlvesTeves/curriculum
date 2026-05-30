package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.EnderecoDTO;
import br.com.senac.mscurriculum.mapper.EnderecoMapper;
import br.com.senac.mscurriculum.repository.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final CandidatoAuthorizationService candidatoAuthorizationService;

    public EnderecoDTO cadastrar(Long candidatoId, EnderecoDTO enderecoDTO, Jwt jwt) {
        candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt);
        return cadastrar(candidatoId, enderecoDTO);
    }

    public EnderecoDTO cadastrar(Long candidatoId, EnderecoDTO dto) {
        var entity = EnderecoMapper.toEntity(dto, candidatoId);
        var endereco = enderecoRepository.save(entity);

        return EnderecoMapper.toDTO(endereco);
    }

    public EnderecoDTO buscarPorCandidato(Long candidatoId) {
        var endereco = enderecoRepository.findByIdCandidato(candidatoId);
        return endereco.map(EnderecoMapper::toDTO)
                        .orElse(null);
    }

    public EnderecoDTO alterar(Long candidatoId, EnderecoDTO enderecoDTO, Jwt jwt) {
        candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt);
        var endereco = enderecoRepository.findByIdCandidato(candidatoId)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado para o candidato ID: " + candidatoId));

        endereco.setRua(enderecoDTO.getRua());
        endereco.setNumero(enderecoDTO.getNumero());
        endereco.setComplemento(enderecoDTO.getComplemento());
        endereco.setCidade(enderecoDTO.getCidade());
        endereco.setEstado(enderecoDTO.getEstado());
        endereco.setCep(enderecoDTO.getCep());
        endereco.setBairro(enderecoDTO.getBairro());
        endereco.setLatitude(enderecoDTO.getLatitude());
        endereco.setLongitude(enderecoDTO.getLongitude());

        var enderecoAtualizado = enderecoRepository.save(endereco);
        return EnderecoMapper.toDTO(enderecoAtualizado);
    }

    public void deletar(Long candidatoId, Jwt jwt) {
        candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt);
        var endereco = enderecoRepository.findByIdCandidato(candidatoId)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado para o candidato ID: " + candidatoId));

        enderecoRepository.delete(endereco);
    }
}

