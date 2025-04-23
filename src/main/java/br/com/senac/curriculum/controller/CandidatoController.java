package br.com.senac.curriculum.controller;


import br.com.senac.curriculum.dto.*;
import br.com.senac.curriculum.repository.candidato.CandidatoEntity;
import br.com.senac.curriculum.repository.candidato.CandidatoRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "/candidato")
public class CandidatoController {

	private CandidatoRepository candidatoRepository;

	CandidatoController(CandidatoRepository candidatoRepository) {
		this.candidatoRepository =  candidatoRepository;
	}

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

}
