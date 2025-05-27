package br.com.senac.curriculum.service;

import br.com.senac.curriculum.controller.CandidatoController;
import br.com.senac.curriculum.dto.*;
import br.com.senac.curriculum.repository.candidato.CandidatoEntity;
import br.com.senac.curriculum.repository.candidato.CandidatoRepository;
import br.com.senac.curriculum.repository.educacao.EducacaoEntity;
import br.com.senac.curriculum.repository.educacao.EducacaoRepository;
import br.com.senac.curriculum.repository.endereco.EnderecoEntity;
import br.com.senac.curriculum.repository.endereco.EnderecoRepository;
import br.com.senac.curriculum.repository.experiencia.ExperienciaEntity;
import br.com.senac.curriculum.repository.experiencia.ExperienciaRepository;
import br.com.senac.curriculum.repository.habilidade.HabilidadeEntity;
import br.com.senac.curriculum.repository.habilidade.HabilidadeRepository;
import br.com.senac.curriculum.repository.usuario.UsuarioEntity;
import br.com.senac.curriculum.repository.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CandidatoService {

    @Autowired
    private CandidatoRepository candidatoRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private EducacaoRepository educacaoRepository;

    @Autowired
    private ExperienciaRepository experienciaRepository;

    @Autowired
    private HabilidadeRepository habilidadeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public CandidatoDTO cadastrar(CandidatoDTO candidatoDTO) {
        UsuarioEntity usuario = usuarioRepository.findById(candidatoDTO.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Optional<CandidatoEntity> candidatoExistente = candidatoRepository.findByUsuario(usuario);
        if (candidatoExistente.isPresent()) {
            throw new RuntimeException("Candidato já cadastrado para o usuário: " + usuario.getId());
        }

        EnderecoEntity endereco = new EnderecoEntity();
        endereco.setRua(candidatoDTO.getEndereco().getRua());
        endereco.setNumero(candidatoDTO.getEndereco().getNumero());
        endereco.setComplemento(candidatoDTO.getEndereco().getComplemento());
        endereco.setCidade(candidatoDTO.getEndereco().getCidade());
        endereco.setEstado(candidatoDTO.getEndereco().getEstado());
        endereco.setCep(candidatoDTO.getEndereco().getCep());
        endereco.setBairro(candidatoDTO.getEndereco().getBairro());

        endereco = enderecoRepository.save(endereco);
        candidatoDTO.getEndereco().setId(endereco.getId());

        CandidatoEntity candidato = new CandidatoEntity();
        candidato.setUsuario(usuario);
        candidato.setNome(candidatoDTO.getNome());
        candidato.setEmail(candidatoDTO.getEmail());
        candidato.setSexo(candidatoDTO.getSexo());
        candidato.setTelefone(candidatoDTO.getTelefone());
        candidato.setDataNascimento(candidatoDTO.getDataNascimento());
        candidato.setResumoProfissional(candidatoDTO.getResumoProfissional());
        candidato.setEndereco(endereco);

        final CandidatoEntity novoCandidato = candidatoRepository.save(candidato);
        candidatoDTO.setId(novoCandidato.getId());

        candidatoDTO.getEducacoes().forEach(educacaoDTO -> {
            EducacaoEntity educacao = new EducacaoEntity();
            educacao.setGrau(educacaoDTO.getGrau());
            educacao.setDataInicio(educacaoDTO.getDataInicio());
            educacao.setDataFim(educacaoDTO.getDataFim());
            educacao.setInstituicao(educacaoDTO.getInstituicao());
            educacao.setCurso(educacaoDTO.getCurso());
            educacao.setCandidato(novoCandidato);

            educacao = educacaoRepository.save(educacao);
            educacaoDTO.setId(educacao.getId());
        });

        candidatoDTO.getExperiencias().forEach(experienciaDTO -> {
            ExperienciaEntity experiencia = new ExperienciaEntity();
            experiencia.setCargo(experienciaDTO.getCargo());
            experiencia.setEmpresa(experienciaDTO.getEmpresa());
            experiencia.setDataInicio(experienciaDTO.getDataInicio());
            experiencia.setDataFim(experienciaDTO.getDataFim());
            experiencia.setCandidato(novoCandidato);

            experiencia = experienciaRepository.save(experiencia);
            experienciaDTO.setId(experiencia.getId());
        });

        candidatoDTO.getHabilidades().forEach(habilidadeDTO -> {
            HabilidadeEntity habilidade = new HabilidadeEntity();
            habilidade.setDescricao(habilidadeDTO.getDescricao());
            habilidade.setNivel(habilidadeDTO.getNivel());
            habilidade.setEspecialidade(habilidadeDTO.getEspecialidade());
            habilidade.setCandidato(novoCandidato);

            habilidade = habilidadeRepository.save(habilidade);
            habilidadeDTO.setId(habilidade.getId());
        });

        return candidatoDTO;
    }

    public CandidatoDTO buscarPorId(Long id) {
        Optional<CandidatoEntity> candidatoEntity = candidatoRepository.findById(id);

        if (candidatoEntity.isEmpty()) {
            throw new RuntimeException("Candidato não encontrado com o ID: " + id);
        }

        CandidatoDTO candidatoDTO = new CandidatoDTO(candidatoEntity.get());
        return candidatoDTO;
    }

    public List<CandidatoDTO> listarTodos() {
        return candidatoRepository.findAll()
                .stream()
                .map(CandidatoDTO::new)
                .toList();
    }

    @Transactional
    public CandidatoDTO editar(CandidatoDTO candidatoDTO) {
        Optional<CandidatoEntity> candidatoAtual = candidatoRepository.findById(candidatoDTO.getId());
        if (candidatoAtual.isEmpty()) {
            throw new RuntimeException("Candidato não encontrado com o ID: " + candidatoDTO.getId());
        }

        Optional<EnderecoEntity> enderecoAtual = enderecoRepository.findById(candidatoDTO.getEndereco().getId());
        if (enderecoAtual.isEmpty()) {
            throw new RuntimeException("Endereço não encontrado com o ID: " + candidatoDTO.getEndereco().getId());
        }

        EnderecoEntity endereco = enderecoAtual.get();
        endereco.setRua(candidatoDTO.getEndereco().getRua());
        endereco.setNumero(candidatoDTO.getEndereco().getNumero());
        endereco.setComplemento(candidatoDTO.getEndereco().getComplemento());
        endereco.setCidade(candidatoDTO.getEndereco().getCidade());
        endereco.setEstado(candidatoDTO.getEndereco().getEstado());
        endereco.setCep(candidatoDTO.getEndereco().getCep());
        endereco.setBairro(candidatoDTO.getEndereco().getBairro());

        enderecoRepository.save(endereco);

        CandidatoEntity candidato = candidatoAtual.get();
        candidato.setNome(candidatoDTO.getNome());
        candidato.setEmail(candidatoDTO.getEmail());
        candidato.setSexo(candidatoDTO.getSexo());
        candidato.setTelefone(candidatoDTO.getTelefone());
        candidato.setDataNascimento(candidatoDTO.getDataNascimento());
        candidato.setResumoProfissional(candidatoDTO.getResumoProfissional());

        candidatoDTO.getEducacoes().forEach(educacaoDTO -> {
            EducacaoEntity educacao = new EducacaoEntity();
            if (educacaoDTO.getId() != null) {
                Optional<EducacaoEntity> educacaoExistente = educacaoRepository.findById(educacaoDTO.getId());
                if (educacaoExistente.isEmpty()) {
                    throw new RuntimeException("Educação não existe: " + educacaoDTO.getId());
                }
                educacao = educacaoExistente.get();
            }

            educacao.setGrau(educacaoDTO.getGrau());
            educacao.setDataInicio(educacaoDTO.getDataInicio());
            educacao.setDataFim(educacaoDTO.getDataFim());
            educacao.setInstituicao(educacaoDTO.getInstituicao());
            educacao.setCurso(educacaoDTO.getCurso());
            educacao.setCandidato(candidato);

            educacaoRepository.save(educacao);
        });

        candidatoDTO.getExperiencias().forEach(experienciaDTO -> {
            ExperienciaEntity experiencia = new ExperienciaEntity();
            if (experienciaDTO.getId() != null) {
                Optional<ExperienciaEntity> experienciaExistente = experienciaRepository.findById(experienciaDTO.getId());
                if (experienciaExistente.isEmpty()) {
                    throw new RuntimeException("Experiência não existe: " + experienciaDTO.getId());
                }
                experiencia = experienciaExistente.get();
            }

            experiencia.setCargo(experienciaDTO.getCargo());
            experiencia.setEmpresa(experienciaDTO.getEmpresa());
            experiencia.setDataInicio(experienciaDTO.getDataInicio());
            experiencia.setDataFim(experienciaDTO.getDataFim());
            experiencia.setCandidato(candidato);

            experienciaRepository.save(experiencia);
        });

        candidatoDTO.getHabilidades().forEach(habilidadeDTO -> {
            HabilidadeEntity habilidade = new HabilidadeEntity();
            if (habilidadeDTO.getId() != null) {
                Optional<HabilidadeEntity> habilidadeExistente = habilidadeRepository.findById(habilidadeDTO.getId());
                if (habilidadeExistente.isEmpty()) {
                    throw new RuntimeException("Habilidade não existe: " + habilidadeDTO.getId());
                }
                habilidade = habilidadeExistente.get();
            }

            habilidade.setDescricao(habilidadeDTO.getDescricao());
            habilidade.setNivel(habilidadeDTO.getNivel());
            habilidade.setEspecialidade(habilidadeDTO.getEspecialidade());
            habilidade.setCandidato(candidato);

            habilidadeRepository.save(habilidade);
        });

        return candidatoDTO;
    }
}
