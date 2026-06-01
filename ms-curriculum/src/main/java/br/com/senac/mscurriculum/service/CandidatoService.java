package br.com.senac.mscurriculum.service;

import br.com.senac.mscurriculum.dto.AlterarCandidatoRequest;
import br.com.senac.mscurriculum.dto.CandidatoDTO;
import br.com.senac.mscurriculum.dto.UsuarioMeResponse;
import br.com.senac.mscurriculum.repository.CandidatoRepository;
import br.com.senac.mscurriculum.repository.entity.CandidatoEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
@Slf4j
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
        log.info("Iniciando cadastro de candidato para o usuário autenticado");
        UsuarioMeResponse usuario = authorizationService.validarTipoCandidato(jwt);

        if (candidatoRepository.findByIdUsuario(usuario.id()).isPresent()) {
            log.warn("Tentativa de cadastro duplicado de candidato para o usuário ID {}", usuario.id());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Candidato já cadastrado para este usuário");
        }

        log.debug("Montando candidato com nome={} e email={}", request.getNome(), request.getEmail());
        CandidatoEntity candidato = new CandidatoEntity();
        candidato.setIdUsuario(usuario.id());
        candidato.setNome(request.getNome());
        candidato.setEmail(request.getEmail());
        candidato.setSexo(request.getSexo());
        candidato.setTelefone(request.getTelefone());
        candidato.setDataNascimento(request.getDataNascimento());
        candidato.setResumoProfissional(request.getResumoProfissional());

        var candidatoCadastrado = candidatoRepository.save(candidato);
        log.info("Candidato cadastrado com sucesso. candidatoId={} usuarioId={}", candidatoCadastrado.getId(), usuario.id());

        if (Objects.nonNull(request.getEndereco())) {
            log.debug("Cadastrando endereço do candidatoId={}", candidatoCadastrado.getId());
            enderecoService.cadastrar(candidatoCadastrado.getId(), request.getEndereco());
        }

        if (Objects.nonNull(request.getHabilidades())) {
            log.debug("Cadastrando {} habilidade(s) do candidatoId={}", request.getHabilidades().size(), candidatoCadastrado.getId());
            request.getHabilidades().forEach(habilidadeDTO -> habilidadeService.cadastrar(candidatoCadastrado.getId(), habilidadeDTO));
        }

        if (Objects.nonNull(request.getExperiencias())) {
            log.debug("Cadastrando {} experiência(s) do candidatoId={}", request.getExperiencias().size(), candidatoCadastrado.getId());
            request.getExperiencias().forEach(experienciaDTO -> experienciaService.cadastrar(candidatoCadastrado.getId(), experienciaDTO));
        }

        if (Objects.nonNull(request.getEducacoes())) {
            log.debug("Cadastrando {} educação(ões) do candidatoId={}", request.getEducacoes().size(), candidatoCadastrado.getId());
            request.getEducacoes().forEach(educacaoDTO -> educacaoService.cadastrar(candidatoCadastrado.getId(), educacaoDTO));
        }

        return carregarCandidatoCompleto(candidatoCadastrado.getId());
    }

    @Transactional
    public CandidatoDTO alterar(AlterarCandidatoRequest request, Jwt jwt) {
        log.info("Iniciando alteração do candidato ID={}", request.getId());
        var candidato = authorizationService.validarAcessoDoUsuario(request.getId(), jwt);

        candidato.setNome(request.getNome());
        candidato.setEmail(request.getEmail());
        candidato.setSexo(request.getSexo());
        candidato.setTelefone(request.getTelefone());
        candidato.setDataNascimento(request.getDataNascimento());
        candidato.setResumoProfissional(request.getResumoProfissional());

        var candidatoAlterado = candidatoRepository.save(candidato);
        log.info("Candidato alterado com sucesso. candidatoId={}", candidatoAlterado.getId());

        return carregarCandidatoCompleto(candidatoAlterado.getId());
    }

    @Transactional(readOnly = true)
    public CandidatoDTO buscarPorId(Long candidatoId) {
        log.debug("Buscando candidato completo por ID={}", candidatoId);
        return carregarCandidatoCompleto(candidatoId);
    }

    private CandidatoDTO carregarCandidatoCompleto(Long candidatoId) {
        log.debug("Carregando candidato completo e relacionamentos. candidatoId={}", candidatoId);
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

        log.debug("Candidato completo carregado com {} habilidade(s), {} experiência(s) e {} educação(ões)",
                dto.getHabilidades() != null ? dto.getHabilidades().size() : 0,
                dto.getExperiencias() != null ? dto.getExperiencias().size() : 0,
                dto.getEducacoes() != null ? dto.getEducacoes().size() : 0);

        return dto;
    }

    @Transactional
    public void deletar(Jwt jwt) {
        log.info("Iniciando exclusão do candidato autenticado");
        var usuario = authorizationService.usuarioAtual(jwt);
        var candidato = candidatoRepository.findByIdUsuario(usuario.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidato não encontrado"));

        authorizationService.validarAcessoDoUsuario(candidato.getId(), jwt);

        candidatoRepository.deleteById(candidato.getId());
        log.info("Candidato excluído com sucesso. candidatoId={} usuarioId={}", candidato.getId(), usuario.id());
    }

    public CandidatoDTO me(Jwt jwt) {
        log.info("Buscando candidato autenticado");
        var usuario = authorizationService.usuarioAtual(jwt);
        var candidato = candidatoRepository.findByIdUsuario(usuario.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidato não encontrado"));

        log.debug("Candidato autenticado encontrado. candidatoId={} usuarioId={}", candidato.getId(), usuario.id());
        return carregarCandidatoCompleto(candidato.getId());
    }
}
