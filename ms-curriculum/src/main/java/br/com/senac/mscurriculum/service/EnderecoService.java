package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.EnderecoDTO;
import br.com.senac.mscurriculum.mapper.EnderecoMapper;
import br.com.senac.mscurriculum.repository.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final CandidatoAuthorizationService candidatoAuthorizationService;

    @Transactional
    public EnderecoDTO cadastrar(Long candidatoId, EnderecoDTO enderecoDTO, Jwt jwt) {
        log.info("Iniciando cadastro de endereço para candidatoId={}", candidatoId);
        candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt);
        return cadastrar(candidatoId, enderecoDTO);
    }

    @Transactional
    public EnderecoDTO cadastrar(Long candidatoId, EnderecoDTO dto) {
        log.debug("Persistindo endereço para candidatoId={} cidade={} estado={}", candidatoId, dto.getCidade(), dto.getEstado());
        var entity = EnderecoMapper.toEntity(dto, candidatoId);
        var endereco = enderecoRepository.save(entity);

        log.info("Endereço cadastrado com sucesso. candidatoId={} enderecoId={}", candidatoId, endereco.getId());
        return EnderecoMapper.toDTO(endereco);
    }

    public EnderecoDTO buscarPorCandidato(Long candidatoId) {
        log.debug("Buscando endereço do candidatoId={}", candidatoId);
        var endereco = enderecoRepository.findByIdCandidato(candidatoId);
        log.debug("Endereço {}encontrado para candidatoId={}", endereco.isPresent() ? "" : "não ", candidatoId);
        return endereco.map(EnderecoMapper::toDTO)
                        .orElse(null);
    }

    public EnderecoDTO alterar(Long candidatoId, EnderecoDTO enderecoDTO, Jwt jwt) {
        log.info("Iniciando alteração de endereço para candidatoId={}", candidatoId);
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
        log.info("Endereço alterado com sucesso. candidatoId={} enderecoId={}", candidatoId, enderecoAtualizado.getId());
        return EnderecoMapper.toDTO(enderecoAtualizado);
    }

    public void deletar(Long candidatoId, Jwt jwt) {
        log.info("Iniciando exclusão de endereço para candidatoId={}", candidatoId);
        candidatoAuthorizationService.validarAcessoDoUsuario(candidatoId, jwt);
        var endereco = enderecoRepository.findByIdCandidato(candidatoId)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado para o candidato ID: " + candidatoId));

        enderecoRepository.delete(endereco);
        log.info("Endereço excluído com sucesso. candidatoId={} enderecoId={}", candidatoId, endereco.getId());
    }
}

