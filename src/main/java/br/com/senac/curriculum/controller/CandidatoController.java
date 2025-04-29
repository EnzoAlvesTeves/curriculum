package br.com.senac.curriculum.controller;


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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/candidato")
public class CandidatoController {

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

	@GetMapping("/{id}")
	public CandidatoDTO buscar(@PathVariable Long id) {
		Optional<CandidatoEntity> candidatoEntity = candidatoRepository.findById(id);
		CandidatoEntity candidato = candidatoEntity.get();

		CandidatoDTO candidatoDTO = new CandidatoDTO();
		EnderecoDTO enderecoDTO = new EnderecoDTO();

		candidato.getExperiencias().forEach(experiencia -> {
			ExperienciaDTO experienciaDTO = new ExperienciaDTO();
			experienciaDTO.setId(experiencia.getId());
			experienciaDTO.setCargo(experiencia.getCargo());
			experienciaDTO.setEmpresa(experiencia.getEmpresa());
			experienciaDTO.setDataInicio(experiencia.getDataInicio());
			experienciaDTO.setDataFim(experiencia.getDataFim());

			candidatoDTO.getExperiencias().add(experienciaDTO);
		});

		candidato.getEducacoes().forEach(educacao -> {
			EducacaoDTO educacaoDTO = new EducacaoDTO();
			educacaoDTO.setId(educacao.getId());
			educacaoDTO.setGrau(educacao.getGrau());
			educacaoDTO.setDataInicio(educacao.getDataInicio());
			educacaoDTO.setDataFim(educacao.getDataFim());
			educacaoDTO.setInstituicao(educacao.getInstituicao());
			educacaoDTO.setCurso(educacao.getCurso());

			candidatoDTO.getEducacoes().add(educacaoDTO);
		});

		candidato.getHabilidades().forEach(habilidade -> {
			HabilidadeDTO habilidadeDTO = new HabilidadeDTO();
			habilidadeDTO.setId(habilidade.getId());
			habilidadeDTO.setDescricao(habilidade.getDescricao());
			habilidadeDTO.setNivel(habilidade.getNivel());
			habilidadeDTO.setEspecialidade(habilidade.getEspecialidade());

			candidatoDTO.getHabilidades().add(habilidadeDTO);
		});

		enderecoDTO.setId(candidato.getEndereco().getId());
		enderecoDTO.setRua(candidato.getEndereco().getRua());
		enderecoDTO.setNumero(candidato.getEndereco().getNumero());
		enderecoDTO.setComplemento(candidato.getEndereco().getComplemento());
		enderecoDTO.setCidade(candidato.getEndereco().getCidade());
		enderecoDTO.setEstado(candidato.getEndereco().getEstado());
		enderecoDTO.setCep(candidato.getEndereco().getCep());

		candidatoDTO.setId(candidato.getId());
		candidatoDTO.setNome(candidato.getNome());
		candidatoDTO.setEmail(candidato.getEmail());
		candidatoDTO.setSexo(candidato.getSexo());
		candidatoDTO.setTelefone(candidato.getTelefone());
		candidatoDTO.setDataNascimento(candidato.getDataNascimento());
		candidatoDTO.setEndereco(enderecoDTO);

		return candidatoDTO;

	}

	@PostMapping
	public CandidatoDTO cadastrar(@RequestBody CandidatoDTO candidatoDTO){
		EnderecoEntity endereco = new EnderecoEntity();
		endereco.setRua(candidatoDTO.getEndereco().getRua());
		endereco.setNumero(candidatoDTO.getEndereco().getNumero());
		endereco.setComplemento(candidatoDTO.getEndereco().getComplemento());
		endereco.setCidade(candidatoDTO.getEndereco().getCidade());
		endereco.setEstado(candidatoDTO.getEndereco().getEstado());
		endereco.setCep(candidatoDTO.getEndereco().getCep());

		endereco = enderecoRepository.save(endereco);

		CandidatoEntity candidato = new CandidatoEntity();
		candidato.setNome(candidatoDTO.getNome());
		candidato.setEmail(candidatoDTO.getEmail());
		candidato.setSexo(candidatoDTO.getSexo());
		candidato.setTelefone(candidatoDTO.getTelefone());
		candidato.setDataNascimento(candidatoDTO.getDataNascimento());
		candidato.setEndereco(endereco);

		final CandidatoEntity novoCandidato = candidatoRepository.save(candidato);

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

			habilidade = habilidadeRepository.save(habilidade);
			habilidadeDTO.setId(habilidade.getId());
		});

		return new CandidatoDTO(candidato);
	}
}

