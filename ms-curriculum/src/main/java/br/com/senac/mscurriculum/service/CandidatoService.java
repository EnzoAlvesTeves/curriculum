package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.AlterarCandidatoRequest;
import br.com.senac.mscurriculum.dto.CandidatoDTO;
import br.com.senac.mscurriculum.dto.UsuarioMeResponse;
import br.com.senac.mscurriculum.repository.CandidatoRepository;
import br.com.senac.mscurriculum.repository.entity.CandidatoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CandidatoService {

    private final CandidatoRepository candidatoRepository;
    private final CandidatoAuthorizationService authorizationService;
    private final EnderecoService enderecoService;
    private final HabilidadeService habilidadeService;
    private final ExperienciaService experienciaService;
    private final EducacaoService educacaoService;

    @Transactional
    public CandidatoDTO cadastrar(CandidatoDTO request, Jwt jwt) {
        UsuarioMeResponse usuario = authorizationService.validarTipoCandidato(jwt);

        if (candidatoRepository.findByIdUsuario(usuario.id()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Candidato já cadastrado para este usuário");
        }

        CandidatoEntity candidato = new CandidatoEntity();
        candidato.setIdUsuario(usuario.id());
        candidato.setNome(request.getNome());
        candidato.setEmail(request.getEmail());
        candidato.setSexo(request.getSexo());
        candidato.setTelefone(request.getTelefone());
        candidato.setDataNascimento(request.getDataNascimento());
        candidato.setResumoProfissional(request.getResumoProfissional());

        var candidatoCadastrado = candidatoRepository.save(candidato);

        enderecoService.cadastrar(candidatoCadastrado.getId(), request.getEndereco());

        request.getHabilidades().forEach(habilidadeDTO -> {
            habilidadeService.cadastrar(candidatoCadastrado.getId(), habilidadeDTO);
        });

        request.getExperiencias().forEach(experienciaDTO -> {
            experienciaService.cadastrar(candidatoCadastrado.getId(), experienciaDTO);
        });

        request.getEducacoes().forEach(educacaoDTO -> {
            educacaoService.cadastrar(candidatoCadastrado.getId(), educacaoDTO);
        });

        return carregarCandidatoCompleto(candidatoCadastrado.getId());
    }

    @Transactional
    public CandidatoDTO alterar(AlterarCandidatoRequest request, Jwt jwt) {
        var candidato = authorizationService.validarAcessoDoUsuario(request.getId(), jwt);

        candidato.setNome(request.getNome());
        candidato.setEmail(request.getEmail());
        candidato.setSexo(request.getSexo());
        candidato.setTelefone(request.getTelefone());
        candidato.setDataNascimento(request.getDataNascimento());
        candidato.setResumoProfissional(request.getResumoProfissional());

        var candidatoAlterado = candidatoRepository.save(candidato);

        return carregarCandidatoCompleto(candidatoAlterado.getId());
    }

    @Transactional(readOnly = true)
    public CandidatoDTO buscarPorId(Long candidatoId) {
        return carregarCandidatoCompleto(candidatoId);
    }

    private CandidatoDTO carregarCandidatoCompleto(Long candidatoId) {
        CandidatoEntity candidato = candidatoRepository.findById(candidatoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidato não encontrado"));

        CandidatoDTO dto = new CandidatoDTO();
        dto.setId(candidato.getId());
        dto.setIdUsuario(candidato.getIdUsuario());
        dto.setNome(candidato.getNome());
        dto.setEmail(candidato.getEmail());
        dto.setSexo(candidato.getSexo());
        dto.setTelefone(candidato.getTelefone());
        dto.setDataNascimento(candidato.getDataNascimento());
        dto.setResumoProfissional(candidato.getResumoProfissional());
        dto.setEndereco(enderecoService.buscarPorCandidato(candidato.getId()));
        dto.setHabilidades(habilidadeService.buscarPorCandidato(candidato.getId()));
        dto.setExperiencias(experienciaService.buscarPorCandidato(candidato.getId()));
        dto.setEducacoes(educacaoService.buscarPorCandidato(candidato.getId()));

        return dto;
    }

    @Transactional
    public void deletar(Jwt jwt) {
        var usuario = authorizationService.usuarioAtual(jwt);
        var candidato = candidatoRepository.findByIdUsuario(usuario.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidato não encontrado"));

        authorizationService.validarAcessoDoUsuario(candidato.getId(), jwt);

        candidatoRepository.deleteById(candidato.getId());
    }
}
